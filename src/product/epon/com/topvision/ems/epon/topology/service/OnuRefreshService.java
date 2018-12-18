/***********************************************************************
 * $Id: OnuRefreshService.java,v1.0 2015-8-8 下午2:54:39 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service;


/**
 * @author Rod John
 * @created @2015-8-8-下午2:54:39
 *
 */
public interface OnuRefreshService {

    /**
     * Refresh Single ONU
     * 
     * @param entityId
     * @param onuIndex
     * @param onuId
     */
    void refreshOnu(Long entityId, Long onuIndex, Long onuId);

    /**
     * Auto Refresh Single ONU
     * 
     * @param entityId
     * @param onuIndex
     * @param onuId
     */
    void autoRefreshOnu(Long entityId, Long onuIndex, Long onuId);
}
