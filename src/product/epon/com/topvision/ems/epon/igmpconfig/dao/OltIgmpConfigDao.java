/***********************************************************************
 * $Id: OltIgmpConfigDao.java,v1.0 2016-6-6 下午4:18:03 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.dao;

import java.util.List;
import java.util.Map;

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
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2016-6-6-下午4:18:03
 *
 */
public interface OltIgmpConfigDao extends BaseEntityDao<Object> {

    /**
     * 插入IGMP全局参数
     * 
     * @param globalParam
     */
    void insertOrUpdateGlobalParam(IgmpGlobalParam globalParam);

    /**
     * 获取OLT IGMP全局参数配置
     * 
     * @param entityId
     * @return
     */
    IgmpGlobalParam queryGloablParam(Long entityId);

    /**
     * 获取IGMP模式
     * 
     * @param entityId
     * @return
     */
    Integer queryIgmpMode(Long entityId);

    /**
     * 修改OLT　IGMP模式
     * 
     * @param entityId
     * @param igmpMode
     */
    void updateIgmpMode(Long entityId, Integer igmpMode);

    /**
     * 修改OLT IGMP全局参数配置
     * 
     * @param globalParam
     */
    void updateGlobalParam(IgmpGlobalParam globalParam);

    /**
     * 插入IGMP级联端口
     * 
     * @param cascadePort
     */
    void insertCascadePort(IgmpCascadePort cascadePort);

    /**
     * 批量插入级联端口
     * 
     * @param entityId
     * @param cascadePortList
     */
    void batchInsertCascadePort(Long entityId, List<IgmpCascadePort> cascadePortList);

    /**
     * 获取级联端口列表
     * 
     * @param entityId
     * @return
     */
    List<IgmpCascadePort> queryCascadePortList(Long entityId);

    /**
     * 删除IGMP级联端口
     * 
     * @param cascadePort
     */
    void deleteCascadePort(IgmpCascadePort cascadePort);

    /**
     * 批量删除IGMP级联端口
     * 
     * @param cascadePorts
     */
    void batchDeleteCascadePort(List<IgmpCascadePort> cascadePorts);

    /**
     * 插入Snooping模式上行端口配置
     * 
     * @param uplinkPort
     */
    void insertOrUpdateSnpUpLinkPort(IgmpSnpUplinkPort uplinkPort);

    /**
     * 批量插入Snooping模式上行端口配置
     * 
     * @param uplinkPortList
     */
    void batchInsertSnpUplinkPort(List<IgmpSnpUplinkPort> uplinkPortList);

    /**
     * 查询Snooping模式上行端口配置
     * 
     * @param entityId
     * @return
     */
    IgmpSnpUplinkPort querySnpUplinkPort(Long entityId);

    /**
     * 修改Snooping模式上行端口配置
     * 
     * @param uplinkPort
     */
    void updateSnpUplinkPort(IgmpSnpUplinkPort uplinkPort);

    /**
     * 插入Snooping模式静态转发组配置
     * 
     * @param staticFwd
     */
    void insertSnpStaticFwd(IgmpSnpStaticFwd staticFwd);

    /**
     * 批量插入Snooping模式静态转发组配置
     * 
     * @param entityId
     * @param staticFwdList
     */
    void batchInsertSnpStaticFwd(Long entityId, List<IgmpSnpStaticFwd> staticFwdList);

    /**
     * 获取Snooping模式静态转发组配置
     * 
     * @param paramMap
     * @return
     */
    List<IgmpSnpStaticFwd> querySnpStaticFwdList(Map<String, Object> paramMap);

    /**
     * 获取静态加入的数量
     * 
     * @param paramMap
     * @return
     */
    Integer querySnpStaticFwdNum(Map<String, Object> paramMap);

    /**
     * 获取所有的静态加入配置
     * 
     * @param entityId
     * @return
     */
    List<IgmpSnpStaticFwd> queryAllStaticFwdList(Long entityId);

    /**
     * 删除Snooping模式静态转发组配置
     * 
     * @param staticFwd
     */
    void deleteSnpStaticFwd(IgmpSnpStaticFwd staticFwd);

    /**
     * 批量删除Snooping模式静态转发组配置
     * 
     * @param staticFwds
     */
    void batchDeleteSnpStaticFwd(List<IgmpSnpStaticFwd> staticFwds);

    /**
     * 插入IGMP VLAN信息
     * 
     * @param vlanInfo
     */
    void insertVlanInfo(IgmpVlanInfo vlanInfo);

    /**
     * 批量插入IGMP VLAN信息
     * 
     * @param entityId
     * @param vlanInfoList
     */
    void batchInsertVlanInfo(Long entityId, List<IgmpVlanInfo> vlanInfoList);

    /**
     * 查询IGMP VLAN信息
     * 
     * @param entityId
     * @return
     */
    List<IgmpVlanInfo> queryVlanInfoList(Long entityId);

