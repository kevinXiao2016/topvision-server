/***********************************************************************
 * $Id: OnuDao.java,v1.0 2013-10-25 上午11:04:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComVlanConfig;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午11:04:40
 *
 */
public interface ElectricityOnuDao extends BaseEntityDao<Object> {
    /**
     * 获取ONU的串口VLAN信息
     * 
     * @param entityId
     * @return
     */
    OltOnuComVlanConfig getComVlanConfig(Long entityId);

    /**
     * 获得ONU的串口服务器基本信息
     * 
     * @param entityId
     * @param onuComIndex
     * @return
     */
    OltOnuComAttribute getComVlamAttribute(Long entityId, Long onuComIndex);

    /**
     * 更新ONU串口VLAN信息 更新ONU串口VLAN信息
     * 
     * @param entityId
     * @param vlan
     */
    void updateOnuComVlan(Long entityId, int vlan);

    /**
     * 采集时处理ONU串口VLAN信息
     * 
     * @param entityId
     * @param vlan
     */
    void batchInsertOnuComVlan(Long entityId, int vlan);

    /**
     * 更新ONU的管理信息
     * 
     * @param entityId
     * @param onuId
     * @param onuIp
     * @param onuMask
     * @param onuGateway
     */
    void updateOnuIpMaskInfo(Long entityId, Long onuId, String onuIp, String onuMask, String onuGateway);

    /**
     * 更新ONU的串口服务器基本信息
     * 
     * @param entityId
     * @param attribute
     */
    void updateOnuComAttribute(Long entityId, OltOnuComAttribute attribute);

    /**
     * 采集时处理ONU的串口服务器基本信息
     * 
     * @param entityId
     * @param attribute
     */
    void batchInsertOnuComAttribute(Long entityId, List<OltOnuComAttribute> attributes);

    /**
     * 加载可用的PON口割接端口列表
     * 
     * @param entityId
     * @param ponCutOverPortIndex
     * @return
     */
    List<Long> loadPonCutOverPort(Long entityId, Long ponCutOverPortIndex);

    /**
     * 从数据库获取ONU MAC地址管理信息
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltOnuMacMgmt getOnuMacMgmt(Long entityId, Long onuIndex);

    /**
     * 修改ONU MAC地址管理信息
     * 
     * @param oltOnuMacMgmt
     */
    void updateOnuMacMgmt(OltOnuMacMgmt oltOnuMacMgmt);

    /**
     * 插入一条ONU MAC弟子管理的信息
     * 
     * @param oltOnuMacMgmt
     */
    void insertOnuMacMgmt(OltOnuMacMgmt oltOnuMacMgmt);

    /**
     * 从数据库加载电力ONU的带内管理信息
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltTopOnuCapability getElecOnuCapability(Long entityId, Long onuIndex);

}
