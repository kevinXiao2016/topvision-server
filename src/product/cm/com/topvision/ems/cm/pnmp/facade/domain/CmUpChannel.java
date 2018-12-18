/***********************************************************************
 * CmtsCm.java,v1.0 17-8-9 下午7:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author jay
 * @created 17-8-9 下午7:05
 */
@Alias("cmUpChannel")
public class CmUpChannel implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;

    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Integer ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.1")
    private Integer upChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.2")
    private Long upChannelFreq;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.3")
    private Long upChannelWidth;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.2.2.1.3")
    private Integer upTxPower;//2.0上行发射电频

    public Integer getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Integer ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getUpChannelWidth() {
        return upChannelWidth;
    }

    public void setUpChannelWidth(Long upChannelWidth) {
        this.upChannelWidth = upChannelWidth;
    }

    public Integer getUpTxPower() {
        return upTxPower;
    }

    public void setUpTxPower(Integer upTxPower) {
        this.upTxPower = upTxPower;
    }

    public Integer getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Integer upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Long getUpChannelFreq() {
        return upChannelFreq;
    }

    public void setUpChannelFreq(Long upChannelFreq) {
        this.upChannelFreq = upChannelFreq;
    }
}
