/***********************************************************************
 * $Id: GponSrvProfilePortNumProfile.java,v1.0 2016年10月24日 下午6:24:50 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年10月24日-下午6:24:50
 *
 */
public class GponSrvProfilePortNumProfile implements AliasesSuperType {
    private static final long serialVersionUID = 8442721363616607332L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.3.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfilePortNumProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.3.1.2", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthNum;// -1:adaptive 0-24:端口数
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.3.1.3", writable = true, type = "Integer32")
    private Integer gponSrvProfileCatvNum;// -1:adaptive 0-2:端口数
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.3.1.4", writable = true, type = "Integer32")
    private Integer gponSrvProfileWlanNum;// -1:adaptive 0-2:端口数
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.3.1.5", writable = true, type = "Integer32")
    private Integer gponSrvProfileVeipNum;// -1:adaptive 0-8:端口数

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfilePortNumProfileIndex() {
        return gponSrvProfilePortNumProfileIndex;
    }

    public void setGponSrvProfilePortNumProfileIndex(Integer gponSrvProfilePortNumProfileIndex) {
        this.gponSrvProfilePortNumProfileIndex = gponSrvProfilePortNumProfileIndex;
    }

    public Integer getGponSrvProfileEthNum() {
        return gponSrvProfileEthNum;
    }

    public void setGponSrvProfileEthNum(Integer gponSrvProfileEthNum) {
        this.gponSrvProfileEthNum = gponSrvProfileEthNum;
    }

    public Integer getGponSrvProfileCatvNum() {
        return gponSrvProfileCatvNum;
    }

    public void setGponSrvProfileCatvNum(Integer gponSrvProfileCatvNum) {
        this.gponSrvProfileCatvNum = gponSrvProfileCatvNum;
    }

    public Integer getGponSrvProfileWlanNum() {
        return gponSrvProfileWlanNum;
    }

    public void setGponSrvProfileWlanNum(Integer gponSrvProfileWlanNum) {
        this.gponSrvProfileWlanNum = gponSrvProfileWlanNum;
    }

    public Integer getGponSrvProfileVeipNum() {
        return gponSrvProfileVeipNum;
    }

    public void setGponSrvProfileVeipNum(Integer gponSrvProfileVeipNum) {
        this.gponSrvProfileVeipNum = gponSrvProfileVeipNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfilePortNumProfile [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfilePortNumProfileIndex=");
        builder.append(gponSrvProfilePortNumProfileIndex);
        builder.append(", gponSrvProfileEthNum=");
        builder.append(gponSrvProfileEthNum);
        builder.append(", gponSrvProfileCatvNum=");
        builder.append(gponSrvProfileCatvNum);
        builder.append(", gponSrvProfileWlanNum=");
        builder.append(gponSrvProfileWlanNum);
        builder.append(", gponSrvProfileVeipNum=");
        builder.append(gponSrvProfileVeipNum);
        builder.append("]");
        return builder.toString();
    }

}
