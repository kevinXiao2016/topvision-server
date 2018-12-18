/***********************************************************************
 * $Id: TelnetLoginService.java,v1.0 2014年7月16日 上午9:08:28 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年7月16日-上午9:08:28
 *
 */
public interface TelnetLoginService extends Service {
    /**
     * 获取全部telnet 登录配置
     * @param map
     * @return
     */
    public List<TelnetLogin> getTelnetLoginConfig(Map<String, Object> map);

    /**
     * 获取全部telnet 登录配置数量
     * @return
     */
    public Long getTelnetLoginConfigCount(Map<String, Object> map);

    /**
     * 删除telnet 登录配置
     * @param ip
     */
    public void deleteTelnetLogin(Long ip);

    /**
     * 添加telnet 登录配置
     * @param telnetLogin
     */
    public void addTelnetLogin(TelnetLogin telnetLogin);

    /**
     * 修改telnet 登录配置
     * @param telnetLogin
     */
    public void modifyTelnetLogin(TelnetLogin telnetLogin);

    /**
     * 获取telnet 登录配置
     * @param ip
     * @return
     */
    public TelnetLogin getTelnetLoginConfigByIp(Long ip);

    /**
     * 获取telnet 登录配置
     * @param ip
     * @return
     */
    public TelnetLogin txGetTelnetLoginConfigByIp(Long ip);

    /**
     * 获取全局密码
     * @return
     */
    public TelnetLogin getGlobalTelnetLogin();

    /**
     * 获取全局密码
     * @return
     */
    public TelnetLogin txGetGlobalTelnetLogin();

    /**
     * 修改全局密码
     * @param telnetLogin
     */
    public void modifyGlobalTelnetLogin(TelnetLogin telnetLogin);

    /**
     * 导出为excel
     * @param telnetLogins
     */
    public void exportToExcel(List<TelnetLogin> telnetLogins);

    /**
     * 导入设备登录密码
     * @param importTelnetLogins
     * @param overwrite
     * @return
     */
    void importTelnetLogin(List<TelnetLogin> importTelnetLogins, boolean overwrite);

    /**
     * 导入设备登录密码
     * @param importTelnetLogins
     * @param overwrite
     * @return
     */
    public TelnetLogin getEntityTelnetLogin(String ip);
}
