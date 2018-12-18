/***********************************************************************
 * $Id: SpectrumPlayServiceImpl.java,v1.0 2014-3-4 下午7:30:43 $
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.dao.SpectrumPlayDao;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideoFrameData;
import com.topvision.ems.cmc.spectrum.service.SpectrumPlayService;
import com.topvision.ems.cmc.spectrum.utils.VersionUtil;
import com.topvision.ems.cmc.spectrum.video.SpectrumVideoTool;

/**
 * @author YangYi
 * @created @2014-3-4-下午7:30:43
 * 
 */
@Service("spectrumPlayService")
public class SpectrumPlayServiceImpl extends CmcBaseCommonService implements SpectrumPlayService {
    @Resource(name = "spectrumPlayDao")
    private SpectrumPlayDao spectrumPlayDao;

    @Override
    public List<SpectrumVideo> getSpectrumVideos(Map<String, Object> map) {
        return spectrumPlayDao.querySpectrumVideos(map);
    }

    @Override
    public Long getSpectrumVideosCount(Map<String, Object> map) {
        return spectrumPlayDao.querySpectrumVideosCount(map);
    }

    @Override
    public SpectrumVideoFrameData getOneFrame(Long videoId, Long frameIndex) throws FileNotFoundException, IOException,
            XMLStreamException {
        SpectrumVideo spectrumVideo = spectrumPlayDao.getVideoById(videoId);
        RandomAccessFile rf = new RandomAccessFile(new File(spectrumVideo.getUrl()), "r");
        SpectrumVideoTool svt = VersionUtil.getVersionClass(rf);
        SpectrumVideoFrameData s = svt.readFrame(rf, frameIndex);
        rf.close();
        return s;
    }

    @Override
    public SpectrumVideo getVideoInfo(Long videoId) throws FileNotFoundException, IOException, XMLStreamException {
        SpectrumVideo spectrumVideo = spectrumPlayDao.getVideoById(videoId);
        RandomAccessFile rf = new RandomAccessFile(new File(spectrumVideo.getUrl()), "r");
        SpectrumVideoTool svt = VersionUtil.getVersionClass(rf);
        Long frameCount = svt.readFrameCount(rf);
        spectrumVideo.setFrameCount(frameCount);
        rf.close();
        return spectrumVideo;
    }

    @Override
    public void deleteVideo(Long[] videoIds) {
        for (Long videoId : videoIds) {
            SpectrumVideo spectrumVideo = spectrumPlayDao.getVideoById(videoId);
            if (spectrumVideo != null) {
                File file = new File(spectrumVideo.getUrl());
                if (file.exists()) {
                    file.delete();// 删除文件
                }
            }
        }
        spectrumPlayDao.deleteVideo(videoIds);
    }

    @Override
    public void clearVideo() {
        // 删除文件
        List<SpectrumVideo> spectrumVideos = spectrumPlayDao.getVideoList();
        for (SpectrumVideo spectrumVideo : spectrumVideos) {
            if (spectrumVideo != null) {
                File file = new File(spectrumVideo.getUrl());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        spectrumPlayDao.clearVideo();
    }

    public static void main(String args[]) {
        // Long frameIndex = 2l;
        // String url =
        // "D:/workspace_ems/topvision-console/video/realTime/30000000000_1426582801320.dat";
        // File file = new File(url);
        // if (file.exists()) {
        // file.delete();// 删除文件
        // }
        // JSONArray valueJson = null;
        // try {
        // RandomAccessFile rf = new RandomAccessFile(new File(url), "r");
        // Integer pointCount;
        // pointCount = VideoVersionToolV1_0_0_0.readInt(rf,
        // VideoVersionToolV1_0_0_0.pointCountIndex);
        // System.out.println("************" + frameIndex + "************");
        // List<List<Number>> list = VideoVersionToolV1_0_0_0.readFrame(rf, frameIndex, pointCount);
        // valueJson = JSONArray.fromObject(list);
        // for (int i = 0; i < 10; i++) {
        // System.out.println(list.get(i).get(0));
        // System.out.println(list.get(i).get(1));
        // }
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // System.out.println(valueJson);
    }

}
