/***********************************************************************
 * $Id: CmcAlertAction.java,v1.0 2011-11-23 下午07:54:56 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.alert.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.alert.service.CmcAlertService;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.zetaframework.util.ZetaUtil;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author loyal
 * @created @2011-11-23-下午07:54:56
 * 
 */
@Controller("cmcAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcAlertAction extends BaseAction {
    private static final long serialVersionUID = -2555136557512575411L;
    private static final Logger logger = LoggerFactory.getLogger(CmcAlertAction.class);
    /**
     * 字符串all,作为一个标记位，代表“所有”。
     */
    private static final String ALL_STR = "all";
    @Resource(name = "cmcAlertService")
    private CmcAlertService cmcAlertService;
    @Resource(name = "alertService")
    private AlertService alertService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Long entityId;
    private Long cmcId;
    private String levelId;
    private String typeId;
    private String startTime;
    private String endTime;
    private int start;
    private int limit;
    private Integer type = 1;
    private String message;
    private JSONArray docsDevEvControlListObject = new JSONArray();
    private List<DocsDevEvControl> docsDevEvControlList;
    private Integer productType;
    private Long alertId;
    private CmcAttribute cmcAttribute;

    /**
     * 显示cmc告警列表页面
     * 
     * @return String
     */
    public String showCmcAlert() {
    	setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        if (productType == null) {
            productType = Integer.parseInt(cmcAttribute.getCmcDeviceStyle().toString());
        }
        return SUCCESS;
    }

    /**
     * 显示cmc历史告警列表页面
     * 
     * @return String
     */
    public String showCmcHistoryAlert() {
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        if (productType == null) {
            productType = Integer.parseInt(cmcAttribute.getCmcDeviceStyle().toString());
        }
        return SUCCESS;
    }

    /**
     * 显示cmc告警设置页面
     * 
     * @return String
     */
    public String showAlertSetting() {
        docsDevEvControlList = cmcAlertService.getdocsDevEvControlList(entityId);
        for (DocsDevEvControl evControl : docsDevEvControlList) {
            evControl.calDocsDevEvReportingType();
        }
        docsDevEvControlListObject = JSONArray.fromObject(docsDevEvControlList);
        return SUCCESS;
    }

    /**
     * 获取告警列表信息
     * 
     * @return String
     */
    public String getCmcAlertList() {
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
        alertList = cmcAlertService.getCmcAlertList(map, start, limit);
        int size = cmcAlertService.getCmcAlertListNum(map);
        for (Alert anAlertList : alertList) {
            anAlertList.setTypeName(resourceManager.getNotNullString(anAlertList.getTypeName()));
            anAlertList.setLevelName(resourceManager.getNotNullString(anAlertList.getLevelName()));
            if (anAlertList.getConfirmUser() == null) {
                anAlertList.setConfirmTimeStr("");
            }
            try {
                anAlertList.setMessage(UnitConfigConstant.parseUnitConfigAlertMsg(anAlertList.getTypeId(),
                        anAlertList.getMessage()));
            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        }
        json.put("data", alertList);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String getCmcAlertType() throws IOException {
        // List<AlertType> alertType = alertService.getAllAlertTypes();
        List<AlertType> alertType = cmcAlertService.getCmcAlertTypes();
        for (AlertType type : alertType) {
            type.setDisplayName(getString(type.getDisplayName(), "fault"));
            type.setName(getString(type.getName(), "fault"));
        }
        JSONArray typeJson = JSONArray.fromObject(alertType);
        writeDataToAjax(typeJson);
        return NONE;
    }

    /**
     * 获取所有的CCMTS告警类型,并组装成树结构,在CCMTS告警查看页面选择告警类型时使用 注意此方法中使用 org.json包里的Json对象更方便处理问题
     * 
     * @author flackyang
     * @since 2013-11-11
     * @return
     * @throws Exception
     */
    public String getAllCmcAlertType() throws Exception {
        List<AlertType> list = cmcAlertService.getCmcAlertTypes();

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
            treeNode.setName(ZetaUtil.getStaticString(type.getDisplayName(), "fault"));
            treeNode.setValue(String.valueOf(type.getTypeId()));
            treeNode.setChildren(new ArrayList<TreeNode>());
            treeNodeList.add(treeNode);
            map.put(String.valueOf(type.getTypeId()), treeNode);
        }

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
            treeNode = new TreeNode();
            treeNode.setName(ZetaUtil.getStaticString(type.getDisplayName(), "fault"));
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

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
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
        alertList = cmcAlertService.getCmcHistoryAlertList(map, start, limit);
        int size = cmcAlertService.getCmcHistoryAlertListNum(map);
        for (HistoryAlert anAlertList : alertList) {
            anAlertList.setTypeName(resourceManager.getNotNullString(anAlertList.getTypeName()));
            anAlertList.setLevelName(resourceManager.getNotNullString(anAlertList.getLevelName()));
            if (anAlertList.getConfirmUser() == null) {
                anAlertList.setConfirmTimeStr("");
            }
            try {
                anAlertList.setMessage(UnitConfigConstant.parseUnitConfigAlertMsg(anAlertList.getTypeId(),
                        anAlertList.getMessage()));
            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        }
        json.put("data", alertList);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public CmcAlertService getCmcAlertService() {
        return cmcAlertService;
    }

    public void setCmcAlertService(CmcAlertService cmcAlertService) {
        this.cmcAlertService = cmcAlertService;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit
     *            the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the levelId
     */
    public String getLevelId() {
        return levelId;
    }

    /**
     * @param levelId
     *            the levelId to set
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public JSONArray getDocsDevEvControlListObject() {
        return docsDevEvControlListObject;
    }

    public void setDocsDevEvControlListObject(JSONArray docsDevEvControlListObject) {
        this.docsDevEvControlListObject = docsDevEvControlListObject;
    }

    public List<DocsDevEvControl> getDocsDevEvControlList() {
        return docsDevEvControlList;
    }

    public void setDocsDevEvControlList(List<DocsDevEvControl> docsDevEvControlList) {
        this.docsDevEvControlList = docsDevEvControlList;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    /**
     * @return the alertService
     */
    public AlertService getAlertService() {
        return alertService;
    }

    /**
     * @param alertService
     *            the alertService to set
     */
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * @return the cmcService
     */
    public CmcService getCmcService() {
        return cmcService;
    }

    /**
     * @param cmcService
     *            the cmcService to set
     */
    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}

}
