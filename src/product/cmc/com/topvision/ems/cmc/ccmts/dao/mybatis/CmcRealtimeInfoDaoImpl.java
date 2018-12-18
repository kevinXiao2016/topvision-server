/***********************************************************************
 * $ CmcRealtimeInfoDaoImpl.java,v1.0 14-5-13 上午10:34 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.dao.CmcRealtimeInfoDao;
import com.topvision.ems.cmc.ccmts.domain.CmcRealtimeInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @14-5-13-上午10:34
 */
@Repository("cmcRealtimeInfoDao")
public class CmcRealtimeInfoDaoImpl  extends MyBatisDaoSupport<CmcRealtimeInfo> implements CmcRealtimeInfoDao{
    @Override
    protected String getDomainName() {
        return CmcRealtimeInfo.class.getName();
    }
}
