package com.topvision.ems.google.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.network.domain.Link;
import com.topvision.framework.dao.BaseEntityDao;

public interface GoogleEntityDao extends BaseEntityDao<GoogleEntity> {
    List<Link> getLinkInGoogleMap();

    /**
     * 插入或更新设备的Google地图关联信息
     */
    void insertOrUpdateGoogleEntity(GoogleEntity entity);
    
    /**
     * 批量插入或更新设备的Google地图关联信息
     * @param googleEntities
     */
    void batchInsertOrUpdateGoogleEntity(List<GoogleEntity> googleEntities);

    /**
     * 查询所有能加入到google地图的设备
     * 
     * @return
     */
    List<Entity> queryAvaibleDevice();

    /**
     * 固定位置
     * 
     * @param entityId
     * @param fixed
     */
    void fixlocation(Long entityId, boolean fixed);

   
}
