package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class AlertFilter extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -8389061452676424070L;
    public static final int IP_FILTER = 0;
    public static final int TYPE_FILTER = 1;
    public static final int PARAMS_FILTER = 2;
    public static final int COMBINE_FILTER = 3;
    private Long filterId;
    //private Integer type;
    private String name;
    private String ip;
    private String typeIds;
    private Integer typeId;
    private String typeName;
    private String params;
    private String extend;
    private String note;
    private Integer onuLevel;

    /**
     * 
     * @return extend
     */
    public String getExtend() {
        return extend;
    }

    /**
     * 
     * @return filterId
     */
    public Long getFilterId() {
        return filterId;
    }

    /**
     * 
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * 
     * @return params
     */
    public String getParams() {
        return params;
    }

    /**
     * 
     * @return typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 
     * @return typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 
     * @param extend
     */
    public void setExtend(String extend) {
        this.extend = extend;
    }

    /**
     * 
     * @param filterId
     */
    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }

    /**
     * 
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 
     * @param params
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 
     * @param typeId
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 
     * @param typeName
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AlertFilter[");
        sb.append(",typeId=");
        sb.append(typeId);
        sb.append("]");
        return sb.toString();
    }

    public String getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(String typeIds) {
        this.typeIds = typeIds;
    }

    public Integer getOnuLevel() {
        return onuLevel;
    }

    public void setOnuLevel(Integer onuLevel) {
        this.onuLevel = onuLevel;
    }

}
