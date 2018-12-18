/***********************************************************************
 * $Id: CmcLoadBalTopPolicy.java,v1.0 2013-4-26 上午9:55:03 $
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
 * @created @2013-4-26-上午9:55:03
 *
 */
@Alias("cmcLoadBalTopPolicy")
public class CmcLoadBalTopPolicy implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 7398024842288918277L;
    private Long cmcId;
    private Long policyId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.4.1.1", writable = true, type = "Integer32")
    private Long topLoadBalPolicyId;
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
    public Long getPolicyId() {
        return policyId;
    }
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }
	public Long getCmcId() {
		return cmcId;
	}
	public void setCmcId(Long cmcId) {
		this.cmcId = cmcId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CmcLoadBalTopPolicy [cmcId=");
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
