/***********************************************************************
 * $Id: OltIgmpConfigServiceImpl.java,v1.0 2016-6-6 下午4:15:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.dao.OltIgmpConfigDao;
import com.topvision.ems.epon.igmpconfig.dao.OnuIgmpConfigDao;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCascadePort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpControlGroupBindRelation;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfileGroupRela;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcRecord;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpStaticFwd;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpUplinkPort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanInfo;
import com.topvision.ems.epon.igmpconfig.facade.OltIgmpConfigFacade;
import com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService;
import com.topvision.ems.epon.igmpconfig.service.OnuIgmpConfigService;
import com.topvision.ems.epon.trunk.service.OltTrunkService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2016-6-6-下午4:15:01
 *
 */
@Service("oltIgmpConfigService")
public class OltIgmpConfigServiceImpl extends BaseService implements OltIgmpConfigService, SynchronizedListener {

    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltIgmpConfigDao oltIgmpConfigDao;
    @Autowired
    private OnuIgmpConfigDao onuIgmpConfigDao;
    @Autowired
    private OnuIgmpConfigService onuIgmpConfigService;
    @Autowired
    private OltTrunkService oltTrunkService;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    public void addGlobalParam(IgmpGlobalParam globalParam) {
        oltIgmpConfigDao.insertOrUpdateGlobalParam(globalParam);
    }

    @Override
    public IgmpGlobalParam getGloablParam(Long entityId) {
        return oltIgmpConfigDao.queryGloablParam(entityId);
    }

    @Override
    public Integer getIgmpMode(Long entityId) {
        return oltIgmpConfigDao.queryIgmpMode(entityId);
    }

