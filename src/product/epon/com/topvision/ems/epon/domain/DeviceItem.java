/**
 * 
 */
package com.topvision.ems.epon.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * 
 */
public class DeviceItem extends BaseEntity {

    private static final long serialVersionUID = -8350221826693585720L;

    private String name;
    private String ip;
    private long entityId;
    private long total;
    private String displayName;

    public String getDisplayName() {
        if (getName().equals(getIp())) {
            return getName();
        }
        displayName = getName() + "[" + getIp() + "]";
        return displayName;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public long getTotal() {
        return total;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
