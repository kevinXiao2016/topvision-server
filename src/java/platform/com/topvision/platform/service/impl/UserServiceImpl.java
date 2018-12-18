/**
 *
 */
package com.topvision.platform.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.exception.dao.HasBindByIPException;
import com.topvision.exception.dao.UpdatePasswdException;
import com.topvision.exception.service.AuthenticationException;
import com.topvision.exception.service.ExistException;
import com.topvision.exception.service.LimitIpLogonException;
import com.topvision.exception.service.UserStateException;
import com.topvision.framework.common.MD5;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.dao.FavouriteFolderDao;
import com.topvision.platform.dao.PortletItemDao;
import com.topvision.platform.dao.RoleDao;
import com.topvision.platform.dao.SystemLogDao;
import com.topvision.platform.dao.UserDao;
import com.topvision.platform.dao.UserPreferencesDao;
import com.topvision.platform.domain.FavouriteFolder;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.GeneralPreferences;
import com.topvision.platform.domain.MenuItem;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.PortletItem;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.ToolbarButton;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserPreferencesMap;
import com.topvision.platform.domain.UserRoleRela;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.UIService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.user.context.UserContextManager;
import com.topvision.platform.user.message.UserEvent;
import com.topvision.platform.user.message.UserInfoListener;
import com.topvision.platform.user.message.UserPreferencesListener;

