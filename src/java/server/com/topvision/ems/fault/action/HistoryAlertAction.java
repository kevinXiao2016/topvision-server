package com.topvision.ems.fault.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.AlertTypeEx;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.HistoryAlertService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("historyAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryAlertAction extends BaseAction {
    private static final long serialVersionUID = 4500665633445539061L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(HistoryAlertAction.class);
    private int typeId;
    private int level;
    private long alertId;
    private String hostDevice;
    private String startTime;
    private String endTime;
    private JSONArray ipNameJson;

    private String clearUser;
    private String clearStartTime;
    private String clearEndTime;
    private String clearMessage;

    private HistoryAlert historyAlert;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private HistoryAlertService historyAlertService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserService userService;

    // Add by Rod For User Alert Load
    private boolean userAlert = false;
    private String typeIdList;
    
    private String getLevelName(byte level) {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        String sLevel = Level.getNameByLevel(level);
        try {
            return resourceManager.getString(sLevel);
        } catch (ResourceNotFoundException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("get Level name.", e.getMessage());
            }
            return sLevel;
        }
    }

    public String loadHistoryAlertList() throws Exception {
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        JSONObject json = new JSONObject();
        Map<String, String> map = new HashMap<String, String>();
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
            /*
             * String[] hostQuery = hostDevice.split(" "); if (hostQuery.length == 1) { long ipLong
             * = 0; try { IpUtils ipUtils; ipUtils = new IpUtils(hostDevice); ipLong =
             * ipUtils.longValue(); } catch (Exception e) { logger.debug("", e); }
             * 
             * if (ipLong == 0) { map.put("host", hostDevice); } else { map.put("ip", hostDevice); }
             * } else { map.put("ip", hostQuery[0]); map.put("host", hostQuery[1]); //
             * 如果带IP则认为是指定了设备的，做精确查找 // map.put("host", hostQuery[1]); }
             */
            map.put("host", hostDevice);
        }
        // 开始时间
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        }
        // 结束时间
        if (endTime != null && !endTime.equals("")) {
            map.put("endTime", endTime);
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
        if (clearMessage != null && !"".equals(clearMessage)) {
            map.put("clearMessage", clearMessage);
        }

        map.put("sort", "lastTime");
        map.put("dir", "DESC");
        map.put("limit", String.valueOf(limit));
        map.put("start", String.valueOf(start));
        PageData<HistoryAlert> pageData = historyAlertService.queryHistoryAlert(super.getExtPage(), map);
        JSONArray array = new JSONArray();
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
                    try {
                        alert.setMessage(
                                UnitConfigConstant.parseUnitConfigAlertMsg(alert.getTypeId(), alert.getMessage()));
                    } catch (Exception e) {
                        logger.debug(e.getMessage());
                    }
                    temp = new JSONObject();
                    temp.put("alertId", alert.getAlertId());
                    temp.put("message", alert.getMessage());
                    temp.put("source", alert.getSource());
                    temp.put("host", alert.getHost());
                    temp.put("typeName", resourceManager.getNotNullString(alert.getTypeName()));
                    temp.put("level", alert.getLevelId());
                    temp.put("levelName", getLevelName(alert.getLevelId()));
                    temp.put("firstTime", DATE_FORMAT.format(alert.getFirstTime()));
                    temp.put("lastTime", DATE_FORMAT.format(alert.getLastTime()));
                    temp.put("clearUser", alert.getClearUser());
                    temp.put("clearTime", DATE_FORMAT.format(alert.getClearTime()));
                    temp.put("entityType", alert.getEntityType());
                    temp.put("entityId", alert.getEntityId());
                    temp.put("entityName", alert.getEntityName());
                    temp.put("nativeMessage", alert.getMessage());
                    Entity entity = entityService.getEntity(alert.getEntityId());
                    if (entity.getParentId() != null) {
                        Entity parent = entityService.getEntity(entity.getParentId());
                        temp.put("parentId", parent.getEntityId());
                        temp.put("parentName", parent.getName());
                    } else {
                        temp.put("parentId", alert.getEntityId());
                        temp.put("parentName", alert.getEntityName());
                    }
                    // 为了配合广州的需求，对于CC的告警，提供告警描述里CC MAC地址跳转
                    if (entityTypeService.isCcmts(alert.getEntityType())) {

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
                    }
                    //PR-910 onu告警信息能直接跳转onu视图
                    if (entityTypeService.isOnu(alert.getEntityType())) {
                        if (alert.getMessage().indexOf("[ONU:") != -1) {
                            Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
                            Matcher matcher = pattern.matcher(alert.getMessage());
                            String alias = "";
                            if (matcher.find()) {
                                alias = matcher.group(1);
                            }
                            String[] str = alert.getMessage().split(alias);
                            StringBuffer sb = new StringBuffer();
                            sb.append(str[0]).append("<a href='#' onclick='showOnuInfo(\"" + alert.getEntityId() + "\",\""
                                    + entity.getName() + "\")'")
                            .append(">").append(alias).append("</a>").append(str[1]);
                            temp.put("message", sb.toString());
                        }
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
                            sb.append(str[0]).append("<a href='#' onclick='viewCmtsSnap(\"" + alert.getEntityId()
                                    + "\", \"" + cmtsIp + "\")'>").append(cmtsIp).append("</a>").append(str[1]);
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
        }
        json.put("data", array);
        json.write(response.getWriter());
        return NONE;
    }

    public String showHistoryAlertDetail() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        historyAlert = historyAlertService.getHistoryAlert(alertId);
        historyAlert.setTypeName(resourceManager.getNotNullString(historyAlert.getTypeName()));
        historyAlert.setLevelName(resourceManager.getNotNullString(historyAlert.getLevelName()));
        return SUCCESS;
    }

    /**
     * 跳转显示历史告警记录
     * 
     * @return
     */
    public String showHistoryAlertList() {
        // 获取所有的设备并组装成 Ip[别名] 的形式,在关联设备查询时使用
        List<Entity> entityList = entityService.getEntityWithIp();
        List<String> IpNameList = new ArrayList<String>();
        for (int i = 0; i < entityList.size(); i++) {
            IpNameList.add(entityList.get(i).getIp() + "[" + entityList.get(i).getName() + "]");
        }
        ipNameJson = JSONArray.fromObject(IpNameList);
        return SUCCESS;
    }

    public String statHistoryAlertByCategory() throws Exception {
        JSONObject json = new JSONObject();
        List<AlertTypeEx> types = historyAlertService.statHistoryAlertByCategory();
        int size = types == null ? 0 : types.size();
        JSONArray array = new JSONArray();
        json.put("rowCount", size);
        JSONObject temp = null;
        AlertTypeEx alertTypeEx = null;
        for (int i = 0; i < size; i++) {
            alertTypeEx = types.get(i);
            temp = new JSONObject();
            temp.put("typeId", alertTypeEx.getTypeId());
            temp.put("name", alertTypeEx.getDisplayName());
            temp.put("note", alertTypeEx.getNote());
            temp.put("count", alertTypeEx.getCount());
            array.add(temp);
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }

    public void setHistoryAlert(HistoryAlert historyAlert) {
        this.historyAlert = historyAlert;
    }

    public void setHistoryAlertService(HistoryAlertService historyAlertService) {
        this.historyAlertService = historyAlertService;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getHostDevice() {
        return hostDevice;
    }

    public void setHostDevice(String hostDevice) {
        this.hostDevice = hostDevice;
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

    public AlertService getAlertService() {
        return alertService;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public HistoryAlertService getHistoryAlertService() {
        return historyAlertService;
    }

    public long getAlertId() {
        return alertId;
    }

    public HistoryAlert getHistoryAlert() {
        return historyAlert;
    }

    public int getLevel() {
        return level;
    }

    public int getTypeId() {
        return typeId;
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

    public String getClearUser() {
        return clearUser;
    }

    public void setClearUser(String clearUser) {
        this.clearUser = clearUser;
    }

    public String getClearStartTime() {
        return clearStartTime;
    }

    public void setClearStartTime(String clearStartTime) {
        this.clearStartTime = clearStartTime;
    }

    public String getClearEndTime() {
        return clearEndTime;
    }

    public void setClearEndTime(String clearEndTime) {
        this.clearEndTime = clearEndTime;
    }

    public String getClearMessage() {
        return clearMessage;
    }

    public void setClearMessage(String clearMessage) {
        this.clearMessage = clearMessage;
    }

    /**
     * @return the userAlert
     */
    public boolean isUserAlert() {
        return userAlert;
    }

    /**
     * @param userAlert the userAlert to set
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

}