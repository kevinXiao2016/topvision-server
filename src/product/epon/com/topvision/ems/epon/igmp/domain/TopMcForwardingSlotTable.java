/***********************************************************************
 * $Id: TopMcForwardingSlotTable.java,v1.0 2012-4-16 下午02:37:30 $
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
 * @created @2012-4-16-下午02:37:30
 * 
 */
@TableProperty(tables = { "default" })
public class TopMcForwardingSlotTable implements AliasesSuperType {

    private static final long serialVersionUID = 7473641922647480726L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.1.1.1", index = true)
    private Integer topMcGroupIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.1.1.2", type = "Integer32")
    private Integer topMcSlotCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.1.1.3", type = "Integer32")
    private Integer topMcSlotList;
    private List<Integer> topMcSlotListIntegers;

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
     * @return the topMcSlotCount
     */
    public Integer getTopMcSlotCount() {
        return topMcSlotCount;
    }

    /**
     * @param topMcSlotCount
     *            the topMcSlotCount to set
     */
    public void setTopMcSlotCount(Integer topMcSlotCount) {
        this.topMcSlotCount = topMcSlotCount;
    }

    /**
     * @return the topMcSlotList
     */
    public Integer getTopMcSlotList() {
        return topMcSlotList;
    }

    /**
     * @param topMcSlotList
     *            the topMcSlotList to set
     */
    public void setTopMcSlotList(Integer topMcSlotList) {
        this.topMcSlotList = topMcSlotList;
        List<Integer> slotList = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            int k = (topMcSlotList & (int) (Math.pow(2, (double) (31 - i)))) >> (31 - i);
            if (k == 1) {
                slotList.add(32 - i);
            }
        }
        topMcSlotListIntegers = slotList;
    }

    /**
     * @return the topMcSlotListIntegers
     */
    public List<Integer> getTopMcSlotListIntegers() {
        return topMcSlotListIntegers;
    }

    /**
     * @param topMcSlotListIntegers
     *            the topMcSlotListIntegers to set
     */
    public void setTopMcSlotListIntegers(List<Integer> topMcSlotListIntegers) {
        this.topMcSlotListIntegers = topMcSlotListIntegers;
        Integer slotList = 0;
        for (Integer i : topMcSlotListIntegers) {
            slotList += (int) (Math.pow(2, (double) (i - 1)));
        }
        topMcSlotList = slotList;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopMcForwardingSlotTable [entityId=");
        builder.append(entityId);
        builder.append(", topMcGroupIdIndex=");
        builder.append(topMcGroupIdIndex);
        builder.append(", topMcSlotCount=");
        builder.append(topMcSlotCount);
        builder.append(", topMcSlotList=");
        builder.append(topMcSlotList);
        builder.append(", topMcSlotListIntegers=");
        builder.append(topMcSlotListIntegers);
        builder.append("]");
        return builder.toString();
    }

}
