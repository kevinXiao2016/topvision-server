/***********************************************************************
 * $Id: GponTrafficProfileDao.java,v1.0 2016年12月20日 上午11:20:26 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.framework.dao.Dao;

/**
 * @author haojie
 * @created @2016年12月20日-上午11:20:26
 *
 */
public interface GponTrafficProfileDao extends Dao {

    /**
     * 获取流量模板列表
     * 
     * @param entityId
     * @return
     */
    List<GponTrafficProfileInfo> selectGponTrafficProfileInfoList(Long entityId);

    /**
     * 获取单个流量模板信息
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    GponTrafficProfileInfo selectGponTrafficProfileInfo(Long entityId, Integer profileId);

    /**
     * 新增流量模板
     * 
     * @param gponTrafficProfileInfo
     */
    void insertGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo);

    /**
     * 修改流量模板
     * 
     * @param gponTrafficProfileInfo
     */
    void updateGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo);

    /**
     * 删除流量模板
     * @param entityId
     * @param gponTrafficProfileId
     */
    void deleteGponTrafficProfileInfo(Long entityId, Integer gponTrafficProfileId);

}
