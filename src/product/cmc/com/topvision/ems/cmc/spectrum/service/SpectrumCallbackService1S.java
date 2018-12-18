package com.topvision.ems.cmc.spectrum.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.service.impl.WebCallback;

public interface SpectrumCallbackService1S {
    /**
     * 添加Callback
     * 
     * @param cmcId
     * @param callback
     * @return
     */
    public Long addCallback(Long cmcId, SpectrumCallback callback);

    /**
     * 删除Callback
     * 
     * @param callbackId
     * @param cmcId
     * @return
     */
    public SpectrumVideo delCallback(Long callbackId, Long cmcId);

    /**
     * 
     * @param cmcId
     * @return
     */
    public Long delHisCallback(Long cmcId);

    /**
     * 检测是否有历史录像存在
     *
     * @param cmcId
     * @return
     */
    public boolean isHisVideoCallbackExist(Long cmcId);

    /**
     * 
     * @modify by Victor@20160823修改方法增加返回值，判断是否需要推送。
     * 
     * @param entityId
     * @param cmcId
     * @param list
     * @param dt
     * @return 如果有推送则返回true，否则返回false
     */
    public boolean pushResult(Long entityId, Long cmcId,Long startFreq,Long endFreq, List<List<Number>> list, Long dt);

    /**
     * 观看超时，推送web消息
     * @param cmcId
     */
    void sendOverTimeMessage(Long cmcId);

    /**
     * 通过cmcId获取HisCallbackId
     * @param cmcId
     * @return
     */
    Long takeHisCallbackId(Long cmcId);

    /**
     * 通过cmcId和WebCallbackId获取RealtimeCallbackId
     * @param callbackId
     * @param cmcId
     * @return
     */
    Long takeRealtimeCallbackIdByWebCallbackId(Long callbackId,Long cmcId);

    /**
     * 判断是否存在相同的callback
     * @param callback
     * @return
     */
    boolean isSameCallbackExist(SpectrumCallback callback);

    /**
     * 判断某个cmc下是否只有历史频谱开启着
     * @param cmcId
     * @return
     */
    boolean isOnlyHisVideoCallbackExist(Long cmcId);

    /**
     * 向WebCallback推送心跳超时的消息
     * @param cmcId
     */
    void sendHeartbeatTimeOut(Long cmcId);
}
