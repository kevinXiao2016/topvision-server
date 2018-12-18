/***********************************************************************
 * $Id: OltControlFacadeImpl.java,v1.0 2011-10-8 下午02:52:14 $
 *
 * @author: Victor
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.vlan.domain.OltPortVlanEntry;
import com.topvision.ems.epon.vlan.domain.OltVlanAttribute;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.TopOltVlanConfigTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifSubIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTransparentRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-10-8-下午02:52:14
 */
public class OltVlanFacadeImpl extends EmsFacade implements OltVlanFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltVlanAttribute getOltVlanAttributes(SnmpParam snmpParam, OltVlanAttribute oltVlanAttribute) {
        return snmpExecutorService.getTableLine(snmpParam, oltVlanAttribute);
    }

    @Override
    public List<VlanAttribute> getSniVlanAttributes(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanAttribute.class);
    }

    @Override
    public List<VlanAggregationRule> getVlanAggregationRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanAggregationRule.class);
    }

    @Override
    public List<VlanAggregationRule> getPortAggregationRules(SnmpParam snmpParam, VlanAggregationRule aggr) {
        return snmpExecutorService.getTableRangeLine(snmpParam, aggr, 1, 4094);
    }

    @Override
    public VlanAggregationRule addSvlanAggrRule(SnmpParam snmpParam, VlanAggregationRule vlanAggregationRule) {
        // 添加聚合规则
        vlanAggregationRule.setAggregationRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanAggregationRule);
    }

    @Override
    public void deleteSvlanAggrRule(SnmpParam snmpParam, VlanAggregationRule vlanAggregationRule) {
        // 删除聚合规则
        vlanAggregationRule.setAggregationRowStatus(RowStatus.DESTORY);
        // 删除时需要将除index外内容置空。
        vlanAggregationRule.setAggregationVidList(null);
        snmpExecutorService.setData(snmpParam, vlanAggregationRule);
    }

    @Override
    public VlanAggregationRule modifyCvlanAggrRule(SnmpParam snmpParam, VlanAggregationRule vlanAggregationRule) {
        // 修改（添加或删除CVLAN）聚合规则
        return snmpExecutorService.setData(snmpParam, vlanAggregationRule);
    }

    @Override
    public List<VlanTranslationRule> getVlanTranslationRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanTranslationRule.class);
    }

    @Override
    public List<VlanTranslationRule> getPortTranslationRules(SnmpParam snmpParam, VlanTranslationRule transRule) {
        return snmpExecutorService.getTableRangeLine(snmpParam, transRule, 1, 4094);
    }

    @Override
    public List<VlanTrunkRule> getVlanTrunkRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanTrunkRule.class);
    }

    @Override
    public VlanTrunkRule getPortVlanTrunkRule(SnmpParam snmpParam, VlanTrunkRule trunkRule) {
        return snmpExecutorService.getTableLine(snmpParam, trunkRule);
    }

    @Override
    public List<VlanTransparentRule> getVlanTransparentRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanTransparentRule.class);
    }

    @Override
    public List<VlanQinQRule> getVlanQinQRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanQinQRule.class);
    }

    @Override
    public VlanQinQRule addQinQRule(SnmpParam snmpParam, VlanQinQRule vlanQinQRule) {
        // 添加QinQ规则
        vlanQinQRule.setPqRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanQinQRule);
    }

    @Override
    public void deleteQinQRule(SnmpParam snmpParam, VlanQinQRule vlanQinQRule) {
        // 删除QinQ规则
        vlanQinQRule.setPqRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanQinQRule);
    }

    @Override
    public void addTransparentRule(SnmpParam snmpParam, VlanTransparentRule vlanTransparentRule) {
        vlanTransparentRule.setPqRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, vlanTransparentRule);
    }

    @Override
    public void delTransparentRule(SnmpParam snmpParam, VlanTransparentRule vlanTransparentRule) {
        VlanTransparentRule v = new VlanTransparentRule();
        v.setDeviceIndex(vlanTransparentRule.getDeviceIndex());
        v.setPqRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, v);
    }

    @Override
    public void modifyTransparentRule(SnmpParam snmpParam, VlanTransparentRule vlanTransparentRule) {
        snmpExecutorService.setData(snmpParam, vlanTransparentRule);
    }

    @Override
    public List<VlanLlidTranslationRule> getVlanLlidTranslationRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanLlidTranslationRule.class);
    }

    @Override
    public List<VlanLlidTrunkRule> getVlanLlidTrunkRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanLlidTrunkRule.class);
    }

    @Override
    public List<VlanLlidAggregationRule> getVlanLlidAggregationRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanLlidAggregationRule.class);
    }

    @Override
    public List<VlanLlidQinQRule> getvlanLlidQinQRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanLlidQinQRule.class);
    }

    @Override
    public List<VlanLlidOnuQinQRule> getvlanLlidOnuQinQRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VlanLlidOnuQinQRule.class);
    }

    @Override
    public VlanTranslationRule addTransRule(SnmpParam snmpParam, VlanTranslationRule vlanTranslationRule) {
        // PON口/UNI口添加一条转换规则
        vlanTranslationRule.setTranslationRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanTranslationRule);
    }

    @Override
    public void deleteTransRule(SnmpParam snmpParam, VlanTranslationRule vlanTranslationRule) {
        // PON口/UNI口删除一条转换规则
        // modify by lzt 此处PON、UNI都使用不能在此处设置索引
        // VlanTranslationRule v = new VlanTranslationRule();
        // v.setDeviceIndex(vlanTranslationRule.getDeviceIndex());
        // v.setSlotNo(0L);
        // v.setPortNo(0L);
        // v.setVlanIndex(vlanTranslationRule.getVlanIndex());
        vlanTranslationRule.setTranslationRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanTranslationRule);
    }

    @Override
    public VlanTrunkRule addTrunkRule(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule) {
        vlanTrunkRule.setPortVlanTrunkRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanTrunkRule);
    }

    @Override
    public PortVlanAttribute modifyPortVlanAttributes(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute) {
        // 测试成功，但是必须满足cfi为空，如果cfi有值，即使为0，设置也会失败
        return snmpExecutorService.setData(snmpParam, portVlanAttribute);
    }

    @Override
    public void deleteTrunkRule(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule) {
        vlanTrunkRule.setPortVlanTrunkRowStatus(RowStatus.DESTORY);
        // 删除时除Index外其他值必须为空
        vlanTrunkRule.setTrunkVidList(null);
        snmpExecutorService.setData(snmpParam, vlanTrunkRule);
    }

    @Override
    public VlanTrunkRule modifyTrunkRule(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule) {
        return snmpExecutorService.setData(snmpParam, vlanTrunkRule);
    }

    @Override
    public VlanLlidTranslationRule addLlidTransRule(SnmpParam snmpParam,
            VlanLlidTranslationRule vlanLlidTranslationRule) {
        vlanLlidTranslationRule.setTopLlidTransRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanLlidTranslationRule);
    }

    @Override
    public void deleteLlidTransRule(SnmpParam snmpParam, VlanLlidTranslationRule vlanLlidTranslationRule) {
        vlanLlidTranslationRule.setTopLlidTransRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanLlidTranslationRule);
    }

    @Override
    public VlanLlidAggregationRule addLlidSvlanAggrRule(SnmpParam snmpParam,
            VlanLlidAggregationRule vlanLlidAggregationRule) {
        vlanLlidAggregationRule.setLlidVlanAggRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanLlidAggregationRule);
    }

    @Override
    public void deleteLlidSvlanAggrRule(SnmpParam snmpParam, VlanLlidAggregationRule vlanLlidAggregationRule) {
        vlanLlidAggregationRule.setLlidVlanAggRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanLlidAggregationRule);
    }

    @Override
    public VlanLlidAggregationRule modifyLlidCvlanAggrRule(SnmpParam snmpParam,
            VlanLlidAggregationRule vlanLlidAggregationRule) {
        return snmpExecutorService.setData(snmpParam, vlanLlidAggregationRule);
    }

    @Override
    public VlanLlidTrunkRule addLlidTrunkRule(SnmpParam snmpParam, VlanLlidTrunkRule vlanLlidTrunkRule) {
        vlanLlidTrunkRule.setLlidVlanTrunkRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanLlidTrunkRule);
    }

    @Override
    public void deleteLlidTrunkRule(SnmpParam snmpParam, VlanLlidTrunkRule vlanLlidTrunkRule) {
        vlanLlidTrunkRule.setLlidVlanTrunkRowStatus(RowStatus.DESTORY);
        // 删除时除Index外其他值必须为空
        vlanLlidTrunkRule.setLlidVlanTrunkVidBmp(null);
        snmpExecutorService.setData(snmpParam, vlanLlidTrunkRule);
    }

    @Override
    public VlanLlidTrunkRule modifyLlidTrunkRule(SnmpParam snmpParam, VlanLlidTrunkRule vlanLlidTrunkRule) {
        return snmpExecutorService.setData(snmpParam, vlanLlidTrunkRule);
    }

    @Override
    public VlanLlidQinQRule addLlidQinQRule(SnmpParam snmpParam, VlanLlidQinQRule vlanLlidQinQRule) {
        vlanLlidQinQRule.setTopLqVlanRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanLlidQinQRule);
    }

    @Override
    public void deleteLlidQinQRule(SnmpParam snmpParam, VlanLlidQinQRule vlanLlidQinQRule) {
        vlanLlidQinQRule.setTopLqVlanRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanLlidQinQRule);
    }

    @Override
    public void deleteLlidOnuQinQRule(SnmpParam snmpParam, VlanLlidOnuQinQRule vlanLlidOnuQinQRule) {
        vlanLlidOnuQinQRule.setTopOnuQinQRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanLlidOnuQinQRule);
    }

    @Override
    public List<PortVlanAttribute> getPortVlanAttributes(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PortVlanAttribute.class);
    }

    @Override
    public PortVlanAttribute updateSniPortAttribute(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute) {
        return snmpExecutorService.setData(snmpParam, portVlanAttribute);
    }

    @Override
    @Deprecated
    public PortVlanAttribute refreshSniPortVlanAttribute(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute) {
        // 被废弃的方法
        return null;
    }

    @Override
    public VlanAttribute modifyVlanName(SnmpParam snmpParam, VlanAttribute vlanAttribute) {
        return snmpExecutorService.setData(snmpParam, vlanAttribute);
    }

    @Override
    public VlanAttribute addOltVlan(SnmpParam snmpParam, VlanAttribute vlanAttribute) {
        vlanAttribute.setOltVlanRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanAttribute);
    }

    @Override
    public void deleteOltVlan(SnmpParam snmpParam, VlanAttribute vlanAttribute) {
        vlanAttribute.setOltVlanRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanAttribute);
    }

    @Override
    public VlanAttribute updateTagStatus(SnmpParam snmpParam, VlanAttribute vlanAttribute) {
        // 改变VLAN属性，改变TAGGED,UNTAGGED列表
        return snmpExecutorService.setData(snmpParam, vlanAttribute);
    }

    @Override
    public PortVlanAttribute getSinglePortVlanAttribute(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute) {
        return snmpExecutorService.getTableLine(snmpParam, portVlanAttribute);
    }

    @Override
    @Deprecated
    public List<VlanAggregationRule> getSingleVlanAggregationRules(SnmpParam snmpPara,
            VlanAggregationRule vlanAggregationRule) {
        // 被废弃的方法
        return null;
    }

    @Override
    @Deprecated
    public List<VlanTranslationRule> getSingleVlanTranslationRules(SnmpParam snmpParam,
            VlanTranslationRule vlanTranslationRule) {
        // 被废弃的方法
        return null;
    }

    @Override
    @Deprecated
    public List<VlanTrunkRule> getSingleVlanTrunkRules(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule) {
        return null;
        // 被废弃的方法
    }

    @Override
    @Deprecated
    public List<VlanQinQRule> getSingleVlanQinQRules(SnmpParam snmpParam, VlanQinQRule qinQRule) {
        return null;
        // 被废弃的方法
    }

    @Override
    public TopOltVlanConfigTable modifyMcFloodMode(SnmpParam snmpParam, TopOltVlanConfigTable topOltVlanConfig) {
        return snmpExecutorService.setData(snmpParam, topOltVlanConfig);
    }

    @Override
    public TopOltVlanVifPriIpTable setVlanPriIp(SnmpParam snmpParam, TopOltVlanVifPriIpTable vlanVifPriIp) {
        vlanVifPriIp.setTopOltVifPriIpRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanVifPriIp);
    }

    @Override
    public TopOltVlanVifPriIpTable modifyVlanVifPriIp(SnmpParam snmpParam, TopOltVlanVifPriIpTable vlanVifPriIp) {
        return snmpExecutorService.setData(snmpParam, vlanVifPriIp);
    }

    @Override
    public void deleteVlanVif(SnmpParam snmpParam, TopOltVlanVifPriIpTable vlanVifPriIp) {
        vlanVifPriIp.setTopOltVifPriIpRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanVifPriIp);
    }

    @Override
    public TopOltVlanVifSubIpTable addVlanVifSubIp(SnmpParam snmpParam, TopOltVlanVifSubIpTable vlanVifSubIp) {
        vlanVifSubIp.setTopOltVifSubIpRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanVifSubIp);
    }

    @Override
    public TopOltVlanVifSubIpTable modifyVlanVifSubIp(SnmpParam snmpParam, TopOltVlanVifSubIpTable vlanVifSubIp) {
        return snmpExecutorService.setData(snmpParam, vlanVifSubIp);
    }

    @Override
    public void deleteVlanVifSubIp(SnmpParam snmpParam, TopOltVlanVifSubIpTable vlanVifSubIp) {
        vlanVifSubIp.setTopOltVifSubIpRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, vlanVifSubIp);
    }

    @Override
    public List<TopOltVlanVifPriIpTable> getOltVlanVifPriIpTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltVlanVifPriIpTable.class);
    }

    @Override
    public List<TopOltVlanVifSubIpTable> getOltVlanVifSubIpTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltVlanVifSubIpTable.class);
    }

    @Override
    public List<TopOltVlanConfigTable> getTopMcFloodMode(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOltVlanConfigTable.class);
    }

    @Override
    public OltAttribute getOltAttribute(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, OltAttribute.class);
    }

    @Override
    public List<OltPortVlanEntry> getOltPortVlanEntry(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPortVlanEntry.class);
    }

    @Override
    public OltPortVlanEntry modifyOltPortVlanEntry(SnmpParam snmpParam, OltPortVlanEntry oltPortVlanEntry) {
        return snmpExecutorService.setData(snmpParam, oltPortVlanEntry);
    }

    @Override
    public OltPortVlanEntry getSingleOltPortVlanEntry(SnmpParam snmpParam, OltPortVlanEntry oltPortVlanEntry) {
        return snmpExecutorService.getTableLine(snmpParam, oltPortVlanEntry);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public VlanLlidOnuQinQRule addLlidOnuQinQRule(SnmpParam snmpParam, VlanLlidOnuQinQRule onuQinQRule) {
        onuQinQRule.setTopOnuQinQRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, onuQinQRule);
    }
}