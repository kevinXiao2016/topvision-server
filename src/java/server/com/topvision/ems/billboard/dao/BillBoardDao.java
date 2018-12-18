/***********************************************************************
 * $Id: BillBoardDao.java,v1.0 2013年9月24日 上午11:28:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.dao;

import java.util.List;

import com.topvision.ems.billboard.domain.Notice;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年9月24日-上午11:28:43
 *
 */
public interface BillBoardDao extends BaseEntityDao<Notice> {

    /**
     * 插入一个新的公告
     * @param notice
     */
    void insertNotice(Notice notice);

    /**
     * 设置某一个公告的状态
     * @param notice
     */
    void expireNotice(Notice notice, boolean status);

    /**
     * 获取所有的有效的公告
     * @return
     */
    List<Notice> selectAllValidNotice();

}
