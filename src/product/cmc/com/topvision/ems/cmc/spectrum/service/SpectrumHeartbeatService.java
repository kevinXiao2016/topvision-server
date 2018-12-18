/***********************************************************************
 * $ SpectrumHeartbeatService.java,v1.0 2014-1-4 15:52:21 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

import java.util.List;

import com.topvision.ems.cmc.spectrum.domain.Heartbeat;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;

/**
 * 心跳监控模块 （HeartbeatManager）
 * 查看界面、实时\历史录像功能都会调用
 * 调用add方法后该模块会调用结果推送注册模块的add方法，注册一个结果回调
 * 调用del方法后该模块会调用结果推送注册模块的del方法，删除一个注册
 * 在service层为每一个回调实例提供一个心跳计数，由界面主动发起一个定期的ajax心跳访问，通知该实例还存活
 * 如果两个周期内接收不到心跳访问，那么就主动将推送注册队列中该实例删除掉
 * 在此模块中将dwr回调和java回调进行抽象统一
 *
 * @author jay
 * @created @2014-1-4-15:52:21
 */
public interface SpectrumHeartbeatService {

    /**
     * 添加一个频谱采集的心跳健康，会自动注册一个回调接口，推送数据
     *
     * @param callback
     * @return callbackId
     */
    Long addHeartbeat(Long cmcId, SpectrumCallback callback);

    /**
     * 提供给action和频谱录像功能使用，删除一个频谱采集的心跳检测
     *
     * @param callbackId
     */
    SpectrumVideo delHeartbeat(Long callbackId);

    /**
     * 心跳维护
     *
     * @param callbackId
     * @throws com.topvision.ems.cmc.spectrum.exception.SpectrumSessionTimeoutException 当心跳超时时抛出
     */
    Boolean heartbeat(Long callbackId);

    /**
     * 提供HeartBeatList对外接口
     * 
     * @return
     */
    List<Heartbeat> getHeartbeatList();

    /**
     * 通过cmcId获取HisCallbackId
     * @param cmcId
     * @return
     */
    Long takeHisCallbackId(Long cmcId);

    /**
     * 通过cmcId和WebCallbackId获取RealtimeCallbackId
     * @param callbackId
     * @return
     */
    Long takeRealtimeCallbackIdByWebCallbackId(Long callbackId);

    /**
     * 删除WebHeartbeat
     * @param callbackId
     */
    void delWebHeartBeat(Long callbackId);
}
