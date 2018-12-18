/***********************************************************************
 * $Id: BillBoardController.java,v1.0 2013年9月25日 下午5:28:44 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.mgmt;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;

import com.topvision.ems.billboard.domain.Notice;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年9月25日-下午5:28:44
 *
 */
public interface BillBoardController extends Service {

    /**
     * 调度一个公告
     * @param notice
     * @throws ParseException 
     */
    void dispatchNotice(Notice notice) throws ParseException;

    /**
     * 加载所有的有效公告
     * @param userId
     * @return 
     */
    List<Notice> loadAllValidNotice(Long userId);

    /**
     * 删除一个公告，异常超期任务，从队列中移除
     * @param noticeId
     * @throws SchedulerException
     */
    void removeExpireNotice(Integer noticeId) throws SchedulerException;

    /**
     * 使某一个公告超期，从队列中移除，修改DB中的状态
     * @param notice
     * @throws SchedulerException
     */
    void removeExpireNotice(Notice notice) throws SchedulerException;

    /**
     * 清除一个公告
     * @param noticeId
     */
    void clearNoticeById(Integer noticeId);

}
