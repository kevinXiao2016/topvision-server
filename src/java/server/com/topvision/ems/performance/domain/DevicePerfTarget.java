package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-3-12-下午2:34:55
 *
 */
public class DevicePerfTarget implements AliasesSuperType {
    private static final long serialVersionUID = 919923663864656495L;

    private Long entityId;
    private String deviceName;
    private String manageIp;
    private String perfTargetName;
    private Long parentType;
    private Long entityType;
    private Long typeId;
    private Integer collectInterval;
    private Integer targetEnable;
    private String targetGroup;
    private Integer globalInterval;
    private Integer globalEnable;
    private String location;
    private String displayName;
    private String uplinkDevice;

    private Long parentId;
    private String parentName;
    private Long parentTypeId;

    public DevicePerfTarget() {
        super();
    }

    public DevicePerfTarget(Long entityId, String perfTargetName, Long parentType, Long entityType, Long typeId,
            Integer collectInterval, Integer targetEnable) {
        super();
        this.entityId = entityId;
        this.perfTargetName = perfTargetName;
        this.parentType = parentType;
        this.entityType = entityType;
        this.typeId = typeId;
        this.collectInterval = collectInterval;
        this.targetEnable = targetEnable;
    }

    public DevicePerfTarget(Long entityId, String deviceName, String manageIp, String perfTargetName, Long parentType,
            Long entityType, Long typeId, Integer collectInterval, Integer targetEnable, String targetGroup,
            Integer globalInterval, Integer globalEnable, String location, String uplinkDevice) {
        super();
        this.entityId = entityId;
        this.deviceName = deviceName;
        this.manageIp = manageIp;
        this.perfTargetName = perfTargetName;
        this.parentType = parentType;
        this.entityType = entityType;
        this.typeId = typeId;
        this.collectInterval = collectInterval;
        this.targetEnable = targetEnable;
        this.targetGroup = targetGroup;
        this.globalInterval = globalInterval;
        this.globalEnable = globalEnable;
        this.location = location;
        this.uplinkDevice = uplinkDevice;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public String getPerfTargetName() {
        return perfTargetName;
    }

    public void setPerfTargetName(String perfTargetName) {
        this.perfTargetName = perfTargetName;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Integer getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(Integer collectInterval) {
        this.collectInterval = collectInterval;
    }

    public Integer getTargetEnable() {
        return targetEnable;
    }

    public void setTargetEnable(Integer targetEnable) {
        this.targetEnable = targetEnable;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Integer getGlobalInterval() {
        return globalInterval;
    }

    public void setGlobalInterval(Integer globalInterval) {
        this.globalInterval = globalInterval;
    }

    public Integer getGlobalEnable() {
        return globalEnable;
    }

    public void setGlobalEnable(Integer globalEnable) {
        this.globalEnable = globalEnable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getParentType() {
        return parentType;
    }

    public void setParentType(Long parentType) {
        this.parentType = parentType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(Long parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    /**
     * 在比较指标是否改变时用到,请不要删除
     * @author flackyang
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevicePerfTarget other = (DevicePerfTarget) obj;
        if (perfTargetName == null) {
            if (other.perfTargetName != null)
                return false;
        } else if (!perfTargetName.equals(other.perfTargetName))
            return false;
        if (collectInterval == null) {
            if (other.collectInterval != null)
                return false;
        } else if (!collectInterval.equals(other.collectInterval))
            return false;
        if (targetEnable == null) {
            if (other.targetEnable != null)
                return false;
        } else if (!targetEnable.equals(other.targetEnable))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DevicePerfTarget [entityId=");
        builder.append(entityId);
        builder.append(", deviceName=");
        builder.append(deviceName);
        builder.append(", manageIp=");
        builder.append(manageIp);
        builder.append(", perfTargetName=");
        builder.append(perfTargetName);
        builder.append(", parentType=");
        builder.append(parentType);
        builder.append(", entityType=");
        builder.append(entityType);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", collectInterval=");
        builder.append(collectInterval);
        builder.append(", targetEnable=");
        builder.append(targetEnable);
        builder.append(", targetGroup=");
        builder.append(targetGroup);
        builder.append(", globalInterval=");
        builder.append(globalInterval);
        builder.append(", globalEnable=");
        builder.append(globalEnable);
        builder.append(", location=");
        builder.append(location);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
