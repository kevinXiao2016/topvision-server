/***********************************************************************
 * $Id: CmcTopLoadBalPolicy.java,v1.0 2013-4-24 上午9:24:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author loyal
 * @created @2013-4-24-上午9:24:20
 *
 */
public class CmcTopLoadBalPolicy implements Serializable{
    private static final long serialVersionUID = -8283887782293370013L;
    private Long cmcId;
    private Long policyId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.4.1.1", index = true)
    private Long topLoadBalPolicyId;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getPolicyId() {
        return policyId;
    }
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }
    public Long getIfIndex() {
        return ifIndex;
    }
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }
    public Long getTopLoadBalPolicyId() {
        return topLoadBalPolicyId;
    }
    public void setTopLoadBalPolicyId(Long topLoadBalPolicyId) {
        this.topLoadBalPolicyId = topLoadBalPolicyId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcTopLoadBalPolicy [cmcId=");
        builder.append(cmcId);
        builder.append(", policyId=");
        builder.append(policyId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", topLoadBalPolicyId=");
        builder.append(topLoadBalPolicyId);
        builder.append("]");
        return builder.toString();
    }
}
