/***********************************************************************
 * $Id: OltClearCmOnTime.java,v1.0 2017年5月27日 上午10:30:59 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年5月27日-上午10:30:59
 *
 */
public class OltClearCmOnTime implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -6169329151732664415L;

 // 私有MIB中清除离线一段时间的CM的时间设置
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.11.1.0", writable = true, type = "Integer32")
    private Integer oltClearTime;
    // 私有MIB中清除离线一段时间的CM的模式设置：polling和timing
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.11.2.0", writable = true, type = "Integer32")
    private Integer oltClearMode;
    
    public Integer getOltClearTime() {
        return oltClearTime;
    }
    public void setOltClearTime(Integer oltClearTime) {
        this.oltClearTime = oltClearTime;
    }
    public Integer getOltClearMode() {
        return oltClearMode;
    }
    public void setOltClearMode(Integer oltClearMode) {
        this.oltClearMode = oltClearMode;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltClearCmOnTime [oltClearTime=");
        builder.append(oltClearTime);
        builder.append(", oltClearMode=");
        builder.append(oltClearMode);
        builder.append("]");
        return builder.toString();
    }

}
