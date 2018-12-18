/***********************************************************************
 * $ OltOnuAutoUpgRecord.java,v1.0 2012-9-19 16:30:28 $
 *
 * @author: yq
 *
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author yq
 * @created @2012-9-19-16:30:28
 */
public class OltOnuAutoUpgRecord implements Serializable {
    private static final long serialVersionUID = 1998823226519675765L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.3.1.1", index = true)
    private Integer slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.3.1.2", index = true)
    private Integer recordId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.3.1.3", type = "Integer32")
    private Integer profileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.3.1.4", type = "OctetString")
    private String upgRecordStartTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.3.1.5", writable = true, type = "Integer32")
    private Integer upgRecordAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.3.1.6", type = "OctetString")
    private String upgRecordOnuStat;
    private List<String> upgRecordOnuList = new ArrayList<String>();
    private List<Integer> upgRecordOnuStatList = new ArrayList<Integer>();

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getUpgRecordStartTime() {
        return upgRecordStartTime;
    }

    public void setUpgRecordStartTime(String upgRecordStartTime) {
        this.upgRecordStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long
                .parseLong(upgRecordStartTime)));
    }

    public Integer getUpgRecordAction() {
        return upgRecordAction;
    }

    public void setUpgRecordAction(Integer upgRecordAction) {
        this.upgRecordAction = upgRecordAction;
    }

    public String getUpgRecordOnuStat() {
        return upgRecordOnuStat;
    }

    public void setUpgRecordOnuStat(String upgRecordOnuStat) {
        this.upgRecordOnuStat = upgRecordOnuStat;
        if (upgRecordOnuStat != null) {
            this.upgRecordOnuList = EponUtil.getAutoUpgOnuListFromMib(upgRecordOnuStat);
            this.upgRecordOnuStatList = EponUtil.getAutoUpgOnuStatListFromMib(upgRecordOnuStat);
        }
    }

    public List<String> getUpgRecordOnuList() {
        return upgRecordOnuList;
    }

    public void setUpgRecordOnuList(List<String> upgRecordOnuList) {
        this.upgRecordOnuList = upgRecordOnuList;
    }

    public List<Integer> getUpgRecordOnuStatList() {
        return upgRecordOnuStatList;
    }

    public void setUpgRecordOnuStatList(List<Integer> upgRecordOnuStatList) {
        this.upgRecordOnuStatList = upgRecordOnuStatList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuAutoUpgRecord");
        sb.append("{entityId=").append(entityId);
        sb.append(", profileId=").append(profileId);
        sb.append('}');
        return sb.toString();
    }
}
