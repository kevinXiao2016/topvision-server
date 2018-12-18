/***********************************************************************
 * $Id: OltMonitorJob.java,v1.0 2011-10-11 涓嬪崍09:38:04 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.job;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.performance.facade.CC8800ASystemTimeFacade;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.job.MonitorJob;
import com.topvision.ems.performance.service.MonitorService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.telnet.TelnetParam;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Victor
 * @created @2011-10-11-上午09:38:04
 * 
 */
public class OltMonitorJob extends MonitorJob {
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        if (logger.isDebugEnabled()) {
            logger.debug("OltMonitorJob start...");
        }
        BeanFactory beanFactory = (BeanFactory) jobDataMap.get("beanFactory");
        OltService oltService = (OltService) beanFactory.getBean("oltService");
        OnuService onuService = (OnuService) beanFactory.getBean("onuService");
        EntityService entityService = (EntityService) beanFactory.getBean("entityService");
        MonitorService monitorService = (MonitorService) beanFactory.getBean("monitorService");
        MessageService messageService = (MessageService) beanFactory.getBean("messageService");
        Long entityId = entityService.getEntityIdByIp(monitor.getIp());
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);

        monitor.setLastCollectTime(new Timestamp(System.currentTimeMillis()));
        OltPerfFacade facade = facadeFactory.getFacade(monitor.getIp(), OltPerfFacade.class);
        //OltFacade oltFacade = facadeFactory.getFacade(monitor.getIp(), OltFacade.class);
        /*
         * List<MonitorType> monitorTypes = oltService.getMonitorTypes();
         * 
         * for (MonitorType type : monitorTypes) { try { Class c =
         * Class.forName("com.topvision.ems.epon.facade.domain.OltSlotAttribute");
         * java.lang.reflect.Field field = c.getDeclaredField(type.getFieldText()); Class c2 =
         * field.getType(); if (c2.getName().equals("java.lang.Long")) { Long value =
         * Long.parseLong(oltControlFacade.getValueByOid(param, type.getFieldOid()));
         * oltService.updateMonitorValue(type.getFieldTable(), type.getFieldText(), (Object)value,
         * type.getPrimaryKey(), type.getPrimaryKeyValue()); }
         * 
         * } catch (Exception e) { // TODO: handle exception logger.debug(e.toString()); } }
         */
        OltAttribute oltAttri = new OltAttribute();
        Long oltSysUpTime = 0L;
        Map<String, String> onuRegi = new HashMap<String, String>();
        try {
            // 获取OLT的基本信息 
            oltAttri = facade.getOltAttribute(param);
            // 获取OLT的在线时长 
            oltSysUpTime = Long.parseLong(facade.getOltSysUpTime(param));
            // 获取ONU的注册时间 
            onuRegi = facade.getOnuRegister(param);
        } catch (Exception e) {
            // 此处很有可能出现设备已经不能连接 仍然执行monitor的问题
            logger.error("", e);
            return;
        }
        oltAttri.setEntityId(entityId);
        // 更新OLT的基本信息 
        oltService.updateOltAttribute(oltAttri);
        // 更新OLT的在线时长 
        oltService.addOltDeviceUpTime(entityId, oltSysUpTime);
        // 更新ONU的注册时间 
        onuService.addOnuDeviceUpTime(entityId, onuRegi);
        /////////////////////////////////////////////////////////////////////////////////////////
        //此代码块通过Telnet访问CC的启动时间来判断CC是重新注册还是重启，此功能为内部功能，客户不可见
        if (jobDataMap.containsKey("telnetFilter") && jobDataMap.getBooleanValueFromString("telnetFilter")) {
            List<OltOnuAttribute> onus = onuService.getOnuListByEntity(entityId);
            TelnetParam telnetParam = new TelnetParam();
            telnetParam.setIpAddress(param.getIpAddress());
            telnetParam.setPassword("topvision");
            telnetParam.setEnablePasswd("topvision");
            //OLT动态密码
            Calendar today = Calendar.getInstance();
            int year = today.get(Calendar.YEAR), mnth = today.get(Calendar.MONTH) + 1, day = today
                    .get(Calendar.DAY_OF_MONTH);
            year = year * 17;
            mnth = (mnth + 12) * 19;
            day = day + 23;
            telnetParam.setSuperPasswd(String.valueOf(year * 1000 + mnth * 100 + day * (day + 29) * 341 - 7));
            for (OltOnuAttribute onu : onus) {
                if (onu.getOnuPreType() != 241) {
                    continue;
                }
                telnetParam.put(new MacUtils(onu.getOnuMac()).toString(), null);
            }
            if (telnetParam.attributes() != null && !telnetParam.attributes().isEmpty()) {
                @SuppressWarnings("unchecked")
                CC8800ASystemTimeFacade<TelnetParam> ccSystemTimeFacade = facadeFactory.getFacade(monitor.getIp(),
                        CC8800ASystemTimeFacade.class);
                Map<String, Long> times = ccSystemTimeFacade.getUptime(telnetParam);
                if (logger.isDebugEnabled()) {
                    logger.debug("OltMonitorJob.times:{}", times);
                }
                Map<String, Long> attrs = new HashMap<String, Long>();
                for (OltOnuAttribute onu : onus) {
                    String mac = new MacUtils(onu.getOnuMac()).toString();
                    if (onu.getOnuPreType() != 241 || !times.containsKey(mac) || times.get(mac) == null) {
                        continue;
                    }
                    Long uptime = System.currentTimeMillis() - times.get(mac);
                    attrs.put("entityId", entityId);
                    attrs.put("uptime", uptime);
                    attrs.put("onuId", onu.getOnuIndex());
                    if (logger.isDebugEnabled()) {
                        logger.debug("OltMonitorJob.attrs:{}", attrs);
                    }
                    onuService.updateCC8800ARestartTime(attrs);
                }
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////
        List<OltSlotStatus> status = facade.getOltSlotStatus(param);
        if (logger.isDebugEnabled()) {
            logger.debug("OltMonitorJob.OltSlotStatus:{}", status);
        }
        for (OltSlotStatus s : status) {
            // 9和10为主控板，如果attribute为1表示为主板，2为备板
            // TODO 需要确认
            // if ((s.getSlotNo() == 9 || s.getSlotNo() == 10) && s.getAttribute() == 1) {
            if (s.getAttribute() == 1) {
                EntitySnap snap = new EntitySnap();
                snap.setCpu(s.getTopSysBdCpuUseRatio().doubleValue() / 100);
                snap.setUsedMem(s.getTopSysBdlMemSize().doubleValue() - s.getTopSysBdFreeMemSize().doubleValue());
                if (s.getTopSysBdlMemSize().doubleValue() != 0) {
                    snap.setMem((s.getTopSysBdlMemSize().doubleValue() - s.getTopSysBdFreeMemSize().doubleValue())
                            / s.getTopSysBdlMemSize().doubleValue());
                } else {
                    snap.setMem(0D);
                }
                snap.setVmem(s.getTopSysBdFreeFlashOctets().doubleValue());
                snap.setUsedDisk(s.getTopTotalFlashOctets().doubleValue()
                        - s.getTopSysBdFreeFlashOctets().doubleValue());
                if (s.getTopTotalFlashOctets().doubleValue() != 0) {
                    snap.setDisk((s.getTopTotalFlashOctets().doubleValue() - s.getTopSysBdFreeFlashOctets()
                            .doubleValue()) / s.getTopTotalFlashOctets().doubleValue());
                } else {
                    snap.setDisk(0D);
                }
                snap.setDiskio(s.getTopTotalFlashOctets().doubleValue());
                snap.setSnapTimeMillis(System.currentTimeMillis());
                monitorService.updateSnapPerf(monitor.getIp(), snap);
                EntityValueEvent event = new EntityValueEvent(entityId);
                event.setEntityId(entityId);
                event.setCpu(snap.getCpu());
                event.setMem(snap.getMem());
                event.setVmem(snap.getVmem());
                event.setDisk(snap.getDisk());

                // modify by @bravin : 不能使用采集时间作为设备启动时长，而且值是一个long型的，为了兼顾CCMTS以及老代码，所以讲long转String
                // event.setSysUpTime(sdf.format(snap.getSnapTime()));
                if (oltSysUpTime != null) {
                    event.setSysUpTime(oltSysUpTime.toString());
                    //modify by lzt
                    //在线时间正常设备state设置为true
                    event.setState(true);
                }
                event.setActionName("performanceChanged");
                event.setListener(EntityValueListener.class);
                messageService.addMessage(event);
                break;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("OltMonitorJob end...");
        }
    }
}
