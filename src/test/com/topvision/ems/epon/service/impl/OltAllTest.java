/***********************************************************************
 * $ OltAllTest.java,v1.0 2011-11-28 17:14:21 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.service.impl;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

/**
 * @author jay
 * @created @2011-11-28-17:14:21
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ OltAclServiceImplTest.class, OltPerfServiceImplTest.class, OltQosServiceImplTest.class,
        OltServiceImplTest.class, UniVlanServiceImplTest.class, OltIgmpServiceImplTest.class })
public class OltAllTest {
}
