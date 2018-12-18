/***********************************************************************
 * $Id: CmHistoryEngineDao.java,v1.0 2015年4月14日 下午3:44:51 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.engine.dao;

import com.topvision.ems.cm.cmhistory.engine.domain.CmHistory;

/**
 * @author YangYi
 * @created @2015年4月14日-下午3:44:51
 * 
 */
public interface CmHistoryEngineDao {
    /**
     * 插入一条CM历史数据
     * 
     * @param c
     */
    void insertCmHistory(CmHistory c);

    /**
     * 插入指定CM列表中CM的更新数据
     * @param c
     */
    void insertSpecifiedCmListLast(CmHistory c);
}
