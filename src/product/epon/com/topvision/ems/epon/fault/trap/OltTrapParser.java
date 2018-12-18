/***********************************************************************
 * $Id: OltTrapParser.java,v1.0 2012-1-13 下午01:34:54 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.OltPerf;
import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.epon.fault.dao.EponTrapCodeDao;
import com.topvision.ems.epon.fault.domain.OltEventSource;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
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
import com.topvision.platform.util.StringUtil;

/**
 * @author Victor
 * @created @2012-1-13-下午01:34:54
 * 
 * 
 * @modify by Rod
 */

@Service("oltTrapParser")
public class OltTrapParser extends AbstractTrapParser {
    private static final Logger logger = LoggerFactory.getLogger(OltTrapParser.class);
    @Autowired
    private EponTrapCodeDao eponTrapCodeDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private LicenseIf licenseIf;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private OltSlotDao oltSlotDao;

    // 上联口事件emsCode
    private static final List<Integer> UPLINK_PORT_EVENTS = new ArrayList<Integer>();

    @PostConstruct
    @Override
    public void initialize() {
        // setTrapCos(cos);
        trapService.registeryParser(this);
        UPLINK_PORT_EVENTS.add(EponCode.PORT_LINK_UP);// PORT_LINK_UP
        UPLINK_PORT_EVENTS.add(EponCode.PORT_LINK_DOWN);// PORT_LINK_DOWN
        UPLINK_PORT_EVENTS.add(EponCode.PON_OPTICAL_REMOVE);// PON_OPTICAL_REMOVE
        UPLINK_PORT_EVENTS.add(EponCode.PON_OPTICAL_INSERT);// PON_OPTICAL_INSERT
    }

    // OLT设备告警类型电信、广电、公司内部ID冲突，定义类型区别不同领域，通过配置文件指定当前设备所支持领域
    @Value("${fault.type}")
    private String type;
    private static Integer cos = 1000;

    @Override
    public boolean match(Long entityId, Trap trap) {
        String type = trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID);
        if (type == null) {
            return false;
        }

