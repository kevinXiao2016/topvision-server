/***********************************************************************
 * $Id: OpticalReceiverRefreshService.java,v1.0 2016年9月18日 上午9:20:05 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.service;

/**
 * @author haojie
 * @created @2016年9月18日-上午9:20:05
 *
 */
public interface OpticalReceiverRefreshService {

    /**
     * 光机类型字符串
     * 
     * @param entityId
     * @param cmcIndex
     * @return
     */
    String refreshSysDorType(Long entityId, Long cmcIndex);

    /**
     * State of A-B switch
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshABSwitch(Long entityId, Long cmcIndex);

    /**
     * 正向频道数量
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshChannelNum(Long entityId, Long cmcIndex);

    /**
     * 直流24V输出电压（DC12V/DC24V）
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshDCPower(Long entityId, Long cmcIndex);

    /**
     * 光机参数表
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshDevParams(Long entityId, Long cmcIndex);

    /**
     * 正向射频支路1-4衰减
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshFwdAtt(Long entityId, Long cmcIndex);

    /**
     * 正向射频支路1-4均衡
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshFwdEq(Long entityId, Long cmcIndex);

    /**
     * 同轴供电输入电压 AC 60V电压
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshLinePower(Long entityId, Long cmcIndex);

    /**
     * 反向射频支路1-4衰减表
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshRevAtt(Long entityId, Long cmcIndex);

    /**
     * RF1-4端口输出电平
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshRFPort(Long entityId, Long cmcIndex);

    /**
     * 反向光收1-4输入光功率
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshRRXOptPow(Long entityId, Long cmcIndex);

    /**
     * 正向光收A路光功率
     * 
     * @param entityId
     * @param cmcIndex
     */
    void refreshOpRxInput(Long entityId, Long cmcIndex);

    /**
     * 正向光发参数
     * @param entityId
     * @param cmcIndex
     */
    void refreshOpRxOutput(Long entityId, Long cmcIndex);
}