    /**
     * 修改IGMP VLAN信息
     * 
     * @param vlanInfo
     */
    void updateVlanInfo(IgmpVlanInfo vlanInfo);

    /**
     * 删除IGMP VLAN信息
     * 
     * @param entityId
     * @param vlanId
     */
    void deleteVlanInfo(Long entityId, Integer vlanId);

    /**
     * 批量删除IGMP VLAN信息
     * 
     * @param entityId
     * @param vlanId
     */
    void batchDeleteVlanInfo(Long entityId, List<Integer> vlanIds);

    /**
     * 插入IMGP组播组信息
     * 
     * @param groupInfo
     */
    void insertVlanGroup(IgmpVlanGroup groupInfo);

    /**
     * 批量插入IMGP组播组信息
     * 
     * @param entityId
     * @param groupList
     */
    void batchInsertVlanGroup(Long entityId, List<IgmpVlanGroup> groupList);

    /**
     * 获取IMGP组播组信息
     * 
     * @param entityId
     * @param vlanId
     * @return
     */
    List<IgmpVlanGroup> queryVlanGroupList(Long entityId, Integer vlanId);

    /**
     * 获取已经配置的组播组
     * 
     * @param entityId
     * @return
     */
    List<IgmpVlanGroup> queryAllVlanGroup(Long entityId);

    /**
     * 获取组播组信息列表
     * 
     * @param paramMap
     * @return
     */
    List<IgmpVlanGroup> queryVlanGroupList(Map<String, Object> paramMap);

    /**
     * 获取组播组数量
     * 
     * @param paramMap
     * @return
     */
    Integer queryVlanGroupNum(Map<String, Object> paramMap);

    /**
     * 获取所有已经配置的组播组ID
     * 
     * @param entityId
     * @return
     */
    List<Integer> queryGroupIdList(Long entityId);

    /**
     * 查询所有带源Ip的组播组
     * 
     * @param entityId
     * @return
     */
    List<IgmpVlanGroup> queryWithSrcGroup(Long entityId);

    /**
     * 查询所有不带源IP的组播组
     * 
     * @param entityId
     * @return
     */
    List<IgmpVlanGroup> queryWithoutSrcGroup(Long entityId);

    /**
     * 修改组播组信息
     * 
     * @param groupInfo
     */
    void updateVlanGroup(IgmpVlanGroup groupInfo);

    /**
     * 修改组播组的预加入状态
     */
    void updateGroupPreJoin(IgmpVlanGroup groupInfo);

    /**
     * 删除IMGP组播组信息
     * 
     * @param entityId
     * @param groulId
     */
    void deleteVlanGroup(Long entityId, Integer groulId);

    /**
     * 批量删除IMGP组播组信息
     * 
     * @param igmpVlanGroups
     */
    void batchDeleteVlanGroup(List<IgmpVlanGroup> igmpVlanGroups);

    /**
     * 批量插入全局IMGP组播组信息
     * 
     * @param entityId
     * @param groupList
     */
    void batchInsertGlobalGroup(Long entityId, List<IgmpGlobalGroup> groupList);

    /**
     * 获取全局IMGP组播组信息
     * 
     * @param paramMap
     * @return
     */
    List<IgmpGlobalGroup> queryGlobalGroupList(Map<String, Object> paramMap);

    /**
     * 获取全局组播组数量
     * 
     * @param paramMap
     * @return
     */
    Integer queryGlobalGroupNum(Map<String, Object> paramMap);

    /**
     * 插入IGMP可控组播配置
     * 
     * @param ctcParam
     */
    void insertOrUpdateCtcParam(IgmpCtcParam ctcParam);

    /**
     * 获取IGMP可控组播配置
     * 
     * @param entityId
     * @return
     */
    IgmpCtcParam queryCtcParam(Long entityId);

    /**
     * 修改IGMP可控组播配置
     * 
     * @param ctcParam
     */
    void updateCtcParam(IgmpCtcParam ctcParam);

    /**
     * 插入可控模板
     * 
     * @param ctcProfile
     */
    void insertCtcProfile(IgmpCtcProfile ctcProfile);

    /**
     * 批量插入可控模板
     * 
     * @param entityId
     * @param profileList
     */
    void batchInsertCtcProfile(Long entityId, List<IgmpCtcProfile> profileList);

    /**
     * 查询可控模板
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    IgmpCtcProfile queryCtcProfile(Long entityId, Integer profileId);

    /**
     * 查询可控模板列表
     * 
     * @param paramMap
     * @return
     */
    List<IgmpCtcProfile> queryCtcProfileList(Map<String, Object> paramMap);

    /**
     * 查询可控模板数量
     * 
     * @param paramMap
     * @return
     */
    Integer queryCtcProfileNum(Map<String, Object> paramMap);

    /**
     * 获取所有的可控组播模板
     * 
     * @param entityId
     * @return
     */
    List<IgmpCtcProfile> queryAllCtcProfile(Long entityId);

