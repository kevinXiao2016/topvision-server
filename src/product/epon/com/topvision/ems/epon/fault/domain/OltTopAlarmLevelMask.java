/***********************************************************************
 * $Id: OltTopAlarmLevelMask.java,v1.0 2013-11-26 下午02:16:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-11-26-下午02:16:06
 * 
 */
public class OltTopAlarmLevelMask implements AliasesSuperType {
    private static final long serialVersionUID = 6216259986342762742L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.3.1.1", index = true)
    private Integer topAlmLvlMaskLevelIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.3.1.2", writable = true, type = "Integer32")
    private Integer topAlmLvlMaskRowStatus;

    /**
     * @return the topAlmLvlMaskLevelIdx
     */
    public Integer getTopAlmLvlMaskLevelIdx() {
        return topAlmLvlMaskLevelIdx;
    }

    /**
     * @param topAlmLvlMaskLevelIdx
     *            the topAlmLvlMaskLevelIdx to set
     */
    public void setTopAlmLvlMaskLevelIdx(Integer topAlmLvlMaskLevelIdx) {
        this.topAlmLvlMaskLevelIdx = topAlmLvlMaskLevelIdx;
    }

    /**
     * @return the topAlmLvlMaskRowStatus
     */
    public Integer getTopAlmLvlMaskRowStatus() {
        return topAlmLvlMaskRowStatus;
    }

    /**
     * @param topAlmLvlMaskRowStatus
     *            the topAlmLvlMaskRowStatus to set
     */
    public void setTopAlmLvlMaskRowStatus(Integer topAlmLvlMaskRowStatus) {
        this.topAlmLvlMaskRowStatus = topAlmLvlMaskRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltTopAlarmLevelMask [topAlmLvlMaskLevelIdx=");
        builder.append(topAlmLvlMaskLevelIdx);
        builder.append(", topAlmLvlMaskRowStatus=");
        builder.append(topAlmLvlMaskRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
