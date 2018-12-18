/***********************************************************************
 * $Id: OnuSrvIgmpVlanTrans.java,v1.0 2016年8月3日 下午3:19:04 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2016年8月3日-下午3:19:04
 *
 */
public class OnuSrvIgmpVlanTrans implements AliasesSuperType {

    private static final long serialVersionUID = -6225205418971295246L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.4.1.1", index = true)
    private Integer igmpProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.4.1.2", index = true)
    private Integer igmpPortId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.4.1.3", index = true)
    private Integer igmpVlanTransId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.4.1.4", writable = true, type = "Integer32")
    private Integer transOldVlan;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.4.1.5", writable = true, type = "Integer32")
    private Integer transNewVlan;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.4.1.6", writable = true, type = "Integer32")
    private Integer rowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIgmpProfileId() {
        return igmpProfileId;
    }

    public void setIgmpProfileId(Integer igmpProfileId) {
        this.igmpProfileId = igmpProfileId;
    }

    public Integer getIgmpPortId() {
        return igmpPortId;
    }

    public void setIgmpPortId(Integer igmpPortId) {
        this.igmpPortId = igmpPortId;
    }

    public Integer getIgmpVlanTransId() {
        return igmpVlanTransId;
    }

    public void setIgmpVlanTransId(Integer igmpVlanTransId) {
        this.igmpVlanTransId = igmpVlanTransId;
    }

    public Integer getTransOldVlan() {
        return transOldVlan;
    }

    public void setTransOldVlan(Integer transOldVlan) {
        this.transOldVlan = transOldVlan;
    }

    public Integer getTransNewVlan() {
        return transNewVlan;
    }

    public void setTransNewVlan(Integer transNewVlan) {
        this.transNewVlan = transNewVlan;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

}
