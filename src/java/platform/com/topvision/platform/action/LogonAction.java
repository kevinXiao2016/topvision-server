package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import com.topvision.exception.service.AuthenticationException;
import com.topvision.exception.service.LimitIpLogonException;
import com.topvision.exception.service.UserStateException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.license.common.exception.LicenseException;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.LicenseView;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferencesMap;
import com.topvision.platform.message.event.LogoffEvent;
import com.topvision.platform.message.event.LogoffListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.FrontEndLogService;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.user.context.UserContextManager;

import net.sf.json.JSONObject;

@Controller("logonAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogonAction extends BaseAction {
    private static final long serialVersionUID = -6996002379533913570L;
    private static final Logger logger = LoggerFactory.getLogger(LogonAction.class);

    public static boolean isSetAppName = true;
    private boolean usedFirstly = false;
    private int displayMode = 0;
    private Date today;
    private String licenseInfo;
    private UserContext userContext;
    private String username;
    private String passwd;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private FrontEndLogService frontEndLogService;
    @Autowired
    private UserContextManager ucm;
    @Autowired
    private MessageService messageService;
    private Boolean frontEndLogSwitch;
    @Value("${socket.port:3006}")
    public int socketServerPort;

    /**
     * 是否开启声音提示
     */
    private Boolean musicFlag;
    /**
     * 是否使用导航动画
     */
    private Boolean navCartoonFlag;
    private String lastSelectedNaviBar;
    /* 记录用户是否已锁屏了 */
    private boolean lockScreen;
    // Add by Victor@20141013根据License判断是否展示mobile客户端下载
    private boolean hasMobilePower;
    @Autowired
    private LicenseIf licenseIf;

    /**
     * @param licenseService
     *            the licenseService to set
     */
    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    /**
     * 不提示的注销
     * 
     * @return
     */
    public String logoffQuietly() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc != null) {
            
            LogoffEvent evt = new LogoffEvent("logoff");
            evt.setActionName("logoff");
            evt.setSessionId(request.getSession().getId());
            evt.setListener(LogoffListener.class);
            messageService.fireMessage(evt);
            
            getSession().clear();
            request.getSession().invalidate();
            ucm.exitUser(uc, request.getRemoteHost());
            userService.syslog(uc.getUser().getUserName(), uc.getUser().getLastLoginIp(), "ExitSystem");
        }
        return NONE;
    }

    /**
     * 登录
     * 
     * @return
     * @throws Exception
     */
    public String logon() throws Exception {
        JSONObject json = new JSONObject();
        System.out.println(super.limit);
        json.put("licenseExpired", !licenseIf.checkLicense());
        json.put("authenticated", false);
        String addr = request.getRemoteAddr();
        UserContext uc = null;
        try {
            uc = ucm.getUserContext(username, true);
            if (uc != null && uc.isLocked()) {
                json.put("locked", true);
            } else if (!uc.allowRepeatedlyLogon() && ucm.ifUserLoggedon(username, request)) {
                // 不允许重复登录
                json.put("errorCode", 4);
            } else {
                userService.txLogin(username, passwd, addr);
                getSession().put(UserContext.KEY, uc);
                json.put("authenticated", true);
                // 登录时修改sessionTimeout
                int $timeout = uc.getUser().getTimeout();
                request.getSession().setMaxInactiveInterval($timeout == -1 ? -1 : ($timeout * 60));
                ucm.registryClientHost(uc, request.getRemoteHost());
                if (getSession().containsKey("lockScreen")) {
                    getSession().remove("lockScreen");
                }
                // Modify by Victor@20140616对超级用户session超时时间改为1天
                if ("superadmin".equals(username) || SystemConstants.isDevelopment) {
                    request.getSession().setMaxInactiveInterval(86400);
                    uc.setSuperAdmin(true);
                } else {
                    // Modify by Victor@20140911解决EMS-9930【EMS-V2.2.6.0】：关闭开发模式后，重新登录仍然有相关页面，打开提示无权限
                    uc.setSuperAdmin(false);
                }
            }
        } catch (AuthenticationException aex) {
            logger.debug("Authentication Failure:", aex);
            // 密码或者用户名错误
            if (aex.getMessage().equals("UserNotExist")) {
                json.put("errorCode", 11);
            } else if (aex.getMessage().equals("PasswordInvalid")) {
                json.put("errorCode", 12);
                if (!"admin".equals(username) && !"superadmin".equals(username)) {
                    if (systemPreferencesService.isStopUserWhenErrors()) {
                        if (ucm.ifUserLocked(username)) {
                            json.put("locked", true);
                            json.put("totalCount", ucm.getStopNumber());
                        } else {
                            int counter = uc.getTryPwdCounter();
                            counter = ucm.getStopNumber() - counter;
                            json.put("restCount", counter);
                        }
                    }
                }
            } else if (aex.getMessage().equals("PasswordIsEmpty")) {
                getSession().put("SystemUrl", true);
                json.put("errorCode", 13);
            } else {
                json.put("errorCode", 1);
            }
            userService.syslog(username, addr, "LoginFailurePWD");
        } catch (UserStateException use) {
            logger.debug("User state Failure:", use);
            // 用户被停用
            json.put("errorCode", 2);
            userService.syslog(username, addr, "LoginFailureSTOP");
        } catch (LimitIpLogonException lie) {
            logger.debug(lie.getMessage());
            // IP被限制
            json.put("errorCode", 3);
            userService.syslog(username, addr, "LoginFailureIP");
        } finally {
            json.write(response.getWriter());
        }
        return NONE;
    }

    public String updatePasswd() throws Exception {
        JSONObject json = new JSONObject();
        try {
            if (username == null || !"admin".equalsIgnoreCase(username)) {
                throw new AuthenticationException("UserError");
            }
            if (passwd == null || passwd.trim().length() == 0) {
                throw new AuthenticationException("PasswdIsEmpty");
            }
            List<Long> ids = new ArrayList<Long>();
            ids.add(2L);// admin的ID为2
            userService.updateUserPasswd(ids, passwd);
            json.put("result", "SUCCESS");
        } catch (Exception e) {
            logger.error("", e);
            json.put("result", e.getMessage());
        } finally {
            json.write(response.getWriter());
        }
        return NONE;
    }

    /**
     * 远程登录。
     * 
     * @return
     * @throws AuthenticationException
     *             , UserStateException, LimitIpLogonException
     */
    @Deprecated
    public String logonFromClient() throws AuthenticationException, UserStateException, LimitIpLogonException {
        try {
            String addr = request.getRemoteAddr();
            UserContext uc = userService.txLogin(username, passwd, addr);
            uc.setStandalone(true);
            getSession().put(UserContext.KEY, uc);
            logger.info("" + new Exception());
            return showMainFrame();
        } catch (AuthenticationException e) {
            logger.error("", e);
            return "failure";
        } catch (UserStateException e) {
            logger.error("", e);
            return "failure";
        } catch (LimitIpLogonException e) {
            logger.error("", e);
            return "failure";
        }
    }

    /**
     * 注销.
     * 
     * @return
     * @throws Throwable
     */
    public String logoff() {
        // 验证license
        try {
            if (!licenseService.verifyLicense()) {
                return "licenseTip";
            }
        } catch (DataAccessException dae) {
            logger.debug(dae.getMessage(), dae);
            return "databaseTip";
        } catch (LicenseException le) {
            logger.debug(le.getMessage(), le);
            return "dongleTip";
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            return "error";
        }
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc != null) {
 
            LogoffEvent evt = new LogoffEvent("logoff");
            evt.setActionName("logoff");
            evt.setSessionId(request.getSession().getId());
            evt.setListener(LogoffListener.class);
            messageService.fireMessage(evt);
            
            getSession().clear();
            request.getSession().invalidate();
            ucm.exitUser(uc, request.getRemoteHost());
            userService.syslog(uc.getUser().getUserName(), uc.getUser().getLastLoginIp(), "Logoff");
        }
        // Modify by Victor@20140522把IP放到session中去
        request.getSession().setAttribute("ip", ServletActionContext.getRequest().getRemoteAddr());
        hasMobilePower = licenseIf.isSupportModule("mobile");
        return SUCCESS;
    }

    /**
     * 显示登录页面.
     * 
     * @return
     * @throws Throwable
     */
    public String showLogon() {
        setWebAppName();
        // Modify by Victor@20140522把IP放到session中去
        request.getSession().setAttribute("ip", request.getRemoteAddr());
        hasMobilePower = licenseIf.isSupportModule("mobile");

        // 验证license
        try {
            if (!licenseService.verifyLicense()) {
                return "licenseTip";
            }
        } catch (DataAccessException dae) {
            logger.debug(dae.getMessage(), dae);
            return "databaseTip";
        } catch (LicenseException le) {
            logger.debug(le.getMessage(), le);
            return "dongleTip";
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            return "error";
        }

        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc == null) {
            // 判断IP地址是否已经绑定到某个用户
            uc = userService.txLoginByIp(request.getRemoteAddr());
            if (uc != null) {
                // 判断是否用户已经登录了
                if (systemPreferencesService.isAllowRepeatedlyLogon()
                        || !ucm.ifUserLoggedon(uc.getUser().getUserName(), request)) {
                    getSession().put(UserContext.KEY, uc);
                    ucm.registryClientHost(uc, request.getRemoteHost());
                    return "mainFrame";
                }
            }
            usedFirstly = systemPreferencesService.isUsedFirstly();
        } else {
            return "mainFrame";
        }

        return SUCCESS;
    }

    /**
     * 转到主窗口.
     * 
     * @return
     */
    public String showMainFrame() {
        setWebAppName();
        // 是否第一次访问
        usedFirstly = systemPreferencesService.isUsedFirstly();
        if (usedFirstly) {
            systemPreferencesService.updateUsedFirstly(false);
        }
        frontEndLogSwitch = frontEndLogService.getFrontEndLogStatus(request.getSession().getId());
        today = new Date();
        userContext = (UserContext) getSession().get(UserContext.KEY);
        ucm.registryClientHost(userContext, request.getRemoteHost());
        UserPreferencesMap<String, String> userPreferencesMap = userContext.getUserPreferencesMap();
        userContext.setModules(ucm.getModules());
        userService.loadUserUI(userContext);
        musicFlag = userPreferencesMap.getBoolean("core.userSoundStatus", true);
        navCartoonFlag = userPreferencesMap.getBoolean("core.navCartoonStatus", false);
        lastSelectedNaviBar = userPreferencesMap.getString("core.lastSelectedNaviBar", "Workbench");
        if (getSession().containsKey("lockScreen")) {
            lockScreen = true;
        }
        return SUCCESS;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public final String getLicenseInfo() {
        // licenseInfo = LicenseManager.getInstance().getLicenseInfo();
        return licenseInfo;
    }

    public LicenseView getLicenseView() {
        if (this.licenseService != null) {
            return this.licenseService.getLicenseView();
        }

        return null;
    }

    public Date getToday() {
        return today;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUsedFirstly() {
        return usedFirstly;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public final void setLicenseInfo(String licenseInfo) {
        this.licenseInfo = licenseInfo;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public void setUsedFirstly(boolean firstly) {
        usedFirstly = firstly;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private void setWebAppName() {
        if (isSetAppName) {
            isSetAppName = false;
            SystemConstants.WEBAPP_NAME = ServletActionContext.getRequest().getContextPath();
        }
    }

    public Boolean getMusicFlag() {
        return musicFlag;
    }

    public void setMusicFlag(Boolean musicFlag) {
        this.musicFlag = musicFlag;
    }

    public Boolean getNavCartoonFlag() {
        return navCartoonFlag;
    }

    public void setNavCartoonFlag(Boolean navCartoonFlag) {
        this.navCartoonFlag = navCartoonFlag;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getLastSelectedNaviBar() {
        return lastSelectedNaviBar;
    }

    public void setLastSelectedNaviBar(String lastSelectedNaviBar) {
        this.lastSelectedNaviBar = lastSelectedNaviBar;
    }

    public boolean isLockScreen() {
        return lockScreen;
    }

    public void setLockScreen(boolean lockScreen) {
        this.lockScreen = lockScreen;
    }

    /**
     * @return the hasMobilePower
     */
    public boolean isHasMobilePower() {
        return hasMobilePower;
    }

    /**
     * @param hasMobilePower
     *            the hasMobilePower to set
     */
    public void setHasMobilePower(boolean hasMobilePower) {
        this.hasMobilePower = hasMobilePower;
    }

    public Boolean getFrontEndLogSwitch() {
        return frontEndLogSwitch;
    }

    public void setFrontEndLogSwitch(Boolean frontEndLogSwitch) {
        this.frontEndLogSwitch = frontEndLogSwitch;
    }
    
    public int getSocketServerPort() {
        return socketServerPort;
    }

    public void setSocketServerPort(int socketServerPort) {
        this.socketServerPort = socketServerPort;
    }

}
