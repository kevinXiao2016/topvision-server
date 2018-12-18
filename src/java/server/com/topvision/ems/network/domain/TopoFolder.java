package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

public class TopoFolder extends BaseEntity implements TreeEntity, AliasesSuperType {
    private static final long serialVersionUID = -319227135184059342L;
    public static final int TOPOFOLDER_INC = 1;
    public static final int TYPE_TOPO_MAP = 1;
    public static final int TYPE_SUBNET = 5;
    public static final int TYPE_CLOUDY = 6;
    public static final int TYPE_HYPERLINK = 8;
    public static final int TYPE_REGION = 20;
    public static final int TYPE_OFF_MANAGEMENT = 10;
    public static final int TYPE_LONELY = 11;
    public static final int TYPE_TOPO_GOOGLE_MAP = 40;
    public static final int TOPO_FOLDER = 1;
    public static final long NETWORK_FOLDER = 2;
    public static final long PHYSICAL_FOLDER = 3;
    public static final long SUBNET_FOLDER = 4;
    public static final long REGION_FOLDER = 5;
    public static final String DEFAULT_BACKGROUND = "../images/background/world.jpg";
    public static final long DEFAULT_REFRESH_INTERVAL = 60000;
    public static final String DEFAULT_FOLDER_ICON = "../images/network/folder_48.gif";
    public static final String DEFAULT_HYPERLINK_ICON = "../images/network/href_48.png";
    public static final String DEFAULT_SUBNET_ICON = "../images/network/subnet_48.gif";
    public static final String DEFAULT_CLOUDY_ICON = "../images/network/cloudy_48.gif";
    public static final int LEFTTOP = 0;
    public static final int CENTER = 1;
    public static final int TILE = 2;

    private Long folderId = 0L;
    private Long mapNodeId = -1L;
    private Long superiorId = 0L;
    private String name;
    private Integer type = TYPE_TOPO_MAP;
    private Integer categoryId = FolderCategory.CLASS_NETWORK;
    private String superiorName;
    private Integer x;
    private Integer y;
    private Long refreshInterval = DEFAULT_REFRESH_INTERVAL;
    private String url;
    private String note;
    private String path;
    private String icon = DEFAULT_FOLDER_ICON;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Boolean backgroundFlag = Boolean.TRUE;
    private String backgroundImg = DEFAULT_BACKGROUND;
    private String backgroundColor = "#ffffff";
    private Integer backgroundPosition = TILE;
    private String linkColor = "#000000";
    private String linkSelectedColor = "#cebd8b";
    private Float linkWidth = 1.0F;
    private Boolean linkStartArrow = Boolean.FALSE;
    private Boolean linkEndArrow = Boolean.FALSE;
    private Boolean linkShadow = Boolean.TRUE;
    private String linkShadowColor = "#e0e0e0";
    private String nodeFillColor = "#c3d9ff";
    private Boolean markerAlertMode = Boolean.TRUE;
    private Boolean displayAlertIcon = Boolean.TRUE;
    private Boolean displayLinkLabel = Boolean.TRUE;
    private Boolean displayEntityLabel = Boolean.TRUE;
    private Boolean displayLink = Boolean.TRUE;
    private Boolean displayCluetip = Boolean.TRUE;
    // 2-设备别名/1-设备名称/0-设备IP(新建地域时，默认显示方式设定为“别名”)
    private int showType = 2;
    // 设备别名
    private Boolean displayName = Boolean.TRUE;
    // 设备名称
    private Boolean displaySysName = Boolean.FALSE;
    private Boolean displayGrid = Boolean.FALSE;
    private Boolean displayNoSnmp = Boolean.TRUE;
    private Boolean displayRouter = Boolean.TRUE;
    private Boolean displaySwitch = Boolean.TRUE;
    private Boolean displayL3switch = Boolean.TRUE;
    private Boolean displayServer = Boolean.TRUE;
    private Boolean displayDesktop = Boolean.TRUE;
    private Boolean displayOthers = Boolean.TRUE;
    private Long entityForOrgin = 0L;
    private Integer depthForOrgin = 1;
    private String entityLabel = TopoLabel.TYPE_CPU;
    private String linkLabel = TopoLabel.TYPE_LINKRATE;
    private Float zoom = 1.0F;
    private Boolean fixed = Boolean.FALSE;
    private Integer iconSize = 0;
    private Integer fontSize = 12;
    private String subnetIp;
    private String subnetMask;
    // 默认值在创建地域时用到,不要随意更改
    private Integer width = 2000;
    private Integer height = 2000;
    private String extend1;
    private String extend2;
    private Boolean chkDisabled;
    private Boolean checked;

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @return the backgroundFlag
     */
    public Boolean getBackgroundFlag() {
        return backgroundFlag;
    }

