package com.topvision.ems.mobile.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("mSpectrumAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MSpectrumAction extends BaseAction {

    private static final long serialVersionUID = -5371552822742175684L;
    private static final String SPECTRUM_OPTIMIZE = "spectrumOptimize";
    @Autowired
    private SpectrumHeartbeatService spectrumHeartbeatService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Autowired
    private SpectrumConfigService spectrumConfigService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    private CmcAttribute cmcAttribute;
    private Long callbackId;
    private Long cmcId;
    private Boolean supportSpectrumOptimize;
    private Boolean oltSupportSpectrumOptimize = false;
    List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList;
    List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfos;
    private Boolean downChannelOpened = false;
    private Integer timeInterval;
    private JSONObject upChannels;
    private Boolean spectrumOltSwitch;

    public String getCmcCurSpectrum() {
        Map<String, Object> map = new HashMap<String, Object>();
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        // 判断版本是否支持频谱优化
        // modify by fanzidong，针对分布式的CC，需要OLT版本和CC版本都到位
        String oltVersion = cmcAttribute.getOltVersion();
        Boolean spectrumOptimize = false;
        if (oltVersion == null || "".equals(oltVersion)) {
            // 只用判断CC版本是否支持
            deviceVersionService.isFunctionSupported(cmcId, SPECTRUM_OPTIMIZE);
        } else {
            // 需要OLT版本和CC版本都到位
            String cmcVersion = cmcAttribute.getDolVersion();
            Boolean oltSupport = DeviceFuctionSupport.isSupportFunction(cmcAttribute.getCmcDeviceStyle(), oltVersion,
                    SPECTRUM_OPTIMIZE);
            Boolean ccSupport = DeviceFuctionSupport.isSupportFunction(cmcAttribute.getCmcDeviceStyle(), cmcVersion,
                    SPECTRUM_OPTIMIZE);
            spectrumOptimize = oltSupport && ccSupport;
            setOltSupportSpectrumOptimize(oltSupport);
        }
        this.supportSpectrumOptimize = spectrumOptimize;
        cmcDownChannelBaseShowInfoList = cmcDownChannelService.getDownChannelFrequencyListByCmcId(cmcId);
        // 获取系统实时频谱采集步长
        setTimeInterval(spectrumConfigService.getTimeInterval());
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
            json.put("modulation", baseInfo.getDocsIfUpChannelModulationProfileName());
            json.put("snr", baseInfo.getDocsIfSigQSignalNoise());
            upChannels.put("channel_" + baseInfo.getChannelId(), json);
        }
        Long productType = cmcAttribute.getCmcDeviceStyle();
        if (productType != null) {
            if (entityTypeService.isCcmtsWithoutAgent(productType)) {
                setSpectrumOltSwitch(spectrumConfigService.getOltSwitchStatus(cmcId));
            }
        }
        map.put("upChannels", upChannels);
        map.put("downChannelOpened", downChannelOpened);
        map.put("supportSpectrumOptimize", supportSpectrumOptimize);
        map.put("oltSupportSpectrumOptimize", oltSupportSpectrumOptimize);
        writeDataToAjax(JSONObject.fromObject(map));
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

    public String stopCurSpectrum() {
        spectrumHeartbeatService.delWebHeartBeat(callbackId);
        return NONE;
    }

    public String getSpectrumUpchannelInfo() throws IOException {
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        JSONArray json = JSONArray.fromObject(cmcUpChannelBaseShowInfoList);
        json.write(response.getWriter());
        return NONE;
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

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Boolean getSupportSpectrumOptimize() {
        return supportSpectrumOptimize;
    }

    public void setSupportSpectrumOptimize(Boolean supportSpectrumOptimize) {
        this.supportSpectrumOptimize = supportSpectrumOptimize;
    }

    public Boolean getOltSupportSpectrumOptimize() {
        return oltSupportSpectrumOptimize;
    }

    public void setOltSupportSpectrumOptimize(Boolean oltSupportSpectrumOptimize) {
        this.oltSupportSpectrumOptimize = oltSupportSpectrumOptimize;
    }

    public List<CmcDownChannelBaseShowInfo> getCmcDownChannelBaseShowInfoList() {
        return cmcDownChannelBaseShowInfoList;
    }

    public void setCmcDownChannelBaseShowInfoList(List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList) {
        this.cmcDownChannelBaseShowInfoList = cmcDownChannelBaseShowInfoList;
    }

    public Boolean getDownChannelOpened() {
        return downChannelOpened;
    }

    public void setDownChannelOpened(Boolean downChannelOpened) {
        this.downChannelOpened = downChannelOpened;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public List<CmcUpChannelBaseShowInfo> getCmcUpChannelBaseShowInfos() {
        return cmcUpChannelBaseShowInfos;
    }

    public void setCmcUpChannelBaseShowInfos(List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfos) {
        this.cmcUpChannelBaseShowInfos = cmcUpChannelBaseShowInfos;
    }

    public JSONObject getUpChannels() {
        return upChannels;
    }

    public void setUpChannels(JSONObject upChannels) {
        this.upChannels = upChannels;
    }

    public Boolean getSpectrumOltSwitch() {
        return spectrumOltSwitch;
    }

    public void setSpectrumOltSwitch(Boolean spectrumOltSwitch) {
        this.spectrumOltSwitch = spectrumOltSwitch;
    }
}
