/***********************************************************************
 * $ IgmpControlledMcCdrTable.java,v1.0 2011-11-23 11:01:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-11:01:13
 */
@TableProperty(tables = { "default" })
public class IgmpControlledMcCdrTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * The sequence number of this CDR, used as the index into eponOltCdrTable.
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.1", index = true)
    private Long sequenceIndex;
    private Long cdrUniIndex;

    /**
     * Decide which UNI this CDR was created for, in conjunction with topControllededMcCdrPonNum,
     * topControllededMcCdrOnuAuthNum and topControllededMcCdrUniNum. INTEGER (1..18)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.2")
    private Integer topControlledMcCdrSlotNum;
    /**
     * Decide which UNI this CDR was created for, in conjunction with topControllededMcCdrSlotNum,
     * topControllededMcCdrOnuAuthNum and topControllededMcCdrUniNum. INTEGER (1..8)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.3")
    private Integer topControlledMcCdrPonNum;
    /**
     * Decide which UNI this CDR was created for, in conjunction with topControllededMcCdrSlotNum,
     * topControllededMcCdrPonNum and topControllededMcCdrUniNum. INTEGER (1..32)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.4")
    private Integer topControlledMcCdrOnuAuthNum;
    /**
     * Decide which UNI this CDR was created for, in conjunction with topControllededMcCdrSlotNum,
     * topControllededMcCdrPonNum and topControllededMcCdrOnuAuthNum. INTEGER (1..32)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.5")
    private Integer topControlledMcCdrUniNum;
    /**
     * Type of the IGMP request message sent by the corresponding UNI. INTEGER { join(0), leave(1) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.7")
    private Long cdrIgmpReqTime;
    private String cdrIgmpReqTimeString;
    /**
     * This object indicates when the Igmp request message was sent. INTEGER (0..4294967295)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.6")
    private Integer cdrIgmpReqType;
    private String cdrIgmpReqTypeString;
    /**
     * This object indicates which channel the Igmp request message was sent for. OCTET STRING (SIZE
     * (4))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.8")
    private String cdrIgmpReqChannel;
    /**
     * This object indicates what access that the UNI has, to the channel the Igmp request message
     * was sent for. INTEGER { deny(0), permit(1), preview(2) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.9")
    private Integer cdrIgmpReqRight;
    private String cdrIgmpReqRightString;
    /**
     * Rusult of the request, success or failure. INTEGER { success(0), failure(1) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.10")
    private Integer cdrIgmpReqResult;
    private String cdrIgmpReqResultString;
    /**
     * The cause of UNI's leave, self-request or being kicked. INTEGER { selfLeaving(0),
     * forceLeaving(1) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.11")
    private Integer cdrLeaveType;
    private String cdrLeaveTypeString;
    /**
     * This object indicates when the CDR was created. INTEGER (0..4294967295)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.1.1.12")
    private Long cdrRecordTime;
    private String cdrRecordTimeString;

    public Long getCdrUniIndex() {
        cdrUniIndex = EponIndex.getUniIndex(topControlledMcCdrSlotNum, topControlledMcCdrPonNum,
                topControlledMcCdrOnuAuthNum, 1, topControlledMcCdrUniNum);
        return cdrUniIndex;
    }

    public void setCdrUniIndex(Long cdrUniIndex) {
        this.cdrUniIndex = cdrUniIndex;
        topControlledMcCdrSlotNum = EponIndex.getSlotNo(cdrUniIndex).intValue();
        topControlledMcCdrPonNum = EponIndex.getPonNo(cdrUniIndex).intValue();
        topControlledMcCdrOnuAuthNum = EponIndex.getOnuNo(cdrUniIndex).intValue();
        topControlledMcCdrUniNum = EponIndex.getUniNo(cdrUniIndex).intValue();
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCdrIgmpReqChannel() {
        return cdrIgmpReqChannel;
    }

    public void setCdrIgmpReqChannel(String cdrIgmpReqChannel) {
        this.cdrIgmpReqChannel = cdrIgmpReqChannel;
    }

    public Integer getCdrIgmpReqResult() {
        return cdrIgmpReqResult;
    }

    public void setCdrIgmpReqResult(Integer cdrIgmpReqResult) {
        this.cdrIgmpReqResult = cdrIgmpReqResult;
        if (cdrIgmpReqResult == 0) {
            cdrIgmpReqResultString = "success";
        } else if (cdrIgmpReqResult == 1) {
            cdrIgmpReqResultString = "failure";
        } else {
            cdrIgmpReqResultString = "unknown";
        }
    }

    public Integer getCdrIgmpReqRight() {
        return cdrIgmpReqRight;
    }

    public void setCdrIgmpReqRight(Integer cdrIgmpReqRight) {
        this.cdrIgmpReqRight = cdrIgmpReqRight;
        if (cdrIgmpReqRight == 0) {
            cdrIgmpReqRightString = "deny";
        } else if (cdrIgmpReqRight == 1) {
            cdrIgmpReqRightString = "permit";
        } else if (cdrIgmpReqRight == 2) {
            cdrIgmpReqRightString = "preview";
        } else {
            cdrIgmpReqRightString = "unknown";
        }
    }

    public Long getCdrIgmpReqTime() {
        return cdrIgmpReqTime;
    }

    public void setCdrIgmpReqTime(Long cdrIgmpReqTime) {
        this.cdrIgmpReqTime = cdrIgmpReqTime;
        Date date = new Date(cdrIgmpReqTime * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(date);
        cdrIgmpReqTimeString = time;
    }

    public Integer getCdrIgmpReqType() {
        return cdrIgmpReqType;
    }

    public void setCdrIgmpReqType(Integer cdrIgmpReqType) {
        this.cdrIgmpReqType = cdrIgmpReqType;
        if (cdrIgmpReqType == 0) {
            cdrIgmpReqTypeString = "join";
        } else if (cdrIgmpReqType == 1) {
            cdrIgmpReqTypeString = "leave";
        } else {
            cdrIgmpReqTypeString = "unknown";
        }
    }

    public Integer getCdrLeaveType() {
        return cdrLeaveType;
    }

    public void setCdrLeaveType(Integer cdrLeaveType) {
        this.cdrLeaveType = cdrLeaveType;
        if (cdrLeaveType == 0) {
            cdrLeaveTypeString = "selfLeaving";
        } else if (cdrLeaveType == 1) {
            cdrLeaveTypeString = "forceLeaving";
        } else {
            cdrLeaveTypeString = "unknown";
        }
    }

    public Integer getTopControlledMcCdrOnuAuthNum() {
        return topControlledMcCdrOnuAuthNum;
    }

    public void setTopControlledMcCdrOnuAuthNum(Integer topControlledMcCdrOnuAuthNum) {
        this.topControlledMcCdrOnuAuthNum = topControlledMcCdrOnuAuthNum;
    }

    public Integer getTopControlledMcCdrPonNum() {
        return topControlledMcCdrPonNum;
    }

    public void setTopControlledMcCdrPonNum(Integer topControlledMcCdrPonNum) {
        this.topControlledMcCdrPonNum = topControlledMcCdrPonNum;
    }

    public Long getCdrRecordTime() {
        return cdrRecordTime;
    }

    public void setCdrRecordTime(Long cdrRecordTime) {
        this.cdrRecordTime = cdrRecordTime;
        Date date = new Date(cdrRecordTime * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(date);
        cdrRecordTimeString = time;
    }

    public Integer getTopControlledMcCdrSlotNum() {
        return topControlledMcCdrSlotNum;
    }

    public void setTopControlledMcCdrSlotNum(Integer topControlledMcCdrSlotNum) {
        this.topControlledMcCdrSlotNum = topControlledMcCdrSlotNum;
    }

    public Integer getTopControlledMcCdrUniNum() {
        return topControlledMcCdrUniNum;
    }

    public void setTopControlledMcCdrUniNum(Integer topControlledMcCdrUniNum) {
        this.topControlledMcCdrUniNum = topControlledMcCdrUniNum;
    }

    public Long getSequenceIndex() {
        return sequenceIndex;
    }

    public void setSequenceIndex(Long sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }

    public String getCdrIgmpReqTimeString() {
        return cdrIgmpReqTimeString;
    }

    public void setCdrIgmpReqTimeString(String cdrIgmpReqTimeString) {
        this.cdrIgmpReqTimeString = cdrIgmpReqTimeString;
    }

    public String getCdrIgmpReqTypeString() {
        return cdrIgmpReqTypeString;
    }

    public void setCdrIgmpReqTypeString(String cdrIgmpReqTypeString) {
        this.cdrIgmpReqTypeString = cdrIgmpReqTypeString;
    }

    public String getCdrIgmpReqRightString() {
        return cdrIgmpReqRightString;
    }

    public void setCdrIgmpReqRightString(String cdrIgmpReqRightString) {
        this.cdrIgmpReqRightString = cdrIgmpReqRightString;
    }

    public String getCdrIgmpReqResultString() {
        return cdrIgmpReqResultString;
    }

    public void setCdrIgmpReqResultString(String cdrIgmpReqResultString) {
        this.cdrIgmpReqResultString = cdrIgmpReqResultString;
    }

    public String getCdrLeaveTypeString() {
        return cdrLeaveTypeString;
    }

    public void setCdrLeaveTypeString(String cdrLeaveTypeString) {
        this.cdrLeaveTypeString = cdrLeaveTypeString;
    }

    public String getCdrRecordTimeString() {
        return cdrRecordTimeString;
    }

    public void setCdrRecordTimeString(String cdrRecordTimeString) {
        this.cdrRecordTimeString = cdrRecordTimeString;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpControlledMcCdrTable");
        sb.append("{entityId=").append(entityId);
        sb.append(", sequenceIndex=").append(sequenceIndex);
        sb.append(", topControlledMcCdrSlotNum=").append(topControlledMcCdrSlotNum);
        sb.append(", topControlledMcCdrPonNum=").append(topControlledMcCdrPonNum);
        sb.append(", topControlledMcCdrOnuAuthNum=").append(topControlledMcCdrOnuAuthNum);
        sb.append(", topControlledMcCdrUniNum=").append(topControlledMcCdrUniNum);
        sb.append(", cdrIgmpReqTime=").append(cdrIgmpReqTime);
        sb.append(", cdrIgmpReqType=").append(cdrIgmpReqType);
        sb.append(", cdrIgmpReqChannel='").append(cdrIgmpReqChannel).append('\'');
        sb.append(", cdrIgmpReqRight=").append(cdrIgmpReqRight);
        sb.append(", cdrIgmpReqResult=").append(cdrIgmpReqResult);
        sb.append(", cdrLeaveType=").append(cdrLeaveType);
        sb.append(", cdrRecordTime=").append(cdrRecordTime);
        sb.append('}');
        return sb.toString();
    }
}
