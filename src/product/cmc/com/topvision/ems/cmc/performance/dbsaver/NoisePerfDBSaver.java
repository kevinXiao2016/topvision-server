/***********************************************************************
 * $ NoisePerfDBSaver.java,v1.0 2012-5-3 15:29:54 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.NoisePerfResult;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.handle.NoiseHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2012-5-3-15:29:54
 * 
 * @modify by Rod 性能采集优化与重构
 */
@Engine("noisePerfDBSaver")
public class NoisePerfDBSaver extends NbiSupportEngineSaver implements PerfEngineSaver<NoisePerfResult, OperClass> {
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    // mark by bravin@20150116:
    // CC和CMTS整合后都在此类处理，但是handler还在各自的包中,所以此处如此处理，而不是直接引用CmtsNoiseHandle.CMTS_NOISE_FLAG
    public static String CMTS_NOISE_FLAG = "CMTS_NOISE";

    @Override
    public void save(NoisePerfResult noisePerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("NoisePerfDBSaver identifyKey[" + noisePerfResult.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        getCallback(PerformanceCallback.class).updateScheduleModifyTime(noisePerfResult.getCmcId(),
                noisePerfResult.getDomain().getCategory());
        // TODO Victor临时解决办法
        // Entity entity = entityDao.selectByPrimaryKey(noisePerfResult.getCmcId());
        // boolean isCmts = entityTypeService.isCmts(entity.getTypeId());
        boolean isCmts = getCallback(PerformanceCallback.class).isCmts(noisePerfResult.getCmcId());
        if (noisePerfResult != null) {
            for (Long ifIndex : noisePerfResult.getNoises().keySet()) {
                Integer noise = noisePerfResult.getNoises().get(ifIndex);
                SingleNoise singleNoise = new SingleNoise();
                singleNoise.setEntityId(noisePerfResult.getCmcId());
                singleNoise.setCmcId(noisePerfResult.getCmcId());
                singleNoise.setIfIndex(ifIndex);
                singleNoise.setDt(new Timestamp(noisePerfResult.getDt()));
                if (noise < 0) {
                    singleNoise.setNoise(0);
                } else {
                    singleNoise.setNoise(noise);
                }
                cmcPerfDao.recordNoise(singleNoise);
                if (isCmts) {
                    PerformanceData data = new PerformanceData(singleNoise.getEntityId(), CMTS_NOISE_FLAG, singleNoise);
                    getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
                } else {
                    PerformanceData data = new PerformanceData(singleNoise.getEntityId(), NoiseHandle.NOISE_FLAG,
                            singleNoise);
                    getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
                }
            }
        }
    }
}
