/***********************************************************************
 * $Id: CmcOpReceiverChannelNum.java,v1.0 2013-12-18 上午11:48:42 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.facade.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-12-18-上午11:48:42
 * 
 */
@Alias("cmcOpReceiverChannelNum")
public class CmcOpReceiverChannelNum implements AliasesSuperType {
    private static final long serialVersionUID = 26570056781268706L;
    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.20.1.1", index = true)
    private Integer channelNumIndex;
    /**
     * 载波频道数目，当取值为-1时表示光机不支持该参数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.20.1.2")
    private Integer channelNum;
    private Timestamp collectTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getChannelNumIndex() {
        return channelNumIndex;
    }

    public void setChannelNumIndex(Integer channelNumIndex) {
        this.channelNumIndex = channelNumIndex;
    }

    public Integer getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

}
