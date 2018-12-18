/***********************************************************************
 * $Id: GponDbaProfileService.java,v1.0 2016年12月17日 上午9:00:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;

/**
 * @author haojie
 * @created @2016年12月17日-上午9:00:08
 *
 */
public interface GponDbaProfileService {

    /**
     * 获取DBA模板列表数据
     * 
     * @param entityId
     * @return
     */
    List<GponDbaProfileInfo> loadGponDbaProfileInfoList(Long entityId);

    /**
     * 获取单个DBA模板 数据
     * @param entityId
     * @param profileId
     * @return
     */
    GponDbaProfileInfo loadGponDbaProfileInfo(Long entityId, Integer profileId);

    /**
     * 添加DBA模板
     * 
     * @param gponDbaProfileInfo
     */
    void addGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo);

    /**
     * 修改DBA模板
     * 
     * @param gponDbaProfileInfo
     */
    void modifyGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo);

    /**
     * 刪除DBA模板
     * 
     * @param entityId
     * @param gponDbaProfileId
     */
    void deleteGponDbaProfileInfo(Long entityId, Integer gponDbaProfileId);

    /**
     * DBA模板列表从设备获取数据
     * 
     * @param entityId
     */
    void refreshGponDbaProfileList(Long entityId);

}
