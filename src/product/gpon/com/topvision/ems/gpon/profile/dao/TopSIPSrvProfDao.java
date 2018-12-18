/***********************************************************************
 * $Id: TopSIPSrvProfDao.java,v1.0 2017年6月21日 上午10:22:15 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;

/**
 * @author haojie
 * @created @2017年6月21日-上午10:22:15
 *
 */
public interface TopSIPSrvProfDao {

    List<TopSIPSrvProfInfo> selectTopSIPSrvProfInfoList(Long entityId);

    TopSIPSrvProfInfo selectTopSIPSrvProfInfo(Long entityId, Integer profileId);

    void insertTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo);

    void updateTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo);

    void deleteTopSIPSrvProfInfo(Long entityId, Integer profileId);
}
