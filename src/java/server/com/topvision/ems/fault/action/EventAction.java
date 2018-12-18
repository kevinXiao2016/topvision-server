package com.topvision.ems.fault.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.EventService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller("eventAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventAction extends BaseAction {
    private static final long serialVersionUID = 2483101053409870739L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            SystemConstants.getInstance().getStringParam("event.time.format", "yyyy-MM-dd (EEE) HH:mm:ss"));
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private EventService eventService = null;
    @Autowired
    private AlertService alertService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    private List<Long> eventIdIds;
    private long typeId = -1;
    private String host;
    private String message;
    private String startTime;
    private String endTime;
    private String desc;
    private Integer eventId;
    private Long eventTypeId;
    private int start;
    private int limit;
    private String eventType;
    private JSONArray ipNameJson;
    private String eventIds;

    public String getAllEventType() throws Exception {
        List<EventType> list = eventService.loadEventTypeToJson();
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>(list == null ? 0 : list.size());
        // 遍历，将事件主分类加入jsonAlertType中
        for (EventType eventType : list) {
            if (Integer.parseInt(eventType.getParentId()) != 0) {
                continue;
            }
            TreeNode treeNode = new TreeNode();
            treeNode.setText(getString(eventType.getDisplayName(), "fault"));
            treeNode.setId(Long.parseLong(eventType.getTypeId().toString()));
            treeNode.setParentId(Long.parseLong(eventType.getParentId().toString()));
            treeNode.setIconCls("folder");
            treeNode.setNeedATag(true);
            treeNode.setChildren(new ArrayList<TreeNode>());
            map.put(treeNode.getId().toString(), treeNode);
            treeNodes.add(treeNode);
        }
        // 再次遍历，将各叶子告警插入相应分类之下
        for (EventType eventType : list) {
            if (Integer.parseInt(eventType.getParentId()) == 0) {
                continue;
            }
            TreeNode treeNode = new TreeNode();
            treeNode.setText(getString(eventType.getDisplayName(), "fault"));
            treeNode.setId(Long.parseLong(eventType.getTypeId().toString()));
            treeNode.setParentId(Long.parseLong(eventType.getParentId().toString()));
            treeNode.setIconCls("icoF1");
            treeNode.setNeedATag(true);
            treeNode.setChildren(new ArrayList<TreeNode>());
            // map的存在意义是始终保留指向相应节点的引用，这样可以方便的修改其children，快速构建正确的树结构
            map.put(String.valueOf(eventType.getTypeId()), treeNode);
            TreeNode parentNode = map.get(String.valueOf(eventType.getParentId()));
            if (parentNode == null) {
                treeNodes.add(treeNode);
            } else {
                parentNode.getChildren().add(treeNode);
                parentNode.setIconCls("folder");
            }
        }
        net.sf.json.JSONArray jsonEventTypeList = new net.sf.json.JSONArray();
        TreeNode allType = new TreeNode();
        allType.setText(getString("ALERT.allType", "fault"));
        allType.setId(0L);
        allType.setParentId(-1L);
        allType.setIconCls("folder");
        allType.setNeedATag(true);
        allType.setChildren(treeNodes);
        jsonEventTypeList.add(allType);

        // jsonEventTypeList.addAll(treeNodes);
        writeDataToAjax(jsonEventTypeList);
        return NONE;
    }

    /**
     * 获取所有的事件类型,并组装成树结构,在事件查看器页面选择事件类型时使用 注意此方法中使用 org.json包里的Json对象更方便处理问题
     * 
     * @author flackyang
     * @since 2013-11-12
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String getEventTypeTree() throws Exception {
        List<EventType> list = eventService.loadEventTypeToJson();
        JSONArray jsonEventTypeList = new JSONArray();
        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0 : list.size());
        JSONObject json = null;
        EventType type = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (Integer.parseInt(type.getParentId()) != 0) {
                continue;
            }
            json = new JSONObject();
            json.put("text", getString(type.getDisplayName(), "fault"));
            json.put("value", type.getTypeId());
            json.put("child", new JSONArray());
            map.put(String.valueOf(type.getTypeId()), json);
            jsonEventTypeList.add(json);
        }
        JSONObject parent = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (Integer.parseInt(type.getParentId()) == 0) {
                continue;
            }
            json = new JSONObject();
            json.put("text", getString(type.getDisplayName(), "fault"));
            json.put("value", type.getTypeId());
            json.put("child", new JSONArray());
            map.put(String.valueOf(type.getTypeId()), json);
            parent = map.get(String.valueOf(type.getParentId()));
            if (parent == null) {
                jsonEventTypeList.add(json);
            } else {
                parent.getJSONArray("child").add(json);
            }
        }
        jsonEventTypeList.write(response.getWriter());
        return NONE;
    }

    /**
     * 跳转到事件查看器
     * 
     * @return
     */
    public String showEventJsp() {
        // 获取所有的设备并组装成 IP 别名 的形式,在关联设备查询时使用
        List<Entity> entityList = entityService.getCentralEntity();
        List<String> IpNameList = new ArrayList<String>();
        for (int i = 0; i < entityList.size(); i++) {
            IpNameList.add(entityList.get(i).getIp() + "[" + entityList.get(i).getName()+ "]");
        }
        ipNameJson = JSONArray.fromObject(IpNameList);
        return SUCCESS;
    }

    /**
     * 加载事件列表
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String queryEventList() throws JSONException, IOException {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        net.sf.json.JSONArray array = new net.sf.json.JSONArray();
        Map<String, String> map = new HashMap<String, String>();
        // 告警来源
        if (host != null && !host.equals("all") && !host.equals("")) {
            String[] hostQuery = host.split(" ");
            if (hostQuery.length == 1) {
                map.put("host", hostQuery[0]);
            } else {
                map.put("ip", hostQuery[0]);
                map.put("host", hostQuery[1]);
            }
        }
        // 事件类型
        if (eventType != null && !eventType.equals("0") && !eventType.equals("")) {
            map.put("typeId", eventType);
        }
        // 发生原因
        if (message != null && !message.equals("")) {
            map.put("message", message);
        }
        // 开始时间
        if (startTime != null && !startTime.equals("")) {
            // map.put("startTime", startTime);
            // 结束时间
            if (endTime != null && !endTime.equals("")) {
                // map.put("endTime", endTime);
                String sql = " A.createTime between '" + startTime + "' and '" + endTime + "' ";
                map.put("sql", sql);
            }
        }
        map.put("start", start + "");
        map.put("limit", limit + "");
        List<Event> pageData = eventService.queryEventList(map);
        int rowCount = eventService.queryEventListSize(map);
        json.put("rowCount", rowCount);
        int size = pageData.size();
        if (size > 0) {
            net.sf.json.JSONObject temp = null;
            Event event = null;
            List<Event> list = pageData;
            for (int i = 0; i < size; i++) {
                event = list.get(i);
                temp = new net.sf.json.JSONObject();
                temp.put("eventId", event.getEventId());
                temp.put("entityType", event.getEntityType());
                temp.put("source", event.getSource());
                temp.put("host", event.getHost());
                temp.put("entityId", event.getEntityId());
                temp.put("message", event.getMessage());
                temp.put("name", getString(event.getName(), "fault"));
                temp.put("createTime", DateUtils.format(event.getCreateTime()));

                if (entityTypeService.isCcmts(event.getEntityType())) {
                    String cmcMac = "00:00:00:00:00:00";
                    String cmcMacString = alertService.getMacById(event.getEntityId());
                    if (cmcMacString != null) {
                        cmcMac = cmcMacString;
                    }
                    temp.put("cmcMac", cmcMac);
                }

                array.add(temp);
            }
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String clearEvent() {
        Map map = new HashMap();
        map.put("eventIdIds", eventIdIds);
        eventService.clearEvent(map);
        return NONE;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String getEventList() throws Exception {
        JSONObject json = new JSONObject();
        // super.setSort("name");
        PageData<Event> pageData = eventService.getEventList(typeId, getExtPage());
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Event> list = pageData.getData();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                Event event = list.get(i);
                temp.put("eventId", event.getEventId());
                temp.put("note", event.getMessage());
                temp.put("host", event.getHost());
                temp.put("type", event.getTypeId());
                temp.put("typeName", getString(event.getTypeName(), "fault"));
                temp.put("date", DATE_FORMAT.format(event.getCreateTime()));
                temp.put("time", TIME_FORMAT.format(event.getCreateTime()));
                array.add(temp);
            }
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String getEventType() throws JSONException, IOException {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        List<AlertType> list = eventService.getEventType();
        List<String> list2 = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            list2.add(getString(list.get(i).getDisplayName(), "fault"));
            list.get(i).setDisplayName(getString(list.get(i).getDisplayName(), "fault"));
        }
        json.put("type", list);
        json.put("content", list2);
        writeDataToAjax(json);
        return NONE;
    }

    public String getRecentEventForPortal() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div align=center width=100% height=100%><a href=\"#\">")
                .append(getString("COMMON.entity", "resources")).append("192.168.1.1")
                .append(getString("EventAction.OffLine", "resources")).append("</a>");
        sb.append("<br>");
        sb.append("<a href=\"#\">").append(getString("COMMON.entity", "resources")).append("192.168.1.2")
                .append(getString("EventAction.OffLine", "resources")).append("</a>");
        sb.append("<br>");
        sb.append("<a href=\"#\">").append(getString("COMMON.entity", "resources")).append("192.168.1.3")
                .append(getString("EventAction.OffLine", "resources")).append("</a>");
        sb.append("<br>");
        sb.append("<a href=\"#\">").append(getString("EventAction.DiscoveryVirus", "resources")).append("</a>");
        sb.append("<br>");
        sb.append("<a href=\"#\">").append(getString("COMMON.entity", "resources")).append("192.168.1.1")
                .append(getString("EventAction.OffLine", "resources")).append("</a>");
        sb.append("<br>");
        sb.append("<img src=\"../images/network/desktop_48.gif\"></div>");
        writeDataToAjax(sb.toString());
        return NONE;
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

    public String deleteEvent() {
        eventService.deleteEvent(eventId);
        return NONE;
    }

    /**
     * 批量删除事件
     * 
     * @return
     */
    public String batchDeleteEvent() {
        eventService.batchDeleteEvent(eventIds);
        return NONE;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String showEventByEntityId() throws Exception {
        return SUCCESS;
    }

    public String showEventViewer() {
        return SUCCESS;
    }

    public List<Long> getEventIdIds() {
        return eventIdIds;
    }

    public void setEventIdIds(List<Long> eventIdIds) {
        this.eventIdIds = eventIdIds;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        super.setStart(start);
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        super.setLimit(limit);
        this.limit = limit;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public AlertService getAlertService() {
        return alertService;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public JSONArray getIpNameJson() {
        return ipNameJson;
    }

    public void setIpNameJson(JSONArray ipNameJson) {
        this.ipNameJson = ipNameJson;
    }

    public String getEventIds() {
        return eventIds;
    }

    public void setEventIds(String eventIds) {
        this.eventIds = eventIds;
    }

}
