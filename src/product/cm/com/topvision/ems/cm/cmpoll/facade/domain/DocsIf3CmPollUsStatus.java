/***********************************************************************
 * $Id: DocsIf3CmPollUsStatus.java,v1.0 2015年3月21日 上午9:36:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2015年3月21日-上午9:36:01
 * 
 */
@Alias("docsIf3CmPollUsStatus")
public class DocsIf3CmPollUsStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7491397734389355817L;
    private Long cmcId;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.3.1.1", index = true)
    private Long cmRegStatusId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.1", index = true)
    private Long cmUsStatusChIfIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.3")
    private Long cmUsStatusRxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.4")
    private Long cmUsStatusSignalNoise;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmUsStatusChIfIndex() {
        return cmUsStatusChIfIndex;
    }

    public void setCmUsStatusChIfIndex(Long cmUsStatusChIfIndex) {
        this.cmUsStatusChIfIndex = cmUsStatusChIfIndex;
    }

    public Long getCmUsStatusSignalNoise() {
        return cmUsStatusSignalNoise;
    }

    public void setCmUsStatusSignalNoise(Long cmUsStatusSignalNoise) {
        this.cmUsStatusSignalNoise = cmUsStatusSignalNoise;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmRegStatusId() {
        return cmRegStatusId;
    }

    public void setCmRegStatusId(Long cmRegStatusId) {
        this.cmRegStatusId = cmRegStatusId;
    }

    public Long getCmUsStatusRxPower() {
        return cmUsStatusRxPower;
    }

    public void setCmUsStatusRxPower(Long cmUsStatusRxPower) {
        this.cmUsStatusRxPower = cmUsStatusRxPower;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DocsIf3CmPollUsStatus [cmcId=");
        builder.append(cmcId);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", cmRegStatusId=");
        builder.append(cmRegStatusId);
        builder.append(", cmUsStatusChIfIndex=");
        builder.append(cmUsStatusChIfIndex);
        builder.append(", cmUsStatusRxPower=");
        builder.append(cmUsStatusRxPower);
        builder.append(", cmUsStatusSignalNoise=");
        builder.append(cmUsStatusSignalNoise);
        builder.append("]");
        return builder.toString();
    }

}
