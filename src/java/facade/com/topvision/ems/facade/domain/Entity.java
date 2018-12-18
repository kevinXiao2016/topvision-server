/**
 * 
 */
package com.topvision.ems.facade.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author niejun
 * 
 */
@Alias("entity")
public class Entity extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 6782364575441771455L;
    public static final long UNKNOWN = 1;
    public static final int UNKNOWN_TYPE = -1;

    public static final long ENTITY_INC = 30000000000L;
    // 设备显示字段
    public static final String IP_DISPLAY_FIELD = "ip";
    public static final String MAC_DISPLAY_FIELD = "mac";
    public static final String NAME_DISPLAY_FIELD = "name";
    public static final String SYS_NAME_DISPLAY_FIELD = "sysName";
    public static final String IP_NAME_DISPLAY_FIELD = "ip_name";
    public static final String NAME_IP_DISPLAY_FIELD = "name_ip";

    private long entityId;
    private Long parentId;
    private long corpId = UNKNOWN;
    private long categoryId;
    /* 型号 */
    private long typeId = UNKNOWN_TYPE;
    /* 型号 */
    private long modelId = UNKNOWN_TYPE;
    private String name;
    private String contact;
    private String ip;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private String icon;
    private boolean snmpSupport;
    private boolean agentSupport;
    private String agentInstalled = "0";
    private String sysName;
    private String sysDescr;
    private String sysObjectID;
    private String sysContact;
    private String sysLocation;
    private String sysServices;
    private String sysUpTime;
    private String panel;
    private String defaultPanel;
    private String os;
    private String mac;
    private String ipv6;
    private String location;
    private String duty;
    private SnmpParam param;// for snmp param
    private String note;
    private long offManagementTime;
    private String driverId;
    // 1:设备正在刷新 0:设备可刷新
    private Integer extend1;
    // 设备是否被当前用户关注
    private boolean attention;

    // 是否被停止管理, true表示被管理, false表是已经停止管理.
    private boolean status = true;
    private String url;
    // is virtual entity
    private boolean virtual = false;

    private Long folderId;
    private String folderIds;
    private String folderName;
    private Integer x = 0;
    private Integer y = 0;
    private String corpName;
    private String typeName;
    private String modelName;
    private String icon16;
    private String icon32;
    private String icon48;
    private String icon64;
    private Integer entityCount = 0;
    private String iconInFolder;
    private String nameInFolder;
    private String displayName;
    private Long groupId;
    private String displayText;
    private Boolean fixed = Boolean.FALSE;
    private Boolean visible = Boolean.TRUE;
    private String oldIp;
    private String typePath;
    private String modulePath;
    private String module;
    // Add by Rod 主要用于在拓扑过程中提示当前拓扑状态
    private String topoInfo;
    // 表示该设备是属于真实拓扑图设备还是快捷方式
    private Integer virtualNetworkStatus = 1;
    private Timestamp lastRefreshTime;
    private Long authorityUserId;
    // CCMTS或者ONU的软件版本
    private String softwareVersion;
    private String uplinkDevice;

    //snmpparam属性
    private Integer version;
    private String community;
    private String writeCommunity;
    private String username;
    private String authProtocol;
    private String privProtocol;
    private String authPassword;
    private String privPassword;

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    @Deprecated
    public String getCorpName() {
        return corpName;
    }

    public String getDisplayName() {
        if (getName() == null) {
            displayName = getIp();
        } else {
            displayName = getName();
        }
        return displayName;
    }

    public String getDisplayText() {
        if (displayText == null) {
            // modified by huangdongsheng 支持在google地图上添加CCMTS 8800A
            // 当name为空时判断是否有Mac
            if (getName() == null) {
                if (getMac() == null) {
                    displayText = getIp();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getMac());
                    sb.append(" [");
                    sb.append(getIp());
                    sb.append("]");
                    displayText = sb.toString();
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(getName());
                sb.append(" [");
                sb.append(getIp());
                sb.append("]");
                displayText = sb.toString();
            }

        }
        return displayText;
    }

    public Integer getEntityCount() {
        return entityCount;
    }

    public Boolean getFixed() {
        return fixed;
    }

    /**
     * @return the folderId
     */
    public Long getFolderId() {
        return folderId;
    }

    public Long getGroupId() {
        return groupId;
    }

    /**
     * @return
     */
    public String getIcon() {
        String i = icon;
        return (i == null || "".equals(i)) ? ("/images/" + icon48) : i;
    }

    /**
     * @return
     */
    public String getIcon16() {
        return icon16;
    }

    /**
     * @return the icon32
     */
    public String getIcon32() {
        return icon32;
    }

    public String getIcon48() {
        return icon48;
    }

    public String getIconInFolder() {
        if (iconInFolder == null) {
            return getIcon();
        }
        return iconInFolder;
    }

    public String getModelName() {
        return modelName;
    }

    public String getNameInFolder() {
        if (nameInFolder == null) {
            return getDisplayName();
        }
        return nameInFolder;
    }

    public String getOldIp() {
        return oldIp;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    public Boolean getVisible() {
        return visible;
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

    /*
     * public Boolean isFixed() { return fixed; }
     */

    /*
     * public Boolean isVisible() { return visible; }
     */

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }

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

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setIcon16(String icon16) {
        this.icon16 = icon16;
    }

    /**
     * @param icon32
     *            the icon32 to set
     */
    public void setIcon32(String icon32) {
        this.icon32 = icon32;
    }

    public void setIcon48(String icon48) {
        this.icon48 = icon48;
    }

    public void setIconInFolder(String iconInFolder) {
        this.iconInFolder = iconInFolder;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setNameInFolder(String nameInFolder) {
        this.nameInFolder = nameInFolder;
    }

    public void setOldIp(String oldIp) {
        this.oldIp = oldIp;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (nameInFolder != null) {
            return nameInFolder;
        }
        if (getName() != null) {
            return getName();
        }
        if (getIp() != null) {
            return getIp();
        }
        return String.valueOf(getEntityId());
    }

    public String getAgentInstalled() {
        return agentInstalled;
    }

    public long getCategoryId() {
        return categoryId;
    }

    /**
     * @return the corpId
     */
    public long getCorpId() {
        return corpId;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    public String getDefaultPanel() {
        return defaultPanel;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDuty() {
        return duty;
    }

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    public String getIpv6() {
        return ipv6;
    }

    public String getLocation() {
        return location;
    }

    public String getMac() {
        return mac;
    }

    public long getModelId() {
        return modelId;
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

    public String getNote() {
        return note;
    }

    public long getOffManagementTime() {
        return offManagementTime;
    }

    public String getOs() {
        return os;
    }

    /**
     * @return the panel
     */
    public String getPanel() {
        return panel == null ? defaultPanel : panel;
    }

    /**
     * @return the param
     */
    public SnmpParam getParam() {
        if (param == null) {
            param = new SnmpParam();
        }
        param.setEntityId(entityId);
        return param;
    }

    public Long getParentId() {
        return parentId;
    }

    /**
     * @return the sysContact
     */
    public String getSysContact() {
        return sysContact;
    }

    /**
     * @return the sysDescr
     */
    public String getSysDescr() {
        return sysDescr;
    }

    /**
     * @return the sysLocation
     */
    public String getSysLocation() {
        return sysLocation;
    }

    /**
     * @return the sysName
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * @return the sysObjectID
     */
    public String getSysObjectID() {
        return sysObjectID;
    }

    /**
     * @return the sysServices
     */
    public String getSysServices() {
        return sysServices;
    }

    public String getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    /**
     * @return the typeId
     */
    public long getTypeId() {
        return typeId;
    }

    public String getUrl() {
        return url;
    }

    /*
     * public String isAgentInstalled() { return agentInstalled; }
     */
    @Deprecated
    public boolean isAgentSupport() {
        agentSupport = "1".equals(agentInstalled);
        return agentSupport;
    }

    /**
     * @return the snmpSupport
     */
    public boolean isSnmpSupport() {
        return snmpSupport;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setAgentInstalled(String agentInstalled) {
        this.agentInstalled = agentInstalled;
    }

    public void setAgentSupport(boolean agentSupport) {
        this.agentSupport = agentSupport;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @param corpId
     *            the corpId to set
     */
    public void setCorpId(long corpId) {
        this.corpId = corpId;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setDefaultPanel(String defaultPanel) {
        this.defaultPanel = defaultPanel;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
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

    public void setNote(String note) {
        this.note = note;
    }

    public void setOffManagementTime(long offManagementTime) {
        this.offManagementTime = offManagementTime;
    }

    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @param panel
     *            the panel to set
     */
    public void setPanel(String panel) {
        this.panel = panel;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(SnmpParam param) {
        this.param = param;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @param snmpSupport
     *            the snmpSupport to set
     */
    public void setSnmpSupport(boolean snmpSupport) {
        this.snmpSupport = snmpSupport;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @param sysContact
     *            the sysContact to set
     */
    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    /**
     * @param sysDescr
     *            the sysDescr to set
     */
    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    /**
     * @param sysLocation
     *            the sysLocation to set
     */
    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    /**
     * @param sysName
     *            the sysName to set
     */
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * @param sysObjectID
     *            the sysObjectID to set
     */
    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    /**
     * @param sysServices
     *            the sysServices to set
     */
    public void setSysServices(String sysServices) {
        this.sysServices = sysServices;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(long typeId) {
        this.typeId = typeId;
        this.modelId = typeId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public String getShowName() {
        // if (name == null || name.equals("")) {
        // if (sysName == null || sysName.equals("")) {
        // if (ip == null || ip.equals("")) {
        // return "";
        // } else {
        // return ip;
        // }
        // } else {
        // return sysName;
        // }
        // } else {
        // return name;
        // }
        return name;
    }

    public String getShowSysName() {
        if (sysName == null || sysName.equals("")) {
            if (ip == null || ip.equals("")) {
                return "";
            } else {
                return ip;
            }
        } else {
            return sysName;
        }
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getTypePath() {
        return typePath;
    }

    public void setTypePath(String typePath) {
        this.typePath = typePath;
    }

    /**
     * @return the virtualNetworkStatus
     */
    public Integer getVirtualNetworkStatus() {
        return virtualNetworkStatus;
    }

    /**
     * @param virtualNetworkStatus
     *            the virtualNetworkStatus to set
     */
    public void setVirtualNetworkStatus(Integer virtualNetworkStatus) {
        this.virtualNetworkStatus = virtualNetworkStatus;
    }

    public Integer getExtend1() {
        return extend1;
    }

    public void setExtend1(Integer extend1) {
        this.extend1 = extend1;
    }

    /**
     * @return the attention
     */
    public boolean isAttention() {
        return attention;
    }

    /**
     * @param attention
     *            the attention to set
     */
    public void setAttention(Integer attention) {
        if (attention == null) {
            this.attention = false;
        } else {
            this.attention = true;
        }
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module
     *            the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the topoInfo
     */
    public String getTopoInfo() {
        return topoInfo;
    }

    /**
     * @param topoInfo
     *            the topoInfo to set
     */
    public void setTopoInfo(String topoInfo) {
        this.topoInfo = topoInfo;
    }

    /**
     * @return the lastRefreshTime
     */
    public Timestamp getLastRefreshTime() {
        return lastRefreshTime;
    }

    /**
     * @param lastRefreshTime
     *            the lastRefreshTime to set
     */
    public void setLastRefreshTime(Timestamp lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    /**
     * @return the authorityUserId
     */
    public Long getAuthorityUserId() {
        return authorityUserId;
    }

    /**
     * @param authorityUserId
     *            the authorityUserId to set
     */
    public void setAuthorityUserId(Long authorityUserId) {
        this.authorityUserId = authorityUserId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        Entity objEntity = (Entity) obj;
        return entityId == objEntity.getEntityId();
    }

    /**
     * @return the icon64
     */
    public String getIcon64() {
        return icon64;
    }

    /**
     * @param icon64
     *            the icon64 to set
     */
    public void setIcon64(String icon64) {
        this.icon64 = icon64;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    public String getPrivProtocol() {
        return privProtocol;
    }

    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    public String getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(String folderIds) {
        this.folderIds = folderIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
