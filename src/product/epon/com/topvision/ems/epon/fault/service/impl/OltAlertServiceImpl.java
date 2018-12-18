/***********************************************************************
 * $Id: OltAlertServiceImpl.java,v1.0 2013-10-26 上午09:52:10 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.OltAlertInstance;
import com.topvision.ems.epon.domain.OltAlertTop;
import com.topvision.ems.epon.exception.TrapConfigInconsistentValueException;
import com.topvision.ems.epon.fault.dao.EponTrapCodeDao;
import com.topvision.ems.epon.fault.dao.OltAlertDao;
import com.topvision.ems.epon.fault.domain.EponEventLog;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.epon.fault.facade.OltAlertFacade;
import com.topvision.ems.epon.fault.service.OltAlertService;
import com.topvision.ems.epon.fault.trap.OltTrapParser;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.dao.EventDao;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:52:10
 * 
 */
@Service("oltAlertService")
public class OltAlertServiceImpl extends BaseService implements OltAlertService, SynchronizedListener, EntityListener {
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private OltAlertDao oltAlertDao;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EponTrapCodeDao eponTrapCodeDao;
    @Autowired
    private EventDao eventDao;

    @Autowired
    private OltTrapParser oltTrapParser;
    // OLT设备告警类型电信、广电、公司内部ID冲突，定义类型区别不同领域，通过配置文件指定当前设备所支持领域
    @Value("${fault.type}")
    private String type;

    @Override
    public List<OltTopAlarmCodeMask> getOltAlertCodeMask(Long entityId) {
        return oltAlertDao.getOltAlertCodeMask(entityId, Integer.parseInt(type));
    }

    @Override
    public List<AlertType> getOltAlertAvailableType(Long entityId) {
        return oltAlertDao.getOltAlertAvailableType(entityId, Integer.parseInt(type));
    }

    @Override
    public void addOltAlertCodeMask(OltTopAlarmCodeMask oltTopAlarmCodeMask) {
        // 向设备上添加OLT告警类型屏蔽规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltTopAlarmCodeMask.getEntityId());
        OltTopAlarmCodeMask newCodeMask = getOltAlertFacade(snmpParam.getIpAddress()).addOltAlertCodeMask(snmpParam,
                oltTopAlarmCodeMask);
        // 添加成功后，插入数据库。
        oltAlertDao.insertOltAlertCodeMask(newCodeMask);
    }

    @Override
    public void deleteOltAlertCodeMask(Long entityId, Long codeMaskIndex) {
        // 删除设备上OLT告警类型屏蔽规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltTopAlarmCodeMask oltTopAlarmCodeMask = new OltTopAlarmCodeMask();
        oltTopAlarmCodeMask.setEntityId(entityId);
        oltTopAlarmCodeMask.setTopAlarmCodeMaskIndex(codeMaskIndex);
        getOltAlertFacade(snmpParam.getIpAddress()).deleteOltAlertCodeMask(snmpParam, oltTopAlarmCodeMask);
        // 修改成功后，删除数据库中规则。
        oltAlertDao.deleteOltAlertCodeMask(entityId, codeMaskIndex);
    }

    @Override
    public List<OltTopAlarmInstanceMask> getOltAlertInstanceMask(Long entityId) {
        List<OltTopAlarmInstanceMask> instanceMasks = oltAlertDao.getOltAlertInstanceMask(entityId);
        for (OltTopAlarmInstanceMask instanceMask : instanceMasks) {
            instanceMask.setSlotNo(oltSlotDao.getSlotNoByIndex(instanceMask.getTopAlarmInstanceMaskIndex(), entityId));
        }
        return instanceMasks;
    }

    @Override
    public List<OltAlertInstance> getOltAlertAvailableInstance(Long entityId) {
        List<OltAlertInstance> instances = oltAlertDao.getOltAlertAvailableInstance(entityId);
        for (OltAlertInstance instance : instances) {
            if ("slot".equals(instance.getInstanceType())) {
                instance.setSlotNo(oltSlotDao.getSlotNoByIndex(instance.getInstanceIndex(), entityId));
            }
        }
        return instances;
    }

