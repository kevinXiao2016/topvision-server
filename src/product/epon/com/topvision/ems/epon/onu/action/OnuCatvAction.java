/***********************************************************************
 * $Id: OnuCatvConfigAction.java,v1.0 2016-4-26 下午5:05:03 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.ems.epon.onu.service.OnuCatvService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.license.parser.LicenseIf;

/**
 * @author haojie
 * @created @2016-4-26-下午5:05:03
 *
 */
@Controller("onuCatvAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuCatvAction extends BaseAction {
    private static final long serialVersionUID = -3486933509049784081L;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuCatvService onuCatvService;
    @Autowired
    private LicenseIf licenseIf;
    @Autowired
    private OnuService onuService;
    @Autowired
    private GponOnuService gponOnuService;
    private OltOnuAttribute onuAttribute;
    private OnuCatvConfig onuCatvConfig;
    private JSONObject onuCatvConfigJson;
    private Entity onu;
    private Long onuId;
    private Long entityId;
    private Long onuIndex;
    private Integer switchCATV;
    private Integer gainControlType;
    private Integer agcUpValue;
    private Integer agcRange;
    private Integer mgcTxAttenuation;
    private Integer inputLO;
    private Integer inputHI;
    private Integer outputLO;
    private Integer outputHI;
    private Integer voltageLO;
    private Integer voltageHI;
    private Integer temperatureLO;
    private Integer temperatureHI;
    private Boolean testProjectSupport = false;
    private GponOnuCapability gponOnuCapability;

    /**
     * 跳转catv光机配置页面
     * 
     * @return
     */
    public String showOnuCatvConfig() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if(onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)){
            gponOnuCapability=gponOnuService.queryForGponOnuCapability(onuId);
            testProjectSupport = licenseIf.isSupportProject("test");
            return "GponOnu";
        }else{
            testProjectSupport = licenseIf.isSupportProject("test");
            return SUCCESS;  
        }        
    }

    /**
     * 获取onu catv 配置信息
     * 
     * @return
     */
    public String getOnuCatvConfigInfo() {
        onuCatvConfig = onuCatvService.getOnuCatvConfig(onuId);
        onuCatvConfigJson = JSONObject.fromObject(onuCatvConfig);
        writeDataToAjax(onuCatvConfigJson);
        return NONE;
    }

    /**
     * 修改catv配置信息
     * 
     * @return
     */
    public String modifyOnuCatvConfig() {
        OnuCatvConfig onuCatvConfig = new OnuCatvConfig();
        onuCatvConfig.setEntityId(entityId);
        onuCatvConfig.setOnuIndex(onuIndex);
        onuCatvConfig.setOnuId(onuId);
        onuCatvConfig.setOnuCatvOrConfigSwitch(switchCATV);
        onuCatvConfig.setOnuCatvOrConfigGainControlType(gainControlType);
        onuCatvConfig.setOnuCatvOrConfigAGCUpValue(agcUpValue);
        onuCatvConfig.setOnuCatvOrConfigAGCRange(agcRange);
        onuCatvConfig.setOnuCatvOrConfigMGCTxAttenuation(mgcTxAttenuation);
        if (licenseIf.isSupportProject("test")) {
            onuCatvConfig.setOnuCatvOrConfigInputHI(inputHI);
            onuCatvConfig.setOnuCatvOrConfigInputLO(inputLO);
            onuCatvConfig.setOnuCatvOrConfigOutputHI(outputHI);
            onuCatvConfig.setOnuCatvOrConfigOutputLO(outputLO);
            onuCatvConfig.setOnuCatvOrConfigVoltageHI(voltageHI);
            onuCatvConfig.setOnuCatvOrConfigVoltageLO(voltageLO);
            onuCatvConfig.setOnuCatvOrConfigTemperatureHI(temperatureHI);
            onuCatvConfig.setOnuCatvOrConfigTemperatureLO(temperatureLO);
        }
        onuCatvService.modifyOnuCatvConfig(onuCatvConfig);
        return NONE;
    }

    /**
     * 刷新catv配置信息
     * 
     * @return
     */
    public String refreshOnuCatvConfig() {
        OnuCatvConfig onuCatvConfig = new OnuCatvConfig();
        onuCatvConfig.setEntityId(entityId);
        onuCatvConfig.setOnuIndex(onuIndex);
        onuCatvService.refreshOnuCatvConfig(onuCatvConfig);
        return NONE;
    }

    public OnuCatvConfig getOnuCatvConfig() {
        return onuCatvConfig;
    }

    public void setOnuCatvConfig(OnuCatvConfig onuCatvConfig) {
        this.onuCatvConfig = onuCatvConfig;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Entity getOnu() {
        return onu;
    }

    public void setOnu(Entity onu) {
        this.onu = onu;
    }

    public JSONObject getOnuCatvConfigJson() {
        return onuCatvConfigJson;
    }

    public void setOnuCatvConfigJson(JSONObject onuCatvConfigJson) {
        this.onuCatvConfigJson = onuCatvConfigJson;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public OnuCatvService getOnuCatvService() {
        return onuCatvService;
    }

    public void setOnuCatvService(OnuCatvService onuCatvService) {
        this.onuCatvService = onuCatvService;
    }

    public Integer getSwitchCATV() {
        return switchCATV;
    }

    public void setSwitchCATV(Integer switchCATV) {
        this.switchCATV = switchCATV;
    }

    public Integer getGainControlType() {
        return gainControlType;
    }

    public void setGainControlType(Integer gainControlType) {
        this.gainControlType = gainControlType;
    }

    public Integer getAgcUpValue() {
        return agcUpValue;
    }

    public void setAgcUpValue(Integer agcUpValue) {
        this.agcUpValue = agcUpValue;
    }

    public Integer getAgcRange() {
        return agcRange;
    }

    public void setAgcRange(Integer agcRange) {
        this.agcRange = agcRange;
    }

    public Integer getMgcTxAttenuation() {
        return mgcTxAttenuation;
    }

    public void setMgcTxAttenuation(Integer mgcTxAttenuation) {
        this.mgcTxAttenuation = mgcTxAttenuation;
    }

    public Integer getInputLO() {
        return inputLO;
    }

    public void setInputLO(Integer inputLO) {
        this.inputLO = inputLO;
    }

    public Integer getInputHI() {
        return inputHI;
    }

    public void setInputHI(Integer inputHI) {
        this.inputHI = inputHI;
    }

    public Integer getOutputLO() {
        return outputLO;
    }

    public void setOutputLO(Integer outputLO) {
        this.outputLO = outputLO;
    }

    public Integer getOutputHI() {
        return outputHI;
    }

    public void setOutputHI(Integer outputHI) {
        this.outputHI = outputHI;
    }

    public Integer getVoltageLO() {
        return voltageLO;
    }

    public void setVoltageLO(Integer voltageLO) {
        this.voltageLO = voltageLO;
    }

    public Integer getVoltageHI() {
        return voltageHI;
    }

    public void setVoltageHI(Integer voltageHI) {
        this.voltageHI = voltageHI;
    }

    public Integer getTemperatureLO() {
        return temperatureLO;
    }

    public void setTemperatureLO(Integer temperatureLO) {
        this.temperatureLO = temperatureLO;
    }

    public Integer getTemperatureHI() {
        return temperatureHI;
    }

    public void setTemperatureHI(Integer temperatureHI) {
        this.temperatureHI = temperatureHI;
    }

    public Boolean getTestProjectSupport() {
        return testProjectSupport;
    }

    public void setTestProjectSupport(Boolean testProjectSupport) {
        this.testProjectSupport = testProjectSupport;
    }

    public OltOnuAttribute getOnuAttribute() {
        return onuAttribute;
    }

    public void setOnuAttribute(OltOnuAttribute onuAttribute) {
        this.onuAttribute = onuAttribute;
    }

    public GponOnuCapability getGponOnuCapability() {
        return gponOnuCapability;
    }

    public void setGponOnuCapability(GponOnuCapability gponOnuCapability) {
        this.gponOnuCapability = gponOnuCapability;
    }

}
