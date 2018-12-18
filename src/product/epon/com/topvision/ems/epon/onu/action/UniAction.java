/***********************************************************************
 * $Id: OnuAction.java,v1.0 2013-10-25 上午11:18:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.exception.UniVlanConfigException;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onu.service.ElectricityOnuService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.UniService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-上午11:18:33
 *
 */
@Controller("uniAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UniAction extends BaseAction {
    private static final long serialVersionUID = -6098632866567931493L;
    private final Logger logger = LoggerFactory.getLogger(UniAction.class);

    private Long entityId;
    private Long uniId;
    private Integer operationResult;
    private Long topUniAttrMacAddrLearnMaxNum;
    private Integer unicastStormEnable;
    private Long unicastStormInPacketRate;
    private Long unicastStormOutPacketRate;
    private Integer multicastStormEnable;
    private Long multicastStormInPacketRate;
    private Long multicastStormOutPacketRate;
    private Integer broadcastStormEnable;
    private Long broadcastStormInPacketRate;
    private Long broadcastStormOutPacketRate;
    private Integer uniAutoNegoEnable;
    private Integer uniFlowCtrlEnable;
    private Integer uni15minEnable;
    private Integer uni24hEnable;
    private Integer uniPortInCBS;
    private Integer uniPortInCIR;
    private Integer uniPortInEBS;
    private Integer uniInEnable;
    private Integer uniPortOutCIR;
    private Integer uniPortOutPIR;
    private Integer uniOutEnable;
    private Integer uniAutoNegotiationStatus;
    private Integer uniAutoNegoMode;
    private Integer uniAdminStatus;
    private Integer uniIsolationEnable;
    private Integer uniMacAddrAgeTime;
    private Integer uniDSLoopBackEnable;
    private Integer topUniLoopDetectEnable;
    private Integer uniUSUtgPri;
    private Long uniIndex;
    private Integer onuType;
    private OltUniExtAttribute oltUniExtAttribute;
    private OltUniStormSuppressionEntry uniStorm;
    private OltUniAttribute onuUniAttribute;
    private OltUniPortRateLimit oltUniPortRateLimit;
    @Autowired
    private OnuService onuService;
    @Autowired
    private UniService uniService;
    @Autowired
    private ElectricityOnuService electricityOnuService;

    /**
     * 获得ONU指定UNI端口属性
     * 
     * @return String
     */
    // TODO 没有找到对应的配置文件，也没有相应的调用
    public String getOnuUniAttribute() {
        onuUniAttribute = onuService.getOnuUniAttribute(uniId);
        return SUCCESS;
    }

    /**
     * 更新UNI端口属性
     * 
     * @return String
     */
    // TODO 没有找到对应的配置文件，也没有相应的调用
    @OperationLogProperty(actionName = "uniAction", operationName = "updateOnuUniAttribute")
    public String updateOnuUniAttribute() {
        try {
            onuService.updateOnuUniAttribute(onuUniAttribute);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("updateOnuUniAttribute error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * 显示UNI的MAC地址管理
     * 
     * @return String
     */
    public String showUniMacAddrMgmt() {
        return SUCCESS;
    }

    /**
     * 显示UNI的MAC地址最大学习数设置
     * 
     * @return String
     */
    public String showUniMacAddrCap() {
        topUniAttrMacAddrLearnMaxNum = uniService.getUniAttrMacAddrLearnMaxNum(uniId);
        return SUCCESS;
    }

    /**
     * 修改uni mac地址最大学习数
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "modifyUniMacAddrLearnMaxNum")
    public String modifyUniMacAddrLearnMaxNum() throws Exception {
        String result;
        try {
            uniService.modifyUniMacAddrLearnMaxNum(entityId, uniId, topUniAttrMacAddrLearnMaxNum);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            result = getString(sce.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView modifyUniMacAddrLearnMaxNum error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 刷新uni mac地址最大学习数
     * 
     * @return
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "refreshUniUSUtgPri")
    public String refreshUniUSUtgPri() {
        String message = "error";
        try {
            uniService.refreshUniUSUtgPri(entityId, uniId);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            logger.debug("refreshUniUSUtgPri error", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示UNI广播风暴抑制管理
     * 
     * @return String
     */
    public String showUniBroadCastStromMgmt() {
        uniStorm = uniService.getUniStormSuppressionByUniId(uniId);
        return SUCCESS;
    }

    /**
     * 更新UNI广播风暴抑制管理
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "updateUniBroadCastStromMgmt")
    public String updateUniBroadCastStromMgmt() throws Exception {
        String result;
        uniStorm = new OltUniStormSuppressionEntry();
        uniStorm.setEntityId(entityId);
        uniStorm.setUniId(uniId);
        uniStorm.setUnicastStormEnable(unicastStormEnable);
        uniStorm.setUnicastStormInPacketRate(unicastStormInPacketRate);
        uniStorm.setUnicastStormOutPacketRate(unicastStormOutPacketRate);
        uniStorm.setMulticastStormEnable(multicastStormEnable);
        uniStorm.setMulticastStormInPacketRate(multicastStormInPacketRate);
        uniStorm.setMulticastStormOutPacketRate(multicastStormOutPacketRate);
        uniStorm.setBroadcastStormEnable(broadcastStormEnable);
        uniStorm.setBroadcastStormInPacketRate(broadcastStormInPacketRate);
        uniStorm.setBroadcastStormOutPacketRate(broadcastStormOutPacketRate);
        try {
            uniService.modifyUniStormInfo(uniStorm);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("updateUniBroadCastStromMgmt error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示UNI端口限速管理
     * 
     * @return String
     */
    public String showUniRateLimit() {
        oltUniPortRateLimit = uniService.getUniRateLimitInfo(uniId);
        return SUCCESS;
    }

    /**
     * 显示UNI端口限速管理
     * 
     * @return String
     */
    public String showUniRateLimitBak() {
        oltUniPortRateLimit = uniService.getUniRateLimitInfo(uniId);
        return SUCCESS;
    }

    /**
     * 从设备刷新UNI端口限速
     * 
     * @return String
     * @throws Exception
     */
    public String refreshUniRateLimit() throws Exception {
        String result;
        try {
            uniService.refreshUniRateLimit(entityId, uniId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("refreshUniRateLimit error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * UNI 服务提供使能
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setOnuUniAdminStatus")
    public String setOnuUniAdminStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUniAdminStatus(entityId, uniId, uniAdminStatus);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * UNI 端口隔离使能
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniIsolationEnableStatus")
    public String setUniIsolationEnableStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUniIsolationEnable(entityId, uniId, uniIsolationEnable);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 显示UNI端口自协商配置管理
     * 
     * @return String
     */
    public String showUniAutoNegoConfig() {
        oltUniExtAttribute = uniService.getOltUniExtAttribute(uniId);
        OltUniAttribute uni = onuService.getOnuUniAttribute(uniId);
        if (uni != null) {
            uniAutoNegoEnable = uni.getUniAutoNegotiationEnable();
        }
        return SUCCESS;
    }

    /**
     * UNI 流控使能
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniAttrFlowCtrlStatus")
    public String setUniAttrFlowCtrlStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUniFlowCtrlEnable(entityId, uniId, uniFlowCtrlEnable);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * UNI 15min性能统计使能
     * 
     * @return String
     */
    public String setUni15MinPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUni15minEnable(entityId, uniId, uni15minEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * UNI 24h性能统计使能
     * 
     * @return String
     */
    public String setOnuUni24HourPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUni24hEnable(entityId, uniId, uni24hEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * UNI 端口限速管理信息更新
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "updateUniPortRateLimit")
    public String updateUniPortRateLimit() throws Exception {
        String result;
        OltUniPortRateLimit oltUniPortRateLimit = new OltUniPortRateLimit();
        oltUniPortRateLimit.setEntityId(entityId);
        oltUniPortRateLimit.setUniId(uniId);
        oltUniPortRateLimit.setUniPortInCBS(uniPortInCBS);
        oltUniPortRateLimit.setUniPortInCIR(uniPortInCIR);
        oltUniPortRateLimit.setUniPortInEBS(uniPortInEBS);
        oltUniPortRateLimit.setUniPortInRateLimitEnable(uniInEnable);
        oltUniPortRateLimit.setUniPortOutCIR(uniPortOutCIR);
        oltUniPortRateLimit.setUniPortOutPIR(uniPortOutPIR);
        oltUniPortRateLimit.setUniPortOutRateLimitEnable(uniOutEnable);
        try {
            uniService.modifyUniRateLimitInfo(oltUniPortRateLimit);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = getString(sce.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("uni portRateLimit config error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * UNI 重新自协商
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setRestartUniAutoNego")
    public String setRestartUniAutoNego() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.restartUniAutoNego(entityId, uniId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("setRestartUniAutoNego error: {}", e);
            operationResult = OperationLog.FAILURE;
            message.put("message", e.getMessage());
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * UNI 自协商使能
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniAutoNegoActive")
    public String setUniAutoNegoActive() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUniAutoNegoEnable(entityId, uniId, uniAutoNegoEnable);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 更新UNI端口自协商状态
     * 
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniAutoNegotiationStat")
    public String setUniAutoNegotiationStat() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            uniService.setUniAutoNegotiationStatus(entityId, uniId, uniAutoNegotiationStatus);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    // TODO 此处两个方法都是修改自协商状态，私有、公有各有一个可以设置自协商状态的节点，该处需要进一步确认使用哪个。
    /**
     * UNI端口自协商模式
     * 
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniAutoNegoModeType")
    public String setUniAutoNegoModeType() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            if (uniAutoNegoMode != 0) {
                uniService.updateOltUniAutoNegotiationMode(entityId, uniId, uniAutoNegoMode);
            }
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", svce.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.debug("uni autoNego error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * UNI端口MAC地址老化时间页面跳转
     * 
     * @return String
     */
    public String showUniMacAgeTime() {
        oltUniExtAttribute = uniService.getOltUniExtAttribute(uniId);
        return SUCCESS;
    }

    /**
     * UNI端口MAC地址老化时间
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "saveUniMacAgeTime")
    public String saveUniMacAgeTime() throws Exception {
        String message;
        OltUniExtAttribute uniExtAttribute = new OltUniExtAttribute();
        uniExtAttribute.setEntityId(entityId);
        uniExtAttribute.setUniId(uniId);
        uniExtAttribute.setMacAge(uniMacAddrAgeTime);
        try {
            uniService.modifyUniMacAgeTime(uniExtAttribute);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception sce) {
            message = getString(sce.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("error:{}", sce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 设置下行报文环回使能
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniDSLoopBackEnable")
    public String setUniDSLoopBackEnable() throws Exception {
        Map<String, String> json = new HashMap<String, String>();
        json.put("message", "success");
        try {
            electricityOnuService.setUniDSLoopBackEnable(entityId, uniId, uniDSLoopBackEnable);
        } catch (Exception e) {
            json.put("message", "fail");
            logger.info("setUniDSLoopBackEnable error:{}", e.getMessage());
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 配置ONU UNI端口环回使能
     * 
     * @return
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "configUniLoopDetectEnable")
    public String configUniLoopDetectEnable() {
        electricityOnuService.configUniLoopDetectEnable(entityId, uniId, topUniLoopDetectEnable);
        return NONE;
    }

    /**
     * 设置上行utag报文指定优先级
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniAction", operationName = "setUniUSUtgPri")
    public String setUniUSUtgPri() throws Exception {
        String mes = "success";
        try {
            electricityOnuService.setUniUSUtgPri(entityId, uniId, uniUSUtgPri);
        } catch (Exception e) {
            mes = "fail";
            logger.info("setUniUSUtgPri error:{}", e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 跳转到UNI上行untag报文优先级设置页面
     * 
     * @return
     */
    public String showUniUSUtgPri() {
        OltUniExtAttribute v = uniService.getOltUniExtAttribute(uniId);
        uniIndex = 0L;
        if (v != null) {
            uniIndex = v.getUniIndex();
        }
        if (v != null && v.getUniUSUtgPri() != null && v.getUniUSUtgPri() < 8 && v.getUniUSUtgPri() > -1) {
            uniUSUtgPri = v.getUniUSUtgPri();
        } else {
            uniUSUtgPri = 255;
        }
        return SUCCESS;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Long getTopUniAttrMacAddrLearnMaxNum() {
        return topUniAttrMacAddrLearnMaxNum;
    }

    public void setTopUniAttrMacAddrLearnMaxNum(Long topUniAttrMacAddrLearnMaxNum) {
        this.topUniAttrMacAddrLearnMaxNum = topUniAttrMacAddrLearnMaxNum;
    }

    public Integer getUnicastStormEnable() {
        return unicastStormEnable;
    }

    public void setUnicastStormEnable(Integer unicastStormEnable) {
        this.unicastStormEnable = unicastStormEnable;
    }

    public Long getUnicastStormInPacketRate() {
        return unicastStormInPacketRate;
    }

    public void setUnicastStormInPacketRate(Long unicastStormInPacketRate) {
        this.unicastStormInPacketRate = unicastStormInPacketRate;
    }

    public Long getUnicastStormOutPacketRate() {
        return unicastStormOutPacketRate;
    }

    public void setUnicastStormOutPacketRate(Long unicastStormOutPacketRate) {
        this.unicastStormOutPacketRate = unicastStormOutPacketRate;
    }

    public Integer getMulticastStormEnable() {
        return multicastStormEnable;
    }

    public void setMulticastStormEnable(Integer multicastStormEnable) {
        this.multicastStormEnable = multicastStormEnable;
    }

    public Long getMulticastStormInPacketRate() {
        return multicastStormInPacketRate;
    }

    public void setMulticastStormInPacketRate(Long multicastStormInPacketRate) {
        this.multicastStormInPacketRate = multicastStormInPacketRate;
    }

    public Long getMulticastStormOutPacketRate() {
        return multicastStormOutPacketRate;
    }

    public void setMulticastStormOutPacketRate(Long multicastStormOutPacketRate) {
        this.multicastStormOutPacketRate = multicastStormOutPacketRate;
    }

    public Integer getBroadcastStormEnable() {
        return broadcastStormEnable;
    }

    public void setBroadcastStormEnable(Integer broadcastStormEnable) {
        this.broadcastStormEnable = broadcastStormEnable;
    }

    public Long getBroadcastStormInPacketRate() {
        return broadcastStormInPacketRate;
    }

    public void setBroadcastStormInPacketRate(Long broadcastStormInPacketRate) {
        this.broadcastStormInPacketRate = broadcastStormInPacketRate;
    }

    public Long getBroadcastStormOutPacketRate() {
        return broadcastStormOutPacketRate;
    }

    public void setBroadcastStormOutPacketRate(Long broadcastStormOutPacketRate) {
        this.broadcastStormOutPacketRate = broadcastStormOutPacketRate;
    }

    public Integer getUniAutoNegoEnable() {
        return uniAutoNegoEnable;
    }

    public void setUniAutoNegoEnable(Integer uniAutoNegoEnable) {
        this.uniAutoNegoEnable = uniAutoNegoEnable;
    }

    public Integer getUniFlowCtrlEnable() {
        return uniFlowCtrlEnable;
    }

    public void setUniFlowCtrlEnable(Integer uniFlowCtrlEnable) {
        this.uniFlowCtrlEnable = uniFlowCtrlEnable;
    }

    public Integer getUni15minEnable() {
        return uni15minEnable;
    }

    public void setUni15minEnable(Integer uni15minEnable) {
        this.uni15minEnable = uni15minEnable;
    }

    public Integer getUni24hEnable() {
        return uni24hEnable;
    }

    public void setUni24hEnable(Integer uni24hEnable) {
        this.uni24hEnable = uni24hEnable;
    }

    public Integer getUniPortInCBS() {
        return uniPortInCBS;
    }

    public void setUniPortInCBS(Integer uniPortInCBS) {
        this.uniPortInCBS = uniPortInCBS;
    }

    public Integer getUniPortInCIR() {
        return uniPortInCIR;
    }

    public void setUniPortInCIR(Integer uniPortInCIR) {
        this.uniPortInCIR = uniPortInCIR;
    }

    public Integer getUniPortInEBS() {
        return uniPortInEBS;
    }

    public void setUniPortInEBS(Integer uniPortInEBS) {
        this.uniPortInEBS = uniPortInEBS;
    }

    public Integer getUniInEnable() {
        return uniInEnable;
    }

    public void setUniInEnable(Integer uniInEnable) {
        this.uniInEnable = uniInEnable;
    }

    public Integer getUniPortOutCIR() {
        return uniPortOutCIR;
    }

    public void setUniPortOutCIR(Integer uniPortOutCIR) {
        this.uniPortOutCIR = uniPortOutCIR;
    }

    public Integer getUniPortOutPIR() {
        return uniPortOutPIR;
    }

    public void setUniPortOutPIR(Integer uniPortOutPIR) {
        this.uniPortOutPIR = uniPortOutPIR;
    }

    public Integer getUniOutEnable() {
        return uniOutEnable;
    }

    public void setUniOutEnable(Integer uniOutEnable) {
        this.uniOutEnable = uniOutEnable;
    }

    public Integer getUniAutoNegotiationStatus() {
        return uniAutoNegotiationStatus;
    }

    public void setUniAutoNegotiationStatus(Integer uniAutoNegotiationStatus) {
        this.uniAutoNegotiationStatus = uniAutoNegotiationStatus;
    }

    public Integer getUniAutoNegoMode() {
        return uniAutoNegoMode;
    }

    public void setUniAutoNegoMode(Integer uniAutoNegoMode) {
        this.uniAutoNegoMode = uniAutoNegoMode;
    }

    public Integer getUniAdminStatus() {
        return uniAdminStatus;
    }

    public void setUniAdminStatus(Integer uniAdminStatus) {
        this.uniAdminStatus = uniAdminStatus;
    }

    public Integer getUniIsolationEnable() {
        return uniIsolationEnable;
    }

    public void setUniIsolationEnable(Integer uniIsolationEnable) {
        this.uniIsolationEnable = uniIsolationEnable;
    }

    public Integer getUniMacAddrAgeTime() {
        return uniMacAddrAgeTime;
    }

    public void setUniMacAddrAgeTime(Integer uniMacAddrAgeTime) {
        this.uniMacAddrAgeTime = uniMacAddrAgeTime;
    }

    public Integer getUniDSLoopBackEnable() {
        return uniDSLoopBackEnable;
    }

    public void setUniDSLoopBackEnable(Integer uniDSLoopBackEnable) {
        this.uniDSLoopBackEnable = uniDSLoopBackEnable;
    }

    public Integer getTopUniLoopDetectEnable() {
        return topUniLoopDetectEnable;
    }

    public void setTopUniLoopDetectEnable(Integer topUniLoopDetectEnable) {
        this.topUniLoopDetectEnable = topUniLoopDetectEnable;
    }

    public Integer getUniUSUtgPri() {
        return uniUSUtgPri;
    }

    public void setUniUSUtgPri(Integer uniUSUtgPri) {
        this.uniUSUtgPri = uniUSUtgPri;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public OltUniExtAttribute getOltUniExtAttribute() {
        return oltUniExtAttribute;
    }

    public void setOltUniExtAttribute(OltUniExtAttribute oltUniExtAttribute) {
        this.oltUniExtAttribute = oltUniExtAttribute;
    }

    public OltUniStormSuppressionEntry getUniStorm() {
        return uniStorm;
    }

    public void setUniStorm(OltUniStormSuppressionEntry uniStorm) {
        this.uniStorm = uniStorm;
    }

    public OltUniPortRateLimit getOltUniPortRateLimit() {
        return oltUniPortRateLimit;
    }

    public void setOltUniPortRateLimit(OltUniPortRateLimit oltUniPortRateLimit) {
        this.oltUniPortRateLimit = oltUniPortRateLimit;
    }

    public void setOnuUniAttribute(OltUniAttribute onuUniAttribute) {
        this.onuUniAttribute = onuUniAttribute;
    }

    public Integer getOnuType() {
        return onuType;
    }

    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }

}
