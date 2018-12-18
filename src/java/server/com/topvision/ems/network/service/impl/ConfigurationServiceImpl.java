package com.topvision.ems.network.service.impl;

import java.util.List;

import com.topvision.ems.facade.domain.Config;
import com.topvision.ems.network.dao.ConfigurationDao;
import com.topvision.ems.network.service.ConfigurationService;
import com.topvision.framework.service.BaseService;

public class ConfigurationServiceImpl extends BaseService implements ConfigurationService {
    private static ConfigurationServiceImpl service = new ConfigurationServiceImpl();
    private ConfigurationDao configurationDao = null;

    public ConfigurationDao getConfigurationDao() {
        return configurationDao;
    }

    public void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    public static ConfigurationServiceImpl getInstance() {
        return service;
    }

    /**
     * 是否有配置没有上传
     * 
     * @param entityId
     * @return
     */
    public boolean isUpLoadAccess(Long entityId) {
        return configurationDao.isUpLoadAccess(entityId); // To change body of
                                                          // implemented
                                                          // methods use File
                                                          // | Settings | File
                                                          // Templates.
    }

    /**
     * 是否有配置没有同步
     * 
     * @param entityId
     * @return
     */
    public boolean isDownLoadAccess(Long entityId) {
        return configurationDao.isDownLoadAccess(entityId); // To change body of
                                                            // implemented
                                                            // methods use File
                                                            // | Settings | File
                                                            // Templates.
    }

    /**
     * 获取需要上传的设备配置
     * 
     * @param entityId
     * @return
     */
    public List<Config> getUpLoadConfig(Long entityId) {
        return configurationDao.getUpLoadConfig(entityId); // To change body of
                                                           // implemented
                                                           // methods use File
                                                           // | Settings | File
                                                           // Templates.
    }

    /**
     * 获取需要同步的设备配置
     * 
     * @param entityId
     * @return
     */
    public List<Config> getDownLoadConfig(Long entityId) {
        return configurationDao.getDownLoadConfig(entityId); // To change body
                                                             // of
                                                             // implemented
                                                             // methods use
                                                             // File |
                                                             // Settings |
                                                             // File
                                                             // Templates.
    }

    /**
     * 上传配置
     * 
     * @param entityId
     * @param configs
     * @return
     */
    public void upLoadConfig(Long entityId, List<Config> configs) {
        configurationDao.upLoadConfig(entityId); // To change body of
                                                 // implemented methods use
                                                 // File | Settings | File
                                                 // Templates.
    }

    /**
     * 同步配置
     * 
     * @param entityId
     * @param configs
     * @return
     */
    public void downLoadConfig(Long entityId, List<Config> configs) {
        configurationDao.downLoadConfig(entityId); // To change body of
                                                   // implemented methods use
                                                   // File | Settings | File
                                                   // Templates.
    }
}