/***********************************************************************
 * $Id: OltBoardReportDaoImpl.java,v1.0 2013-10-26 上午9:45:00 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltboard.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.report.oltboard.dao.OltBoardReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-26-上午9:45:00
 * 
 */
@Repository("oltBoardReportDao")
public class OltBoardReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltBoardReportDao {

    @Override
    public List<EponBoardStatistics> getBoardList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getEponBoardList", map);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltBoardReport";
    }

}
