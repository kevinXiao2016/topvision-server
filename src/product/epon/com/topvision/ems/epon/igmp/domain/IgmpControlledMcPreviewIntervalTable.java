/***********************************************************************
 * $Id: IgmpControlledMcPreviewIntervalTable.java,v1.0 2012-4-13 上午10:42:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2012-4-13-上午10:42:22
 * 
 */
@TableProperty(tables = { "default" })
public class IgmpControlledMcPreviewIntervalTable implements AliasesSuperType {
    private static final long serialVersionUID = 1233614434758305583L;
    private Long entityId;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.2.1.1", index = true)
    private Integer cmIndex;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.3.2.1.2", writable = true, type = "Integer32")
    private Integer previewInterval;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Integer cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Integer getPreviewInterval() {
        return previewInterval;
    }

    public void setPreviewInterval(Integer previewInterval) {
        this.previewInterval = previewInterval;
    }

}
