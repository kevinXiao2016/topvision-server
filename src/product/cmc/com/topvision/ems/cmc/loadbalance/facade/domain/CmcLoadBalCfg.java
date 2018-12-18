/***********************************************************************
 * $Id: CmcLoadBalCfg.java,v1.0 2013-4-23 上午10:21:37 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-4-23-上午10:21:37
 *
 */
@Alias("cmcLoadBalCfg")
public class CmcLoadBalCfg implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 8402445803092501525L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.1", index = true)
    private Long topLoadBalConfigCmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.2", writable = true, type = "Integer32")
    private Integer topLoadBalConfigEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.3", writable = true, type = "Gauge32")
    private Long topLoadBalConfigInterval;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.4", writable = true, type = "Gauge32")
    private Long topLoadBalConfigMaxMoves;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.5", writable = true, type = "Integer32")
    private Integer topLoadBalConfigMethod;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.6", writable = true, type = "Integer32")
    private Integer topLoadBalConfigNumPeriod;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.7", writable = true, type = "Gauge32")
    private Long topLoadBalConfigPeriod;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.8", writable = true, type = "Gauge32")
    private Long topLoadBalConfigTriggerThresh;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.9", writable = true, type = "Gauge32")
    private Long topLoadBalConfigDiffThresh;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.11", writable = true, type = "Integer32")
    private Integer topLoadBalConfigDccInitAtdma;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.12", writable = true, type = "Integer32")
    private Integer topLoadBalConfigDccInitScdma;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.13", writable = true, type = "Integer32")
    private Integer topLoadBalConfigDbcInitAtdma;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.1.1.14", writable = true, type = "Integer32")
    private Integer topLoadBalConfigDbcInitScdma;
    
    public Long getTopLoadBalConfigCmcIndex() {
        return topLoadBalConfigCmcIndex;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public void setTopLoadBalConfigCmcIndex(Long topLoadBalConfigCmcIndex) {
        this.topLoadBalConfigCmcIndex = topLoadBalConfigCmcIndex;
    }
    public Integer getTopLoadBalConfigEnable() {
        return topLoadBalConfigEnable;
    }
    public void setTopLoadBalConfigEnable(Integer topLoadBalConfigEnable) {
        this.topLoadBalConfigEnable = topLoadBalConfigEnable;
    }
    public Long getTopLoadBalConfigInterval() {
        return topLoadBalConfigInterval;
    }
    public void setTopLoadBalConfigInterval(Long topLoadBalConfigInterval) {
        this.topLoadBalConfigInterval = topLoadBalConfigInterval;
    }
    public Long getTopLoadBalConfigMaxMoves() {
        return topLoadBalConfigMaxMoves;
    }
    public void setTopLoadBalConfigMaxMoves(Long topLoadBalConfigMaxMoves) {
        this.topLoadBalConfigMaxMoves = topLoadBalConfigMaxMoves;
    }
    public Integer getTopLoadBalConfigMethod() {
        return topLoadBalConfigMethod;
    }
    public void setTopLoadBalConfigMethod(Integer topLoadBalConfigMethod) {
        this.topLoadBalConfigMethod = topLoadBalConfigMethod;
    }
    public Integer getTopLoadBalConfigNumPeriod() {
        return topLoadBalConfigNumPeriod;
    }
    public void setTopLoadBalConfigNumPeriod(Integer topLoadBalConfigNumPeriod) {
        this.topLoadBalConfigNumPeriod = topLoadBalConfigNumPeriod;
    }
    public Long getTopLoadBalConfigPeriod() {
        return topLoadBalConfigPeriod;
    }
    public void setTopLoadBalConfigPeriod(Long topLoadBalConfigPeriod) {
        this.topLoadBalConfigPeriod = topLoadBalConfigPeriod;
    }
    public Long getTopLoadBalConfigTriggerThresh() {
        return topLoadBalConfigTriggerThresh;
    }
    public void setTopLoadBalConfigTriggerThresh(Long topLoadBalConfigTriggerThresh) {
        this.topLoadBalConfigTriggerThresh = topLoadBalConfigTriggerThresh;
    }
    public Long getTopLoadBalConfigDiffThresh() {
        return topLoadBalConfigDiffThresh;
    }
    public void setTopLoadBalConfigDiffThresh(Long topLoadBalConfigDiffThresh) {
        this.topLoadBalConfigDiffThresh = topLoadBalConfigDiffThresh;
    }
    public Integer getTopLoadBalConfigDccInitAtdma() {
        return topLoadBalConfigDccInitAtdma;
    }
    public void setTopLoadBalConfigDccInitAtdma(Integer topLoadBalConfigDccInitAtdma) {
        this.topLoadBalConfigDccInitAtdma = topLoadBalConfigDccInitAtdma;
    }
    public Integer getTopLoadBalConfigDccInitScdma() {
        return topLoadBalConfigDccInitScdma;
    }
    public void setTopLoadBalConfigDccInitScdma(Integer topLoadBalConfigDccInitScdma) {
        this.topLoadBalConfigDccInitScdma = topLoadBalConfigDccInitScdma;
    }
    public Integer getTopLoadBalConfigDbcInitAtdma() {
        return topLoadBalConfigDbcInitAtdma;
    }
    public void setTopLoadBalConfigDbcInitAtdma(Integer topLoadBalConfigDbcInitAtdma) {
        this.topLoadBalConfigDbcInitAtdma = topLoadBalConfigDbcInitAtdma;
    }
    public Integer getTopLoadBalConfigDbcInitScdma() {
        return topLoadBalConfigDbcInitScdma;
    }
    public void setTopLoadBalConfigDbcInitScdma(Integer topLoadBalConfigDbcInitScdma) {
        this.topLoadBalConfigDbcInitScdma = topLoadBalConfigDbcInitScdma;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcLoadBalCfg [cmcId=");
        builder.append(cmcId);
        builder.append(", topLoadBalConfigCmcIndex=");
        builder.append(topLoadBalConfigCmcIndex);
        builder.append(", topLoadBalConfigEnable=");
        builder.append(topLoadBalConfigEnable);
        builder.append(", topLoadBalConfigInterval=");
        builder.append(topLoadBalConfigInterval);
        builder.append(", topLoadBalConfigMaxMoves=");
        builder.append(topLoadBalConfigMaxMoves);
        builder.append(", topLoadBalConfigMethod=");
        builder.append(topLoadBalConfigMethod);
        builder.append(", topLoadBalConfigNumPeriod=");
        builder.append(topLoadBalConfigNumPeriod);
        builder.append(", topLoadBalConfigPeriod=");
        builder.append(topLoadBalConfigPeriod);
        builder.append(", topLoadBalConfigTriggerThresh=");
        builder.append(topLoadBalConfigTriggerThresh);
        builder.append(", topLoadBalConfigDiffThresh=");
        builder.append(topLoadBalConfigDiffThresh);
        builder.append(", topLoadBalConfigDccInitAtdma=");
        builder.append(topLoadBalConfigDccInitAtdma);
        builder.append(", topLoadBalConfigDccInitScdma=");
        builder.append(topLoadBalConfigDccInitScdma);
        builder.append(", topLoadBalConfigDbcInitAtdma=");
        builder.append(topLoadBalConfigDbcInitAtdma);
        builder.append(", topLoadBalConfigDbcInitScdma=");
        builder.append(topLoadBalConfigDbcInitScdma);
        builder.append("]");
        return builder.toString();
    }
}
