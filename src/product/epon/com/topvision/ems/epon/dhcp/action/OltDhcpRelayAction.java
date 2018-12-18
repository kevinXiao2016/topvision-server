/***********************************************************************
 * $Id: OltDhcpRelayAction.java,v1.0 2013-10-25 下午5:41:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayConfigSetting;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayVlanMap;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.ems.epon.dhcp.service.OltDhcpRelayService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author flack
 * @created @2013-10-25-下午5:41:37
 *
 */
@Controller("oltDhcpRelayAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpRelayAction extends BaseAction {
    private static final long serialVersionUID = -2189239647272671265L;
    private final Logger logger = LoggerFactory.getLogger(OltDhcpRelayAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpRelayService oltDhcpRelayService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private Long entityId;
    private Integer action;
    private Entity entity;
    private String bundleInterface;
    private String bundleInterfaceEnd;
    private JSONArray bundleInterfaceListEnd = new JSONArray();
    private JSONArray deviceTypes = new JSONArray();
    private JSONObject dhcpRelayBundleFullConfig = new JSONObject();
    private Integer dhcpRelaySwitch;
    private DhcpBundle dhcpRelayBundle;
    private DhcpRelayVlanMap dhcpRelayVlanMap;
    private DhcpServerConfig[] dhcpServerConfigs = new DhcpServerConfig[100];
    private DhcpGiaddrConfig[] dhcpGiaddrConfigs = new DhcpGiaddrConfig[20];
    private DhcpOption60[] dhcpOption60Configs = new DhcpOption60[100];
    private String cameraSwitch;

    public OltDhcpRelayAction() {
        for (int i = 0; i < dhcpServerConfigs.length; i++) {
            dhcpServerConfigs[i] = new DhcpServerConfig();
        }
        for (int i = 0; i < dhcpGiaddrConfigs.length; i++) {
            dhcpGiaddrConfigs[i] = new DhcpGiaddrConfig();
        }
        for (int i = 0; i < dhcpOption60Configs.length; i++) {
            dhcpOption60Configs[i] = new DhcpOption60();
        }
    }

    /**
     * 显示DHCP Relay配置列表
     * 
     * @return
     */
    public String showDhcpRelayConfigList() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        dhcpRelaySwitch = oltDhcpRelayService.getDhcpRelaySwitch(entityId);
        return SUCCESS;
    }

    /**
     * 显示修改DHCP Relay配置页面
     * 
     * @return
     */
    public String showModifyDhcpRelayConfig() {
        List<String> bundleListEnd = oltDhcpRelayService.getCmcDhcpBundleEndList(entityId);
        bundleInterfaceListEnd = JSONArray.fromObject(bundleListEnd);
        if (action == 2) {
            DhcpRelayConfigSetting setting = new DhcpRelayConfigSetting();
            setting.setDhcpBundle(oltDhcpRelayService.getCmcDhcpBundle(entityId, bundleInterface));
            setting.setDhcpGiAddr(oltDhcpRelayService.getCmcDhcpGiAddrList(entityId, bundleInterface));
            setting.setDhcpOption60(oltDhcpRelayService.getCmcDhcpOption60List(entityId, bundleInterface));
            setting.setDhcpServer(oltDhcpRelayService.getCmcDhcpServerList(entityId, bundleInterface));
            deviceTypes = JSONArray.fromObject(oltDhcpRelayService.getDeviceTypes(entityId, bundleInterface));
            dhcpRelayBundleFullConfig = JSONObject.fromObject(setting);
        }
        return SUCCESS;
    }

    /**
     * 获取DHCP Relay配置数据
     * 
     * @return
     * @throws IOException 
     */
    public String getDhcpRelayConfigList() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        List<DhcpRelayConfig> relay = oltDhcpRelayService.getDhcpRelayConfigList(entityId);
        json.put("data", relay);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String modifyDhcpRelaySwitch() throws IOException {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            oltDhcpRelayService.modifyDhcpRelaySwitch(entityId, dhcpRelaySwitch);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    public String modifyDhcpRelayBundleConfig() throws IOException {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            DhcpRelayConfigSetting setting = new DhcpRelayConfigSetting(bundleInterfaceEnd, dhcpRelayBundle,
                    dhcpGiaddrConfigs, dhcpOption60Configs, dhcpServerConfigs, dhcpRelayVlanMap);
            oltDhcpRelayService.modifyDhcpRelayConfig(entityId, setting);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 删除DHCP Relay配置
     * 
     * @return
     * @throws IOException 
     */
    public String deleteDhcpRelayConfig() throws IOException {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            oltDhcpRelayService.deleteDhcpRelayConfig(entityId, bundleInterface);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    public String refreshDhcpRelayConfigFromDevice() throws IOException {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            oltDhcpRelayService.refreshDhcpRelayConfig(entityId);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getBundleInterface() {
        return bundleInterface;
    }

    public void setBundleInterface(String bundleInterface) {
        this.bundleInterface = bundleInterface;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Integer getDhcpRelaySwitch() {
        return dhcpRelaySwitch;
    }

    public void setDhcpRelaySwitch(Integer dhcpRelaySwitch) {
        this.dhcpRelaySwitch = dhcpRelaySwitch;
    }

    public DhcpBundle getDhcpRelayBundle() {
        return dhcpRelayBundle;
    }

    public void setDhcpRelayBundle(DhcpBundle dhcpRelayBundle) {
        this.dhcpRelayBundle = dhcpRelayBundle;
    }

    public DhcpServerConfig[] getDhcpServerConfigs() {
        return dhcpServerConfigs;
    }

    public void setDhcpServerConfigs(DhcpServerConfig[] dhcpServerConfigs) {
        this.dhcpServerConfigs = dhcpServerConfigs;
    }

    public DhcpGiaddrConfig[] getDhcpGiaddrConfigs() {
        return dhcpGiaddrConfigs;
    }

    public void setDhcpGiaddrConfigs(DhcpGiaddrConfig[] dhcpGiaddrConfigs) {
        this.dhcpGiaddrConfigs = dhcpGiaddrConfigs;
    }

    public DhcpOption60[] getDhcpOption60Configs() {
        return dhcpOption60Configs;
    }

    public void setDhcpOption60Configs(DhcpOption60[] dhcpOption60Configs) {
        this.dhcpOption60Configs = dhcpOption60Configs;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public JSONArray getBundleInterfaceListEnd() {
        return bundleInterfaceListEnd;
    }

    public void setBundleInterfaceListEnd(JSONArray bundleInterfaceListEnd) {
        this.bundleInterfaceListEnd = bundleInterfaceListEnd;
    }

    public JSONArray getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(JSONArray deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public JSONObject getDhcpRelayBundleFullConfig() {
        return dhcpRelayBundleFullConfig;
    }

    public void setDhcpRelayBundleFullConfig(JSONObject dhcpRelayBundleFullConfig) {
        this.dhcpRelayBundleFullConfig = dhcpRelayBundleFullConfig;
    }

    public DhcpRelayVlanMap getDhcpRelayVlanMap() {
        return dhcpRelayVlanMap;
    }

    public void setDhcpRelayVlanMap(DhcpRelayVlanMap dhcpRelayVlanMap) {
        this.dhcpRelayVlanMap = dhcpRelayVlanMap;
    }

    public String getBundleInterfaceEnd() {
        return bundleInterfaceEnd;
    }

    public void setBundleInterfaceEnd(String bundleInterfaceEnd) {
        this.bundleInterfaceEnd = bundleInterfaceEnd;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}
