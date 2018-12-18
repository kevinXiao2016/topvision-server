/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

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
import com.topvision.ems.cmc.perf.service.CmcPerfCommonService;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.PerfTargetConstants;
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
@Controller("cmcUpChannelPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcUpChannelPortalAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcUpChannelPortalAction.class);
    @Autowired
    private CmcChannelService cmcChannelService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcPerfService cmcPerfService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Resource(name = "cmcPerfCommonService")
    private CmcPerfCommonService perfCommonService;

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
    private String channelTypeString;
    private CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo;
    private UsBitErrorRate bitErrorRate;
    private Integer cmNumOnline;
    private Integer cmNumOffline;
    private String cmcDeviceNode;
    //用于保存用户自定义的上行信道页面视图
    private Long cmcType;
    private String upChannelLeft;
    private String upChannelRight;

    /**
     * 显示cmc上行端口快照
     * 
     * @return String
     */
    public String showUpChannelPortal() {
        channelTypeString = "US";
        // 上行信道基本信息
        cmcUpChannelBaseShowInfo = cmcUpChannelService.getUpChannelBaseShowInfo(cmcPortId);

        // 上行用户数信息
        /*@SuppressWarnings("deprecation")
        List<ChannelCmNum> channelCmNumList = cmcChannelService.getChannelCmNumList(cmcId);
        for (ChannelCmNum aChannelCmNumList : channelCmNumList) {
            if (aChannelCmNumList.getCmcPortId().equals(cmcPortId)) {
                cmNumOnline = aChannelCmNumList.getCmNumOnline();
                cmNumOffline = aChannelCmNumList.getCmNumOffline();
            }
        }*/
        //从usernum表里获取信道cmNum实时数据
        ChannelCmNum channelCmNum = cmcPerfService.getChannelCmNum(cmcPortId);
        if (channelCmNum != null) {
            if (channelCmNum.getCmNumOnline() != null) {
                cmNumOnline = channelCmNum.getCmNumOnline();
            }
            if (channelCmNum.getCmNumOffline() != null) {
                cmNumOffline = channelCmNum.getCmNumOffline();
            }
        }

        bitErrorRate = cmcPerfService.getErrorCodesByPortId(cmcId, cmcPortId, PerfTargetConstants.CCER);
        UsBitErrorRate unBitErrorRate = cmcPerfService
                .getErrorCodesByPortId(cmcId, cmcPortId, PerfTargetConstants.UCER);
        if (unBitErrorRate != null) {
            bitErrorRate.setUcerRate(unBitErrorRate.getUcerRate());
        }
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        // 性能图信息
        timeType = ViewerParam.TODAY;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        viewerParam = JSONObject.fromObject(getPerfViewerParam());
        entity = entityService.getEntity(entityId);
        nodePath = entity.getDisplayName();
        cmcDeviceNode = cmcAttribute.getTopCcmtsSysMacAddr();

        // 信噪比chart
        Highcharts highcharts = HighChartsUtils.createDefaultLineXdateTimeChart("noiseHis",
                ResourcesUtil.getString("CCMTS.todayChannelNoiceGraph"), ResourcesUtil.getString("CCMTS.channelNoice")
                        + "(dB)", null, 300);
        highcharts.getChart().setMarginRight(50);
        highcharts.getCredits().setEnabled(false);
        highcharts.getyAxis().get(0).setMax(50D);
        highcharts.getyAxis().get(0).setMin(0D);
        highcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
        highcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
        chartParam = HighChartsUtils.toJSONObject(highcharts);

        // 误码率chart
        // CCMTS.todayChannelUtilizationGraph
        Highcharts errorRatehighcharts = HighChartsUtils.createDefaultLineXdateTimeChart("noiseHis",
                ResourcesUtil.getString("CCMTS.todayChannelErrorGateGraph"), ResourcesUtil
                        .getString("CCMTS.channelErrorGate" + "%"), null, 300);
        errorRatehighcharts.getChart().setMarginRight(50);
        errorRatehighcharts.getCredits().setEnabled(false);
        errorRatehighcharts.getyAxis().get(0).setMax(50D);
        errorRatehighcharts.getyAxis().get(0).setMin(0D);
        errorRatehighcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
        errorRatehighcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
        errorRateChartParam = HighChartsUtils.toJSONObject(errorRatehighcharts);

        // 通道利用率chart
        //
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
        //获取用户设置的上行信道页面视图
        cmcType = cmcAttribute.getCmcDeviceStyle();
        upChannelLeft = this.getUpChannelView(cmcType).getProperty("upChannelLeft");
        upChannelRight = this.getUpChannelView(cmcType).getProperty("upChannelRight");
        return SUCCESS;
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
     * 取得portal中用到的上行信道基本信息
     * 
     * @return String
     */
    public String getUpChannelInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = null;
        try {
            cmcUpChannelBaseShowInfoList = cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId);

            List<CmcSignalQuality> sigQList = perfCommonService.fetchCmcSignalQuality(cmcId);
            Map<Long, Long> temp = new HashMap<Long, Long>();
            for (CmcSignalQuality signal : sigQList) {
                Long channalIndex = signal.getCmcChannelIndex();
                temp.put(channalIndex, signal.getDocsIfSigQSignalNoise());
            }
            for (CmcUpChannelBaseShowInfo up : cmcUpChannelBaseShowInfoList) {
                Long channalIndex = up.getChannelIndex();
                Long noise = temp.get(channalIndex);
                if (noise == null) {
                    noise = 0L;
                }
                up.setDocsIfSigQSignalNoise(noise);
            }

        } catch (Exception ex) {
            logger.debug("", ex);
        }
        json.put("data", cmcUpChannelBaseShowInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 取得portal中用到的上行信道用户数统计
     * 
     * @return String
     */
    public String getUpChannelUserNum() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<ChannelCmNum> channelCmNumList = null;
        try {
            channelCmNumList = cmcChannelService.getChannelCmNumListNew(cmcId, 0);
            Iterator<ChannelCmNum> it = channelCmNumList.iterator();
            while (it.hasNext()) {
                if (it.next().getChannelType() != 0) {
                    it.remove();
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        }
        json.put("data", channelCmNumList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 取得portal中用到的下行信道用户数统计
     * 
     * @return String
     */
    public String getDownChannelUserNum() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<ChannelCmNum> channelCmNumList = null;
        try {
            channelCmNumList = cmcChannelService.getChannelCmNumListNew(cmcId, 1);
            Iterator<ChannelCmNum> it = channelCmNumList.iterator();
            while (it.hasNext()) {
                if (it.next().getChannelType() != 1) {
                    it.remove();
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        }
        json.put("data", channelCmNumList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 保存用户设置的上行信道页面视图
     * @author flackyang
     * @since 2013-11-08
     * @return
     */
    public String saveUpChannelView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties upChannelView = new Properties();
        upChannelView.setProperty("upChannelLeft", upChannelLeft);
        upChannelView.setProperty("upChannelRight", upChannelRight);
        userPreferencesService.batchSaveModulePreferences(Long.toString(cmcType), uc.getUserId(), upChannelView);
        return NONE;
    }

    /**
     * 获取用户保存的上行信道页面视图
     * @author flackyang
     * @since 2013-11-08
     * @param typeId
     * @return
     */
    private Properties getUpChannelView(long typeId) {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule(Long.toString(typeId));
        userPre.setUserId(uc.getUserId());
        Properties upChannelView = userPreferencesService.getModulePreferences(userPre);
        return upChannelView;
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

    public String getChannelTypeString() {
        return channelTypeString;
    }

    public void setChannelTypeString(String channelTypeString) {
        this.channelTypeString = channelTypeString;
    }

    public CmcUpChannelBaseShowInfo getCmcUpChannelBaseShowInfo() {
        return cmcUpChannelBaseShowInfo;
    }

    public void setCmcUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        this.cmcUpChannelBaseShowInfo = cmcUpChannelBaseShowInfo;
    }

    public UsBitErrorRate getBitErrorRate() {
        return bitErrorRate;
    }

    public void setBitErrorRate(UsBitErrorRate bitErrorRate) {
        this.bitErrorRate = bitErrorRate;
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

    public String getUpChannelLeft() {
        return upChannelLeft;
    }

    public void setUpChannelLeft(String upChannelLeft) {
        this.upChannelLeft = upChannelLeft;
    }

    public String getUpChannelRight() {
        return upChannelRight;
    }

    public void setUpChannelRight(String upChannelRight) {
        this.upChannelRight = upChannelRight;
    }

}
