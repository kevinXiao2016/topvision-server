package com.topvision.ems.network.action;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;

@Controller("networkAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NetworkAction extends BaseAction {
    private static final long serialVersionUID = -921751031764966393L;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    private String cmdKey;
    private String netCmd;
    private long entityId = -1;
    private String ip;
    // 设备快照页面拓扑定位使用,设备Id
    private Long equId;

    private Long typeId;
    private String version;
    // 用于保存用户设置的设备视图页面自定义视图
    private String deviceViewLeft;
    private String deviceViewRight;
    // 用于保存用户设置的资源列表页面自定义视图
    private Boolean showCartoon;
    // 用于保存是否显示刷新提醒
    private Boolean showRefreshTip;
    // 用于保存终端定位用户选择CM/CPE
    private String terminalView;

    public String findEntity() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        JSONObject json = new JSONObject();
        List<TopoFolderEx> folders = topologyService.getTopoFolderByIp(ip);
        int size = folders == null ? 0 : folders.size();
        JSONArray array = new JSONArray();
        TopoFolderEx folder = null;
        JSONObject temp = null;
        for (int i = 0; i < size; i++) {
            folder = folders.get(i);
            temp = new JSONObject();
            temp.put("entityId", folder.getEntityId());
            Long folderId = folder.getFolderId();
            temp.put("folderId", folderId);
            String name = folder.getName();
            /* 如果是默认地域，则对name进行国际化处理 */
            if (10 == folderId) {
                temp.put("name", resourceManager.getString(name));
            } else {
                temp.put("name", name);
            }
            array.put(temp);
        }
        json.put("folder", array);
        json.put("equId", equId);
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        json.write(ServletActionContext.getResponse().getWriter());

        return NONE;
    }

    public String loadNetworkServices() throws Exception {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("data", array);
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载设备视图页面 为了业务处理需要,将之前页面直接使用jsp跳转改为使用后台action跳转
     * 
     * @author flackyang
     * @since 2013-11-08
     * @return
     */
    public String showDeviceViewJsp() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule("deviceView");
        userPre.setUserId(uc.getUserId());
        Properties deviceView = userPreferencesService.getModulePreferences(userPre);
        deviceViewLeft = deviceView.getProperty("deviceViewLeft");
        deviceViewRight = deviceView.getProperty("deviceViewRight");
        return SUCCESS;
    }

    /**
     * 保存用户设置的设备视图页面自定义视图
     * 
     * @author flackyang
     * @since 2013-11-08
     * @return
     */
    public String saveDeviceView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties deviceView = new Properties();
        deviceView.setProperty("deviceViewLeft", deviceViewLeft);
        deviceView.setProperty("deviceViewRight", deviceViewRight);
        userPreferencesService.batchSaveModulePreferences("deviceView", uc.getUserId(), deviceView);
        return NONE;
    }

    /**
     * 显示资源列表
     * 
     * @return
     */
    public String showEntitySnapList() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule("entityView");
        userPre.setUserId(uc.getUserId());
        Properties deviceView = userPreferencesService.getModulePreferences(userPre);
        showCartoon = Boolean.parseBoolean(deviceView.getProperty("showCartoon"));
        showRefreshTip = Boolean.parseBoolean(deviceView.getProperty("showRefreshTip"));
        return SUCCESS;
    }

    /**
     * 保存用户设置的资源列表页面自定义视图
     * 
     * @return
     */
    public String saveEntitySnapView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties entityView = new Properties();
        if (showCartoon != null) {
            entityView.setProperty("showCartoon", showCartoon.toString());
        }
        if (showRefreshTip != null) {
            entityView.setProperty("showRefreshTip", showRefreshTip.toString());
        }
        userPreferencesService.batchSaveModulePreferences("entityView", uc.getUserId(), entityView);
        return NONE;
    }

    /**
     * 显示设备列表，主要是展示所有entity对应的地域
     * 
     * @return
     */
    public String showEntityList() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule("entityView");
        userPre.setUserId(uc.getUserId());
        Properties deviceView = userPreferencesService.getModulePreferences(userPre);
        showCartoon = Boolean.parseBoolean(deviceView.getProperty("showCartoon"));
        return SUCCESS;
    }

    /**
     * 显示全局终端定位页面
     * 
     * @return
     */
    public String showTerminalLocation() {
        terminalView = getTerminalUserView();
        return SUCCESS;
    }

    /**
     * 保存用户选择的终端定位选项 CM/ONUCPE
     * 
     * @return
     */
    public String saveTerminalUserView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        userPreferencesService.saveModulePreferences("terminalView", "terminalLocation", terminalView, uc);
        return NONE;
    }

    /**
     * 获取用户选择的终端定位选项
     * 
     * @return
     */
    private String getTerminalUserView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("terminalLocation");
        up.setUserId(uc.getUserId());
        Properties terminalView = userPreferencesService.getModulePreferences(up);
        return terminalView.getProperty("terminalView", "CM");
    }

    public String newSubnetJsp() {
        return SUCCESS;
    }

    public String newTopoFolderJsp() {
        return SUCCESS;
    }

    public String showDeleteFolder() {
        return SUCCESS;
    }

    public String getCmdKey() {
        return cmdKey;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getIp() {
        return ip;
    }

    public String getNetCmd() {
        return netCmd;
    }

    public void setCmdKey(String cmdKey) {
        this.cmdKey = cmdKey;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setNetCmd(String netCmd) {
        this.netCmd = netCmd;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    public String getDeviceViewLeft() {
        return deviceViewLeft;
    }

    public void setDeviceViewLeft(String deviceViewLeft) {
        this.deviceViewLeft = deviceViewLeft;
    }

    public String getDeviceViewRight() {
        return deviceViewRight;
    }

    public void setDeviceViewRight(String deviceViewRight) {
        this.deviceViewRight = deviceViewRight;
    }

    public Boolean getShowCartoon() {
        return showCartoon;
    }

    public void setShowCartoon(Boolean showCartoon) {
        this.showCartoon = showCartoon;
    }

    public Long getEquId() {
        return equId;
    }

    public void setEquId(Long equId) {
        this.equId = equId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getShowRefreshTip() {
        return showRefreshTip;
    }

    public void setShowRefreshTip(Boolean showRefreshTip) {
        this.showRefreshTip = showRefreshTip;
    }

    public String getTerminalView() {
        return terminalView;
    }

    public void setTerminalView(String terminalView) {
        this.terminalView = terminalView;
    }

}
