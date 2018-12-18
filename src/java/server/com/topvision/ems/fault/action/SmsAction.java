package com.topvision.ems.fault.action;

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
import com.topvision.platform.domain.Action;
import com.topvision.platform.service.ActionService;

import net.sf.json.JSONObject;

@Controller("smsAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SmsAction extends BaseAction {
    private static final long serialVersionUID = -1155836168471483010L;
    public static final int ACTION_TYPE = 2;
    private static final Logger logger = LoggerFactory.getLogger(SmsAction.class);
    private Long actionId;
    private String name = null;
    private String mobile = null;
    @Autowired
    private FaultService faultService;
    @Autowired
    @Qualifier(value = "smsActionService")
    private ActionService actionService;

    private Action createAction() {
        Action action = new Action();
        action.setActionTypeId(ACTION_TYPE);
        action.setEnabled(true);
        action.setName(name);
        action.setParams(mobile.getBytes());
        return action;
    }

    public String newSmsAction() {
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

    public String sendTestSms() throws Exception {
        JSONObject json = new JSONObject();
        try {
            actionService.sendAction(createAction(), null, "Test sms by SmsAction create");
            json.put("success", true);
            json.put("feedback", "Send a test sms to the " + mobile);
        } catch (ServiceException ex) {
            logger.debug("send test sms:", ex);
            json.put("success", false);
            json.put("feedback", ex.getMessage());
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String sendTestSmsBak() throws Exception {
        JSONObject json = new JSONObject();
        String judge = null;
        try {
            judge = actionService.sendActionBak(createAction(), null, "Test sms by SmsAction create");
            if (judge == "fail") {
            json.put("success", false);
            }else{
            json.put("success", true);
            }
        } catch (ServiceException ex) {
            logger.debug("send test sms:", ex);
            json.put("success", false);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String showSmsAction() {
        Action action = faultService.getActionById(actionId);
        name = action.getName();
        mobile = (String) action.getParamsObject();
        actionId = action.getActionId();
        return SUCCESS;
    }

    public String updateSmsAction() {
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

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getActionId() {
        return actionId;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }
}
