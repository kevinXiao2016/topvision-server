/***********************************************************************
 * $Id: LoadBalExcludeCm.java,v1.0 2013-4-23 下午4:47:12 $
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
 * @created @2013-4-23-下午4:47:12
 *
 */
@Alias("cmcLoadBalExcludeCm")
public class CmcLoadBalExcludeCm implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -4680057704129227817L;
    private Long excRangId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.3.1.1", index = true)
    private Long topLoadBalExcludeCmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.3.1.2", writable = true, type = "OctetString")
    private String topLoadBalExcludeCmMacRang;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.3.1.3", writable = true, type = "Integer32")
    private Integer topLoadBalExcludeCmStatus;
    public Long getExcRangId() {
        return excRangId;
    }
    public void setExcRangId(Long excRangId) {
        this.excRangId = excRangId;
    }
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
    public Long getTopLoadBalExcludeCmIndex() {
        return topLoadBalExcludeCmIndex;
    }
    public void setTopLoadBalExcludeCmIndex(Long topLoadBalExcludeCmIndex) {
        this.topLoadBalExcludeCmIndex = topLoadBalExcludeCmIndex;
    }
    public String getTopLoadBalExcludeCmMacRang() {
        if(topLoadBalExcludeCmMacRang!=null){
            return topLoadBalExcludeCmMacRang.toUpperCase();
        }else{
            return topLoadBalExcludeCmMacRang;
        }
         
    }
    public void setTopLoadBalExcludeCmMacRang(String topLoadBalExcludeCmMacRang) {
        this.topLoadBalExcludeCmMacRang = topLoadBalExcludeCmMacRang;
    }
    public Integer getTopLoadBalExcludeCmStatus() {
        return topLoadBalExcludeCmStatus;
    }
    public void setTopLoadBalExcludeCmStatus(Integer topLoadBalExcludeCmStatus) {
        this.topLoadBalExcludeCmStatus = topLoadBalExcludeCmStatus;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcLoadBalExcludeCm [excRangId=");
        builder.append(excRangId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", topLoadBalExcludeCmIndex=");
        builder.append(topLoadBalExcludeCmIndex);
        builder.append(", topLoadBalExcludeCmMacRang=");
        builder.append(topLoadBalExcludeCmMacRang);
        builder.append(", topLoadBalExcludeCmStatus=");
        builder.append(topLoadBalExcludeCmStatus);
        builder.append("]");
        return builder.toString();
    }
}
