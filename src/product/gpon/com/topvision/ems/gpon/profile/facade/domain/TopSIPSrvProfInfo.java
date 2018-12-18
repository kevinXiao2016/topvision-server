/***********************************************************************
 * $Id: TopSIPSrvProfInfo.java,v1.0 2017年6月16日 下午3:53:07 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年6月16日-下午3:53:07
 *
 */
public class TopSIPSrvProfInfo implements AliasesSuperType {
    private static final long serialVersionUID = 3551644861603577383L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.1", index = true, type = "Integer32")
    private Integer topSIPSrvProfIdx;// 1-64 SIP业务数据模板索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.2", writable = true, type = "OctetString")
    private String topSIPSrvProfName;// 1-31 SIP业务数据模板名

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.3", writable = true, type = "Integer32")
    private Integer topSIPSrvProfCallWait;// 呼叫等待权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.4", writable = true, type = "Integer32")
    private Integer topSIPSrvProf3Way;// 三方通话权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.5", writable = true, type = "Integer32")
    private Integer topSIPSrvProfCallTransfer;// 呼叫转移权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.6", writable = true, type = "Integer32")
    private Integer topSIPSrvProfCallHold;// 呼叫保持权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.8", writable = true, type = "Integer32")
    private Integer topSIPSrvProfDND;// 呼叫免打扰权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.10", writable = true, type = "Integer32")
    private Integer topSIPSrvProfHotline;// 热线业务权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.11", writable = true, type = "OctetString")
    private String topSIPSrvProfHotlineNum;// 热线业务号码 1..32
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.12", writable = true, type = "Integer32")
    private Integer topSIPSrvProfHotDelay;// 延迟热线权限 0,1
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.13", type = "Integer32")
    private Integer topSIPSrvProfBindCnt;// 模板绑定次数

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.3.1.14", writable = true, type = "Integer32")
    private Integer topSIPSrvProfRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopSIPSrvProfIdx() {
        return topSIPSrvProfIdx;
    }

    public void setTopSIPSrvProfIdx(Integer topSIPSrvProfIdx) {
        this.topSIPSrvProfIdx = topSIPSrvProfIdx;
    }

    public String getTopSIPSrvProfName() {
        return topSIPSrvProfName;
    }

    public void setTopSIPSrvProfName(String topSIPSrvProfName) {
        this.topSIPSrvProfName = topSIPSrvProfName;
    }

    public Integer getTopSIPSrvProfCallWait() {
        return topSIPSrvProfCallWait;
    }

    public void setTopSIPSrvProfCallWait(Integer topSIPSrvProfCallWait) {
        this.topSIPSrvProfCallWait = topSIPSrvProfCallWait;
    }

    public Integer getTopSIPSrvProf3Way() {
        return topSIPSrvProf3Way;
    }

    public void setTopSIPSrvProf3Way(Integer topSIPSrvProf3Way) {
        this.topSIPSrvProf3Way = topSIPSrvProf3Way;
    }

    public Integer getTopSIPSrvProfCallTransfer() {
        return topSIPSrvProfCallTransfer;
    }

    public void setTopSIPSrvProfCallTransfer(Integer topSIPSrvProfCallTransfer) {
        this.topSIPSrvProfCallTransfer = topSIPSrvProfCallTransfer;
    }

    public Integer getTopSIPSrvProfCallHold() {
        return topSIPSrvProfCallHold;
    }

    public void setTopSIPSrvProfCallHold(Integer topSIPSrvProfCallHold) {
        this.topSIPSrvProfCallHold = topSIPSrvProfCallHold;
    }

    public Integer getTopSIPSrvProfDND() {
        return topSIPSrvProfDND;
    }

    public void setTopSIPSrvProfDND(Integer topSIPSrvProfDND) {
        this.topSIPSrvProfDND = topSIPSrvProfDND;
    }

    public Integer getTopSIPSrvProfHotline() {
        return topSIPSrvProfHotline;
    }

    public void setTopSIPSrvProfHotline(Integer topSIPSrvProfHotline) {
        this.topSIPSrvProfHotline = topSIPSrvProfHotline;
    }

    public String getTopSIPSrvProfHotlineNum() {
        return topSIPSrvProfHotlineNum;
    }

    public void setTopSIPSrvProfHotlineNum(String topSIPSrvProfHotlineNum) {
        this.topSIPSrvProfHotlineNum = topSIPSrvProfHotlineNum;
    }

    public Integer getTopSIPSrvProfHotDelay() {
        return topSIPSrvProfHotDelay;
    }

    public void setTopSIPSrvProfHotDelay(Integer topSIPSrvProfHotDelay) {
        this.topSIPSrvProfHotDelay = topSIPSrvProfHotDelay;
    }

    public Integer getTopSIPSrvProfBindCnt() {
        return topSIPSrvProfBindCnt;
    }

    public void setTopSIPSrvProfBindCnt(Integer topSIPSrvProfBindCnt) {
        this.topSIPSrvProfBindCnt = topSIPSrvProfBindCnt;
    }

    public Integer getTopSIPSrvProfRowStatus() {
        return topSIPSrvProfRowStatus;
    }

    public void setTopSIPSrvProfRowStatus(Integer topSIPSrvProfRowStatus) {
        this.topSIPSrvProfRowStatus = topSIPSrvProfRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopSIPSrvProfInfo [entityId=");
        builder.append(entityId);
        builder.append(", topSIPSrvProfIdx=");
        builder.append(topSIPSrvProfIdx);
        builder.append(", topSIPSrvProfName=");
        builder.append(topSIPSrvProfName);
        builder.append(", topSIPSrvProfCallWait=");
        builder.append(topSIPSrvProfCallWait);
        builder.append(", topSIPSrvProf3Way=");
        builder.append(topSIPSrvProf3Way);
        builder.append(", topSIPSrvProfCallTransfer=");
        builder.append(topSIPSrvProfCallTransfer);
        builder.append(", topSIPSrvProfCallHold=");
        builder.append(topSIPSrvProfCallHold);
        builder.append(", topSIPSrvProfDND=");
        builder.append(topSIPSrvProfDND);
        builder.append(", topSIPSrvProfHotline=");
        builder.append(topSIPSrvProfHotline);
        builder.append(", topSIPSrvProfHotlineNum=");
        builder.append(topSIPSrvProfHotlineNum);
        builder.append(", topSIPSrvProfHotDelay=");
        builder.append(topSIPSrvProfHotDelay);
        builder.append(", topSIPSrvProfBindCnt=");
        builder.append(topSIPSrvProfBindCnt);
        builder.append(", topSIPSrvProfRowStatus=");
        builder.append(topSIPSrvProfRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
