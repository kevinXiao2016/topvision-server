/***********************************************************************
 * $Id: IRecordSupport.java,v1.0 2013年12月3日 下午1:54:13 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.batchdeploy.domain;

import java.io.Serializable;

/**
 * @author Bravin
 * @created @2013年12月3日-下午1:54:13
 *
 */
public abstract class BatchRecordSupport implements Serializable {
    private static final long serialVersionUID = -1387608680718500804L;
    protected int reason;
    protected int typeId;
    protected String item;

    public BatchRecordSupport() {
    }

    /**
     * 能精确定位到设置目标的标识
     */
    public abstract String getItem();

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getReason() {
        return reason;
    }

}
