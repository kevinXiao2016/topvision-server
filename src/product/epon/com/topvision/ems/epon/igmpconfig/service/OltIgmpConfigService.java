/***********************************************************************
 * $Id: OltIgmpConfigService.java,v1.0 2016-6-6 下午4:13:26 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.service;

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
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2016-6-6-下午4:13:26
 *
 */
public interface OltIgmpConfigService extends Service {
    /**
     * 添加IGMP全局参数
     * 
     * @param globalParam
     */
    void addGlobalParam(IgmpGlobalParam globalParam);

    /**
     * 获取OLT IGMP全局参数配置
     * 
     * @param entityId
     * @return
     */
    IgmpGlobalParam getGloablParam(Long entityId);

    /**
     * 获取IGMP模式
     * 
     * @param entityId
     * @return
     */
    Integer getIgmpMode(Long entityId);

    /**
     * 修改OLT　IGMP模式
     * 
     * @param entityId
     * @param igmpMode
     */
    void modifyIgmpMode(Long entityId, Integer igmpMode);

    /**
     * 修改OLT IGMP全局参数配置
     * 
     * @param globalParam
     */
    void modifyGlobalParam(IgmpGlobalParam globalParam);

    /**
     * 刷新OLT IGMP全局参数配置
     * 
     * @param entityId
     */
    void refreshGlobalParam(Long entityId);

    /**
     * 添加IGMP级联端口
     * 
     * @param cascadePort
     */
    void addCascadePort(IgmpCascadePort cascadePort);

    /**
     * 获取级联端口列表
     * 
     * @param entityId
     * @return
     */
    List<IgmpCascadePort> getCascadePortList(Long entityId);

    /**
     * 删除IGMP级联端口
     * 
     * @param cascadePort
     */
    void deleteCascadePort(IgmpCascadePort cascadePort);

    /**
     * 批量删除级联端口,返回删除失败的端口名字
     * 
     * @param entityId
     * @param cascadePortIndexs
     */
    String batchDeleteCascadePort(Long entityId, List<IgmpCascadePort> cascadePorts);

    /**
     * 刷新IGMP级联端口
     * 
     * @param entityId
     */
    void refreshCascadePort(Long entityId);

    /**
     * 添加Snooping模式上行端口配置
     * 
     * @param uplinkPort
     */
    void addSnpUpLinkPort(IgmpSnpUplinkPort uplinkPort);

    /**
     * 查询Snooping模式上行端口配置
     * 
     * @param entityId
     * @return
     */
    IgmpSnpUplinkPort getSnpUplinkPort(Long entityId);

    /**
     * 修改Snooping模式上行端口配置
     * 
     * @param uplinkPort
     */
    void modifySnpUplinkPort(IgmpSnpUplinkPort uplinkPort);

    /**
     * 刷新Snooping模式上行端口配置
     * 
     * @param entityId
     */
    void refreshSnpUplinkPort(Long entityId);

    /**
     * 添加Snooping模式静态转发组配置
     * 
     * @param staticFwd
     */
    void addSnpStaticFwd(IgmpSnpStaticFwd staticFwd);

    /**
     * 获取Snooping模式静态转发组配置
     * 
     * @param paramMap
     * @return
     */
    List<IgmpSnpStaticFwd> getSnpStaticFwdList(Map<String, Object> paramMap);

    /**
     * 获取静态加入的数量
     * 
     * @param paramMap
     * @return
     */
    Integer getSnpStaticFwdNum(Map<String, Object> paramMap);

    /**
     * 获取所有静态加入配置
     * 
     * @param entityId
     * @return
     */
    List<IgmpSnpStaticFwd> getAllSnpStaticFwdList(Long entityId);

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
     * @return
     */
    String batchDeleteSnpStaticFwd(Long entityId, List<IgmpSnpStaticFwd> staticFwds);

    /**
     * 刷新Snooping模式静态转发组配置
     * 
     * @param entityId
     */
    void refreshSnpStaticFwd(Long entityId);

