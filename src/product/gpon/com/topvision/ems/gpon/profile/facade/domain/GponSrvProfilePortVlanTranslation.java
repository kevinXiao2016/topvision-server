/***********************************************************************
 * $Id: GponSrvProfilePortVlanTranslation.java,v1.0 2016年10月25日 上午8:33:05 $
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
 * @created @2016年10月25日-上午8:33:05
 *
 */
public class GponSrvProfilePortVlanTranslation implements AliasesSuperType {
    private static final long serialVersionUID = 7374501166868028205L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.2.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTransProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.2.1.2", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTransPortTypeIndex;// 0:eth,1:wlan,2:catv
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.2.1.3", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTransPortIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.2.1.4", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTransVlanIndex;// 1-4094
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.2.1.5", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTransNewVlan;// 1-4094
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.2.1.6", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTransRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfilePortVlanTransProfileIndex() {
        return gponSrvProfilePortVlanTransProfileIndex;
    }

    public void setGponSrvProfilePortVlanTransProfileIndex(Integer gponSrvProfilePortVlanTransProfileIndex) {
        this.gponSrvProfilePortVlanTransProfileIndex = gponSrvProfilePortVlanTransProfileIndex;
    }

    public Integer getGponSrvProfilePortVlanTransPortTypeIndex() {
        return gponSrvProfilePortVlanTransPortTypeIndex;
    }

    public void setGponSrvProfilePortVlanTransPortTypeIndex(Integer gponSrvProfilePortVlanTransPortTypeIndex) {
        this.gponSrvProfilePortVlanTransPortTypeIndex = gponSrvProfilePortVlanTransPortTypeIndex;
    }

    public Integer getGponSrvProfilePortVlanTransPortIdIndex() {
        return gponSrvProfilePortVlanTransPortIdIndex;
    }

    public void setGponSrvProfilePortVlanTransPortIdIndex(Integer gponSrvProfilePortVlanTransPortIdIndex) {
        this.gponSrvProfilePortVlanTransPortIdIndex = gponSrvProfilePortVlanTransPortIdIndex;
    }

    public Integer getGponSrvProfilePortVlanTransVlanIndex() {
        return gponSrvProfilePortVlanTransVlanIndex;
    }

    public void setGponSrvProfilePortVlanTransVlanIndex(Integer gponSrvProfilePortVlanTransVlanIndex) {
        this.gponSrvProfilePortVlanTransVlanIndex = gponSrvProfilePortVlanTransVlanIndex;
    }

    public Integer getGponSrvProfilePortVlanTransNewVlan() {
        return gponSrvProfilePortVlanTransNewVlan;
    }

    public void setGponSrvProfilePortVlanTransNewVlan(Integer gponSrvProfilePortVlanTransNewVlan) {
        this.gponSrvProfilePortVlanTransNewVlan = gponSrvProfilePortVlanTransNewVlan;
    }

    public Integer getGponSrvProfilePortVlanTransRowStatus() {
        return gponSrvProfilePortVlanTransRowStatus;
    }

    public void setGponSrvProfilePortVlanTransRowStatus(Integer gponSrvProfilePortVlanTransRowStatus) {
        this.gponSrvProfilePortVlanTransRowStatus = gponSrvProfilePortVlanTransRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfilePortVlanTranslation [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfilePortVlanTransProfileIndex=");
        builder.append(gponSrvProfilePortVlanTransProfileIndex);
        builder.append(", gponSrvProfilePortVlanTransPortTypeIndex=");
        builder.append(gponSrvProfilePortVlanTransPortTypeIndex);
        builder.append(", gponSrvProfilePortVlanTransPortIdIndex=");
        builder.append(gponSrvProfilePortVlanTransPortIdIndex);
        builder.append(", gponSrvProfilePortVlanTransVlanIndex=");
        builder.append(gponSrvProfilePortVlanTransVlanIndex);
        builder.append(", gponSrvProfilePortVlanTransNewVlan=");
        builder.append(gponSrvProfilePortVlanTransNewVlan);
        builder.append(", gponSrvProfilePortVlanTransRowStatus=");
        builder.append(gponSrvProfilePortVlanTransRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
