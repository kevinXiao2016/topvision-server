/**
 * 
 */
package com.topvision.ems.facade.domain;

import java.io.Serializable;

/**
 * @author kelers
 * 
 *         设备的具体管理参数.
 */
public class EntityParam implements Serializable {

    private static final long serialVersionUID = 8326247924170586239L;

    private Long entityId;
    private String paramName;
    private String paramValue;
    private String paramType;
    private Integer paramSeq;
    private String paramDisplayName;
    private String paramNote;

    public Long getEntityId() {
        return entityId;
    }

    public String getParamDisplayName() {
        return paramDisplayName;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamNote() {
        return paramNote;
    }

    public Integer getParamSeq() {
        return paramSeq;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setParamDisplayName(String paramDisplayName) {
        this.paramDisplayName = paramDisplayName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setParamNote(String paramNote) {
        this.paramNote = paramNote;
    }

    public void setParamSeq(Integer paramSeq) {
        this.paramSeq = paramSeq;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
