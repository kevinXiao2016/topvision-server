/***********************************************************************
 * $Id: IgmpPortInfo.java,v1.0 2016-6-20 下午7:58:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-6-20-下午7:58:43
 * 表示IGMP管理中使用到的端口信息
 */
public class IgmpPortInfo implements AliasesSuperType {
    private static final long serialVersionUID = 1038272723281555798L;

    //端口唯一标识,SNI/PON口INDEX或者上联口聚合ID
    private Long portIndex;
    //端口类型
    private Integer portType;
    //端口名称
    private String portName;

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public String getPortName() {
        if (portName == null && portIndex != null) {
            portName = EponIndex.getStringFromIndex(portIndex);
        }
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpPortInfo [portIndex=");
        builder.append(portIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", portName=");
        builder.append(portName);
        builder.append("]");
        return builder.toString();
    }

}
