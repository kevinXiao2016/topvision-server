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

import javax.annotation.Resource;

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
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.downchannel.domain.TxPowerLimit;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcDownChannelListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcDownChannelListAction extends BaseAction {
    private static final long serialVersionUID = -5347086165354662731L;
    private final Logger logger = LoggerFactory.getLogger(CmcDownChannelListAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcChannelService cmcChannelService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    private CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo;
    private JSONObject cmcDownChannelBaseShowInfoJson;
    private CmcDSIpqamBaseInfo cc8800bHttpDownChannelIPQAM;
    private CmcAttribute cmcAttribute;
    private Long entityId;
    private Long cmcId;
    private Long cmcPortId;
    private Integer channelAdminstatus;
    private Integer operationResult;
    private String source;
    private Integer productType;
    private JSONObject channelListObject = new JSONObject();
    private Long adminStatusUpNum;
    private String cmcIp;
    private JSONObject channelFunctionJson;
    private Boolean isDocsisInfChanged;
    private Boolean isIPQAMInfChanged;
    private Boolean isIpqamSupported = false;// 是否支持

    private JSONObject supportFunction = new JSONObject();

    private JSONObject powerLimit = new JSONObject();

    /**
     * 显示下行信道列表页面
     * 
     * @return String
     */
    public String showDownChannelList() {
        int maxIpqamCount = 0;
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        // 获取该设备的电平设置限制
        List<TxPowerLimit> powerLimitList = cmcDownChannelService.getDownChannelTxPowerLimt(cmcId, cmcAttribute.getCmcIndex());
        // 转换成map结构，方便查找
        for(TxPowerLimit _powerLimit : powerLimitList) {
            powerLimit.put(_powerLimit.getChannelNum(), _powerLimit);
        }
        if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())
                && cmcAttribute.compareVersion(CmcConstants.CMC_WEB_DOWNCHANNEL_VER_MIN) >= 0) {
            SnmpParam snmpParam = cmcService.getSnmpParamByCmcId(cmcId);
            cmcIp = snmpParam.getIpAddress();

            CmcFpgaSpecification fpga = cmcService.getFpgaSpecificationById(cmcId);
            if (fpga != null && fpga.getItemNum() != 0 && fpga.getIpqamChannelCount() > 0) {
                isIpqamSupported = true;
                maxIpqamCount = fpga.getIpqamChannelCount();
            }
            
            // 由数据库获取数据
            List<CmcDSIpqamBaseInfo> downChannelIPQAMList = cmcDownChannelService.getDownChannelIPQAMInfoList(cmcId);
            JSONObject js = new JSONObject();
            js.put("data", JSONArray.fromObject(downChannelIPQAMList));
            request.setAttribute("downChannelIPQAMList", js);
            request.setAttribute("maxIpqamCount", maxIpqamCount);
            return "ccmtsWeb";
        } else {
            return SUCCESS;
        }

    }

    /**
     * 获取下行信道基本信息列表
     * 
     * @return String
     */
    public String getDownStreamBaseInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmcId);
        json.put("data", cmcDownChannelBaseShowInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取下行信道IPQAM基本信息列表
     * 
     * @return String
     */
    public String getDownStreamIpqamBaseInfo() {
        // TODO 考虑位置
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDSIpqamBaseInfo> cc8800bHttpDownChannelIPQAMs = cmcDownChannelService
                .getDownChannelIPQAMInfoList(cmcId);
        json.put("data", cc8800bHttpDownChannelIPQAMs);

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取下行信道统计信息列表
     * 
     * @return String
     */
    public String getDownStreamStatisticInfo() {
        // TODO
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDownChannelStaticInfo> cmcDownChannelStaticInfoList = cmcDownChannelService
                .getDownChannelStaticInfoList(cmcId);
        json.put("data", cmcDownChannelStaticInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示下行信道修改基本信息页面
     * 
     * @return String
     */
    public String showDownStreamConfigInfo() {
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        // version = "V1.2.10.0";
        String channelInterleave = deviceVersionService.getParamValue(cmcId, "downChannel", "channelInterleave");
        supportFunction.put("channelInterleaveSelect", channelInterleave);
        String docsIfDownChannelWidth = deviceVersionService.getParamValue(cmcId, "downChannel", "docsIfDownChannelWidth");
        supportFunction.put("docsIfDownChannelWidth", docsIfDownChannelWidth);
        String annexMode = deviceVersionService.getParamValue(cmcId, "downChannel", "annexMode");
        supportFunction.put("annexMode", annexMode);
        cmcDownChannelBaseShowInfo = cmcDownChannelService.getDownChannelBaseShowInfo(cmc,cmcPortId);
        
        cmcDownChannelBaseShowInfoJson = JSONObject.fromObject(cmcDownChannelBaseShowInfo);
        
        adminStatusUpNum = cmcDownChannelService.getDownChannelAdminStatusUpNum(cmcId);

        CmcFpgaSpecification fpga = cmcService.getFpgaSpecificationById(cmcId);
        if (fpga != null && fpga.getItemNum() != 0 && fpga.getIpqamChannelCount() > 0) {
            isIpqamSupported = true;

        }
        Map<String, List<?>> channelMap = cmcChannelService.getDSChannelFrequencyListByPortId(cmcPortId, cmcId);
        channelListObject.put("upList", channelMap.get("upList"));
        channelListObject.put("downList", channelMap.get("downList"));
        return SUCCESS;
    }

    /**
     * 显示下行信道IPQAM修改信息页面
     * 
     * @return String
     */
    public String showDSIpqamConfigInfo() {
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        String channelInterleave = deviceVersionService.getParamValue(cmcId, "downChannel", "channelInterleave");
        supportFunction.put("channelInterleave", channelInterleave);
        String channelWidth = deviceVersionService.getParamValue(cmcId, "downChannel", "channelWidth");
        supportFunction.put("channelWidth", channelWidth);
        cmcDownChannelBaseShowInfo = cmcDownChannelService.getDownChannelBaseShowInfo(cmc, cmcPortId);
        cmcDownChannelBaseShowInfoJson = JSONObject.fromObject(cmcDownChannelBaseShowInfo);
        adminStatusUpNum = cmcDownChannelService.getDownChannelAdminStatusUpNum(cmcId);

        CmcFpgaSpecification fpga = cmcService.getFpgaSpecificationById(cmcId);
        if (fpga != null && fpga.getItemNum() != 0 && fpga.getIpqamChannelCount() > 0) {
            isIpqamSupported = true;
        }
        Map<String, List<?>> channelMap = cmcChannelService.getDSChannelFrequencyListByPortId(cmcPortId, cmcId);

        channelListObject.put("upList", channelMap.get("upList"));
        channelListObject.put("downList", channelMap.get("downList"));
        cc8800bHttpDownChannelIPQAM = cmcDownChannelService.getDownChannelIPQAMInfo(cmcPortId);

        return SUCCESS;
    }

    /**
     * 修改单条下行信道基本信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcChannelAction", operationName = "modifyDownStreamBaseInfo${source}")
    public String modifyDownStreamBaseInfo() {
        String result = null;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        Long channelIndex = cmcChannelService.getChannelIndexByPortId(cmcPortId);
        if (entityTypeService.isCcmtsWithAgent(cmcType.longValue())) {
            source = CmcIndexUtils.getChannelId(channelIndex).toString();
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(channelIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(channelIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getChannelId(channelIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        Map<String, String> message = new HashMap<String, String>();
        cmcDownChannelBaseShowInfo.setCmcId(cmcId);
        cmcDownChannelBaseShowInfo.setCmcPortId(cmcPortId);
        cmcDownChannelBaseShowInfo.setEntityId(entityId);
        try {
            if (isDocsisInfChanged != null && isDocsisInfChanged) {
                cmcDownChannelService.modifyDownChannelBaseShowInfo(cmcDownChannelBaseShowInfo, productType);
            }
            if (isIPQAMInfChanged != null && isIPQAMInfChanged && isIPQAMInfChanged
                    && cc8800bHttpDownChannelIPQAM != null) {
                cc8800bHttpDownChannelIPQAM.setDocsIfDownChannelId(CmcIndexUtils.getChannelId(channelIndex).intValue());
                cc8800bHttpDownChannelIPQAM
                        .setDocsIfDownChannelFrequency(cmcDownChannelBaseShowInfo.getDocsIfDownChannelFrequency());
                cc8800bHttpDownChannelIPQAM
                        .setDocsIfDownChannelPower(cmcDownChannelBaseShowInfo.getDocsIfDownChannelPower());
                cc8800bHttpDownChannelIPQAM
                        .setDocsIfDownChannelAnnex(cmcDownChannelBaseShowInfo.getDocsIfDownChannelAnnex());
                cc8800bHttpDownChannelIPQAM
                        .setDocsIfDownChannelInterleave(cmcDownChannelBaseShowInfo.getDocsIfDownChannelInterleave());
                cc8800bHttpDownChannelIPQAM
                        .setDocsIfDownChannelModulation(cmcDownChannelBaseShowInfo.getDocsIfDownChannelModulation());
                cc8800bHttpDownChannelIPQAM
                        .setDocsIfDownChannelWidth(cmcDownChannelBaseShowInfo.getDocsIfDownChannelWidth());

                cc8800bHttpDownChannelIPQAM.setIfAdminStatus(3);
                cmcDownChannelService.modifyChannelIpqamBaseInfo(cmcId, cc8800bHttpDownChannelIPQAM);
            }
            // cmcDownChannelService.modifyDownChannelBaseShowInfo(cmcDownChannelBaseShowInfo,
            // productType);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 刷新下行通道基本信息
     * 
     * @return String
     */
    public String refreshDownChannelBaseInfo() {
        String message;
        try {
            cmcDownChannelService.refreshDownChannelBaseInfo(cmcId, productType);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新下行通道统计信息
     * 
     * @return String
     */
    public String refreshDownChannelStaticInfo() {
        String message;
        try {
            cmcDownChannelService.refreshDownChannelStaticInfo(cmcId, productType);
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

    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    public CmcDownChannelBaseShowInfo getCmcDownChannelBaseShowInfo() {
        return cmcDownChannelBaseShowInfo;
    }

    public void setCmcDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo) {
        this.cmcDownChannelBaseShowInfo = cmcDownChannelBaseShowInfo;
    }

    public CmcDSIpqamBaseInfo getCc8800bHttpDownChannelIPQAM() {
        return cc8800bHttpDownChannelIPQAM;
    }

    public void setCc8800bHttpDownChannelIPQAM(CmcDSIpqamBaseInfo cc8800bHttpDownChannelIPQAM) {
        this.cc8800bHttpDownChannelIPQAM = cc8800bHttpDownChannelIPQAM;
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

    public Integer getChannelAdminstatus() {
        return channelAdminstatus;
    }

    public void setChannelAdminstatus(Integer channelAdminstatus) {
        this.channelAdminstatus = channelAdminstatus;
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

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    /**
     * @return the channelListObject
     */
    public JSONObject getChannelListObject() {
        return channelListObject;
    }

    /**
     * @param channelListObject
     *            the channelListObject to set
     */
    public void setChannelListObject(JSONObject channelListObject) {
        this.channelListObject = channelListObject;
    }

    public Long getAdminStatusUpNum() {
        return adminStatusUpNum;
    }

    public void setAdminStatusUpNum(Long adminStatusUpNum) {
        this.adminStatusUpNum = adminStatusUpNum;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public JSONObject getChannelFunctionJson() {
        return channelFunctionJson;
    }

    public void setChannelFunctionJson(JSONObject channelFunctionJson) {
        this.channelFunctionJson = channelFunctionJson;
    }

    public Logger getLogger() {
        return logger;
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

    public JSONObject getSupportFunction() {
        return supportFunction;
    }

    public void setSupportFunction(JSONObject supportFunction) {
        this.supportFunction = supportFunction;
    }

    /**
     * @return the isIpqamSupported
     */
    public Boolean getIsIpqamSupported() {
        return isIpqamSupported;
    }

    /**
     * @param isIpqamSupported
     *            the isIpqamSupported to set
     */
    public void setIsIpqamSupported(Boolean isIpqamSupported) {
        this.isIpqamSupported = isIpqamSupported;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public JSONObject getCmcDownChannelBaseShowInfoJson() {
        return cmcDownChannelBaseShowInfoJson;
    }

    public void setCmcDownChannelBaseShowInfoJson(JSONObject cmcDownChannelBaseShowInfoJson) {
        this.cmcDownChannelBaseShowInfoJson = cmcDownChannelBaseShowInfoJson;
    }

    public JSONObject getPowerLimit() {
        return powerLimit;
    }

    public void setPowerLimit(JSONObject powerLimit) {
        this.powerLimit = powerLimit;
    }
}
