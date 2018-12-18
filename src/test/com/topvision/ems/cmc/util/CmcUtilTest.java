/***********************************************************************
 * $Id: TypeChangeTest.java,v1.0 2011-11-29 下午05:07:30 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Administrator
 * @created @2011-11-29-下午05:07:30
 * 
 */
public class CmcUtilTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

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
     * Test method for {@link com.topvision.ems.cmc.util.TypeChange#timeFormat(long)}.
     */
    @Test
    public void testTimeFormat() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.topvision.ems.cmc.util.TypeChange#turnToEndWithKOrM(long)}.
     */
    @Test
    public void testTurnToEndWithKOrM() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.topvision.ems.cmc.util.TypeChange#turnToPercent(long, long)}.
     */
    @Test
    public void testTurnToPercent() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.topvision.ems.cmc.util.TypeChange#turnToMacType(java.lang.String)}
     * .
     */
    @Test
    public void testTurnToMacType() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.cmc.util.TypeChange#turnBitsToString(java.lang.String, java.lang.String[])}
     * .
     */
    @Test
    public void testTurnBitsToString() {
        String[] BITMAP = { "rulePriority", "activationState", "ipTos", "ipProtocol", "ipSourceAddr", "ipSourceMask",
                "ipDestAddr", "ipDestMask", "sourcePortStart", "sourcePortEnd", "destPortStart", "destPortEnd",
                "destMac", "sourceMac", "ethertype", "userPri", "vlanId" };
        assertEquals(CmcUtil.turnBitsToString("00:00:00:0", BITMAP), "");
    }

}
