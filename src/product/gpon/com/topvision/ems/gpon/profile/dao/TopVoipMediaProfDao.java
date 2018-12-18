/***********************************************************************
 * $Id: TopVoipMediaProfDao.java,v1.0 2017年6月21日 上午9:30:14 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;

/**
 * @author haojie
 * @created @2017年6月21日-上午9:30:14
 *
 */
public interface TopVoipMediaProfDao {

    List<TopVoipMediaProfInfo> selectTopVoipMediaProfInfoList(Long entityId);

    TopVoipMediaProfInfo selectTopVoipMediaProfInfo(Long entityId, Integer profileId);

    void insertTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo);

    void updateTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo);

    void deleteTopVoipMediaProfInfo(Long entityId, Integer profileId);
}
