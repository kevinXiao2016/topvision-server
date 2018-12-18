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
 * @author jay
 * @created @2015年2月10日-上午11:08:45
 *
 */
public class CmSystemInfoExt implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4493242165379207556L;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long cmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.20.1.1")
    private String cmHardWare;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.20.1.2")
    private String cmSoftWare;

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public String getCmHardWare() {
        return cmHardWare;
    }

    public void setCmHardWare(String cmHardWare) {
        this.cmHardWare = cmHardWare;
    }

    public String getCmSoftWare() {
        return cmSoftWare;
    }

    public void setCmSoftWare(String cmSoftWare) {
        this.cmSoftWare = cmSoftWare;
    }
}
