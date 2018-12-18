/***********************************************************************
 * $Id: CmcAclDefAct.java,v1.0 2013-5-3 下午04:44:20 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.acl.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzs
 * @created @2013-5-3-下午04:44:20
 *
 */
@Alias("cmcAclDefAct")
public class CmcAclDefAct implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -420336880881481963L;
    private Long entityId;
    private Integer uplinkIngress,uplinkEgress,cableEgress,cabelIngress;
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public Integer getUplinkIngress() {
        return uplinkIngress;
    }
    public void setUplinkIngress(Integer uplinkIngress) {
        this.uplinkIngress = uplinkIngress;
    }

    public Integer getUplinkEgress() {
        return uplinkEgress;
    }
    public void setUplinkEgress(Integer uplinkEgress) {
        this.uplinkEgress = uplinkEgress;
    }
    public Integer getCableEgress() {
        return cableEgress;
    }
    public void setCableEgress(Integer cableEgress) {
        this.cableEgress = cableEgress;
    }
    public Integer getCabelIngress() {
        return cabelIngress;
    }
    public void setCabelIngress(Integer cabelIngress) {
        this.cabelIngress = cabelIngress;
    }
    
    

}
