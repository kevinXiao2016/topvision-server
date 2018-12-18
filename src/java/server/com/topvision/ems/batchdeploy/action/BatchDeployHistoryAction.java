/***********************************************************************
 * $Id: BatchDeployHistoryAction.java,v1.0 2013年12月17日 上午11:27:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.batchdeploy.domain.BatchDeployRecord;
import com.topvision.ems.batchdeploy.service.BatchDeployRecordService;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.util.ZetaUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2013年12月17日-上午11:27:09
 *
 */
@Controller("batchDeployHistoryAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchDeployHistoryAction extends BaseAction {
    private static final long serialVersionUID = -3037462449054920689L;
    @Autowired
    private BatchDeployRecordService batchDeployRecordService;
    private Long entityId;
    private Integer typeId;
    private Integer batchDeployId;
    private String st;
    private String et;
    private String operator;
    private String entityName;
    private String recordTime;

    /**
     * 跳转到历史记录查询页面，通用页面
     * 
     * @return
     */
    public String showBatchHistory() {
        return SUCCESS;
    }

    /**
     * 批量配置历史记录查询
     * 
     * @return
     * @throws IOException
     */
    public String queryForBatchHistory() throws IOException {
        Map<String, Object> queryCondition = new HashMap<>();
        if (entityId != null && entityId != -1) {
            queryCondition.put("entityId", entityId);
        }
        if (typeId != null && typeId != -1) {
            queryCondition.put("typeId", typeId);
        }
        if (operator != null) {
            queryCondition.put("operator", operator);
        }
        if (st != null) {
            queryCondition.put("st", st);
            queryCondition.put("et", et);
        }
        queryCondition.put("start", start);
        queryCondition.put("limit", limit);
        List<BatchDeployRecord> records = batchDeployRecordService.loadRecords(queryCondition);
        int rowCount = batchDeployRecordService.loadRecordCount(queryCondition);
        JSONObject json = new JSONObject();
        json.put("data", records);
        json.put("count", rowCount);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 展示成功的目标列表
     * 
     * @return
     * @throws IOException
     */
    public String loadSuccess() throws IOException {
        JSONArray json = new JSONArray();
        BatchDeployRecord record = batchDeployRecordService.loadSuccess(batchDeployId);
        List<? extends BatchRecordSupport> successlist = record.getSuccess();
        for (BatchRecordSupport success : successlist) {
            JSONObject o = new JSONObject();
            o.put("target", success.getItem());
            json.add(o);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 展示失败的目标列表
     * 
     * @return
     * @throws IOException
     */
    public String loadFailures() throws IOException {
        JSONArray json = new JSONArray();
        BatchDeployRecord record = batchDeployRecordService.loadFailures(batchDeployId);
        List<? extends BatchRecordSupport> failures = record.getFailures();
        for (BatchRecordSupport failure : failures) {
            JSONObject o = new JSONObject();
            o.put("target", failure.getItem());
            String $reason = ZetaUtil.getStaticString("reason-" + failure.getReason(), "BATCHDEPLOY");
            o.put("reason", $reason);
            json.add(o);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 展示失败的配置对象
     * 
     * @return
     */
    public String showBatchDeployFault() {
        return SUCCESS;
    }

    /**
     * 展示成功的配置对象
     * 
     * @return
     */
    public String showBatchDeploySuccess() {
        return SUCCESS;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getBatchDeployId() {
        return batchDeployId;
    }

    public void setBatchDeployId(Integer batchDeployId) {
        this.batchDeployId = batchDeployId;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

}
