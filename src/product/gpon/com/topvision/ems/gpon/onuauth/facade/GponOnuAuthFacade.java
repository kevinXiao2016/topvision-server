/***********************************************************************
 * $Id: GponOnuAuthFacade.java,v1.0 2016年12月19日 下午1:15:21 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.facade;

import java.util.List;

import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:15:21
 *
 */
@EngineFacade(serviceName = "GponOnuAuthFacade", beanName = "gponOnuAuthFacade")
public interface GponOnuAuthFacade {

    /**
     * @param snmpParam
     * @param class1
     * @return
     */
    List<GponAutoAuthConfig> refershGponOnuAutoAuthList(SnmpParam snmpParam, Class<GponAutoAuthConfig> class1);

    /**
     * @param snmpParam
     * @param class1
     * @return
     */
    List<GponOnuAutoFind> refershGponOnuAutoFindList(SnmpParam snmpParam, Class<GponOnuAutoFind> class1);

    /**
     * @param snmpParam
     * @param class1
     * @return
     */
    List<GponOnuAuthConfig> refershGponOnuAuthConfigList(SnmpParam snmpParam, Class<GponOnuAuthConfig> class1);

    /**
     * @param snmpParam
     * @param class1
     * @return
     */
    List<GponOnuAuthMode> refershGponOnuAuthModeList(SnmpParam snmpParam, Class<GponOnuAuthMode> class1);

    /**
     * @param snmpParam
     * @param gponOnuAuthConfig
     */
    void addGponOnuAuth(SnmpParam snmpParam, GponOnuAuthConfig gponOnuAuthConfig);

    /**
     * @param snmpParam
     * @param gponOnuAuthMode
     */
    void modifyOnuAuthMode(SnmpParam snmpParam, GponOnuAuthMode gponOnuAuthMode);

    /**
     * @param snmpParam
     * @param gponAutoAuthConfig
     */
    void addGponAutoAuthConfig(SnmpParam snmpParam, GponAutoAuthConfig gponAutoAuthConfig);
    
    /**
     * @param snmpParam
     * @param gponAutoAuthConfig
     */
    void modifyGponAutoAuthConfig(SnmpParam snmpParam, GponAutoAuthConfig gponAutoAuthConfig);

    /**
     * @param snmpParam
     * @param gponOnuAuthConfig
     */
    void deleteGponOnuAuth(SnmpParam snmpParam, GponOnuAuthConfig gponOnuAuthConfig);

    /**
     * @param snmpParam
     * @param gponAutoAuthConfig
     */
    void deleteGponAutoAuth(SnmpParam snmpParam, GponAutoAuthConfig gponAutoAuthConfig);

}
