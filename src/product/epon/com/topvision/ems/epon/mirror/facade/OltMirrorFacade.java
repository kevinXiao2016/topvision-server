/***********************************************************************
 * $Id: OltMirrorFacade.java,v1.0 2013-10-25 下午2:13:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.facade;

import java.util.List;

import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午2:13:07
 *
 */
@EngineFacade(serviceName = "oltMirrorFacade", beanName = "oltMirrorFacade")
public interface OltMirrorFacade extends Facade {

    /**
     * 修改mirror名称
     * 
     * @param snmpParam
     * @param oltSniMirrorConfig
     * @return
     */
    OltSniMirrorConfig modifyMirrorName(SnmpParam snmpParam, OltSniMirrorConfig oltSniMirrorConfig);

    /**
     * MIRROR视图中更新目的端口、入流量端口、出流量端口列表
     * 
     * @param snmpParam
     * @param oltSniMirrorConfig
     * @return
     */
    OltSniMirrorConfig updateMirrorPortList(SnmpParam snmpParam, OltSniMirrorConfig oltSniMirrorConfig);

    /**
     * 采集OLT mirror信息
     * @param snmpParam
     * @return
     */
    List<OltSniMirrorConfig> getOltMirrorConfigs(SnmpParam snmpParam);

}
