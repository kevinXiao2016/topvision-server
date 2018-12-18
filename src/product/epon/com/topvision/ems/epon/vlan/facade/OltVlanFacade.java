/***********************************************************************
 * $Id: OltConfigureFacade.java,v1.0 2011-9-26 下午12:10:27 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.facade;

import java.util.List;

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
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author huqiao
 * 
 */
@EngineFacade(serviceName = "OltVlanFacade", beanName = "oltVlanFacade")
public interface OltVlanFacade extends Facade {

    /**
     * 获得设备VLAN基本信息
     * 
     * @param snmpParam
     * @return list
     */
    OltVlanAttribute getOltVlanAttributes(SnmpParam snmpParam, OltVlanAttribute oltVlanAttribute);

    /**
     * 获得SNI口VLAN基本信息
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanAttribute> getSniVlanAttributes(SnmpParam snmpParam);

    /**
     * 获得Port VLAN 基本属性
     * 
     * @param snmpParam
     * @return list
     */

    List<PortVlanAttribute> getPortVlanAttributes(SnmpParam snmpParam);

    /**
     * 获得某个uni/sni端口VLAN基本属性
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param portVlanAttribute
     *            端口Vlan基本属性
     * @return
     */

    PortVlanAttribute getSinglePortVlanAttribute(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute);

    /**
     * 修改 port vlan基本属性
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param portVlanAttribute
     *            端口Vlan基本属性
     */

    PortVlanAttribute modifyPortVlanAttributes(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute);

    /**
     * 获得VLAN聚合规则
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanAggregationRule> getVlanAggregationRules(SnmpParam snmpParam);

    /**
     * 获得单个UNI端口VLAN聚合规则
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanAggregationRule> getPortAggregationRules(SnmpParam snmpParam, VlanAggregationRule aggr);

    /**
     * 获得单个VLAN聚合规则
     * 
     * @param snmpParam
     * @return list
     */
    @Deprecated
    List<VlanAggregationRule> getSingleVlanAggregationRules(SnmpParam snmpPara,
            VlanAggregationRule vlanAggregationRule);

    /**
     * 获得VLAN转换规则
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanTranslationRule> getVlanTranslationRules(SnmpParam snmpParam);

    /**
     * 获得单个端口VLAN转换规则列表
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanTranslationRule> getPortTranslationRules(SnmpParam snmpParam, VlanTranslationRule transRule);

    /**
     * 获得单个VLAN转换规则
     * 
     * @param snmpParam
     * @return list
     */
    @Deprecated
    List<VlanTranslationRule> getSingleVlanTranslationRules(SnmpParam snmpParam,
            VlanTranslationRule vlanTranslationRule);

    /**
     * 获得VLAN trunk规则
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanTrunkRule> getVlanTrunkRules(SnmpParam snmpParam);

    VlanTrunkRule getPortVlanTrunkRule(SnmpParam snmpParam, VlanTrunkRule trunkRule);

    List<VlanTransparentRule> getVlanTransparentRules(SnmpParam snmpParam);

    /**
     * 获得单个VLAN trunk规则
     * 
     * @param snmpParam
     * @return list
     */
    @Deprecated
    List<VlanTrunkRule> getSingleVlanTrunkRules(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule);

    /**
     * 获得VLAN QinQ规则
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanQinQRule> getVlanQinQRules(SnmpParam snmpParam);

    /**
     * 获得单个VLAN QinQ规则
     * 
     * @param snmpParam
     * @return list
     */
    @Deprecated
    List<VlanQinQRule> getSingleVlanQinQRules(SnmpParam snmpParam, VlanQinQRule qinQRule);

