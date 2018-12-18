/***********************************************************************
 * $Id: TopGponSrvProfile.java,v1.0 2017年5月4日 上午10:54:01 $
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
 * @created @2017年5月4日-上午10:54:01
 *
 */
public class TopGponSrvProfile implements AliasesSuperType {
    private static final long serialVersionUID = 4918469366809519661L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.1.1.1", index = true, type = "Integer32")
    private Integer topGponSrvProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.1.1.2", writable = true, type = "Integer32")
    private Integer topGponSrvProfilePotsNum;// 0-2 pots端口数 0表示没有pots口

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopGponSrvProfileIndex() {
        return topGponSrvProfileIndex;
    }

    public void setTopGponSrvProfileIndex(Integer topGponSrvProfileIndex) {
        this.topGponSrvProfileIndex = topGponSrvProfileIndex;
    }

    public Integer getTopGponSrvProfilePotsNum() {
        return topGponSrvProfilePotsNum;
    }

    public void setTopGponSrvProfilePotsNum(Integer topGponSrvProfilePotsNum) {
        this.topGponSrvProfilePotsNum = topGponSrvProfilePotsNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopGponSrvProfile [entityId=");
        builder.append(entityId);
        builder.append(", topGponSrvProfileIndex=");
        builder.append(topGponSrvProfileIndex);
        builder.append(", topGponSrvProfilePotsNum=");
        builder.append(topGponSrvProfilePotsNum);
        builder.append("]");
        return builder.toString();
    }

}
