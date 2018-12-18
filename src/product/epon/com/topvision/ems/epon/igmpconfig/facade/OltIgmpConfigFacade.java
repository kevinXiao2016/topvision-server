/***********************************************************************
 * $Id: OltIgmpConfigFacade.java,v1.0 2016-6-6 下午4:37:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.facade;

import java.util.List;

import com.topvision.ems.epon.igmpconfig.domain.IgmpCascadePort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfileGroupRela;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcRecord;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpStaticFwd;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpUplinkPort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanInfo;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2016-6-6-下午4:37:13
 *
 */
@EngineFacade(serviceName = "oltIgmpConfigFacade", beanName = "oltIgmpConfigFacade")
public interface OltIgmpConfigFacade extends Facade {

    /**
     * 获取OLT IGMP全局参数配置
     * @param snmpParam
     * @return
     */
    public IgmpGlobalParam getGloablParam(SnmpParam snmpParam);

    /**
     * 修改OLT　IGMP模式
     * @param snmpParam
     * @param igmpMode
     */
    public void modifyIgmpMode(SnmpParam snmpParam, Integer igmpMode);

    /**
     * 修改OLT IGMP全局参数配置
     * @param snmpParam
     * @param globalParam
     */
    public void modifyGlobalParam(SnmpParam snmpParam, IgmpGlobalParam globalParam);

    /**
     * 添加IGMP级联端口
     * @param snmpParam
     * @param cascadePort
     */
    public void createCascadePort(SnmpParam snmpParam, IgmpCascadePort cascadePort);

    /**
     * 获取级联端口列表
     * @param snmpParam
     * @return
     */
    public List<IgmpCascadePort> getCascadePortList(SnmpParam snmpParam);

    /**
     * 删除IGMP级联端口
     * @param snmpParam
     * @param cascadePort
     */
    public void destoryCascadePort(SnmpParam snmpParam, IgmpCascadePort cascadePort);

    /**
     * 查询Snooping模式上行端口配置
     * @param snmpParam
     * @return
     */
    public IgmpSnpUplinkPort getSnpUplinkPort(SnmpParam snmpParam);

    /**
     * 修改Snooping模式上行端口配置
     * @param snmpParam
     * @param uplinkPort
     */
    public void modifySnpUplinkPort(SnmpParam snmpParam, IgmpSnpUplinkPort uplinkPort);

    /**
     * 添加Snooping模式静态转发组配置
     * @param snmpParam
     * @param staticFwd
     */
    public void createSnpStaticFwd(SnmpParam snmpParam, IgmpSnpStaticFwd staticFwd);

    /**
     * 获取Snooping模式静态转发组配置
     * @param snmpParam
     * @return
     */
    public List<IgmpSnpStaticFwd> getSnpStaticFwdList(SnmpParam snmpParam);

    /**
     * 删除Snooping模式静态转发组配置
     * @param snmpParam
     * @param staticFwd
     */
    public void destorySnpStaticFwd(SnmpParam snmpParam, IgmpSnpStaticFwd staticFwd);

    /**
     * 添加IGMP VLAN信息
     * @param snmpParam
     * @param vlanInfo
     */
    public void createVlanInfo(SnmpParam snmpParam, IgmpVlanInfo vlanInfo);

    /**
     * 查询IGMP VLAN信息
     * @param snmpParam
     * @return
     */
    public List<IgmpVlanInfo> getVlanInfoList(SnmpParam snmpParam);

    /**
     * 修改IGMP VLAN信息
     * @param snmpParam
     * @param vlanInfo
     */
    public void modifyVlanInfo(SnmpParam snmpParam, IgmpVlanInfo vlanInfo);

    /**
     * 删除IGMP VLAN信息
     * @param snmpParam
     * @param vlanId
     */
    public void destoryVlanInfo(SnmpParam snmpParam, Integer vlanId);

