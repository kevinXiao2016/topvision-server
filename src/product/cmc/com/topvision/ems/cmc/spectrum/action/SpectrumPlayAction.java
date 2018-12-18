/***********************************************************************
 * $Id: SpectrumPlayAction.java,v1.0 2014-3-4 下午6:48:55 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideoFrameData;
import com.topvision.ems.cmc.spectrum.service.SpectrumPlayService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author YangYi
 * @created @2014-3-4-下午6:48:55
 * 
 */
@Controller("spectrumPlayAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpectrumPlayAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Resource(name = "spectrumPlayService")
    private SpectrumPlayService spectrumPlayService;
    private Long videoId;
    private String videoName;
    private String startTime;
    private String firstFrameTime;
    private String endTime;
    private Integer videoType;
    private String userName;
    private String terminalIp;
    private String cmtsAlias;
    private Integer timeInterval;
    private Long[] videoIds;
    private Long frameTotal;
    private Long frameIndex;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private JSONObject videoJSON;
    // @Value("${Spectrum.unit}")
    private String spectrumUnit;
    private Long startFreq;
    private Long endFreq;

    /**
     * 加载录像列表
     * 
     * @return
     */
    public String loadSpectrumVideo() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (videoName != null && !"".equals(videoName)) {
            map.put("videoName", videoName);
        }
        if (startTime != null && !"".equals(startTime)) {
            map.put("startTime", startTime);
        }
        if (endTime != null && !"".equals(endTime)) {
            map.put("endTime", endTime);
        }
        if (cmtsAlias != null && !"".equals(cmtsAlias)) {
            map.put("cmtsAlias", cmtsAlias);
        }
        if (userName != null && !"".equals(userName)) {
            map.put("userName", userName);
        }
        if (terminalIp != null && !"".equals(terminalIp)) {
            map.put("terminalIp", terminalIp);
        }
        if (videoType != null && videoType != 0) {
            map.put("videoType", videoType);
        }
        map.put("start", super.getStart());
        map.put("limit", super.getLimit());
        List<SpectrumVideo> spectrumVideos = spectrumPlayService.getSpectrumVideos(map);
        Long size = spectrumPlayService.getSpectrumVideosCount(map);
        json.put("data", spectrumVideos);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String playVideo() {
        // 根据录像的ID获取录像的基本信息(帧数，开始时间，结束时间)
        try {
            SpectrumVideo spectrumVideo = spectrumPlayService.getVideoInfo(videoId);
            SpectrumVideoFrameData firstFrameData = spectrumPlayService.getOneFrame(videoId, 1L);
            startFreq = firstFrameData.getStartFreq();
            endFreq = firstFrameData.getEndFreq();
            videoJSON = JSONObject.fromObject(spectrumVideo);
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } catch (XMLStreamException e) {
            logger.error("", e);
        }
        // 电平单位
        spectrumUnit = (String) UnitConfigConstant.get("elecLevelUnit");
        return SUCCESS;
    }

    /**
     * 获取指定帧的数据
     * 
     * @return
     */
    public String getSpectrumVideoFrame() {
        try {
            SpectrumVideoFrameData frameData = spectrumPlayService.getOneFrame(videoId, frameIndex);
            List<CmcUpChannelBaseShowInfo> channelList = frameData.getChannelList();
            JSONArray channelArray = new JSONArray();
            for (int i = 0; i < channelList.size(); i++) {
                if (channelList.get(i).getIfAdminStatus() == 1) {// 信道开启再添加
                    JSONObject channelInfo = new JSONObject();
                    channelInfo.put("channelId", i + 1);
                    channelInfo.put("channelFreq", channelList.get(i).getChannelFrequency());
                    channelInfo.put("channelWidth", channelList.get(i).getChannelWidth());
                    channelInfo.put("adminStatus", channelList.get(i).getIfAdminStatus());
                    channelArray.add(channelInfo);
                }
            }
            JSONObject obj = new JSONObject();
            obj.put("channels", channelArray);
            JSONArray data = JSONArray.fromObject(frameData.getDataList());
            obj.put("startFreq", frameData.getStartFreq());
            obj.put("endFreq", frameData.getEndFreq());
            obj.put("data", data);
            writeDataToAjax(obj);
        } catch (FileNotFoundException e) {
            writeDataToAjax("FileNotFound");
            logger.error("", e);
        } catch (IOException e) {
            writeDataToAjax("FileReadError");
            logger.error("", e);
        } catch (XMLStreamException e) {
            writeDataToAjax("FileReadError");
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 删除频谱录像
     * 
     * @return
     */
    public String deleteVideo() {
        String result = "";
        try {
            spectrumPlayService.deleteVideo(videoIds);
            result = "success";
        } catch (Exception e) {
            logger.debug("delete video fails!", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 清空频谱录像
     * 
     * @return
     */
    public String clearVideo() {
        String result = "";
        try {
            spectrumPlayService.clearVideo();
            result = "success";
        } catch (Exception e) {
            logger.debug("clear video fails!", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 跳转频谱录像管理页面
     * 
     * @return
     */
    public String showSpectrumVideoMgmt() {
        return SUCCESS;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(Long frameIndex) {
        this.frameIndex = frameIndex;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getFrameTotal() {
        return frameTotal;
    }

    public void setFrameTotal(Long frameTotal) {
        this.frameTotal = frameTotal;
    }

    public String getFirstFrameTime() {
        return firstFrameTime;
    }

    public void setFirstFrameTime(String firstFrameTime) {
        this.firstFrameTime = firstFrameTime;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public JSONObject getVideoJSON() {
        return videoJSON;
    }

    public void setVideoJSON(JSONObject videoJSON) {
        this.videoJSON = videoJSON;
    }

    public String getSpectrumUnit() {
        return spectrumUnit;
    }

    public void setSpectrumUnit(String spectrumUnit) {
        this.spectrumUnit = spectrumUnit;
    }

    public Integer getVideoType() {
        return videoType;
    }

    public String getUserName() {
        return userName;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public String getCmtsAlias() {
        return cmtsAlias;
    }

    public void setVideoType(Integer videoType) {
        this.videoType = videoType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public void setCmtsAlias(String cmtsAlias) {
        this.cmtsAlias = cmtsAlias;
    }

    public Long[] getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(Long[] videoIds) {
        this.videoIds = videoIds;
    }

    public Long getStartFreq() {
        return startFreq;
    }

    public void setStartFreq(Long startFreq) {
        this.startFreq = startFreq;
    }

    public Long getEndFreq() {
        return endFreq;
    }

    public void setEndFreq(Long endFreq) {
        this.endFreq = endFreq;
    }

}
