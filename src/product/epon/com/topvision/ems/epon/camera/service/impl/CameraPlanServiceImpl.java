/***********************************************************************
 * $Id: CameraPlanServiceImpl.java,v1.0 2013年12月23日 下午3:05:19 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.service.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.camera.dao.CameraPlanDao;
import com.topvision.ems.epon.camera.domain.CameraPhysicalInfo;
import com.topvision.ems.epon.camera.domain.CameraPlanTable;
import com.topvision.ems.epon.camera.service.CameraPlanService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.zetaframework.util.Cell;
import com.topvision.platform.zetaframework.util.ExcelResolver;
import com.topvision.platform.zetaframework.util.Row;
import com.topvision.platform.zetaframework.util.RowHandler;
import com.topvision.platform.zetaframework.util.Validator;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:05:19
 *
 */
@Service
public class CameraPlanServiceImpl extends BaseService implements CameraPlanService {
    @Autowired
    private CameraPlanDao cameraPlanDao;

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#batchImportPlan(java.io.FileInputStream)
     */
    @Override
    public boolean batchImportPlan(FileInputStream fis) {
        final List<CameraPlanTable> list = new ArrayList<>();
        final AtomicBoolean hasIllegalInfo = new AtomicBoolean(false);
        ExcelResolver.resolve(new RowHandler() {

            @Override
            public Boolean handleRow(Row row) {
                if (row.getRowNum() > 1) {
                    List<Cell> cells = row.getCells();
                    CameraPlanTable plan = new CameraPlanTable();
                    for (int i = 0; i < cells.size(); i++) {
                        Cell cell = cells.get(i);
                        if (i == 0) {
                            plan.setCameraNo(cell.getContent());
                        } else if (i == 1) {
                            String ipAddress = cell.getContent();
                            if (Validator.isIpV4(ipAddress)) {
                                plan.setIp(ipAddress);
                            } else {
                                hasIllegalInfo.compareAndSet(false, true);
                                return CONTINUE;
                            }
                        } else if (i == 2) {
                            plan.setLocation(cell.getContent());
                        }
                    }
                    list.add(plan);
                }
                return CONTINUE;
            }

        }, fis);
        cameraPlanDao.batchInsertPlan(list);
        return hasIllegalInfo.get();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#batchImportPhyInfo(java.io.FileInputStream)
     */
    @Override
    public boolean batchImportPhyInfo(FileInputStream fis) {
        final List<CameraPhysicalInfo> list = new ArrayList<>();
        final AtomicBoolean hasIllegalInfo = new AtomicBoolean(false);
        ExcelResolver.resolve(new RowHandler() {

            @Override
            public Boolean handleRow(Row row) {
                if (row.getRowNum() > 1) {
                    List<Cell> cells = row.getCells();
                    CameraPhysicalInfo plan = new CameraPhysicalInfo();
                    for (int i = 0; i < cells.size(); i++) {
                        Cell cell = cells.get(i);
                        if (i == 0) {
                            String macAddress = cell.getContent();
                            if (Validator.isMac(macAddress)) {
                                plan.setMac(MacUtils.formatMac(macAddress));
                            } else {
                                hasIllegalInfo.compareAndSet(false, true);
                                return CONTINUE;
                            }
                        } else if (i == 1) {
                            plan.setType(cell.getContent());
                        } else if (i == 2) {
                            plan.setNote(cell.getContent());
                        }
                    }
                    list.add(plan);
                }
                return CONTINUE;
            }

        }, fis);
        cameraPlanDao.batchInsertPhyInfo(list);
        return hasIllegalInfo.get();
    }

    /* (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#loadCameraPhyList(java.util.Map)
     */
    @Override
    public List<CameraPhysicalInfo> loadCameraPhyList(Map<String, Object> map) {
        return cameraPlanDao.selectCameraPhyList(map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#getCameraPhyCount(java.util.Map)
     */
    @Override
    public int getCameraPhyCount(Map<String, Object> map) {
        return cameraPlanDao.queryCameraPhyCount(map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#loadCameraPlanList(java.util.Map)
     */
    @Override
    public List<CameraPlanTable> loadCameraPlanList(Map<String, Object> map) {
        return cameraPlanDao.selectCameraPlanList(map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#getCameraPlanCount(java.util.Map)
     */
    @Override
    public int getCameraPlanCount(Map<String, Object> map) {
        return cameraPlanDao.queryCameraPlanCount(map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#deleteCameraPlan(java.lang.String)
     */
    @Override
    public void deleteCameraPlan(String cameraNo) {
        if (cameraNo.contains("$")) {
            String[] list = cameraNo.split("\\$");
            for (String camera : list) {
                if (!camera.equals("")) {
                    cameraPlanDao.deleteCameraPlan(camera);
                }
            }
        } else {
            cameraPlanDao.deleteCameraPlan(cameraNo);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#deletePhyInfo(java.lang.String)
     */
    @Override
    public void deletePhyInfo(String mac) {
        if (mac.contains("$")) {
            String[] list = mac.split("\\$");
            for (String $mac : list) {
                if (!$mac.equals("")) {
                    cameraPlanDao.deletePhyInfo($mac);
                }
            }
        } else {
            cameraPlanDao.deletePhyInfo(mac);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#modifyCameraPlan(com.topvision.ems.epon.camera.domain.CameraPlanTable)
     */
    @Override
    public void modifyCameraPlan(CameraPlanTable table) {
        cameraPlanDao.modifyCameraPlan(table);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService# (com.topvision.ems.epon.camera.domain.CameraPhysicalInfo)
     */
    @Override
    public void modifyPhyInfo(CameraPhysicalInfo info) {
        cameraPlanDao.modifyPhyInfo(info);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#addCameraPlan(com.topvision.ems.epon.camera.domain.CameraPlanTable)
     */
    @Override
    public void addCameraPlan(CameraPlanTable table) {
        cameraPlanDao.insertPlan(table);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.service.CameraPlanService#addCameraPhy(com.topvision.ems.epon.camera.domain.CameraPhysicalInfo)
     */
    @Override
    public void addCameraPhy(CameraPhysicalInfo info) {
        cameraPlanDao.insertPhyInfo(info);
    }

}
