/***********************************************************************
 * $Id: OnuPortalAction.java,v1.0 2015年4月21日 上午9:44:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuCatvService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.ems.epon.topology.service.OnuRefreshService;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.utils.OnuTypeConvertor;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author Bravin
 * @created @2015年4月21日-上午9:44:37
 *
 */
@Controller("onuPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuPortalAction extends BaseAction {
    private static final long serialVersionUID = -5577488884513349039L;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuRefreshService onuRefreshService;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private OnuCatvService onuCatvService;
    @Autowired
    private OnuWanService onuWanService;
    @Autowired
    private GponOnuService gponOnuService;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;

    private Long entityId;
    private Integer module;
    private String leftPartItems;
    private String rightPartItems;
    private Entity onu;
    private OltOnuAttribute onuAttribute;
    private Entity olt;
    private OltOnuCapability oltOnuCapability;
    private GponOnuCapability gponOnuCapability;
    private GponOnuInfoSoftware gponOnuInfoSoftware;
    private OnuWanConfig onuWanConfig;
    private Long onuId;
    private Integer type = 1;
    private OltOnuPonAttribute onuPonAttribute;
    private Long alertId;
    private OnuCatvInfo onuCatvInfo;

    /**
     * 展示ONU快照
     * 
     * @return
     */
    public String showOnuPortal() {
        onu = entityService.getEntity(onuId);
        onuWanConfig = onuWanService.getOnuWanConfig(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if (onu.getTypeId() > 255) {
            onu.setTypeName(OnuTypeConvertor.convertTypeName(onuAttribute.getOnuPreType()));
        }
        olt = entityService.getEntity(onu.getParentId());
        onuPonAttribute = onuService.getOnuPonAttributeByOnuId(onuId);
        onuCatvInfo = onuCatvService.getOnuCatvInfo(onuId);
        switchCatvInfoUnit(onuCatvInfo);
        // 加载用户保存的设备快照视图
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
            // modify by lzt
            // 原设计对GPON 和EPON ONU快照页面拖拽保存采用了同一套，通过typeID加载会导致在EPON ONU 没有GPON ONU portaID时页面报错
            // 现对GPON ONU typeId 在保存portal时通过负数来进行区分
            leftPartItems = this.getPortalView(-onu.getTypeId()).getProperty("portalLeftPart");
            rightPartItems = this.getPortalView(-onu.getTypeId()).getProperty("portalRightPart");
            gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
            gponOnuInfoSoftware = gponOnuService.queryForGponOnuSoftware(onuId);
            return "GponOnu";
        } else {
            leftPartItems = this.getPortalView(onu.getTypeId()).getProperty("portalLeftPart");
            rightPartItems = this.getPortalView(onu.getTypeId()).getProperty("portalRightPart");
            oltOnuCapability = onuService.getOltOnuCapabilityByOnuId(onuId);
            return SUCCESS;
        }
    }

    /**
     * 查询数据库中是否存在该onu
     * 
     * @return
     */
    public String isOnuExist() {
        JSONObject obj = new JSONObject();
        Integer num = onuService.getOnuCountByOnuId(onuId);
        if (num > 0) {
            obj.put("exist", true);
        } else {
            obj.put("exist", false);
        }
        writeDataToAjax(obj);
        return NONE;
    }

    /**
     * 获取指定ONU的PON信息
     * 
     * @return
     */
    public String loadOnuPonAttribute() {
        onuPonAttribute = onuService.getOnuPonAttributeByOnuId(onuId);
        writeDataToAjax(onuPonAttribute);
        return NONE;
    }

    /**
     * 切换电平和温度单位
     * 
     * @param onuCatvInfo
     */
    private void switchCatvInfoUnit(OnuCatvInfo onuCatvInfo) {
        if (onuCatvInfo != null) {
            // 电平单位转换
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            if (onuCatvInfo.getOnuCatvOrInfoRfOutVoltage() != null) {
                Double rfOutVoltage;
                if (powerUnit.equalsIgnoreCase(UnitConfigConstant.MICRO_VOLT_UNIT)) {
                    rfOutVoltage = Double.parseDouble(onuCatvInfo.getOnuCatvOrInfoRfOutVoltage().toString()) / 10;
                } else {
                    rfOutVoltage = UnitConfigConstant.transDBμVToDBmV(Double.parseDouble(onuCatvInfo
                            .getOnuCatvOrInfoRfOutVoltage().toString()) / 10);
                }
                onuCatvInfo.setRfOutVoltageForunit(df.format(rfOutVoltage) + " " + powerUnit);
            } else {
                onuCatvInfo.setRfOutVoltageForunit("--");
            }
            // 温度单位转换
            String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
            if (onuCatvInfo.getOnuCatvOrInfoTemperature() != null) {
                Double temperature;
                if (tempUnit.equalsIgnoreCase(UnitConfigConstant.CENTI_TEMP_UNIT)) {
                    temperature = Double.parseDouble(onuCatvInfo.getOnuCatvOrInfoTemperature().toString()) / 10;
                } else {
                    temperature = UnitConfigConstant.transCentiToF(Double.parseDouble(onuCatvInfo
                            .getOnuCatvOrInfoTemperature().toString()) / 10);
                }
                onuCatvInfo.setTemperatureForunit(df.format(temperature) + " " + tempUnit);
            } else {
                onuCatvInfo.setTemperatureForunit("--");
            }
        }
    }

    /**
     * 刷新单个ONU
     * 
     * @return
     */
    public String refreshOnu() {
        uniVlanProfileService.refreshProfileAndRule(entityId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        // TODO 用新的刷新单个onu的流程,需要测试
        // onuDeviceService.refreshOnu(onuId, entityId, onuAttribute.getOnuIndex());
        onuAssemblyService.refreshOnuQuality(onuAttribute.getEntityId(), onuId, onuAttribute.getOnuIndex());
        onuRefreshService.refreshOnu(onuAttribute.getEntityId(), onuAttribute.getOnuIndex(), onuId);
        //gpon的ONU刷新版本信息，需要实时显示在界面上，所以从异步刷新移到同步刷新
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())){
            try {
                gponOnuService.refreshGponOnuSoftware(entityId, onuAttribute.getOnuIndex());
                logger.info("refreshGponOnuSoftware finish");
            } catch (Exception e) {
                logger.error("refreshGponOnuSoftware error:", e);
            }
        }
        
        return NONE;
    }

    /**
     * 获取ONU的在线时长
     * 
     * @return
     * @throws IOException
     */
    public String getOnuUpTime() throws IOException {
        JSONObject json = new JSONObject();
        Long currentTime = System.currentTimeMillis();
        Long uptime;
        EntitySnap snap = entityService.getEntitySnapById(onuId);
        if (snap != null) {
            OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
            if (snap.isState() != null && snap.isState()) {
                uptime = (currentTime - onuAttribute.getChangeTime().getTime()) / 1000
                        + onuAttribute.getOnuTimeSinceLastRegister();
            } else {
                uptime = -1L;
            }
        } else {
            uptime = 0l;
        }
        json.put("sysUpTime", uptime);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取用户保存的设备快照页面视图
     * 
     * @author flackyang
     * @since 2013-11-07
     * @param typeId
     * @return
     */
    private Properties getPortalView(long typeId) {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule(Long.toString(typeId));
        userPre.setUserId(uc.getUserId());
        Properties portalView = userPreferencesService.getModulePreferences(userPre);
        return portalView;
    }

    public String showOnuUniList() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        return SUCCESS;
    }

    public String showOnuAlert() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if (onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)) {
            gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
            return "GponOnu";
        } else {
            return SUCCESS;
        }
    }

    public String showOnuPerf() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if (onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)) {
            gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
            return "GponOnu";
        } else {
            return SUCCESS;
        }
    }

    public String showOnuAlertHistory() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        return SUCCESS;
    }

    public String showOnuHostList() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if (onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)) {
            gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
        }
        return SUCCESS;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getLeftPartItems() {
        return leftPartItems;
    }

    public void setLeftPartItems(String leftPartItems) {
        this.leftPartItems = leftPartItems;
    }

    public String getRightPartItems() {
        return rightPartItems;
    }

    public void setRightPartItems(String rightPartItems) {
        this.rightPartItems = rightPartItems;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getModule() {
        return module;
    }

    public Entity getOlt() {
        return olt;
    }

    public void setOlt(Entity olt) {
        this.olt = olt;
    }

    public Entity getOnu() {
        return onu;
    }

    public void setOnu(Entity onu) {
        this.onu = onu;
    }

    public OltOnuAttribute getOnuAttribute() {
        return onuAttribute;
    }

    public void setOnuAttribute(OltOnuAttribute onuAttribute) {
        this.onuAttribute = onuAttribute;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public OltOnuCapability getOltOnuCapability() {
        return oltOnuCapability;
    }

    public void setOltOnuCapability(OltOnuCapability oltOnuCapability) {
        this.oltOnuCapability = oltOnuCapability;
    }

    public OltOnuPonAttribute getOnuPonAttribute() {
        return onuPonAttribute;
    }

    public void setOnuPonAttribute(OltOnuPonAttribute onuPonAttribute) {
        this.onuPonAttribute = onuPonAttribute;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public OnuCatvInfo getOnuCatvInfo() {
        return onuCatvInfo;
    }

    public void setOnuCatvInfo(OnuCatvInfo onuCatvInfo) {
        this.onuCatvInfo = onuCatvInfo;
    }

    public OnuWanConfig getOnuWanConfig() {
        return onuWanConfig;
    }

    public void setOnuWanConfig(OnuWanConfig onuWanConfig) {
        this.onuWanConfig = onuWanConfig;
    }

    public GponOnuCapability getGponOnuCapability() {
        return gponOnuCapability;
    }

    public void setGponOnuCapability(GponOnuCapability gponOnuCapability) {
        this.gponOnuCapability = gponOnuCapability;
    }

    public GponOnuInfoSoftware getGponOnuInfoSoftware() {
        return gponOnuInfoSoftware;
    }

    public void setGponOnuInfoSoftware(GponOnuInfoSoftware gponOnuInfoSoftware) {
        this.gponOnuInfoSoftware = gponOnuInfoSoftware;
    }

}
