/***********************************************************************
 * $Id: EponBatchDeployTarget.java,v1.0 2013年12月2日 上午9:58:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013年12月2日-上午9:58:49
 *
 */
public class Target extends BatchRecordSupport implements AliasesSuperType, Serializable {
    private static final long serialVersionUID = 2634503480903025983L;
    private Long slot;
    private Long port;
    private Long llid;
    private Long uni;
    private Long targetIndex;
    private Long id;
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public Long getLlid() {
        return llid;
    }

    public void setLlid(Long llid) {
        this.llid = llid;
    }

    public Long getUni() {
        return uni;
    }

    public void setUni(Long uni) {
        this.uni = uni;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(Long targetIndex) {
        this.slot = (targetIndex.longValue() & 0xFF00000000L) >> 32;
        this.port = (targetIndex.longValue() & 0x00FF000000L) >> 24;
        this.llid = (targetIndex.longValue() & 0x0000FF0000L) >> 16;
        this.uni = targetIndex.longValue() & 0x00000000FFL;
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return "Target [slot=" + slot + ", port=" + port + ", llid=" + llid + ", uni=" + uni + ", targetIndex="
                + targetIndex + ", id=" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((targetIndex == null) ? 0 : targetIndex.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Target other = (Target) obj;
        if (targetIndex == null) {
            if (other.targetIndex != null)
                return false;
        } else if (!targetIndex.equals(other.targetIndex))
            return false;
        return true;
    }


    /*
     * (non-Javadoc)
     * @see com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport#getItem()
     */
    @Override
    public String getItem() {
        return EponIndex.getStringFromIndex(targetIndex);
    }

}
