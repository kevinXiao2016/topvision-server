/***********************************************************************
 * $Id: TopVoIPLineStatus.java,v1.0 2017年5月4日 下午1:26:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author haojie
 * @created @2017年5月4日-下午1:26:50
 *
 */
public class TopVoIPLineStatus implements AliasesSuperType {
    private static final long serialVersionUID = 3740964229946641222L;
    private Long entityId;
    private Long onuId;
    private final static String[] TOPVOIPLINE_CODEC = { "pcmu", "reserved1", "reserved2", "gsm", "g723", "dvi4-8k",
            "dvi4-16k", "lpc", "pcma", "g722", "l16-2c", "l16-1c", "qcelp", "cn", "mpa", "g728", "dvi4-11k",
            "dvi4-22k", "g729" };
    private final static String[] TOPVOIPLINE_SERVSTATUS = { "None/initial", "Registered", "In session",
            "Failed registration - icmp error", "Failed registration - failed tcp",
            "Failed registration - failed authentication", "Failed registration - timeout",
            "Failed registration - server fail code", "Failed invite - icmp error", "Failed invite - failed tcp",
            "Failed invite - failed authentication", "Failed invite - timeout", "Failed invite - server fail code",
            "Port not configured", "Config done", "Disabled by switch" };
    private final static String[] TOPVOIPLINE_SESSTYPE = { "Idle/none", "2way", "3way", "Fax", "Telem", "Conference" };
    private final static String[] TOPVOIPLINE_STATE = { "Idle, on-hook", "Off-hook dial tone", "Dialling",
            "Ringing or FSK alerting/data", "Audible ringback", "Connecting", "Connected",
            "Disconnecting, audible indication", "Receiver off hook (ROH), no tone", "ROH with tone",
            "Unknown or undefined" };

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.1", index = true)
    private Integer topVoIPLineSlotIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.2", index = true)
    private Integer topVoIPLinePortIndx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.3", index = true)
    private Integer topVoIPLineOnuIdx;
    private Long onuIndex;// 网管用onuIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.4", index = true)
    private Integer topVoIPLinePotsIdx;// pots口index，一般就只有一个，index为1

    // 0 pcmu
    // 1 reserved1
    // 2 reserved2
    // 3 gsm
    // 4 g723
    // 5 dvi4-8k
    // 6 dvi4-16k
    // 7 lpc
    // 8 pcma
    // 9 g722
    // 10 l16-2c
    // 11 l16-1c
    // 12 qcelp
    // 13 cn
    // 14 mpa
    // 15 g728
    // 16 dvi4-11k
    // 17 dvi4-22k
    // 18 g729
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.5", type = "Integer32")
    private Integer topVoIPLineCodec;// 编码模式
    private String topVoIPLineCodecString;
    // 0 None/initial
    // 1 Registered
    // 2 In session
    // 3 Failed registration - icmp error
    // 4 Failed registration - failed tcp
    // 5 Failed registration - failed authentication
    // 6 Failed registration - timeout
    // 7 Failed registration - server fail code
    // 8 Failed invite - icmp error
    // 9 Failed invite - failed tcp
    // 10 Failed invite - failed authentication
    // 11 Failed invite - timeout
    // 12 Failed invite - server fail code
    // 13 Port not configured
    // 14 Config done
    // 15 Disabled by switch
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.6", type = "Integer32")
    private Integer topVoIPLineServStatus;// VoIP业务服务状态
    private String topVoIPLineServStatusString;
    // 0 Idle/none
    // 1 2way
    // 2 3way
    // 3 Fax
    // 4 Telem
    // 5 Conference
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.7", type = "Integer32")
    private Integer topVoIPLineSessType;// 会话类型
    private String topVoIPLineSessTypeString;
    // 0 Idle, on-hook
    // 1 Off-hook dial tone
    // 2 Dialling
    // 3 Ringing or FSK alerting/data
    // 4 Audible ringback
    // 5 Connecting
    // 6 Connected
    // 7 Disconnecting, audible indication
    // 8 Receiver off hook (ROH), no tone
    // 9 ROH with tone
    // 10 Unknown or undefined
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.3.1.8", type = "Integer32")
    private Integer topVoIPLineState;// Voip线路状态
    private String topVoIPLineStateString;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getTopVoIPLineSlotIdx() {
        return topVoIPLineSlotIdx;
    }

    public void setTopVoIPLineSlotIdx(Integer topVoIPLineSlotIdx) {
        this.topVoIPLineSlotIdx = topVoIPLineSlotIdx;
    }

    public Integer getTopVoIPLinePortIndx() {
        return topVoIPLinePortIndx;
    }

    public void setTopVoIPLinePortIndx(Integer topVoIPLinePortIndx) {
        this.topVoIPLinePortIndx = topVoIPLinePortIndx;
    }

    public Integer getTopVoIPLineOnuIdx() {
        return topVoIPLineOnuIdx;
    }

