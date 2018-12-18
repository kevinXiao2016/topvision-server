/***********************************************************************
 * $Id: CollectorFacadeImpl.java,v1.0 Aug 9, 2016 3:42:01 PM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import com.topvision.ems.facade.CollectorFacade;
import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @Aug 9, 2016-3:42:01 PM
 *
 */
@Engine("collectorFacade")
public class CollectorFacadeImpl extends EmsFacade implements CollectorFacade {
    @Override
    public void shutdown() {
        System.out.println("The collector shutdown!!!");
        logger.info("The collector shutdown!!!");
        System.exit(0);
    }
}
