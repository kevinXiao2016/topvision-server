/***********************************************************************
 * CorrelationGroup.java,v1.0 17-8-29 下午3:02 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.ems.cm.cmpoll.facade.domain.Complex;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author jay
 * @created 17-8-29 下午3:02
 */
@Alias("correlationGroup")
public class CorrelationGroup implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;

    private Long groupId = null;
    private Long cmcId;
    private Integer upChannelId;
    private String cmMac;
    private String orginalValue;
    private Complex[] freqResponse;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getOrginalValue() {
        return orginalValue;
    }

    public void setOrginalValue(String orginalValue) {
        this.orginalValue = orginalValue;
    }

    public Integer getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Integer upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Complex[] getFreqResponse() {
        return freqResponse;
    }

    public void setFreqResponse(Complex[] freqResponse) {
        this.freqResponse = freqResponse;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "CorrelationGroup{" +
                "groupId=" + groupId +
                ", cmcId=" + cmcId +
                ", upChannelId=" + upChannelId +
                ", cmMac='" + cmMac + '\'' +
                ", orginalValue='" + orginalValue + '\'' +
                ", freqResponse=" + Arrays.toString(freqResponse) +
                '}';
    }
}
