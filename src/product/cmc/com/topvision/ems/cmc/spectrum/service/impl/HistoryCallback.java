/***********************************************************************
 * $Id: HistoryCallback.java,v1.0 2014-2-24 上午10:05:38 $
 * 
 * @author: YangYi
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

import com.topvision.ems.cmc.spectrum.video.SpectrumVideoToolV1_2;
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
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;

/**
 * @author YangYi
 * @created @2014-2-24-上午10:05:38
 * 
 */

@Service("historyCallback")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryCallback implements SpectrumCallback {
    protected static Logger logger = LoggerFactory.getLogger(HistoryCallback.class);
    private Long callbackId = -1L;
    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private EntityService entityService;
    @Resource(name = "spectrumRecordingService")
    private SpectrumRecordingService spectrumRecordingService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    private Long startTime;
    private Long endTime;
    private Long firstFrameTime;
    private Long cmcId;
    private String videoRootPath = "./video/his/";
    private Integer pointCount = -1;
    private Long frameIndex = 0L;
    private Long videoId;
    private RandomAccessFile rf;
    private SpectrumVideoToolV1_2 videoTool = new SpectrumVideoToolV1_2();

    @Override
    public synchronized void appendResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list, Long dt) {
        if (this.cmcId.equals(cmcId) && list != null) {
            if (pointCount == -1) {
                this.initFile(entityId, cmcId, list, dt);
            } else {
                try {
                    // 如果采集步长发生变化，需要重新记录录像文件
                    if (pointCount != list.size()) {
                        finish();
                        startTime = System.currentTimeMillis();
                        this.initFile(entityId, cmcId, list, dt);
                    } else {
                        frameIndex++;
                        videoTool.writeLong(rf, frameIndex, SpectrumVideoToolV1_2.frameCountIndex);
                    }
                    if(frameIndex >  3600 * 24){//如果数值过大，记录
                        logger.error("HistoryCallback appendResult frameIndex = " + frameIndex);
                        return;
                    }
                    videoTool.writeFrame(rf, list, frameIndex, pointCount,
                            startFreq,endFreq,cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId));
                } catch (IOException e) {
                    logger.debug("RealtimeCallback : Write frame fails", e);
                }
            }
        }
    }

    @Override
    public SpectrumVideo finish() {
        SpectrumVideo spectrumVideo = null;
        endTime = System.currentTimeMillis();
        logger.debug("HistoryCallback finish endTime Long: " + endTime);
        logger.debug("HistoryCallback finish endTime : " + new Date(endTime));
        logger.debug("HistoryCallback finish endTime : " + new Timestamp(endTime));
        pointCount = -1;
        try {
            rf.close();
            // 更新录像记录的结束时间
            spectrumRecordingService.updateVideoEndTime(videoId, new Timestamp(endTime));
            spectrumVideo = spectrumRecordingService.getVideoById(this.videoId);
        } catch (IOException e) {
            logger.debug("HistoryCallback : close file fails", e);
        }
        return spectrumVideo;
    }

    private void initFile(Long entityId, Long cmcId, List<List<Number>> list, Long dt) {
        makeFile(cmcId);
        pointCount = list.size();
        frameIndex = 0L;
        try {
            videoTool.writeLong(rf, entityId, SpectrumVideoToolV1_2.entityIdIndex);
            videoTool.writeLong(rf, cmcId, SpectrumVideoToolV1_2.cmcIdIndex);
            videoTool.writeLong(rf, dt, SpectrumVideoToolV1_2.dtIndex);
            videoTool.writeInt(rf, pointCount, SpectrumVideoToolV1_2.pointCountIndex);
            videoTool.writeLong(rf, frameIndex, SpectrumVideoToolV1_2.frameCountIndex);
        } catch (IOException e) {
            logger.debug("HistoryCallback : Write base params fails", e);
        }
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
                logger.debug("HistoryCallback makeFile startTime Long: " + startTime);
                logger.debug("HistoryCallback makeFile startTime Date: " + new Date(startTime));
                logger.debug("HistoryCallback makeFile startTime Timestamp: " + new Timestamp(startTime));
                String filePath = this.getPath(entity.getEntityId());
                rf = new RandomAccessFile(new File(filePath), "rw");
                try {
                    videoTool
                            .writeInt(rf, VersionUtil.getVersionLong("1.0.2.0"),
                            SpectrumVideoToolV1_2.versionIndex);
                } catch (IOException e) {
                    logger.debug("RealtimeCallback : write file version fails", e);
                }
                // 将频谱信息存入数据库
                SpectrumVideo spectrumVideo = new SpectrumVideo();
                spectrumVideo.setVideoName(entity.getName() + "-" + format.format(new Date(startTime)));
                spectrumVideo.setStartTime(new Timestamp(startTime));
                spectrumVideo.setEndTime(new Timestamp(startTime));
                spectrumVideo.setFirstFrameTime(new Timestamp(firstFrameTime));
                spectrumVideo.setVideoType(SpectrumVideo.HIS_VIDEO);
                spectrumVideo.setUrl(filePath);
                spectrumVideo.setUserName("system");
                spectrumVideo.setTerminalIp("-");
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
    public String getTerminalIp() {
        return null;
    }

    @Override
    public void setTerminalIp(String terminalIp) {

    }

    @Override
    public Long getCallbackId() {
        return callbackId;
    }

    @Override
    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

}
