/***********************************************************************
 * $Id: TopMcForwardingOnuTable.java,v1.0 2012-4-16 下午03:00:32 $
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
 * @created @2012-4-16-下午03:00:32
 * 
 */
@TableProperty(tables = { "default" })
public class TopMcForwardingOnuTable implements AliasesSuperType {

    private static final long serialVersionUID = 3457083713135750467L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.3.1.1", index = true)
    private Integer topMcGroupIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.3.1.2", index = true)
    private Integer topMcSlotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.3.1.3", index = true)
    private Integer topMcPortIndex;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.3.1.4", type = "Integer32")
    private Integer topMcOnuCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.4.3.1.5", type = "OctetString")
    private String topMcOnuList;
    /**
     * 结构[onuNo, 1-8uniNo, 9-16uniNo, 17-24uniNo, 25-32uniNo ,......] uniNo<0-255> 4,7,8 ->
     * 2^(8-4)+2^(8-7)+2^(8-8) 0表示没有
     */
    private List<Integer> topMcOnuListUniIndexList;

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
     * @return the topMcPortIndex
     */
    public Integer getTopMcPortIndex() {
        return topMcPortIndex;
    }

    /**
     * @param topMcPortIndex
     *            the topMcPortIndex to set
     */
    public void setTopMcPortIndex(Integer topMcPortIndex) {
        this.topMcPortIndex = topMcPortIndex;
    }

    /**
     * @return the ponIndex
     */
    public Long getPonIndex() {
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    /**
     * @return the topMcOnuCount
     */
    public Integer getTopMcOnuCount() {
        return topMcOnuCount;
    }

    /**
     * @param topMcOnuCount
     *            the topMcOnuCount to set
     */
    public void setTopMcOnuCount(Integer topMcOnuCount) {
        this.topMcOnuCount = topMcOnuCount;
    }

    /**
     * @return the topMcOnuList
     */
    public String getTopMcOnuList() {
        return topMcOnuList;
    }

    /**
     * @param topMcOnuList
     *            the topMcOnuList to set
     */
    public void setTopMcOnuList(String topMcOnuList) {
        this.topMcOnuList = topMcOnuList;
        if (topMcOnuList != null) {
            topMcOnuListUniIndexList = new ArrayList<Integer>();
            if (topMcOnuList.split(":").length == 640) {
                for (String s : topMcOnuList.split(":")) {
                    topMcOnuListUniIndexList.add(Integer.parseInt(s, 16));
                }
            }
        }
    }

    /**
     * @return the topMcOnuListUniIndexList
     */
    public List<Integer> getTopMcOnuListUniIndexList() {
        return topMcOnuListUniIndexList;
    }

    /**
     * @param topMcOnuListUniIndexList
     *            the topMcOnuListUniIndexList to set
     */
    public void setTopMcOnuListUniIndexList(List<Integer> topMcOnuListUniIndexList) {
        this.topMcOnuListUniIndexList = topMcOnuListUniIndexList;
        if (topMcOnuListUniIndexList.size() > 0) {
            topMcOnuList = changeListIntegerToString(topMcOnuListUniIndexList);
        }
    }

    private String changeListIntegerToString(List<Integer> list) {
        StringBuilder r = new StringBuilder();
        for (Integer i : list) {
            if (Integer.toHexString(i).length() == 1) {
                r.append(String.format("0%s", Integer.toHexString(i))).append(":");
            } else if (Integer.toHexString(i).length() == 2) {
                r.append(String.format("%s", Integer.toHexString(i))).append(":");
            }
        }
        return r.substring(0, r.length() - 1);
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
        builder.append("TopMcForwardingOnuTable [entityId=");
        builder.append(entityId);
        builder.append(", topMcGroupIdIndex=");
        builder.append(topMcGroupIdIndex);
        builder.append(", topMcSlotIndex=");
        builder.append(topMcSlotIndex);
        builder.append(", topMcPortIndex=");
        builder.append(topMcPortIndex);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", topMcOnuCount=");
        builder.append(topMcOnuCount);
        builder.append(", topMcOnuList=");
        builder.append(topMcOnuList);
        builder.append(", topMcOnuListUniIndexList=");
        builder.append(topMcOnuListUniIndexList);
        builder.append("]");
        return builder.toString();
    }

}