    /**
     * @return the backgroundImg
     */
    public String getBackgroundImg() {
        return backgroundImg;
    }

    /**
     * @return the backgroundPosition
     */
    public Integer getBackgroundPosition() {
        return backgroundPosition;
    }

    /**
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @return the depthForOrgin
     */
    public Integer getDepthForOrgin() {
        return depthForOrgin;
    }

    /**
     * @return the displayAlertIcon
     */
    public Boolean getDisplayAlertIcon() {
        return displayAlertIcon;
    }

    /**
     * @return the displayCluetip
     */
    public Boolean getDisplayCluetip() {
        return displayCluetip;
    }

    /**
     * @return the displayDesktop
     */
    public Boolean getDisplayDesktop() {
        return displayDesktop;
    }

    /**
     * @return the displayEntityLabel
     */
    public Boolean getDisplayEntityLabel() {
        return displayEntityLabel;
    }

    /**
     * @return the displayGrid
     */
    public Boolean getDisplayGrid() {
        return displayGrid;
    }

    /**
     * @return the displayL3switch
     */
    public Boolean getDisplayL3switch() {
        return displayL3switch;
    }

    /**
     * @return the displayLink
     */
    public Boolean getDisplayLink() {
        return displayLink;
    }

    /**
     * @return the displayLinkLabel
     */
    public Boolean getDisplayLinkLabel() {
        return displayLinkLabel;
    }

    /**
     * @return the displaySysName
     */
    public Boolean getDisplaySysName() {
        return displaySysName;
    }

    /**
     * @return the displayName
     */
    public Boolean getDisplayName() {
        return displayName;
    }

    /**
     * @return the displayNoSnmp
     */
    public Boolean getDisplayNoSnmp() {
        return displayNoSnmp;
    }

    /**
     * @return the displayOthers
     */
    public Boolean getDisplayOthers() {
        return displayOthers;
    }

    /**
     * @return the displayRouter
     */
    public Boolean getDisplayRouter() {
        return displayRouter;
    }

    /**
     * @return the displayServer
     */
    public Boolean getDisplayServer() {
        return displayServer;
    }

    /**
     * @return the displaySwitch
     */
    public Boolean getDisplaySwitch() {
        return displaySwitch;
    }

    /**
     * @return the entityForOrgin
     */
    public Long getEntityForOrgin() {
        return entityForOrgin;
    }

    /**
     * @return the entityLabel
     */
    public String getEntityLabel() {
        return entityLabel;
    }

    /**
     * @return the extend1
     */
    public String getExtend1() {
        return extend1;
    }

