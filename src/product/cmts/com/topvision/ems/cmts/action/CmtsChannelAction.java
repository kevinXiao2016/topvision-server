/***********************************************************************
 * $Id: CmtsChannelAction.java,v1.0 2013-8-8 上午11:48:45 $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUsSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmtsModulationEntry;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.ems.cmts.service.CmtsChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.util.ZetaUtil;

/**
 * @author loyal
 * @created @2013-8-8-上午11:48:45
 * 
 */
@Controller("cmtsChannelAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsChannelAction extends BaseAction {
    private static final long serialVersionUID = 6934052920159979656L;
    private final Logger logger = LoggerFactory.getLogger(CmtsChannelAction.class);
    @Resource(name = "cmtsChannelService")
    private CmtsChannelService cmtsChannelService;
    @Resource(name = "cmcChannelService")
    private CmcChannelService cmcChannelService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    private Long cmcId;
    private CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo;
    private CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo;
    private Long cmcPortId;
    private JSONArray channelListObject = new JSONArray();
    private JSONArray cmtsModTypeList = new JSONArray();
    private Long adminStatusUpNum;
    private Integer productType = 40000;

    /**
     * 修改CMTS下行信道信息
     * 
     * @return
     */
    public String modifyDownStreamBaseInfo() {
        Map<String, String> message = new HashMap<String, String>();
        String result = "";
        Long channelIndex = cmcChannelService.getChannelIndexByPortId(cmcPortId);
        cmcDownChannelBaseShowInfo.setChannelIndex(channelIndex);
        cmcDownChannelBaseShowInfo.setCmcId(cmcId);
        cmcDownChannelBaseShowInfo.setCmcPortId(cmcPortId);
        try {
            cmtsChannelService.modifyDownChannelBaseShowInfo(cmcDownChannelBaseShowInfo);
            result = "success";
        } catch (Exception e) {
            result = "error";
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 修改CMTS上行信道信息
     * 
     * @return
     */
    public String modifyUpStreamBaseInfo() {
        Map<String, String> message = new HashMap<String, String>();
        String result = "";
        Long channelIndex = cmcChannelService.getChannelIndexByPortId(cmcPortId);
        cmcUpChannelBaseShowInfo.setChannelIndex(channelIndex);
        cmcUpChannelBaseShowInfo.setCmcId(cmcId);
        cmcUpChannelBaseShowInfo.setCmcPortId(cmcPortId);
        try {
            cmtsChannelService.modifyUpChannelBaseShowInfo(cmcUpChannelBaseShowInfo);
            result = "success";
        } catch (Exception e) {
            result = "error";
        }
        cmcUpChannelService.refreshUpChannelBaseInfo(cmcId, productType, null);
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 显示上行信道列表tab页面
     * 
     * @return String
     */
    public String showUpChannelList() {
        return SUCCESS;
    }

    /**
     * 显示下行信道列表tab页面
     * 
     * @return String
     */
    public String showDownChannelList() {
        return SUCCESS;
    }

    /**
     * 显示上联口tab页面
     * 
     * @return String
     */
    public String showUpLinkPortList() {
        return SUCCESS;
    }

    /**
     * 获取上行信道基本信息列表
     * 
     * @return String
     */
    public String getUpStreamBaseInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmtsChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        /*Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();*/
        for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
            /*String ifDescr = cmcUpChannelBaseShowInfo.getIfDescr();
            cmcUpChannelBaseShowInfo.setIfDescr(CmtsUtils.getCmtsUpChannelIndex(typeId, ifDescr, entityTypeService));*/
            cmcUpChannelBaseShowInfo.setDocsIfUpChannelModulationProfileName(cmcUpChannelBaseShowInfo
                    .getCmtsChannelModulationProfile()
                    + "("
                    + getUpChannelModulatiionProfileName(cmcUpChannelBaseShowInfo.getChannelModulationProfile()) + ")");
        }
        json.put("data", cmcUpChannelBaseShowInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    private String getUpChannelModulatiionProfileName(Long channelModulationProfile) {
        /*
         * Value list: 1: other(1) 2: qpsk(2) 3: qam16(3) 4: qam8(4) 5: qam32(5) 6: qam64(6) 7:
         * qam128(7)
         */
        switch (channelModulationProfile.intValue()) {
        case 1:
            return "other";
        case 2:
            return "qpsk";
        case 3:
            return "qam16";
        case 4:
            return "qam8";
        case 5:
            return "qam32";
        case 6:
            return "qam64";
        case 7:
            return "qam128";
        default:
            return "other";
        }
    }

    /**
     * 获取下行信道基本信息列表
     * 
     * @return String
     */
    public String getDownStreamBaseInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        /*Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();*/
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmcId);
        /*for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
            String ifDescr = cmcDownChannelBaseShowInfo.getIfDescr();
            cmcDownChannelBaseShowInfo
                    .setIfDescr(CmtsUtils.getCmtsDownChannelIndex(typeId, ifDescr, entityTypeService));
        }*/
        json.put("data", cmcDownChannelBaseShowInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取下行信道基本信息列表
     * 
     * @return String
     */
    public String getUpLinkPortList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmtsUpLinkPort> cmtsUpLinkPort = cmtsChannelService.getUpLinkPortList(cmcId);
        // add by fanzidong
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmtsUpLinkPort upLinkPort : cmtsUpLinkPort) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(upLinkPort.getIfPhysAddress(), macRule);
            upLinkPort.setIfPhysAddress(formatedMac);
        }
        json.put("data", cmtsUpLinkPort);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 刷新cmts端口信息
     * 
     * @return
     */
    public String refreshCmtsPorts() {
        String message;
        try {
            cmcChannelService.refreshCmtsPorts(cmcId);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示上行信道修改基本信息页面
     * 
     * @return String
     */
    public String showUpStreamBaseInfoConfig() {
        cmcUpChannelBaseShowInfo = cmcUpChannelService.getUpChannelBaseShowInfo(cmcPortId);
        List<CmtsModulationEntry> docsIfCmtsModTypeList = cmcUpChannelService.getDocsIfCmtsModTypeList(cmcId);
        /*Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();
        String ifDescr = cmcUpChannelBaseShowInfo.getIfDescr();
        cmcUpChannelBaseShowInfo.setIfDescr(CmtsUtils.getCmtsUpChannelIndex(typeId, ifDescr, entityTypeService));*/
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelListByPortId(cmcPortId);
        channelListObject = JSONArray.fromObject(cmcUpChannelBaseShowInfoList);
        cmtsModTypeList = JSONArray.fromObject(docsIfCmtsModTypeList);
        return SUCCESS;
    }

    /**
     * 显示下行信道修改基本信息页面
     * 
     * @return String
     */
    public String showDownStreamConfigInfo() {
        cmcDownChannelBaseShowInfo = cmcDownChannelService.getDownChannelBaseShowInfo(null, cmcPortId);
        /*Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();
        String ifDescr = cmcDownChannelBaseShowInfo.getIfDescr();
        if (ifDescr != null && entityTypeService.isUbrCmts(typeId)) {
            cmcDownChannelBaseShowInfo
                    .setIfDescr(CmtsUtils.getCmtsDownChannelIndex(typeId, ifDescr, entityTypeService));
        } else if (ifDescr != null) {
            cmcDownChannelBaseShowInfo
                    .setIfDescr(CmtsUtils.getCmtsDownChannelIndex(typeId, ifDescr, entityTypeService));
        }*/
        adminStatusUpNum = cmcDownChannelService.getDownChannelAdminStatusUpNum(cmcId);
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelListByPortId(cmcPortId);
        channelListObject = JSONArray.fromObject(cmcDownChannelBaseShowInfoList);
        return SUCCESS;
    }

    /**
     * 获取上行信道质量信息列表
     * 
     * @return String
     * @throws IOException 
     */
    public String getUpStreamQualityInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUsSignalQualityInfo> cmcUsSignalQualityInfos = cmcUpChannelService.getUsSignalQualityInfoList(cmcId);
       /* Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();
        for (CmcUsSignalQualityInfo cmcUsSignalQualityInfo : cmcUsSignalQualityInfos) {
            String ifDescr = cmcUsSignalQualityInfo.getIfDescr();
            cmcUsSignalQualityInfo.setIfDescr(CmtsUtils.getCmtsUpChannelIndex(typeId, ifDescr, entityTypeService));
        }*/
        json.put("data", cmcUsSignalQualityInfos);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 取得portal中用到的下行信道基本信息
     * 
     * @return String
     */
    public String getDownChannelInfo() {
        StringBuilder sb = new StringBuilder();
        Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();
        sb.append("<table id='getDownChannelInfo' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        // 端口名称
        sb.append(ResourcesUtil.getString("WorkBench.PortName"));
        sb.append("</th><Th width='100'>");
        // 中心频率
        sb.append(ResourcesUtil.getString("CM.middleFrequency"));
        sb.append("</th><Th width='100'>");
        // 发射电频
        sb.append(ResourcesUtil.getString("CM.channelPower"));
        sb.append("</th><Th width='100'>");
        // 调制方式
        sb.append(ResourcesUtil.getString("CM.modStyle"));
        sb.append("</th></tr></thead><tbody>");
        try {
            @SuppressWarnings("unused")
            int i = 0;
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(cmcId);
            if (cmcDownChannelBaseShowInfoList != null && cmcDownChannelBaseShowInfoList.size() > 0) {
                this.sortChannelInfo(cmcDownChannelBaseShowInfoList, typeId);
                for (CmcDownChannelBaseShowInfo downChannelInfo : cmcDownChannelBaseShowInfoList) {
                    /*String ifDescr = downChannelInfo.getIfDescr();
                    ifDescr = CmtsUtils.getCmtsDownChannelIndex(typeId, ifDescr, entityTypeService);*/
                    sb.append("<tr>");
                    sb.append("<Td align=center>");
                    sb.append(downChannelInfo.getIfName());
                    sb.append("</Td>");

                    sb.append("<Td align=center>");
                    sb.append(downChannelInfo.getDocsIfDownChannelFrequencyForunit());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append(downChannelInfo.getDocsIfDownChannelPowerForunit());
                    sb.append("</Td>");
                    sb.append("<Td align=center  style='padding-left:10px'>");
                    sb.append(downChannelInfo.getDocsIfDownChannelModulationName());
                    sb.append("</Td>");
                    sb.append("</tr>");
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 取得portal中用到的上行信道基本信息
     * 
     * @return String
     */
    public String getUpChannelInfo() {
        StringBuilder sb = new StringBuilder();
        Entity entity = entityService.getEntity(cmcId);
        Long typeId = entity.getTypeId();
        sb.append("<table id='getUpChannelInfo' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        // 端口名称
        sb.append(ResourcesUtil.getString("WorkBench.PortName"));
        sb.append("</th><th style=\"text-align:center\">");
        // 中心频率
        sb.append(ResourcesUtil.getString("CM.middleFrequency"));
        sb.append("</th><th style=\"text-align:center\">");
        // 频宽
        sb.append(ResourcesUtil.getString("CMC.label.bandwidth"));
        sb.append("</th><th style=\"text-align:center\">");
        // 信噪比
        sb.append(ResourcesUtil.getString("WorkBench.Noise"));
        sb.append("</th></tr>");
        try {
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmtsChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            if (cmcUpChannelBaseShowInfoList != null && cmcUpChannelBaseShowInfoList.size() > 0) {
                this.sortChannelInfo(cmcUpChannelBaseShowInfoList, typeId);
                for (CmcUpChannelBaseShowInfo upChannelInfo : cmcUpChannelBaseShowInfoList) {
                    /*String ifDescr = upChannelInfo.getIfDescr();
                    ifDescr = CmtsUtils.getCmtsUpChannelIndex(typeId, ifDescr, entityTypeService);*/
                    sb.append("<tr>");
                    sb.append("<Td align=center>");
                    sb.append(upChannelInfo.getIfName());
                    sb.append("</Td>");

                    sb.append("<Td align=right style='padding-right:80px'>");
                    sb.append(upChannelInfo.getDocsIfUpChannelFrequencyForunit());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append(upChannelInfo.getDocsIfUpChannelWidthForunit());
                    sb.append("</Td>");

                    sb.append("<Td align=right style='padding-right:80px'>");
                    sb.append(upChannelInfo.getDocsIfSigQSignalNoiseForunit());
                    sb.append("</Td>");

                    sb.append("</tr>");
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 根据上/下行信道的显示端口名称排序
     * 
     * @author YangYi
     * @param channelBaseShowInfoList
     * @param typeId
     * @return
     */
    private <T> void sortChannelInfo(List<T> channelBaseShowInfoList, Long typeId) {
        int size = channelBaseShowInfoList.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                T t1 = channelBaseShowInfoList.get(i);
                T t2 = channelBaseShowInfoList.get(j);
                String descrI = "";
                String descrJ = "";
                if (t1 instanceof CmcUpChannelBaseShowInfo) {
                    descrI = ((CmcUpChannelBaseShowInfo) t1).getChannelIndex().toString();
                } else if (t1 instanceof CmcDownChannelBaseShowInfo) {
                    descrI = ((CmcDownChannelBaseShowInfo) t1).getChannelIndex().toString();
                } else {
                    return;
                }
                if (t2 instanceof CmcUpChannelBaseShowInfo) {
                    descrJ = ((CmcUpChannelBaseShowInfo) t2).getChannelIndex().toString();
                } else if (t2 instanceof CmcDownChannelBaseShowInfo) {
                    descrJ = ((CmcDownChannelBaseShowInfo) t2).getChannelIndex().toString();
                } else {
                    return;
                }
                if (descrI.compareTo(descrJ) > 0) {
                    T temp = t1;
                    channelBaseShowInfoList.set(i, t2);
                    channelBaseShowInfoList.set(j, temp);
                }
            }
        }
    }

    /**
     * 取得portal中用到的端口列表信息
     * 
     * @return String
     */
    public String getCmtsPortInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id='getCmtsPortInfo' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        // 端口名称
        sb.append(ResourcesUtil.getString("WorkBench.PortName"));
        sb.append("</th><th style=\"text-align:center\">");
        // 实时速率
        sb.append(ResourcesUtil.getString("CCMTS.RTSpeed"));
        sb.append("</th><th style=\"text-align:center\">");
        // 带宽
        sb.append(ResourcesUtil.getString("topo.linkProperty.bandwidth"));
        sb.append("</th><th style=\"text-align:center\">");
        // 利用率
        sb.append(ResourcesUtil.getString("WorkBench.Utilization"));
        sb.append("</th></tr></thead><tbody>");
        try {
            List<ChannelPerfInfo> cmcChannelPerfInfoList = cmtsChannelService.getCmtsChannelPerfInfoList(cmcId);
            if (cmcChannelPerfInfoList != null && cmcChannelPerfInfoList.size() > 0) {
                for (ChannelPerfInfo cmcChannelPerfInfo : cmcChannelPerfInfoList) {
                    if (cmcChannelPerfInfo.getIfOperStatus().intValue() != 2) {
                        if (cmcChannelPerfInfo.getChannelType() == 0) {
                            sb.append("<tr>");
                            sb.append("<Td align=center>");
                            sb.append(cmcChannelPerfInfo.getIfName());
                            sb.append("</Td>");
                            sb.append("<Td align=center>");
                            sb.append(cmcChannelPerfInfo.getChannelOctetsRateStr());
                        } else if (cmcChannelPerfInfo.getChannelType() == 1) {
                            sb.append("<tr>");
                            sb.append("<Td align=center>");
                            sb.append(cmcChannelPerfInfo.getIfName());
                            sb.append("</Td>");
                            sb.append("<Td align=center>");
                            sb.append(cmcChannelPerfInfo.getChannelOctetsRateStr());
                        }
                        sb.append("</Td>");
                        sb.append("<Td align=center>");
                        sb.append(cmcChannelPerfInfo.getIfSpeedString());
                        sb.append("</Td>");
                        sb.append("<Td align=center>");
                        sb.append("<div id= 'channelUtilization'>");
                        sb.append("<div class='percentBarBg'><div class='percentBar' style='width:")
                                .append(cmcChannelPerfInfo.getChannelUtilizationString()).append("'>");
                        sb.append("<div class='percentBarLeftConner'></div>");
                        sb.append("</div>");
                        sb.append("<div class='percentBarTxt'><div class='noWidthCenterOuter clearBoth'><ol class='noWidthCenter leftFloatOl pT1'>");
                        // add by haojie
                        initPercentDiv(sb, cmcChannelPerfInfo.getChannelUtilizationString());
                        sb.append("<li><div class='percentNumPercent'></div></li>");
                        sb.append("</ol></div></div></div>");
                        sb.append("</div>");
                        sb.append("</Td>");
                        sb.append("</tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 初始化百分比显示
     * 
     * @param sb
     */
    private void initPercentDiv(StringBuilder sb, String usage) {
        String[] percentDivData = new String[10];
        boolean beforeDotFlag = true;
        for (int i = 0; i < usage.length() - 1; i++) {
            percentDivData[i] = usage.substring(i, i + 1);
            if (percentDivData[i].equals(".")) {
                sb.append("<li><div class='percentNumDot").append("'></div></li>");
                beforeDotFlag = !beforeDotFlag;
            } else {
                if (beforeDotFlag) {
                    sb.append("<li><div class='percentNum").append(percentDivData[i]).append("'></div></li>");
                } else {
                    sb.append("<li><div class='miniPercentNum").append(percentDivData[i]).append("'></div></li>");
                }
            }
        }
    }

    /**
     * 取得portal中用到的上行信道用户数统计
     * 
     * @return String
     */
    public String getUpChannelUserNum() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id='getUpChannelUserNum' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        // 端口名称
        sb.append(ResourcesUtil.getString("WorkBench.PortName"));
        sb.append("</th><th style=\"text-align:center\">");
        // 总数
        sb.append(ResourcesUtil.getString("CHANNEL.total"));
        sb.append("</th><th style=\"text-align:center\">");
        // 在线
        sb.append(ResourcesUtil.getString("CCMTS.status.4"));
        sb.append("</th><th style=\"text-align:center\">");
        // 上线中
        sb.append(ResourcesUtil.getString("CHANNEL.onlining"));
        sb.append("</th><th style=\"text-align:center\">");
        // 下线
        sb.append(ResourcesUtil.getString("CMC.label.offline"));
        sb.append("</th><th style=\"text-align:center\">");
        // CM明细列表
        sb.append(ResourcesUtil.getString("CM.cmList"));
        sb.append("</th></tr>");
        try {
            List<ChannelCmNum> channelCmNumList = cmcChannelService.getChannelCmNumListNew(cmcId, 0);
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            Map<Long, String> map = new HashMap<Long, String>();
            for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
                map.put(cmcUpChannelBaseShowInfo.getChannelIndex(), cmcUpChannelBaseShowInfo.getIfDescr());
            }
            if (channelCmNumList != null && channelCmNumList.size() > 0) {
                for (ChannelCmNum channelCmNum : channelCmNumList) {
                    if (channelCmNum.getChannelType() == 0) {
                        sb.append("<tr>");
                        sb.append("<Td align=center>");
                        sb.append(cmcUpChannelBaseShowInfo.getIfName());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumTotal());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumOnline());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumActive());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumOffline());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append("<a href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(");
                        sb.append(channelCmNum.getChannelIndex());
                        sb.append(",");
                        sb.append(channelCmNum.getEntityId());
                        sb.append(",");
                        sb.append(channelCmNum.getChannelType());
                        sb.append(");'>" + ZetaUtil.getStaticString("CMC/COMMON.view") + "</a>");
                        sb.append("</Td></tr>");
                    }
                }
            }

        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 取得portal中用到的下行信道用户数统计
     * 
     * @return String
     */
    public String getDownChannelUserNum() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id='getDownChannelUserNum' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        // 端口名称
        sb.append(ResourcesUtil.getString("WorkBench.PortName"));
        sb.append("</th><th style=\"text-align:center\">");
        // 总数
        sb.append(ResourcesUtil.getString("CHANNEL.total"));
        sb.append("</th><th style=\"text-align:center\">");
        // 在线
        sb.append(ResourcesUtil.getString("CCMTS.status.4"));
        sb.append("</th><th style=\"text-align:center\">");
        // 上线中
        sb.append(ResourcesUtil.getString("CHANNEL.onlining"));
        sb.append("</th><th style=\"text-align:center\">");
        // 下线
        sb.append(ResourcesUtil.getString("CMC.label.offline"));
        sb.append("</th><th style=\"text-align:center\">");
        // CM明细列表
        sb.append(ResourcesUtil.getString("CM.cmList"));
        sb.append("</th></tr>");
        try {
            List<ChannelCmNum> channelCmNumList = cmcChannelService.getChannelCmNumListNew(cmcId, 1);
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(cmcId);
            Map<Long, String> map = new HashMap<Long, String>();
            for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
                map.put(cmcDownChannelBaseShowInfo.getChannelIndex(), cmcDownChannelBaseShowInfo.getIfDescr());
            }
            if (channelCmNumList != null && channelCmNumList.size() > 0) {
                for (ChannelCmNum channelCmNum : channelCmNumList) {
                    if (channelCmNum.getChannelType() == 1) {
                        sb.append("<tr>");
                        sb.append("<Td align=center>");
                        sb.append(cmcDownChannelBaseShowInfo.getIfName());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumTotal());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumOnline());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumActive());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(channelCmNum.getCmNumOffline());
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append("<a  href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(");
                        sb.append(channelCmNum.getChannelIndex());
                        sb.append(",");
                        sb.append(channelCmNum.getEntityId());
                        sb.append(",");
                        sb.append(channelCmNum.getChannelType());
                        sb.append(");'>" + ZetaUtil.getStaticString("CMC/COMMON.view") + "</a>");
                        sb.append("</Td></tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public CmtsChannelService getCmtsChannelService() {
        return cmtsChannelService;
    }

    public void setCmtsChannelService(CmtsChannelService cmtsChannelService) {
        this.cmtsChannelService = cmtsChannelService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    public CmcUpChannelBaseShowInfo getCmcUpChannelBaseShowInfo() {
        return cmcUpChannelBaseShowInfo;
    }

    public void setCmcUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        this.cmcUpChannelBaseShowInfo = cmcUpChannelBaseShowInfo;
    }

    public CmcDownChannelBaseShowInfo getCmcDownChannelBaseShowInfo() {
        return cmcDownChannelBaseShowInfo;
    }

    public void setCmcDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo) {
        this.cmcDownChannelBaseShowInfo = cmcDownChannelBaseShowInfo;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public JSONArray getChannelListObject() {
        return channelListObject;
    }

    public void setChannelListObject(JSONArray channelListObject) {
        this.channelListObject = channelListObject;
    }

    public Long getAdminStatusUpNum() {
        return adminStatusUpNum;
    }

    public void setAdminStatusUpNum(Long adminStatusUpNum) {
        this.adminStatusUpNum = adminStatusUpNum;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public JSONArray getCmtsModTypeList() {
        return cmtsModTypeList;
    }

    public void setCmtsModTypeList(JSONArray cmtsModTypeList) {
        this.cmtsModTypeList = cmtsModTypeList;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

}
