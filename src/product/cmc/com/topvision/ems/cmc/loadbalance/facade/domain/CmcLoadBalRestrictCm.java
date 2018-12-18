/***********************************************************************
 * $Id: CmcLoadBalRestrictCm.java,v1.0 2013-4-23 下午4:43:20 $
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
 * @created @2013-4-23-下午4:43:20
 *
 */
@Alias("cmcLoadBalRestrictCm")
public class CmcLoadBalRestrictCm implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 8611199632262166869L;
    private Long rangId;
    private Long cmcId;
    private Long grpId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.3.1.1.1", index = true)
    private Long docsLoadBalGrpId ;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.2.1.2", index = true)
    private Long topLoadBalRestrictCmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.2.1.3", writable = true, type = "OctetString")
    private String topLoadBalRestrictCmMacRang;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.12.2.1.4", writable = true, type = "Integer32")
    private Integer topLoadBalRestrictCmStatus;
    public Long getRangId() {
        return rangId;
    }
    public void setRangId(Long rangId) {
        this.rangId = rangId;
    }
    public Long getGrpId() {
        return grpId;
    }
    public void setGrpId(Long grpId) {
        this.grpId = grpId;
    }
    public Long getTopLoadBalRestrictCmIndex() {
        return topLoadBalRestrictCmIndex;
    }
    public void setTopLoadBalRestrictCmIndex(Long topLoadBalRestrictCmIndex) {
        this.topLoadBalRestrictCmIndex = topLoadBalRestrictCmIndex;
    }
    public String getTopLoadBalRestrictCmMacRang() {
        return topLoadBalRestrictCmMacRang.toUpperCase();
    }
    public void setTopLoadBalRestrictCmMacRang(String topLoadBalRestrictCmMacRang) {
        this.topLoadBalRestrictCmMacRang = topLoadBalRestrictCmMacRang;
    }
    public Long getDocsLoadBalGrpId() {
        return docsLoadBalGrpId;
    }
    public void setDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        this.docsLoadBalGrpId = docsLoadBalGrpId;
    }
    public Integer getTopLoadBalRestrictCmStatus() {
        return topLoadBalRestrictCmStatus;
    }
    public void setTopLoadBalRestrictCmStatus(Integer topLoadBalRestrictCmStatus) {
        this.topLoadBalRestrictCmStatus = topLoadBalRestrictCmStatus;
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
        builder.append("CmcLoadBalRestrictCm [rangId=");
        builder.append(rangId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", grpId=");
        builder.append(grpId);
        builder.append(", docsLoadBalGrpId=");
        builder.append(docsLoadBalGrpId);
        builder.append(", topLoadBalRestrictCmIndex=");
        builder.append(topLoadBalRestrictCmIndex);
        builder.append(", topLoadBalRestrictCmMacRang=");
        builder.append(topLoadBalRestrictCmMacRang);
        builder.append(", topLoadBalRestrictCmStatus=");
        builder.append(topLoadBalRestrictCmStatus);
        builder.append("]");
        return builder.toString();
    }
}
