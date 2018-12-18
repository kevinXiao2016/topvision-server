package com.topvision.ems.fault.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jofc2.model.Chart;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.FaultService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.util.StringUtil;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

@Controller("alertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlertAction extends BaseAction {
    private static final long serialVersionUID = -4308376648455529399L;
    private static final Logger logger = LoggerFactory.getLogger(AlertAction.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Integer OLT_CPUUSED_ALERT_ID = -757;
    public static Integer OLT_SLOTTEMP_ALERT_ID = -755;
    public static Integer OLT_MEMUSED_ALERT_ID = -753;
    public static Integer OLT_FLASHUSED_ALERT_ID = -751;
    @Autowired
    private AlertService alertService;
    @Autowired
    private FaultService faultService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    private Alert alert;
    private Long alertId = 0L;
    private String folderId;
    private String message;
    private List<Long> alertIds;
    private Long entityId;
    private String startTime;
    private String endTime;

    private Integer confirmStatus; /* 0:close;1:confirm;-1/null:select */
    private Integer clearStatus;
    private String confirmUser;
    private String confirmStartTime;
    private String confirmEndTime;

    private String clearUser;
    private String clearStartTime;
    private String clearEndTime;

    private int typeId;
    // Add by Rod For User Alert Load
    private boolean userAlert = false;
    private String typeIdList;
    private int level;
    private int day = 1;
    private String subTime;
    private String percent;
    private String hostDevice;
    private JSONArray ipNameJson;
    private String queryModel;
    private String queryContent;

    private Boolean simpleModeFlag = true;
    private static final String QUERYMODE_SIMPLE = "Simple";
    private static final String QUERYMODE_ADVANCED = "Advanced";

    public String clearAlert() throws Exception {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        JSONArray array = new JSONArray();
        List<Alert> alerts = alertService.txClearAlert(alertIds, uc.getUser().getUserName(), message);
        if (alerts != null) {
            JSONObject json = null;
            Alert alert = null;
            for (int i = 0; i < alerts.size(); i++) {
                json = new JSONObject();
                alert = alerts.get(i);
                json.put("entityId", alert.getEntityId());
                json.put("level", alert.getLevelId());
                json.put("levelName", faultService.getLevelName(alert.getLevelId()));
                json.put("message", alert.getMessage());
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    public String confirmAlert() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        alertService.txConfirmAlert(alertIds, uc.getUser().getUserName(), message);
        return NONE;
    }

    public String getBusinessAlarm() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "<div style=\"padding:5px\" width=100% height=100%><img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br>");
        sb.append("<img src=\"../images/tip.gif\" border=0 valign=absmiddle><a href=\"#\">192.168.1.1 ")
                .append(getResourceString("AlertAction.AlreadyOffline", "com.topvision.ems.resources.resources"))
                .append("</a><br></div>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /*
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    private String getCurrentAlertSort(String sort) {
        if (sort == null)
            return "lastTime";
        if ("level".equals(sort))
            return "levelName";
        return sort;
    }

    public String getCurrentAlertList() throws Exception {
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        JSONObject json = new JSONObject();

        // 封装查询条件
        Map<String, String> map = packageQueryMap();

        PageData<Alert> pageData = alertService.queryCurrentAlert(getExtPage(), map);
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Alert> list = pageData.getData();
            Alert alert = null;
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                alert = list.get(i);
                try {
                    alert.setMessage(UnitConfigConstant.parseUnitConfigAlertMsg(alert.getTypeId(), alert.getMessage()));
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
                temp.put("alertId", alert.getAlertId());
                temp.put("message", alert.getMessage());
                if (alert.getHost() == null || "".equals(alert.getHost())) {
                    temp.put("host", alert.getSource());
                } else {
                    temp.put("host", alert.getHost());
                }
                temp.put("source", alert.getSource());
                temp.put("typeId", alert.getTypeId());
                temp.put("typeName", resourceManager.getNotNullString(alert.getTypeName()));
                temp.put("level", alert.getLevelId());
                temp.put("levelName", getLevelName(alert.getLevelId()));
                temp.put("status", alert.getStatus());

                temp.put("firstTime", DATE_FORMAT.format(alert.getFirstTime()));
                temp.put("lastTime", DATE_FORMAT.format(alert.getLastTime()));
                temp.put("confirmUser", alert.getConfirmUser());
                temp.put("confirmTime", alert.getStatus() == null || alert.getStatus() == Alert.ALERT ? ""
                        : DATE_FORMAT.format(alert.getConfirmTime()));
                temp.put("confirmMsg", alert.getConfirmMessage());
                temp.put("clearUser", alert.getClearUser());
                temp.put("clearMsg", alert.getClearMessage());
                temp.put("clearTime", DATE_FORMAT.format(alert.getClearTime()));
                temp.put("entityType", alert.getEntityType());
                temp.put("entityId", alert.getEntityId());
                temp.put("entityName", alert.getEntityName());
                temp.put("nativeMessage", alert.getMessage());
                // 为了配合广州的需求，对于CC的告警，提供告警描述里CC MAC地址跳转
                if (entityTypeService.isCcmts(alert.getEntityType())) {
                    Entity entity = entityService.getEntity(alert.getEntityId());
                    if (entity.getParentId() != null) {
                        Entity parent = entityService.getEntity(entity.getParentId());
                        temp.put("parentId", parent.getEntityId());
                        temp.put("parentName", parent.getName());
                    } else {
                        temp.put("parentId", alert.getEntityId());
                        temp.put("parentName", alert.getEntityName());
                    }

                    if (alert.getMessage().indexOf("CMTS[") != -1) {
                        Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
                        Matcher matcher = pattern.matcher(alert.getMessage());
                        String cmcMac = "";
                        if (matcher.find()) {
                            cmcMac = matcher.group(1);
                        }
                        Long cmcId = alertService.getEntityIdByMac(cmcMac);
                        Entity cmcEntity = entityService.getEntity(cmcId);
                        if (cmcId != null) {
                            String[] str = alert.getMessage().split(cmcMac);
                            // 需要格式化MAC地址进行展示
                            String formattedMac = MacUtils.convertMacToDisplayFormat(cmcMac, macRule);
                            StringBuffer sb = new StringBuffer();
                            sb.append(str[0])
                                    .append("<a href='#' onclick='showCmcPortal(\"" + cmcId + "\",\""
                                            + cmcEntity.getName() + "\")'")
                                    .append(">").append(formattedMac).append("</a>").append(str[1]);
                            temp.put("message", sb.toString());
                        }
                    }
                } else if (entityTypeService.isOnu(alert.getEntityType())) {
                    Entity entity = entityService.getEntity(alert.getEntityId());
                    if (entity.getParentId() != null) {
                        Entity parent = entityService.getEntity(entity.getParentId());
                        temp.put("parentId", parent.getEntityId());
                        temp.put("parentName", parent.getName());
                    } else {
                        temp.put("parentId", alert.getEntityId());
                        temp.put("parentName", alert.getEntityName());
                    }
                    // PR-910 onu告警信息能直接跳转onu视图
                    if (alert.getMessage().indexOf("[ONU:") != -1) {
                        Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
                        Matcher matcher = pattern.matcher(alert.getMessage());
                        String alias = "";
                        if (matcher.find()) {
                            alias = matcher.group(1);
                        }
                        String[] str = alert.getMessage().split(alias);
                        StringBuffer sb = new StringBuffer();
                        sb.append(str[0])
                                .append("<a href='#' onclick='showOnuInfo(\"" + alert.getEntityId() + "\",\""
                                        + entity.getName() + "\")'")
                                .append(">").append(alias).append("</a>").append(str[1]);
                        temp.put("message", sb.toString());
                    }
                } else {
                    temp.put("parentId", alert.getEntityId());
                    temp.put("parentName", alert.getEntityName());
                }
                // 为CMTS加上IP链接
                if (entityTypeService.isCmts(alert.getEntityType())) {
                    if (alert.getMessage().indexOf("CMTS[") != -1) {
                        Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
                        Matcher matcher = pattern.matcher(alert.getMessage());
                        String cmtsIp = "";
                        if (matcher.find()) {
                            cmtsIp = matcher.group(1);
                        }
                        // 重新组织message
                        String[] str = alert.getMessage().split(cmtsIp);
                        StringBuffer sb = new StringBuffer();
                        sb.append(str[0]).append("<a href='#' onclick='viewCmtsSnap(\"" + alert.getEntityId() + "\", \""
                                + cmtsIp + "\")'>").append(cmtsIp).append("</a>").append(str[1]);
                        temp.put("message", sb.toString());
                        temp.put("cmtsIp", cmtsIp);
                    }
                }

                if (entityTypeService.isCcmts(alert.getEntityType())) {
                    String cmcMac = "00:00:00:00:00:00";
                    String cmcMacString = alertService.getMacById(alert.getEntityId());
                    if (cmcMacString != null) {
                        cmcMac = cmcMacString;
                    }
                    cmcMac = MacUtils.convertMacToDisplayFormat(cmcMac, macRule);
                    temp.put("cmcMac", cmcMac);
                }

                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);
        return NONE;
    }

    private Map<String, String> packageQueryMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (queryModel != null && queryModel.equals("quick")) {
            // 快捷查询
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryContent.contains("_")) {
                queryContent = queryContent.replace("_", "\\_");
            }
            queryContent = StringEscapeUtils.escapeSql(queryContent);
            map.put("queryContent", queryContent);
        } else {
            // 高级查询
            if (level > 0) {
                map.put("levelId", String.valueOf(level));
            }
            if (typeId != 0) {
                map.put("typeId", String.valueOf(typeId));
            }
            // Add by Rod User Alert
            if (typeIdList != null && typeId == 0) {
                map.put("typeIds", typeIdList);
            }

            if (hostDevice != null && !"".equals(hostDevice.trim())) {
                map.put("host", hostDevice);
            }

            // 告警状态
            String statusQuery = getAlertStatusQuery();
            if (!StringUtil.isEmpty(statusQuery)) {
                map.put("statusQuery", statusQuery.toString());
            }

            // 开始时间
            if (startTime != null && !startTime.equals("")) {
                map.put("startTime", startTime);
            }
            // 结束时间
            if (endTime != null && !endTime.equals("")) {
                map.put("endTime", endTime);
            }

            // 确认信息查询
            if (confirmStartTime != null && !confirmStartTime.equals("")) {
                map.put("confirmStartTime", confirmStartTime);
            }
            if (confirmEndTime != null && !confirmEndTime.equals("")) {
                map.put("confirmEndTime", confirmEndTime);
            }
            if (confirmUser != null && !confirmUser.equals("")) {
                map.put("confirmUser", confirmUser);
            }
            // 清除信息查询
            if (clearStartTime != null && !clearStartTime.equals("")) {
                map.put("clearStartTime", clearStartTime);
            }
            if (clearEndTime != null && !clearEndTime.equals("")) {
                map.put("clearEndTime", clearEndTime);
            }
            if (clearUser != null && !clearUser.equals("")) {
                map.put("clearUser", clearUser);
            }
        }

        map.put("sort", this.getCurrentAlertSort(sort));
        map.put("dir", dir == null ? "DESC" : dir);
        map.put("limit", String.valueOf(limit));
        map.put("start", String.valueOf(start));

        return map;
    }

    private String getAlertStatusQuery() {
        StringBuilder statusQuery = new StringBuilder();
        if (confirmStatus != null && confirmStatus == 0) {
            statusQuery.append("confirmUser is null");
        } else if (confirmStatus != null && confirmStatus == 1) {
            statusQuery.append("confirmUser is not null");
        }
        if (clearStatus != null && clearStatus == 0) {
            if (statusQuery.length() > 0) {
                statusQuery.append(" and ");
            }
            statusQuery.append("clearUser is null");
        } else if (clearStatus != null && clearStatus == 1) {
            if (statusQuery.length() > 0) {
                statusQuery.append(" and ");
            }
            statusQuery.append("clearUser is not null");
        }
        return statusQuery.toString();
    }

    private String getLevelName(byte level) {
        String sLevel = Level.getNameByLevel(level);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
            return resourceManager.getNotNullString(sLevel);
        } catch (ResourceNotFoundException e) {
            return sLevel;
        }
    }
    
    /**
     * 获取用户设置的CM列表查询选择
     * 
     * @return
     */
    private String getCurrentAlertQueryMode() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("currentAlertQueryModel");
        up.setUserId(uc.getUserId());
        Properties currentAlertQueryModel = userPreferencesService.getModulePreferences(up);
        if(currentAlertQueryModel!=null) {
            return (String) currentAlertQueryModel.get("currentAlertQueryModel");
        }
        return QUERYMODE_SIMPLE;
    }

    /**
     * 保存用户设置的当前告警最后查询条件
     * 
     * @return
     */
    public String saveCurrentAlertQueryModel() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties currentAlertQueryModel = new Properties();
        if (simpleModeFlag) {
            currentAlertQueryModel.setProperty("currentAlertQueryModel", QUERYMODE_SIMPLE);
        } else {
            currentAlertQueryModel.setProperty("currentAlertQueryModel", QUERYMODE_ADVANCED);
        }
        userPreferencesService.batchSaveModulePreferences("currentAlertQueryModel", uc.getUserId(),
                currentAlertQueryModel);
        return NONE;
    }

    public String loadRecentAlert() throws Exception {
        JSONObject json = new JSONObject();
        final JSONArray array = new JSONArray();
        alertService.handleRecentAlert(25, new MyResultHandler() {
            @Override
            public void handleResult(ResultContext resultcontext) {
                alert = (Alert) resultcontext.getResultObject();
                JSONObject temp = new JSONObject();
                try {
                    temp.put("alertId", alert.getAlertId());
                    temp.put("entityId", alert.getEntityId());
                    temp.put("monitorId", alert.getMonitorId());
                    temp.put("typeId", alert.getTypeId());
                    temp.put("typeName", alert.getTypeName());
                    temp.put("host", alert.getHost());
                    temp.put("source", alert.getSource());
                    temp.put("level", alert.getLevelId());
                    temp.put("levelName", alert.getLevelName());
                    temp.put("message", alert.getMessage());
                    temp.put("firstTimeStr", DATE_FORMAT.format(alert.getFirstTime()));
                    temp.put("status", alert.getStatus());
                    array.add(temp);
                } catch (JSONException json) {
                    logger.error("load recent alert.", json.getMessage());
                }
            }

            @Override
            public void complete() {
            }

            @Override
            public void prepare() {
            }
        });
        json.put("rowCount", array.size());
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadRecentAlertByEntityId() throws Exception {
        JSONObject json = new JSONObject();
        final JSONArray array = new JSONArray();
        alertService.handleRecentAlert(5, new MyResultHandler() {
            @Override
            public void handleResult(ResultContext resultcontext) {
                alert = (Alert) resultcontext.getResultObject();
                JSONObject temp = new JSONObject();
                try {
                    temp.put("alertId", alert.getAlertId());
                    temp.put("entityId", alert.getEntityId());
                    temp.put("monitorId", alert.getMonitorId());
                    temp.put("typeId", alert.getTypeId());
                    temp.put("typeName", alert.getTypeName());
                    temp.put("host", alert.getHost());
                    temp.put("source", alert.getSource());
                    temp.put("level", alert.getLevelId());
                    temp.put("levelName", alert.getLevelName());
                    temp.put("message", alert.getMessage());
                    temp.put("firstTimeStr", DATE_FORMAT.format(alert.getFirstTime()));
                    temp.put("status", alert.getStatus());
                    array.add(temp);
                } catch (JSONException json) {
                    logger.error("load recent alert.", json.getMessage());
                }
            }

            @Override
            public void complete() {
            }

            @Override
            public void prepare() {
            }
        });
        json.put("rowCount", array.size());
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 生成设备可用性饼图
     *
     * @return
     * @throws Exception
     */
    public String loadChart() throws Exception {
        Long noneTime = alertService.getEntityAvailability(entityId, "301", day);
        Long subTime = day * 24 * 3600 - noneTime;
        Chart chart = new Chart();
        jofc2.model.elements.PieChart pie = new jofc2.model.elements.PieChart();
        chart.setBackgroundColour("#FFFFFF");
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        pie.addSlice(subTime, resourceManager.getNotNullString("chart.subTime"));
        pie.addSlice(noneTime, resourceManager.getNotNullString("chart.noneTime"));
        pie.setTooltip(resourceManager.getNotNullString("chart.noneTime") + "#percent#");
        pie.setColours(resourceManager.getNotNullString("chart.color1"),
                resourceManager.getNotNullString("chart.color2"));
        pie.setKey_on_click(SUCCESS);
        pie.setAnimate(true);
        pie.setBorder(100);
        pie.setRadius(80);
        chart.addElements(pie);
        String data = chart.toString();

        writeDataToAjax(data);
        return NONE;
    }

    /**
     * 获得设备可用时长
     *
     * @return
     * @throws Exception
     */
    public String loadEntityAvailability() throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        Long noneTime = alertService.getEntityAvailability(entityId, "301", day);
        Long subTime = day * 24 * 3600 - noneTime;
        BigDecimal d = new BigDecimal(100).multiply(new BigDecimal(subTime)).divide(new BigDecimal(day * 24 * 3600), 2,
                BigDecimal.ROUND_HALF_UP);
        StringBuilder time = StringUtil.getTimeString(subTime);
        String p = d.toString() + "%";
        json.put("subTime", time);
        json.put("percent", p);
        array.add(json);
        writeDataToAjax(json);
        return NONE;
    }

    public String showCurrentAlertList() {
        // 获取所有的设备并组装成 Ip[别名] 的形式,在关联设备查询时使用
        List<Entity> entityList = entityService.getEntityWithIp();
        List<String> IpNameList = new ArrayList<String>();
        for (int i = 0; i < entityList.size(); i++) {
            IpNameList.add(entityList.get(i).getIp() + "[" + entityList.get(i).getName() + "]");
        }
        ipNameJson = JSONArray.fromObject(IpNameList);
        
        String queryMode = getCurrentAlertQueryMode();
        if (QUERYMODE_ADVANCED.equals(queryMode)) {
            simpleModeFlag = false;
        } else {
            simpleModeFlag = true;
        }
        
        return SUCCESS;
    }

    public String showAttentionAlert() {
        return SUCCESS;
    }

    /**
     * @deprecated 方法实现方式已经改变,见方法 getAlertTypeTree
     * @return
     * @throws IOException
     */
    @Deprecated
    public String getAllAlertType() throws IOException {
        List<AlertType> alertType = alertService.getAllAlertTypes();
        for (AlertType type : alertType) {
            type.setDisplayName(getString(type.getDisplayName(), "fault"));
            type.setName(getString(type.getName(), "fault"));
        }
        JSONArray typeJson = JSONArray.fromObject(alertType);
        typeJson.write(response.getWriter());
        return NONE;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public String showAlertDetail() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        alert = alertService.getAlertById(alertId);

        /*
         * 上面方法查询的是当前告警。当设备重启后，当前告警会自动转到历史告警，所以需要对上面方法的查询结果做非空判断 如果查询结果为空，则提示转到历史告警查看。
         */
        if (alert == null) {
            return "noCurrentAlert";
        }

        alert.setLevelName(resourceManager.getNotNullString(alert.getLevelName()));
        alert.setTypeName(resourceManager.getNotNullString(alert.getTypeName()));
        // 处理温度转换
        try {
            alert.setMessage(UnitConfigConstant.parseUnitConfigAlertMsg(alert.getTypeId(), alert.getMessage()));
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return SUCCESS;
    }

    /**
     * 获取所有的告警类型,并组装成树结构,在告警查看器页面选择告警类型时使用 注意此方法中使用 org.json包里的Json对象更方便处理问题
     *
     * @author flackyang
     * @since 2013-11-11
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String getAlertTypeTree() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        List<AlertType> list = alertService.getAllAlertTypes();
        JSONArray jsonAlertType = new JSONArray();

        // 将要返回的告警树结构
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

        // 构建告警非叶子节点的map
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>(list == null ? 0 : list.size());
        AlertType type = null;
        TreeNode treeNode = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() != 0) {
                continue;
            }
            treeNode = new TreeNode();
            treeNode.setName(resourceManager.getString(type.getDisplayName()));
            treeNode.setValue(String.valueOf(type.getTypeId()));
            treeNode.setChildren(new ArrayList<TreeNode>());
            treeNodeList.add(treeNode);
            map.put(String.valueOf(type.getTypeId()), treeNode);
        }

        // 第二遍遍历，构建叶子节点
        TreeNode parent;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() == 0) {
                continue;
            }
            if (type.getTypeId().equals(200002) || type.getTypeId().equals(12326)) {
                // 移出spectrumAlertClearType和光信号恢复告警
                continue;
            }
            if (!uc.hasSupportModule("cmc")) {
                if (EponConstants.CC_ALERT_TYPE.contains(type.getCategory())
                        || EponConstants.CC_ALERT_TYPE.contains(type.getTypeId())) {
                    continue;
                }
            }
            treeNode = new TreeNode();
            treeNode.setName(resourceManager.getString(type.getDisplayName()));
            treeNode.setValue(String.valueOf(type.getTypeId()));
            treeNode.setChildren(new ArrayList<TreeNode>());
            map.put(String.valueOf(type.getTypeId()), treeNode);
            parent = map.get(String.valueOf(type.getParentId()));
            if (parent == null) {
                treeNodeList.add(treeNode);
            } else {
                parent.getChildren().add(treeNode);
            }
        }
        writeDataToAjax(treeNodeList);
        return NONE;
    }

    public String showAlarmViewer() {
        return SUCCESS;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public void setAlertIds(List<Long> alertIds) {
        this.alertIds = alertIds;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getHostDevice() {
        return hostDevice;
    }

    public void setHostDevice(String hostDevice) {
        this.hostDevice = hostDevice;
    }

    public String getEndTime() {
        return endTime;
    }

    public Long getEntityId() {
        return entityId;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public int getDay() {
        return day;
    }

    public String getFolderId() {
        return folderId;
    }

    public int getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getTypeId() {
        return typeId;
    }

    /**
     * @return the userAlert
     */
    public boolean isUserAlert() {
        return userAlert;
    }

    /**
     * @param userAlert
     *            the userAlert to set
     */
    public void setUserAlert(boolean userAlert) {
        this.userAlert = userAlert;
    }

    public String getTypeIdList() {
        return typeIdList;
    }

    public void setTypeIdList(String typeIdList) {
        this.typeIdList = typeIdList;
    }

    public Alert getAlert() {
        return alert;
    }

    public Long getAlertId() {
        return alertId;
    }

    /**
     * @return the confirmStatus
     */
    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    /**
     * @param confirmStatus
     *            the confirmStatus to set
     */
    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    /**
     * @return the clearStatus
     */
    public Integer getClearStatus() {
        return clearStatus;
    }

    /**
     * @param clearStatus
     *            the clearStatus to set
     */
    public void setClearStatus(Integer clearStatus) {
        this.clearStatus = clearStatus;
    }

    /**
     * @return the confirmUser
     */
    public String getConfirmUser() {
        return confirmUser;
    }

    /**
     * @param confirmUser
     *            the confirmUser to set
     */
    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    /**
     * @return the confirmStartTime
     */
    public String getConfirmStartTime() {
        return confirmStartTime;
    }

    /**
     * @param confirmStartTime
     *            the confirmStartTime to set
     */
    public void setConfirmStartTime(String confirmStartTime) {
        this.confirmStartTime = confirmStartTime;
    }

    /**
     * @return the confirmEndTime
     */
    public String getConfirmEndTime() {
        return confirmEndTime;
    }

    /**
     * @param confirmEndTime
     *            the confirmEndTime to set
     */
    public void setConfirmEndTime(String confirmEndTime) {
        this.confirmEndTime = confirmEndTime;
    }

    /**
     * @return the clearUser
     */
    public String getClearUser() {
        return clearUser;
    }

    /**
     * @param clearUser
     *            the clearUser to set
     */
    public void setClearUser(String clearUser) {
        this.clearUser = clearUser;
    }

    /**
     * @return the clearStartTime
     */
    public String getClearStartTime() {
        return clearStartTime;
    }

    /**
     * @param clearStartTime
     *            the clearStartTime to set
     */
    public void setClearStartTime(String clearStartTime) {
        this.clearStartTime = clearStartTime;
    }

    /**
     * @return the clearEndTime
     */
    public String getClearEndTime() {
        return clearEndTime;
    }

    /**
     * @param clearEndTime
     *            the clearEndTime to set
     */
    public void setClearEndTime(String clearEndTime) {
        this.clearEndTime = clearEndTime;
    }

    public List<Long> getAlertIds() {
        return alertIds;
    }

    public AlertService getAlertService() {
        return alertService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public JSONArray getIpNameJson() {
        return ipNameJson;
    }

    public void setIpNameJson(JSONArray ipNameJson) {
        this.ipNameJson = ipNameJson;
    }

    public String getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(String queryModel) {
        this.queryModel = queryModel;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public Boolean getSimpleModeFlag() {
        return simpleModeFlag;
    }

    public void setSimpleModeFlag(Boolean simpleModeFlag) {
        this.simpleModeFlag = simpleModeFlag;
    }

}
