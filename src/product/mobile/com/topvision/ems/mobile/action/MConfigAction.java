package com.topvision.ems.mobile.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.mobile.domain.FlatnessInfo;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.mobile.domain.AttenuationInfo;
import com.topvision.ems.mobile.domain.MobileDeviceType;
import com.topvision.ems.mobile.service.MConfigService;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("mConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MConfigAction extends BaseAction {
    private static final long serialVersionUID = -4293143253615628247L;
    @Resource(name = "mConfigService")
    private MConfigService mConfigService;
    private Long cmcId;
    private Long typeId;
    private Long callbackId;
    private String deviceType;
    private String corporation;
    private String frequency;
    private String powerlevel;
    private Double deviceSetPowerLevel;
    private Double defaultPowerLevel;
    private Integer mode;
    private static Integer SIMPLEMODE = 0, SENIORMODE = 1;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 显示手持终端型号配置界面
     * 
     * @return
     * @throws IOException
     */
    public String showMobileDeviceConfig() {
        return SUCCESS;
    }

    /**
     * 修改界面
     * 
     * @return
     * @throws IOException
     */
    public String showDefaultPowerLevelConfig() {
        defaultPowerLevel = mConfigService.getDefaultPowerLevel();
        return SUCCESS;
    }

    /**
     * 修改界面
     * 
     * @return
     * @throws IOException
     */
    public String showModifyMobileDeviceType() {
        return SUCCESS;
    }

    /**
     * 修改界面
     * 
     * @return
     * @throws IOException
     */
    public String showAddMobileDeviceType() {
        return SUCCESS;
    }

    /**
     * 手持设备列表查询
     * 
     * @return
     * @throws IOException
     */
    public String mobileDeviceTypeList() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<MobileDeviceType> mobileDeviceTypes = mConfigService.getMobileDeviceTypeList();
        map.put("data", mobileDeviceTypes);
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    /**
     * 修改
     * 
     * @return
     * @throws IOException
     */
    public String modifyMobileDeviceType() {
        mConfigService.modifyMobileDeviceType(typeId, frequency, powerlevel);
        return NONE;
    }

    /**
     * add
     * 
     * @return
     * @throws IOException
     */
    public String addMobileDeviceType() {
        mConfigService.addMobileDeviceType(deviceType, corporation, frequency, powerlevel);
        return NONE;
    }

    /**
     * 删除
     * 
     * @return
     * @throws IOException
     */
    public String delMobileDeviceType() {
        mConfigService.delMobileDeviceType(typeId);
        return NONE;
    }

    /**
     * setDefalut
     * 
     * @return
     * @throws IOException
     */
    public String setDefaultMobileDeviceType() {
        mConfigService.setDefaultMobileDeviceType(typeId);
        return NONE;
    }

    /**
     * setDefalut
     * 
     * @return
     * @throws IOException
     */
    public String modifyDefaultPowerLevel() {
        mConfigService.modifyDefaultPowerLevel(defaultPowerLevel);
        return NONE;
    }

    /**
     * getDefaultPowerLevel
     * 
     * @return
     * @throws IOException
     */
    public String getDefaultPowerLevelInfo() {
        Double defaultPowerLevel = mConfigService.getDefaultPowerLevel();
        writeDataToAjax(defaultPowerLevel.toString());
        return NONE;
    }

    /**
     * calculateAttenuation
     * 
     * @return
     * @throws IOException
     */
    public String startCalculateAttenuation() {
        Long callbackId = mConfigService.startCalculateAttenuation(cmcId, typeId, deviceSetPowerLevel,
                defaultPowerLevel);
        writeDataToAjax("" + callbackId);
        return NONE;
    }

    /**
     * calculateAttenuation
     * 
     * @return
     * @throws IOException
     */
    public String stopCalculateAttenuation() {
        mConfigService.stopCalculateAttenuation(callbackId);
        return NONE;
    }

    /**
     * 手持设备列表查询
     * 
     * @return
     * @throws IOException
     */
    public String getMobileDeviceForMobile() throws IOException {
        List<MobileDeviceType> mobileDeviceTypes = mConfigService.getMobileDeviceTypeList();
        JSONArray json = JSONArray.fromObject(mobileDeviceTypes);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * getAttenuationInfo
     * 
     * @return
     * @throws IOException
     */
    public String pickAttenuationInfo() {
        AttenuationInfo attenuationInfo = new AttenuationInfo();
        if (mode == SENIORMODE) {
            attenuationInfo = mConfigService.pickAttenuationInfo(callbackId);
        } else {
            attenuationInfo = mConfigService.pickAttenuationInfoSimple(callbackId);
        }

        logger.debug("pickAttenuationInfo[" + attenuationInfo + "]");
        writeDataToAjax(JSONObject.fromObject(attenuationInfo));
        return NONE;
    }

    /**
     * getAttenuationInfo
     *
     * @return
     * @throws IOException
     */
    public String getTimeInterval() {
        Integer timeInterval = mConfigService.getTimeInterval();
        writeDataToAjax("" + timeInterval);
        return NONE;
    }


    /**
     * getAttenuationInfo
     *
     * @return
     * @throws IOException
     */
    public String getPowerLevelUnit() {
        String powerLevelUnit = (String)UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        writeDataToAjax("" + powerLevelUnit);
        return NONE;
    }


    /**
     * 启动平坦度
     *
     * @return
     * @throws IOException
     */
    public String startFlatnessInfo() {
        Long callbackId = mConfigService.startFlatnessInfo(cmcId, typeId);
        writeDataToAjax("" + callbackId);
        return NONE;
    }

    /**
     * calculateAttenuation
     *
     * @return
     * @throws IOException
     */
    public String stopFlatnessInfo() {
        mConfigService.stopFlatnessInfo(callbackId);
        return NONE;
    }

    /**
     * calculateAttenuation
     *
     * @return
     * @throws IOException
     */
    public String resetFlatnessInfo() {
        mConfigService.resetFlatnessInfo(callbackId);
        return NONE;
    }

    /**
     * getAttenuationInfo
     *
     * @return
     * @throws IOException
     */
    public String pickFlatnessInfo() {
        FlatnessInfo flatnessInfo = mConfigService.pickFlatnessInfo(callbackId);
        logger.debug("pickFlatnessInfo[" + flatnessInfo + "]");
        writeDataToAjax(JSONObject.fromObject(flatnessInfo));
        return NONE;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPowerlevel() {
        return powerlevel;
    }

    public void setPowerlevel(String powerlevel) {
        this.powerlevel = powerlevel;
    }

    public Double getDefaultPowerLevel() {
        return defaultPowerLevel;
    }

    public void setDefaultPowerLevel(Double defaultPowerLevel) {
        this.defaultPowerLevel = defaultPowerLevel;
    }

    public Double getDeviceSetPowerLevel() {
        return deviceSetPowerLevel;
    }

    public void setDeviceSetPowerLevel(Double deviceSetPowerLevel) {
        this.deviceSetPowerLevel = deviceSetPowerLevel;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Long getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }
}
