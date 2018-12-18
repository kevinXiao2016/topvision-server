/***********************************************************************
 * $Id: OltDiscoveryFacadeImplTest.java,v1.0 2011-10-24 下午01:24:48 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.engine.demo;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.topvision.ems.epon.topology.facade.OltDiscoveryFacade;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.facade.DubboFacadeFactory;

/**
 * @author Victor
 * @created @2011-10-24-下午01:24:48
 * 
 */
public class OltDiscoveryFacadeImplTest {
    private String ip = "172.10.10.11";
    private String roCommunity = "public";
    private String rwCommunity = "private";
    private String mib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";

    @SuppressWarnings("unused")
    private OltDiscoveryFacade getFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltDiscoveryFacade.class);
    }

    @SuppressWarnings("unused")
    private SnmpParam getSnmpParam() {
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam param = new SnmpParam();
        param.setIpAddress(ip);
        param.setCommunity(roCommunity);
        param.setWriteCommunity(rwCommunity);
        param.setMibs(mib);
        return param;
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.demo.OltDiscoveryFacadeImpl#discoverySlot(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testDiscoverySlot() {
        fail("Not yet implemented");
    }

}
