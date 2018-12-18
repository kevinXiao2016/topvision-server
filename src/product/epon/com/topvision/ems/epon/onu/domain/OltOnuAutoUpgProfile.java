/***********************************************************************
 * $ OltOnuAutoUpgProfile.java,v1.0 2012-9-19 16:30:28 $
 *
 * @author: yq
 *
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * @author yq
 * @created @2012-9-19-16:30:28
 */
public class OltOnuAutoUpgProfile implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -3468916700356088206L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.1", index = true)
    private Integer profileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.2", writable = true, type = "OctetString")
    private String proName;
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.3", writable =
    // true, type = "Integer32")
    private String proOnuTypeStr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.3", writable = true, type = "Integer32")
    private Integer proOnuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.4", writable = true, type = "OctetString")
    private String proHwVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.5", writable = true, type = "Integer32")
    private Integer proMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.6", writable = true, type = "OctetString")
    private String proNewVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.7", writable = true, type = "OctetString")
    private String proUpgTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.8", writable = true, type = "OctetString")
    private String boot;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.9", writable = true, type = "OctetString")
    private String webs;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.10", writable = true, type = "OctetString")
    private String other;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.11", writable = true, type = "OctetString")
    private String app;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.12", writable = true, type = "OctetString")
    private String pers;
    // 暂时不支持的字段 @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.13",
    // writable = true, type = "Integer32")
    private Integer proPri;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.14", type = "Integer32")
    private Integer proBandStat;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.1.1.15", writable = true, type = "Integer32")
    private Integer topOnuAutoUpgProfileRowStatus;

    //将从设备获取的格式转换化时间字符串格式
    private String upgradeTimeStr;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getProOnuType() {
        return proOnuType;
    }

    public void setProOnuType(Integer proOnuType) {
        this.proOnuType = proOnuType;
        if (proOnuType > 0 && EponConstants.EPON_ONU_PRETYPE.containsValue(proOnuType)) {
            this.proOnuTypeStr = "86" + (proOnuType / 16) + (proOnuType % 16);
        } else if (proOnuType.equals(241)) {
            this.proOnuTypeStr = "8800";
        } else {
            this.proOnuTypeStr = "none";
        }
    }

    public String getProOnuTypeStr() {
        return proOnuTypeStr;
    }

    public void setProOnuTypeStr(String proOnuTypeStr) {
        this.proOnuTypeStr = proOnuTypeStr;
        if (!proOnuTypeStr.equalsIgnoreCase("none") && EponConstants.EPON_ONU_PRETYPE.containsKey(proOnuTypeStr)) {
            this.proOnuType = EponConstants.EPON_ONU_PRETYPE.get(proOnuTypeStr);
        } else if (proOnuTypeStr.equalsIgnoreCase("CC8800A") || proOnuTypeStr.equalsIgnoreCase("8800")) {
            this.proOnuType = 241;
        } else {
            this.proOnuType = 0;
        }
    }

    public String getProHwVersion() {
        return proHwVersion;
    }

    public void setProHwVersion(String proHwVersion) {
        this.proHwVersion = proHwVersion;
    }

    public Integer getProMode() {
        return proMode;
    }

    public void setProMode(Integer proMode) {
        this.proMode = proMode;
    }

    public String getProNewVersion() {
        return proNewVersion;
    }

    public void setProNewVersion(String proNewVersion) {
        this.proNewVersion = proNewVersion;
    }

    public String getProUpgTime() {
        return proUpgTime;
    }

    public void setProUpgTime(String proUpgTime) {
        this.proUpgTime = proUpgTime;
    }

    public String getBoot() {
        return boot;
    }

    public void setBoot(String boot) {
        this.boot = boot;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getWebs() {
        return webs;
    }

    public void setWebs(String webs) {
        this.webs = webs;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPers() {
        return pers;
    }

    public void setPers(String pers) {
        this.pers = pers;
    }

    public Integer getProPri() {
        return proPri;
    }

    public void setProPri(Integer proPri) {
        this.proPri = proPri;
    }

    public Integer getProBandStat() {
        return proBandStat;
    }

    public void setProBandStat(Integer proBandStat) {
        this.proBandStat = proBandStat;
    }

    public Integer getTopOnuAutoUpgProfileRowStatus() {
        return topOnuAutoUpgProfileRowStatus;
    }

    public void setTopOnuAutoUpgProfileRowStatus(Integer topOnuAutoUpgProfileRowStatus) {
        this.topOnuAutoUpgProfileRowStatus = topOnuAutoUpgProfileRowStatus;
    }

    public String getUpgradeTimeStr() {
        if (proUpgTime != null && upgradeTimeStr == null) {
            Long upgtime = Long.parseLong(proUpgTime);
            if (upgtime < 0) {
                return null;
            } else {
                upgradeTimeStr = DateUtils.format(upgtime, DateUtils.MINUTE_FORMAT);
            }
        }
        return upgradeTimeStr;
    }

    public void setUpgradeTimeStr(String upgradeTimeStr) {
        this.upgradeTimeStr = upgradeTimeStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuAutoUpgProfile");
        sb.append("{entityId=").append(entityId);
        sb.append(", profileId=").append(profileId);
        sb.append('}');
        return sb.toString();
    }
}
