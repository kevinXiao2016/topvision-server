package com.topvision.ems.cmc.spectrum.service.impl;

import java.util.*;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallback;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.SpectrumMonitorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("spectrumCallbackCollection")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpectrumCallbackCollection {
    private Logger logger = LoggerFactory.getLogger(SpectrumCallbackCollection.class);
    private static Long nextCallbackId = 0L;
    private Map<Long,WebCallback> webCallbacks = Collections.synchronizedMap(new HashMap<Long, WebCallback>());
    private Map<Long,RealtimeCallback> realtimeVideoCallbacks = Collections.synchronizedMap(new HashMap<Long, RealtimeCallback>());
    private Map<String,Long> realTimeKeyToCallbackId = Collections.synchronizedMap(new HashMap<String, Long>());
    private HistoryCallback hisVideoCallback = null;
    private Map<Long,MobileCallback> mobileCallbacks = Collections.synchronizedMap(new HashMap<Long, MobileCallback>());

    @Autowired
    private SpectrumConfigService spectrumConfigService;
    @Autowired
    private SpectrumMonitorService spectrumMonitorService;

    //全局只能同时进行一个startMonitor避免同时开启同一个设备的多个monitor
    private static Object syncStart = new Object();

    public SpectrumCallbackCollection() {
    }

    public SpectrumVideo removeCallback(Long callbackId) {
        SpectrumVideo spectrumVideo = null;
        SpectrumCallback callback = null;
        if (webCallbacks.containsKey(callbackId)) {// 检测webCallback
            callback = webCallbacks.get(callbackId);
            webCallbacks.remove(callbackId);
        } else if (realtimeVideoCallbacks.containsKey(callbackId)) {// 检测RealTimeVideoCallback
            callback = realtimeVideoCallbacks.get(callbackId);
            realtimeVideoCallbacks.remove(callbackId);
        } else if (hisVideoCallback != null && hisVideoCallback.getCallbackId().equals(callbackId)) { // 检测HisVideoCallback
            callback = hisVideoCallback;
            hisVideoCallback = null;
        } else if (mobileCallbacks.containsKey(callbackId)) { // 检测AttenuationCallback
            callback = mobileCallbacks.get(callbackId);
            mobileCallbacks.remove(callbackId);
        }

        if (callback != null) {
            try {
                //停止SpectrumCallback
                spectrumVideo = callback.finish();
            } catch (Exception e) {
                logger.debug("",e);
            }
            //如果所有callback都停了，则停止monitor
            if (!isAnyCallbackExist()) {
                spectrumMonitorService.stopSpectrumMonitor(callback.getCmcId());
                logger.debug("Spectrum delCallback stop SpectrumMonitor, cmcId = " + callback.getCmcId() + " && callbackId = "
                        + callbackId + " && callback type = " + callback.getClass());
            }
        }

        return spectrumVideo;
    }

    public Long removeHisCallback() {
        Long callbackId = hisVideoCallback.getCallbackId();
        removeCallback(hisVideoCallback.getCallbackId());
        return callbackId;
    }

    public boolean isSameCallbackExist(SpectrumCallback callback) {
        if (callback instanceof HistoryCallback && this.isHisVideoCallbackExist()) {
            return true;
        }
        if (callback instanceof RealtimeCallback) {
            String realTimeKey = makeRealtimeKey(callback);
            if (realTimeKeyToCallbackId.containsKey(realTimeKey)) {
                return true;
            }
        }
        return false;
    }

    public void pushAllResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list, Long dt) {
        logger.info("Spectrum SpectrumCallbackCollection pushAllResult");
        logger.info("Following callbacks exist");
        for (WebCallback webCallback : webCallbacks.values()) {
            logger.info("WebCallback: callbackId = " + webCallback.getCallbackId() + " terminalIp = "
                    + webCallback.getTerminalIp() + " startTime = " + new Date(webCallback.getStartTime()));
            webCallback.appendResult(entityId, cmcId, startFreq, endFreq, list, dt);
        }
        for (RealtimeCallback realtimeCallback : realtimeVideoCallbacks.values()) {
            logger.debug("RealtimeCallback: callbackId = " + realtimeCallback.getCallbackId() + " terminalIp = "
                    + realtimeCallback.getTerminalIp());
            realtimeCallback.appendResult(entityId, cmcId, startFreq, endFreq, list, dt);
        }
        if (hisVideoCallback != null) {
            logger.debug("HistoryCallback: callbackId = " + hisVideoCallback.getCallbackId());
            hisVideoCallback.appendResult(entityId, cmcId, startFreq, endFreq, list, dt);
        }
        for (MobileCallback spectrumCallback : mobileCallbacks.values()) {
            logger.debug("AttenuationCallback: callbackId = " + spectrumCallback.getCallbackId());
            spectrumCallback.appendResult(entityId, cmcId, startFreq, endFreq, list, dt);
        }
    }

    public void sendOverTimeMessage() {
        logger.info("Spectrum SpectrumCallbackCollection sendOverTimeMessage");
        for (WebCallback webCallback : webCallbacks.values()) {
            logger.info("WebCallback: callbackId = " + webCallback.getCallbackId() + " terminalIp = "
                    + webCallback.getTerminalIp() + " startTime = " + new Date(webCallback.getStartTime()));
            webCallback.sendOverTimeMessage();
        }
    }

    public void sendHeartbeatTimeOut() {
        logger.info("Spectrum SpectrumCallbackCollection sendHeartbeatTimeOut");
        for (WebCallback webCallback : webCallbacks.values()) {
            logger.info("WebCallback: callbackId = " + webCallback.getCallbackId() + " terminalIp = "
                    + webCallback.getTerminalIp() + " startTime = " + new Date(webCallback.getStartTime()));
            webCallback.sendHeartbeatTimeOut();
        }

    }
    /**
     * 添加Callback
     *
     */
    public boolean addCallback(SpectrumCallback callback) {
        if (!isAnyCallbackExist()) {
            callback.setCallbackId(SpectrumCallbackCollection.nextCallbackId());
            if (callback instanceof WebCallback) {
                this.addWebCallback((WebCallback) callback);
            } else if (callback instanceof RealtimeCallback) {
                this.addRealtimeCallback((RealtimeCallback) callback);
                String realTimeKey = makeRealtimeKey(callback);
                realTimeKeyToCallbackId.put(realTimeKey, callback.getCallbackId());
            } else if (callback instanceof HistoryCallback) {
                this.addHisVideoCallback((HistoryCallback) callback);
            } else if (callback instanceof MobileCallback) {
                this.addMobileCallback((MobileCallback) callback);
            }
            try {
                startSpectrumMonitor(callback.getCmcId(),callback);
            } catch (Exception e) {
                logger.debug("",e);
                return false;
            }
        } else {
            callback.setCallbackId(SpectrumCallbackCollection.nextCallbackId());
            if (callback instanceof WebCallback) {
                this.addWebCallback((WebCallback) callback);
            } else if (callback instanceof RealtimeCallback) {
                this.addRealtimeCallback((RealtimeCallback) callback);
                String realTimeKey = makeRealtimeKey(callback);
                realTimeKeyToCallbackId.put(realTimeKey, callback.getCallbackId());
            } else if (callback instanceof HistoryCallback) {
                this.addHisVideoCallback((HistoryCallback) callback);
            } else if (callback instanceof MobileCallback) {
                this.addMobileCallback((MobileCallback) callback);
            }
        }
        return true;
    }

    /**
     * 是否只有历史录像在工作
     *
     */
    public boolean isOnlyHisVideoCallbackExist() {
        return isHisVideoCallbackExist() && getWebCallbackSize() == 0 && getRealTimeCallbackSzie() == 0
                && getAttenuationCallbackSzie() == 0;
    }

    /**
     * 是否有任何频谱工作正在进行
     *
     */
    public boolean isAnyCallbackExist() {
        return isHisVideoCallbackExist() || getWebCallbackSize() > 0 || getRealTimeCallbackSzie() > 0
                || getAttenuationCallbackSzie() > 0;
    }

    /*--------衰减-------*/
    public Long addMobileCallback(MobileCallback mobileCallback) {
        mobileCallbacks.put(mobileCallback.getCallbackId(), mobileCallback);
        return mobileCallback.getCallbackId();
    }

    public int getAttenuationCallbackSzie() {
        return mobileCallbacks.size();
    }

    /*--------历史录像-------*/
    public Long addHisVideoCallback(HistoryCallback hisVideoCallback) {
        this.hisVideoCallback = hisVideoCallback;
        return hisVideoCallback.getCallbackId();
    }

    public boolean isHisVideoCallbackExist() {
        return this.hisVideoCallback != null;
    }

    public Long takeHisCallbackId() {
        return hisVideoCallback.getCallbackId();
    }
    /*--------实时录像-------*/
    public Long addRealtimeCallback(RealtimeCallback realtimeCallback) {
        realtimeVideoCallbacks.put(realtimeCallback.getCallbackId(),realtimeCallback);
        return realtimeCallback.getCallbackId();
    }

    public int getRealTimeCallbackSzie() {
        return realtimeVideoCallbacks.size();
    }

    /*--------实时查看-------*/
    public Long addWebCallback(WebCallback webResultCallback) {
        webCallbacks.put(webResultCallback.getCallbackId(),webResultCallback);
        return webResultCallback.getCallbackId();
    }

    public int getWebCallbackSize() {
        return webCallbacks.size();
    }

    public Long takeRealtimeCallbackIdByWebCallbackId(Long callbackId) {
        if (webCallbacks.containsKey(callbackId)) {
            WebCallback webCallback = webCallbacks.get(callbackId);
            String realtimeKey = makeRealtimeKey(webCallback);
            return realTimeKeyToCallbackId.get(realtimeKey);
        } else {
            return null;
        }
    }

    /**
     *
     * @return 开启是否成功
     */
    private Boolean startSpectrumMonitor(Long cmcId, SpectrumCallback callback) {
        Integer period = 5000;
        if (callback.getClass() == WebCallback.class) { // 实时采集
            period = spectrumConfigService.getTimeInterval();
        } else if (callback.getClass() == RealtimeCallback.class) { // 实时频谱录像
            period = spectrumConfigService.getTimeInterval();
        } else if (callback.getClass() == HistoryCallback.class) { // 历史频谱录像
            period = spectrumConfigService.getHisTimeInterval();
        }
        synchronized (syncStart) {
            return spectrumMonitorService.startSpectrumMonitor(cmcId, period);
        }
    }

    private synchronized static Long nextCallbackId() {
        return nextCallbackId++;
    }

    private String makeRealtimeKey(SpectrumCallback callback) {
        return callback.getCmcId() + callback.getTerminalIp();
    }

}
