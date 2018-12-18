/***********************************************************************
 * $Id: ReportTemplateServiceImpl.java,v1.0 2013-6-18 下午5:36:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.report.dao.ReportTemplateDao;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.ReportTemplate;
import com.topvision.ems.report.service.ReportTaskService;
import com.topvision.ems.report.service.ReportTemplateService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013-6-18-下午5:36:12
 * 
 */
@Service("reportTemplateService")
public class ReportTemplateServiceImpl extends BaseService implements ReportTemplateService {
    private static final String BASE_NODE_PATH = "/css/treeview/";
    private List<ReportTemplate> reportTemplates;
    @Autowired
    private ReportTemplateDao reportTemplateDao;
    @Autowired
    private ReportTaskService reportTaskService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @PostConstruct
    public void initialize() {
        reportTemplates = reportTemplateDao.getReportTemplate();
    }

    @Override
    /**
     * @modify by fanzidong
     */
    public JSONArray loadAllReportTemplate(Map<String, Boolean> hasSupportMap) {
        return loadReportTemplate(false, false, hasSupportMap);
    }

    @Override
    public JSONArray loadDisplayReportTemplate(Map<String, Boolean> hasSupportMap) {
        return loadReportTemplate(true, false, hasSupportMap);
    }

    @Override
    public JSONArray getTaskableReportTemplate(Map<String, Boolean> hasSupportMap) {
        return loadReportTemplate(true, true, hasSupportMap);
    }

    private JSONArray loadReportTemplate(Boolean display, Boolean taskable, Map<String,Boolean> hasSupportMap) {
        // 将reportTemplates转成TreeNode的标准树
        reportTemplates = reportTemplateDao.getReportTemplate();
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        HashMap<String, TreeNode> treeMap = new HashMap<String, TreeNode>();
        for (ReportTemplate template : reportTemplates) {
            // 封装基本属性
            TreeNode treeNode = new TreeNode();
            treeNode.setId(template.getTemplateId());
            treeNode.setParentId(template.getSuperiorId());
            treeNode.setText(ReportTaskUtil.getString(template.getDisplayName(), "report"));
            treeNode.setName(template.getName());
            treeNode.setIconCls("icoG1");
            treeNode.setUrl(template.getUrl());
            treeNode.setDisplay(template.isDisplay());
            if (treeNode.getUrl() == null || treeNode.getUrl() == "") {
                treeNode.setNeedATag(false);
            } else {
                treeNode.setNeedATag(true);
            }
            treeNode.setChildren(new ArrayList<TreeNode>());
            // 如果只提供CCMTS模块，则不显示CM实时统计报表、OLT用户流量统计报表、OLT运行状态报表、信道使用情况报表
            if ("false".equalsIgnoreCase(hasSupportMap.get("olt").toString())) {
                if ("cmRealTimeUserStaticReportCreator".equals(template.getName())
                        || "cmcUserFlowReportCreator".equals(template.getName())
                        || "oltRunningStatusReportCreator".equals(template.getName())
                        || "ccmtsChannelListReportCreator".equals(template.getName())
                        
                        || "oltDeviceListReportCreator".equals(template.getName())
                        || "oltBoardReportCreator".equals(template.getName())
                        || "oltSniPortReportCreator".equals(template.getName())
                        || "oltPonPortReportCreator".equals(template.getName())
                        || "oltCpuReportCreator".equals(template.getName())
                        || "oltMemReportCreator".equals(template.getName())
                        || "oltSniPortFlowReportCreator".equals(template.getName())
                        || "oltPonPortFlowReportCreator".equals(template.getName())
                        || "oltResponseReportCreator".equals(template.getName())
                        || "currentAlarmReportCreator".equals(template.getName())
                        || "historyAlarmReportCreator".equals(template.getName())) {
                    continue;
                }
            }
            
            if("false".equalsIgnoreCase(hasSupportMap.get("onu").toString())){
                if("onuDeviceListReportCreator".equals(template.getName())){
                    continue;
                }
            }
            
            if("false".equalsIgnoreCase(hasSupportMap.get("cmc").toString())){
                if("ccmtsDeviceListReportCreator".equals(template.getName())
                        ||"cmReportCreator".equals(template.getName())
                        ||"cmcSnrReportCreator".equals(template.getName())
                        ||"cmDailyNumStaticReportCreator".equals(template.getName())
                        ||"ccmtsUpChlFlowReportCreator".equals(template.getName())
                        ||"ccmtsDownChlFlowReportCreator".equals(template.getName())
                        ||"cmRealTimeUserStaticReportCreator".equals(template.getName())
                        ||"ccmtsChannelListReportCreator".equals(template.getName())){
                    continue;
                }
            }
            
            // 用于显示用途，如果该报表不支持显示，则不加入报表树中
            if (display && !template.isDisplay()) {
                continue;
            }
            // 用于任务用途，如果该报表不支持任务，则不加入报表树中
            if (taskable && !template.isTaskable() && template.getSuperiorId() != -1L) {
                continue;
            }
            // 将此节点添加到相应的父节点中
            treeMap.put(treeNode.getId().toString(), treeNode);
            TreeNode parentNode = treeMap.get(treeNode.getParentId().toString());
            if (parentNode == null) {
                treeNodes.add(treeNode);
            } else {
                parentNode.getChildren().add(treeNode);
            }
        }
        return JSONArray.fromObject(treeNodes);
    }

    @Override
    public JSONArray loadReportTaskTreeData() {
        JSONArray jsonReportTaskTreeData = new JSONArray();
        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
        ReportTemplate templte = null;
        JSONObject json = null;
        int size = reportTemplates == null ? 0 : reportTemplates.size();
        for (int i = 0; i < size; i++) {
            templte = reportTemplates.get(i);

            JSONArray rtsArray = new JSONArray();
            List<ReportTask> rts = reportTaskService.getReportTaskListByTemplateId(templte.getTemplateId());
            if (rts.size() == 0) {
                continue;
            }
            for (int j = 0; j < rts.size(); j++) {
                JSONObject json1 = new JSONObject();
                json1.put("text", rts.get(j).getTaskName());
                json1.put("leaf", true);
                json1.put("id", rts.get(j).getTaskId());
                rtsArray.add(json1);
            }

            json = new JSONObject();
            json.put("text", ReportTaskUtil.getString(templte.getDisplayName(), "report"));
            json.put("expanded", true);
            json.put("leaf", false);
            json.put("id", "tempId-" + templte.getTemplateId());
            json.put("children", rtsArray);
            json.put("name", templte.getName());
            if (templte.getSuperiorId() == -1) {
                json.put("iconCls", "reportTaskIcon");
            } else {
                json.put("icon", BASE_NODE_PATH.concat(templte.getIcon16()).concat(".png"));
            }
            map.put(String.valueOf(templte.getTemplateId()), json);
            JSONObject parent = map.get(String.valueOf(templte.getSuperiorId()));
            if (parent == null) {
                jsonReportTaskTreeData.add(json);
            } else {
                parent.getJSONArray("children").add(json);
            }
        }
        return jsonReportTaskTreeData;
    }

    @Override
    public void updateReportDisplay(List<ReportTemplate> reportTemplates) {
        reportTemplateDao.updateReportDisplay(reportTemplates);
    }

}
