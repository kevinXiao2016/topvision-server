/***********************************************************************
 * $Id: SpectrumCurAction.java,v1.0 2014-1-13 上午9:17:27 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.SpectrumHeartbeatService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2014-1-13-上午9:17:27
 * 
 */
@Controller("spectrumCurAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpectrumCurAction extends BaseAction {
    private static final long serialVersionUID = -761499997281159709L;
    private static final String SPECTRUM_OPTIMIZE = "spectrumOptimize";

    private Logger logger = LoggerFactory.getLogger(SpectrumCurAction.class);
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private SpectrumHeartbeatService spectrumHeartbeatService;
    @Autowired
    private SpectrumConfigService spectrumConfigService;
    private String seesionId = "";
    private String dwrId;
    private Long cmcId;
    private Long callbackId;
    private JSONObject tabFuncs = new JSONObject();
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    
    private JSONObject upChannels;
    private String upChannelStr;
    private Long productType;
    private Boolean spectrumOltSwitch;
    List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList;
    List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList;
    List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfos;
    
    private JSONArray upChannelList;
    private JSONArray downChannelList;
    // @Value("${Spectrum.unit}")
    private String spectrumUnit;
    private CmcAttribute cmcAttribute;
    private Boolean supportSpectrumOptimize;
    private Boolean oltSupportSpectrumOptimize = false;
    // 实时采集步长
    private Integer timeInterval;
    private Boolean downChannelOpened = false;

    /**
     * 频谱功能页面
     * 
     * @return
     */
    public String showCmcCurSpectrum() {
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));

        // 判断版本是否支持频谱优化
        // modify by fanzidong，针对分布式的CC，需要OLT版本和CC版本都到位
        String oltVersion = cmcAttribute.getOltVersion();
        Boolean spectrumOptimize;
        if (oltVersion == null || "".equals(oltVersion)) {
            // 只用判断CC版本是否支持
            spectrumOptimize = deviceVersionService.isFunctionSupported(cmcId, SPECTRUM_OPTIMIZE);
        } else {
            // 需要OLT版本和CC版本都到位
            String cmcVersion = cmcAttribute.getDolVersion();
            Boolean oltSupport = DeviceFuctionSupport.isSupportFunction(cmcAttribute.getCmcDeviceStyle(), oltVersion,
                    SPECTRUM_OPTIMIZE);
            Boolean ccSupport = DeviceFuctionSupport.isSupportFunction(cmcAttribute.getCmcDeviceStyle(), cmcVersion,
                    SPECTRUM_OPTIMIZE);
            spectrumOptimize = oltSupport && ccSupport;
            oltSupportSpectrumOptimize = oltSupport;
        }
        this.supportSpectrumOptimize = spectrumOptimize;

        // 获取系统实时频谱采集步长
        setTimeInterval(spectrumConfigService.getTimeInterval());

        // 获取实时频谱的数据
        // 获取信道信息
        cmcUpChannelBaseShowInfoList = cmcUpChannelService.getUpChannelFrequencyListByCmcId(cmcId);
        cmcDownChannelBaseShowInfoList = cmcDownChannelService.getDownChannelFrequencyListByCmcId(cmcId);
        upChannelList = JSONArray.fromObject(cmcUpChannelBaseShowInfoList);
        downChannelList = JSONArray.fromObject(cmcDownChannelBaseShowInfoList);

        // 判断是否有至少一个处于开启状态的下行信道
        for (CmcDownChannelBaseShowInfo downChannel : cmcDownChannelBaseShowInfoList) {
            if (downChannel.getIfAdminStatus() != null && downChannel.getIfAdminStatus().equals(1)) {
                downChannelOpened = true;
                break;
            }
        }

        // 封装成实时频谱端需要的格式
        cmcUpChannelBaseShowInfos = cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId);
        upChannels = new JSONObject();
        for (CmcUpChannelBaseShowInfo baseInfo : cmcUpChannelBaseShowInfos) {
        		JSONObject json = new JSONObject();
                json.put("id", baseInfo.getChannelId());
                json.put("center", (double) baseInfo.getChannelFrequency() / 1000000);
                json.put("width", (double) baseInfo.getChannelWidth() / 1000000);
            json.put("start",
                    (double) baseInfo.getChannelFrequency() / 1000000 - (double) baseInfo.getChannelWidth() / 2000000);
            json.put("end",
                    (double) baseInfo.getChannelFrequency() / 1000000 + (double) baseInfo.getChannelWidth() / 2000000);
                if (baseInfo.getIfAdminStatus() == null || baseInfo.getIfAdminStatus() != 1) {
                    json.put("adminStatus", false);
                } else {
                    json.put("adminStatus", true);
                }
                upChannels.put("channel_" + baseInfo.getChannelId(), json);
        }
        // 如果是整合型的CCMTS，还需要获取上联OLT的频谱采集开关
        // TODO 设备类型判断先写死，deviceType重构之后再修改
        if (productType != null) {
            if (entityTypeService.isCcmtsWithoutAgent(productType)) {
                spectrumOltSwitch = spectrumConfigService.getOltSwitchStatus(cmcId);
            }
        }
        // 电平单位
        spectrumUnit = (String) UnitConfigConstant.get("elecLevelUnit");
        return SUCCESS;
    }

    public String loadTimeInterval() {
        timeInterval = spectrumConfigService.getTimeInterval();
        JSONObject json = new JSONObject();
        json.put("timeInterval", timeInterval);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadOltSwitchStatus() throws IOException {
        JSONObject result = new JSONObject();
        spectrumOltSwitch = spectrumConfigService.getOltSwtichFromDevice(cmcId);
        result.put("status", spectrumOltSwitch);
        result.write(response.getWriter());
        return NONE;
    }

    public String heartbeat() throws IOException {
        try {
            Boolean b = spectrumHeartbeatService.heartbeat(callbackId);
            if (!b) {
                writeDataToAjax("heartbeat error");
            }
        } catch (Exception e) {
            logger.debug("", e);
            writeDataToAjax("heartbeat error");
        }
        return NONE;
    }

    public String saveChannelData() throws IOException {
        JSONObject upChannel = JSONObject.fromObject(upChannelStr);
        CmcUpChannelBaseInfo chl = new CmcUpChannelBaseInfo();
        Integer channleId = upChannel.getInt("id");
        Long channelIndex = cmcUpChannelService.getChannleIndex(cmcId, channleId);
        chl.setCmcId(cmcId);
        chl.setChannelIndex(channelIndex);
        Double fr = Double.parseDouble(upChannel.get("center").toString()) * 1000000;
        chl.setChannelFrequency(fr.longValue());
        Double wid = Double.parseDouble(upChannel.get("width").toString()) * 1000000;
        chl.setChannelWidth(wid.longValue());
        JSONObject result = new JSONObject();
        try {
            cmcUpChannelService.modifyUpChannelForSpe(chl, upChannel.getInt("productType"));
            result.put("success", true);
        } catch (Exception e) {
            logger.debug("updateUpchannelForSpe fail!", e);
            result.put("success", false);
            // TODO 详细错误信息
            result.put("error", "......");
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    public String stopCurSpectrum() {
        spectrumHeartbeatService.delWebHeartBeat(callbackId);
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getSeesionId() {
        return seesionId;
    }

    public void setSeesionId(String seesionId) {
        this.seesionId = seesionId;
    }

    public JSONObject getTabFuncs() {
        return tabFuncs;
    }

    public void setTabFuncs(JSONObject tabFuncs) {
        this.tabFuncs = tabFuncs;
    }

    public JSONObject getUpChannels() {
        return upChannels;
    }

    public void setUpChannels(JSONObject upChannels) {
        this.upChannels = upChannels;
    }

    public String getUpChannelStr() {
        return upChannelStr;
    }

    public void setUpChannelStr(String upChannelStr) {
        this.upChannelStr = upChannelStr;
    }

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public Boolean getSpectrumOltSwitch() {
        return spectrumOltSwitch;
    }

    public void setSpectrumOltSwitch(Boolean spectrumOltSwitch) {
        this.spectrumOltSwitch = spectrumOltSwitch;
    }

    public String getDwrId() {
        return dwrId;
    }

    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    public List<CmcUpChannelBaseShowInfo> getCmcUpChannelBaseShowInfoList() {
        return cmcUpChannelBaseShowInfoList;
    }

    public void setCmcUpChannelBaseShowInfoList(List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList) {
        this.cmcUpChannelBaseShowInfoList = cmcUpChannelBaseShowInfoList;
    }

    public List<CmcDownChannelBaseShowInfo> getCmcDownChannelBaseShowInfoList() {
        return cmcDownChannelBaseShowInfoList;
    }

    public void setCmcDownChannelBaseShowInfoList(List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList) {
        this.cmcDownChannelBaseShowInfoList = cmcDownChannelBaseShowInfoList;
    }

    public JSONArray getUpChannelList() {
        return upChannelList;
    }

    public void setUpChannelList(JSONArray upChannelList) {
        this.upChannelList = upChannelList;
    }

    public JSONArray getDownChannelList() {
        return downChannelList;
    }

    public void setDownChannelList(JSONArray downChannelList) {
        this.downChannelList = downChannelList;
    }

    public List<CmcUpChannelBaseShowInfo> getCmcUpChannelBaseShowInfos() {
        return cmcUpChannelBaseShowInfos;
    }

    public void setCmcUpChannelBaseShowInfos(List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfos) {
        this.cmcUpChannelBaseShowInfos = cmcUpChannelBaseShowInfos;
    }

    public String getSpectrumUnit() {
        return spectrumUnit;
    }

    public void setSpectrumUnit(String spectrumUnit) {
        this.spectrumUnit = spectrumUnit;
    }

    public Long getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Boolean getSupportSpectrumOptimize() {
        return supportSpectrumOptimize;
    }

    public void setSupportSpectrumOptimize(Boolean supportSpectrumOptimize) {
        this.supportSpectrumOptimize = supportSpectrumOptimize;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Boolean getOltSupportSpectrumOptimize() {
        return oltSupportSpectrumOptimize;
    }

    public void setOltSupportSpectrumOptimize(Boolean oltSupportSpectrumOptimize) {
        this.oltSupportSpectrumOptimize = oltSupportSpectrumOptimize;
    }

    public Boolean getDownChannelOpened() {
        return downChannelOpened;
    }

    public void setDownChannelOpened(Boolean downChannelOpened) {
        this.downChannelOpened = downChannelOpened;
    }

} 
