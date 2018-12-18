/***********************************************************************
 * $Id: OltPerfFacadeImplTest.java,v1.0 2011-12-6 下午02:25:11 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.engine.executor;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;

/**
 * @author Administrator
 * @created @2011-12-6-下午02:25:11
 * 
 */
public class OltPerfFacadeImplTest extends EmsSpringBaseTest {
    private OltPerfFacade oltPerfFacade;

    // protected static Logger logger = LoggerFactory.getLogger(MonitorJob.class);
    // private String ip = "172.10.10.25";
    // private String roCommunity = "public";
    // private String rwCommunity = "private";
    // private String mib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";
    //
    // private OltPerfFacade getFacade() {
    // FacadeFactory facadeFactory = new RmiFacadeFactory();
    // EngineServer engine = new EngineServer();
    // engine.setIp("localhost");
    // engine.setPort(3004);
    // return facadeFactory.getFacade(engine, OltPerfFacade.class);
    // }
    //
    // private SnmpParam getSnmpParam() {
    // EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
    // SnmpParam param = new SnmpParam();
    // param.setIpAddress(ip);
    // param.setCommunity(roCommunity);
    // param.setWriteCommunity(rwCommunity);
    // param.setMibs(mib);
    // return param;
    // }

    @Before
    public void setUp() {
        setIp("172.10.10.25");
        init();
        oltPerfFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OltPerfFacade.class);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getOltAttribute(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetOltAttribute() {
        // OltAttribute oltAttribute = snmpUtil.get(OltAttribute.class);
        // OltAttribute oltAttribute = snmpExecutorService.getData(snmpParam,OltAttribute.class);
        // OltAttribute oltAttribute = oltPerfFacade.getOltAttribute(snmpParam);
        // logger.info(oltAttribute.toString());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getOltSlotStatus(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetOltSlotStatus() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#setSnmpExecutorService(com.topvision.framework.snmp.SnmpExecutorService)}
     * .
     */
    @Test
    public void testSetSnmpExecutorService() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getCurPerfRecord(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfCurStatsTable)}
     * .
     */
    @Test
    public void testGetCurPerfRecord() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#modifyPerfThreshold(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfThresholdPort)}
     * .
     */
    @Test
    public void testModifyPerfThresholdSnmpParamPerfThresholdPort() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#modifyPerfThreshold(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfThresholdTemperature)}
     * .
     */
    @Test
    public void testModifyPerfThresholdSnmpParamPerfThresholdTemperature() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfStatCycle(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetPerfStatCycle() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfStats15Table(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetPerfStats15TableSnmpParam() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfStats15Table(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfStats15Table, java.lang.Long)}
     * .
     */
    @Test
    public void testGetPerfStats15TableSnmpParamPerfStats15TableLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfStats24Table(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetPerfStats24TableSnmpParam() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfStats24Table(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfStats24Table, java.lang.Long)}
     * .
     */
    @Test
    public void testGetPerfStats24TableSnmpParamPerfStats24TableLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfStatsGlobalSet(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetPerfStatsGlobalSet() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfThresholdPortList(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetPerfThresholdPortList() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#getPerfThresholdTemperatureList(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetPerfThresholdTemperatureList() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#savePerfStatCycle(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfStatCycle)}
     * .
     */
    @Test
    public void testSavePerfStatCycle() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.performance.engine.OltPerfFacadeImpl#savePerfStatsGlobalSet(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.PerfStatsGlobalSet)}
     * .
     */
    @Test
    public void testSavePerfStatsGlobalSet() {
        fail("Not yet implemented");
    }

}
