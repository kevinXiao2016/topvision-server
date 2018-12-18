/***********************************************************************
 * $Id: EponCodeTest.java,v1.0 2012-2-2 上午10:54:04 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Victor
 * @created @2012-2-2-上午10:54:04
 * 
 */
public class EponCodeTest {
    static EponCode code = new EponCode();

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        code.setType(0);
        code.initialize();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * Test method for {@link com.topvision.ems.epon.fault.EponCode#translation(int)}.
     */
    @Test
    public void testTranslation() {
        assertEquals(EponCode.ONU_ONLINE, 29194);
        code.setType(1);
        code.initialize();
        assertEquals(EponCode.ONU_ONLINE, 129194);
        code.setType(2);
        code.initialize();
        assertEquals(EponCode.ONU_ONLINE, 129194);
    }
}
