package com.topvision.ems.network.domain;

import com.topvision.framework.domain.BaseEntity;

public class PanelIfIndex extends BaseEntity {
    private static final long serialVersionUID = -5863025314652440248L;
    private Long id;
    private Long typeId;
    private String x;
    private String y;
    private Integer ifIndex;
    private Integer type;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the ifIndex
     */
    public Integer getIfIndex() {
        return ifIndex;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @return the typeId
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * @return the x
     */
    public String getX() {
        return x;
    }

    /**
     * @return the y
     */
    public String getY() {
        return y;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Integer ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(String y) {
        this.y = y;
    }
}
