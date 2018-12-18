/***********************************************************************
 * $Id: CmcDaoImpl.java,v1.0 2011-10-25 下午04:33:11 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.engine.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.domain.PortalChannelUtilizationShow;
import com.topvision.ems.cmc.facade.domain.CmcPortMonitorDomain;
import com.topvision.ems.cmc.perf.domain.CmcBerQuality;
import com.topvision.ems.cmc.perf.domain.CmcSnrQuality;
import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStatic;
import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.ems.cmc.performance.domain.CmcDorLinePowerQualityPerfResult;
import com.topvision.ems.cmc.performance.domain.CmcDorOptTempQualityPerfResult;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerfResult;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.performance.facade.CmcCmNumber;
import com.topvision.ems.cmc.performance.facade.CmcDorLinePowerQuality;
import com.topvision.ems.cmc.performance.facade.CmcDorOptTempQuality;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.cmc.performance.facade.CmcLinkQualityFor8800A;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author loyal
 * @created @2011-10-25-下午04:33:11
 * 
 */
public class CmcPerfDaoImpl extends EngineDaoSupport<ChannelCmNum> implements CmcPerfDao {
    private static final String STR_CPU = "cpu";
    private static final String STR_MEM = "mem";
    private static final String STR_UP = "up";
    private static final String STR_DOWN = "down";

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.perf.domain.CmcPerf";
    }

    @Override
    public List<Cmc> getNetworkCcmtsDeviceLoadingTop(String item) {
        if (STR_CPU.equals(item)) {
            return super.getSqlSession().selectList(getNameSpace() + "getDeviceCcmtsCpuLoadingTop");
        } else if (STR_MEM.equals(item)) {
            return super.getSqlSession().selectList(getNameSpace() + "getDeviceCcmtsMemLoadingTop");
        }

        return null;
    }

    @Override
    public void recordNoise(SingleNoise singleNoise) {
        SingleNoise sn = (SingleNoise) getSqlSession().selectOne(getNameSpace() + "selectLastNoise", singleNoise);
        if (sn != null) {
            getSqlSession().update(getNameSpace() + "updateLastNoise", singleNoise);
        } else {
            getSqlSession().insert(getNameSpace() + "insertLastNoise", singleNoise);
        }
        getSqlSession().insert(getNameSpace() + "insertHisNoise", singleNoise);
    }

    @Override
    public void recordChannelUtilization(ChannelUtilization channelUtilization) {
        ChannelUtilization cUtilization = (ChannelUtilization) getSqlSession().selectOne(
                getNameSpace() + "selectLastChannelUtilization", channelUtilization);
        if (cUtilization != null) {
            getSqlSession().update(getNameSpace() + "updateLastChannelUtilization", channelUtilization);
        } else {
            getSqlSession().insert(getNameSpace() + "insertLastChannelUtilization", channelUtilization);
        }
        getSqlSession().insert(getNameSpace() + "insertHisChannelUtilization", channelUtilization);
    }

    @Override
    public Long getSNRMonitorId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getSNRMonitorId", cmcId);
    }

    @Override
    public List<Integer> getIfIndexByCmcId(Long cmcId, Integer type) {
        if (type == 129) {
            return getSqlSession().selectList(getNameSpace() + "getUpChannelIfIndexByCmcId", cmcId);
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cmcId", cmcId);
            map.put("ifType", type);
            return getSqlSession().selectList(getNameSpace() + "getIfIndexByCmcId", map);
        }
    }

    @Override
    public List<SingleNoise> getSnrData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd) {
        List<SingleNoise> list;
        map.put("timeStart", timeStart);
        map.put("timeEnd", timeEnd);
        list = getSqlSession().selectList(getNameSpace() + "getSnrDataBetweenStartAndEnd", map);
        map.remove("timeStart");
        map.remove("timeEnd");
        return list;
    }

    @Override
    public List<SingleNoise> getSnrData(Map<String, Object> map, Integer size) {
        long start = (Long) getSqlSession().selectOne(getNameSpace() + "getSnrDataCount", map);
        if (start < size) {
            start = 0;
        } else {
            start -= size;
        }
        RowBounds r = new RowBounds((int) start, size);
        return getSqlSession().selectList(getNameSpace() + "getSnrDataBySize", map, r);
    }

    @Override
    public Integer getSnrPeriod(Long cmcId) {
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getSNRPeriod", cmcId);
    }

    @Override
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, final Timestamp timeStart,
            final Timestamp timeEnd) {
        map.put("timeStart", timeStart);
        map.put("timeEnd", timeEnd);
        List<ChannelUtilization> list = getSqlSession().selectList(
                getNameSpace() + "getUtilizationDataBetweenStartAndEnd", map);
        map.remove("timeStart");
        map.remove("timeEnd");
        return list;
    }

    @Override
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Integer size) {
        long start = (Long) getSqlSession().selectOne(getNameSpace() + "getUtilizationDataCount", map);
        if (start < size) {
            start = 0;
        } else {
            start -= size;
        }
        RowBounds r = new RowBounds((int) start, size);
        return getSqlSession().selectList(getNameSpace() + "getUtilizationDataBySize", map, r);
    }

    @Override
    public void recordCcmtsSystemInfo(CmcAttribute cmcAttribute) {
        // 更新CCMTS的属性表
        if (getSqlSession().selectOne(getNameSpace() + "getCmcAttributeByMacLong", cmcAttribute) != null) {
            getSqlSession().insert(getNameSpace() + "updateCmcAttribute", cmcAttribute);
        }
        // 将CCMTS的CPU利用率、内存利用率插入历史记录中
        getSqlSession().insert(getNameSpace() + "insertSystemHis", cmcAttribute);
    }

    @Override
    public Long getMonitorId(Long cmcId, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("category", category);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getMonitorId", map);
    }

    @Override
    public Integer getCcmtsSystemPeriod(Long cmcId, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("category", category);
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getPeriod", map);
    }

    @Override
    public List<PortalChannelUtilizationShow> getNetworkCcmtsDeviceLoadingTop(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getTopChnlUtiliLoading", map);
    }

    @Override
    @Deprecated
    public void recordChannelCmNum(ChannelCmNum channelCmNum) {
        ChannelCmNum cmNum = (ChannelCmNum) getSqlSession().selectOne(getNameSpace() + "selectLastChannelCmNum",
                channelCmNum);
        if (cmNum != null) {
            getSqlSession().update(getNameSpace() + "updateLastChannelCmNum", channelCmNum);
        } else {
            getSqlSession().insert(getNameSpace() + "insertLastChannelCmNum", channelCmNum);
        }
        getSqlSession().insert(getNameSpace() + "insertHisChannelCmNum", channelCmNum);
    }

    @Override
    public Long getChannelCmNumMonitorId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getChannelCmNumMonitorId", cmcId);
    }

    @Override
    public Integer getChannelCmPeriod(Long cmcId) {
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getChannelCmPeriod", cmcId);
    }

    @Override
    public Integer getUsBitErrorRatePeriod(Long cmcId) {
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getUsBitErrorRatePeriod", cmcId);
    }

    @Override
    public Long getUsBitErrorRateMonitorId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getUsBitErrorRateMonitorId", cmcId);
    }

    @Override
    public void recordUsBitErrorRate(UsBitErrorRate usBitErrorRate) {
        UsBitErrorRate bitErrorRate = (UsBitErrorRate) getSqlSession().selectOne(
                getNameSpace() + "selectLastUsBitErrorRate", usBitErrorRate);
        if (bitErrorRate != null) {
            getSqlSession().update(getNameSpace() + "updateLastUsBitErrorRate", usBitErrorRate);
        } else {
            getSqlSession().insert(getNameSpace() + "insertLastUsBitErrorRate", usBitErrorRate);
        }
        getSqlSession().insert(getNameSpace() + "insertHisUsBitErrorRate", usBitErrorRate);
    }

    @Override
    public Integer getChannelSpeedStaticPeriod(Long cmcId) {
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getChannelSpeedStaticPeriod", cmcId);
    }

    @Override
    public Long getChannelSpeedStaticMonitorId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getChannelSpeedStaticMonitorId", cmcId);
    }

    @Override
    public void recordChannelSpeedStatic(ChannelSpeedStatic channelSpeedStatic) {
        ChannelSpeedStatic speedStatic = (ChannelSpeedStatic) getSqlSession().selectOne(
                getNameSpace() + "selectLastChannelSpeedStatic", channelSpeedStatic);
        if (speedStatic != null) {
            getSqlSession().update(getNameSpace() + "updateLastChannelSpeedStatic", channelSpeedStatic);
        } else {
            getSqlSession().insert(getNameSpace() + "insertLastChannelSpeedStatic", channelSpeedStatic);
        }
        getSqlSession().insert(getNameSpace() + "insertHisChannelSpeedStatic", channelSpeedStatic);
    }

    @Override
    public ChannelSpeedStatic getLastChannelSpeedStatic(ChannelSpeedStatic channelSpeedStatic) {
        return (ChannelSpeedStatic) getSqlSession().selectOne(getNameSpace() + "selectLastChannelSpeedStatic",
                channelSpeedStatic);
    }

    @Override
    public List<Point> noiseRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectNoiseHisPerf", viewerParam);
    }

    @Override
    public List<Point> cpuRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCpuHisPerf", viewerParam);
    }

    @Override
    public List<Point> memRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectMemHisPerf", viewerParam);
    }

    @Override
    public Long getCmStatusMonitorId(Long entityId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmStatusMonitorId", entityId);
    }

    @Override
    public List<UsBitErrorRate> getTopPortletErrorCodesLoading(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getTopPortletErrorCodesLoading", paramMap);
    }

    @Override
    public List<UsBitErrorRate> getChannelBerRate(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getChannelBerRateList", paramMap);
    }

    @Override
    public Integer getChannelBerRateCount(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "getChannelBerRateListCount", paramMap);
    }

    @Override
    public UsBitErrorRate getErrorCodesByPortId(Long cmcId, Long portIndex, String targetName) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("portIndex", portIndex);
        if (PerfTargetConstants.CCER.equals(targetName)) {
            return (UsBitErrorRate) getSqlSession().selectOne(getNameSpace() + "getErrorCodesByPortId", paramMap);
        } else {
            return (UsBitErrorRate) getSqlSession().selectOne(getNameSpace() + "getUnErrorCodesByPortId", paramMap);
        }
    }

    @Override
    public List<SingleNoise> getTopLowNoiseLoading(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getTopLowNoiseLoading", paramMap);
    }

    @Override
    public List<ChannelCmNum> getNetworkCcmtsDeviceUsersLoadingTop(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Integer channelType = 0;
        if (STR_UP.equals(map.get("target"))) {
            channelType = 0;
            map.put("channelType", channelType);
            return getSqlSession().selectList(getNameSpace() + "getCmTotalUsersNum", map);
        } else if (STR_DOWN.equals(map.get("target"))) {
            channelType = 1;
            map.put("channelType", channelType);
            return getSqlSession().selectList(getNameSpace() + "getCmTotalUsersNum", map);
        } else {
            // 设备级别TOP10
            return getSqlSession().selectList(getNameSpace() + "getDevCmTotalUsersNum", map);
        }
    }

    @Override
    public List<ChannelCmNum> getCcmtsDeviceUsersList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Integer channelType = 0;
        if (STR_UP.equals(map.get("target"))) {
            channelType = 0;
            map.put("channelType", channelType);
            return getSqlSession().selectList(getNameSpace() + "getCmTotalUsersNum", map);
        } else if (STR_DOWN.equals(map.get("target"))) {
            channelType = 1;
            map.put("channelType", channelType);
            return getSqlSession().selectList(getNameSpace() + "getCmTotalUsersNum", map);
        } else {
            return getSqlSession().selectList(getNameSpace() + "getDevCmTotalUsersNum", map);
        }
    }

    @Override
    public Integer getCcmtsDeviceUsersCount(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Integer channelType = 0;
        if (STR_UP.equals(map.get("target"))) {
            channelType = 0;
            map.put("channelType", channelType);
            return getSqlSession().selectOne(getNameSpace() + "getCmTotalUsersCount", map);
        } else if (STR_DOWN.equals(map.get("target"))) {
            channelType = 1;
            map.put("channelType", channelType);
            return getSqlSession().selectOne(getNameSpace() + "getCmTotalUsersCount", map);
        } else {
            return getSqlSession().selectOne(getNameSpace() + "getDevCmTotalUsersCount", map);
        }
    }

    @Override
    public List<Point> bitErrorRateRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectBitErrorRateHisPerf", viewerParam);
    }

    @Override
    public List<Point> unBitErrorRateRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectUnBitErrorRateHisPerf", viewerParam);
    }

    @Override
    public List<Point> cmNumTotalRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCmNumTotalHisPerf", viewerParam);
    }

    @Override
    public List<Point> cmNumOnlineRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCmNumOnlineHisPerf", viewerParam);
    }

    @Override
    public List<Point> cmNumActiveRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCmNumActiveHisPerf", viewerParam);
    }

    @Override
    public List<Point> cmNumUnRegisteredRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCmNumUnregisteredHisPerf", viewerParam);
    }

    @Override
    public List<Point> cmNumOfflineRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCmNumOfflineHisPerf", viewerParam);
    }

    @Override
    public List<Point> cmNumRegisteredRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectCmNumRegisteredHisPerf", viewerParam);
    }

    @Override
    public List<Point> channelUtilizationRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectChannelUtilizationHisPerf", viewerParam);
    }

    @Override
    public List<Point> usSpeedRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectUsSpeedHisPerf", viewerParam);
    }

    @Override
    public List<Point> dsSpeedRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectDsSpeedHisPerf", viewerParam);
    }

    @Override
    public List<ChannelSpeedStatic> getLastCmcChannelSpeedStaticList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getLastCmcChannelSpeedStaticList", cmcId);
    }

    @Override
    public CmcCmNumStatic getCmcCmNumStatic(Long cmcId) {
        return (CmcCmNumStatic) getSqlSession().selectOne(getNameSpace() + "getCmcCmNumStatic", cmcId);
    }

    @Override
    public void updateCmc8800BSnap(EntityValueEvent event) {
        @SuppressWarnings("unused")
        EntitySnap entitySnap = (EntitySnap) getSqlSession().selectOne(getNameSpace() + "selectSnapByEntityId", event);
    }

    @Override
    public void changeCmStatusOffine(long cmcId) {
    	//EMS-15662  EMS-取消OLT或者CC离线（ICMP不通）导致的其下属的所有信道和CM自动置下线的机制
        //getSqlSession().update(getNameSpace() + "changeCmStatusOffine", cmcId);
    }

    @Override
    public boolean isExistCmc(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "isExistCmc", cmcId) != null;
    }

    @Override
    public Long getTopoEntityIdByCmcId(long cmcId, Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("type", type);
        Object obj = getSqlSession().selectOne(getNameSpace() + "getTopoEntityIdByCmcId", map);
        if (obj == null) {
            return null;
        } else {
            return (Long) obj;
        }
    }

    @Override
    public void updateIfSpeed(Long cmcId, Long ifIndex, Long ifSpeed) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("ifIndex", ifIndex);
        map.put("ifSpeed", ifSpeed);
        getSqlSession().update(getNameSpace() + "updateCcmtsIfSpeed", map);
    }

    @Override
    public void updateCcmtsPortStatus(Long cmcId, CmcPortMonitorDomain cmcPortMonitorDomain) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("ifIndex", cmcPortMonitorDomain.getIfIndex());
        map.put("ifAdminStatus", cmcPortMonitorDomain.getIfAdminStatus());
        map.put("ifOperStatus", cmcPortMonitorDomain.getIfOperStatus());
        getSqlSession().update(getNameSpace() + "updateCcmtsPortStatus", map);
    }

    @Override
    public void updateCcmtsPortStatus(final Long cmcId, final List<CmcFlowQuality> flowQualitys) {
        SqlSession sqlSession = getBatchSession();
        try {

            for (CmcFlowQuality flowQuality : flowQualitys) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", cmcId);
                map.put("ifIndex", flowQuality.getIfIndex());
                map.put("ifAdminStatus", flowQuality.getIfAdminStatus());
                map.put("ifOperStatus", flowQuality.getIfOperStatus());
                sqlSession.update(getNameSpace() + "updateCcmtsPortStatus", map);
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
    public Long getCpeStatusMonitorId(Long entityId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCpeStatusMonitorId", entityId);
    }

    @Override
    public void changeCmc8800BStatus(Long cmcId, Boolean status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        if (status) {
            map.put("status", CmcConstants.TOPCCMTSSYSSTATUS_ONLINE);
            map.put("ifOperStatus", CmcConstants.IFOPERSTATUS_UP);
        } else {
            map.put("status", CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE);
            map.put("ifOperStatus", CmcConstants.IFOPERSTATUS_DOWN);
        }

        getSqlSession().update(getNameSpace() + "updateCcmtsSysStatus", map);
        getSqlSession().update(getNameSpace() + "updateCcmtsPortStatus", map);
    }

    @Override
    public List<SingleNoise> selectNoiseRate(String name, Long deviceType, String sort, String dir, int start, int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        if ("name".equals(sort)) {
            params.put("sort", "d.name");
        } else {
            params.put("sort", sort);
        }
        params.put("name", name);
        params.put("deviceType", deviceType);
        params.put("dir", dir);
        params.put("start", start);
        params.put("limit", limit);
        return getSqlSession().selectList(getNameSpace() + "selectNoiseRate", params);
    }

    @Override
    public Long selectNoiseRateCount(String name, Long deviceType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        params.put("name", name);
        params.put("deviceType", deviceType);
        return (Long) getSqlSession().selectOne(getNameSpace() + "selectNoiseRateCount", params);
    }

    @Override
    public List<PortalChannelUtilizationShow> selectChannelUsed(String name, Long deviceType, String sort, String dir,
            int start, int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        if ("name".equals(sort)) {
            params.put("sort", "C.name");
        } else {
            params.put("sort", sort);
        }
        params.put("name", name);
        params.put("deviceType", deviceType);
        params.put("dir", dir);
        params.put("start", start);
        params.put("limit", limit);
        return getSqlSession().selectList(getNameSpace() + "selectChannelUsed", params);
    }

    @Override
    public Long selectChannelUsedCount(String name, Long deviceType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        params.put("name", name);
        params.put("deviceType", deviceType);
        return (Long) getSqlSession().selectOne(getNameSpace() + "selectChannelUsedCount", params);
    }

    @Override
    public void insertCmcServiceQuality(Long cmcId, String targetName, CmcServiceQualityPerfResult result) {
        CmcServiceQuality perf = result.getPerf();
        Map<String, Object> perfMap = new HashMap<String, Object>();
        if (PerfTargetConstants.CMC_CPUUSED.equals(targetName)) {
            perfMap.clear();
            perfMap.put("cmcId", cmcId);
            perfMap.put("collectValue", perf.getTopCcmtsSysCPURatio());
            perfMap.put("collectTime", perf.getCollectTime());
            getSqlSession().insert(getNameSpace() + "insertCmcCpuQuality", perfMap);
        } else if (PerfTargetConstants.CMC_FLASHUSED.equals(targetName)) {
            perfMap.clear();
            perfMap.put("cmcId", cmcId);
            perfMap.put("collectValue", perf.getTopCcmtsSysFlashRatio());
            perfMap.put("collectTime", perf.getCollectTime());
            getSqlSession().insert(getNameSpace() + "insertCmcFlashQuality", perfMap);
        } else if (PerfTargetConstants.CMC_MEMUSED.equals(targetName)) {
            perfMap.clear();
            perfMap.put("cmcId", cmcId);
            perfMap.put("collectValue", perf.getTopCcmtsSysRAMRatio());
            perfMap.put("collectTime", perf.getCollectTime());
            getSqlSession().insert(getNameSpace() + "insertCmcMemQuality", perfMap);
        }
    }

    @Override
    public List<Integer> getCmcPerformanceMonitorId(Long cmcId, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("category", category);
        List<Integer> result = getSqlSession().selectList(getNameSpace() + "getCmcPerformanceMonitorId", map);
        return result;
    }

    @Override
    public void insertCmcSignalQuality(Long cmcId, String targetName, CmcSignalQualityPerfResult result) {
        List<CmcSignalQuality> list = result.getPerfs();
        if (PerfTargetConstants.CMC_SNR.equals(targetName)) {
            List<CmcSnrQuality> snrQualites = new ArrayList<CmcSnrQuality>();

            for (CmcSignalQuality quality : list) {
                CmcSnrQuality snrQuality = new CmcSnrQuality();
                snrQuality.setCmcId(cmcId);
                snrQuality.setChannelIndex(quality.getCmcChannelIndex());
                snrQuality.setCollectValue(quality.getDocsIfSigQSignalNoise());
                snrQuality.setCollectTime(quality.getCollectTime());
                snrQualites.add(snrQuality);

                //                perfMap.put("cmcId", cmcId);
                //                perfMap.put("channelIndex", quality.getCmcChannelIndex());
                //                perfMap.put("collectValue", quality.getDocsIfSigQSignalNoise());
                //                perfMap.put("collectTime", quality.getCollectTime());
                //                getSqlSession().insert(getNameSpace() + "insertCmcSnrQuality", perfMap);
                //                getSqlSession().update(getNameSpace() + "syncCmcSnrQuality", snrQuality);
                //                //插入汇总表haojie
                //                getSqlSession().insert(getNameSpace() + "insertNoiseSummary", perfMap);
            }
            if (snrQualites.size() > 0) {
                getSqlSession().insert(getNameSpace() + "batchInsertCmcSnrQuality", snrQualites);
                //getSqlSession().update(getNameSpace() + "batchSyncCmcSnrQuality", snrQualites);
                getSqlSession().insert(getNameSpace() + "batchInsertNoiseSummary", snrQualites);
            }
        } else if (PerfTargetConstants.CMC_BER.equals(targetName)) {
            List<CmcBerQuality> cmcBerQualities = new ArrayList<CmcBerQuality>();
            for (CmcSignalQuality quality : list) {
                if (quality.getSigQCorrectedRate() == null || quality.getSigQUnerroredRate() == null) {
                    continue;
                }
                CmcBerQuality cmcBerQuality = new CmcBerQuality();
                cmcBerQuality.setCmcId(cmcId);
                cmcBerQuality.setChannelIndex(quality.getCmcChannelIndex());
                cmcBerQuality.setCcerCode(quality.getSigQCorrecteds());
                cmcBerQuality.setUcerCode(quality.getSigQUncorrectables());
                cmcBerQuality.setCcerRate(quality.getSigQCorrectedRate());
                cmcBerQuality.setUcerRate(quality.getSigQUncorrectedRate());
                cmcBerQuality.setNoerCode(quality.getSigQUnerroreds());
                cmcBerQuality.setNoerRate(quality.getSigQUnerroredRate());
                cmcBerQuality.setCollectTime(quality.getCollectTime());
                cmcBerQualities.add(cmcBerQuality);

                //                perfMapCcer.put("cmcId", cmcId);
                //                perfMapCcer.put("channelIndex", quality.getCmcChannelIndex());
                //                perfMapCcer.put("ccerCode", quality.getSigQCorrecteds());
                //                perfMapCcer.put("ucerCode", quality.getSigQUncorrectables());
                //                perfMapCcer.put("ccerRate", quality.getSigQCorrectedRate());
                //                perfMapCcer.put("ucerRate", quality.getSigQUncorrectedRate());
                //                perfMapCcer.put("noerCode", quality.getSigQUnerroreds());
                //                perfMapCcer.put("noerRate", quality.getSigQUnerroredRate());
                //                perfMapCcer.put("collectTime", quality.getCollectTime());
                //                getSqlSession().insert(getNameSpace() + "insertCmcErrorCodeQuality", perfMapCcer);
            }
            if (cmcBerQualities.size() > 0) {
                getSqlSession().insert(getNameSpace() + "batchInsertCmcErrorCodeQuality", cmcBerQualities);
            }
        }
    }

    @Override
    public void insertCmcLinkQuality(final List<CmcLinkQualityData> cmcLinkQualityPerfs) {
        if (cmcLinkQualityPerfs == null || cmcLinkQualityPerfs.size() == 0) {
            return;
        }
        getSqlSession().insert(getNameSpace("batchInsertCmcLinkQuality"), cmcLinkQualityPerfs);
        //        SqlSession sqlSession = getBatchSession();
        //        try {
        //            for (CmcLinkQualityData linkQuality : cmcLinkQualityPerfs) {
        //                sqlSession.insert(getNameSpace() + "insertCmcLinkQuality", linkQuality);
        //            }
        //            sqlSession.commit();
        //        } catch (Exception e) {
        //            logger.error("", e);
        //            sqlSession.rollback();
        //        } finally {
        //            sqlSession.close();
        //        }
    }

    @Override
    public void insertCmcCmNumberQuality(final List<CmcCmNumber> cmNumbers) {
        if (cmNumbers == null || cmNumbers.size() == 0) {
            return;
        }
        getSqlSession().insert(getNameSpace("batchInsertCmcCmNumberQuality"), cmNumbers);
        //        SqlSession sqlSession = getBatchSession();
        //        try {
        //
        //            for (CmcCmNumber cmNumber : cmNumbers) {
        //                sqlSession.insert(getNameSpace() + "insertCmcCmNumberQuality", cmNumber);
        //            }
        //            sqlSession.commit();
        //        } catch (Exception e) {
        //            logger.error("", e);
        //            sqlSession.rollback();
        //        } finally {
        //            sqlSession.close();
        //        }
    }

    @Override
    public void insertCmcFlowQuality(final List<CmcFlowQuality> flowQualities) {
        if (flowQualities == null || flowQualities.size() == 0) {
            return;
        }
        getSqlSession().insert(getNameSpace("batchInsertCmcFlowQuality"), flowQualities);

        //        SqlSession sqlSession = getBatchSession();
        //        try {
        //            for (CmcFlowQuality flowQuality : flowQualities) {
        //                if (flowQuality.getIfUtilization() != null) {
        //                    sqlSession.insert(getNameSpace() + "insertCmcFlowQuality", flowQuality);
        //                }
        //            }
        //            sqlSession.commit();
        //        } catch (Exception e) {
        //            logger.error("", e);
        //            sqlSession.rollback();
        //        } finally {
        //            sqlSession.close();
        //        }
    }

    @Override
    public void insertCmcTempQuality(final List<CmcTempQualityFor8800B> tempQualityFor8800Bs) {
        if (tempQualityFor8800Bs == null || tempQualityFor8800Bs.size() == 0) {
            return;
        }
        getSqlSession().insert(getNameSpace("batchInsertCmcTempQuality"), tempQualityFor8800Bs);

        //        SqlSession sqlSession = getBatchSession();
        //        try {
        //
        //            for (CmcTempQualityFor8800B tmp : tempQualityFor8800Bs) {
        //                sqlSession.insert(getNameSpace() + "insertCmcTempQuality", tmp);
        //            }
        //            sqlSession.commit();
        //        } catch (Exception e) {
        //            logger.error("", e);
        //            sqlSession.rollback();
        //        } finally {
        //            sqlSession.close();
        //        }
    }

    @Override
    public void updateCmcStatus(CmcServiceQuality cmcServiceQuality) {
        getSqlSession().update(getNameSpace() + "updateCmcStatus", cmcServiceQuality);
    }

    @Override
    public Long getSpectrumMonitorId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getSpectrumMonitorId", cmcId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<ScheduleMessage> getAllSpectrumMonitorId() {
        return getSqlSession().selectList(getNameSpace() + "getAllSpectrumMonitor");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfDao#getTopPortletFlapInsGrowthLoading()
     */
    @Override
    public List<CmFlap> getTopPortletFlapInsGrowthLoading(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getTopPortletFlapInsGrowthLoading", paramMap);
    }

    @Override
    public List<CmFlap> getCmFlapIns(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getCmFlapInsList", paramMap);
    }

    @Override
    public Integer getCmFlapInsCount(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getCmFlapInsCount", paramMap).size();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.dao.CmcPerfDao#syncOnuPonOptical(com.topvision.ems.cmc.performance
     * .facade.CmcLinkQualityFor8800A)
     */
    @Override
    public void syncOnuPonOptical(CmcLinkQualityFor8800A cmcLinkQualityFor8800A) {
        getSqlSession().update(getNameSpace() + "syncOnuPonOptical", cmcLinkQualityFor8800A);
    }

    @Override
    public List<Point> ponCmNumTotalRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectponCmNumTotalHisPerf", viewerParam);
    }

    @Override
    public List<Point> ponCmNumOnlineRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectponCmNumOnlineHisPerf", viewerParam);
    }

    @Override
    public List<Point> ponCmNumActiveRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectponCmNumActiveHisPerf", viewerParam);
    }

    @Override
    public List<Point> ponCmNumOfflineRead(ViewerParam viewerParam) {
        return getSqlSession().selectList(getNameSpace() + "selectponCmNumOfflineHisPerf", viewerParam);
    }

    @Override
    public ChannelCmNum getChannelCmNum(Long cmcPortId) {
        ChannelCmNum channelCmNum = getSqlSession().selectOne(getNameSpace() + "getChannelCmNumParam", cmcPortId);
        return getSqlSession().selectOne(getNameSpace() + "getChannelCmNum", channelCmNum);
    }

    public List<CmcLinkQualityData> queryCmcOpticalInfo(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectList(getNameSpace("queryCmcOpticalInfo"), paramMap);
    }

    @Override
    public int queryCmcOpticalNum(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("queryCmcOpticalNum"), paramMap);
    }

    @Override
    public List<CmcPort> selectCmcportList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcPortList", entityId);
    }

    @Override
    public void insertCmcDorOptTempQuality(CmcDorOptTempQualityPerfResult result) {
        CmcDorOptTempQuality perf = result.getPerf();
        getSqlSession().insert(getNameSpace() + "insertCmcDorOptTempQuality", perf);
    }

    @Override
    public void insertCmcDorLinePowerQuality(CmcDorLinePowerQualityPerfResult result) {
        CmcDorLinePowerQuality perf = result.getPerf();
        getSqlSession().insert(getNameSpace() + "insertCmcDorLinePowerQuality", perf);
    }

}