    /**
     * 获得VLAN转换规则（基于LLID）
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanLlidTranslationRule> getVlanLlidTranslationRules(SnmpParam snmpParam);

    /**
     * 获得VLAN trunk规则（基于LLID）
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanLlidTrunkRule> getVlanLlidTrunkRules(SnmpParam snmpParam);

    /**
     * 获得VLAN聚合规则（基于LLID）
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanLlidAggregationRule> getVlanLlidAggregationRules(SnmpParam snmpParam);

    /**
     * 获得VLAN QinQ规则（基于LLID）
     * 
     * @param snmpParam
     * @return list
     */
    List<VlanLlidQinQRule> getvlanLlidQinQRules(SnmpParam snmpParam);

    /**
     * 获得VLAN QinQ规则（基于LLID,适配TL1）
     * 
     * @param snmpParam
     * @return
     */
    List<VlanLlidOnuQinQRule> getvlanLlidOnuQinQRules(SnmpParam snmpParam);

    /**
     * 获得OltVlanVifPriIpTables
     * 
     * @param snmpParam
     * @return list
     */
    List<TopOltVlanVifPriIpTable> getOltVlanVifPriIpTables(SnmpParam snmpParam);

    /**
     * 获得OltVlanVifSubIpTables
     * 
     * @param snmpParam
     * @return list
     */
    List<TopOltVlanVifSubIpTable> getOltVlanVifSubIpTables(SnmpParam snmpParam);

    /**
     * 添加转换规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanTranslationRule
     *            VLAN转换规则
     * @return VlanTranslationRule
     */
    VlanTranslationRule addTransRule(SnmpParam snmpParam, VlanTranslationRule vlanTranslationRule);

    /**
     * 删除转换规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanTranslationRule
     *            VLAN转换规则
     */
    void deleteTransRule(SnmpParam snmpParam, VlanTranslationRule vlanTranslationRule);

    /**
     * 添加SVLAN聚合规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanAggregationRule
     *            VLAN聚合规则
     * 
     * @return VlanAggregationRule
     */
    VlanAggregationRule addSvlanAggrRule(SnmpParam snmpParam, VlanAggregationRule vlanAggregationRule);

    /**
     * 删除SVLAN聚合规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanAggregationRule
     *            VLAN聚合规则
     */
    void deleteSvlanAggrRule(SnmpParam snmpParam, VlanAggregationRule vlanAggregationRule);

    /**
     * 删除、添加CVLAN聚合规则（即修改聚合规则）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanAggregationRule
     *            VLAN聚合规则
     * @return VlanAggregationRule
     */
    VlanAggregationRule modifyCvlanAggrRule(SnmpParam snmpParam, VlanAggregationRule vlanAggregationRule);

    /**
     * 添加该PON口新的Trunk规则（第一次添加trunk规则）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanTrunkRule
     *            VLAN Trunk规则
     * @return VlanTrunkRule
     */
    VlanTrunkRule addTrunkRule(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule);

    /**
     * 删除该PON口的Trunk规则列表（删除完最后一条trunk规则）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanTrunkRule
     *            VLAN Trunk规则
     */
    void deleteTrunkRule(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule);

    /**
     * 删除(非最后一条删除)、添加(非首条添加)Trunk规则（即修改Trunk规则）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanTrunkRule
     *            VLAN Trunk规则
     * @return VlanTrunkRule
     */
    VlanTrunkRule modifyTrunkRule(SnmpParam snmpParam, VlanTrunkRule vlanTrunkRule);

    /**
     * 添加QinQ规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanQinQRule
     *            VLANQinQ规则
     * @return VlanQinQRule
     */
    VlanQinQRule addQinQRule(SnmpParam snmpParam, VlanQinQRule vlanQinQRule);

    /**
     * 删除QinQ规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanQinQRule
     *            VLANQinQ规则
     */
    void deleteQinQRule(SnmpParam snmpParam, VlanQinQRule vlanQinQRule);

    // transparent
    void addTransparentRule(SnmpParam snmpParam, VlanTransparentRule vlanTransparentRule);

    void delTransparentRule(SnmpParam snmpParam, VlanTransparentRule vlanTransparentRule);

    void modifyTransparentRule(SnmpParam snmpParam, VlanTransparentRule vlanTransparentRule);

