package com.topvision.ems.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.EventDao;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository("eventDao")
public class EventDaoImpl extends MyBatisDaoSupport<Event> implements EventDao {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.dao.EventDao#isAlertClearByEvent(com.topvision.ems.fault.domain.Event
     * )
     */
    public Boolean isAlertClearByEvent(Event event) {
        Integer flag = getSqlSession().selectOne(getNameSpace("isAlertClearByEvent"), event.getTypeId());
        if (flag == null || flag == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Event> queryEventList(Map<String, String> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryEventList"), map);
    }

    @Override
    public List<AlertType> getEventType() {
        return getSqlSession().selectList(getNameSpace("getEventType"));
    }

    @Override
    public int queryEventListSize(Map<String, String> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryEventListSize"), map);
    }

    @Override
    public void deleteEvent(int eventId) {
        getSqlSession().delete(getNameSpace() + "deleteEvent", eventId);
    }

    @Override
    public void batchDeleteEvent(String eventIds) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eventIds", eventIds);
        this.getSqlSession().delete(getNameSpace("batchDeleteEvent"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.Event";
    }
}
