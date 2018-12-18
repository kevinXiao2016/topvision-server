/***********************************************************************
 * $Id: CommandSendDao.java,v1.0 2014年7月17日 下午4:09:03 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2014年7月17日-下午4:09:03
 * 
 */
public interface CommandSendDao extends BaseEntityDao<SendConfigEntity> {
    /**
     * 获取设备列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Entity> selectEntityList(Map<String, Object> map, int start, int limit);

    /**
     * 获取设备数量
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    Long selectEntityListNum(Map<String, Object> map);

    /**
     * 获取所有未完成的设备列表
     * 
     * @return
     */
    List<SendConfigEntity> getUncompleteEntitys();

    /**
     * 获取所有失败的设备列表
     * 
     * @return
     */
    List<SendConfigEntity> getFailedEntitys();

    /**
     * 获取所有未开始的设备列表
     * 
     * @return
     */
    List<SendConfigEntity> getUnstartEntitys();

    /**
     * 更新设备下发配置信息
     * 
     * @return
     */
    void updateSendConfigEntity(SendConfigResult sendConfigResult);

    /**
     * 添加设备下发列表
     * 
     * @return
     */
    void insertSendConfigEntity(List<Long> entityIdList);

    /**
     * 删除设备下发列表
     * 
     * @return
     */
    void deleteSendConfigEntity(List<Long> entityIdList);

    /**
     * 获取下发设备列表
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<SendConfigEntity> selectCommandSendEntityList(Map<String, Object> map, int start, int limit);

    /**
     * 获取下发设备数量
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    Long selectCommandSendEntityListNum(Map<String, Object> map);

    /**
     * 获取下发设备
     * 
     * @param entityId
     * @return
     */
    SendConfigEntity selectCommandSendEntityByEntityId(Long entityId);

    /**
     * 查询设备所在地域
     * 
     * @param long
     * @return
     */
    List<String> selectEntityFolder(Long entityId);

    /**
     * 获取配置下发结果
     * 
     * @param long
     * @return
     */
    String selectSendConfigResult(Long entityId);

    /**
     * 更新配置下发状态
     * 
     * @param long
     * @return
     */
    void updateCommandSendStatue(List<SendConfigEntity> sendConfigEntitys, Integer status);
}
