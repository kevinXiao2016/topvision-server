/***********************************************************************
 * $Id: TopMcForwardingPortTable.java,v1.0 2012-4-16 下午02:40:45 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-4-16-下午02:40:45
 * 
 */
@TableProperty(tables = { "default" })
public class TopMcForwardingPortTable implements AliasesSuperType {

    private static final long serialVersionUID = 396386872042981997L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.2.1.1", index = true)
    private Integer topMcGroupIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.2.1.2", index = true)
    private Integer topMcSlotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.2.1.3", type = "Integer32")
    private Integer topMcPortCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.2.1.4", type = "Integer32")
    private Integer topMcPortList;
    private List<Integer> topMcPortListIntegers;

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

    /**
     * @return the topMcGroupIdIndex
     */
    public Integer getTopMcGroupIdIndex() {
        return topMcGroupIdIndex;
    }

    /**
     * @param topMcGroupIdIndex
     *            the topMcGroupIdIndex to set
     */
    public void setTopMcGroupIdIndex(Integer topMcGroupIdIndex) {
        this.topMcGroupIdIndex = topMcGroupIdIndex;
    }

    /**
     * @return the topMcSlotIndex
     */
    public Integer getTopMcSlotIndex() {
        return topMcSlotIndex;
    }

    /**
     * @param topMcSlotIndex
     *            the topMcSlotIndex to set
     */
    public void setTopMcSlotIndex(Integer topMcSlotIndex) {
        this.topMcSlotIndex = topMcSlotIndex;
    }

    /**
     * @return the topMcPortCount
     */
    public Integer getTopMcPortCount() {
        return topMcPortCount;
    }

    /**
     * @param topMcPortCount
     *            the topMcPortCount to set
     */
    public void setTopMcPortCount(Integer topMcPortCount) {
        this.topMcPortCount = topMcPortCount;
    }

    /**
     * @return the topMcPortList
     */
    public Integer getTopMcPortList() {
        return topMcPortList;
    }

    /**
     * @param topMcPortList
     *            the topMcPortList to set
     */
    public void setTopMcPortList(Integer topMcPortList) {
        this.topMcPortList = topMcPortList;
        List<Integer> portList = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            int k = (topMcPortList & (int) (Math.pow(2, (double) (31 - i)))) >> (31 - i);
            if (k == 1) {
                portList.add(32 - i);
            }
        }
        topMcPortListIntegers = portList;
    }

    /**
     * @return the topMcPortListIntegers
     */
    public List<Integer> getTopMcPortListIntegers() {
        return topMcPortListIntegers;
    }

    /**
     * @param topMcPortListIntegers
     *            the topMcPortListIntegers to set
     */
    public void setTopMcPortListIntegers(List<Integer> topMcPortListIntegers) {
        this.topMcPortListIntegers = topMcPortListIntegers;
        Integer portList = 0;
        for (Integer i : topMcPortListIntegers) {
            portList += (int) (Math.pow(2, (double) (i - 1)));
        }
        topMcPortList = portList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopMcForwardingPortTable [entityId=");
        builder.append(entityId);
        builder.append(", topMcGroupIdIndex=");
        builder.append(topMcGroupIdIndex);
        builder.append(", topMcSlotIndex=");
        builder.append(topMcSlotIndex);
        builder.append(", topMcPortCount=");
        builder.append(topMcPortCount);
        builder.append(", topMcPortList=");
        builder.append(topMcPortList);
        builder.append(", topMcPortListIntegers=");
        builder.append(topMcPortListIntegers);
        builder.append("]");
        return builder.toString();
    }

}