    /**
     * 获取所有已经配置的模板ID
     * 
     * @param entityId
     * @return
     */
    List<Integer> queryProfileIdList(Long entityId);

    /**
     * 更新可控模板
     * 
     * @param ctcProfile
     */
    void updateCtcProfile(IgmpCtcProfile ctcProfile);

    /**
     * 删除可控模板
     * 
     * @param entityId
     * @param profileId
     */
    void deleteCtcProfile(Long entityId, Integer profileId);

    /**
     * 批量删除可控模板
     * 
     * @param entityId
     * @param profileIds
     */
    void batchDeleteCtcProfile(Long entityId, List<Integer> profileIds);

    /**
     * 插入可控模板关联组播组
     * 
     * @param profileGroup
     */
    void insertProfileGroupRela(IgmpCtcProfileGroupRela profileGroup);

    /**
     * 批量插入可控模板关联组播组
     * 
     * @param enti
     * @param profileGroupList
     */
    void batchInsertProfileGroupRela(Long entityId, List<IgmpCtcProfileGroupRela> profileGroupList);

    /**
     * 查询可控模板关联组播组
     * 
     * @param profileId
     * @return
     */
    List<IgmpControlGroupBindRelation> queryProfileGroupRelaList(Long entityId, Integer profileId);

    /**
     * 获取所有的可控模板关联组播组信息
     * 
     * @param entityId
     * @return
     */
    List<IgmpCtcProfileGroupRela> queryAllProfileGroupRela(Long entityId);

    /**
     * 查询所有关联了带源IP的组播组的模板关联关系
     * 
     * @param entityId
     * @return
     */
    List<IgmpCtcProfileGroupRela> queryWithSrcGroupRela(Long entityId);

    /**
     * 查询所有关联了不带源IP的组播组的模板关联关系
     * 
     * @param entityId
     * @return
     */
    List<IgmpCtcProfileGroupRela> queryWithoutSrcGroupRela(Long entityId);

    /**
     * 删除可控模板关联组播组
     * 
     * @param profileGroup
     */
    void deleteProfileGroupRela(IgmpCtcProfileGroupRela profileGroup);

    /**
     * 插入呼叫记录
     * 
     * @param ctcRecord
     */
    void insertCtcRecord(IgmpCtcRecord ctcRecord);

    /**
     * 批量添加呼叫记录
     * 
     * @param entityId
     * @param recordList
     */
    void batchInserCtcRecord(Long entityId, List<IgmpCtcRecord> recordList);

    /**
     * 获取呼叫记录列表
     * 
     * @param paramsMap
     * @return
     */
    List<IgmpCtcRecord> queryCtcRecordList(Map<String, Object> paramsMap);

    /**
     * 获取呼叫记录数量
     * 
     * @param paramsMap
     * @return
     */
    Integer queryCtcRecordNum(Map<String, Object> paramsMap);

    /**
     * 根据SNI端口类型获取端口列表
     * 
     * @param entityId
     * @param typeList
     * @return
     */
    List<IgmpPortInfo> querySniPortByType(Long entityId, List<Integer> typeList);

    /**
     * 获取SNI端口列表
     * 
     * @param entityId
     * @return
     */
    List<IgmpPortInfo> querySniPort(Long entityId);

    /**
     * 获取上联端口聚合组列表
     * 
     * @param entityId
     * @return
     */
    List<IgmpPortInfo> querySniAggList(Long entityId);

    /**
     * 根据PON端口类型获取端口列表
     * 
     * @param entityId
     * @param portType
     * @return
     */
    List<IgmpPortInfo> queryPonPortByType(Long entityId, Integer portType);

    /**
     * 获取指定端口类型的级联端口
     * 
     * @param entityId
     * @param portType
     * @return
     */
    List<IgmpCascadePort> queryCascadePortByType(Long entityId, Integer portType);

    /**
     * 插入或者修改组播组别名
     * 
     * @param groupInfo
     */
    void insertOrUpdateGroupName(IgmpVlanGroup groupInfo);

    /**
     * 删除组播组别名
     * 
     * @param groupInfo
     */
    void deleteGroupName(IgmpVlanGroup groupInfo);

    /**
     * 批量删除组播组别名
     * 
     * @param groupInfo
     */
    void batchDeleteGroupName(List<IgmpVlanGroup> groupInfos);

    /**
     * 插入模板别名
     * 
     * @param ctcProfile
     */
    void insertProfileName(IgmpCtcProfile ctcProfile);

    /**
     * 插入或者修改模板别名
     * 
     * @param ctcProfile
     */
    void insertOrUpdateProfileName(IgmpCtcProfile ctcProfile);

    /**
     * 删除模板别名
     * 
     * @param entityId
     * @param profileId
     */
    void deleteProfileName(Long entityId, Integer profileId);

    /**
     * 批量删除模板别名
     * 
     * @param entityId
     * @param profileIds
     */
    void batchDeleteProfileName(Long entityId, List<Integer> profileIds);

}
