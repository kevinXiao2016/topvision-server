/***********************************************************************
 * $Id: EponTrapCodeDao.java,v1.0 2013-10-26 上午10:07:57 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.dao;

import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2013-10-26-上午10:07:57
 *
 */
public interface EponTrapCodeDao extends BaseEntityDao<Object> {
    
    /**
     * 设备Code与网管Code进行转换
     * 
     * @param trapCode
     * @param type
     * @param module
     * @return
     */
    Integer getEmsCodeFromTrap(Integer trapCode, String module, Integer type);

    /**
     * 获取对应的CCMTS事件code
     * @param trapCode
     * @return
     */
    Integer getRelatedCmtsEventCode(Long trapCode);

}
