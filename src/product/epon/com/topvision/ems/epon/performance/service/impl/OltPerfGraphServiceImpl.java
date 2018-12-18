/***********************************************************************
 * $Id: EponStatsService.java,v1.0 2013-10-25 下午3:46:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPortInfo;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.performance.dao.OltPerfGraphDao;
import com.topvision.ems.epon.performance.service.OltPerfGraphService;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author flack
 * @created @2013-10-25-下午3:46:42
 *
 */
@Service("oltPerfGraphService")
public class OltPerfGraphServiceImpl extends BaseService implements OltPerfGraphService {

    @Autowired
    private OltPerfGraphDao oltPerfGraphDao;

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryOltSlotListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltSlotAttribute> queryOltSlotListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltSlotListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryOltFanListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltFanAttribute> queryOltFanListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltFanListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryOltSniListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltSniAttribute> queryOltSniListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltSniListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryOltPonListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltPonAttribute> queryOltPonListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltPonListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryOltOnuPonListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltOnuPonAttribute> queryOltOnuPonListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltOnuPonListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryUniListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltUniAttribute> queryUniListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltUniListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryUniListByEntityId(java.lang.Long)
     */
    @Override
    public List<OltPortInfo> queryEponPortListByEntityId(Long entityId) {
        return oltPerfGraphDao.selectOltFirberListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryOltServicePerfPoints(java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public List<Point> queryOltServicePerfPoints(Long entityId, Long slotIndex, String perfTarget, String startTime,
            String endTime) {
        if (PerfTargetConstants.OLT_CPUUSED.equals(perfTarget)) {
            return oltPerfGraphDao.selectOltCpuUsedPoints(entityId, slotIndex, startTime, endTime);
        }
        if (PerfTargetConstants.OLT_MEMUSED.equals(perfTarget)) {
            return oltPerfGraphDao.selectOltMemUsedPoints(entityId, slotIndex, startTime, endTime);
        }
        if (PerfTargetConstants.OLT_FLASHUSED.equals(perfTarget)) {
            return oltPerfGraphDao.selectOltFlashUsedPoints(entityId, slotIndex, startTime, endTime);
        }
        if (PerfTargetConstants.OLT_BOARDTEMP.equals(perfTarget)) {
            return oltPerfGraphDao.selectOltBoardTempPoints(entityId, slotIndex, startTime, endTime);
        }
        if (PerfTargetConstants.OLT_FANSPEED.equals(perfTarget)) {
            return oltPerfGraphDao.selectOltFanSpeedPoints(entityId, slotIndex, startTime, endTime);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryFirberPortPerfPoints(java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public List<Point> queryOltOptLinkPerfPoints(Long entityId, Long slotIndex, String perfTarget, String startTime,
            String endTime) {
        List<Point> pList = oltPerfGraphDao.selectOltOptLinkPerfPoints(entityId, slotIndex, perfTarget, startTime,
                endTime);
        for (Point p : pList) {
            if (PerfTargetConstants.OPT_VOLTAGE.equals(perfTarget)) {
                Double data = Double.parseDouble(NumberUtils.TWODOT_FORMAT.format((p.getY() / NumberUtils.K10)));
                p.setY(data);
            }
        }
        return pList;
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltPerfGraphService#queryPortFlowPerfPoints(java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public List<Point> queryPortFlowPerfPoints(Long entityId, Long slotIndex, String perfTarget, String startTime,
            String endTime, Integer direction) {
        List<Point> pList = oltPerfGraphDao.selectPortFlowPerfPoints(entityId, slotIndex, perfTarget, startTime,
                endTime, direction);
        for (Point p : pList) {
            if (PerfTargetConstants.OLT_SNIUSED.equals(perfTarget)
                    || PerfTargetConstants.OLT_PONUSED.equals(perfTarget)) {
                Double data = 0D;
                if (p.getY() == -1 || p.getY() == null) {
                    data = -1D;
                } else {
                    data = Double.parseDouble(NumberUtils.TWODOT_FORMAT.format((p.getY() * 100)));
                }
                p.setY(data);
            }
        }
        return pList;
    }

    public OltPerfGraphDao getOltPerfGraphDao() {
        return oltPerfGraphDao;
    }

    public void setOltPerfGraphDao(OltPerfGraphDao oltPerfGraphDao) {
        this.oltPerfGraphDao = oltPerfGraphDao;
    }

    @Override
    public List<OltUniAttribute> getOltUniList(Map<String, Object> map) {
        return oltPerfGraphDao.queryOltUniList(map);
    }

    @Override
    public int getOltUniCount(Map<String, Object> map) {
        return oltPerfGraphDao.queryOltUniCount(map);
    }

    @Override
    public List<OltOnuPonAttribute> getOltOnuPonList(Map<String, Object> map) {
        return oltPerfGraphDao.queryOltOnuPonList(map);
    }

    @Override
    public int getOltOntPonCount(Map<String, Object> map) {
        return oltPerfGraphDao.queryOltOntPonCount(map);
    }

    @Override
    public List<Point> queryOltRelayPerfPoints(Long entityId, String startTime, String endTime) {
        return oltPerfGraphDao.queryOltRelayPerfPoints(entityId, startTime, endTime);
    }

}
