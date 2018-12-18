/***********************************************************************
 * $Id: OltIgmpService.java,v1.0 2013-10-25 下午4:30:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.service;

import java.util.List;

import com.topvision.ems.epon.domain.IgmpForwardingSnooping;
import com.topvision.ems.epon.exception.AddIgmpMvlanException;
import com.topvision.ems.epon.exception.AddIgmpProxyException;
import com.topvision.ems.epon.exception.AddIgmpVlanTransException;
import com.topvision.ems.epon.exception.DeleteIgmpMvlanException;
import com.topvision.ems.epon.exception.DeleteIgmpProxyException;
import com.topvision.ems.epon.exception.DeleteIgmpVlanTransException;
import com.topvision.ems.epon.exception.ModifyIgmpGlobalInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpMaxGroupNumException;
import com.topvision.ems.epon.exception.ModifyIgmpMcOnuInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpMcUniConfigException;
import com.topvision.ems.epon.exception.ModifyIgmpMvlanInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpProxyInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpSniConfigException;
import com.topvision.ems.epon.exception.ModifyIgmpVlanTransException;
import com.topvision.ems.epon.exception.ModifyMulticastUserAuthorityListException;
import com.topvision.ems.epon.exception.ModifyProxyMulticastVIDException;
import com.topvision.ems.epon.exception.RefreshIgmpForwardingTableException;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcCdrTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.domain.IgmpForwardingTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcSniConfigMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcUniConfigTable;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingOnuTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingPortTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingSlotTable;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午4:30:11
 *
 */
public interface OltIgmpService extends Service {
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
     * @throws ModifyIgmpGlobalInfoException
     */
    public void modifyIgmpGlobalInfo(IgmpEntityTable igmpEntityTable);

    /**
     * 修改IGMP SNI口全局属性
     * 
     * @param entityId
     *            设备ID
     * @param topMcSniPortType
     *            所有SNI口、指定SNI口、trunk组
     * @param topMcSniPort
     *            指定端口
     * @param topMcSniAggPort
     *            trunk组 ID
     * @throws ModifyIgmpSniConfigException
     */
    public void modifyIgmpSniConfig(Long entityId, Integer topMcSniPortType, String topMcSniPort,
            Integer topMcSniAggPort);

