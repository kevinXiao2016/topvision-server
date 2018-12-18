/***********************************************************************
 * $Id: OltTrapConfigTest.java,v1.0 Nov 23, 2011 11:14:41 AM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.facade.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.topvision.ems.epon.fault.domain.OltTrapConfig;

/**
 * @author Victor
 * @created @Nov 23, 2011-11:14:41 AM
 * 
 */
public class OltTrapConfigTest {
    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.domain.OltTrapConfig#getEponManagementAddrTAddress()}.
     */
    @Test
    public void testGetEponManagementAddrTAddress() {
        OltTrapConfig trapConfig = new OltTrapConfig();
        trapConfig.setAddrTAddress("172.10.10.50");
        trapConfig.setAddrTPort(1);
        assertEquals("AC:0A:0A:32:00:00:00:01", trapConfig.getEponManagementAddrTAddress().toUpperCase());
        trapConfig = new OltTrapConfig();
        trapConfig.setAddrTAddress("172.10.10.50");
        // trapConfig.setAddrTPort(162);//默认值，不用设置
        assertEquals("AC:0A:0A:32:00:00:00:A2", trapConfig.getEponManagementAddrTAddress().toUpperCase());
        trapConfig = new OltTrapConfig();
        trapConfig.setAddrTAddress("172.10.10.50");
        trapConfig.setAddrTPort(3255);
        assertEquals("AC:0A:0A:32:00:00:0C:B7", trapConfig.getEponManagementAddrTAddress().toUpperCase());
        trapConfig = new OltTrapConfig();
        trapConfig.setAddrTAddress("172.10.10.50");
        trapConfig.setAddrTPort(65535);
        assertEquals("AC:0A:0A:32:00:00:FF:FF", trapConfig.getEponManagementAddrTAddress().toUpperCase());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.domain.OltTrapConfig#setEponManagementAddrTAddress(java.lang.String)}
     * .
     */
    @Test
    public void testSetEponManagementAddrTAddress() {
        OltTrapConfig trapConfig = new OltTrapConfig();
        trapConfig.setEponManagementAddrTAddress("AC:0A:0A:32:00:00:00:A2");
        assertEquals("172.10.10.50", trapConfig.getAddrTAddress());
        assertEquals(162, trapConfig.getAddrTPort().intValue());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.domain.OltTrapConfig#setAddrTAddress(java.lang.String)}.
     */
    @Test
    public void testSetAddrTAddress() {
        OltTrapConfig trapConfig = new OltTrapConfig();
        trapConfig.setAddrTAddress("172.10.10.50");
        assertEquals("49.55.50.46.49.48.46.49.48.46.53.48", trapConfig.getEponManagementAddrName());
    }
}
