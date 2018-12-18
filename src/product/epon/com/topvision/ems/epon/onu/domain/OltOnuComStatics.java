/***********************************************************************
 * $Id: OltOnuComStatics.java,v1.0 2012-12-18 下午14:47:59 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author lzt
 * @created @2012-12-18-下午14:47:59
 * 
 */
public class OltOnuComStatics {

    private Long entityId;
    private Long onuComId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.1", index = true)
    private Integer topOnuComInfoCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.2", index = true)
    private Integer topOnuComInfoPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.3", index = true)
    private Integer topOnuComInfoOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.4", index = true)
    private Integer topOnuComInfoComIndex;
    private Long onuComIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.5", type = "Counter64")
    private String onuComStatisCts;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.6", type = "Counter64")
    private String onuComStatisDsr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.7", type = "Counter64")
    private String onuComStatisRng;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.8", type = "Counter64")
    private String onuComStatisDcd;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.9", type = "Counter64")
    private String onuComStatisRxBytes;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.10", type = "Counter64")
    private String onuComStatisTxBytes;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.11", type = "Counter64")
    private String onuComStatisFrames;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.12", type = "Counter64")
    private String onuComStatisOverRun;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.13", type = "Counter64")
    private String onuComStatisParityErr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.14", type = "Counter64")
    private String onuComStatisBrk;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.15", type = "Counter64")
    private String onuComStatisBufOverRun;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.16", type = "Counter64")
    private String onuComStatisRxDrops;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.17", type = "Counter64")
    private String onuComStatisTxDrops;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.18", type = "Counter64")
    private String onuComStatisRecvFromNet;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.19", type = "Counter64")
    private String onuComStatisSendToNet;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.3.1.20", writable = true, type = "Integer32")
    private Integer onuComStatisClear;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuComId() {
        return onuComId;
    }

    public void setOnuComId(Long onuComId) {
        this.onuComId = onuComId;
    }

    public Integer getTopOnuComInfoCardIndex() {
        return topOnuComInfoCardIndex;
    }

    public void setTopOnuComInfoCardIndex(Integer topOnuComInfoCardIndex) {
        this.topOnuComInfoCardIndex = topOnuComInfoCardIndex;
    }

    public Integer getTopOnuComInfoPonIndex() {
        return topOnuComInfoPonIndex;
    }

    public void setTopOnuComInfoPonIndex(Integer topOnuComInfoPonIndex) {
        this.topOnuComInfoPonIndex = topOnuComInfoPonIndex;
    }

    public Integer getTopOnuComInfoOnuIndex() {
        return topOnuComInfoOnuIndex;
    }

    public void setTopOnuComInfoOnuIndex(Integer topOnuComInfoOnuIndex) {
        this.topOnuComInfoOnuIndex = topOnuComInfoOnuIndex;
    }

    public Integer getTopOnuComInfoComIndex() {
        return topOnuComInfoComIndex;
    }

    public void setTopOnuComInfoComIndex(Integer topOnuComInfoComIndex) {
        this.topOnuComInfoComIndex = topOnuComInfoComIndex;
    }

    public Long getOnuComIndex() {
        if (onuComIndex == null) {
            onuComIndex = EponIndex.getOnuComIndex(topOnuComInfoCardIndex, topOnuComInfoPonIndex,
                    topOnuComInfoOnuIndex, topOnuComInfoComIndex);
        }
        return onuComIndex;
    }

    public void setOnuComIndex(Long onuComIndex) {
        this.topOnuComInfoCardIndex = EponIndex.getSlotNo(onuComIndex).intValue();
        this.topOnuComInfoPonIndex = EponIndex.getPonNo(onuComIndex).intValue();
        this.topOnuComInfoOnuIndex = EponIndex.getOnuNo(onuComIndex).intValue();
        this.topOnuComInfoComIndex = EponIndex.getUniNo(onuComIndex).intValue();
        this.onuComIndex = onuComIndex;
    }

    public String getOnuComStatisCts() {
        return onuComStatisCts;
    }

    public void setOnuComStatisCts(String onuComStatisCts) {
        this.onuComStatisCts = onuComStatisCts;
    }

    public String getOnuComStatisDsr() {
        return onuComStatisDsr;
    }

    public void setOnuComStatisDsr(String onuComStatisDsr) {
        this.onuComStatisDsr = onuComStatisDsr;
    }

    public String getOnuComStatisRng() {
        return onuComStatisRng;
    }

    public void setOnuComStatisRng(String onuComStatisRng) {
        this.onuComStatisRng = onuComStatisRng;
    }

    public String getOnuComStatisDcd() {
        return onuComStatisDcd;
    }

    public void setOnuComStatisDcd(String onuComStatisDcd) {
        this.onuComStatisDcd = onuComStatisDcd;
    }

    public String getOnuComStatisRxBytes() {
        return onuComStatisRxBytes;
    }

    public void setOnuComStatisRxBytes(String onuComStatisRxBytes) {
        this.onuComStatisRxBytes = onuComStatisRxBytes;
    }

