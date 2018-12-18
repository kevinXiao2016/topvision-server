/***********************************************************************
 * $ com.topvision.ems.cmc.spectrum.service.SpectrumRecordingService,v1.0 2014-1-4 15:53:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

import java.sql.Timestamp;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.service.impl.RealtimeCallback;

/**
 * @author jay
 * @created @2014-1-4-15:53:48
 */
public interface SpectrumRecordingService {
    /**
     * 一个实时频谱录像
     * 
     * @param cmcId
     *            进行频谱录像的cmcId
     * @return 心跳ID
     */
    RealtimeCallback startRealtimeVideo(Long cmcId, Long startTime, String ip);

    /**
     * 停止一个实时频谱录像
     */
    SpectrumVideo stopRealtimeVideo(Long cmcId, Long callbackId);

    /**
     * 将频谱录像信息存入数据库
     * 
     * @param cmcId
     * @param spectrumVideo
     */
    Long saveVideoToDB(Long cmcId, SpectrumVideo spectrumVideo);

    /**
     * 更新频谱录像结束时间
     * 
     * @param videoId
     * @param timestamp
     */
    void updateVideoEndTime(Long videoId, Timestamp endTime);

    /**
     * 重命名录像
     * 
     * @param videoId
     * @param videoName
     */
    void renameVideo(Long videoId, String videoName);

    /**
     * 根据videoId取出其对应记录
     * 
     * @param videoId
     * @return
     */
    SpectrumVideo getVideoById(Long videoId);

    /**
     * 开启历史频谱采集
     * 
     * @param cmcId
     *            要开启历史频谱采集的CMTS的CMC_ID
     */
    Boolean startHisVideo(Long cmcId);

    /**
     * 关闭历史频谱采集
     * 
     * @param cmcId
     *            要关闭历史频谱采集的CMTS的CMC_ID
     */
    Boolean stopHisVideo(Long cmcId);

}