    public void setTopVoIPLineOnuIdx(Integer topVoIPLineOnuIdx) {
        this.topVoIPLineOnuIdx = topVoIPLineOnuIdx;
    }

    public Long getOnuIndex() {
        if (onuIndex != null) {
            return onuIndex;
        }
        return EponIndex.getOnuIndex(topVoIPLineSlotIdx, topVoIPLinePortIndx, topVoIPLineOnuIdx);
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            topVoIPLineSlotIdx = EponIndex.getSlotNo(onuIndex).intValue();
            topVoIPLinePortIndx = EponIndex.getPonNo(onuIndex).intValue();
            topVoIPLineOnuIdx = EponIndex.getOnuNo(onuIndex).intValue();
        }
    }

    public Integer getTopVoIPLinePotsIdx() {
        return topVoIPLinePotsIdx;
    }

    public void setTopVoIPLinePotsIdx(Integer topVoIPLinePotsIdx) {
        this.topVoIPLinePotsIdx = topVoIPLinePotsIdx;
    }

    public Integer getTopVoIPLineCodec() {
        return topVoIPLineCodec;
    }

    public void setTopVoIPLineCodec(Integer topVoIPLineCodec) {
        this.topVoIPLineCodec = topVoIPLineCodec;
        if (topVoIPLineCodec != null) {
            topVoIPLineCodecString = TOPVOIPLINE_CODEC[topVoIPLineCodec];
        }
    }

    public Integer getTopVoIPLineServStatus() {
        return topVoIPLineServStatus;
    }

    public void setTopVoIPLineServStatus(Integer topVoIPLineServStatus) {
        this.topVoIPLineServStatus = topVoIPLineServStatus;
        if (topVoIPLineServStatus != null) {
            topVoIPLineServStatusString = TOPVOIPLINE_SERVSTATUS[topVoIPLineServStatus];
        }
    }

    public Integer getTopVoIPLineSessType() {
        return topVoIPLineSessType;
    }

    public void setTopVoIPLineSessType(Integer topVoIPLineSessType) {
        this.topVoIPLineSessType = topVoIPLineSessType;
        if (topVoIPLineSessType != null) {
            topVoIPLineSessTypeString = TOPVOIPLINE_SESSTYPE[topVoIPLineSessType];
        }
    }

    public Integer getTopVoIPLineState() {
        return topVoIPLineState;
    }

    public void setTopVoIPLineState(Integer topVoIPLineState) {
        this.topVoIPLineState = topVoIPLineState;
        if (topVoIPLineState != null) {
            topVoIPLineStateString = TOPVOIPLINE_STATE[topVoIPLineState];
        }
    }

    public String getTopVoIPLineCodecString() {
        return topVoIPLineCodecString;
    }

    public void setTopVoIPLineCodecString(String topVoIPLineCodecString) {
        this.topVoIPLineCodecString = topVoIPLineCodecString;
    }

    public String getTopVoIPLineServStatusString() {
        return topVoIPLineServStatusString;
    }

    public void setTopVoIPLineServStatusString(String topVoIPLineServStatusString) {
        this.topVoIPLineServStatusString = topVoIPLineServStatusString;
    }

    public String getTopVoIPLineSessTypeString() {
        return topVoIPLineSessTypeString;
    }

    public void setTopVoIPLineSessTypeString(String topVoIPLineSessTypeString) {
        this.topVoIPLineSessTypeString = topVoIPLineSessTypeString;
    }

    public String getTopVoIPLineStateString() {
        return topVoIPLineStateString;
    }

    public void setTopVoIPLineStateString(String topVoIPLineStateString) {
        this.topVoIPLineStateString = topVoIPLineStateString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopVoIPLineStatus [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", topVoIPLineSlotIdx=");
        builder.append(topVoIPLineSlotIdx);
        builder.append(", topVoIPLinePortIndx=");
        builder.append(topVoIPLinePortIndx);
        builder.append(", topVoIPLineOnuIdx=");
        builder.append(topVoIPLineOnuIdx);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topVoIPLinePotsIdx=");
        builder.append(topVoIPLinePotsIdx);
        builder.append(", topVoIPLineCodec=");
        builder.append(topVoIPLineCodec);
        builder.append(", topVoIPLineCodecString=");
        builder.append(topVoIPLineCodecString);
        builder.append(", topVoIPLineServStatus=");
        builder.append(topVoIPLineServStatus);
        builder.append(", topVoIPLineServStatusString=");
        builder.append(topVoIPLineServStatusString);
        builder.append(", topVoIPLineSessType=");
        builder.append(topVoIPLineSessType);
        builder.append(", topVoIPLineSessTypeString=");
        builder.append(topVoIPLineSessTypeString);
        builder.append(", topVoIPLineState=");
        builder.append(topVoIPLineState);
        builder.append(", topVoIPLineStateString=");
        builder.append(topVoIPLineStateString);
        builder.append("]");
        return builder.toString();
    }

}
