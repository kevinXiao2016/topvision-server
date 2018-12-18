/***********************************************************************
 * $Id: OnuTrapParser.java,v1.0 2016-3-24 上午9:47:21 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.fault.dao.EponTrapCodeDao;
import com.topvision.ems.epon.fault.domain.OltEventSource;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventSource;
import com.topvision.ems.fault.exception.TrapInstanceException;
import com.topvision.ems.fault.parser.AbstractTrapParser;
import com.topvision.ems.fault.parser.TrapConstants;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.utils.EponIndex;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.ResourceManager;

/**
 * @author flack
 * @created @2016-3-24-上午9:47:21 用于拦截处理OLT上报的ONU/CCMTS上下线相关事件
 */
@Service("onuTrapParser")
public class OnuTrapParser extends AbstractTrapParser {
    private static final Logger logger = LoggerFactory.getLogger(OnuTrapParser.class);
    @Autowired
    private EponTrapCodeDao eponTrapCodeDao;
    @Autowired
    private OnuDao onuDao;

    // OLT设备告警类型电信、广电、公司内部ID冲突，定义类型区别不同领域，通过配置文件指定当前设备所支持领域
    @Value("${fault.type}")
    private String type;
    @Value("${trap.firbreakSwitch:0}")
    private int firbreakSwitch;
    @Autowired
    private LicenseIf licenseIf;
    private static Integer cos = 100;

    @Override
    public boolean match(Long entityId, Trap trap) {
        String trapType = trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID);
        // 需要单独处理CCMTS的CCMTS_LINK_LOSE
        if (TrapConstants.CCMTS_TRAP_TYPE.equals(trapType)) {
            Long cmcTrapCode = trap.getVarialbleLong(TrapConstants.CCMTS_TRAP_CODE);
            // 如果接收到的是CCMTS_LINK_LOSE事件,则一定是CCMTS设备事件,不用再进行设备类型判断
            if (cmcTrapCode == OltTrapConstants.CCMTS_LINK_LOSE) {
                return true;
            }
        } else if (OltTrapConstants.OLT_TRAP_TYPE_ALERT.equals(trapType)
                || OltTrapConstants.OLT_TRAP_TYPE_EVENT.equals(trapType)) {
            int eponTrapCode = trap.getVarialbleInt(OltTrapConstants.TRAP_OBJECT_CODE);
            // 只处理OLT上报的ONU相关TRAP,不能吞噬其他TRAP
            if (OltTrapConstants.onuParseEvent.contains(eponTrapCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long parseTrapCode(Trap trap) {
        String trapType = trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID);
        if (TrapConstants.CCMTS_TRAP_TYPE.equals(trapType)) {
            return trap.getVarialbleLong(TrapConstants.CCMTS_TRAP_CODE);
        } else {
            return Long.valueOf(trap.getVarialbleInt(OltTrapConstants.TRAP_OBJECT_CODE));
        }
    }

    private boolean isStandardTrap(Trap trap) throws TrapInstanceException {
        String[] instances = trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_INSTANCE).split(":");
        if (instances == null || instances.length != 8) {
            throw new TrapInstanceException();
        }
        return instances[7].equals("00");
    }

    @Override
    public Integer convertToEmsCode(Long entityId, Trap trap, Long trapCode) {
        Integer emsCode = null;

        if (trapCode.intValue() == OltTrapConstants.CCMTS_LINK_LOSE) {
            // CCMTS_LINK_LOSE
            try {
                emsCode = eponTrapCodeDao.getRelatedCmtsEventCode(trapCode);
                if (emsCode == null) {
                    throw new TrapInstanceException("emsCode is null, code is " + trapCode);
                }

                return emsCode;
            } catch (ClassCastException e) {
                // 如果Dao层查询不到相应网管Code 抛出此种异常
                throw new TrapInstanceException(e);
            }
        }

        try {

            emsCode = eponTrapCodeDao.getEmsCodeFromTrap(trapCode.intValue(), "EPON", Integer.parseInt(type));

            /* //GPON ONU的code 与EPON 不一致需要进行转换
             if (OltTrapConstants.GPON_ONU_OFFLINE == emsCode) {
                 emsCode = OltTrapConstants.ONU_OFFLINE;
             }
             if (OltTrapConstants.GPON_ONU_ONLINE == emsCode) {
                 emsCode = OltTrapConstants.ONU_ONLINE;
             }*/

            logger.debug("trap trapCode:" + trapCode);
            logger.debug("trap type:" + type);
            logger.debug("trap emsCode:" + emsCode);
            if (emsCode == null) {
                throw new TrapInstanceException("emsCode is null, trapCode is " + trapCode);
            }
            // 先排除告警的清除可能带来的干扰(现在使用ONU上线来进行清除)

            int eponTrapCorrelationId = 0;
            try {
                eponTrapCorrelationId = trap.getVarialbleInt(OltTrapConstants.TRAP_OBJECT_CORRELATIONID);
            } catch (NullPointerException e) {
                eponTrapCorrelationId = 0;
            }

            if (eponTrapCorrelationId != 0) {
                Integer clearCode = OltTrapConstants.event2Alert.get(emsCode);
                if (clearCode != null) {
                    emsCode = clearCode;
                }
            }

            if (emsCode != OltTrapConstants.ONU_DELETE) {
                // 网管是否能获取到下级设备信息
                String[] instances = trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_INSTANCE).split(":");

                if (instances == null || instances.length != 8) {
                    throw new TrapInstanceException();
                }

                Long slotNo = 0L;
                Long portNo = 0L;
                Long onuNo = 0L;

                if (isStandardTrap(trap)) {// 广电模式下instance的解析
                    slotNo = Long.parseLong(instances[1], 16);
                    portNo = Long.parseLong(instances[2], 16);
                    onuNo = Long.parseLong(instances[3], 16);
                } else {// 内部模式下instance的解析
                    slotNo = Long.parseLong(instances[0], 16);
                    portNo = Long.parseLong(instances[1], 16);
                    onuNo = Long.parseLong(instances[2], 16);
                }

                // 解析ONUINDEX
                Long onuIndex = EponIndex.getOnuIndex(String.valueOf(slotNo), String.valueOf(portNo),
                        String.valueOf(onuNo));
                Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
                Entity onuEntity = entityDao.selectByPrimaryKey(onuId);
                if (onuEntity != null) {
                    // 此时关联具体的设备
                    if (entityTypeService.isCcmtsWithoutAgent(onuEntity.getTypeId())) {
                        // 处理由emscode产生的对应cmts事件code
                        Integer ccmtsCode = eponTrapCodeDao.getRelatedCmtsEventCode(Long.valueOf(emsCode));
                        if (ccmtsCode != null) {
                            emsCode = ccmtsCode;
                        }
                    }
                } else {
                    // 无法获取具体设备类型,此时不处理下线事件和断纤事件(通常对应ONU删除和CCMTS解绑定)
                    if (emsCode == OltTrapConstants.ONU_OFFLINE || emsCode == OltTrapConstants.ONU_FIBER_BREAK) {
                        throw new TrapInstanceException("onuTrapParser cannot handle ONU_OFFLINE or ONU_FIBER_BREAK");
                    }
                }
            }

            return emsCode;
        } catch (Exception e) {
            logger.error("", e);
            // 如果Dao层查询不到相应网管Code 抛出此种异常
            throw new TrapInstanceException(e);
        }
    }

