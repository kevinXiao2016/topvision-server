/**
 *
 */
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author niejun
 */
public interface SystemPreferencesDao extends BaseEntityDao<SystemPreferences> {

    /**
     * 删除一个module的系统配置
     * 
     * @param module
     */
    void deleteByModule(String module);

    /**
     * 通过model获取系统配置
     * 
     * @param module
     * @return
     */
    List<SystemPreferences> selectByModule(String module);

    /**
     * 通过model和name获取系统配置
     * 
     * @param module
     * @param name
     * @return
     */
    SystemPreferences selectByModuleAndName(String module, String name);
    
    Object getCache();
    
    int getCacheSize();
    
    void clearCache();

}
