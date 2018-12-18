package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.domain.Port;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("portDao")
public class PortDaoImpl extends MyBatisDaoSupport<Port> implements PortDao {

    private Map<String, Port> portCaches = new HashMap<String, Port>();

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#getConnectedPortByIp(java.lang.String)
     */
    @Override
    public Port getConnectedPortByIp(String ip) {
        return getSqlSession().selectOne(getNameSpace("getConnectedPortByIp"), ip);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#getPortByIfIndex(Long, Integer)
     */
    @Override
    public Port getPortByIfIndex(Long entityId, Long ifIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ifIndex", ifIndex);
        String key = entityId + "_" + ifIndex;
        if (!portCaches.containsKey(key)) {
            Port port = getSqlSession().selectOne(getNameSpace("getPortByIfIndex"), map);
            synchronized (portCaches) {
                portCaches.put(key, port);
            }
        }
        return portCaches.get(key);

    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#getPortCoord(Long)
     */
    @Override
    public List<Port> getPortCoord(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getPortCoord"), entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#getPortsByEntityId(Long)
     */
    @Override
    public List<Port> getPortsByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getPortByEntityId"), entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#insertOrUpdateEntity(java.util.List)
     */
    @Override
    public void insertOrUpdateEntity(List<Port> ports) {
        for (Port port : ports) {
            this.insertOrUpdateEntity(port);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#insertOrUpdateEntity(com.topvision.ems.network.domain.Port)
     */
    @Override
    public void insertOrUpdateEntity(Port port) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", port.getEntityId());
        map.put("ifIndex", port.getIfIndex());
        Port obj = getSqlSession().selectOne(getNameSpace("getPortByIfIndex"), map);
        if (obj == null) {
            insertEntity(port);
        } else {
            port.setPortId(obj.getPortId());
            updateEntity(port);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#savePortCoord(java.util.List)
     */
    @Override
    public void savePortCoord(final List<Port> ports) {
        if (ports == null || ports.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = ports.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "savePortCoord", ports.get(i));
            }
            sqlSession.commit();
            for (int i = 0; i < size; i++) {
                Port port = getPortFromDB(ports.get(i));
                String key = port.getPortId() + "_" + port.getIfIndex();
                synchronized (portCaches) {
                    portCaches.put(key, port);
                }
            }
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
     * @see com.topvision.ems.network.dao.PortDao#updatePortOutline(com.topvision.ems.network.domain.Port)
     */
    @Override
    public void updatePortOutline(Port port) {
        getSqlSession().update(getNameSpace() + "updatePortOutline", port);
        String key = port.getPortId() + "_" + port.getIfIndex();
        Port valuePort = getPortFromDB(port);
        synchronized (portCaches) {
            portCaches.put(key, valuePort);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortDao#updatePortStatus(com.topvision.ems.network.domain.Port)
     */
    @Override
    public void updatePortStatus(Port port) {
        getSqlSession().update(getNameSpace() + "updatePortStatus", port);
        String key = port.getPortId() + "_" + port.getIfIndex();
        Port valuePort = getPortFromDB(port);
        synchronized (portCaches) {
            portCaches.put(key, valuePort);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.PortDao#isPortExists(com.topvision.ems.network.domain.Port)
     */
    @Override
    public Long isPortExists(Port port) {
        Long portId = getSqlSession().selectOne(getNameSpace("isPortExists"), port);
        if (portId != null) {
            return portId;
        }
        return null;
    }

    @Override
    public Map<String, Port> getPortCaches() {
        return portCaches;
    }

    @Override
    public void updateEntity(Port port) {
        String key = port.getPortId() + "_" + port.getIfIndex();
        getSqlSession().update(getNameSpace("updateEntity"), port);
        synchronized (portCaches) {
            portCaches.put(key, getPortFromDB(port));
        }
    }

    @Override
    public Port getPortFromDB(Port port) {
        return getSqlSession().selectOne(getNameSpace("selectByPrimaryKey"), port);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.Port";
    }
}
