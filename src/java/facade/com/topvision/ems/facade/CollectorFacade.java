/***********************************************************************
 * $Id: CollectorFacade.java,v1.0 Aug 9, 2016 3:28:21 PM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import com.topvision.framework.annotation.EngineFacade;

/**
 * @author Victor
 * @created @Aug 9, 2016-3:28:21 PM
 *
 */
@EngineFacade(serviceName = "CollectorFacade", beanName = "collectorFacade", category = "System")
public interface CollectorFacade {
    void shutdown();
}
