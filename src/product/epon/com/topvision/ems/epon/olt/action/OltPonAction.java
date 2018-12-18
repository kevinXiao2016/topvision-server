/***********************************************************************
 * $Id: OltPonAction.java,v1.0 2013-10-25 上午10:38:59 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.util.HashMap;
import java.util.Map;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.TopPonPortRateLimit;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.service.PonPortVlanService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-上午10:38:59
 *
 */
@Controller("oltPonAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPonAction extends AbstractEponAction {
    private static final long serialVersionUID = 2473750870926857766L;
    private final Logger logger = LoggerFactory.getLogger(OltPonAction.class);
    private Long ponId;
    private String source;
    private Integer ponAdminStatus;
    private Integer ponIsolationStatus;
    private Integer pon15MinPerfStatus;
    private Integer pon24HourPerfStatus;
    private Integer ponPortEncryptMode;
    private Integer ponPortEncryptKeyExchangeTime;
    private Long ponPortMacAddrLearnMaxNum;
    private Integer ponSpeedMode;
    private Integer ponBandMax;
    private OltPonStormSuppressionEntry ponStormSuppression;
    private Integer unicastStormEnable;
    private Integer unicastStormInPacketRate;
    private Integer multicastStormEnable;
    private Integer multicastStormInPacketRate;
    private Integer broadcastStormEnable;
    private Integer broadcastStormInPacketRate;
    private TopPonPortSpeedEntry ponPortSpeedEntry;
    private Long portIndex;
    private Integer upLinkRate;
    private Integer downLinkRate;
    private String portType;
    private Integer vlanPvid;

    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private PonPortVlanService ponPortVlanService;
    
    /**
     * 设置PON口使能
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPonAdminStatus")
    public String modifyPonAdminStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltPonService.setPonAdminStatus(entityId, ponId, ponAdminStatus);
            source = getPortLoc(ponId, 0);
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            logger.debug("setPonAdminStatus Error:{}", sce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 设置PON口隔离使能
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPonIsolationStatus")
    public String modifyPonIsolationStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltPonService.setPonIsolationStatus(entityId, ponId, ponIsolationStatus);
            source = getPortLoc(ponId, 0);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setPonIsolationStatus Error:{}", sce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 设置PON口15分钟性能统计使能状态
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPon15MinPerfStatus")
    public String modifyPon15MinPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltPonService.setPon15MinPerfStatus(entityId, ponId, pon15MinPerfStatus);
            source = getPortLoc(ponId, 0);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setPon15MinPerfStatus Error:{}", sce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 设置PON口24小时性能统计使能状态
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPon24HourPerfStatus")
    public String modifyPon24HourPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltPonService.setPon24HourPerfStatus(entityId, ponId, pon24HourPerfStatus);
            source = getPortLoc(ponId, 0);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setPon24HourPerfStatus Error:{}", sce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * PON口加密模式（包括加密模式和密钥交换时间）
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPonPortEncryptMode")
    public String modifyPonPortEncryptMode() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltPonService.setPonPortEncryptMode(entityId, ponId, ponPortEncryptMode, ponPortEncryptKeyExchangeTime);
            // TODO 修改PON口最大带宽限制,仅到数据库的方法
            // oltService.modifyPonBandMax(ponId, ponBandMax);
            source = getPortLoc(ponId, 0);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException e) {
            message.put("message", getString(e.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setPonBasicInfo Error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取PON口MAC地址最大学习数
     * 
     * @return String
     */
    public String showPonMacLearnMaxNnm() {
        OltPonAttribute ponAttr = oltPonService.getPonAttribute(ponId);
        if (ponAttr != null) {
            ponPortMacAddrLearnMaxNum = ponAttr.getPonPortMacAddrLearnMaxNum();
        } else {
            ponPortMacAddrLearnMaxNum = EponConstants.PON_MAC_DEFAULT_MAX_LEARN_NUM;
        }
        return SUCCESS;
    }

    /**
     * 获取PON口MAC地址最大学习数
     * 针对1.6.X版本
     * @return String
     */
    public String showPonMacNum() {
        OltPonAttribute ponAttr = oltPonService.getPonAttribute(ponId);
        if (ponAttr != null) {
            ponPortMacAddrLearnMaxNum = ponAttr.getPonPortMacAddrLearnMaxNum();
        } else {
            ponPortMacAddrLearnMaxNum = EponConstants.PON_MAC_DEFAULT_MAX_LEARN_NUM2;
        }
        return SUCCESS;
    }

    /**
     * MAC地址最大学习数
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPonMaxLearnMacNum")
    public String modifyPonMaxLearnMacNum() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltPonService.setPonMaxLearnMacNum(entityId, ponId, ponPortMacAddrLearnMaxNum);
            source = getPortLoc(ponId, 0);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException e) {
            message.put("message", getString(e.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setPonBasicInfo Error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示PON口基本配置管理
     * 
     * @return String
     */
    public String showPonBaseConfig() {
        OltPonAttribute ponAttr = oltPonService.getPonAttribute(ponId);
        // TODO 入网测试用的假字段的初始化数据：PON口最大带宽限制
        ponBandMax = 1000;
        if (ponAttr != null && ponAttr.getPonBandMax() != null && ponAttr.getPonBandMax() > 0) {
            ponBandMax = ponAttr.getPonBandMax();
        }
        return SUCCESS;
    }

    /**
     * 显示PON口广播风暴管理
     * 
     * @return String
     */
    public String showPonBroadStormConfig() {
        ponStormSuppression = oltPonService.getPonStormSuppression(ponId);
        return SUCCESS;
    }
    
    /**
     * 修改PON口广播风暴参数
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "modifyPonBroadStormConfig")
    public String modifyPonBroadStormConfig() {
        OltPonStormSuppressionEntry oltPonStormSuppressionEntry = new OltPonStormSuppressionEntry();
        oltPonStormSuppressionEntry.setEntityId(entityId);
        oltPonStormSuppressionEntry.setPonId(ponId);
        oltPonStormSuppressionEntry.setUnicastStormEnable(unicastStormEnable);
        oltPonStormSuppressionEntry.setUnicastStormInPacketRate(unicastStormInPacketRate);
        oltPonStormSuppressionEntry.setMulticastStormEnable(multicastStormEnable);
        oltPonStormSuppressionEntry.setMulticastStormInPacketRate(multicastStormInPacketRate);
        oltPonStormSuppressionEntry.setBroadcastStormEnable(broadcastStormEnable);
        oltPonStormSuppressionEntry.setBroadcastStormInPacketRate(broadcastStormInPacketRate);
        try {
            oltPonService.modifyPonStormInfo(oltPonStormSuppressionEntry);
            source = getPortLoc(ponId, 0);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("modifyPonBroadStormConfig error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    public String refreshPonBroadCastStorm() {
        oltPonService.refreshOltPonStormSuppressionEntry(entityId);
        return NONE;
    }

    /**
     * 获取PON口或SNI口的loc
     * 
     * @param id
     * @param type
     *            : 0:ponId, 1:sniId
     * @return
     */
    private String getPortLoc(Long id, Integer type) {
        String loc = "";
        if (type.equals(0)) {// ponId
            id = oltPonService.getPonIndex(id);
        } else if (type.equals(1)) {// sniId
            id = oltSniService.getSniIndex(id);
        }
        loc = EponIndex.getSlotNo(id) + "/" + EponIndex.getSniNo(id);
        return loc;
    }

    /**
     * 显示PON端口工作模式
     * 
     * @return
     */
    public String showPonSpeedMode() {
        ponPortSpeedEntry = oltPonService.getPonPortSpeedMode(entityId, ponId);
        return SUCCESS;
    }


    /**
     * 跳转到PON口限速配置页面
     * 
     * @return
     */
    public String showPonPortLmt() {
        OltPonAttribute oltPonAttribute = oltPonService.getPonAttribute(ponId);
        upLinkRate = oltPonAttribute.getMaxUsBandwidth();
        downLinkRate = oltPonAttribute.getMaxDsBandwidth();
        portType = String.valueOf(oltPonAttribute.getPonPortType());
        return SUCCESS;
    }

    public String loadPonPortLmt() {
        Map<String, Integer> message = new HashMap<>();
        try {
            OltPonAttribute oltPonAttribute = oltPonService.loadPonPortLmt(entityId, ponId);
            upLinkRate = oltPonAttribute.getMaxUsBandwidth();
            downLinkRate = oltPonAttribute.getMaxDsBandwidth();
            message.put("status", 0);
            message.put("upLinkRate", upLinkRate);
            message.put("downLinkRate", downLinkRate);
            writeDataToAjax(message);
        } catch (Exception e) {
            logger.info("load Pon Port error", e);
            message.put("messsage", 1);
        }

        return NONE;
    }

    /**
     * 保存PON口限速配置信息
     * 
     * @return
     */
    public String savePonPortLmtCfg() throws Exception {
        String msg = "success";
        try {
            TopPonPortRateLimit topPonPortRateLimit = new TopPonPortRateLimit();
            topPonPortRateLimit.setEntityId(entityId);
            topPonPortRateLimit.setPonId(ponId);
            topPonPortRateLimit.setPonIndex(portIndex);
            topPonPortRateLimit.setPonPortUpRateLmt(upLinkRate);
            topPonPortRateLimit.setPonPortDownRateLmt(downLinkRate);
            oltPonService.setPonPortRateLimit(topPonPortRateLimit);
        } catch (Exception e) {
            msg = e.getMessage();
            logger.error("update pon limit failed:{}", e);
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 修改PON端口工作模式
     * 
     * @return
     * @throws Exception
     */
    public String modifyPonSpeedMode() throws Exception {
        String msg = "success";
        try {
            oltPonService.modifyPonPortSpeedMode(entityId, ponId, ponSpeedMode);
        } catch (Exception e) {
            msg = e.getMessage();
            logger.debug("modifyPonPortSpeedMode failed:{}", e);
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * PON口复位
     * @return
     */
    @OperationLogProperty(actionName = "oltPonAction", operationName = "ponReset")
    public String resetPon() {
        oltPonService.resetPon(entityId, ponId);
        source = getPortLoc(ponId, 0);
        return NONE;
    }

    /**
     * 显示PON口PVID配置
     * @return
     */
    public String showPonPvidConfig() {
        PortVlanAttribute ponVlan = ponPortVlanService.getPonVlan(ponId);
        if (ponVlan != null) {
            vlanPvid = ponVlan.getVlanPVid();
        }
        return SUCCESS;
    }

    /**
     * 修改PON口PVID配置
     * @return
     */
    public String modifyPonPvid() {
        PortVlanAttribute ponVlan = new PortVlanAttribute();
        ponVlan.setEntityId(entityId);
        ponVlan.setPortId(ponId);
        ponVlan.setPortIndex(portIndex);
        ponVlan.setVlanPVid(vlanPvid);
        ponPortVlanService.updatePonPvid(ponVlan);
        return NONE;
    }

    /**
     * 刷新PON口VLAN信息
     * @return
     */
    public String refreshPonVlanInfo() {
        ponPortVlanService.refreshPonVlanInfo(entityId, ponId, portIndex);
        return NONE;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPonAdminStatus() {
        return ponAdminStatus;
    }

    public void setPonAdminStatus(Integer ponAdminStatus) {
        this.ponAdminStatus = ponAdminStatus;
    }

    public Integer getPonIsolationStatus() {
        return ponIsolationStatus;
    }

    public void setPonIsolationStatus(Integer ponIsolationStatus) {
        this.ponIsolationStatus = ponIsolationStatus;
    }

    public Integer getPon15MinPerfStatus() {
        return pon15MinPerfStatus;
    }

    public void setPon15MinPerfStatus(Integer pon15MinPerfStatus) {
        this.pon15MinPerfStatus = pon15MinPerfStatus;
    }

    public Integer getPon24HourPerfStatus() {
        return pon24HourPerfStatus;
    }

    public void setPon24HourPerfStatus(Integer pon24HourPerfStatus) {
        this.pon24HourPerfStatus = pon24HourPerfStatus;
    }

    public Integer getPonPortEncryptMode() {
        return ponPortEncryptMode;
    }

    public void setPonPortEncryptMode(Integer ponPortEncryptMode) {
        this.ponPortEncryptMode = ponPortEncryptMode;
    }

    public Integer getPonPortEncryptKeyExchangeTime() {
        return ponPortEncryptKeyExchangeTime;
    }

    public void setPonPortEncryptKeyExchangeTime(Integer ponPortEncryptKeyExchangeTime) {
        this.ponPortEncryptKeyExchangeTime = ponPortEncryptKeyExchangeTime;
    }

    public Long getPonPortMacAddrLearnMaxNum() {
        return ponPortMacAddrLearnMaxNum;
    }

    public void setPonPortMacAddrLearnMaxNum(Long ponPortMacAddrLearnMaxNum) {
        this.ponPortMacAddrLearnMaxNum = ponPortMacAddrLearnMaxNum;
    }

    public Integer getPonSpeedMode() {
        return ponSpeedMode;
    }

    public void setPonSpeedMode(Integer ponSpeedMode) {
        this.ponSpeedMode = ponSpeedMode;
    }

    public Integer getPonBandMax() {
        return ponBandMax;
    }

    public void setPonBandMax(Integer ponBandMax) {
        this.ponBandMax = ponBandMax;
    }

    public OltPonStormSuppressionEntry getPonStormSuppression() {
        return ponStormSuppression;
    }

    public void setPonStormSuppression(OltPonStormSuppressionEntry ponStormSuppression) {
        this.ponStormSuppression = ponStormSuppression;
    }

    public Integer getUnicastStormEnable() {
        return unicastStormEnable;
    }

    public void setUnicastStormEnable(Integer unicastStormEnable) {
        this.unicastStormEnable = unicastStormEnable;
    }

    public Integer getUnicastStormInPacketRate() {
        return unicastStormInPacketRate;
    }

    public void setUnicastStormInPacketRate(Integer unicastStormInPacketRate) {
        this.unicastStormInPacketRate = unicastStormInPacketRate;
    }

    public Integer getMulticastStormEnable() {
        return multicastStormEnable;
    }

    public void setMulticastStormEnable(Integer multicastStormEnable) {
        this.multicastStormEnable = multicastStormEnable;
    }

    public Integer getMulticastStormInPacketRate() {
        return multicastStormInPacketRate;
    }

    public void setMulticastStormInPacketRate(Integer multicastStormInPacketRate) {
        this.multicastStormInPacketRate = multicastStormInPacketRate;
    }

    public Integer getBroadcastStormEnable() {
        return broadcastStormEnable;
    }

    public void setBroadcastStormEnable(Integer broadcastStormEnable) {
        this.broadcastStormEnable = broadcastStormEnable;
    }

    public Integer getBroadcastStormInPacketRate() {
        return broadcastStormInPacketRate;
    }

    public void setBroadcastStormInPacketRate(Integer broadcastStormInPacketRate) {
        this.broadcastStormInPacketRate = broadcastStormInPacketRate;
    }

    public TopPonPortSpeedEntry getPonPortSpeedEntry() {
        return ponPortSpeedEntry;
    }

    public void setPonPortSpeedEntry(TopPonPortSpeedEntry ponPortSpeedEntry) {
        this.ponPortSpeedEntry = ponPortSpeedEntry;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getUpLinkRate() {
        return upLinkRate;
    }

    public void setUpLinkRate(Integer upLinkRate) {
        this.upLinkRate = upLinkRate;
    }

    public Integer getDownLinkRate() {
        return downLinkRate;
    }

    public void setDownLinkRate(Integer downLinkRate) {
        this.downLinkRate = downLinkRate;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public Integer getVlanPvid() {
        return vlanPvid;
    }

    public void setVlanPvid(Integer vlanPvid) {
        this.vlanPvid = vlanPvid;
    }

}
