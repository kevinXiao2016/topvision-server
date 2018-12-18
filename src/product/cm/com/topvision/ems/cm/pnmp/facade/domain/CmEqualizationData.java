/***********************************************************************
 * CmtsCm.java,v1.0 17-8-9 下午7:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author jay
 * @created 17-8-9 下午7:05
 */
@Alias("cmEqualizationData")
public class CmEqualizationData implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;

    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Integer ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.2.2.1.17")
    private String equalizationData;

    public Integer getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Integer ifIndex) {
        this.ifIndex = ifIndex;
    }

    public String getEqualizationData() {
        return equalizationData;
    }

    public void setEqualizationData(String equalizationData) {
        this.equalizationData = equalizationData;
    }
}
