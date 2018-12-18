/***********************************************************************
 * $Id: CmcClearCmOnTime.java,v1.0 2017年5月23日 上午10:07:28 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年5月23日-上午10:07:28
 *
 */
@Alias("cmcClearCmOnTime")
public class CmcClearCmOnTime implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 469796290499635675L;
    // 私有MIB中清除离线一段时间的CM的时间设置
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.11.1.0", writable = true, type = "Integer32")
    private Integer cmcClearTime;
    // 私有MIB中清除离线一段时间的CM的模式设置：polling和timing
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.11.2.0", writable = true, type = "Integer32")
    private Integer cmcClearMode;

    public Integer getCmcClearTime() {
        return cmcClearTime;
    }

    public void setCmcClearTime(Integer cmcClearTime) {
        this.cmcClearTime = cmcClearTime;
    }

    public Integer getCmcClearMode() {
        return cmcClearMode;
    }

    public void setCmcClearMode(Integer cmcClearMode) {
        this.cmcClearMode = cmcClearMode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcClearCmOnTime [cmcClearTime=");
        builder.append(cmcClearTime);
        builder.append(", cmcClearMode=");
        builder.append(cmcClearMode);
        builder.append("]");
        return builder.toString();
    }

}