    @Override
    public void modifyIgmpMode(Long entityId, Integer igmpMode) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyIgmpMode(snmpParam, igmpMode);
        oltIgmpConfigDao.updateIgmpMode(entityId, igmpMode);
    }

    @Override
    public void modifyGlobalParam(IgmpGlobalParam globalParam) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(globalParam.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyGlobalParam(snmpParam, globalParam);
        oltIgmpConfigDao.updateGlobalParam(globalParam);
    }

    @Override
    public void refreshGlobalParam(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        IgmpGlobalParam globalParam = getOltIgmpFacade(snmpParam.getIpAddress()).getGloablParam(snmpParam);
        globalParam.setEntityId(entityId);
        oltIgmpConfigDao.insertOrUpdateGlobalParam(globalParam);
    }

    @Override
    public void addCascadePort(IgmpCascadePort cascadePort) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cascadePort.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createCascadePort(snmpParam, cascadePort);
        oltIgmpConfigDao.insertCascadePort(cascadePort);
    }

    @Override
    public List<IgmpCascadePort> getCascadePortList(Long entityId) {
        return oltIgmpConfigDao.queryCascadePortList(entityId);
    }

    @Override
    public void deleteCascadePort(IgmpCascadePort cascadePort) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cascadePort.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).destoryCascadePort(snmpParam, cascadePort);
        oltIgmpConfigDao.deleteCascadePort(cascadePort);
    }

    @Override
    public String batchDeleteCascadePort(Long entityId, List<IgmpCascadePort> cascadePorts) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        StringBuilder sb = new StringBuilder();
        Iterator<IgmpCascadePort> iterator = cascadePorts.iterator();
        while (iterator.hasNext()) {
            IgmpCascadePort cascadePort = iterator.next();
            try {
                getOltIgmpFacade(snmpParam.getIpAddress()).destoryCascadePort(snmpParam, cascadePort);
            } catch (Exception e) {
                // 从设备删除失败从集合中去除
                iterator.remove();
                // 同时记录portName
                sb.append(cascadePort.getPortName()).append(",");
                logger.error("", e);
            }
        }
        // 删除数据库中从设备删除成功的记录
        oltIgmpConfigDao.batchDeleteCascadePort(cascadePorts);
        return getFailInfo(sb);
    }

    @Override
    public void refreshCascadePort(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpCascadePort> cascadePortList = getOltIgmpFacade(snmpParam.getIpAddress())
                .getCascadePortList(snmpParam);
        oltIgmpConfigDao.batchInsertCascadePort(entityId, cascadePortList);
    }

    @Override
    public void addSnpUpLinkPort(IgmpSnpUplinkPort uplinkPort) {
        oltIgmpConfigDao.insertOrUpdateSnpUpLinkPort(uplinkPort);
    }

    @Override
    public IgmpSnpUplinkPort getSnpUplinkPort(Long entityId) {
        return oltIgmpConfigDao.querySnpUplinkPort(entityId);
    }

    @Override
    public void modifySnpUplinkPort(IgmpSnpUplinkPort uplinkPort) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(uplinkPort.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifySnpUplinkPort(snmpParam, uplinkPort);
        oltIgmpConfigDao.updateSnpUplinkPort(uplinkPort);
    }

    @Override
    public void refreshSnpUplinkPort(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 同步trunk组数据
        try {
            oltTrunkService.refreshSniTrunkConfig(entityId);
        } catch (Exception e) {
            logger.debug("refresh sni trunk fail", e);
        }
        IgmpSnpUplinkPort uplinkPort = getOltIgmpFacade(snmpParam.getIpAddress()).getSnpUplinkPort(snmpParam);
        uplinkPort.setEntityId(entityId);
        oltIgmpConfigDao.insertOrUpdateSnpUpLinkPort(uplinkPort);
    }

    @Override
    public void addSnpStaticFwd(IgmpSnpStaticFwd staticFwd) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(staticFwd.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createSnpStaticFwd(snmpParam, staticFwd);
        oltIgmpConfigDao.insertSnpStaticFwd(staticFwd);
    }

    @Override
    public List<IgmpSnpStaticFwd> getSnpStaticFwdList(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.querySnpStaticFwdList(paramMap);
    }

    @Override
    public Integer getSnpStaticFwdNum(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.querySnpStaticFwdNum(paramMap);
    }

    @Override
    public List<IgmpSnpStaticFwd> getAllSnpStaticFwdList(Long entityId) {
        return oltIgmpConfigDao.queryAllStaticFwdList(entityId);
    }

    @Override
    public void deleteSnpStaticFwd(IgmpSnpStaticFwd staticFwd) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(staticFwd.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).destorySnpStaticFwd(snmpParam, staticFwd);
        oltIgmpConfigDao.deleteSnpStaticFwd(staticFwd);
    }

    @Override
    public String batchDeleteSnpStaticFwd(Long entityId, List<IgmpSnpStaticFwd> staticFwds) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        StringBuilder sb = new StringBuilder();
        Iterator<IgmpSnpStaticFwd> iterator = staticFwds.iterator();
        while (iterator.hasNext()) {
            IgmpSnpStaticFwd staticFwd = iterator.next();
            try {
                getOltIgmpFacade(snmpParam.getIpAddress()).destorySnpStaticFwd(snmpParam, staticFwd);
            } catch (Exception e) {
                // 从设备删除失败从集合中去除
                iterator.remove();
                // 同时记录portName
                sb.append(staticFwd.getPortName()).append(",");
                logger.error("", e);
            }
        }
        // 删除数据库中从设备删除成功的记录
        oltIgmpConfigDao.batchDeleteSnpStaticFwd(staticFwds);
        return getFailInfo(sb);
    }

    @Override
    public void refreshSnpStaticFwd(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpSnpStaticFwd> staticFwdList = getOltIgmpFacade(snmpParam.getIpAddress())
                .getSnpStaticFwdList(snmpParam);
        oltIgmpConfigDao.batchInsertSnpStaticFwd(entityId, staticFwdList);
    }

    @Override
    public void addVlanInfo(IgmpVlanInfo vlanInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createVlanInfo(snmpParam, vlanInfo);
        oltIgmpConfigDao.insertVlanInfo(vlanInfo);
    }

    @Override
    public List<IgmpVlanInfo> getVlanInfoList(Long entityId) {
        return oltIgmpConfigDao.queryVlanInfoList(entityId);
    }

    @Override
    public void modifyVlanInfo(IgmpVlanInfo vlanInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyVlanInfo(snmpParam, vlanInfo);
        oltIgmpConfigDao.updateVlanInfo(vlanInfo);
    }

    @Override
    public void deleteVlanInfo(Long entityId, Integer vlanId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltIgmpFacade(snmpParam.getIpAddress()).destoryVlanInfo(snmpParam, vlanId);
        oltIgmpConfigDao.deleteVlanInfo(entityId, vlanId);
    }

    @Override
    public String batchDeleteVlanInfo(Long entityId, List<Integer> vlanIds) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = vlanIds.iterator();
        while (iterator.hasNext()) {
            Integer vlanId = iterator.next();
            try {
                getOltIgmpFacade(snmpParam.getIpAddress()).destoryVlanInfo(snmpParam, vlanId);
            } catch (Exception e) {
                // 设备上删除失败则将此vlanId从vlanIds中去除
                iterator.remove();
                // 同时将删除失败的vlanId记录
                sb.append(vlanId).append(",");
                logger.error("", e);
            }
        }
        // 从数据库删除设备上删除成功的VlanInfo
        oltIgmpConfigDao.batchDeleteVlanInfo(entityId, vlanIds);
        return getFailInfo(sb);
    }

    @Override
    public void refreshVlanData(Long entityId) {
        // 先刷新VLAN信息
        refreshVlanInfo(entityId);
        // 刷新VLAN信息成功后再刷新组播组信息
        refreshVlanGroup(entityId);
    }

    @Override
    public void refreshVlanInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpVlanInfo> vlanInfoList = getOltIgmpFacade(snmpParam.getIpAddress()).getVlanInfoList(snmpParam);
        oltIgmpConfigDao.batchInsertVlanInfo(entityId, vlanInfoList);
    }

    @Override
    public void addVlanGroup(IgmpVlanGroup groupInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createVlanGroup(snmpParam, groupInfo);
        oltIgmpConfigDao.insertVlanGroup(groupInfo);
        oltIgmpConfigDao.insertOrUpdateGroupName(groupInfo);
        // 因为组播组状态在全局Group表中维护,所有在添加完成后需要同步全局Group表记录
        try {
            refreshGlobalGroup(groupInfo.getEntityId());
        } catch (Exception e) {
            logger.error("Sync gloablGroup Failed when addVlanGroup", e);
        }
    }

    @Override
    public List<IgmpVlanGroup> getVlanGroupList(Long entityId, Integer vlanId) {
        return oltIgmpConfigDao.queryVlanGroupList(entityId, vlanId);
    }

    @Override
    public List<IgmpVlanGroup> getAllVlanGroup(Long entityId) {
        return oltIgmpConfigDao.queryAllVlanGroup(entityId);
    }

    @Override
    public List<IgmpVlanGroup> getVlanGroupList(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.queryVlanGroupList(paramMap);
    }

    @Override
    public Integer getVlanGroupNum(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.queryVlanGroupNum(paramMap);
    }

    @Override
    public List<Integer> getGroupIdList(Long entityId) {
        return oltIgmpConfigDao.queryGroupIdList(entityId);
    }

    @Override
    public List<Integer> getProfileIdList(Long entityId) {
        return oltIgmpConfigDao.queryProfileIdList(entityId);
    }

    @Override
    public void modifyVlanGroup(IgmpVlanGroup groupInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyVlanGroup(snmpParam, groupInfo);
        oltIgmpConfigDao.updateVlanGroup(groupInfo);
        oltIgmpConfigDao.insertOrUpdateGroupName(groupInfo);
        // 因为组播组状态在全局Group表中维护,所有修改预加入状态后需要同步全局Group表记录
        if (groupInfo.getJoinMode().equals(IgmpConstants.IGMP_JOINMODE_JOIN)) {
            try {
                refreshGlobalGroup(groupInfo.getEntityId());
            } catch (Exception e) {
                logger.error("Sync gloablGroup Failed when modifyVlanGroup", e);
            }
        }
    }

    @Override
    public void modifyGroupPreJoin(IgmpVlanGroup groupInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyVlanGroup(snmpParam, groupInfo);
        oltIgmpConfigDao.updateGroupPreJoin(groupInfo);
        // 因为组播组状态在全局Group表中维护,所有修改预加入状态后需要同步全局Group表记录
        try {
            refreshGlobalGroup(groupInfo.getEntityId());
        } catch (Exception e) {
            logger.error("Sync gloablGroup Failed when modifyGroupPreJoin", e);
        }
    }

    @Override
    public void deleteVlanGroup(IgmpVlanGroup groupInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).destoryVlanGroup(snmpParam, groupInfo);
        oltIgmpConfigDao.deleteVlanGroup(groupInfo.getEntityId(), groupInfo.getGroupId());
        oltIgmpConfigDao.deleteGroupName(groupInfo);
        // 因为组播组状态在全局Group表中维护,所有修改预加入状态后需要同步全局Group表记录
        try {
            refreshGlobalGroup(groupInfo.getEntityId());
        } catch (Exception e) {
            logger.error("Sync gloablGroup Failed when deleteVlanGroup", e);
        }
    }

    @Override
    public String batchDeleteVlanGroup(Long entityId, List<IgmpVlanGroup> igmpVlanGroups) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        StringBuilder sb = new StringBuilder();
        Iterator<IgmpVlanGroup> iterator = igmpVlanGroups.iterator();
        while (iterator.hasNext()) {
            IgmpVlanGroup igmpVlanGroup = iterator.next();
            try {
                getOltIgmpFacade(snmpParam.getIpAddress()).destoryVlanGroup(snmpParam, igmpVlanGroup);
            } catch (Exception e) {
                // 从设备删除失败的移出集合
                iterator.remove();
                // 同时记录
                sb.append(igmpVlanGroup.getGroupId()).append(",");
                logger.error("", e);
            }
        }
        // 批量删除从设备删除成功的记录
        oltIgmpConfigDao.batchDeleteVlanGroup(igmpVlanGroups);
        oltIgmpConfigDao.batchDeleteGroupName(igmpVlanGroups);
        try {
            // 因为组播组状态在全局Group表中维护,所有修改预加入状态后需要同步全局Group表记录
            refreshGlobalGroup(entityId);
        } catch (Exception e) {
            logger.error("Sync gloablGroup Failed when deleteVlanGroup", e);
        }
        return getFailInfo(sb);
    }

    @Override
    public void refreshVlanGroup(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpVlanGroup> groupList = getOltIgmpFacade(snmpParam.getIpAddress()).getVlanGroupList(snmpParam);
        oltIgmpConfigDao.batchInsertVlanGroup(entityId, groupList);
    }

    @Override
    public List<IgmpGlobalGroup> getGlobalGroupList(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.queryGlobalGroupList(paramMap);
    }

    @Override
    public Integer getGlobalGroupNum(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.queryGlobalGroupNum(paramMap);
    }

    @Override
    public void refreshGlobalGroup(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpGlobalGroup> globalGroupList = getOltIgmpFacade(snmpParam.getIpAddress())
                .getGlobalGroupList(snmpParam);
        oltIgmpConfigDao.batchInsertGlobalGroup(entityId, globalGroupList);
    }

    @Override
    public void addCtcParam(IgmpCtcParam ctcParam) {
        oltIgmpConfigDao.insertOrUpdateCtcParam(ctcParam);
    }

    @Override
    public IgmpCtcParam getCtcParam(Long entityId) {
        return oltIgmpConfigDao.queryCtcParam(entityId);
    }

    @Override
    public void modifyCtcParam(IgmpCtcParam ctcParam) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ctcParam.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyCtcParam(snmpParam, ctcParam);
        oltIgmpConfigDao.updateCtcParam(ctcParam);
    }

    @Override
    public void reportCtcCdr(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltIgmpFacade(snmpParam.getIpAddress()).reportCtcCdr(snmpParam);
    }

    @Override
    public void refreshCtcParam(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        IgmpCtcParam ctcParam = getOltIgmpFacade(snmpParam.getIpAddress()).getCtcParam(snmpParam);
        ctcParam.setEntityId(entityId);
        oltIgmpConfigDao.insertOrUpdateCtcParam(ctcParam);
    }

    @Override
    public void addCtcProfile(IgmpCtcProfile ctcProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ctcProfile.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createCtcProfile(snmpParam, ctcProfile);
        oltIgmpConfigDao.insertCtcProfile(ctcProfile);
        oltIgmpConfigDao.insertProfileName(ctcProfile);
    }

    @Override
    public IgmpCtcProfile getCtcProfile(Long entityId, Integer profileId) {
        return oltIgmpConfigDao.queryCtcProfile(entityId, profileId);
    }

    @Override
    public List<IgmpCtcProfile> getCtcProfileList(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.queryCtcProfileList(paramMap);
    }

    @Override
    public Integer getCtcProfileNum(Map<String, Object> paramMap) {
        return oltIgmpConfigDao.queryCtcProfileNum(paramMap);
    }

    @Override
    public List<IgmpCtcProfile> getAllCtcProfile(Long entityId) {
        return oltIgmpConfigDao.queryAllCtcProfile(entityId);
    }

    @Override
    public void modifyCtcProfile(IgmpCtcProfile ctcProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ctcProfile.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).modifyCtcProfile(snmpParam, ctcProfile);
        oltIgmpConfigDao.updateCtcProfile(ctcProfile);
        oltIgmpConfigDao.insertOrUpdateProfileName(ctcProfile);
    }

    @Override
    public void deleteCtcProfile(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltIgmpFacade(snmpParam.getIpAddress()).destoryCtcProfile(snmpParam, profileId);
        oltIgmpConfigDao.deleteCtcProfile(entityId, profileId);
        oltIgmpConfigDao.deleteProfileName(entityId, profileId);
    }

    @Override
    public String batchDeleteCtcProfile(Long entityId, List<Integer> profileIds) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Iterator<Integer> iterator = profileIds.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Integer profileId = iterator.next();
            try {
                getOltIgmpFacade(snmpParam.getIpAddress()).destoryCtcProfile(snmpParam, profileId);
            } catch (Exception e) {
                // 从设备删除失败，则从集合中移出该项
                iterator.remove();
                // 同时记录失败记录
                sb.append(profileId).append(",");
                logger.error("", e);
            }
        }
        // 批量删除从设备删除成功的数据
        oltIgmpConfigDao.batchDeleteCtcProfile(entityId, profileIds);
        oltIgmpConfigDao.batchDeleteProfileName(entityId, profileIds);
        return getFailInfo(sb);
    }

    @Override
    public void refreshProfileData(Long entityId) {
        // 刷新可控组播模板信息
        refreshCtcProfile(entityId);
        // 在刷新模板信息成功后再刷新模板关联组播组信息
        refreshProfileGroupRela(entityId);
    }

    @Override
    public void refreshCtcProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpCtcProfile> profileList = getOltIgmpFacade(snmpParam.getIpAddress()).getCtcProfileList(snmpParam);
        oltIgmpConfigDao.batchInsertCtcProfile(entityId, profileList);
    }

    @Override
    public void addProfileGroupRela(IgmpCtcProfileGroupRela profileGroup) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profileGroup.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createProfileGroupRela(snmpParam, profileGroup);
        oltIgmpConfigDao.insertProfileGroupRela(profileGroup);
    }

    @Override
    public List<IgmpControlGroupBindRelation> getProfileGroupRelaList(Long entityId, Integer profileId) {
        List<IgmpControlGroupBindRelation> list = oltIgmpConfigDao.queryProfileGroupRelaList(entityId, profileId);
        Map<Integer, IgmpControlGroupBindRelation> map = new HashMap<Integer, IgmpControlGroupBindRelation>();
        for (IgmpControlGroupBindRelation bind : list) {
            Integer key = bind.getGroupId();
            IgmpControlGroupBindRelation bindRelation = map.get(key);
            if (bindRelation == null) {
                map.put(key, bind);
                continue;
            }
            Integer newProfileId = bind.getProfileId();
            if (profileId.equals(newProfileId)) {
                map.put(key, bind);
            } else if (bindRelation.getProfileId() != profileId && newProfileId == null) {
                map.put(key, bind);
            }
        }
        return new ArrayList<IgmpControlGroupBindRelation>(map.values());
    }

    @Override
    public List<IgmpCtcProfileGroupRela> getAllProfileGroupRela(Long entityId) {
        return oltIgmpConfigDao.queryAllProfileGroupRela(entityId);
    }

    @Override
    public void deleteProfileGroupRela(IgmpCtcProfileGroupRela profileGroup) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profileGroup.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).destoryProfileGroupRela(snmpParam, profileGroup);
        oltIgmpConfigDao.deleteProfileGroupRela(profileGroup);
    }

    @Override
    public void refreshProfileGroupRela(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpCtcProfileGroupRela> profileGroupRelaList = getOltIgmpFacade(snmpParam.getIpAddress())
                .getProfileGroupRelaList(snmpParam);
        oltIgmpConfigDao.batchInsertProfileGroupRela(entityId, profileGroupRelaList);
    }

    @Override
    public void addCtcRecord(IgmpCtcRecord ctcRecord) {
        oltIgmpConfigDao.insertCtcRecord(ctcRecord);
    }

    @Override
    public List<IgmpCtcRecord> getCtcRecordList(Map<String, Object> paramsMap) {
        return oltIgmpConfigDao.queryCtcRecordList(paramsMap);
    }

    @Override
    public Integer getCtcRecordNum(Map<String, Object> paramsMap) {
        return oltIgmpConfigDao.queryCtcRecordNum(paramsMap);
    }

    @Override
    public void refreshCtcRecord(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpCtcRecord> recordList = getOltIgmpFacade(snmpParam.getIpAddress()).getCtcRecordList(snmpParam);
        oltIgmpConfigDao.batchInserCtcRecord(entityId, recordList);
    }

    @Override
    public void refreshUniBindCtcProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpUniBindCtcProfile> bindProfileList = getOltIgmpFacade(snmpParam.getIpAddress())
                .getBindCtcProfileList(snmpParam);
        onuIgmpConfigDao.batchInsertBindProfile(entityId, bindProfileList);
    }

    @Override
    public void refreshOnuConfigList(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpOnuConfig> onuConfigList = getOltIgmpFacade(snmpParam.getIpAddress()).getIgmpOnuConfigList(snmpParam);
        onuIgmpConfigDao.batchInsertOnuConfig(entityId, onuConfigList);
    }

    @Override
    public void refreshAllUniConfigList(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpUniConfig> uniConfigList = getOltIgmpFacade(snmpParam.getIpAddress()).getAllUniConfigList(snmpParam);
        onuIgmpConfigDao.batchInsertUniConfig(entityId, uniConfigList);
    }

    @Override
    public void refreshAllUniVlanTransList(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IgmpUniVlanTrans> vlanTransList = getOltIgmpFacade(snmpParam.getIpAddress()).getAllUniVlanTrans(snmpParam);
        onuIgmpConfigDao.batchInsertVlanTrans(entityId, vlanTransList);
    }

    @Override
    public List<IgmpPortInfo> getSniPortByType(Long entityId, List<Integer> typeList) {
        return oltIgmpConfigDao.querySniPortByType(entityId, typeList);
    }

    @Override
    public List<IgmpPortInfo> getSniPort(Long entityId) {
        return oltIgmpConfigDao.querySniPort(entityId);
    }

    @Override
    public List<IgmpPortInfo> getSniAggList(Long entityId) {
        return oltIgmpConfigDao.querySniAggList(entityId);
    }

    @Override
    public List<IgmpPortInfo> getPonPortByType(Long entityId, Integer portType) {
        return oltIgmpConfigDao.queryPonPortByType(entityId, portType);
    }

    @Override
    public List<IgmpCascadePort> getCascadePortByType(Long entityId, Integer portType) {
        return oltIgmpConfigDao.queryCascadePortByType(entityId, portType);
    }

    @Override
    public void deleteVlanUplink(Long entityId) {
        List<IgmpVlanInfo> vlanList = this.getVlanInfoList(entityId);
        for (IgmpVlanInfo vlanInfo : vlanList) {
            if (!vlanInfo.getPortType().equals(IgmpConstants.IGMP_PORTTYPE_INVALID)) {
                vlanInfo.setPortType(IgmpConstants.IGMP_PORTTYPE_INVALID);
                vlanInfo.setSlotNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
                vlanInfo.setPortNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
                vlanInfo.setUplinkAggId(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
                this.modifyVlanInfo(vlanInfo);
            }
        }
    }

    @Override
    public void deleteSnpUplink(Long entityId) {
        IgmpSnpUplinkPort snpUplinkPort = this.getSnpUplinkPort(entityId);
        if (!snpUplinkPort.getPortType().equals(IgmpConstants.IGMP_PORTTYPE_INVALID)) {
            snpUplinkPort.setPortType(IgmpConstants.IGMP_PORTTYPE_INVALID);
            snpUplinkPort.setSlotNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
            snpUplinkPort.setPortNo(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
            snpUplinkPort.setUplinkAggId(IgmpConstants.IGMP_DEFAULT_PORTFLAG);
            this.modifySnpUplinkPort(snpUplinkPort);
        }
    }

    @Override
    public void deleteAllVlanConfig(Long entityId) {
        // 先进行可控模板关联组播组的删除业务
        this.deleteAllProfileGroup(entityId);
        // 再进行VLAN的删除
        List<IgmpVlanInfo> vlanList = this.getVlanInfoList(entityId);
        for (IgmpVlanInfo vlanInfo : vlanList) {
            this.deleteVlanInfo(entityId, vlanInfo.getVlanId());
        }
    }

    @Override
    public void deleteAllProfileGroup(Long entityId) {
        // 如果模板被UNI端口绑定,则需要先解绑定模板
        this.deleteHasGroupBindProfile(entityId);
        // 获取所有可控模板关联组播组
        List<IgmpCtcProfileGroupRela> groupRelaLilst = this.getAllProfileGroupRela(entityId);
        for (IgmpCtcProfileGroupRela groupRela : groupRelaLilst) {
            // 解除UNI端口绑定模板关系后,再将模板绑定的组播组删除
            this.deleteProfileGroupRela(groupRela);
        }
    }

    /**
     * 删除关联了Group的模板与UNI端口绑定关系
     * 
     * @param entityId
     */
    private void deleteHasGroupBindProfile(Long entityId) {
        List<IgmpUniBindCtcProfile> uniBindList = onuIgmpConfigService.getHasGroupBindProfile(entityId);
        for (IgmpUniBindCtcProfile uniBindProfie : uniBindList) {
            onuIgmpConfigService.deleteBindCtcProfile(uniBindProfie);
        }
    }

    @Override
    public void modifyCtcToDisable(Long entityId) {
        IgmpCtcParam ctcConfig = this.getCtcParam(entityId);
        if (ctcConfig.getCtcEnable().equals(EponConstants.ABALITY_ENABLE)) {
            ctcConfig.setCtcEnable(EponConstants.ABALITY_DISABLE);
            // 防止之前配置了上报日志,故将上报日志重置为不上报
            ctcConfig.setCdrReport(IgmpConstants.IGMP_CDRREPORT_NO);
            this.modifyCtcParam(ctcConfig);
        }
    }

    @Override
    public void deleteAllSnpStaticFwd(Long entityId) {
        List<IgmpSnpStaticFwd> staticFwdList = this.getAllSnpStaticFwdList(entityId);
        for (IgmpSnpStaticFwd staticFwd : staticFwdList) {
            this.deleteSnpStaticFwd(staticFwd);
        }
    }

    @Override
    public void deleteSpecialGroup(Long entityId, boolean withSrcIp) {
        // 首先获取包含指定组播组的模板与UNI绑定信息,并进行删除
        deleteSpecialUniBindInfo(entityId, withSrcIp);
        // 然后获取可控模板与指定组播组的关联关系,并进行删除
        deleteSpecialProfileGroup(entityId, withSrcIp);
        // 最后获取指定的组播组,并进行删除
        List<IgmpVlanGroup> groupList = null;
        if (withSrcIp) {
            groupList = oltIgmpConfigDao.queryWithSrcGroup(entityId);
        } else {
            groupList = oltIgmpConfigDao.queryWithoutSrcGroup(entityId);
        }
        for (IgmpVlanGroup groupInfo : groupList) {
            IgmpVlanGroup group = new IgmpVlanGroup();
            group.setEntityId(groupInfo.getEntityId());
            group.setVlanId(groupInfo.getVlanId());
            group.setGroupId(groupInfo.getGroupId());
            group.setGroupIp(groupInfo.getGroupIp());
            group.setGroupSrcIp(groupInfo.getGroupSrcIp());
            this.deleteVlanGroup(group);
        }
    }

    /**
     * 删除指定的模板关联组播组
     * 
     * @param entityId
     * @param withSrcIp
     */
    private void deleteSpecialProfileGroup(Long entityId, boolean withSrcIp) {
        List<IgmpCtcProfileGroupRela> relaList = null;
        if (withSrcIp) {
            relaList = oltIgmpConfigDao.queryWithSrcGroupRela(entityId);
        } else {
            relaList = oltIgmpConfigDao.queryWithoutSrcGroupRela(entityId);
        }
        for (IgmpCtcProfileGroupRela relaInfo : relaList) {
            this.deleteProfileGroupRela(relaInfo);
        }
    }

    /**
     * 删除指定的UNI端口绑定模板信息
     * 
     * @param entityId
     * @param withSrcIp
     */
    private void deleteSpecialUniBindInfo(Long entityId, boolean withSrcIp) {
        List<IgmpUniBindCtcProfile> uniBindList = null;
        if (withSrcIp) {
            uniBindList = onuIgmpConfigDao.queryWithSrcGroupBindProfile(entityId);
        } else {
            uniBindList = onuIgmpConfigDao.queryWithoutSrcGroupBindProfile(entityId);
        }
        for (IgmpUniBindCtcProfile uniBindInfo : uniBindList) {
            onuIgmpConfigService.deleteBindCtcProfile(uniBindInfo);
        }
    }

    @Override
    public void refreshOltIgmpData(Long entityId) {
        // 获取OLT IGMP全局配置
        try {
            refreshGlobalParam(entityId);
            logger.info("refreshGlobalParam finish");
        } catch (Exception e) {
            logger.error("refreshGlobalParam wrong", e);
        }
        // 获取级联端口配置
        try {
            refreshCascadePort(entityId);
            logger.info("refreshCascadePort finish");
        } catch (Exception e) {
            logger.error("refreshCascadePort wrong", e);
        }
        // 获取Snooping模式下的上行端口配置
        try {
            refreshSnpUplinkPort(entityId);
            logger.info("refreshSnpUplinkPort finish");
        } catch (Exception e) {
            logger.error("refreshSnpUplinkPort wrong", e);
        }
        // 获取Snooping模式下的静态加入配置
        try {
            refreshSnpStaticFwd(entityId);
            logger.info("refreshSnpStaticFwd finish");
        } catch (Exception e) {
            logger.error("refreshSnpStaticFwd wrong", e);
        }
        // 获取IGMP VLAN信息
        try {
            refreshVlanInfo(entityId);
            logger.info("refreshVlanInfo finish");
        } catch (Exception e) {
            logger.error("refreshVlanInfo wrong", e);
        }
        // 获取IGMP组播组信息
        try {
            refreshVlanGroup(entityId);
            logger.info("refreshGroupInfo finish");
        } catch (Exception e) {
            logger.error("refreshGroupInfo wrong", e);
        }
        // 获取全局IGMP组播组信息
        try {
            refreshGlobalGroup(entityId);
            logger.info("refreshGlobalGroup finish");
        } catch (Exception e) {
            logger.error("refreshGlobalGroup wrong", e);
        }
        // 获取可控组播信息
        try {
            refreshCtcParam(entityId);
            logger.info("refreshCtcParam finish");
        } catch (Exception e) {
            logger.error("refreshCtcParam wrong", e);
        }
        // 获取可控组播模板信息
        try {
            refreshCtcProfile(entityId);
            logger.info("refreshCtcProfile finish");
        } catch (Exception e) {
            logger.error("refreshCtcProfile wrong", e);
        }
        // 获取可控组播模板关联组播组信息
        try {
            refreshProfileGroupRela(entityId);
            logger.info("refreshProfileGroupRela finish");
        } catch (Exception e) {
            logger.error("refreshProfileGroupRela wrong", e);
        }
        // 获取呼叫记录信息
        try {
            refreshCtcRecord(entityId);
            logger.info("refreshCtcRecord finish");
        } catch (Exception e) {
            logger.error("refreshCtcRecord wrong", e);
        }
    }

    @Override
    public void refreshOnuIgmpData(Long entityId) {
        // 获取UNI绑定组播模板信息
        try {
            refreshUniBindCtcProfile(entityId);
            logger.info("refreshUniBindCtcProfile finish");
        } catch (Exception e) {
            logger.error("refreshUniBindCtcProfile wrong", e);
        }
        // 获取ONU IGMP配置
        try {
            refreshOnuConfigList(entityId);
            logger.info("refreshOnuConfigList finish");
        } catch (Exception e) {
            logger.error("refreshOnuConfigList wrong", e);
        }
        // 获取UNI IGMP配置
        try {
            refreshAllUniConfigList(entityId);
            logger.info("refreshAllUniConfigList finish");
        } catch (Exception e) {
            logger.error("refreshAllUniConfigList wrong", e);
        }
        // 获取UNI VLAN转换配置
        try {
            refreshAllUniVlanTransList(entityId);
            logger.info("refreshAllUniVlanTransList finish");
        } catch (Exception e) {
            logger.error("refreshAllUniVlanTransList wrong", e);
        }
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        // 进行所有IGMP相关业务发现的处理
        Long entityId = event.getEntityId();
        refreshOltIgmpData(entityId);
        refreshOnuIgmpData(entityId);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {

    }

    /**
     * 获取OLT IGMP配置Facade
     * 
     * @param ip
     * @return
     */
    private OltIgmpConfigFacade getOltIgmpFacade(String ip) {
        return facadeFactory.getFacade(ip, OltIgmpConfigFacade.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService#importIgmpGroupAlias(com.
     * topvision .ems.epon.igmpconfig.domain.IgmpVlanGroup)
     */
    @Override
    public void updateIgmpGroupAlias(IgmpVlanGroup group) {
        oltIgmpConfigDao.insertOrUpdateGroupName(group);
    }

    @Override
    public void batchAddVlanGroup(IgmpVlanGroup groupInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(groupInfo.getEntityId());
        getOltIgmpFacade(snmpParam.getIpAddress()).createVlanGroup(snmpParam, groupInfo);
        oltIgmpConfigDao.insertVlanGroup(groupInfo);
        oltIgmpConfigDao.insertOrUpdateGroupName(groupInfo);
    }

    private String getFailInfo(StringBuilder sb) {
        return sb.toString().equals("") ? "none" : sb.toString().substring(0, sb.toString().length() - 1);
    }
}
