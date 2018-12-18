/**
 *
 */
package com.topvision.platform.dao.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author niejun
 */
@Repository("systemPreferencesDao")
public class SystemPreferencesDaoImpl extends MyBatisDaoSupport<SystemPreferences> implements SystemPreferencesDao {

    private ConcurrentHashMap<String, List<SystemPreferences>> caches = new ConcurrentHashMap<String, List<SystemPreferences>>();

    @Override
    public String getDomainName() {
        return SystemPreferences.class.getName();
    }

    @Override
    public void deleteByPrimaryKey(List<?> preferences) {
        if (preferences == null || preferences.size() == 0) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<SystemPreferences> systemPreferences = (List<SystemPreferences>) preferences;
        List<String> moduleNames = new ArrayList<String>();
        SqlSession session = getBatchSession();
        try {
            for (SystemPreferences systemPreference : systemPreferences) {
                if (!moduleNames.contains(systemPreference.getModule())) {
                    moduleNames.add(systemPreference.getModule());
                }
                session.delete(getNameSpace("deleteByPrimaryKey"), systemPreference);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        for (String module : moduleNames) {
            caches.remove(module);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.dao.SystemPreferencesDao#deleteByModule( java.lang.String)
     */
    @Override
    public void deleteByModule(String module) {
        if (module == null) {
            return;
        }
        getSqlSession().delete(getNameSpace("deleteByModule"), module);
        caches.remove(module);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.SystemPreferencesDao#selectByModule(java.lang .String)
     */
    @Override
    public List<SystemPreferences> selectByModule(String module) {
        if (module == null) {
            return null;
        }
        List<SystemPreferences> systemPreferences = null;

        if (caches.containsKey(module)) {
            systemPreferences = caches.get(module);
            if (systemPreferences == null || systemPreferences.isEmpty()) {// 某些未知原因导致cache中的value为空集合，此时应更新缓存
                systemPreferences = getSqlSession().selectList(getNameSpace("selectByModule"), module);
                caches.put(module, systemPreferences);
            }
        } else {
            systemPreferences = getSqlSession().selectList(getNameSpace("selectByModule"), module);
            caches.put(module, systemPreferences);
        }

        if (systemPreferences == null) {
            return null;
        }

        // modify by fanzidong, 避免多线程竞争资源，并且避免外部对caches内容进行修改。每次请求返回copy对象
        List<SystemPreferences> retList = new ArrayList<SystemPreferences>();
        for (SystemPreferences systemPreference : systemPreferences) {
            retList.add(systemPreference);
        }
        return new CopyOnWriteArrayList<>(retList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.SystemPreferencesDao#selectByModule(java.lang.String)
     */
    @Override
    public SystemPreferences selectByModuleAndName(String module, String name) {
        List<SystemPreferences> systemPreferences = selectByModule(module);
        SystemPreferences ret = null;
        for (SystemPreferences preference : systemPreferences) {
            if (preference.getName().equals(name)) {
                ret = preference;
                break;
            }
        }
        return ret;
    }

    @Override
    public void updateEntity(SystemPreferences preferences) {
        getSqlSession().update(getNameSpace("updateEntity"), preferences);
        caches.remove(preferences.getModule());
    }

    @Override
    public void updateEntity(List<SystemPreferences> preferences) {
        SqlSession session = getBatchSession();
        List<String> moduleNames = new ArrayList<String>();
        try {
            for (SystemPreferences systemPreference : preferences) {
                if (!moduleNames.contains(systemPreference.getModule())) {
                    moduleNames.add(systemPreference.getModule());
                }
                session.update(getNameSpace("updateEntity"), systemPreference);
                session.commit();
            }
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        for (String module : moduleNames) {
            caches.remove(module);
        }
    }

    @Override
    public Object getCache() {
        return caches;
    }

    @Override
    public int getCacheSize() {
        return caches.size();
    }

    @Override
    public void clearCache() {
        caches.clear();
    }

}
