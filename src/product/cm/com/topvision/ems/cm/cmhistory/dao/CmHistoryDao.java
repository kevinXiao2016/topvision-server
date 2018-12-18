/*********************************************************************** 
 * $Id: CmHistoryDao.java,v1.0 2015年4月9日 下午8:19:46 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.cmhistory.domain.CmHistoryShow;
import com.topvision.ems.cm.cmhistory.engine.domain.CmHistory;


/**
 * @author YangYi
 * @created @2015年4月9日-下午8:19:46
 * 
 */
public interface CmHistoryDao {
    // TODO 刪除 批量插入数据
    void batchInsertCmHistory(List<CmHistory> CmHistoryList);
    
    /**
     * 获取CM历史数据列表
     * 
     * @param queryMap
     * @return
     */
    List<CmHistoryShow> getCmHistory(Map<String, Object> queryMap);
}
