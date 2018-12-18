/***********************************************************************
 * $Id: CommonConfigDao.java,v1.0 2014年7月22日 下午7:32:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2014年7月22日-下午7:32:06
 *
 */
public interface CommonConfigDao extends BaseEntityDao<SendConfigEntity>{
    
    /**
     * 获取配置
     * @param type
     * @return
     */
    List<String> getCommonConfigs(Long type);
    
    /**
     * 获取配置
     * @param type
     * @param folderId
     * @return
     */
    List<String> getCommonConfigs(Long type, Long folderId);
    
    /**
     * 清除配置
     * @param type
     */
    void clearCommonConfig(Long type);
    
    /**
     * 清除配置
     * @param type
     * @param folderId
     */
    void clearCommonConfig(Long type, Long folderId);
    
    /**
     * 添加配置
     * @param config
     * @param type
     */
    void addCommonConfig(String config, Long type);
    
    /**
     * 添加配置
     * @param config
     * @param type
     * @param folderId
     */
    void addCommonConfig(String config, Long type, Long folderId);
    
    /**
     * 添加配置
     * @param config
     * @param type
     */
    void addCommonConfig(List<String> configs, Long type);
    
    /**
     * 添加配置
     * @param configs
     * @param type
     * @param folderId
     */
    void addCommonConfig(List<String> configs, Long type, Long folderId);

}
