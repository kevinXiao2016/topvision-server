/***********************************************************************
 * $Id: OltBfsxFacade.java,v1.0 2014年9月25日 上午9:05:03 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.facade;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2014年9月25日-上午9:05:03
 *
 */
@EngineFacade(serviceName = "oltBfsxFacade", beanName = "oltBfsxFacade")
public interface OltBfsxFacade extends Facade {

    /**
     * 刷新设备的面板图界面
     * @param snmpParam
     * @return
     */
    Map<String, Object> bfsxOltInfo(SnmpParam snmpParam);

    /**
     * 刷新设备MIRROR视图界面
     * @param snmpParam
     * @return
     */
    List<OltSniMirrorConfig> bfsxOltMirror(SnmpParam snmpParam);

    /**
     * 刷新设备TRUNK视图界面
     * @param snmpParam
     * @return
     */
    List<OltSniTrunkConfig> bfsxOltTrunk(SnmpParam snmpParam);

    /**
     * 刷新设备PON保护视图界面
     * @param snmpParam
     * @return
     */
    List<OltPonProtectConfig> bfsxOltPonProtectGroup(SnmpParam snmpParam);

}
