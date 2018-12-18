package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.CmFlapMonitor;
import com.topvision.ems.cmc.performance.domain.FlapResult;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("flapScheduler")
public class FlapScheduler extends AbstractExecScheduler<CmFlapMonitor> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("FlapScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
        }
        // 增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 采集时间
            long now = System.currentTimeMillis();

            if (logger.isDebugEnabled()) {
                logger.debug("FlapScheduler collectStartTime[" + sdf.format(now) + "]");
            }
            long entityId = operClass.getEntityId();

            // 其他功能的采集器的代码中都有下面的获取snmpParam的方法，测试看看效果
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());

            FlapResult cmFlapResult = new FlapResult(operClass);
            try {
                cmFlapResult.setCmFlapList(snmpExecutorService.getTable(snmpParam, CmFlap.class));
            } catch (Exception e) {
                logger.error("FlapScheduler error:", e);
            }

            now = System.currentTimeMillis();

            if (logger.isDebugEnabled()) {
                logger.debug("FlapScheduler collectFinishTime[" + sdf.format(now) + "]");
            }

            cmFlapResult.setEntityId(entityId);
            cmFlapResult.setCollectFinishTime(now);
            logger.debug("FlapScheduler write result to file.");
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmFlapResult);
        } catch (Exception e) {
            logger.error("FlapScheduler error:", e);
        }
        logger.debug("FlapScheduler exec end.");
    }
}
