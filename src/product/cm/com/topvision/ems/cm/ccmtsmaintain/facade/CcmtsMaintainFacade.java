/***********************************************************************
 * $Id: CcmtsMaintainFacade.java,v1.0 2015-5-27 下午3:54:31 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.facade;

import java.util.Map;

import com.topvision.framework.annotation.EngineFacade;

/**
 * @author fanzidong
 * @created @2015-5-27-下午3:54:31
 *
 */
@EngineFacade(serviceName = "CcmtsMaintainFacade", beanName = "ccmtsMaintainFacade", category = "CmPoll")
public interface CcmtsMaintainFacade {
    /**
     * 获取engine端的小C运维数据
     * 
     * @return
     */
    Map<Long, Map<Integer, Map<String, Double>>> getDistributionData();

}
