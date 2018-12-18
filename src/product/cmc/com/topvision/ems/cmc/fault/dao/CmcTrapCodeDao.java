/***********************************************************************
 * $Id: CmcTrapCodeDao.java,v1.0 2013-4-24 上午09:24:15 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.dao;

/**
 * @author Rod John
 * @created @2013-4-24-上午09:24:15
 *
 */
public interface CmcTrapCodeDao {

    /**
     * 设备Code与网管Code进行转换
     * 
     * @param trapCode
     * @return
     */
    Integer getEmsCodeFromTrap(Long trapCode);
    
    Boolean isCcmtsOnline(Long entityId, Long cmcIndex);
    
}
