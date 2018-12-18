package com.topvision.ems.performance.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class MonitorValue extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -7893031732875323876L;
    private Long id;
    private Long entityId;
    private String itemName;
    private Integer itemIndex = 0;
    private Double itemValue;
    private Double extValue;
    private String note;
    private Timestamp collectTime;

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @return the extValue
     */
    public Double getExtValue() {
        return extValue;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the itemIndex
     */
    public Integer getItemIndex() {
        return itemIndex;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @return the itemValue
     */
    public Double getItemValue() {
        return itemValue;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param extValue
     *            the extValue to set
     */
    public void setExtValue(Double extValue) {
        this.extValue = extValue;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param itemIndex
     *            the itemIndex to set
     */
    public void setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
    }

    /**
     * @param itemName
     *            the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @param itemValue
     *            the itemValue to set
     */
    public void setItemValue(Double itemValue) {
        if (itemValue == Double.NaN || itemValue == Double.POSITIVE_INFINITY || itemValue == Double.NEGATIVE_INFINITY) {
            this.itemValue = 0.0;
        } else {
            this.itemValue = itemValue;
        }
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder("monitorId:").append(entityId).append(",itemValue[").append(itemIndex).append("](")
                .append(itemName).append(")=").append(itemValue).toString();
    }
}
