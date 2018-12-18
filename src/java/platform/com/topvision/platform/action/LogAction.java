package com.topvision.platform.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.service.OperationService;
import com.topvision.platform.service.SystemLogService;

@Controller("logAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogAction extends BaseAction {
    private static final long serialVersionUID = -7244523580053723498L;
    @Autowired
    private SystemLogService systemLogService;
    @Autowired
    private OperationService operationService;

    private String entityIp;
    private String clientIp;
    private String status;
    private String operName;
    private int start;
    private int limit;
    private JSONArray entityIpList = new JSONArray();
    private JSONArray userList = new JSONArray();
    private JSONArray clientIpList = new JSONArray();

    /**
     * 载入日志列表
     * 
     * @return
     * @throws Exception
     */
    public String loadLogList() throws Exception {
        List<SystemLog> userLogList = new ArrayList<SystemLog>();
        List<SystemLog> userLogAllList = new ArrayList<SystemLog>();
        userLogList = systemLogService.getUserLogByParams(start, limit);
        userLogAllList = systemLogService.getUserLogCounts();
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("rowCount", userLogAllList.size());
        json.put("data", userLogList);
		writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 跳转操作日志页面
     * 
     * @return
     * @throws IOException
     */
    public String showOperationList() throws IOException {
        List<String> entityIpStrings = operationService.getOperationLogEntityIp();
        List<String> clientStrings = operationService.getOperationLogClientIp();
        List<String> userStrings = operationService.getOperationLogUserName();
        entityIpList = JSONArray.fromObject(entityIpStrings);
        clientIpList = JSONArray.fromObject(clientStrings);
        userList = JSONArray.fromObject(userStrings);
        return SUCCESS;
    }

    /**
     * 获取设备操作日志记录
     * 
     * @return
     */
    public String loadOperationLogList() throws Exception {
        List<OperationLog> operLogList = new ArrayList<OperationLog>();
        List<OperationLog> operLogAllList = new ArrayList<OperationLog>();
        operLogList = operationService.getOperationLogByParams(entityIp, clientIp, operName, status, start, limit);
        // operLogAllList = operationService.getOperationLogByParams(entityIp, operName, status,0,
        // 100);
        operLogAllList = operationService.getOperationLogByParamsCounts(entityIp, clientIp, operName, status);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("totalProperty", operLogAllList.size());
        json.put("data", operLogList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public void setSystemLogService(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    public OperationService getOperationService() {
        return operationService;
    }

    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public SystemLogService getSystemLogService() {
        return systemLogService;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public JSONArray getEntityIpList() {
        return entityIpList;
    }

    public void setEntityIpList(JSONArray entityIpList) {
        this.entityIpList = entityIpList;
    }

    public JSONArray getUserList() {
        return userList;
    }

    public void setUserList(JSONArray userList) {
        this.userList = userList;
    }

    public JSONArray getClientIpList() {
        return clientIpList;
    }

    public void setClientIpList(JSONArray clientIpList) {
        this.clientIpList = clientIpList;
    }

} 
