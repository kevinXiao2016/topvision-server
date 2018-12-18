/***********************************************************************
 * $Id: CmcRateLimit.java,v1.0 2013-4-23 下午2:08:07 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-4-23-下午2:08:07
 *
 */
@Alias("cmcRateLimit")
public class CmcRateLimit implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 1L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.1.0", writable = true, type = "Integer32")
    private Integer topCcmtsRateLimiteCpuPortEgressArp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.2.0", writable = true, type = "Integer32")
    private Integer topCcmtsRateLimiteCpuPortEgressUni;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.3.0", writable = true, type = "Integer32")
    private Integer topCcmtsRateLimiteCpuPortEgressUdp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.4.0", writable = true, type = "Integer32")
    private Integer topCcmtsRateLimiteCpuPortEgressDhcp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.5.0", writable = true, type = "Integer32")
    private Integer topCcmtsRateLimiteUplinkEgressIcmp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.6.0", writable = true, type = "Integer32")
    private Integer topCcmtsRateLimiteUplinkEgressIgmp;
    /**
     * Modified By huangdongsheng
     * Add uplinkIngressBroadcast、uplinkIngressMulticast
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.7.0", writable = true, type = "Integer32")
    private Integer uplinkIngressBroadcast;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.8.0", writable = true, type = "Integer32")
    private Integer uplinkIngressMulticast;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.9.0", writable = true, type = "Integer32")
    private Integer cableIngressBroadcast;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.9.10.0", writable = true, type = "Integer32")
    private Integer cableIngressMulticast;

    public Integer getTopCcmtsRateLimiteCpuPortEgressArp() {
        return topCcmtsRateLimiteCpuPortEgressArp;
    }

    public void setTopCcmtsRateLimiteCpuPortEgressArp(Integer topCcmtsRateLimiteCpuPortEgressArp) {
        this.topCcmtsRateLimiteCpuPortEgressArp = topCcmtsRateLimiteCpuPortEgressArp;
    }

    public Integer getTopCcmtsRateLimiteCpuPortEgressUni() {
        return topCcmtsRateLimiteCpuPortEgressUni;
    }

    public void setTopCcmtsRateLimiteCpuPortEgressUni(Integer topCcmtsRateLimiteCpuPortEgressUni) {
        this.topCcmtsRateLimiteCpuPortEgressUni = topCcmtsRateLimiteCpuPortEgressUni;
    }

    public Integer getTopCcmtsRateLimiteCpuPortEgressUdp() {
        return topCcmtsRateLimiteCpuPortEgressUdp;
    }

    public void setTopCcmtsRateLimiteCpuPortEgressUdp(Integer topCcmtsRateLimiteCpuPortEgressUdp) {
        this.topCcmtsRateLimiteCpuPortEgressUdp = topCcmtsRateLimiteCpuPortEgressUdp;
    }

    public Integer getTopCcmtsRateLimiteCpuPortEgressDhcp() {
        return topCcmtsRateLimiteCpuPortEgressDhcp;
    }

    public void setTopCcmtsRateLimiteCpuPortEgressDhcp(Integer topCcmtsRateLimiteCpuPortEgressDhcp) {
        this.topCcmtsRateLimiteCpuPortEgressDhcp = topCcmtsRateLimiteCpuPortEgressDhcp;
    }

    public Integer getTopCcmtsRateLimiteUplinkEgressIcmp() {
        return topCcmtsRateLimiteUplinkEgressIcmp;
    }

    public void setTopCcmtsRateLimiteUplinkEgressIcmp(Integer topCcmtsRateLimiteUplinkEgressIcmp) {
        this.topCcmtsRateLimiteUplinkEgressIcmp = topCcmtsRateLimiteUplinkEgressIcmp;
    }

    public Integer getTopCcmtsRateLimiteUplinkEgressIgmp() {
        return topCcmtsRateLimiteUplinkEgressIgmp;
    }

    public void setTopCcmtsRateLimiteUplinkEgressIgmp(Integer topCcmtsRateLimiteUplinkEgressIgmp) {
        this.topCcmtsRateLimiteUplinkEgressIgmp = topCcmtsRateLimiteUplinkEgressIgmp;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    

    public Integer getUplinkIngressBroadcast() {
        return uplinkIngressBroadcast;
    }

    public void setUplinkIngressBroadcast(Integer uplinkIngressBroadcast) {
        this.uplinkIngressBroadcast = uplinkIngressBroadcast;
    }

    public Integer getUplinkIngressMulticast() {
        return uplinkIngressMulticast;
    }

    public void setUplinkIngressMulticast(Integer uplinkIngressMulticast) {
        this.uplinkIngressMulticast = uplinkIngressMulticast;
    }

    public Integer getCableIngressBroadcast() {
        return cableIngressBroadcast;
    }

    public void setCableIngressBroadcast(Integer cableIngressBroadcast) {
        this.cableIngressBroadcast = cableIngressBroadcast;
    }

    public Integer getCableIngressMulticast() {
        return cableIngressMulticast;
    }

    public void setCableIngressMulticast(Integer cableIngressMulticast) {
        this.cableIngressMulticast = cableIngressMulticast;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcRateLimit [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsRateLimiteCpuPortEgressArp=");
        builder.append(topCcmtsRateLimiteCpuPortEgressArp);
        builder.append(", topCcmtsRateLimiteCpuPortEgressUni=");
        builder.append(topCcmtsRateLimiteCpuPortEgressUni);
        builder.append(", topCcmtsRateLimiteCpuPortEgressUdp=");
        builder.append(topCcmtsRateLimiteCpuPortEgressUdp);
        builder.append(", topCcmtsRateLimiteCpuPortEgressDhcp=");
        builder.append(topCcmtsRateLimiteCpuPortEgressDhcp);
        builder.append(", topCcmtsRateLimiteUplinkEgressIcmp=");
        builder.append(topCcmtsRateLimiteUplinkEgressIcmp);
        builder.append(", topCcmtsRateLimiteUplinkEgressIgmp=");
        builder.append(topCcmtsRateLimiteUplinkEgressIgmp);
        builder.append(", uplinkIngressBroadcast=");
        builder.append(uplinkIngressBroadcast);
        builder.append(", uplinkIngressMulticast=");
        builder.append(uplinkIngressMulticast);
        builder.append(", cableIngressBroadcast=");
        builder.append(cableIngressBroadcast);
        builder.append(", cableIngressMulticast=");
        builder.append(cableIngressMulticast);
        builder.append("]");
        return builder.toString();
    }

}
