/***********************************************************************
 * $Id: OltDiscoverySnmpWorkerTest.java,v1.0 Nov 21, 2011 5:17:18 PM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.engine.executor;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * @author Victor
 * @created @Nov 21, 2011-5:17:18 PM
 */
public class OltDiscoverySnmpWorkerTest extends EmsSpringBaseTest {
    private static Logger logger = LoggerFactory.getLogger(OltDiscoverySnmpWorkerTest.class);

    protected SnmpUtil snmpUtil;

    @Before
    public void setUp() {
        setIp("172.10.10.25");
        init();
    }

    @Test
    public void testS() throws Exception {
        snmpUtil = new SnmpUtil();
        snmpUtil.reset(snmpParam);
        List<OltSniAttribute> snis = snmpUtil.getTable(OltSniAttribute.class, true);
    }
}
