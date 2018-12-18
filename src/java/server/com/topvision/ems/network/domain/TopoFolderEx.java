package com.topvision.ems.network.domain;

public class TopoFolderEx extends TopoFolder {
    private static final long serialVersionUID = -1371863179962502820L;

    private Long entityId;
    private Byte alertLevel;
    private String alertMessage = "";

    /**
     * @return the alertLevel
     */
    public Byte getAlertLevel() {
        return alertLevel;
    }

    /**
     * @return the alertMessage
     */
    public String getAlertMessage() {
        return alertMessage;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param alertLevel
     *            the alertLevel to set
     */
    public void setAlertLevel(Byte alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * @param alertMessage
     *            the alertMessage to set
     */
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
