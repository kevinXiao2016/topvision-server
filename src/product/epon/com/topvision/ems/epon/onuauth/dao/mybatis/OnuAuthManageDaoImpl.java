/***********************************************************************
 * $Id: OnuAuthManageDaoImpl.java,v1.0 2015年4月20日 上午11:44:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onuauth.dao.OnuAuthManageDao;
import com.topvision.ems.epon.onuauth.domain.OltOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.domain.OnuAuthInfo;
import com.topvision.ems.epon.onuauth.domain.PonOnuAuthStatistics;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author loyal
 * @created @2015年4月20日-上午11:44:22
 * 
 */
@Repository("onuAuthManageDao")
public class OnuAuthManageDaoImpl extends MyBatisDaoSupport<Object> implements OnuAuthManageDao {

    @Override
    public List<OnuAuthInfo> selectOnuAuthList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "selectOnuAuthList", queryMap);
    }
    
    @Override
    public Long selectOnuAuthListCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "selectOnuAuthListCount", queryMap);
    }

    @Override
    public List<OnuAuthFail> selectOnuAuthFailList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "selectOnuAuthFailList", queryMap);
    }
    
    @Override
    public Long selectOnuAuthFailListCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "selectOnuAuthFailListCount", queryMap);
    }

    @Override
    public List<OltOnuAuthStatistics> selectOltOnuAuthStatistics(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "selectOltOnuAuthStatistics", queryMap);
    }
    
    @Override
    public Long selectOltOnuAuthStatisticsCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "selectOltOnuAuthStatisticsCount", queryMap);
    }

    @Override
    public List<PonOnuAuthStatistics> selectPonOnuAuthStatistics(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "selectPonOnuAuthStatistics", entityId);
    }
    
    @Override
    public List<Long> getOnuAuthIndex(Long entityId, Long ponIndex, Integer authAction) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ponIndex", ponIndex);
        map.put("authAction", authAction);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthIndex", map);
    }

    @Override
    protected String getDomainName() {
        return OnuAuthInfo.class.getName();
    }

}
