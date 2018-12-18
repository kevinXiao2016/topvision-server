/***********************************************************************
 * $Id: CmtsSignalQualityScheduler.java,v1.0 2014-4-16 上午11:10:32 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmts.performance.domain.CmtsSignalQualityPerf;
import com.topvision.ems.cmts.performance.engine.CmtsPerfDao;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2014-4-16-上午11:10:32
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmtsSignalQualityScheduler")
public class CmtsSignalQualityScheduler extends AbstractExecScheduler<CmtsSignalQualityPerf> {

    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmtsSignalQualityScheduler entityId[" + operClass.getEntityId() + "] exec start.");
        }
        // Add by Rod 增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            // long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            long cmcId = operClass.getEntityId();
            List<CmcSignalQuality> cmtsSignalQualities = new ArrayList<CmcSignalQuality>();
            //Modify by flackyang@20160725为解决大C信道可能会变化的问题，改为直接每次采集时在engine端获取信道信息
            CmtsPerfDao perfDao = engineDaoFactory.getEngineDao(CmtsPerfDao.class);
            List<Long> channelIndexs = new ArrayList<Long>();
            List<Long> upChannelIndexs = perfDao.getUpChannelIndex(cmcId);
            List<Long> downChannelIndexs = perfDao.getDownChannelIndex(cmcId);
            channelIndexs.addAll(upChannelIndexs);
            channelIndexs.addAll(downChannelIndexs);
            try {
                Timestamp collectTime = new Timestamp(System.currentTimeMillis());
                for (Long channelIndex : channelIndexs) {
                    CmcSignalQuality quality = new CmcSignalQuality();
                    quality.setCmcId(cmcId);
                    quality.setCmcChannelIndex(channelIndex);
                    quality.setCollectTime(collectTime);
                    cmtsSignalQualities.add(snmpExecutorService.getTableLine(snmpParam, quality));
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("CmtsServiceQualityPertResult write result to file.");
            CmcSignalQualityPerfResult cmcSignalQualityPerfResult = new CmcSignalQualityPerfResult(operClass);
            cmcSignalQualityPerfResult.setEntityId(cmcId);
            cmcSignalQualityPerfResult.setPerfs(cmtsSignalQualities);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(cmcSignalQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcServiceQualityPertResult exec end.");
    }
}
