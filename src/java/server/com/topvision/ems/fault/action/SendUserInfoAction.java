package com.topvision.ems.fault.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.Action;
import com.topvision.platform.service.ActionService;

@Controller("sendUserInfoAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendUserInfoAction extends BaseAction {
    private static final long serialVersionUID = 6872577652424998052L;
    public static final int ACTION_TYPE = 1;
    private String userNameNeedAlert[];
    private String emailNeedAlert[];
    private String SMSNeedAlert[];
    private String chooseAlert[];
    private String MsgNeedAlert = null;

    @Autowired
    @Qualifier(value = "mailActionService")
    private ActionService actionServiceEmail;
    
    @Autowired
    @Qualifier(value = "smsActionService")
    private ActionService actionServiceSMS;

    private Action createActionEmail(String userNameNeedAlert, String emailNeedAlert) {
        Action action = new Action();
        action.setActionTypeId(ACTION_TYPE);
        action.setEnabled(true);
        action.setName(userNameNeedAlert);
        action.setParams(emailNeedAlert.getBytes());
        return action;
    }
    
    private Action createActionSMS(String userNameNeedAlert, String smsNeedAlert) {
        Action action = new Action();
        action.setActionTypeId(ACTION_TYPE);
        action.setEnabled(true);
        action.setName(userNameNeedAlert);
        action.setParams(smsNeedAlert.getBytes());
        return action;
    }

    public void sendAlertInfo() throws Exception {
        try {
            for (int i = 0; i < chooseAlert.length; i++) {
                if ( chooseAlert[i].equals("Email") ) {
                    actionServiceEmail.sendAction(createActionEmail(userNameNeedAlert[i], emailNeedAlert[i]), null,
                            MsgNeedAlert);
                }else if( chooseAlert[i].equals("Mobile") ){
                    actionServiceSMS.sendAction(createActionSMS(userNameNeedAlert[i], SMSNeedAlert[i]), null,
                            MsgNeedAlert);
                }else if(chooseAlert[i].equals("Email&Mobile")){
                    actionServiceEmail.sendAction(createActionEmail(userNameNeedAlert[i], emailNeedAlert[i]), null,
                            MsgNeedAlert);
                    actionServiceSMS.sendAction(createActionSMS(userNameNeedAlert[i], SMSNeedAlert[i]), null,
                            MsgNeedAlert);
                }
            }
        } catch (ServiceException ex) {
            logger.info("send test email:", ex);
        }
    }

    public String[] getUserNameNeedAlert() {
        return userNameNeedAlert;
    }

    public void setUserNameNeedAlert(String[] userNameNeedAlert) {
        this.userNameNeedAlert = userNameNeedAlert;
    }

    public String[] getEmailNeedAlert() {
        return emailNeedAlert;
    }

    public void setEmailNeedAlert(String[] emailNeedAlert) {
        this.emailNeedAlert = emailNeedAlert;
    }

    public String[] getSMSNeedAlert() {
        return SMSNeedAlert;
    }

    public void setSMSNeedAlert(String[] sMSNeedAlert) {
        SMSNeedAlert = sMSNeedAlert;
    }

    public String[] getChooseAlert() {
        return chooseAlert;
    }

    public void setChooseAlert(String[] chooseAlert) {
        this.chooseAlert = chooseAlert;
    }

    public String getMsgNeedAlert() {
        return MsgNeedAlert;
    }

    public void setMsgNeedAlert(String msgNeedAlert) {
        MsgNeedAlert = msgNeedAlert;
    }

}
