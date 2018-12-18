package com.topvision.platform.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.SystemService;
import com.topvision.platform.service.UserService;

@Controller("systemAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemAction extends BaseAction {
    private static final long serialVersionUID = -591912277160411193L;
    @Autowired
    private SystemPreferencesService systemPreferencesService = null;
    @Autowired
    private SystemService systemService = null;
    @Autowired
    private UserService userService = null;
    private boolean appletActive;
    private TopologyParam topoParam = null;
    private UserContext userContext = null;

    // 单位显示配置
    private String tempUnit;
    private String elecLevelUnit;

    /**
     * 重启
     * 
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    public String restart() throws UnknownHostException, IOException {
        int port = SystemConstants.getInstance().getIntParam("jconsole.listener.port", 12120);
        Socket socket = new Socket("localhost", port);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.println("restart");
        writer.flush();
        writer.close();
        socket.close();
        return NONE;
    }

    /**
     * 显示Applet配置页面
     * 
     * @return
     */
    public String showAppletConfig() {
        appletActive = systemPreferencesService.isUsedApplet();
        return SUCCESS;
    }

    /**
     * 显示单位显示配置页面
     * 
     * @return
     */
    public String showUnitConfig() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.platform.resources");
        Properties unitConfigs = systemPreferencesService.getModulePreferences("unit");
        tempUnit = unitConfigs.getProperty("tempUnit", resourceManager.getString("COMMON.Centigrade"));
        elecLevelUnit = unitConfigs.getProperty("elecLevelUnit", "dBmV");
        return SUCCESS;
    }

    /**
     * 保存单位显示配置
     * 
     * @return
     */
    public String saveUnitConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences preference = new SystemPreferences();
        preference.setModule("unit");
        preference.setName("tempUnit");
        preference.setValue(tempUnit);
        preferences.add(preference);
        preference = new SystemPreferences();
        preference.setModule("unit");
        preference.setName("elecLevelUnit");
        preference.setValue(elecLevelUnit);
        preferences.add(preference);
        systemPreferencesService.updateUnitConfig(preferences);
        return NONE;
    }

    public String showSystemPreferemces() {
        return SUCCESS;
    }

    public String modifySecurityParams() {
        return NONE;
    }

    /**
     * 更新Applet配置
     * 
     * @return
     */
    public String updateAppletConfig() {
        SystemPreferences preferences = new SystemPreferences();
        preferences.setName("applet");
        preferences.setModule("applet");
        preferences.setValue("" + appletActive);
        systemPreferencesService.savePreferences(preferences);
        systemPreferencesService.setUsedApplet(appletActive);
        return NONE;
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public TopologyParam getTopoParam() {
        return topoParam;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public UserService getUserService() {
        return userService;
    }

    public boolean isAppletActive() {
        return appletActive;
    }

    public void setAppletActive(boolean appletActive) {
        this.appletActive = appletActive;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public void setTopoParam(TopologyParam topoParam) {
        this.topoParam = topoParam;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getElecLevelUnit() {
        return elecLevelUnit;
    }

    public void setElecLevelUnit(String elecLevelUnit) {
        this.elecLevelUnit = elecLevelUnit;
    }

}