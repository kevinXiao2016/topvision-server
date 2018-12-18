/*********************************************************************** 
 * $Id: CmPollConfigDao.java,v1.0 2017年1月14日 下午8:19:46 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.config.dao;


import java.util.List;

/**
 * @author jay
 * @created @2017年1月14日-下午8:19:46
 * 
 */
public interface CmPollConfigDao {
    /**
     * 批量插入指定CM列表
     * @param macList
     */
    void batchInsertSpecifiedCmList(List<String> macList);

    /**
     * 获取所有指定CM列表
     * @return
     */
    List<String> selectSpecifiedCmList();
}