        // eponAlarmNotification or eponEventNotification
        return OltTrapConstants.OLT_TRAP_TYPE_ALERT.equals(type) || OltTrapConstants.OLT_TRAP_TYPE_EVENT.equals(type);
    }

    @Override
    public Long parseTrapCode(Trap trap) {
        int code = trap.getVarialbleInt(OltTrapConstants.TRAP_OBJECT_CODE);

        // 下面代码注释，同设备确认，additionText没有用

        /*
         * // 广电标准告警中，up和down对应告警id均为203，此处做特殊处理。(EPON告警统一前必须进行LINKUP/LINKDOWN的转换) if
         * (isStandardTrap(trap)) { try { if (code == 203) { String additionText =
         * trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT); if
         * (additionText.substring(10, 11).equals("1")) { code = 2031; } else if
         * (additionText.substring(10, 11).equals("2")) { code = 2032; } } } catch (Exception e) {
         * // 上面的处理适用于EPON告警统一前的处理,修改后的告警不需要进行处理 } } else { if (code == 203) { // link down code =
         * 2031; } }
         */
        return code == 203 ? Long.valueOf(2031) : Long.valueOf(code);
    }

    @Override
    public Integer convertToEmsCode(Long entityId, Trap trap, Long trapCode) throws TrapInstanceException {

        // 将设备Code转换成网管Code
        Integer emsCode;
        try {
            // 这里需要保证type一定与设备的告警环境类型一致
            emsCode = eponTrapCodeDao.getEmsCodeFromTrap(trapCode.intValue(), "EPON", Integer.parseInt(type));
            if (emsCode == null) {
                throw new TrapInstanceException("emsCode is null, trapCode is " + trapCode);
            }
            // ONU CATV LICENSE TEST
            if (OltTrapConstants.onuCatvEvents.contains(emsCode) && !licenseIf.isSupportProject("test")) {
                throw new TrapInstanceException("LICENSE does not support, trapCode is " + trapCode);
            }

            // Add by Rod 告警重构核心部分
            // if (!isStandardTrap(trap)) {
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
            // }
            return emsCode;
        } catch (Exception e) {
            // 如果Dao层查询不到相应网管Code 抛出此种异常
            throw new TrapInstanceException(e);
        }
    }

    @Override
    public EventSource parseEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode)
            throws TrapInstanceException {
        String[] instances = trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_INSTANCE).split(":");

        if (instances == null || instances.length != 8) {
            throw new TrapInstanceException();
        }

        String instanceType = instances[7];

        OltEventSource sourceObject = new OltEventSource(entityId);

        if (emsCode.equals(EponCode.ONU_MAC_AUTH_ERROR)) {
            sourceObject.setSource(OltEventSource.SOURCE_ADDITIONALTEXT);
            sourceObject.setAdditionalText(trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT));
        } else if (instanceType.equals("00")) {
            // 广电标准模式
            parseStandardEventSource(trap, instances, entityId, emsCode, sourceObject);
        } else {
            // 内部模式
            parseInnerEventSource(trap, instances, entityId, emsCode, sourceObject);
        }
        return sourceObject;
    }

    private void parseStandardEventSource(Trap trap, String[] instances, Long entityId, Integer emsCode,
            OltEventSource sourceObject) {
        if (instances[1].equals("00") && instances[2].equals("00") && instances[3].equals("00")
                && instances[4].equals("00") && instances[5].equals("00") && instances[6].equals("00")
                && instances[7].equals("00")) {
            // OLT级别：第1个字节有意义，第2-8个字节均为0
            sourceObject.setSource(OltEventSource.SOURCE_OLT);
            sourceObject.setIp(trap.getAddress());
        } else if (instances[2].equals("00") && instances[3].equals("00") && instances[4].equals("00")
                && instances[5].equals("00") && instances[6].equals("00") && instances[7].equals("00")) {
            // SLOT级别：第1-2个字节有意义，第3-8个字节均为0
            sourceObject.setSource(OltEventSource.SOURCE_SLOT);
            sourceObject.setSlotNo(Long.parseLong(instances[1], 16));
        } else if (instances[3].equals("00") && instances[4].equals("00") && instances[5].equals("00")
                && instances[6].equals("00") && instances[7].equals("00")) {
            // PORT级别: 第1-3个字节有意义，第4-8个字节均为0
            Long slotNo = Long.parseLong(instances[1], 16);
            Long portNo = Long.parseLong(instances[2], 16);

            sourceObject.setSource(OltEventSource.SOURCE_PORT);
            sourceObject.setSlotNo(slotNo);
            sourceObject.setPortNo(portNo);
        } else if (instances[5].equals("00") && instances[6].equals("00") && instances[7].equals("00")) {
            // ONU级别：第1-4个字节有意义，第5-8个字节均为0
            // ONU PON级别：第1-5个字节有意义，第6-8个字节均为0
            Long slotNo = Long.parseLong(instances[1], 16);
            Long portNo = Long.parseLong(instances[2], 16);
            Long onuNo = Long.parseLong(instances[3], 16);

            sourceObject.setSource(OltEventSource.SOURCE_ONU);
            sourceObject.setSlotNo(slotNo);
            sourceObject.setPortNo(portNo);
            sourceObject.setOnuNo(onuNo);

            // 根据slotNo, portNo, onuNo获取onuIndex, 再查询onuId, 将告警对象的entityId设置为对应ONU的id
            Long onuIndex = EponIndex
                    .getOnuIndex(String.valueOf(slotNo), String.valueOf(portNo), String.valueOf(onuNo));
            Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
            if (onuId != null) {
                // 经讨论，只有ONU才需要进行处理，类A型设备仍然显示为OLT的entityId
                Entity entity = entityDao.selectByPrimaryKey(onuId);
                if (entityTypeService.isOnu(entity.getTypeId())) {
                    sourceObject.setEntityId(onuId);
                    // PR-910 增加onu告警的别名显示
                    sourceObject.setOnuAlias(entity.getName());
                }
            } else {
                throw new TrapInstanceException("cannot find onu entity");
            }
        } else if (instances[6].equals("00") && instances[7].equals("00")) {
            // UNI口级别：第1-6个字节有意义，第7-8个字节均为0
            Long slotNo = Long.parseLong(instances[1], 16);
            Long portNo = Long.parseLong(instances[2], 16);
            Long onuNo = Long.parseLong(instances[3], 16);
            Long onuPortNo = Long.parseLong(instances[5], 16);

            sourceObject.setSource(OltEventSource.SOURCE_ONU_PORT);
            sourceObject.setSlotNo(slotNo);
            sourceObject.setPortNo(portNo);
            sourceObject.setOnuNo(onuNo);
            sourceObject.setOnuPortNo(onuPortNo);

            // 根据slotNo, portNo, onuNo获取onuIndex, 再查询onuId, 将告警对象的entityId设置为对应下级设备的id
            Long onuIndex = EponIndex
                    .getOnuIndex(String.valueOf(slotNo), String.valueOf(portNo), String.valueOf(onuNo));
            Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
            if (onuId != null) {
                sourceObject.setEntityId(onuId);
                // PR-910 增加onu告警的别名显示
                Entity entity = entityDao.selectByPrimaryKey(onuId);
                sourceObject.setOnuAlias(entity.getName());
            }
        }
    }

    private void parseInnerEventSource(Trap trap, String[] instances, Long entityId, Integer emsCode,
            OltEventSource sourceObject) {
        String instanceType = instances[7];

        if (instanceType.equals("01")) {
            // olt级别
            sourceObject.setSource(OltEventSource.SOURCE_OLT);
            sourceObject.setIp(trap.getAddress());
        } else if (instanceType.equals("02")) {
            // SLOT级别
            sourceObject.setSource(OltEventSource.SOURCE_SLOT);
            sourceObject.setSlotNo(Long.parseLong(instances[0], 16));
        } else if (instanceType.equals("03")) {
            // PORT级别
            sourceObject.setSource(OltEventSource.SOURCE_PORT);
            sourceObject.setSlotNo(Long.parseLong(instances[0], 16));
            sourceObject.setPortNo(Long.parseLong(instances[1], 16));
        } else if (instanceType.equals("04") || instanceType.equals("05")) {
            // ONU级别，ONU_DEV=04， ONU_PON=05
            Long slotNo = Long.parseLong(instances[0], 16);
            Long portNo = Long.parseLong(instances[1], 16);
            Long onuNo = Long.parseLong(instances[2], 16);
            logger.debug("oltTrapParser:" + instances.toString());

            sourceObject.setSource(OltEventSource.SOURCE_ONU);
            sourceObject.setSlotNo(slotNo);
            sourceObject.setPortNo(portNo);
            sourceObject.setOnuNo(onuNo);

            // 根据slotNo, portNo, onuNo获取onuIndex, 再查询onuId, 将告警对象的entityId设置为对应ONU的id
            Long onuIndex = EponIndex
                    .getOnuIndex(String.valueOf(slotNo), String.valueOf(portNo), String.valueOf(onuNo));
            Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
            if (onuId != null) {
                // 经讨论，只有ONU才需要进行处理，类A型设备仍然显示为OLT的entityId
                Entity entity = entityDao.selectByPrimaryKey(onuId);
                if (entityTypeService.isOnu(entity.getTypeId())) {
                    sourceObject.setEntityId(onuId);
                    // PR-910 增加onu告警的别名显示
                    sourceObject.setOnuAlias(entity.getName());
                }
            } else {
                throw new TrapInstanceException("cannot find onu entity");
            }
        } else if (instanceType.equals("06") || instanceType.equals("07") || instanceType.equals("08")) {
            // UNI口级别
            Long slotNo = Long.parseLong(instances[0], 16);
            Long portNo = Long.parseLong(instances[1], 16);
            Long onuNo = Long.parseLong(instances[2], 16);
            Long onuPortNo = Long.parseLong(instances[3], 16);

            sourceObject.setSource(OltEventSource.SOURCE_ONU_PORT);
            sourceObject.setSlotNo(slotNo);
            sourceObject.setPortNo(portNo);
            sourceObject.setOnuNo(onuNo);
            sourceObject.setOnuPortNo(onuPortNo);

            // 根据slotNo, portNo, onuNo获取onuIndex, 再查询onuId, 将告警对象的entityId设置为对应下级设备的id
            Long onuIndex = EponIndex
                    .getOnuIndex(String.valueOf(slotNo), String.valueOf(portNo), String.valueOf(onuNo));
            Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
            if (onuId != null) {
                sourceObject.setEntityId(onuId);
                // PR-910 增加onu告警的别名显示
                Entity entity = entityDao.selectByPrimaryKey(onuId);
                sourceObject.setOnuAlias(entity.getName());
            }
        } else if (instanceType.equals("09")) {
            // VLAN 级别
            Long vlan = Long.parseLong(instances[3], 16);

            sourceObject.setSource(OltEventSource.SOURCE_VLAN);
            sourceObject.setVlan(vlan);
        }
    }

    @Override
    public Event buildEvent(Trap trap, Long trapCode, Integer emsCode, EventSource eventSource) {
        OltEventSource oltEventSource = (OltEventSource) eventSource;

        Event event = EventSender.getInstance().createEvent(emsCode, trap.getAddress(), oltEventSource.formatSource());
        event.setEntityId(oltEventSource.getEntityId());
        event.setSourceObject(oltEventSource);
        event.setOriginMessage(trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT));
        event.setOrginalCode(trapCode);
        event.setCreateTime(new Timestamp(trap.getTrapTime().getTime()));
        event.setUserObject(trap);

        String message = "";
        try {
            String onuAlias = oltEventSource.getOnuAlias();
            // EMS-14249 制造PN8602-E上联口linkdown告警，在告警列表中查看端口上报不正确
            String source = convertSource(oltEventSource, emsCode);
            if (onuAlias != null && onuAlias != "") {
                message = parseEventDescr(emsCode,
                        onuAlias + "," + trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT), source);
            } else {
                message = parseEventDescr(emsCode, trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT),
                        source);
            }
        } catch (Exception e) {
            message = trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT);
        }
        event.setMessage(message);
        return event;
    }

    @Override
    public void sendEvent(Trap trap, Event event) {
        super.sendEvent(trap, event);

        if (event.getTypeId().equals(EponCode.ONU_ONLINE)) {
            String macSource = trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT);
            Event macAuthSuccessEvent = EventSender.getInstance().createEvent(EponCode.ONU_MAC_AUTH_SUCCESS,
                    trap.getAddress(), macSource);
            macAuthSuccessEvent.setEntityId(event.getEntityId());
            macAuthSuccessEvent.setLevelId(trap.getVarialbleByte(OltTrapConstants.TRAP_OBJECT_SEVERITY));
            macAuthSuccessEvent.setMessage("ONU[" + macSource + "] MAC_AUTH_SUCCESS");
            macAuthSuccessEvent.setCreateTime(new Timestamp(parseTrapTime(trap
                    .getVarialbleValue(OltTrapConstants.TRAP_OBJECT_OCCURTIME))));
            EventSender.getInstance().send(macAuthSuccessEvent);
        }
    }

    /**
     * 上联口告警信息source需要转换，展示为displayName
     * 
     * @param oltEventSource
     * @param emsCode
     * @return
     */
    private String convertSource(OltEventSource oltEventSource, Integer emsCode) {
        String displayName = "";
        if (UPLINK_PORT_EVENTS.contains(emsCode)) {
            Long slotNo = oltEventSource.getSlotNo();
            Long portNo = oltEventSource.getPortNo();
            Long entityId = oltEventSource.getEntityId();
            // 主控板为0号
            if (slotNo.equals(oltSlotDao.getMasterSlotNo(entityId))) {
                slotNo = 0L;
            }
            long sniIndex = EponIndex.getSniIndex(slotNo.intValue(), portNo.intValue());
            displayName = oltSniDao.querySniDisplayName(entityId, sniIndex);
        }

        boolean needConvert = OltEventSource.SOURCE_PORT.equals(oltEventSource.getSource())
                && !StringUtil.isEmpty(displayName);
        return needConvert ? "PORT:" + displayName : oltEventSource.formatSource();
    }

    /*
     * private boolean isStandardTrap(Trap trap) throws TrapInstanceException { String[] instances =
     * trap.getVarialbleValue(OltTrapConstants.TRAP_OBJECT_INSTANCE).split(":"); if (instances ==
     * null || instances.length != 8) { throw new TrapInstanceException(); } return
     * instances[7].equals("00"); }
     */
    public String parseEventDescr(Integer typeId, String message, String source) {
        String result = "";
        switch (typeId) {
        case 4101:// 板卡拔出
            result = getString("DeviceAlert.slotExtract", source);
            break;
        case 4102:// 板卡插入
            // if (message.substring(10, 11).equals("2")) {
            result = getString("DeviceAlert.slotInsert", source);
            break;
        case 259:// 板启动正常 （板上线）
            result = getString("DeviceAlert.slotOnline", source);
            break;
        case 260:// 板离线 260
            result = getString("DeviceAlert.slotOffline", source);
            break;
        case 261:// ONU自动升级
            result = getString("DeviceAlert.onuAutoUpgrade", source, message);
            break;
        case 262:// 非法ONU注册
            result = getString("DeviceAlert.onuIllegalregister", source, message);
            break;
        case 263:// ONU LOID冲突
            result = getString("DeviceAlert.onuLoidconflict", source, message);
            break;
        case 266:// ONU MAC冲突
            result = getString("DeviceAlert.onuMacConflict", source, message);
            break;
        case 4109:// 设备风扇框被拔出
            result = getString("DeviceAlert.fanExtract", source);
            break;
        case 4110:// 设备风扇框被插入
            result = getString("DeviceAlert.fanInsert", source);
            break;
        case 4113:// 设备电源框被拔
            result = getString("DeviceAlert.powerExtract", source);
            break;
        case 4114:// 设备电源框被插入
            result = getString("DeviceAlert.powerInsert", source);
            break;
        case 4125:// 板CPU负荷异常
            result = getString("DeviceAlert.bdCpuOverload", source, message);
            break;
        case 4126:// 板CPU负荷正常 4125对应的清除事件
            result = getString("DeviceAlert.bdCpuNormal", source, message);
            break;
        case 16432:// ONU保护倒换
            result = getString("DeviceAlert.onuProtectSwitch", source, message);
            break;
        case 16431:// ONU保护倒换恢复
            result = getString("DeviceAlert.proSwitchRecovery", source);
            break;

        case 16423:// ONU温度过高
            // int onuTempHigh = Integer.parseInt(message.replaceAll(":", ""), 16);
            // result = getString("DeviceAlert.onuTempHigh", source, onuTempHigh + "");
            result = getString("DeviceAlert.onuTempHigh2", source, message);
            break;
        case 16424:// ONU温度过低
            // int onuTempLow = Integer.parseInt(message.replaceAll(":", ""), 16);
            // result = getString("DeviceAlert.onuTempLow", source, onuTempLow + "");
            result = getString("DeviceAlert.onuTempLow2", source, message);
            break;
        case 16425:// ONU温度转正常
            result = getString("DeviceAlert.onuTempNormal", source, message);
            break;
        case 8193:// SNI LINK DOWN
            result = getString("DeviceAlert.portLinkdown", source);
            break;
        case 8194:// SNI LINK UP
            result = getString("DeviceAlert.portLinkup", source);
            break;
        case 4098:// 板卡启动失败
            // 板卡启动失败 alarm Info:4字节：0x00000001-下载软件失败；0x00000002-配置恢复失败
            /*
             * String info = ""; if (message.equals("00:00:00:01")) { info =
             * getString("DeviceAlert.bdDownloadSoftwareFail"); } else if
             * (message.equals("00:00:00:02")) { info =
             * getString("DeviceAlert.bdConfigRecoverFail"); } result =
             * getString("DeviceAlert.bdStartFail", source, info);
             */
            result = getString("DeviceAlert.bdStartFail2", source, message);
            break;
        case 4099:
            result = getString("DeviceAlert.bdReset", source);
            break;
        case 4103:// 板卡温度异常
            // Info:内部采取摄氏度作为单位；4字节；CTC需转换：高2字节0x0000，低两字节实测值（单位：1/256摄氏度，-128-128摄氏度）
            // int bdTempHigh = Integer.parseInt(message.replaceAll(":", ""), 16);
            // result = getString("DeviceAlert.bdTempHigh", source, bdTempHigh + "");
            message = message.replace("C", getString("COMMON.degreesCelsius"));
            result = getString("DeviceAlert.bdTempHigh2", source, message);
            break;
        case 4105:// 板卡温度异常（过低）
            // Info:内部采取摄氏度作为单位；4字节；CTC需转换：高2字节0x0000，低两字节实测值（单位：1/256摄氏度，-128-128摄氏度）
            // int bdTempLow = Integer.parseInt(message.replaceAll(":", ""), 16);
            // result = getString("DeviceAlert.bdTempLow", source, bdTempLow + "");
            message = message.replace("C", getString("COMMON.degreesCelsius"));
            result = getString("DeviceAlert.bdTempLow2", source, message);
            break;
        case 4104:// 板卡温度异常（过高或过低转正常）
            result = getString("DeviceAlert.bdTempNormal", source, message);
            break;
        case 4107:// 风扇异常
            // result = getString("DeviceAlert.fanAbnormal", source);
            result = getString("DeviceAlert.fanAbnormal2", source, message);
            break;
        case 4108:// 风扇正常
            result = getString("DeviceAlert.fanNormal", source, message);
            break;
        case 4111:// 电源异常（异常时上报）
            // alarm Info:4字节：高两字节：电压值（将来支持）；低两字节：温度值（将来支持）
            result = getString("DeviceAlert.powerAbnormal", source);
            break;
        case 4112:// 电源异常（异常恢复）
            result = getString("DeviceAlert.powerNormal", source);
            break;
        case 4115:// OLT硬件故障（PON芯片严重故障 FATAL ERROR）
            // alarm Info:4字节：PON芯片序号
            // result = getString("DeviceAlert.ponChipError", source, message);
            result = getString("DeviceAlert.ponChipError2", source, message);
            break;
        case 4117:// 板类型不匹配
            // alarm Info:4字节：实际业务板类型字符串MPU/GEU/EPU，比如，GEU在8602主控报MPU
            // result = getString("DeviceAlert.bdTypeMisMatch", source);
            result = getString("DeviceAlert.bdTypeMisMatch2", source, message);
            break;
        case 4118:// 板类型不匹配恢复
            result = getString("DeviceAlert.bdTypeMatch", source, message);
            break;
        case 4119:// 板卡版本不正确
            // alarm Info:上报实际版本字符串，最长63个字符
            // result = getString("DeviceAlert.bdSWMisMatch", source);
            result = getString("DeviceAlert.bdSWMisMatch2", source, message);
            break;
        case 4120:// 板版本不正确恢复
            result = getString("DeviceAlert.bdSWMatch", source, message);
            break;
        case 4121:// 板槽位错误（主控板校验，接口板点灯）
            // result = getString("DeviceAlert.bdSlotMisMatch", source);
            result = getString("DeviceAlert.bdSlotMisMatch2", source);
            break;
        case 4122:// 板槽位错误（主控板校验，接口板点灯）恢复
            result = getString("DeviceAlert.bdSlotMatch", source);
            break;
        case 4123:// 板性能统计超门限过高
            // alarm Info:4字节：具体统计项索引
            String thresholdHighDesc = getString(OltPerf.PERFNAME[Integer.parseInt(message.replaceAll(":", ""), 16)]);
            result = getString("DeviceAlert.bdPerfHigh", source, thresholdHighDesc);
            break;
        case 4124:// 板性能统计超门限过低
            // alarm Info:4字节：具体统计项索引
            String thresholdLowDesc = getString(OltPerf.PERFNAME[Integer.parseInt(message.replaceAll(":", ""), 16)]);
            result = getString("DeviceAlert.bdPerfLow", source, thresholdLowDesc);
            break;
        case 4130:// 内存使用率过高
            result = getString("DeviceAlert.memUsageHigh", source, message);
            break;
        case 4131:// 内存使用率过高恢复
            result = getString("DeviceAlert.memUsageNormal", source, message);
            break;
        case 16388:// MAC认证失败
            // alarm Info:ONU-MAC6字节
            // result = getString("DeviceAlert.macAuthfail", source, message);
            result = getString("DeviceAlert.macAuthfail2", source, message);
            break;
        case 16398:// 性能统计上限越界（当前15分钟）
            // alarm Info:4字节，统计项索引值
            String onuPerfHigh = getString(OltPerf.PERFNAME[Integer.parseInt(message.replaceAll(":", ""), 16)]);
            result = getString("DeviceAlert.onuPerfHigh", source, onuPerfHigh);
            break;
        case 16399:// 性能统计下限越界（当前15分钟）
            // alarm Info:4字节，统计项索引值
            String onuPerfLow = getString(OltPerf.PERFNAME[Integer.parseInt(message.replaceAll(":", ""), 16)]);
            result = getString("DeviceAlert.onuPerfLow", source, onuPerfLow);
            break;
        case 1:// 设备重启
            result = getString("DeviceAlert.systemReset", source);
            break;
        case 2:// 设备启动
            result = getString("DeviceAlert.systemStart", source);
            break;
        case 12290:// PON口光模块失效
            result = getString("DeviceAlert.portSfpFail", source);
            break;
        case 12293:// PON口光信号丢失
            result = getString("DeviceAlert.ponSignallost", source);
            break;
        case 12294:// PON口不可用恢复
            result = getString("DeviceAlert.ponEnable", source);
            break;
        case 12295:// PON LLID注册数量溢出
            result = getString("DeviceAlert.ponLlidExcd", source);
            break;
        case 12297:// PON口光模块拔出
            result = getString("DeviceAlert.opticalModuleRemove", source);
            break;
        case 12298:// PON口光模块插入
            result = getString("DeviceAlert.opticalModuleInsert", source);
            break;
        case 12299:// PON端口检测到长发光ONU
            result = getString("DeviceAlert.ponPortRogueOnu", source);
            break;
        case 12327:// PON系统检测到DOS攻击后的黑名单
            result = getString("DeviceAlert.ponBlackList", source);
            break;
        case 12328:// DOS攻击黑名单清除 12327对应的清除事件
            result = getString("DeviceAlert.ponBlackListClear", source);
            break;
        case 20481:// ONU的UNI口不可用/信号丢失/linkdown
            result = getString("DeviceAlert.onuLinkdown", message, source);
            break;
        case 20482:// ONU的UNI口LINK UP
            result = getString("DeviceAlert.onuLinkup", message, source);
            break;
        case 20483:// ONU下挂设备出现环路
            result = getString("DeviceAlert.onuLoop2", source);
            break;
        case 20484:// ONU以太网口环路消除
            result = getString("DeviceAlert.onuLoopClear", source);
            break;
        case 20485:// ONU以太网口自协商失败
            result = getString("DeviceAlert.onuAutoneg2", source, message);
            break;
        case 20486:// ONU以太网口自协商成功
            result = getString("DeviceAlert.onuAutonegSuccess", message, source);
            break;
        case 16385:// ONU严重事件告警
            result = getString("DeviceAlert.onuFatalerror", source);
            break;
        case 12325:// PON口光信号LOS告警
            result = getString("DeviceAlert.ponLos", source);
            break;
        case 12326:// PON口光信号LOS告警恢复
            result = getString("DeviceAlert.ponLosRecovery", source);
            break;
        case 7:// ONU升级
            /*
             * //0x00000001成功 if (message.substring(10, 11).equals("1")) { result =
             * getString("DeviceAlert.onuUpgradeSuccess"); } else { result =
             * getString("DeviceAlert.onuUpgradeFailure"); }
             */
            result = getString("DeviceAlert.onuUpgrade", source, message);
            break;
        case 9:
            // 文件上传 4字节，0x00000001成功 其它值是失败
            /*
             * if (message.substring(10, 11).equals("1")) { result =
             * getString("DeviceAlert.fileDownSuccess"); } else { result =
             * getString("DeviceAlert.fileUpFailure"); }
             */
            result = getString("DeviceAlert.fileUpload", message);
            break;
        case 11:
            // 文件下载 4字节，0x00000001成功；其它值是失败
            /*
             * if (message.substring(10, 11).equals("1")) { result =
             * getString("DeviceAlert.fileUpSuccess"); } else { result =
             * getString("DeviceAlert.fileUpgradeFailure"); }
             */
            result = getString("DeviceAlert.fileDownload", message);
            break;
        case 13:// 主备软件同步
            result = getString("DeviceAlert.swSyncOk", message);
            break;
        case 15:// 主备配置文件同步
            result = getString("DeviceAlert.configSyncOk", message);
            break;
        case 1537:// CDR记录主动上报
            result = getString("DeviceAlert.cdrReport", message);
            break;
        case 12315:
            result = getString("DeviceAlert.ponSfpRxPwr_hight", source, message);
            break;
        case 12316:
            result = getString("DeviceAlert.ponSfpRxPwr_low", source, message);
            break;
        case 12317:
            result = getString("DeviceAlert.ponSfpTxPwr_hight", source, message);
            break;
        case 12318:
            result = getString("DeviceAlert.ponSfpTxPwr_low", source, message);
            break;
        case 12311:
            result = getString("DeviceAlert.ponVoltageHigh", source);
            break;
        case 12312:
            result = getString("DeviceAlert.ponVoltageLow", source);
            break;
        case 16389:
            // result = getString("DeviceAlert.onuUpRxPwr_low");
            result = getString("DeviceAlert.onuUpRxPwr_low2", source, message);
            break;
        case 16390:
            // result = getString("DeviceAlert.onuUpRxPwr_hight");
            result = getString("DeviceAlert.onuUpRxPwr_hight2", source, message);
            break;
        case 53249:// ONU上行接收光功率正常
            result = getString("DeviceAlert.onuUpRxPwr_ok", source, message);
            break;
        case 16391:
            // result = getString("DeviceAlert.onuDsRxPwr_low");
            result = getString("DeviceAlert.onuDsRxPwr_low2", source, message);
            break;
        case 16392:
            // result = getString("DeviceAlert.onuDsRxPwr_hight");
            result = getString("DeviceAlert.onuDsRxPwr_hight2", source, message);
            break;
        case 53250:// ONU下行接收光功率正常
            result = getString("DeviceAlert.onuDsRxPwr_ok", source, message);
            break;
        case 3:
            // 主备倒换 4字节：0x00000001开始；0x00000002完成；0x00000003失败；0x00000004对板复位
            /*
             * if (message.substring(10, 11).equals("1")) { result =
             * getString("DeviceAlert.switchStart"); } else if (message.substring(10,
             * 11).equals("2")) { result = getString("DeviceAlert.switchSuccess"); } else if
             * (message.substring(10, 11).equals("3")) { result =
             * getString("DeviceAlert.switchFailure"); } else { result =
             * getString("DeviceAlert.switchRestart"); }
             */
            result = getString("DeviceAlert.slotSwitch", message);
            break;
        case 12305:// 光模块接受光功率过高
            result = getString("DeviceAlert.rxpwrHigh", source, message);
            break;
        case 12306:// 光模块接受光功率过低
            result = getString("DeviceAlert.rxpwrLow", source, message);
            break;
        case 12304:// 光模板接收光功率正常
            result = getString("DeviceAlert.rxpwrNormal", source, message);
            break;
        case 12307:// 光模块发送光功率过高
            result = getString("DeviceAlert.txpwrHigh", source, message);
            break;
        case 12308:// 光模块发送光功率过低
            result = getString("DeviceAlert.txpwrLow", source, message);
            break;
        case 12303:// 光模板接收光功率正常
            result = getString("DeviceAlert.txpwrNormal", source, message);
            break;
        case 19:// ARP源IP地址攻击
            result = getString("DeviceAlert.arpSrcIpAttack", source, message);
            break;
        case 21:// ARP源MAC地址攻击
            result = getString("DeviceAlert.arpSrcMacAttack", source, message);
            break;
        case 5126:// CPU接收ARP报文数低于下门限
            result = getString("DeviceAlert.cpuInArpPktsLow", source, message);
            break;
        case 5128:// CPU接收ARP报文字节数低于下门限
            result = getString("DeviceAlert.cpuInArpOctetsLow", source, message);
            break;
        case 5130:// CPU接收ARP速率低于下门限
            result = getString("DeviceAlert.cpuInArpRateLow", source, message);
            break;
        case 5382:// CPU接收ARP报文数超过上门限
            result = getString("DeviceAlert.cpuInArpPktsHigh", source, message);
            break;
        case 5384:// CPU接收ARP报文字节数超过上门限
            result = getString("DeviceAlert.cpuInArpOctetsHigh", source, message);
            break;
        case 5386:// CPU接收ARP速率超过上门限
            result = getString("DeviceAlert.cpuInArpRateHigh", source, message);
            break;
        case 5638:// CPU发送ARP报文数低于下门限
            result = getString("DeviceAlert.cpuOutArpPktsLow", source, message);
            break;
        case 5640:// CPU发送ARP报文字节数低于下门限
            result = getString("DeviceAlert.cpuOutArpOctetsLow", source, message);
            break;
        case 5642:// CPU发送ARP速率低于下门限
            result = getString("DeviceAlert.cpuOutArpRateLow", source, message);
            break;
        case 5894:// CPU发送ARP报文数超过上门限
            result = getString("DeviceAlert.cpuOutArpPktsHigh", source, message);
            break;
        case 5896:// CPU发送ARP报文字节数超过上门限
            result = getString("DeviceAlert.cpuOutArpOctetsHigh", source, message);
            break;
        case 5898:// CPU发送ARP速率超过上门限
            result = getString("DeviceAlert.cpuOutArpRateHigh", source, message);
            break;
        case 28673:// VLAN IF ARP学习数达到上限
            result = getString("DeviceAlert.arpLearnNumReachLimit", source, message);
            break;
        case 6145:
            result = getString("DeviceAlert.arpLearnHigh", source, message);
            break;
        case 53251:// ARP源IP地址攻击恢复 19对应的恢复事件
            result = getString("DeviceAlert.arpSrcIpAttackRec", source, message);
            break;
        case 53252:// ARP源MAC地址攻击恢复 21对应的恢复事件
            result = getString("DeviceAlert.arpSrcMacAttackRec", source, message);
            break;
        case 53253:// CPU接收ARP报文数正常 5126和5382对应的恢复事件
            result = getString("DeviceAlert.cpuInArpPktsNormal", source, message);
            break;
        case 53254:// CPU接收ARP报文字节数正常 5128和5384对应的恢复事件
            result = getString("DeviceAlert.cpuInArpOctetsNormal", source, message);
            break;
        case 53255:// CPU接收ARP速率正常 5130和5386对应的恢复事件
            result = getString("DeviceAlert.cpuInArpRateNormal", source, message);
            break;
        case 53256:// CPU发送ARP报文数正常 5638和5894对应的恢复事件
            result = getString("DeviceAlert.cpuOutArpPktsNormal", source, message);
            break;
        case 53257:// CPU发送ARP报文字节数正常 5640和5896对应的恢复事件
            result = getString("DeviceAlert.cpuOutArpOctetsNormal", source, message);
            break;
        case 53258:// CPU发送ARP速率正常 5642和5898对应的恢复事件
            result = getString("DeviceAlert.cpuOutArpRateNormal", source, message);
            break;
        case 53259:// VLAN IF ARP学习数正常 28673对应的恢复事件
            result = getString("DeviceAlert.arpLearnNumReachNormal", source, message);
            break;
        case 53260:// DEVICE ARP学习数正常对应的恢复事件
            result = getString("DeviceAlert.deviceArpLearnNumNormal", source, message);
            break;

        case 993:// CATV ONU告警
            result = getString("DeviceAlert.tempHigh", source, message);
            break;
        case 994:// CATV ONU告警
            result = getString("DeviceAlert.tempLow", source, message);
            break;
        case 995:// CATV ONU告警
            result = getString("DeviceAlert.RxPowerHigh", source, message);
            break;
        case 996:
            result = getString("DeviceAlert.RxPowerLow", source, message);
            break;
        case 997:
            result = getString("DeviceAlert.TxPowerHigh", source, message);
            break;
        case 998:
            result = getString("DeviceAlert.TxPowerLow", source, message);
            break;
        case 999:
            result = getString("DeviceAlert.VoltageHigh", source, message);
            break;
        case 1000:
            result = getString("DeviceAlert.VoltageLow", source, message);
            break;
        default:
            result = source + "(" + message + ")";
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.TrapParser#getTrapCos()
     */
    @Override
    public Integer getTrapCos() {
        return cos;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
