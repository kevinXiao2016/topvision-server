/***********************************************************************
 * $Id: TopSIPPstnUser.java,v1.0 2017年5月4日 上午11:38:37 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.domain;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:38:37
 *
 */
public class TopSIPPstnUser implements AliasesSuperType {
    private static final long serialVersionUID = -7746731738914049233L;
    private Long entityId;
    private Long onuId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.1", index = true)
    private Integer topSIPPstnUserSlotIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.2", index = true)
    private Integer topSIPPstnUserPortIndx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.3", index = true)
    private Integer topSIPPstnUserOnuIdx;
    private Long onuIndex;// 网管用onuIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.4", index = true)
    private Integer topSIPPstnUserPotsIdx;// pots口index，一般就只有一个，index为1

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.5", writable = true, type = "OctetString")
    private String topSIPPstnUserTelno;// 1-64 SIP PSTN用户电话号码
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.6", writable = true, type = "OctetString")
    private String topSIPPstnUserName;// 1-24 SIP PSTN用户认证的用户名
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.7", writable = true, type = "OctetString")
    private String topSIPPstnUserPwd;// 1-24 SIP PSTN用户认证的密码

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.8", writable = true, type = "Integer32")
    private Integer topSIPPstnUserForwardType;// 呼叫前转类型 0-disable 1-busy;2-no reply;3-unconditional
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.9", writable = true, type = "OctetString")
    private String topSIPPstnUserTransferNum;// 1-64 呼叫转移号码
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.10", writable = true, type = "Integer32")
    private Integer topSIPPstnUserForwardTime;// 5..55 s 呼叫前转无响应超时时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.11", writable = true, type = "Integer32")
    private Integer topSIPPstnUserDigitmapId;// 0-64 数图模板ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.2.1.12", writable = true, type = "Integer32")
    private Integer topSIPPstnUserSipsrvId;// 0-64 SIP业务数据模板ID

    private List<TopSIPSrvProfInfo> topSIPSrvProfInfos;// 已经存在的sip业务数据模板集合
    private List<TopDigitMapProfInfo> digitMapProfInfos;// 已经存在的数图模板集合

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

    public Integer getTopSIPPstnUserSlotIdx() {
        return topSIPPstnUserSlotIdx;
    }

    public void setTopSIPPstnUserSlotIdx(Integer topSIPPstnUserSlotIdx) {
        this.topSIPPstnUserSlotIdx = topSIPPstnUserSlotIdx;
    }

    public Integer getTopSIPPstnUserPortIndx() {
        return topSIPPstnUserPortIndx;
    }

    public void setTopSIPPstnUserPortIndx(Integer topSIPPstnUserPortIndx) {
        this.topSIPPstnUserPortIndx = topSIPPstnUserPortIndx;
    }

    public Integer getTopSIPPstnUserOnuIdx() {
        return topSIPPstnUserOnuIdx;
    }

    public void setTopSIPPstnUserOnuIdx(Integer topSIPPstnUserOnuIdx) {
        this.topSIPPstnUserOnuIdx = topSIPPstnUserOnuIdx;
    }

