package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

public class AlertType extends BaseEntity implements TreeEntity, AliasesSuperType {
    private static final long serialVersionUID = 3120669965813405028L;
    public static final Integer[] ALERT_EPON = { 0, -10000, -20000, -30000, -60000, -50000, -50001, -49999, -49998, -7,
            -6, -5 };
    public static final Integer[] ALERT_OLT = { 0, -10000, -20000, -30000, -60000, -50000, -50001, -49999, -7, -6 };
    public static final Integer[] ALERT_CMTS = { 0, -10000, -20000, -30000, -60000, -50000, -50002, -8 };
    public static final Integer[] ALERT_ONU = { 0, -10000, -20000, -30000, -60000, -50000, -50001, -49998, -7, -5 };
    public static final Integer[] SPECIAL_CATEGORY_CMTS = { -50002, -8 };
    public static final Integer[] SPECIAL_CATEGORY_ONU = { -49998, -5 };
    public static final Integer[] SPECIAL_CATEGORY_OLT = { -49999, -6 };
    public static final Integer[] SPECIAL_CATEGORY_EPON = { -50001, -49999, -49998, -7, -6, -5 };

    private Integer typeId;
    private Integer category;
    private String name;
    private String displayName;
    private Byte levelId;
    private Integer updateLevel;
    private Boolean smartUpdate;
    private Boolean terminate;
    private Boolean active;// 是否通过阈值产生告警级别
    private Boolean threshold;
    private String note;
    private String oid;
    private String alertTimes;
    private String localName;

    private String value;// author:bravin,为了方便jquery autocomplete调用

    /**
     * 
     * @return active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @return alertTimes
     */
    public String getAlertTimes() {
        return alertTimes;
    }

    /**
     * 
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getId() {
        return String.valueOf(typeId);
    }

    /**
     * 
     * @return levelId
     */
    public Byte getLevelId() {
        return levelId;
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
     * @return oid
     */
    public String getOid() {
        return oid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getParentId()
     */
    @Override
    public String getParentId() {
        return String.valueOf(category);
    }

    /**
     * @return the category
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 
     * @return smartUpdate
     */
    public Boolean getSmartUpdate() {
        return smartUpdate;
    }

    /**
     * 
     * @return terminate
     */
    public Boolean getTerminate() {
        return terminate;
    }

    @Override
    public String getText() {
        return name;
    }

    /**
     * 
     * @return threshold
     */
    public Boolean getThreshold() {
        return threshold;
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
     * @return smartUpdate
     */
    /*
    public Boolean isSmartUpdate() {
     return smartUpdate;
    }*/

    /**
     * 
     * @return terminate
     */
    /* public Boolean isTerminate() {
         return terminate;
     }*/

    /**
     * 
     * @return threshold
     */
    /*
    public Boolean isThreshold() {
     return threshold;
    }*/

    /**
     * 
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @param alertTimes
     */
    public void setAlertTimes(String alertTimes) {
        this.alertTimes = alertTimes;
    }

    /**
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @param levelId
     */
    public void setLevelId(Byte levelId) {
        this.levelId = levelId;
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
     * @param oid
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * 
     * @param category
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 
     * @param smartUpdate
     */
    public void setSmartUpdate(Boolean smartUpdate) {
        this.smartUpdate = smartUpdate;
    }

    /**
     * 
     * @param terminate
     */
    public void setTerminate(Boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * 
     * @param threshold
     */
    public void setThreshold(Boolean threshold) {
        this.threshold = threshold;
    }

    /**
     * 
     * @param typeId
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the updateLevel
     */
    public Integer getUpdateLevel() {
        return updateLevel;
    }

    /**
     * @param updateLevel
     *            the updateLevel to set
     */
    public void setUpdateLevel(Integer updateLevel) {
        this.updateLevel = updateLevel;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

}
