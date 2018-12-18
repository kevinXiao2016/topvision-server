/***********************************************************************
 * $Id: TopSysFileDirEntry.java,v1.0 2013-12-16 上午11:11:59 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2013-12-16-上午11:11:59
 *
 */

public class TopSysFileDirEntry implements AliasesSuperType {

    private static final long serialVersionUID = 8333714553101344132L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.11.1.1.1", index = true)
    private Integer fileDirType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.11.1.1.2", type = "OctetString")
    private String fileDirPath;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.11.1.1.3", type = "Integer32")
    private Integer fileDirAttr;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getFileDirType() {
        return fileDirType;
    }

    public void setFileDirType(Integer fileDirType) {
        this.fileDirType = fileDirType;
    }

    public String getFileDirPath() {
        return fileDirPath;
    }

    public void setFileDirPath(String fileDirPath) {
        this.fileDirPath = fileDirPath;
    }

    public Integer getFileDirAttr() {
        return fileDirAttr;
    }

    public void setFileDirAttr(Integer fileDirAttr) {
        this.fileDirAttr = fileDirAttr;
    }

}
