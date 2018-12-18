/***********************************************************************
 * $Id: CameraPlanDao.java,v1.0 2013年12月23日 下午3:29:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.camera.domain.CameraPhysicalInfo;
import com.topvision.ems.epon.camera.domain.CameraPlanTable;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:29:27
 *
 */
public interface CameraPlanDao extends BaseEntityDao<Entity> {

    /**
     * 批量插入摄像头规划信息
     * @param list
     */
    public void batchInsertPlan(List<CameraPlanTable> list);

    /**
     * 插入规划信息
     * @param plan
     */
    public void insertPlan(CameraPlanTable plan);

    /**
     * 插入硬件信息
     * @param info
     */
    public void insertPhyInfo(CameraPhysicalInfo info);

    /**
     * 批量导入摄像头硬件信息
     * @param list
     */
    public void batchInsertPhyInfo(List<CameraPhysicalInfo> list);

    /**
     * 加载规划表 
     * @param map
     * @return
     */
    public List<CameraPlanTable> selectCameraPlanList(Map<String, Object> map);

    /**
     * 查询摄像头规划信息总数
     * @param map
     * @return
     */
    public int queryCameraPlanCount(Map<String, Object> map);

    /**
     * 加载硬件信息表 
     * @param map
     * @return
     */
    public List<CameraPhysicalInfo> selectCameraPhyList(Map<String, Object> map);

    /**
     * 查询摄像头物理信息总数
     * @param map
     * @return
     */
    public int queryCameraPhyCount(Map<String, Object> map);

    /**
     *  删除规划表表项
     * @param cameraNo
     */
    public void deleteCameraPlan(String cameraNo);

    /**
     *  删除物理信息表项
     * @param mac
     */
    public void deletePhyInfo(String mac);

    /**
     * 修改规划表项
     * @param table
     */
    public void modifyCameraPlan(CameraPlanTable table);

    /**
     * 修改物理信息备注
     * @param info
     */
    public void modifyPhyInfo(CameraPhysicalInfo info);
}
