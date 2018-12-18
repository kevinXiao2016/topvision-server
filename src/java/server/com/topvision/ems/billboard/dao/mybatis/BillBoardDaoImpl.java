/***********************************************************************
 * $Id: BillBoardDaoImpl.java,v1.0 2013年9月24日 上午11:30:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.billboard.dao.BillBoardDao;
import com.topvision.ems.billboard.domain.Notice;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2013年9月24日-上午11:30:47
 *
 */
@Repository("billBoardDao")
public class BillBoardDaoImpl extends MyBatisDaoSupport<Notice> implements BillBoardDao {

    @Override
    protected String getDomainName() {
        //return Notice.class.getName();
        return "com.topvision.ems.billboard.domain.BillBoard.";
    }

    @Override
    public void insertNotice(Notice notice) {
        getSqlSession().insert(getDomainName() + "insertNotice", notice);
        //set username
        Notice notice2 = getSqlSession().selectOne(getDomainName() + "selectByPrimaryKey", notice.getNoticeId());
        notice.setUsername(notice2.getUsername());
    }

    @Override
    public void expireNotice(Notice notice, boolean status) {
        notice.setStatus(status);
        getSqlSession().update(getDomainName() + "expireNotice", notice);
    }

    @Override
    public List<Notice> selectAllValidNotice() {
        return getSqlSession().selectList(getDomainName() + "selectAllValidNotice");
    }

}
