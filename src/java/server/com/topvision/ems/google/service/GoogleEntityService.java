package com.topvision.ems.google.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.network.domain.Link;
import com.topvision.framework.service.Service;

public interface GoogleEntityService extends Service {
    /**
     * @param entityId
     */
    void deleteGoogleEntity(long entityId);

    List<GoogleEntity> getAllGoogleEntity(Map<String, String> filter);

    GoogleEntity getGoogleEntityById(Long entityId);

    List<Link> getAllLinks();

    void insertGoogleEntity(GoogleEntity entity);

    /**
     * 插入或更新设备的Google地图关联信息
     * 
     * @param entity
     */
    void insertOrUpdateGoogleEntity(GoogleEntity entity);

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

    /**
     * 根据经纬度查询地理位置
     * 
     * @param latitude
     * @param longitude
     * @return
     */
    String queryForGeoLocationFromLatLng(double latitude, double longitude);

    /**
     * 根据地理位置查询经纬度
     * 
     * @return
     */
    String queryForLatLngFromGeoLocation();

    /**
     * 批量插入或更新
     * 
     * @param googleEntities
     */
    void batchInsertOrUpdateGoogleEntity(List<GoogleEntity> googleEntities);
}
