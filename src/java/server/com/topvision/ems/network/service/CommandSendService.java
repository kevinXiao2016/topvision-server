/***********************************************************************
 * $Id: CommandSendService.java,v1.0 2014年7月17日 下午4:03:44 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年7月17日-下午4:03:44
 * 
 */
public interface CommandSendService extends Service {
    /**
     * 获取设备列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Entity> loadEntityList(Map<String, Object> map, int start, int limit);

    /**
     * 获取设备数量
     * 
     * @param map
     * @return
     */
    Long loadEntityListNum(Map<String, Object> map);

    /**
     * 生成批量配置下发阻塞队列列表
     */
    void makeSendConfigArray();

    /**
     * 生成下发失败的批量配置下发阻塞队列列表
     */
    void makeFailedSendConfigArray();

    /**
     * 生成未开始下发的批量配置下发阻塞队列列表
     */
    void makeUnstartSendConfigArray();

    /**
     * 记录下发结果
     * 
     * @param sendConfigResult
     */
    void recordSendConfigResult(SendConfigResult sendConfigResult);

    /**
     * 重启构造阻塞队列的线程，用于修改时间间隔
     */
    void restartMakeSendConfigArrayJob(Long interval);

    /**
     * 修改每个命令之间的间隔时间
     * 
     * @param sendCommandInterval
     */
    void modifySendCommandInterval(Long sendCommandInterval);

    /**
     * 添加设备下发列表
     *
     */
    public void addSendConfigEntity(List<Long> entityIdList);

    /**
     * 删除设备下发列表
     *
     */
    public void deleteSendConfigEntity(List<Long> entityIdList);

    /**
     * 获取下发设备列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<SendConfigEntity> getCommandSendEntityList(Map<String, Object> map, int start, int limit);

    /**
     * 获取下发设备数量
     * 
     * @param map
     * @return
     */
    Long getCommandSendEntityListNum(Map<String, Object> map);

    /**
     * 查询设备所在地域
     *
     * @return
     */
    List<String> getEntityFolder(Long entityId);

    /**
     * 获取配置下发结果
     *
     * @return
     */
    String getSendConfigResult(Long entityId);

    /**
     * 自动添加设备到配置下发
     * 
     * @param entityId
     */
    void addAutoSendConfigEntity(Long entityId);
}
