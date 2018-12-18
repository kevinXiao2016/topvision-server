/***********************************************************************
 * $Id: SpectrumRecordingDao.java,v1.0 2014-1-14 下午4:36:26 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao;

import java.sql.Timestamp;

import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;

/**
 * @author haojie
 * @created @2014-1-14-下午4:36:26
 * 
 */
public interface SpectrumRecordingDao {

    /**
     * 查询产生频谱录像的相关信息
     * 
     * @return
     */
    SpectrumVideo generateVideoInfo(Long cmcId);

    /**
     * 向数据库中插入录像记录
     * 
     * @param spectrumVideo
     * @return
     */
    Long addVideoToDB(SpectrumVideo spectrumVideo);

    /**
     * 更新录像记录结束时间
     * 
     * @param cmcId
     * @param endTime
     */
    void updateVideoEndTime(Long cmcId, Timestamp endTime);

    /**
     * 插入录像与设备的关系表
     * 
     * @param cmcId
     * @param videoId
     */
    void insertVideoCmtsRela(Long cmcId, Long videoId);

    /**
     * 重命名录像
     * 
     * @param videoId
     * @param videoName
     */
    void renameVideo(Long videoId, String videoName);

    /**
     * 从perfmonitor中取出timeInterval
     * 
     * @param cmcId
     * @return
     */
    Integer getTimeInterval(Long cmcId);

    /**
     * 根据videoId取出其对应的记录
     * 
     * @param videoId
     * @return
     */
    SpectrumVideo getVideoById(Long videoId);

    /**
     * 开启CMTS历史频谱录像(数据库层)
     * 
     * @param cmcId
     */
    void startHisVideo(Long cmcId);

    /**
     * 关闭CMTS历史频谱录像(数据库层)
     * 
     * @param cmcId
     */
    void stopHisVideo(Long cmcId);

    /**
     * 根据CmcId获取该CMTS的频谱开关、历史频谱开关
     * 
     * @param cmcId
     * @return
     */
    CmtsSpectrumConfig getCmtsSwitchById(Long cmcId);
}
