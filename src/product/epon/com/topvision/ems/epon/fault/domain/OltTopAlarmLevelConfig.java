/***********************************************************************
 * $Id: OltTopAlarmLevelConfig.java,v1.0 2013-11-26 下午02:20:34 $
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
 * @created @2013-11-26-下午02:20:34
 * 
 */
public class OltTopAlarmLevelConfig implements AliasesSuperType {
    private static final long serialVersionUID = -2099631541993223632L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.6.1.1.1", index = true)
    private Integer topAlmLvlAlarmIdIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.6.1.1.2", writable = true)
    private Integer topAlmLvlLevel;

    /**
     * @return the topAlmLvlAlarmIdIdx
     */
    public Integer getTopAlmLvlAlarmIdIdx() {
        return topAlmLvlAlarmIdIdx;
    }

    /**
     * @param topAlmLvlAlarmIdIdx
     *            the topAlmLvlAlarmIdIdx to set
     */
    public void setTopAlmLvlAlarmIdIdx(Integer topAlmLvlAlarmIdIdx) {
        this.topAlmLvlAlarmIdIdx = topAlmLvlAlarmIdIdx;
    }

    /**
     * @return the topAlmLvlLevel
     */
    public Integer getTopAlmLvlLevel() {
        return topAlmLvlLevel;
    }

    /**
     * @param topAlmLvlLevel
     *            the topAlmLvlLevel to set
     */
    public void setTopAlmLvlLevel(Integer topAlmLvlLevel) {
        this.topAlmLvlLevel = topAlmLvlLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltTopAlarmLevelConfig [topAlmLvlAlarmIdIdx=");
        builder.append(topAlmLvlAlarmIdIdx);
        builder.append(", topAlmLvlLevel=");
        builder.append(topAlmLvlLevel);
        builder.append("]");
        return builder.toString();
    }

}
