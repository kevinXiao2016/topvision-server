package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Link extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 4028644404198510491L;

    public static final byte UNKNOWN_LINK_TYPE = 0;
    public static final byte L2_LINK_TYPE = 1;
    public static final byte L3_LINK_TYPE = 2;
    public static final byte MANUAL_LINK_TYPE = 3;
    public static final byte LOGIC_LINK_TYPE = 10;
    public static final byte CONNECT_STRAIGHT = 5;
    public static final byte CONNECT_HORIZONAL = 6;
    public static final byte CONNECT_VERTICAL = 7;
    public static final byte CONNECT_ARROW = 8;
    public static final byte CONNECT_ENTITY = 9;

    private Long linkId = 0L;
    private String name;
    private Long ifSpeed = 0L;
    private Long srcEntityId = 0L;
    private Long destEntityId = 0L;
    private Long srcIfIndex = 0L;
    private Long destIfIndex = 0L;
    private Long srcIfSpeed = 0L;
    private Long destIfSpeed = 0L;
    private Double srcIfOctets = 0d;
    private Double destIfOctets = 0d;
    private Double srcIfOctetsRate = 0d;
    private Double destIfOctetsRate = 0d;
    private Double linkIfOctets = 0d;
    private Double linkIfOctetsRate = 0d;
    private Byte type = UNKNOWN_LINK_TYPE;
    private Integer connectType = (int) CONNECT_STRAIGHT;
    private Integer width = 1;
    private Boolean dashed = Boolean.FALSE;
    private Boolean startArrow = Boolean.FALSE;
    private Boolean endArrow = Boolean.FALSE;
    private String startArrowStyle = "none";
    private String endArrowStyle = "none";
    private String strokeColor = "#ffffff";
    private String strokeWeight = "1";
    private String note;
    private Timestamp createTime;
    private Timestamp modifyTime;

    public Integer getConnectType() {
        return connectType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public Long getDestEntityId() {
        return destEntityId;
    }

    public Double getDestIfOctets() {
        return destIfOctets;
    }

    public Double getDestIfOctetsRate() {
        return destIfOctetsRate;
    }

    /**
     * @return the destIfSpeed
     */
    public Long getDestIfSpeed() {
        return destIfSpeed;
    }

    public Long getDestIfIndex() {
        return destIfIndex;
    }

    public String getEndArrowStyle() {
        return endArrowStyle;
    }

    public Double getIfOctets() {
        if (srcIfOctets > 0 && destIfOctets > 0) {
            return Math.min(srcIfOctets, destIfOctets);
        } else {
            return Math.max(srcIfOctets, destIfOctets);
        }
    }

    /**
     * @return the destIfOctetsRate
     */
    public Double getIfOctetsRate() {
        if (srcIfOctetsRate > 0 && destIfOctetsRate > 0) {
            return Math.min(srcIfOctetsRate, destIfOctetsRate);
        } else {
            return Math.max(srcIfOctetsRate, destIfOctetsRate);
        }
    }

    public Long getIfSpeed() {
        if (ifSpeed == 0) {
            if (srcIfSpeed > 0 && destIfSpeed > 0) {
                ifSpeed = Math.min(srcIfSpeed, destIfSpeed);
            } else {
                ifSpeed = Math.max(srcIfSpeed, destIfSpeed);
            }
        }
        return ifSpeed;
    }

    public Long getLinkId() {
        return linkId;
    }

    public Double getLinkIfOctets() {
        return linkIfOctets;
    }

    public Double getLinkIfOctetsRate() {
        return linkIfOctetsRate;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public Long getSrcEntityId() {
        return srcEntityId;
    }

    public Double getSrcIfOctets() {
        return srcIfOctets;
    }

    public Double getSrcIfOctetsRate() {
        return srcIfOctetsRate;
    }

    /**
     * @return the srcIfSpeed
     */
    public Long getSrcIfSpeed() {
        return srcIfSpeed;
    }

    public Long getSrcIfIndex() {
        return srcIfIndex;
    }

    public String getStartArrowStyle() {
        return startArrowStyle;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public String getStrokeWeight() {
        return strokeWeight;
    }

    public Byte getType() {
        return type;
    }

    /**
     * @return the type
     */
    public String getTypeName() {
        switch (type) {
        case Link.L2_LINK_TYPE:
            return "L2Link";
        case Link.L3_LINK_TYPE:
            return "L3Link";
        case Link.MANUAL_LINK_TYPE:
            return "ManualLink";
        default:
            return "unknown";
        }
    }

    public Integer getWidth() {
        return width;
    }

    public Boolean isDashed() {
        return dashed;
    }

    public Boolean isEndArrow() {
        return endArrow;
    }

    public Boolean isStartArrow() {
        return startArrow;
    }

    public void setConnectType(Integer connectType) {
        this.connectType = connectType;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    public void setDestEntityId(Long destEntityId) {
        this.destEntityId = destEntityId;
    }

    public void setDestIfOctets(Double destIfOctets) {
        this.destIfOctets = destIfOctets;
    }

    public void setDestIfOctetsRate(Double destIfOctetsRate) {
        this.destIfOctetsRate = destIfOctetsRate;
    }

    /**
     * @param destIfSpeed
     *            the destIfSpeed to set
     */
    public void setDestIfSpeed(Long destIfSpeed) {
        this.destIfSpeed = destIfSpeed;
    }

    public void setDestIfIndex(Long destIfIndex) {
        this.destIfIndex = destIfIndex;
    }

    public void setEndArrow(Boolean endArrow) {
        this.endArrow = endArrow;
    }

    public void setEndArrowStyle(String endArrowStyle) {
        this.endArrowStyle = endArrowStyle;
    }

    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public void setLinkIfOctets(Double linkIfOctets) {
        this.linkIfOctets = linkIfOctets;
    }

    public void setLinkIfOctetsRate(Double linkIfOctetsRate) {
        this.linkIfOctetsRate = linkIfOctetsRate;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSrcEntityId(Long srcEntityId) {
        this.srcEntityId = srcEntityId;
    }

    public void setSrcIfOctets(Double srcIfOctets) {
        this.srcIfOctets = srcIfOctets;
    }

    public void setSrcIfOctetsRate(Double srcIfOctetsRate) {
        this.srcIfOctetsRate = srcIfOctetsRate;
    }

    /**
     * @param srcIfSpeed
     *            the srcIfSpeed to set
     */
    public void setSrcIfSpeed(Long srcIfSpeed) {
        this.srcIfSpeed = srcIfSpeed;
    }

    public void setSrcIfIndex(Long srcIfIndex) {
        this.srcIfIndex = srcIfIndex;
    }

    public void setStartArrow(Boolean startArrow) {
        this.startArrow = startArrow;
    }

    public void setStartArrowStyle(String startArrowStyle) {
        this.startArrowStyle = startArrowStyle;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeWeight(String strokeWeight) {
        this.strokeWeight = strokeWeight;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder("Link(").append(getTypeName()).append("):").append(srcEntityId).append(":")
                .append(srcIfIndex).append("--").append(destEntityId).append(":").append(destIfIndex).toString();
    }
}
