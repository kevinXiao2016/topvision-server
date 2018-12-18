/***********************************************************************
 * $Id: OltDhcpService.java,v1.0 2013-10-25 下午5:42:52 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.dhcp.dao.OltDhcpDao;
import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.ems.epon.dhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.dhcp.service.OltDhcpService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午5:42:52
 *
 */
@Service("oltDhcpService")
public class OltDhcpServiceImpl extends BaseService implements OltDhcpService {

    @Autowired
    private OltDhcpDao oltDhcpDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityService entityService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    private OltDhcpFacade getOltDhcpFacade(String ip) {
        return facadeFactory.getFacade(ip, OltDhcpFacade.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#getDhcpBaseConfig(java.lang.Long)
     */
    @Override
    public OltDhcpBaseConfig getDhcpBaseConfig(Long entityId) {
        return oltDhcpDao.getDhcpBaseConfig(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#getDhcpServerConfigs(java.lang.Long)
     */
    @Override
    public List<OltDhcpServerConfig> getDhcpServerConfigs(Long entityId) {
        return oltDhcpDao.getDhcpServerConfigs(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#deleteDhcpServerConfigs(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public void deleteDhcpServerConfigs(Long entityId, Integer dhcpServerIndex) {
        OltDhcpServerConfig serverConfig = new OltDhcpServerConfig();
        serverConfig.setEntityId(entityId);
        serverConfig.setTopOltDHCPServerIndex(dhcpServerIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltDhcpFacade(snmpParam.getIpAddress()).deleteDhcpServerConfig(snmpParam, serverConfig);
        oltDhcpDao.deleteDhcpServerRule(entityId, dhcpServerIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#insertDhcpServerConfigs(com.topvision.ems.epon
     * .facade.domain.OltDhcpServerConfig)
     */
    @Override
    public void insertDhcpServerConfigs(OltDhcpServerConfig dhcpServerConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(dhcpServerConfig.getEntityId());
        getOltDhcpFacade(snmpParam.getIpAddress()).addDhcpServerConfig(snmpParam, dhcpServerConfig);
        oltDhcpDao.insertDhcpServerRule(dhcpServerConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#updateDhcpServerConfigs(com.topvision.ems.epon
     * .facade.domain.OltDhcpServerConfig)
     */
    @Override
    public void updateDhcpServerConfigs(OltDhcpServerConfig dhcpServerConfig) {
        // 由于设备不支持修改 网管功能的修改是先下发删除，然后再进行增加，保证Index前后相同即可
        deleteDhcpServerConfigs(dhcpServerConfig.getEntityId(), dhcpServerConfig.getTopOltDHCPServerIndex());
        insertDhcpServerConfigs(dhcpServerConfig);
        oltDhcpDao.deleteDhcpServerRule(dhcpServerConfig.getEntityId(), dhcpServerConfig.getTopOltDHCPServerIndex());
        oltDhcpDao.insertDhcpServerRule(dhcpServerConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#modifyDhcpBaseConfigs(com.topvision.ems.epon
     * .facade.domain.OltDhcpBaseConfig)
     */
    @Override
    public void modifyDhcpBaseConfigs(OltDhcpBaseConfig dhcpBaseConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(dhcpBaseConfig.getEntityId());
        getOltDhcpFacade(snmpParam.getIpAddress()).modifyDhcpBaseConfig(snmpParam, dhcpBaseConfig);
        oltDhcpDao.modifyDhcpBaseConfig(dhcpBaseConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#getDhcpGiaddrConfigs(java.lang.Long)
     */
    @Override
    public List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigs(Long entityId) {
        return oltDhcpDao.getDhcpGiaddrConfigs(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#deleteDhcpGiaddrConfigs(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public void deleteDhcpGiaddrConfigs(Long entityId, Integer dhcpGiaddrIndex) {
        OltDhcpGiaddrConfig giaddrConfig = new OltDhcpGiaddrConfig();
        giaddrConfig.setEntityId(entityId);
        giaddrConfig.setTopOltDHCPGiaddrIndex(dhcpGiaddrIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltDhcpFacade(snmpParam.getIpAddress()).deleteDhcpGiaddrConfig(snmpParam, giaddrConfig);
        oltDhcpDao.deleteDhcpGiaddrRule(entityId, dhcpGiaddrIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#insertDhcpGiaddrConfigs(com.topvision.ems.epon
     * .facade.domain.OltDhcpGiaddrConfig)
     */
    @Override
    public void insertDhcpGiaddrConfigs(OltDhcpGiaddrConfig dhcpGiaddrConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(dhcpGiaddrConfig.getEntityId());
        getOltDhcpFacade(snmpParam.getIpAddress()).addDhcpGiaddrConfig(snmpParam, dhcpGiaddrConfig);
        oltDhcpDao.insertDhcpGiaddrRule(dhcpGiaddrConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#getDhcpIpMacStatics(java.lang.Long)
     */
    @Override
    public List<OltDhcpIpMacStatic> getDhcpIpMacStatics(Long entityId) {
        return oltDhcpDao.getDhcpIpMacStatics(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#deleteDhcpIpMacStatic(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public void deleteDhcpIpMacStatic(Long entityId, Integer topOltDHCPIpMacIdx) {
        OltDhcpIpMacStatic ipMacStatic = new OltDhcpIpMacStatic();
        ipMacStatic.setEntityId(entityId);
        ipMacStatic.setTopOltDHCPIpMacIdx(topOltDHCPIpMacIdx);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltDhcpFacade(snmpParam.getIpAddress()).deleteDhcpIpMacStatic(snmpParam, ipMacStatic);
        oltDhcpDao.deleteDhcpIpMacStatic(entityId, topOltDHCPIpMacIdx);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#insertDhcpIpMacStatic(com.topvision.ems.epon
     * .facade.domain.OltDhcpIpMacStatic)
     */
    @Override
    public void insertDhcpIpMacStatic(OltDhcpIpMacStatic dhcpIpMacStatic) {
        int maxIndex = 0;
        if (oltDhcpDao.getMacIndexForIpMacStatic(dhcpIpMacStatic.getEntityId()) == null) {
            maxIndex = 0;
        } else {
            maxIndex = oltDhcpDao.getMacIndexForIpMacStatic(dhcpIpMacStatic.getEntityId()) + 1;
        }
        dhcpIpMacStatic.setTopOltDHCPIpMacIdx(maxIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(dhcpIpMacStatic.getEntityId());
        dhcpIpMacStatic = getOltDhcpFacade(snmpParam.getIpAddress()).addDhcpIpMacStatic(snmpParam, dhcpIpMacStatic);
        oltDhcpDao.insertDhcpIpMacStatic(dhcpIpMacStatic);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#updateDhcpGiaddrConfigs(com.topvision.ems.epon
     * .facade.domain.OltDhcpGiaddrConfig)
     */
    @Override
    public void updateDhcpGiaddrConfigs(OltDhcpGiaddrConfig dhcpGiaddrConfig) {
        deleteDhcpGiaddrConfigs(dhcpGiaddrConfig.getEntityId(), dhcpGiaddrConfig.getTopOltDHCPGiaddrIndex());
        insertDhcpGiaddrConfigs(dhcpGiaddrConfig);
        oltDhcpDao.deleteDhcpGiaddrRule(dhcpGiaddrConfig.getEntityId(), dhcpGiaddrConfig.getTopOltDHCPGiaddrIndex());
        oltDhcpDao.insertDhcpGiaddrRule(dhcpGiaddrConfig);
    }

    public void discoveryOltDhcpBaseConfig(SnmpParam snmpParam) {
        if (getOltDhcpFacade(snmpParam.getIpAddress()).getDhcpBaseConfig(snmpParam) != null) {
            OltDhcpBaseConfig baseConfig = getOltDhcpFacade(snmpParam.getIpAddress()).getDhcpBaseConfig(snmpParam);
            baseConfig.setEntityId(snmpParam.getEntityId());
            oltDhcpDao.batchInsertDhcpBaseConfig(snmpParam.getEntityId(), baseConfig);
        }
    }

    public void discoveryOltDhcpServerConfig(SnmpParam snmpParam) {
        List<OltDhcpServerConfig> serverConfigs = new ArrayList<OltDhcpServerConfig>();
        serverConfigs = getOltDhcpFacade(snmpParam.getIpAddress()).getDhcpServerConfigs(snmpParam);
        if (serverConfigs.size() > 0) {
            for (OltDhcpServerConfig serverConfig : serverConfigs) {
                serverConfig.setEntityId(snmpParam.getEntityId());
            }
        }
        oltDhcpDao.batchInsertDhcpServerConfig(snmpParam.getEntityId(), serverConfigs);
    }

    public void discoveryOltDhcpGiaddrConfig(SnmpParam snmpParam) {
        List<OltDhcpGiaddrConfig> giaddrConfigs = new ArrayList<OltDhcpGiaddrConfig>();
        giaddrConfigs = getOltDhcpFacade(snmpParam.getIpAddress()).getDhcpGiaddrConfigs(snmpParam);
        if (giaddrConfigs.size() > 0) {
            for (OltDhcpGiaddrConfig giaddrConfig : giaddrConfigs) {
                giaddrConfig.setEntityId(snmpParam.getEntityId());
            }
        }
        oltDhcpDao.batchInsertDhcpGiaddrConfig(snmpParam.getEntityId(), giaddrConfigs);
    }

    public void discoveryOltDhcpIpMacStaticConfig(SnmpParam snmpParam) {
        List<OltDhcpIpMacStatic> ipMacStatics = new ArrayList<OltDhcpIpMacStatic>();
        ipMacStatics = getOltDhcpFacade(snmpParam.getIpAddress()).getDhcpIpMacStatics(snmpParam);
        if (ipMacStatics.size() > 0) {
            for (OltDhcpIpMacStatic ipMacStatic : ipMacStatics) {
                ipMacStatic.setEntityId(snmpParam.getEntityId());
            }
        }
        oltDhcpDao.batchInsertDhcpIpMacStatic(snmpParam.getEntityId(), ipMacStatics);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#refreshOltDhcpBaseConfig(java.lang.Long)
     */
    @Override
    public void refreshOltDhcpBaseConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        discoveryOltDhcpBaseConfig(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#refreshOltDhcpServerConfig(java.lang.Long)
     */
    @Override
    public void refreshOltDhcpServerConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        discoveryOltDhcpServerConfig(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#refreshOltDhcpGiaddrConfig(java.lang.Long)
     */
    @Override
    public void refreshOltDhcpGiaddrConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        discoveryOltDhcpGiaddrConfig(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltDhcpService#refreshOltDhcpIpMacStaticConfig(java.lang.Long)
     */
    @Override
    public void refreshOltDhcpIpMacStaticConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        discoveryOltDhcpIpMacStaticConfig(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltDhcpService#getCountForIpMacStatic(java.lang.Long,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public Integer getCountForIpMacStatic(Long entityId, Long topOltDHCPIpAddrLong, Long topOltDHCPMacAddrLong) {
        return oltDhcpDao.getCountForIpMacStatic(entityId, topOltDHCPIpAddrLong, topOltDHCPMacAddrLong);
    }

}
