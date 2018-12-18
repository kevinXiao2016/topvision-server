/***********************************************************************
 * $Id: TopOltVlanVifSubIpTable.java,v1.0 2012-4-17 下午07:03:22 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-4-17-下午07:03:22
 * 
 */
@TableProperty(tables = { "default" })
public class TopOltVlanVifSubIpTable implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -8067648799973893008L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.2.1.1", index = true)
    private Integer topOltVifSubIpVlanIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.2.1.2", index = true)
    private Integer topOltVifSubIpSeqIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.2.1.3", writable = true, type = "IpAddress")
    private String topOltVifSubIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.2.1.4", writable = true, type = "IpAddress")
    private String topOltVifSubIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.4.2.1.5", writable = true, type = "Integer32")
    private Integer topOltVifSubIpRowStatus;
    //子IP最多可以添加20个,可以使用的索引集合
    public static final Integer[] indexs = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

    /**
     * @return the topOltVifSubIpVlanIdx
     */
    public Integer getTopOltVifSubIpVlanIdx() {
        return topOltVifSubIpVlanIdx;
    }

    /**
     * @param topOltVifSubIpVlanIdx
     *            the topOltVifSubIpVlanIdx to set
     */
    public void setTopOltVifSubIpVlanIdx(Integer topOltVifSubIpVlanIdx) {
        this.topOltVifSubIpVlanIdx = topOltVifSubIpVlanIdx;
    }

    /**
     * @return the topOltVifSubIpSeqIdx
     */
    public Integer getTopOltVifSubIpSeqIdx() {
        return topOltVifSubIpSeqIdx;
    }

    /**
     * @param topOltVifSubIpSeqIdx
     *            the topOltVifSubIpSeqIdx to set
     */
    public void setTopOltVifSubIpSeqIdx(Integer topOltVifSubIpSeqIdx) {
        this.topOltVifSubIpSeqIdx = topOltVifSubIpSeqIdx;
    }

    /**
     * @return the topOltVifSubIpAddr
     */
    public String getTopOltVifSubIpAddr() {
        return topOltVifSubIpAddr;
    }

    /**
     * @param topOltVifSubIpAddr
     *            the topOltVifSubIpAddr to set
     */
    public void setTopOltVifSubIpAddr(String topOltVifSubIpAddr) {
        this.topOltVifSubIpAddr = topOltVifSubIpAddr;
    }

    /**
     * @return the topOltVifSubIpMask
     */
    public String getTopOltVifSubIpMask() {
        return topOltVifSubIpMask;
    }

    /**
     * @param topOltVifSubIpMask
     *            the topOltVifSubIpMask to set
     */
    public void setTopOltVifSubIpMask(String topOltVifSubIpMask) {
        this.topOltVifSubIpMask = topOltVifSubIpMask;
    }

    /**
     * @return the topOltVifSubIpRowStatus
     */
    public Integer getTopOltVifSubIpRowStatus() {
        return topOltVifSubIpRowStatus;
    }

    /**
     * @param topOltVifSubIpRowStatus
     *            the topOltVifSubIpRowStatus to set
     */
    public void setTopOltVifSubIpRowStatus(Integer topOltVifSubIpRowStatus) {
        this.topOltVifSubIpRowStatus = topOltVifSubIpRowStatus;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public static Integer[] getIndexs() {
        return indexs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltVlanVifSubIpTable [entityId=");
        builder.append(entityId);
        builder.append(", topOltVifSubIpVlanIdx=");
        builder.append(topOltVifSubIpVlanIdx);
        builder.append(", topOltVifSubIpSeqIdx=");
        builder.append(topOltVifSubIpSeqIdx);
        builder.append(", topOltVifSubIpAddr=");
        builder.append(topOltVifSubIpAddr);
        builder.append(", topOltVifSubIpMask=");
        builder.append(topOltVifSubIpMask);
        builder.append(", topOltVifSubIpRowStatus=");
        builder.append(topOltVifSubIpRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
