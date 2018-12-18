/***********************************************************************
 * $Id: OltDhcpAction.java,v1.0 2013-10-25 下午5:40:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.ems.epon.dhcp.service.OltDhcpService;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-下午5:40:53
 *
 */
@Controller("oltDhcpAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpAction extends BaseAction {
    private static final long serialVersionUID = 2928942812300817708L;
    @Autowired
    private OltDhcpService oltDhcpService;
    private OltDhcpBaseConfig baseConfig;
    private Long entityId;
    private String onuType;
    private Integer pppoeEnable;
    private Integer dhcpRelayMode;
    private Integer dhcpIpMacDynamicEnable;
    private Integer dhcpServerIndex;
    private Integer dhcpServerVid;
    private String dhcpServerIp;
    private String dhcpServerIpMask;
    private Integer dhcpGiaddrIndex;
    private Integer dhcpGiaddrVid;
    private String dhcpGiaddrIp;
    private Integer dhcpIpMacStaticIdx;
    private String dhcpIpMacStaticIp;
    private String dhcpIpMacStaticMac;
    private String dhcpIpMacStaticOnuMac;
    private JSONArray dhcpServerIndexObject;
    private JSONArray dhcpGiaddrIndexObject;
    private JSONArray onuTypeObject = new JSONArray();
    private Integer operationResult;

    public String showOltDhcpGiaddrAdd() {
        List<OltDhcpGiaddrConfig> dhcpGiaddrList = new ArrayList<OltDhcpGiaddrConfig>();
        List<OltDhcpGiaddrConfig> dhcpAllTypeList = new ArrayList<OltDhcpGiaddrConfig>();
        List<OltDhcpGiaddrConfig> dhcpExistTypeList = new ArrayList<OltDhcpGiaddrConfig>();
        dhcpGiaddrList = oltDhcpService.getDhcpGiaddrConfigs(entityId);
        dhcpAllTypeList = EponUtil.getDhcpGiaddrConfigList();
        for (OltDhcpGiaddrConfig config : dhcpGiaddrList) {
            for (OltDhcpGiaddrConfig typeConfig : dhcpAllTypeList) {
                if (typeConfig.equalsForIndex(config)) {
                    dhcpExistTypeList.add(typeConfig);
                }
            }
        }
        dhcpAllTypeList.removeAll(dhcpExistTypeList);
        dhcpGiaddrIndexObject = JSONArray.fromObject(dhcpAllTypeList);
        return SUCCESS;
    }

    public String showOltDhcpConfig() {
        baseConfig = oltDhcpService.getDhcpBaseConfig(entityId);
        return SUCCESS;
    }

    public String showOltDhcpServerConfig() {
        return SUCCESS;
    }

    public String showOltDhcpGiaddrConfig() {
        return SUCCESS;
    }

    public String showOltDhcpIpMacStaticConfig() {
        return SUCCESS;
    }

    public String showOltDhcpServerModify() {
        return SUCCESS;
    }

    public String showOltDhcpIpMacStaticModify() {
        return SUCCESS;
    }

    public String showOltDhcpIpMacStaticAdd() {
        return SUCCESS;
    }

    public String showOltDhcpGiaddrModify() {
        return SUCCESS;
    }

    public String showOltDhcpIpMacDynamicConfig() {
        return SUCCESS;
    }

    public String showOltDhcpServerAdd() {
        List<OltDhcpServerConfig> dhcpServerList = new ArrayList<OltDhcpServerConfig>();
        List<OltDhcpServerConfig> dhcpAllTypeList = new ArrayList<OltDhcpServerConfig>();
        List<OltDhcpServerConfig> dhcpExistTypeList = new ArrayList<OltDhcpServerConfig>();
        dhcpServerList = oltDhcpService.getDhcpServerConfigs(entityId);
        dhcpAllTypeList = EponUtil.getOltDhcpServerConfigList();
        for (OltDhcpServerConfig config : dhcpServerList) {
            for (OltDhcpServerConfig typeConfig : dhcpAllTypeList) {
                if (typeConfig.equalsForIndex(config)) {
                    dhcpExistTypeList.add(typeConfig);
                }
            }
        }
        dhcpAllTypeList.removeAll(dhcpExistTypeList);
        dhcpServerIndexObject = JSONArray.fromObject(dhcpAllTypeList);
        return SUCCESS;
    }

    /**
    * 修改DHCP基本配置
    * 
    * @return
    * @throws Exception
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "modifyDhcpBaseConfig")
    public String modifyDhcpBaseConfig() throws Exception {
        OltDhcpBaseConfig dhcpBaseConfig = new OltDhcpBaseConfig();
        dhcpBaseConfig.setEntityId(entityId);
        dhcpBaseConfig.setTopOltPPPOEPlusEnable(pppoeEnable);
        dhcpBaseConfig.setTopOltDHCPRelayMode(dhcpRelayMode);
        dhcpBaseConfig.setTopOltDHCPDyncIPMACBind(dhcpIpMacDynamicEnable);
        try {
            oltDhcpService.modifyDhcpBaseConfigs(dhcpBaseConfig);
            writeDataToAjax("modifyOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("modifyFail");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
    * 加载DHCP 静态绑定数据
    * 
    * @return
    * @throws IOException
    */
    public String loadDhcpIpMacStaticConfig() throws IOException {
        List<OltDhcpIpMacStatic> list = oltDhcpService.getDhcpIpMacStatics(entityId);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("totalProperty", list.size());
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
    * 加载DHCP 网关配置
    * 
    * @return
    * @throws IOException
    */
    public String loadDhcpGiaddrConfig() throws IOException {
        List<OltDhcpGiaddrConfig> list = oltDhcpService.getDhcpGiaddrConfigs(entityId);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("totalProperty", list.size());
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
    * 加载DHCP Server配置
    * 
    * @return
    * @throws IOException
    */
    public String loadDhcpServerBaseConfig() throws IOException {
        List<OltDhcpServerConfig> list = new ArrayList<OltDhcpServerConfig>();
        list = oltDhcpService.getDhcpServerConfigs(entityId);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("totalProperty", list.size());
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
    * 删除DHCP Server配置
    * 
    * @return
    * @throws Exception
    */
    @OperationLogProperty(actionName = "dhcpAction", operationName = "deleteDhcpServerIndex")
    public String deleteDhcpServerIndex() throws Exception {
        try {
            oltDhcpService.deleteDhcpServerConfigs(entityId, dhcpServerIndex);
            writeDataToAjax("deleteOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("deleteFailure");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
    * 新增DHCP Server配置
    * 
    * @return
    * @throws Exception
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "addDhcpServerConfig")
    public String addDhcpServerConfig() throws Exception {
        OltDhcpServerConfig dhcpServerConfig = new OltDhcpServerConfig();
        dhcpServerConfig.setEntityId(entityId);
        dhcpServerConfig.setTopOltDHCPServerIndex(dhcpServerIndex);
        dhcpServerConfig.setTopOltDHCPServerVid(dhcpServerVid);
        dhcpServerConfig.setTopOltDHCPServerIpAddr(dhcpServerIp);
        dhcpServerConfig.setTopOltDHCPServerIpMask(dhcpServerIpMask);
        try {
            oltDhcpService.insertDhcpServerConfigs(dhcpServerConfig);
            writeDataToAjax("addOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("addFailure");
            operationResult = OperationLog.FAILURE;
        }

        return NONE;
    }

    /**
    * 新增DHCP网关配置
    * 
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "addDhcpGiaddrConfig")
    public String addDhcpGiaddrConfig() throws Exception {
        OltDhcpGiaddrConfig dhcpGiaddrConfig = new OltDhcpGiaddrConfig();
        dhcpGiaddrConfig.setEntityId(entityId);
        dhcpGiaddrConfig.setOnuType(onuType);
        dhcpGiaddrConfig.setTopOltDHCPGiaddrIndex(dhcpGiaddrIndex);
        dhcpGiaddrConfig.setTopOltDHCPGiaddrIpAddr(dhcpGiaddrIp);
        dhcpGiaddrConfig.setTopOltDHCPGiaddrVid(dhcpGiaddrVid);
        try {
            oltDhcpService.insertDhcpGiaddrConfigs(dhcpGiaddrConfig);
            writeDataToAjax("addOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("addFailure");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
    * 修改DHCP网关
    * 
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "modifyDhcpGiaddrConfig")
    public String modifyDhcpGiaddrConfig() throws Exception {
        OltDhcpGiaddrConfig dhcpGiaddrConfig = new OltDhcpGiaddrConfig();
        dhcpGiaddrConfig.setEntityId(entityId);
        dhcpGiaddrConfig.setOnuType(onuType);
        dhcpGiaddrConfig.setTopOltDHCPGiaddrIndex(dhcpGiaddrIndex);
        dhcpGiaddrConfig.setTopOltDHCPGiaddrIpAddr(dhcpGiaddrIp);
        dhcpGiaddrConfig.setTopOltDHCPGiaddrVid(dhcpGiaddrVid);
        try {
            oltDhcpService.updateDhcpGiaddrConfigs(dhcpGiaddrConfig);
            writeDataToAjax("updateOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("updateFailure");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
    * 删除DHCP网关
    * 
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "deleteDhcpGiaddrConfig")
    public String deleteDhcpGiaddrConfig() throws Exception {
        try {
            oltDhcpService.deleteDhcpGiaddrConfigs(entityId, dhcpGiaddrIndex);
            writeDataToAjax("deleteOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("deleteFailure");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
    * 新增静态绑定记录
    * 
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "addDhcpIpMacStaticConfig")
    public String addDhcpIpMacStaticConfig() throws Exception {
        OltDhcpIpMacStatic dhcpIpMacStatic = new OltDhcpIpMacStatic();
        dhcpIpMacStatic.setEntityId(entityId);
        dhcpIpMacStatic.setTopOltDHCPIpAddr(dhcpIpMacStaticIp);
        dhcpIpMacStatic.setTopOltDHCPMacAddr(dhcpIpMacStaticMac);
        dhcpIpMacStatic.setTopOltDHCPOnuMacAddr(dhcpIpMacStaticOnuMac);
        if (oltDhcpService.getCountForIpMacStatic(entityId, dhcpIpMacStatic.getTopOltDHCPIpAddrLong(),
                dhcpIpMacStatic.getTopOltDHCPMacAddrLong()) == 0) {
            try {
                oltDhcpService.insertDhcpIpMacStatic(dhcpIpMacStatic);
                writeDataToAjax("addOK");
                operationResult = OperationLog.SUCCESS;
            } catch (SnmpException e) {
                writeDataToAjax("addFailure");
                operationResult = OperationLog.FAILURE;
            }
        } else {
            writeDataToAjax("addConfigExists");
        }
        return NONE;
    }

    /**
    * 删除静态绑定记录
    * 
    */
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "deleteDhcpIpMacStaticConfig")
    public String deleteDhcpIpMacStaticConfig() throws Exception {
        try {
            oltDhcpService.deleteDhcpIpMacStatic(entityId, dhcpIpMacStaticIdx);
            writeDataToAjax("deleteOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("deleteFailure");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    // 修改DHCP Server配置
    @OperationLogProperty(actionName = "oltDhcpAction", operationName = "modifyDhcpServerConfig")
    public String modifyDhcpServerConfig() throws Exception {
        OltDhcpServerConfig dhcpServerConfig = new OltDhcpServerConfig();
        dhcpServerConfig.setEntityId(entityId);
        dhcpServerConfig.setTopOltDHCPServerIndex(dhcpServerIndex);
        dhcpServerConfig.setTopOltDHCPServerVid(dhcpServerVid);
        dhcpServerConfig.setTopOltDHCPServerIpAddr(dhcpServerIp);
        dhcpServerConfig.setTopOltDHCPServerIpMask(dhcpServerIpMask);
        try {
            oltDhcpService.updateDhcpServerConfigs(dhcpServerConfig);
            writeDataToAjax("updateOK");
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            writeDataToAjax("updateFailure");
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
    * 从设备刷新dhcp基本配置
    * 
    */
    public String refreshDhcpBaseConfig() throws Exception {
        try {
            oltDhcpService.refreshOltDhcpBaseConfig(entityId);
            writeDataToAjax("refreshOK");
        } catch (SnmpException e) {
            writeDataToAjax("updateFailure");
        }
        return NONE;
    }

    /**
    * 从设备刷新dhcp服务配置
    * 
    */
    public String refreshDhcpServerConfig() throws Exception {
        try {
            oltDhcpService.refreshOltDhcpServerConfig(entityId);
            writeDataToAjax("refreshOK");
        } catch (SnmpException e) {
            writeDataToAjax("updateFailure");
        }
        return NONE;
    }

    /**
    * 从设备刷新dhcp网关配置
    * 
    */
    public String refreshDhcpGiaddrConfig() throws Exception {
        try {
            oltDhcpService.refreshOltDhcpGiaddrConfig(entityId);
            writeDataToAjax("refreshOK");
        } catch (SnmpException e) {
            writeDataToAjax("updateFailure");
        }
        return NONE;
    }

    /**
    * 从设备刷新dhcp静态绑定信息配置
    * 
    */
    public String refreshDhcpIpMacStaticConfig() throws Exception {
        try {
            oltDhcpService.refreshOltDhcpIpMacStaticConfig(entityId);
            writeDataToAjax("refreshOK");
        } catch (SnmpException e) {
            writeDataToAjax("updateFailure");
        }
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOnuType() {
        return onuType;
    }

    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    public Integer getDhcpServerIndex() {
        return dhcpServerIndex;
    }

    public void setDhcpServerIndex(Integer dhcpServerIndex) {
        this.dhcpServerIndex = dhcpServerIndex;
    }

    public Integer getDhcpServerVid() {
        return dhcpServerVid;
    }

    public void setDhcpServerVid(Integer dhcpServerVid) {
        this.dhcpServerVid = dhcpServerVid;
    }

    public String getDhcpServerIp() {
        return dhcpServerIp;
    }

    public void setDhcpServerIp(String dhcpServerIp) {
        this.dhcpServerIp = dhcpServerIp;
    }

    public String getDhcpServerIpMask() {
        return dhcpServerIpMask;
    }

    public void setDhcpServerIpMask(String dhcpServerIpMask) {
        this.dhcpServerIpMask = dhcpServerIpMask;
    }

    public JSONArray getDhcpServerIndexObject() {
        return dhcpServerIndexObject;
    }

    public void setDhcpServerIndexObject(JSONArray dhcpServerIndexObject) {
        this.dhcpServerIndexObject = dhcpServerIndexObject;
    }

    public JSONArray getOnuTypeObject() {
        return onuTypeObject;
    }

    public void setOnuTypeObject(JSONArray onuTypeObject) {
        this.onuTypeObject = onuTypeObject;
    }

    public Integer getDhcpGiaddrVid() {
        return dhcpGiaddrVid;
    }

    public Integer getDhcpGiaddrIndex() {
        return dhcpGiaddrIndex;
    }

    public void setDhcpGiaddrIndex(Integer dhcpGiaddrIndex) {
        this.dhcpGiaddrIndex = dhcpGiaddrIndex;
    }

    public void setDhcpGiaddrVid(Integer dhcpGiaddrVid) {
        this.dhcpGiaddrVid = dhcpGiaddrVid;
    }

    public String getDhcpGiaddrIp() {
        return dhcpGiaddrIp;
    }

    public void setDhcpGiaddrIp(String dhcpGiaddrIp) {
        this.dhcpGiaddrIp = dhcpGiaddrIp;
    }

    public JSONArray getDhcpGiaddrIndexObject() {
        return dhcpGiaddrIndexObject;
    }

    public void setDhcpGiaddrIndexObject(JSONArray dhcpGiaddrIndexObject) {
        this.dhcpGiaddrIndexObject = dhcpGiaddrIndexObject;
    }

    public Integer getDhcpIpMacStaticIdx() {
        return dhcpIpMacStaticIdx;
    }

    public void setDhcpIpMacStaticIdx(Integer dhcpIpMacStaticIdx) {
        this.dhcpIpMacStaticIdx = dhcpIpMacStaticIdx;
    }

    public String getDhcpIpMacStaticIp() {
        return dhcpIpMacStaticIp;
    }

    public void setDhcpIpMacStaticIp(String dhcpIpMacStaticIp) {
        this.dhcpIpMacStaticIp = dhcpIpMacStaticIp;
    }

    public String getDhcpIpMacStaticMac() {
        return dhcpIpMacStaticMac;
    }

    public void setDhcpIpMacStaticMac(String dhcpIpMacStaticMac) {
        this.dhcpIpMacStaticMac = dhcpIpMacStaticMac;
    }

    public String getDhcpIpMacStaticOnuMac() {
        return dhcpIpMacStaticOnuMac;
    }

    public void setDhcpIpMacStaticOnuMac(String dhcpIpMacStaticOnuMac) {
        this.dhcpIpMacStaticOnuMac = dhcpIpMacStaticOnuMac;
    }

    public Integer getPppoeEnable() {
        return pppoeEnable;
    }

    public void setPppoeEnable(Integer pppoeEnable) {
        this.pppoeEnable = pppoeEnable;
    }

    public Integer getDhcpRelayMode() {
        return dhcpRelayMode;
    }

    public void setDhcpRelayMode(Integer dhcpRelayMode) {
        this.dhcpRelayMode = dhcpRelayMode;
    }

    public Integer getDhcpIpMacDynamicEnable() {
        return dhcpIpMacDynamicEnable;
    }

    public void setDhcpIpMacDynamicEnable(Integer dhcpIpMacDynamicEnable) {
        this.dhcpIpMacDynamicEnable = dhcpIpMacDynamicEnable;
    }

    public OltDhcpBaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(OltDhcpBaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

}
