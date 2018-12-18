package com.topvision.ems.cmc.spectrum.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.cmc.spectrum.domain.Heartbeat;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumHeartbeatService;
import com.topvision.framework.scheduler.AbstractJob;

public class HeartBeatJob extends AbstractJob {
    @Override
    public void doJob(JobExecutionContext context) throws JobExecutionException {
        Integer heartbeatTimeout = (Integer) jobDataMap.get("heartbeatTimeout");
        SpectrumCallbackService1S spectrumCallbackService1S = (SpectrumCallbackService1S) jobDataMap
                .get("spectrumCallbackService1S");
        SpectrumHeartbeatService spectrumHeartbeatService = (SpectrumHeartbeatService) jobDataMap
                .get("spectrumHeartbeatService");
        SpectrumConfigService spectrumConfigService = (SpectrumConfigService) jobDataMap.get("spectrumConfigService");
        Long maxTimeout = spectrumConfigService.getTimeLimit();
        Long now = System.currentTimeMillis();
        List<Heartbeat> heartbeatList = new ArrayList<>(spectrumHeartbeatService.getHeartbeatList());
        try {
            for (Iterator<Heartbeat> heartbeatIterator = heartbeatList.iterator(); heartbeatIterator.hasNext();) {
                Heartbeat heartbeat = heartbeatIterator.next();
                logger.debug("HeartBeatJob doJob, checking cmc Id = " + heartbeat.getCmcId() + ", callback id = " + heartbeat.getCallbackId());
                if (heartbeat.getType().equals(Heartbeat.WEB)) {
                    // 心跳队列中是否有超过最大限制时长的心跳实例，删除掉这些实例
                    if ((now - heartbeat.getCreateTime()) >= maxTimeout) {
                        try {
                            //发送查看超时消息给界面
                            spectrumCallbackService1S.sendOverTimeMessage(heartbeat.getCmcId());
                            spectrumHeartbeatService.delWebHeartBeat(heartbeat.getCallbackId());
                        } catch (Exception e) {
                            logger.debug("",e);
                        }
                    }
                }
                //超过时间没有心跳包过滤的需需要从心跳队列中删除
                if (heartbeat.getType().equals(Heartbeat.WEB) || heartbeat.getType().equals(Heartbeat.MOBILE)) {
                    if ((now - heartbeat.getHeartbeatTime()) >= heartbeatTimeout) {
                        try {
                            //发送查看超时消息给界面
                            spectrumCallbackService1S.sendHeartbeatTimeOut(heartbeat.getCmcId());
                            spectrumHeartbeatService.delWebHeartBeat(heartbeat.getCallbackId());
                        } catch (Exception e) {
                            logger.debug("",e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("HeartBeatJob error", e);
        }
    }
}
