/***********************************************************************
 * $Id: CmcDSIpqamBaseInfo.java,v1.0 2013-10-12 上午09:07:27 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author bryan
 * @created @2013-10-12-上午09:07:27
 *
 */
@Alias("txPowerLimit")
public class TxPowerLimit implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -1348183609901117635L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.29.1.1", index = true)
    private Integer channelNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.29.1.2")
    private Integer maxPowerTenthdBmV;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.29.1.3")
    private Integer minPowerTenthdBmV;
    
    private Double maxPower;
    private Double minPower;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Integer getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

    public Integer getMaxPowerTenthdBmV() {
        return maxPowerTenthdBmV;
    }

    public void setMaxPowerTenthdBmV(Integer maxPowerTenthdBmV) {
        this.maxPowerTenthdBmV = maxPowerTenthdBmV;
        this.maxPower = maxPowerTenthdBmV.doubleValue() / 10;
    }

    public Integer getMinPowerTenthdBmV() {
        return minPowerTenthdBmV;
    }

    public void setMinPowerTenthdBmV(Integer minPowerTenthdBmV) {
        this.minPowerTenthdBmV = minPowerTenthdBmV;
        this.minPower = minPowerTenthdBmV.doubleValue() / 10;
    }

    public Double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
    }

    public Double getMinPower() {
        return minPower;
    }

    public void setMinPower(Double minPower) {
        this.minPower = minPower;
    }
}