    /**
     * 添加IMGP组播组信息
     * @param snmpParam
     * @param groupInfo
     */
    public void createVlanGroup(SnmpParam snmpParam, IgmpVlanGroup groupInfo);

    /**
     * 获取IMGP组播组信息
     * @param snmpParam
     * @return
     */
    public List<IgmpVlanGroup> getVlanGroupList(SnmpParam snmpParam);

    /**
     * 修改组播组信息
     * @param snmpParam
     * @param groupInfo
     */
    public void modifyVlanGroup(SnmpParam snmpParam, IgmpVlanGroup groupInfo);

    /**
     * 删除IMGP组播组信息
     * @param snmpParam
     * @param groupInfo
     */
    public void destoryVlanGroup(SnmpParam snmpParam, IgmpVlanGroup groupInfo);

    /**
     * 获取全局IGMP组播信息
     * @param snmpParam
     * @return
     */
    public List<IgmpGlobalGroup> getGlobalGroupList(SnmpParam snmpParam);

    /**
     * 获取IGMP可控组播配置
     * @param snmpParam
     * @return
     */
    public IgmpCtcParam getCtcParam(SnmpParam snmpParam);

    /**
     * 修改IGMP可控组播配置
     * @param snmpParam
     * @param ctcParam
     */
    public void modifyCtcParam(SnmpParam snmpParam, IgmpCtcParam ctcParam);

    /**
     * 手动上报CDR日志
     * @param snmpParam
     */
    public void reportCtcCdr(SnmpParam snmpParam);

    /**
     * 添加可控模板
     * @param snmpParam
     * @param ctcProfile
     */
    public void createCtcProfile(SnmpParam snmpParam, IgmpCtcProfile ctcProfile);

    /**
     * 查询可控模板列表
     * @param snmpParam
     * @return
     */
    public List<IgmpCtcProfile> getCtcProfileList(SnmpParam snmpParam);

    /**
     * 更新可控模板
     * @param snmpParam
     * @param ctcProfile
     */
    public void modifyCtcProfile(SnmpParam snmpParam, IgmpCtcProfile ctcProfile);

    /**
     * 删除可控模板
     * @param snmpParam
     * @param profileId
     */
    public void destoryCtcProfile(SnmpParam snmpParam, Integer profileId);

    /**
     * 添加可控模板关联组播组
     * @param snmpParam
     * @param profileGroup
     */
    public void createProfileGroupRela(SnmpParam snmpParam, IgmpCtcProfileGroupRela profileGroup);

    /**
     * 查询可控模板关联组播组
     * @param snmpParam
     * @return
     */
    public List<IgmpCtcProfileGroupRela> getProfileGroupRelaList(SnmpParam snmpParam);

    /**
     * 删除可控模板关联组播组
     * @param snmpParam
     * @param profileGroup
     */
    public void destoryProfileGroupRela(SnmpParam snmpParam, IgmpCtcProfileGroupRela profileGroup);

    /**
     * 获取呼叫记录列表
     * @param snmpParam
     * @return
     */
    public List<IgmpCtcRecord> getCtcRecordList(SnmpParam snmpParam);

    /**
     * 查询UNI口绑定CTC模板列表
     * @param onuId
     * @param uniIndex
     * @return
     */
    public List<IgmpUniBindCtcProfile> getBindCtcProfileList(SnmpParam snmpParam);

    /**
     * 获取所有ONU IGMP配置信息
     * @param snmpParam
     * @return
     */
    public List<IgmpOnuConfig> getIgmpOnuConfigList(SnmpParam snmpParam);

    /**
     * 获取所有的UNI IGMP配置
     * @param snmpParam
     * @return
     */
    public List<IgmpUniConfig> getAllUniConfigList(SnmpParam snmpParam);

    /**
     * 获取所有UNI VLAN转换关系配置
     * @param snmpParam
     * @return
     */
    public List<IgmpUniVlanTrans> getAllUniVlanTrans(SnmpParam snmpParam);
}
