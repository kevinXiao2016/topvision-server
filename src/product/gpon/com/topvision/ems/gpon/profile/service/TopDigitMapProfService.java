/***********************************************************************
 * $Id: TopDigitMapProfService.java,v1.0 2017年6月21日 下午1:26:32 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;

/**
 * @author haojie
 * @created @2017年6月21日-下午1:26:32
 *
 */
public interface TopDigitMapProfService {

    List<TopDigitMapProfInfo> loadTopDigitMapProfInfoList(Long entityId);

    TopDigitMapProfInfo loadTopDigitMapProfInfo(Long entityId, Integer profileId);

    void addTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo);

    void modifyTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo);

    void deleteTopDigitMapProfInfo(Long entityId, Integer profileId);

    void refreshTopDigitMapProfInfo(Long entityId);
}
