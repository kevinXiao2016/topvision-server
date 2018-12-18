/***********************************************************************
 * $Id: UpgradeGlobalParam.java,v1.0 2014年9月23日 下午2:41:32 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年9月23日-下午2:41:32
 * 
 */
@Alias("upgradeGlobalParam")
public class UpgradeGlobalParam implements AliasesSuperType {
    private static final long serialVersionUID = 7864348912124481861L;

    private Long retryInterval;
    private Long writeConfig;
    private Long retryTimes;

    public Long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    public Long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getWriteConfig() {
        return writeConfig;
    }

    public void setWriteConfig(Long writeConfig) {
        this.writeConfig = writeConfig;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpgradeGlobalParam [retryInterval=");
        builder.append(retryInterval);
        builder.append(", writeConfig=");
        builder.append(writeConfig);
        builder.append(", retryTimes=");
        builder.append(retryTimes);
        builder.append("]");
        return builder.toString();
    }

}
