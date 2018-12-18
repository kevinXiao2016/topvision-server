package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class EntityAddress extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 9070165299520096859L;
    private Long entityId = -1L;
    private String ip;
    private String oldIp;

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return ip.equals(((EntityAddress) o).getIp());
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the oldIp
     */
    public String getOldIp() {
        return oldIp;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param oldIp
     *            the oldIp to set
     */
    public void setOldIp(String oldIp) {
        this.oldIp = oldIp;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ip;
    }
}
