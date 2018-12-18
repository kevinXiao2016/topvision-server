/***********************************************************************
 * $Id: HisVideoJob.java,v1.0 2014-3-5 下午7:57:30 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.job;

import com.topvision.ems.cmc.spectrum.service.SpectrumHeartbeatService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.impl.HistoryCallback;
import com.topvision.ems.performance.job.MonitorJob;

/**
 * @author YangYi
 * @created @2014-3-5-下午7:57:30
 * 
 */
public class HisVideoJob extends MonitorJob {
    private BeanFactory beanFactory;
    private SpectrumConfigService spectrumConfigService;
    private SpectrumHeartbeatService spectrumHeartbeatService;
    private Long callbackId;
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        logger.debug("Spectrum HisVideoJob start");
        try {
            beanFactory = (BeanFactory) jobDataMap.get("beanFactory");
            spectrumConfigService = (SpectrumConfigService) beanFactory
                    .getBean("spectrumConfigService");
            spectrumHeartbeatService = (SpectrumHeartbeatService) beanFactory
                    .getBean("spectrumHeartbeatService");
            Long cmcId = monitor.getEntityId();
            logger.debug("Spectrum HisVideoJob, cmcId = " + cmcId);
            Long duration = spectrumConfigService.getHisCollectDuration();// 采集持续时间 (例如15分钟)
            Long now = System.currentTimeMillis(); // 开始时间
            Long end = now + duration; // 结束时间
            callbackId = startHisVideo(cmcId);
            if (callbackId != -1) {
                while (now < end) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                    now = System.currentTimeMillis();
                }
                stopHisVideo(callbackId);
            }
        } catch (Exception e) {
            logger.debug("Spectrum HisVideoJob exception", e);
        }
        logger.debug("Spectrum HisVideoJob end");
    }

    private Long startHisVideo(Long cmcId) {
        HistoryCallback historyCallback = (HistoryCallback) beanFactory.getBean("historyCallback");
        historyCallback.setCmcId(cmcId);
        return spectrumHeartbeatService.addHeartbeat(cmcId, historyCallback);
    }

    private void stopHisVideo(Long callbackId) {
        spectrumHeartbeatService.delHeartbeat(callbackId);
    }
}
