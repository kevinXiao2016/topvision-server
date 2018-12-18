package com.topvision.platform.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.exception.dao.UpdatePasswdException;
import com.topvision.framework.common.MD5;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONObject;

@Controller("passwdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PasswdAction extends BaseAction {
    private static final long serialVersionUID = 4121054673453142504L;
    private static final Logger logger = LoggerFactory.getLogger(PasswdAction.class);

    private String oldPasswd;
    private String newPasswd;
    private String confirmPasswd;
    private boolean checkPasswdComplex;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    /**
     * 打开设置密码页面
     */
    public String popModifyPasswd() {
        checkPasswdComplex = systemPreferencesService.isCheckPasswdComplex();
        return SUCCESS;
    }

    /**
     * 设置密码
     * 
     * @return
     * @throws Exception
     */
    public String setPasswd() throws Exception {
        JSONObject json = new JSONObject();
        try {
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            if (uc.getUser().getPasswd().equals(MD5.makeMd5(oldPasswd))) {
                String temp = MD5.makeMd5(newPasswd);
                User u = new User();
                u.setUserId(uc.getUserId());
                u.setPasswd(temp);
                u.setUserName(uc.getUser().getUserName());
                userService.updatePasswd(u);
                uc.getUser().setPasswd(temp);
                json.put("success", true);
            } else {
                json.put("passwd", true);
                json.put("success", false);
            }
        } catch (UpdatePasswdException ex) {
            json.put("success", false);
            logger.error("Update passwd.", ex);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public String getOldPasswd() {
        return oldPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }

    public void setOldPasswd(String oldPasswd) {
        this.oldPasswd = oldPasswd;
    }

    public boolean isCheckPasswdComplex() {
        return checkPasswdComplex;
    }

    public void setCheckPasswdComplex(boolean checkPasswdComplex) {
        this.checkPasswdComplex = checkPasswdComplex;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

}
