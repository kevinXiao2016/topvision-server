/***********************************************************************
 * $Id: OltDhcpFacadeImplTest.java,v1.0 Nov 21, 2011 7:19:22 PM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.engine.executor;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.facade.OltDhcpFacade;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.facade.DubboFacadeFactory;

/**
 * @author Victor
 * @created @Nov 21, 2011-7:19:22 PM
 * 
 */
public class OltDhcpFacadeImplTest {
    private static OltDhcpFacade oltDhcpFacade;
    private static SnmpParam snmpParam;
    private static Logger logger = LoggerFactory.getLogger(OltDhcpFacadeImplTest.class);

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String ip = "172.10.10.20";
        String roCommunity = "public";
        String rwCommunity = "private";
        String mib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";

        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        oltDhcpFacade = facadeFactory.getFacade(engine, OltDhcpFacade.class);

        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        snmpParam = new SnmpParam();
        snmpParam.setIpAddress(ip);
        snmpParam.setTimeout(200000);
        snmpParam.setRetry((byte) 0);
        snmpParam.setCommunity(roCommunity);
        snmpParam.setWriteCommunity(rwCommunity);
        snmpParam.setMibs(mib);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#setSnmpExecutorService(com.topvision.framework.snmp.SnmpExecutorService)}
     * .
     */
    @Test
    public void testSetSnmpExecutorService() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#getSnmpExecutorService()}.
     */
    @Test
    public void testGetSnmpExecutorService() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#getDhcpBaseConfig(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetDhcpBaseConfig() {
        OltDhcpBaseConfig baseConfig = oltDhcpFacade.getDhcpBaseConfig(snmpParam);
        logger.info(baseConfig.getTopOltPPPOEPlusEnable().toString());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#getDhcpServerConfigs(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetDhcpServerConfigs() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#getDhcpGiaddrConfigs(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetDhcpGiaddrConfigs() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#getDhcpIpMacStatics(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetDhcpIpMacStatics() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#modifyDhcpBaseConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpBaseConfig)}
     * .
     */
    @Test
    public void testModifyDhcpBaseConfig() {
        OltDhcpBaseConfig baseConfig = new OltDhcpBaseConfig();
        baseConfig.setTopOltDHCPDyncIPMACBind(1);
        baseConfig.setTopOltDHCPRelayMode(1);
        baseConfig.setTopOltPPPOEPlusEnable(1);
        oltDhcpFacade.modifyDhcpBaseConfig(snmpParam, baseConfig);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#addDhcpServerConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpServerConfig)}
     * .
     */
    @Test
    public void testAddDhcpServerConfig() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#addDhcpGiaddrConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpGiaddrConfig)}
     * .
     */
    @Test
    public void testAddDhcpGiaddrConfig() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#addDhcpIpMacStatic(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpIpMacStatic)}
     * .
     */
    @Test
    public void testAddDhcpIpMacStatic() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#deleteDhcpServerConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpServerConfig)}
     * .
     */
    @Test
    public void testDeleteDhcpServerConfig() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#deleteDhcpGiaddrConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpGiaddrConfig)}
     * .
     */
    @Test
    public void testDeleteDhcpGiaddrConfig() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#deleteDhcpIpMacStatic(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltDhcpIpMacStatic)}
     * .
     */
    @Test
    public void testDeleteDhcpIpMacStatic() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltDhcpFacadeImpl#getDhcpIpMacDynamics(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetDhcpIpMacDynamics() {
        fail("Not yet implemented");
    }

}
