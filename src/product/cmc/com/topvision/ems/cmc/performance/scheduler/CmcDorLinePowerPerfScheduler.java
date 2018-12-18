package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.CmcDorLinePowerQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcDorLinePowerQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcDorLinePowerQuality;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcDorLinePowerPerfScheduler")
public class CmcDorLinePowerPerfScheduler extends AbstractExecScheduler<CmcDorLinePowerQualityPerf> {

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcDorLinePowerPerfScheduler entityId[" + operClass.getCmcId() + "] exec start.");
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
            CmcDorLinePowerQuality cmcDorLinePowerQuality = new CmcDorLinePowerQuality();
            cmcDorLinePowerQuality.setCmcId(operClass.getCmcId());
            cmcDorLinePowerQuality.setCmcIndex(operClass.getCmcIndex());
            cmcDorLinePowerQuality.setDorLinePowerIndex(operClass.getCmcIndex() + 2);
            cmcDorLinePowerQuality.setCollectTime(new Timestamp(System.currentTimeMillis()));
            try {
                cmcDorLinePowerQuality = snmpExecutorService.getTableLine(snmpParam, cmcDorLinePowerQuality);
            } catch (Exception e) {
                logger.debug("execute CmcDorLinePowerQuality getTableLine error", e);
            }
            logger.trace("CmcDorLinePowerQualityPertResult write result to file.");
            CmcDorLinePowerQualityPerfResult cmcDorLinePowerQualityPerfResult = new CmcDorLinePowerQualityPerfResult(
                    operClass);
            cmcDorLinePowerQualityPerfResult.setEntityId(operClass.getCmcId());
            cmcDorLinePowerQualityPerfResult.setPerf(cmcDorLinePowerQuality);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcDorLinePowerQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("CmcDorLinePowerPerfScheduler execute end.entityId[" + operClass.getCmcId() + "]");
        }
    }

}