    @Override
    public void addOltAlertInstanceMask(OltTopAlarmInstanceMask oltTopAlarmInstanceMask) {
        // 向设备上添加OLT告警实体屏蔽规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltTopAlarmInstanceMask.getEntityId());
        OltTopAlarmInstanceMask newInstanceMask = getOltAlertFacade(snmpParam.getIpAddress()).addOltAlertInstanceMask(
                snmpParam, oltTopAlarmInstanceMask);
        // 添加成功后，插入数据库。
        oltAlertDao.insertOltAlertInstanceMask(newInstanceMask);
    }

    @Override
    public void deleteOltAlertInstanceMask(Long entityId, Long instanceMaskIndex) {
        // 删除设备上OLT告警实体屏蔽规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltTopAlarmInstanceMask oltTopAlarmInstanceMask = new OltTopAlarmInstanceMask();
        oltTopAlarmInstanceMask.setEntityId(entityId);
        oltTopAlarmInstanceMask.setTopAlarmInstanceMaskIndex5Byte(instanceMaskIndex);
        getOltAlertFacade(snmpParam.getIpAddress()).deleteOltAlertInstanceMask(snmpParam, oltTopAlarmInstanceMask);
        // 删除成功后，删除数据库中规则。
        oltAlertDao.deleteOltAlertInstanceMask(entityId, instanceMaskIndex);
    }

    @Override
    public void refreshMaskDataFromFacility(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 从设备获取告警类型屏蔽信息
        discoveryOltAlertCodeMask(snmpParam);
        discoveryOltAlertInstanceMask(snmpParam);
    }

    @Override
    public List<OltTrapConfig> getOltTrapConfig(Long entityId) {
        return oltAlertDao.getOltTrapConfig(entityId);
    }

