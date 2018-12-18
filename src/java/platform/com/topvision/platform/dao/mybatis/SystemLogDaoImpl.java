/**
 *
 */
package com.topvision.platform.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.SystemLogDao;
import com.topvision.platform.domain.SystemLog;

/**
 * @author niejun
 */
@Repository("systemLogDao")
public class SystemLogDaoImpl extends MyBatisDaoSupport<SystemLog> implements SystemLogDao {
    @Override
    public String getDomainName() {
        return SystemLog.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.SystemLogDao#getUserLogByParams(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public List<SystemLog> getUserLogByParams(Integer start, Integer limit) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("getUserLogByParams"), paramMap);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.SystemLogDao#getUserLogCounts()
     */
    @Override
    public List<SystemLog> getUserLogCounts() {
        return getSqlSession().selectList(getNameSpace("getAllUserLog"));
    }
}
