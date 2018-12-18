/***********************************************************************
 * $Id: OltIgmpDao.java,v1.0 2013-10-25 下午4:21:49 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.dao;

import java.util.List;

import com.topvision.ems.epon.domain.IgmpForwardingSnooping;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcCdrTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcPreviewIntervalTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastUserAuthorityTable;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.domain.IgmpForwardingTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcSniConfigMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcUniConfigTable;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.domain.TopIgmpForwardingSnooping;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingOnuTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingPortTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingSlotTable;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author jay
 * @created @2011-11-23-18:10:03
 * 
 * @Mybatis Modify by Rod @2013-10-25
 */
public interface OltIgmpDao extends BaseEntityDao<Object> {

    /***************************************************** 全局igmp设置 *************************************************/
    /**
     * 获得IGMP全局属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    public IgmpEntityTable getIgmpGlobalInfo(Long entityId);

    /**
     * 获取SNI口的IGMP全局属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    public IgmpMcSniConfigMgmtObjects getIgmpSniConfig(Long entityId);

    /**
     * 获取最大组播组数目
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    public IgmpMcParamMgmtObjects getIgmpMaxGroupNum(Long entityId);

    /**
     * 修改IGMP全局属性
     * 
     * @param igmpEntityTable
     *            IGMP全局配置
     */
    public void modifyIgmpGlobalInfo(IgmpEntityTable igmpEntityTable);

    /**
     * 修改IGMP SNI口全局属性
     * 
     * @param igmpMcSniConfigMgmtObjects
     */
    public void modifyIgmpSniConfig(IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObjects);

    /**
     * 修改最大组播组数
     * 
     * @param igmpMcParamMgmtObjects
     */
    public void modifyIgmpMaxGroupNum(IgmpMcParamMgmtObjects igmpMcParamMgmtObjects);

    /***************************************************** 组播组 *************************************************/
    /**
     * 获取组播组列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpControlledMulticastPackageTable>
     */
    public List<IgmpControlledMulticastPackageTable> getIgmpMvlanInfo(Long entityId);

