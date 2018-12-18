/***********************************************************************
 * $Id: DeviceLocation.java,v1.0 2012-6-25 上午09:02:13 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2012 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 设备所处位置
 * 
 * @author yq
 * 
 */
public class DeviceLocation implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2333765238739873533L;
    private Long entityId;
    private String sysLocation;
    private Integer rackNum;
    private Integer frameNum;
    private String oltType;
    private Integer occupyFrameNum;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public Integer getRackNum() {
        return rackNum;
    }

    public void setRackNum(Integer rackNum) {
        this.rackNum = rackNum;
    }

    public Integer getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(Integer frameNum) {
        this.frameNum = frameNum;
    }

    public String getOltType() {
        return oltType;
    }

    public void setOltType(String oltType) {
        this.oltType = oltType;
        Integer tmp = EponConstants.DEVICE_OCCUPY_FRAME_NUM.get(oltType);
        if (tmp != null && tmp > 0) {
            this.occupyFrameNum = tmp;
        } else {
            this.occupyFrameNum = 1;
        }
    }

    public Integer getOccupyFrameNum() {
        if (occupyFrameNum == null || occupyFrameNum < 1) {
            if (oltType != null && oltType != "") {
                Integer tmp = EponConstants.DEVICE_OCCUPY_FRAME_NUM.get(oltType);
                if (tmp != null && tmp > 0) {
                    occupyFrameNum = tmp;
                } else {
                    occupyFrameNum = 1;
                }
            } else {
                occupyFrameNum = 1;
            }
        }
        return occupyFrameNum;
    }

    public void setOccupyFrameNum(Integer occupyFrameNum) {
        this.occupyFrameNum = occupyFrameNum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DeviceLocation");
        sb.append("{entityId=").append(entityId);
        sb.append(", sysLocation=").append(sysLocation);
        sb.append(", rackNum=").append(rackNum);
        sb.append(", oltType=").append(oltType);
        sb.append(", occupyFrameNum=").append(occupyFrameNum);
        sb.append(", frameNum='").append(frameNum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
