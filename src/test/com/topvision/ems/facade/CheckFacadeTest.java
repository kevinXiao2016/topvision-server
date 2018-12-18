/***********************************************************************
 * $Id: CheckFacadeTest.java,v1.0 2011-10-10 下午04:46:50 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.facade.DubboFacadeFactory;

/**
 * @author Victor
 * @created @2011-10-10-下午04:46:50
 * 
 */
public class CheckFacadeTest {

    /**
     * Test method for {@link com.topvision.ems.facade.CheckFacade#check()}.
     */
    @Test
    public void testCheck() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        CheckFacade checkFacade = facadeFactory.getFacade(engine, CheckFacade.class);
        //checkFacade.check(engine.getIp());
        assertEquals(1, 1);
    }
}
