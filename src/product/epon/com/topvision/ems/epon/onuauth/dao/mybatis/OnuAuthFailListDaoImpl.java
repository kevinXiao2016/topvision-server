/***********************************************************************
 * $Id: OnuAuthFailDaoImpl.java,v1.0 2015年4月18日 上午10:38:31 $
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

import com.topvision.ems.epon.onuauth.dao.OnuAuthFailListDao;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2015年4月18日-上午10:38:31
 * 
 */
@Repository("onuAuthFailDao")
public class OnuAuthFailListDaoImpl extends MyBatisDaoSupport<Object> implements OnuAuthFailListDao {

    @Override
    public List<OnuAuthFail> selectOnuAuthFailList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectOnuAuthFailList"), queryMap);
    }

    @Override
    public Long selectOnuAuthFailCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("selectOnuAuthFailCount"), queryMap);
    }

    @Override
    public OnuAuthFail getOnuAuthFailObject(Long entityId, Long onuIndex) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("getOnuAuthFailObject"), param);
    }

    @Override
    protected String getDomainName() {
        return OnuAuthFail.class.getName();
    }

}
