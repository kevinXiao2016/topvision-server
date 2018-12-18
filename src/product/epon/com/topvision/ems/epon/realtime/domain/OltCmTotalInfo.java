/***********************************************************************
 * $Id: OltCmTotalInfo.java,v1.0 2014-7-14 上午11:01:04 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-7-14-上午11:01:04
 *
 */
public class OltCmTotalInfo implements AliasesSuperType {
    private static final long serialVersionUID = -8092632453305521815L;

    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.1.0", type = "Integer32")
    private Integer totalNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.2.0", type = "Integer32")
    private Integer onlineNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.5.0", type = "Integer32")
    private Integer offlineNum;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(Integer offlineNum) {
        this.offlineNum = offlineNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltCmTotalInfo [entityId=");
        builder.append(entityId);
        builder.append(", totalNum=");
        builder.append(totalNum);
        builder.append(", onlineNum=");
        builder.append(onlineNum);
        builder.append(", offlineNum=");
        builder.append(offlineNum);
        builder.append("]");
        return builder.toString();
    }

}
