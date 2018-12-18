/***********************************************************************
 * $Id: SpectrumPlayDaoImpl.java,v1.0 2014-3-4 下午7:52:14 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.spectrum.dao.SpectrumPlayDao;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author YangYi
 * @created @2014-3-4-下午7:52:14
 * 
 */
@Repository("spectrumPlayDao")
public class SpectrumPlayDaoImpl extends MyBatisDaoSupport<SpectrumVideo> implements SpectrumPlayDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.spectrum.domain.SpectrumPlay";
    }

    @Override
    public List<SpectrumVideo> querySpectrumVideos(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getSpectrumVideos"), map);
    }

    @Override
    public Long querySpectrumVideosCount(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getSpectrumVideosCount"), map);
    }

    @Override
    public SpectrumVideo getVideoById(Long videoId) {
        return getSqlSession().selectOne(getNameSpace("getVideoById"), videoId);
    }

    @Override
    public void deleteVideo(Long[] videoIds) {
        SqlSession session = getBatchSession();
        try {
            for (Long videoId : videoIds) {
                session.delete(getNameSpace("deleteVideo"), videoId);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void clearVideo() {
        getSqlSession().delete(getNameSpace("clearVideo"));
    }

    @Override
    public List<SpectrumVideo> getVideoList() {
        return getSqlSession().selectList(getNameSpace("getVideoList"));
    }
}
