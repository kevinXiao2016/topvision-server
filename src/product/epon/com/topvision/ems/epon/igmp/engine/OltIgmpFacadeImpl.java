/***********************************************************************
 * $Id: OltIgmpFacadeImpl.java,v1.0 2013-10-25 下午4:47:59 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
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
import com.topvision.ems.epon.igmp.facade.OltIgmpFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午4:47:59
 *
 */
public class OltIgmpFacadeImpl extends EmsFacade implements OltIgmpFacade {

    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /**
     * 添加一个组播组
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     *            组播组
     */
    @Override
    public void addIgmpMvlan(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        igmpControlledMulticastPackageTable.setCmRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastPackageTable);
    }

    /**
     * 添加一个频道
     * 
     * @param snmpParam
     * @param igmpProxyParaTable
     *            频道
     */
    @Override
    public void addIgmpProxy(SnmpParam snmpParam, IgmpProxyParaTable igmpProxyParaTable) {
        igmpProxyParaTable.setProxyRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, igmpProxyParaTable);
    }

    /**
     * 添加组播VLAN转换规则
     * 
     * @param snmpParam
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    @Override
    public void addIgmpVlanTrans(SnmpParam snmpParam, IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        igmpMcOnuVlanTransTable.setTopMcOnuVlanTransRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, igmpMcOnuVlanTransTable);
    }

    /**
     * 删除一个组播组
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     * 
     */
    @Override
    public void deleteIgmpMvlan(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        igmpControlledMulticastPackageTable.setCmRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastPackageTable);
    }

    /**
     * 删除一个频道
     * 
     * @param snmpParam
     * @param igmpProxyParaTable
     */
    @Override
    public void deleteIgmpProxy(SnmpParam snmpParam, IgmpProxyParaTable igmpProxyParaTable) {
        igmpProxyParaTable.setProxyRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, igmpProxyParaTable);
    }

    /**
     * 删除组播VLAN转换规则
     * 
     * @param snmpParam
     * @param igmpMcOnuVlanTransTable
     */
    @Override
    public void deleteIgmpVlanTrans(SnmpParam snmpParam, IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        igmpMcOnuVlanTransTable.setTopMcOnuVlanTransRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, igmpMcOnuVlanTransTable);
    }

    /**
     * 刷新呼叫信息记录表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdrTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpControlledMcCdrTable.class);
    }

    /**
     * 刷新可控组播业务包表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpControlledMulticastPackageTable> getIgmpControlledMulticastPackageTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpControlledMulticastPackageTable.class);
    }

    /**
     * 可控组播用户权限表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpControlledMulticastUserAuthorityTable> getIgmpControlledMulticastUserAuthorityTable(
            SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpControlledMulticastUserAuthorityTable.class);
    }

    /**
     * 刷新IGMP实体表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpEntityTable> getIgmpEntityTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpEntityTable.class);
    }

    /**
     * 刷新活跃PON口表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpForwardingTable> getIgmpForwardingTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpForwardingTable.class);
    }

    /**
     * Onu IGMP参数表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpMcOnuTable> getIgmpMcOnuTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpMcOnuTable.class);
    }

    /**
     * 组播组转换规则表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpMcOnuVlanTransTable> getIgmpMcOnuVlanTransTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpMcOnuVlanTransTable.class);
    }

    /**
     * 刷新最大组播组数
     * 
     * @param snmpParam
     * @return IgmpMcParamMgmtObjects
     */
    @Override
    public IgmpMcParamMgmtObjects getIgmpMcParamMgmtObjects(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, IgmpMcParamMgmtObjects.class);
    }

    /**
     * 刷新Sni口组播组属性
     * 
     * @param snmpParam
     * @return IgmpMcSniConfigMgmtObjects
     */
    @Override
    public IgmpMcSniConfigMgmtObjects getIgmpMcSniConfigMgmtObjects(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, IgmpMcSniConfigMgmtObjects.class);
    }

    /**
     * 刷新Uni口组播组属性
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpMcUniConfigTable> getIgmpMcUniConfigTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpMcUniConfigTable.class);
    }

    /**
     * 刷新频道表
     * 
     * @param snmpParam
     * @return
     */
    @Override
    public List<IgmpProxyParaTable> getIgmpProxyParaTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpProxyParaTable.class);
    }

    /**
     * 修改IGMP全局属性
     * 
     * @param snmpParam
     * @param igmpEntityTable
     *            IGMP全局配置
     */
    @Override
    public void modifyIgmpGlobalInfo(SnmpParam snmpParam, IgmpEntityTable igmpEntityTable) {
        snmpExecutorService.setData(snmpParam, igmpEntityTable);
    }

    /**
     * 修改最大组播组数
     * 
     * @param snmpParam
     * @param igmpEntityTable
     */
    @Override
    public void modifyIgmpMaxGroupNum(SnmpParam snmpParam, IgmpMcParamMgmtObjects igmpMcParamMgmtObjects) {
        snmpExecutorService.setData(snmpParam, igmpMcParamMgmtObjects);
    }

    /**
     * 修改ONU IGMP信息
     * 
     * @param snmpParam
     * @param igmpMcOnuTable
     *            ONU组播组信息
     */
    @Override
    public void modifyIgmpMcOnuInfo(SnmpParam snmpParam, IgmpMcOnuTable igmpMcOnuTable) {
        snmpExecutorService.setData(snmpParam, igmpMcOnuTable);
    }

    /**
     * 修改UNI口的IGMP信息
     * 
     * @param snmpParam
     * @param igmpMcUniConfigTable
     *            UNI口IGMP信息
     */
    @Override
    public void modifyIgmpMcUniConfig(SnmpParam snmpParam, IgmpMcUniConfigTable igmpMcUniConfigTable) {
        snmpExecutorService.setData(snmpParam, igmpMcUniConfigTable);
    }

    /**
     * 修改组播组信息
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     *            组播组信息
     */
    @Override
    public void modifyIgmpMvlanInfo(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastPackageTable);
    }

    /**
     * 修改频道
     * 
     * @param snmpParam
     * @param igmpProxyParaTable
     *            频道
     */
    @Override
    public void modifyIgmpProxyInfo(SnmpParam snmpParam, IgmpProxyParaTable igmpProxyParaTable) {
        snmpExecutorService.setData(snmpParam, igmpProxyParaTable);
    }

    /**
     * 修改IGMP SNI口全局属性
     * 
     * @param snmpParam
     * @param igmpMcSniConfigMgmtObjects
     *            SNI口组播组参数
     */
    @Override
    public void modifyIgmpSniConfig(SnmpParam snmpParam, IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObjects) {
        snmpExecutorService.setData(snmpParam, igmpMcSniConfigMgmtObjects);
    }

    /**
     * 修改组播VLAN转换规则
     * 
     * @param snmpParam
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    @Override
    public void modifyIgmpVlanTrans(SnmpParam snmpParam, IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        snmpExecutorService.setData(snmpParam, igmpMcOnuVlanTransTable);
    }

    /**
     * 修改PON口 IGMP信息
     * 
     * @param snmpParam
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    @Override
    public void modifyMulticastUserAuthorityList(SnmpParam snmpParam,
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable) {
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastUserAuthorityTable);
    }

    /**
     * 修改频道的组播组vlan ID
     * 
     * @param snmpParam
     * @param igmpControlledMulticastPackageTable
     */
    @Override
    public void modifyMulticastPackage(SnmpParam snmpParam,
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastPackageTable);
    }

    @Override
    public void addMulticastUserAuthorityList(SnmpParam snmpParam,
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable) {
        igmpControlledMulticastUserAuthorityTable.setCmUserAuthorityRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastUserAuthorityTable);

    }

    @Override
    public void deleteMulticastUserAuthorityList(SnmpParam snmpParam,
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable) {
        // TODO Auto-generated method stub
        igmpControlledMulticastUserAuthorityTable.setCmUserAuthorityRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, igmpControlledMulticastUserAuthorityTable);
    }

    @Override
    public <T> T getDomainInfoLine(SnmpParam snmpParam, T t) {
        return snmpExecutorService.getTableLine(snmpParam, t);
    }

    @Override
    public void modifyMcPreviewInterval(SnmpParam snmpParam,
            IgmpControlledMcPreviewIntervalTable igmpControlledMcPreviewInterval) {
        snmpExecutorService.setData(snmpParam, igmpControlledMcPreviewInterval);
    }

    @Override
    public List<TopMcForwardingSlotTable> getTopMcForwardingSlotTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopMcForwardingSlotTable.class);
    }

    @Override
    public List<TopMcForwardingPortTable> getTopMcForwardingPortTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopMcForwardingPortTable.class);
    }

    @Override
    public List<TopMcForwardingOnuTable> getTopMcForwardingOnuTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopMcForwardingOnuTable.class);
    }

    @Override
    public TopMcForwardingSlotTable getTopMcForwardingSlotTable(SnmpParam snmpParam, Integer proxyIndex) {
        TopMcForwardingSlotTable forwardingSlot = new TopMcForwardingSlotTable();
        forwardingSlot.setEntityId(snmpParam.getEntityId());
        forwardingSlot.setTopMcGroupIdIndex(proxyIndex);
        return snmpExecutorService.getTableLine(snmpParam, forwardingSlot);
    }

    @Override
    public TopMcForwardingPortTable getTopMcForwardingPortTable(SnmpParam snmpParam, Integer topMcGroupIdIndex,
            Integer slotIndex) {
        TopMcForwardingPortTable forwardingPort = new TopMcForwardingPortTable();
        forwardingPort.setEntityId(snmpParam.getEntityId());
        forwardingPort.setTopMcGroupIdIndex(topMcGroupIdIndex);
        forwardingPort.setTopMcSlotIndex(slotIndex);
        return snmpExecutorService.getTableLine(snmpParam, forwardingPort);
    }

    @Override
    public TopMcForwardingOnuTable getTopMcForwardingOnuTable(SnmpParam snmpParam, Integer topMcGroupIdIndex,
            Integer topMcSlotIndex, Integer portIndex) {
        TopMcForwardingOnuTable forwardingOnu = new TopMcForwardingOnuTable();
        forwardingOnu.setEntityId(snmpParam.getEntityId());
        forwardingOnu.setTopMcGroupIdIndex(topMcGroupIdIndex);
        forwardingOnu.setTopMcSlotIndex(topMcSlotIndex);
        forwardingOnu.setTopMcPortIndex(portIndex);
        return snmpExecutorService.getTableLine(snmpParam, forwardingOnu);
    }

    @Override
    public List<IgmpControlledMcPreviewIntervalTable> getIgmpControlledMcPreviewIntervalTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpControlledMcPreviewIntervalTable.class);
    }

    @Override
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdrTableList(SnmpParam snmpParam, Long start, Long end) {
        List<IgmpControlledMcCdrTable> igmpCdr = new ArrayList<IgmpControlledMcCdrTable>();
        for (long i = start; i <= end; i++) {
            IgmpControlledMcCdrTable temp = new IgmpControlledMcCdrTable();
            temp.setSequenceIndex(i);
            temp = snmpExecutorService.getTableLine(snmpParam, temp);
            temp.setEntityId(snmpParam.getEntityId());
            igmpCdr.add(temp);
        }
        return igmpCdr;
    }

    @Override
    public List<TopIgmpForwardingSnooping> refreshIgmpSnooping(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopIgmpForwardingSnooping.class);
    }

}
