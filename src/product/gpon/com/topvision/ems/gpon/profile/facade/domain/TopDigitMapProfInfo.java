/***********************************************************************
 * $Id: TopDigitMapProfInfo.java,v1.0 2017年6月17日 上午9:52:57 $
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
 * @created @2017年6月17日-上午9:52:57
 *
 */
public class TopDigitMapProfInfo implements AliasesSuperType {
    private static final long serialVersionUID = -8413749221337909370L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.1", index = true, type = "Integer32")
    private Integer topDigitMapProfIdx;// 1-64 数图模板索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.2", writable = true, type = "OctetString")
    private String topDigitMapProfName;// 1-31 数图模板名

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.3", writable = true, type = "Integer32")
    private Integer topDigitMapCirtDialTime;// 精确匹配超时时间 1-65535 ms
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.4", writable = true, type = "Integer32")
    private Integer topDigitMapPartDialTime;// 部分匹配超时时间 1-65536 ms
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.6", writable = true, type = "OctetString")
    private String topDigitMapDialPlanToken;// 数图体 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.7", type = "Integer32")
    private Integer topDigitMapBindCnt;// 模板绑定次数

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.6.1.4.1.8", writable = true, type = "Integer32")
    private Integer topDigitMapRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopDigitMapProfIdx() {
        return topDigitMapProfIdx;
    }

    public void setTopDigitMapProfIdx(Integer topDigitMapProfIdx) {
        this.topDigitMapProfIdx = topDigitMapProfIdx;
    }

    public String getTopDigitMapProfName() {
        return topDigitMapProfName;
    }

    public void setTopDigitMapProfName(String topDigitMapProfName) {
        this.topDigitMapProfName = topDigitMapProfName;
    }

    public Integer getTopDigitMapCirtDialTime() {
        return topDigitMapCirtDialTime;
    }

    public void setTopDigitMapCirtDialTime(Integer topDigitMapCirtDialTime) {
        this.topDigitMapCirtDialTime = topDigitMapCirtDialTime;
    }

    public Integer getTopDigitMapPartDialTime() {
        return topDigitMapPartDialTime;
    }

    public void setTopDigitMapPartDialTime(Integer topDigitMapPartDialTime) {
        this.topDigitMapPartDialTime = topDigitMapPartDialTime;
    }

    public String getTopDigitMapDialPlanToken() {
        return topDigitMapDialPlanToken;
    }

    public void setTopDigitMapDialPlanToken(String topDigitMapDialPlanToken) {
        this.topDigitMapDialPlanToken = topDigitMapDialPlanToken;
    }

    public Integer getTopDigitMapBindCnt() {
        return topDigitMapBindCnt;
    }

    public void setTopDigitMapBindCnt(Integer topDigitMapBindCnt) {
        this.topDigitMapBindCnt = topDigitMapBindCnt;
    }

    public Integer getTopDigitMapRowStatus() {
        return topDigitMapRowStatus;
    }

    public void setTopDigitMapRowStatus(Integer topDigitMapRowStatus) {
        this.topDigitMapRowStatus = topDigitMapRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopDigitMapProfInfo [entityId=");
        builder.append(entityId);
        builder.append(", topDigitMapProfIdx=");
        builder.append(topDigitMapProfIdx);
        builder.append(", topDigitMapProfName=");
        builder.append(topDigitMapProfName);
        builder.append(", topDigitMapCirtDialTime=");
        builder.append(topDigitMapCirtDialTime);
        builder.append(", topDigitMapPartDialTime=");
        builder.append(topDigitMapPartDialTime);
        builder.append(", topDigitMapDialPlanToken=");
        builder.append(topDigitMapDialPlanToken);
        builder.append(", topDigitMapBindCnt=");
        builder.append(topDigitMapBindCnt);
        builder.append(", topDigitMapRowStatus=");
        builder.append(topDigitMapRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
