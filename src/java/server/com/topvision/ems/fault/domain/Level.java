package com.topvision.ems.fault.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Level extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -8839970351517516698L;
    public static final byte CLEAR_LEVEL = 0;// 清除告警
    public static final byte INFO_LEVEL = 1;// 提示信息
    public static final byte WARNING_LEVEL = 2;// 一般告警
    public static final byte MINOR_LEVEL = 3;// 次要告警
    public static final byte MAJOR_LEVEL = 4;// 主要告警
    public static final byte SERVERITY_LEVEL = 5;// 严重告警
    public static final byte CRITICAL_LEVEL = 6;// 紧急告警
    /**
     * 表示设备不在线
     */
    public static final byte OFFLINE = -1;
    /**
     * 没有采集数据, 不支持SNMP或者其它采集数据方式.
     */
    public static final byte NODATA = -2;
    public static final String[] LEVEL_NAME = { "Clear", "Information", "Warning", "Minor", "Major", "Serverity",
            "Critical" };

    public static final String getNameByLevel(byte level) {
        return LEVEL_NAME[level];
    }

    private Byte levelId = MINOR_LEVEL;
    private String name;
    private String icon;
    private String note;
    private Boolean active;
    private Integer soundId; // 告警对应声音ID
    private String soundName;// 告警对应声音文件名
    private String soundDesc;// 告警对应声音描述

    /**
     * 
     * @return icon
     */
    public String getIcon() {
        return icon;
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
     * @return active
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * 
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
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

    public Integer getSoundId() {
        return soundId;
    }

    public void setSoundId(Integer soundId) {
        this.soundId = soundId;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public String getSoundDesc() {
        return soundDesc;
    }

    public void setSoundDesc(String soundDesc) {
        this.soundDesc = soundDesc;
    }

}
