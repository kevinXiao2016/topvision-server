/***********************************************************************
 * $Id: CmcLoadBalGrp.java,v1.0 2013-4-23 下午5:24:24 $
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
 * @created @2013-4-23-下午5:24:24
 *
 */
@Alias("cmcLoadBalGrp")
public class CmcLoadBalGrp implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 7939317479647171517L;
    private Long grpId;
    private Long cmcId;
    private String groupName;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.3.1.1.1", index = true)
    private Long docsLoadBalGrpId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.3.1.1.8", writable = true, type = "Integer32")
    private Integer docsLoadBalGrpStatus;
    public Long getGrpId() {
        return grpId;
    }
    public void setGrpId(Long grpId) {
        this.grpId = grpId;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Long getDocsLoadBalGrpId() {
        return docsLoadBalGrpId;
    }
    public void setDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        this.docsLoadBalGrpId = docsLoadBalGrpId;
    }
    public Integer getDocsLoadBalGrpStatus() {
        return docsLoadBalGrpStatus;
    }
    public void setDocsLoadBalGrpStatus(Integer docsLoadBalGrpStatus) {
        this.docsLoadBalGrpStatus = docsLoadBalGrpStatus;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcLoadBalGrp [grpId=");
        builder.append(grpId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", groupName=");
        builder.append(groupName);
        builder.append(", docsLoadBalGrpId=");
        builder.append(docsLoadBalGrpId);
        builder.append(", docsLoadBalGrpStatus=");
        builder.append(docsLoadBalGrpStatus);
        builder.append("]");
        return builder.toString();
    }
}
