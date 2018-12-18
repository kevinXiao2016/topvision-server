package com.topvision.ems.network.domain;

import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class MapNode extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 2498577660148038657L;
    public static final long MAPNODE_INC = 10000000000L;
    public static final int UNKNOWN = 0;
    public static final int LINE = 10;
    public static final int DASHED_LINE = 11;
    public static final int ARROW_LINE = 12;
    public static final int DARROW_LINE = 13;
    public static final int POLY_LINE = 14;
    public static final int RECT = 16;
    public static final int ROUNDRECT = 17;
    public static final int OVAL = 18;
    public static final int TEXTRECT = 19;
    public static final int TEXT_ = 24;
    public static final int PICTURE = 25;

    private Long nodeId = 0L;
    private Long folderId = 0L;
    private String name;
    private Integer type = UNKNOWN;
    private String strokeColor = "#000000";
    private String fillColor = "#FFFFFF";
    // private Boolean dashed = Boolean.FALSE;
    private Boolean dashed = Boolean.FALSE;
    private Boolean startArrow = Boolean.FALSE;
    private Boolean endArrow = Boolean.FALSE;
    private String text;
    private String href;
    private Integer x = 0;
    private Integer y = 0;
    private Integer width = 0;
    private Integer height = 0;
    private String icon;
    private String textColor = "#000000";
    private Double strokeWeight = 1d;
    private Boolean shadow = Boolean.FALSE;
    private String shadowColor = "gray";
    private Integer shadowOffset = 5;
    private String points;
    private String url;
    private Boolean gradient = Boolean.FALSE;
    private String gradientColor = "#ffffff";
    private Integer fontSize = 12;
    private String fontStyle = "bold";
    private String fontWeight = "bold";
    private Integer zIndex;
    private Long groupId;
    private Integer userObjType = NetworkConstants.TYPE_SHAPE;
    private Long userObjId;

    // 组是否展开, 对组有效
    private Boolean expanded = Boolean.TRUE;
    // 是否固定位置
    private Boolean fixed = Boolean.FALSE;
    // 是否可见.
    private Boolean visible = Boolean.TRUE;
    // 是否可移动
    private Boolean movable = Boolean.TRUE;
    // 是否允许选择
    private Boolean selectable = Boolean.TRUE;
    // 是否允许缩放大小
    private Boolean resizable = Boolean.TRUE;

    /**
     * @return the dashed
     */
    public Boolean getDashed() {
        return dashed;
    }

    /**
     * @return the endArrow
     */
    public Boolean getEndArrow() {
        return endArrow;
    }

    /**
     * @return the expanded
     */
    public Boolean getExpanded() {
        return expanded;
    }

    /**
     * @return the fillColor
     */
    public String getFillColor() {
        return fillColor;
    }

    /**
     * @return the fixed
     */
    public Boolean getFixed() {
        return fixed;
    }

    /**
     * @return the folderId
     */
    public Long getFolderId() {
        return folderId;
    }

    /**
     * @return the fontSize
     */
    public Integer getFontSize() {
        return fontSize;
    }

    /**
     * @return the fontStyle
     */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * @return the fontWeight
     */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
     * @return the gradient
     */
    public Boolean getGradient() {
        return gradient;
    }

    /**
     * @return the gradientColor
     */
    public String getGradientColor() {
        return gradientColor;
    }

    /**
     * @return the groupId
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the movable
     */
    public Boolean getMovable() {
        return movable;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the nodeId
     */
    public Long getNodeId() {
        return nodeId;
    }

    /**
     * @return the points
     */
    public String getPoints() {
        return points;
    }

    /**
     * @return the resizable
     */
    public Boolean getResizable() {
        return resizable;
    }

    /**
     * @return the selectable
     */
    public Boolean getSelectable() {
        return selectable;
    }

    /**
     * @return the shadow
     */
    public Boolean getShadow() {
        return shadow;
    }

    /**
     * @return the shadowColor
     */
    public String getShadowColor() {
        return shadowColor;
    }

    /**
     * @return the shadowOffset
     */
    public Integer getShadowOffset() {
        return shadowOffset;
    }

    /**
     * @return the startArrow
     */
    public Boolean getStartArrow() {
        return startArrow;
    }

    /**
     * @return the strokeColor
     */
    public String getStrokeColor() {
        return strokeColor;
    }

    /**
     * @return the strokeWeight
     */
    public Double getStrokeWeight() {
        return strokeWeight;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the textColor
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url == null ? null : url.trim();
    }

    /**
     * @return the userObjId
     */
    public Long getUserObjId() {
        return userObjId;
    }

    /**
     * @return the userObjType
     */
    public Integer getUserObjType() {
        return userObjType;
    }

    /**
     * @return the visible
     */
    public Boolean getVisible() {
        return visible;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @return the zIndex
     */
    public Integer getzIndex() {
        return zIndex;
    }

    /**
     * @param dashed
     *            the dashed to set
     */
    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * @param endArrow
     *            the endArrow to set
     */
    public void setEndArrow(Boolean endArrow) {
        this.endArrow = endArrow;
    }

    /**
     * @param expanded
     *            the expanded to set
     */
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * @param fillColor
     *            the fillColor to set
     */
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * @param fixed
     *            the fixed to set
     */
    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * @param fontSize
     *            the fontSize to set
     */
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @param fontStyle
     *            the fontStyle to set
     */
    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     * @param fontWeight
     *            the fontWeight to set
     */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    /**
     * @param gradient
     *            the gradient to set
     */
    public void setGradient(Boolean gradient) {
        this.gradient = gradient;
    }

    /**
     * @param gradientColor
     *            the gradientColor to set
     */
    public void setGradientColor(String gradientColor) {
        this.gradientColor = gradientColor;
    }

    /**
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param movable
     *            the movable to set
     */
    public void setMovable(Boolean movable) {
        this.movable = movable;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param nodeId
     *            the nodeId to set
     */
    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @param points
     *            the points to set
     */
    public void setPoints(String points) {
        this.points = points;
    }

    /**
     * @param resizable
     *            the resizable to set
     */
    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    /**
     * @param selectable
     *            the selectable to set
     */
    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * @param shadow
     *            the shadow to set
     */
    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    /**
     * @param shadowColor
     *            the shadowColor to set
     */
    public void setShadowColor(String shadowColor) {
        this.shadowColor = shadowColor;
    }

    /**
     * @param shadowOffset
     *            the shadowOffset to set
     */
    public void setShadowOffset(Integer shadowOffset) {
        this.shadowOffset = shadowOffset;
    }

    /**
     * @param startArrow
     *            the startArrow to set
     */
    public void setStartArrow(Boolean startArrow) {
        this.startArrow = startArrow;
    }

    /**
     * @param strokeColor
     *            the strokeColor to set
     */
    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * @param strokeWeight
     *            the strokeWeight to set
     */
    public void setStrokeWeight(Double strokeWeight) {
        this.strokeWeight = strokeWeight;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @param textColor
     *            the textColor to set
     */
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param userObjId
     *            the userObjId to set
     */
    public void setUserObjId(Long userObjId) {
        this.userObjId = userObjId;
    }

    /**
     * @param userObjType
     *            the userObjType to set
     */
    public void setUserObjType(Integer userObjType) {
        this.userObjType = userObjType;
    }

    /**
     * @param visible
     *            the visible to set
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @param zIndex
     *            the zIndex to set
     */
    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }
}
