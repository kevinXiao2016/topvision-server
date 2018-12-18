/***********************************************************************
 * $Id: OnuManageAction.java,v1.0 2016-4-12 上午9:12:20 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.service.ElectricityOnuService;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.UniService;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author lizongtian
 * @created @2016-4-12-上午9:12:20
 *
 */
@Controller("onuManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuManageAction extends BaseAction {

    private static final long serialVersionUID = -4349796601608883075L;
    private Long onuId;
    private Long uniId;
    private Long entityId;
    private OltUniAttribute uniAttribute;
    private OltUniExtAttribute uniExtAttribute;
    private OltUniPortRateLimit uniRateLimit;
    private PortVlanAttribute uniPortVlan;
    private UniVlanBindTable uniBindInfo;
    private UniVlanProfileTable uniVlanProfile;
    private Long macLearnMaxNum;
    @Autowired
    private OnuService onuService;
    @Autowired
    private UniService uniService;
    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private ElectricityOnuService electricityOnuService;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;
    private JSONArray uniList;
    private Boolean portEnable;
    private Integer pvid;
    private Integer vlanMode;
    private Integer profileId;
    private Boolean inDirEnable;
    private Integer inCir;
    private Integer cbs;
    private Integer ebs;
    private Boolean outDirEnable;
    private Integer pir;
    private Integer outCir;
    private Boolean autonegotiation;
    private Boolean flowControlEnable;
    private Integer uniAutoType;
    private Integer priority;
    private Boolean perfEnable;
    private Long uniIndex;
    private List<VlanTranslationRule> vlanTranslationRuleList;
    private List<VlanAggregationRule> vlanAggregationRuleList;
    private VlanTrunkRule trunkRule;
    private Long sourceIndex;

    public String showUniServiceConfig() {
        List<UniPort> list = onuAssemblyService.loadUniList(onuId);
        if (!list.isEmpty() && uniId == null) {
            uniId = list.get(0).getUniId();
        }
        uniList = JSONArray.fromObject(list);
        return SUCCESS;
    }

    /**
     * 加载ONU的业务配置
     * 
     * @return
     * @throws IOException
     */
    public String loadUniServiceConfig() throws IOException {
        JSONObject json = new JSONObject();
        OltUniAttribute uniAttribute = onuService.getOnuUniAttribute(uniId);
        UniVlanBindTable uniBindInfo = uniVlanProfileService.getUniBindInfo(uniId, entityId);
        if (uniBindInfo != null && uniBindInfo.getBindProfileId() > EponConstants.UNIVALN_UNBIND_PROFILEID) {
            uniVlanProfile = uniVlanProfileService.getUniVlanProfile(entityId, uniBindInfo.getBindProfileId());
        } else {
            uniVlanProfile = new UniVlanProfileTable();
        }
        OltUniExtAttribute oltUniExtAttribute = uniService.getOltUniExtAttribute(uniId);
        PortVlanAttribute uniVlanAttribute = uniVlanService.getUniVlanAttribute(uniId);
        /*
         * if (uniVlanAttribute.getVlanMode() != null) { switch (uniVlanAttribute.getVlanMode()) {
         * case 0: case 1: break; case 2: vlanTranslationRuleList =
         * uniVlanService.getVlanTranslationRuleList(uniId); json.put("translation",
         * vlanTranslationRuleList); case 3: vlanAggregationRuleList =
         * uniVlanService.getVlanAggregationRuleList(uniId); json.put("aggr",
         * vlanTranslationRuleList); case 4: trunkRule = uniVlanService.getVlanTrunkRules(uniId);
         * json.put("trunkRule", trunkRule); case 5: break; default: break; } }
         */
        OltUniPortRateLimit uniRateLimitInfo = uniService.getUniRateLimitInfo(uniId);
        Long maclearn = uniService.getUniAttrMacAddrLearnMaxNum(uniId);
        json.put("uniId", uniId);
        json.put("uniIndex", oltUniExtAttribute.getUniIndex());
        json.put("uniAdminStatus", uniAttribute.getUniAdminStatus());
        json.put("vlanPVid", uniVlanAttribute.getVlanPVid());
        json.put("vlanMode", uniVlanAttribute.getVlanMode());
        json.put("uniPortVlanPortString", uniVlanAttribute.getPortString());
        json.put("uniPortInRateLimitEnable", uniRateLimitInfo.getUniPortInRateLimitEnable());
        json.put("uniPortInCIR", uniRateLimitInfo.getUniPortInCIR());
        json.put("uniPortInCBS", uniRateLimitInfo.getUniPortInCBS());
        json.put("uniPortInEBS", uniRateLimitInfo.getUniPortInEBS());
        json.put("uniPortOutRateLimitEnable", uniRateLimitInfo.getUniPortOutRateLimitEnable());
        json.put("uniPortOutPIR", uniRateLimitInfo.getUniPortOutPIR());
        json.put("uniPortOutCIR", uniRateLimitInfo.getUniPortOutCIR());
        json.put("uniAutoNegotiationEnable", uniAttribute.getUniAutoNegotiationEnable());
        json.put("flowCtrl", oltUniExtAttribute.getFlowCtrl());
        json.put("topUniAttrAutoNegotiationAdvertisedTechAbilityInteger",
                oltUniExtAttribute.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger() == null
                        || oltUniExtAttribute.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger() == -1 ? 0
                        : oltUniExtAttribute.getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger());
        json.put("uniUSUtgPri", oltUniExtAttribute.getUniUSUtgPri());
        json.put("macLearnMaxNum", maclearn == null ? 0 : maclearn);
        json.put("perfStats15minuteEnable", oltUniExtAttribute.getPerfStats15minuteEnable());
        json.put("profileName", uniVlanProfile.getProfileName() != null ? uniVlanProfile.getProfileName() : "--");
        json.put("profileId", uniVlanProfile.getProfileId() == null ? 0 : uniVlanProfile.getProfileId());
        json.put("bindProfAttr", uniBindInfo != null ? uniBindInfo.getBindProfAttr() : "--");
        writeDataToAjax(json);
        return NONE;
    }

    public String setPortEnabled() {
        Integer portStatus = portEnable ? 1 : 2;
        uniService.setUniAdminStatus(entityId, uniId, portStatus);
        return NONE;
    }

    public String setUniVlan() {
        PortVlanAttribute uniVlan = new PortVlanAttribute();
        uniVlan.setEntityId(entityId);
        uniVlan.setPortIndex(uniIndex);
        uniVlan.setPortId(uniId);
        uniVlan.setVlanPVid(pvid);
        uniVlan.setVlanMode(vlanMode);
        if (sourceIndex != null && !uniIndex.equals(sourceIndex)) {
            uniVlanService.copyUniVlanAttribute(entityId, sourceIndex, uniIndex);
        } else {
            uniVlanService.updateUniVlanAttribute(uniVlan);
        }
        return NONE;
    }

    public String setPortRateLimit() {
        OltUniPortRateLimit oltUniPortRateLimit = new OltUniPortRateLimit();
        oltUniPortRateLimit.setEntityId(entityId);
        oltUniPortRateLimit.setUniId(uniId);
        oltUniPortRateLimit.setUniPortInCBS(cbs);
        oltUniPortRateLimit.setUniPortInCIR(inCir);
        oltUniPortRateLimit.setUniPortInEBS(ebs);
        oltUniPortRateLimit.setUniPortInRateLimitEnable(inDirEnable ? 1 : 2);
        oltUniPortRateLimit.setUniPortOutCIR(outCir);
        oltUniPortRateLimit.setUniPortOutPIR(pir);
        oltUniPortRateLimit.setUniPortOutRateLimitEnable(outDirEnable ? 1 : 2);
        uniService.modifyUniRateLimitInfo(oltUniPortRateLimit);
        return NONE;
    }

    public String setPortWorkMode() {
        uniService.setUniAutoNegoEnable(entityId, uniId, autonegotiation ? 1 : 2);
        uniService.setUniFlowCtrlEnable(entityId, uniId, flowControlEnable ? 1 : 2);
        if (!autonegotiation && uniAutoType != 0) {
            uniService.updateOltUniAutoNegotiationMode(entityId, uniId, uniAutoType);
        }
        return NONE;
    }

    public String setUniUSUtgPri() {
        electricityOnuService.setUniUSUtgPri(entityId, uniId, priority);
        return NONE;
    }

    public String setUniMacLearnNum() {
        uniService.modifyUniMacAddrLearnMaxNum(entityId, uniId, macLearnMaxNum);
        return NONE;
    }

    public String setUniPerfStatus() {
        uniService.setUni15minEnable(entityId, uniId, perfEnable ? 1 : 2);
        return NONE;
    }

    /**
     * 应用到其他ONU
     * 
     * @return
     */
    public String applyServiceConfig() {
        List<UniPort> list = onuAssemblyService.loadUniList(onuId);
        uniList = JSONArray.fromObject(list);
        return SUCCESS;
    }

    public String loadOnuUniList() {
        return NONE;
    }

    public String loadUniPortConfig() {
        return NONE;
    }

    public String refreshUniConfig() {
        OltUniExtAttribute uniExtAttribute = uniService.getOltUniExtAttribute(uniId);
        Long uniIndex = uniExtAttribute.getUniIndex();
        OltUniAttribute uniAttribute = new OltUniAttribute();
        OltUniExtAttribute oltUniExtAttribute = new OltUniExtAttribute();
        PortVlanAttribute uniVlanAttribute = new PortVlanAttribute();
        OltUniPortRateLimit uniRateLimitInfo = new OltUniPortRateLimit();
        uniAttribute.setEntityId(onuId);
        uniAttribute.setUniIndex(uniIndex);

        oltUniExtAttribute.setEntityId(onuId);
        oltUniExtAttribute.setUniIndex(uniIndex);

        uniVlanAttribute.setEntityId(onuId);
        uniVlanAttribute.setPortIndex(uniIndex);

        uniRateLimitInfo.setEntityId(onuId);
        uniRateLimitInfo.setUniIndex(uniIndex);

        return NONE;
    }

    public String showApplyOtherPortView() throws IOException {
        List<UniPort> list = onuAssemblyService.loadUniList(onuId);
        uniList = JSONArray.fromObject(list);
        return SUCCESS;
    }

    public String applyPortConfig() {
        return NONE;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public OltUniAttribute getUniAttribute() {
        return uniAttribute;
    }

    public void setUniAttribute(OltUniAttribute uniAttribute) {
        this.uniAttribute = uniAttribute;
    }

    public OltUniExtAttribute getUniExtAttribute() {
        return uniExtAttribute;
    }

    public void setUniExtAttribute(OltUniExtAttribute uniExtAttribute) {
        this.uniExtAttribute = uniExtAttribute;
    }

    public OltUniPortRateLimit getUniRateLimit() {
        return uniRateLimit;
    }

    public void setUniRateLimit(OltUniPortRateLimit uniRateLimit) {
        this.uniRateLimit = uniRateLimit;
    }

    public Long getMacLearnMaxNum() {
        return macLearnMaxNum;
    }

    public void setMacLearnMaxNum(Long macLearnMaxNum) {
        this.macLearnMaxNum = macLearnMaxNum;
    }

    public PortVlanAttribute getUniPortVlan() {
        return uniPortVlan;
    }

    public void setUniPortVlan(PortVlanAttribute uniPortVlan) {
        this.uniPortVlan = uniPortVlan;
    }

    public JSONArray getUniList() {
        return uniList;
    }

    public void setUniList(JSONArray uniList) {
        this.uniList = uniList;
    }

    public OnuService getOnuService() {
        return onuService;
    }

    public void setOnuService(OnuService onuService) {
        this.onuService = onuService;
    }

    public Boolean getPortEnable() {
        return portEnable;
    }

    public void setPortEnable(Boolean portEnable) {
        this.portEnable = portEnable;
    }

    public Integer getPvid() {
        return pvid;
    }

    public void setPvid(Integer pvid) {
        this.pvid = pvid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public Boolean getInDirEnable() {
        return inDirEnable;
    }

    public void setInDirEnable(Boolean inDirEnable) {
        this.inDirEnable = inDirEnable;
    }

    public Integer getInCir() {
        return inCir;
    }

    public void setInCir(Integer inCir) {
        this.inCir = inCir;
    }

    public Integer getCbs() {
        return cbs;
    }

    public void setCbs(Integer cbs) {
        this.cbs = cbs;
    }

    public Integer getEbs() {
        return ebs;
    }

    public void setEbs(Integer ebs) {
        this.ebs = ebs;
    }

    public Boolean getOutDirEnable() {
        return outDirEnable;
    }

    public void setOutDirEnable(Boolean outDirEnable) {
        this.outDirEnable = outDirEnable;
    }

    public Integer getPir() {
        return pir;
    }

    public void setPir(Integer pir) {
        this.pir = pir;
    }

    public Integer getOutCir() {
        return outCir;
    }

    public void setOutCir(Integer outCir) {
        this.outCir = outCir;
    }

    public Boolean getAutonegotiation() {
        return autonegotiation;
    }

    public void setAutonegotiation(Boolean autonegotiation) {
        this.autonegotiation = autonegotiation;
    }

    public Boolean getFlowControlEnable() {
        return flowControlEnable;
    }

    public void setFlowControlEnable(Boolean flowControlEnable) {
        this.flowControlEnable = flowControlEnable;
    }

    public Integer getUniAutoType() {
        return uniAutoType;
    }

    public void setUniAutoType(Integer uniAutoType) {
        this.uniAutoType = uniAutoType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getPerfEnable() {
        return perfEnable;
    }

    public void setPerfEnable(Boolean perfEnable) {
        this.perfEnable = perfEnable;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public UniVlanBindTable getUniBindInfo() {
        return uniBindInfo;
    }

    public void setUniBindInfo(UniVlanBindTable uniBindInfo) {
        this.uniBindInfo = uniBindInfo;
    }

    public UniVlanProfileTable getUniVlanProfile() {
        return uniVlanProfile;
    }

    public void setUniVlanProfile(UniVlanProfileTable uniVlanProfile) {
        this.uniVlanProfile = uniVlanProfile;
    }

    public List<VlanTranslationRule> getVlanTranslationRuleList() {
        return vlanTranslationRuleList;
    }

    public void setVlanTranslationRuleList(List<VlanTranslationRule> vlanTranslationRuleList) {
        this.vlanTranslationRuleList = vlanTranslationRuleList;
    }

    public List<VlanAggregationRule> getVlanAggregationRuleList() {
        return vlanAggregationRuleList;
    }

    public void setVlanAggregationRuleList(List<VlanAggregationRule> vlanAggregationRuleList) {
        this.vlanAggregationRuleList = vlanAggregationRuleList;
    }

    public VlanTrunkRule getTrunkRule() {
        return trunkRule;
    }

    public void setTrunkRule(VlanTrunkRule trunkRule) {
        this.trunkRule = trunkRule;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Long getSourceIndex() {
        return sourceIndex;
    }

    public void setSourceIndex(Long sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

}
