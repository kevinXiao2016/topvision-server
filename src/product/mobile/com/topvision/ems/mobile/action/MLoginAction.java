package com.topvision.ems.mobile.action;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.spectrum.utils.VersionUtil;
import com.topvision.exception.service.AuthenticationException;
import com.topvision.exception.service.LimitIpLogonException;
import com.topvision.exception.service.UserStateException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.SystemVersion;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;
import com.topvision.platform.user.context.UserContextManager;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONObject;

@Controller("mLoginAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MLoginAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String username;
    private String password;
    @Autowired
    private UserService userService;
    @Autowired
    private LicenseIf licenseIf;
    @Autowired
    private UserContextManager ucm;
    
    @Value("${terminal.wanId}")
    protected Integer initWanId;

    /**
     * 测试服务器请求连通性
     * 
     * @return
     * @throws IOException
     */
    public String testConnect() throws IOException {
        JSONObject json = new JSONObject();
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        SystemVersion version = new SystemVersion();
        if (uc != null) {
            Boolean operationDevice = uc.hasPower("operationDevice");
            json.put("operationDevice", operationDevice);
            json.put("UserContext", true);
            json.put("version", version.getBuildVersion());
            json.put("versionLong", VersionUtil.getVersionLong(version.getBuildVersion()));
        } else {
            json.put("operationDevice", false);
            json.put("UserContext", false);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 登陆
     * 
     * @return
     * @throws IOException
     */
    public String login() throws IOException {
        String addr = request.getRemoteAddr();
        JSONObject json = new JSONObject();
        json.put("errorCode", -1);
        json.put("authenticated", false);
        try {
            boolean hasMobilePower = licenseIf.isSupportModule("mobile");
            if (!hasMobilePower) {
                json.put("authenticated", false);
                json.put("errorCode", 21);
            } else {
                SystemVersion version = new SystemVersion();
                userService.txLogin(username, password, addr);
                String powerLevelUnit = (String)UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
                json.put("authenticated", true);
                UserContext uc = ucm.getUserContext(username, true);
                userService.loadUserUI(uc);
                Boolean operationDevice = uc.hasPower("operationDevice");
                Boolean refreshDevice = uc.hasPower("refreshDevice");
                Boolean topoDevice = uc.hasPower("topoEdit");
                Boolean seeOltList = uc.hasPower("oltList");
                Boolean seeOnuList = uc.hasPower("onuList");
                Boolean seeCMTSList = uc.hasPower("ccmtsList");
                Boolean seeCMList = uc.hasPower("cmList");
                Boolean seeFailOnuList = uc.hasPower("onuAuthFailList");
                Boolean changeName=uc.hasPower("changeEmsName");
                json.put("operationDevice", operationDevice);
                json.put("refreshDevice", refreshDevice);
                json.put("topoDevice", topoDevice);
                json.put("seeOltList", seeOltList);
                json.put("seeOnuList", seeOnuList);
                json.put("seeCMTSList", seeCMTSList);
                json.put("seeCMList", seeCMList);
                json.put("seeFailOnuList", seeFailOnuList);
                json.put("changeName", changeName);
                json.put("version", version.getBuildVersion());
                json.put("versionLong", VersionUtil.getVersionLong(version.getBuildVersion()));
                json.put("systemPowerUnit", powerLevelUnit);
                
                json.put("initWanId", initWanId);
                getSession().put(UserContext.KEY, uc);
            }
        } catch (AuthenticationException aex) {
            logger.debug("Authentication Failure:", aex);
            // 密码或者用户名错误
            if (aex.getMessage().equals("UserNotExist")) {
                json.put("errorCode", 11);
            } else if (aex.getMessage().equals("PasswordInvalid")) {
                json.put("errorCode", 12);
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
    
    /**
     * 登陆时判断权限
     * 
     * @return
     * @throws IOException
     */
    public String loginWithPower() throws IOException {
        String addr = request.getRemoteAddr();
        JSONObject json = new JSONObject();
        json.put("errorCode", -1);
        json.put("authenticated", false);
        try {
            boolean hasMobilePower = licenseIf.isSupportModule("mobile");
            if (!hasMobilePower) {
                json.put("authenticated", false);
                json.put("errorCode", 21);
            } else {
                SystemVersion version = new SystemVersion();
                userService.txLogin(username, password, addr);
                json.put("authenticated", true);
                UserContext uc = ucm.getUserContext(username, true);
                userService.loadUserUI(uc);
                Boolean operationDevice = uc.hasPower("operationDevice");
                Boolean refreshDevice = uc.hasPower("refreshDevice");
                Boolean seeOltList = uc.hasPower("oltlist");
                Boolean seeCMTSList = uc.hasPower("ccmtslist");
                Boolean seeCMList = uc.hasPower("cmlist");
                json.put("operationDevice", operationDevice);
                json.put("refreshDevice", refreshDevice);
                json.put("seeOltList", seeOltList);
                json.put("seeCMTSList", seeCMTSList);
                json.put("seeCMList", seeCMList);
                json.put("version", version.getBuildVersion());
                json.put("versionLong", VersionUtil.getVersionLong(version.getBuildVersion()));
                getSession().put(UserContext.KEY, uc);
            }
        } catch (AuthenticationException aex) {
            logger.debug("Authentication Failure:", aex);
            // 密码或者用户名错误
            if (aex.getMessage().equals("UserNotExist")) {
                json.put("errorCode", 11);
            } else if (aex.getMessage().equals("PasswordInvalid")) {
                json.put("errorCode", 12);
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

    public String logout() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc != null) {
            getSession().clear();
            request.getSession().invalidate();
            ucm.exitUser(uc, request.getRemoteHost());
            userService.syslog(uc.getUser().getUserName(), uc.getUser().getLastLoginIp(), "Logoff");
        }
        // Modify by Victor@20140522把IP放到session中去
        request.getSession().setAttribute("ip", ServletActionContext.getRequest().getRemoteAddr());
        return NONE;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
