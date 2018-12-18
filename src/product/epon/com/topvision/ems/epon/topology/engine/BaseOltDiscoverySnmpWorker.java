/***********************************************************************
 * $Id: BaseOltDiscoverySnmpWorker.java,v1.0 2014-10-15 上午10:09:07 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;
import com.topvision.framework.utils.ApplicationContextUtil;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2014-10-15-上午10:09:07
 * 
 */
public class BaseOltDiscoverySnmpWorker<T extends OltDiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = -2064120275618689845L;
    private static final String SYSTEMOID = "1.3.6.1.2.1.1.2.0";
    private static final String TOPSYSOLTUPTIME_OID = "1.3.6.1.4.1.32285.11.2.3.1.2.9.0";

    /**
     * @param snmpParam
     */
    public BaseOltDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        Long timeTmp = 0L;
        logger.info("begin to discovery ipAddress:" + snmpParam.getIpAddress() + "  EntityId:"
                + snmpParam.getEntityId());
        logger.info("begin to discovery TopologyData ipAddress:" + snmpParam.getIpAddress() + "  EntityId:"
                + snmpParam.getEntityId());
        snmpParam
                .setMibs("RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB,TOPVISION-CCMTS-MIB,SNMP-VIEW-BASED-ACM-MIB,SNMP-USER-BASED-SM-MIB,SNMP-TARGET-MIB,SNMP-NOTIFICATION-MIB");

        snmpUtil.reset(snmpParam);
        result.setDiscoveryTime(System.currentTimeMillis());
        result.setEntityId(snmpParam.getEntityId());
        result.setIp(snmpParam.getIpAddress());
        snmpParam.setVersion(snmpUtil.getVersion());
        result.setSnmpParam(snmpParam);

        try {
            snmpUtil.get(SYSTEMOID);
        } catch (Exception e) {
            result.setStackTrace(new SnmpException("snmpError", e));
            return;
        }
        // RFC1213-MIB中的system信息
        timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "System");
        try {
            DeviceBaseInfo deviceBaseInfo = snmpUtil.get(DeviceBaseInfo.class);
            // EMS-15300 中广有线蚌埠+欧阳益湘+设备和网管上的OLT在线时长不一致
            // PN8600-V1.10.0.5才支持该节点
            DeviceVersionService deviceVersionService = ApplicationContextUtil.getContext().getBean(
                    "deviceVersionService", DeviceVersionService.class);
            Boolean supporttopsysoltuptime = deviceVersionService.isFunctionSupported(snmpParam.getEntityId(),
                    "topsysoltuptime");
            if (supporttopsysoltuptime) {
                String sysUpTime = snmpUtil.get(TOPSYSOLTUPTIME_OID);
                // 判断是不是数字字符串
                if (StringUtils.isNumeric(sysUpTime)) {
                    deviceBaseInfo.setSysUpTime(sysUpTime);
                }
            }
            result.setSystem(deviceBaseInfo);
            result.setIpAddressTables(snmpUtil.getTable(IpAddressTable.class, true));
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "System", timeTmp);
        logger.info("topology system finished!");

        List<String> excludeOids = result.getExcludeOids();
        timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltAttribute");
        try {
            OltAttribute attr = snmpUtil.get(OltAttribute.class, excludeOids);
            attr.setEntityId(result.getEntityId());
            // 把启动时长换算为服务器的时刻，以便界面显示启动时长 ,此处的时间单位为10毫秒，不是SNMP4J标准的毫米
            attr.setOltDeviceUpTime(System.currentTimeMillis() - attr.getOltDeviceUpTime() * 10);
            result.setAttribute(attr);
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltAttribute", timeTmp);
        logger.info("topology oltattribute finished!");

        // 槽位板卡信息
        try {
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltSlotAttribute");
            List<OltSlotAttribute> slots = snmpUtil.getTable(OltSlotAttribute.class, true, excludeOids);
            if (slots != null) {
                result.setSlots(new ArrayList<OltSlotAttribute>());
            }
            for (OltSlotAttribute slot : slots) {
                if (slot.getSlotNo().equals(0L)) {
                    slot.setSlotNo(1L);
                }
                slot.setEntityId(result.getEntityId());
                slot.setSlotIndex(EponIndex.getSlotIndex(slot.getSlotNo().intValue()));
                result.addSlot(slot);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltSlotAttribute", timeTmp);
        logger.info("topology slots finished!");

        try {
            // SlotStatus
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltSlotStatus");
            List<OltSlotStatus> slotStatus = snmpUtil.getTable(OltSlotStatus.class, true, excludeOids);
            if (slotStatus != null) {
                result.setSlotStatus(new ArrayList<OltSlotStatus>());
            }
            for (OltSlotStatus slot : slotStatus) {
                slot.setEntityId(result.getEntityId());
                result.addSlotStatus(slot);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltSlotStatus", timeTmp);
        logger.info("topology slot's status finished!");

        // 电源
        try {
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltPowerAttribute");
            List<OltPowerAttribute> powers = snmpUtil.getTable(OltPowerAttribute.class, true, excludeOids);
            if (powers != null) {
                result.setPowers(new ArrayList<OltPowerAttribute>());
            }
            for (OltPowerAttribute power : powers) {
                power.setEntityId(result.getEntityId());
                result.addPower(power);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltPowerAttribute", timeTmp);
        logger.info("topology powers finished!");

        try {
            // PowerStatus
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltPowerStatus");
            List<OltPowerStatus> powerStatus = snmpUtil.getTable(OltPowerStatus.class, true, excludeOids);
            if (powerStatus != null) {
                result.setPowerStatus(new ArrayList<OltPowerStatus>());
            }
            for (OltPowerStatus power : powerStatus) {
                power.setEntityId(result.getEntityId());
                result.addPowerStatus(power);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltPowerStatus", timeTmp);
        logger.info("topology power's status finished!");

        try {
            // 风扇
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltFanAttribute");
            List<OltFanAttribute> fans = snmpUtil.getTable(OltFanAttribute.class, true, excludeOids);
            if (fans != null) {
                result.setFans(new ArrayList<OltFanAttribute>());
            }
            for (OltFanAttribute fan : fans) {
                fan.setEntityId(result.getEntityId());
                result.addFan(fan);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltFanAttribute", timeTmp);
        logger.info("topology fans finished!");
        try {
            // FanStatus
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltFanStatus");
            List<OltFanStatus> fanStatus = snmpUtil.getTable(OltFanStatus.class, true, excludeOids);
            if (fanStatus != null) {
                result.setFanStatus(new ArrayList<OltFanStatus>());
            }
            for (OltFanStatus fan : fanStatus) {
                fan.setEntityId(result.getEntityId());
                result.addFanStatus(fan);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltFanStatus", timeTmp);
        logger.info("topology fan's status finished!");

        snmpUtil.reset(snmpParam);
        try {
            // PON
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltPonAttribute");
            List<OltPonAttribute> pons = snmpUtil.getTable(OltPonAttribute.class, true, excludeOids);
            if (pons != null) {
                result.setPons(new ArrayList<OltPonAttribute>());
            }
            for (OltPonAttribute pon : pons) {
                pon.setEntityId(result.getEntityId());
                result.addPon(pon);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltPonAttribute", timeTmp);
        logger.info("topology pons finished!");

        try {
            // SNI
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltSniAttribute");
            List<OltSniAttribute> snis = snmpUtil.getTable(OltSniAttribute.class, true, excludeOids);
            if (snis != null) {
                result.setSnis(new ArrayList<OltSniAttribute>());
            }
            for (OltSniAttribute sni : snis) {
                sni.setEntityId(result.getEntityId());
                result.addSni(sni);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltSniAttribute", timeTmp);
        logger.info("topology snis finished!");
    }

}