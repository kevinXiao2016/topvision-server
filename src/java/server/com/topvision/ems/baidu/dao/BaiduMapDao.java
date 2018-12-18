/***********************************************************************
 * $Id: BaiduMapDao.java,v1.0 2015年9月16日 上午10:42:35 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.baidu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.baidu.domain.BaiduEntity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2015年9月16日-上午10:42:35
 * 
 */
public interface BaiduMapDao extends BaseEntityDao<BaiduEntity> {

    /**
     * 添加设备到百度地图
     * 
     * @param baiduEntity
     */
    public void insertBaiduEntity(BaiduEntity baiduEntity);

    /**
     * 获取所有百度地图上设备
     * 
     * @return
     */
    public List<BaiduEntity> selectBaiduEntity();

    /**
     * 更新百度地图上设备
     * 
     * @param baiduEntity
     */
    public void updateBaiduEntity(BaiduEntity baiduEntity);

    /**
     * 删除百度地图上设备
     * 
     * @param entityId
     */
    public void deleteBaiduEntity(Long entityId);

    /**
     * 获取百度地图上设备
     * 
     * @param entityId
     */
    public BaiduEntity selectBaiduEntityByEntityId(Long entityId);

    /**
     * 搜索百度地图上设备，模糊查询，匹配别名，MAC，IP
     * 
     * @param entityId
     * @return
     */
    public List<BaiduEntity> searchEntity(Map<String, Object> queryMap);
    
    public void batchInsertOrUpdateBaiduMap(List<BaiduEntity>baiduEntities);
}
