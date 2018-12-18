/***********************************************************************
 * $Id: GponDbaProfileDao.java,v1.0 2016年12月20日 上午10:48:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;
import com.topvision.framework.dao.Dao;

/**
 * @author haojie
 * @created @2016年12月20日-上午10:48:50
 *
 */
public interface GponDbaProfileDao extends Dao {

    /**
     * 获取DBA模板列表
     * 
     * @param entityId
     * @return
     */
    List<GponDbaProfileInfo> selectGponDbaProfileInfoList(Long entityId);

    /**
     * 获取单个DBA模板
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    GponDbaProfileInfo selectGponDbaProfileInfo(Long entityId, Integer profileId);

    /**
     * 新增DBA模板
     * 
     * @param gponDbaProfileInfo
     */
    void insertGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo);

    /**
     * 修改DBA模板
     * 
     * @param gponDbaProfileInfo
     */
    void updateGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo);

    /**
     * 删除DBA模板
     * 
     * @param entityId
     * @param gponDbaProfileId
     */
    void deleteGponDbaProfileInfo(Long entityId, Integer gponDbaProfileId);

}
