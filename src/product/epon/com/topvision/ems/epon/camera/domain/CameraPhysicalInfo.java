/***********************************************************************
 * $Id: CameraPhysicalInfo.java,v1.0 2013年12月23日 下午5:22:24 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.domain;

import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013年12月23日-下午5:22:24
 *
 */
public class CameraPhysicalInfo extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -5008993466543539898L;

    private String mac;
    private String type;
    private String note;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = new MacUtils(mac).toString(MacUtils.MAOHAO).toUpperCase();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "CameraPhysicalInfo [mac=" + mac + ", type=" + type + ", note=" + note + "]";
    }

}
