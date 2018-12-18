/***********************************************************************
 * $Id: OnuReplaceDao.java,v1.0 2016-4-18 上午11:29:07 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2016-4-18-上午11:29:07
 *
 */
public interface OnuReplaceDao extends BaseEntityDao<Entity> {

    /**
     * getOnuMacListByEntityId
     * 
     * @param entityId
     * @return
     */
    Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId);
    
    
    /**
     * getOnuSnListByEntityId
     * 
     * @param entityId
     * @return
     */
    Map<String, Map<String, Object>> getOnuSnListByEntityId(Long entityId);

    /**
     * getOnuAttributeByMacAddress
     * 
     * @param entityId
     * @param mac
     * @return
     */
    OltOnuAttribute getOnuAttributeByMacAddress(Long entityId, String mac);
    
    /**
     * getOnuAttributeBySn
     * 
     * @param entityId
     * @param sn
     * @return
     */
    OltOnuAttribute getOnuAttributeBySn(Long entityId, String sn);
    
    /**
     * getPonAuthType
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    Integer getPonAuthType(Long entityId, Long onuIndex);

    
    /**
     * 替换ONU后同步ONU信息
     * 
     * @param entityId
     * @param onuIndex
     * @param onuId
     * @param macAddress
     * @param sn
     * @param pwd
     */
    void syncOnuInfoAfterReplace(Long entityId, Long onuIndex, Long onuId, String macAddress, String sn, String pwd);
    
}
