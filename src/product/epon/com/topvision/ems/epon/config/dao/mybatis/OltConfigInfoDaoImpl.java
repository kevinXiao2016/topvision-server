/***********************************************************************
 * $Id: OltConfigInfoDao.java,v1.0 2013-10-26 上午9:43:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.config.dao.OltConfigInfoDao;
import com.topvision.ems.epon.domain.OltVlanInterface;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-26-上午9:43:09
 *
 */
@Repository("oltConfigInfoDao")
public class OltConfigInfoDaoImpl extends MyBatisDaoSupport<Object> implements OltConfigInfoDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.config.domain.OltConfigInfo";
    }

    @Override
    public void updateIpMaskInfo(OltAttribute oltAttribute) {
        getSqlSession().update(getNameSpace("updateInbandParamInfo"), oltAttribute);
    }

    @Override
    public void updateGateInfo(OltAttribute oltAttribute) {
        if (oltAttribute.getInbandGateway() == null) {
            oltAttribute.setInbandIpGateway(0L);
        } else if (oltAttribute.getOutbandGateway() == null) {
            oltAttribute.setOutbandIpGateway(0L);
        }
        getSqlSession().update(getNameSpace("updateGate"), oltAttribute);
    }

    @Override
    public void updateOutbandParamInfo(OltAttribute oltAttribute) {
        getSqlSession().update(getNameSpace("updateOutbandParamInfo"), oltAttribute);
    }

    @Override
    public List<OltVlanInterface> selectVlanInterfaceList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectVlanInterfaceList"), entityId);
    }

    @Override
    public List<VlanAttribute> selectAvailableVlanIndexList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectAvailableVlanIndexList"), entityId);
    }

    @Override
    public void updateInBandConfig(OltAttribute oltAttribute) {
        OltAttribute oltInBandConfig = new OltAttribute();
        oltInBandConfig.setEntityId(oltAttribute.getEntityId());
        oltInBandConfig.setInbandIpAddress(oltAttribute.getInbandIpAddress());
        oltInBandConfig.setInbandIpGateway(oltAttribute.getInbandIpGateway());
        oltInBandConfig.setInbandIpSubnetMask(oltAttribute.getInbandIpSubnetMask());
        oltInBandConfig.setTopSysInBandMaxBw(oltAttribute.getTopSysInBandMaxBw());
        oltInBandConfig.setInbandVlanId(oltAttribute.getInbandVlanId());
        oltInBandConfig.setInbandPortIndex(oltAttribute.getInbandPortIndex());
        // oltInBandConfig.setInbandMacAddress(oltAttribute.getInbandMacAddress());
        getSqlSession().update(getNameSpace("updateInBandConfig"), oltInBandConfig);
    }

    @Override
    public void updateOutBandConfig(OltAttribute oltAttribute) {
        OltAttribute oltOutBandConfig = new OltAttribute();
        oltOutBandConfig.setEntityId(oltAttribute.getEntityId());
        oltOutBandConfig.setOutbandIpAddress(oltAttribute.getOutbandIpAddress());
        oltOutBandConfig.setOutbandIpGateway(oltAttribute.getOutbandIpGateway());
        oltOutBandConfig.setOutbandIpSubnetMask(oltAttribute.getOutbandIpSubnetMask());
        // oltOutBandConfig.setOutbandMacAddress(oltAttribute.getOutbandMacAddress());
        getSqlSession().update(getNameSpace("updateOutBandConfig"), oltOutBandConfig);
    }

    @Override
    public void updateOltSnmpConfig(OltAttribute oltAttribute) {
        OltAttribute oltSnmpConfig = new OltAttribute();
        oltSnmpConfig.setEntityId(oltAttribute.getEntityId());
        // oltSnmpConfig.setTopSysReadCommunity(oltAttribute.getTopSysReadCommunity());
        // oltSnmpConfig.setTopSysWriteCommunity(oltAttribute.getTopSysWriteCommunity());
        oltSnmpConfig.setTopSysSnmpHostIp(oltAttribute.getTopSysSnmpHostIp());
        oltSnmpConfig.setTopSysSnmpHostIpMask(oltAttribute.getTopSysSnmpHostIpMask());
        oltSnmpConfig.setTopSysSnmpVersion(oltAttribute.getTopSysSnmpVersion());
        getSqlSession().update(getNameSpace("modifyOltSnmpConfig"), oltSnmpConfig);
    }

    @Override
    public void updateOltSnmpV2CConfig(OltAttribute oltAttribute) {
        getSqlSession().update(getNameSpace("modifyOltSnmpV2CConfig"), oltAttribute);
    }

}
