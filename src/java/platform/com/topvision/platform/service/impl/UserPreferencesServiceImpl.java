/***********************************************************************
 * $Id: UserPreferencesServiceImpl.java,v 1.1 Sep 17, 2008 1:18:37 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.UserPreferencesDao;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserPreferencesMap;
import com.topvision.platform.service.UserPreferencesService;

@Service("userPreferencesService")
public class UserPreferencesServiceImpl extends BaseService implements UserPreferencesService {
    @Autowired
    private UserPreferencesDao userPreferencesDao;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.UserPreferencesService#
     *      getModulePreferences(com.topvision.platform.domain.server.system.domain.UserPreferences)
     */
    @Override
    public Properties getModulePreferences(UserPreferences up) {
        Properties props = new Properties();
        List<UserPreferences> list = userPreferencesDao.getModulePreferences(up);
        for (UserPreferences pref : list) {
            if (pref != null) {
                props.put(pref.getName(), pref.getValue());
            }
        }
        return props;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.UserPreferencesService#
     *      batchSaveModulePreferences(java.lang.String, Long, java.util.Properties)
     */
    @Override
    public void batchSaveModulePreferences(String module, Long userId, Properties props) {
        List<UserPreferences> list = new ArrayList<UserPreferences>(props.size());
        for (Object key : props.keySet()) {
            UserPreferences up = new UserPreferences();
            up.setModule(module);
            up.setUserId(userId);
            up.setName((String) key);
            up.setValue(props.getProperty((String) key));
            list.add(up);
        }
        userPreferencesDao.insertEntity(list);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.service.UserPreferencesService#saveModulePreferences(java.lang.String, java.lang.String, java.lang.Object, com.topvision.platform.domain.UserContext)
     */
    @Override
    public void saveModulePreferences(String key, String module, Object value, UserContext uc) {
        UserPreferences up = new UserPreferences();
        up.setUserId(uc.getUserId());
        up.setModule(module);
        up.setName(key);
        up.setValue(value.toString());
        userPreferencesDao.createOrUpdateEntity(up);
        //更新UserContext
        UserPreferencesMap<String, String> userPreferencesMap = uc.getUserPreferencesMap();
        userPreferencesMap.put(module.concat(".").concat(key), value.toString());
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.service.UserPreferencesService#getAllUserPerferences(java.lang.Long)
     */
    @Override
    public List<UserPreferences> getAllUserPerferences(Long userId) {
        return userPreferencesDao.getAllUserPerferences(userId);
    }

    @Override
    public UserPreferences getUserPreference(Long userId, String name) {
        return userPreferencesDao.getUserPreference(userId, name);
    }
}
