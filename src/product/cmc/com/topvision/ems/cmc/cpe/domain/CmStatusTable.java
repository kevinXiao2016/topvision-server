/***********************************************************************
 * $Id: CmLocateInfo.java,v1.0 2015年2月10日 上午11:08:45 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

import java.io.Serializable;

/**
 * CM上面直接采集时使用
 * @author jay
 * @created @2015年2月10日-上午11:08:45
 *
 */
public class CmStatusTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4493242165379207556L;

    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Integer ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.2.2.1.4")
    private Long docsIfCmStatusResets;

    public Integer getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Integer ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfCmStatusResets() {
        return docsIfCmStatusResets;
    }

    public void setDocsIfCmStatusResets(Long docsIfCmStatusResets) {
        this.docsIfCmStatusResets = docsIfCmStatusResets;
    }
}
