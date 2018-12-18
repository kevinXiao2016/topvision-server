/***********************************************************************
 * $Id: CcmtsChannelListReportDaoImpl.java,v1.0 2013-10-29 上午8:47:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtschannellist.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.ccmtschannellist.dao.CcmtsChannelListReportDao;
import com.topvision.ems.cmc.report.domain.CcmtsChannelUsageDetail;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-上午8:47:29
 * 
 */
@Repository("ccmtsChannelListReportDao")
public class CcmtsChannelListReportDaoImpl extends MyBatisDaoSupport<Entity> implements CcmtsChannelListReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CcmtsChannelListReport";
    }

    @Override
    public List<CcmtsChannelUsageDetail> getCcmtsChannelUsageReport(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getCcmtsChannelUsageReport"), queryMap);
    }

}
