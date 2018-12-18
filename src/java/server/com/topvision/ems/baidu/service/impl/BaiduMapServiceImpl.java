/***********************************************************************
 * $Id: BaiduMapServiceImpl.java,v1.0 2015年9月16日 上午10:44:26 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.baidu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.baidu.dao.BaiduMapDao;
import com.topvision.ems.baidu.domain.BaiduEntity;
import com.topvision.ems.baidu.service.BaiduMapService;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author loyal
 * @created @2015年9月16日-上午10:44:26
 * 
 */
@Service("baiduEntityService")
public class BaiduMapServiceImpl extends BaseService implements BaiduMapService {
    @Autowired
    private BaiduMapDao baiduMapDao;
    @Autowired
    private UserPreferencesService userPreferencesService;

    @Override
    public BaiduEntity addBaiduEntity(BaiduEntity baiduEntity) {
        baiduMapDao.insertBaiduEntity(baiduEntity);
        return baiduMapDao.selectBaiduEntityByEntityId(baiduEntity.getEntityId());
    }

    @Override
    public List<BaiduEntity> getBaiduEntity() {
        return baiduMapDao.selectBaiduEntity();
    }

    @Override
    public void modifyBaiduEntity(BaiduEntity baiduEntity) {
        baiduMapDao.updateBaiduEntity(baiduEntity);
    }

    @Override
    public void deleteBaiduEntity(Long entityId) {
        baiduMapDao.deleteBaiduEntity(entityId);
    }

    @Override
    public BaiduEntity getBaiduEntityByEntityId(Long entityId) {
        return baiduMapDao.selectBaiduEntityByEntityId(entityId);
    }

    @Override
    public List<BaiduEntity> searchEntity(Map<String, Object> queryMap) {
        return baiduMapDao.searchEntity(queryMap);
    }

    @Override
    public void saveBaiduMapCenter(UserContext uc, double longitud, double latitude, Byte zoom) {
        userPreferencesService.saveModulePreferences("longitud", "baidu", longitud, uc);
        userPreferencesService.saveModulePreferences("latitude", "baidu", latitude, uc);
        userPreferencesService.saveModulePreferences("zoom", "baidu", zoom, uc);
    }
    @Override
    public void batchInsertOrUpdateBaiduMap(List<BaiduEntity> baiduEntities) {
        baiduMapDao.batchInsertOrUpdateBaiduMap(baiduEntities);
    }

}
