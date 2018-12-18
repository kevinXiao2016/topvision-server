package com.topvision.ems.network.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.PortPerfDao;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("portPerfDao")
public class PortPerfDaoImpl extends MyBatisDaoSupport<PortPerf> implements PortPerfDao {

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortPerfDao#getPortStatusByEntityId(Long)
     */
    @Override
    public List<Port> getPortStatusByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getPortStatusByEntityId"), entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.PortPerfDao#updatePortStatus(java.util.List)
     */
    @Override
    public void updatePortStatus(final List<Port> ports) {
        if (ports == null || ports.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (Port port : ports) {
                sqlSession.update(getNameSpace() + "updatePortStatus", port);
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
     * @see com.topvision.ems.network.dao.PortPerfDao#updatePortStatus(com.topvision.ems.network.domain.Port)
     */
    @Override
    public void updatePortStatus(Port port) {
        getSqlSession().update(getNameSpace() + "updatePortStatus", port);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.PortPerf";
    }
}
