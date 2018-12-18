/***********************************************************************
 * $Id: BatchDeployFacade.java,v1.0 2013年11月30日 下午5:00:41 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.batchdeploy;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年11月30日-下午5:00:41
 *
 */
@EngineFacade(serviceName = "BatchDeployFacade", beanName = "batchDeployFacade")
public interface BatchDeployFacade extends Facade {

    /**
     * 在分布式engine中执行批量下发，可包含多个设备
     * @param readyMap
     * @param bundle
     * @param executor
     */
    <T extends BatchRecordSupport, V> List<Result<T>> execBatchDeploy(Map<SnmpParam, List<T>> readyMap, V bundle,
            String executor);
}
