/***********************************************************************
 * $Id: GponLineProfileTcont.java,v1.0 2016年10月24日 下午5:58:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年10月24日-下午5:58:12
 *
 */
public class GponLineProfileTcont implements AliasesSuperType {
    private static final long serialVersionUID = -8063988852345167907L;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.2.1.1", index = true, type = "Integer32")
    private Integer gponLineProfileTcontProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.2.1.2", index = true, type = "Integer32")
    private Integer gponLineProfileTcontIndex;// 1-7
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.2.1.3", writable = true, type = "Integer32")
    private Integer gponLineProfileTcontDbaProfileId;// 1-256
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.2.1.4", writable = true, type = "Integer32")
    private Integer gponLineProfileTcontRowStatus;
    private Long entityId;

    public Integer getGponLineProfileTcontProfileIndex() {
        return gponLineProfileTcontProfileIndex;
    }

    public void setGponLineProfileTcontProfileIndex(Integer gponLineProfileTcontProfileIndex) {
        this.gponLineProfileTcontProfileIndex = gponLineProfileTcontProfileIndex;
    }

    public Integer getGponLineProfileTcontIndex() {
        return gponLineProfileTcontIndex;
    }

    public void setGponLineProfileTcontIndex(Integer gponLineProfileTcontIndex) {
        this.gponLineProfileTcontIndex = gponLineProfileTcontIndex;
    }

    public Integer getGponLineProfileTcontDbaProfileId() {
        return gponLineProfileTcontDbaProfileId;
    }

    public void setGponLineProfileTcontDbaProfileId(Integer gponLineProfileTcontDbaProfileId) {
        this.gponLineProfileTcontDbaProfileId = gponLineProfileTcontDbaProfileId;
    }

    public Integer getGponLineProfileTcontRowStatus() {
        return gponLineProfileTcontRowStatus;
    }

    public void setGponLineProfileTcontRowStatus(Integer gponLineProfileTcontRowStatus) {
        this.gponLineProfileTcontRowStatus = gponLineProfileTcontRowStatus;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponLineProfileTcont [gponLineProfileTcontProfileIndex=");
        builder.append(gponLineProfileTcontProfileIndex);
        builder.append(", gponLineProfileTcontIndex=");
        builder.append(gponLineProfileTcontIndex);
        builder.append(", gponLineProfileTcontDbaProfileId=");
        builder.append(gponLineProfileTcontDbaProfileId);
        builder.append(", gponLineProfileTcontRowStatus=");
        builder.append(gponLineProfileTcontRowStatus);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append("]");
        return builder.toString();
    }

}
