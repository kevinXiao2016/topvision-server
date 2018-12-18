/***********************************************************************
 * $Id: OltDhcpDao.java,v1.0 2013-10-25 下午5:44:34 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.dao;

import java.util.List;

import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-下午5:44:34
 *
 */
public interface OltDhcpDao extends BaseEntityDao<Object> {
    
    /**
     * 
     * 
     * @param entityId
     * @return
     */
    OltDhcpBaseConfig getDhcpBaseConfig(Long entityId);

    /**
     * 
     * 
     * @param baseConfig
     */
    void insertDhcpBaseConfig(OltDhcpBaseConfig baseConfig);

    /**
     * 
     * 
     * @param baseConfig
     */
    void modifyDhcpBaseConfig(OltDhcpBaseConfig baseConfig);

    /**
     * 
     * 
     * @param entityId
     * @return
     */
    List<OltDhcpServerConfig> getDhcpServerConfigs(Long entityId);

    /**
     * 
     * 
     * @param entityId
     * @param dhcpServerIndex
     */
    void deleteDhcpServerRule(Long entityId, Integer dhcpServerIndex);

    /**
     * 
     * 
     * @param dhcpServerConfig
     */
    void insertDhcpServerRule(OltDhcpServerConfig dhcpServerConfig);

    /**
     * 
     * 
     * @param entityId
     * @return
     */
    List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigs(Long entityId);

    /**
     * 
     * 
     * @param entityId
     * @param dhcpGiaddrIndex
     */
    void deleteDhcpGiaddrRule(Long entityId, Integer dhcpGiaddrIndex);

    /**
     * 
     * 
     * @param giaddrConfig
     */
    void insertDhcpGiaddrRule(OltDhcpGiaddrConfig giaddrConfig);

    /**
     * 
     * 
     * @param entityId
     * @return
     */
    List<OltDhcpIpMacStatic> getDhcpIpMacStatics(Long entityId);

    /**
     * 
     * 
     * @param entityId
     * @param topOltDHCPIpMacIdx
     */
    void deleteDhcpIpMacStatic(Long entityId, Integer topOltDHCPIpMacIdx);

    /**
     * 
     * 
     * @param dhcpIpMacStatic
     */
    void insertDhcpIpMacStatic(OltDhcpIpMacStatic dhcpIpMacStatic);

    /**
     * 
     * 
     * @param entityId
     * @param serverConfigs
     */
    void batchInsertDhcpServerConfig(Long entityId, final List<OltDhcpServerConfig> serverConfigs);

    /**
     * 
     * 
     * @param entityId
     * @param giaddrConfigs
     */
    void batchInsertDhcpGiaddrConfig(Long entityId, final List<OltDhcpGiaddrConfig> giaddrConfigs);

    /**
     * 
     * 
     * @param entityId
     * @param ipMacStatics
     */
    void batchInsertDhcpIpMacStatic(Long entityId, final List<OltDhcpIpMacStatic> ipMacStatics);

    void batchInsertDhcpBaseConfig(Long entityId, OltDhcpBaseConfig baseConfig);

    Integer getMacIndexForIpMacStatic(Long entityId);

    Integer getCountForIpMacStatic(Long entityId, Long topOltDHCPIpAddrLong, Long topOltDHCPMacAddrLong);

}
