/***********************************************************************
 * $Id: PnmpPollEngineDaoImpl.java,v1.0 2017-8-15 下午2:32:02 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.dao.mybatis;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.topvision.ems.cm.pnmp.engine.dao.PnmpPollEngineDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCalculationResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.engine.dao.EngineDaoSupport;

/**
 * @author jay
 * @created @2017-8-15-下午2:32:02
 *
 */
public class PnmpPollEngineDaoImpl extends EngineDaoSupport<PnmpCalculationResult> implements PnmpPollEngineDao {

    // targetName-thresholdName-lowValue/highValue
    private ConcurrentHashMap<String, Double> thresholdMap = new ConcurrentHashMap<String, Double>();

    private static Long lastCacheTime = null;

    private static final long TIMEOUT = 600000L;
    private static final String MTR = "mtr";
    private static final String MTR_BAD_HIGHVALUE = "mtr_bad_highValue";
    private static final String MTR_MARGINAL_HIGHVALUE = "mtr_marginal_highValue";
    private static final Double MTR_BAD_HIGHVALUE_DEFAULT = 18d;
    private static final Double MTR_BAD_MARGINAL_DEFAULT = 25d;

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.pnmp.facade.domain.PnmpCalculationResult";
    }

    @Override
    public Integer getMtrLevel(Double mtr) {
        if (mtr == null) {
            return PnmpCalculationResult.NULL;
        }
        if (checkIfNeedCache()) {
            resetCache();
        }
        Double mtr_bad_highValue = thresholdMap.get(MTR_BAD_HIGHVALUE);
        Double mtr_marginal_highValue = thresholdMap.get(MTR_MARGINAL_HIGHVALUE);
        // 判断是否为bad
        if (mtr < mtr_bad_highValue) {
            return PnmpCalculationResult.BAD;
        }
        // 判断是否为marginal
        else if (mtr < mtr_marginal_highValue) {
            return PnmpCalculationResult.MARGINAL;
        }
        // 健康
        else {
            return PnmpCalculationResult.HEALTH;
        }
    }

    @Override
    public void insertPnmpCalculationResult(PnmpCalculationResult pnmpCalculationResult) {
        getSqlSession().insert(getNameSpace("insertPnmpCalculationResult"), pnmpCalculationResult);
        getSqlSession().insert(getNameSpace("insertPnmpCalculationResultHis"), pnmpCalculationResult);
    }

    @Override
    public void insertLowPnmpCalculationResult(PnmpCalculationResult pnmpCalculationResult) {
        getSqlSession().insert(getNameSpace("insertLowPnmpCalculationResult"), pnmpCalculationResult);
        getSqlSession().insert(getNameSpace("insertPnmpCalculationResultHis"), pnmpCalculationResult);
    }

    @Override
    public void deleteMiddleMonitorCm(String cmMac) {
        getSqlSession().delete(getNameSpace("deleteMiddleMonitorCm"), cmMac);
    }

    @Override
    public void insertMiddleMonitorCm(PnmpPollResult pnmpPollResult) {
        getSqlSession().insert(getNameSpace("insertMiddleMonitorCm"), pnmpPollResult);
    }

    @Override
    public void deleteHighMonitorCm(String cmMac) {
        getSqlSession().delete(getNameSpace("deleteHighMonitorCm"), cmMac);
    }

    @Override
    public void insertHighMonitorCm(PnmpPollResult pnmpPollResult) {
        getSqlSession().insert(getNameSpace("insertHighMonitorCm"), pnmpPollResult);
    }

    @Override
    public List<PnmpCmData> loadCmDataList(Long cmcId, String cmMac, long from) {
        Map<String,Object> param = new HashMap<>();
        param.put("cmcId",cmcId);
        param.put("cmMac",cmMac);
        param.put("fromTime",new Timestamp(from));
        return getSqlSession().selectList(getNameSpace("selectCmDataList"), param);
    }

    @Override
    public void insertPnmpVariance(PnmpCalculationResult pnmpCalculationResult) {
        getSqlSession().insert(getNameSpace("insertPnmpVariance"), pnmpCalculationResult);
        getSqlSession().insert(getNameSpace("insertPnmpVarianceHis"), pnmpCalculationResult);
    }

    @Override
    public Integer getLastMtrLevel(String cmMac) {
        Integer mtrLevel = getSqlSession().selectOne(getNameSpace("selectCmMtrLevel"), cmMac);
        if (mtrLevel == null) {
            mtrLevel = PnmpCalculationResult.HEALTH;
        }
        return mtrLevel;
    }

    private boolean checkIfNeedCache() {
        if (lastCacheTime == null || !thresholdMap.containsKey(MTR_BAD_HIGHVALUE)) {
            return true;
        }
        return System.currentTimeMillis() - lastCacheTime > TIMEOUT;
    }

    private void resetCache() {
        // 从数据库获取，并更新缓存
        try {
            List<PnmpTargetThreshold> thresholds = getSqlSession().selectList(getNameSpace("selectMtrThresholds"));
            for (PnmpTargetThreshold threshold : thresholds) {
                if (MTR.equals(threshold.getTargetName()) && "bad".equals(threshold.getThresholdName())) {
                    thresholdMap.put(MTR_BAD_HIGHVALUE, threshold.getHighValue());
                } else if (MTR.equals(threshold.getTargetName()) && "marginal".equals(threshold.getThresholdName())) {
                    thresholdMap.put(MTR_MARGINAL_HIGHVALUE, threshold.getHighValue());
                }
            }
        } catch (Exception e) {
            logger.error("resetCache error: " + e);
        }
        if (!thresholdMap.containsKey(MTR_BAD_HIGHVALUE)) {
            thresholdMap.put(MTR_BAD_HIGHVALUE, MTR_BAD_HIGHVALUE_DEFAULT);
        }
        if (!thresholdMap.containsKey(MTR_BAD_HIGHVALUE)) {
            thresholdMap.put(MTR_MARGINAL_HIGHVALUE, MTR_BAD_MARGINAL_DEFAULT);
        }
        lastCacheTime = System.currentTimeMillis();
    }

}
