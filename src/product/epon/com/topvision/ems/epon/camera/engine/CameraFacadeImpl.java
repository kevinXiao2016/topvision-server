/***********************************************************************
 * $Id: CameraFacadeImpl.java,v1.0 2013年12月10日 下午4:08:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.ems.epon.camera.facade.CameraFacade;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年12月10日-下午4:08:37
 *
 */
@Engine("cameraFacade")
public class CameraFacadeImpl extends EmsFacade implements CameraFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<CameraFilterTable> refreshCameraList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CameraFilterTable.class);
    }

    @Override
    public CameraFilterTable addCameraFilter(SnmpParam snmpParam, CameraFilterTable cameraFitler) {
        cameraFitler.setRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, cameraFitler);
    }

    @Override
    public void deleteCameraConfig(SnmpParam snmpParam, CameraFilterTable cameraFitler) {
        cameraFitler.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cameraFitler);
    }

    @Override
    public CameraFilterTable updateCameraConfig(SnmpParam snmpParam, CameraFilterTable cameraFitler) {
        return snmpExecutorService.setData(snmpParam, cameraFitler);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.camera.facade.CameraFacade#getAvailableCameraIndex(com.topvision.framework.snmp.SnmpParam, java.lang.String)
     */
    @Override
    public Integer getAvailableCameraIndex(SnmpParam snmpParam, String oid) {
        String rawOid = "1.3.6.1.4.1.32285.11.2.3.5.3.1.1.5." + oid;
        for (int index = 1; index < 5; index++) {
            String data = snmpExecutorService.get(snmpParam, rawOid + "." + index);
            if ("noSuchInstance".equals(data)) {
                return index;
            }
        }
        throw new RuntimeException("camera fulled!");

    }

}
