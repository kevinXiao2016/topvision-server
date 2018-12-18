/***********************************************************************
 * $Id: CmcChannelCommonTable.java,v1.0 2016年5月9日 下午5:02:29 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2016年5月9日-下午5:02:29
 * 
 */
@Alias("cmcChannelCommonTable")
public class CmcChannelCommonTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -477210157583589458L;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.2.1.1")
    private Integer qamChannelCommonOutputBw;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.2.1.2")
    private Integer qamChannelCommonUtilization;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getQamChannelCommonOutputBw() {
        return qamChannelCommonOutputBw;
    }

    public void setQamChannelCommonOutputBw(Integer qamChannelCommonOutputBw) {
        this.qamChannelCommonOutputBw = qamChannelCommonOutputBw;
    }

    public Integer getQamChannelCommonUtilization() {
        return qamChannelCommonUtilization;
    }

    public void setQamChannelCommonUtilization(Integer qamChannelCommonUtilization) {
        this.qamChannelCommonUtilization = qamChannelCommonUtilization;
    }

}
