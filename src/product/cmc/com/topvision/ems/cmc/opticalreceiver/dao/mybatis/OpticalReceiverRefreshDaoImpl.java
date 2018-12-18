/***********************************************************************
 * $Id: OpticalReceiverDaoImpl.java,v1.0 2016年9月13日 下午3:16:45 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverRefreshDao;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorABSwitch;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorChannelNum;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDevParams;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorLinePower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxInput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxOutput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:16:45
 *
 */
@Repository("opticalReceiverRefreshDao")
public class OpticalReceiverRefreshDaoImpl extends MyBatisDaoSupport<Object> implements OpticalReceiverRefreshDao {

    @Override
    protected String getDomainName() {
        return this.getClass().getName();
    }

    @Override
    public void updateSysDorType(TopCcmtsSysDorType topCcmtsSysDorType) {
        getSqlSession().update(getNameSpace("updateSysDorType"), topCcmtsSysDorType);
    }

    @Override
    public void insertOrUpdateABSwitch(TopCcmtsDorABSwitch topCcmtsDorABSwitch) {
        getSqlSession().insert(getNameSpace("insertOrUpdateABSwitch"), topCcmtsDorABSwitch);
    }

    @Override
    public void insertOrUpdateChannelNum(TopCcmtsDorChannelNum topCcmtsDorChannelNum) {
        getSqlSession().insert(getNameSpace("insertOrUpdateChannelNum"), topCcmtsDorChannelNum);
    }

    @Override
    public void batchInsertOrUpdateDCPower(List<TopCcmtsDorDCPower> powerList) {
        SqlSession session = getBatchSession();
        try {
            for (TopCcmtsDorDCPower power : powerList) {
                session.insert(getNameSpace("insertOrUpdateDCPower"), power);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertOrUpdateDevParams(TopCcmtsDorDevParams topCcmtsDorDevParams) {
        getSqlSession().insert(getNameSpace("insertOrUpdateDevParams"), topCcmtsDorDevParams);
    }

    @Override
    public void batchInsertOrUpdateFwdAtts(List<TopCcmtsDorFwdAtt> topCcmtsDorFwdAtts) {
        SqlSession session = getBatchSession();
        try {
            for (TopCcmtsDorFwdAtt topCcmtsDorFwdAtt : topCcmtsDorFwdAtts) {
                session.insert(getNameSpace("insertOrUpdateFwdAtt"), topCcmtsDorFwdAtt);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateFwdEqs(List<TopCcmtsDorFwdEq> topCcmtsDorFwdEqs) {
        SqlSession session = getBatchSession();
        try {
            for (TopCcmtsDorFwdEq topCcmtsDorFwdEq : topCcmtsDorFwdEqs) {
                session.insert(getNameSpace("insertOrUpdateFwdEq"), topCcmtsDorFwdEq);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertOrUpdateLinePower(TopCcmtsDorLinePower topCcmtsDorLinePower) {
        getSqlSession().insert(getNameSpace("insertOrUpdateLinePower"), topCcmtsDorLinePower);
    }

    @Override
    public void batchInsertOrUpdateRevAtts(List<TopCcmtsDorRevAtt> topCcmtsDorRevAtts) {
        SqlSession session = getBatchSession();
        try {
            for (TopCcmtsDorRevAtt topCcmtsDorRevAtt : topCcmtsDorRevAtts) {
                session.insert(getNameSpace("insertOrUpdateRevAtt"), topCcmtsDorRevAtt);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateRFPorts(List<TopCcmtsDorRFPort> topCcmtsDorRFPorts) {
        SqlSession session = getBatchSession();
        try {
            for (TopCcmtsDorRFPort topCcmtsDorRFPort : topCcmtsDorRFPorts) {
                session.insert(getNameSpace("insertOrUpdateRFPort"), topCcmtsDorRFPort);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdatesRRXOptPows(List<TopCcmtsDorRRXOptPow> topCcmtsDorRRXOptPows) {
        SqlSession session = getBatchSession();
        try {
            for (TopCcmtsDorRRXOptPow topCcmtsDorRRXOptPow : topCcmtsDorRRXOptPows) {
                session.insert(getNameSpace("insertOrUpdateRRXOptPow"), topCcmtsDorRRXOptPow);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertOrUpdateOpRxInput(TopCcmtsOpRxInput topCcmtsOpRxInput) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOpRxInput"), topCcmtsOpRxInput);
    }

    @Override
    public void insertOrUpdateOpRxOutput(TopCcmtsOpRxOutput topCcmtsOpRxOutput) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOpRxOutput"), topCcmtsOpRxOutput);
    }

}
