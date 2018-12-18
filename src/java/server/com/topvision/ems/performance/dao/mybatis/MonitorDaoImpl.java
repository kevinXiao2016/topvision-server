package com.topvision.ems.performance.dao.mybatis;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.performance.dao.MonitorDao;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("monitorDao")
public class MonitorDaoImpl extends MyBatisDaoSupport<Monitor> implements MonitorDao {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#existMonitor(Long, java.lang.String)
     */
    @Override
    public Boolean existMonitor(Long entityId, String category) {
        Monitor dm = new Monitor();
        dm.setEntityId(entityId);
        dm.setCategory(category);
        return getSqlSession().selectOne(getNameSpace("existMonitor"), dm) != null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#getAllMonitor()
     */
    @Override
    public List<Monitor> getAllMonitor() {
        return getSqlSession().selectList(getNameSpace("getAllMonitor"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#getMonitorByEntity(Long)
     */
    @Override
    public List<Monitor> getMonitorByEntity(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getMonitorByEntity"), entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#getMonitorByEntityType(Integer)
     */
    @Override
    public List<Monitor> getMonitorByEntityType(Integer type) {
        return getSqlSession().selectList(getNameSpace("getMonitorByEntityType"), type);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#getMonitorByType(java.lang.String)
     */
    @Override
    public List<Monitor> getMonitorByType(String type) {
        return getSqlSession().selectList(getNameSpace("getMonitorByType"), type);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#getLastValuesByMonitor(Long)
     */
    @Override
    public List<Monitor> getMonitorByCategory(String category) {
        return getSqlSession().selectList(getNameSpace("getMonitorByCategory"), category);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#getLastValuesByMonitor(Long)
     */
    @Override
    public List<MonitorValue> getLastValuesByMonitor(Long monitorId) {
        return getSqlSession().selectList(getNameSpace("getLastValuesByMonitor"), monitorId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#insertConnectivity(com.topvision.ems.network.domain.MonitorValue)
     */
    @Override
    public void insertConnectivity(MonitorValue value) {
        getSqlSession().insert(getNameSpace() + "insertConnectivity", value);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#insertConnectivity(java.util.List)
     */
    @Override
    public void insertConnectivity(final List<MonitorValue> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = values.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "insertConnectivity", values.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#insertValue(com.topvision.ems.network.domain.MonitorValue)
     */
    @Override
    public void insertValue(MonitorValue value) {
        getSqlSession().insert(getNameSpace() + "insertValue", value);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#insertValue(java.util.List)
     */
    @Override
    public void insertValue(final List<MonitorValue> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = values.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "insertValue", values.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#updatePollingInterval(java.util.List, Long,
     *      Long)
     */
    @Override
    public void updatePollingInterval(final List<Long> ids, final Long intervalOfNormal, final Long intervalAfterError) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (Long id : ids) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("monitorId", id);
                params.put("intervalOfNormal", intervalOfNormal);
                params.put("intervalAfterError", intervalAfterError);
                sqlSession.update(getNameSpace() + "updatePollingInterval", params);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#updatePollingInterval(java.util.Map)
     */
    @Override
    public void updatePollingInterval(Map<String, String> map) {
        getSqlSession().update(getNameSpace() + "batchUpdatePollingInterval", map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MonitorDao#updateStatus(java.util.List, Boolean)
     */
    @Override
    public void updateStatus(final List<Long> ids, final Boolean status) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (Long id : ids) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("monitorId", id);
                params.put("enabled", status ? 1 : 0);
                sqlSession.update(getNameSpace() + "updateStatus", params);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.MonitorDao#updateStatus(java.lang.Long,
     * java.lang.Boolean)
     */
    @Override
    public void updateStatus(Long id, Boolean status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monitorId", id);
        params.put("enabled", status ? 1 : 0);
        getSqlSession().update(getNameSpace("updateStatus"), params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.MonitorDao#updateHealthy(com.topvision.ems.performance.
     * domain.Monitor)
     */
    @Override
    public void updateHealthy(Monitor monitor) {
        getSqlSession().update(getNameSpace() + "updateHealthy", monitor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.domain.Monitor";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.MonitorDao#insertPerfConnectivity(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public void insertPerfConnectivity(Long entityId, Integer delay, Timestamp collectTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("collectValue", delay);
        params.put("collectTime", collectTime);
        getSqlSession().update(getNameSpace() + "insertPerfConnectivity", params);
    }
}
