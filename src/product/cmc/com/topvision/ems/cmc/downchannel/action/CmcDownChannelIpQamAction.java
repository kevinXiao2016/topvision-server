/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamISInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamMappings;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamOSInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamStatusInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcIpqamInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcDownChannelIpQamAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcDownChannelIpQamAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcDownChannelIpQamAction.class);
    @Autowired
    private CmcService cmcService;
    private Long cmcId;
    private String cmcIp;// 8800B
    private Long entityId;

    @Autowired
    private CmcDownChannelService cmcDownChannelService;

    private CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo;

    private CmcDSIpqamBaseInfo cc8800bHttpDownChannelIPQAM;
    private String cc8800bHttpDSIpqamMappingListStr;
    private CmcDSIpqamMappings cc8800BIpqamMappings;
    private CmcIpqamInfo cmcIpqamInfo;
    private Boolean isDocsisInfChanged;
    private Boolean isIPQAMInfChanged;
    private Integer channelId;
    private Long channelIndex;
    private Long cmcPortId;
    private Integer channelAdminstatus;
    private Long mappingId;
    private Integer action;
    private String pidMapNum;
    private String ipqamPidMapString;
    private String ipqamStreamType;
    private String source;
    private Integer productType;
    private Integer operationResult;

    /**
     * 显示ipqam信息页面
     * 
     * @return
     */
    public String showCmcIPQamList() {
        SnmpParam snmpParam = cmcService.getSnmpParamByCmcId(cmcId);
        cmcIp = snmpParam.getIpAddress();
        cmcIpqamInfo = cmcDownChannelService.queryCCIpqamIpInfo(cmcId);

        return SUCCESS;
    }

    /**
     * 显示ipqam节目流信息
     * 
     * @return
     */
    public String showProgramList() {
        SnmpParam snmpParam = cmcService.getSnmpParamByCmcId(cmcId);
        cmcIp = snmpParam.getIpAddress();
        return SUCCESS;
    }

    /**
     * 获取下行信道IPQAM基本信息列表
     * 
     * @return String
     */
    public String getDownStreamIpqamBaseInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDSIpqamBaseInfo> cc8800bHttpDownChannelIPQAMs = cmcDownChannelService
                .getDownChannelIPQAMInfoList(cmcId);
        json.put("data", cc8800bHttpDownChannelIPQAMs);

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取IPQAM信道状态信息列表
     * 
     * @return String
     */
    public String showCCIpqamOutPutStatusList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDSIpqamStatusInfo> ipQamStautsList = cmcDownChannelService.queryCCIpqamOutPutStatusList(cmcId);
        json.put("data", ipQamStautsList);

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取IPQAM的IP地址类信息
     * 
     * @return
     */
    public String getCCIpqamIpInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        CmcIpqamInfo cmcIpqamInfo = cmcDownChannelService.queryCCIpqamIpInfo(cmcId);

        json.put("data", cmcIpqamInfo);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 修改IPQAM的IP类信息
     * 
     * @return
     */
    public String modifyCCIpqamIpInfo() {
        String message;
        try {
            cmcDownChannelService.modifyCCIpqamIpInfo(cmcIpqamInfo, cmcId);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取IPQAM信道映射信息列表
     * 
     * @return String
     */
    public String showIpqamStreamMapList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDSIpqamMappings> ipqamMappingsList = cmcDownChannelService.queryIpqamStreamMappingsList(cmcId);
        json.put("data", ipqamMappingsList);

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 展示IPQAM配置页面 根据action（1表示删除，2表示修改，3表示增加）获取或返回相应数据
     * 
     * @return
     */
    public String showIpQamConfigInfo() {
        cc8800BIpqamMappings = new CmcDSIpqamMappings();
        if (action == 3) {
            cc8800BIpqamMappings.setCmcId(cmcId);
            cc8800BIpqamMappings.setIpqamOutputQAMChannel(channelId);
        } else {
            cc8800BIpqamMappings = cmcDownChannelService.queryIpqamStreamMappingsById(mappingId);
        }
        Map<String, Object> statusJson = new HashMap<String, Object>();
        Map<String, Object> mappingsJson = new HashMap<String, Object>();
        Map<String, Object> mappingsListJson = new HashMap<String, Object>();
        List<CmcDSIpqamStatusInfo> ipQamStatusList = cmcDownChannelService.queryCCIpqamOutPutStatusList(cmcId);
        List<CmcDSIpqamMappings> mappingsList = cmcDownChannelService.queryIpqamStreamMappingsList(cmcId);
        statusJson.put("data", ipQamStatusList);
        mappingsListJson.put("data", mappingsList);
        mappingsJson.put("data", cc8800BIpqamMappings);

        request.setAttribute("ipQamStatusList", JSONObject.fromObject(statusJson));
        request.setAttribute("ipQamMappingsList", JSONObject.fromObject(mappingsListJson));
        request.setAttribute("ipQamMappingsObj", JSONObject.fromObject(mappingsJson));
        return SUCCESS;
    }

    /**
     * 设置（修改，删除，增加） 1表示删除，2表示修改，3表示增加
     * 
     * @return
     */

    public String modifyIPQAMStreamMapList() {
        String message;
        List<CmcDSIpqamMappings> mappingsList = new ArrayList<CmcDSIpqamMappings>();
        try {
            if (action == 1) {// 删除+批量
                // cc8800bHttpDSIpqamMappingStr.s
                String[] cc8800bHttpDSIpqamMappingStr = cc8800bHttpDSIpqamMappingListStr.split(";");
                for (int i = 0; i < cc8800bHttpDSIpqamMappingStr.length; i++) {
                    String str = cc8800bHttpDSIpqamMappingStr[i];
                    JSONObject js = JSONObject.fromObject(str);
                    CmcDSIpqamMappings ipqamMappings = new CmcDSIpqamMappings();
                    ipqamMappings.setIpqamPidMapString(js.getString("ipqamPidMapString"));
                    ipqamMappings.setIpqamOutputQAMChannel(js.getInt("ipqamOutputQAMChannel"));
                    ipqamMappings.setIpqamDestinationIPAddress(js.getString("ipqamDestinationIPAddress"));
                    ipqamMappings.setIpqamOldUDPPort(js.getInt("ipqamUDPPort"));// 删除时，udpport与oldudpport一样
                    ipqamMappings.setIpqamUDPPort(js.getInt("ipqamUDPPort"));
                    ipqamMappings.setIpqamActive(js.getInt("ipqamActive"));
                    ipqamMappings.setIpqamStreamType(js.getInt("ipqamStreamType"));
                    ipqamMappings.setIpqamProgramNumberInput(js.getInt("ipqamProgramNumberInput"));
                    ipqamMappings.setIpqamProgramNumberOutput(js.getInt("ipqamProgramNumberOutput"));
                    ipqamMappings.setIpqamPMV(js.getInt("ipqamPMV"));
                    ipqamMappings.setIpqamDataRateEnable(js.getInt("ipqamDataRateEnable"));
                    ipqamMappings.setIpqamDataRate(js.getInt("ipqamDataRate"));

                    mappingsList.add(ipqamMappings);
                }

            } else {// 增加+修改
                mappingsList.add(cc8800BIpqamMappings);
            }

            cmcDownChannelService.modifyIpqamMappingsList(cmcId, mappingsList, action);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);

        return NONE;
    }

    /**
     * 弹出PID修改或查看页面
     * 
     * @return
     */
    public String ipqamPidMapModify() {

        request.setAttribute("pidMapNum", pidMapNum);
        request.setAttribute("ipqamPidMapString", ipqamPidMapString);
        request.setAttribute("ipqamStreamType", ipqamStreamType);

        return SUCCESS;

    }

    /**
     * 获取IPQAM输入节目流信息
     * 
     * @return
     */
    public String showIpqamInputStreamInfoList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDSIpqamISInfo> isList = cmcDownChannelService.showIpqamInputStreamInfoList(cmcId);
        json.put("data", isList);

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取IPQAM输出节目流信息
     * 
     * @return
     */
    public String showIpqamOutputStreamInfoList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDSIpqamOSInfo> osList = cmcDownChannelService.showIpqamOutputStreamInfoList(cmcId);

        json.put("data", osList);

        writeDataToAjax(JSONObject.fromObject(json));

        return NONE;
    }

    /**
     * 刷新IPQAM状态信息及映射信息
     * 
     * @return
     */
    public String refreshIpQamMappingsStatus() {
        String message;

        try {
            cmcDownChannelService.refreshIpQamMappingsStatus(cmcId);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);

        return NONE;
    }

    /**
     * 刷新IPQAM输入输出节目流信息
     * 
     * @return
     */
    public String refreshIpqamStreamInfoList() {
        String message;
        try {
            cmcDownChannelService.refreshIpqamStreamInfoList(cmcId);
            message = "true";

        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);

        return NONE;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * @return the cc8800bHttpDownChannelIPQAM
     */
    public CmcDSIpqamBaseInfo getCc8800bHttpDownChannelIPQAM() {
        return cc8800bHttpDownChannelIPQAM;
    }

    /**
     * @param cc8800bHttpDownChannelIPQAM
     *            the cc8800bHttpDownChannelIPQAM to set
     */
    public void setCc8800bHttpDownChannelIPQAM(CmcDSIpqamBaseInfo cc8800bHttpDownChannelIPQAM) {
        this.cc8800bHttpDownChannelIPQAM = cc8800bHttpDownChannelIPQAM;
    }

    /**
     * @return the cc8800bHttpDSIpqamMappingListStr
     */
    public String getCc8800bHttpDSIpqamMappingListStr() {
        return cc8800bHttpDSIpqamMappingListStr;
    }

    /**
     * @param cc8800bHttpDSIpqamMappingListStr
     *            the cc8800bHttpDSIpqamMappingListStr to set
     */
    public void setCc8800bHttpDSIpqamMappingListStr(String cc8800bHttpDSIpqamMappingListStr) {
        this.cc8800bHttpDSIpqamMappingListStr = cc8800bHttpDSIpqamMappingListStr;
    }

    /**
     * @return the cc8800BIpqamMappings
     */
    public CmcDSIpqamMappings getCc8800BIpqamMappings() {
        return cc8800BIpqamMappings;
    }

    /**
     * @param cc8800bIpqamMappings
     *            the cc8800BIpqamMappings to set
     */
    public void setCc8800BIpqamMappings(CmcDSIpqamMappings cc8800bIpqamMappings) {
        cc8800BIpqamMappings = cc8800bIpqamMappings;
    }

    /**
     * @return the isDocsisInfChanged
     */
    public Boolean getIsDocsisInfChanged() {
        return isDocsisInfChanged;
    }

    /**
     * @param isDocsisInfChanged
     *            the isDocsisInfChanged to set
     */
    public void setIsDocsisInfChanged(Boolean isDocsisInfChanged) {
        this.isDocsisInfChanged = isDocsisInfChanged;
    }

    /**
     * @return the isIPQAMInfChanged
     */
    public Boolean getIsIPQAMInfChanged() {
        return isIPQAMInfChanged;
    }

    /**
     * @param isIPQAMInfChanged
     *            the isIPQAMInfChanged to set
     */
    public void setIsIPQAMInfChanged(Boolean isIPQAMInfChanged) {
        this.isIPQAMInfChanged = isIPQAMInfChanged;
    }

    /**
     * @return the channelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            the channelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    /**
     * @return the channelAdminstatus
     */
    public Integer getChannelAdminstatus() {
        return channelAdminstatus;
    }

    /**
     * @param channelAdminstatus
     *            the channelAdminstatus to set
     */
    public void setChannelAdminstatus(Integer channelAdminstatus) {
        this.channelAdminstatus = channelAdminstatus;
    }

    /**
     * @return the mappingId
     */
    public Long getMappingId() {
        return mappingId;
    }

    /**
     * @param mappingId
     *            the mappingId to set
     */
    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    /**
     * @return the action
     */
    public Integer getAction() {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(Integer action) {
        this.action = action;
    }

    /**
     * @return the pidMapNum
     */
    public String getPidMapNum() {
        return pidMapNum;
    }

    /**
     * @param pidMapNum
     *            the pidMapNum to set
     */
    public void setPidMapNum(String pidMapNum) {
        this.pidMapNum = pidMapNum;
    }

    /**
     * @return the ipqamPidMapString
     */
    public String getIpqamPidMapString() {
        return ipqamPidMapString;
    }

    /**
     * @param ipqamPidMapString
     *            the ipqamPidMapString to set
     */
    public void setIpqamPidMapString(String ipqamPidMapString) {
        this.ipqamPidMapString = ipqamPidMapString;
    }

    /**
     * @return the ipqamStreamType
     */
    public String getIpqamStreamType() {
        return ipqamStreamType;
    }

    /**
     * @param ipqamStreamType
     *            the ipqamStreamType to set
     */
    public void setIpqamStreamType(String ipqamStreamType) {
        this.ipqamStreamType = ipqamStreamType;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcDownChannelBaseShowInfo
     */
    public CmcDownChannelBaseShowInfo getCmcDownChannelBaseShowInfo() {
        return cmcDownChannelBaseShowInfo;
    }

    /**
     * @param cmcDownChannelBaseShowInfo
     *            the cmcDownChannelBaseShowInfo to set
     */
    public void setCmcDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo) {
        this.cmcDownChannelBaseShowInfo = cmcDownChannelBaseShowInfo;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the productType
     */
    public Integer getProductType() {
        return productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    /**
     * @return the operationResult
     */
    public Integer getOperationResult() {
        return operationResult;
    }

    /**
     * @param operationResult
     *            the operationResult to set
     */
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * @return the cmcIpqamInfo
     */
    public CmcIpqamInfo getCmcIpqamInfo() {
        return cmcIpqamInfo;
    }

    /**
     * @param cmcIpqamInfo
     *            the cmcIpqamInfo to set
     */
    public void setCmcIpqamInfo(CmcIpqamInfo cmcIpqamInfo) {
        this.cmcIpqamInfo = cmcIpqamInfo;
    }

} 
