/***********************************************************************
 * $Id: DolSnmpParam.java,v1.0 2011-11-13 下午04:08:00 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-11-13-下午04:08:00
 * 
 */
@Alias("dolSnmpParam")
public class DolSnmpParam extends SnmpParam implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 3947428236774569011L;

    /**
     * 从SnmpParam克隆
     */
    public static DolSnmpParam clone(SnmpParam sp) {
        DolSnmpParam param = new DolSnmpParam();
        param.setAuthoritativeEngineID(sp.getAuthoritativeEngineID());
        param.setAuthPassword(sp.getAuthPassword());
        param.setAuthProtocol(sp.getAuthProtocol());
        param.setCommunity(sp.getCommunity());
        param.setContextId(sp.getContextId());
        param.setContextName(sp.getContextName());
        param.setEntityId(sp.getEntityId());
        param.setIpAddress(sp.getIpAddress());
        param.setMibs(sp.getMibs());
        param.setPort(sp.getPort());
        param.setPrivPassword(sp.getPrivPassword());
        param.setPrivProtocol(sp.getPrivProtocol());
        param.setRetry(sp.getRetry());
        param.setSecurityLevel(sp.getSecurityLevel());
        param.setTimeout(sp.getTimeout());
        param.setUsername(sp.getUsername());
        param.setVersion(sp.getVersion());
        return param;
    }

}
