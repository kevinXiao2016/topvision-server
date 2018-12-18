/***********************************************************************
 * $Id: UniPortVlan.java,v1.0 2016年6月13日 下午7:05:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年6月13日-下午7:05:22
 *
 */
public class UniPortVlan implements AliasesSuperType {
    private static final long serialVersionUID = 2806291171450828224L;
    private Long entityId;
    private Long uniIndex;
    private Long uniId;
    private String profileName;
    private Integer vlanPVid;
    private Integer vlanMode;
    private Integer vlanTagPriority;

    public String getPortLocation() {
        return EponIndex.getStringFromIndex(uniIndex);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getVlanPVid() {
        return vlanPVid;
    }

    public void setVlanPVid(Integer vlanPVid) {
        this.vlanPVid = vlanPVid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public Integer getVlanTagPriority() {
        return vlanTagPriority;
    }

    public void setVlanTagPriority(Integer vlanTagPriority) {
        this.vlanTagPriority = vlanTagPriority;
    }

}
