/***********************************************************************
 * $Id: OnuAlertAction.java,v1.0 2015年4月27日 上午9:08:44 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.utils.StringUtils;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.util.ZetaUtil;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author Bravin
 * @created @2015年4月27日-上午9:08:44
 *
 */
@Controller("onuAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuAlertAction extends BaseAction {
    private static final long serialVersionUID = 6782674706298124801L;
    private static final Logger logger = LoggerFactory.getLogger(OnuAlertAction.class);
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private OnuService onuService;
    private Long entityId;
    private String levelId;
    private String typeId;
    private String message;
    private String startTime;
    private String endTime;
    private Long onuId;
    private Long alertId;

    /**
     * 字符串all,作为一个标记位，代表“所有”。
     */
    private static final String ALL_STR = "all";

    /**
     * 获取所有ONU告警类型
     * 
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String getOnuAlertType() throws IOException, JSONException {
        List<AlertType> list = onuAssemblyService.getOnuAlertTypes();

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

    /**
     * 查询ONU告警列表
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuAlert() throws IOException {
        JSONObject json = new JSONObject();
        List<Alert> alertList;
        Map<String, Object> map = new HashMap<String, Object>();
        /*
         * OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId); Long onuIndex =
         * onuAttribute.getOnuIndex();
         */
        // map.put("entityId", entityId);
        // map.put("source", "ONU:" + EponIndex.getPortStringByIndex(onuIndex) + "/" +
        // EponIndex.getOnuNo(onuIndex));
        map.put("onuId", onuId);
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
        if (alertId != null) {
            map.put("alertId", alertId);
        }
        map.put("sort", "lastTime");
        map.put("dir", "DESC");
        map.put("start", start);
        map.put("limit", limit);
        alertList = onuAssemblyService.getOnuAlertList(map);
        int size = onuAssemblyService.getOnuAlertListNum(map);
        for (Alert anAlertList : alertList) {
            anAlertList.setTypeName(ZetaUtil.getStaticString(anAlertList.getTypeName(), "fault"));
            anAlertList.setLevelName(ZetaUtil.getStaticString(anAlertList.getLevelName(), "fault"));
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
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 查询ONU告警列表
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuHistoryAlert() throws IOException {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        /*
         * OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId); Long onuIndex =
         * onuAttribute.getOnuIndex();
         */
        map.put("entityId", entityId);
        map.put("onuId", onuId);
        // map.put("source", "ONU:" + EponIndex.getPortStringByIndex(onuIndex) + "/" +
        // EponIndex.getOnuNo(onuIndex));
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
        map.put("start", start);
        map.put("limit", limit);
        List<HistoryAlert> list = onuAssemblyService.getOnuHistoryAlertList(map);
        int size = onuAssemblyService.getOnuHistoryAlertListNum(map);
        JSONArray array = new JSONArray();
        for (HistoryAlert alert : list) {
            JSONObject $alert = new JSONObject();
            $alert.put("alertId", alert.getAlertId());
            try {
                $alert.put("message", UnitConfigConstant.parseUnitConfigAlertMsg(alert.getTypeId(), alert.getMessage()));
            } catch (Exception e) {
                $alert.put("message", alert.getMessage());
                logger.debug(e.getMessage());
            }
            $alert.put("source", alert.getSource());
            $alert.put("typeName", ZetaUtil.getStaticString(alert.getTypeName(), "fault"));
            $alert.put("level", alert.getLevelId());
            $alert.put("levelName", ZetaUtil.getStaticString(alert.getLevelName(), "fault"));
            $alert.put("status", alert.getStatus());
            $alert.put("confirmUser", alert.getConfirmUser());
            $alert.put("clearUser", alert.getClearUser());
            $alert.put("firstTime", DateUtils.format(alert.getFirstTime()));
            $alert.put("lastTime", DateUtils.format(alert.getLastTime()));
            if (alert.getConfirmUser() != null) {
                $alert.put("confirmTime", DateUtils.format(alert.getConfirmTime()));
            } else {
                $alert.put("confirmTime", "");
            }
            if (alert.getClearUser() != null) {
                $alert.put("clearTime", DateUtils.format(alert.getClearTime()));
            } else {
                $alert.put("clearTime", "");
            }
            array.add($alert);
        }
        json.put("data", array);
        json.put("rowCount", size);
        json.write(response.getWriter());
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

}
