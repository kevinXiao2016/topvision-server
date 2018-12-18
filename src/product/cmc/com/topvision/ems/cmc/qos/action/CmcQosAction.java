/***********************************************************************
 * $Id: QosAction.java,v1.0 2011-12-8 上午10:28:30 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceClassInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosDynamicServiceStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceClass;
import com.topvision.ems.cmc.qos.service.CmcQosService;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * 服务流相关功能
 * 
 * @author loyal
 * @created @2011-12-8-上午10:28:30
 * 
 */
@Controller("cmcQosAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcQosAction extends BaseAction {
    private static final long serialVersionUID = 4846331244733488600L;
    private Logger logger = LoggerFactory.getLogger(CmcQosAction.class);
    @Resource(name = "cmcQosService")
    private CmcQosService cmcQosService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "ccmtsCmListService")
    private CcmtsCmListService ccmtsCmListService;
    private CmcQosDynamicServiceStats cmcUpQosDynamicServiceStats;
    private CmcQosDynamicServiceStats cmcDownQosDynamicServiceStats;
    private CmcQosServiceFlowStats upServiceFlowStats;
    private CmcQosServiceFlowStats downServiceFlowStats;
    private JSONArray cmAttributeObject = new JSONArray();
    private JSONArray cmcQosParamSetObject = new JSONArray();
    private JSONArray cmcQosPktClassObject = new JSONArray();
    private int start;
    private int limit;
    private Long cmcId;
    private Long serviceFlowId;
    private Integer direction;
    private Long scId;
    private String cmMac;
    private String setValueMessage;
    private CmcQosServiceClass cmcQosServiceClass;
    @Resource(name = "cmService")
    private CmService cmService;

    /**
     * 刷新服务流基本信息/状态信息/包分类器/参数集/所属CM
     *
     * @return String
     */
    public String refreshServiceFlow() {
        String message;
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        try {
            ccmtsCmListService.refreshContactedCmList(cmcId, cmcIndex);
            cmcQosService.refreshServiceFlowBaseInfoOnCC(cmcId, cmcIndex);
            cmcQosService.refreshServiceFlowParamSetOnCC(cmcId, cmcIndex);
            cmcQosService.refreshCmMacToServiceFlows(cmcId);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示QoS服务流信息
     *
     * @return String
     */
    public String showCmcQosServiceFlowInfo() {
        // 获取上下行动态服务流统计
        cmcUpQosDynamicServiceStats = cmcQosService.getQosUpDynamicServiceStatsInfo(cmcId);
        cmcDownQosDynamicServiceStats = cmcQosService.getQosDownDynamicServiceStatsInfo(cmcId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("time", new Date().getTime());
        // TODO 获取服务流数量统计值，在私有MIB提出前使用
        upServiceFlowStats = cmcQosService.getUpQosServiceFlowStatsInfo(map);
        downServiceFlowStats = cmcQosService.getDownQosServiceFlowStatsInfo(map);
        return SUCCESS;
    }

    /**
     * 显示Qos服务流页面
     *
     * @return String
     */
    public String showQosServiceStreamInfo() {
        return SUCCESS;
    }

    /**
     * 显示Qos配置文件列表
     *
     * @return String
     */
    public String showQosConfigFiles() {
        return SUCCESS;
    }

    /**
     * 获取所有服务流信息
     *
     * @return String
     */
    public String getCmcQosServiceFlowListInfo() {
        // 获得服务流数据，根据过滤条件进行过滤
        Map<String, String> serviceMap = new HashMap<String, String>();
        Map<String, Object> json = new HashMap<String, Object>();
        if (direction != null && (direction == 1 || direction == 2)) {
            serviceMap.put("direction", direction.toString());
        }
        if (cmcId != null) {
            serviceMap.put("cmcId", cmcId.toString());
        }
        if (!StringUtils.isEmpty(cmMac)) {
            serviceMap.put("cmMac", CmcUtil.turnToMacType(cmMac));
        }
        List<CmcQosServiceFlowInfo> cmcQosServiceFlowInfo = cmcQosService.getCmcQosServiceFlowListInfoWithCondition(
                serviceMap, start, limit);
        int size = cmcQosService.getCmcQosServiceFlowListNumWithCondition(serviceMap);
        json.put("rowCount", size);
        //add by fanzidong,需要对需要展示的MAC地址进行格式化
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmcQosServiceFlowInfo flowInfo : cmcQosServiceFlowInfo) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(flowInfo.getStatusMacAddress(), macRule);
            flowInfo.setStatusMacAddress(formatedMac);
        }
        json.put("data", cmcQosServiceFlowInfo);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取服务流参数集弹出页面
     *
     * @return String
     */
    public String showQosParamSetInfo() {
        List<CmcQosParamSetInfo> cmcQosParamSetInfoList = cmcQosService.getQosParamSetInfo(serviceFlowId);
        cmcQosParamSetObject = JSONArray.fromObject(cmcQosParamSetInfoList);
        return SUCCESS;

    }

    /**
     * 获取服务流参数集列表
     *
     * @return String
     */
    public String getQosParamSetInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcQosParamSetInfo> cmcQosParamSetInfo = cmcQosService.getQosParamSetInfo(serviceFlowId);
        json.put("data", cmcQosParamSetInfo);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取包分类器
     *
     * @return String
     */
    public String getQosPktClassInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcQosPktClassInfo> cmcQosPktClassInfoList = cmcQosService.getQosPktClassInfo(serviceFlowId);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmcQosPktClassInfo classInfo : cmcQosPktClassInfoList) {
            String formatedDestMac = MacUtils.convertMacToDisplayFormat(classInfo.getClassDestMacAddr(), macRule);
            classInfo.setClassDestMacAddr(formatedDestMac);
            String formatedSourceMac = MacUtils.convertMacToDisplayFormat(classInfo.getClassSourceMacAddr(), macRule);
            classInfo.setClassSourceMacAddr(formatedSourceMac);
        }
        json.put("data", cmcQosPktClassInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 返回包分类器弹出页面
     *
     * @return String
     */
    public String showQosPktClassInfo() {
        List<CmcQosPktClassInfo> cmcQosPktClassInfoList = cmcQosService.getQosPktClassInfo(serviceFlowId);
        cmcQosPktClassObject = JSONArray.fromObject(cmcQosPktClassInfoList);
        return SUCCESS;

    }

    /**
     * 返回服务流关联的CM的弹出页面
     *
     * @return String
     */
    public String showQosAssociatedCmInfo() {
        CmAttribute cmAttribute = cmcQosService.getServiceFlowConnectedCm(serviceFlowId);
        //add by fanzidong,格式化mac地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
        cmAttribute.setStatusMacAddress(formatedMac);
        if (cmAttribute != null) {
            cmAttributeObject = JSONArray.fromObject(cmAttribute);
        }
        return SUCCESS;
    }

    /**
     * 获得服务流关联的CM信息
     *刘占山增加废弃注解，若发现此方法有效，请删除注解。20130411
     * @return String
     */
    @Deprecated
    public String getQosAssociatedCmInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        CmAttribute cmAttribute = cmcQosService.getServiceFlowConnectedCm(serviceFlowId);
        json.put("data", cmAttribute);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 返回上行ServiceClass的配置页面
     *
     * @return String
     */
    public String showUpServiceClassConfigInfo() {
        cmcQosServiceClass = cmcQosService.getQosServiceClassInfo(scId);
        return SUCCESS;
    }

    /**
     * 返回下行ServiceClass的配置页面
     *
     * @return String
     */
    public String showDownServiceClassConfigInfo() {
        cmcQosServiceClass = cmcQosService.getQosServiceClassInfo(scId);
        return SUCCESS;
    }

    /**
     * 返回增加ServiceClass页面
     *
     * @return String
     */
    public String showCreateServiceClass() {
        return SUCCESS;
    }

    /**
     * 添加或修改服务流模板
     *
     * @return String
     */
    public String createOrModifyServiceClassInfo() {
        Map<String, String> message = new HashMap<String, String>();
        cmcQosServiceClass = new CmcQosServiceClass();
        if (scId != null) {
            cmcQosServiceClass.setScId(scId);
        }
        try {
            cmcQosService.createOrModifyServiceClassInfo(cmcQosServiceClass, cmcId);
        } catch (SetValueFailException e) {
            setValueMessage = e.getMessage();
            logger.debug("", e);
        } finally {
            message.put("message", setValueMessage);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 删除服务流模板
     *
     * @return String
     */
    public String deleteServiceClass() {
        cmcQosService.deleteServiceClassInfo(scId, cmcId);
        return NONE;
    }

    /**
     * ServiceClass
     * 
     * @return String
     */
    public String getServiceClassList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcQosServiceClassInfo> cmcQosServiceClassInfo = cmcQosService.getQosServiceClassList(cmcId);
        json.put("rowCount", cmcQosServiceClassInfo.size());
        json.put("data", cmcQosServiceClassInfo);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public CmcQosDynamicServiceStats getCmcUpQosDynamicServiceStats() {
        return cmcUpQosDynamicServiceStats;
    }

    public void setCmcUpQosDynamicServiceStats(CmcQosDynamicServiceStats cmcUpQosDynamicServiceStats) {
        this.cmcUpQosDynamicServiceStats = cmcUpQosDynamicServiceStats;
    }

    public JSONArray getCmcQosPktClassObject() {
        return cmcQosPktClassObject;
    }

    public void setCmcQosPktClassObject(JSONArray cmcQosPktClassObject) {
        this.cmcQosPktClassObject = cmcQosPktClassObject;
    }

    public CmcQosDynamicServiceStats getCmcDownQosDynamicServiceStats() {
        return cmcDownQosDynamicServiceStats;
    }

    public void setCmcDownQosDynamicServiceStats(CmcQosDynamicServiceStats cmcDownQosDynamicServiceStats) {
        this.cmcDownQosDynamicServiceStats = cmcDownQosDynamicServiceStats;
    }

    public CmcQosServiceFlowStats getUpServiceFlowStats() {
        return upServiceFlowStats;
    }

    public void setUpServiceFlowStats(CmcQosServiceFlowStats upServiceFlowStats) {
        this.upServiceFlowStats = upServiceFlowStats;
    }

    public CmcQosServiceFlowStats getDownServiceFlowStats() {
        return downServiceFlowStats;
    }

    public void setDownServiceFlowStats(CmcQosServiceFlowStats downServiceFlowStats) {
        this.downServiceFlowStats = downServiceFlowStats;
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

    public Long getServiceFlowId() {
        return serviceFlowId;
    }

    public void setServiceFlowId(Long serviceFlowId) {
        this.serviceFlowId = serviceFlowId;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public CmcQosService getCmcQosService() {
        return cmcQosService;
    }

    public void setCmcQosService(CmcQosService cmcQosService) {
        this.cmcQosService = cmcQosService;
    }

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public CmcQosServiceClass getCmcQosServiceClass() {
        return cmcQosServiceClass;
    }

    public void setCmcQosServiceClass(CmcQosServiceClass cmcQosServiceClass) {
        this.cmcQosServiceClass = cmcQosServiceClass;
    }

    public String getSetValueMessage() {
        return setValueMessage;
    }

    public void setSetValueMessage(String setValueMessage) {
        this.setValueMessage = setValueMessage;
    }

    public JSONArray getCmAttributeObject() {
        return cmAttributeObject;
    }

    public void setCmAttributeObject(JSONArray cmAttributeObject) {
        this.cmAttributeObject = cmAttributeObject;
    }

    public JSONArray getCmcQosParamSetObject() {
        return cmcQosParamSetObject;
    }

    public void setCmcQosParamSetObject(JSONArray cmcQosParamSetObject) {
        this.cmcQosParamSetObject = cmcQosParamSetObject;
    }

    public CmService getCmService() {
        return cmService;
    }

    public void setCmService(CmService cmService) {
        this.cmService = cmService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }
}
