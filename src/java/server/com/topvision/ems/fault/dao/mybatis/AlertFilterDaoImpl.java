package com.topvision.ems.fault.dao.mybatis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.AlertFilterDao;
import com.topvision.ems.fault.domain.AlertFilter;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("alertFilterDao")
public class AlertFilterDaoImpl extends MyBatisDaoSupport<AlertFilter> implements AlertFilterDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertFilterDao#deleteAlertFilterByType(java .lang.Integer)
     */
    @Override
    public void deleteAlertFilterByType(Integer type) {
        getSqlSession().delete(getNameSpace("deleteAlertFilterByType"), type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertFilterDao#getAlertFilterByType(java. lang.Integer)
     */
    @Override
    public List<AlertFilter> getAlertFilterByType(Integer type) {
        return getSqlSession().selectList(getNameSpace("getAlertFilterByType"), type);
    }

    @Override
    public List<AlertFilter> getAlertFilter(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("getAlertFilter"), queryMap);
    }

    @Override
    public Integer getAlertFilterCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("getAlertFilterCount"), queryMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.AlertFilter";
    }

}
