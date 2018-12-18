/***********************************************************************
 * $Id: TopSysErrInfoEntry.java,v1.0 2012-6-6 下午17:30:42 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * errorCode 属性
 * 
 * @author lizongtian
 * 
 */
public class TopSysErrInfoEntry implements Serializable {

    private static final long serialVersionUID = 2875761566738710096L;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.7.1.1.1", index = true)
    private Integer topSysErrInfoLangIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.7.1.1.2", index = true)
    private Integer topSysErrInfoIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.7.1.1.3")
    private String topSysErrInfoDescription;

    public Integer getTopSysErrInfoLangIndex() {
        return topSysErrInfoLangIndex;
    }

    public void setTopSysErrInfoLangIndex(Integer topSysErrInfoLangIndex) {
        this.topSysErrInfoLangIndex = topSysErrInfoLangIndex;
    }

    public Integer getTopSysErrInfoIdIndex() {
        return topSysErrInfoIdIndex;
    }

    public void setTopSysErrInfoIdIndex(Integer topSysErrInfoIdIndex) {
        this.topSysErrInfoIdIndex = topSysErrInfoIdIndex;
    }

    public String getTopSysErrInfoDescription() {
        return topSysErrInfoDescription;
    }

    public void setTopSysErrInfoDescription(String topSysErrInfoDescription) {
        this.topSysErrInfoDescription = topSysErrInfoDescription;
    }

}
