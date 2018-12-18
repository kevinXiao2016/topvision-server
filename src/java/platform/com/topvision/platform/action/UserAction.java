package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.exception.dao.HasBindByIPException;
import com.topvision.exception.service.AuthenticationException;
import com.topvision.exception.service.ExistException;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MD5;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.GeneralPreferences;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.domain.UserGroup;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserPreferencesMap;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserGroupService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.user.context.UserContextManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("userAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserAction extends BaseAction {
    private static final long serialVersionUID = 5407778420545847849L;
    private static final Logger logger = LoggerFactory.getLogger(UserAction.class);

    private int maxNaviNumber = 8;
    private List<String> naviBarVisible;
    private List<String> naviBarOrder;
    private List<NavigationButton> naviBars;
    private boolean allowIpBindLogon;
    private boolean checkPasswdComplex;
    private List<UserGroup> userGroups;
    private List<Long> userIds;
    private List<String> userNames;
    private List<Long> roleIds;
    private String styleName = "default";
    private long userId;
    private String userName;
    private String oldPwd;
    private String newPwd;
    private User user;
    private UserEx userEx;
    private JSONObject userJson;
    private UserContext userContext;
    private UserPreferences userPreferences;
    private GeneralPreferences generalPreferences;
    private List<SystemPreferences> themes;
    private String bindIp;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private UserContextManager userContextManager;
    @Autowired
    private UserContextManager ucm;
    @Autowired
    private LicenseIf licenseIf;
    
    private List<FunctionItem> customerList;

    private String functionNames;
    private String functionActions;
    private String functionIcons;

    private Boolean userSoundStatus;
    // 导航动画选项
    private Boolean navCartoonStatus;
    private String displayField;
    private String cameraSwitch;
    private boolean allowMutliIpLoginStatus;
    private int timeout;
    private Long folderId;
    private String choose;

    public String showUserList() {
        // 判断当前用户是否有打开页面的权限
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (uc.hasPower("userManagement")) {
            return SUCCESS;
        } else {
            return FORBIDDEN;
        }
    }

    /**
     * 设置用户是否允许多点登录
     * 
     * @return
     */
    public String allowMutliIpLogin() {
        userService.updateMutilIpLogin(userId, allowMutliIpLoginStatus);
        return NONE;
    }

    /**
     * 设置用户超时时间
     * 
     * @return
     */
    public String updateUserSession() {
        // long $userId = CurrentRequest.getCurrentUser().getUser().getUserId();
        userService.updateUserSession(userId, timeout);
        return NONE;
    }

    /**
     * 进入用户超时时间设置界面
     * 
     * @return
     */
    public String showSessionTime() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (!uc.hasPower("userManagement")) {
            return FORBIDDEN;
        }
        timeout = userService.getUserEx(userId).getTimeout();
        userName = userService.getUserEx(userId).getUserName();
        return SUCCESS;
    }

    /**
     * 加载个性化我的工作台
     * 
     * @return
     */
    public String showWorkbench() {
        UserContext uc = (UserContext) request.getSession().getAttribute("UserContext");
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        long userId = uc.getUserId();
        customerList = userService.loadUserWorkbence(userId);
        for (int i = 0; i < customerList.size(); i++) {
            customerList.get(i).setDisplayName(resourceManager.getString(customerList.get(i).getDisplayName()));
        }
        return SUCCESS;
    }

    /**
     * 用户锁屏
     * 
     * @return
     */
    public String lockScreen() {
        getSession().put("lockScreen", true);
        return NONE;
    }

    /**
     * 用户解锁屏
     * 
     * @return
     */
    public String unlockScreen() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc != null) {
            User user = uc.getUser();
            if ("superadmin".equalsIgnoreCase(user.getUserName())) {
                if (licenseIf.verifySuperPasswd(newPwd.trim()) == false) {
                    throw new AuthenticationException("PasswordInvalid");
                }
            } else if (MD5.makeMd5(newPwd.trim()).equalsIgnoreCase(user.getPasswd()) == false) {
                throw new AuthenticationException("PasswordInvalid");
            }
        }
        return NONE;
    }

    /**
     * 定制我的工作台
     * 
     * @return
     */
    public String updateCustomMydesck() {
        List<FunctionItem> items = new ArrayList<FunctionItem>();
        if (!"".equals(functionNames)) {
            String[] names = functionNames.split("-");
            String[] actions = functionActions.split("-");
            String[] icons = functionIcons.split("-");
            for (int i = 0; i < names.length; i++) {
                FunctionItem item = new FunctionItem();
                item.setFunctionName(names[i]);
                item.setFunctionAction(actions[i]);
                item.setIcon(icons[i]);
                items.add(item);
            }
        }
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        long userId = uc.getUserId();
        userService.updateCustomMydesck(userId, items);
        return NONE;
    }

    /**
     * 创建一个用户.
     * 
     * @return
     * @throws Exception
     */
    public String createUser() throws Exception {
        JSONObject json = new JSONObject();
        try {
            userService.insertUser(userEx);
            json.put("exists", false);
        } catch (ExistException ee) {
            json.put("exists", true);
        } finally {
            json.write(response.getWriter());
        }
        return NONE;
    }

    /**
     * 新建用户时检查用户名是否已经存在
     * 
     * @return
     * @throws Exception
     */
    public String isUserNameExist() throws Exception {
        JSONObject json = new JSONObject();
        User user = userService.getUser(userName);
        if (user != null) {
            json.put("exists", true);
        } else {
            json.put("exists", false);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 用户数量是否超过了许可证限制
     * 
     * @return
     */
    public String userOutofLimit() {
        JSONObject jObj = new JSONObject();
        try {
            Integer count = userService.getUserCount() - 1;// 去掉一个superadmin
            Integer userLimitCount = Integer.valueOf(licenseService.getLicenseView().getNumberOfUsers());

            if (userLimitCount >= 0 && count >= userLimitCount) {
                jObj.put("outofLimit", true);
            } else {
                jObj.put("outofLimit", false);
            }

            jObj.put("success", true);
            jObj.put("msg", "");
        } catch (Exception e) {
            LOG.error("", e);
            jObj.put("success", false);
            jObj.put("msg", e.getMessage());
        }

        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 删除一个用户
     * 
     * @return
     */
    public String deleteUser() {
        userService.deleteUser(userIds);
        return NONE;
    }

    /**
     * 用户是否绑定IP
     * 
     * @return
     * @throws Exception
     */
    public String hasBindIp() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        JSONObject json = new JSONObject();
        try {
            User u = userService.hasBindByIp(bindIp);
            if (u != null && u.getUserId() != uc.getUserId()) {
                json.put("exists", true);
                json.put("name", u.getUserName());
            } else {
                json.put("exists", false);
            }
        } catch (HasBindByIPException ex) {
            json.put("exists", true);
            logger.error("Has Bind ip: " + bindIp, ex);
            throw ex;
        } finally {
            json.write(response.getWriter());
        }
        return NONE;
    }

    /**
     * 载入用户列表
     * 
     * @return
     * @throws Exception
     */
    public String loadUserList() throws Exception {
        final ResourceManager resourceManager = ResourceManager
                .getResourceManager("com.topvision.ems.resources.resources");
        JSONObject json = new JSONObject();
        List<UserEx> list = userService.loadUserList();
        JSONArray array = new JSONArray();
        int size = list == null ? 0 : list.size();
        json.put("rowCount", size);
        JSONObject temp = null;
        UserEx user = null;
        String n = "";
        for (int i = 0; i < size; i++) {
            temp = new JSONObject();
            user = list.get(i);
            UserContext uc = ucm.getUserContext(user.getUserName(), true);
            temp.put("userId", user.getUserId());
            temp.put("userName", user.getUserName());
            temp.put("familyName", user.getFamilyName());
            temp.put("status", user.getStatus());
            temp.put("roleIds", user.getRoleIds());
            temp.put("roleName", user.getRoleNames());
            temp.put("placeId", user.getPlaceId());
            temp.put("placeName", user.getPlaceName());
            temp.put("departmentId", user.getDepartmentId());
            temp.put("departmentName", user.getDepartmentName());
            temp.put("mobile", user.getMobile());
            temp.put("email", user.getEmail());
            temp.put("allowMutliIpLogin", user.isAllowMutliIpLogin());
            temp.put("timeout", user.getTimeout());
            temp.put("limitIp", user.getLimitIp());
            temp.put("userGroupName", resourceManager.getString(user.getUserGroupName()));
            temp.put("bindIp", user.getBindIp());
            temp.put("locked", uc.isLocked());
            if (user.getFamilyName() != null) {
                n = user.getFamilyName();
            }
            if (user.getFirstName() != null) {
                n = n + " " + user.getFirstName();
            }
            temp.put("name", n);
            temp.put("createTime", DateUtils.format(user.getCreateTime()));
            temp.put("lastLoginIp", user.getLastLoginIp());
            temp.put("lastLoginTime", DateUtils.format(user.getLastLoginTime()));
            array.add(temp);
        }
        json.put("data", array);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 载入用户个性化设置
     * 
     * @return
     */
    public String loadUserPreferences() {
        userContext = (UserContext) getSession().get(UserContext.KEY);
        // allowIpBindLogon = systemPreferencesService.isAllowIpBindLogon();
        themes = systemPreferencesService.getSystemPreferences("theme");
        displayField = userContext.getPreference("user.displayField");
        return SUCCESS;
    }

    /**
     * 重置导航按钮.
     * 
     * @return
     */
    public String resetNaviOption() {
        return showNaviOption();
    }

    /**
     * 重置用户密码
     * 
     * @return
     */
    public String resetUserPasswd() {
        userService.updateUserPasswd(userIds, "123456");
        return NONE;
    }

    /**
     * 加载修改密码页面
     * 
     * @return
     */
    public String loadModifyPwd() {
        // 根据用户的ID取到用户的信息
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (!uc.hasPower("userManagement")) {
            return FORBIDDEN;
        }
        userJson = new JSONObject();
        checkPasswdComplex = systemPreferencesService.isCheckPasswdComplex();
        userJson.put("userId", userId);
        userJson.put("userName", userName);
        userJson.put("checkPasswdComplex", checkPasswdComplex);
        return SUCCESS;
    }

    public String modifyUserPwd() {
        User user = userService.getUser(userName);
        Map<String, Object> json = new HashMap<String, Object>();
        boolean oldPwdMatch = user.getPasswd().equals(MD5.makeMd5(oldPwd));
        if (!oldPwdMatch) {
            json.put("success", false);
        } else {
            User u = new User();
            u.setUserId(userId);
            u.setUserName(userName);
            u.setPasswd(MD5.makeMd5(newPwd));
            userService.updatePasswd(u);
            // 如果修改的当前系统登录用户，则需要修改session数据
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            if (uc.getUserId() == userId) {
                uc.getUser().setPasswd(MD5.makeMd5(newPwd));
            }
            json.put("success", true);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 设置全局个性化设置
     * 
     * @return
     */
    public String saveGeneralPreferences() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        UserPreferencesMap<String, String> map = uc.getUserPreferencesMap();
        userService.saveGeneralPreferences(uc.getUserId(), generalPreferences);
        uc.setTabbed(generalPreferences.isTabbed());
        uc.setDisplayInputTip(generalPreferences.isDisplayInputTip());
        map.put("core.tabbed", String.valueOf(generalPreferences.isTabbed()));
        map.put("core.alarmWhenCloseManyTab", String.valueOf(generalPreferences.isAlarmWhenCloseManyTab()));
        map.put("core.switchWhenNewTab", String.valueOf(generalPreferences.isSwitchWhenNewTab()));
        map.put("core.notifyWhenMsg", String.valueOf(generalPreferences.isNotifyWhenMsg()));
        map.put("core.styleName", generalPreferences.getStyleName());
        map.put("core.tooltipStyle", String.valueOf(generalPreferences.getTooltipStyle()));
        map.put("core.pageSize", String.valueOf(generalPreferences.getPageSize()));
        map.put("core.tabMaxLimit", String.valueOf(generalPreferences.isTabMaxLimit()));
        map.put("core.displayInputTip", String.valueOf(generalPreferences.isDisplayInputTip()));
        map.put("core.macDisplayStyle", generalPreferences.getMacDisplayStyle());
        map.put("core.tipShowTime", String.valueOf(generalPreferences.getTipShowTime()));
        map.put("core.weekStartDay", String.valueOf(generalPreferences.getWeekStartDay()));
        map.put("core.autoRefreshTime", String.valueOf(generalPreferences.getAutoRefreshTime()));
        map.put("core.topNumber", String.valueOf(generalPreferences.getTopNumber()));
        map.put("core.soundTimeInterval", String.valueOf(generalPreferences.getSoundTimeInterval()));
        map.put("core.topoTreeDisplayDevice", String.valueOf(generalPreferences.getTopoTreeDisplayDevice()));
        map.put("core.topoTreeClickToOpen", String.valueOf(generalPreferences.getTopoTreeClickToOpen()));
        // bind ip
        userService.updateUserIpBind(uc.getUserId(), bindIp);
        uc.getUser().setBindIp(bindIp);
        
        return NONE;
    }

    /**
     * 保存用户个性化设置
     * 
     * @return
     */
    public String saveUserPreferences() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        userPreferences.setUserId(uc.getUserId());
        String key = userPreferences.getModule() + "." + userPreferences.getName();
        if (uc.getUserPreferencesMap().containsKey(key)) {
            userService.updatePreferences(userPreferences);
        } else {
            userService.insertPreferences(userPreferences);
        }
        uc.getUserPreferencesMap().put(key, userPreferences.getValue());
        return NONE;
    }

    /**
     * 设置导航指令
     * 
     * @return
     */
    public String setNaviOrder() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        saveNaviOption(uc, naviBarOrder.toString(), naviBarVisible.toString());
        uc.getUserPreferencesMap().put("core.naviBarVisible", naviBarVisible.toString());
        uc.getUserPreferencesMap().put("core.naviBarOrder", naviBarOrder.toString());
        uc.getUserPreferencesMap().put("core.maxNaviNum", String.valueOf(maxNaviNumber));
        return NONE;
    }

    /**
     * 设置样式
     * 
     * @return
     */
    public String setStyle() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        UserPreferences preference = new UserPreferences();
        preference.setUserId(uc.getUserId());
        preference.setModule("core");
        preference.setName("styleName");
        preference.setValue(styleName);
        if (uc.getUserPreferencesMap().containsKey("core.styleName")) {
            userService.updatePreferences(preference);
        } else {
            userService.insertPreferences(preference);
        }
        uc.getUserPreferencesMap().put("core.styleName", styleName);
        uc.setStyleName(styleName);
        return NONE;
    }

    /**
     * 修改用户是否声音提示的状态
     * 
     * @return
     */
    public String modifyUserSoundStatus() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        // userService.createOrModifyUserSoundStatus(userSoundStatus, uc.getUserId());
        userPreferencesService.saveModulePreferences("userSoundStatus", "core", userSoundStatus, uc);
        return NONE;
    }

    /**
     * 修改用户设置的导航动画选项
     * 
     * @return
     */
    public String modifyNavCartoonStatus() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        userPreferencesService.saveModulePreferences("navCartoonStatus", "core", navCartoonStatus, uc);
        return NONE;
    }

    public String showSetRole() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (!uc.hasPower("userManagement")) {
            return FORBIDDEN;
        }
        return SUCCESS;
    }

    /**
     * 设置用户角色
     * 
     * @return
     */
    public String setUserRole() {
        userService.updateUserRole(userIds, roleIds);
        return NONE;
    }

    /**
     * 我的个性化设置
     * 
     * @return
     */
    public String showMyPreferemces() {
        userContext = (UserContext) getSession().get(UserContext.KEY);
        // allowIpBindLogon = systemPreferencesService.isAllowIpBindLogon();
        themes = systemPreferencesService.getSystemPreferences("theme");

        Map<String, NavigationButton> naviBarHash = new HashMap<String, NavigationButton>();
        List<NavigationButton> bars = userContext.getNaviBars();
        int size = bars == null ? 0 : bars.size();
        for (int i = 0; i < size; i++) {
            NavigationButton item = bars.get(i);
            naviBarHash.put(String.valueOf(item.getName()), item);
        }

        if (userContext.getUserPreferencesMap().containsKey("core.naviBarOrder")) {
            naviBars = new ArrayList<NavigationButton>();
            String s = userContext.getUserPreferencesMap().get("core.naviBarOrder");
            if (s != null) {
                s = s.substring(1, s.length() - 1);
                String[] arr = s.split(",");
                if (arr.length == bars.size()) {
                    for (int i = 0; i < arr.length; i++) {
                        naviBars.add(naviBarHash.get(arr[i].trim()));
                    }
                } else {
                    naviBars = bars;
                }
            }
        } else {
            naviBars = bars;
        }

        String v = userContext.getUserPreferencesMap().get("core.naviBarVisible");
        for (int i = 0; i < naviBars.size(); i++) {
            NavigationButton item = naviBars.get(i);
            if (v == null || v.indexOf(item.getName()) != -1) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
        }
        return SUCCESS;
    }

    /**
     * 显示导航选项
     * 
     * @return
     */
    public String showNaviOption() {
        userContext = (UserContext) getSession().get(UserContext.KEY);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");

        Map<String, NavigationButton> naviBarHash = new HashMap<String, NavigationButton>();
        List<NavigationButton> bars = userContext.getNaviBars();
        for (int i = 0; i < bars.size(); i++) {
            int length = bars.get(i).getDisplayName().split("\\.").length;
            if (length != 1) {
                bars.get(i).setDisplayName(resourceManager.getString(bars.get(i).getDisplayName()));
            }
        }

        int size = bars == null ? 0 : bars.size();
        for (int i = 0; i < size; i++) {
            NavigationButton item = bars.get(i);
            naviBarHash.put(String.valueOf(item.getName()), item);
        }

        if (userContext.getUserPreferencesMap().containsKey("core.naviBarOrder")) {
            naviBars = new ArrayList<NavigationButton>();
            String s = userContext.getUserPreferencesMap().get("core.naviBarOrder");
            if (s != null) {
                s = s.substring(1, s.length() - 1);
                String[] arr = s.split(",");
                if (arr.length == bars.size()) {
                    for (int i = 0; i < arr.length; i++) {
                        naviBars.add(naviBarHash.get(arr[i].trim()));
                    }
                } else {
                    naviBars = bars;
                }
            }
        } else {
            naviBars = bars;
        }

        String v = userContext.getUserPreferencesMap().get("core.naviBarVisible");
        for (int i = 0; i < naviBars.size(); i++) {
            NavigationButton item = naviBars.get(i);
            if (v == null || v.indexOf(item.getName()) != -1) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
        }
        return SUCCESS;
    }

    /**
     * 显示新建用户
     * 
     * @return
     */
    public String showNewUser() {
        // allowIpBindLogon = systemPreferencesService.isAllowIpBindLogon();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (!uc.hasPower("userManagement")) {
            return FORBIDDEN;
        }
        checkPasswdComplex = systemPreferencesService.isCheckPasswdComplex();
        userGroups = userGroupService.getAllUserGroup();
        return SUCCESS;
    }

    /**
     * 显示修改用户
     * 
     * @return
     */
    public String showModifyUser() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (!uc.hasPower("userManagement")) {
            return FORBIDDEN;
        }
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        userEx = userService.getUserEx(userId);
        userEx.setUserGroupName(resourceManager.getString(userEx.getUserGroupName()));
        userJson = JSONObject.fromObject(userEx);
        return SUCCESS;
    }

    /**
     * 显示个人信息
     * 
     * @return
     */
    public String showPersonalInfo() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        userEx = userService.getUserEx(uc.getUserId());
        return SUCCESS;
    }

    /**
     * 显示一个用户的属性
     * 
     * @return
     */
    public String showUserProperty() {
        userEx = userService.getUserEx(userId);
        return SUCCESS;
    }

    /**
     * 启用一个用户
     * 
     * @return
     */
    public String startUser() {
        userService.updateUserState(userIds, (int) User.STARTED);
        return NONE;
    }

    /**
     * 停用一个用户
     * 
     * @return
     */
    public String stopUser() {
        userService.updateUserState(userIds, (int) User.STOPPED);
        return NONE;
    }

    /**
     * 解除用户锁定
     * 
     * @return
     */
    public String unlockUser() {
        userContextManager.unlockUser(userName);
        return NONE;
    }

    /**
     * 更新用户个人信息
     * 
     * @return
     */
    public String updateUserPersonaInfo() {
        userService.updateUser(userEx);
        return NONE;
    }

    /**
     * 修改用户信息
     * 
     * @return
     */
    public String updateUserDetail() {
        userService.updateUserDetail(userEx);
        return NONE;
    }

    private void saveNaviOption(UserContext uc, String o, String v) {
        List<UserPreferences> inserts = new ArrayList<UserPreferences>();
        List<UserPreferences> updates = new ArrayList<UserPreferences>();
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("core");
        userPreferences.setName("maxNaviNum");
        userPreferences.setValue(String.valueOf(maxNaviNumber));
        if (uc.getUserPreferencesMap().containsKey("core.maxNaviNum")) {
            updates.add(userPreferences);
        } else {
            inserts.add(userPreferences);
        }

        userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("core");
        userPreferences.setName("naviBarOrder");
        userPreferences.setValue(o);
        if (uc.getUserPreferencesMap().containsKey("core.naviBarOrder")) {
            updates.add(userPreferences);
        } else {
            inserts.add(userPreferences);
        }

        userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("core");
        userPreferences.setName("naviBarVisible");
        userPreferences.setValue(v);
        if (uc.getUserPreferencesMap().containsKey("core.naviBarVisible")) {
            updates.add(userPreferences);
        } else {
            inserts.add(userPreferences);
        }

        userService.insertPreferences(inserts);
        userService.updatePreferences(updates);

        Map<String, NavigationButton> naviBarHash = new HashMap<String, NavigationButton>();
        List<NavigationButton> naviBars = uc.getNaviBars();
        List<NavigationButton> visible = uc.getNaviBarsVisible();
        int size = naviBars == null ? 0 : naviBars.size();
        for (int i = 0; i < size; i++) {
            NavigationButton item = naviBars.get(i);
            naviBarHash.put(String.valueOf(item.getName()), item);
        }
        visible.clear();
        String s = v;
        if (s.length() > 2) {
            s = s.substring(1, s.length() - 1);
            String[] arr = s.split(",");
            for (int i = 0; i < arr.length; i++) {
                visible.add(naviBarHash.get(arr[i].trim()));
            }
        }
    }

    /**
     * 个性化我的工作台
     * 
     * @return
     */
    public String showCustomMyDesk() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        long userId = uc.getUserId();
        List<String> list = userService.loadCustomMyDesk(userId);
        ServletActionContext.getRequest().setAttribute("customList", list);
        return SUCCESS;
    }

    /**
     * 个性化我的工作台
     * 
     * @return
     */
    public String showNetwork() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        return SUCCESS;
    }

    public String showSwitchRootFolder() {
        userContext = (UserContext) getSession().get(UserContext.KEY);
        return SUCCESS;
    }

    public String switchRoorFolder() {
        if (userId != 2L) {
            userService.switchRoorFolder(userId, folderId);
        }
        // 需要更改当前session的usercontext信息
        userContext = (UserContext) getSession().get(UserContext.KEY);
        userContext.getUser().setUserGroupId(folderId);
        return NONE;
    }

    /**
     * show set password page
     * 
     * @return
     */
    public String showSetPassword() {
        // 防止绕开权限，需要判断当前用户是否没有密码
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc != null) {
            return FORBIDDEN;
        }
        // 获取session中的标识，判断是否是系统生成的跳转连接
        if (getSession().get("SystemUrl") == null) {
            getSession().remove("SystemUrl");
            return FORBIDDEN;
        }
        userName = "admin";
        return SUCCESS;
    }

    public String getBindIp() {
        return bindIp;
    }

    public GeneralPreferences getGeneralPreferences() {
        return generalPreferences;
    }

    public int getMaxNaviNumber() {
        return maxNaviNumber;
    }

    public List<String> getNaviBarOrder() {
        return naviBarOrder;
    }

    public List<NavigationButton> getNaviBars() {
        return naviBars;
    }

    public List<String> getNaviBarVisible() {
        return naviBarVisible;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public String getStyleName() {
        return styleName;
    }

    public List<SystemPreferences> getThemes() {
        return themes;
    }

    public User getUser() {
        return user;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public UserEx getUserEx() {
        return userEx;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public long getUserId() {
        return userId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public boolean isAllowIpBindLogon() {
        return allowIpBindLogon;
    }

    public void setAllowIpBindLogon(boolean allowIpBindLogon) {
        this.allowIpBindLogon = allowIpBindLogon;
    }

    public boolean isCheckPasswdComplex() {
        return checkPasswdComplex;
    }

    public void setCheckPasswdComplex(boolean checkPasswdComplex) {
        this.checkPasswdComplex = checkPasswdComplex;
    }

    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }

    public void setGeneralPreferences(GeneralPreferences generalPreferences) {
        this.generalPreferences = generalPreferences;
    }

    public void setMaxNaviNumber(int maxNaviNumber) {
        this.maxNaviNumber = maxNaviNumber;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public void setNaviBarOrder(List<String> naviBarList) {
        this.naviBarOrder = naviBarList;
    }

    public void setNaviBars(List<NavigationButton> naviBars) {
        this.naviBars = naviBars;
    }

    public void setNaviBarVisible(List<String> naviBarVisible) {
        this.naviBarVisible = naviBarVisible;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public void setThemes(List<SystemPreferences> themes) {
        this.themes = themes;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    public void setUserEx(UserEx userEx) {
        this.userEx = userEx;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<FunctionItem> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<FunctionItem> customerList) {
        this.customerList = customerList;
    }

    public String getFunctionNames() {
        return functionNames;
    }

    public void setFunctionNames(String functionNames) {
        this.functionNames = functionNames;
    }

    public String getFunctionActions() {
        return functionActions;
    }

    public void setFunctionActions(String functionActions) {
        this.functionActions = functionActions;
    }

    public String getFunctionIcons() {
        return functionIcons;
    }

    public void setFunctionIcons(String functionIcons) {
        this.functionIcons = functionIcons;
    }

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public JSONObject getUserJson() {
        return userJson;
    }

    public void setUserJson(JSONObject userJson) {
        this.userJson = userJson;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public Boolean getUserSoundStatus() {
        return userSoundStatus;
    }

    public void setUserSoundStatus(Boolean userSoundStatus) {
        this.userSoundStatus = userSoundStatus;
    }

    public Boolean getNavCartoonStatus() {
        return navCartoonStatus;
    }

    public void setNavCartoonStatus(Boolean navCartoonStatus) {
        this.navCartoonStatus = navCartoonStatus;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public boolean isAllowMutliIpLoginStatus() {
        return allowMutliIpLoginStatus;
    }

    public void setAllowMutliIpLoginStatus(boolean allowMutliIpLoginStatus) {
        this.allowMutliIpLoginStatus = allowMutliIpLoginStatus;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

}
