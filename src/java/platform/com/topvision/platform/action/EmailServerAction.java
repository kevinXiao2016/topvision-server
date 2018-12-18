/***********************************************************************
 * $Id: EmailServerAction.java,v 1.1 Sep 17, 2008 12:53:50 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.Action;
import com.topvision.platform.service.MailService;
import com.topvision.platform.service.SystemPreferencesService;

@Controller("emailServerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailServerAction extends BaseAction {
    private static final long serialVersionUID = 7675338787028585221L;

    public static final String MODULE = Action.MAIL_SERVER;
    @Autowired
    private MailService mailActionService = null;
    @Autowired
    private SystemPreferencesService systemPreferencesService = null;
    private String smtpServer = null;
    private int smtpPort = 25;
    private String senderEmail = null;
    private boolean requireAuth = false;
    private String username = null;
    private String password = null;
    private boolean bakEmailServer = false;
    private String smtpServer1 = null;
    private int smtpPort1 = 25;
    private boolean requireAuth1 = false;
    private String username1 = null;
    private String password1 = null;
    private String testEmail = null;

    /**
     * 保存EmailServer的参数信息
     * 
     * @return
     */
    public String saveEmailServer() {
        Properties props = new Properties();
        props.put("smtpServer", smtpServer);
        props.put("smtpPort", String.valueOf(smtpPort));
        props.put("senderEmail", senderEmail);
        props.put("requireAuth", String.valueOf(requireAuth));
        props.put("username", username);
        props.put("password", password);
        props.put("bakEmailServer", String.valueOf(bakEmailServer));
        props.put("smtpServer1", smtpServer1);
        props.put("smtpPort1", String.valueOf(smtpPort1));
        props.put("requireAuth1", String.valueOf(requireAuth1));
        props.put("username1", username1);
        props.put("password1", password1);

        systemPreferencesService.savePreferences(MODULE, props);
        mailActionService.reset();
        return NONE;
    }

    /**
     * 发送一个测试邮件
     * 
     * @return
     * @throws MailException
     *             发送测试邮件失败的时候抛出该异常
     */
    public String sendTestEmail() throws MailException {
        Properties props = new Properties();
        props.put("smtpServer", smtpServer);
        props.put("smtpPort", String.valueOf(smtpPort));
        props.put("senderEmail", senderEmail);
        props.put("requireAuth", String.valueOf(requireAuth));
        props.put("username", username);
        props.put("password", password);
        props.put("bakEmailServer", String.valueOf(bakEmailServer));
        props.put("smtpServer1", smtpServer1);
        props.put("smtpPort1", String.valueOf(smtpPort1));
        props.put("requireAuth1", String.valueOf(requireAuth1));
        props.put("username1", username1);
        props.put("password1", password1);
        props.put("testEmail", testEmail);
        mailActionService.sendTestMail("This is a test mail.", props);
        return NONE;
    }

    /**
     * 显示EmailServer参数页面
     * 
     * @return
     */
    public String showEmailServer() {
        Properties props = null;
        props = systemPreferencesService.getModulePreferences(MODULE);
        smtpServer = props.getProperty("smtpServer", "");
        smtpPort = Integer.parseInt(props.getProperty("smtpPort", "25"));
        senderEmail = props.getProperty("senderEmail", "");
        requireAuth = Boolean.parseBoolean(props.getProperty("requireAuth", "false"));
        username = props.getProperty("username", "");
        password = props.getProperty("password", "");
        bakEmailServer = Boolean.parseBoolean(props.getProperty("bakEmailServer", "false"));
        smtpServer1 = props.getProperty("smtpServer1", "");
        smtpPort1 = Integer.parseInt(props.getProperty("smtpPort1", "25"));
        requireAuth1 = Boolean.parseBoolean(props.getProperty("requireAuth1", "false"));
        username1 = props.getProperty("username1", "");
        password1 = props.getProperty("password1", "");
        return SUCCESS;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword1() {
        return password1;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public int getSmtpPort1() {
        return smtpPort1;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public String getSmtpServer1() {
        return smtpServer1;
    }

    public String getTestEmail() {
        return testEmail;
    }

    public String getUsername() {
        return username;
    }

    public String getUsername1() {
        return username1;
    }

    public boolean isBakEmailServer() {
        return bakEmailServer;
    }

    public boolean isRequireAuth() {
        return requireAuth;
    }

    public boolean isRequireAuth1() {
        return requireAuth1;
    }

    public void setBakEmailServer(boolean bakEmailServer) {
        this.bakEmailServer = bakEmailServer;
    }

    public void setMailActionService(MailService mailActionService) {
        this.mailActionService = mailActionService;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setRequireAuth(boolean requireAuth) {
        this.requireAuth = requireAuth;
    }

    public void setRequireAuth1(boolean requireAuth1) {
        this.requireAuth1 = requireAuth1;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpPort1(int smtpPort1) {
        this.smtpPort1 = smtpPort1;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public void setSmtpServer1(String smtpServer1) {
        this.smtpServer1 = smtpServer1;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public void setTestEmail(String testEmail) {
        this.testEmail = testEmail;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }
}
