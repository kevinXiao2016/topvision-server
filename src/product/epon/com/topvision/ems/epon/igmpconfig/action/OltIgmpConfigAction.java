/***********************************************************************
 * $Id: OltIgmpAction.java,v1.0 2016-6-6 下午4:03:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCascadePort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpControlGroupBindRelation;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfileGroupRela;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcRecord;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpStaticFwd;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpUplinkPort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanInfo;
import com.topvision.ems.epon.igmpconfig.modestate.ModeContext;
import com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2016-6-6-下午4:03:47 OLT IGMP配置管理
 */
@Controller("oltIgmpConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltIgmpConfigAction extends BaseAction {
    private static final long serialVersionUID = 6511683220443586788L;

    private Long entityId;
    private Integer igmpMode;
    private Integer currentMode;
    private Long portIndex;
    private Integer portType;
    private Integer vlanId;
    private Integer groupId;
    private Integer profileId;
    private Integer uplinkAggId;
    private Integer igmpVersion;
    private Boolean versionChange = false;
    private Integer joinMode;
    private Integer groupState;
    private JSONObject paramJson;
    private IgmpGlobalParam globalParam;
    private JSONObject ctcJson;
    private IgmpCtcParam ctcParam;
    private IgmpCtcProfile ctcProfile;
    private JSONObject uplinkJson;
    private IgmpSnpStaticFwd staticFwd;
    private IgmpVlanGroup groupInfo;
    private String cascadePorts;
    private String staticFwds;
    private List<Integer> vlanIds;
    private String groupInfos;
    private List<Integer> profileIds;

    @Autowired
    private OltIgmpConfigService oltIgmpConfigService;
    @Autowired
    private OltSniService oltSniService;

    /**
     * 显示OLT　IGMP配置页面
     * 
     * @return
     */
    public String showOltIgmpConfig() {
        igmpMode = oltIgmpConfigService.getIgmpMode(entityId);
        return SUCCESS;
    }

    /**
     * 修改IMGP模式
     * 
     * @return
     */
    public String modifyIgmpMode() {
        JSONObject json = new JSONObject();
        try {
            ModeContext modeContext = new ModeContext();
            modeContext.setEntityId(entityId);
            modeContext.setOltIgmpConfigService(oltIgmpConfigService);
            modeContext.createCurrentMode(currentMode);
            modeContext.changeNewMode(igmpMode);
            json.put("result", true);
        } catch (Exception e) {
            logger.info("modifyIgmpMode error {}", e);
            json.put("result", false);
            refreshAllIgmpData();
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 刷新所有IGMP业务
     */
    public String refreshAllIgmpData() {
        oltIgmpConfigService.refreshOltIgmpData(entityId);
        oltIgmpConfigService.refreshOnuIgmpData(entityId);
        return NONE;
    }

    /**
     * 显示IGMP基本协议配置
     * 
     * @return
     */
    public String showIgmpProtocol() {
        IgmpGlobalParam globalParam = oltIgmpConfigService.getGloablParam(entityId);
        paramJson = JSONObject.fromObject(globalParam);
        return SUCCESS;
    }

    /**
     * 修改IGMP全局参数
     * 
     * @return
     */
    public String modifyIgmpProtocol() {
        // 处理版本切换业务
        if (versionChange) {
            handVersionChange(globalParam.getEntityId(), globalParam.getIgmpVersion());
        }
        oltIgmpConfigService.modifyGlobalParam(globalParam);
        return NONE;
    }

    /**
     * 处理IGMP版本切换的业务
     * 
     * @param entityId
     * @param igmpVersion
     */
    private void handVersionChange(Long entityId, Integer igmpVersion) {
        switch (igmpVersion) {
        case IgmpConstants.IGMP_VERSION_V2: // V2版本
            // 切换到V2版本时不允许存在配置源ip的组播组,删除所有带源IP的组播组
            // 如果组播组被模板关联，并且模板被UNI端口绑定,则需要先删除UNI绑定关系,再从模板中删除关联的组播组,最后才能先组播组删除
            oltIgmpConfigService.deleteSpecialGroup(entityId, true);
            break;
        case IgmpConstants.IGMP_VERSION_V3: // V3兼容版本
            // 切换到V3兼容模式时不用处理任何业务
            break;
        case IgmpConstants.IGMP_VERSION_V3ONLY: // V3-only版本
            // V3-only版本不允许存在没有配置源ip的组播组,删除所有没有配置源IP的组播组
            // 如果组播组被模板关联，并且模板被UNI端口绑定,则需要先删除UNI绑定关系,再从模板中删除关联的组播组,最后才能先组播组删除
            oltIgmpConfigService.deleteSpecialGroup(entityId, false);
            break;
        default:
            throw new RuntimeException("IgmpVersion Value is wrong :" + igmpVersion);
        }
    }

    /**
     * 刷新IGMP基本参数配置
     * 
     * @return
     */
    public String refreshIgmpProtocol() {
        oltIgmpConfigService.refreshGlobalParam(entityId);
        return NONE;
    }

    /**
     * 显示级联端口配置
     * 
     * @return
     */
    public String showIgmpCascadePort() {
        return SUCCESS;
    }

    /**
     * 加载级联端口列表
     * 
     * @return
     */
    public String loadCascadePortList() {
        List<IgmpCascadePort> cascadePortList = oltIgmpConfigService.getCascadePortList(entityId);
        JSONArray portList = JSONArray.fromObject(cascadePortList);
        writeDataToAjax(portList);
        return NONE;
    }

    /**
     * 添加级联端口
     * 
     * @return
     */
    public String addCascadePort() {
        IgmpCascadePort cascadePort = new IgmpCascadePort();
        cascadePort.setEntityId(entityId);
        cascadePort.setPortIndex(portIndex);
        cascadePort.setPortType(portType);
        oltIgmpConfigService.addCascadePort(cascadePort);
        return NONE;
    }

    /**
     * 删除级联端口
     * 
     * @return
     */
    public String deleteCascadePort() {
        IgmpCascadePort cascadePort = new IgmpCascadePort();
        cascadePort.setEntityId(entityId);
        cascadePort.setPortIndex(portIndex);
        cascadePort.setPortType(portType);
        oltIgmpConfigService.deleteCascadePort(cascadePort);
        return NONE;
    }

    /**
     * 批量删除级联端口
     * 
     * @return
     */
    public String batchDeleteCascadePort() {
        String message = "";
        try {
            JSONArray jsonArray = JSONArray.fromObject(cascadePorts);
            @SuppressWarnings("unchecked")
            List<IgmpCascadePort> igmpCascadePorts = JSONArray.toList(jsonArray, new IgmpCascadePort(),
                    new JsonConfig());
            message = oltIgmpConfigService.batchDeleteCascadePort(entityId, igmpCascadePorts);
        } catch (Exception e) {
            message = ERROR;
            logger.error("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取级联端口数据
     * 
     * @return
     */
    public String refreshCascadePort() {
        oltIgmpConfigService.refreshCascadePort(entityId);
        return NONE;
    }

    /**
     * 显示IGMP VLAN配置
     * 
     * @return
     */
    public String showIgmpVlan() {
        return SUCCESS;
    }

    /**
     * 加载IGMP VLAN列表
     * 
     * @return
     */
    public String loadIgmpVlanList() {
        List<IgmpVlanInfo> vlanList = oltIgmpConfigService.getVlanInfoList(entityId);
        // 当端口类型为GE和XE时，处理端口展示名称
        for (IgmpVlanInfo vlanInfo : vlanList) {
            Integer portType = vlanInfo.getPortType();
            if (portType.equals(IgmpConstants.IGMP_PORTTYPE_GE) || portType.equals(IgmpConstants.IGMP_PORTTYPE_XE)) {
                String sniName = oltSniService.getSniDisplayName(vlanInfo.getEntityId(), vlanInfo.getPortIndex());
                if (sniName != null) {
                    vlanInfo.setPortName(sniName);
                } else {
                    vlanInfo.setPortName(EponIndex.getPortStringByIndex(vlanInfo.getPortIndex()).toString());
                }
            }
        }
        JSONArray vlanArray = JSONArray.fromObject(vlanList);
        writeDataToAjax(vlanArray);
        return NONE;
    }

    /**
     * 添加IGMP VLAN
     * 
     * @return
     */
    public String addIgmpVlan() {
        IgmpVlanInfo vlanInfo = new IgmpVlanInfo();
        vlanInfo.setEntityId(entityId);
        vlanInfo.setVlanId(vlanId);
        vlanInfo.setPortType(IgmpConstants.IGMP_PORTTYPE_INVALID);
        vlanInfo.setSlotNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        vlanInfo.setPortNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        vlanInfo.setUplinkAggId(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        oltIgmpConfigService.addVlanInfo(vlanInfo);
        return NONE;
    }

    /**
     * 修改IGMP VLAN
     * 
     * @return
     */
    public String modifyIgmpVlan() {
        IgmpVlanInfo vlanInfo = new IgmpVlanInfo();
        vlanInfo.setEntityId(entityId);
        vlanInfo.setVlanId(vlanId);
        vlanInfo.setPortType(portType);
        if (portIndex != null && portIndex != IgmpConstants.UNCONFIG_FLAG) {
            vlanInfo.setSlotNo(EponIndex.getSlotNo(portIndex).intValue());
            vlanInfo.setPortNo(EponIndex.getSniNo(portIndex).intValue());
        } else {
            vlanInfo.setSlotNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
            vlanInfo.setPortNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        }
        if (uplinkAggId != null && uplinkAggId != IgmpConstants.UNCONFIG_FLAG) {
            vlanInfo.setUplinkAggId(uplinkAggId);
        } else {
            vlanInfo.setUplinkAggId(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        }
        oltIgmpConfigService.modifyVlanInfo(vlanInfo);
        return NONE;
    }

    /**
     * 删除IGMP VLAN
     * 
     * @return
     */
    public String deleteIgmpVlan() {
        oltIgmpConfigService.deleteVlanInfo(entityId, vlanId);
        return NONE;
    }

    /**
     * 批量删除IGMP VLAN
     * 
     * @return
     */
    public String batchDeleteIgmpVlan() {
        String message = oltIgmpConfigService.batchDeleteVlanInfo(entityId, vlanIds);
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新IGMP VLAN
     * 
     * @return
     */
    public String refreshIgmpVlan() {
        oltIgmpConfigService.refreshVlanData(entityId);
        return NONE;
    }

    /**
     * 显示IGMP组播组配置
     * 
     * @return
     */
    public String showIgmpGroup() {
        IgmpGlobalParam globalParam = oltIgmpConfigService.getGloablParam(entityId);
        igmpVersion = globalParam.getIgmpVersion();
        return SUCCESS;
    }

    /**
     * 获取所有已经配置的组播组
     * 
     * @return
     */
    public String loadAllVlanGroup() {
        List<IgmpVlanGroup> groupList = oltIgmpConfigService.getAllVlanGroup(entityId);
        JSONArray groupArr = JSONArray.fromObject(groupList);
        writeDataToAjax(groupArr);
        return NONE;
    }

    /**
     * 加载组播组列表
     * 
     * @return
     */
    public String loadVlanGroupList() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        if (joinMode != null && !joinMode.equals(IgmpConstants.UNCONFIG_FLAG)) {
            paramMap.put("joinMode", joinMode);
        }
        if (groupState != null && !groupState.equals(IgmpConstants.UNCONFIG_FLAG)) {
            paramMap.put("groupState", groupState);
        }
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        List<IgmpVlanGroup> groupList = oltIgmpConfigService.getVlanGroupList(paramMap);
        Integer totalCount = oltIgmpConfigService.getVlanGroupNum(paramMap);
        JSONObject groupJson = new JSONObject();
        // 防止将数值型转换成0对页面展示造成影响
        groupJson.put("data", JSONArray.fromObject(groupList, getJsonConfig()));
        groupJson.put("rowCount", totalCount);
        writeDataToAjax(groupJson);
        return NONE;
    }

    /**
     * 添加组播组
     * 
     * @return
     */
    public String addVlanGroup() {
        oltIgmpConfigService.addVlanGroup(groupInfo);
        return NONE;
    }

    /**
     * 修改组播组
     * 
     * @return
     */
    public String modifyVlanGroup() {
        oltIgmpConfigService.modifyVlanGroup(groupInfo);
        return NONE;
    }

    /**
     * 删除组播组
     * 
     * @return
     */
    public String deleteVlanGroup() {
        oltIgmpConfigService.deleteVlanGroup(groupInfo);
        return NONE;
    }

    public String batchDeleteVlanGroup() {
        String message = "";
        try {
            JSONArray jsonArray = JSONArray.fromObject(groupInfos);
            @SuppressWarnings("unchecked")
            List<IgmpVlanGroup> igmpVlanGroups = JSONArray.toList(jsonArray, new IgmpVlanGroup(), new JsonConfig());
            message = oltIgmpConfigService.batchDeleteVlanGroup(entityId, igmpVlanGroups);
        } catch (Exception e) {
            message = ERROR;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 取消组播组的预加入配置
     * 
     * @return
     */
    public String modifyGroupPreJoin() {
        oltIgmpConfigService.modifyGroupPreJoin(groupInfo);
        return NONE;
    }

    /**
     * 刷新组播组
     * 
     * @return
     */
    public String refreshVlanGroup() {
        oltIgmpConfigService.refreshVlanGroup(entityId);
        // 因为组播组状态在全局Group表中维护,所以需要同时对全局Groupg表进行刷新
        oltIgmpConfigService.refreshGlobalGroup(entityId);
        return NONE;
    }

    /**
     * 显示Snooping模式下组播组
     * 
     * @return
     */
    public String showSnoopingGroup() {
        // 在展示Snooping组播组时先刷新数据,防止其它模式下配置的组播组数据冗余
        try {
            oltIgmpConfigService.refreshGlobalGroup(entityId);
        } catch (Exception e) {
            logger.error("Load Global group failed when showSnoopingGroup: ", e);
        }
        return SUCCESS;
    }

    /**
     * 加载Snooping模式下组播组列表
     * 
     * @return
     */
    public String loadSnpGroupList() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        List<IgmpGlobalGroup> groupList = oltIgmpConfigService.getGlobalGroupList(paramMap);
        Integer totalCount = oltIgmpConfigService.getGlobalGroupNum(paramMap);
        JSONObject groupJson = new JSONObject();
        groupJson.put("data", groupList);
        groupJson.put("rowCount", totalCount);
        writeDataToAjax(groupJson);
        return NONE;
    }

    /**
     * 刷新Snooping模式下组播组列表
     * 
     * @return
     */
    public String refreshSnpGroup() {
        oltIgmpConfigService.refreshGlobalGroup(entityId);
        return NONE;
    }

    /**
     * 显示上联端口配置
     * 
     * @return
     */
    public String showIgmpUplinkPort() {
        IgmpSnpUplinkPort uplinkPort = oltIgmpConfigService.getSnpUplinkPort(entityId);
        uplinkJson = JSONObject.fromObject(uplinkPort);
        writeDataToAjax(uplinkJson);
        return SUCCESS;
    }

    /**
     * 修改Snooping上联端口配置
     * 
     * @return
     */
    public String modifyUplinkPort() {
        IgmpSnpUplinkPort uplinkPort = new IgmpSnpUplinkPort();
        uplinkPort.setEntityId(entityId);
        uplinkPort.setPortType(portType);
        if (portIndex != null && portIndex != IgmpConstants.UNCONFIG_FLAG) {
            uplinkPort.setSlotNo(EponIndex.getSlotNo(portIndex).intValue());
            uplinkPort.setPortNo(EponIndex.getSniNo(portIndex).intValue());
        } else {
            uplinkPort.setSlotNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
            uplinkPort.setPortNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        }
        if (uplinkAggId != null && uplinkAggId != IgmpConstants.UNCONFIG_FLAG) {
            uplinkPort.setUplinkAggId(uplinkAggId);
        } else {
            uplinkPort.setUplinkAggId(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
        }
        oltIgmpConfigService.modifySnpUplinkPort(uplinkPort);
        return NONE;
    }

    /**
     * 刷新Snooping上联端口配置
     * 
     * @return
     */
    public String refreshUplinkPort() {
        oltIgmpConfigService.refreshSnpUplinkPort(entityId);
        return NONE;
    }

    /**
     * 显示静态加入配置
     * 
     * @return
     */
    public String showIgmpStaticJoin() {
        IgmpGlobalParam globalParam = oltIgmpConfigService.getGloablParam(entityId);
        igmpVersion = globalParam.getIgmpVersion();
        return SUCCESS;
    }

    /**
     * 加载静态加入列表
     * 
     * @return
     */
    public String loadStaticJoinList() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        List<IgmpSnpStaticFwd> staticList = oltIgmpConfigService.getSnpStaticFwdList(paramMap);
        Integer totalCount = oltIgmpConfigService.getSnpStaticFwdNum(paramMap);
        JSONObject staticJson = new JSONObject();
        staticJson.put("data", staticList);
        staticJson.put("rowCount", totalCount);
        writeDataToAjax(staticJson);
        return NONE;
    }

    /**
     * 添加静态加入配置
     * 
     * @return
     */
    public String addSnpStaticJoin() {
        oltIgmpConfigService.addSnpStaticFwd(staticFwd);
        return NONE;
    }

    /**
     * 删除静态加入配置
     * 
     * @return
     */
    public String deleteSnpStaticJoin() {
        oltIgmpConfigService.deleteSnpStaticFwd(staticFwd);
        return NONE;
    }

    /**
     * 批量删除静态加入配置
     * 
     * @return
     */
    public String batchDeleteSnpStaticJoin() {
        String message = "";
        try {
            JSONArray jsonArray = JSONArray.fromObject(staticFwds);
            @SuppressWarnings("unchecked")
            List<IgmpSnpStaticFwd> igmpSnpStaticFwds = JSONArray.toList(jsonArray, new IgmpSnpStaticFwd(),
                    new JsonConfig());
            message = oltIgmpConfigService.batchDeleteSnpStaticFwd(entityId, igmpSnpStaticFwds);
        } catch (Exception e) {
            message = ERROR;
            logger.error("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新静态中入配置
     * 
     * @return
     */
    public String refreshSnpStaticJoin() {
        oltIgmpConfigService.refreshSnpStaticFwd(entityId);
        return NONE;
    }

    /**
     * 显示可控组播配置
     * 
     * @return
     */
    public String showIgmpCtcConfig() {
        IgmpCtcParam ctcParam = oltIgmpConfigService.getCtcParam(entityId);
        ctcJson = JSONObject.fromObject(ctcParam);
        writeDataToAjax(ctcJson);
        return SUCCESS;
    }

    /**
     * 修改可控组播配置
     * 
     * @return
     */
    public String modifyCtcParam() {
        oltIgmpConfigService.modifyCtcParam(ctcParam);
        return NONE;
    }

    /**
     * 手动上报CDR日志
     * 
     * @return
     */
    public String reportCtcCdr() {
        oltIgmpConfigService.reportCtcCdr(entityId);
        return NONE;
    }

    /**
     * 刷新可控组播配置
     * 
     * @return
     */
    public String refreshCtcParam() {
        oltIgmpConfigService.refreshCtcParam(entityId);
        return NONE;
    }

    /**
     * 显示可控模板配置
     * 
     * @return
     */
    public String showIgmpCtcProfile() {
        return SUCCESS;
    }

    /**
     * 加载CTC模板列表
     * 
     * @return
     */
    public String loadCtcProfileList() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        List<IgmpCtcProfile> profileList = oltIgmpConfigService.getCtcProfileList(paramMap);
        Integer totalCount = oltIgmpConfigService.getCtcProfileNum(paramMap);
        JSONObject profileJson = new JSONObject();
        profileJson.put("data", profileList);
        profileJson.put("rowCount", totalCount);
        writeDataToAjax(profileJson);
        return NONE;
    }

    /**
     * 添加可控模板
     * 
     * @return
     */
    public String addCtcProfile() {
        oltIgmpConfigService.addCtcProfile(ctcProfile);
        return NONE;
    }

    /**
     * 修改可控组播模板
     * 
     * @return
     */
    public String modifyCtcProfile() {
        oltIgmpConfigService.modifyCtcProfile(ctcProfile);
        return NONE;
    }

    /**
     * 删除可控模板
     * 
     * @return
     */
    public String deleteCtcProfile() {
        oltIgmpConfigService.deleteCtcProfile(entityId, profileId);
        return NONE;
    }

    /**
     * 删除可控模板
     * 
     * @return
     */
    public String batchDeleteCtcProfile() {
        String message = oltIgmpConfigService.batchDeleteCtcProfile(entityId, profileIds);
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新可控模板
     * 
     * @return
     */
    public String refreshCtcProfile() {
        oltIgmpConfigService.refreshProfileData(entityId);
        return NONE;
    }

    /**
     * 显示可控模板关联组播组配置
     * 
     * @return
     */
    public String showProfileRelaGroup() {
        return SUCCESS;
    }

    /**
     * 加载可控模板关联组播组列表
     * 
     * @return
     */
    public String loadProfileGroupList() {
        List<IgmpControlGroupBindRelation> profileGroupList = oltIgmpConfigService.getProfileGroupRelaList(entityId,
                profileId);
        writeDataToAjax(profileGroupList);
        return NONE;
    }

    /**
     * 添加可控模板关联组播组
     * 
     * @return
     */
    public String addProfileGroupRela() {
        IgmpCtcProfileGroupRela profileGroup = new IgmpCtcProfileGroupRela();
        profileGroup.setEntityId(entityId);
        profileGroup.setProfileId(profileId);
        profileGroup.setGroupId(groupId);
        oltIgmpConfigService.addProfileGroupRela(profileGroup);
        return NONE;
    }

    /**
     * 删除可控模板关联组播组
     * 
     * @return
     */
    public String deleteProfileGroupRela() {
        IgmpCtcProfileGroupRela profileGroup = new IgmpCtcProfileGroupRela();
        profileGroup.setEntityId(entityId);
        profileGroup.setProfileId(profileId);
        profileGroup.setGroupId(groupId);
        oltIgmpConfigService.deleteProfileGroupRela(profileGroup);
        return NONE;
    }

    /**
     * 刷新可控模板关联组播组
     * 
     * @return
     */
    public String refreshProfileGroupRela() {
        oltIgmpConfigService.refreshProfileGroupRela(entityId);
        return NONE;
    }

    /**
     * 显示呼叫记录
     * 
     * @return
     */
    public String showIgmpCtcRecord() {
        return SUCCESS;
    }

    /**
     * 加载呼叫记录
     * 
     * @return
     */
    public String loadCtcRecordList() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        List<IgmpCtcRecord> recordList = oltIgmpConfigService.getCtcRecordList(paramMap);
        Integer totalCount = oltIgmpConfigService.getCtcRecordNum(paramMap);
        JSONObject recordJson = new JSONObject();
        recordJson.put("data", recordList);
        recordJson.put("rowCount", totalCount);
        writeDataToAjax(recordJson);
        return NONE;
    }

    /**
     * 刷新呼叫记录
     * 
     * @return
     */
    public String refreshCtcRecord() {
        oltIgmpConfigService.refreshCtcRecord(entityId);
        return NONE;
    }

    /**
     * 获取指定类型的SNI端口列表
     * 
     * @return
     */
    public String loadSniListByType() {
        List<IgmpPortInfo> sniList = new ArrayList<>();
        if (portType.equals(IgmpConstants.IGMP_PORTTYPE_GE)) {
            sniList = oltIgmpConfigService.getSniPortByType(entityId, IgmpConstants.SNI_GE_TYPES);
        } else if (portType.equals(IgmpConstants.IGMP_PORTTYPE_XE)) {
            sniList = oltIgmpConfigService.getSniPortByType(entityId, IgmpConstants.SNI_XE_TYPES);
        }
        JSONArray sniArr = JSONArray.fromObject(sniList);
        writeDataToAjax(sniArr);
        return NONE;
    }

    /**
     * 获取SNI端口列表
     * 
     * @return
     */
    public String loadSniList() {
        List<IgmpPortInfo> sniList = new ArrayList<>();
        sniList = oltIgmpConfigService.getSniPort(entityId);
        JSONArray sniArr = JSONArray.fromObject(sniList);
        writeDataToAjax(sniArr);
        return NONE;
    }

    /**
     * 获取上联聚合组端口列表
     * 
     * @return
     */
    public String loadSniAggList() {
        List<IgmpPortInfo> aggList = oltIgmpConfigService.getSniAggList(entityId);
        JSONArray aggArr = JSONArray.fromObject(aggList);
        writeDataToAjax(aggArr);
        return NONE;
    }

    /**
     * 获取指定类型的PON口列表
     * 
     * @return
     */
    public String loadPonListByType() {
        List<IgmpPortInfo> ponList = new ArrayList<>();
        if (portType.equals(IgmpConstants.IGMP_PORTTYPE_EPON)) {
            ponList = oltIgmpConfigService.getPonPortByType(entityId, EponConstants.PON_PORT_TYPE_GEEPON);
        } else if (portType.equals(IgmpConstants.IGMP_PORTTYPE_GPON)) {
            ponList = oltIgmpConfigService.getPonPortByType(entityId, EponConstants.PON_PORT_TYPE_GPON);
        } else if (portType.equals(IgmpConstants.IGMP_PORTTYPE_EPON10G)) {
            ponList = oltIgmpConfigService.getPonPortByType(entityId, EponConstants.PON_PORT_TYPE_TENGEEPON);
        }
        JSONArray ponArr = JSONArray.fromObject(ponList);
        writeDataToAjax(ponArr);
        return NONE;
    }

    /**
     * 获取所有的可控组播模板
     * 
     * @return
     */
    public String loadAllCtcProfile() {
        List<IgmpCtcProfile> profileList = oltIgmpConfigService.getAllCtcProfile(entityId);
        JSONArray profileArr = JSONArray.fromObject(profileList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     * 获取所有已经配置的组播组ID
     * 
     * @return
     */
    public String loadAllGroupId() {
        List<Integer> groupIdList = oltIgmpConfigService.getGroupIdList(entityId);
        JSONArray idArray = JSONArray.fromObject(groupIdList);
        writeDataToAjax(idArray);
        return NONE;
    }

    /**
     * 获取所有已经配置的模板ID
     * 
     * @return
     */
    public String loadAllProfileId() {
        List<Integer> profileIdList = oltIgmpConfigService.getProfileIdList(entityId);
        JSONArray idArray = JSONArray.fromObject(profileIdList);
        writeDataToAjax(idArray);
        return NONE;
    }

    /**
     * 获取指定端口类型的级联端口
     * 
     * @return
     */
    public String loadCascadePortByType() {
        List<IgmpCascadePort> cascadePortList = oltIgmpConfigService.getCascadePortByType(entityId, portType);
        JSONArray portList = JSONArray.fromObject(cascadePortList);
        writeDataToAjax(portList);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIgmpMode() {
        return igmpMode;
    }

    public void setIgmpMode(Integer igmpMode) {
        this.igmpMode = igmpMode;
    }

    public JSONObject getParamJson() {
        return paramJson;
    }

    public void setParamJson(JSONObject paramJson) {
        this.paramJson = paramJson;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public IgmpGlobalParam getGlobalParam() {
        return globalParam;
    }

    public void setGlobalParam(IgmpGlobalParam globalParam) {
        this.globalParam = globalParam;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public JSONObject getCtcJson() {
        return ctcJson;
    }

    public void setCtcJson(JSONObject ctcJson) {
        this.ctcJson = ctcJson;
    }

    public IgmpCtcParam getCtcParam() {
        return ctcParam;
    }

    public void setCtcParam(IgmpCtcParam ctcParam) {
        this.ctcParam = ctcParam;
    }

    public IgmpCtcProfile getCtcProfile() {
        return ctcProfile;
    }

    public void setCtcProfile(IgmpCtcProfile ctcProfile) {
        this.ctcProfile = ctcProfile;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public JSONObject getUplinkJson() {
        return uplinkJson;
    }

    public void setUplinkJson(JSONObject uplinkJson) {
        this.uplinkJson = uplinkJson;
    }

    public IgmpSnpStaticFwd getStaticFwd() {
        return staticFwd;
    }

    public void setStaticFwd(IgmpSnpStaticFwd staticFwd) {
        this.staticFwd = staticFwd;
    }

    public Integer getUplinkAggId() {
        return uplinkAggId;
    }

    public void setUplinkAggId(Integer uplinkAggId) {
        this.uplinkAggId = uplinkAggId;
    }

    public IgmpVlanGroup getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(IgmpVlanGroup groupInfo) {
        this.groupInfo = groupInfo;
    }

    public Integer getIgmpVersion() {
        return igmpVersion;
    }

    public void setIgmpVersion(Integer igmpVersion) {
        this.igmpVersion = igmpVersion;
    }

    public Integer getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Integer currentMode) {
        this.currentMode = currentMode;
    }

    public Boolean getVersionChange() {
        return versionChange;
    }

    public void setVersionChange(Boolean versionChange) {
        this.versionChange = versionChange;
    }

    public Integer getJoinMode() {
        return joinMode;
    }

    public void setJoinMode(Integer joinMode) {
        this.joinMode = joinMode;
    }

    public Integer getGroupState() {
        return groupState;
    }

    public void setGroupState(Integer groupState) {
        this.groupState = groupState;
    }

    public String getCascadePorts() {
        return cascadePorts;
    }

    public void setCascadePorts(String cascadePorts) {
        this.cascadePorts = cascadePorts;
    }

    public List<Integer> getVlanIds() {
        return vlanIds;
    }

    public void setVlanIds(List<Integer> vlanIds) {
        this.vlanIds = vlanIds;
    }

    private static JsonConfig getJsonConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            @Override
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            @Override
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Long.class, new DefaultValueProcessor() {
            @Override
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            @Override
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(String.class, new DefaultValueProcessor() {
            @Override
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        return jsonConfig;
    }

    public String getGroupInfos() {
        return groupInfos;
    }

    public void setGroupInfos(String groupInfos) {
        this.groupInfos = groupInfos;
    }

    public List<Integer> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<Integer> profileIds) {
        this.profileIds = profileIds;
    }

    public String getStaticFwds() {
        return staticFwds;
    }

    public void setStaticFwds(String staticFwds) {
        this.staticFwds = staticFwds;
    }

}
