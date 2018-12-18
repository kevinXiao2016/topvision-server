/***********************************************************************
 * $Id: CameraPlanTable.java,v1.0 2013年12月23日 下午3:30:33 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:30:33
 *
 */
public class CameraPlanTable extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 1157894758864324140L;

    /* 编号不一定为纯数字，比如这种类型的编号  FZ-1103L  **/
    private String cameraNo;
    /* 规划表中暂时允许Ip-Location一对多**/
    private String location;
    private String ip;



    public String getCameraNo() {
        return cameraNo;
    }

    public void setCameraNo(String cameraNo) {
        this.cameraNo = cameraNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "CameraPlanTable [cameraNo=" + cameraNo + ", location=" + location + ", ip=" + ip + "]";
    }

}
