/***********************************************************************
 * $Id: CameraPlanService.java,v1.0 2013年12月23日 下午3:04:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.camera.domain.CameraPhysicalInfo;
import com.topvision.ems.epon.camera.domain.CameraPlanTable;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:04:47
 *
 */
public interface CameraPlanService extends Service {

    /**
     * 批量导入规划信息表
     * @param fis
     * @return 
     */
    boolean batchImportPlan(FileInputStream fis);

    /**
     * 批量导入摄像头硬件信息
     * @param fis
     * @return 
     */
    boolean batchImportPhyInfo(FileInputStream fis);

    /**
     * 加载硬件信息表 
     * @param map
     * @return
     */
    List<CameraPhysicalInfo> loadCameraPhyList(Map<String, Object> map);

    /**
     * 获取硬件信息总数
     * @param map
     * @return
     */
    int getCameraPhyCount(Map<String, Object> map);

    /**
     * 加载规划表 
     * @param map
     * @return
     */
    List<CameraPlanTable> loadCameraPlanList(Map<String, Object> map);

    /**
     * 获取规划信息总数
     * @param map
     * @return
     */
    int getCameraPlanCount(Map<String, Object> map);

    /**
     * 删除规划表表项
     * @param cameraNo
     */
    void deleteCameraPlan(String cameraNo);

    /**
     * 删除物理信息表项
     * @param mac
     */
    void deletePhyInfo(String mac);

    /**
     * 修改规划表项
     * @param table
     */
    void modifyCameraPlan(CameraPlanTable table);

    /**
     *  修改物理信息备注
     * @param info
     */
    void modifyPhyInfo(CameraPhysicalInfo info);

    /**
     * 手动添加摄像头规划
     * @param table
     */
    void addCameraPlan(CameraPlanTable table);

    /**
     * 手动添加硬件信息
     * @param info
     */
    void addCameraPhy(CameraPhysicalInfo info);

}
