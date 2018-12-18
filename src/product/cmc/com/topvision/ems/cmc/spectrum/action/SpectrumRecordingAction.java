/***********************************************************************
 * $Id: SpectrumRecordingAction.java,v1.0 2014-1-13 上午9:18:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.action;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.exception.CmtsSwitchOffException;
import com.topvision.ems.cmc.spectrum.exception.OltSwitchOffException;
import com.topvision.ems.cmc.spectrum.service.SpectrumRecordingService;
import com.topvision.ems.cmc.spectrum.service.impl.RealtimeCallback;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2014-1-13-上午9:18:38
 * 
 */
@Controller("spectrumRecordingAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpectrumRecordingAction extends BaseAction {
    private static final long serialVersionUID = -8564820274808807135L;
    private final Logger logger = LoggerFactory.getLogger(SpectrumRecordingAction.class);
    @Resource(name = "spectrumRecordingService")
    private SpectrumRecordingService spectrumRecordingService;
    private Long cmcId;
    private Long callbackId;
    private String videoName;
    private Long videoId;
    private Long videoStartTime;

    /**
     * 跳转重命名录像页面
     * 
     * @return
     */
    public String showRenameVideo() {
        return SUCCESS;
    }

    /**
     * 重命名录像
     * 
     * @return
     */
    public String renameVideo() {
        String result = "";
        try {
            spectrumRecordingService.renameVideo(videoId, videoName);
            result = "success";
        } catch (Exception e) {
            logger.debug("renameVideo fail!", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    public String loadVideoStartTime() {
        JSONObject responseJson = new JSONObject();
        Long videoStartTime = (new Date().getTime() / 1000) * 1000;
        logger.debug("Spectrum loadVideoStartTime videoStartTime : " + videoStartTime);
        responseJson.put("videoStartTime", videoStartTime);
        writeDataToAjax(responseJson);
        return NONE;
    }

    /**
     * 启动实时频谱录像
     * 
     * @return
     */
    public String realTimeVideoStart() {
        JSONObject responseJson = new JSONObject();
        try {
            logger.debug("Spectrum realTimeVideoStart videoStartTime : " + videoStartTime);
            RealtimeCallback realtimeCallback = spectrumRecordingService.startRealtimeVideo(cmcId, videoStartTime,
                    request.getRemoteAddr());
            responseJson.put("callbackId", realtimeCallback.getCallbackId());
            responseJson.put("isStarted", realtimeCallback.isStarted());
            responseJson.put("videoStartTime", realtimeCallback.getStartTime());
        } catch (OltSwitchOffException e) {
            responseJson.put("error", ResourcesUtil.getString("spectrum.OLTSpectrumOff"));
        } catch (CmtsSwitchOffException e) {
            responseJson.put("error", ResourcesUtil.getString("spectrum.CMTSSpectrumOff"));
        } finally {
            writeDataToAjax(responseJson);
        }
        return NONE;
    }

    /**
     * 停止实时频谱录像
     * 
     * @return
     */
    public String realTimeVideoStop() {
        JSONObject result = new JSONObject();
        try {
            SpectrumVideo spectrumVideo = spectrumRecordingService.stopRealtimeVideo(cmcId, callbackId);
            result.put("success", true);
            result.put("videoId", spectrumVideo.getVideoId());
            result.put("videoName", spectrumVideo.getVideoName());
            result.put("endTime", spectrumVideo.getEndTime().getTime());
        } catch (Exception e) {
            result.put("success", false);
        }
        writeDataToAjax(result);
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public void setVideoStartTime(Long videoStartTime) {
        this.videoStartTime = videoStartTime;
    }

    public Long getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

}
