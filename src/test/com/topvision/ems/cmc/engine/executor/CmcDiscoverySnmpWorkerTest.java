/***********************************************************************
 * $Id: CmcDiscoverySnmpWorkerTest.java,v1.0 2011-11-24 上午10:51:16 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.engine.executor;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * @author Victor
 * @created @2011-11-24-上午10:51:16
 * 
 */
public class CmcDiscoverySnmpWorkerTest {
    private static Logger logger = LoggerFactory.getLogger(CmcDiscoverySnmpWorkerTest.class);
    private static SnmpUtil snmpUtil;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String ip = "192.165.53.100";
        String roCommunity = "public";
        String rwCommunity = "private";
        String mib = "DOCS-IF-MIB,TOPVISION-CCMTS-MIB";
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam param = new SnmpParam();
        param.setIpAddress(ip);
        param.setCommunity(roCommunity);
        param.setWriteCommunity(rwCommunity);
        param.setMibs(mib);
        snmpUtil = new SnmpUtil(param);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        snmpUtil.releaseResources();
    }

    /**
     * Test method for {@link com.topvision.ems.cmc.engine.executor.CmcDiscoverySnmpWorker#exec()}.
     */
    @Test
    public void testExec() {
        List<CmcAttribute> cmcAttributes;
        cmcAttributes = snmpUtil.getTable(CmcAttribute.class, true);
        assertNotNull(cmcAttributes);
        logger.debug(cmcAttributes.toString());
    }
}
