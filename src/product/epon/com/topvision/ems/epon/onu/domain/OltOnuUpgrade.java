/***********************************************************************
 * $Id: OltOnuUpgrade.java,v1.0 2011-11-24 下午04:27:54 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * ONU升级
 * 
 * @author zhanglongyang
 * @created @2011-11-24-下午04:27:54
 * 
 */
public class OltOnuUpgrade implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -9022853170236286135L;
    private Long entityId;
    private Long onuUpgradeId;
    private Long slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.1", index = true)
    private Integer topOnuUpgradeTransactionIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.2", writable = true, type = "Integer32")
    private Integer topOnuUpgradeSlotNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.3", writable = true, type = "Integer32")
    private Integer topOnuUpgradeOnuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.4", writable = true, type = "OctetString")
    private String topOnuUpgradeHwVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.5", writable = true, type = "OctetString")
    private String topOnuUpgradeFileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.6", writable = true, type = "Integer32")
    private Integer topOnuUpgradeFileType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.7", writable = true, type = "Integer32")
    private Integer topOnuUpgradeMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.8", writable = true, type = "OctetString")
    private String topOnuUpgradeOnuList;
    private List<Integer> topOnuUpgradeOnuListList;
    private List<String> upgradeOnuList;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.9", writable = true, type = "Integer32")
    private Integer topOnuUpgradeOperAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.10", type = "OctetString")
    private String topOnuUpgradeStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.11", writable = true, type = "Integer32")
    private Integer topOnuUpgradeRowStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.12", writable = true, type = "Integer32")
    private Integer topOnuUpgradeOption;
    private Timestamp upgradeTime;
    private String upgradeTimeString;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuUpgradeId() {
        return onuUpgradeId;
    }

    public void setOnuUpgradeId(Long onuUpgradeId) {
        this.onuUpgradeId = onuUpgradeId;
    }

    public Long getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getTopOnuUpgradeTransactionIndex() {
        return topOnuUpgradeTransactionIndex;
    }

    public void setTopOnuUpgradeTransactionIndex(Integer topOnuUpgradeTransactionIndex) {
        this.topOnuUpgradeTransactionIndex = topOnuUpgradeTransactionIndex;
    }

    public Integer getTopOnuUpgradeSlotNum() {
        return topOnuUpgradeSlotNum;
    }

    public void setTopOnuUpgradeSlotNum(Integer topOnuUpgradeSlotNum) {
        this.topOnuUpgradeSlotNum = topOnuUpgradeSlotNum;
    }

    public Integer getTopOnuUpgradeOnuType() {
        return topOnuUpgradeOnuType;
    }

    public void setTopOnuUpgradeOnuType(Integer topOnuUpgradeOnuType) {
        this.topOnuUpgradeOnuType = topOnuUpgradeOnuType;
    }

    public String getTopOnuUpgradeFileName() {
        return topOnuUpgradeFileName;
    }

    public void setTopOnuUpgradeFileName(String topOnuUpgradeFileName) {
        this.topOnuUpgradeFileName = topOnuUpgradeFileName;
    }

    public Integer getTopOnuUpgradeFileType() {
        return topOnuUpgradeFileType;
    }

    public void setTopOnuUpgradeFileType(Integer topOnuUpgradeFileType) {
        this.topOnuUpgradeFileType = topOnuUpgradeFileType;
    }

    public Integer getTopOnuUpgradeMode() {
        return topOnuUpgradeMode;
    }

    public void setTopOnuUpgradeMode(Integer topOnuUpgradeMode) {
        this.topOnuUpgradeMode = topOnuUpgradeMode;
    }

    public String getTopOnuUpgradeOnuList() {
        return topOnuUpgradeOnuList;
    }

    public void setTopOnuUpgradeOnuList(String topOnuUpgradeOnuList) {
        this.topOnuUpgradeOnuList = topOnuUpgradeOnuList;
        topOnuUpgradeOnuListList = EponUtil.getOnuListFromBitMap(topOnuUpgradeOnuList);
        setOnuUpgradeStatusDesc();
    }

    /**
     * 设置ONU升级状态说明
     */
    private void setOnuUpgradeStatusDesc() {
        if (topOnuUpgradeOnuListList != null && !topOnuUpgradeOnuListList.isEmpty()) {
            upgradeOnuList = new ArrayList<String>();
            for (Integer onuSeq : topOnuUpgradeOnuListList) {
                StringBuilder sb = new StringBuilder();
                if (topOnuUpgradeStatus != null && topOnuUpgradeStatus.length() > onuSeq
                        && topOnuUpgradeStatus.substring(onuSeq, onuSeq + 1) != null) {
                    Integer status = Integer
                            .parseInt(topOnuUpgradeStatus.substring(onuSeq * 3 - 2, onuSeq * 3 - 1), 16);
                    if (!status.equals(EponConstants.ONU_UPGRADE_STATUS_SUCCESS)) {
                        sb.append("<font color='red'>");
                    } else {
                        sb.append("<font color='green'>");
                    }
                } else {
                    sb.append("<font color='green'>");
                }
                sb.append("[");
                sb.append(topOnuUpgradeSlotNum);
                sb.append("/");
                sb.append(onuSeq / 128 + 1);
                sb.append(":");
                sb.append(onuSeq % 128);
                sb.append("]");
                sb.append("</font>");
                upgradeOnuList.add(sb.toString());
            }
        }
    }

    public List<Integer> getTopOnuUpgradeOnuListList() {
        return topOnuUpgradeOnuListList;
    }

    public void setTopOnuUpgradeOnuListList(List<Integer> topOnuUpgradeOnuListList) {
        this.topOnuUpgradeOnuListList = topOnuUpgradeOnuListList;
        setOnuUpgradeStatusDesc();
        topOnuUpgradeOnuList = EponUtil.getOnuListBitMapFromList(topOnuUpgradeOnuListList);
    }

    public List<String> getUpgradeOnuList() {
        return upgradeOnuList;
    }

    public void setUpgradeOnuList(List<String> upgradeOnuList) {
        this.upgradeOnuList = upgradeOnuList;
    }

    public Integer getTopOnuUpgradeOperAction() {
        return topOnuUpgradeOperAction;
    }

    public void setTopOnuUpgradeOperAction(Integer topOnuUpgradeOperAction) {
        this.topOnuUpgradeOperAction = topOnuUpgradeOperAction;
    }

    public String getTopOnuUpgradeStatus() {
        return topOnuUpgradeStatus;
    }

    public void setTopOnuUpgradeStatus(String topOnuUpgradeStatus) {
        this.topOnuUpgradeStatus = topOnuUpgradeStatus;
        setOnuUpgradeStatusDesc();
    }

    public Integer getTopOnuUpgradeRowStatus() {
        return topOnuUpgradeRowStatus;
    }

    public void setTopOnuUpgradeRowStatus(Integer topOnuUpgradeRowStatus) {
        this.topOnuUpgradeRowStatus = topOnuUpgradeRowStatus;
    }

    public String getTopOnuUpgradeHwVersion() {
        return topOnuUpgradeHwVersion;
    }

    public void setTopOnuUpgradeHwVersion(String topOnuUpgradeHwVersion) {
        this.topOnuUpgradeHwVersion = topOnuUpgradeHwVersion;
    }

    public Timestamp getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Timestamp upgradeTime) {
        upgradeTimeString = DateUtils.format(upgradeTime.getTime());
        this.upgradeTime = upgradeTime;
    }

    public String getUpgradeTimeString() {
        return upgradeTimeString;
    }

    public void setUpgradeTimeString(String upgradeTimeString) {
        this.upgradeTimeString = upgradeTimeString;
    }

    public Integer getTopOnuUpgradeOption() {
        return topOnuUpgradeOption;
    }

    public void setTopOnuUpgradeOption(Integer topOnuUpgradeOption) {
        this.topOnuUpgradeOption = topOnuUpgradeOption;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuUpgrade [entityId=");
        builder.append(entityId);
        builder.append(", onuUpgradeId=");
        builder.append(onuUpgradeId);
        builder.append(", slotIndex=");
        builder.append(slotIndex);
        builder.append(", topOnuUpgradeTransactionIndex=");
        builder.append(topOnuUpgradeTransactionIndex);
        builder.append(", topOnuUpgradeSlotNum=");
        builder.append(topOnuUpgradeSlotNum);
        builder.append(", topOnuUpgradeOnuType=");
        builder.append(topOnuUpgradeOnuType);
        builder.append(", topOnuUpgradeHwVersion=");
        builder.append(topOnuUpgradeHwVersion);
        builder.append(", topOnuUpgradeFileName=");
        builder.append(topOnuUpgradeFileName);
        builder.append(", topOnuUpgradeFileType=");
        builder.append(topOnuUpgradeFileType);
        builder.append(", topOnuUpgradeMode=");
        builder.append(topOnuUpgradeMode);
        builder.append(", topOnuUpgradeOnuList=");
        builder.append(topOnuUpgradeOnuList);
        builder.append(", topOnuUpgradeOnuListList=");
        builder.append(topOnuUpgradeOnuListList);
        builder.append(", upgradeOnuList=");
        builder.append(upgradeOnuList);
        builder.append(", topOnuUpgradeOperAction=");
        builder.append(topOnuUpgradeOperAction);
        builder.append(", topOnuUpgradeStatus=");
        builder.append(topOnuUpgradeStatus);
        builder.append(", topOnuUpgradeOption=");
        builder.append(topOnuUpgradeOption);
        builder.append(", topOnuUpgradeRowStatus=");
        builder.append(topOnuUpgradeRowStatus);
        builder.append(", upgradeTime=");
        builder.append(upgradeTime);
        builder.append("]");
        return builder.toString();
    }
}
