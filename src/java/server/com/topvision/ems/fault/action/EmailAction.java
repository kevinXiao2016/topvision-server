package com.topvision.ems.fault.action;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.service.FaultService;
import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.ActionService;

import net.sf.json.JSONObject;

@Controller("emailAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailAction extends BaseAction {
    private static final long serialVersionUID = -1155836168471483010L;
    private static final Logger logger = LoggerFactory.getLogger(EmailAction.class);
    public static final int ACTION_TYPE = 1;
    private Long actionId;
    private String name = null;
    private String email = null;
    private String userNameNeedAlert[];
    private String emailNeedAlert[];
    private String SMSNeedAlert[];
    private String chooseAlert[];
    private String MsgNeedAlert=null;


    @Autowired
    private FaultService faultService;
    @Autowired
    @Qualifier(value = "mailActionService")
    private ActionService actionService;

    private Action createAction() {
        Action action = new Action();
        action.setActionTypeId(ACTION_TYPE);
        action.setEnabled(true);
        action.setName(name);
        action.setParams(email.getBytes());
        return action;
    }
    
    /**
     * 判断邮件服务器是否设置
     * 
     * @return
     */
    public boolean isServerSetting() {
        return actionService.isServerSetting();
    }

    public String newEmailAction() {
        if (name == null) {
            return SUCCESS;
        }
        if (faultService.existActionName(ACTION_TYPE, name, actionId)) {
            writeDataToAjax("action exist");
            return NONE;
        }
        faultService.insertAction(createAction());
        return NONE;
    }

    public String sendTestEmail() throws Exception {
        JSONObject json = new JSONObject();
        UserContext context = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources",
                context.getUser().getLanguage());
        try {
            actionService.sendAction(createAction(), null, "This is a test mail from topvision's email action create.");
            json.put("success", true);
            json.put("feedback", resourceManager.getString("EmailAction.sendMessageTo") + email);
        } catch (ServiceException ex) {
            logger.error("send test email:", ex);
            json.put("success", false);
            json.put("feedback", resourceManager.getString("EmailAction.note1"));
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }
    
    public String showEmailAction() {
        Action action = faultService.getActionById(actionId);
        name = action.getName();
        email = new String(action.getParams());
        actionId = action.getActionId();
        return SUCCESS;
    }

    public String updateEmailAction() {
        if (name == null) {
            return SUCCESS;
        }
        if (faultService.existActionName(ACTION_TYPE, name, actionId)) {
            writeDataToAjax("action exist");
            return NONE;
        }
        Action a = createAction();
        a.setActionId(actionId);
        faultService.updateAction(a);
        return NONE;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getActionId() {
        return actionId;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public String getEmail() {
        return email;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public String getName() {
        return name;
    }
    
    public String getMsgNeedAlert() {
        return MsgNeedAlert;
    }

    public void setMsgNeedAlert(String msgNeedAlert) {
        MsgNeedAlert = msgNeedAlert;
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

}