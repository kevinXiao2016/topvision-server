/***********************************************************************
 * $Id: TelnetLoginDao.java,v1.0 2014年7月16日 上午9:11:37 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2014年7月16日-上午9:11:37
 *
 */
public interface TelnetLoginDao extends BaseEntityDao<TelnetLogin>{
    
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
     * 批量插入设备登录密码，如存在则不添加
     * @param ip
     * @return
     */
    public void batchInsertTelnetLogin(List<TelnetLogin> importTelnetLogins);
    
    /**
     * 批量插入设备登录密码，如存在则删除后再添加
     * @param ip
     * @return
     */
    public void batchInsertOrUpdateTelnetLogin(List<TelnetLogin> importTelnetLogins);
}