/**
 * @author niejun
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements UserService {
    private static final String SUPER = "superadmin";
    @Autowired
    private final UserDao userDao = null;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private final SystemLogDao systemLogDao = null;
    @Autowired
    private final UserPreferencesDao userPreferencesDao = null;
    @Autowired
    private final FavouriteFolderDao favouriteFolderDao = null;
    @Autowired
    private PortletItemDao portletItemDao = null;
    @Autowired
    private UIService uiService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserContextManager ucm;
    @Autowired
    private LicenseIf licenseIf;
    public static final Long USERINFO = 201L;
    public static final String ADMIN_USER = "admin";
    public static final String ENTITY = "entity";
    private static final String TABLE_ENTITY_PRE = "t_entity_";
    public static final String TOPOFOLDER = "topofolder";
    private static final String VIEW_TOPO_PRE = "V_Topo_";

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#deleteFavouriteFolder
     * (java.util.List)
     */
    @Override
    public void deleteFavouriteFolder(List<Long> folderIds) {
        favouriteFolderDao.deleteByPrimaryKey(folderIds);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#deleteFavouriteFolder (Long)
     */
    @Override
    public void deleteFavouriteFolder(Long folderId) {
        favouriteFolderDao.deleteByPrimaryKey(folderId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#deleteFavouriteFolder(java .lang.String)
     */
    @Override
    public void deleteFavouriteFolder(String folderPath) {
        favouriteFolderDao.deleteFavouriteFolder(folderPath + "%");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#deleteUser(java.util .List)
     */
    @Override
    public void deleteUser(List<Long> userIds) {
        for (Long userid : userIds) {
            UserEx user = userDao.selectByPrimaryKey(userid);
            ucm.userDeleted(user.getUserName());
        }
        userDao.deleteByPrimaryKey(userIds);
    }

    private Boolean existIpLimit(String ip, String rule) {
        boolean flag = false;
        if (rule != null) {
            StringTokenizer st = new StringTokenizer(rule, ",");
            while (st.hasMoreTokens()) {
                if (st.nextToken().trim().equals(ip)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#getFavouriteFolder (Long)
     */
    @Override
    public FavouriteFolder getFavouriteFolder(Long folderId) {
        return favouriteFolderDao.selectByPrimaryKey(folderId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#getRoleByUserId()
     */
    @Override
    public List<Role> getRoleByUserId(Long userId) {
        return roleDao.getRoleByUser(userId);
    }

    public SystemLogDao getSystemLogDao() {
        return systemLogDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#getUser(java.lang.String)
     */
    @Override
    public User getUser(String username) {
        return userDao.selectByUsername(username);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#getUserEx(Long)
     */
    @Override
    public UserEx getUserEx(Long userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#getUserPreferences (Long)
     */
    @Override
    public List<UserPreferences> getUserPreferences(Long userId) {
        return userPreferencesDao.getUserPreferences(userId);
    }

    public UserPreferencesDao getUserPreferencesDao() {
        return userPreferencesDao;
    }

    private UserPreferencesMap<String, String> getUserPreferencesMap(Long userId) {
        final UserPreferencesMap<String, String> userPreferences = new UserPreferencesMap<String, String>();
        userPreferencesDao.handleUserPreferences(userId, new MyResultHandler() {
            @Override
            public void complete() {
            }

            @Override
            public void prepare() {
            }

            @Override
            public void handleResult(ResultContext resultcontext) {
                UserPreferences preference = (UserPreferences) resultcontext.getResultObject();
                userPreferences.put(preference.getModule() + "." + preference.getName(), preference.getValue());
            }
        });
        return userPreferences;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.system.service.UserService#hasBindByIp(java.lang .String)
     */
    @Override
    public User hasBindByIp(String ip) throws HasBindByIPException {
        return userDao.hasBindByIp(ip);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#insertFavouriteFolder
     * (com.topvision.ems.server.system.domain.FavouriteFolder)
     */
    @Override
    public void insertFavouriteFolder(FavouriteFolder folder) {
        favouriteFolderDao.insertEntity(folder);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#insertPreferences(java.util .List)
     */
    @Override
    public void insertPreferences(List<UserPreferences> preferences) {
        userPreferencesDao.insertEntity(preferences);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.common.service.DefaultService#savePreferences
     * (com.topvision.ems.server.system.domain.UserPreferences)
     */
    @Override
    public void insertPreferences(UserPreferences preferences) {
        userPreferencesDao.insertEntity(preferences);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#insertUser(com. topvision
     * .ems.server.system .domain.User)
     */
    @Override
    public void insertUser(UserEx user) throws ExistException {
        User temp = userDao.selectByUsername(user.getUserName());
        if (temp != null) {
            throw new ExistException("Exist User " + user.getUserName());
        }
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setPasswd(MD5.makeMd5(user.getPasswd()));
        userDao.insertUsersInfo(user);

        StringTokenizer st = new StringTokenizer(user.getRoleIds(), ",");
        List<UserRoleRela> roles = new ArrayList<UserRoleRela>();
        while (st.hasMoreTokens()) {
            UserRoleRela role = new UserRoleRela();
            role.setUserId(user.getUserId());
            role.setRoleId(Long.parseLong(st.nextToken()));
            roles.add(role);
        }
        roleDao.inserUserRole(roles);

        List<PortletItem> items = new ArrayList<PortletItem>();
        PortletItem item = new PortletItem();
        item.setUserId(user.getUserId());
        item.setItemId(USERINFO);
        items.add(item);
        portletItemDao.insertEntity(items);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#loadFavouriteFolder (Long)
     */
    @Override
    public List<FavouriteFolder> loadFavouriteFolder(Long userId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        return favouriteFolderDao.selectByMap(map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#loadUserList()
     */
    @Override
    public List<UserEx> loadUserList() {
        List<UserEx> list = userDao.loadUserList();
        List<UserRoleRela> urs = roleDao.getUserRoles();
        Map<String, String> map = new HashMap<String, String>();
        String key = null;
        String value = null;
        UserRoleRela ur = null;
        for (int i = 0; i < urs.size(); i++) {
            ur = urs.get(i);
            key = String.valueOf(ur.getUserId());
            value = map.get(key);
            if (value == null) {
                value = ur.getName();
            } else {
                value = value + ", " + ur.getName();
            }
            map.put(key, value);
        }

        UserEx user = null;
        for (int i = 0; i < list.size(); i++) {
            user = list.get(i);
            user.setRoleNames(map.get(String.valueOf(user.getUserId())));
        }
        return list;
    }

    @Override
    public String getUserAuthorityViewName(Long userId) {
        User user = userDao.selectByPrimaryKey(userId);
        if (user != null) {
            if (user.getUserName().equals(ADMIN_USER)) {
                return ENTITY;
            }
            return TABLE_ENTITY_PRE + user.getUserGroupId();
        }
        return ENTITY;
    }

    @Override
    public String getUserAuthorityFolderName(Long userId) {
        User user = userDao.selectByPrimaryKey(userId);
        if (user.getUserName().equals(ADMIN_USER)) {
            return TOPOFOLDER;
        }
        return VIEW_TOPO_PRE + user.getUserGroupId();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#loadUserUI(com.topvision. platform
     * .domain.UserContext)
     */
    @Override
    public void loadUserUI(UserContext uc) {
        // 用户常规选项
        // modify by bravin@20131111:加载用户首选项的功能在用户登录时操作
        // uc.setUserPreferencesMap(getUserPreferencesMap(uc.getUserId()));

        // 获取用户所有角色下的功能项, 并进行散列映射封装
        List<String> powers = uiService.getUserPowerByUser(uc.getUserId());
        Set<String> functionHash = new HashSet<String>();
        functionHash.addAll(powers);
        // 设置用户的权限映射
        uc.setFunctionHash(functionHash);

        // 设置用户菜单
        // List<MenuItem> menus = uiService.getMenuItemByUser(uc.getUserId());
        List<MenuItem> menus = uiService.loadMenus();
        uc.setMenuItems(menus);

        // 设置用户工具栏
        // List<ToolbarButton> buttons =
        // uiService.getToolbarButtonByUser(uc.getUserId());
        List<ToolbarButton> buttons = uiService.loadToolbarButton();
        uc.setToolbarButtons(buttons);

        // 设置导航按钮
        List<NavigationButton> navigationButtons = uiService.getNavigationButtonByUser(uc.getUserId());
        List<NavigationButton> naviBarsVisible = null;
        String naviBarList = uc.getUserPreferencesMap().get("core.naviBarVisible");
        if (naviBarList != null) {
            naviBarsVisible = new ArrayList<NavigationButton>();
            // naviBarList为[]表示没有可见的导航条
            if (naviBarList.length() > 2) {
                naviBarList = naviBarList.substring(1, naviBarList.length() - 1);
                String[] arr = naviBarList.split(",");
                Map<String, NavigationButton> keys = new HashMap<String, NavigationButton>();
                for (int i = 0; i < navigationButtons.size(); i++) {
                    keys.put(navigationButtons.get(i).getName(), navigationButtons.get(i));
                }
                for (int i = 0; i < arr.length; i++) {
                    if (keys.get(arr[i].trim()) != null) {
                        naviBarsVisible.add(keys.get(arr[i].trim()));
                    }
                }
            }
        } else {
            naviBarsVisible = navigationButtons;
        }
        // 设置用户可用的导航条
        uc.setNaviBars(navigationButtons);
        // 设置用户当前可见的导航条
        uc.setNaviBarsVisible(naviBarsVisible);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.system.service.UserService#loginForDesktop(java .lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void loginForDesktop(String username, String passwd, String ip)
            throws UserStateException, AuthenticationException {
        User user = userDao.selectByUsername(username);
        if (user == null || MD5.makeMd5(passwd.trim()).equalsIgnoreCase(user.getPasswd()) == false) {
            throw new AuthenticationException();
        }
        if (user.getStatus() == User.STOPPED) {
            throw new UserStateException();
        }
    }

    @Override
    public void removePreferences(UserPreferences Preferences) {
        userPreferencesDao.updateEntity(Preferences);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService# saveGeneralPreferences (Long,
     * com.topvision.ems.server.system.domain.GeneralPreferences)
     */
    @Override
    public void saveGeneralPreferences(Long userId, GeneralPreferences generalPreferences) {
        List<UserPreferences> preferencesInserted = new ArrayList<UserPreferences>();
        List<UserPreferences> preferencesUpdated = new ArrayList<UserPreferences>();
        Map<String, String> map = getUserPreferencesMap(userId);

        // tabbed
        boolean tabbed = generalPreferences.isTabbed();
        UserPreferences preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("tabbed");
        preference.setValue(String.valueOf(tabbed));
        if (map.containsKey("core.tabbed")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // alarmWhenCloseManyTab
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("alarmWhenCloseManyTab");
        preference.setValue(String.valueOf(generalPreferences.isAlarmWhenCloseManyTab()));
        if (map.containsKey("core.alarmWhenCloseManyTab")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // switchWhenNewTab
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("switchWhenNewTab");
        preference.setValue(String.valueOf(generalPreferences.isSwitchWhenNewTab()));
        if (map.containsKey("core.switchWhenNewTab")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // notifyWhenMsg
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("notifyWhenMsg");
        preference.setValue(String.valueOf(generalPreferences.isNotifyWhenMsg()));
        if (map.containsKey("core.notifyWhenMsg")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // style
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("styleName");
        preference.setValue(generalPreferences.getStyleName());
        if (map.containsKey("core.styleName")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // tooltip style
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("displayInputTip");
        preference.setValue(String.valueOf(generalPreferences.isDisplayInputTip()));
        if (map.containsKey("core.displayInputTip")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // pageSize
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("pageSize");
        preference.setValue(String.valueOf(generalPreferences.getPageSize()));
        if (map.containsKey("core.pageSize")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // macDisplayStyle
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("macDisplayStyle");
        preference.setValue(generalPreferences.getMacDisplayStyle());
        if (map.containsKey("core.macDisplayStyle")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // tabMaxLimit
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("tabMaxLimit");
        preference.setValue(String.valueOf(generalPreferences.isTabMaxLimit()));
        if (map.containsKey("core.tabMaxLimit")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // tipShowTime
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("tipShowTime");
        preference.setValue(String.valueOf(generalPreferences.getTipShowTime()));
        if (map.containsKey("core.tipShowTime")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // weekStartDay
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("weekStartDay");
        preference.setValue(String.valueOf(generalPreferences.getWeekStartDay()));
        if (map.containsKey("core.weekStartDay")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }
        // autoRefreshTime
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("autoRefreshTime");
        preference.setValue(String.valueOf(generalPreferences.getAutoRefreshTime()));
        if (map.containsKey("core.autoRefreshTime")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }
        // topNumber
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("topNumber");
        preference.setValue(String.valueOf(generalPreferences.getTopNumber()));
        if (map.containsKey("core.topNumber")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // soundTimeInterval
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("soundTimeInterval");
        preference.setValue(String.valueOf(generalPreferences.getSoundTimeInterval()));
        if (map.containsKey("core.soundTimeInterval")) {
            preferencesUpdated.add(preference);
        } else {
            preferencesInserted.add(preference);
        }

        // topoTreeDisplayDevice
        int topoTreeDisplayDevice = 1;
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("topoTreeDisplayDevice");
        preference.setValue(String.valueOf(generalPreferences.getTopoTreeDisplayDevice()));
        if (map.containsKey("core.topoTreeDisplayDevice")) {
            preferencesUpdated.add(preference);
            topoTreeDisplayDevice = Integer.valueOf(map.get("core.topoTreeDisplayDevice"));
        } else {
            preferencesInserted.add(preference);
        }

        // topoTreeClickToOpen
        int topoTreeClickToOpen = 1;
        preference = new UserPreferences();
        preference.setUserId(userId);
        preference.setModule("core");
        preference.setName("topoTreeClickToOpen");
        preference.setValue(String.valueOf(generalPreferences.getTopoTreeClickToOpen()));
        if (map.containsKey("core.topoTreeClickToOpen")) {
            preferencesUpdated.add(preference);
            topoTreeClickToOpen = Integer.valueOf(map.get("core.topoTreeClickToOpen"));
        } else {
            preferencesInserted.add(preference);
        }

        if (generalPreferences.getTopoTreeClickToOpen() != topoTreeClickToOpen
                || generalPreferences.getTopoTreeDisplayDevice() != topoTreeDisplayDevice) {
            //通知拓扑视图用户个性设置已改变
            UserEvent event = new UserEvent(userId);
            event.setActionName("userPreferencesChanged");
            event.setListener(UserPreferencesListener.class);
            messageService.fireMessage(event);
        }

        insertPreferences(preferencesInserted);
        updatePreferences(preferencesUpdated);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.system.service.UserService#selectUserByIp(java. lang.String)
     */
    @Override
    public User selectUserByIp(String ip) {
        return userDao.selectByIp(ip);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.system.service.UserService#login(java.lang.String, java.lang.String)
     */
    @Override
    public UserContext txLogin(String username, String passwd, String ip)
            throws AuthenticationException, UserStateException, LimitIpLogonException {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            throw new AuthenticationException("UserNotExist");
        }
        if (user.getStatus() == User.STOPPED) {
            throw new UserStateException();
        }
        if ("admin".equalsIgnoreCase(username) && (user.getPasswd() == null || user.getPasswd().trim().length() == 0)) {
            throw new AuthenticationException("PasswordIsEmpty");
        }
        if ("superadmin".equalsIgnoreCase(username)) {
            if (licenseIf.verifySuperPasswd(passwd.trim()) == false) {
                throw new AuthenticationException("PasswordInvalid");
            }
        } else if (MD5.makeMd5(passwd.trim()).equalsIgnoreCase(user.getPasswd()) == false) {
            throw new AuthenticationException("PasswordInvalid");
        }
        if (existIpLimit(ip, user.getLimitIp())) {
            throw new LimitIpLogonException(ip + " is limited for logon.");
        }

        Timestamp current = new Timestamp(System.currentTimeMillis());
        user.setLastLoginTime(current);
        user.setLastLoginIp(ip);
        // 更新数据库中的最近登录时间
        userDao.updateLastLoginTime(user);
        // 插入用户登录系统的的日志
        syslog(username, ip, "Login");
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#txLoginByIp(java.lang.String)
     */
    @Override
    public UserContext txLoginByIp(String ip) {
        UserContext uc = null;
        User user = userDao.selectByIp(ip);
        if (user != null && user.getStatus() == User.STARTED) {
            // 是否存在IP登陆限制
            if (existIpLimit(ip, user.getLimitIp())) {
                return null;
            }

            // 存在IP绑定,插入自动登陆的系统日志
            Timestamp current = new Timestamp(System.currentTimeMillis());
            user.setLastLoginIp(ip);
            user.setLastLoginTime(current);
            userDao.updateLastLoginTime(user);
            String username = user.getUserName();

            syslog(username, user.getLastLoginIp(), "AutoLoginIP");

            uc = ucm.getUserContext(username);
            // 设置usercontext中的上次登录时间
            ucm.setLastLoginTime(username, current);
        }
        return uc;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#txMoveFavouriteFolder
     * (com.topvision.ems.server.system.domain.FavouriteFolder)
     */
    @Override
    public void txMoveFavouriteFolder(FavouriteFolder folder) {
        favouriteFolderDao.moveFavouriteFolder(folder);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#renameFavouriteFolder
     * (com.topvision.ems.server.system.domain.FavouriteFolder)
     */
    @Override
    public void txRenameFavouriteFolder(FavouriteFolder folder) {
        favouriteFolderDao.renameFavouriteFolder(folder);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#updateFavouriteFolder
     * (com.topvision.ems.server.system.domain.FavouriteFolder)
     */
    @Override
    public void updateFavouriteFolder(FavouriteFolder folder) {
        favouriteFolderDao.updateEntity(folder);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#updatePasswd(com.topvision
     * .platform.domain.User)
     */
    @Override
    public void updatePasswd(User user) throws UpdatePasswdException {
        UserContext uc = ucm.getUserContext(user.getUserName());
        uc.getUser().setPasswd(user.getPasswd());
        userDao.updatePasswd(user);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#updatePreferences(java.util .List)
     */
    @Override
    public void updatePreferences(List<UserPreferences> preferences) {
        userPreferencesDao.updateEntity(preferences);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#updatePreferences(com. topvision
     * .platform.domain.UserPreferences)
     */
    @Override
    public void updatePreferences(UserPreferences Preferences) {
        userPreferencesDao.updateEntity(Preferences);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#updateStatusByUserName(java .lang.String,
     * java.lang.Integer)
     */
    @Override
    public void updateStatusByUserName(String username, Integer state) {
        if (username.equals(SUPER)) {
            return;
        }
        User user = new User();
        user.setUserName(username);
        user.setStatus(state);
        userDao.updateStatusByUserName(user);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.system.service.UserService#updateUser(com. topvision
     * .platform.system .domain.UserEx)
     */
    @Override
    public void updateUser(UserEx user) {
        userDao.updateUserDetail(user);
        // 通知用户信息已更改，公告中使用的是缓存中的用户，需要同步更新
        UserEvent event = new UserEvent(user);
        event.setActionName("userInfoChanged");
        event.setListener(UserInfoListener.class);
        messageService.fireMessage(event);
        // reset session
        // Object attribute =
        // ServletActionContext.getRequest().getSession().getAttribute(UserContext.KEY);
        // modify by bravin@20140527: 改sessio是没有效果的,因为存在多个用户使用同一个账户登录
        User u = ucm.getUserContext(user.getUserName()).getUser();
        u.setFamilyName(user.getFamilyName());
        u.setWorkTelphone(user.getWorkTelphone());
        u.setHomeTelphone(user.getHomeTelphone());
        u.setMobile(user.getMobile());
        u.setEmail(user.getEmail());
        u.setDepartmentId(user.getDepartmentId());
        u.setDepartmentName(user.getDepartmentName());
        u.setPlaceId(user.getPlaceId());
        u.setPlaceName(user.getPlaceName());
        // ((UserContext) attribute).setUser(u);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#updateUserDetail(com.topvision
     * .platform.domain.UserEx)
     */
    @Override
    public void updateUserDetail(User user) {
        if (user.getUserId() == 2L) {
            user.setUserGroupId(null);
        }
        userDao.updateUserDetail(user);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.system.service.UserService#updateUserIpBind(Long, boolean,
     * java.lang.String)
     */
    @Override
    public void updateUserIpBind(Long userId, String ip) {
        User user = new User();
        user.setUserId(userId);
        user.setBindIp(ip);
        userDao.updateUserIpBind(user);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#updateUserPasswd(java .util.List,
     * java.lang.String)
     */
    @Override
    public void updateUserPasswd(List<Long> userIds, String passwd) {
        String p = MD5.makeMd5(passwd);
        List<User> users = new ArrayList<User>();
        User user = null;
        for (int i = 0; i < userIds.size(); i++) {
            user = new User();
            user.setUserId(userIds.get(i));
            user.setPasswd(p);
            users.add(user);
        }
        userDao.updateUserPasswd(users);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#updateUserRole(java .util.List,
     * java.util.List)
     */
    @Override
    public void updateUserRole(List<Long> userIds, List<Long> roleIds) {
        roleDao.deleteRoleByUser(userIds);
        List<UserRoleRela> urs = new ArrayList<UserRoleRela>();
        UserRoleRela ur = null;
        for (int i = 0; i < userIds.size(); i++) {
            // if (userIds.get(i) == User.ADMINISTRATOR_ID) {
            // continue;
            // }
            for (int j = 0; j < roleIds.size(); j++) {
                ur = new UserRoleRela();
                ur.setUserId(userIds.get(i));
                ur.setRoleId(roleIds.get(j));
                urs.add(ur);
            }
        }
        roleDao.inserUserRole(urs);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.server.system.service.UserService#updateUserState(java .util.List,
     * int)
     */
    @Override
    public void updateUserState(List<Long> userIds, Integer state) {
        List<User> users = new ArrayList<User>();
        User user = null;
        for (int i = 0; i < userIds.size(); i++) {
            user = new User();
            user.setUserId(userIds.get(i));
            user.setStatus(state);
            users.add(user);
            UserEx ue = userDao.selectByPrimaryKey(userIds.get(i));
            UserContext uc = ucm.getUserContext(ue.getUserName());
            uc.getUser().setStatus(state);
        }
        userDao.updateUserStatus(users);
    }

    @Override
    public List<NavigationButton> loadcustomMyDesk() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#getUserCount()
     */
    @Override
    public Integer getUserCount() {
        return userDao.getUserCount();
    }

    @Override
    public List<FunctionItem> loadUserWorkbence(long userId) {
        return userDao.loadUserWorkbence(userId);
    }

    @Override
    public void updateCustomMydesck(long userId, List<FunctionItem> items) {
        userDao.updateCustomMydesck(userId, items);
    }

    @Override
    public List<String> loadCustomMyDesk(long userId) {
        return userDao.loadCustomMyDesk(userId);
    }

    @Override
    public void updateLanguage(long userId, String lang) {
        userDao.updateLanguage(userId, lang);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#syslog(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void syslog(String username, String ip, String desc) {
        if (username.equals(SUPER) && (desc.equals("ExitSystem") || desc.equals("Logoff") || desc.equals("Login")
                || desc.equals("AutoLoginIP"))) {
            return;
        }
        SystemLog sysLog = new SystemLog();
        sysLog.setUserName(username);
        sysLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysLog.setIp(ip);
        sysLog.setDescription(desc);
        systemLogDao.insertEntity(sysLog);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#getRoleUsedCount(java.lang. Long)
     */
    @Override
    public Integer getRoleUsedCount(Long roleId) {
        return userDao.getRoleUsedCount(roleId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#getDepartmentUsedCount(java. lang.Long)
     */
    @Override
    public Integer getDepartmentUsedCount(Long departmentId) {
        return userDao.getDepartmentUsedCount(departmentId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.UserService#getPostUsedCount(java.lang. Long)
     */
    @Override
    public Integer getPostUsedCount(Long postId) {
        return userDao.getPostUsedCount(postId);
    }

    @Override
    public void switchRoorFolder(Long userId, Long folderId) {
        userDao.switchRoorFolder(userId, folderId);
    }

    /**
     * @return the portletItemDao
     */
    public PortletItemDao getPortletItemDao() {
        return portletItemDao;
    }

    /**
     * @param portletItemDao
     *            the portletItemDao to set
     */
    public void setPortletItemDao(PortletItemDao portletItemDao) {
        this.portletItemDao = portletItemDao;
    }

    @Override
    public void updateMutilIpLogin(long userId, boolean allowMutliIpLoginStatus) {
        userDao.updateMutilIpLogin(userId, allowMutliIpLoginStatus);
    }

    @Override
    public void updateUserSession(long userId, int timeout) {
        userDao.updateUserSession(userId, timeout);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.UserService#getUserAlertTypeId(java.lang.Long)
     */
    @Override
    public List<Integer> getUserAlertTypeId(Long userId) {
        return userDao.getUserAlertTypeId(userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.UserService#updateUserAlertType(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void updateUserAlertType(Long userId, List<Integer> alertTypeId) {
        userDao.updateUserAlertType(userId, alertTypeId);
    }
}
