/***********************************************************************
 * $ RealtimeCallback.java,v1.0 2014-1-18 9:58:19 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallback;
import com.topvision.ems.cmc.spectrum.service.SpectrumRecordingService;
import com.topvision.ems.cmc.spectrum.utils.VersionUtil;
import com.topvision.ems.cmc.spectrum.video.SpectrumVideoToolV1_2;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;

/**
 * @author jay
 * @created @2014-1-18-9:58:19
 */
@Service("realtimeCallback")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RealtimeCallback implements SpectrumCallback {
    private Long callbackId = -1L;
    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private EntityService entityService;
    @Resource(name = "spectrumRecordingService")
    private SpectrumRecordingService spectrumRecordingService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    private final Logger logger = LoggerFactory.getLogger(RealtimeCallback.class);
    private Long startTime;
    private Long endTime;
    private Long firstFrameTime;
    private Long cmcId;
    private String videoRootPath = "./video/realTime/";
    private Integer pointCount;
    // private Integer lastPointCount;
    private Long frameIndex = 0L;
    private Long videoId;
    private String terminalIp;
    private String userName;
    private boolean isStarted = false;
    private RandomAccessFile rf;
    private SpectrumVideoToolV1_2 videoTool = new SpectrumVideoToolV1_2();

    /**
     * 初始化录像
     * 
     * @return
     */
    public boolean initRecord() {
        boolean makeFileSuccess = makeFile(cmcId);
        if (makeFileSuccess) {
            try {
                videoTool.writeLong(rf, cmcId, SpectrumVideoToolV1_2.cmcIdIndex);
                frameIndex = 0L;
                videoTool.writeLong(rf, frameIndex, SpectrumVideoToolV1_2.frameCountIndex);
                isStarted = true;
            } catch (IOException e) {
                logger.debug("RealtimeCallback : Write base params fails", e);
            }
        }
        return isStarted;
    }

    @Override
    public void appendResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list, Long dt) {
        if (!this.cmcId.equals(cmcId) || list == null) { // 当无录像数据时，直接return
            return;
        }
        try {
            pointCount = list.size();
            frameIndex++;
            videoTool.writeInt(rf, pointCount, SpectrumVideoToolV1_2.pointCountIndex);
            videoTool.writeLong(rf, frameIndex, SpectrumVideoToolV1_2.frameCountIndex);
            videoTool.writeFrame(rf, list, frameIndex, pointCount,startFreq,endFreq,
                    cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId));
        } catch (IOException e) {
            logger.debug("RealtimeCallback : Write frame fails", e);
        }
    }

    @Override
    public SpectrumVideo finish() {
        SpectrumVideo spectrumVideo = null;
        endTime = System.currentTimeMillis();
        logger.debug("RealtimeCallback finish endTime Long: " + endTime);
        logger.debug("RealtimeCallback finish endTime : " + new Date(endTime));
        logger.debug("RealtimeCallback finish endTime : " + new Timestamp(endTime));
        try {
            rf.close();
            // 更新录像记录的结束时间
            spectrumRecordingService.updateVideoEndTime(videoId, new Timestamp(endTime));
            spectrumVideo = spectrumRecordingService.getVideoById(this.videoId);
        } catch (IOException e) {
            logger.debug("RealtimeCallback : close file fails", e);
        }
        return spectrumVideo;
    }

    private boolean makeFile(Long cmcId) {
        this.cmcId = cmcId;
        try {
            Entity entity = entityService.getEntity(cmcId);
            if (entity != null) {
                File dir = new File(videoRootPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                firstFrameTime = System.currentTimeMillis();
                startTime = firstFrameTime;
                logger.debug("RealtimeCallback makeFile startTime Long: " + startTime);
                logger.debug("RealtimeCallback makeFile startTime Date: " + new Date(startTime));
                logger.debug("RealtimeCallback makeFile startTime Timestamp: " + new Timestamp(startTime));
                String filePath = this.getPath(entity.getEntityId());
                rf = new RandomAccessFile(new File(filePath), "rw");
                try {
                    videoTool.writeInt(rf, VersionUtil.getVersionLong("1.0.2.0"), SpectrumVideoToolV1_2.versionIndex);
                } catch (IOException e) {
                    logger.debug("RealtimeCallback : write file version fails", e);
                }
                // 将频谱信息存入数据库
                SpectrumVideo spectrumVideo = new SpectrumVideo();
                spectrumVideo.setVideoName(entity.getName() + "-" + format.format(new Date(startTime)));
                spectrumVideo.setStartTime(new Timestamp(startTime));
                spectrumVideo.setEndTime(new Timestamp(startTime));
                spectrumVideo.setFirstFrameTime(new Timestamp(firstFrameTime));
                spectrumVideo.setVideoType(SpectrumVideo.REALTIME_VIDEO);
                spectrumVideo.setUrl(filePath);
                spectrumVideo.setUserName(userName);
                spectrumVideo.setTerminalIp(terminalIp);
                this.videoId = spectrumRecordingService.saveVideoToDB(cmcId, spectrumVideo);
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            logger.debug("RealtimeCallback : create file fails", e);
            return false;
        }
    }

    private String getPath(Long entityId) {
        return videoRootPath + entityId + "_" + startTime + ".dat";
    }

    @Override
    public Long getCmcId() {
        return cmcId;
    }

    @Override
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    @Override
    public Long getCallbackId() {
        return callbackId;
    }

    @Override
    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isStarted() {
        return isStarted;
    }

}