    /**
     * @return the extend2
     */
    public String getExtend2() {
        return extend2;
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
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the iconSize
     */
    public Integer getIconSize() {
        return iconSize;
    }

    @Override
    public String getId() {
        return String.valueOf(getFolderId());
    }

    /**
     * @return the linkColor
     */
    public String getLinkColor() {
        return linkColor;
    }

    /**
     * @return the linkEndArrow
     */
    public Boolean getLinkEndArrow() {
        return linkEndArrow;
    }

    /**
     * @return the linkLabel
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * @return the linkSelectedColor
     */
    public String getLinkSelectedColor() {
        return linkSelectedColor;
    }

    /**
     * @return the linkShadow
     */
    public Boolean getLinkShadow() {
        return linkShadow;
    }

    /**
     * @return the linkShadowColor
     */
    public String getLinkShadowColor() {
        return linkShadowColor;
    }

    /**
     * @return the linkStartArrow
     */
    public Boolean getLinkStartArrow() {
        return linkStartArrow;
    }

    /**
     * @return the linkWidth
     */
    public Float getLinkWidth() {
        return linkWidth;
    }

    /**
     * @return the markerAlertMode
     */
    public Boolean getMarkerAlertMode() {
        return markerAlertMode;
    }

    /**
     * @return the modifyTime
     */
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the nodeFillColor
     */
    public String getNodeFillColor() {
        return nodeFillColor;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    @Override
    public String getParentId() {
        return String.valueOf(getSuperiorId());
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the refreshInterval
     */
    public Long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * @return the subnetIp
     */
    public String getSubnetIp() {
        return subnetIp;
    }

    /**
     * @return the subnetMask
     */
    public String getSubnetMask() {
        return subnetMask;
    }

    /**
     * @return the superiorId
     */
    public Long getSuperiorId() {
        return superiorId;
    }

    /**
     * @return the superiorName
     */
    public String getSuperiorName() {
        return superiorName;
    }

    @Override
    public String getText() {
        return getName();
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
        return url;
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
     * @return the zoom
     */
    public Float getZoom() {
        return zoom;
    }

    /**
     * @param backgroundColor
     *            the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @param backgroundFlag
     *            the backgroundFlag to set
     */
    public void setBackgroundFlag(Boolean backgroundFlag) {
        this.backgroundFlag = backgroundFlag;
    }

    /**
     * @param backgroundImg
     *            the backgroundImg to set
     */
    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    /**
     * @param backgroundPosition
     *            the backgroundPosition to set
     */
    public void setBackgroundPosition(Integer backgroundPosition) {
        this.backgroundPosition = backgroundPosition;
    }

    /**
     * @param categoryId
     *            the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @param depthForOrgin
     *            the depthForOrgin to set
     */
    public void setDepthForOrgin(Integer depthForOrgin) {
        this.depthForOrgin = depthForOrgin;
    }

    /**
     * @param displayAlertIcon
     *            the displayAlertIcon to set
     */
    public void setDisplayAlertIcon(Boolean displayAlertIcon) {
        this.displayAlertIcon = displayAlertIcon;
    }

    /**
     * @param displayCluetip
     *            the displayCluetip to set
     */
    public void setDisplayCluetip(Boolean displayCluetip) {
        this.displayCluetip = displayCluetip;
    }

    /**
     * @param displayDesktop
     *            the displayDesktop to set
     */
    public void setDisplayDesktop(Boolean displayDesktop) {
        this.displayDesktop = displayDesktop;
    }

    /**
     * @param displayEntityLabel
     *            the displayEntityLabel to set
     */
    public void setDisplayEntityLabel(Boolean displayEntityLabel) {
        this.displayEntityLabel = displayEntityLabel;
    }

    /**
     * @param displayGrid
     *            the displayGrid to set
     */
    public void setDisplayGrid(Boolean displayGrid) {
        this.displayGrid = displayGrid;
    }

    /**
     * @param displayL3switch
     *            the displayL3switch to set
     */
    public void setDisplayL3switch(Boolean displayL3switch) {
        this.displayL3switch = displayL3switch;
    }

    /**
     * @param displayLink
     *            the displayLink to set
     */
    public void setDisplayLink(Boolean displayLink) {
        this.displayLink = displayLink;
    }

    /**
     * @param displayLinkLabel
     *            the displayLinkLabel to set
     */
    public void setDisplayLinkLabel(Boolean displayLinkLabel) {
        this.displayLinkLabel = displayLinkLabel;
    }

    /**
     * @param displaySysName
     *            the displaySysName to set
     */
    public void setDisplaySysName(Boolean displaySysName) {
        this.displaySysName = displaySysName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(Boolean displayName) {
        this.displayName = displayName;
    }

    /**
     * @param displayNoSnmp
     *            the displayNoSnmp to set
     */
    public void setDisplayNoSnmp(Boolean displayNoSnmp) {
        this.displayNoSnmp = displayNoSnmp;
    }

    /**
     * @param displayOthers
     *            the displayOthers to set
     */
    public void setDisplayOthers(Boolean displayOthers) {
        this.displayOthers = displayOthers;
    }

    /**
     * @param displayRouter
     *            the displayRouter to set
     */
    public void setDisplayRouter(Boolean displayRouter) {
        this.displayRouter = displayRouter;
    }

    /**
     * @param displayServer
     *            the displayServer to set
     */
    public void setDisplayServer(Boolean displayServer) {
        this.displayServer = displayServer;
    }

    /**
     * @param displaySwitch
     *            the displaySwitch to set
     */
    public void setDisplaySwitch(Boolean displaySwitch) {
        this.displaySwitch = displaySwitch;
    }

    /**
     * @param entityForOrgin
     *            the entityForOrgin to set
     */
    public void setEntityForOrgin(Long entityForOrgin) {
        this.entityForOrgin = entityForOrgin;
    }

    /**
     * @param entityLabel
     *            the entityLabel to set
     */
    public void setEntityLabel(String entityLabel) {
        this.entityLabel = entityLabel;
    }

    /**
     * @param extend1
     *            the extend1 to set
     */
    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    /**
     * @param extend2
     *            the extend2 to set
     */
    public void setExtend2(String extend2) {
        this.extend2 = extend2;
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
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param iconSize
     *            the iconSize to set
     */
    public void setIconSize(Integer iconSize) {
        this.iconSize = iconSize;
    }

    /**
     * @param linkColor
     *            the linkColor to set
     */
    public void setLinkColor(String linkColor) {
        this.linkColor = linkColor;
    }

    /**
     * @param linkEndArrow
     *            the linkEndArrow to set
     */
    public void setLinkEndArrow(Boolean linkEndArrow) {
        this.linkEndArrow = linkEndArrow;
    }

    /**
     * @param linkLabel
     *            the linkLabel to set
     */
    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    /**
     * @param linkSelectedColor
     *            the linkSelectedColor to set
     */
    public void setLinkSelectedColor(String linkSelectedColor) {
        this.linkSelectedColor = linkSelectedColor;
    }

    /**
     * @param linkShadow
     *            the linkShadow to set
     */
    public void setLinkShadow(Boolean linkShadow) {
        this.linkShadow = linkShadow;
    }

    /**
     * @param linkShadowColor
     *            the linkShadowColor to set
     */
    public void setLinkShadowColor(String linkShadowColor) {
        this.linkShadowColor = linkShadowColor;
    }

    /**
     * @param linkStartArrow
     *            the linkStartArrow to set
     */
    public void setLinkStartArrow(Boolean linkStartArrow) {
        this.linkStartArrow = linkStartArrow;
    }

    /**
     * @param linkWidth
     *            the linkWidth to set
     */
    public void setLinkWidth(Float linkWidth) {
        this.linkWidth = linkWidth;
    }

    /**
     * @param markerAlertMode
     *            the markerAlertMode to set
     */
    public void setMarkerAlertMode(Boolean markerAlertMode) {
        this.markerAlertMode = markerAlertMode;
    }

    /**
     * @param modifyTime
     *            the modifyTime to set
     */
    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param nodeFillColor
     *            the nodeFillColor to set
     */
    public void setNodeFillColor(String nodeFillColor) {
        this.nodeFillColor = nodeFillColor;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @param refreshInterval
     *            the refreshInterval to set
     */
    public void setRefreshInterval(Long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    /**
     * @param subnetIp
     *            the subnetIp to set
     */
    public void setSubnetIp(String subnetIp) {
        this.subnetIp = subnetIp;
    }

    /**
     * @param subnetMask
     *            the subnetMask to set
     */
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    /**
     * @param superiorId
     *            the superiorId to set
     */
    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    /**
     * @param superiorName
     *            the superiorName to set
     */
    public void setSuperiorName(String superiorName) {
        this.superiorName = superiorName;
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
     * @param zoom
     *            the zoom to set
     */
    public void setZoom(Float zoom) {
        this.zoom = zoom;
    }

    public Long getMapNodeId() {
        if (mapNodeId == -1) {
            return getFolderId();
        } else {
            return mapNodeId;
        }
    }

    public void setMapNodeId(Long mapNodeId) {
        this.mapNodeId = mapNodeId;
    }

    public int getShowType() {
        if (displayName) {
            showType = 2;
        } else {
            if (displaySysName) {
                showType = 1;
            } else {
                showType = 0;
            }
        }
        return showType;
    }

    public void setShowType(int showType) {
        if (showType == 2) {
            setDisplayName(true);
            setDisplaySysName(false);
        } else if (showType == 1) {
            setDisplayName(false);
            setDisplaySysName(true);
        } else {
            setDisplayName(false);
            setDisplaySysName(false);
        }
        this.showType = showType;
    }

    public Boolean getChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(Boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

}
