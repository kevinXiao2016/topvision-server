/***********************************************************************
 * $Id: CmtsAlertAction.java,v1.0 2013-10-22 下午1:46:32 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.ems.cmts.service.CmtsAlertService;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

/**
 * @author loyal
 * @created @2013-10-22-下午1:46:32
 * 
 */
@Controller("cmtsAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsAlertAction extends BaseAction {
    private static final long serialVersionUID = -1576776507564840447L;
    /**
     * 字符串all,作为一个标记位，代表“所有”。
     */
    private static final String ALL_STR = "all";
    @Resource(name = "cmtsAlertService")
    private CmtsAlertService cmtsAlertService;
    private Integer type = 1;
    private Long cmcId;
    private Integer productType;
    private String levelId;
    private String typeId;
    private String startTime;
    private String endTime;
    private int start;
    private int limit;
    private String message;
    private Long alertId;

    /**
     * 显示cmts告警列表页面
     * 
     * @return String
     */
    public String showCmtsAlert() {
        return SUCCESS;
    }

    /**
     * 显示cmts历史告警列表页面
     * 
     * @return String
     */
    public String showCmtsHistoryAlert() {
        return SUCCESS;
    }

    /**
     * 显示cmts告警类型
     * 
     * @return String
     * @throws IOException
     */
    public String getCmtsAlertType() throws IOException {
        List<AlertType> alertType = cmtsAlertService.getCmtsAlertType();
        for (AlertType type : alertType) {
            type.setDisplayName(getString(type.getDisplayName(), "fault"));
            type.setName(getString(type.getName(), "fault"));
        }
        JSONArray typeJson = JSONArray.fromObject(alertType);
        writeDataToAjax(typeJson);
        return NONE;
    }

    /**
     * 获取所有的CMTS告警类型,并组装成树结构,在Cmts设备告警查看页面使用
     * @author flackyang
     * @since 2013-11-12
     * @return
     * @throws Exception
     */
    public String getAllCmtsAlertType() throws Exception {
        List<AlertType> list = cmtsAlertService.getCmtsAlertType();
        JSONArray jsonAlertType = new JSONArray();
        JSONObject json = new JSONObject();
        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0
                : list.size());
        AlertType type = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() != 0) {
                continue;
            }
            json = new JSONObject();
            json.put("name", getString(type.getDisplayName(), "fault"));
            json.put("value", type.getTypeId());
            json.put("children", new JSONArray());
            map.put(String.valueOf(type.getTypeId()), json);
            jsonAlertType.add(json);
        }
        JSONObject parent = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() == 0) {
                continue;
            }
            json = new JSONObject();
            json.put("name", getString(type.getDisplayName(), "fault"));
            json.put("value", type.getTypeId());
            json.put("children", new JSONArray());
            map.put(String.valueOf(type.getTypeId()), json);
            parent = map.get(String.valueOf(type.getParentId()));
            if (parent == null) {
                jsonAlertType.add(json);
            } else {
                parent.getJSONArray("children").add(json);
            }
        }
        jsonAlertType.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示cmts告警
     * 
     * @return String
     */
    public String getCmtsAlertList() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        Map<String, Object> json = new HashMap<String, Object>();
        List<Alert> alertList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        // 告警等级
        if (!StringUtils.isEmpty(levelId) && !ALL_STR.equals(levelId)) {
            map.put("levelId", levelId);
        }
        // 告警类型
        if (!StringUtils.isEmpty(typeId) && !ALL_STR.equals(typeId)) {
            map.put("typeId", typeId);
        }
        // 告警描述
        if (!StringUtils.isEmpty(message)) {
            map.put("message", message);
        }
        // 开始时间
        if (!StringUtils.isEmpty(startTime)) {
            map.put("startTime", startTime);
        }
        // 结束时间
        if (!StringUtils.isEmpty(endTime)) {
            map.put("endTime", endTime);
        }
        if(alertId != null){
        	map.put("alertId", alertId);
        }
        map.put("sort", "lastTime");
        map.put("dir", "DESC");
        alertList = cmtsAlertService.getCmtsAlertList(map, start, limit);
        int size = cmtsAlertService.getCmtsAlertListNum(map);
        for (Alert anAlertList : alertList) {
            anAlertList.setTypeName(resourceManager.getNotNullString(anAlertList.getTypeName()));
            anAlertList.setLevelName(resourceManager.getNotNullString(anAlertList.getLevelName()));
            if (anAlertList.getConfirmUser() == null) {
                anAlertList.setConfirmTimeStr("");
            }
        }
        json.put("data", alertList);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取历史告警列表信息
     * 
     * @return String
     */
    public String getCmcHistoryAlertList() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        Map<String, Object> json = new HashMap<String, Object>();
        List<HistoryAlert> alertList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        // 告警等级
        if (!StringUtils.isEmpty(levelId) && !ALL_STR.equals(levelId)) {
            map.put("levelId", levelId);
        }
        // 告警类型
        if (!StringUtils.isEmpty(typeId) && !ALL_STR.equals(typeId)) {
            map.put("typeId", typeId);
        }
        // 告警描述
        if (!StringUtils.isEmpty(message)) {
            map.put("message", message);
        }
        // 开始时间
        if (!StringUtils.isEmpty(startTime)) {
            map.put("startTime", startTime);
        }
        // 结束时间
        if (!StringUtils.isEmpty(endTime)) {
            map.put("endTime", endTime);
        }
        map.put("sort", "lastTime");
        map.put("dir", "DESC");
        alertList = cmtsAlertService.getCmtsHistoryAlertList(map, start, limit);
        int size = cmtsAlertService.getCmtsHistoryAlertListNum(map);
        for (HistoryAlert anAlertList : alertList) {
            anAlertList.setTypeName(resourceManager.getNotNullString(anAlertList.getTypeName()));
            anAlertList.setLevelName(resourceManager.getNotNullString(anAlertList.getLevelName()));
            if (anAlertList.getConfirmUser() == null) {
                anAlertList.setConfirmTimeStr("");
            }
        }
        json.put("data", alertList);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
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

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public CmtsAlertService getCmtsAlertService() {
        return cmtsAlertService;
    }

    public void setCmtsAlertService(CmtsAlertService cmtsAlertService) {
        this.cmtsAlertService = cmtsAlertService;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

}
