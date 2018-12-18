/***********************************************************************
 * $ OltQosServiceImplTest.java,v1.0 2011-11-27 14:25:49 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.service.impl;

/**
 * @author jay
 * @created @2011-11-27-14:25:49
 *
 */

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.qos.service.OltQosService;

public class OltQosServiceImplTest extends EmsSpringBaseTest {
    private OltQosService oltQosService;

    @Before
    public void setUp() {
        oltQosService = (OltQosService) ctx.getBean("oltQosService");
    }

    @Test
    public void testRefreshQosDeviceBaseQosMapTable() throws Exception {
        oltQosService.refreshQosDeviceBaseQosMapTable(entity.getEntityId());
    }

    @Test
    public void testRefreshQosDeviceBaseQosPolicyTable() throws Exception {
        oltQosService.refreshQosDeviceBaseQosPolicyTable(entity.getEntityId());
    }

    @Test
    public void testRefreshQosPortBaseQosMapTable() throws Exception {
        oltQosService.refreshQosPortBaseQosMapTable(entity.getEntityId());
    }

    @Test
    public void testRefreshQosPortBaseQosPolicyTable() throws Exception {
        // TODO 测试不通过 死循环
        oltQosService.refreshQosPortBaseQosPolicyTable(entity.getEntityId());
    }

    @Test
    public void testRefreshSlaTable() throws Exception {
        oltQosService.refreshSlaTable(entity.getEntityId());
    }
}