    /**
     * 修改组播组信息
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组信息
     */
    public void modifyIgmpMvlanInfo(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 添加一个组播组
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组
     */
    public void addIgmpMvlan(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 删除一个组播组
     * 
     * @param igmpControlledMulticastPackageTable
     * 
     */
    public void deleteIgmpMvlan(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 获取组播组当前活跃pon口列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpForwardingTable>
     */
    public List<IgmpForwardingTable> getIgmpForwardingInfo(Long entityId);

    /***************************************************** 组播组 *************************************************/
    /**
     * 获取组播组频道列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpProxyParaTable>
     */
    public List<IgmpProxyParaTable> getIgmpProxyInfo(Long entityId);

    /**
     * 修改频道
     * 
     * @param igmpProxyParaTable
     *            频道
     */
    public void modifyIgmpProxyInfo(IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 添加一个频道
     * 
     * @param igmpProxyParaTable
     *            频道
     */
    public void addIgmpProxy(IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 删除一个频道
     * 
     * @param igmpProxyParaTable
     */
    public void deleteIgmpProxy(IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 修改频道的组播组vlan ID
     * 
     * @param igmpControlledMulticastPackageTable
     *            设备ID
     */
    public void modifyMulticastPackage(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 获取组播组频道LIST
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            组播组INDEX
     * @return IgmpControlledMulticastPackageTable
     */
    public IgmpControlledMulticastPackageTable getIgmpControlledMulticastPackageTable(Long entityId, Integer cmIndex);

    /***************************************************** pon口 *************************************************/
    /**
     * 获取 PON口IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param ponIndex
     *            PON口INDEX
     * @return List<Long>
     */
    public IgmpControlledMulticastUserAuthorityTable getIgmpControlledMulticastUserAuthorityTable(Long entityId,
            Long ponIndex);

    /**
     * 新增UNI口 IGMP信息
     * 
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    public void addMulticastUserAuthorityList(
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable);

    /**
     * 新增UNI口可控组播配置信息
     * 
     * @param igmpMcUniConfig
     * 
     */
    public void addMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfig);

    /**
     * 修改UNI口 IGMP信息
     * 
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    public void modifyMulticastUserAuthorityList(
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable);

    /**
     * 删除UNI口可控组播配置信息
     * 
     * @param igmpMcUniConfig
     * 
     */
    public void deleteMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfig);

    /**
     * 删除UNI口 IGMP信息
     * 
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    public void deleteMulticastUserAuthorityList(
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable);

    /***************************************************** 呼叫记录 *************************************************/
    /**
     * 获取呼叫信息记录
     * 
     * @param entityId
     *            设备ID
     * @return <IgmpControlledMcCdrTable>
     */
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId);

    /***************************************************** ONU *************************************************/
    /**
     * 获取ONU IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU INDEX
     * @return IgmpMcOnuTable
     */
    public IgmpMcOnuTable getIgmpMcOnuInfo(Long entityId, Long onuIndex);

    /**
     * 获取所有ONU IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuTable>
     */
    public List<IgmpMcOnuTable> getIgmpMcOnuInfo(Long entityId);

    /**
     * 修改ONU IGMP信息
     * 
     * @param igmpMcOnuTable
     *            ONU组播组信息
     */
    public void modifyIgmpMcOnuInfo(IgmpMcOnuTable igmpMcOnuTable);

    /***************************************************** UNI *************************************************/
    /**
     * 获取UNI口的IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param uniIndex
     *            UNI INDEX
     * @return IgmpMcUniConfigTable
     */
    public IgmpMcUniConfigTable getIgmpMcUniConfig(Long entityId, Long uniIndex);

    /**
     * 修改UNI口的IGMP信息
     * 
     * @param igmpMcUniConfigTable
     *            UNI口IGMP信息
     */
    public void modifyIgmpMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfigTable);

    /***************************************************** UNI *************************************************/
    /**
     * 获取组播VLAN转换列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuVlanTransTable>
     */
    public List<IgmpMcOnuVlanTransTable> getIgmpVlanTrans(Long entityId);

    /**
     * 添加组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    public void addIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /**
     * 修改组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    public void modifyIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /**
     * 删除组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     */
    public void deleteIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /********************************************* 刷新组播组当前活跃PON口的信息 ***************************************/
    /**
     * 保存呼叫信息记录表
     * 
     * @param igmpControlledMcCdrTables
     */
    void saveIgmpControlledMcCdrTable(Long entityId, List<IgmpControlledMcCdrTable> igmpControlledMcCdrTables);

    /**
     * 保存可控组播业务包表
     * 
     * @param igmpControlledMulticastPackageTables
     * 
     */
    void saveIgmpControlledMulticastPackageTable(Long entityId,
            List<IgmpControlledMulticastPackageTable> igmpControlledMulticastPackageTables);

    /**
     * 保存可控组播用户权限表
     * 
     * @param igmpControlledMulticastUserAuthorityTables
     * 
     */
    void saveIgmpControlledMulticastUserAuthorityTable(Long entityId,
            List<IgmpControlledMulticastUserAuthorityTable> igmpControlledMulticastUserAuthorityTables);

    /**
     * 保存IGMP实体表
     * 
     * @param igmpEntityTables
     */
    void saveIgmpEntityTable(Long entityId, List<IgmpEntityTable> igmpEntityTables);

    /**
     * 保存活跃PON口表
     * 
     * @param igmpForwardingTables
     */
    void saveIgmpForwardingTable(Long entityId, List<IgmpForwardingTable> igmpForwardingTables);

    /**
     * 保存 Onu IGMP参数表
     * 
     * @param igmpMcOnuTables
     */
    void saveIgmpMcOnuTable(Long entityId, List<IgmpMcOnuTable> igmpMcOnuTables);

    /**
     * 保存组播组转换规则表
     * 
     * @param igmpMcOnuVlanTransTables
     */
    void saveIgmpMcOnuVlanTransTable(Long entityId, List<IgmpMcOnuVlanTransTable> igmpMcOnuVlanTransTables);

    /**
     * 保存最大组播组数
     * 
     * @param igmpMcParamMgmtObjectses
     */
    void saveIgmpMcParamMgmtObjects(Long entityId, IgmpMcParamMgmtObjects igmpMcParamMgmtObject);

    /**
     * 保存Sni口组播组属性
     * 
     * @param igmpMcSniConfigMgmtObjectses
     */
    void saveIgmpMcSniConfigMgmtObjects(Long entityId, IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObject);

    /**
     * 保存Uni口组播组属性
     * 
     * @param igmpMcUniConfigTables
     */
    void saveIgmpMcUniConfigTable(Long entityId, List<IgmpMcUniConfigTable> igmpMcUniConfigTables);

    /**
     * 保存频道表
     * 
     * @param igmpProxyParaTables
     */
    void saveIgmpProxyParaTable(Long entityId, List<IgmpProxyParaTable> igmpProxyParaTables);

    /**
     * 获取板卡igmpForwarding信息
     * 
     * @param entityId
     * @param proxyId
     * @return
     */
    public TopMcForwardingSlotTable getTopMcForwardingSlot(Long entityId, Integer proxyId);

    /**
     * 获取端口igmpForwarding信息
     * 
     * @param entityId
     * @param proxyId
     * @param slotNo
     * @return
     */
    public TopMcForwardingPortTable getTopMcForwardingPort(Long entityId, Integer proxyId, Integer slotNo);

    /**
     * 获取ONUigmpForwarding信息
     * 
     * @param entityId
     * @param proxyId
     * @param slotNo
     * @param ponPortNo
     * @return
     */
    public TopMcForwardingOnuTable getTopMcForwardingOnu(Long entityId, Integer proxyId, Integer slotNo,
            Integer ponPortNo);

    /**
     * 批量更新组播组板卡信息
     * 
     * @param topMcForwardingSlotTables
     * @param entityId
     */
    public void batchInsertTopMcForwardingSlotTables(List<TopMcForwardingSlotTable> topMcForwardingSlotTables,
            Long entityId);

    /**
     * 批量更新组播组端口信息
     * 
     * @param topMcForwardingPortTables
     * @param entityId
     */
    public void batchInsertTopMcForwardingPortTables(List<TopMcForwardingPortTable> topMcForwardingPortTables,
            Long entityId);

    /**
     * 批量更新组播组ONU信息
     * 
     * @param topMcForwardingOnuTables
     * @param entityId
     */
    public void batchInsertTopMcForwardingOnuTables(List<TopMcForwardingOnuTable> topMcForwardingOnuTables,
            Long entityId);

    /**
     * 获取IGMP FORWARDING SLOT
     * 
     * @param entityId
     * @return
     */
    public List<TopMcForwardingSlotTable> getForwardingSlot(Long entityId);

    /**
     * 获取IGMP FORWARDING PORT
     * 
     * @param entityId
     * @return
     */
    public List<TopMcForwardingPortTable> getForwardingPort(Long entityId);

    /**
     * 保存IgmpControlledMcPreviewIntervalTable
     * 
     * @param entityId
     * @param igmpControlledMcPreviewIntervalTables
     */
    public void saveIgmpControlledMcPreviewIntervalTable(Long entityId,
            List<IgmpControlledMcPreviewIntervalTable> igmpControlledMcPreviewIntervalTables);

    /**
     * @param entityId
     * @param start
     * @param limit
     * @return
     */
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId, int start, int limit);

    /**
     * @param entityId
     * @param portIndex
     * @param vid
     * @return
     */
    public List<IgmpForwardingSnooping> loadIgmpSnoopingData(Long entityId, Long portIndex, Integer vid);

    /**
     * @param list
     * @param entityId
     */
    public void batchInsertTopIgmpForwardingSnooping(List<TopIgmpForwardingSnooping> list, Long entityId);
}