    @Override
    public EventSource parseEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode)
            throws TrapInstanceException {
        if (trapCode.intValue() == OltTrapConstants.CCMTS_LINK_LOSE) {
            return parseCmtsEventSource(trap, trapCode, entityId, emsCode);
        } else {
            return parseOnuEventSource(trap, trapCode, entityId, emsCode);
        }
    }

    private EventSource parseOnuEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode) {
        OltEventSource sourceObject = new OltEventSource(entityId);

        // 获取Trap中的SOURCE信息
        String[] instances = trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_INSTANCE).split(":");

        if (instances == null || instances.length != 8) {
            throw new TrapInstanceException();
        }

        logger.debug("trap instance:" + instances.toString());

        Long slotNo = 0L;
        Long portNo = 0L;
        Long onuNo = 0L;
        if (isStandardTrap(trap)) {// 广电模式下instance的解析
            slotNo = Long.parseLong(instances[1], 16);
            portNo = Long.parseLong(instances[2], 16);
            onuNo = Long.parseLong(instances[3], 16);
        } else {// 内部模式下instance的解析
            slotNo = Long.parseLong(instances[0], 16);
            portNo = Long.parseLong(instances[1], 16);
            onuNo = Long.parseLong(instances[2], 16);
        }

        sourceObject.setSource(OltEventSource.SOURCE_ONU);
        sourceObject.setSlotNo(slotNo);
        sourceObject.setPortNo(portNo);
        sourceObject.setOnuNo(onuNo);

        // 解析ONUINDEX
        Long onuIndex = EponIndex.getOnuIndex(String.valueOf(slotNo), String.valueOf(portNo), String.valueOf(onuNo));
        Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
        sourceObject.setEntityId(onuId);
        Entity onuEntity = entityDao.selectByPrimaryKey(onuId);

        if (emsCode == OltTrapConstants.ONU_DELETE) {
            sourceObject.setOnuAlias(onuEntity.getName());
        } else {
            try {
                // 网管是否能获取到下级设备信息
                if (onuEntity != null) {
                    // 此时关联具体的设备
                    if (entityTypeService.isOnu(onuEntity.getTypeId())) {
                        sourceObject.setOnuAlias(onuEntity.getName());
                    } else if (entityTypeService.isCcmtsWithoutAgent(onuEntity.getTypeId())) {
                        sourceObject.setSource(OltEventSource.SOURCE_CMTS);
                    }
                }
            } catch (Exception e) {
                // 如果获取设备信息出错,记录日志信息,直接返回
                logger.error("", e);
                throw new TrapInstanceException(e);
            }
        }

        return sourceObject;
    }

    private EventSource parseCmtsEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode) {
        OltEventSource sourceObject = new OltEventSource(entityId);
        sourceObject.setSource(OltEventSource.SOURCE_CMTS);

        // 从TRAP中解析CCMTS的mac地址
        String cmcMacString = null;
        try {
            cmcMacString = getCcmtsMacFromTrapText(trap.getVariableBindings().get(TrapConstants.CCMTS_MAC_NODE)
                    .toString());
        } catch (Exception e) {
            logger.info("OnuTrapParser.getCcmtsMacFromTrapText error:", e);
        }
        Onu ccmtsInfo = null;
        // CC上报的告警必须包括MAC字段
        if (cmcMacString == null || cmcMacString.length() == 0) {
            // 无法解析MAC地址时认为无法获取设备信息,不进行处理
            throw new TrapInstanceException("cannot get cmts mac, entityId: " + entityId);
        } else {
            // 根据mac地址和上联OLT地址获取ccmts的entityId, onuIndex信息
            ccmtsInfo = onuDao.getOnuByMacAndParentId(cmcMacString, entityId);
            if (ccmtsInfo == null) {
                throw new TrapInstanceException("cannot get cmts, entityId: " + entityId);
            } else {
                sourceObject.setEntityId(ccmtsInfo.getOnuId());
            }
        }

        sourceObject.setIndex(ccmtsInfo.getOnuIndex());
        sourceObject.setSlotNo(EponIndex.getSlotNo(ccmtsInfo.getOnuIndex()));
        sourceObject.setPortNo(EponIndex.getPonNo(ccmtsInfo.getOnuIndex()));
        sourceObject.setOnuNo(EponIndex.getOnuNo(ccmtsInfo.getOnuIndex()));

        return sourceObject;
    }

    @Override
    public Event buildEvent(Trap trap, Long trapCode, Integer emsCode, EventSource eventSource) {
        if (trapCode.intValue() == OltTrapConstants.CCMTS_LINK_LOSE) {
            return buildCmtsEvent(trap, emsCode, trapCode, eventSource);
        } else {
            return buildOnuEvent(trap, emsCode, trapCode, eventSource);
        }
    }

    private Event buildOnuEvent(Trap trap, Integer emsCode, Long trapCode, EventSource eventSource) {
        OltEventSource oltEventSource = (OltEventSource) eventSource;

        Event event = EventSender.getInstance().createEvent(emsCode, trap.getAddress(), eventSource.formatSource());
        event.setEntityId(eventSource.getEntityId());
        event.setOriginMessage(trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT));
        event.setOrginalCode(trapCode + 0L);
        logger.debug("trapToEvent:" + event.toString());
        try {
            // 暂时先不考虑trapVersion不同的情况,只提供统一的描述信息
            String onuAlias = oltEventSource.getOnuAlias();
            if (onuAlias != null && onuAlias != "") {
                event.setMessage(parseEventDescr(emsCode,
                        onuAlias + "," + trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT),
                        eventSource.formatSource()));
            } else {
                event.setMessage(parseEventDescr(emsCode,
                        trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT), eventSource.formatSource()));
            }
        } catch (Exception e) {
            event.setMessage(trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT));
        }
        event.setCreateTime(new Timestamp(trap.getTrapTime().getTime()));
        event.setUserObject(trap);

        Long onuIndex = EponIndex.getOnuIndex(String.valueOf(oltEventSource.getSlotNo()),
                String.valueOf(oltEventSource.getPortNo()), String.valueOf(oltEventSource.getOnuNo()));
        event.setDeviceIndex(onuIndex);
        event.setSourceObject(eventSource);
        if (logger.isDebugEnabled()) {
            logger.debug("OnuTrapParser.toAlert:{}", event);
        }

        return event;
    }

    private Event buildCmtsEvent(Trap trap, Integer emsCode, Long trapCode, EventSource eventSource) {
        OltEventSource oltEventSource = (OltEventSource) eventSource;

        Event event = EventSender.getInstance().createEvent(emsCode, trap.getAddress(), eventSource.formatSource());
        event.setEntityId(eventSource.getEntityId());
        event.setOrginalCode(trapCode + 0L);

        // 事件描述信息
        String cmcMacString = getCcmtsMacFromTrapText(trap.getVariableBindings().get(TrapConstants.CCMTS_MAC_NODE)
                .toString());
        String extraMsg = "MAC:" + cmcMacString;
        String eventDesc = getString("DeviceAlert.ccmtsLinkLose", eventSource.formatSource(), extraMsg);
        event.setMessage(eventDesc);
        event.setCreateTime(new Timestamp(trap.getTrapTime().getTime()));
        event.setUserObject(trap);
        event.setDeviceIndex(oltEventSource.getIndex());

        if (logger.isDebugEnabled()) {
            logger.debug("OnuTrapParser.toAlert:{}", event);
        }

        return event;
    }

    /**
     * 获取事件描述信息
     * 
     * @param typeId
     * @param message
     * @param source
     * @return
     */
    private String parseEventDescr(Integer typeId, String message, String source) {
        String result = "";
        switch (typeId) {
        case 258:// ONU解注册
            result = getString("DeviceAlert.onuDeregister", source, message);
            break;
        case 16393:// ONU停电告警
            result = getString("DeviceAlert.onuPoweroff2", source, message);
            break;
        case 16433:// onu下线
            result = getString("DeviceAlert.onuOffline2", source, message);
            break;
        case 16434:// onu上线
            result = getString("DeviceAlert.onuOnline2", source, message);
            break;
        case 16435:// ONU断纤告警
            result = getString("DeviceAlert.onuFiberBreak", source, message);
            break;
        case 16437:// ONU长发光
            result = getString("DeviceAlert.onuRogue", source, message);
            break;
        case 16439:// ONU删除告警
            result = getString("DeviceAlert.onuDelete", source, message);
            break;
        case 101003:// CCMTS断电告警
            result = getString("DeviceAlert.ccmtsLinkLose", source, message);
            break;
        case 1016433:// CCMTS下线告警
            if (firbreakSwitch == 1 || licenseIf.isProject("gz")) {
                result = getString("DeviceAlert.ccmtsFiberBreak", source, message);
            } else {
                result = getString("DeviceAlert.ccmtsOffline", source, message);
            }
            break;
        case 1016434:// CCMTS上线
            result = getString("DeviceAlert.ccmtsOnline", source, message);
            break;
        case 1016435:// CCMTS断纤告警
            result = getString("DeviceAlert.ccmtsFiberBreak", source, message);
            break;
        }
        return result;
    }

    private String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.epon.resources")
                    .getNotNullString(key, strings);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 告警报文的text节点下截取CCMTS的mac地址
     */
    private String getCcmtsMacFromTrapText(String event_text) {
        String ccmts_mac = "";
        if (event_text.indexOf("MAC") != -1 || event_text.indexOf("CMC") != -1) {
            // 兼容1.8.X版本直接以文本的方式给出信息
        } else {
            // 兼容1.7.X版本以十六进制的方式给出信息
            String[] array = event_text.split(":");
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < array.length; j++) {
                sb.append(array[j]);
            }
            String text = sb.toString();
            event_text = EponIndex.toStringHex(text);
        }
        if (event_text.indexOf("CMTSMAC=") != -1) {
            int position = event_text.indexOf("CMTSMAC=");
            ccmts_mac = event_text.substring(position + 8, position + 25);
        } else if (event_text.indexOf("CMC=") != -1) {
            int position = event_text.indexOf("CMC=");
            ccmts_mac = event_text.substring(position + 4, position + 21);
        } else if (event_text.indexOf("CmcMac=") != -1) {
            int position = event_text.indexOf("CmcMac=");
            ccmts_mac = event_text.substring(position + 7, position + 24);
        } else if (event_text.indexOf("CMTS-MAC=") != -1) {
            int position = event_text.indexOf("CMTS-MAC=");
            String style = event_text.substring(position + 9, position + 14);
            if (style.indexOf(".") != -1) {
                ccmts_mac = changeStyle(event_text.substring(position + 9, position + 23));
            } else if (style.indexOf(":") != -1) {
                ccmts_mac = event_text.substring(position + 9, position + 26);
            }
        }
        return ccmts_mac.toUpperCase();
    }

    private String changeStyle(String address) {
        String[] tmp = address.split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append(tmp[0].substring(0, 2)).append(":").append(tmp[0].substring(2, 4)).append(":");
        sb.append(tmp[1].substring(0, 2)).append(":").append(tmp[1].substring(2, 4)).append(":");
        sb.append(tmp[2].substring(0, 2)).append(":").append(tmp[2].substring(2, 4));
        return sb.toString();
    }

    @Override
    public Integer getTrapCos() {
        return cos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
