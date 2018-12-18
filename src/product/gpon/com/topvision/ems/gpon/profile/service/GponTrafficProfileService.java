/***********************************************************************
 * $Id: GponTrafficProfileService.java,v1.0 2016年12月17日 上午9:00:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;

/**
 * @author haojie
 * @created @2016年12月17日-上午9:00:44
 *
 */
public interface GponTrafficProfileService {

    /**
     * 获取流量模板列表数据
     * 
     * @param entityId
     * @return
     */
    List<GponTrafficProfileInfo> loadGponTrafficProfileInfoList(Long entityId);

    /**
     * 获取单个流量模板信息
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    GponTrafficProfileInfo loadGponTrafficProfileInfo(Long entityId, Integer profileId);

    /**
     * 添加流量模板
     * 
     * @param gponTrafficProfileInfo
     */
    void addGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo);

    /**
     * 修改流量模板
     * 
     * @param gponTrafficProfileInfo
     */
    void modifyGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo);

    /**
     * 删除流量模板
     * 
     * @param entityId
     * @param gponTrafficProfileId
     */
    void deleteGponTrafficProfileInfo(Long entityId, Integer gponTrafficProfileId);

    /**
     * 流量模板列表从设备获取数据
     * 
     * @param entityId
     */
    void refreshGponTrafficProfileList(Long entityId);

}
