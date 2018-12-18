/***********************************************************************
 * $Id: BillBoardService.java,v1.0 2013年9月24日 上午11:24:36 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.service;

import java.text.ParseException;
import java.util.List;

import com.topvision.ems.billboard.domain.Notice;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年9月24日-上午11:24:36
 *
 */
public interface BillBoardService extends Service {

    /**
     *  创建一个新的公告
     * @param notice
     * @throws ParseException 
     */
    void createNotice(Notice notice) throws ParseException;

    /**
     * 加载所有的有效公告
     * @param userId
     * @return 
     */
    List<Notice> loadAllValidNotice(Long userId);

    /**
     * 清除一个公告
     * @param noticeId
     */
    void clearNoticeById(Integer noticeId);

}
