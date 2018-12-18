/***********************************************************************
 * $Id: TelnetRecord.java,v1.0 2017年1月8日 下午3:57:31 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author vanzand
 * @created @2017年1月8日-下午3:57:31
 *
 */
public class TelnetRecord implements AliasesSuperType{

    private static final long serialVersionUID = 2933293326618497694L;
    private static final DateFormat stTimeFormatter_Daily = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private Long id;
    private String ip;
    private Long userId;
    private String userName;
    private String command;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public String getCreateTimeStr() {
        try{
            String timeStr = stTimeFormatter_Daily.format(createTime);
            return timeStr;
        } catch(Exception e) {
            return "";
        }
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
