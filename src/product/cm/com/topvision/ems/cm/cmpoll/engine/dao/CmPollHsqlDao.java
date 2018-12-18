/***********************************************************************
 * $Id: CmPollHsqlDao.java,v1.0 2015年3月6日 下午2:29:45 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.engine.dao;

import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;

import java.util.List;

/**
 * @author jay
 * @created @2015年3月6日-下午2:29:45
 *
 */
public interface CmPollHsqlDao {


    /**
     * 保存一个CM轮询结果
     */
    void saveLocalRecord(long time,CmPollResult cmPollResult);

    /**
     * 读取N个结果
     */
    List<CmPollResult> readLocalRecords(Long time,int start,int n);

    /**
     * 创建本轮存储表
     * @param time
     */
    void createRoundTable(Long time);
}
