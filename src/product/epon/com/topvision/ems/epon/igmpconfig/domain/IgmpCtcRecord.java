/***********************************************************************
 * $Id: IgmpCtcRecord.java,v1.0 2016-6-7 下午4:55:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2016-6-7-下午4:55:13
 *
 */
public class IgmpCtcRecord implements AliasesSuperType {
    private static final long serialVersionUID = 3496855341609899167L;

    private Long entityId;
    private Long uniIndex;
    //序列号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.1", index = true)
    private byte[] sequenceIndex;
    private Long cdrSequence;
    //端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.2", writable = true, type = "Integer32")
    private Integer cdrType;
    //槽位数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.3", writable = true, type = "Integer32")
    private Integer cdrSlot;
    //pon端口数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.4", writable = true, type = "Integer32")
    private Integer cdrPon;
    //ONU ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.5", writable = true, type = "Integer32")
    private Integer cdrOnu;
    //UNI ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.6", writable = true, type = "Integer32")
    private Integer cdrUni;
    //组播请求消息类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.7", writable = true, type = "Integer32")
    private Integer cdrReqType;
    //组播请求时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.8", writable = true, type = "Integer32")
    private Long cdrReqTime;
    private String reqTimeStr;
    //组播申请访问的频道
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.9", writable = true, type = "Integer32")
    private Integer cdrReqGrpId;
    //组播频道权限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.10", writable = true, type = "Integer32")
    private Integer cdrGrpAuth;
    //组播报文结果
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.11", writable = true, type = "Integer32")
    private Integer cdrReqResult;
    //组播离开方式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.12", writable = true, type = "Integer32")
    private Integer cdrLeaveType;
    //组播记录创建时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.5.1.13", writable = true, type = "Integer32")
    private Long cdrRecordTime;
    private String recordTimeStr;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public byte[] getSequenceIndex() {
        return sequenceIndex;
    }

    public void setSequenceIndex(byte[] sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
        if (sequenceIndex != null) {
            int indexSize = sequenceIndex.length;
            Long sequenceNum = 0L;
            for (int i = 0; i < indexSize; i++) {
                sequenceNum += sequenceIndex[i] << ((indexSize - 1 - i) * 8);
            }
            this.cdrSequence = sequenceNum;
        }
    }

    public Long getCdrSequence() {
        return cdrSequence;
    }

    public void setCdrSequence(Long cdrSequence) {
        this.cdrSequence = cdrSequence;
    }

    public Integer getCdrType() {
        return cdrType;
    }

    public void setCdrType(Integer cdrType) {
        this.cdrType = cdrType;
    }

    public Integer getCdrSlot() {
        return cdrSlot;
    }

    public void setCdrSlot(Integer cdrSlot) {
        this.cdrSlot = cdrSlot;
    }

    public Integer getCdrPon() {
        return cdrPon;
    }

    public void setCdrPon(Integer cdrPon) {
        this.cdrPon = cdrPon;
    }

    public Integer getCdrOnu() {
        return cdrOnu;
    }

    public void setCdrOnu(Integer cdrOnu) {
        this.cdrOnu = cdrOnu;
    }

    public Integer getCdrUni() {
        return cdrUni;
    }

    public void setCdrUni(Integer cdrUni) {
        this.cdrUni = cdrUni;
    }

    public Integer getCdrReqType() {
        return cdrReqType;
    }

    public void setCdrReqType(Integer cdrReqType) {
        this.cdrReqType = cdrReqType;
    }

    public Long getCdrReqTime() {
        return cdrReqTime;
    }

    public void setCdrReqTime(Long cdrReqTime) {
        this.cdrReqTime = cdrReqTime;
    }

    public Integer getCdrReqGrpId() {
        return cdrReqGrpId;
    }

    public void setCdrReqGrpId(Integer cdrReqGrpId) {
        this.cdrReqGrpId = cdrReqGrpId;
    }

    public Integer getCdrGrpAuth() {
        return cdrGrpAuth;
    }

    public void setCdrGrpAuth(Integer cdrGrpAuth) {
        this.cdrGrpAuth = cdrGrpAuth;
    }

    public Integer getCdrReqResult() {
        return cdrReqResult;
    }

    public void setCdrReqResult(Integer cdrReqResult) {
        this.cdrReqResult = cdrReqResult;
    }

    public Integer getCdrLeaveType() {
        return cdrLeaveType;
    }

    public void setCdrLeaveType(Integer cdrLeaveType) {
        this.cdrLeaveType = cdrLeaveType;
    }

    public Long getCdrRecordTime() {
        return cdrRecordTime;
    }

    public void setCdrRecordTime(Long cdrRecordTime) {
        this.cdrRecordTime = cdrRecordTime;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public String getReqTimeStr() {
        if (reqTimeStr == null && this.cdrReqTime != null) {
            this.reqTimeStr = df.format(new Date(this.cdrReqTime * 1000L));
        }
        return reqTimeStr;
    }

    public void setReqTimeStr(String reqTimeStr) {
        this.reqTimeStr = reqTimeStr;
    }

    public String getRecordTimeStr() {
        if (recordTimeStr == null && this.cdrRecordTime != null) {
            this.recordTimeStr = df.format(new Date(this.cdrRecordTime * 1000L));
        }
        return recordTimeStr;
    }

    public void setRecordTimeStr(String recordTimeStr) {
        this.recordTimeStr = recordTimeStr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpCtcRecord [entityId=");
        builder.append(entityId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", sequenceIndex=");
        builder.append(sequenceIndex);
        builder.append(", cdrSequence=");
        builder.append(cdrSequence);
        builder.append(", cdrType=");
        builder.append(cdrType);
        builder.append(", cdrSlot=");
        builder.append(cdrSlot);
        builder.append(", cdrPon=");
        builder.append(cdrPon);
        builder.append(", cdrOnu=");
        builder.append(cdrOnu);
        builder.append(", cdrUni=");
        builder.append(cdrUni);
        builder.append(", cdrReqType=");
        builder.append(cdrReqType);
        builder.append(", cdrReqTime=");
        builder.append(cdrReqTime);
        builder.append(", reqTimeStr=");
        builder.append(reqTimeStr);
        builder.append(", cdrReqGrpId=");
        builder.append(cdrReqGrpId);
        builder.append(", cdrGrpAuth=");
        builder.append(cdrGrpAuth);
        builder.append(", cdrReqResult=");
        builder.append(cdrReqResult);
        builder.append(", cdrLeaveType=");
        builder.append(cdrLeaveType);
        builder.append(", cdrRecordTime=");
        builder.append(cdrRecordTime);
        builder.append(", recordTimeStr=");
        builder.append(recordTimeStr);
        builder.append("]");
        return builder.toString();
    }


}
