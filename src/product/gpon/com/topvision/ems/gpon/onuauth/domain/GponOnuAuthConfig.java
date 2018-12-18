/***********************************************************************
 * $Id: OnuAuthConfig.java,v1.0 2016年12月19日 上午10:31:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.domain;

import com.topvision.ems.gpon.utils.GponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年12月19日-上午10:31:22
 *
 */
public class GponOnuAuthConfig implements AliasesSuperType {
    private static final long serialVersionUID = -7447066867139271686L;
    private Long entityId;
    private Long ponIndex;
    private Long ponId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.1", index = true, type = "Integer32")
    private Long authenOnuId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.2", type = "OctetString", writable = true)
    private String sn;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.3", type = "OctetString", writable = true)
    private String password;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.4", type = "OctetString", writable = true)
    private String loid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.5", type = "OctetString", writable = true)
    private String loidPassword;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.6", type = "Integer32", writable = true)
    private Integer lineProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.7", type = "Integer32", writable = true)
    private Integer srvProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.6.1.1.8", type = "Integer32", writable = true)
    private Integer rowStatus;
    private Integer authMode;
    private Long onuId;

    public String getLocation() {
        return GponIndex.getSlotNoFromMibIndex(authenOnuId) + "/" + GponIndex.getPonNoFromMibIndex(authenOnuId) + ":"
                + GponIndex.getOnuNoFromMibIndex(authenOnuId);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonIndex() {
        if (ponIndex == null) {
            return GponIndex.getPonIndexFromMibIndex(authenOnuId);
        }
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getAuthenOnuId() {
        return authenOnuId;
    }

    public void setAuthenOnuId(Long authenOnuId) {
        this.authenOnuId = authenOnuId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoid() {
        return loid;
    }

    public void setLoid(String loid) {
        this.loid = loid;
    }

    public String getLoidPassword() {
        return loidPassword;
    }

    public void setLoidPassword(String loidPassword) {
        this.loidPassword = loidPassword;
    }

    public Integer getLineProfileId() {
        return lineProfileId;
    }

    public void setLineProfileId(Integer lineProfileId) {
        this.lineProfileId = lineProfileId;
    }

    public Integer getSrvProfileId() {
        return srvProfileId;
    }

    public void setSrvProfileId(Integer srvProfileId) {
        this.srvProfileId = srvProfileId;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Integer getAuthMode() {
        return authMode;
    }

    public void setAuthMode(Integer authMode) {
        this.authMode = authMode;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    @Override
    public String toString() {
        return "GponOnuAuthConfig [entityId=" + entityId + ", ponIndex=" + ponIndex + ", ponId=" + ponId
                + ", authenOnuId=" + authenOnuId + ", sn=" + sn + ", password=" + password + ", loid=" + loid
                + ", loidPassword=" + loidPassword + ", lineProfileId=" + lineProfileId + ", srvProfileId="
                + srvProfileId + ", rowStatus=" + rowStatus + "]";
    }

}
