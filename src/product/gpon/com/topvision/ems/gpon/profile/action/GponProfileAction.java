/***********************************************************************
 * $Id: GponProfileAction.java,v1.0 2016年10月24日 下午2:33:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;
import com.topvision.ems.gpon.profile.service.GponDbaProfileService;
import com.topvision.ems.gpon.profile.service.GponLineProfileService;
import com.topvision.ems.gpon.profile.service.GponProfileRefreshService;
import com.topvision.ems.gpon.profile.service.GponSrvProfileService;
import com.topvision.ems.gpon.profile.service.GponTrafficProfileService;
import com.topvision.ems.gpon.profile.service.TopDigitMapProfService;
import com.topvision.ems.gpon.profile.service.TopSIPAgentProfService;
import com.topvision.ems.gpon.profile.service.TopSIPSrvProfService;
import com.topvision.ems.gpon.profile.service.TopVoipMediaProfService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Bravin
 * @created @2016年10月24日-下午2:33:38
 *
 */
@Controller("gponProfileAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GponProfileAction extends BaseAction {
    private static final long serialVersionUID = -1640091142239631847L;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponProfileRefreshService gponProfileRefreshService;
    @Autowired
    private GponDbaProfileService gponDbaProfileService;
    @Autowired
    private GponTrafficProfileService gponTrafficProfileService;
    @Autowired
    private GponLineProfileService gponLineProfileService;
    @Autowired
    private GponSrvProfileService gponSrvProfileService;
    @Autowired
    private TopSIPAgentProfService topSIPAgentProfService;
    @Autowired
    private TopVoipMediaProfService topVoipMediaProfService;
    @Autowired
    private TopSIPSrvProfService topSIPSrvProfService;
    @Autowired
    private TopDigitMapProfService topDigitMapProfService;
    private Long entityId;
    private Entity entity;
    private String action;
    private String moduleType;
    private String parentModuleType;
    private Integer profileId;
    private Integer subProfileId;
    private Integer thirdProfileId;
    private Integer portTypeIndex;
    private Integer portIndex;
    private Integer vlanIndex;
    private Boolean parentEditable;
    private GponLineProfileTcont gponLineProfileTcont;
    private GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg;
    private GponTrafficProfileInfo gponTrafficProfileInfo;
    private GponLineProfileGemMap gponLineProfileGemMap;
    private GponSrvProfilePortNumProfile gponSrvProfilePortNumProfile;
    private GponDbaProfileInfo gponDbaProfileInfo;
    private GponLineProfileInfo gponLineProfileInfo;
    private GponSrvProfileCfg gponSrvProfileCfg;
    private GponSrvProfileInfo gponSrvProfileInfo;
    private GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation;
    private GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk;
    private GponLineProfileGem gponLineProfileGem;
    private GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig;
    private GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation;
    private TopSIPAgentProfInfo topSIPAgentProfInfo;
    private TopVoipMediaProfInfo topVoipMediaProfInfo;
    private TopSIPSrvProfInfo topSIPSrvProfInfo;
    private TopDigitMapProfInfo topDigitMapProfInfo;
    private TopGponSrvPotsInfo topGponSrvPotsInfo;

    /**
     * 展开GPON模版配置页面
     * 
     * @return
     */
    public String showGponProfile() {
        setEntity(entityService.getEntity(entityId));
        // add by fanzidong, 获取该版本是否支持VOIP
        return SUCCESS;
    }

    /**
     * 展开一级GPON模版配置页面
     * 
     * @return
     */
    public String showMainProfileCfg() {
        return SUCCESS;
    }

    public String showDependProfileCfg() {
        return SUCCESS;
    }

    public String showServiceProfileCfg() {
        return SUCCESS;
    }

    public String refreshGponProfile() {
        gponProfileRefreshService.refreshGponProfile(entityId);
        return NONE;
    }

    //-----------------------------线路模板start--------------------------------------------------------------//
    public String loadLineProfileList() {
        List<GponLineProfileInfo> lineProfileList = gponLineProfileService.loadGponLineProfileInfoList(entityId);
        writeDataToAjax(lineProfileList);
        return NONE;
    }

    public String addLineProfile() {
        gponLineProfileService.addGponLineProfileInfo(gponLineProfileInfo);
        return NONE;
    }

    public String loadLineProfile() {
        writeDataToAjax(gponLineProfileService.loadGponLineProfileInfo(entityId, profileId));
        return NONE;
    }

    public String modifyLineProfile() {
        gponLineProfileService.modifyGponLineProfileInfo(gponLineProfileInfo);
        return NONE;
    }

    public String deleteLineProfile() {
        gponLineProfileService.deleteGponLineProfileInfo(entityId, profileId);
        return NONE;
    }

    public String refreshLineProfile() {
        gponLineProfileService.refreshGponLineProfileList(entityId);
        return NONE;
    }

    public String loadLineProfileTcontList() {
        writeDataToAjax(gponLineProfileService.loadGponLineProfileTcontList(entityId, profileId));
        return NONE;
    }

    public String addLineProfileTcont() {
        gponLineProfileService.addGponLineProfileTcont(gponLineProfileTcont);
        return NONE;
    }

    public String modifyLineProfileTcont() {
        gponLineProfileService.modifyGponLineProfileTcont(gponLineProfileTcont);
        return NONE;
    }

    public String deleteLineProfileTcont() {
        gponLineProfileService.deleteGponLineProfileTcont(entityId, profileId, subProfileId);
        return NONE;
    }

    public String refreshLineProfileTcont() {
        gponLineProfileService.refreshGponLineProfileTcontList(entityId, profileId);
        return NONE;
    }

    public String loadLineProfileGemList() {
        writeDataToAjax(gponLineProfileService.loadGponLineProfileGemList(entityId, profileId));
        return NONE;
    }

    public String loadLineProfileGem() {
        writeDataToAjax(gponLineProfileService.loadGponLineProfileGem(entityId, profileId, subProfileId));
        return NONE;
    }

    public String addLineProfileGem() {
        gponLineProfileService.addGponLineProfileGem(gponLineProfileGem);
        return NONE;
    }

    public String modifyLineProfileGem() {
        gponLineProfileService.modifyGponLineProfileGem(gponLineProfileGem);
        return NONE;
    }

    public String deleteLineProfileGem() {
        gponLineProfileService.deleteGponLineProfileGem(entityId, profileId, subProfileId);
        return NONE;
    }

    public String refreshLineProfileGem() {
        gponLineProfileService.refreshGponLineProfileGemList(entityId, profileId);
        return NONE;
    }

    public String loadLineProfileGemMapList() {
        writeDataToAjax(gponLineProfileService.loadGponLineProfileGemMapList(entityId, profileId, subProfileId));
        return NONE;
    }

    public String addLineProfileGemMap() {
        gponLineProfileService.addGponLineProfileGemMap(gponLineProfileGemMap);
        return NONE;
    }

    public String modifyLineProfileGemMap() {
        gponLineProfileService.modifyGponLineProfileGemMap(gponLineProfileGemMap);
        return NONE;
    }

    public String deleteLineProfileGemMap() {
        gponLineProfileService.deleteGponLineProfileGemMap(entityId, profileId, subProfileId, thirdProfileId);
        return NONE;
    }

    public String refreshLineProfileGemMap() {
        gponLineProfileService.refreshGponLineProfileGemMapList(entityId, profileId, subProfileId);
        return NONE;
    }
    //-----------------------------线路模板end--------------------------------------------------------------//

    //-----------------------------业务模板start--------------------------------------------------------------//
    public String loadServiceProfileList() {
        List<GponSrvProfileInfo> srvProfileList = gponSrvProfileService.loadGponSrvProfileInfoList(entityId);
        writeDataToAjax(srvProfileList);
        return NONE;
    }

    public String addServiceProfile() {
        gponSrvProfileService.addGponSrvProfileInfo(gponSrvProfileInfo);
        return NONE;
    }

    public String loadServiceProfile() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfileInfo(entityId, profileId));
        return NONE;
    }

    public String modifyServiceProfile() {
        gponSrvProfileService.modifyGponSrvProfileInfo(gponSrvProfileInfo);
        return NONE;
    }

    public String deleteServiceProfile() {
        gponSrvProfileService.deleteGponSrvProfileInfo(entityId, profileId);
        return NONE;
    }

    public String refreshServiceProfile() {
        gponSrvProfileService.refreshGponSrvProfileInfoList(entityId);
        return NONE;
    }

    public String loadServiceProfileEthList() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfileEthPortConfigList(entityId, profileId));
        return NONE;
    }

    public String modifyServiceProfileEth() {
        gponSrvProfileService.modifyGponSrvProfileEthPortConfig(gponSrvProfileEthPortConfig);
        return NONE;
    }

    public String refreshServiceProfileEth() {
        gponSrvProfileService.refreshGponSrvProfileEthPortConfig(entityId, profileId);
        return NONE;
    }

    public String loadServiceProfileVlanList() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfilePortVlanCfgList(entityId, profileId));
        return NONE;
    }

    public String showModifyServiceProfileVlan() {
        return SUCCESS;
    }

    public String modifyServiceProfileVlan() {
        gponSrvProfileService.modifyGponSrvProfilePortVlanCfg(gponSrvProfilePortVlanCfg);
        return NONE;
    }

    public String modifyServiceProfileVlanMode() {
        gponSrvProfileService.modifyGponSrvProfilePortVlanMode(gponSrvProfilePortVlanCfg);
        return NONE;
    }

    public String loadServiceProfileVlan() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfilePortVlanCfg(entityId, profileId, portTypeIndex, portIndex));
        return NONE;
    }

    public String refreshServiceProfileVlan() {
        gponSrvProfileService.refreshGponSrvProfilePortVlanCfg(entityId, profileId);
        return NONE;
    }

    public String loadServiceProfileVlanTranslateList() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfilePortVlanTranslation(entityId, profileId, portTypeIndex,
                portIndex));
        return NONE;
    }

    public String refreshServiceProfileVlanTranslate() {
        gponProfileRefreshService.refreshGponSrvProfilePortVlanTranslation(entityId);
        return NONE;
    }

    public String addServiceProfileVlanTranslate() {
        gponSrvProfileService.addGponSrvProfilePortVlanTranslation(gponSrvProfilePortVlanTranslation);
        return NONE;
    }

    public String deleteServiceProfileVlanTranslate() {
        gponSrvProfileService.deleteGponSrvProfilePortVlanTranslation(entityId, profileId, portTypeIndex, portIndex,
                vlanIndex);
        return NONE;
    }

    public String loadServiceProfileVlanAggregationList() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfilePortVlanAggregation(entityId, profileId, portTypeIndex,
                portIndex));
        return NONE;
    }

    public String addServiceProfileVlanAggregation() {
        String aggrVlanString = gponSrvProfilePortVlanAggregation.getAggrVlanString();
        String strip = convertUnique(aggrVlanString);
        gponSrvProfilePortVlanAggregation.setAggrVlanString(strip);
        gponSrvProfileService.addGponSrvProfilePortVlanAggregation(gponSrvProfilePortVlanAggregation);
        return NONE;
    }

    /**
     * 字符串去重
     * @param aggrVlanString  如：2000,2000,2001
     * @return  去重后的字符串，如：2000,2001
     */
    private String convertUnique(String aggrVlanString) {
        String[] split = aggrVlanString.split(",");
        if (split.length == 1) {
            return aggrVlanString;
        }
        List<String> asList = Arrays.asList(split);
        List<String> newList = new ArrayList<>(split.length);
        for (String str : asList) {
            if (newList.contains(str)) {
                continue;
            }else {
                newList.add(str);
            }
        }
        newList.sort((o1,o2) -> Integer.valueOf(o1) - Integer.valueOf(o2));
        String newStr = newList.toString().replaceAll(" ", "");
        return StringUtils.strip(newStr, "[]");
    }

    public String modifyServiceProfileVlanAggregation() {
        String aggrVlanString = gponSrvProfilePortVlanAggregation.getAggrVlanString();
        String strip = convertUnique(aggrVlanString);
        gponSrvProfilePortVlanAggregation.setAggrVlanString(strip);
        gponSrvProfileService.modifyGponSrvProfilePortVlanAggregation(gponSrvProfilePortVlanAggregation);
        return NONE;
    }

    public String deleteServiceProfileVlanAggregation() {
        gponSrvProfileService.deleteGponSrvProfilePortVlanAggregation(entityId, profileId, portTypeIndex, portIndex,
                vlanIndex);
        return NONE;
    }

    public String refreshServiceProfileVlanAggregation() {
        gponProfileRefreshService.refreshGponSrvProfilePortVlanAggregation(entityId);
        return NONE;
    }

    public String loadServiceProfileVlanTrunkList() {
        writeDataToAjax(gponSrvProfileService.loadGponSrvProfilePortVlanTrunk(entityId, profileId, portTypeIndex, portIndex));
        return NONE;
    }

    public String addServiceProfileVlanTrunk() {
        String trunkVlan = gponSrvProfilePortVlanTrunk.getTrunkVlanString();
        String unique = convertUnique(trunkVlan);
        gponSrvProfilePortVlanTrunk.setTrunkVlanString(unique);
        gponSrvProfileService.addGponSrvProfilePortVlanTrunk(gponSrvProfilePortVlanTrunk);
        return NONE;
    }

    public String modifyServiceProfileVlanTrunk() {
        String trunkVlan = gponSrvProfilePortVlanTrunk.getTrunkVlanString();
        String unique = convertUnique(trunkVlan);
        gponSrvProfilePortVlanTrunk.setTrunkVlanString(unique);
        /* gponSrvProfileService.addGponSrvProfilePortVlanTrunk(gponSrvProfilePortVlanTrunk); */
        gponSrvProfileService.modifyGponSrvProfilePortVlanTrunk(gponSrvProfilePortVlanTrunk);
        return NONE;
    }

    public String deleteServiceProfileVlanTrunk() {
        gponSrvProfileService.deleteGponSrvProfilePortVlanTrunk(entityId, profileId, portTypeIndex, portIndex);
        return NONE;
    }

    public String refreshServiceProfileVlanTrunk() {
        gponProfileRefreshService.refreshGponSrvProfilePortVlanTrunk(entityId);
        return NONE;
    }

    public String loadTopGponSrvPotsInfoList() {
        writeDataToAjax(gponSrvProfileService.loadTopGponSrvPotsInfoList(entityId, profileId));
        return NONE;
    }

    public String modifyTopGponSrvPotsInfo() {
        gponSrvProfileService.modifyTopGponSrvPotsInfo(topGponSrvPotsInfo);
        return NONE;
    }

    public String refreshTopGponSrvPotsInfo() {
        gponSrvProfileService.refreshTopGponSrvPotsInfo(entityId, profileId);
        return NONE;
    }
    //-----------------------------业务模板end--------------------------------------------------------------//

    //-----------------------------DBA模板start--------------------------------------------------------------//
    public String loadDbaProfileList() {
        List<GponDbaProfileInfo> dbaProfileList = gponDbaProfileService.loadGponDbaProfileInfoList(entityId);
        writeDataToAjax(dbaProfileList);
        return NONE;
    }

    public String addDbaProfile() {
        gponDbaProfileService.addGponDbaProfileInfo(gponDbaProfileInfo);
        return NONE;
    }

    public String loadDbaProfile() {
        writeDataToAjax(gponDbaProfileService.loadGponDbaProfileInfo(entityId, profileId));
        return NONE;
    }

    public String modifyDbaProfile() {
        gponDbaProfileService.modifyGponDbaProfileInfo(gponDbaProfileInfo);
        return NONE;
    }

    public String deleteDbaProfile() {
        gponDbaProfileService.deleteGponDbaProfileInfo(entityId, profileId);
        return NONE;
    }

    public String refreshDbaProfile() {
        gponDbaProfileService.refreshGponDbaProfileList(entityId);
        return NONE;
    }
    //-----------------------------DBA模板end--------------------------------------------------------------//

    //-----------------------------流量模板start--------------------------------------------------------------//
    public String loadTrafficProfileList() {
        List<GponTrafficProfileInfo> trafficProfileInfo = gponTrafficProfileService
                .loadGponTrafficProfileInfoList(entityId);
        writeDataToAjax(trafficProfileInfo);
        return NONE;
    }

    public String addTrafficProfile() {
        gponTrafficProfileService.addGponTrafficProfileInfo(gponTrafficProfileInfo);
        return NONE;
    }

    public String loadTrafficProfile() {
        writeDataToAjax(gponTrafficProfileService.loadGponTrafficProfileInfo(entityId, profileId));
        return NONE;
    }

    public String modifyTrafficProfile() {
        gponTrafficProfileService.modifyGponTrafficProfileInfo(gponTrafficProfileInfo);
        return NONE;
    }

    public String deleteTrafficProfile() {
        gponTrafficProfileService.deleteGponTrafficProfileInfo(entityId, profileId);
        return NONE;
    }

    public String refreshTrafficProfile() {
        gponTrafficProfileService.refreshGponTrafficProfileList(entityId);
        return NONE;
    }
    //-----------------------------流量模板end--------------------------------------------------------------//

    //-----------------------------SIP代理模板start--------------------------------------------------------------//
    public String loadSipAgentProfileList() {
        writeDataToAjax(topSIPAgentProfService.loadTopSIPAgentProfInfoList(entityId));
        return NONE;
    }

    public String addSipAgentProfile() {
        topSIPAgentProfService.addTopSIPAgentProfInfo(topSIPAgentProfInfo);
        return NONE;
    }

    public String loadSipAgentProfile() {
        writeDataToAjax(topSIPAgentProfService.loadTopSIPAgentProfInfo(entityId, profileId));
        return NONE;
    }

    public String modifySipAgentProfile() {
        topSIPAgentProfService.modifyTopSIPAgentProfInfo(topSIPAgentProfInfo);
        return NONE;
    }

    public String deleteSipAgentProfile() {
        topSIPAgentProfService.deleteTopSIPAgentProfInfo(entityId, profileId);
        return NONE;
    }

    public String refreshSipAgentProfile() {
        topSIPAgentProfService.refreshTopSIPAgentProfInfo(entityId);
        return NONE;
    }
    //-----------------------------SIP代理模板end--------------------------------------------------------------//

    //-----------------------------SIP业务数据模板start--------------------------------------------------------------//
    public String loadTopSIPSrvProfList() {
        writeDataToAjax(topSIPSrvProfService.loadTopSIPSrvProfInfoList(entityId));
        return NONE;
    }

    public String addTopSIPSrvProf() {
        topSIPSrvProfService.addTopSIPSrvProfInfo(topSIPSrvProfInfo);
        return NONE;
    }

    public String loadTopSIPSrvProf() {
        writeDataToAjax(topSIPSrvProfService.loadTopSIPSrvProfInfo(entityId, profileId));
        return NONE;
    }

    public String modifyTopSIPSrvProf() {
        topSIPSrvProfService.modifyTopSIPSrvProfInfo(topSIPSrvProfInfo);
        return NONE;
    }

    public String deleteTopSIPSrvProf() {
        topSIPSrvProfService.deleteTopSIPSrvProfInfo(entityId, profileId);
        return NONE;
    }

    public String refreshTopSIPSrvProf() {
        topSIPSrvProfService.refreshTopSIPSrvProfInfo(entityId);
        return NONE;
    }
    //-----------------------------SIP业务数据模板end--------------------------------------------------------------//

    //-----------------------------数图模板start--------------------------------------------------------------//
    public String loadTopDigitMapProfList() {
        writeDataToAjax(topDigitMapProfService.loadTopDigitMapProfInfoList(entityId));
        return NONE;
    }

    public String addTopDigitMapProf() {
        topDigitMapProfService.addTopDigitMapProfInfo(topDigitMapProfInfo);
        return NONE;
    }

    public String loadTopDigitMapProf() {
        writeDataToAjax(topDigitMapProfService.loadTopDigitMapProfInfo(entityId, profileId));
        return NONE;
    }

    public String modifyTopDigitMapProf() {
        topDigitMapProfService.modifyTopDigitMapProfInfo(topDigitMapProfInfo);
        return NONE;
    }

    public String deleteTopDigitMapProf() {
        topDigitMapProfService.deleteTopDigitMapProfInfo(entityId, profileId);
        return NONE;
    }

    public String refreshTopDigitMapProf() {
        topDigitMapProfService.refreshTopDigitMapProfInfo(entityId);
        return NONE;
    }
    //-----------------------------数图模板end--------------------------------------------------------------//

    //-----------------------------VOIP媒体模板start--------------------------------------------------------------//
    public String loadTopVoipMediaProfList() {
        writeDataToAjax(topVoipMediaProfService.loadTopVoipMediaProfInfoList(entityId));
        return NONE;
    }

    public String addTopVoipMediaProf() {
        topVoipMediaProfService.addTopVoipMediaProfInfo(topVoipMediaProfInfo);
        return NONE;
    }

    public String loadTopVoipMediaProf() {
        writeDataToAjax(topVoipMediaProfService.loadTopVoipMediaProfInfo(entityId, profileId));
        return NONE;
    }

    public String modifyTopVoipMediaProf() {
        topVoipMediaProfService.modifyTopVoipMediaProfInfo(topVoipMediaProfInfo);
        return NONE;
    }

    public String deleteTopVoipMediaProf() {
        topVoipMediaProfService.deleteTopVoipMediaProfInfo(entityId, profileId);
        return NONE;
    }

    public String refreshTopVoipMediaProf() {
        topVoipMediaProfService.refreshTopVoipMediaProfInfo(entityId);
        return NONE;
    }
    //-----------------------------VOIP媒体模板end--------------------------------------------------------------//

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public GponLineProfileTcont getGponLineProfileTcont() {
        return gponLineProfileTcont;
    }

    public void setGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont) {
        this.gponLineProfileTcont = gponLineProfileTcont;
    }

    public GponSrvProfilePortVlanCfg getGponSrvProfilePortVlanCfg() {
        return gponSrvProfilePortVlanCfg;
    }

    public void setGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg) {
        this.gponSrvProfilePortVlanCfg = gponSrvProfilePortVlanCfg;
    }

    public GponTrafficProfileInfo getGponTrafficProfileInfo() {
        return gponTrafficProfileInfo;
    }

    public void setGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo) {
        this.gponTrafficProfileInfo = gponTrafficProfileInfo;
    }

    public GponLineProfileGemMap getGponLineProfileGemMap() {
        return gponLineProfileGemMap;
    }

    public void setGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap) {
        this.gponLineProfileGemMap = gponLineProfileGemMap;
    }

    public GponSrvProfilePortNumProfile getGponSrvProfilePortNumProfile() {
        return gponSrvProfilePortNumProfile;
    }

    public void setGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile gponSrvProfilePortNumProfile) {
        this.gponSrvProfilePortNumProfile = gponSrvProfilePortNumProfile;
    }

    public GponDbaProfileInfo getGponDbaProfileInfo() {
        return gponDbaProfileInfo;
    }

    public void setGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo) {
        this.gponDbaProfileInfo = gponDbaProfileInfo;
    }

    public GponLineProfileInfo getGponLineProfileInfo() {
        return gponLineProfileInfo;
    }

    public void setGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo) {
        this.gponLineProfileInfo = gponLineProfileInfo;
    }

    public GponSrvProfileCfg getGponSrvProfileCfg() {
        return gponSrvProfileCfg;
    }

    public void setGponSrvProfileCfg(GponSrvProfileCfg gponSrvProfileCfg) {
        this.gponSrvProfileCfg = gponSrvProfileCfg;
    }

    public GponSrvProfileInfo getGponSrvProfileInfo() {
        return gponSrvProfileInfo;
    }

    public void setGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo) {
        this.gponSrvProfileInfo = gponSrvProfileInfo;
    }

    public GponSrvProfilePortVlanAggregation getGponSrvProfilePortVlanAggregation() {
        return gponSrvProfilePortVlanAggregation;
    }

    public void setGponSrvProfilePortVlanAggregation(
            GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation) {
        this.gponSrvProfilePortVlanAggregation = gponSrvProfilePortVlanAggregation;
    }

    public GponSrvProfilePortVlanTrunk getGponSrvProfilePortVlanTrunk() {
        return gponSrvProfilePortVlanTrunk;
    }

    public void setGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk) {
        this.gponSrvProfilePortVlanTrunk = gponSrvProfilePortVlanTrunk;
    }

    public GponLineProfileGem getGponLineProfileGem() {
        return gponLineProfileGem;
    }

    public void setGponLineProfileGem(GponLineProfileGem gponLineProfileGem) {
        this.gponLineProfileGem = gponLineProfileGem;
    }

    public GponSrvProfileEthPortConfig getGponSrvProfileEthPortConfig() {
        return gponSrvProfileEthPortConfig;
    }

    public void setGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig) {
        this.gponSrvProfileEthPortConfig = gponSrvProfileEthPortConfig;
    }

    public GponSrvProfilePortVlanTranslation getGponSrvProfilePortVlanTranslation() {
        return gponSrvProfilePortVlanTranslation;
    }

    public void setGponSrvProfilePortVlanTranslation(
            GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation) {
        this.gponSrvProfilePortVlanTranslation = gponSrvProfilePortVlanTranslation;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getParentModuleType() {
        return parentModuleType;
    }

    public void setParentModuleType(String parentModuleType) {
        this.parentModuleType = parentModuleType;
    }

    public Integer getSubProfileId() {
        return subProfileId;
    }

    public void setSubProfileId(Integer subProfileId) {
        this.subProfileId = subProfileId;
    }

    public Integer getThirdProfileId() {
        return thirdProfileId;
    }

    public void setThirdProfileId(Integer thirdProfileId) {
        this.thirdProfileId = thirdProfileId;
    }

    public Integer getPortTypeIndex() {
        return portTypeIndex;
    }

    public void setPortTypeIndex(Integer portTypeIndex) {
        this.portTypeIndex = portTypeIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public Boolean getParentEditable() {
        return parentEditable;
    }

    public void setParentEditable(Boolean parentEditable) {
        this.parentEditable = parentEditable;
    }

    public TopSIPAgentProfInfo getTopSIPAgentProfInfo() {
        return topSIPAgentProfInfo;
    }

    public void setTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo) {
        this.topSIPAgentProfInfo = topSIPAgentProfInfo;
    }

    public TopVoipMediaProfInfo getTopVoipMediaProfInfo() {
        return topVoipMediaProfInfo;
    }

    public void setTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo) {
        this.topVoipMediaProfInfo = topVoipMediaProfInfo;
    }

    public TopSIPSrvProfInfo getTopSIPSrvProfInfo() {
        return topSIPSrvProfInfo;
    }

    public void setTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo) {
        this.topSIPSrvProfInfo = topSIPSrvProfInfo;
    }

    public TopDigitMapProfInfo getTopDigitMapProfInfo() {
        return topDigitMapProfInfo;
    }

    public void setTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo) {
        this.topDigitMapProfInfo = topDigitMapProfInfo;
    }

    public TopGponSrvPotsInfo getTopGponSrvPotsInfo() {
        return topGponSrvPotsInfo;
    }

    public void setTopGponSrvPotsInfo(TopGponSrvPotsInfo topGponSrvPotsInfo) {
        this.topGponSrvPotsInfo = topGponSrvPotsInfo;
    }

}
