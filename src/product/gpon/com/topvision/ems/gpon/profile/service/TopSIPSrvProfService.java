/***********************************************************************
 * $Id: TopSIPSrvProfService.java,v1.0 2017年6月21日 上午10:11:52 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;

/**
 * @author haojie
 * @created @2017年6月21日-上午10:11:52
 *
 */
public interface TopSIPSrvProfService {

    List<TopSIPSrvProfInfo> loadTopSIPSrvProfInfoList(Long entityId);

    TopSIPSrvProfInfo loadTopSIPSrvProfInfo(Long entityId, Integer profileId);

    void addTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo);

    void modifyTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo);

    void deleteTopSIPSrvProfInfo(Long entityId, Integer profileId);

    void refreshTopSIPSrvProfInfo(Long entityId);
}
