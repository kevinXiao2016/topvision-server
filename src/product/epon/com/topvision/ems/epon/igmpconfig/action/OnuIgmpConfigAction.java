/***********************************************************************
 * $Id: OnuIgmpConfigAction.java,v1.0 2016-6-6 下午4:12:19 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.epon.igmpconfig.service.OnuIgmpConfigService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2016-6-6-下午4:12:19
 *
 */
@Controller("onuIgmpConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuIgmpConfigAction extends BaseAction {
    private static final long serialVersionUID = -2106979303313517072L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private Long uniIndex;
    private Integer profileId;
    private Integer transIndex;
    private String uniName;
    private JSONArray uniListJson;
    private IgmpOnuConfig onuConfig;
    private IgmpUniConfig uniConfig;
    private IgmpUniVlanTrans vlanTrans;

    @Autowired
    private OnuIgmpConfigService onuIgmpConfigService;

    /**
     * 显示ONU IGMP配置页面
     * @return
     */
    public String showOnuIgmpConfig() {
        List<IgmpPortInfo> uniList = onuIgmpConfigService.getUniPortList(onuId);
        uniListJson = JSONArray.fromObject(uniList);
        return SUCCESS;
    }

    /**
     * 加载ONU　IGMP配置
     * @return
     */
    public String loadOnuIgmpConfig() {
        IgmpOnuConfig onuConfig = onuIgmpConfigService.getIgmpOnuConfig(entityId, onuIndex);
        JSONObject onuJson = JSONObject.fromObject(onuConfig);
        writeDataToAjax(onuJson);
        return NONE;
    }

    /**
     * 修改OUN IGMP配置
     * @return
     */
    public String modifyOnuIgmpConfig() {
        onuIgmpConfigService.modifyIgmpOnuConfig(onuConfig);
        return NONE;
    }

    /**
     * 刷新ONU IGMP配置
     * @return
     */
    public String refreshOnuIgmpConfig() {
        onuIgmpConfigService.refreshIgmpOnuConfig(entityId, onuIndex);
        return NONE;
    }

    /**
     * 加载UNI端口IGMP基本配置
     * @return
     */
    public String loadUniIgmpConfig() {
        IgmpUniConfig uniConfig = onuIgmpConfigService.getIgmpUniConfig(entityId, uniIndex);
        JSONObject uniJson = JSONObject.fromObject(uniConfig);
        writeDataToAjax(uniJson);
        return NONE;
    }

    /**
     * 修改UNI端口IGMP基本配置
     * @return
     */
    public String modifyUniIgmpConfig() {
        //当VLAN序列没有配置的时候,使用默认配置
        if (uniConfig != null && uniConfig.getUniVlanArray() == null) {
            uniConfig.setUniVlanArray(new ArrayList<Integer>());
        }
        onuIgmpConfigService.modifyIgmpUniConfig(uniConfig);
        return NONE;
    }

    /**
     * 刷新UNI端口IGMP配置
     * @return
     */
    public String refreshUniIgmpConfig() {
        onuIgmpConfigService.refreshIgmpUniConfig(entityId, uniIndex);
        return NONE;
    }

    /**
     * 显示UNI口VLAN转换列表配置页面
     * @return
     */
    public String showUniVlanTrans() {
    	uniName = EponIndex.getUniStringByIndex(uniIndex).toString();
        return SUCCESS;
    }

    /**
     * 加载UNI口VLAN转换列表
     * @return
     */
    public String loadUniVlanTransList() {
        List<IgmpUniVlanTrans> vlanTransList = onuIgmpConfigService.getUniVlanTransList(entityId, uniIndex);
        JSONArray transArr = JSONArray.fromObject(vlanTransList);
        writeDataToAjax(transArr);
        return NONE;
    }

    /**
     * 添加UNI口VLAN转换配置
     * @return
     */
    public String addUniVlanTrans() {
        onuIgmpConfigService.addUniVlanTrans(vlanTrans);
        return NONE;
    }

    /**
     * 删除UNI口VLAN转换配置
     * @return
     */
    public String deleteUniVlanTrans() {
        IgmpUniVlanTrans vlanTrans = new IgmpUniVlanTrans();
        vlanTrans.setEntityId(entityId);
        vlanTrans.setUniIndex(uniIndex);
        vlanTrans.setTransIndex(transIndex);
        onuIgmpConfigService.deleteUniVlanTrans(vlanTrans);
        return NONE;
    }

    /**
     * 刷新UNI口VLAN转换配置
     * @return
     */
    public String refreshUniVlanTrans() {
        onuIgmpConfigService.refreshUniVlanTrans(entityId);
        return NONE;
    }

    /**
     * 加载UNI端口绑定可控模板列表
     * @return
     */
    public String loadUniBindProfileList() {
        List<IgmpUniBindCtcProfile> bindList = onuIgmpConfigService.getBindCtcProfileList(entityId, uniIndex);
        JSONArray bindArr = JSONArray.fromObject(bindList);
        writeDataToAjax(bindArr);
        return NONE;
    }

    /**
     * 添加UNI端口绑定可控组播模板配置
     * @return
     */
    public String addUniBindProfile() {
        IgmpUniBindCtcProfile bindProfile = new IgmpUniBindCtcProfile();
        bindProfile.setEntityId(entityId);
        bindProfile.setUniIndex(uniIndex);
        bindProfile.setPortType(IgmpConstants.IGMP_PORTTYPE_UNI);
        bindProfile.setProfileId(profileId);
        onuIgmpConfigService.addBindCtcProfile(bindProfile);
        return NONE;
    }

    /**
     * 删除UNI端口绑定可控组播模板
     * @return
     */
    public String deleteUniBindProfile() {
        IgmpUniBindCtcProfile bindProfile = new IgmpUniBindCtcProfile();
        bindProfile.setEntityId(entityId);
        bindProfile.setUniIndex(uniIndex);
        bindProfile.setProfileId(profileId);
        bindProfile.setPortType(IgmpConstants.IGMP_PORTTYPE_UNI);
        onuIgmpConfigService.deleteBindCtcProfile(bindProfile);
        return NONE;
    }

    /**
     * 刷新UNI端口绑定可控组播配置
     * @return
     */
    public String refreshUniBindProfile() {
        onuIgmpConfigService.refreshUniBindProfile(entityId);
        return NONE;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public JSONArray getUniListJson() {
        return uniListJson;
    }

    public void setUniListJson(JSONArray uniListJson) {
        this.uniListJson = uniListJson;
    }

    public IgmpOnuConfig getOnuConfig() {
        return onuConfig;
    }

    public void setOnuConfig(IgmpOnuConfig onuConfig) {
        this.onuConfig = onuConfig;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public IgmpUniConfig getUniConfig() {
        return uniConfig;
    }

    public void setUniConfig(IgmpUniConfig uniConfig) {
        this.uniConfig = uniConfig;
    }

    public IgmpUniVlanTrans getVlanTrans() {
        return vlanTrans;
    }

    public void setVlanTrans(IgmpUniVlanTrans vlanTrans) {
        this.vlanTrans = vlanTrans;
    }

    public Integer getTransIndex() {
        return transIndex;
    }

    public void setTransIndex(Integer transIndex) {
        this.transIndex = transIndex;
    }

	public String getUniName() {
		return uniName;
	}

	public void setUniName(String uniName) {
		this.uniName = uniName;
	}

}
