package com.topvision.ems.cmts.performance.engine.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.performance.facade.CmtsFlowQuality;
import com.topvision.ems.cmts.facade.domain.UplinkSpeedPerf;
import com.topvision.ems.cmts.performance.domain.CmtsServiceQualityResult;
import com.topvision.ems.cmts.performance.domain.IfUtilization;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStatic;
import com.topvision.ems.cmts.performance.engine.CmtsPerfDao;
import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * 
 * @author YangYi
 * @created @2013-10-10-上午10:42:44
 * 
 */
public class CmtsPerfDaoImpl extends EngineDaoSupport<Point> implements CmtsPerfDao {
    @Override
    public List<Point> selectCmtsSnrPoints(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectCmtsSnrPoints"), paramMap);
    }

    @Override
    public List<Point> selectCmtsCcerPoints(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectCmtsCcerPoints"), paramMap);
    }

    @Override
    public List<Point> selectCmtsUcerPoints(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectCmtsUcerPoints"), paramMap);
    }

    @Override
    public List<Point> selectCmtsChannelSpeedPoints(Long entityId, Long channelIndex, String perfTarget,
            String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectCmtsChannelSpeedPoints"), paramMap);
    }

    @Override
    public List<Point> selectCmtsUpLinkSpeedPoints(Long entityId, Long channelIndex, String perfTarget,
            String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.UPLINK_IN_FLOW.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace("selectCmtsLinkInSpeedPoints"), paramMap);
        } else {
            return getSqlSession().selectList(getNameSpace("selectCmtsLinkOutSpeedPoints"), paramMap);
        }

    }

    @Override
    public List<Point> selectCmtsPortUsedPoints(Long entityId, Long channelIndex, String perfTarget, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectCmtsPortUsedPoints"), paramMap);
    }

    @Override
    public void updateCmtsPortStatus(long entityId, UplinkSpeedPerf uplinkSpeedPerf) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ifIndex", uplinkSpeedPerf.getIfIndex());
        map.put("ifAdminStatus", uplinkSpeedPerf.getIfAdminStatus());
        map.put("ifOperStatus", uplinkSpeedPerf.getIfOperStatus());
        getSqlSession().update(getNameSpace("updateCmtsPortStatus"), map);
    }

    @Override
    public void recordIfSpeedStatic(UplinkSpeedStatic uplinkSpeedStatic) {
        UplinkSpeedStatic speedStatic = (UplinkSpeedStatic) getSqlSession().selectOne(
                getNameSpace("selectLastUplinkSpeedStatic"), uplinkSpeedStatic);
        if (speedStatic != null) {
            getSqlSession().update(getNameSpace("updateLastUplinkSpeedStatic"), uplinkSpeedStatic);
        } else {
            getSqlSession().insert(getNameSpace("insertLastUplinkSpeedStatic"), uplinkSpeedStatic);
        }
        getSqlSession().insert(getNameSpace("insertHisUplinkSpeedStatic"), uplinkSpeedStatic);
    }

    @Override
    public void recordIfUtilization(IfUtilization ifUtilization) {
        IfUtilization cUtilization = (IfUtilization) getSqlSession().selectOne(
                getNameSpace("selectLastUplinkUtilization"), ifUtilization);
        if (cUtilization != null) {
            getSqlSession().update(getNameSpace("updateLastUplinkUtilization"), ifUtilization);
        } else {
            getSqlSession().insert(getNameSpace("insertLastUplinkUtilization"), ifUtilization);
        }
        getSqlSession().insert(getNameSpace("insertHisUplinkUtilization"), ifUtilization);
    }

    @Override
    public void updateIfSpeed(long entityId, long ifIndex, long ifSpeed) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ifIndex", ifIndex);
        map.put("ifSpeed", ifSpeed);
        getSqlSession().update(getNameSpace("updateCmtsIfSpeed"), map);
    }

    @Override
    public List<Point> queryCmtsRelayPerfPoints(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmtsRelayPerfPoints", paramMap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmts.domain.CmtsPerf";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsPerfDao#getCmtsPerformanceMonitorId(java.lang.Long, java.lang.String)
     */
    @Override
    public List<Integer> getCmtsPerformanceMonitorId(Long entityId, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("category", category);
        List<Integer> result = getSqlSession().selectList(getNameSpace() + "getCmtsPerformanceMonitorId", map);
        return result;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsPerfDao#insertCmtsSignalQuality(java.lang.Long, java.lang.String, com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult)
     */
    @Override
    public void insertCmtsSignalQuality(Long entityId, String targetName, CmcSignalQualityPerfResult result) {
        List<CmcSignalQuality> list = result.getPerfs();
        if (PerfTargetConstants.CMC_SNR.equals(targetName)) {
            Map<String, Object> perfMap = new HashMap<String, Object>();
            for (CmcSignalQuality quality : list) {
                perfMap.put("cmcId", entityId);
                perfMap.put("channelIndex", quality.getCmcChannelIndex());
                perfMap.put("collectValue", quality.getDocsIfSigQSignalNoise());
                perfMap.put("collectTime", quality.getCollectTime());
                getSqlSession().insert(getNameSpace() + "insertCmtsSnrQuality", perfMap);
            }
        } else if (PerfTargetConstants.CMC_BER.equals(targetName)) {
            Map<String, Object> perfMapCcer = new HashMap<String, Object>();
            for (CmcSignalQuality quality : list) {
                if (quality.getSigQCorrectedRate() == null || quality.getSigQUnerroredRate() == null) {
                    continue;
                }
                perfMapCcer.put("cmcId", entityId);
                perfMapCcer.put("channelIndex", quality.getCmcChannelIndex());
                perfMapCcer.put("ccerCode", quality.getSigQCorrecteds());
                perfMapCcer.put("ucerCode", quality.getSigQUncorrectables());
                perfMapCcer.put("ccerRate", quality.getSigQCorrectedRate());
                perfMapCcer.put("ucerRate", quality.getSigQUnerroredRate());
                perfMapCcer.put("noerCode", quality.getSigQUnerroreds());
                perfMapCcer.put("noerRate", quality.getSigQUnerroredRate());
                perfMapCcer.put("collectTime", quality.getCollectTime());
                getSqlSession().insert(getNameSpace() + "insertCmtsErrorCodeQuality", perfMapCcer);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsPerfDao#insertCmtsFlowQuality(java.util.List)
     */
    @Override
    public void insertCmtsFlowQuality(List<CmtsFlowQuality> flowQualities) {
        if (flowQualities == null || flowQualities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (CmtsFlowQuality flowQuality : flowQualities) {
                if (flowQuality.getIfUtilization() != null) {
                    sqlSession.insert(getNameSpace() + "insertCmtsFlowQuality", flowQuality);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertCmtsCpuQuality(CmtsServiceQualityResult serviceResult) {
        this.getSqlSession().insert(getNameSpace("insertCmtsCpuQuality"), serviceResult);
    }

    @Override
    public void insertCmtsMemQuality(CmtsServiceQualityResult serviceResult) {
        this.getSqlSession().insert(getNameSpace("insertCmtsMemQuality"), serviceResult);
    }

    @Override
    public List<Point> queryCmtsCpuData(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return this.getSqlSession().selectList(getNameSpace("queryCmtsCpuData"), paramMap);
    }

    @Override
    public List<Point> queryCmtsMemData(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return this.getSqlSession().selectList(getNameSpace("queryCmtsMemData"), paramMap);
    }

    @Override
    public List<Long> getUpLinkIndex(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("selectUpLinkIndex"), entityId);
    }

    @Override
    public List<Long> getUpChannelIndex(Long cmcId) {
        return this.getSqlSession().selectList(getNameSpace("selectUpChannelIndex"), cmcId);
    }

    @Override
    public List<Long> getDownChannelIndex(Long cmcId) {
        return this.getSqlSession().selectList(getNameSpace("selectDownChannelIndex"), cmcId);
    }
}
