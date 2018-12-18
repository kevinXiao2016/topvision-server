/***********************************************************************
 * $Id: SpectrumDiscoveryService.java,v1.0 2014-3-5 上午9:57:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

/**
 * @author haojie
 * @created @2014-3-5-上午9:57:50
 *
 */
public interface SpectrumDiscoveryService {

    /**
     * 刷新Ccmts设备的频谱配置
     * @param entity
     */
    void refreshSpectrumCmts(Long entityId);
    
    /**
     * 刷新Olt设备的频谱开关
     */
    void refreshSpectrumOlt(Long entityId);
}
