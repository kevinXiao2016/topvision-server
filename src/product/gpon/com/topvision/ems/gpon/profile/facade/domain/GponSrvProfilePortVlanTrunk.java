/***********************************************************************
 * $Id: GponSrvProfilePortVlanTrunk.java,v1.0 2016年10月25日 上午8:39:40 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年10月25日-上午8:39:40
 *
 */
public class GponSrvProfilePortVlanTrunk implements AliasesSuperType {
    private static final long serialVersionUID = -4053249834834637800L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.4.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTrunkProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.4.1.2", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTrunkPortTypeIndex;// 0:eth,1:wlan,2:catv
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.4.1.3", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTrunkPortIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.4.1.4", writable = true, type = "OctetString")
    private String gponSrvProfilePortVlanTrunkVlanList;// 2-16字节 最大支持8个vlan配置，每2个bytes表示一个vlan，覆盖式下发
    private String trunkVlanString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.4.1.5", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanTrunkRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfilePortVlanTrunkProfileIndex() {
        return gponSrvProfilePortVlanTrunkProfileIndex;
    }

    public void setGponSrvProfilePortVlanTrunkProfileIndex(Integer gponSrvProfilePortVlanTrunkProfileIndex) {
        this.gponSrvProfilePortVlanTrunkProfileIndex = gponSrvProfilePortVlanTrunkProfileIndex;
    }

    public Integer getGponSrvProfilePortVlanTrunkPortTypeIndex() {
        return gponSrvProfilePortVlanTrunkPortTypeIndex;
    }

    public void setGponSrvProfilePortVlanTrunkPortTypeIndex(Integer gponSrvProfilePortVlanTrunkPortTypeIndex) {
        this.gponSrvProfilePortVlanTrunkPortTypeIndex = gponSrvProfilePortVlanTrunkPortTypeIndex;
    }

    public Integer getGponSrvProfilePortVlanTrunkPortIdIndex() {
        return gponSrvProfilePortVlanTrunkPortIdIndex;
    }

    public void setGponSrvProfilePortVlanTrunkPortIdIndex(Integer gponSrvProfilePortVlanTrunkPortIdIndex) {
        this.gponSrvProfilePortVlanTrunkPortIdIndex = gponSrvProfilePortVlanTrunkPortIdIndex;
    }

    public String getGponSrvProfilePortVlanTrunkVlanList() {
        return gponSrvProfilePortVlanTrunkVlanList;
    }

    public void setGponSrvProfilePortVlanTrunkVlanList(String gponSrvProfilePortVlanTrunkVlanList) {
        this.gponSrvProfilePortVlanTrunkVlanList = gponSrvProfilePortVlanTrunkVlanList;
        if (gponSrvProfilePortVlanTrunkVlanList != null) {
            trunkVlanString = EponUtil.getGponSrvProfilePortVlanAggrVlanListString(gponSrvProfilePortVlanTrunkVlanList);
        }
    }

    public Integer getGponSrvProfilePortVlanTrunkRowStatus() {
        return gponSrvProfilePortVlanTrunkRowStatus;
    }

    public void setGponSrvProfilePortVlanTrunkRowStatus(Integer gponSrvProfilePortVlanTrunkRowStatus) {
        this.gponSrvProfilePortVlanTrunkRowStatus = gponSrvProfilePortVlanTrunkRowStatus;
    }

    /**
     * @return the trunkVlanString
     */
    public String getTrunkVlanString() {
        return trunkVlanString;
    }

    /**
     * @param trunkVlanString
     *            the trunkVlanString to set
     */
    public void setTrunkVlanString(String trunkVlanString) {
        this.trunkVlanString = trunkVlanString;
        if (trunkVlanString != null) {
            gponSrvProfilePortVlanTrunkVlanList = EponUtil.getGponSrvProfilePortVlanAggrVlanListBytes(trunkVlanString);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfilePortVlanTrunk [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfilePortVlanTrunkProfileIndex=");
        builder.append(gponSrvProfilePortVlanTrunkProfileIndex);
        builder.append(", gponSrvProfilePortVlanTrunkPortTypeIndex=");
        builder.append(gponSrvProfilePortVlanTrunkPortTypeIndex);
        builder.append(", gponSrvProfilePortVlanTrunkPortIdIndex=");
        builder.append(gponSrvProfilePortVlanTrunkPortIdIndex);
        builder.append(", gponSrvProfilePortVlanTrunkVlanList=");
        builder.append(gponSrvProfilePortVlanTrunkVlanList);
        builder.append(", trunkVlanString=");
        builder.append(trunkVlanString);
        builder.append(", gponSrvProfilePortVlanTrunkRowStatus=");
        builder.append(gponSrvProfilePortVlanTrunkRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
