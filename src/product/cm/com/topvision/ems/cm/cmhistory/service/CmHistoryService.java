/***********************************************************************
 * $Id: CmHistoryServiceImpl.java,v1.0 2015年4月9日 下午8:38:46 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.cmhistory.domain.CmHistoryShow;

/**
 * @author YangYi
 * @created @2015年4月9日-下午8:38:46
 * 
 */
public interface CmHistoryService {

    /**
     * 获取CM历史数据列表
     * 
     * @param queryMap
     * @return
     */
    List<CmHistoryShow> getCmHistory(Map<String, Object> queryMap);

    // TODO Test 准备删除
    void insert1WData();

    // TODO Test 准备删除
    void insert3600WData();
}
