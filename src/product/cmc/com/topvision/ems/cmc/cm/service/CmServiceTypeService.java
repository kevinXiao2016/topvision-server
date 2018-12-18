/***********************************************************************
 * $Id: CmServiceTypeService.java,v1.0 2016年11月3日 上午10:28:47 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service;

import java.util.List;

import com.topvision.ems.cmc.cm.domain.CmFileNameChangeLog;
import com.topvision.ems.cmc.cm.domain.CmServiceType;

/**
 * @author haojie
 * @created @2016年11月3日-上午10:28:47
 *
 */
public interface CmServiceTypeService {

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
    void addCmServiceType(CmServiceType cmServiceType);

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
    void modifyCmServiceType(CmServiceType cmServiceType);

    /**
     * 删除CM业务类型
     * 
     * @param fileName
     */
    void deleteCmServiceType(String fileName);

    /**
     * 批量导入CM业务类型
     * 
     * @param cmServiceTypes
     * @param deleteStatus
     */
    void addCmServiceTypes(List<CmServiceType> cmServiceTypes, Boolean deleteStatus);

    /**
     * 获取CM业务类型更改记录
     * @param cmId
     * @return
     */
    List<CmFileNameChangeLog> getlogs(Long cmId);

}
