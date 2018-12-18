/***********************************************************************
 * $Id: FacadeAutoAware.java,v1.0 2015年4月17日 下午12:00:10 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.launcher;

import com.topvision.ems.facade.domain.EngineServerParam;

/**
 * @author Victor
 * @created @2015年4月17日-下午12:00:10
 *
 */
public interface FacadeAutoAware {
    public void init() throws Exception;

    public void destroy();

    public void facadeAutoAware(EngineServerParam param);

    public void facadeChangeAware(EngineServerParam param);
}
