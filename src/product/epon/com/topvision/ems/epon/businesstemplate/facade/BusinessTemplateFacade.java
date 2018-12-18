/***********************************************************************
 * $Id: BusinessTemplateFacade.java,v1.0 2015-12-8 下午2:25:11 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.facade;

import java.util.List;

import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:25:11
 *
 */
@EngineFacade(serviceName = "BusinessTemplateFacade", beanName = "businessTemplateFacade")
public interface BusinessTemplateFacade extends Facade {

    public List<OnuSrvProfile> refreshOnuSrvProfile(SnmpParam snmpParam);

    public List<OnuPortVlanProfile> refreshOnuPortVlanProfile(SnmpParam snmpParam);

    public List<OnuIgmpProfile> refreshOnuIgmpProfile(SnmpParam snmpParam);

    public List<OnuSrvIgmpVlanTrans> refreshOnuSrvIgmpVlanTrans(SnmpParam snmpParam);

    public List<OnuCapability> refreshOnuCapability(SnmpParam snmpParam);

    void deleteOnuSrvProfile(SnmpParam snmpParam, OnuSrvProfile srvProfile);

    void deleteOnuPortVlanProfile(SnmpParam snmpParam, OnuPortVlanProfile portVlanProfile);

    void deleteOnuIgmpProfile(SnmpParam snmpParam, OnuIgmpProfile igmpProfile);

    void deleteOnuIgmpVlanTrans(SnmpParam snmpParam, OnuSrvIgmpVlanTrans onuIgmpVlanTrans);

    void deleteOnuCapability(SnmpParam snmpParam, OnuCapability capability);

    OnuSrvProfile modifyOnuSrvProfile(SnmpParam snmpParam, OnuSrvProfile srvProfile);

    OnuPortVlanProfile modifyOnuPortVlanProfile(SnmpParam snmpParam, OnuPortVlanProfile portVlanProfile);

    OnuIgmpProfile modifyOnuIgmpProfile(SnmpParam snmpParam, OnuIgmpProfile igmpProfile);

    void addOnuSrvProfile(SnmpParam snmpParam, OnuSrvProfile srvProfile);

    void addOnuPortVlanProfile(SnmpParam snmpParam, OnuPortVlanProfile portVlanProfile);

    void addOnuIgmpProfile(SnmpParam snmpParam, OnuIgmpProfile igmpProfile);

    void addOnuSrvIgmpVlanTrans(SnmpParam snmpParam, OnuSrvIgmpVlanTrans onuIgmpVlanTrans);

    void addOnuCapability(SnmpParam snmpParam, OnuCapability capability);

    /**
     * 解除能力集与模板的绑定
     * @param snmpParam
     * @param srvProfile
     */
    void unBindCapability(SnmpParam snmpParam, OnuSrvProfile srvProfile);

}
