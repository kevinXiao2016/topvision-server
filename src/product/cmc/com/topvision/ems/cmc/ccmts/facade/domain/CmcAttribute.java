/***********************************************************************
 * $Id: CmcAttribute.java,v1.0 2011-10-26 下午02:53:36 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:53:36
 * 
 */
@Alias("cmcAttribute")
public class CmcAttribute extends CmcEntity implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 4564489755396302409L;
    // public static String DEFAULT_CC_NAME_PRE = "CC8800";
    public static String[] DEFAULT_CC_NAME_PRE = new String[] { "CC8800", "CMTS_", "CCMTS_" };
    public static String DEFAULT_ONU_NAME_PRE = "ONU_";
    // private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;// 是表的ifIndex，Mac域
    private String nmName;
    private Long cmcDeviceStyle;// 1:8800a;2:8800b
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.1", type = "OctetString")
    private String topCcmtsSysDescr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.2")
    private String topCcmtsSysObjectId;
    // @EMS-10489 把类A型设备的运行时长采集从sysUptime改为topCcmtsSysRunTime获取
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.19")
    private Long topCcmtsSysRunTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3")
    private Long topCcmtsSysUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.4", type = "OctetString", writable = true)
    private String topCcmtsSysContact;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.5", type = "OctetString", writable = true)
    private String topCcmtsSysName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.6", type = "OctetString", writable = true)
    private String topCcmtsSysLocation;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.7")
    private Integer topCcmtsSysService;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.8")
    private Long topCcmtsSysORLastChange;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.9")
    private Integer topCcmtsDocsisBaseCapability;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10")
    private Integer topCcmtsSysRAMRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11")
    private Integer topCcmtsSysCPURatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12")
    private String topCcmtsSysMacAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.13")
    private Integer topCcmtsSysFlashRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.14")
    private Integer topCcmtsSysStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.15")
    private String topCcmtsSysSwVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.16")
    private String topCcmtsSysSerialNumber;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.20")
    private String topCcmtsSysDorType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.28.1.1")
    private Integer topCcmtsEqamSupport;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.1")
    private Long topCcmtsCmNumTotal;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.2")
    private Long topCcmtsCmNumOutline;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.3")
    private Long topCcmtsCmNumOnline;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.4")
    private Long topCcmtsCmNumReg;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.5")
    private Long topCcmtsCmNumRanged;
    @SnmpProperty(table = "cmNum", oid = "1.3.6.1.4.1.32285.11.1.1.2.2.1.1.6")
    private Long topCcmtsCmNumRanging;
    private Long topCcmtsSysMacAddrLong;

    private final static String[] DEVICESSTYPES = { "", "CC8800A", "CC8800B" };
    private String cmcDeviceStyleString;
    private String topCcmtsSysRAMRatiotoString;
    private String topCcmtsSysCPURatiotoString;
    private String topCcmtsSysFlashRatiotoString;
    private String topCcmtsSysUpTimeString;
    private String topCcmtsSysStatusString;
    private String manageIp;
    private String interfaceInfo;
    private String oltVersion;
    private Long typeId;
    private String typeName;
    private String manageName;
    private String contact;
    private String location;
    private String note;
    private Timestamp dt;
    // CC的状态改变时间 Added by huangdongsheng
    private Timestamp statusChangeTime = new Timestamp(System.currentTimeMillis());
    private String statusChangeTimeStr;
    private Long statusChangeTimeLong;

    private Long folderId;// CCMTS从属地域ID
    private String folderName;// 地域名称
    private Integer status;
    // 经计算后的内存利用率
    private Double memUsed;
    private Double cpuUsed;
    private Boolean attention;
    private String uplinkDevice;

    // Add by Rod 用于自动清除离线CMC
    private Timestamp lastDeregisterTime;

    private Integer mddInterval;// <0-2000> millisecond,0 means to disable MDD

    public CmcAttribute() {
    }

    public CmcAttribute(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * 比较版本
     * 
     * @param version
     *            version的格式为：V1.1.1.1，4位版本号
     * @return 当比version大时返回1，当比version 小时返回-1，相等时返回0
     */
    public Integer compareVersion(String version) {
        String str = getDolVersion().substring(1, getDolVersion().length());
        String[] myVersion = str.split("\\x2e");
        String[] other = version.substring(1, version.length()).split("\\x2e");
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (myVersion.length - 1 > i && other.length - 1 > i) {
                if (Integer.parseInt(myVersion[i]) > Integer.parseInt(other[i])) {
                    return 1;
                } else if (Integer.parseInt(myVersion[i]) < Integer.parseInt(other[i])) {
                    return -1;
                }
            } else if (myVersion.length - 1 < i && other.length - 1 >= i) {
                result = -1;
                break;
            } else if (myVersion.length - 1 >= i && other.length - 1 < i) {
                result = 1;
                break;
            }
        }
        return result;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getTopCcmtsSysDescr() {
        if (this.topCcmtsSysDescr != null) {
            topCcmtsSysDescr = CmcUtil.cutStringIn255(this.topCcmtsSysDescr);
        } else {
            topCcmtsSysDescr = "";
        }
        return topCcmtsSysDescr;
    }

    public void setTopCcmtsSysDescr(String topCcmtsSysDescr) {
        if (topCcmtsSysDescr != null && topCcmtsSysDescr.length() > 255) {
            this.topCcmtsSysDescr = topCcmtsSysDescr.substring(0, 254);
        } else {
            this.topCcmtsSysDescr = topCcmtsSysDescr;
        }
        this.topCcmtsSysDescr = topCcmtsSysDescr;
    }

    public String getTopCcmtsSysObjectId() {
        return topCcmtsSysObjectId;
    }

    @Deprecated
    public void setTopCcmtsSysObjectId(String topCcmtsSysObjectId) {
        this.topCcmtsSysObjectId = topCcmtsSysObjectId;
        // if ("1.3.6.1.4.1.32285.11.1.1.1.1".equals(topCcmtsSysObjectId)) {
        // this.setCmcDeviceStyle(CmcConstants.CMC_TYPE_8800A);
        // } else if ("1.3.6.1.4.1.32285.11.1.1.1.2".equals(topCcmtsSysObjectId)) {
        // this.setCmcDeviceStyle(CmcConstants.CMC_TYPE_8800B);
        // } else if ("1.3.6.1.4.1.32285.11.1.1.1.3.1".equals(topCcmtsSysObjectId)) {
        // this.setCmcDeviceStyle(CmcConstants.CMC_TYPE_8800C_A);
        // } else if ("1.3.6.1.4.1.32285.11.1.1.1.3.2".equals(topCcmtsSysObjectId)) {
        // this.setCmcDeviceStyle(CmcConstants.CMC_TYPE_8800C_B);
        // } else if ("1.3.6.1.4.1.32285.11.1.1.1.4".equals(topCcmtsSysObjectId)) {
        // this.setCmcDeviceStyle(CmcConstants.CMC_TYPE_8800D);
        // } else if ("1.3.6.1.4.1.32285.11.1.1.1.5".equals(topCcmtsSysObjectId)) {
        // this.setCmcDeviceStyle(CmcConstants.CMC_TYPE_8800S);
        // }
    }

    public Long getTopCcmtsSysUpTime() {
        if (topCcmtsSysRunTime != null) {
            topCcmtsSysUpTime = topCcmtsSysRunTime;
        }
        return topCcmtsSysUpTime;
    }

    public void setTopCcmtsSysUpTime(Long topCcmtsSysUpTime) {
        if (topCcmtsSysRunTime != null) {
            this.topCcmtsSysUpTime = topCcmtsSysRunTime;
            topCcmtsSysUpTimeString = CmcUtil.timeFormatToZh(topCcmtsSysUpTime / 100);
        } else {
            this.topCcmtsSysUpTime = topCcmtsSysUpTime;
            if (topCcmtsSysUpTime != null) {
                topCcmtsSysUpTimeString = CmcUtil.timeFormatToZh(topCcmtsSysUpTime / 100);
            }
        }
    }

    public String getTopCcmtsSysContact() {
        if (this.topCcmtsSysContact != null) {
            topCcmtsSysContact = CmcUtil.cutStringIn255(this.topCcmtsSysContact);
        } else {
            topCcmtsSysContact = "";
        }
        return topCcmtsSysContact;
    }

    public void setTopCcmtsSysContact(String topCcmtsSysContact) {
        this.topCcmtsSysContact = topCcmtsSysContact;
    }

    public String getTopCcmtsSysName() {
        topCcmtsSysName = CmcUtil.cutStringIn255(this.topCcmtsSysName);
        return topCcmtsSysName;
    }

    public void setTopCcmtsSysName(String topCcmtsSysName) {
        this.topCcmtsSysName = topCcmtsSysName;
    }

    public String getTopCcmtsSysLocation() {
        topCcmtsSysLocation = CmcUtil.cutStringIn255(this.topCcmtsSysLocation);
        return topCcmtsSysLocation;
    }

    public void setTopCcmtsSysLocation(String topCcmtsSysLocation) {
        this.topCcmtsSysLocation = topCcmtsSysLocation;
    }

    public Integer getTopCcmtsSysService() {
        return topCcmtsSysService;
    }

    public void setTopCcmtsSysService(Integer topCcmtsSysService) {
        this.topCcmtsSysService = topCcmtsSysService;
    }

    public Long getTopCcmtsSysORLastChange() {
        return topCcmtsSysORLastChange;
    }

    public void setTopCcmtsSysORLastChange(Long topCcmtsSysORLastChange) {
        this.topCcmtsSysORLastChange = topCcmtsSysORLastChange;
    }

    public String getTopCcmtsSysMacAddr() {
        // 只有在没有取varchar类型的mac地址，并且取了long类型的mac地址时，才采取转换策略
        if (topCcmtsSysMacAddr == null && topCcmtsSysMacAddrLong != null) {
            this.topCcmtsSysMacAddr = new MacUtils(topCcmtsSysMacAddrLong).toString(MacUtils.MAOHAO).toUpperCase();
        }
        return topCcmtsSysMacAddr;
    }

    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
        if (topCcmtsSysMacAddr != null) {
            this.topCcmtsSysMacAddrLong = new MacUtils(topCcmtsSysMacAddr).longValue();
        }
    }

    public Integer getTopCcmtsDocsisBaseCapability() {
        return topCcmtsDocsisBaseCapability;
    }

    public void setTopCcmtsDocsisBaseCapability(Integer topCcmtsDocsisBaseCapability) {
        this.topCcmtsDocsisBaseCapability = topCcmtsDocsisBaseCapability;
    }

    public Integer getTopCcmtsSysRAMRatio() {
        return topCcmtsSysRAMRatio;
    }

    public void setTopCcmtsSysRAMRatio(Integer topCcmtsSysRAMRatio) {
        this.topCcmtsSysRAMRatio = topCcmtsSysRAMRatio;
    }

    public Integer getTopCcmtsSysCPURatio() {
        return topCcmtsSysCPURatio;
    }

    public void setTopCcmtsSysCPURatio(Integer topCcmtsSysCPURatio) {
        this.topCcmtsSysCPURatio = topCcmtsSysCPURatio;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        if (cmcIndex != null) {
            this.cmcIndex = cmcIndex;
            this.interfaceInfo = CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getCmcId(cmcIndex).toString();
        }
    }

    public Long getCmcDeviceStyle() {
        return cmcDeviceStyle;
    }

    public void setCmcDeviceStyle(Long cmcDeviceStyle) {
        this.cmcDeviceStyle = cmcDeviceStyle;

    }

    public Integer getTopCcmtsSysFlashRatio() {
        return topCcmtsSysFlashRatio;
    }

    public void setTopCcmtsSysFlashRatio(Integer topCcmtsSysFlashRatio) {
        this.topCcmtsSysFlashRatio = topCcmtsSysFlashRatio;
    }

    public Long getTopCcmtsSysMacAddrLong() {
        return topCcmtsSysMacAddrLong;
    }

    public void setTopCcmtsSysMacAddrLong(Long topCcmtsSysMacAddrLong) {
        this.topCcmtsSysMacAddrLong = topCcmtsSysMacAddrLong;
    }

    public static String[] getDevicesstypes() {
        return DEVICESSTYPES;
    }

    public void setCmcDeviceStyleString(String cmcDeviceStyleString) {
        this.cmcDeviceStyleString = cmcDeviceStyleString;
    }

    // modify by loyal 改为从数据库连表查询
    public String getCmcDeviceStyleString() {
        // cmcDeviceStyleString = CmcConstants.getCmcTypeString(cmcDeviceStyle);
        return cmcDeviceStyleString;
    }

    public String getTopCcmtsSysRAMRatiotoString() {
        if (this.getTopCcmtsSysRAMRatio() != null) {
            topCcmtsSysRAMRatiotoString = this.getTopCcmtsSysRAMRatio() + Symbol.PERCENT;
        }
        return topCcmtsSysRAMRatiotoString;
    }

    public void setTopCcmtsSysRAMRatiotoString(String topCcmtsSysRAMRatiotoString) {
        this.topCcmtsSysRAMRatiotoString = topCcmtsSysRAMRatiotoString;
    }

    public String getTopCcmtsSysCPURatiotoString() {
        if (this.getTopCcmtsSysCPURatio() != null) {
            topCcmtsSysCPURatiotoString = this.getTopCcmtsSysCPURatio() + Symbol.PERCENT;
        }
        return topCcmtsSysCPURatiotoString;
    }

    public void setTopCcmtsSysCPURatiotoString(String topCcmtsSysCPURatiotoString) {
        this.topCcmtsSysCPURatiotoString = topCcmtsSysCPURatiotoString;
    }

    public String getTopCcmtsSysFlashRatiotoString() {
        if (this.getTopCcmtsSysFlashRatio() != null) {
            topCcmtsSysFlashRatiotoString = this.getTopCcmtsSysFlashRatio() + Symbol.PERCENT;
        }
        return topCcmtsSysFlashRatiotoString;
    }

    public void setTopCcmtsSysFlashRatiotoString(String topCcmtsSysFlashRatiotoString) {
        this.topCcmtsSysFlashRatiotoString = topCcmtsSysFlashRatiotoString;
    }

    public String getTopCcmtsSysUpTimeString() {
        return topCcmtsSysUpTimeString;
    }

    public void setTopCcmtsSysUpTimeString(String topCcmtsSysUpTimeString) {
        this.topCcmtsSysUpTimeString = topCcmtsSysUpTimeString;
    }

    public Integer getTopCcmtsSysStatus() {
        return topCcmtsSysStatus;
    }

    public void setTopCcmtsSysStatus(Integer topCcmtsSysStatus) {
        this.topCcmtsSysStatus = topCcmtsSysStatus;
    }

    public String getTopCcmtsSysSwVersion() {
        topCcmtsSysSwVersion = CmcUtil.cutStringIn255(this.topCcmtsSysSwVersion);
        return topCcmtsSysSwVersion;
    }

    public void setTopCcmtsSysSwVersion(String topCcmtsSysSwVersion) {
        this.topCcmtsSysSwVersion = topCcmtsSysSwVersion;
    }

    public String getTopCcmtsSysStatusString() {
        if (topCcmtsSysStatus != null && topCcmtsSysStatus.intValue() > 0
                && topCcmtsSysStatus.intValue() < CmcConstants.TOPCCMTSSYSSTATUS_NUM) {
            if (topCcmtsSysStatus.intValue() == 0) {
                topCcmtsSysStatusString = "";
            } else {
                topCcmtsSysStatusString = ResourcesUtil.getString("CCMTS.status." + topCcmtsSysStatus.intValue());
            }
        }
        return topCcmtsSysStatusString;
    }

    public void setTopCcmtsSysStatusString(String topCcmtsSysStatusString) {
        this.topCcmtsSysStatusString = topCcmtsSysStatusString;
    }

    public Long getTopCcmtsCmNumTotal() {
        return topCcmtsCmNumTotal;
    }

    public void setTopCcmtsCmNumTotal(Long topCcmtsCmNumTotal) {
        this.topCcmtsCmNumTotal = topCcmtsCmNumTotal;
    }

    public Long getTopCcmtsCmNumOutline() {
        return topCcmtsCmNumOutline;
    }

    public void setTopCcmtsCmNumOutline(Long topCcmtsCmNumOutline) {
        this.topCcmtsCmNumOutline = topCcmtsCmNumOutline;
    }

    public Long getTopCcmtsCmNumOnline() {
        return topCcmtsCmNumOnline;
    }

    public void setTopCcmtsCmNumOnline(Long topCcmtsCmNumOnline) {
        this.topCcmtsCmNumOnline = topCcmtsCmNumOnline;
    }

    public Long getTopCcmtsCmNumReg() {
        return topCcmtsCmNumReg;
    }

    public void setTopCcmtsCmNumReg(Long topCcmtsCmNumReg) {
        this.topCcmtsCmNumReg = topCcmtsCmNumReg;
    }

    public Long getTopCcmtsCmNumRanged() {
        return topCcmtsCmNumRanged;
    }

    public void setTopCcmtsCmNumRanged(Long topCcmtsCmNumRanged) {
        this.topCcmtsCmNumRanged = topCcmtsCmNumRanged;
    }

    public Long getTopCcmtsCmNumRanging() {
        return topCcmtsCmNumRanging;
    }

    public void setTopCcmtsCmNumRanging(Long topCcmtsCmNumRanging) {
        this.topCcmtsCmNumRanging = topCcmtsCmNumRanging;
    }

    public String getTopCcmtsSysSerialNumber() {
        return topCcmtsSysSerialNumber;
    }

    public void setTopCcmtsSysSerialNumber(String topCcmtsSysSerialNumber) {
        this.topCcmtsSysSerialNumber = topCcmtsSysSerialNumber;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public Timestamp getDt() {
        return dt;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public String getInterfaceInfo() {
        return interfaceInfo;
    }

    public void setInterfaceInfo(String interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }

    public String getNmName() {
        return nmName;
    }

    public void setNmName(String nmName) {
        this.nmName = nmName;
    }

    public String getOltVersion() {
        return oltVersion;
    }

    public void setOltVersion(String oltVersion) {
        this.oltVersion = oltVersion;
    }

    public String getDolVersion() {
        if (this.topCcmtsSysSwVersion != null) {
            // A C-A 420B5-V1.3.10.0 BCM3218 2.44, 0.1.13, 0.1.1, 0.2.1
            // B V1.2.13.5-P3-S;420B5-V1.3.10.0
            if (this.topCcmtsSysSwVersion.indexOf(";") != -1) {
                String[] version = this.topCcmtsSysSwVersion.split(";");
                return version[0];
            } else if (this.topCcmtsSysSwVersion.indexOf(" ") != -1) {
                String part = this.topCcmtsSysSwVersion.split(" ")[0];
                if (part.indexOf("-V") != -1) {
                    return part.substring(part.indexOf("-V") + 1);
                } else if (part.startsWith("V")) {
                    return part;
                }
            } else {
                return this.topCcmtsSysSwVersion;
            }
        }
        return null;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public Timestamp getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(Timestamp statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public void setStatusChangeTimeStr(String statusChangeTimeStr) {
        this.statusChangeTimeStr = statusChangeTimeStr;
    }

    public String getStatusChangeTimeStr() {
        return this.statusChangeTimeStr;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getStatusChangeTimeLong() {
        return statusChangeTime != null ? statusChangeTime.getTime() : System.currentTimeMillis();
    }

    public void setStatusChangeTimeLong(Long statusChangeTimeLong) {
        this.statusChangeTimeLong = statusChangeTimeLong;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Double memUsed) {
        this.memUsed = memUsed;
    }

    public Double getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Double cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    /**
     * @return the topCcmtsSysRunTime
     */
    public Long getTopCcmtsSysRunTime() {
        return topCcmtsSysRunTime;
    }

    /**
     * @param topCcmtsSysRunTime
     *            the topCcmtsSysRunTime to set
     */
    public void setTopCcmtsSysRunTime(Long topCcmtsSysRunTime) {
        this.topCcmtsSysRunTime = topCcmtsSysRunTime;
    }

    /**
     * @return the attention
     */
    public Boolean getAttention() {
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

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public Integer getTopCcmtsEqamSupport() {
        return topCcmtsEqamSupport;
    }

    public void setTopCcmtsEqamSupport(Integer topCcmtsEqamSupport) {
        this.topCcmtsEqamSupport = topCcmtsEqamSupport;
    }

    /**
     * @return the topCcmtsSysDorType
     */
    public String getTopCcmtsSysDorType() {
        return topCcmtsSysDorType;
    }

    /**
     * @param topCcmtsSysDorType
     *            the topCcmtsSysDorType to set
     */
    public void setTopCcmtsSysDorType(String topCcmtsSysDorType) {
        this.topCcmtsSysDorType = topCcmtsSysDorType;
    }

    /**
     * @return the mddInterval
     */
    public Integer getMddInterval() {
        return mddInterval;
    }

    /**
     * @param mddInterval
     *            the mddInterval to set
     */
    public void setMddInterval(Integer mddInterval) {
        this.mddInterval = mddInterval;
    }

    /**
     * @param attention
     *            the attention to set
     */
    public void setAttention(Boolean attention) {
        this.attention = attention;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcAttribute [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", nmName=");
        builder.append(nmName);
        builder.append(", cmcDeviceStyle=");
        builder.append(cmcDeviceStyle);
        builder.append(", topCcmtsSysDescr=");
        builder.append(topCcmtsSysDescr);
        builder.append(", topCcmtsSysObjectId=");
        builder.append(topCcmtsSysObjectId);
        builder.append(", topCcmtsSysRunTime=");
        builder.append(topCcmtsSysRunTime);
        builder.append(", topCcmtsSysUpTime=");
        builder.append(topCcmtsSysUpTime);
        builder.append(", topCcmtsSysContact=");
        builder.append(topCcmtsSysContact);
        builder.append(", topCcmtsSysName=");
        builder.append(topCcmtsSysName);
        builder.append(", topCcmtsSysLocation=");
        builder.append(topCcmtsSysLocation);
        builder.append(", topCcmtsSysService=");
        builder.append(topCcmtsSysService);
        builder.append(", topCcmtsSysORLastChange=");
        builder.append(topCcmtsSysORLastChange);
        builder.append(", topCcmtsDocsisBaseCapability=");
        builder.append(topCcmtsDocsisBaseCapability);
        builder.append(", topCcmtsSysRAMRatio=");
        builder.append(topCcmtsSysRAMRatio);
        builder.append(", topCcmtsSysCPURatio=");
        builder.append(topCcmtsSysCPURatio);
        builder.append(", topCcmtsSysMacAddr=");
        builder.append(topCcmtsSysMacAddr);
        builder.append(", topCcmtsSysFlashRatio=");
        builder.append(topCcmtsSysFlashRatio);
        builder.append(", topCcmtsSysStatus=");
        builder.append(topCcmtsSysStatus);
        builder.append(", topCcmtsSysSwVersion=");
        builder.append(topCcmtsSysSwVersion);
        builder.append(", topCcmtsSysSerialNumber=");
        builder.append(topCcmtsSysSerialNumber);
        builder.append(", topCcmtsSysDorType=");
        builder.append(topCcmtsSysDorType);
        builder.append(", topCcmtsCmNumTotal=");
        builder.append(topCcmtsCmNumTotal);
        builder.append(", topCcmtsCmNumOutline=");
        builder.append(topCcmtsCmNumOutline);
        builder.append(", topCcmtsCmNumOnline=");
        builder.append(topCcmtsCmNumOnline);
        builder.append(", topCcmtsCmNumReg=");
        builder.append(topCcmtsCmNumReg);
        builder.append(", topCcmtsCmNumRanged=");
        builder.append(topCcmtsCmNumRanged);
        builder.append(", topCcmtsCmNumRanging=");
        builder.append(topCcmtsCmNumRanging);
        builder.append(", topCcmtsSysMacAddrLong=");
        builder.append(topCcmtsSysMacAddrLong);
        builder.append(", cmcDeviceStyleString=");
        builder.append(cmcDeviceStyleString);
        builder.append(", topCcmtsSysRAMRatiotoString=");
        builder.append(topCcmtsSysRAMRatiotoString);
        builder.append(", topCcmtsSysCPURatiotoString=");
        builder.append(topCcmtsSysCPURatiotoString);
        builder.append(", topCcmtsSysFlashRatiotoString=");
        builder.append(topCcmtsSysFlashRatiotoString);
        builder.append(", topCcmtsSysUpTimeString=");
        builder.append(topCcmtsSysUpTimeString);
        builder.append(", topCcmtsSysStatusString=");
        builder.append(topCcmtsSysStatusString);
        builder.append(", manageIp=");
        builder.append(manageIp);
        builder.append(", interfaceInfo=");
        builder.append(interfaceInfo);
        builder.append(", oltVersion=");
        builder.append(oltVersion);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", manageName=");
        builder.append(manageName);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", statusChangeTime=");
        builder.append(statusChangeTime);
        builder.append(", statusChangeTimeStr=");
        builder.append(statusChangeTimeStr);
        builder.append(", statusChangeTimeLong=");
        builder.append(statusChangeTimeLong);
        builder.append(", folderId=");
        builder.append(folderId);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append(", status=");
        builder.append(status);
        builder.append(", memUsed=");
        builder.append(memUsed);
        builder.append(", cpuUsed=");
        builder.append(cpuUsed);
        builder.append(", attention=");
        builder.append(attention);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append(", mddInterval=");
        builder.append(mddInterval);
        builder.append("]");
        return builder.toString();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}