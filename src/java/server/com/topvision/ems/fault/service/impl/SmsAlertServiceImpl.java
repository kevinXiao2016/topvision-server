/***********************************************************************
 * $Id: SmsAlertServiceImpl.java,v1.0 2017年4月6日 下午7:30:00 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.service.impl;

import java.util.Properties;

import montnets.mondem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.stereotype.Service;

import com.topvision.common.sms.api.SmsService;
import com.topvision.ems.fault.service.SmsAlertService;
import com.topvision.ems.nbi.rmi.NbiRmiService;
import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.User;
import com.topvision.platform.service.ActionService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserService;

/**
 * @author admin
 * @created @2017年4月6日-下午7:30:00
 *
 */
@Service("smsAlertService")
public class SmsAlertServiceImpl extends BaseService implements SmsAlertService , ActionService{
    public static final String MODULE_SMS = "smsServer";
    public static final int SMS_MAX_LENGTH = 69;
    private SmsService smsService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private UserService userService;
    @Autowired
    private NbiRmiService nbiRmiService;

    public void sendMsg(String no, String content, String smsServiceIp, int smsServicePort){
        try {
            smsService = nbiRmiService.getNbiService(SmsService.class, smsServiceIp, smsServicePort);
            smsService.sendSms(no, content);
        } catch (RemoteLookupFailureException e) {
            getLogger().debug("SMSPortUnconnect");           
        }
    }
    
    @Override
    public String checkConnection(String smsServerIp, int smsServicePort) {
        String connectState=null;
        try {
            smsService = nbiRmiService.getNbiService(SmsService.class, smsServerIp, smsServicePort);
            connectState=smsService.check(smsServerIp, smsServicePort);
        } catch (RemoteLookupFailureException e) {
            getLogger().debug("SMSPortUnconnect");           
        }
        return connectState;
    }
    
    @Override
    public Boolean isServerSetting() {
        return null;
    }

    @Override
    public void reset() {
    }

    @Override
    public void sendAction(Action action, Object object, String content) throws ServiceException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties settings = systemPreferencesService.getModulePreferences("smsServer");
                String smsServiceIp = settings.getProperty("smsServerIp", "127.0.0.1");
                int smsServicePort = Integer.parseInt(settings.getProperty("smsServerPort", "3021"));
                User user = userService.getUserEx(action.getUserId());
                if (user.getMobile() == null || user.getMobile().length() == 0) {
                    return;
                }
                String no = new String(user.getMobile());
                sendMsg(no, content + "【NM3000】", smsServiceIp, smsServicePort);
            }
        }).start();
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


}
