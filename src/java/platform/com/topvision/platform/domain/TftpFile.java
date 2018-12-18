/***********************************************************************
 * $Id: TftpFile.java,v1.0 2013-1-26 下午3:51:47 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author Administrator
 * @created @2013-1-26-下午3:51:47
 *
 */
public class TftpFile extends BaseEntity {
    private static final long serialVersionUID = -534195927305967659L;
    //文件名称
    private String name;
    //文件大小
    private long size;
    //文件更新时间
    private String updateTime;

    public TftpFile(String name, long size, String updateTime) {
        super();
        this.name = name;
        this.size = size;
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
