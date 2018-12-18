/***********************************************************************
 * $Id: OnuSlaService.java,v1.0 2013年10月25日 下午5:49:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.service;

import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:49:29
 *
 */
public interface OnuSlaService extends Service {

    /**
     * 获取OLT下所有ONU的sla
     * @param entityId
     */
    void refreshOnuSla(Long entityId);

    /**
     * 获取单个onu的sla
     * @param entityId
     * @param onuIndex
     */
    void refreshOnuSla(Long entityId, Long onuIndex);
}
