/***********************************************************************
 * $Id: OpticalReceiverDaoImpl.java,v1.0 2016年9月20日 上午9:25:04 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverDao;
import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年9月20日-上午9:25:04
 *
 */
@Repository("opticalReceiverDao")
public class OpticalReceiverDaoImpl extends MyBatisDaoSupport<Object> implements OpticalReceiverDao {

    @Override
    protected String getDomainName() {
        return this.getClass().getName();
    }

    @Override
    public OpticalReceiverData getOpticalReceiverData(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getOpticalReceiverData"), cmcId);
    }

    @Override
    public List<TopCcmtsDorDCPower> getDCPowers(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getDCPowers"), cmcId);
    }

    @Override
    public List<TopCcmtsDorFwdAtt> getFwdAtts(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getFwdAtts"), cmcId);
    }

    @Override
    public List<TopCcmtsDorFwdEq> getFwdEqs(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getFwdEqs"), cmcId);
    }

    @Override
    public List<TopCcmtsDorRevAtt> getRevAtts(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getRevAtts"), cmcId);
    }

    @Override
    public List<TopCcmtsDorRFPort> getRfPorts(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getRfPorts"), cmcId);
    }

    @Override
    public List<TopCcmtsDorRRXOptPow> getRrxOptPows(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getRrxOptPows"), cmcId);
    }

}
