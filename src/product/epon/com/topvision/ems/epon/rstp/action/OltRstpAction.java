/***********************************************************************
 * $Id: OltRstpAction.java,v1.0 2013-10-25 下午5:31:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.ems.epon.rstp.service.OltRstpService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-下午5:31:01
 *
 */
@Controller("oltRstpAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltRstpAction extends BaseAction {
    private static final long serialVersionUID = 515888357573379888L;
    private final Logger logger = LoggerFactory.getLogger(OltRstpAction.class);

    private Long entityId;
    private Long onuId;
    private Integer stpGlobalSetEnable;
    private Integer stpGlobalSetVersion;
    private Integer stpGlobalSetPriority;
    private Integer stpGlobalSetBridgeMaxAge;
    private Integer stpGlobalSetBridgeHelloTime;
    private Integer stpGlobalSetBridgeForwardDelay;
    private Integer stpGlobalSetRstpTxHoldCount;
    private Integer stpPortEnabled;
    private Long portId;
    private Integer status;
    private JSONArray oltStpPortConfigObject;
    private Integer stpPortPriority;
    private Integer stpPortPathCost;
    private Integer stpPortRstpAdminEdgePort;
    private Integer stpPortPointToPointAdminStatus;
    private Long sniId;
    private Long sniIndex;
    private Integer operationResult;
    private Integer onuRstpBridgeMode;
    // 用于操作日志国际化时表示来源的变量
    private String source;
    private OltOnuRstp oltOnuRstp;
    private OltStpGlobalConfig oltStpGlobalConfig;
    private OltStpPortConfig oltStpPortConfig;
    @Autowired
    private OltRstpService oltRstpService;
    @Autowired
    private OltService oltService;
    @Autowired
    private OnuService onuService;
    private Integer sniAutoNegotiationMode;

    /**
     * 获取OLT设备stp基本配置信息
     * 
     * @return
     * @throws Exception
     */
    public String getStpGlobalConfigAttribute() throws Exception {
        oltStpGlobalConfig = oltRstpService.getOltStpGlobalConfig(entityId);
        return SUCCESS;
    }

    /**
     * 更新OLT设备stp配置信息
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltRstpAction", operationName = "updateStpGlobalConfig")
    public String updateStpGlobalConfig() throws Exception {
        String message = "success";
        OltStpGlobalConfig stpGlobalConfig = new OltStpGlobalConfig();
        stpGlobalConfig.setEntityId(entityId);
        stpGlobalConfig.setEnable(stpGlobalSetEnable);
        stpGlobalConfig.setVersion(stpGlobalSetVersion);
        stpGlobalConfig.setPriority(stpGlobalSetPriority);
        stpGlobalConfig.setBridgeForwardDelay(stpGlobalSetBridgeForwardDelay * 100);
        stpGlobalConfig.setBridgeHelloTime(stpGlobalSetBridgeHelloTime * 100);
        stpGlobalConfig.setBridgeMaxAge(stpGlobalSetBridgeMaxAge * 100);
        stpGlobalConfig.setRstpTxHoldCount(stpGlobalSetRstpTxHoldCount);
        try {
            oltRstpService.updateStpGlobalConfig(entityId, stpGlobalConfig);
            oltRstpService.refreshOltStpPortConfig(entityId);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpNoResponseException excp) {
            logger.debug("setStpPortEnable Error:{}", excp);
            for (int i = 0; i < 3; i++) {
                try {
                    // 如果开启RSTP使能时返回response没有成功,捕获到SnmpNoResponseException异常
                    // 那么就等10s后去刷新设备,取得设备当前值跟设置的值比较,如果相同则认为成功,如果不相同则认为失败
                    Thread.sleep(10000L);
                    // 刷新设备
                    oltRstpService.refreshOltStpGlobalConfig(entityId);
                    // 获得当前设备上的值与设置的值进行比较
                    OltStpGlobalConfig globalConfig = oltRstpService.getOltStpGlobalConfig(entityId);
                    if (globalConfig.getEnable().equals(stpGlobalSetEnable)) {
                        message = "success";
                    } else {
                        message = "error[" + excp.getMessage() + "]";
                    }
                    break;
                } catch (Exception e) {
                    message = "error[" + e.getMessage() + "]";
                }
            }
        } catch (Exception e) {
            message = "error[" + e.getMessage() + "]";
            operationResult = OperationLog.FAILURE;
            logger.debug("updateStpGlobalConfig is error:{}", e);
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * OLT设备RSTP使能
     * 
     * @return
     * @throws Exception
     */
    public String setStpGlobalEnable() throws Exception {
        String result = "success";
        List<Long> enList = new ArrayList<Long>();
        try {
            oltRstpService.setStpGlobalSetEnable(entityId, stpGlobalSetEnable);
            enList = oltRstpService.getEnablePortList(entityId);
            result = enList.toString();
        } catch (SnmpNoResponseException excp) {
            logger.debug("setStpPortEnable Error:{}", excp);
            for (int i = 0; i < 3; i++) {
                try {
                    // 如果开启RSTP使能时返回response没有成功,捕获到SnmpNoResponseException异常
                    // 那么就等10s后去刷新设备,取得设备当前值跟设置的值比较,如果相同则认为成功,如果不相同则认为失败
                    Thread.sleep(10000L);
                    // 刷新设备
                    oltRstpService.refreshOltStpGlobalConfig(entityId);
                    // 获得当前设备上的值与设置的值进行比较
                    OltStpGlobalConfig globalConfig = oltRstpService.getOltStpGlobalConfig(entityId);
                    if (globalConfig.getEnable() == stpGlobalSetEnable.intValue()) {
                        result = "success";
                    } else {
                        result = "error";
                    }
                    break;
                } catch (Exception e) {
                    result = "error";
                }
            }
        } catch (Exception sce) {
            logger.debug("setStpPortEnable Error:{}", sce);
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 刷新设备RSTP
     * 
     * @return
     * @throws Exception
     */
    public String refreshStpGlobalConfig() throws Exception {
        String message;
        try {
            oltRstpService.refreshOltStpGlobalConfig(entityId);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + "]";
            logger.debug("refreshStpGlobalConfig error:{}", message);
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取SNI端口stp基本配置信息
     * 
     * @return
     * @throws Exception
     */
    public String getStpPortConfigAttribute() throws Exception {
        oltStpGlobalConfig = oltRstpService.getOltStpGlobalConfig(entityId);
        oltStpPortConfig = oltRstpService.getOltStpPortConfig(entityId, portId);
        oltStpPortConfigObject = JSONArray.fromObject(oltStpPortConfig);
        return SUCCESS;
    }

    /**
     * 更新SNI端口stp配置信息
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltRstpAction", operationName = "updateStpPortConfig")
    public String updateStpPortConfig() throws Exception {
        String message;
        try {
            OltStpPortConfig stpPortConfig = new OltStpPortConfig();
            stpPortConfig.setEntityId(entityId);
            stpPortConfig.setSniId(sniId);
            stpPortConfig.setSniIndex(sniIndex);
            stpPortConfig.setStpPortEnabled(stpPortEnabled);
            stpPortConfig.setStpPortPriority(stpPortPriority);
            stpPortConfig.setStpPortPathCost(stpPortPathCost);
            stpPortConfig.setStpPortRstpAdminEdgePort(stpPortRstpAdminEdgePort);
            stpPortConfig.setStpPortPointToPointAdminStatus(stpPortPointToPointAdminStatus);
            oltRstpService.updateStpPortConfig(entityId, stpPortConfig);
            source = EponIndex.getPortStringByIndex(sniIndex).toString();
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = "error[" + e.getMessage() + "]";
            operationResult = OperationLog.FAILURE;
            logger.debug("updateStpPortConfig error:{}", e);
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * SNI端口RSTP使能
     * 
     * @return
     * @throws Exception
     */
    public String setStpPortEnable() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltRstpService.setStpPortEnabled(entityId, sniId, stpPortEnabled);
        } catch (Exception sce) {
            message.put("message", sce.getMessage());
            logger.debug("setStpPortEnable Error:{}", sce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * SNI端口协议迁移
     * 
     * @return
     * @throws Exception
     */
    public String setRstpProtocolMigration() throws Exception {
        String result = "fail";
        try {
            oltRstpService.setPortRstpProtocolMigration(entityId, sniId, status);
            result = "success";
        } catch (Exception sce) {
            logger.debug("setRstpProtocolMigration Error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示OLT RSTP配置
     * 
     * @return String
     */
    public String showOltRstpConfig() {
        return SUCCESS;
    }

    /**
     * 显示Sni RSTP配置
     * 
     * @return String
     */
    public String showSniRstpConfig() {
        return SUCCESS;
    }

    /**
     * 刷新SNI端口RSTP
     * 
     * @return
     * @throws Exception
     */
    public String refreshStpPortConfig() throws Exception {
        String message;
        try {
            oltRstpService.refreshOltStpPortConfig(entityId);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + "]";
            logger.debug("refreshStpPortConfig error:{}", message);
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示ONU Rstp模式
     * 
     * @return String
     */
    public String showOnuRstpMode() {
        oltOnuRstp = onuService.getOltOnuRstpByOnuId(onuId);
        return SUCCESS;
    }

    /**
     * ONU RSTP桥模式设置
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "oltRstpAction", operationName = "modifyOnuRstpMode")
    public String modifyOnuRstpMode() throws Exception {
        String message;
        try {
            onuService.setOnuRstpBridgeMode(entityId, onuId, onuRstpBridgeMode);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("modifyOnuRstpMode error: {}", e);
            message = getString(e.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    @OperationLogProperty(actionName = "oltRstpAction", operationName = "fetchOnuRstpMode")
    public String fetchOnuRstpMode() {
        JSONObject json = new JSONObject();
        String message = null;
        OltOnuRstp oltOnuRstp = new OltOnuRstp();
        try {
            oltOnuRstp = onuService.getOltOnuRstp(entityId, onuId);
            if (oltOnuRstp.getRstpBridgeMode() == null) {
                message = "OltOnuRstp rstpBridgeMode is null!";
            } else {
                message = "success";
            }
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("fetchOnuRstpMode error: {}", e);
            message = getString(e.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
        }
        json.put("message", message);
        json.put("oltOnuRstp", oltOnuRstp);
        writeDataToAjax(json);
        return NONE;
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

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getStpGlobalSetEnable() {
        return stpGlobalSetEnable;
    }

    public void setStpGlobalSetEnable(Integer stpGlobalSetEnable) {
        this.stpGlobalSetEnable = stpGlobalSetEnable;
    }

    public Integer getStpGlobalSetVersion() {
        return stpGlobalSetVersion;
    }

    public void setStpGlobalSetVersion(Integer stpGlobalSetVersion) {
        this.stpGlobalSetVersion = stpGlobalSetVersion;
    }

    public Integer getStpGlobalSetPriority() {
        return stpGlobalSetPriority;
    }

    public void setStpGlobalSetPriority(Integer stpGlobalSetPriority) {
        this.stpGlobalSetPriority = stpGlobalSetPriority;
    }

    public Integer getStpGlobalSetBridgeMaxAge() {
        return stpGlobalSetBridgeMaxAge;
    }

    public void setStpGlobalSetBridgeMaxAge(Integer stpGlobalSetBridgeMaxAge) {
        this.stpGlobalSetBridgeMaxAge = stpGlobalSetBridgeMaxAge;
    }

    public Integer getStpGlobalSetBridgeHelloTime() {
        return stpGlobalSetBridgeHelloTime;
    }

    public void setStpGlobalSetBridgeHelloTime(Integer stpGlobalSetBridgeHelloTime) {
        this.stpGlobalSetBridgeHelloTime = stpGlobalSetBridgeHelloTime;
    }

    public Integer getStpGlobalSetBridgeForwardDelay() {
        return stpGlobalSetBridgeForwardDelay;
    }

    public void setStpGlobalSetBridgeForwardDelay(Integer stpGlobalSetBridgeForwardDelay) {
        this.stpGlobalSetBridgeForwardDelay = stpGlobalSetBridgeForwardDelay;
    }

    public Integer getStpGlobalSetRstpTxHoldCount() {
        return stpGlobalSetRstpTxHoldCount;
    }

    public void setStpGlobalSetRstpTxHoldCount(Integer stpGlobalSetRstpTxHoldCount) {
        this.stpGlobalSetRstpTxHoldCount = stpGlobalSetRstpTxHoldCount;
    }

    public Integer getStpPortEnabled() {
        return stpPortEnabled;
    }

    public void setStpPortEnabled(Integer stpPortEnabled) {
        this.stpPortEnabled = stpPortEnabled;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public JSONArray getOltStpPortConfigObject() {
        return oltStpPortConfigObject;
    }

    public void setOltStpPortConfigObject(JSONArray oltStpPortConfigObject) {
        this.oltStpPortConfigObject = oltStpPortConfigObject;
    }

    public Integer getStpPortPriority() {
        return stpPortPriority;
    }

    public void setStpPortPriority(Integer stpPortPriority) {
        this.stpPortPriority = stpPortPriority;
    }

    public Integer getStpPortPathCost() {
        return stpPortPathCost;
    }

    public void setStpPortPathCost(Integer stpPortPathCost) {
        this.stpPortPathCost = stpPortPathCost;
    }

    public Integer getStpPortRstpAdminEdgePort() {
        return stpPortRstpAdminEdgePort;
    }

    public void setStpPortRstpAdminEdgePort(Integer stpPortRstpAdminEdgePort) {
        this.stpPortRstpAdminEdgePort = stpPortRstpAdminEdgePort;
    }

    public Integer getStpPortPointToPointAdminStatus() {
        return stpPortPointToPointAdminStatus;
    }

    public void setStpPortPointToPointAdminStatus(Integer stpPortPointToPointAdminStatus) {
        this.stpPortPointToPointAdminStatus = stpPortPointToPointAdminStatus;
    }

    public Long getSniId() {
        return sniId;
    }

    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    public Long getSniIndex() {
        return sniIndex;
    }

    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public OltOnuRstp getOltOnuRstp() {
        return oltOnuRstp;
    }

    public void setOltOnuRstp(OltOnuRstp oltOnuRstp) {
        this.oltOnuRstp = oltOnuRstp;
    }

    public OltStpGlobalConfig getOltStpGlobalConfig() {
        return oltStpGlobalConfig;
    }

    public void setOltStpGlobalConfig(OltStpGlobalConfig oltStpGlobalConfig) {
        this.oltStpGlobalConfig = oltStpGlobalConfig;
    }

    public OltStpPortConfig getOltStpPortConfig() {
        return oltStpPortConfig;
    }

    public void setOltStpPortConfig(OltStpPortConfig oltStpPortConfig) {
        this.oltStpPortConfig = oltStpPortConfig;
    }

    public OltService getOltService() {
        return oltService;
    }

    public void setOltService(OltService oltService) {
        this.oltService = oltService;
    }

    public Integer getOnuRstpBridgeMode() {
        return onuRstpBridgeMode;
    }

    public void setOnuRstpBridgeMode(Integer onuRstpBridgeMode) {
        this.onuRstpBridgeMode = onuRstpBridgeMode;
    }

    public Integer getSniAutoNegotiationMode() {
        return sniAutoNegotiationMode;
    }

    public void setSniAutoNegotiationMode(Integer sniAutoNegotiationMode) {
        this.sniAutoNegotiationMode = sniAutoNegotiationMode;
    }

}
