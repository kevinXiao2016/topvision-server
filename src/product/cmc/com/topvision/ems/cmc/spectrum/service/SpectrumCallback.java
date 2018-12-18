/***********************************************************************
 * $ com.topvision.ems.cmc.spectrum.service.SpectrumCallback,v1.0 2014-1-7 17:03:00 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

import java.util.List;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;

/**
 * 频谱结果推送回调接口
 * 
 * @author jay
 * @created @2014-1-7-17:03:00
 */
public interface SpectrumCallback {

    /**
     * 推送结果到每一个web客户端
     */
    void appendResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list, Long dt);

    /**
     * 停止接收时调用
     */
    SpectrumVideo finish();

    /**
     * 获得该callback的Id
     * 
     * @return
     */
    Long getCallbackId();

    /**
     * 设置callback的Id
     * 
     * @param callbackId
     */
    void setCallbackId(Long callbackId);

    /**
     * 获得cmcId
     * 
     * @return
     */
    public Long getCmcId();

    /**
     * 设置CmcId
     * 
     * @param cmcId
     */
    public void setCmcId(Long cmcId);

    public String getTerminalIp();
    public void setTerminalIp(String terminalIp);
    
}
