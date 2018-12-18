/***********************************************************************
 * $Id: CmActions.java,v1.0 2011-7-7 上午10:54:07 $
 *
 * @author: zhanglongyang
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.action;

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

import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONObject;

/**
 * 基础功能
 * 
 * @author zhanglongyang
 * @created @2011-7-7-上午10:54:07
 * 
 */
@Controller("cmAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "cmService")
    private CmService cmService;
    private int start;
    private int limit;
    private Long cmId;
    private Long entityId;
    private Long deniedNum;
    private Long registeredNum;
    protected Integer operationResult;
    private String cmIp;
    private String cmMac;
    private String source;
    private String readCommunity;
    private String writeCommunity;
    private CmAttribute cmAttribute;
    private ArrayList<String> cmsArrayIp;// 批量重启的ip参数
    private ArrayList<String> cmsArrayId;// 批量重启的id参数
    private JSONObject cmFunctionJson;
    private CmTopologyInfo cmTopologyInfo;

    /**
     * 刷新单条CM信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmAction", operationName = "Refesh CM${source}")
    public String refreshCmInfo() {
        entityId = cmService.getTopologyInfo(cmId).getEntityId();
        source = cmIp;
        try {
            cmAttribute = cmService.refreshCmInfo(cmId);
            // add by fanzidong ,格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String macRule = uc.getMacDisplayStyle();
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
            cmAttribute.setStatusMacAddress(formatedMac);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        }
        writeDataToAjax(JSONObject.fromObject(cmAttribute));
        return NONE;
    }

    /**
     * 获取cm上下线行为数据
     * 
     * @return
     */
    public String loadCmActionInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);
        map.put("entityId", cmAttribute.getEntityId());
        map.put("cmMac", new MacUtils(cmAttribute.getStatusMacAddress()).longValue());
        List<CmAct> cmActList = cmService.getCmActionInfo(map);
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CmAct cmAct : cmActList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAct.getCmmacString(), displayRule);
            cmAct.setCmmacString(formatedMac);
        }
        Map<String, Object> json = new HashMap<String, Object>();
        Integer size = cmActList.size();
        json.put("rowCount", size);
        json.put("data", cmActList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 重启CM
     * 
     * @return String
     */
    // @OperationLogProperty(actionName = "cmAction", operationName = "resetCM${source}")
    public String resetCm() {
        Map<String, Object> json = new HashMap<String, Object>();
        CmAttribute cm = cmService.getCmAttributeByCmId(cmId);
        entityId = cmService.getTopologyInfo(cmId).getEntityId();
        String ip = cm.getStatusIpAddress();
        if (ip == null || ip.equalsIgnoreCase("") || ip.equalsIgnoreCase("noSuchObject")) {
            ip = cm.getStatusInetAddress();
        }
        source = ip;
        int result = cmService.resetCm(cmId);
        switch (result) {
        case 1:
            json.put("message", "failure");
            json.put("reason", "offline");
            break;
        case 2:
            json.put("message", "failure");
            json.put("reason", "PingException");
            break;
        case 3:
            json.put("message", "failure");
            json.put("reason", "SnmpException");
            break;
        default:
            json.put("message", "success");
        }
        logger.info("cmid " + cmId + " cmip " + ip);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取CM关联的服务流
     * 
     * @return String
     */
    public String getCmServiceFlowList() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 通过cmMac查询该cm关联的服务流列表
        List<CmcQosServiceFlowInfo> SFlist = cmService.getCmServiceFlowListInfo(cmMac);
        // add by fanzidong,需要在展示之前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmcQosServiceFlowInfo flowInfo : SFlist) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(flowInfo.getStatusMacAddress(), macRule);
            flowInfo.setStatusMacAddress(formatedMac);
        }
        json.put("data", SFlist);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public String getCmIp() {
        return cmIp;
    }

    public CmTopologyInfo getCmTopologyInfo() {
        return cmTopologyInfo;
    }

    public void setCmTopologyInfo(CmTopologyInfo cmTopologyInfo) {
        this.cmTopologyInfo = cmTopologyInfo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getRegisteredNum() {
        return registeredNum;
    }

    public void setRegisteredNum(Long registeredNum) {
        this.registeredNum = registeredNum;
    }

    public Long getDeniedNum() {
        return deniedNum;
    }

    public void setDeniedNum(Long deniedNum) {
        this.deniedNum = deniedNum;
    }

    public ArrayList<String> getCmsArrayIp() {
        return cmsArrayIp;
    }

    public void setCmsArrayIp(ArrayList<String> cmsArrayIp) {
        this.cmsArrayIp = cmsArrayIp;
    }

    public ArrayList<String> getCmsArrayId() {
        return cmsArrayId;
    }

    public void setCmsArrayId(ArrayList<String> cmsArrayId) {
        this.cmsArrayId = cmsArrayId;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public JSONObject getCmFunctionJson() {
        return cmFunctionJson;
    }

    public void setCmFunctionJson(JSONObject cmFunctionJson) {
        this.cmFunctionJson = cmFunctionJson;
    }

    public CmAttribute getCmAttribute() {
        return cmAttribute;
    }

    public void setCmAttribute(CmAttribute cmAttribute) {
        this.cmAttribute = cmAttribute;
    }
}
