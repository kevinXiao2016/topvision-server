/***********************************************************************
 * $Id: CmcIpqamInfo.java,v1.0 2013-12-21 下午2:56:20 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2013-12-21-下午2:56:20
 *
 */
@Alias("cmcIpqamInfo")
public class CmcIpqamInfo implements Serializable, AliasesSuperType {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Long cmcId;
    private String ipqamIpAddr;
    private String ipqamIpMask;
    private String ipqamGw;
    private String ipqamMacAddr;
    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }
    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    /**
     * @return the ipqamIpAddr
     */
    public String getIpqamIpAddr() {
        return ipqamIpAddr;
    }
    /**
     * @param ipqamIpAddr the ipqamIpAddr to set
     */
    public void setIpqamIpAddr(String ipqamIpAddr) {
        this.ipqamIpAddr = ipqamIpAddr;
    }
    /**
     * @return the ipqamIpMask
     */
    public String getIpqamIpMask() {
        return ipqamIpMask;
    }
    /**
     * @param ipqamIpMask the ipqamIpMask to set
     */
    public void setIpqamIpMask(String ipqamIpMask) {
        this.ipqamIpMask = ipqamIpMask;
    }
    /**
     * @return the ipqamGw
     */
    public String getIpqamGw() {
        return ipqamGw;
    }
    /**
     * @param ipqamGw the ipqamGw to set
     */
    public void setIpqamGw(String ipqamGw) {
        this.ipqamGw = ipqamGw;
    }
    /**
     * @return the ipqamMacAddr
     */
    public String getIpqamMacAddr() {
        return ipqamMacAddr;
    }
    /**
     * @param ipqamMacAddr the ipqamMacAddr to set
     */
    public void setIpqamMacAddr(String ipqamMacAddr) {
        this.ipqamMacAddr = ipqamMacAddr;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmcIpqamInfo [cmcId=" + cmcId + ", ipqamIpAddr=" + ipqamIpAddr + ", ipqamIpMask=" + ipqamIpMask
                + ", ipqamGw=" + ipqamGw + ", ipqamMacAddr=" + ipqamMacAddr + "]";
    }
    
    
    
    

}
