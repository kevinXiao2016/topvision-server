package com.topvision.ems.network.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Config;
import com.topvision.ems.network.dao.ConfigurationDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("configurationDao")
public class ConfigurationDaoImpl extends MyBatisDaoSupport<Config> implements ConfigurationDao {

    public boolean isUpLoadAccess(Long entityId) {
        int configStatus = (Integer) getSqlSession().selectOne(getNameSpace("isUpLoadAccess"), entityId);
        return configStatus == 2 || configStatus == 3; // To change body of
                                                       // implemented methods
                                                       // use File | Settings |
                                                       // File Templates.
    }

    public void downLoadConfig(Long entityId) {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    public List<Config> getDownLoadConfig(Long entityId) {
        return null; // To change body of implemented methods use File |
                     // Settings | File Templates.
    }

    public List<Config> getUpLoadConfig(Long entityId) {
        return null; // To change body of implemented methods use File |
                     // Settings | File Templates.
    }

    public boolean isDownLoadAccess(Long entityId) {
        return false; // To change body of implemented methods use File |
                      // Settings | File Templates.
    }

    public void upLoadConfig(Long entityId) {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.Configuration";
    }
}