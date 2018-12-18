/***********************************************************************
 * $Id: CameraFacade.java,v1.0 2013年12月10日 下午3:41:15 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.facade;

import java.util.List;

import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年12月10日-下午3:41:15
 *
 */
@EngineFacade(serviceName = "CameraFacade", beanName = "cameraFacade")
public interface CameraFacade extends Facade {
    /**
     * 刷新摄像头列表
     * @param snmpParam
     * @return
     */
    public List<CameraFilterTable> refreshCameraList(SnmpParam snmpParam);

    /**
     * 添加摄像头
     * @param snmpParam 
     * @param cameraFitler
     * @return 
     */
    public CameraFilterTable addCameraFilter(SnmpParam snmpParam, CameraFilterTable cameraFitler);

    /**
     * 删除摄像头(解绑定)
     * @param snmpParam
     * @param cameraFitler
     */
    void deleteCameraConfig(SnmpParam snmpParam, CameraFilterTable cameraFitler);

    /**
     * 更新摄像头(替换)
     * @param snmpParam
     * @param cameraFitler
     * @return
     */
    CameraFilterTable updateCameraConfig(SnmpParam snmpParam, CameraFilterTable cameraFitler);
    
    /**
     * 找到可以使用的cameraindex
     * @param snmpParam
     * @param oid
     * @return
     */
    Integer getAvailableCameraIndex(SnmpParam snmpParam,String oid);
}
