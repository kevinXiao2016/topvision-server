/***********************************************************************
 * $Id: CameraDao.java,v1.0 2013年12月10日 下午3:50:13 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年12月10日-下午3:50:13
 *
 */
public interface CameraDao extends BaseEntityDao<CameraFilterTable> {

    /**
     * 批量插入允许通过的摄像头列表
     * @param entityId
     * @param cameraList
     */
    void batchInsertCameraList(Long entityId, List<CameraFilterTable> cameraList);

    /**
     * 加载摄像头列表
     * @param map
     * @return
     */
    List<CameraFilterTable> loadCameraList(Map<String, Object> map);

    /**
     * 插入摄像头到数据库
     * @param cameraFitler
     */
    void insertCameraFilter(CameraFilterTable cameraFitler);

    /**
     * 查询摄像头数目
     * @param map
     * @return
     */
    int queryCameraCount(Map<String, Object> map);

    /**
     * 删除摄像头
     * @param cameraFitler
     */
    void deleteCamera(CameraFilterTable cameraFitler);

    /**
     * 更新摄像头
     * @param cameraFitler
     */
    void updateCamera(CameraFilterTable cameraFitler);

    /**
     * 获取所有的摄像头类型
     * @return
     */
    List<String> queryAllCameraType();

    /**
     * 判断IP,MAC是否已经存在
     * @param filter
     * @return
     */
    int checkDuplicate(CameraFilterTable filter);

}
