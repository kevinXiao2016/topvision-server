/***********************************************************************
 * $Id: CameraServiceImpl.java,v1.0 2013年12月10日 下午2:53:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.camera.dao.CameraDao;
import com.topvision.ems.epon.camera.domain.CameraBindDuplicate;
import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.ems.epon.camera.facade.CameraFacade;
import com.topvision.ems.epon.camera.service.CameraService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013年12月10日-下午2:53:42
 *
 */
@Service("cameraService")
public class CameraServiceImpl extends BaseService implements CameraService, SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CameraDao cameraDao;

    @PostConstruct
    public void initilize() {
        messageService.addListener(SynchronizedListener.class, this);
    }

    @PreDestroy
    public void destory() {
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    public void refreshCameraList(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<CameraFilterTable> cameraList = getFacade(snmpParam.getIpAddress()).refreshCameraList(snmpParam);
        cameraDao.batchInsertCameraList(entityId, cameraList);
    }

    @Override
    public void refreshAllCamera() {
        List<Entity> oltList = entityService.getEntityByType(entityTypeService.getOltType());
        SnmpParam snmpParam = null;
        List<CameraFilterTable> cameraList = null;
        for (Entity entity : oltList) {
            try {
                snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
                cameraList = getFacade(snmpParam.getIpAddress()).refreshCameraList(snmpParam);
                cameraDao.batchInsertCameraList(entity.getEntityId(), cameraList);
            } catch (Exception e) {
                logger.error("olt:{} can't be refreshed!:{}", entity.getIp(), e);
            }
        }
    }

    private CameraFacade getFacade(String ip) {
        return facadeFactory.getFacade(ip, CameraFacade.class);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            refreshCameraList(event.getEntityId());
            logger.info("refreshCameraList success");
        } catch (Exception e) {
            logger.error("refreshCameraList error:", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public List<CameraFilterTable> loadCameraList(Map<String, Object> map) {
        return cameraDao.loadCameraList(map);
    }

    @Override
    public void addCameraFilter(CameraFilterTable cameraFitler) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cameraFitler.getEntityId());
        if (cameraFitler.getCameraIndex() == null) {
            String oid = cameraFitler.getSlotIndex() + "." + cameraFitler.getPonIndex() + "."
                    + cameraFitler.getOnuIndex();
            Integer cameraIndex = getFacade(snmpParam.getIpAddress()).getAvailableCameraIndex(snmpParam, oid);
            cameraFitler.setCameraIndex(cameraIndex);
        }
        CameraFilterTable filter = null;
        try {
            filter = getFacade(snmpParam.getIpAddress()).addCameraFilter(snmpParam, cameraFitler);
            cameraDao.insertCameraFilter(filter);
        } catch (SnmpSetException e) {
            if (cameraFitler != null) {
                Integer duplicated = cameraDao.checkDuplicate(cameraFitler);
                throw new SnmpSetException(duplicated.toString());
            }
        }
    }

    @Override
    public int getCameraCount(Map<String, Object> map) {
        return cameraDao.queryCameraCount(map);
    }

    @Override
    public void deleteCameraConfig(CameraFilterTable camera) {
        Integer duplicated = cameraDao.checkDuplicate(camera);
        if (duplicated == CameraBindDuplicate.NONE_DUP) {
            camera.setIp(null);
            camera.setMac(null);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(camera.getEntityId());
            this.getFacade(snmpParam.getIpAddress()).deleteCameraConfig(snmpParam, camera);
            cameraDao.deleteCamera(camera);
        } else {
            throw new SnmpSetException(duplicated.toString());
        }
    }

    @Override
    public List<String> getAllCameraType() {
        return cameraDao.queryAllCameraType();
    }

}
