/**
 *
 */
package com.topvision.platform.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author kelers
 */
public class InterceptorLog implements Serializable {
    private static final long serialVersionUID = -5378015739134684759L;

    private long logId;
    private long entityId;
    private String entityIp;
    private String entityName;
    private Long ifIndex;
    private String ifName;
    private boolean auto;
    private boolean result;
    private String reason;
    private String description;
    private Timestamp interceptorTime;

    private String relatedEntity;
    private String relatedPort;

    public boolean getAuto() {
        return auto;
    }

    public String getDescription() {
        return description;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public String getEntityName() {
        return entityName;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public String getIfName() {
        return ifName;
    }

    public Timestamp getInterceptorTime() {
        return interceptorTime;
    }

    public long getLogId() {
        return logId;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    public String getRelatedEntity() {
        if (entityIp != null && entityName != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(entityName);
            sb.append("[");
            sb.append(entityIp);
            sb.append("]");
            relatedEntity = sb.toString();
        } else {
            relatedEntity = entityIp;
        }
        return relatedEntity;
    }

    public String getRelatedPort() {
        StringBuilder sb = new StringBuilder();
        sb.append(ifName);
        sb.append("[");
        sb.append(ifIndex);
        sb.append("]");
        relatedPort = sb.toString();
        return relatedPort;
    }

    public boolean getResult() {
        return result;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public void setInterceptorTime(Timestamp interceptorTime) {
        this.interceptorTime = interceptorTime;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRelatedEntity(String relatedEntity) {
        this.relatedEntity = relatedEntity;
    }

    public void setRelatedPort(String relatedPort) {
        this.relatedPort = relatedPort;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

}
