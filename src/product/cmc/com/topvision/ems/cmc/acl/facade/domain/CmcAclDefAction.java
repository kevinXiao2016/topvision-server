/***********************************************************************
 * $Id: CmcAclDefAction.java,v1.0 2013-4-23 下午04:01:07 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.acl.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * cmc acl 四个放置点的默认动作
 * @author lzs
 * @created @2013-4-23-下午04:01:07
 *
 */
public class CmcAclDefAction implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6962294318175559831L;
    /**
     * 1: uplinkIngress(1) 
     * 2: uplinkEgress(2) 
     * 3: cableEgress(3) 
     * 4: cableIngress(4) 
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.1.1.1", index = true)
    private Integer topAclPositionIndex;
    /**
     * 1: deny(0) 
     * 2: permit(1) 
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.14.1.1.2",type = "Integer32", writable = true)
    private Integer topPositionDefAction;
    
    
    
    
    public Integer getTopAclPositionIndex() {
        return topAclPositionIndex;
    }
    public void setTopAclPositionIndex(Integer topAclPositionIndex) {
        this.topAclPositionIndex = topAclPositionIndex;
    }
    public Integer getTopPositionDefAction() {
        return topPositionDefAction;
    }
    public void setTopPositionDefAction(Integer topPositionDefAction) {
        this.topPositionDefAction = topPositionDefAction;
    }
    
    
    
}
