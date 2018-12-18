/***********************************************************************
 * $Id: UserPreferencesService.java,v 1.1 Sep 17, 2008 1:15:57 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;
import java.util.Properties;

import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;

/**
 * @author kelers
 * @Create Date Sep 17, 2008 1:15:57 PM
 */
public interface UserPreferencesService {

    /**
     * 通过用户配置获取模块属性
     * 
     * @param up
     * @return
     */
    Properties getModulePreferences(UserPreferences up);

    /**
     * 用于批量保存某一模块的属性
     * 
     * @param module
     * @param userId
     * @param props
     */
    void batchSaveModulePreferences(String module, Long userId, Properties props);


    /**
     * 保存一个用户配置项
     * @param key
     * @param module
     * @param value
     * @param uc 
     */
    void saveModulePreferences(String key, String module, Object value, UserContext uc);

    /**
     * 获取某一用户所有的userpreferences
     * @param userId
     * @return 
     */
    List<UserPreferences> getAllUserPerferences(Long userId);

    /**
     * 获取指定用户的指定个性化的值
     * @param userId
     * @param name
     * @return
     */
    UserPreferences getUserPreference(Long userId, String name);
}
