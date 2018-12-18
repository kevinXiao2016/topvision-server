/***********************************************************************
 * $Id: NoticeExpireJob.java,v1.0 2013年9月25日 上午11:03:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.mgmt;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.billboard.domain.Notice;

/**
 * 公告超期自动移除公告队列任务
 * @author Bravin
 * @created @2013年9月25日-上午11:03:34
 *
 */
public class NoticeExpireJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(NoticeExpireJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        /*这里本来是计划在notice中增加jobKey来对应的，每一个有效的公告都有一个对应的超期失效任务，通过jobKey来建立 job与notice
                          之间的对应关系。但是考虑到jobDataMap中存储的也是对象引用，删除的时候直接通过引用来删除其实例，而不是通过对应关系找到其实例后删除，所以才有后者*/
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Notice notice = (Notice) jobDataMap.get("notice");
        BillBoardController billBoardController = (BillBoardController) jobDataMap
                .get("billBoardController");
        try {
            billBoardController.removeExpireNotice(notice);
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

}
