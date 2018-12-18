/**
 *
 */
package com.topvision.platform.domain;

import java.io.Serializable;

/**
 * @author kelers
 */
public class EngineScope implements Serializable {

    private static final long serialVersionUID = 2046826983237470266L;

    private long scopeId;
    private String engineId;
    private String fromIp;
    private String endIp;
    private String note;

    public String getEndIp() {
        return endIp;
    }

    public String getEngineId() {
        return engineId;
    }

    public String getFromIp() {
        return fromIp;
    }

    public String getNote() {
        return note;
    }

    public long getScopeId() {
        return scopeId;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setScopeId(long scopeId) {
        this.scopeId = scopeId;
    }

}
