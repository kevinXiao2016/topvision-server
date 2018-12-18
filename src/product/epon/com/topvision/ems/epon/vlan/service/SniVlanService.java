/***********************************************************************
 * $Id: SniVlanService.java,v1.0 2013-10-25 下午4:33:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.OltVlanAttribute;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifSubIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author huqiao
 * @created @2011-10-24-19:01:45
 */
public interface SniVlanService extends Service {

    /**
     * 获取SNI VLAN基本属性
     * 
     * @param entityId
     * @param sniId
     * @return
     */
    PortVlanAttribute getSniPortVlanAttribute(Long entityId, Long sniIndex);

    /**
     * 修改SNI VLAN基本属性
     * 
     * @param portVlanAttribute
     */
    void updateSniPortVlanAttribute(Long sniId, Integer vlanPVid, Integer vlanTagPriority, String vlanTagTpid,
            Integer vlanMode, Long sniIndex, Long entityId);

    /**
     * 从设备刷新数据
     * 
     * @param entityId
     * @param sniId
     * @return
     */
    void refreshSniPortVlanAttribute(Long entityId, Long sniIndex);

    /**
     * 修改VLAN名称
     * 
     * @param vlanAttribute
     */
    void modifyVlanName(Long entityId, Integer vlanIndex, String oltVlanName, Integer topMcFloodMode);

    /**
     * 获取全局VLAN属性
     * 
     * @param entityId
     * @return
     */
    OltVlanAttribute getOltVlanGlobalInfo(Long entityId);

    /**
     * 获取VLAN列表数据
     * 
     * @param entityId
     * @return
     */
    List<VlanAttribute> getOltVlanConfigList(Long entityId);

    /**
     * 新增VLAN
     * 
     * @param vlanAttribute
     * @return
     */
    void addOltVlan(VlanAttribute vlanAttribute);

    /**
     * 删除VLAN
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     *  
     */
    List<Integer> deleteOltVlan(Long entityId, String vidListStr);

    /**
     * 更新某VLAN下TAGGED和UNTAGGED列表
     * 
     * @param param
     */
    void updateTagStatus(Long entityId, Integer vlanIndex, List<Long> taggedPortList, List<Long> untaggedPortList,
            SnmpParam param);

    /**
     * 获取单个VLAN配置属性
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     */
    VlanAttribute getOltVlanConfig(Long entityId, Integer vlanIndex);

    VlanAttribute getSniVlanConfig(Long entityId, Integer vlanIndex);

    /**
     * 更新VLAN视图中全局VLAN属性
     * 
     * @param snmpParam
     */
    void refreshSniVlanView(SnmpParam snmpParam);

    /**
     * 更新SNI口VLAN基本信息
     * 
     * @param snmpParam
     */
    void refreshSniPortVlan(SnmpParam snmpParam);

    /**
     * 更新SNI口VLAN属性
     * 
     * @param snmpParam
     */
    void refreshSniVlanAttribute(SnmpParam snmpParam);

    /**
     * 修改pvid的时候同时将此端口加入vlan的untaggedport中
     * 
     * @param param
     */
    void refreshVlanAttributeByChangePVid(Long entityId, List<Long> portIndex, Integer pvid, SnmpParam param);

    /**
     * 创建vlan虚接口
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifPriIpAddr
     * @param topOltVifPriIpMask
     */
    void setVlanPriIp(Long entityId, Integer vlanIndex, String topOltVifPriIpAddr, String topOltVifPriIpMask);

    /**
     * 修改vlan虚接口主IP
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifPriIpAddr
     * @param topOltVifPriIpMask
     */
    void modifyVlanVifPriIp(Long entityId, Integer vlanIndex, String topOltVifPriIpAddr, String topOltVifPriIpMask);

    /**
     * 删除vlan虚接口
     * 
     * @param entityId
     * @param vlanIndex
     */
    void deleteVlanVif(Long entityId, Integer vlanIndex);

    /**
     * 新增VLAN虚接口子IP
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifSubIpAddr
     * @param topOltVifSubIpMask
     */
    void addVlanVifSubIp(Long entityId, Integer vlanIndex, String topOltVifSubIpAddr, String topOltVifSubIpMask);

    /**
     * 修改vlan虚接口子IP
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifSubIpSeqIdx
     * @param topOltVifSubIpAddr
     * @param topOltVifSubIpMask
     */
    void modifyVlanVifSubIp(Long entityId, Integer vlanIndex, Integer topOltVifSubIpSeqIdx, String topOltVifSubIpAddr,
            String topOltVifSubIpMask);

    /**
     * 删除vlan虚接口子IP
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifSubIpSeqIdx
     */
    void deleteVlanVifSubIp(Long entityId, Integer vlanIndex, Integer topOltVifSubIpSeqIdx);

    /**
     * 获取VLAN虚接口主IP
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     */
    TopOltVlanVifPriIpTable getVlanVifPriIp(Long entityId, Integer vlanIndex);

    /**
     * 获取VLAN虚接口列表
     * 
     * @param entityId
     * @return
     */
    List<TopOltVlanVifPriIpTable> getVlanVifPriIpList(Long entityId);

    /**
     * 获取VLAN虚接口子IP
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     */
    List<TopOltVlanVifSubIpTable> getVlanVifSubIp(Long entityId, Integer vlanIndex);

    /**
     * 
     * 
     * @param snmpParam
     */
    void refreshVlanVifPriIp(SnmpParam snmpParam);

    /**
     * 
     * 
     * @param snmpParam
     */
    void refreshVlanVifSubIp(SnmpParam snmpParam);

    /**
     * 
     * 
     * @param snmpParam
     */
    void refreshTopMcFloodMode(SnmpParam snmpParam);

    /**
     * 刷新vlan虚接口数据
     * 
     * @param entityId
     */
    void refreshVlanVif(Long entityId);
}
