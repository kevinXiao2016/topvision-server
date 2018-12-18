/***********************************************************************
 * $Id: SpectrumRecordingDaoImpl.java,v1.0 2014-1-14 下午4:37:42 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao.mybatis;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.spectrum.dao.SpectrumRecordingDao;
import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2014-1-14-下午4:37:42
 * 
 */
@Repository("spectrumRecordingDao")
public class SpectrumRecordingDaoImpl extends MyBatisDaoSupport<SpectrumVideo> implements SpectrumRecordingDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.spectrum.domain.SpectrumRecording";
    }

    @Override
    public SpectrumVideo generateVideoInfo(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("generateVideoInfo"), cmcId);
    }

    @Override
    public Long addVideoToDB(SpectrumVideo spectrumVideo) {
        return (long) getSqlSession().insert(getNameSpace("insertVideo"), spectrumVideo);
    }

    @Override
    public void updateVideoEndTime(Long videoId, Timestamp endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("videoId", videoId);
        map.put("endTime", endTime);
        getSqlSession().update(getNameSpace("updateVideoEndTime"), map);
    }

    @Override
    public void insertVideoCmtsRela(Long cmcId, Long videoId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("videoId", videoId);
        getSqlSession().insert(getNameSpace("insertVideoCmtsRela"), map);
    }

    @Override
    public void renameVideo(Long videoId, String videoName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("videoId", videoId);
        map.put("videoName", videoName);
        getSqlSession().insert(getNameSpace("updateVideoName"), map);
    }

    @Override
    public Integer getTimeInterval(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getTimeInterval"), cmcId);
    }

    @Override
    public SpectrumVideo getVideoById(Long videoId) {
        return getSqlSession().selectOne(getNameSpace("selectByVideoId"), videoId);
    }

    @Override
    public void startHisVideo(Long cmcId) {
        CmtsSpectrumConfig config = getSqlSession().selectOne(getNameSpace("getCmtsSwitchById"), cmcId);
        if (config != null) {
            getSqlSession().update(getNameSpace("startHisVideoSwitch"), cmcId);
        } else {
            getSqlSession().insert(getNameSpace("insertHisVideoSwitch"), cmcId);
        }
    }

    @Override
    public void stopHisVideo(Long cmcId) {
        getSqlSession().update(getNameSpace("stopHisVideoSwitch"), cmcId);
    }

    @Override
    public CmtsSpectrumConfig getCmtsSwitchById(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getCmtsSwitchById"), cmcId);
    }

}
