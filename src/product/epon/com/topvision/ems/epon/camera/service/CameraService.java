/***********************************************************************
 * $Id: CameraService.java,v1.0 2013年12月10日 下午2:52:36 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年12月10日-下午2:52:36
 *
 */
public interface CameraService extends Service {

    /**
     * 刷新摄像头信息
     * @param entityId
     */
    void refreshCameraList(Long entityId);

    /**
     * 刷新所有的摄像头信息
     */
    void refreshAllCamera();

    /**
     * 加载摄像头列表
     * @param entityId
     * @param uniIndex
     * @return
     */
    List<CameraFilterTable> loadCameraList(Map<String, Object> map);

    /**
     * 添加摄像头
     * @param cameraFitler
     */
    void addCameraFilter(CameraFilterTable cameraFitler);

    /**
     * 获得摄像头数目
     * @param map
     * @return
     */
    int getCameraCount(Map<String, Object> map);

    /**
     * 删除摄像头配置(解绑定)
     * @param camera
     */
    void deleteCameraConfig(CameraFilterTable camera);

    /**
     * 获取所有的摄像头类型
     * @return
     */
    List<String> getAllCameraType();

}
