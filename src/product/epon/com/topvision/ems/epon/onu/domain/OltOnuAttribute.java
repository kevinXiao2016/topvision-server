/***********************************************************************
 * $Id: OltOnuAttribute.java,v1.0 2011-9-26 上午09:18:16 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * Onu属性
 * 
 * @author zhanglongyang
 * 
 */
public class OltOnuAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4682843933096808561L;

    public static final int ONU_TYPE_FIXED = 1;
    public static final int ONU_TYPE_CHASSISBASED = 2;
    public static final int ONU_TYPE_CCMTS = 0xF1; // 8800A or 8800C-A
    public static final int ONU_TYPE_CCMTS_E = 0xF4; // 8800E
    public static final int ONU_TYPE_CCMTS_C_E = 0xF6; // C-E
    public static final int ONU_TYPE_CCMTS_D_E = 0xF7; // D-E
    public static final int ONU_TYPE_CCMTS_C_10G = 0xF8; // C-10G
    public static final int ONU_TYPE_CCMTS_F = 0xF9; // F
    public static final int ONU_TYPE_UNKNOWN = 255;
    public static final int ONU_ENTITYTYPE_OTHERCORP = 13100;
    private static final int Type_PN8644 = 1178939702;
    private static final int Type_PN8641 = 1178087477;
    public static final int ONU_TYPE_NONE = 1852796517;

    public static final int ONU_ENTIYTTYPE_CCMTS_A = 30001;

    public static final String EPON_OID_PRE = "1.3.6.1.4.1.17409.2.3";
    public static final String GPON_OID_PRE = "1.3.6.1.4.1.17409.2.8";

    protected String entityIp; // 所属oltip
    protected Long entityId;
    protected Long onuId;
    protected Long ponId;
    protected String ponName;
    protected String position;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.1", index = true)
    private Long onuMibIndex;
    protected Long onuIndex;
    // onuName由网管侧维护
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.2", writable = true, type = "OctetString")
    private String onuName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.3")
    private Integer onuType;
    private String onuTypeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.4")
    private String onuIpAddress;
    private Integer onuPreType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.7")
    private String onuMac;
    private Long onuMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.8")
    private Integer onuOperationStatus;
    private String onuOperationStatusString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.9", writable = true, type = "Integer32")
    private Integer onuAdminStatus;
    private String onuAdminStatusString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.10")
    private String onuChipVendor;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.11")
    private String onuChipType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.12")
    private String onuChipVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.13")
    private String onuSoftwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.14")
    private String onuFirmwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.15")
    private Integer onuTestDistance;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.16")
    private Integer onuLlidId;
    private String onuLlid16;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.17", writable = true, type = "Integer32")
    private Integer resetONU;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.18")
    private Long onuTimeSinceLastRegister;
    protected String onuIcon;
    protected String topOnuHardwareVersion;
    protected Timestamp changeTime;
    protected EntityType entityType;
    protected Long typeId;
    // onu 别名,解决排序时无法使用数据库列别名问题
    protected String name;
    protected Integer ponPerfStats15minuteEnable;
    protected Integer temperatureDetectEnable;
    protected Integer onuIsolationEnable;
    protected Boolean attention;
    protected String topOnuExtAttr;
    protected Integer catvCapability;
    protected Integer wlanCapability;

    private String onuUniqueIdentification;
    private String uniqueId;
    private Integer laserSwitch;

    // EPON OR GPON
    private String onuEorG = EponConstants.EPON_ONU;
    
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.28")  //epon loid认证时需要sn
    private String onuSerialNum;

    // Add by Rod
    private Timestamp lastDeregisterTime;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the ponName
     */
    public String getPonName() {
        return ponName;
    }

    /**
     * @param ponName
     *            the ponName to set
     */
    public void setPonName(String ponName) {
        this.ponName = ponName;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        // 设置所属PON口名称
        ponName = EponIndex.getSlotNo(onuIndex) + "/" + EponIndex.getPonNo(onuIndex);
    }

    /**
     * @return the onuName
     */
    public String getOnuName() {
        return onuName;
    }

    /**
     * @param onuName
     *            the onuName to set
     */
    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    /**
     * @return the onuType
     */
    public Integer getOnuType() {
        return this.onuType;
    }

    /**
     * @param onuType
     *            the onuType to set
     */
    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }

    /**
     * @return the onuIpAddress
     */
    public String getOnuIpAddress() {
        return onuIpAddress;
    }

    /**
     * @param onuIpAddress
     *            the onuIpAddress to set
     */
    public void setOnuIpAddress(String onuIpAddress) {
        this.onuIpAddress = onuIpAddress;
    }

    /**
     * @return the onuMacAddress
     */
    public Long getOnuMacAddress() {
        return onuMacAddress;
    }

    /**
     * @param onuMacAddress
     *            the onuMacAddress to set
     */
    public void setOnuMacAddress(Long onuMacAddress) {
        this.onuMacAddress = onuMacAddress;
        this.onuMac = new MacUtils(onuMacAddress).toString(MacUtils.MAOHAO).toUpperCase();
    }

    /**
     * @return the onuMac
     */
    public String getOnuMac() {
        return onuMac;
    }

    /**
     * @param onuMac
     *            the onuMac to set
     */
    public void setOnuMac(String onuMac) {
        this.onuMac = EponUtil.getMacStringFromNoISOControl(onuMac);
        this.onuMacAddress = new MacUtils(this.onuMac).longValue();
    }

    /**
     * @return the onuOperationStatus
     */
    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    /**
     * @param onuOperationStatus
     *            the onuOperationStatus to set
     */
    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    /**
     * @return the onuAdminStatus
     */
    public Integer getOnuAdminStatus() {
        return onuAdminStatus;
    }

    /**
     * @param onuAdminStatus
     *            the onuAdminStatus to set
     */
    public void setOnuAdminStatus(Integer onuAdminStatus) {
        this.onuAdminStatus = onuAdminStatus;
    }

    /**
     * @return the onuChipVendor
     */
    public String getOnuChipVendor() {
        return onuChipVendor;
    }

    /**
     * @param onuChipVendor
     *            the onuChipVendor to set
     */
    public void setOnuChipVendor(String onuChipVendor) {
        this.onuChipVendor = onuChipVendor;
    }

    /**
     * @return the onuChipType
     */
    public String getOnuChipType() {
        return onuChipType;
    }

    /**
     * @param onuChipType
     *            the onuChipType to set
     */
    public void setOnuChipType(String onuChipType) {
        this.onuChipType = onuChipType;
    }

    /**
     * @return the onuChipVersion
     */
    public String getOnuChipVersion() {
        return onuChipVersion;
    }

    /**
     * @param onuChipVersion
     *            the onuChipVersion to set
     */
    public void setOnuChipVersion(String onuChipVersion) {
        this.onuChipVersion = onuChipVersion;
    }

    /**
     * @return the onuSoftwareVersion
     */
    public String getOnuSoftwareVersion() {
        return onuSoftwareVersion;
    }

    /**
     * @param onuSoftwareVersion
     *            the onuSoftwareVersion to set
     */
    public void setOnuSoftwareVersion(String onuSoftwareVersion) {
        this.onuSoftwareVersion = onuSoftwareVersion;
    }

    /**
     * @return the onuFirmwareVersion
     */
    public String getOnuFirmwareVersion() {
        return onuFirmwareVersion;
    }

    /**
     * @param onuFirmwareVersion
     *            the onuFirmwareVersion to set
     */
    public void setOnuFirmwareVersion(String onuFirmwareVersion) {
        this.onuFirmwareVersion = onuFirmwareVersion;
    }

    /**
     * @return the onuTestDistance
     */
    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    /**
     * @param onuTestDistance
     *            the onuTestDistance to set
     */
    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    /**
     * @return the onuLlidId
     */
    public Integer getOnuLlidId() {
        return onuLlidId;
    }

    /**
     * @param onuLlidId
     *            the onuLlidId to set
     */
    public void setOnuLlidId(Integer onuLlidId) {
        this.onuLlidId = onuLlidId;
        this.onuLlid16 = "0x" + Integer.toHexString(onuLlidId).toUpperCase();
    }

    /**
     * @return the onuTimeSinceLastRegister
     */
    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    /**
     * @param onuTimeSinceLastRegister
     *            the onuTimeSinceLastRegister to set
     */
    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
    }

    public Integer getResetONU() {
        return resetONU;
    }

    public void setResetONU(Integer resetONU) {
        this.resetONU = resetONU;
    }

    public String getOnuIcon() {
        return onuIcon;
    }

    public void setOnuIcon(String onuIcon) {
        this.onuIcon = onuIcon;
    }

    public String getOnuOperationStatusString() {
        return onuOperationStatusString;
    }

    public void setOnuOperationStatusString(String onuOperationStatusString) {
        this.onuOperationStatusString = onuOperationStatusString;
    }

    public String getOnuAdminStatusString() {
        return onuAdminStatusString;
    }

    public void setOnuAdminStatusString(String onuAdminStatusString) {
        this.onuAdminStatusString = onuAdminStatusString;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOnuTypeString() {
        return onuTypeString;
    }

    public void setOnuTypeString(String onuTypeString) {
        this.onuTypeString = onuTypeString;
    }

    public Integer getOnuPreType() {
        /*
         * if (GponConstant.GPON_ONU.equals(onuEorG)) { return 37; }
         */
        // 确保在ONU类型获取不到时默认使用未知类型
        return onuPreType == null ? EponConstants.UNKNOWN_ONU_TYPE : onuPreType;
    }

    public void setOnuPreType(Integer onuPreType) {
        // TODO 应付现网测试代码 特殊处理两种类型ONU
        if (onuPreType.equals(Type_PN8644)) {
            this.onuPreType = 68;
        } else if (onuPreType.equals(Type_PN8641)) {
            this.onuPreType = 65;
        } else {
            this.onuPreType = onuPreType;
        }
    }

    public String getTopOnuHardwareVersion() {
        return topOnuHardwareVersion;
    }

    public void setTopOnuHardwareVersion(String topOnuHardwareVersion) {
        this.topOnuHardwareVersion = topOnuHardwareVersion;
    }

    public String getOnuLlid16() {
        return onuLlid16;
    }

    public void setOnuLlid16(String onuLlid16) {
        this.onuLlid16 = onuLlid16;
    }

    /**
     * @return the entityType
     */
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * @param entityType
     *            the entityType to set
     */
    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPonPerfStats15minuteEnable() {
        return ponPerfStats15minuteEnable;
    }

    public void setPonPerfStats15minuteEnable(Integer ponPerfStats15minuteEnable) {
        this.ponPerfStats15minuteEnable = ponPerfStats15minuteEnable;
    }

    public Integer getTemperatureDetectEnable() {
        return temperatureDetectEnable;
    }

    public void setTemperatureDetectEnable(Integer temperatureDetectEnable) {
        this.temperatureDetectEnable = temperatureDetectEnable;
    }

    public Integer getOnuIsolationEnable() {
        return onuIsolationEnable;
    }

    public void setOnuIsolationEnable(Integer onuIsolationEnable) {
        this.onuIsolationEnable = onuIsolationEnable;
    }

    public Boolean getAttention() {
        return attention;
    }

    public void setAttention(Boolean attention) {
        if (attention == null) {
            this.attention = false;
        } else {
            this.attention = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        OltOnuAttribute objAttribute;
        try {
            objAttribute = (OltOnuAttribute) obj;
        } catch (Exception e) {
            return false;
        }
        if (this.getOnuEorG().equals(objAttribute.getOnuEorG())
                && this.getEntityId().equals(objAttribute.getEntityId())
                && this.getOnuIndex().equals(objAttribute.getOnuIndex())
                && (this.getOnuUniqueIdentification().replaceAll(":", "").toUpperCase().equals(objAttribute
                        .getOnuUniqueIdentification().replaceAll(":", "").toUpperCase()))) {
            return true;
        }
        return false;
    }

    public static boolean isOnuBelongToCCMTS_E(int onuPreType) {
        if (onuPreType == ONU_TYPE_CCMTS_E || onuPreType == ONU_TYPE_CCMTS_C_E || onuPreType == ONU_TYPE_CCMTS_D_E
                || onuPreType == ONU_TYPE_CCMTS_C_10G || onuPreType == ONU_TYPE_CCMTS_F) {
            return true;
        }
        return false;
    }

    public static boolean isOnuBelongToCCMTS_A(int onuPreType) {
        if (onuPreType == ONU_TYPE_CCMTS) {
            return true;
        }
        return false;
    }

    /**
     * @return the topOnuExtAttr
     */
    public String getTopOnuExtAttr() {
        return topOnuExtAttr;
    }

    /**
     * @param topOnuExtAttr
     *            the topOnuExtAttr to set
     */
    public void setTopOnuExtAttr(String topOnuExtAttr) {
        this.topOnuExtAttr = topOnuExtAttr;
        if (topOnuExtAttr != null) {
            catvCapability = EponUtil.getCatvCapability(topOnuExtAttr);
            wlanCapability = EponUtil.getWlanCapability(topOnuExtAttr);
        } else {
            catvCapability = 0;
            wlanCapability = 0;
        }
    }

    /**
     * @return the catvCapability
     */
    public Integer getCatvCapability() {
        return catvCapability;
    }

    /**
     * @param catvCapability
     *            the catvCapability to set
     */
    public void setCatvCapability(Integer catvCapability) {
        this.catvCapability = catvCapability;
    }

    /**
     * @return the wlanCapability
     */
    public Integer getWlanCapability() {
        return wlanCapability;
    }

    /**
     * @param wlanCapability
     *            the wlanCapability to set
     */
    public void setWlanCapability(Integer wlanCapability) {
        this.wlanCapability = wlanCapability;
    }

    /**
     * @return the onuEorG
     */
    public String getOnuEorG() {
        return onuEorG;
    }

    /**
     * @param onuEorG
     *            the onuEorG to set
     */
    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

    /**
     * @return the onuSerialNum
     */
    public String getOnuSerialNum() {
        return onuSerialNum;
    }

    /**
     * @param onuSerialNum
     *            the onuSerialNum to set
     */
    public void setOnuSerialNum(String onuSerialNum) {
        this.onuSerialNum = onuSerialNum;
    }

    /**
     * @return the onuUniqueIdentification
     */
    public String getOnuUniqueIdentification() {
        if (onuUniqueIdentification == null) {
            if (EponConstants.EPON_ONU.equals(this.onuEorG)) {
                onuUniqueIdentification = onuMac;
            } else if (GponConstant.GPON_ONU.equals(this.onuEorG)) {
                onuUniqueIdentification = onuSerialNum;
            }
        }
        return onuUniqueIdentification;
    }

    /**
     * @param onuUniqueIdentification
     *            the onuUniqueIdentification to set
     */
    public void setOnuUniqueIdentification(String onuUniqueIdentification) {
        this.onuUniqueIdentification = onuUniqueIdentification.replaceAll(":", "").toUpperCase();
    }

    /**
     * @return the lastDeregisterTime
     */
    public Timestamp getLastDeregisterTime() {
        return lastDeregisterTime;
    }

    /**
     * @param lastDeregisterTime
     *            the lastDeregisterTime to set
     */
    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        this.lastDeregisterTime = lastDeregisterTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getLaserSwitch() {
        return laserSwitch;
    }

    public void setLaserSwitch(Integer laserSwitch) {
        this.laserSwitch = laserSwitch;
    }

}
