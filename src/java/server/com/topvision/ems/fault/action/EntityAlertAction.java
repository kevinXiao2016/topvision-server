package com.topvision.ems.fault.action;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.HistoryAlertService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.domain.Page;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

@Controller("entityAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityAlertAction extends BaseAction {
    private static final long serialVersionUID = -5308915108398551190L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(EntityAlertAction.class);
    private Entity entity;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private HistoryAlertService historyAlertService;
    private Long entityId;
    private String ip;
    private int type = 1;
    private String host;
    private String levelId;
    private String typeId;
    private String message;
    private String startTime;
    private String endTime;
    private String entityType;
    private Long cmcId;
    private String productType;
    private Long alertId;

    @SuppressWarnings("unchecked")
    public String loadEntityAlert() throws Exception {
        Page page = super.getExtPage();
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        Map<String, String> map = new HashMap<String, String>();
        if (type == 1) {
            map.put("sql", " A.confirmUser is null");
        } else if (type == 2) {
            map.put("sql", " A.confirmUser is not null");
        }
        // 告警来源
        if (host != null && !host.equals("all") && !host.equals("")) {
            map.put("host", host);
        }
        // 告警等级
        if (levelId != null && !levelId.equals("all") && !levelId.equals("")) {
            map.put("levelId", levelId);
        }
        // 告警类型
        if (typeId != null && !typeId.equals("all") && !typeId.equals("")) {
            map.put("typeId", typeId);
        }
        // 发生原因
        if (message != null && !message.equals("")) {
            map.put("message", message);
        }
        // 开始时间
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        }
        // 结束时间
        if (endTime != null && !endTime.equals("")) {
            map.put("endTime", endTime);
        }
        if(alertId != null){
        	map.put("alertId", String.valueOf(alertId));
        }
        map.put("limit", String.valueOf(limit));
        map.put("start", String.valueOf(start));
        map.put("sort", "lastTime");
        map.put("dir", "DESC");
        map.put("limit", String.valueOf(limit));
        map.put("start", String.valueOf(start));
        pageData = alertService.getEntityCurrentAlert(entityId, page, map);
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            int size = pageData.getData().size();
            if (size > 0) {
                JSONObject temp = null;
                Alert alert = null;
                List<Alert> list = pageData.getData();
                for (int i = 0; i < size; i++) {
                    alert = list.get(i);
                    temp = new JSONObject();
                    temp.put("alertId", alert.getAlertId());
                    try {
                        temp.put("message", UnitConfigConstant.parseUnitConfigAlertMsg(alert.getTypeId(), alert.getMessage()));
                    } catch (Exception e) {
                        temp.put("message", alert.getMessage());
                        logger.debug(e.getMessage());
                    }
                    temp.put("source", alert.getSource());
                    temp.put("typeName", getString(alert.getTypeName(), "fault"));
                    temp.put("level", alert.getLevelId());
                    temp.put("levelName", getString(alert.getLevelName(), "fault"));
                    temp.put("status", alert.getStatus());
                    temp.put("confirmUser", alert.getConfirmUser());
                    temp.put("confirmTime", alert.getStatus() == null || alert.getStatus() == Alert.ALERT ? ""
                            : DATE_FORMAT.format(alert.getConfirmTime()));
                    temp.put("confirmMsg", alert.getConfirmMessage());
                    temp.put("clearUser", alert.getClearUser());
                    temp.put("clearMsg", alert.getClearMessage());
                    temp.put("clearTime", DATE_FORMAT.format(alert.getClearTime()));
                    temp.put("firstTime", DateUtils.format(alert.getFirstTime()));
                    temp.put("lastTime", DateUtils.format(alert.getLastTime()));
                    if (alert.getConfirmUser() != null) {
                        temp.put("confirmTime", DateUtils.format(alert.getConfirmTime()));
                    } else {
                        temp.put("confirmTime", "");
                    }
                    array.add(temp);
                }
            }
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    @SuppressWarnings("unchecked")
    public String loadEntityHistoryAlert() throws Exception {
        Page page = super.getExtPage();
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        Map<String, String> map = new HashMap<String, String>();
        if (type == 3) {
            map.put("sql", " confirmUser is null");
        } else if (type == 4) {
            map.put("sql", " confirmUser is not null");
        }
        // 告警来源
        if (host != null && !host.equals("all") && !host.equals("")) {
            map.put("host", host);
        }
        // 告警等级
        if (levelId != null && !levelId.equals("all") && !levelId.equals("")) {
            map.put("levelId", levelId);
        }
        // 告警类型
        if (typeId != null && !typeId.equals("all") && !typeId.equals("")) {
            map.put("typeId", typeId);
        }
        // 发生原因
        if (message != null && !message.equals("")) {
            map.put("message", message);
        }
        // 开始时间
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        }
        // 结束时间
        if (endTime != null && !endTime.equals("")) {
            map.put("endTime", endTime);
        }
        map.put("limit", String.valueOf(limit));
        map.put("start", String.valueOf(start));
        map.put("sort", "lastTime");
        map.put("dir", "DESC");
        map.put("limit", String.valueOf(limit));
        map.put("start", String.valueOf(start));
        pageData = historyAlertService.getEntityHistoryAlert(entityId, page, map);
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            int size = pageData.getData().size();
            if (size > 0) {
                JSONObject temp = null;
                HistoryAlert alert = null;
                List<HistoryAlert> list = pageData.getData();
                for (int i = 0; i < size; i++) {
                    alert = list.get(i);
                    temp = new JSONObject();
                    temp.put("alertId", alert.getAlertId());
                    try {
                        temp.put("message", UnitConfigConstant.parseUnitConfigAlertMsg(alert.getTypeId(), alert.getMessage()));
                    } catch (Exception e) {
                        temp.put("message", alert.getMessage());
                        logger.debug(e.getMessage());
                    }
                    temp.put("source", alert.getSource());
                    temp.put("typeName", getString(alert.getTypeName(), "fault"));
                    temp.put("level", alert.getLevelId());
                    temp.put("levelName", getString(alert.getLevelName(), "fault"));
                    temp.put("status", alert.getStatus());
                    temp.put("confirmUser", alert.getConfirmUser());
                    temp.put("clearUser", alert.getClearUser());
                    temp.put("firstTime", DateUtils.format(alert.getFirstTime()));
                    temp.put("lastTime", DateUtils.format(alert.getLastTime()));
                    if (alert.getConfirmUser() != null) {
                        temp.put("confirmTime", DateUtils.format(alert.getConfirmTime()));
                    } else {
                        temp.put("confirmTime", "");
                    }
                    if (alert.getClearUser() != null) {
                        temp.put("clearTime", DateUtils.format(alert.getClearTime()));
                    } else {
                        temp.put("clearTime", "");
                    }
                    array.add(temp);
                }
            }
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadFloatingAlert() throws Exception {
        if (entityId == 0) {
            return NONE;
        }
        List<Alert> alerts = alertService.loadFloatingAlert(entityId);
        int size = alerts.size();
        boolean flag = false;
        if (size > 5) {
            flag = true;
            size = size - 1;
        }
        StringBuilder sb = new StringBuilder("");
        sb.append("<div style=\"padding:0 10px 5px 10px;\"><table cellspacing=2 cellpadding=0>");
        for (int i = 0; i < size; i++) {
            Alert alert = alerts.get(i);
            sb.append("<tr><td width=20px>");
            sb.append(String.valueOf(i + 1));
            sb.append(".</td>");
            sb.append("<td>");
            sb.append(alert.getMessage());
            sb.append("</td></tr>");
        }
        if (flag) {
            sb.append("<tr><td></td><td>...</td></tr>");
        }
        sb.append("</table></div>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showEntityAlertJsp() {
        if (entityId != null && entityId > 0) {
            entity = entityService.getEntity(entityId);
            ip = entity.getIp();
        } else {
            entity = entityService.getEntityByIp(ip);
            entityId = entity.getEntityId();
        }
        if (entityTypeService.isOlt(entity.getTypeId())) {
            return "olt";
        }
        if (entityTypeService.isCcmts(entity.getTypeId())) {
            this.setCmcId(entityId);
            this.setProductType(entity.getTypeId() + "");
            return "cmc";
        }
        return SUCCESS;
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

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setHistoryAlertService(HistoryAlertService historyAlertService) {
        this.historyAlertService = historyAlertService;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Entity getEntity() {
        return entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getIp() {
        return ip;
    }

    public int getType() {
        return type;
    }

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
    
}
