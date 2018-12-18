/**
 *
 */
package com.topvision.platform.service;

import java.util.List;
import java.util.Properties;

import com.topvision.framework.service.Service;
import com.topvision.platform.EmsCacheService;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author niejun
 */
public interface SystemPreferencesService extends Service, EmsCacheService {

    /**
     * 获取模块的系统配置
     * 
     * @param module
     *            模块名
     * @return Properties
     */
    Properties getModulePreferences(String module);

    /**
     * 得到允许用户密码输入错误的次数.
     * 
     * @return
     */
    Integer getStopUserWhenErrorNumber();

    /**
     * 获取系统配置
     * 
     * @param name
     * @return SystemPreferences
     */
    SystemPreferences getSystemPreference(String name);

    /**
     * 获取模块的所有系统配置
     * 
     * @param module
     *            模块名
     * @return List<SystemPreferences>
     */
    List<SystemPreferences> getSystemPreferences(String module);

    /**
     * 获得系统的所有系统配置
     * 
     * @return
     */
    List<SystemPreferences> getSystemThemes();

    /**
     * 是否允许通过IP绑定登录.
     * 
     * @return
     */
    Boolean isAllowIpBindLogon();

    /**
     * 是否允许单个用户重复登录.
     * 
     * @return
     */
    Boolean isAllowRepeatedlyLogon();

    /**
     * 是否进行密码强度检测.
     * 
     * @return
     */
    Boolean isCheckPasswdComplex();

    /**
     * 是否显示menubar
     * 
     * @return
     */
    Boolean isMenubarEnabled();

    /**
     * 是否在密码连续输入错误的时候停用该用户.
     * 
     * @return
     */
    Boolean isStopUserWhenErrors();

    /**
     * 是否使用Applet
     * 
     * @return
     */
    Boolean isUsedApplet();

    /**
     * 是否地一次使用本系统.
     * 
     * @return
     */
    Boolean isUsedFirstly();

    /**
     * @param latitude
     * @param longitude
     * @param zoom
     */
    void saveGoogleLocation(Double latitude, Double longitude, Byte zoom);

    /**
     * 保存 Google key.
     * 
     * @param url
     * @param key
     */
    void saveGoogleMapKey(String url, String key, String provider);

    /**
     * 批量保存系统配置.
     * 
     * @param preferences
     */
    void savePreferences(List<SystemPreferences> preferences);

    /**
     * 批量保存某个模块的系统配置.
     * 
     * @param preferences
     */
    void savePreferences(String module, List<SystemPreferences> preferences);

    /**
     * 保存一个系统配置
     * 
     * @param module
     * @param props
     */
    void savePreferences(String module, Properties props);

    /**
     * 保存设置.
     * 
     * @param preferences
     */
    void savePreferences(SystemPreferences preferences);

    /**
     * 设置是否允许IP绑定登录
     * 
     * @param allowIpBindLogon
     */
    void setAllowIpBindLogon(Boolean allowIpBindLogon);

    /**
     * 设置是否允许单个用户重复登录
     * 
     * @param allowRepeatedlyLogon
     */
    void setAllowRepeatedlyLogon(Boolean allowRepeatedlyLogon);

    /**
     * 设置是否允许密码强度检测
     * 
     * @param checkPasswdComplex
     */
    void setCheckPasswdComplex(Boolean checkPasswdComplex);

    /**
     * .设置多少次密码输入错误后停用用户
     * 
     * @param stopUserWhenErrorNumber
     */
    public void setStopUserWhenErrorNumber(Integer stopUserWhenErrorNumber);

    /**
     * 设置是否在密码连续输入错误的时候停用该用户
     * 
     * @param stopUserWhenErrors
     */
    void setStopUserWhenErrors(Boolean stopUserWhenErrors);

    /**
     * 设置是否使用Applet
     * 
     * @param applet
     */
    void setUsedApplet(Boolean applet);

    /**
     * 更新第一次使用系统标志.
     * 
     * @param firstly
     */
    void updateUsedFirstly(Boolean firstly);
    
    /**
     * 获得锁定时长
     * @return
     */
    public Integer getLockTime();
    /**
     * 设置锁定时长
     * @param lockTime
     */
    public void setLockTime(Integer lockTime);

    /**
     * 更新单位显示配置
     * 同时需要更新内存中的缓存单位配置
     * @param preferences
     */
    public void updateUnitConfig(List<SystemPreferences> preferences);
    
    /**
     * 通过model和name获取系统配置
     * 
     * @param module
     * @param name
     * @return
     */
    SystemPreferences selectByModuleAndName(String module, String name);

}
