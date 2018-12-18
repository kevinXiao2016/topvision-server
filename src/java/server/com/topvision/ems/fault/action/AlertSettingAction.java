package com.topvision.ems.fault.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.domain.ActionType;
import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.service.AlertConfirmConfigService;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.EventService;
import com.topvision.ems.fault.service.FaultService;
import com.topvision.framework.domain.TreeEntity;
import com.topvision.framework.web.dhtmlx.DefaultDhtmlxHandler;
import com.topvision.framework.web.dhtmlx.DhtmlxTreeOutputter;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author admin
 * @created @2016年10月14日-下午5:46:48
 *
 */
@Controller("alertSettingAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlertSettingAction extends BaseAction {
    private static final long serialVersionUID = -5435213811616174357L;
    @Autowired
    private FaultService faultService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private AlertConfirmConfigService alertConfirmConfigService;
    private Integer alertTypeId = 0;
    private String name;
    private String displayName;
    private byte level;
    private String alertTimes;
    private boolean smartUpdate;
    private boolean terminate;


    private List<Long> eventIds = null;
    private List<Long> actionIds = null;
    private Long oneUserId = null;
    private String isTrue = null;
    private int start;
    private int limit;

    public String alertSetting() {
        return SUCCESS;
    }

    public String loadAction() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        final List<Long> aas = alertService.getActionsOfAlertType(alertTypeId);
        List<ActionType> types = faultService.getActionTypes();
        List<Action> actions = faultService.getActions();
        List<TreeEntity> list = new ArrayList<TreeEntity>(
                (types == null ? 0 : types.size()) + (actions == null ? 0 : actions.size()));
        if (types != null && !types.isEmpty()) {
            list.addAll(types);
        }
        if (actions != null && !actions.isEmpty()) {
            list.addAll(actions);
        }
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(list) {
            @Override
            public Element buildElement(Object obj) {
                TreeEntity item = (TreeEntity) obj;
                Element el = new DefaultElement("item");
                el.addAttribute("id", item.getId());
                el.addAttribute("text", getString(item.getText()));
                el.addAttribute("open", "1");
                el.addAttribute("im0", "function.gif");
                el.addAttribute("im1", "function.gif");
                el.addAttribute("im2", "function.gif");
                if (obj instanceof ActionType) {
                } else if (obj instanceof Action) {
                    if (aas != null && aas.contains(((Action) obj).getActionId())) {
                        el.addAttribute("checked", "1");
                    }
                }
                return el;
            }
        };
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    public String loadAlertType() throws Exception {
        JSONArray array = new JSONArray();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        List<AlertType> list = alertService.getAllAlertTypes();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setDisplayName(resourceManager.getString(list.get(i).getDisplayName()));
        }
        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0 : list.size());
        for (int i = 0; list != null && i < list.size(); i++) {
            AlertType type = list.get(i);
            JSONObject json = new JSONObject();
            json.put("text", type.getDisplayName());
            json.put("expanded", true);
            json.put("id", String.valueOf(type.getTypeId()));
            json.put("children", new JSONArray());
            if (Integer.parseInt(type.getParentId()) == 0) {
                json.put("icon", "../images/fault/alertFolder.gif");
            } else {
                json.put("icon", "../images/fault/alertLeaf.gif");
            }
            map.put(String.valueOf(type.getTypeId()), json);
            JSONObject parent = map.get(String.valueOf(type.getParentId()));
            if (parent == null) {
                array.add(json);
            } else {
                parent.getJSONArray("children").add(json);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    public String loadEventType() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());

        final List<Long> events = eventService.getEventsOfAlertType(alertTypeId);
        List<EventType> list = eventService.getAllEventType();
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(list) {
            @Override
            public Element buildElement(Object obj) {
                EventType item = (EventType) obj;
                Element el = new DefaultElement("item");
                el.addAttribute("id", item.getId());
                el.addAttribute("text", item.getDisplayName());
                el.addAttribute("open", "1");
                el.addAttribute("im0", "function.gif");
                el.addAttribute("im1", "function.gif");
                el.addAttribute("im2", "function.gif");
                if (events != null && events.contains(item.getTypeId())) {
                    el.addAttribute("checked", "1");
                }
                return el;
            }
        };
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    public String loadParamOfAlertType() throws Exception {
        JSONObject json = new JSONObject();
        AlertType alertType = alertService.getAlertTypeById(alertTypeId);
        if (alertType != null) {
            json.put("level", alertType.getLevelId());
            json.put("alertTimes", alertType.getAlertTimes());
            json.put("smartUpdate", alertType.getSmartUpdate());
            json.put("terminate", alertType.getTerminate());
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String saveAlertTypeParam() {
        AlertType type = new AlertType();
        type.setTypeId(alertTypeId);
        type.setAlertTimes(alertTimes);
        type.setLevelId(level);
        type.setSmartUpdate(smartUpdate);
        type.setTerminate(terminate);
        alertService.saveAlertTypeParam(type, eventIds, actionIds);
        eventService.reset();
        return NONE;
    }

    /**
     * 获取所有事件
     * 
     * @return
     * @throws IOException
     */
    public String getAllEventTypeByTypeId() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        List<EventType> eventTypeList = alertConfirmConfigService.getAllEventTypeNoAlert();

        for (EventType event : eventTypeList) {
            event.setDisplayName(getString(event.getDisplayName(), "fault"));
        }
        json.put("data", eventTypeList);
        writeDataToAjax(net.sf.json.JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取指定告警相关事件，包括来源事件以及清除事件
     * 
     * @return
     * @throws IOException
     */
    public String getRelationAlertByAlertTypeId() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        List<EventType> confirmEventTypeList = alertConfirmConfigService
                .getConfirmEventByEventTypeId(new Integer(alertTypeId));
        for (EventType eventType : confirmEventTypeList) {
            eventType.setDisplayName(getString(eventType.getDisplayName(), "fault"));
        }
        json.put("clear", confirmEventTypeList);
        List<EventType> originEventTypeList = alertConfirmConfigService
                .getOriginEventByEventTypeId(new Integer(alertTypeId));
        for (EventType eventType : originEventTypeList) {
            eventType.setDisplayName(getString(eventType.getDisplayName(), "fault"));
        }
        json.put("origin", originEventTypeList);
        writeDataToAjax(net.sf.json.JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 取得用户和告警方式信息
     * 
     * @return void
     * @throws Exception
     */
    public void getAlertAboutUser() throws Exception {
        List<AlertAboutUsers> list = new ArrayList<AlertAboutUsers>();
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("start", start);
        query.put("limit", limit);
        query.put("sort", sort);
        query.put("dir", dir);
        list = alertService.getuserAlertList(query);
        int count = alertService.getUserAlertListCount();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i).getChoose();
            if (temp == null || temp == "" || temp.length() < 1) {
                list.get(i).setChoose("unConfig");
            }           
        }
        if (list != null) {
            map.put("judge", "success");
            map.put("num", count);
            map.put("result", list);
        }
        writeDataToAjax(JSONObject.fromObject(map));
    }

    /**
     * 获取需要发送信息的用户信息
     * 
     * @return void
     * @throws Exception
     */
    public void getSendingInfoOfUsers() throws Exception {
        List<AlertAboutUsers> list = new ArrayList<AlertAboutUsers>();
        Map<String, Object> map = new HashMap<String, Object>();
        list = alertService.getSendingInfoOfUsers(alertTypeId);
        if (list != null) {
            map.put("data", list);
        }
        writeDataToAjax(JSONObject.fromObject(map));
    }

    /**
     * 获取某个用户的告警选择
     * 
     * @return void
     * @throws Exception
     */
    public void getOneUserActionCs() throws Exception {
        List<AlertAboutUsers> list = new ArrayList<AlertAboutUsers>();
        list = alertService.getOneUserActionCs(oneUserId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (list.size() != 0) {
            map.put("judge", "success");
            map.put("data", list);
        } else {
            map.put("judge", "fail");
            map.put("data", "");
        }
        writeDataToAjax(JSONObject.fromObject(map));
    }

    /**
     * 修改某个用户的告警选择
     * 
     * @return void
     * @throws Exception
     */
	public void modifyUserActionCs() throws Exception {
        alertService.updateUserActionCs(oneUserId, isTrue);
    }

    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    protected String getString(String key, String... strings) {
        return ResourceManager.getResourceManager("com.topvision.ems.fault.resources").getNotNullString(key, strings);
    }

    public void setActionIds(List<Long> actionIds) {
        this.actionIds = actionIds;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setAlertTimes(String alertTimes) {
        this.alertTimes = alertTimes;
    }

    public void setAlertTypeId(int alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEventIds(List<Long> eventIds) {
        this.eventIds = eventIds;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSmartUpdate(boolean smartUpdate) {
        this.smartUpdate = smartUpdate;
    }

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    public AlertConfirmConfigService getAlertConfirmConfigService() {
        return alertConfirmConfigService;
    }

    public void setAlertConfirmConfigService(AlertConfirmConfigService alertConfirmConfigService) {
        this.alertConfirmConfigService = alertConfirmConfigService;
    }

    public void setAlertTypeId(Integer alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    public List<Long> getActionIds() {
        return actionIds;
    }

    public AlertService getAlertService() {
        return alertService;
    }

    public String getAlertTimes() {
        return alertTimes;
    }

    public int getAlertTypeId() {
        return alertTypeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Long> getEventIds() {
        return eventIds;
    }

    public EventService getEventService() {
        return eventService;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public byte getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public boolean isSmartUpdate() {
        return smartUpdate;
    }

    public boolean isTerminate() {
        return terminate;
    }

    public Long getOneUserId() {
        return oneUserId;
    }

    public void setOneUserId(Long oneUserId) {
        this.oneUserId = oneUserId;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