    /**
     * 添加IGMP VLAN信息
     * 
     * @param vlanInfo
     */
    void addVlanInfo(IgmpVlanInfo vlanInfo);

    /**
     * 查询IGMP VLAN信息
     * 
     * @param entityId
     * @return
     */
    List<IgmpVlanInfo> getVlanInfoList(Long entityId);

    /**
     * 修改IGMP VLAN信息
     * 
     * @param vlanInfo
     */
    void modifyVlanInfo(IgmpVlanInfo vlanInfo);

    /**
     * 删除IGMP VLAN信息
     * 
     * @param entityId
     * @param vlanId
     */
    void deleteVlanInfo(Long entityId, Integer vlanId);

    /**
     * 批量删除IGMP VLAN 信息，返回删除失败的VLAN id组成的字符串
     * 
     * @param entityId
     * @param vlanIds
     * @return
     */
    String batchDeleteVlanInfo(Long entityId, List<Integer> vlanIds);

    /**
     * 刷新IGMP VLAN业务 在刷新VLAN信息时由于外键关联会同时删除组播组信息 因此在刷新VLAN数据成功后需要同时刷新组播组信息
     * 
     * @param entityId
     */
    void refreshVlanData(Long entityId);

    /**
     * 刷新Vlan信息
     * 
     * @param entityId
     */
    void refreshVlanInfo(Long entityId);

    /**
     * 添加IMGP组播组信息
     * 
     * @param groupInfo
     */
    void addVlanGroup(IgmpVlanGroup groupInfo);

    /**
     * 获取IMGP组播组信息
     * 
     * @param entityId
     * @param vlanId
     * @return
     */
    List<IgmpVlanGroup> getVlanGroupList(Long entityId, Integer vlanId);

    /**
     * 获取已经配置的组播组
     * 
     * @param entityId
     * @return
     */
    List<IgmpVlanGroup> getAllVlanGroup(Long entityId);

    /**
     * 获取组播组数据
     * 
     * @param paramMap
     * @return
     */
    List<IgmpVlanGroup> getVlanGroupList(Map<String, Object> paramMap);

    /**
     * 获取组播组数量
     * 
     * @param paramMap
     * @return
     */
    Integer getVlanGroupNum(Map<String, Object> paramMap);

    /**
     * 获取所有已经配置的组播组ID
     * 
     * @param entityId
     * @return
     */
    List<Integer> getGroupIdList(Long entityId);

    /**
     * 修改组播组信息
     * 
     * @param groupInfo
     */
    void modifyVlanGroup(IgmpVlanGroup groupInfo);

    /**
     * 修改组播组的预加入状态
     */
    void modifyGroupPreJoin(IgmpVlanGroup groupInfo);

    /**
     * 删除IMGP组播组信息
     * 
     * @param groupInfo
     */
    void deleteVlanGroup(IgmpVlanGroup groupInfo);

    /**
     * 批量删除IGMP组播组信息
     * 
     * @param entityId
     * @param igmpVlanGroups
     * @return
     */
    String batchDeleteVlanGroup(Long entityId, List<IgmpVlanGroup> igmpVlanGroups);

    /**
     * 刷新组播组信息
     * 
     * @param entityId
     */
    void refreshVlanGroup(Long entityId);

    /**
     * 获取全局IMGP组播组信息
     * 
     * @param paramMap
     * @return
     */
    List<IgmpGlobalGroup> getGlobalGroupList(Map<String, Object> paramMap);

    /**
     * 获取全局组播组数量
     * 
     * @param paramMap
     * @return
     */
    Integer getGlobalGroupNum(Map<String, Object> paramMap);

    /**
     * 刷新全局IGMP组播组
     * 
     * @param entityId
     */
    void refreshGlobalGroup(Long entityId);

    /**
     * 添加IGMP可控组播配置
     * 
     * @param ctcParam
     */
    void addCtcParam(IgmpCtcParam ctcParam);

    /**
     * 获取IGMP可控组播配置
     * 
     * @param entityId
     * @return
     */
    IgmpCtcParam getCtcParam(Long entityId);

