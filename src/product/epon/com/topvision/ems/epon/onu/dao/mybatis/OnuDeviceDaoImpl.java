/***********************************************************************
 * $Id: OnuPortalDaoImpl.java,v1.0 2015年4月21日 下午4:14:58 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuDeviceDao;
import com.topvision.ems.epon.onu.domain.OnuDeviceInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Administrator
 * @created @2015年4月21日-下午4:14:58
 *
 */
@Repository
public class OnuDeviceDaoImpl extends MyBatisDaoSupport<OnuDeviceInfo> implements OnuDeviceDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.OnuDeviceInfo";
    }

    @Override
    public List<OnuDeviceInfo> selectOnuDeviceList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectOnuDeviceList"), queryMap);
    }

    @Override
    public Integer selectOnuDeviceCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectOnuDeviceCount"), queryMap);
    }

}
