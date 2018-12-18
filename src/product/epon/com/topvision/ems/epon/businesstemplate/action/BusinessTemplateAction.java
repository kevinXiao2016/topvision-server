package com.topvision.ems.epon.businesstemplate.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.ems.epon.businesstemplate.service.BusinessTemplateService;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("businessTemplateAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BusinessTemplateAction extends BaseAction {
    private static final long serialVersionUID = -3813895282573496583L;

    private Integer profileId;
    private Long entityId;
    private Integer srvBindCap;
    private Integer portId;
    private Integer vlanTransId;
    private Integer transOldVlan;
    private Integer transNewVlan;

    private OnuSrvProfile srvProfile;
    private OnuPortVlanProfile vlanProfile;
    private OnuIgmpProfile igmpProfile;
    private OnuCapability capability;

    @Autowired
    private BusinessTemplateService businessTemplateService;


    /**
     * 展示ONU业务模板页面
     * @return
     */
    public String showOnuProfile() {
        return SUCCESS;
    }

    /**
     * 加载ONU业务模板
     * @return
     * @throws IOException
     */
    public String loadOnuProfile() throws IOException {
        List<OnuSrvProfile> profileList = businessTemplateService.getOnuSrvProfiles(entityId);
        JSONArray profileArr = JSONArray.fromObject(profileList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     * 添加ONU业务模板
     * @return
     */
    public String addOnuProfle() {
        businessTemplateService.addOnuSrvProfile(srvProfile);
        return NONE;
    }

    /**
     * 修改为ONU业务模板
     * @return
     */
    public String modifyOnuProfile() {
        businessTemplateService.updateOnuSrvProfile(srvProfile);
        return NONE;
    }

    /**
     * 删除ONU业务模板
     * @return
     */
    public String deleteOnuProfile() {
        businessTemplateService.deleteOnuSrvProfile(srvProfile);
        return NONE;
    }

    /**
     * 刷新ONU业务模板数据
     * @return
     */
    public String refreshOnuProfile() {
        businessTemplateService.refreshOnuSrvProfile(entityId);
        return NONE;
    }

    /**
     * 展示ONU VLAN业务模板页面
     * @return
     */
    public String showVlanProfile() {
        return SUCCESS;
    }

    /**
     * 加载ONU VLAN业务模板
     * @return
     * @throws Exception
     */
    public String loadVlanProfile() throws Exception {
        List<OnuPortVlanProfile> profileList = businessTemplateService.getOnuPortVlanProfiles(entityId, profileId);
        JSONArray profileArr = JSONArray.fromObject(profileList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     * 添加ONU VLAN业务模板
     * @return
     */
    public String addVlanProfile() {
        businessTemplateService.addOnuPortVlanProfile(vlanProfile);
        return NONE;
    }

    /**
     * 修改ONU VLAN业务模板
     * @return
     */
    public String modifyVlanProfile() {
        businessTemplateService.updateOnuPortVlanProfile(vlanProfile);
        return NONE;
    }

    /**
     * 删除ONU VLAN业务模板
     * @return
     */
    public String deleteVlanProfile() {
        businessTemplateService.deleteOnuPortVlanProfile(vlanProfile);
        return NONE;
    }

    /**
     * 刷新ONU VLAN业务模板数据
     * @return
     */
    public String refreshVlanProfile() {
        businessTemplateService.refreshOnuPortVlanProfile(entityId);
        return NONE;
    }

    /**
     * 展示ONU IGMP业务模板页面
     * @return
     */
    public String showIgmpProfile() {
        return SUCCESS;
    }

    /**
     * 加载IGMP业务模板
     * @return
     * @throws IOException 
     */
    public String loadIgmpProfile() throws IOException {
        List<OnuIgmpProfile> profileList = businessTemplateService.getOnuIgmpProfiles(entityId, profileId);
        JSONArray profileArr = JSONArray.fromObject(profileList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     * 添加IGMP业务模板
     * @return
     */
    public String addIgmpProfile() {
        businessTemplateService.addOnuIgmpProfile(igmpProfile);
        return NONE;
    }

    /**
     * 修改IGMP业务模板
     * @return
     */
    public String modifyIgmpProfile() {
        businessTemplateService.updateOnuIgmpProfile(igmpProfile);
        return NONE;
    }

    /**
     * 删除IGMP业务模板
     * @return
     */
    public String deleteIgmpProfile() {
        businessTemplateService.deleteOnuIgmpProfile(igmpProfile);
        return NONE;
    }

    /**
     * 刷新IGMP业务模板数据
     * @return
     */
    public String refreshIgmpProfile() {
        businessTemplateService.refreshOnuIgmpProfile(entityId);
        businessTemplateService.refreshOnuSrvIgmpVlanTrans(entityId);
        return NONE;
    }

    /**
     * 展示ONU能力集模板页面
     * @return
     */
    public String showCapabilityProfile() {
        return SUCCESS;
    }

    /**
     * 加载ONU能力集模板列表
     * @return
     * @throws Exception
     */
    public String loadCapabilityProfile() throws Exception {
        List<OnuCapability> profileList = businessTemplateService.getOnuCapabilitys(entityId);
        JSONArray profileArr = JSONArray.fromObject(profileList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     * 添加ONU能力集模板
     * @return
     */
    public String addCapabilityProfile() {
        businessTemplateService.addOnuCapability(capability);
        return NONE;
    }

    /**
     * 删除ONU能力集模板
     * @return
     */
    public String deleteCapabilityProfile() {
        businessTemplateService.deleteOnuCapability(capability);
        return NONE;
    }

    /**
     * 刷新ONU能力集模板数据
     * @return
     */
    public String refreshCapabilityProfile() {
        businessTemplateService.refreshOnuCapability(entityId);
        return NONE;
    }

    /**
     * 获取IGMP VLAN列表
     * @return
     * @throws Exception 
     */
    public String loadIgmpVlanList() throws Exception {
        List<Integer> igmpVlanList = businessTemplateService.getIgmpVlanList(entityId);
        JSONArray vlanList = convertToZtreeFormat(igmpVlanList);
        writeDataToAjax(vlanList);
        return NONE;
    }

    /**
     * 将集合转换成ztree要求的格式
     * @param vlanList
     * @return
     */
    private JSONArray convertToZtreeFormat(List<Integer> list) {
        JSONArray igmpList = new JSONArray();
        for (Integer value : list) {
            JSONObject json = new JSONObject();
            json.put("id", value);
            json.put("pId", -1);
            json.put("name", value);
            json.put("open", true);
            json.put("chkDisabled", false);
            json.put("checked", false);
            igmpList.add(json);
        }
        return igmpList;
    }

    /**
     * 获取IGMP转换列表
     * @return
     * @throws Exception
     */
    public String getIgmpVlanTransIds() throws Exception {
        List<Integer> tranIds = businessTemplateService.getIgmpVlanTransIds(entityId);
        JSONArray idList = convertToZtreeFormat(tranIds);
        writeDataToAjax(idList);
        return NONE;
    }

    /**
     * 解绑定能力集
     * @return
     */
    public String unBindCapability() {
        businessTemplateService.unBindCapability(srvProfile);
        return NONE;
    }

    /**
     * 显示业务模板下的VLAN配置
     * @return
     */
    public String showProfileVlanConfig() {
        return SUCCESS;
    }

    /**
     * 显示业务模板下的IGMP配置
     * @return
     */
    public String showProfileIgmpConfig() {
        return SUCCESS;
    }

    /**
     * 展示ONU IGMP VLAN转换列表
     * 
     * @return
     */
    public String showOnuIgmpVlanTrans() {
        return SUCCESS;
    }

    /**
     * 加载ONU IGMP VLAN转换列表
     * 
     * @return
     */
    public String loadOnuIgmpVlanTrans() {
        List<OnuSrvIgmpVlanTrans> igmpVlanTrans = businessTemplateService.loadOnuIgmpVlanTrans(entityId, profileId,
                portId);
        JSONArray vlanTransArr = JSONArray.fromObject(igmpVlanTrans);
        writeDataToAjax(vlanTransArr);
        return NONE;
    }

    /**
     * 新增ONU IGMP VLAN转换列表
     * 
     * @return
     */
    public String addOnuIgmpVlanTrans() {
        OnuSrvIgmpVlanTrans igmpVlanTrans = new OnuSrvIgmpVlanTrans();
        igmpVlanTrans.setEntityId(entityId);
        igmpVlanTrans.setIgmpProfileId(profileId);
        igmpVlanTrans.setIgmpPortId(portId);
        igmpVlanTrans.setIgmpVlanTransId(vlanTransId);
        igmpVlanTrans.setTransOldVlan(transOldVlan);
        igmpVlanTrans.setTransNewVlan(transNewVlan);
        businessTemplateService.addOnuSrvIgmpVlanTrans(igmpVlanTrans);
        return NONE;
    }

    /**
     * 删除ONU IGMP VLAN转换列表
     * 
     * @return
     */
    public String deleteOnuIgmpVlanTrans() {
        OnuSrvIgmpVlanTrans igmpVlanTrans = new OnuSrvIgmpVlanTrans();
        igmpVlanTrans.setEntityId(entityId);
        igmpVlanTrans.setIgmpProfileId(profileId);
        igmpVlanTrans.setIgmpPortId(portId);
        igmpVlanTrans.setIgmpVlanTransId(vlanTransId);
        businessTemplateService.deleteOnuSrvIgmpVlanTrans(igmpVlanTrans);
        return NONE;
    }

    public String refreshOnuIgmpVlanTrans() {
        businessTemplateService.refreshOnuSrvIgmpVlanTrans(entityId);
        return NONE;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public OnuCapability getCapability() {
        return capability;
    }

    public void setCapability(OnuCapability capability) {
        this.capability = capability;
    }

    public OnuIgmpProfile getIgmpProfile() {
        return igmpProfile;
    }

    public void setIgmpProfile(OnuIgmpProfile igmpProfile) {
        this.igmpProfile = igmpProfile;
    }

    public OnuSrvProfile getSrvProfile() {
        return srvProfile;
    }

    public void setSrvProfile(OnuSrvProfile srvProfile) {
        this.srvProfile = srvProfile;
    }

    public OnuPortVlanProfile getVlanProfile() {
        return vlanProfile;
    }

    public void setVlanProfile(OnuPortVlanProfile vlanProfile) {
        this.vlanProfile = vlanProfile;
    }

    public Integer getSrvBindCap() {
        return srvBindCap;
    }

    public void setSrvBindCap(Integer srvBindCap) {
        this.srvBindCap = srvBindCap;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public Integer getVlanTransId() {
        return vlanTransId;
    }

    public void setVlanTransId(Integer vlanTransId) {
        this.vlanTransId = vlanTransId;
    }

    public Integer getTransOldVlan() {
        return transOldVlan;
    }

    public void setTransOldVlan(Integer transOldVlan) {
        this.transOldVlan = transOldVlan;
    }

    public Integer getTransNewVlan() {
        return transNewVlan;
    }

    public void setTransNewVlan(Integer transNewVlan) {
        this.transNewVlan = transNewVlan;
    }

}
