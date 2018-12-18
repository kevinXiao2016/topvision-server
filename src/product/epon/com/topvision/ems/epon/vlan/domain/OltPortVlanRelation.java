/***********************************************************************
 * $Id: OltPortVlanRelation.java,v1.0 2016年6月8日 上午10:04:44 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年6月8日-上午10:04:44
 *
 */
public class OltPortVlanRelation implements AliasesSuperType {
    private static final long serialVersionUID = 7378763069927996186L;
    public static final int TAG_PORT = 1;
    public static final int UNTAG_PORT = 2;
    private Long entityId;
    private Long portIndex;
    private Integer vlanIndex;
    private int type;

    public OltPortVlanRelation() {

    }

    public OltPortVlanRelation(Long entityId, Long portIndex, Integer vlanIndex, int type) {
        this.entityId = entityId;
        this.portIndex = portIndex;
        this.vlanIndex = vlanIndex;
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
