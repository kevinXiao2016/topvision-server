/***********************************************************************
 * $Id: OltPonProtectFacade.java,v1.0 2013-10-25 下午3:32:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.facade;

import java.util.List;

import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午3:32:42
 *
 */
@EngineFacade(serviceName = "OltPonProtectFacade", beanName = "oltPonProtectFacade")
public interface OltPonProtectFacade extends Facade {

    /**
     * 添加保护组
     * 
     * @param param
     * @param ponprotect
     */
    public OltPonProtectConfig addPonProtect(SnmpParam param, OltPonProtectConfig ponprotect);

    /**
     * 修改保护组属性
     * 
     * @param param
     * @param ponprotect
     * @return
     */
    public OltPonProtectConfig modifyOltPonProtect(SnmpParam param, OltPonProtectConfig ponprotect);

    /**
     * 删除保护组
     * 
     * @param param
     * @param ponprotect
     */
    public void deletePonProtect(SnmpParam param, OltPonProtectConfig ponprotect);

    public List<OltPonProtectConfig> refreshOltPonProtect(SnmpParam snmpParam);
}
