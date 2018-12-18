/***********************************************************************
 * $Id: OltTrunkFacade.java,v1.0 2013-10-25 下午3:21:25 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.facade;

import java.util.List;

import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午3:21:25
 *
 */
@EngineFacade(serviceName = "OltTrunkFacade", beanName = "oltTrunkFacade")
public interface OltTrunkFacade extends Facade {

    /**
     * TRUNK组的增加
     * 
     * @param snmpParam
     * @param sniTrunkConfig
     * @return
     */
    OltSniTrunkConfig addSniTrunkConfig(SnmpParam snmpParam, OltSniTrunkConfig sniTrunkConfig);

    /**
     * 删除一个TrunkGroup
     * 
     * @param snmpParam
     *            对应设备的snmpParam
     * @param oltSniTrunkConfig
     *            删除的trunk组对象
     */
    void deleteSniTrunkConfig(SnmpParam snmpParam, OltSniTrunkConfig oltSniTrunkConfig);

    /**
     * 修改一个TrunkGroup
     * 
     * @param snmpParam
     *            对应设备的snmpParam
     * @param oltSniTrunkConfig
     * @return OltSniTrunkConfig
     */
    OltSniTrunkConfig modifySniTrunkConfig(SnmpParam snmpParam, OltSniTrunkConfig oltSniTrunkConfig);

    /**
     * 刷新TrunkGroup
     * 
     * @param snmpParam
     * @return OltSniTrunkConfig
     */
    List<OltSniTrunkConfig> refreshSniTrunkConfig(SnmpParam snmpParam);

}
