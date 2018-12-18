/***********************************************************************
 * $Id: CmServiceTypeDao.java,v1.0 2016年11月3日 上午10:31:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.dao;

import java.util.List;

import com.topvision.ems.cmc.cm.domain.CmFileNameChangeLog;
import com.topvision.ems.cmc.cm.domain.CmServiceType;

/**
 * @author haojie
 * @created @2016年11月3日-上午10:31:08
 *
 */
public interface CmServiceTypeDao {

    /**
     * 获取CM业务类型字典数据
     * 
     * @return
     */
    List<CmServiceType> getCmServiceTypeList();

    /**
     * 新增CM业务类型
     * 
     * @param cmServiceType
     */
    void insertCmServiceType(CmServiceType cmServiceType);

    /**
     * 获取单个CM业务类型
     * 
     * @param fileName
     * @return
     */
    CmServiceType getCmServiceTypeById(String fileName);

    /**
     * 修改CM业务类型
     * 
     * @param cmServiceType
     */
    void updateCmServiceType(CmServiceType cmServiceType);

    /**
     * 删除CM业务类型
     * 
     * @param fileId
     */
    void deleteCmServiceType(String fileName);

    /**
     * 清除CM业务类型
     */
    void deleteCmServiceType();

    /**
     * 批量插入或更新CM业务类型
     * 
     * @param cmServiceTypes
     */
    void batchInsertOrUpdateCmServiceType(List<CmServiceType> cmServiceTypes);

    /**
     * 获取CM业务类型更改记录
     * @param cmId
     * @return
     */
    List<CmFileNameChangeLog> getlogs(Long cmId);

}
