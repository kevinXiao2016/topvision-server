/***********************************************************************
 * $Id: GponSrvProfilePortVlanAggregation.java,v1.0 2016年10月25日 上午8:36:20 $
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
 * @created @2016年10月25日-上午8:36:20
 *
 */
public class GponSrvProfilePortVlanAggregation implements AliasesSuperType {
    private static final long serialVersionUID = 1833233018647169743L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.3.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanAggrProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.3.1.2", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanAggrPortTypeIndex;// 0:eth,1:wlan,2:catv
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.3.1.3", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanAggrPortIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.3.1.4", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanAggrVlanIndex;// 1-4094
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.3.1.5", writable = true, type = "OctetString")
    private String gponSrvProfilePortVlanAggrVlanList;// 2-16字节 最大支持8个vlan配置，每2个bytes表示一个vlan，覆盖式下发
    private String aggrVlanString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.3.1.6", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanAggrRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfilePortVlanAggrProfileIndex() {
        return gponSrvProfilePortVlanAggrProfileIndex;
    }

    public void setGponSrvProfilePortVlanAggrProfileIndex(Integer gponSrvProfilePortVlanAggrProfileIndex) {
        this.gponSrvProfilePortVlanAggrProfileIndex = gponSrvProfilePortVlanAggrProfileIndex;
    }

    public Integer getGponSrvProfilePortVlanAggrPortTypeIndex() {
        return gponSrvProfilePortVlanAggrPortTypeIndex;
    }

    public void setGponSrvProfilePortVlanAggrPortTypeIndex(Integer gponSrvProfilePortVlanAggrPortTypeIndex) {
        this.gponSrvProfilePortVlanAggrPortTypeIndex = gponSrvProfilePortVlanAggrPortTypeIndex;
    }

    public Integer getGponSrvProfilePortVlanAggrPortIdIndex() {
        return gponSrvProfilePortVlanAggrPortIdIndex;
    }

    public void setGponSrvProfilePortVlanAggrPortIdIndex(Integer gponSrvProfilePortVlanAggrPortIdIndex) {
        this.gponSrvProfilePortVlanAggrPortIdIndex = gponSrvProfilePortVlanAggrPortIdIndex;
    }

    public Integer getGponSrvProfilePortVlanAggrVlanIndex() {
        return gponSrvProfilePortVlanAggrVlanIndex;
    }

    public void setGponSrvProfilePortVlanAggrVlanIndex(Integer gponSrvProfilePortVlanAggrVlanIndex) {
        this.gponSrvProfilePortVlanAggrVlanIndex = gponSrvProfilePortVlanAggrVlanIndex;
    }

    public String getGponSrvProfilePortVlanAggrVlanList() {
        return gponSrvProfilePortVlanAggrVlanList;
    }

    public void setGponSrvProfilePortVlanAggrVlanList(String gponSrvProfilePortVlanAggrVlanList) {
        this.gponSrvProfilePortVlanAggrVlanList = gponSrvProfilePortVlanAggrVlanList;
        if (gponSrvProfilePortVlanAggrVlanList != null) {
            aggrVlanString = EponUtil.getGponSrvProfilePortVlanAggrVlanListString(gponSrvProfilePortVlanAggrVlanList);
        }
    }

    public Integer getGponSrvProfilePortVlanAggrRowStatus() {
        return gponSrvProfilePortVlanAggrRowStatus;
    }

    public void setGponSrvProfilePortVlanAggrRowStatus(Integer gponSrvProfilePortVlanAggrRowStatus) {
        this.gponSrvProfilePortVlanAggrRowStatus = gponSrvProfilePortVlanAggrRowStatus;
    }

    /**
     * @return the aggrVlanString
     */
    public String getAggrVlanString() {
        return aggrVlanString;
    }

    /**
     * @param aggrVlanString
     *            the aggrVlanString to set
     */
    public void setAggrVlanString(String aggrVlanString) {
        this.aggrVlanString = aggrVlanString;
        if (aggrVlanString != null) {
            gponSrvProfilePortVlanAggrVlanList = EponUtil.getGponSrvProfilePortVlanAggrVlanListBytes(aggrVlanString);
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
        builder.append("GponSrvProfilePortVlanAggregation [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfilePortVlanAggrProfileIndex=");
        builder.append(gponSrvProfilePortVlanAggrProfileIndex);
        builder.append(", gponSrvProfilePortVlanAggrPortTypeIndex=");
        builder.append(gponSrvProfilePortVlanAggrPortTypeIndex);
        builder.append(", gponSrvProfilePortVlanAggrPortIdIndex=");
        builder.append(gponSrvProfilePortVlanAggrPortIdIndex);
        builder.append(", gponSrvProfilePortVlanAggrVlanIndex=");
        builder.append(gponSrvProfilePortVlanAggrVlanIndex);
        builder.append(", gponSrvProfilePortVlanAggrVlanList=");
        builder.append(gponSrvProfilePortVlanAggrVlanList);
        builder.append(", gponSrvProfilePortVlanAggrRowStatus=");
        builder.append(gponSrvProfilePortVlanAggrRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
