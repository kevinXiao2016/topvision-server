/***********************************************************************
 * $Id: CachedThreshholdAlertService.java,v1.0 2016年8月13日 下午2:31:15 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.util.List;

import com.topvision.ems.fault.domain.Alert;

/**
 * @author Bravin
 * @created @2016年8月13日-下午2:31:15
 *
 */
public interface CachedThresholdAlertService {

    Float takePrevAlertValue(Long entityId, Integer alertEventId, String source);

    void cacheThresholdAlertValue(Long entityId, Integer alertEventId, String source, Float value, Integer levelId);

    void removeThresholdAlertValue(Long entityId, Integer alertEventId, String source);

    boolean alertKeyExist(Long entityId, Integer alertEventId, String source);

    Integer takePrevAlertLevelId(Long entityId, Integer alertEventId, String source);

    void removeThresholdAlertValue(List<Alert> list);

}
