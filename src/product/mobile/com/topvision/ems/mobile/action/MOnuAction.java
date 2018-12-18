/***********************************************************************
 * $Id: MOnuAction.java,v1.0 2016年7月16日 上午10:31:12 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.ems.epon.onucpe.service.OnuCpeService;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.epon.optical.service.OltOpticalService;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.service.GponOnuAuthService;
import com.topvision.ems.mobile.domain.MobileOnu;
import com.topvision.ems.mobile.service.MOnuService;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2016年7月16日-上午10:31:12 手机网管ONU管理
 */
@Controller("mOnuAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MOnuAction extends BaseAction {
    private static final long serialVersionUID = 424854281283919170L;

    private Long onuId;
    private Long entityId;
    private Long uniId;
    private Long uniIndex;
    private Long onuIndex;
    private Long ponId;
    private Integer uniPvid;
    private Integer vlanMode;
    private OltOnuPonAttribute onuPonAttribute;
    private OltOnuAttribute onuAttribute;
    private Integer gponOnuUniPri;
    private Integer gponOnuUniPvid;
    @Autowired
    private MOnuService mOnuService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuCpeService onuCpeService;
    @Autowired
    private OltOpticalService oltOpticalService;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private GponOnuService gponOnuService;
    @Autowired
    private GponOnuAuthService gponOnuAuthService;

    /**
     * ONU基本信息展示 1、基本信息需要展示的内容包括设备类型、设备别名、管理IP、在线状态、在线时间、设备位置、芯片厂商、芯片类型、软件版本、LLID
     * GPON onu也要支持
     * @return
     */
    public String getOnuBaseInfo() {
        JSONObject json = new JSONObject();
        MobileOnu onu=mOnuService.getOnuBaseInfo(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if (onu == null) {
            json.put("deviceExist", false);
        }else{
            onu.setSysUpTimeString(CmcUtil.timeFormatToZh(onu.getSysUpTime()));
            json = JSONObject.fromObject(onu);
            json.put("deviceExist", true);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取ONU能力信息 1、能力信息需要展示的内容包括GE端口数、FE端口数、GE端口号、FE端口号、上行队列数、下行队列数、上行端口最大队列数、下行端口最大队列数、FEC使能状态
     * 支持GPON onu
     * @return
     */
    public String getOnuCapabilityInfo() {
        onuAttribute = onuService.getOnuAttribute(onuId);
        if(GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())){
            GponOnuCapability gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
            JSONObject json = JSONObject.fromObject(gponOnuCapability);
            json.put("isGpon", true);
            writeDataToAjax(json);
        }else{
            OltOnuCapability onuCapa = mOnuService.getOnuCapabilityInfo(onuId);
            JSONObject json = JSONObject.fromObject(onuCapa);
            json.put("isGpon", false);
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * ONU 光功率信息展示 1、光功率信息需要展示的内容包括ONU PON光接收功率、ONU PON光发送功率、上联PON口光接收功率、上联PON口光发送功率、偏置电流、工作电压、光模块温度
     * 2、首次打开页面从数据库读取数据 3、点击刷新按钮从设备实时读取，并刷新到界面
     * 
     * @return
     */
    public String getOnuOpticalInfo() {
        onuPonAttribute = onuService.getOnuPonAttributeByOnuId(onuId);
        OltPonOptical ponOptical = mOnuService.getOnuLinkPonOptical(onuId);
        if (ponOptical != null) {
            String ponTxOpticalStr = "";
            String ponReOpticalStr = "";
            if (ponOptical.getTransPower() != null) {
                ponTxOpticalStr = NumberUtils.TWODOT_FORMAT.format(ponOptical.getTransPower()) + " dBm";
            } else {
                ponTxOpticalStr = "--";
            }
            if(ponOptical.getRevPower() != null){
                ponReOpticalStr = NumberUtils.TWODOT_FORMAT.format(ponOptical.getRevPower()) + " dBm";
            } else {
                ponReOpticalStr = "--";
            }
            onuPonAttribute.setOltPonTransPowerStr(ponTxOpticalStr);
            onuPonAttribute.setOltPonRevPowerStr(ponReOpticalStr);
        }
        writeDataToAjax(onuPonAttribute);
        return NONE;
    }

    /**
     * 刷新ONU光功率信息
     * 
     * @return
     */
    public String refreshOnuOpticalInfo() {
        try {
            OltOnuAttribute onuAttr = onuService.getOnuAttribute(onuId);
            Long entityId = onuAttr.getEntityId();
            Long onuPonIndex = mOnuService.getOnuPonIndex(entityId, onuId);
            Integer slotNum = EponIndex.getSlotNo(onuPonIndex).intValue();
            Integer ponNum = EponIndex.getPonNo(onuPonIndex).intValue();
            Long ponIndex = EponIndex.getPonIndex(slotNum, ponNum);
            OnuPonOptical re = oltOpticalService.loadOnuPonOptical(entityId, onuPonIndex);
            oltOpticalService.loadOltPonOptical(entityId, ponIndex);
            // 刷新完成后重新查找数据
            onuPonAttribute = onuService.getOnuPonAttributeByOnuId(onuId);
            if (onuPonAttribute != null && re != null) {
                onuPonAttribute.setOnuWorkingVoltage(re.getWorkingVoltage());
                onuPonAttribute.setOnuWorkingTemperature(re.getWorkingTemp());
                onuPonAttribute.setOnuBiasCurrent(re.getBiasCurrent());
                onuPonAttribute.setOnuTramsmittedOpticalPower(re.getTxPower());
                onuPonAttribute.setOnuReceivedOpticalPower(re.getRxPower());
            }
            OltPonOptical ponOptical = mOnuService.getOnuLinkPonOptical(onuId);
            if (ponOptical != null) {
                String ponTxOpticalStr = NumberUtils.TWODOT_FORMAT.format(ponOptical.getTransPower()) + " dBm";
                String ponReOpticalStr = "";
                if (ponOptical.getRevPower() != null) {
                    ponReOpticalStr = NumberUtils.TWODOT_FORMAT.format(ponOptical.getRevPower()) + " dBm";
                } else {
                    ponReOpticalStr = "-- dBm";
                }
                onuPonAttribute.setOltPonTransPowerStr(ponTxOpticalStr);
                onuPonAttribute.setOltPonRevPowerStr(ponReOpticalStr);
            }
        } catch (Exception e) {
            logger.info("refreshOnuOpticalInfo error {}", e);
        }
        writeDataToAjax(onuPonAttribute);
        return NONE;
    }

    /**
     * ONU UNI口列表展示 1、UNI口需要展示的内容包括端口号、端口连接状态、PVID、VLAN模式 2、首次打开页面从数据库获取 3、点击刷新按钮从设备实时读取并刷新到界面
     * 
     * @return
     */
    public String getOnuUniList() {
        onuAttribute = onuService.getOnuAttribute(onuId);
        JSONObject obj=new JSONObject();
        if(GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())){
            List<GponUniAttribute> unilist = gponOnuService.loadGponOnuUniList(onuId);
            JSONArray objArr=JSONArray.fromObject(unilist);
            obj.put("data", objArr);
            obj.put("isGpon", true);
            writeDataToAjax(obj);
        }else{
            List<UniPort> list = onuAssemblyService.loadUniList(onuId);
            JSONArray objArr=JSONArray.fromObject(list);
            obj.put("data", objArr);    
            obj.put("isGpon", false);
            writeDataToAjax(obj);
        }
        return NONE;
    }

    /**
     * 修改UNI端口VLAN信息
     * 
     * @return
     */
    public String modifyUniVlanInfo() {
        JSONObject result = new JSONObject();
        try {
            PortVlanAttribute uniVlan = new PortVlanAttribute();
            uniVlan.setEntityId(entityId);
            uniVlan.setPortIndex(uniIndex);
            uniVlan.setPortId(uniId);
            uniVlan.setVlanPVid(uniPvid);
            uniVlan.setVlanMode(vlanMode);
            uniVlanService.updateUniVlanAttribute(uniVlan);
            result.put("success", true);
        } catch (Exception e) {
            logger.error("", e);
            result.put("success", false);
        }
        writeDataToAjax(result);
        return NONE;
    }
    
    public String setGponUniInfo() {
        JSONObject result = new JSONObject();
        try {
            gponOnuService.setUniVlanConfig(entityId, uniId, gponOnuUniPri, gponOnuUniPvid);
            result.put("success", true);
        } catch (Exception e) {
            logger.error("", e);
            result.put("success", false);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 刷新ONU UNI列表信息
     * 
     * @return
     */
    public String refreshOnuUniList() {
        JSONObject result = new JSONObject();
        try {
            onuAssemblyService.refreshOnuUniInfo(onuId);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * ONU CPE列表展示 1、CPE需要展示的内容包括CPE MAC、MAC地址学习类型、VLAN 2、以上数据都是从设备实时读取
     * 
     * @return
     */
    public String getOnuCpeList() {
        onuCpeService.refreshOnuUniCpe(onuId);
        List<OnuUniCpe> list = onuCpeService.loadOnuUniCpeList(onuId);
        writeDataToAjax(list);
        return NONE;
    }

    /**
     * 重启ONU
     * 
     * @return
     */
    public String rebootOnu() {
        JSONObject json = new JSONObject();
        try {
            onuService.resetOnu(entityId, onuId);
            json.put("result", true);
        } catch (Exception e) {
            json.put("result", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 解注册ONU
     * 
     * @return
     */
    public String deregisterOnu() {
        JSONObject json = new JSONObject();
        try {
            onuService.deregisterOnu(entityId, onuId);
            json.put("result", true);
        } catch (Exception e) {
            json.put("result", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String deleteOnuAuth() {
        JSONObject json = new JSONObject();
        try {
            OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
            if(onuAttribute.getOnuEorG().equals(EponConstants.EPON_ONU)){
                onuIndex = onuAttribute.getOnuIndex();
                OltAuthentication auth = onuAuthService.getOltAuthenticationByIndex(entityId, onuIndex);
                ponId = auth.getPonId();
                onuAuthService.deleteAuthenPreConfig(entityId, ponId, onuIndex, auth.getAuthType());
            }else{
                GponOnuAuthConfig gponOnuAuthConfig = new GponOnuAuthConfig();
                gponOnuAuthConfig.setEntityId(onuAttribute.getEntityId());
                gponOnuAuthConfig.setOnuId(onuAttribute.getOnuId());
                gponOnuAuthService.deleteGponOnuAuth(gponOnuAuthConfig);
            }
            json.put("result", true);
        } catch (Exception e) {
            json.put("result", false);
        }
        writeDataToAjax(json);
        return NONE;
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

    public OltOnuPonAttribute getOnuPonAttribute() {
        return onuPonAttribute;
    }

    public void setOnuPonAttribute(OltOnuPonAttribute onuPonAttribute) {
        this.onuPonAttribute = onuPonAttribute;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Integer getUniPvid() {
        return uniPvid;
    }

    public void setUniPvid(Integer uniPvid) {
        this.uniPvid = uniPvid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getGponOnuUniPri() {
        return gponOnuUniPri;
    }

    public void setGponOnuUniPri(Integer gponOnuUniPri) {
        this.gponOnuUniPri = gponOnuUniPri;
    }

    public Integer getGponOnuUniPvid() {
        return gponOnuUniPvid;
    }

    public void setGponOnuUniPvid(Integer gponOnuUniPvid) {
        this.gponOnuUniPvid = gponOnuUniPvid;
    }
 
}
