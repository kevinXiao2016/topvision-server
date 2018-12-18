/**
 *
 */
package com.topvision.platform.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.platform.dao.UserPreferencesDao;
import com.topvision.platform.domain.UserPreferences;

/**
 * @author niejun
 */
@Repository("userPreferencesDao")
public class UserPreferencesDaoImpl extends MyBatisDaoSupport<UserPreferences> implements UserPreferencesDao {
    @Override
    public String getDomainName() {
        return UserPreferences.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.dao.UserPreferencesDao#getModulePreferences *
     * (com.topvision.platform.domain.UserPreferences)
     */
    @Override
    public List<UserPreferences> getModulePreferences(UserPreferences up) {
        return getSqlSession().selectList(getNameSpace("getModulePreferences"), up);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserPreferencesDao#getUserPreferences(java .lang.Long)
     */
    @Override
    public List<UserPreferences> getUserPreferences(Long userId) {
        return getSqlSession().selectList(getNameSpace("getUserPreferences"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserPreferencesDao#handleUserPreferences(java .lang.Long,
     * com.topvision.framework.event.RowHandler)
     */
    @Override
    public void handleUserPreferences(Long userId, MyResultHandler handler) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        selectWithRowHandler(getNameSpace("handleUserPreferences"), map, handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#insertEntity(java .util.List)
     */
    @Override
    public void insertEntity(List<UserPreferences> ups) {
        for (UserPreferences up : ups) {
            createOrUpdateEntity(up);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.dao.UserPreferencesDao#createOrUpdateEntity(com.topvision.platform
     * .domain.UserPreferences)
     */
    @Override
    public void createOrUpdateEntity(UserPreferences up) {
        Object selectOne = getSqlSession().selectOne(getNameSpace("queryEntityExisted"), up);
        if (selectOne != null) {
            getSqlSession().update(getNameSpace("updateEntity"), up);
        } else {
            getSqlSession().insert(getNameSpace("insertEntity"), up);

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserPreferencesDao#getAllUserPerferences(java.lang.Long)
     */
    @Override
    public List<UserPreferences> getAllUserPerferences(Long userId) {
        return getSqlSession().selectList(getNameSpace("getAllUserPerferences"), userId);
    }

    @Override
    public List<UserPreferences> getTotalUserPerferences() {
        return getSqlSession().selectList(getNameSpace("getTotalUserPerferences"));
    }

    @Override
    public UserPreferences getUserPreference(Long userId, String name) {
        UserPreferences preferences = new UserPreferences();
        preferences.setUserId(userId);
        preferences.setName(name);
        return getSqlSession().selectOne(getNameSpace("getUserPreference"), preferences);
    }

}
