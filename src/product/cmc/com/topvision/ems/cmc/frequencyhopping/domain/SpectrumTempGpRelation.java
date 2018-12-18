/***********************************************************************
 * $Id: SpectrumTempGpRelation.java,v1.0 2013-8-2 下午1:31:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;

/**
 * @author haojie
 * @created @2013-8-2-下午1:31:50
 * 
 */
public class SpectrumTempGpRelation implements Serializable {
    private static final long serialVersionUID = -5015856806579916917L;

    private Long tempLateId;
    private Long emsGroupId;
    private Integer groupId;

    public Long getTempLateId() {
        return tempLateId;
    }

    public void setTempLateId(Long tempLateId) {
        this.tempLateId = tempLateId;
    }

    public Long getEmsGroupId() {
        return emsGroupId;
    }

    public void setEmsGroupId(Long emsGroupId) {
        this.emsGroupId = emsGroupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumTempGpRelation [tempLateId=");
        builder.append(tempLateId);
        builder.append(", emsGroupId=");
        builder.append(emsGroupId);
        builder.append(", groupId=");
        builder.append(groupId);
        builder.append("]");
        return builder.toString();
    }

}
