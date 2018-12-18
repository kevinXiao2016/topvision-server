/***********************************************************************
 * $Id: CmcOpticalReceiverDaoImpl.java,v1.0 2013-12-2 下午4:48:57 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.optical.dao.CmcOpticalReceiverDao;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverChannelNum;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverSwitchCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author dosion
 * @created @2013-12-2-下午4:48:57
 * 
 */
@Repository("cmcOpticalReceiverDao")
public class CmcOpticalReceiverDaoImpl extends MyBatisDaoSupport<Object> implements CmcOpticalReceiverDao {
    @Override
    protected String getDomainName() {
        return this.getClass().getName();
    }

    @Override
    public CmcOpReceiverRfCfg selectOpReceiverRfCfg(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectOpReceiverRfCfg", cmcId);
    }

    @Override
    public CmcOpReceiverSwitchCfg selectOpReceiverSwitchCfg(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectOpReceiverSwitchCfg", cmcId);
    }

    @Override
    public List<CmcOpReceiverInputInfo> selectOpReceiverInputInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectOpReceiverInputInfo", cmcId);
    }

    @Override
    public CmcOpReceiverAcPower selectOpReceiverAcPower(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectOpReceiverAcPower", cmcId);
    }

    @Override
    public List<CmcOpReceiverDcPower> selectOpReceiverDcPowerList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectOpReceiverDcPower", cmcId);
    }

    @Override
    public CmcOpReceiverDcPower selectOpReceiverDcPower(Long cmcId, Integer powerIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("powerIndex", powerIndex);
        return getSqlSession().selectOne(getNameSpace() + "selectOpReceiverAcPowerByMap", map);
    }

    @Override
    public void insertOpReceiverRfCfg(Long cmcId, CmcOpReceiverRfCfg cmcOpReceiverRfCfg) {
        cmcOpReceiverRfCfg.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverRfCfg", cmcOpReceiverRfCfg);
    }

    @Override
    public void updateOpReceiverRfCfg(Long cmcId, CmcOpReceiverRfCfg cmcOpReceiverRfCfg) {
        cmcOpReceiverRfCfg.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverRfCfg", cmcOpReceiverRfCfg);
    }

    @Override
    public void updateOpReceiverSwitchCfg(Long cmcId, CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg) {
        cmcOpReceiverSwitchCfg.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverSwitchCfg", cmcOpReceiverSwitchCfg);
    }

    @Override
    public void updateOpReceiverInputInfo(Long cmcId, CmcOpReceiverInputInfo cmcOpReceiverInputInfo) {
        cmcOpReceiverInputInfo.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverInputInfo", cmcOpReceiverInputInfo);
    }

    @Override
    public void updateOpReceiverAcPower(Long cmcId, CmcOpReceiverAcPower cmcOpReceiverAcPower) {
        cmcOpReceiverAcPower.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverAcPower", cmcOpReceiverAcPower);
    }

    @Override
    public void updateOpReceiverDcPower(Long cmcId, CmcOpReceiverDcPower cmcOpReceiverDcPower) {
        cmcOpReceiverDcPower.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverDcPower", cmcOpReceiverDcPower);
    }

    @Override
    public void insertOpReceiverSwitchCfg(Long cmcId, CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg) {
        cmcOpReceiverSwitchCfg.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverSwitchCfg", cmcOpReceiverSwitchCfg);
    }

    @Override
    public void insertOpReceiverInputInfo(Long cmcId, CmcOpReceiverInputInfo cmcOpReceiverInputInfo) {
        cmcOpReceiverInputInfo.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverInputInfo", cmcOpReceiverInputInfo);
    }

    @Override
    public void insertOpReceiverAcPower(Long cmcId, CmcOpReceiverAcPower cmcOpReceiverAcPower) {
        cmcOpReceiverAcPower.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverAcPower", cmcOpReceiverAcPower);
    }

    @Override
    public void insertOpReceiverDcPower(Long cmcId, CmcOpReceiverDcPower cmcOpReceiverDcPower) {
        cmcOpReceiverDcPower.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverDcPower", cmcOpReceiverDcPower);
    }

    @Override
    public void insertOpReceiverInputInfoHis(Long cmcId, CmcOpReceiverInputInfo cmcOpReceiverInputInfo) {
        cmcOpReceiverInputInfo.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverInputInfoHis", cmcOpReceiverInputInfo);
    }

    @Override
    public List<Point> selectOpReceiverInputInfoHis(Long cmcId, Long index, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("inputIndex", index);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "selectOpReceiverInputInfoHis", map);
    }

    @Override
    public void insertOpReceiverRfPort(Long cmcId, CmcOpReceiverRfPort cmcOpReceiverRfPort) {
        cmcOpReceiverRfPort.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverRfPort", cmcOpReceiverRfPort);
    }

    @Override
    public void updateOpReceiverRfPort(Long cmcId, CmcOpReceiverRfPort cmcOpReceiverRfPort) {
        cmcOpReceiverRfPort.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverRfPort", cmcOpReceiverRfPort);
    }

    @Override
    public List<CmcOpReceiverRfPort> selectOpReceiverRfPortList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectOpReceiverRfPortList", cmcId);
    }

    @Override
    public void insertOpReceiverChannelNum(Long cmcId, CmcOpReceiverChannelNum cmcOpReceiverChannelNum) {
        cmcOpReceiverChannelNum.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverChannelNum", cmcOpReceiverChannelNum);
    }

    @Override
    public void updateOpReceiverChannelNum(Long cmcId, CmcOpReceiverChannelNum cmcOpReceiverChannelNum) {
        cmcOpReceiverChannelNum.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverChannelNum", cmcOpReceiverChannelNum);
    }

    @Override
    public CmcOpReceiverChannelNum selectOpReceiverChannelNum(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectOpReceiverChannelNum", cmcId);
    }

    @Override
    public CmcOpReceiverType selectOpReceiverType(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectOpReceiverType", cmcId);
    }

    @Override
    public void insertOpReceiverType(Long cmcId, CmcOpReceiverType opType) {
        opType.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertOpReceiverType", opType);
    }

    @Override
    public void updateOpReceiverType(Long cmcId, CmcOpReceiverType opType) {
        opType.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateOpReceiverType", opType);
    }
}
