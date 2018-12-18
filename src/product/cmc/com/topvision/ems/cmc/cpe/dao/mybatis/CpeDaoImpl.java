/***********************************************************************
 * $Id: CmCpeImpl.java,v1.0 2013-10-17 下午3:59:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cpe.dao.CpeDao;
import com.topvision.ems.cmc.domain.Area;
import com.topvision.ems.cmc.domain.CmCmcRunningInfo;
import com.topvision.ems.cmc.domain.CmCpeNumInArea;
import com.topvision.ems.cmc.domain.CmOltRunningInfo;
import com.topvision.ems.cmc.domain.CmcCmReatimeNum;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.domain.CmcUserNumHisPerf;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.performance.domain.CmCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author haojie
 * @created @2013-10-17-下午3:59:59
 * 
 */
@Repository("cpeDao")
public class CpeDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CpeDao {

    @Override
    public List<Area> selectAllAreas() {
        return getSqlSession().selectList(getNameSpace("selectAllAreas"));
    }

    @Override
    public List<Entity> selectEntityWithIp(Long type) {
        return getSqlSession().selectList(getNameSpace("selectEntityWithIp"), type);
    }

    @Override
    public List<CmcEntity> selectCcmstWithOutAgent(Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        map.put("type", type);
        return getSqlSession().selectList(getNameSpace("selectCcmstWithOutAgent"), map);
    }

    @Override
    public List<CmcUserNumHisPerf> selectAllUserNumHis(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectAllUserNumHis"), queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> selectUserNumHisByArea(Long regionId) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByArea"), regionId);
    }

    @Override
    public List<CmcUserNumHisPerf> selectUserNumHisByDevice(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByDevice"), queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> selectUserNumHisByCmc(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByCmc"), queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> selectUserNumHisByChannel(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByChannel"), queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> selectUserNumHisByCmcWithOutAgent(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByCmcWithOutAgent"), queryMap);
    }

    @Override
    public List<CmCpeNumInArea> selectCmCpeNumInRegion() {
        return getSqlSession().selectList(getNameSpace("selectCmCpeNumInRegion"));
    }

    @Override
    public List<CmNum> selectAllAllDeviceCmNum(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectAllAllDeviceCmNum"), map);
    }

    @Override
    public CmcEntityRelation selectCmcByIndexAndEntityId(Long cmcIndex, Long entityId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("cmcIndex", cmcIndex);
        return getSqlSession().selectOne(getNameSpace("selectCmcByIndexAndEntityId"), map);
    }

    @Override
    public List<CmcCmReatimeNum> selectCcmtsCmNumInfo(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectCcmtsCmNumInfo"), entityId);
    }

    @Override
    public List<Point> selectCpuHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCpuHisPerf"), queryMap);
    }

    @Override
    public List<CmCpeAttribute> queryCmCpeListByCondition(String cmMac, String cpeMac, String cpeIp, String cmLocation) {
        Map<String, String> map = new HashMap<String, String>();
        if (cmMac != null && !"".equalsIgnoreCase(cmMac)) {
            String formatQueryCmMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryCmMac.indexOf(":") == -1) {
                map.put("cmMacWithoutSplit", formatQueryCmMac);
            }
            map.put("statusMacAddress", formatQueryCmMac);
        }
        if (cpeMac != null && !"".equalsIgnoreCase(cpeMac)) {
            String formatQueryCpeMac = MacUtils.formatQueryMac(cpeMac);
            if (formatQueryCpeMac.indexOf(":") == -1) {
                map.put("cpeMacWithoutSplit", formatQueryCpeMac);
            }
            map.put("topCmCpeMacAddress", formatQueryCpeMac);
        }
        if (cpeIp != null && !"".equalsIgnoreCase(cpeIp)) {
            map.put("topCmCpeIpAddress", cpeIp);
        }
        if (cmLocation != null && !"".equalsIgnoreCase(cmLocation)) {
            map.put("cmAlias", cmLocation);
        }
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryCmCpeListByCondition"), map);
    }

    @Override
    public List<CmCpeAttribute> queryCmCpeListByConditionHis(String cmMac, Long cpeMacLong, Long cpeIpLong, String cmLocation) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (cmMac != null && !"".equalsIgnoreCase(cmMac)) {
            String formatQueryCmMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryCmMac.indexOf(":") == -1) {
                map.put("cmMacWithoutSplit", formatQueryCmMac);
            }
            map.put("statusMacAddress", formatQueryCmMac);
        }
        if (cpeMacLong !=0 ) {
            map.put("cpeMacLong", cpeMacLong);
        }
        if (cpeIpLong != 0) {
            map.put("cpeIpLong", cpeIpLong);
        }
        if (cmLocation != null && !"".equalsIgnoreCase(cmLocation)) {
            map.put("cmAlias", cmLocation);
        }
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryCmCpeListByConditionHis"), map);
    }

    @Override
    public List<Point> selectSnrHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectSnrHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectUpChannelFlowHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUpChannelFlowHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectDownChannelFlowHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectDownChannelFlowHisPerf"), queryMap);
    }
    
	@Override
	public List<Point> selectCmtsDownChannelFlowHisPerf(Map<String, Object> queryMap) {
		return getSqlSession().selectList(getNameSpace("selectCmtsDownChannelFlowHisPerf"), queryMap);
	}

	@Override
    public CmCmcRunningInfo selectCmCmcRunningInfo(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmCmcRunningInfo"), cmId);
    }

    @Override
    public CmCmcRunningInfo selectCmCmtsRunningInfo(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmCmtsRunningInfo"), cmId);
    }

    @Override
    public List<Point> selectBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectBitErrorRateHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectCmtsBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCmtsBitErrorRateHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectUnBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUnBitErrorRateHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectCmtsUnBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCmtsUnBitErrorRateHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectCmOnlineNumHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCmOnlineNumHisPerf"), queryMap);
    }

    @Override
    public List<Point> selectCmOfflineNumHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCmOfflineNumHisPerf"), queryMap);
    }

    @Override
    public CmOltRunningInfo selectCmOltRunningInfo(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmOltRunningInfo"), cmId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cpe.dao.CpeDao#selectCmtsSnrHisPerf(java.util.Map)
     */
    public List<Point> selectCmtsSnrHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCmtsSnrHisPerf"), queryMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cpe.dao.CpeDao#selectCmtsUpChannelFlowHisPerf(java.util.Map)
     */
    public List<Point> selectCmtsUpChannelFlowHisPerf(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectCmtsUpChannelFlowHisPerf"), queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByCmcNew(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByCmcNew"), queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByPon(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectUserNumHisByPon"), queryMap);
    }

    @Override
    public Long getPonIndexByPonId(Long ponId) {
        return getSqlSession().selectOne(getNameSpace("selectPonIndexByPonId"), ponId);
    }

    @Override
    public Long getCmcIndexByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("selectCmcIndexByCmcId"), cmcId);
    }

    @Override
    public List<Long> getAllOnlineIds() {
        return getSqlSession().selectList(getNameSpace("getAllOnlineIds"));
    }

    @Override
    public List<CmCpe> getCpeListByCmId(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getCpeListByCmId"), cmId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cpe.domain.CmCpe";
    }

}