    /**
     * 修改IGMP可控组播配置
     * 
     * @param ctcParam
     */
    void modifyCtcParam(IgmpCtcParam ctcParam);

    /**
     * 手动上报CDR日志
     * 
     * @param entityId
     */
    void reportCtcCdr(Long entityId);

    /**
     * 刷新可控组播配置
     * 
     * @param entityId
     */
    void refreshCtcParam(Long entityId);

    /**
     * 添加可控模板
     * 
     * @param ctcProfile
     */
    void addCtcProfile(IgmpCtcProfile ctcProfile);

    /**
     * 查询可控模板
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    IgmpCtcProfile getCtcProfile(Long entityId, Integer profileId);

    /**
     * 查询可控模板列表
     * 
     * @param paramMap
     * @return
     */
    List<IgmpCtcProfile> getCtcProfileList(Map<String, Object> paramMap);

    /**
     * 获取CTC模板数量
     * 
     * @param paramMap
     * @return
     */
    Integer getCtcProfileNum(Map<String, Object> paramMap);

    /**
     * 获取所有的可控组播模板
     * 
     * @param entityId
     * @return
     */
    List<IgmpCtcProfile> getAllCtcProfile(Long entityId);

    /**
     * 获取所有已经配置的模板ID
     * 
     * @param entityId
     * @return
     */
    List<Integer> getProfileIdList(Long entityId);

    /**
     * 更新可控模板
     * 
     * @param ctcProfile
     */
    void modifyCtcProfile(IgmpCtcProfile ctcProfile);

    /**
     * 删除可控模板
     * 
     * @param entityId
     * @param profileId
     */
    void deleteCtcProfile(Long entityId, Integer profileId);

    /**
     * 批量删除可控模板，返回删除失败的profileId
     * 
     * @param entityId
     * @param profileIds
     */
    String batchDeleteCtcProfile(Long entityId, List<Integer> profileIds);

    /**
     * 刷新可控模板业务 在刷新模板信息时由于外键关联会将关联的组播组配置删除 因此在刷新模板成功后需要刷新模板关联组播组信息
     * 
     * @param entityId
     */
    void refreshProfileData(Long entityId);

    /**
     * 刷新可控组播模板
     * 
     * @param entityId
     */
    void refreshCtcProfile(Long entityId);

    /**
     * 添加可控模板关联组播组
     * 
     * @param profileGroup
     */
    void addProfileGroupRela(IgmpCtcProfileGroupRela profileGroup);

    /**
     * 查询可控模板关联组播组
     * 
     * @param profileId
     * @return
     */
    List<IgmpControlGroupBindRelation> getProfileGroupRelaList(Long entityId, Integer profileId);

    /**
     * 获取所有的可控模板关联组播组信息
     * 
     * @param entityId
     * @return
     */
    List<IgmpCtcProfileGroupRela> getAllProfileGroupRela(Long entityId);

    /**
     * 删除可控模板关联组播组
     * 
     * @param profileGroup
     */
    void deleteProfileGroupRela(IgmpCtcProfileGroupRela profileGroup);

    /**
     * 刷新可控模板关联组播组
     * 
     * @param entityId
     */
    void refreshProfileGroupRela(Long entityId);

    /**
     * 添加呼叫记录
     * 
     * @param ctcRecord
     */
    void addCtcRecord(IgmpCtcRecord ctcRecord);

    /**
     * 获取呼叫记录列表
     * 
     * @param paramsMap
     * @return
     */
    List<IgmpCtcRecord> getCtcRecordList(Map<String, Object> paramsMap);

    /**
     * 获取呼叫记录数量
     * 
     * @param paramsMap
     * @return
     */
    Integer getCtcRecordNum(Map<String, Object> paramsMap);

    /**
     * 刷新呼叫记录
     * 
     * @param entityId
     */
    void refreshCtcRecord(Long entityId);

    /**
     * 刷新UNI口绑定组播模板信息
     * 
     * @param entityId
     */
    void refreshUniBindCtcProfile(Long entityId);

    /**
     * 刷新ONU IGMP配置
     * 
     * @param entityId
     */
    void refreshOnuConfigList(Long entityId);

