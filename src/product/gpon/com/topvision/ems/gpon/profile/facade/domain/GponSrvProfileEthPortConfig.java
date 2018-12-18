/***********************************************************************
 * $Id: GponSrvProfileEthPortConfig.java,v1.0 2016年10月24日 下午6:27:33 $
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
 * @created @2016年10月24日-下午6:27:33
 *
 */
public class GponSrvProfileEthPortConfig implements AliasesSuperType {
    private static final long serialVersionUID = -2491494970829794570L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfileEthPortProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.2", index = true, type = "Integer32")
    private Integer gponSrvProfileEthPortIdIndex;// 1-24
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.3", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortMacLimited;// -1,0-255 -1:unconcern 0:unlimited
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.4", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortMtu;// 0，1518-2000 0:unconcern
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.5", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortFlowCtrl;// 0:unconcern 1:enable 2:disable
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.6", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortInTrafficProfileId;// 0-1024 0:unconcern
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.7", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortOutTrafficProfileId;// 0-1024 0:unconcern
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.8", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortMcMaxGroup;// -1,0-255 -1:unconcern 0:unlimited
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.9", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortDnMcMode;// 0:unconcern 1:keep 2:strip 3:translate
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.10", writable = true, type = "Integer32")
    private Integer gponSrvProfileEthPortDnMcTCI;// 0:unconcern 0-15bit:vlan 16-31bit:pri
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.4.1.11", writable = true, type =
    // "OctetString")
    private String gponSrvProfileEthPortMcMvlanList;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfileEthPortProfileIndex() {
        return gponSrvProfileEthPortProfileIndex;
    }

    public void setGponSrvProfileEthPortProfileIndex(Integer gponSrvProfileEthPortProfileIndex) {
        this.gponSrvProfileEthPortProfileIndex = gponSrvProfileEthPortProfileIndex;
    }

    public Integer getGponSrvProfileEthPortIdIndex() {
        return gponSrvProfileEthPortIdIndex;
    }

    public void setGponSrvProfileEthPortIdIndex(Integer gponSrvProfileEthPortIdIndex) {
        this.gponSrvProfileEthPortIdIndex = gponSrvProfileEthPortIdIndex;
    }

    public Integer getGponSrvProfileEthPortMacLimited() {
        return gponSrvProfileEthPortMacLimited;
    }

    public void setGponSrvProfileEthPortMacLimited(Integer gponSrvProfileEthPortMacLimited) {
        this.gponSrvProfileEthPortMacLimited = gponSrvProfileEthPortMacLimited;
    }

    public Integer getGponSrvProfileEthPortMtu() {
        return gponSrvProfileEthPortMtu;
    }

    public void setGponSrvProfileEthPortMtu(Integer gponSrvProfileEthPortMtu) {
        this.gponSrvProfileEthPortMtu = gponSrvProfileEthPortMtu;
    }

    public Integer getGponSrvProfileEthPortFlowCtrl() {
        return gponSrvProfileEthPortFlowCtrl;
    }

    public void setGponSrvProfileEthPortFlowCtrl(Integer gponSrvProfileEthPortFlowCtrl) {
        this.gponSrvProfileEthPortFlowCtrl = gponSrvProfileEthPortFlowCtrl;
    }

    public Integer getGponSrvProfileEthPortInTrafficProfileId() {
        return gponSrvProfileEthPortInTrafficProfileId;
    }

    public void setGponSrvProfileEthPortInTrafficProfileId(Integer gponSrvProfileEthPortInTrafficProfileId) {
        this.gponSrvProfileEthPortInTrafficProfileId = gponSrvProfileEthPortInTrafficProfileId;
    }

    public Integer getGponSrvProfileEthPortOutTrafficProfileId() {
        return gponSrvProfileEthPortOutTrafficProfileId;
    }

    public void setGponSrvProfileEthPortOutTrafficProfileId(Integer gponSrvProfileEthPortOutTrafficProfileId) {
        this.gponSrvProfileEthPortOutTrafficProfileId = gponSrvProfileEthPortOutTrafficProfileId;
    }

    public Integer getGponSrvProfileEthPortMcMaxGroup() {
        return gponSrvProfileEthPortMcMaxGroup;
    }

    public void setGponSrvProfileEthPortMcMaxGroup(Integer gponSrvProfileEthPortMcMaxGroup) {
        this.gponSrvProfileEthPortMcMaxGroup = gponSrvProfileEthPortMcMaxGroup;
    }

    public Integer getGponSrvProfileEthPortDnMcMode() {
        return gponSrvProfileEthPortDnMcMode;
    }

    public void setGponSrvProfileEthPortDnMcMode(Integer gponSrvProfileEthPortDnMcMode) {
        this.gponSrvProfileEthPortDnMcMode = gponSrvProfileEthPortDnMcMode;
    }

    public Integer getGponSrvProfileEthPortDnMcTCI() {
        return gponSrvProfileEthPortDnMcTCI;
    }

    public void setGponSrvProfileEthPortDnMcTCI(Integer gponSrvProfileEthPortDnMcTCI) {
        this.gponSrvProfileEthPortDnMcTCI = gponSrvProfileEthPortDnMcTCI;
    }

    public String getGponSrvProfileEthPortMcMvlanList() {
        return gponSrvProfileEthPortMcMvlanList;
    }

    public void setGponSrvProfileEthPortMcMvlanList(String gponSrvProfileEthPortMcMvlanList) {
        this.gponSrvProfileEthPortMcMvlanList = gponSrvProfileEthPortMcMvlanList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfileEthPortConfig [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfileEthPortProfileIndex=");
        builder.append(gponSrvProfileEthPortProfileIndex);
        builder.append(", gponSrvProfileEthPortIdIndex=");
        builder.append(gponSrvProfileEthPortIdIndex);
        builder.append(", gponSrvProfileEthPortMacLimited=");
        builder.append(gponSrvProfileEthPortMacLimited);
        builder.append(", gponSrvProfileEthPortMtu=");
        builder.append(gponSrvProfileEthPortMtu);
        builder.append(", gponSrvProfileEthPortFlowCtrl=");
        builder.append(gponSrvProfileEthPortFlowCtrl);
        builder.append(", gponSrvProfileEthPortInTrafficProfileId=");
        builder.append(gponSrvProfileEthPortInTrafficProfileId);
        builder.append(", gponSrvProfileEthPortOutTrafficProfileId=");
        builder.append(gponSrvProfileEthPortOutTrafficProfileId);
        builder.append(", gponSrvProfileEthPortMcMaxGroup=");
        builder.append(gponSrvProfileEthPortMcMaxGroup);
        builder.append(", gponSrvProfileEthPortDnMcMode=");
        builder.append(gponSrvProfileEthPortDnMcMode);
        builder.append(", gponSrvProfileEthPortDnMcTCI=");
        builder.append(gponSrvProfileEthPortDnMcTCI);
        builder.append(", gponSrvProfileEthPortMcMvlanList=");
        builder.append(gponSrvProfileEthPortMcMvlanList);
        builder.append("]");
        return builder.toString();
    }

}
