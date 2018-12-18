package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.GoogleConstants;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

@Service("systemPreferencesService")
public class SystemPreferencesServiceImpl extends BaseService implements SystemPreferencesService {
    private boolean usedFirstly;
    private boolean usedApplet = true;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao = null;
    private boolean allowIpBindLogon = false;
    private boolean allowRepeatedlyLogon = false;
    private boolean stopUserWhenErrors = false;
    private int stopUserWhenErrorNumber = 6;
    private boolean checkPasswdComplex = false;
    private boolean menubarEnabled = true;
    private static Object lock = new Object();
    // lock time default 30 minutes
    private Integer lockTime = 30;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemService#getModulePreferences
     * (java.lang.String)
     */
    @Override
    public Properties getModulePreferences(String module) {
        Properties props = new Properties();
        List<SystemPreferences> list = systemPreferencesDao.selectByModule(module);
        for (SystemPreferences pref : list) {
            if (pref != null && pref.getName() != null && pref.getValue() != null) {
                props.put(pref.getName(), pref.getValue());
            }
        }
        return props;
    }

    @Override
    public Integer getStopUserWhenErrorNumber() {
        return stopUserWhenErrorNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.core.service.DefaultService#
     * getSystemPreferences(java.lang.String)
     */
    @Override
    public SystemPreferences getSystemPreference(String name) {
        return systemPreferencesDao.selectByPrimaryKey(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemService#getSystemPreferences
     * (java.lang.String)
     */
    @Override
    public List<SystemPreferences> getSystemPreferences(String module) {
        return systemPreferencesDao.selectByModule(module);
    }

    @Override
    public List<SystemPreferences> getSystemThemes() {
        return getSystemPreferences("theme");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        // 是否第一次访问
        SystemPreferences preference = null;
        try {
            preference = systemPreferencesDao.selectByPrimaryKey("used.firstly");
        } catch (DataAccessException e) {
            getLogger().error("Load System Preferences...[used.firstly]", e);
        }
        if (preference != null) {
            usedFirstly = "true".equalsIgnoreCase(preference.getValue());
        }

        // 是否激活menubar
        try {
            preference = systemPreferencesDao.selectByPrimaryKey("menubar");
        } catch (DataAccessException e) {
            getLogger().error("Load System Preferences...[menubar]", e);
        }
        if (preference != null) {
            menubarEnabled = "true".equalsIgnoreCase(preference.getValue());
        }

        // 是否使用Applet显示实时性能
        try {
            preference = systemPreferencesDao.selectByPrimaryKey("applet");
        } catch (DataAccessException e) {
            getLogger().error("Load System Preferences...[applet]", e);
        }
        if (preference != null) {
            usedApplet = "true".equalsIgnoreCase(preference.getValue());
        }

        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        try {
            preferences = getSystemPreferences("logon");
        } catch (DataAccessException e) {
            getLogger().error("Load System Preferences...[logon]", e);
        }
        for (int i = 0; i < preferences.size(); i++) {
            preference = preferences.get(i);
            if ("allowIpBindLogon".equalsIgnoreCase(preference.getName())) {
                allowIpBindLogon = "true".equalsIgnoreCase(preference.getValue());
            } else if ("allowRepeatedlyLogon".equalsIgnoreCase(preference.getName())) {
                allowRepeatedlyLogon = "true".equalsIgnoreCase(preference.getValue());
            } else if ("checkPasswdComplex".equalsIgnoreCase(preference.getName())) {
                checkPasswdComplex = "true".equalsIgnoreCase(preference.getValue());
            } else if ("stopUserWhenErrors".equalsIgnoreCase(preference.getName())) {
                stopUserWhenErrors = "true".equalsIgnoreCase(preference.getValue());
            } else if ("stopUserWhenErrorNumber".equalsIgnoreCase(preference.getName())) {
                try {
                    stopUserWhenErrorNumber = Integer.parseInt(preference.getValue());
                } catch (NumberFormatException e) {
                    continue;
                }
            } else if ("lockTime".equalsIgnoreCase(preference.getName())) {
                try {
                    lockTime = Integer.parseInt(preference.getValue());
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        List<SystemPreferences> unitPres = new ArrayList<SystemPreferences>();
        try {
            unitPres = getSystemPreferences("unit");
        } catch (DataAccessException e) {
            getLogger().error("Load System Preferences...[unit]", e);
        }
        for (SystemPreferences unitPre : unitPres) {
            UnitConfigConstant.add(unitPre.getName(), unitPre.getValue());
        }
    }

    @Override
    public Boolean isAllowIpBindLogon() {
        return allowIpBindLogon;
    }

    @Override
    public Boolean isAllowRepeatedlyLogon() {
        return allowRepeatedlyLogon;
    }

    @Override
    public Boolean isCheckPasswdComplex() {
        return checkPasswdComplex;
    }

    @Override
    public Boolean isMenubarEnabled() {
        return menubarEnabled;
    }

    @Override
    public Boolean isStopUserWhenErrors() {
        return stopUserWhenErrors;
    }

    @Override
    public Boolean isUsedApplet() {
        return usedApplet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemPreferencesService#isUsedFirstly ()
     */
    @Override
    public Boolean isUsedFirstly() {
        return usedFirstly;
    }

    @Override
    public void saveGoogleLocation(Double latitude, Double longitude, Byte zoom) {
        synchronized (lock) {
            systemPreferencesDao.deleteByModule(GoogleConstants.GOOGLE_LOC_MODULE);
            List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
            SystemPreferences preference = new SystemPreferences();
            preference.setName("google.latitude");
            preference.setModule(GoogleConstants.GOOGLE_LOC_MODULE);
            preference.setValue(String.valueOf(latitude));
            preferences.add(preference);

            preference = new SystemPreferences();
            preference.setName("google.longitude");
            preference.setModule(GoogleConstants.GOOGLE_LOC_MODULE);
            preference.setValue(String.valueOf(longitude));
            preferences.add(preference);

            preference = new SystemPreferences();
            preference.setName("google.zoom");
            preference.setModule(GoogleConstants.GOOGLE_LOC_MODULE);
            preference.setValue(String.valueOf(zoom));
            preferences.add(preference);
            systemPreferencesDao.insertEntity(preferences);
        }
    }

    @Override
    public void saveGoogleMapKey(String url, String key, String provider) {
        synchronized (lock) {
            systemPreferencesDao.deleteByModule(GoogleConstants.GOOGLE_KEY_MODULE);
            List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
            SystemPreferences preference = new SystemPreferences();
            preference.setName("google.url");
            preference.setValue(url);
            preference.setModule(GoogleConstants.GOOGLE_KEY_MODULE);
            preferences.add(preference);

            preference = new SystemPreferences();
            preference.setName("google.key");
            preference.setValue(key);
            preference.setModule(GoogleConstants.GOOGLE_KEY_MODULE);
            preferences.add(preference);

            preference = new SystemPreferences();
            preference.setName("google.provider");
            preference.setValue(provider);
            preference.setModule(GoogleConstants.GOOGLE_KEY_MODULE);
            preferences.add(preference);
            systemPreferencesDao.insertEntity(preferences);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemService#savePreferences(java .util.List)
     */
    @Override
    public void savePreferences(List<SystemPreferences> preferences) {
        synchronized (lock) {
            systemPreferencesDao.deleteByPrimaryKey(preferences);
            systemPreferencesDao.insertEntity(preferences);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemService#savePreferences(java .lang.String,
     * java.util.List)
     */
    @Override
    public void savePreferences(String module, List<SystemPreferences> preferences) {
        synchronized (lock) {
            systemPreferencesDao.deleteByModule(module);
            systemPreferencesDao.insertEntity(preferences);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemService#savePreferences(java .lang.String,
     * java.util.Properties)
     */
    @Override
    public void savePreferences(String module, Properties props) {
        List<SystemPreferences> list = new ArrayList<SystemPreferences>(props.size());
        for (Object key : props.keySet()) {
            if (key != null && key instanceof String && props.getProperty((String) key) != null) {
                SystemPreferences up = new SystemPreferences();
                up.setModule(module);
                up.setName((String) key);
                up.setValue(props.getProperty((String) key));
                list.add(up);
            }
        }
        synchronized (lock) {
            systemPreferencesDao.deleteByModule(module);
            systemPreferencesDao.insertEntity(list);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.common.service.DefaultService#
     * savePreferences(com.topvision.ems.server.common.domain.Preferences)
     */
    @Override
    public void savePreferences(SystemPreferences preferences) {
        if (systemPreferencesDao.selectByPrimaryKey(preferences.getName()) == null) {
            systemPreferencesDao.insertEntity(preferences);
        } else {
            systemPreferencesDao.updateEntity(preferences);
        }
    }

    @Override
    public void setAllowIpBindLogon(Boolean allowIpBindLogon) {
        this.allowIpBindLogon = allowIpBindLogon;
    }

    @Override
    public void setAllowRepeatedlyLogon(Boolean allowRepeatedlyLogon) {
        this.allowRepeatedlyLogon = allowRepeatedlyLogon;
    }

    @Override
    public void setCheckPasswdComplex(Boolean checkPasswdComplex) {
        this.checkPasswdComplex = checkPasswdComplex;
    }

    @Override
    public void setStopUserWhenErrorNumber(Integer stopUserWhenErrorNumber) {
        this.stopUserWhenErrorNumber = stopUserWhenErrorNumber;
    }

    @Override
    public void setStopUserWhenErrors(Boolean stopUserWhenErrors) {
        this.stopUserWhenErrors = stopUserWhenErrors;
    }

    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    @Override
    public void setUsedApplet(Boolean usedApplet) {
        this.usedApplet = usedApplet;
    }

    public Integer getLockTime() {
        return lockTime;
    }

    public void setLockTime(Integer lockTime) {
        this.lockTime = lockTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemPreferencesService#setUsedFirstly (boolean)
     */
    @Override
    public void updateUsedFirstly(Boolean usedFirstly) {
        this.usedFirstly = usedFirstly;
        if (!usedFirstly) {
            SystemPreferences preferences = new SystemPreferences();
            preferences.setName("used.firstly");
            preferences.setValue("false");
            preferences.setModule("core");
            systemPreferencesDao.updateEntity(preferences);
        }
    }

    @Override
    public void updateUnitConfig(List<SystemPreferences> preferences) {
        synchronized (lock) {
            savePreferences(preferences);
            // 20160726修改,切换单位显示后不再立即生效,需要重启系统服务才生效
            /*
             * for (SystemPreferences unitPre : preferences) {
             * UnitConfigConstant.add(unitPre.getName(), unitPre.getValue()); }
             */
        }
    }

    @Override
    public SystemPreferences selectByModuleAndName(String module, String name) {
        return systemPreferencesDao.selectByModuleAndName(module, name);
    }

    @Override
    public Object getCache() {
        return systemPreferencesDao.getCache();
    }

    @Override
    public int getCacheSize() {
        return systemPreferencesDao.getCacheSize();
    }

    @Override
    public void clearCache() {
        systemPreferencesDao.clearCache();
    }

}
