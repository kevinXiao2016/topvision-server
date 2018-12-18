/***********************************************************************
 * $Id: TopOnuGlobalCfgMgmt.java,v1.0 2016年8月2日 下午7:38:37 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年8月2日-下午7:38:37
 *
 */
public class TopOnuGlobalCfgMgmt implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2744074101943913797L;
    
    public static final int WAN = 1;
    public static final int WLAN = 2;
    public static final int CATV = 3;
    
    public static final int ON = 0;
    public static final int OFF = 1;
    
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.15.2.1.1", index = true)
    private Integer topOnuGlobalCfgMgmtItemIndex;// 1: wan(1) 2: wlan(2) 3: catv(3)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.15.2.1.2", writable = true)
    private String topOnuGlobalCfgMgmtValue;
    private Integer mgmtValue;// bit0: 1-enable olt oam/omci management; 0-disable olt oam/omci
                              // management;

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
     * @return the topOnuGlobalCfgMgmtItemIndex
     */
    public Integer getTopOnuGlobalCfgMgmtItemIndex() {
        return topOnuGlobalCfgMgmtItemIndex;
    }

    /**
     * @param topOnuGlobalCfgMgmtItemIndex
     *            the topOnuGlobalCfgMgmtItemIndex to set
     */
    public void setTopOnuGlobalCfgMgmtItemIndex(Integer topOnuGlobalCfgMgmtItemIndex) {
        this.topOnuGlobalCfgMgmtItemIndex = topOnuGlobalCfgMgmtItemIndex;
    }

    /**
     * @return the topOnuGlobalCfgMgmtValue
     */
    public String getTopOnuGlobalCfgMgmtValue() {
        return topOnuGlobalCfgMgmtValue;
    }

    /**
     * @param topOnuGlobalCfgMgmtValue
     *            the topOnuGlobalCfgMgmtValue to set
     */
    public void setTopOnuGlobalCfgMgmtValue(String topOnuGlobalCfgMgmtValue) {
        this.topOnuGlobalCfgMgmtValue = topOnuGlobalCfgMgmtValue;
        if (topOnuGlobalCfgMgmtValue != null) {
            mgmtValue = EponUtil.getOnuGlobalCfgMgmt(topOnuGlobalCfgMgmtValue);
        }
    }

    /**
     * @return the mgmtValue
     */
    public Integer getMgmtValue() {
        return mgmtValue;
    }

    /**
     * @param mgmtValue
     *            the mgmtValue to set
     */
    public void setMgmtValue(Integer mgmtValue) {
        this.mgmtValue = mgmtValue;
        if (mgmtValue != null) {
            topOnuGlobalCfgMgmtValue = EponUtil.getOnuGlobalCfgMgmtMibValue(mgmtValue);
        }
    }

}
