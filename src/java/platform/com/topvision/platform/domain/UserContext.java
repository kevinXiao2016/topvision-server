package com.topvision.platform.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.topvision.framework.annotation.Database;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.user.context.UserContextListener;

public class UserContext implements Serializable, UserContextListener {
    private static final long serialVersionUID = -6033013422519633042L;
    public static final int DEFAULT_TRY_PWD_COUNT = 0;
    public static final String KEY = "UserContext";
    private static final long MAX_INTERRUPT_INTERVAL = 60000;
    private boolean standalone = false;
    private boolean tabbed = true;
    private String startPage;
    private String styleName;
    private int pageSize = 25;
    private int maxNaviNum = 9;
    private int tooltipStyle = 1;
    private boolean alarmWhenCloseManyTab = false;
    private boolean switchWhenNewTab = true;
    private boolean notifyWhenMsg = true;
    private boolean tabMaxLimit = true;
    private boolean displayInputTip = true;
    private User user = null;
    private boolean superAdmin = false;
    private String macDisplayStyle = "6#M#U";
    private int tipShowTime = 5;
    private int tryPwdCounter = 0;
    private boolean locked = false;
    private int weekStartDay = 1;
    private int autoRefreshTime = 10;
    private int topNumber = 10;
    private int soundTimeInterval = 1;
    private int topoTreeDisplayDevice = 1;
    private int topoTreeClickToOpen = 1;
    /* 同一账户可以多个地方登陆,需要记录以便得知当前有多少用户登陆 */
    private final Map<String, Long> hosts = new HashMap<String, Long>();
    private LicenseIf licenseIf;

    // 用户参数配置
    private UserPreferencesMap<String, String> userPreferencesMap = new UserPreferencesMap<String, String>();
    /**
     * 权限集合.
     */
    private Set<String> functionHash = null;
    // 可用的导航条
    private List<NavigationButton> naviBars = null;
    private List<NavigationButton> naviBarsVisible = null;
    private List<MenuItem> menuItems;
    private List<ToolbarButton> toolbarButtons;
    private Map<String, Database> modules = null;

    /**
     * 获取用户某一个首选项信息 格式为: module.name 例如： getPreference("core.pageSize") = "25"
     * 
     * @param key
     * @return
     */
    public String getPreference(String key) {
        return userPreferencesMap.get(key);
    }

    /**
     * 获取指定模块下的所有首选项信息
     * 
     * @param module
     * @return
     */
    public Properties getModulePreferences(String module) {
        Properties props = new Properties();
        Set<String> keySet = userPreferencesMap.keySet();
        String $module = module + '.';
        int $moduleLength = module.length();
        for (String key : keySet) {
            if (key.startsWith($module)) {
                props.put(module.substring($moduleLength), userPreferencesMap.get(key));
            }
        }
        return props;
    }

    public void destroy() {

    }

    /**
     * 得到权限集合, 她是唯一的权限码的集合。
     * 
     * @return
     */
    @SuppressWarnings("unused")
    @Deprecated
    private Set<String> getFunctionHash() {
        return functionHash;
    }

    /**
     * 获取支持的模块：modules为程序启动时注入的模块集合，来源于模块Version（同数据库控制模块的管理）。在开发阶段表现为加入源码的产品模块，
     * 发布阶段表现为对应的模块jar是否在WEB-INF/lib或者classpath中。
     * 
     * 
     * @return 模块的支持情况
     */
    public Map<String, Boolean> getSupportModules() {
        //return modules.containsKey(type) && licenseIf.isSupportModule(type);
        //V2 License改为全部安装，license控制模块 
        Boolean hasSupportOlt = hasSupportModule("olt");
        Boolean hasSupportOnu = hasSupportModule("onu");
        Boolean hasSupportCmc = hasSupportModule("cmc");
        Boolean hasSupportCmts = hasSupportModule("cmts");
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("olt", hasSupportOlt);
        map.put("onu", hasSupportOnu);
        map.put("cmc", hasSupportCmc);
        map.put("cmts", hasSupportCmts);
        return map;
    }

    /**
     * 模块支持判断：modules为程序启动时注入的模块集合，来源于模块Version（同数据库控制模块的管理）。在开发阶段表现为加入源码的产品模块，
     * 发布阶段表现为对应的模块jar是否在WEB-INF/lib或者classpath中。
     * 
     * @param type
     *            模块名称，此名称和Version中模块名称一致，注意区分大小写，比如epon、cmc、wlan等
     * @return 是否支持期望模块
     */
    public boolean hasSupportModule(String type) {
        //return modules.containsKey(type) && licenseIf.isSupportModule(type);
        //V2 License改为全部安装，license控制模块 
        return licenseIf.isSupportModule(type);
    }

    /**
     * add by Victor@20150519增加项目名称的判断，根据项目进行功能控制
     * 
     * @param projectName 项目名称，默认是城市/地区简写，gz-广州珠江数码，nm-内蒙广电
     * @return
     */
    public boolean hasSupportProject(String projectName) {
        return licenseIf.isSupportProject(projectName);
    }

