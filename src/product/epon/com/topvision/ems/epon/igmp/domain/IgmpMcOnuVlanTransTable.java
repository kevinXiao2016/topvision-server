/***********************************************************************
 * $ IgmpMcOnuVlanTransTable.java,v1.0 2011-11-23 10:47:34 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-10:47:34
 */
@TableProperty(tables = { "default" })
public class IgmpMcOnuVlanTransTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * translation index INTEGER (1..64)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.2.1.1", index = true)
    private Integer topMcOnuVlanTransIndex;
    /**
     * old vlan id INTEGER (1..4094)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.2.1.2", writable = true, type = "OctetString")
    private String topMcOnuVlanTransOldVid;
    private List<Integer> topMcOnuVlanTransOldVidList;
    /**
     * vid after translation INTEGER (1..4094)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.2.1.3", writable = true, type = "OctetString")
    private String topMcOnuVlanTransNewVid;
    private List<Integer> topMcOnuVlanTransNewVidList;
    /**
     * add/del/set
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.2.1.4", writable = true, type = "Integer32")
    private Integer topMcOnuVlanTransRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopMcOnuVlanTransIndex() {
        return topMcOnuVlanTransIndex;
    }

    public void setTopMcOnuVlanTransIndex(Integer topMcOnuVlanTransIndex) {
        this.topMcOnuVlanTransIndex = topMcOnuVlanTransIndex;
    }

    /**
     * @return the topMcOnuVlanTransOldVid
     */
    public String getTopMcOnuVlanTransOldVid() {
        return topMcOnuVlanTransOldVid;
    }

    /**
     * @param topMcOnuVlanTransOldVid
     *            the topMcOnuVlanTransOldVid to set
     */
    public void setTopMcOnuVlanTransOldVid(String topMcOnuVlanTransOldVid) {
        this.topMcOnuVlanTransOldVid = topMcOnuVlanTransOldVid;
        topMcOnuVlanTransOldVidList = EponUtil.getTwiceBitValueFromOcterString(topMcOnuVlanTransOldVid);
    }

    /**
     * @return the topMcOnuVlanTransOldVidList
     */
    public List<Integer> getTopMcOnuVlanTransOldVidList() {
        return topMcOnuVlanTransOldVidList;
    }

    /**
     * @param topMcOnuVlanTransOldVidList
     *            the topMcOnuVlanTransOldVidList to set
     */
    public void setTopMcOnuVlanTransOldVidList(List<Integer> topMcOnuVlanTransOldVidList) {
        this.topMcOnuVlanTransOldVidList = topMcOnuVlanTransOldVidList;
        topMcOnuVlanTransOldVid = EponUtil.getTwiceByteBitMapFromVlanId(topMcOnuVlanTransOldVidList);
    }

    /**
     * @return the topMcOnuVlanTransNewVid
     */
    public String getTopMcOnuVlanTransNewVid() {
        return topMcOnuVlanTransNewVid;
    }

    /**
     * @param topMcOnuVlanTransNewVid
     *            the topMcOnuVlanTransNewVid to set
     */
    public void setTopMcOnuVlanTransNewVid(String topMcOnuVlanTransNewVid) {
        this.topMcOnuVlanTransNewVid = topMcOnuVlanTransNewVid;
        topMcOnuVlanTransNewVidList = EponUtil.getTwiceBitValueFromOcterString(topMcOnuVlanTransNewVid);
    }

    /**
     * @return the topMcOnuVlanTransNewVidList
     */
    public List<Integer> getTopMcOnuVlanTransNewVidList() {
        return topMcOnuVlanTransNewVidList;
    }

    /**
     * @param topMcOnuVlanTransNewVidList
     *            the topMcOnuVlanTransNewVidList to set
     */
    public void setTopMcOnuVlanTransNewVidList(List<Integer> topMcOnuVlanTransNewVidList) {
        this.topMcOnuVlanTransNewVidList = topMcOnuVlanTransNewVidList;
        topMcOnuVlanTransNewVid = EponUtil.getTwiceByteBitMapFromVlanId(topMcOnuVlanTransNewVidList);
    }

    public Integer getTopMcOnuVlanTransRowStatus() {
        return topMcOnuVlanTransRowStatus;
    }

    public void setTopMcOnuVlanTransRowStatus(Integer topMcOnuVlanTransRowStatus) {
        this.topMcOnuVlanTransRowStatus = topMcOnuVlanTransRowStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpMcOnuVlanTransTable");
        sb.append("{entityId=").append(entityId);
        sb.append(", topMcOnuVlanTransIndex=").append(topMcOnuVlanTransIndex);
        sb.append(", topMcOnuVlanTransOldVid=").append(topMcOnuVlanTransOldVid);
        sb.append(", topMcOnuVlanTransNewVid=").append(topMcOnuVlanTransNewVid);
        sb.append(", topMcOnuVlanTransRowStatus=").append(topMcOnuVlanTransRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
