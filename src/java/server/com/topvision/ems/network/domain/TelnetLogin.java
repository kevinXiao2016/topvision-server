/***********************************************************************
 * $Id: TelnetLogin.java,v1.0 2014年7月16日 上午9:16:46 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年7月16日-上午9:16:46
 *
 */
@Alias("telnetLogin")
public class TelnetLogin implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -1869828302672148516L;
    
    private Long ip;
    private String ipString;
    private String userName;
    private String password;
    private String enablePassword;
    private Boolean isAAA = false;
    public Long getIp() {
        return ip;
    }
    public void setIp(Long ip) {
        this.ip = ip;
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

    public Boolean getIsAAA() {
        return isAAA;
    }

    public void setIsAAA(Boolean isAAA) {
        this.isAAA = isAAA;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TelnetLogin [ip=");
        builder.append(ip);
        builder.append(", ipString=");
        builder.append(ipString);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", password=");
        builder.append(password);
        builder.append(", enablePassword=");
        builder.append(enablePassword);
        builder.append("]");
        return builder.toString();
    }
    
}