    public String getOnuComStatisTxBytes() {
        return onuComStatisTxBytes;
    }

    public void setOnuComStatisTxBytes(String onuComStatisTxBytes) {
        this.onuComStatisTxBytes = onuComStatisTxBytes;
    }

    public String getOnuComStatisFrames() {
        return onuComStatisFrames;
    }

    public void setOnuComStatisFrames(String onuComStatisFrames) {
        this.onuComStatisFrames = onuComStatisFrames;
    }

    public String getOnuComStatisOverRun() {
        return onuComStatisOverRun;
    }

    public void setOnuComStatisOverRun(String onuComStatisOverRun) {
        this.onuComStatisOverRun = onuComStatisOverRun;
    }

    public String getOnuComStatisParityErr() {
        return onuComStatisParityErr;
    }

    public void setOnuComStatisParityErr(String onuComStatisParityErr) {
        this.onuComStatisParityErr = onuComStatisParityErr;
    }

    public String getOnuComStatisBrk() {
        return onuComStatisBrk;
    }

    public void setOnuComStatisBrk(String onuComStatisBrk) {
        this.onuComStatisBrk = onuComStatisBrk;
    }

    public String getOnuComStatisBufOverRun() {
        return onuComStatisBufOverRun;
    }

    public void setOnuComStatisBufOverRun(String onuComStatisBufOverRun) {
        this.onuComStatisBufOverRun = onuComStatisBufOverRun;
    }

    public String getOnuComStatisRxDrops() {
        return onuComStatisRxDrops;
    }

    public void setOnuComStatisRxDrops(String onuComStatisRxDrops) {
        this.onuComStatisRxDrops = onuComStatisRxDrops;
    }

    public String getOnuComStatisTxDrops() {
        return onuComStatisTxDrops;
    }

    public void setOnuComStatisTxDrops(String onuComStatisTxDrops) {
        this.onuComStatisTxDrops = onuComStatisTxDrops;
    }

    public String getOnuComStatisRecvFromNet() {
        return onuComStatisRecvFromNet;
    }

    public void setOnuComStatisRecvFromNet(String onuComStatisRecvFromNet) {
        this.onuComStatisRecvFromNet = onuComStatisRecvFromNet;
    }

    public String getOnuComStatisSendToNet() {
        return onuComStatisSendToNet;
    }

    public void setOnuComStatisSendToNet(String onuComStatisSendToNet) {
        this.onuComStatisSendToNet = onuComStatisSendToNet;
    }

    public Integer getOnuComStatisClear() {
        return onuComStatisClear;
    }

    public void setOnuComStatisClear(Integer onuComStatisClear) {
        this.onuComStatisClear = onuComStatisClear;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuComStatics [entityId=");
        builder.append(entityId);
        builder.append(", onuComId=");
        builder.append(onuComId);
        builder.append(", topOnuComInfoCardIndex=");
        builder.append(topOnuComInfoCardIndex);
        builder.append(", topOnuComInfoPonIndex=");
        builder.append(topOnuComInfoPonIndex);
        builder.append(", topOnuComInfoOnuIndex=");
        builder.append(topOnuComInfoOnuIndex);
        builder.append(", topOnuComInfoComIndex=");
        builder.append(topOnuComInfoComIndex);
        builder.append(", onuComIndex=");
        builder.append(onuComIndex);
        builder.append(", onuComStatisCts=");
        builder.append(onuComStatisCts);
        builder.append(", onuComStatisDsr=");
        builder.append(onuComStatisDsr);
        builder.append(", onuComStatisRng=");
        builder.append(onuComStatisRng);
        builder.append(", onuComStatisDcd=");
        builder.append(onuComStatisDcd);
        builder.append(", onuComStatisRxBytes=");
        builder.append(onuComStatisRxBytes);
        builder.append(", onuComStatisTxBytes=");
        builder.append(onuComStatisTxBytes);
        builder.append(", onuComStatisFrames=");
        builder.append(onuComStatisFrames);
        builder.append(", onuComStatisOverRun=");
        builder.append(onuComStatisOverRun);
        builder.append(", onuComStatisParityErr=");
        builder.append(onuComStatisParityErr);
        builder.append(", onuComStatisBrk=");
        builder.append(onuComStatisBrk);
        builder.append(", onuComStatisBufOverRun=");
        builder.append(onuComStatisBufOverRun);
        builder.append(", onuComStatisRxDrops=");
        builder.append(onuComStatisRxDrops);
        builder.append(", onuComStatisTxDrops=");
        builder.append(onuComStatisTxDrops);
        builder.append(", onuComStatisRecvFromNet=");
        builder.append(onuComStatisRecvFromNet);
        builder.append(", onuComStatisSendToNet=");
        builder.append(onuComStatisSendToNet);
        builder.append(", onuComStatisClear=");
        builder.append(onuComStatisClear);
        builder.append("]");
        return builder.toString();
    }

}
