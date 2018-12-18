/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.action;

import java.io.IOException;
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
import com.topvision.ems.cmc.exception.RefreshDataException;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.service.FrequencyHoppingService;
import com.topvision.ems.cmc.perf.service.CmcPerfCommonService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUsSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelCounterInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcUpChannelListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcUpChannelListAction extends BaseAction {
    private static final long serialVersionUID = -5347086165354662731L;
    private final Logger logger = LoggerFactory.getLogger(CmcUpChannelListAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcChannelService cmcChannelService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcPerfCommonService")
    private CmcPerfCommonService perfCommonService;
    @Autowired
    private FrequencyHoppingService ccmtsSpectrumGpService;
    private CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo;
    private CmcAttribute cmcAttribute;
    private Long entityId;
    private Long cmcId;
    private String cmcMac;
    private Long cmcPortId;
    private Integer channelId;
    private Integer operationResult;
    private String source;
    private Integer productType;
    private JSONObject channelListObject = new JSONObject();
    private CcmtsSpectrumGpChnl chnlGroupInfo;

    private JSONObject channelFunctionJson = new JSONObject();

    private JSONObject supportFunction = new JSONObject();

    /**
     * 显示上行信道列表tab页面
     * 
     * @return String
     */
    public String showUpChannelList() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("channelExtModeName", deviceVersionService.getParamValue(cmcId, "upChannel", "channelExtModeName"));
        map.put("rangingBackoff", deviceVersionService.getParamValue(cmcId, "upChannel", "rangingBackoff"));
        map.put("txBackoff", deviceVersionService.getParamValue(cmcId, "upChannel", "txBackoff"));
        // 上行信道支持的功能
        channelFunctionJson.putAll(map);
        // 获得cmcMac
        cmcMac = cmcAttribute.getTopCcmtsSysMacAddr();
        // 获得entityId
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 获取上行信道基本信息列表
     * 
     * @return String
     */
    public String getUpStreamBaseInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);

        json.put("data", cmcUpChannelBaseShowInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取上行信道统计信息列表
     * 
     * @return String
     */
    public String getUpStreamStasticInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUpChannelCounterInfo> cmcUpChannelCounterInfoList = cmcUpChannelService
                .getUpChannelStaticInfoList(cmcId);
        json.put("data", cmcUpChannelCounterInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取上行信道质量信息列表 --modify 将上行信道质量信息来源改为由性能模块表获取
     * 
     * @return String
     */
    public String getUpStreamQualityInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcUsSignalQualityInfo> cmcUsSignalQualityInfos = cmcUpChannelService.getUsSignalQualityInfoList(cmcId);
        /*
         * 从性能模块获取实时信号质量 /*List<CmcSignalQuality> sigQList =
         * perfCommonService.fetchCmcSignalQuality(cmcId); Map<Long, CmcSignalQuality> temp = new
         * HashMap<Long, CmcSignalQuality>(); for (CmcSignalQuality signal : sigQList) { Long
         * channalIndex = signal.getCmcChannelIndex(); temp.put(channalIndex, signal); } for
         * (CmcUsSignalQualityInfo up : cmcUsSignalQualityInfos) { Long channalIndex =
         * up.getChannelIndex(); CmcSignalQuality qa = temp.get(channalIndex); if (qa != null) {
         * up.setSigQSignalNoise(qa.getDocsIfSigQSignalNoise());
         * up.setSigQCorrecteds(qa.getDocsIfSigQCorrecteds());
         * up.setSigQUncorrectables(qa.getDocsIfSigQUncorrectables()); } }
         */
        json.put("data", cmcUsSignalQualityInfos);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示上行信道修改基本信息页面
     * 
     * @return String
     */
    public String showUpStreamBaseInfoConfig() {
        String rangingBackoff = deviceVersionService.getParamValue(cmcId, "upChannel", "rangingBackoff");
        supportFunction.put("rangingBackoff", rangingBackoff);
        String txBackoff = deviceVersionService.getParamValue(cmcId, "upChannel", "txBackoff");
        supportFunction.put("txBackoff", txBackoff);
        String channelExtModeName = deviceVersionService.getParamValue(cmcId, "upChannel", "channelExtModeName");
        supportFunction.put("channelExtModeName", channelExtModeName);
        String profileName = deviceVersionService.getParamValue(cmcId, "upChannel", "modulationProfile");
        supportFunction.put("modulationProfile", profileName);
        cmcUpChannelBaseShowInfo = cmcUpChannelService.getUpChannelBaseShowInfo(cmcPortId);

        Map<String, List<?>> channelMap = cmcChannelService.getUSChannelFrequencyListByPortId(cmcPortId, cmcId);
        channelListObject.put("upList", channelMap.get("upList"));
        channelListObject.put("downList", channelMap.get("downList"));
        // 获得entityId
        entityId = cmcService.getEntityIdByCmcId(cmcId);

        // TODO 在此次版本中屏蔽自动跳频功能(勿删)
        // chnlGroupInfo = ccmtsSpectrumGpService.getChnlGroupInfo(entityId, channelIndex);
        return SUCCESS;
    }

    /**
     * 修改单条上行信道基本信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcChannelAction", operationName = "modifyUpStreamBaseInfo${source}")
    public String modifyUpStreamBaseInfo() {
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
        cmcUpChannelBaseShowInfo.setCmcId(cmcId);
        cmcUpChannelBaseShowInfo.setCmcPortId(cmcPortId);
        cmcUpChannelBaseShowInfo.setEntityId(entityId);
        try {
            // 修改单条上行信道基本信息
            cmcUpChannelService.modifyUpChannelBaseShowInfo(cmcUpChannelBaseShowInfo, productType, chnlGroupInfo);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (RefreshDataException rde) {
            result = "refreshError";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
            operationResult = OperationLog.FAILURE;
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 刷新上行信道基本信息
     * 
     * @return String
     */
    public String refreshUpChannelBaseInfo() {
        String message;
        CcmtsSpectrumGpChnl chnlGroup = new CcmtsSpectrumGpChnl();
        chnlGroup.setEntityId(entityId);
        chnlGroup.setChnlCmcMac(cmcMac);
        if (cmcMac != null) {
            chnlGroup.setCmcMacForDevice(new PhysAddress(cmcMac));
        }
        try {
            cmcUpChannelService.refreshUpChannelBaseInfo(cmcId, productType, chnlGroup);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新上行通道信号质量
     * 
     * @return String
     */
    public String refreshUpChannelSignalQualityInfo() {
        String message;
        try {
            cmcUpChannelService.refreshUpChannelSignalQualityInfo(cmcId, productType);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 将信道跳频组恢复到静态配置状态 TODO 考虑是否需要移动位置
     * 
     * @return
     * @throws IOException
     */
    public String resertChnlGpToStatic() throws IOException {
        CcmtsSpectrumGpChnl chnlGroup = new CcmtsSpectrumGpChnl();
        chnlGroup.setEntityId(entityId);
        chnlGroup.setChnlCmcMac(cmcMac);
        chnlGroup.setCmcMacForDevice(new PhysAddress(cmcMac));
        chnlGroup.setChnlId(channelId);
        // 恢复到静态配置状态
        chnlGroup.setChnlResetTostatic(CcmtsSpectrumGpChnl.CHNL_RESET_TOSTATIC);
        ccmtsSpectrumGpService.modifyGpChnlResetToStatic(chnlGroup);
        return NONE;
    }

    public CmcUpChannelBaseShowInfo getCmcUpChannelBaseShowInfo() {
        return cmcUpChannelBaseShowInfo;
    }

    public void setCmcUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        this.cmcUpChannelBaseShowInfo = cmcUpChannelBaseShowInfo;
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

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
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

    public CcmtsSpectrumGpChnl getChnlGroupInfo() {
        return chnlGroupInfo;
    }

    public void setChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroupInfo) {
        this.chnlGroupInfo = chnlGroupInfo;
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

    public JSONObject getSupportFunction() {
        return supportFunction;
    }

    public void setSupportFunction(JSONObject supportFunction) {
        this.supportFunction = supportFunction;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

}
