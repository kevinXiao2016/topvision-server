/***********************************************************************
 * $Id: EntityPassword.java,v1.0 2014年5月19日 上午9:41:44 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年5月19日-上午9:41:44
 *
 */
@Alias("entityPassword")
public class EntityPassword implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 3007951022458176352L;

    private Long ip;
    private String ipString;
    private String userName;
    private String password;
    private String enablePassword;
    public Long getIp() {
        return ip;
    }
    public void setIp(Long ip) {
        this.ip = ip;
        this.ipString = new IpUtils(ip).toString();
    }
    public String getIpString() {
        return ipString;
    }
    public void setIpString(String ipString) {
        this.ipString = ipString;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEnablePassword() {
        return enablePassword;
    }
    public void setEnablePassword(String enablePassword) {
        this.enablePassword = enablePassword;
    }
    
}
