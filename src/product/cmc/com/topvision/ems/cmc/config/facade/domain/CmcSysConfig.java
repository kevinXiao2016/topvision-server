/***********************************************************************
 * $Id: CmcSysConfig.java,v1.0 2013-7-22 下午4:28:37 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-7-22-下午4:28:37
 * 
 */
@Alias("cmcSysConfig")
public class CmcSysConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -272957506207427951L;
    private Long cmcId;
    @SnmpProperty(oid="1.3.6.1.2.1.2.2.1.1", index=true)
    private Long ifIndex;
    @SnmpProperty(oid="1.3.6.1.4.1.32285.11.1.1.2.18.1.1.1", writable=true, type="Integer32")
    private Integer topCcmtsSysCfgPiggyback;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getTopCcmtsSysCfgPiggyback() {
        return topCcmtsSysCfgPiggyback;
    }

    public void setTopCcmtsSysCfgPiggyback(Integer topCcmtsSysCfgPiggyback) {
        this.topCcmtsSysCfgPiggyback = topCcmtsSysCfgPiggyback;
    }

}
