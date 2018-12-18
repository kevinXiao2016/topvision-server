/***********************************************************************
 * $Id: RealtimeCpe.java,v1.0 2014-11-19 上午8:42:30 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author jay
 * @created @2014-11-19-上午8:42:30
 *
 */
public class RealtimeCpe implements Serializable {
    private static final long serialVersionUID = 2646893565977606741L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.4.1.1", index = true)
    private Long topCmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.4.1.2", index = true)
    private Long topCmCpeIpIndex; // 
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.4.1.3")
    private String topCmCpeMacAddress; // MAC地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.4.1.4")
    private String topCmCpeTypeStr; // CPE类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.4.1.5")
    private String topCmCpeIpAddress;// 因特网地址

    public Long getTopCmIndex() {
        return topCmIndex;
    }

    public void setTopCmIndex(Long topCmIndex) {
        this.topCmIndex = topCmIndex;
    }

    public Long getTopCmCpeIpIndex() {
        return topCmCpeIpIndex;
    }

    public void setTopCmCpeIpIndex(Long topCmCpeIpIndex) {
        this.topCmCpeIpIndex = topCmCpeIpIndex;
    }

    public String getTopCmCpeMacAddress() {
        return topCmCpeMacAddress;
    }

    public void setTopCmCpeMacAddress(String topCmCpeMacAddress) {
        this.topCmCpeMacAddress = topCmCpeMacAddress;
    }

    public String getTopCmCpeTypeStr() {
        return topCmCpeTypeStr;
    }

    public void setTopCmCpeTypeStr(String topCmCpeTypeStr) {
        this.topCmCpeTypeStr = topCmCpeTypeStr;
    }

    public String getTopCmCpeIpAddress() {
        return topCmCpeIpAddress;
    }

    public void setTopCmCpeIpAddress(String topCmCpeIpAddress) {
        this.topCmCpeIpAddress = topCmCpeIpAddress;
    }

}
