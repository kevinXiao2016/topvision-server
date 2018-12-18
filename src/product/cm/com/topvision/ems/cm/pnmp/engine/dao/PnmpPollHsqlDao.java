/***********************************************************************
 * $Id: CmPollHsqlDao.java,v1.0 2015年3月6日 下午2:29:45 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.dao;



import java.util.List;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;

/**
 * @author jay
 * @created @2015年3月6日-下午2:29:45
 *
 */
public interface PnmpPollHsqlDao {


    /**
     * 保存一个CM轮询结果
     */
    void saveLocalRecord(long time, PnmpPollResult pnmpPollResult, PnmpPollTask pnmpPollTask);

    /**
     * 读取N个结果
     */
    List<PnmpPollResult> readLocalRecords(Long time, int start, int n, PnmpPollTask pnmpPollTask);

    /**
     * 创建本轮存储表
     * @param time
     * @param pnmpPollTask
     */
    void createRoundTable(Long time, PnmpPollTask pnmpPollTask);
}
