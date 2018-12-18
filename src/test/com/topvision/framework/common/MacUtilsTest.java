/***********************************************************************
 * $Id: MacUtilsTest.java,v1.0 2013-11-11 下午1:48:13 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Victor
 * @created @2013-11-11-下午1:48:13
 *
 */
public class MacUtilsTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.topvision.framework.common.MacUtils#MacUtils(java.lang.String)}.
     */
    @Test
    public void testMacUtilsString() {
        MacUtils mac = new MacUtils("00-21-CC-BE-77-09");
        assertEquals("00:21:CC:BE:77:09", mac.toString());
        mac = new MacUtils(mac.getBytes());
        assertEquals("00:21:CC:BE:77:09", mac.toString());
        mac = new MacUtils(mac.longValue());
        assertEquals("00:21:CC:BE:77:09", mac.toString());
    }

}
