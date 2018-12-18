/***********************************************************************
 * $Id: BaiduMapService.java,v1.0 2015年9月16日 上午10:04:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.baidu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.baidu.domain.BaiduEntity;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.UserContext;

/**
 * @author loyal
 * @created @2015年9月16日-上午10:04:10
 * 
 */
public interface BaiduMapService extends Service {

    /**
     * 添加设备到百度地图
     * 
     * @param baiduEntity
     */
    public BaiduEntity addBaiduEntity(BaiduEntity baiduEntity);

    /**
     * 获取所有百度地图上设备
     * 
     * @return
     */
    public List<BaiduEntity> getBaiduEntity();

    /**
     * 更新百度地图上设备
     * 
     * @param baiduEntity
     */
    public void modifyBaiduEntity(BaiduEntity baiduEntity);

    /**
     * 删除百度地图上设备
     * 
     * @param entityId
     */
    public void deleteBaiduEntity(Long entityId);

    /**
     * 设备是否在百度地图上
     * 
     * @param entityId
     * @return
     */
    public BaiduEntity getBaiduEntityByEntityId(Long entityId);

    /**
     * 搜索百度地图上设备，模糊查询，匹配别名，MAC，IP
     * 
     * @param searchTxt
     * @return
     */
    public List<BaiduEntity> searchEntity(Map<String, Object> queryMap);

    /**
     * 保存当前百度地图中心点
     * 
     * @param uc
     * @param longitud
     * @param latitude
     * @param zoom
     */
    public void saveBaiduMapCenter(UserContext uc, double longitud, double latitude, Byte zoom);
    
    /**
     * 批量插入或更新经纬度
     * @param
     * @return void
     */
    void batchInsertOrUpdateBaiduMap(List<BaiduEntity> googleEntities); 

}
