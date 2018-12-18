/***********************************************************************
 * $Id: WebCallback.java,v1.0 2014-4-2 上午10:05:38 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallback;
import com.topvision.ems.socketserver.domain.SocketResponse;

import net.sf.json.JSONObject;

/**
 * @author YangYi
 * @created @2014-4-2-上午10:05:38
 * @modify by jay 2016-11-09
 * 
 */

@Service("webCallback")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WebCallback implements SpectrumCallback {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Long callbackId = -1L;
    private Long cmcId;
    private Long startTime;
    private String terminalIp;
    private SocketResponse socketResponse;
    
    private int i=0;
    private Object sendSyn = new Object();
    private boolean flag = false;

    private WebCallback() {
    }

    @Override
    public void appendResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list,
            Long dt) {
        
        logger.debug("Spectrum WebCallback  appendResult cmcId = " + cmcId + " callbackId = " + callbackId);
        logger.debug("Spectrum WebCallback  appendResult  List  size = " + list.size());
        if (flag) {
            logger.debug("skip this result.");
            return;
        }
        synchronized (sendSyn) {
            flag = true;
            if (socketResponse != null && !socketResponse.isClosed()) {
                try {
                    // 发送开始消息
                    JSONObject startMessage = new JSONObject();
                    startMessage.put("message", "SPECTRUM_FRAME_START");
                    startMessage.put("cmcId", cmcId);
                    startMessage.put("index", i);
                    socketResponse.write(startMessage.toString());
                    socketResponse.flush();
                    logger.debug("SPECTRUM_FRAME_START" + new Date());
    
                    // 推送数据
                    // modify by fanzidong，每1000个点推一次
                    for (int i = 1, len = list.size() / 1000 + 1; i <= len; i++) {
                        int endIndex = (i * 1000 >= list.size()) ? list.size() : i * 1000;
                        List<List<Number>> curList = list.subList((i-1) * 1000, endIndex);
                        JSONObject dataMessage = new JSONObject();
                        dataMessage.put("message", "SPECTRUM_FRAME_DATA");
                        dataMessage.put("data", curList);
                        dataMessage.put("startFreq", startFreq);
                        dataMessage.put("endFreq", endFreq);
                        dataMessage.put("cmcId", cmcId);
                        dataMessage.put("index", i);
                        socketResponse.write(dataMessage.toString());
                        socketResponse.flush();
                    }
    
                    // 发送结束消息
                    JSONObject endMessage = new JSONObject();
                    endMessage.put("message", "SPECTRUM_FRAME_END");
                    endMessage.put("cmcId", cmcId);
                    endMessage.put("index", i);
                    socketResponse.write(endMessage.toString());
                    socketResponse.flush();
                    logger.debug("SPECTRUM_FRAME_END" + new Date());
                    i++;
                } catch (Exception e) {
                    logger.error("", e);
                }
            } else {
                logger.debug("SPECTRUM_IS_CLOSE" + new Date());
            }
            flag = false;
        }
    }

    public void sendOverTimeMessage() {
        if (socketResponse != null && !socketResponse.isClosed()) {
            try {
                logger.debug("sendOverTimeMessage SPECTRUM_OVERTIME start" + new Date());
                JSONObject message = new JSONObject();
                message.put("message", "SPECTRUM_OVERTIME");
                socketResponse.write(message.toString());
                socketResponse.flush();
                logger.debug("sendOverTimeMessage SPECTRUM_OVERTIME end" + new Date());
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            logger.debug("sendOverTimeMessage SPECTRUM_OVERTIME" + new Date());
        }
    }

    public void sendHeartbeatTimeOut() {
        if (socketResponse != null && !socketResponse.isClosed()) {
            try {
                logger.debug("sendHeartbeatTimeOut SPECTRUM_TIMEOUT start" + new Date());
                JSONObject message = new JSONObject();
                message.put("message", "SPECTRUM_TIMEOUT");
                socketResponse.write(message.toString());
                socketResponse.flush();
                logger.debug("sendHeartbeatTimeOut SPECTRUM_TIMEOUT end" + new Date());
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            logger.debug("sendOverTimeMessage SPECTRUM_TIMEOUT" + new Date());
        }
    }

    @Override
    public SpectrumVideo finish() {
        if (socketResponse != null && !socketResponse.isClosed()) {
            socketResponse.setClosed(true);
            socketResponse = null;
        }
        return null;
    }

    @Override
    public Long getCmcId() {
        return cmcId;
    }

    @Override
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    @Override
    public Long getCallbackId() {
        return callbackId;
    }

    @Override
    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public SocketResponse getSocketResponse() {
        return socketResponse;
    }

    public void setSocketResponse(SocketResponse socketResponse) {
        this.socketResponse = socketResponse;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

}
