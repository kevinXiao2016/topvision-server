package com.topvision.ems.network.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.HostService;
import com.topvision.ems.network.dao.HostServiceDao;
import com.topvision.ems.network.domain.Services;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("hostServiceDao")
public class HostServiceDaoImpl extends MyBatisDaoSupport<HostService> implements HostServiceDao {

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.HostServiceDao#getNetworkServices()
     */
    @Override
    public List<Services> getNetworkServices() {
        return getSqlSession().selectList(getNameSpace("getNetworkServices"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.HostServiceDao#setServicesScanned(java.util.List)
     */
    @Override
    public void setServicesScanned(final List<Services> services) {
        if (services == null || services.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = services.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "setServicesScanned", services.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.HostService";
    }
}
