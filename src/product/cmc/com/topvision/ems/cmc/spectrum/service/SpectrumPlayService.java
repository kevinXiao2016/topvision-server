/***********************************************************************
 * $Id: SpectrumPlayService.java,v1.0 2014-3-4 下午7:30:13 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideoFrameData;

/**
 * @author YangYi
 * @created @2014-3-4-下午7:30:13
 * 
 */
public interface SpectrumPlayService {

    /**
     * 根据VideoId和期望获取帧编号得到一帧的信息
     * 
     * @param videoId
     * @param frameIndex
     *            第几帧
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    SpectrumVideoFrameData getOneFrame(Long videoId, Long frameIndex) throws FileNotFoundException, IOException,
            XMLStreamException;

    /**
     * 根据VidoeId查询信息
     * 
     * @param videoId
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */

    SpectrumVideo getVideoInfo(Long videoId) throws FileNotFoundException, IOException, XMLStreamException;

    /**
     * 获取录像列表
     * 
     * @param map
     * @return
     */
    List<SpectrumVideo> getSpectrumVideos(Map<String, Object> map);

    /**
     * 获取录像数量
     * 
     * @param map
     * @return
     */
    Long getSpectrumVideosCount(Map<String, Object> map);

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

}
