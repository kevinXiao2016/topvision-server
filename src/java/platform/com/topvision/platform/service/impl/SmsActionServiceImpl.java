/***********************************************************************
 * $Id: SmsActionServiceImpl.java,v 1.1 Sep 25, 2008 10:45:55 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.Properties;

import montnets.mondem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.User;
import com.topvision.platform.service.ActionService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserService;

/**
 * @author kelers
 * @Create Date Sep 25, 2008 10:45:55 PM
 */
@Service("smsActionService")
public class SmsActionServiceImpl extends BaseService implements ActionService {
    public static final String MODULE_SMS = "smsServer";
    public static final int SMS_MAX_LENGTH = 69;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private UserService userService;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.ActionService#isServerSetting()
     */
    @Override
    public Boolean isServerSetting() {
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.ActionService#reset()
     */
    @Override
    public void reset() {
    }    

     /**
     * (non-Javadoc)
     *
     * @see com.topvision.platform.service.ActionService#
     * sendAction(com.topvision.platform.domain.server.fault.domain.Action, java.lang.String)
     */
     @Override
     public void sendAction(Action action, Object object, String content) throws ServiceException
     {
     // Properties settings = systemPreferencesService.getModulePreferences(MODULE_SMS);
     // int serial = Integer.parseInt(settings.getProperty("sms.port", "1")) - 1;
     int serial=3;
     if (getLogger().isDebugEnabled()) {
     getLogger().debug("SMS.sendAction.action:{}", action);
     // getLogger().debug("SMS.sendAction.mobile:{}", new String(action.getParams()));
     getLogger().debug("SMS.sendAction.content:{}", content);
     getLogger().debug("SMS.sendAction.serial:{}", serial);
     }
     int rc;
     mondem sms = new mondem();
     // 开启线程模式
     if (sms.SetThreadMode(1) == 0) {
     getLogger().debug("Set thread mode successful");
     } else {
     getLogger().debug("Set thread mode fail");
     return;
     }
     for (int i = 0; i <= 8; i++) {
     sms.SetModemType(i, 0);// 设置成单口模式
     }
     // sms.SetModemType(0, 0);// 设置成单口模式
     // sms.SetModemType(1, 0);// 设置成单口模式
     // sms.SetModemType(2, 0);// 设置成单口模式
     // sms.SetModemType(3, 0);// 设置成单口模式
     // sms.SetModemType(4, 0);// 设置成单口模式
     // sms.SetModemType(5, 0);// 设置成单口模式
     // sms.SetModemType(6, 0);// 设置成单口模式
     // sms.SetModemType(7, 0);// 设置成单口模式
     // sms.SetModemType(8, 0);// 设置成单口模式
     if (sms.InitModem(serial) == 0) {
     getLogger().debug("mondem Successful initialization");
     User user = userService.getUserEx(action.getUserId());
     // String no = new String(action.getParams());
     if (user.getMobile() == null || user.getMobile().length() == 0) {
     return;
     }
     String no = new String(user.getMobile());
     int length = content.length();
     for (int i = 0; i < content.length() / SMS_MAX_LENGTH + 1; i++) {
     int max = (i + 1) * SMS_MAX_LENGTH > length ? length : (i + 1) * SMS_MAX_LENGTH;
     String msg = content.substring(i * SMS_MAX_LENGTH, max);
     rc = sms.SendMsg(serial, no, msg);
     if (rc >= 0) {
     getLogger().debug("Submit[{}]successful, rc={}", msg, rc);
     } else {
     getLogger().error("Submit[{}]fail, rc={}", msg, rc);
     }
     int index = 10;
     while (index > 0) {
     index--;
     String[] readMsg = sms.ReadMsgEx(serial);
     if (readMsg[0].equals("-1")) {
     try {
     // 延时等待
     Thread.sleep(5000);
     } catch (InterruptedException e) {
     }
     } else {
     if (readMsg[2].equals(msg)) {
     getLogger().debug("Sent successfully:{}>>{}>>{}", readMsg[0], readMsg[1], readMsg[2]);
     break;
     } else {
     getLogger().debug("Received messages:{}>>{}>>{}", readMsg[0], readMsg[1], readMsg[2]);
     }
     }
     }
     }
     } else {
     getLogger().warn("mondem Failed to initialize");
     }
     // 不用关闭，不然发送不成功
     // sms.CloseModem(0);
     }

    @Override
    public String sendActionBak(Action action, Object object, String content) throws ServiceException {
        Properties settings = systemPreferencesService.getModulePreferences(MODULE_SMS);
        int serial = Integer.parseInt(settings.getProperty("sms.port", "1")) - 1;
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("SMS.sendAction.action:{}", action);
            getLogger().debug("SMS.sendAction.mobile:{}", new String(action.getParams()));
            getLogger().debug("SMS.sendAction.content:{}", content);
            getLogger().debug("SMS.sendAction.serial:{}", serial);
        }
        int rc;
        mondem sms = new mondem();
        // 开启线程模式
        if (sms.SetThreadMode(1) == 0) {
            getLogger().debug("Set thread mode successful");
        } else {
            getLogger().debug("Set thread mode fail");
            return "fail";
        }
        for (int i = 0; i <= 8; i++) {
            sms.SetModemType(i, 0);// 设置成单口模式
        }
        if (sms.InitModem(serial) == 0) {
            getLogger().debug("mondem Successful initialization");
            String no = new String(action.getParams());
            int length = content.length();
            for (int i = 0; i < content.length() / SMS_MAX_LENGTH + 1; i++) {
                int max = (i + 1) * SMS_MAX_LENGTH > length ? length : (i + 1) * SMS_MAX_LENGTH;
                String msg = content.substring(i * SMS_MAX_LENGTH, max);
                rc = sms.SendMsg(serial, no, msg);
                if (rc >= 0) {
                    getLogger().debug("Submit[{}]successful, rc={}", msg, rc);
                } else {
                    getLogger().error("Submit[{}]fail, rc={}", msg, rc);
                }
                int index = 10;
                while (index > 0) {
                    index--;
                    String[] readMsg = sms.ReadMsgEx(serial);
                    if (readMsg[0].equals("-1")) {
                        try {
                            // 延时等待
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        if (readMsg[2].equals(msg)) {
                            getLogger().debug("Sent successfully:{}>>{}>>{}", readMsg[0], readMsg[1], readMsg[2]);
                            break;
                        } else {
                            getLogger().debug("Received messages:{}>>{}>>{}", readMsg[0], readMsg[1], readMsg[2]);
                        }
                    }
                }
            }
            return "success";
        } else {
            getLogger().warn("mondem Failed to initialize");
            return "fail";
        }
    }

    /**
     * @return the systemPreferencesService
     */
    public SystemPreferencesService getSystemPreferencesService() {
        return systemPreferencesService;
    }

    /**
     * @param systemPreferencesService
     *            the systemPreferencesService to set
     */
    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    @Override
    public String checkConnection(String smsServerIp, int smsServicePort) {
        return null;
    }
}