    // LLID VLAN转换
    /**
     * 添加转换规则(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidTranslationRule
     *            LLID VLAN转换规则
     * @return VlanLlidTranslationRule
     */
    VlanLlidTranslationRule addLlidTransRule(SnmpParam snmpParam, VlanLlidTranslationRule vlanLlidTranslationRule);

    /**
     * 删除转换规则(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidTranslationRule
     *            LLID VLAN转换规则
     */
    void deleteLlidTransRule(SnmpParam snmpParam, VlanLlidTranslationRule vlanLlidTranslationRule);

    // LLID VLAN聚合
    /**
     * 添加SVLAN聚合规则（基于LLID）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidAggregationRule
     *            LLID VLAN聚合规则
     * @return vlanLlidAggregationRule
     */
    VlanLlidAggregationRule addLlidSvlanAggrRule(SnmpParam snmpParam, VlanLlidAggregationRule vlanLlidAggregationRule);

    /**
     * 删除SVLAN聚合规则（基于LLID）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidAggregationRule
     *            LLID VLAN聚合规则
     */
    void deleteLlidSvlanAggrRule(SnmpParam snmpParam, VlanLlidAggregationRule vlanLlidAggregationRule);

    /**
     * 删除、添加CVLAN聚合规则（即修改聚合规则）
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidAggregationRule
     *            VLAN聚合规则
     * @return vlanLlidAggregationRule
     */
    VlanLlidAggregationRule modifyLlidCvlanAggrRule(SnmpParam snmpParam,
            VlanLlidAggregationRule vlanLlidAggregationRule);

    // LLID VLAN Trunk
    /**
     * 添加Trunk规则(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidTrunkRule
     *            LLID VLAN Trunk规则
     * @return VlanLlidTrunkRule
     */
    VlanLlidTrunkRule addLlidTrunkRule(SnmpParam snmpParam, VlanLlidTrunkRule vlanLlidTrunkRule);

    /**
     * 删除Trunk规则(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidTrunkRule
     *            LLID VLAN Trunk规则
     */
    void deleteLlidTrunkRule(SnmpParam snmpParam, VlanLlidTrunkRule vlanLlidTrunkRule);

    /**
     * 删除(非最后一条删除)、添加(非首条添加)Trunk规则（即修改Trunk规则）(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidTrunkRule
     *            VLAN Trunk规则
     * @return VlanLlidTrunkRule
     */
    VlanLlidTrunkRule modifyLlidTrunkRule(SnmpParam snmpParam, VlanLlidTrunkRule vlanLlidTrunkRule);

    // LLID VLAN QinQ
    /**
     * 添加QinQ规则(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidQinQRule
     *            LLID VLAN QinQ规则
     * @return VlanLlidQinQRule
     */
    VlanLlidQinQRule addLlidQinQRule(SnmpParam snmpParam, VlanLlidQinQRule vlanLlidQinQRule);

    /**
     * 删除QinQ规则(基于LLID)
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param vlanLlidQinQRule
     *            LLID VLAN QinQ规则
     */
    void deleteLlidQinQRule(SnmpParam snmpParam, VlanLlidQinQRule vlanLlidQinQRule);

    /**
     * 
     * 删除QinQ规则(基于LLID,适配TL1)
     * 
     * @param snmpParam
     * @param vlanLlidOnuQinQRule
     */
    void deleteLlidOnuQinQRule(SnmpParam snmpParam, VlanLlidOnuQinQRule vlanLlidOnuQinQRule);

    /**
     * 更新SNI口VLAN属性
     * 
     * @param snmpParam
     * @param portVlanAttribute
     */
    PortVlanAttribute updateSniPortAttribute(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute);

    /**
     * 从设备获取数据
     * 
     * @param snmpParam
     * @param entityId
     * @param sniId
     */
    @Deprecated
    PortVlanAttribute refreshSniPortVlanAttribute(SnmpParam snmpParam, PortVlanAttribute portVlanAttribute);

