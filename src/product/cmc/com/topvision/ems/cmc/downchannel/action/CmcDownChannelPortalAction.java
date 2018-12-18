/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.highcharts.HighChartsUtils;
import com.topvision.platform.util.highcharts.domain.Highcharts;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcDownChannelPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcDownChannelPortalAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcDownChannelPortalAction.class);
    @Autowired
    private CmcChannelService cmcChannelService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Autowired
    private UserPreferencesService userPreferencesService;

    private CmcAttribute cmcAttribute;
    private Entity entity;
    private Long entityId;
    private Long cmcId;
    private Long cmcPortId;

    private JSONObject chartParam;
    private JSONObject cmcAttrJson;
    private JSONObject errorRateChartParam;
    private JSONObject channelUtilizationChartParam;
    private JSONObject channelCmNumChartParam;
    private JSONObject viewerParam;
    private String nodePath;
    private String timeType;
    private String perfType;
    private Long index;
    private String st;
    private String et;
    private CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo;
    private String channelTypeString;
    private Integer cmNumOnline;
    private Integer cmNumOffline;
    private String cmcDeviceNode;
    //用于保存用户自定义的下行信道视图
    private Long cmcType;
    private String downChannelLeft;
    private String downChannelRight;

    /**
     * 显示cmc下行端口快照
     * 
     * @return String
     */
    public String showDownChannelPortal() {
        channelTypeString = "DS";

        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        cmcDownChannelBaseShowInfo = cmcDownChannelService.getDownChannelBaseShowInfo(cmcAttribute, cmcPortId);

        timeType = ViewerParam.TODAY;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        viewerParam = JSONObject.fromObject(getPerfViewerParam());
        entity = entityService.getEntity(entityId);
        nodePath = entity.getDisplayName();
        cmcDeviceNode = cmcAttribute.getTopCcmtsSysMacAddr();

        // 下行用户数信息
        @SuppressWarnings("deprecation")
        List<ChannelCmNum> channelCmNumList = cmcChannelService.getChannelCmNumList(cmcId);
        for (ChannelCmNum aChannelCmNumList : channelCmNumList) {
            Long cmcPort = aChannelCmNumList.getCmcPortId();

            if (cmcPort != null && cmcPort.equals(cmcPortId)) {
                cmNumOnline = aChannelCmNumList.getCmNumOnline();
                cmNumOffline = aChannelCmNumList.getCmNumOffline();
            }
        }

        // 通道利用率chart
        Highcharts channelUtilizationhighcharts = HighChartsUtils.createDefaultLineXdateTimeChart(
                "channelUtilizationHis", ResourcesUtil.getString("CCMTS.todayChannelUtilizationGraph"),
                ResourcesUtil.getString("CCMTS.channelUtilization") + "(%)", null, 300);
        channelUtilizationhighcharts.getChart().setMarginRight(50);
        channelUtilizationhighcharts.getCredits().setEnabled(false);
        channelUtilizationhighcharts.getyAxis().get(0).setMax(100D);
        channelUtilizationhighcharts.getyAxis().get(0).setMin(0D);
        channelUtilizationhighcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
        channelUtilizationhighcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
        channelUtilizationChartParam = HighChartsUtils.toJSONObject(channelUtilizationhighcharts);

        // 在线用户数chart
        Highcharts channelCmNumhighcharts = HighChartsUtils.createDefaultLineXdateTimeChart("channelCmNumActiveHis",
                ResourcesUtil.getString("CCMTS.todayChannelUserGraph"),
                ResourcesUtil.getString("CCMTS.channelUserCount"), null, 300);
        channelCmNumhighcharts.getChart().setMarginRight(50);
        channelCmNumhighcharts.getCredits().setEnabled(false);
        channelCmNumhighcharts.getyAxis().get(0).setMax(100D);
        channelCmNumhighcharts.getyAxis().get(0).setMin(0D);
        channelCmNumhighcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
        channelCmNumhighcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
        channelCmNumChartParam = HighChartsUtils.toJSONObject(channelCmNumhighcharts);
        //获取用户自定义的下行信道视图
        cmcType = cmcAttribute.getCmcDeviceStyle();
        downChannelLeft = this.getDownChannelView(cmcType).getProperty("downChannelLeft");
        downChannelRight = this.getDownChannelView(cmcType).getProperty("downChannelRight");
        return SUCCESS;
    }

    /**
     * 取得portal中用到的下行信道基本信息
     * 
     * @return String
     */
    public String getDownChannelInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = null;
        try {
            cmcDownChannelBaseShowInfoList = cmcDownChannelService.getDownChannelBaseShowInfoList(cmcId);
        } catch (Exception ex) {
            logger.debug("", ex);
        }
        json.put("data", cmcDownChannelBaseShowInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public ViewerParam getPerfViewerParam() {
        ViewerParam viewerParam = new ViewerParam();
        viewerParam.setPerfType(perfType);
        viewerParam.setEntityId(entityId);
        viewerParam.setCmcId(cmcId);
        viewerParam.setIndex(index);
        if (ViewerParam.CUSTOM.equalsIgnoreCase(timeType)) {
            viewerParam.setEt(et);
            viewerParam.setSt(st);
        }
        viewerParam.setTimeType(timeType);
        return viewerParam;
    }

    /**
     * 保存用户设置的上行信道页面视图
     * @author flackyang
     * @since 2013-11-08
     * @return
     */
    public String saveDownChannelView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties downChannelView = new Properties();
        downChannelView.setProperty("downChannelLeft", downChannelLeft);
        downChannelView.setProperty("downChannelRight", downChannelRight);
        userPreferencesService.batchSaveModulePreferences(Long.toString(cmcType), uc.getUserId(), downChannelView);
        return NONE;
    }

    /**
     * 获取用户保存的上行信道页面视图
     * @author flackyang
     * @since 2013-11-08
     * @param typeId
     * @return
     */
    private Properties getDownChannelView(long typeId) {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule(Long.toString(typeId));
        userPre.setUserId(uc.getUserId());
        Properties downChannelView = userPreferencesService.getModulePreferences(userPre);
        return downChannelView;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public JSONObject getChartParam() {
        return chartParam;
    }

    public void setChartParam(JSONObject chartParam) {
        this.chartParam = chartParam;
    }

    public JSONObject getCmcAttrJson() {
        return cmcAttrJson;
    }

    public void setCmcAttrJson(JSONObject cmcAttrJson) {
        this.cmcAttrJson = cmcAttrJson;
    }

    public JSONObject getErrorRateChartParam() {
        return errorRateChartParam;
    }

    public void setErrorRateChartParam(JSONObject errorRateChartParam) {
        this.errorRateChartParam = errorRateChartParam;
    }

    public JSONObject getChannelUtilizationChartParam() {
        return channelUtilizationChartParam;
    }

    public void setChannelUtilizationChartParam(JSONObject channelUtilizationChartParam) {
        this.channelUtilizationChartParam = channelUtilizationChartParam;
    }

    public JSONObject getChannelCmNumChartParam() {
        return channelCmNumChartParam;
    }

    public void setChannelCmNumChartParam(JSONObject channelCmNumChartParam) {
        this.channelCmNumChartParam = channelCmNumChartParam;
    }

    public JSONObject getViewerParam() {
        return viewerParam;
    }

    public void setViewerParam(JSONObject viewerParam) {
        this.viewerParam = viewerParam;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getPerfType() {
        return perfType;
    }

    public void setPerfType(String perfType) {
        this.perfType = perfType;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
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

    public CmcDownChannelBaseShowInfo getCmcDownChannelBaseShowInfo() {
        return cmcDownChannelBaseShowInfo;
    }

    public void setCmcDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo) {
        this.cmcDownChannelBaseShowInfo = cmcDownChannelBaseShowInfo;
    }

    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    public String getChannelTypeString() {
        return channelTypeString;
    }

    public void setChannelTypeString(String channelTypeString) {
        this.channelTypeString = channelTypeString;
    }

    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    public Integer getCmNumOffline() {
        return cmNumOffline;
    }

    public void setCmNumOffline(Integer cmNumOffline) {
        this.cmNumOffline = cmNumOffline;
    }

    public String getCmcDeviceNode() {
        return cmcDeviceNode;
    }

    public void setCmcDeviceNode(String cmcDeviceNode) {
        this.cmcDeviceNode = cmcDeviceNode;
    }

    public Logger getLogger() {
        return logger;
    }

    public Long getCmcType() {
        return cmcType;
    }

    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }

    public String getDownChannelLeft() {
        return downChannelLeft;
    }

    public void setDownChannelLeft(String downChannelLeft) {
        this.downChannelLeft = downChannelLeft;
    }

    public String getDownChannelRight() {
        return downChannelRight;
    }

    public void setDownChannelRight(String downChannelRight) {
        this.downChannelRight = downChannelRight;
    }

}
