package com.topvision.ems.fault.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.EventTypeDao;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("eventTypeDao")
public class EventTypeDaoImpl extends MyBatisDaoSupport<EventType> implements EventTypeDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.EventDao#getEventType()
     */
    @Override
    public List<EventType> getEventType(Map<String, String> filter) {
        return getSqlSession().selectList(getNameSpace("getEventType"), filter);
    }

    @Override
    public List<EventType> selectEventByType(Map<String, String> filter) {
        return null;
    }

    @Override
    public List<EventType> getAllEventType() {
        return getSqlSession().selectList(getNameSpace("getAllEventType"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.EventType";
    }
}
