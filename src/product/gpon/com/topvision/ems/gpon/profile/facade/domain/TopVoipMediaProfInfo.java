/***********************************************************************
 * $Id: TopVoipMediaProfInfo.java,v1.0 2017年6月16日 下午3:44:36 $
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
 * @created @2017年6月16日-下午3:44:36
 *
 */
public class TopVoipMediaProfInfo implements AliasesSuperType {
    private static final long serialVersionUID = -7521398618726557008L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.2.1.1", index = true, type = "Integer32")
    private Integer topVoipMediaProfIdx;// 1-64 VOIP媒体模板索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.2.1.2", writable = true, type = "OctetString")
    private String topVoipMediaProfName;// 1-31 VOIP媒体模板名

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.2.1.3", writable = true, type = "Integer32")
    private Integer topVoipMediaFaxmode;// 0-Passthru 1-T.38 传真方式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.2.1.4", writable = true, type = "Integer32")
    private Integer topVoipMediaNegotiate;// 0-Negotiate 1-self-switch 协商方式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.2.1.5", type = "Integer32")
    private Integer topVoipMediaBindCnt;// 被绑定次数

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.2.1.6", writable = true, type = "Integer32")
    private Integer topVoipMediaRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopVoipMediaProfIdx() {
        return topVoipMediaProfIdx;
    }

    public void setTopVoipMediaProfIdx(Integer topVoipMediaProfIdx) {
        this.topVoipMediaProfIdx = topVoipMediaProfIdx;
    }

    public String getTopVoipMediaProfName() {
        return topVoipMediaProfName;
    }

    public void setTopVoipMediaProfName(String topVoipMediaProfName) {
        this.topVoipMediaProfName = topVoipMediaProfName;
    }

    public Integer getTopVoipMediaFaxmode() {
        return topVoipMediaFaxmode;
    }

    public void setTopVoipMediaFaxmode(Integer topVoipMediaFaxmode) {
        this.topVoipMediaFaxmode = topVoipMediaFaxmode;
    }

    public Integer getTopVoipMediaNegotiate() {
        return topVoipMediaNegotiate;
    }

    public void setTopVoipMediaNegotiate(Integer topVoipMediaNegotiate) {
        this.topVoipMediaNegotiate = topVoipMediaNegotiate;
    }

    public Integer getTopVoipMediaBindCnt() {
        return topVoipMediaBindCnt;
    }

    public void setTopVoipMediaBindCnt(Integer topVoipMediaBindCnt) {
        this.topVoipMediaBindCnt = topVoipMediaBindCnt;
    }

    public Integer getTopVoipMediaRowStatus() {
        return topVoipMediaRowStatus;
    }

    public void setTopVoipMediaRowStatus(Integer topVoipMediaRowStatus) {
        this.topVoipMediaRowStatus = topVoipMediaRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopVoipMediaProfInfo [entityId=");
        builder.append(entityId);
        builder.append(", topVoipMediaProfIdx=");
        builder.append(topVoipMediaProfIdx);
        builder.append(", topVoipMediaProfName=");
        builder.append(topVoipMediaProfName);
        builder.append(", topVoipMediaFaxmode=");
        builder.append(topVoipMediaFaxmode);
        builder.append(", topVoipMediaNegotiate=");
        builder.append(topVoipMediaNegotiate);
        builder.append(", topVoipMediaBindCnt=");
        builder.append(topVoipMediaBindCnt);
        builder.append(", topVoipMediaRowStatus=");
        builder.append(topVoipMediaRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
