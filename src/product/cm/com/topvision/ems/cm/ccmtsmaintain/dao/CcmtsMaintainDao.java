/***********************************************************************
 * $Id: CcmtsMaintainDao.java,v1.0 2015-5-28 上午10:50:39 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.dao;

import java.util.List;

import com.topvision.ems.cm.ccmtsmaintain.domain.CcmtsMaintain;

/**
 * @author fanzidong
 * @created @2015-5-28-上午10:50:39
 *
 */
public interface CcmtsMaintainDao {
    /**
     * 批量插入CCMTS运维数据
     * 
     * @param dispersions
     */
    void batchInsertMaintainData(List<CcmtsMaintain> ccmtsMaintains);
}
