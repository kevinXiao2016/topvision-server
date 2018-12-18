/***********************************************************************
 * $Id: EponIndexTest.java,v1.0 2011-9-28 上午08:51:39 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;

/**
 * 0x0203150103=8641642755L
 * 
 * @author Victor
 * @created @2011-9-28-上午08:51:39
 * 
 */
public class EponIndexTest {
    private long l = 8641642755L;

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#EponIndex(java.lang.Long)}.
     */
    @Test
    public void testEponIndexLong() {
        assertEquals(EponIndex.getUniIndex(l).longValue(), l);
    }

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#EponIndex(java.lang.Integer[])}
     * .
     */
    @Test
    public void testEponIndexIntegerArray() {
        EponIndex index = new EponIndex(2);
        assertEquals(index.getUniIndex().longValue(), 8589934592L);
        index = new EponIndex(2, 3);
        assertEquals(index.getUniIndex().longValue(), 8640266240L);
        index = new EponIndex(2, 3, 21);
        assertEquals(index.getUniIndex().longValue(), 8641642496L);
        index = new EponIndex(2, 3, 21, 1, 3);
        assertEquals(index.getUniIndex().longValue(), l);
    }

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#getSlotIndex()}.
     */
    @Test
    public void testGetSlotIndex() {
        assertEquals(EponIndex.getSlotIndex(l).longValue(), 0x0200000000L);
    }

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#getSlotNo()}.
     */
    @Test
    public void testGetSlotNo() {
        assertEquals(EponIndex.getSlotNo(l).longValue(), 2);
    }

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#getPonIndex()}.
     */
    @Test
    public void testGetPonIndex() {
        assertEquals(EponIndex.getPonIndex(l).longValue(), 0x0203000000L);
    }

    /**
     * Test method for {@link com.topvision.framework.utils.EponIndex#getPonNo()}
     * .
     */
    @Test
    public void testGetPonNo() {
        assertEquals(EponIndex.getPonNo(l).longValue(), 3);
    }

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#getOnuIndex()}.
     */
    @Test
    public void testGetOnuIndex() {
        assertEquals(EponIndex.getOnuIndex(l).longValue(), 0x0203150000L);
    }

    /**
     * Test method for {@link com.topvision.framework.utils.EponIndex#getOnuNo()}
     * .
     */
    @Test
    public void testGetOnuNo() {
        assertEquals(EponIndex.getOnuNo(l).longValue(), 21);
    }

    /**
     * Test method for
     * {@link com.topvision.framework.utils.EponIndex#getUniIndex()}.
     */
    @Test
    public void testGetUniIndex() {
        assertEquals(EponIndex.getUniIndex(l).longValue(), 0x0203150103L);
    }

    /**
     * Test method for {@link com.topvision.framework.utils.EponIndex#getUniNo()}
     * .
     */
    @Test
    public void testGetUniNo() {
        assertEquals(EponIndex.getUniNo(l).longValue(), 3);
    }

    @Test
    public void testNewSlotIndex() {
        // System.out.println(EponIndex.getAclPonIndexByPonIndex(51556384768L));
        Long portIndex = 33554432L;
        System.out.println(((portIndex & 0xFF00000000L) >> 5) | ((portIndex & 0xFF000000L) >> 1));
        Long ifIndex = 16777216L;
        System.out.println(((ifIndex & 0xF8000000) << 5) | ((ifIndex & 0x7800000) << 1));

        //System.out.println(EponUtil.getStrFromMap(EponUtil.getMapfromStr("1-4,6", 8)));
    }   
}
