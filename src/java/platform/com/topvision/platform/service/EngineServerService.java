/***********************************************************************
 * $Id: EngineServerService.java,v 1.1 Jul 19, 2009 10:28:05 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import com.alibaba.dubbo.registry.NotifyListener;
import com.topvision.ems.enginemgr.EngineManage;
import com.topvision.ems.facade.domain.EngineServerStatus;
import com.topvision.platform.domain.EngineServer;

/**
 * @author kelers
 * @Create Date Jul 19, 2009 10:28:05 AM
 */
public interface EngineServerService extends NotifyListener {
    
    /**
     * 验证采集器唯一性
     * 
     * @param engineServer
     * @return
     */
    boolean checkEngineServerExist(EngineServer engineServer);
    
    /**
     * 添加一个分布式采集器
     * 
     * @param engineServer
     */
    void addEngineServer(EngineServer engineServer);
    
    /**
     * 重启分布式采集器
     * 
     * @param engineServerIds
     */
    void reStartEngineServer(Integer engineId);

    /**
     * 删除一个分布式采集器
     * 
     * @param engineServer
     */
    void deleteEngineServer(Integer engineId);

    /**
     * 启用分布式采集器
     * 
     * @param engineServer
     */
    void startEngineServer(Integer engineId);

    /**
     * 停用分布式采集器
     * 
     * @param engineServer
     */
    void stopEngineServer(Integer engineId);

    /**
     * 修改分布式采集器
     * 
     * @param engineServer
     */
    void modifyEngineServer(EngineServer engineServer);
    
    /**
     * 修改本地分布式采集器
     * 
     * @param engineServer
     */
    void modifyLocalEngineServer(EngineServer engineServer);

    /**
     * 获得所有的EngineServer列表
     * 
     * @return List<EngineServer>
     */
    List<EngineServer> getEngineServerList();

    /**
     * 获取分布式采集器的状态信息
     * 
     * @return List<EngineServer>
     */
    EngineServerStatus getEngineServerStatus(Integer engineServerId);

    /**
     * 批量获取分布式采集器的状态信息
     * 
     * @return List<EngineServer>
     */
    List<EngineServerStatus> getEngineServerStatuss(List<Integer> engineServerIds);

    /**
     * 获得指定管理器
     * 
     * @param ip
     * @return
     */
    EngineManage getEngineManage(String ip);
    
    
    /**
     * 获得一个指定的Engine
     * 
     * @param engineId
     *            指定的EngineID
     * @return EngineServer
     */
    EngineServer getEngineServerById(Integer engineId);

    /**
     * 升级Engine
     * 
     * @param enginePort
     * @param engineManage
     */
    void upgradeEngine(EngineServer engineServer, EngineManage engineManage);

    /**
     * 初始化engine,下发配置参数
     * @param engineServer
     */
    void initEngine(EngineServer engineServer);
    
    /**
     * 所有的Engine采集器是否启动
     * 
     * @return
     */
    boolean isAllEngineStart();

    /**
     * 重置EngineServer缓存
     * 
     */
    void updateEngineStatusBeforeStart();
    
    
    /**
     * 更新采集器版本
     * 
     * @param id
     * @param version
     */
    void updateEngineVersion(Integer id, String version);
    
    
    /**
     * 发送状态变化消息
     * @param engineServer
     * @param status
     */
    void sendStatusChangeMsg(EngineServer engineServer, byte status);

    /**
     * 通知所有Engine连接NBI
     * @param nbiIpAddress
     * @param nbiPort
     * @param isStart 
     */
    void notifyEngineConnectNbi(String nbiIpAddress, int nbiPort, boolean isStart);

    /**
     * 通知指定的Engine连接NBI
     * @param engineServer
     * @param nbiIpAddress
     * @param nbiPort
     * @param isStart
     */
    void notifyEngineConnectNbi(EngineServer engineServer, String nbiIpAddress, int nbiPort, boolean isStart);
}
