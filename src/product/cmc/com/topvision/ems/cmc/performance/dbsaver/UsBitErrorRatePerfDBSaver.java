/***********************************************************************
 * $Id: UsBitErrorRatePerfDBSaver.java,v1.0 2012-7-12 下午04:11:37 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRatePerfDomain;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRatePerfResult;
import com.topvision.ems.cmc.performance.handle.UsBitErrorRateHandle;
import com.topvision.ems.cmc.performance.handle.UsBitUnErrorRateHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-12-下午04:11:37
 * 
 * @modify by Rod 性能采集优化与重构
 * 
 */
@Engine("usBitErrorRatePerfDBSaver")
public class UsBitErrorRatePerfDBSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<UsBitErrorRatePerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(UsBitErrorRatePerfDBSaver.class);
    public static String CMTS_USBITERRORRATE_FLAG = "CMTS_ERRORCODE";
    public static String CMTS_UNERRORRATE_FLAG = "CMTS_UNERRORCODE";
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(UsBitErrorRatePerfResult usBitErrorRatePerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("UsBitErrorRatePerfDBSaver identifyKey["
                    + usBitErrorRatePerfResult.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        getCallback(PerformanceCallback.class).updateScheduleModifyTime(usBitErrorRatePerfResult.getCmcId(),
                usBitErrorRatePerfResult.getDomain().getCategory());
        List<UsBitErrorRatePerfDomain> usBitErrorRatePerfDomains = usBitErrorRatePerfResult
                .getUsBitErrorRatePerfDomains();

        //TODO Victor临时解决办法
        //Long type = entityDao.selectByPrimaryKey(usBitErrorRatePerfResult.getCmcId()).getTypeId();
        //boolean isCmts = entityTypeService.isCmts(type);
        boolean isCmts = getCallback(PerformanceCallback.class).isCmts(usBitErrorRatePerfResult.getCmcId());
        if (usBitErrorRatePerfDomains != null) {
            for (UsBitErrorRatePerfDomain usBitErrorRatePerfDomain : usBitErrorRatePerfDomains) {
                UsBitErrorRate usBitErrorRate = new UsBitErrorRate();
                usBitErrorRate.setEntityId(usBitErrorRatePerfResult.getEntityId());
                usBitErrorRate.setCmcId(usBitErrorRatePerfResult.getCmcId());
                usBitErrorRate.setDt(new Timestamp(usBitErrorRatePerfResult.getDt()));
                usBitErrorRate.setChannelIndex(usBitErrorRatePerfDomain.getChannelIndex());
                Integer bitErrorRate = 0;
                Integer unBitErrorRate = 0;
                if (logger.isDebugEnabled()) {
                    logger.debug("QUnerroreds:" + usBitErrorRatePerfDomain.getDocsIfSigQUnerroreds() + ";QCorrecteds:"
                            + usBitErrorRatePerfDomain.getDocsIfSigQCorrecteds() + ";QUncorrectables:"
                            + usBitErrorRatePerfDomain.getDocsIfSigQUncorrectables());
                }
                if (usBitErrorRatePerfDomain.getDocsIfSigQUnerroreds()
                        + usBitErrorRatePerfDomain.getDocsIfSigQCorrecteds()
                        + usBitErrorRatePerfDomain.getDocsIfSigQUncorrectables() > 0) {
                    // docsIfSigQCorrecteds/（docsIfSigQUnerroreds+docsIfSigQCorrecteds+docsIfSigQUncorrectables）可纠错码率
                    bitErrorRate = (int) (usBitErrorRatePerfDomain.getDocsIfSigQCorrecteds() * 100 / (usBitErrorRatePerfDomain
                            .getDocsIfSigQUnerroreds() + usBitErrorRatePerfDomain.getDocsIfSigQCorrecteds() + usBitErrorRatePerfDomain
                            .getDocsIfSigQUncorrectables()));
                    // docsIfSigQUncorrectables/（docsIfSigQUnerroreds+docsIfSigQCorrecteds+docsIfSigQUncorrectables）不可纠错码率
                    unBitErrorRate = (int) (usBitErrorRatePerfDomain.getDocsIfSigQUncorrectables() * 100 / (usBitErrorRatePerfDomain
                            .getDocsIfSigQUnerroreds() + usBitErrorRatePerfDomain.getDocsIfSigQCorrecteds() + usBitErrorRatePerfDomain
                            .getDocsIfSigQUncorrectables()));
                }
                usBitErrorRate.setBitErrorRate(bitErrorRate);
                usBitErrorRate.setUnBitErrorRate(unBitErrorRate);
                redirctPerformanceData(usBitErrorRate, usBitErrorRatePerfResult,
                        usBitErrorRatePerfResult.getEntityId(), usBitErrorRatePerfDomain.getChannelIndex());
                cmcPerfDao.recordUsBitErrorRate(usBitErrorRate);
                // 可纠错数 Send TO 性能处理中心
                if (isCmts) {
                    PerformanceData bitErrorData = new PerformanceData(usBitErrorRate.getEntityId(),
                            CMTS_USBITERRORRATE_FLAG, usBitErrorRate);
                    getCallback(PerformanceCallback.class).sendPerfomaceResult(bitErrorData);
                } else {
                    PerformanceData bitErrorData = new PerformanceData(usBitErrorRate.getEntityId(),
                            UsBitErrorRateHandle.ERRORRATE_FLAG, usBitErrorRate);
                    getCallback(PerformanceCallback.class).sendPerfomaceResult(bitErrorData);
                }
                // 不可纠错数 Send TO 性能处理中心
                if (isCmts) {
                    PerformanceData unBitErrorData = new PerformanceData(usBitErrorRate.getEntityId(),
                            CMTS_UNERRORRATE_FLAG, usBitErrorRate);
                    getCallback(PerformanceCallback.class).sendPerfomaceResult(unBitErrorData);
                } else {
                    PerformanceData unBitErrorData = new PerformanceData(usBitErrorRate.getEntityId(),
                            UsBitUnErrorRateHandle.UN_ERRORRATE_FLAG, usBitErrorRate);
                    getCallback(PerformanceCallback.class).sendPerfomaceResult(unBitErrorData);
                }
            }
        }
        // getCallback(PerformanceCallback.class).sendPerfomaceResult(performanceResult);
    }
}