    public Long getOnuIndex() {
        if (onuIndex != null) {
            return onuIndex;
        }
        return EponIndex.getOnuIndex(topSIPPstnUserSlotIdx, topSIPPstnUserPortIndx, topSIPPstnUserOnuIdx);
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            topSIPPstnUserSlotIdx = EponIndex.getSlotNo(onuIndex).intValue();
            topSIPPstnUserPortIndx = EponIndex.getPonNo(onuIndex).intValue();
            topSIPPstnUserOnuIdx = EponIndex.getOnuNo(onuIndex).intValue();
        }
    }

    public Integer getTopSIPPstnUserPotsIdx() {
        return topSIPPstnUserPotsIdx;
    }

    public void setTopSIPPstnUserPotsIdx(Integer topSIPPstnUserPotsIdx) {
        this.topSIPPstnUserPotsIdx = topSIPPstnUserPotsIdx;
    }

    public String getTopSIPPstnUserTelno() {
        return topSIPPstnUserTelno;
    }

    public void setTopSIPPstnUserTelno(String topSIPPstnUserTelno) {
        this.topSIPPstnUserTelno = topSIPPstnUserTelno;
    }

    public String getTopSIPPstnUserName() {
        return topSIPPstnUserName;
    }

    public void setTopSIPPstnUserName(String topSIPPstnUserName) {
        this.topSIPPstnUserName = topSIPPstnUserName;
    }

    public String getTopSIPPstnUserPwd() {
        return topSIPPstnUserPwd;
    }

    public void setTopSIPPstnUserPwd(String topSIPPstnUserPwd) {
        this.topSIPPstnUserPwd = topSIPPstnUserPwd;
    }

    public String getTopSIPPstnUserTransferNum() {
        return topSIPPstnUserTransferNum;
    }

    public void setTopSIPPstnUserTransferNum(String topSIPPstnUserTransferNum) {
        this.topSIPPstnUserTransferNum = topSIPPstnUserTransferNum;
    }

    public Integer getTopSIPPstnUserDigitmapId() {
        return topSIPPstnUserDigitmapId;
    }

    public void setTopSIPPstnUserDigitmapId(Integer topSIPPstnUserDigitmapId) {
        this.topSIPPstnUserDigitmapId = topSIPPstnUserDigitmapId;
    }

    public Integer getTopSIPPstnUserSipsrvId() {
        return topSIPPstnUserSipsrvId;
    }

    public void setTopSIPPstnUserSipsrvId(Integer topSIPPstnUserSipsrvId) {
        this.topSIPPstnUserSipsrvId = topSIPPstnUserSipsrvId;
    }

    public Integer getTopSIPPstnUserForwardType() {
        return topSIPPstnUserForwardType;
    }

    public void setTopSIPPstnUserForwardType(Integer topSIPPstnUserForwardType) {
        this.topSIPPstnUserForwardType = topSIPPstnUserForwardType;
    }

    public List<TopSIPSrvProfInfo> getTopSIPSrvProfInfos() {
        return topSIPSrvProfInfos;
    }

    public void setTopSIPSrvProfInfos(List<TopSIPSrvProfInfo> topSIPSrvProfInfos) {
        this.topSIPSrvProfInfos = topSIPSrvProfInfos;
    }

    public List<TopDigitMapProfInfo> getDigitMapProfInfos() {
        return digitMapProfInfos;
    }

    public void setDigitMapProfInfos(List<TopDigitMapProfInfo> digitMapProfInfos) {
        this.digitMapProfInfos = digitMapProfInfos;
    }

    public Integer getTopSIPPstnUserForwardTime() {
        return topSIPPstnUserForwardTime;
    }

    public void setTopSIPPstnUserForwardTime(Integer topSIPPstnUserForwardTime) {
        this.topSIPPstnUserForwardTime = topSIPPstnUserForwardTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopSIPPstnUser [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", topSIPPstnUserSlotIdx=");
        builder.append(topSIPPstnUserSlotIdx);
        builder.append(", topSIPPstnUserPortIndx=");
        builder.append(topSIPPstnUserPortIndx);
        builder.append(", topSIPPstnUserOnuIdx=");
        builder.append(topSIPPstnUserOnuIdx);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topSIPPstnUserPotsIdx=");
        builder.append(topSIPPstnUserPotsIdx);
        builder.append(", topSIPPstnUserTelno=");
        builder.append(topSIPPstnUserTelno);
        builder.append(", topSIPPstnUserName=");
        builder.append(topSIPPstnUserName);
        builder.append(", topSIPPstnUserPwd=");
        builder.append(topSIPPstnUserPwd);
        builder.append(", topSIPPstnUserForwardType=");
        builder.append(topSIPPstnUserForwardType);
        builder.append(", topSIPPstnUserTransferNum=");
        builder.append(topSIPPstnUserTransferNum);
        builder.append(", topSIPPstnUserForwardTime=");
        builder.append(topSIPPstnUserForwardTime);
        builder.append(", topSIPPstnUserDigitmapId=");
        builder.append(topSIPPstnUserDigitmapId);
        builder.append(", topSIPPstnUserSipsrvId=");
        builder.append(topSIPPstnUserSipsrvId);
        builder.append(", topSIPSrvProfInfos=");
        builder.append(topSIPSrvProfInfos);
        builder.append(", digitMapProfInfos=");
        builder.append(digitMapProfInfos);
        builder.append("]");
        return builder.toString();
    }

}
