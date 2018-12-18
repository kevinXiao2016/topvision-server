/***********************************************************************
 * $ SpectrumRecordingServiceImpl.java,v1.0 2014-1-4 17:07:00 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.sql.Timestamp;

import javax.annotation.Resource;

import com.topvision.ems.cmc.spectrum.service.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.dao.SpectrumRecordingDao;
import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author jay
 * @created @2014-1-4-17:07:00
 */
@Service("spectrumRecordingService")
public class SpectrumRecordingServiceImpl extends CmcBaseCommonService implements SpectrumRecordingService,
        BeanFactoryAware {
    private BeanFactory beanFactory;
    @Resource(name = "spectrumRecordingDao")
    private SpectrumRecordingDao spectrumRecordingDao;
    @Autowired
    private SpectrumHeartbeatService spectrumHeartbeatService;
    @Autowired
    private SpectrumCallbackService1S spectrumCallbackService1S;
    @Resource(name = "spectrumConfigService")
    private SpectrumConfigService spectrumConfigService;
    @Resource(name = "spectrumMonitorService")
    private SpectrumMonitorService spectrumMonitorService;

    @Override
    public RealtimeCallback startRealtimeVideo(Long cmcId, Long startTime, String ip) {
        RealtimeCallback realtimeCallback = (RealtimeCallback) beanFactory.getBean("realtimeCallback");
        realtimeCallback.setCmcId(cmcId);
        realtimeCallback.setUserName(CurrentRequest.getCurrentUser().getUser().getUserName());
        realtimeCallback.setTerminalIp(ip);
        realtimeCallback.setStartTime(startTime);
        realtimeCallback.initRecord();
        spectrumHeartbeatService.addHeartbeat(cmcId, realtimeCallback);
        return realtimeCallback;
    }

    @Override
    public SpectrumVideo stopRealtimeVideo(Long cmcId, Long callbackId) {
        return spectrumHeartbeatService.delHeartbeat(callbackId);
    }

    @Override
    public Boolean startHisVideo(Long cmcId) {
        CmtsSpectrumConfig cmtsSpectrumconfig = spectrumRecordingDao.getCmtsSwitchById(cmcId);
        if (cmtsSpectrumconfig != null && "1".equals(cmtsSpectrumconfig.getHisVideoSwitch())) {
            return true; // 如果已经开启历史录像直接返回Ture;
        }
        Long collectCycle = spectrumConfigService.getHisCollectCycle();// 采集周期
        spectrumMonitorService.addMonitor(cmcId, 0l, collectCycle);// 开启历史采集Monitor
        for (int i = 0; i < 30; i++) { // 搜索1分钟历史频谱是否开启成功
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            if (spectrumCallbackService1S.isHisVideoCallbackExist(cmcId)) {// 搜索到了历史频谱的callback，开启成功
                spectrumRecordingDao.startHisVideo(cmcId); // 改变数据库中历史频谱开关状态
                return true;
            }
        }
        this.stopHisVideo(cmcId);// 未搜索到historyCallback，直接停掉历史采集
        return false;
    }

    @Override
    public Boolean stopHisVideo(Long cmcId) {
        boolean removeSuccess = spectrumMonitorService.removeMonitor(cmcId);
        Long callbackId = spectrumHeartbeatService.takeHisCallbackId(cmcId);
        spectrumHeartbeatService.delHeartbeat(callbackId);
        if (removeSuccess) {
            spectrumRecordingDao.stopHisVideo(cmcId);
        }
        return removeSuccess;
    }

    @Override
    public Long saveVideoToDB(Long cmcId, SpectrumVideo spectrumVideo) {
        SpectrumVideo s = spectrumRecordingDao.generateVideoInfo(cmcId);
        spectrumVideo.setEntityIp(s.getEntityIp());
        spectrumVideo.setEntityType(s.getEntityType());
        spectrumVideo.setOltAlias(s.getOltAlias());
        spectrumVideo.setCmtsAlias(s.getCmtsAlias());
        // 插入步长信息，便于录像播放的时候使用
        Integer timeInterval = spectrumRecordingDao.getTimeInterval(cmcId);
        spectrumVideo.setTimeInterval(timeInterval);
        // 将spectrumVideo存入数据库
        spectrumRecordingDao.addVideoToDB(spectrumVideo);
        // 插入录像与设备的关联关系表
        spectrumRecordingDao.insertVideoCmtsRela(cmcId, spectrumVideo.getVideoId());
        return spectrumVideo.getVideoId();
    }

    @Override
    public void updateVideoEndTime(Long videoId, Timestamp endTime) {
        spectrumRecordingDao.updateVideoEndTime(videoId, endTime);
    }

    @Override
    public void renameVideo(Long videoId, String videoName) {
        spectrumRecordingDao.renameVideo(videoId, videoName);
    }

    @Override
    public SpectrumVideo getVideoById(Long videoId) {
        return spectrumRecordingDao.getVideoById(videoId);
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
