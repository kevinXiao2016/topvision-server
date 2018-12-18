/***********************************************************************
 * $Id: NbiConnectionService.java,v1.0 2016年3月17日 上午9:49:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.service;

import com.topvision.performance.nbi.api.NbiFtpConfig;

/**
 * @author Bravin
 * @created @2016年3月17日-上午9:49:42
 *
 */
public interface NbiConnectionService {

    /**
     * @return
     */
    String getServerIp();

    /**
     * @return
     */
    int getServerPort();

    /**
     * 通知所有ENGINE连接NBI，并推送数据
     */
    void notifyEngineConnectNbi();

    /**
     * @return
     */
    String getNbiIpAddress();

    /**
     * 获取NBI端口
     * @return
     */
    Integer getNbiPort();

    /**
     * 处理NBI断开连接的情况
     */
    void incrementAndHandleDisconnected();

    /**
     * 根据NBI的check间隔
     * @return
     */
    int getCheckNbiInterval();

    /**
     * 每次修改NBI配置后都需要检测
     */
    void retryCheckNbi();

    /**
     * 运行时修改组配置
     */
    void dynamicUpdateGroupIndexs();

    /**
     * 运行时修改组周期
     */
    void dynamicUpdateGroupPeroid();

    /**
     * 运行时修改组周期
     */
    void dynamicUpdateFtpConfig(NbiFtpConfig config);

    /**
     * 判断NBI是否已连接上
     * @return
     */
    boolean isNbiConnected();

    /**
     * @param entityId
     */
    void dynamicRemoveEntity(long entityId);

    /**
     * @param ip
     */
    void dynamicRemoveEntity(String ip);

}
