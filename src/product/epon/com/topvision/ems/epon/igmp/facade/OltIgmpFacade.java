/***********************************************************************
 * $Id: OltIgmpFacade.java,v1.0 2013-10-25 下午4:45:39 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.facade;

import java.util.List;

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
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午4:45:39
 *
 */
@EngineFacade(serviceName = "OltIgmpFacade", beanName = "oltIgmpFacade")
public interface OltIgmpFacade extends Facade {

    /**
     * 修改IGMP全局属性
     * 
     * @param snmpParam
     * @param igmpEntityTable
     *            IGMP全局配置
     */
    public void modifyIgmpGlobalInfo(SnmpParam snmpParam, IgmpEntityTable igmpEntityTable);

    /**
     * 修改IGMP SNI口全局属性
     * 
     * @param snmpParam
     * @param igmpMcSniConfigMgmtObjects
     *            SNI口组播组参数
     */
    public void modifyIgmpSniConfig(SnmpParam snmpParam, IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObjects);

    /**
     * 修改最大组播组数
     * 
     * @param snmpParam
     * @param igmpEntityTable
     */
    public void modifyIgmpMaxGroupNum(SnmpParam snmpParam, IgmpMcParamMgmtObjects igmpMcParamMgmtObjects);

    /***************************************************** 组播组 *************************************************/
    /**
     * 修改组播组信息
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     *            组播组信息
     */
    public void modifyIgmpMvlanInfo(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 添加一个组播组
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     *            组播组
     */
    public void addIgmpMvlan(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /**
     * 删除一个组播组
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     * 
     */
    public void deleteIgmpMvlan(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /***************************************************** 组播组 *************************************************/
    /**
     * 修改频道
     * 
     * @param snmpParam
     * @param igmpProxyParaTable
     *            频道
     */
    public void modifyIgmpProxyInfo(SnmpParam snmpParam, IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 添加一个频道
     * 
     * @param snmpParam
     * @param igmpProxyParaTable
     *            频道
     */
    public void addIgmpProxy(SnmpParam snmpParam, IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 删除一个频道
     * 
     * @param snmpParam
     * @param igmpProxyParaTable
     */
    public void deleteIgmpProxy(SnmpParam snmpParam, IgmpProxyParaTable igmpProxyParaTable);

    /**
     * 修改频道的组播组vlan ID
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     */
    public void modifyMulticastPackage(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable);

    /***************************************************** UNI口 *************************************************/
    /**
     * 修改UNI口 IGMP信息
     * 
     * @param snmpParam
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    public void modifyMulticastUserAuthorityList(SnmpParam snmpParam,
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable);

    /**
     * 新增UNI口IGMP信息
     * 
     * @param snmpParam
     * @param igmpControlledMulticastUserAuthorityTable
     */
    public void addMulticastUserAuthorityList(SnmpParam snmpParam,
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable);

    /**
     * 删除UNI口IGMP信息
     * 
     * @param snmpParam
     * @param igmpControlledMulticastUserAuthorityTable
     */
    public void deleteMulticastUserAuthorityList(SnmpParam snmpParam,
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable);

    /***************************************************** ONU *************************************************/
    /**
     * 修改ONU IGMP信息
     * 
     * @param snmpParam
     * @param igmpMcOnuTable
     *            ONU组播组信息
     */
    public void modifyIgmpMcOnuInfo(SnmpParam snmpParam, IgmpMcOnuTable igmpMcOnuTable);

    /***************************************************** UNI *************************************************/
    /**
     * 修改UNI口的IGMP信息
     * 
     * @param snmpParam
     * @param igmpMcUniConfigTable
     *            UNI口IGMP信息
     */
    public void modifyIgmpMcUniConfig(SnmpParam snmpParam, IgmpMcUniConfigTable igmpMcUniConfigTable);

    /***************************************************** UNI *************************************************/
    /**
     * 添加组播VLAN转换规则
     * 
     * @param snmpParam
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    public void addIgmpVlanTrans(SnmpParam snmpParam, IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /**
     * 修改组播VLAN转换规则
     * 
     * @param snmpParam
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    public void modifyIgmpVlanTrans(SnmpParam snmpParam, IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /**
     * 删除组播VLAN转换规则
     * 
     * @param snmpParam
     * @param igmpMcOnuVlanTransTable
     */
    public void deleteIgmpVlanTrans(SnmpParam snmpParam, IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable);

    /********************************************* 刷新组播组当前活跃PON口的信息 ***************************************/
    /**
     * 刷新呼叫信息记录表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpControlledMcCdrTable> getIgmpControlledMcCdrTable(SnmpParam snmpParam);

    /**
     * 刷新可控组播业务包表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpControlledMulticastPackageTable> getIgmpControlledMulticastPackageTable(SnmpParam snmpParam);

    /**
     * 可控组播用户权限表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpControlledMulticastUserAuthorityTable> getIgmpControlledMulticastUserAuthorityTable(SnmpParam snmpParam);

    /**
     * 刷新IGMP实体表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpEntityTable> getIgmpEntityTable(SnmpParam snmpParam);

    /**
     * 刷新活跃PON口表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpForwardingTable> getIgmpForwardingTable(SnmpParam snmpParam);

    /**
     * Onu IGMP参数表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpMcOnuTable> getIgmpMcOnuTable(SnmpParam snmpParam);

    /**
     * 组播组转换规则表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpMcOnuVlanTransTable> getIgmpMcOnuVlanTransTable(SnmpParam snmpParam);

    /**
     * 刷新最大组播组数
     * 
     * @param snmpParam
     * @return
     */
    IgmpMcParamMgmtObjects getIgmpMcParamMgmtObjects(SnmpParam snmpParam);

    /**
     * 刷新Sni口组播组属性
     * 
     * @param snmpParam
     * @return
     */
    IgmpMcSniConfigMgmtObjects getIgmpMcSniConfigMgmtObjects(SnmpParam snmpParam);

    /**
     * 刷新Uni口组播组属性
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpMcUniConfigTable> getIgmpMcUniConfigTable(SnmpParam snmpParam);

    /**
     * 刷新频道表
     * 
     * @param snmpParam
     * @return
     */
    List<IgmpProxyParaTable> getIgmpProxyParaTable(SnmpParam snmpParam);

    /**
     * 
     * @param <T>
     *            表示此为泛型接口
     * @param t
     * @return
     */
    <T> T getDomainInfoLine(SnmpParam snmpParam, T t);

    /**
     * 修改可控组播预览间隔
     * 
     * @param snmpParam
     * @param igmpControlledMcPreviewInterval
     */
    public void modifyMcPreviewInterval(SnmpParam snmpParam,
            IgmpControlledMcPreviewIntervalTable igmpControlledMcPreviewInterval);

    /**
     * 获取组播组下板卡信息
     * 
     * @param snmpParam
     * @return
     */
    public List<TopMcForwardingSlotTable> getTopMcForwardingSlotTables(SnmpParam snmpParam);

    /**
     * 获取组播组下端口信息
     * 
     * @param snmpParam
     * @return
     */
    public List<TopMcForwardingPortTable> getTopMcForwardingPortTables(SnmpParam snmpParam);

    /**
     * 获取组播组下ONU信息
     * 
     * @param snmpParam
     * @return
     */
    public List<TopMcForwardingOnuTable> getTopMcForwardingOnuTables(SnmpParam snmpParam);

    /**
     * 获取FORWARDING SLOT
     * 
     * @param snmpParam
     * @param proxyIndex
     * @return
     */
    public TopMcForwardingSlotTable getTopMcForwardingSlotTable(SnmpParam snmpParam, Integer proxyIndex);

    /**
     * 获取FORWARDING PORT
     * 
     * @param snmpParam
     * @param topMcGroupIdIndex
     * @param slotIndex
     * @return
     */
    public TopMcForwardingPortTable getTopMcForwardingPortTable(SnmpParam snmpParam, Integer topMcGroupIdIndex,
            Integer slotIndex);

    /**
     * 获取 FORWARDING ONU
     * 
     * @param snmpParam
     * @param topMcGroupIdIndex
     * @param topMcSlotIndex
     * @param portIndex
     * @return
     */
    public TopMcForwardingOnuTable getTopMcForwardingOnuTable(SnmpParam snmpParam, Integer topMcGroupIdIndex,
            Integer topMcSlotIndex, Integer portIndex);

    /**
     * 获取IgmpControlledMcPreviewIntervalTable
     * 
     * @param snmpParam
     * @return
     */
    public List<IgmpControlledMcPreviewIntervalTable> getIgmpControlledMcPreviewIntervalTable(SnmpParam snmpParam);

    /**
     * 获取IgmpControlledMcCdrTable呼叫记录
     * 
     * @param snmpParam
     * @param start
     * @param end
     * @return
     */
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdrTableList(SnmpParam snmpParam, Long start, Long end);

    /**
     * 从设备获取igmp Snooping信息
     * 
     * @param snmpParam
     * 
     * @return
     */
    public List<TopIgmpForwardingSnooping> refreshIgmpSnooping(SnmpParam snmpParam);

}
