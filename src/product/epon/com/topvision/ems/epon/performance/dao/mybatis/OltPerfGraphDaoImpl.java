/***********************************************************************
 * $Id: OltPerfGraphDaoImpl.java,v1.0 2013-8-6 下午03:11:21 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPortInfo;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.performance.dao.OltPerfGraphDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author lizongtian
 * @created @2013-8-6-下午03:11:21
 * 
 * @Mybatis Modify by Rod @2013-10-25
 *
 */

@Repository("oltPerfGraphDao")
public class OltPerfGraphDaoImpl extends MyBatisDaoSupport<Entity> implements OltPerfGraphDao {

    @Override
    public List<OltSlotAttribute> selectOltSlotListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltSlotListByEntityId", entityId);
    }

    @Override
    public List<OltFanAttribute> selectOltFanListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltFanListByEntityId", entityId);
    }

    @Override
    public List<OltSniAttribute> selectOltSniListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltSniListByEntityId", entityId);
    }

    @Override
    public List<OltPonAttribute> selectOltPonListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltPonListByEntityId", entityId);
    }

    @Override
    public List<OltOnuPonAttribute> selectOltOnuPonListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltOnuPonListByEntityId", entityId);
    }

    @Override
    public List<OltUniAttribute> selectOltUniListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltUniListByEntityId", entityId);
    }

    @Override
    public List<OltPortInfo> selectOltFirberListByEntityId(Long entityId) {
        List<OltSniAttribute> sniFirberList = getSqlSession().selectList(
                getNameSpace() + "getOltSniFirberListByEntityId", entityId);
        List<OltPonAttribute> ponPortList = getSqlSession().selectList(getNameSpace() + "getOltPonListByEntityId",
                entityId);
        List<OltPortInfo> portList = new ArrayList<OltPortInfo>();
        OltPortInfo portInfo = null;
        for (OltSniAttribute sniAttribute : sniFirberList) {
            portInfo = new OltPortInfo();
            portInfo.setEntityId(sniAttribute.getEntityId());
            portInfo.setSlotId(sniAttribute.getSlotId());
            portInfo.setPortId(sniAttribute.getSniId());
            portInfo.setPortIndex(sniAttribute.getSniIndex());
            portInfo.setPortStatus(sniAttribute.getSniOperationStatus());
            portInfo.setDisplayName(sniAttribute.getSniDisplayName());
            portList.add(portInfo);
        }
        for (OltPonAttribute ponAttribute : ponPortList) {
            portInfo = new OltPortInfo();
            portInfo.setEntityId(ponAttribute.getEntityId());
            portInfo.setSlotId(ponAttribute.getSlotId());
            portInfo.setPortId(ponAttribute.getPonId());
            portInfo.setPortIndex(ponAttribute.getPonIndex());
            portInfo.setPortStatus(ponAttribute.getPonOperationStatus());
            portList.add(portInfo);
        }
        return portList;
    }

    @Override
    public List<Point> selectOltCpuUsedPoints(Long entityId, Long slotIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getOltCpuUsedPerfPoints", paramMap);
    }

    @Override
    public List<Point> selectOltMemUsedPoints(Long entityId, Long slotIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getOltMemUsedPerfPoints", paramMap);
    }

    @Override
    public List<Point> selectOltFlashUsedPoints(Long entityId, Long slotIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getOltFlashUsedPerfPoints", paramMap);
    }

    @Override
    public List<Point> selectOltBoardTempPoints(Long entityId, Long slotIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getOltBoardTempPerfPoints", paramMap);
    }

    @Override
    public List<Point> selectOltFanSpeedPoints(Long entityId, Long slotIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getOltFanSpeedPerfPoints", paramMap);
    }

    @Override
    public List<Point> selectOltOptLinkPerfPoints(Long entityId, Long portIndex, String perfTarget, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("portIndex", portIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Point> plist = new ArrayList<Point>();
        if (PerfTargetConstants.OPT_TXPOWER.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOltOptTxPowerPoints", paramMap);
        } else if (PerfTargetConstants.OPT_RXPOWER.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOltOptRxPowerPoints", paramMap);
        } else if (PerfTargetConstants.OPT_CURRENT.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOltOptCurrentPoints", paramMap);
        } else if (PerfTargetConstants.OPT_VOLTAGE.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOltOptVoltagePoints", paramMap);
        } else if (PerfTargetConstants.OPT_TEMP.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOltOptTempPoints", paramMap);
        }
        return plist;
    }

    @Override
    public List<Point> selectPortFlowPerfPoints(Long entityId, Long portIndex, String perfTarget, String startTime,
            String endTime, Integer direction) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("portIndex", portIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Point> pList = new ArrayList<Point>();
        if (direction == PerfTargetConstants.DIRECTION_IN) {
            if (PerfTargetConstants.OLT_SNIFLOW.equals(perfTarget)
                    || PerfTargetConstants.OLT_PONFLOW.equals(perfTarget)) {
                pList = getSqlSession().selectList(getNameSpace() + "getPortInFlowPerfPoints", paramMap);
            } else {
                pList = getSqlSession().selectList(getNameSpace() + "getPortInUsedPerfPoints", paramMap);
            }
        } else if (direction == PerfTargetConstants.DIRECTION_OUT) {
            if (PerfTargetConstants.OLT_SNIFLOW.equals(perfTarget)
                    || PerfTargetConstants.OLT_PONFLOW.equals(perfTarget)) {
                pList = getSqlSession().selectList(getNameSpace() + "getPortOutFlowPerfPoints", paramMap);
            } else {
                pList = getSqlSession().selectList(getNameSpace() + "getPortOutUsedPerfPoints", paramMap);
            }
        }
        return pList;
    }

    @Override
    public List<Point> queryOltRelayPerfPoints(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getOltRelayPerfPoints", paramMap);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.performance.domain.OltPerfGraph";
    }

    @Override
    public List<OltUniAttribute> queryOltUniList(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("getOltUniList"), map);
    }

    @Override
    public int queryOltUniCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("queryOltUniCount"), map);
    }

    @Override
    public List<OltOnuPonAttribute> queryOltOnuPonList(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("queryOltOnuPonList"), map);
    }

    @Override
    public int queryOltOntPonCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("queryOltOntPonCount"), map);
    }

}