    /**
     * 修改最大组播组数
     * 
     * @param entityId
     *            设备ID
     * @param topMcMaxGroupNum
     *            最大组播组数
     * @throws ModifyIgmpMaxGroupNumException
     */
    public void modifyIgmpMaxGroupNum(Long entityId, Integer topMcMaxGroupNum, Long topMcMaxBw,
            Integer topMcSnoopingAgingTime, List<Integer> topMcMVlanList);

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
     * @throws ModifyIgmpMvlanInfoException
     */
    public void modifyIgmpMvlanInfo(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 添加一个组播组
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组
     * @throws AddIgmpMvlanException
     */
    public void addIgmpMvlan(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 删除一个组播组
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            组播组INDEX
     * @throws DeleteIgmpMvlanException
     */
    public void deleteIgmpMvlan(Long entityId, Integer cmIndex);

    /**
     * 获取组播组当前活跃pon口列表 当前活跃PON口不支持
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
     * @throws ModifyIgmpProxyInfoException
     */
    public void modifyIgmpProxyInfo(IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 添加一个频道
     * 
     * @param igmpProxyParaTable
     *            频道
     * @throws AddIgmpProxyException
     */
    public void addIgmpProxy(IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 删除一个频道
     * 
     * @param entityId
     *            设备ID
     * @param proxyIndex
     *            频道INDEX
     * @throws DeleteIgmpProxyException
     */
    public void deleteIgmpProxy(Long entityId, Integer proxyIndex);

    /**
     * 修改频道的组播组vlan ID
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            频道对应的组播组ID
     * @param cmProxyListList
     *            可控组播的频道列表
     * @throws ModifyProxyMulticastVIDException
     * 
     */
    public void modifyMulticastPackage(Long entityId, Integer cmIndex, List<Integer> cmProxyListList);

    /**
     * 获取组播组频道LIST
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            组播组INDEX
     * @return List<Long>
     */
    public List<Integer> getIgmpMvlanProxyList(Long entityId, Integer cmIndex);

    /***************************************************** pon口 *************************************************/
    /**
     * 获取 UNI口IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param uniIndex
     *            UNI口INDEX
     * @return List<Integer>
     */
    public List<Integer> getIgmpUniPortInfo(Long entityId, Long uniIndex);

    /**
     * 修改UNI口 IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param multicastPackageList
     *            组播组ID列表
     * @throws ModifyMulticastUserAuthorityListException
     * 
     */
    public void modifyMulticastUserAuthorityList(Long entityId, Long portIndex, List<Integer> multicastPackageList);

    /**
     * 刷新UNI口 IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * 
     */
    public void refreshIgmpUniInOnu(Long entityId, Long uniIndex);

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
     * 获取所有ONU IGMP模式信息
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuTable>
     */
    public List<IgmpMcOnuTable> getIgmpMcOnuInfoList(Long entityId);

    /**
     * 修改ONU IGMP信息
     * 
     * @param igmpMcOnuTable
     *            ONU组播组信息
     * @throws ModifyIgmpMcOnuInfoException
     */
    public void modifyIgmpMcOnuInfo(IgmpMcOnuTable igmpMcOnuTable);

    /**
     * 添加一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param onuIndex
     *            onu索引
     * @throws Exception
     */
    public void addIgmpUniInOnu(Long entityId, Long onuIndex);

    /**
     * 添加一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param uniIndex
     *            uni索引
     * @param multicastPackageListNum
     *            频道列表
     * @throws Exception
     */
    public void addIgmpUni(Long entityId, Long uniIndex, List<Integer> multicastPackageListNum);

    /**
     * 修改一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param uniIndex
     *            uni索引
     * @param multicastPackageListNum
     *            频道列表
     * @throws Exception
     */
    public void modifyIgmpUni(Long entityId, Long uniIndex, List<Integer> multicastPackageListNum);

    /**
     * 删除一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param onuIndex
     *            onu索引
     * @throws Exception
     */
    public void deleteIgmpUniInOnu(Long entityId, Long onuIndex);

    /***************************************************** UNI *************************************************/
    /**
     * 获取UNI口的IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param uniIndex
     *            UNI INDEX
     * @return List<IgmpMcUniConfigTable>
     */
    public IgmpMcUniConfigTable getIgmpMcUniConfig(Long entityId, Long uniIndex);

    /**
     * 修改UNI口的IGMP信息
     * 
     * @param igmpMcUniConfigTable
     *            UNI口IGMP信息
     * @throws ModifyIgmpMcUniConfigException
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
     * @throws AddIgmpVlanTransException
     */
    public void addIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /**
     * 修改组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     * @throws ModifyIgmpVlanTransException
     */
    public void modifyIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /**
     * 删除组播VLAN转换规则
     * 
     * @param entityId
     *            设备ID
     * @param topMcOnuVlanTransIndex
     *            组播VLAN转换规则INDEX
     * @throws DeleteIgmpVlanTransException
     */
    public void deleteIgmpVlanTrans(Long entityId, Integer topMcOnuVlanTransIndex);

    /********************************************* 刷新组播组当前活跃PON口的信息 ***************************************/
    /**
     * 刷新活跃PON口列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpForwardingTable>
     * @throws RefreshIgmpForwardingTableException
     * 
     */
    List<IgmpForwardingTable> refreshIgmpForwardingInfo(Long entityId);

    void refreshIgmpControlledMulticastPackageTable(Long entityId);

    void refreshIgmpControlledMulticastUserAuthorityTable(Long entityId);

    void refreshIgmpEntityTable(Long entityId);

    void refreshIgmpForwardingTable(Long entityId);

    void refreshIgmpMcOnuTable(Long entityId);

    void refreshIgmpMcOnuVlanTransTable(Long entityId);

    void refreshIgmpMcParamMgmtObjects(Long entityId);

    void refreshIgmpMcSniConfigMgmtObjects(Long entityId);

    void refreshIgmpMcUniConfigTable(Long entityId);

    void refreshIgmpProxyParaTable(Long entityId);

    void refreshIgmpControlledMcCdrTable(Long entityId);

    /**
     * 获取板卡igmpForwarding信息
     * 
     * @param entityId
     * @param proxyId
     */
    public TopMcForwardingSlotTable getTopMcForwardingSlot(Long entityId, Integer proxyId);

    /**
     * 获取端口igmpForwarding信息
     * 
     * @param entityId
     * @param proxyId
     * @param slotNo
     */
    public TopMcForwardingPortTable getTopMcForwardingPort(Long entityId, Integer proxyId, Integer slotNo);

    /**
     * 获取ONUigmpForwarding信息
     * 
     * @param entityId
     * @param proxyId
     * @param slotNo
     * @param ponPortNo
     */
    public TopMcForwardingOnuTable getTopMcForwardingOnu(Long entityId, Integer proxyId, Integer slotNo,
            Integer ponPortNo);

    public void refreshTopMcForwardingSlotTable(Long entityId);

    public void refreshTopMcForwardingPortTable(Long entityId);

    public void refreshTopMcForwardingOnuTable(Long entityId);

    /**
     * @param entityId
     * @param start
     * @param limit
     * @return
     */
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId, int start, int limit);

    /**
     * 加载igmp snooping信息
     * 
     * @param entityId
     * @param portIndex
     * @param vid
     * @return
     */
    public List<IgmpForwardingSnooping> loadIgmpSnoopingData(Long entityId, Long portIndex, Integer vid);

    /**
     * 从设备获取igmpsnooping信息
     * 
     * @param entityId
     */
    public void refreshIgmpSnooping(Long entityId);
}
