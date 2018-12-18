/***********************************************************************
 * $Id: SniVlanDao.java,v1.0 2013-10-25 上午11:42:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.OltVlanAttribute;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.TopOltVlanConfigTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifSubIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-9-19-下午01:59:50
 * 
 */
public interface SniVlanDao extends BaseEntityDao<Object> {

    /**
     * 更新OLT设备基本属性
     * 
     * @param oltVlanAttribute
     */
    void updateOltVlanGlobalInfo(OltVlanAttribute oltVlanAttribute);

    /**
     * 批量插入SNI口VLAN基本配置
     * 
     * @param list
     */
    void batchInsertOltVlanConfig(List<VlanAttribute> list, Long entityId);

    /**
     * 批量更新SNI口VLAN信息
     * 
     * @param list
     */
    void batchInsertOltPortVlan(List<PortVlanAttribute> list, Long entityId);


    /**
     * 获取SNI VLAN基本属性
     * 
     * @param entityId
     *            设备id
     * @param sniIndex
     *            sni口Index
     * @return PortVlanAttribute
     */
    PortVlanAttribute getSniPortVlanAttribute(Long entityId, Long sniIndex);

    /**
     * 修改SNI VLAN基本属性
     * 
     * @param portVlanAttribute
     */
    void updateSniPortAttribute(PortVlanAttribute portVlanAttribute);

    /**
     * 修改VLAN名称
     * 
     * @param entityId
     * @param vlanIndex
     * @param oltVlanName
     */
    void modifyVlanName(Long entityId, Integer vlanIndex, String oltVlanName, Integer topMcFloodMode);

    /**
     * 获取VLAN全局属性
     * 
     * @param entityId
     * @return
     */
    OltVlanAttribute getOltVlanGlobalInfo(Long entityId);

    /**
     * 获取VLAN列表
     * 
     * @param entityId
     * @return
     */
    List<VlanAttribute> getOltVlanConfigList(Long entityId);

    /**
     * 新增VLAN
     * 
     * @param vlanAttribute
     */
    void addOltVlan(VlanAttribute vlanAttribute);

    /**
     * 删除VLAN
     * 
     * @param entityId
     * @param vlanIndex
     */
    void deleteOltVlan(Long entityId, Integer vlanIndex);

    /**
     * 更新某VLAN下TAGGED和UNTAGGED列表
     * 
     * @param vlanAttribute
     */
    void updateTagStatus(VlanAttribute vlanAttribute);

    /**
     * 获取单个VLAN配置属性
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     */
    VlanAttribute getOltVlanConfig(Long entityId, Integer vlanIndex);

    /**
     * 创建vlan虚接口
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifPriIpAddr
     * @param topOltVifPriIpMask
     */
    void setVlanPriIp(TopOltVlanVifPriIpTable vlanVifPriIp);

    /**
     * 修改vlan虚接口主IP
     * 
     * @param entityId
     * @param vlanIndex
     * @param topOltVifPriIpAddr
     * @param topOltVifPriIpMask
     */
    void modifyVlanVifPriIp(TopOltVlanVifPriIpTable vlanVifPriIp);

    /**
     * 删除vlan虚接口
     * 
     * @param vlanVifPriIp
     */
    void deleteVlanVif(TopOltVlanVifPriIpTable vlanVifPriIp);

    /**
     * 新增vlan虚接口子IP
     * 
     * @param newObject
     */
    void addVlanVifSubIp(TopOltVlanVifSubIpTable newObject);

    /**
     * 修改vlan虚接口子IP
     * 
     * @param vlanVifSubIp
     */
    void modifyVlanVifSubIp(TopOltVlanVifSubIpTable vlanVifSubIp);

    /**
     * 删除VLAN虚接口子IP
     * 
     * @param vlanVifSubIp
     */
    void deleteVlanVifSubIp(TopOltVlanVifSubIpTable vlanVifSubIp);

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
     * 删除vlan虚接口子Ip
     * 
     * @param entityId
     * @param vlanIndex
     */
    void deleteVlanVifSubIps(Long entityId, Integer vlanIndex);

    /**
     * 批量插入VLAN虚接口主IP
     * 
     * @param list
     */
    void batchInsertVlanVifPriIp(List<TopOltVlanVifPriIpTable> list, Long entityId);

    /**
     * 批量插入VLAN虚接口子IP
     * 
     * @param list
     */
    void batchInsertVlanVifSubIp(List<TopOltVlanVifSubIpTable> list, Long entityId);

    /**
     * 批量插入vlan组播模式
     * 
     * @param topOltVlanConfigTables
     * @param entityId
     */
    void batchInsertTopMcFloodMode(List<TopOltVlanConfigTable> topOltVlanConfigTables, Long entityId);

    /**
     * 查询子ip已经使用的index
     * @param entityId
     * @param vlanIndex
     * @return
     */
    List<Integer> queryUsedSubIpIndex(Long entityId, Integer vlanIndex);

}
