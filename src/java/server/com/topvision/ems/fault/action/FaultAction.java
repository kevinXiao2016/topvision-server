package com.topvision.ems.fault.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.domain.ActionType;
import com.topvision.ems.fault.service.FaultService;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("faultAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FaultAction extends BaseAction {
    private static final long serialVersionUID = -1234161663164122932L;
    private static final Logger logger = LoggerFactory.getLogger(FaultAction.class);
    @Autowired
    private FaultService faultService;
    private List<Long> actionIds;

    public String deleteAction() {
        faultService.deleteActions(actionIds);
        return NONE;
    }

    public String disableAction() {
        faultService.updateActionStatus(actionIds, false);
        return NONE;
    }

    public String enableAction() {
        faultService.updateActionStatus(actionIds, true);
        return NONE;
    }

    private String getString(String key) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
            return resourceManager.getNotNullString(key);
        } catch (ResourceNotFoundException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("get String.", e.getMessage());
            }
            return key;
        }
    }

    public String loadActionList() throws Exception {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<ActionType> list = faultService.getActionTypes();
        Map<Integer, ActionType> types = new HashMap<Integer, ActionType>(list.size());
        for (ActionType type : list) {
            types.put(type.getActionTypeId(), type);
        }
        List<Action> actions = faultService.getActions();
        for (int i = 0; actions != null && i < actions.size(); i++) {
            Action action = actions.get(i);
            if (action.getActionTypeId() == 3) {
                continue;
            }
            JSONObject json = new JSONObject();
            ActionType type = types.get(action.getActionTypeId());
            json.put("actionId", action.getActionId());
            json.put("actionTypeId", action.getActionTypeId());
            json.put("name", action.getName());
            json.put("type", type.getName());
            json.put("typeName", getString(type.getName()));
            json.put("typeIcon", type.getName().toLowerCase() + ".gif");
            json.put("status", action.isEnabled());
            json.put("params", new String(action.getParams()));
            array.add(json);
        }
        data.put("data", array);
        writeDataToAjax(data);
        return NONE;
    }

    public String loadActionType() throws Exception {
        JSONArray array = new JSONArray();
        List<ActionType> list = faultService.getActionTypes();
        for (int i = 0; list != null && i < list.size(); i++) {
            if (list.get(i).getActionTypeId() == 3) {
                continue;
            }
            ActionType type = list.get(i);
            JSONObject json = new JSONObject();
            json.put("text", getString(type.getName()));
            json.put("expanded", true);
            json.put("id", String.valueOf(type.getActionTypeId()));
            json.put("name", type.getName());
            json.put("icon", "../images/fault/" + type.getName().toLowerCase() + ".gif");
            json.put("children", new JSONArray());
            array.add(json);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 获取所有的告警级别.
     * 
     * @return
     * @throws Exception
     */
    public String loadAllAlertLevel() throws Exception {
        JSONArray array = faultService.loadJSONAlertLevel();
        writeDataToAjax(array);
        return NONE;
    }

    public void setActionIds(List<Long> actionIds) {
        this.actionIds = actionIds;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public List<Long> getActionIds() {
        return actionIds;
    }

    public FaultService getFaultService() {
        return faultService;
    }
}