    /**
     * 判断模块是否有license许可
     * @param module模块名称
     * @return
     */
    public boolean hasModulePermission(String module) {
        return licenseIf.isSupportDeviceOrParent(null, module);
    }

    public int getMaxNaviNum() {
        String value = userPreferencesMap.get("core.maxNaviNum");
        return value == null ? maxNaviNum : Integer.parseInt(value);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public List<NavigationButton> getNaviBars() {
        return naviBars;
    }

    public List<NavigationButton> getNaviBarsVisible() {
        return naviBarsVisible;
    }

    public int getPageSize() {
        String value = userPreferencesMap.get("core.pageSize");
        return value == null ? pageSize : Integer.parseInt(value);
    }

    public String getStartPage() {
        String value = userPreferencesMap.get("core.startPage");
        return value == null ? startPage : value;
    }

    public String getStyleName() {
        String value = userPreferencesMap.get("core.styleName");
        return value == null ? styleName : value;
    }

    public List<ToolbarButton> getToolbarButtons() {
        return toolbarButtons;
    }

    public int getTooltipStyle() {
        return userPreferencesMap.getInt("core.tooltipStyle", tooltipStyle);
    }

    public User getUser() {
        return user;
    }

    public long getUserId() {
        return user.getUserId();
    }

    public UserPreferencesMap<String, String> getUserPreferencesMap() {
        return userPreferencesMap;
    }

    public boolean hasPower(String key) {
        return superAdmin || functionHash.contains(key);
    }

    public boolean isAlarmWhenCloseManyTab() {
        String value = userPreferencesMap.get("core.alarmWhenCloseManyTab");
        return value == null ? alarmWhenCloseManyTab : "true".equals(value);
    }

    public boolean isDisplayInputTip() {
        String value = userPreferencesMap.get("core.displayInputTip");
        return value == null ? displayInputTip : "true".equals(value);
    }

    public boolean isNotifyWhenMsg() {
        String value = userPreferencesMap.get("core.notifyWhenMsg");
        return value == null ? notifyWhenMsg : "true".equals(value);
    }

    public boolean isStandalone() {
        return standalone;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public boolean isSwitchWhenNewTab() {
        String value = userPreferencesMap.get("core.switchWhenNewTab");
        return value == null ? switchWhenNewTab : "true".equals(value);
    }

    public boolean isTabbed() {
        String value = userPreferencesMap.get("core.tabbed");
        return value == null ? tabbed : "true".equals(value);
    }

    public boolean isTabMaxLimit() {
        String value = userPreferencesMap.get("core.tabMaxLimit");
        return value == null ? tabMaxLimit : "true".equals(value);
    }

    public void setAlarmWhenCloseManyTab(boolean alarmWhenCloseManyTab) {
        this.alarmWhenCloseManyTab = alarmWhenCloseManyTab;
    }

    public void setDisplayInputTip(boolean displayInputTip) {
        this.displayInputTip = displayInputTip;
    }

    public void setFunctionHash(Set<String> functionHash) {
        //TODO Modify by Victor@20141022暂时去掉Google地图的功能
        functionHash.remove("googleMap");
        this.functionHash = functionHash;
    }

    public void setMaxNaviNum(int maxNaviNum) {
        this.maxNaviNum = maxNaviNum;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void setNaviBars(List<NavigationButton> modules) {
        this.naviBars = modules;
    }

    public void setNaviBarsVisible(List<NavigationButton> naviBarsVisible) {
        this.naviBarsVisible = naviBarsVisible;
    }

    public void setNotifyWhenMsg(boolean notifyWhenMsg) {
        this.notifyWhenMsg = notifyWhenMsg;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public void setSwitchWhenNewTab(boolean switchWhenNewTab) {
        this.switchWhenNewTab = switchWhenNewTab;
    }

    public void setTabbed(boolean tabbed) {
        this.tabbed = tabbed;
    }

    public void setTabMaxLimit(boolean tabMaxLimit) {
        this.tabMaxLimit = tabMaxLimit;
    }

    public void setToolbarButtons(List<ToolbarButton> toolbarButtons) {
        this.toolbarButtons = toolbarButtons;
    }

    public void setTooltipStyle(int tooltipStyle) {
        this.tooltipStyle = tooltipStyle;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserPreferencesMap(UserPreferencesMap<String, String> userPreferences) {
        this.userPreferencesMap = userPreferences;
    }

    /**
     * @param modules
     *            the modules to set
     */
    public void setModules(Map<String, Database> modules) {
        this.modules = modules;
    }

    public String getMacDisplayStyle() {
        String value = userPreferencesMap.get("core.macDisplayStyle");
        return (value == null) ? macDisplayStyle : value;
    }

    public void setMacDisplayStyle(String macDisplayStyle) {
        this.macDisplayStyle = macDisplayStyle;
    }

    public int getTipShowTime() {
        String value = userPreferencesMap.get("core.tipShowTime");
        return value == null ? tipShowTime : Integer.parseInt(value);
    }

    public void setTipShowTime(int tipShowTime) {
        this.tipShowTime = tipShowTime;
    }

    public int getTryPwdCounter() {
        return tryPwdCounter;
    }

    public int increamentTryCounter(Integer stopNumber) {
        return ++tryPwdCounter > stopNumber ? stopNumber : tryPwdCounter;
    }

    public void setTryPwdCounter(int tryPwdCounter) {
        this.tryPwdCounter = tryPwdCounter;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getTopNumber() {
        String value = userPreferencesMap.get("core.topNumber");
        return (value == null) ? topNumber : Integer.valueOf(value);
    }

    public void setTopNumber(int topNumber) {
        this.topNumber = topNumber;
    }
    

    public int getSoundTimeInterval() {
    	String value = userPreferencesMap.get("core.soundTimeInterval");
		return (value == null) ? soundTimeInterval : Integer.valueOf(value);
	}

	public void setSoundTimeInterval(int soundTimeInterval) {
		this.soundTimeInterval = soundTimeInterval;
	}
	

	public int getTopoTreeDisplayDevice() {
	    String value = userPreferencesMap.get("core.topoTreeDisplayDevice");
        return (value == null) ? topoTreeDisplayDevice : Integer.valueOf(value);
    }

    public void setTopoTreeDisplayDevice(int topoTreeDisplayDevice) {
        this.topoTreeDisplayDevice = topoTreeDisplayDevice;
    }

    public int getTopoTreeClickToOpen() {
        String value = userPreferencesMap.get("core.topoTreeClickToOpen");
        return (value == null) ? topoTreeClickToOpen : Integer.valueOf(value);
    }

    public void setTopoTreeClickToOpen(int topoTreeClickToOpen) {
        this.topoTreeClickToOpen = topoTreeClickToOpen;
    }

    @Override
    public void registry(String host) {
        hosts.put(host, System.currentTimeMillis());
        setTryPwdCounter(DEFAULT_TRY_PWD_COUNT);
    }

    @Override
    public void unregistry(String host) {
        hosts.remove(host);
    }

    public Set<String> getHosts() {
        Set<String> set = hosts.keySet();
        Iterator<String> it = set.iterator();
        long current = System.currentTimeMillis();
        while (it.hasNext()) {
            String host = it.next();
            long diff = current - hosts.get(host);
            if (diff > MAX_INTERRUPT_INTERVAL) {
                it.remove();
            }
        }
        return hosts.keySet();
    }

    @Override
    public String toString() {
        return "UserContext [standalone=" + standalone + "<br> tabbed=" + tabbed + "<br> startPage=" + startPage
                + "<br> styleName=" + styleName + "<br> pageSize=" + pageSize + "<br> maxNaviNum=" + maxNaviNum
                + "<br> tooltipStyle=" + tooltipStyle + "<br> alarmWhenCloseManyTab=" + alarmWhenCloseManyTab
                + "<br> switchWhenNewTab=" + switchWhenNewTab + "<br> notifyWhenMsg=" + notifyWhenMsg
                + "<br> tabMaxLimit=" + tabMaxLimit + "<br> displayInputTip=" + displayInputTip + "<br> user=" + user
                + "<br> superAdmin=" + superAdmin + "<br> macDisplayStyle=" + macDisplayStyle + "<br> tipShowTime="
                + tipShowTime + "<br> tryPwdCounter=" + tryPwdCounter + "<br> locked=" + locked + "<br> hosts=" + hosts
                + "<br> userPreferencesMap=" + userPreferencesMap + "<br> functionHash=" + functionHash
                + "<br> naviBars=" + naviBars + "<br> naviBarsVisible=" + naviBarsVisible + "<br> menuItems="
                + menuItems + "<br> toolbarButtons=" + toolbarButtons + "<br> modules=" + modules + "]";
    }

    /**
     * 记录客户端最后激活时间
     * @param host
     */
    public void activeClient(String host) {
        hosts.put(host, System.currentTimeMillis());
    }

    public int getWeekStartDay() {
        String value = userPreferencesMap.get("core.weekStartDay");
        return (value == null) ? weekStartDay : Integer.valueOf(value);
    }

    public void setWeekStartDay(int weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    /**
     * @param licenseIf the licenseIf to set
     */
    public void setLicenseIf(LicenseIf licenseIf) {
        this.licenseIf = licenseIf;
    }

    public int getAutoRefreshTime() {
        String value = userPreferencesMap.get("core.autoRefreshTime");
        return (value == null) ? autoRefreshTime : Integer.valueOf(value);
    }

    public void setAutoRefreshTime(int autoRefreshTime) {
        this.autoRefreshTime = autoRefreshTime;
    }

    public boolean allowRepeatedlyLogon() {
        return user.isAllowMutliIpLogin();
    }

}