    /**
     * 刷新所有的UNI IGMP配置
     * 
     * @param entityId
     */
    void refreshAllUniConfigList(Long entityId);

    /**
     * 刷新所有的UNI VLAN转换配置
     * 
     * @param entityId
     */
    void refreshAllUniVlanTransList(Long entityId);

    /**
     * 根据SNI端口类型获取端口列表,sniPortType
     * 
     * @param entityId
     * @param typeList
     * @return
     */
    List<IgmpPortInfo> getSniPortByType(Long entityId, List<Integer> typeList);

    /**
     * 获取SNI端口类型
     * 
     * @param entityId
     * @return
     */
    List<IgmpPortInfo> getSniPort(Long entityId);

    /**
     * 获取上联端口聚合组列表
     * 
     * @param entityId
     * @return
     */
    List<IgmpPortInfo> getSniAggList(Long entityId);

    /**
     * 根据PON端口类型获取端口列表
     * 
     * @param entityId
     * @param portType
     * @return
     */
    List<IgmpPortInfo> getPonPortByType(Long entityId, Integer portType);

    /**
     * 获取指定端口类型的级联端口
     * 
     * @param entityId
     * @param portType
     * @return
     */
    List<IgmpCascadePort> getCascadePortByType(Long entityId, Integer portType);

    /**
     * 删除所有VLAN上联端口配置<br/>
     * 即将VLAN上联端口全部配置为INVALID<br/>
     * (此操作没有业务限制,可以直接修改成功)
     * 
     * @param entityId
     */
    void deleteVlanUplink(Long entityId);

    /**
     * 删除Snooping上联端口配置<br/>
     * 即将Snooping上联端口配置为INVALID<br/>
     * (此操作没有业务限制,可以直接修改成功)
     * 
     * @param entityId
     */
    void deleteSnpUplink(Long entityId);

    /**
     * 删除所有VLAN业务配置<br/>
     * 如果VLAN上的组播组被模板引用,并且模板被模板被UNI端口绑定,则VLAN无法直接删除,则VLAN无法直接删除<br/>
     * 这时需要先删除UNI端口绑定模板,再删除模板关联组播组,然后再删除VLAN<br/>
     * 删除模板关联组播组和UNI端口绑定模板关系由deleteAllProfileGroup方法实现
     * 
     * @param entityId
     */
    void deleteAllVlanConfig(Long entityId);

    /**
     * 删除所有可控模板中的组播组<br/>
     * 如果可控模板被UNI端口绑定,则模板的修改<br/>
     * 这时需要先删除UNI端口绑定模板,再删除模板关联组播组
     * 
     * @param entityId
     */
    void deleteAllProfileGroup(Long entityId);

    /**
     * 将CTC配置为不使能
     * 
     * @param entityId
     */
    void modifyCtcToDisable(Long entityId);

    /**
     * 删除所有的静态加入配置<br/>
     * (此操作没有业务限制,可以直接修改成功)
     * 
     * @param entityId
     */
    void deleteAllSnpStaticFwd(Long entityId);

    /**
     * 刷新OLT IGMP业务
     * 
     * @param entityId
     */
    void refreshOltIgmpData(Long entityId);

    /**
     * 刷新ONU IGMP业务
     * 
     * @param entityId
     */
    void refreshOnuIgmpData(Long entityId);

    /**
     * 删除特定的组播组,通过withSrcIp指定<br/>
     * 如果withSrcIp为true,则删除所有带源Ip的组播组<br/>
     * 如果withSrcIp为false,则删除所有不带源Ip的组播组
     * 
     * @param entityId
     * @param withSrcIp
     */
    void deleteSpecialGroup(Long entityId, boolean withSrcIp);

    /**
     * 更新IGMP组播组别名
     * 
     * @param group
     * @return
     */
    void updateIgmpGroupAlias(IgmpVlanGroup group);

    /**
     * 批量添加组播组
     * 
     * @param groupInfo
     */
    void batchAddVlanGroup(IgmpVlanGroup groupInfo);

}
