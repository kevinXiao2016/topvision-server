/***********************************************************************
 * $Id: TopSIPAgentProfDao.java,v1.0 2017年5月5日 下午1:59:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;

/**
 * @author haojie
 * @created @2017年5月5日-下午1:59:29
 *
 */
public interface TopSIPAgentProfDao {

    List<TopSIPAgentProfInfo> selectTopSIPAgentProfInfoList(Long entityId);

    TopSIPAgentProfInfo selectTopSIPAgentProfInfo(Long entityId, Integer profileId);

    void insertTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo);

    void updateTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo);

    void deleteTopSIPAgentProfInfo(Long entityId, Integer profileId);

}
