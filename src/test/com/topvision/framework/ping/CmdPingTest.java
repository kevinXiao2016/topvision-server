/***********************************************************************
 * $Id: CmdPingTest.java,v1.0 2013-11-8 上午11:27:14 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Victor
 * @created @2013-11-8-上午11:27:14
 *
 */
public class CmdPingTest {
    Ping ping = null;
    String host = "172.16.30.15";
    int timeout = 2000;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ping = new CmdPing();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.topvision.framework.ping.CmdPing#myOnline(java.lang.String, int)}.
     */
    @Test
    public void testMyOnline() {
        for (int i = 0; i < 2; i++) {
            System.out.println("testMyOnline.Ping " + host + ">" + ping.online(host, timeout, 1));
        }
    }

    /**
     * Test method for {@link com.topvision.framework.ping.CmdPing#myPing(java.lang.String, int)}.
     */
    @Test
    public void testMyPing() {
        for (int i = 0; i < 2; i++) {
            System.out.println("testMyPing.Ping " + host + ">" + ping.ping(host, timeout, 1));
        }
    }
}
