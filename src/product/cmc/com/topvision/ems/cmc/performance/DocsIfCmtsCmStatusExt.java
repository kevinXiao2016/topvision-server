/***********************************************************************
 * $Id: DocsIfCmtsCmStatusExt.java,v1.0 2011-10-26 下午04:38:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author jay
 * @created @2011-10-26-下午04:38:38
 */
@Alias("docsIfCmtsCmStatusExt")
public class DocsIfCmtsCmStatusExt implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -830134461940934671L;
    public static final Integer NOTCOLLECTED = -1;
    public static final Integer VERSIONNOTSUPPORTED = -2;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long statusIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.21.3.1.1")
    private Integer docsisMode;

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public Integer getDocsisMode() {
        return docsisMode;
    }

    public void setDocsisMode(Integer docsisMode) {
        this.docsisMode = docsisMode;
    }
}
