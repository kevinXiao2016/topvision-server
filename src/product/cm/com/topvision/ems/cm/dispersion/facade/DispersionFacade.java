/***********************************************************************
 * $Id: DispersionFacade.java,v1.0 2015-3-25 上午11:27:14 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.facade;

import java.util.Map;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;

/**
 * @author fanzidong
 * @created @2015-3-25-上午11:27:14
 * 
 */
@EngineFacade(serviceName = "DispersionFacade", beanName = "dispersionFacade", category = "CmPoll")
public interface DispersionFacade extends Facade {
    /**
     * 获取engine端的离散度分布数据
     * 
     * @return
     */
    Map<Long, Map<String, Map<Integer, Integer>>> getDistributionData();
}
