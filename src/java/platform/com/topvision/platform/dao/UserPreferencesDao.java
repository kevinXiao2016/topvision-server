/**
 *
 */
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.platform.domain.UserPreferences;

/**
 * @author niejun
 */
public interface UserPreferencesDao extends BaseEntityDao<UserPreferences> {

    /**
     * 获取模块配置
     * 
     * @param up
     * @return
     */
    List<UserPreferences> getModulePreferences(UserPreferences up);

    /**
     * 通过用户ID获取用户配置
     * 
     * @param userId
     * @return
     */
    List<UserPreferences> getUserPreferences(Long userId);

    /**
     * 
     * @param userId
     * @param handler
     */
    void handleUserPreferences(Long userId, MyResultHandler handler);

    /**
     * 新建或者更新用户选项
     * 
     * @param up
     */
    void createOrUpdateEntity(UserPreferences up);

    /**
     * 获取某一用户所有的userpreferences
     * 
     * @param userId
     * @return
     */
    List<UserPreferences> getAllUserPerferences(Long userId);

    /**
     * 获取所有用户的userpreferences
     * 
     * @return
     */
    List<UserPreferences> getTotalUserPerferences();

    /**
     * 获取指定用户的指定个性化的值
     * 
     * @param userId
     * @param name
     * @return
     */
    UserPreferences getUserPreference(Long userId, String name);

}
