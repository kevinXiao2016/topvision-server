/***********************************************************************
 * $Id: SpectrumPlayDao.java,v1.0 2014-3-4 下午7:47:28 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;

/**
 * @author YangYi
 * @created @2014-3-4-下午7:47:28
 * 
 */
public interface SpectrumPlayDao {

    /**
     * 根据录像的ID获取一条数据
     * 
     * @param videoId
     * @return
     */
    SpectrumVideo getVideoById(Long videoId);

    /**
     * 获取录像列表
     * 
     * @param map
     * @return
     */
    List<SpectrumVideo> querySpectrumVideos(Map<String, Object> map);

    /**
     * 获取录像数量
     * 
     * @param map
     * @return
     */
    Long querySpectrumVideosCount(Map<String, Object> map);

    /**
     * 批量删除频谱录像
     * 
     * @param videoIds
     */
    void deleteVideo(Long[] videoIds);

    /**
     * 清空录像记录
     */
    void clearVideo();

    /**
     * 获取所有录像记录
     * 
     * @return
     */
    List<SpectrumVideo> getVideoList();
}
