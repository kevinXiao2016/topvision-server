/***********************************************************************
 * $Id: OltConfigInfoDao.java,v1.0 2013-10-26 上午9:42:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.dao;

import java.util.List;

import com.topvision.ems.epon.domain.OltVlanInterface;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-26-上午9:42:13
 *
 */
public interface OltConfigInfoDao extends BaseEntityDao<Object> {

    void updateIpMaskInfo(OltAttribute oltAttribute);

    void updateGateInfo(OltAttribute oltAttribute);

    void updateOutbandParamInfo(OltAttribute oltAttribute);

    List<OltVlanInterface> selectVlanInterfaceList(Long entityId);

    List<VlanAttribute> selectAvailableVlanIndexList(Long entityId);

    /**
     * 修改OLT设备带内网管配置
     * 
     * @param oltAttribute
     *            设备属性
     */
    void updateInBandConfig(OltAttribute oltAttribute);

    /**
     * 修改OLT设备带外网管配置
     * 
     * @param oltAttribute
     *            设备属性
     */
    void updateOutBandConfig(OltAttribute oltAttribute);

    /**
     * 修改OLT设备SNMP配置
     * 
     * @param oltAttribute
     *            设备属性
     */
    void updateOltSnmpConfig(OltAttribute oltAttribute);

    /**
     * 修改OLT设备SNMPV2C配置
     * 
     * @param oltAttribute
     *            设备属性
     */
    void updateOltSnmpV2CConfig(OltAttribute oltAttribute);

}
