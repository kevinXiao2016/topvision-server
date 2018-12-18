/***********************************************************************
 * $Id: TopDigitMapProfDao.java,v1.0 2017年6月21日 下午1:35:00 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;

/**
 * @author haojie
 * @created @2017年6月21日-下午1:35:00
 *
 */
public interface TopDigitMapProfDao {

    List<TopDigitMapProfInfo> selectTopDigitMapProfInfoList(Long entityId);

    TopDigitMapProfInfo selectTopDigitMapProfInfo(Long entityId, Integer profileId);

    void insertTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo);

    void updateTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo);

    void deleteTopDigitMapProfInfo(Long entityId, Integer profileId);
}
