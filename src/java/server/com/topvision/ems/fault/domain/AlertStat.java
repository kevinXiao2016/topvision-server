package com.topvision.ems.fault.domain;

public class AlertStat extends Alert {
    private static final long serialVersionUID = -1890918132364806033L;
    private Long count;
    private String entityName;
    private String displayName;
    private Long parentId;
    private Long entityType;
    private String uplinkDevice;

    /**
     * 
     * @return count
     */
    public Long getCount() {
        return count;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    /**
     * 
     * @param count
     */
    public void setCount(Long count) {
        this.count = count;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

}
