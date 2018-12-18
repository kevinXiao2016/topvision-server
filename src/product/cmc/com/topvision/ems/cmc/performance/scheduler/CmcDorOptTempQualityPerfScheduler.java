package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.CmcDorOptTempQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcDorOptTempQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcDorOptTempQuality;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcDorOptTempQualityPerfScheduler")
public class CmcDorOptTempQualityPerfScheduler extends AbstractExecScheduler<CmcDorOptTempQualityPerf> {

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("cmcDorOptTempQualityPerfScheduler entityId[" + operClass.getCmcId() + "] exec start.");
        }
        // 增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback().recordTaskCollectTime(operClass.getMonitorId(),
                        new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            CmcDorOptTempQuality cmcDorOptTempQuality = new CmcDorOptTempQuality();
            cmcDorOptTempQuality.setCmcId(operClass.getCmcId());
            cmcDorOptTempQuality.setCmcIndex(operClass.getCmcIndex());
            cmcDorOptTempQuality.setDorOptTempIndex(operClass.getCmcIndex() + 2);
            cmcDorOptTempQuality.setCollectTime(new Timestamp(System.currentTimeMillis()));
            try {
                cmcDorOptTempQuality = snmpExecutorService.getTableLine(snmpParam, cmcDorOptTempQuality);
            } catch (Exception e) {
                logger.debug("execute CmcDorOptTempQuality getTableLine error", e);
            }
            logger.trace("CmcDorOptTempQualityPertResult write result to file.");
            CmcDorOptTempQualityPerfResult cmcDorOptTempQualityPerfResult = new CmcDorOptTempQualityPerfResult(
                    operClass);
            cmcDorOptTempQualityPerfResult.setEntityId(operClass.getCmcId());
            cmcDorOptTempQualityPerfResult.setPerf(cmcDorOptTempQuality);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcDorOptTempQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("cmcDorOptTempQualityPerfScheduler execute end. entityId[" + operClass.getCmcId() + "]");
        }
    }

}