    @Override
    public void addOltTrapConfig(OltTrapConfig oltTrapConfig) {
        // 向设备上添加Trap配置
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltTrapConfig.getEntityId());
        OltTrapConfig newTrapConfig = new OltTrapConfig();
        try {
            newTrapConfig = getOltAlertFacade(snmpParam.getIpAddress()).addOltTrapConfig(snmpParam, oltTrapConfig);
        } catch (SnmpException e) {
            if (e.getErrorStatusText().contains("Inconsistent")) {
                // 重复设置
                throw new TrapConfigInconsistentValueException("Business.trapInconsistent");
            } else {
                // 设值异常,重新抛出。
                throw new SnmpException(e);
            }
        }
        // 添加成功后，插入数据库。
        oltAlertDao.insertOltTrapConfig(newTrapConfig);
    }

    @Override
    public void deleteOltTrapConfig(Long entityId, String trapNameString) {
        // 删除设备上Trap配置
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltTrapConfig oltTrapConfig = new OltTrapConfig();
        oltTrapConfig.setEntityId(entityId);
        oltTrapConfig.setEponManagementAddrName(trapNameString);
        // 删除设备上数据时，需要将index和rowstatus之外的值置空。
        oltTrapConfig.setEponManagementAddrTAddress(null);
        getOltAlertFacade(snmpParam.getIpAddress()).deleteOltTrapConfig(snmpParam, oltTrapConfig);
        // 删除成功后，删除数据库中保存数据。
        oltAlertDao.deleteOltTrapConfig(oltTrapConfig);
    }

    @Override
    public void refreshTrapConfigFromFacility(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 从设备获取Trap配置信息
        discoveryOltTrapConfig(snmpParam);
    }

    @Override
    public List<String> getAvailableEntityIp() {
        return oltAlertDao.getAvailableEntityIp();
    }

    @Override
    public List<Level> getAlertSeverity() {
        return oltAlertDao.getAlertSeverity();
    }

    /**
     * 获取olt告警类型,主要是排除ccmts和cmts告警类型
     * 
     * @update by flackyang 2013-11-12
     */
    @Override
    public List<AlertType> getAlertType() {
        List<AlertType> alertTypes = oltAlertDao.getAlertType();
        List<AlertType> oltAlertTypes = new ArrayList<AlertType>();
        List<Integer> specialOnuType = Arrays.asList(AlertType.SPECIAL_CATEGORY_ONU);
        for (AlertType type : alertTypes) {
            if (!specialOnuType.contains(type.getCategory()) && !specialOnuType.contains(type.getTypeId())) {
                oltAlertTypes.add(type);
            }
        }
        return oltAlertTypes;
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());

        try {
            setOltTrapConfig(event.getEntityId());
            logger.info("TrapServerInsert Finish!");
        } catch (Exception e) {
            logger.debug("setOltTrapConfig wrong", e);
        }

        try {
            discoveryOltAlertCodeMask(snmpParam);
            logger.info("refresh discoveryOltAlertCodeMask finished!");
        } catch (Exception e) {
            logger.error("refresh discoveryOltAlertCodeMask error", e);
        }

        try {
            discoveryOltAlertInstanceMask(snmpParam);
            logger.info("refresh discoveryOltAlertCodeMask finished!");
        } catch (Exception e) {
            logger.error("refresh discoveryOltAlertInstanceMask error", e);
        }

        try {
            discoveryOltTrapConfig(snmpParam);
            logger.info("refresh discoveryOltAlertCodeMask finished!");
        } catch (Exception e) {
            logger.error("refresh discoveryOltTrapConfig error", e);
        }

        try {
            // 通过配置文件的告警模式type 转换为支持的字节模式
            Integer eponAlarmCode = EponUtil.getEponAlarmCodeByConfig(type);
            // 初始化设备的时候根据配置文件设置当前设备支持的告警模式
            setTopEponAlarmMode(snmpParam, eponAlarmCode);
            /*
             * String eponAlarmCodeHex = Integer.toHexString(eponAlarmCode); // 可以直接mod
             * 16取余数，但是为了更清楚表达含义，并且保证异常情况得到处理，故采用下列方法 switch
             * (Integer.parseInt(eponAlarmCodeHex.substring(eponAlarmCodeHex.length() - 1))) { case
             * 1: EponCode.setEponCode(1); break; case 2: EponCode.setEponCode(2); break; case 3:
             * EponCode.setEponCode(3); break; default: EponCode.setEponCode(1); break; }
             */
            logger.info("refresh getTopEponAlarmMode finished!");
        } catch (Exception e) {
            logger.error("refresh getTopEponAlarmMode error", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /**
     * 采集OLT告警类型屏蔽数据
     * 
     * @param snmpParam
     */
    private void discoveryOltAlertCodeMask(SnmpParam snmpParam) {
        List<OltTopAlarmCodeMask> oltTopAlarmCodeMaskList = getOltAlertFacade(snmpParam.getIpAddress())
                .getOltAlertCodeMask(snmpParam);
        if (oltTopAlarmCodeMaskList != null) {
            for (OltTopAlarmCodeMask codeMask : oltTopAlarmCodeMaskList) {
                codeMask.setEntityId(snmpParam.getEntityId());
            }
            oltAlertDao.batchInsertOltAlertCodeMask(snmpParam.getEntityId(), oltTopAlarmCodeMaskList);
        }
    }

    /**
     * 采集OLT告警实体屏蔽
     * 
     * @param snmpParam
     */
    private void discoveryOltAlertInstanceMask(SnmpParam snmpParam) {
        List<OltTopAlarmInstanceMask> oltTopAlarmInstanceMaskList = getOltAlertFacade(snmpParam.getIpAddress())
                .getOltAlertInstanceMask(snmpParam);
        if (oltTopAlarmInstanceMaskList != null) {
            for (OltTopAlarmInstanceMask instanceMask : oltTopAlarmInstanceMaskList) {
                instanceMask.setEntityId(snmpParam.getEntityId());
            }
            oltAlertDao.batchInsertOltAlertInstanceMask(snmpParam.getEntityId(), oltTopAlarmInstanceMaskList);
        }
    }

    /**
     * 采集Trap配置数据
     * 
     * @param snmpParam
     */
    private void discoveryOltTrapConfig(SnmpParam snmpParam) {
        List<OltTrapConfig> oltTrapConfigList = getOltAlertFacade(snmpParam.getIpAddress()).getOltTrapConfig(snmpParam);
        if (oltTrapConfigList != null) {
            for (OltTrapConfig trapConfig : oltTrapConfigList) {
                trapConfig.setEntityId(snmpParam.getEntityId());
            }
            oltAlertDao.batchInsertOltTrapConfig(snmpParam.getEntityId(), oltTrapConfigList);
        }
    }

    /**
     * 获取OltAlertFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltAlertFacade
     */
    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    @Override
    public List<OltAlertTop> getOltDeviceAlertTop() {
        return oltAlertDao.getOltDeviceAlertTop();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Integer getTopEponAlarmMode(SnmpParam snmpParam) {
        return getOltAlertFacade(snmpParam.getIpAddress()).getTopEponAlarmMode(snmpParam);
    }

    /**
     * 设置设备当前支持的告警模式
     * 
     * @param snmpParam
     * @param code
     */
    public void setTopEponAlarmMode(SnmpParam snmpParam, Integer code) {
        getOltAlertFacade(snmpParam.getIpAddress()).setTopEponAlarmMode(snmpParam, code);
    }

    @Override
    public void entityAdded(EntityEvent event) {
        /**
         * 同步设备告警信息
         * 
         * @param snmpParam
         */
        Entity entity = event.getEntity();
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntity().getEntityId());
            List<EponEventLog> eponEventLogs = getOltAlertFacade(snmpParam.getIpAddress()).getEponEventLogs(snmpParam);
            for (EponEventLog log : eponEventLogs) {
                Integer emsCode;
                try {
                    // 告警同步时，从设备上获取当前的告警环境类型
                    emsCode = eponTrapCodeDao.getEmsCodeFromTrap(log.getEventCode(), "EPON",
                            getTopEponAlarmMode(snmpParam));
                } catch (ClassCastException e) {
                    // 如果Dao层查询不到相应网管Code 抛出此种异常
                    return;
                }
                Event eventLog = EventSender.getInstance().createEvent(emsCode, snmpParam.getIpAddress(),
                        log.getEventSource());
                eventLog.setEntityId(snmpParam.getEntityId());
                eventLog.setCreateTime(log.getEventTime());
                eventLog.setMessage(oltTrapParser.parseEventDescr(log.getEventCode(), log.getEventAdditionalText(),
                        log.getEventSource()));
                // EventSender.getInstance().send(eventLog);
                eventDao.insertEntity(eventLog);
            }
        }
    }

    @Override
    public void setOltTrapConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // Modify by Victor@20140418TrapServer地址改为服务器地址，也就是服务器所带默认采集器进行trap接收
        String manageIp = EnvironmentConstants.getHostAddress();
        // 设置设备trap配置
        try {
            getOltFacade(snmpParam.getIpAddress()).setOltTrapConfig(snmpParam, manageIp, "public");
        } catch (SnmpException e) {
            if (e.getErrorStatusText().contains("Inconsistent")) {
                // 重复设置
                logger.info("this trap server has been set, won't set it again!");
            } else {
                // 设值异常,重新抛出。
                throw new SnmpException(e);
            }
        }
        // 设备上设置成功后，插入数据库。
        OltTrapConfig oltTrapConfig = new OltTrapConfig();
        oltTrapConfig.setEntityId(entityId);
        oltTrapConfig.setAddrTAddress(manageIp);
        oltTrapConfig.setEponManagementAddrName(manageIp);
        oltTrapConfig.setEponManagementAddrCommunity("public");
        oltAlertDao.insertOrUpdateOltTrapConfig(oltTrapConfig);
    }

    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    @Override
    public void entityChanged(EntityEvent event) {
    }

    @Override
    public void entityRemoved(EntityEvent event) {
    }

    @Override
    public void managerChanged(EntityEvent event) {
    }

    private OltAlertFacade getOltAlertFacade(String ip) {
        return facadeFactory.getFacade(ip, OltAlertFacade.class);
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}