    /**
     * 
     * @param snmpParam
     * @param entityId
     * @param vlanIndex
     * @param oltVlanName
     */
    VlanAttribute modifyVlanName(SnmpParam snmpParam, VlanAttribute vlanAttribute);

    /**
     * VLAN视图中新增VLAN
     * 
     * @param snmpParam
     * @param vlanAttribute
     */
    VlanAttribute addOltVlan(SnmpParam snmpParam, VlanAttribute vlanAttribute);

    /**
     * VLAN视图中删除VLAN
     * 
     * @param snmpParam
     * @param vlanIndex
     */
    void deleteOltVlan(SnmpParam snmpParam, VlanAttribute vlanAttribute);

    /**
     * 修改VLAN属性中TAGGED,UNTAGGED列表
     * 
     * @param snmpParam
     * @param vlanAttribute
     */
    VlanAttribute updateTagStatus(SnmpParam snmpParam, VlanAttribute vlanAttribute);

    /**
     * 修改vlan组播控制模式
     * 
     * @param snmpParam
     * @param topOltVlanConfig
     * @return
     */
    TopOltVlanConfigTable modifyMcFloodMode(SnmpParam snmpParam, TopOltVlanConfigTable topOltVlanConfig);

    /**
     * 创建vlan虚接口
     * 
     * @param snmpParam
     * @param vlanVifPriIp
     * @return
     */
    TopOltVlanVifPriIpTable setVlanPriIp(SnmpParam snmpParam, TopOltVlanVifPriIpTable vlanVifPriIp);

    /**
     * 修改vlan虚接口主IP
     * 
     * @param snmpParam
     * @param vlanVifPriIp
     * @return
     */
    TopOltVlanVifPriIpTable modifyVlanVifPriIp(SnmpParam snmpParam, TopOltVlanVifPriIpTable vlanVifPriIp);

    /**
     * 删除vlan虚接口
     * 
     * @param snmpParam
     * @param vlanVifPriIp
     */
    void deleteVlanVif(SnmpParam snmpParam, TopOltVlanVifPriIpTable vlanVifPriIp);

    /**
     * 新增vlan虚接口子IP
     * 
     * @param snmpParam
     * @param vlanVifSubIp
     * @return
     */
    TopOltVlanVifSubIpTable addVlanVifSubIp(SnmpParam snmpParam, TopOltVlanVifSubIpTable vlanVifSubIp);

    /**
     * 修改vlan虚接口子IP
     * 
     * @param snmpParam
     * @param vlanVifSubIp
     * @return
     */
    TopOltVlanVifSubIpTable modifyVlanVifSubIp(SnmpParam snmpParam, TopOltVlanVifSubIpTable vlanVifSubIp);

    /**
     * 删除vlan虚接口子IP
     * 
     * @param snmpParam
     * @param vlanVifSubIp
     */
    void deleteVlanVifSubIp(SnmpParam snmpParam, TopOltVlanVifSubIpTable vlanVifSubIp);

    /**
     * 获取vlan组播控制模式
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltVlanConfigTable> getTopMcFloodMode(SnmpParam snmpParam);

    /**
     * 获得OLT的基本信息 Add by Rod EMS-3861
     * 
     * @return
     */
    OltAttribute getOltAttribute(SnmpParam snmpParam);

    /**
     * 获取SNI/PON vlan
     * 
     * @param snmpParam
     * @return
     */
    List<OltPortVlanEntry> getOltPortVlanEntry(SnmpParam snmpParam);

    OltPortVlanEntry modifyOltPortVlanEntry(SnmpParam snmpParam, OltPortVlanEntry oltPortVlanEntry);

    OltPortVlanEntry getSingleOltPortVlanEntry(SnmpParam snmpParam, OltPortVlanEntry oltPortVlanEntry);

    /**
     * @param snmpParam
     * @param onuQinQRule
     * @return
     */
    VlanLlidOnuQinQRule addLlidOnuQinQRule(SnmpParam snmpParam, VlanLlidOnuQinQRule onuQinQRule);

}