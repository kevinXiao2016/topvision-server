/***********************************************************************
 * $ OltPerfServiceImplTest.java,v1.0 2011-11-28 13:40:24 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.service.impl;

/**
 * @author jay
 * @created @2011-11-28-13:40:24
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.domain.OltSimplePort;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.performance.service.OltPerfService;

public class OltPerfServiceImplTest extends EmsSpringBaseTest {
    private OltPerfService oltPerfService;
    private OltSlotService oltSlotService;

    @Before
    public void setUp() {
        oltPerfService = (OltPerfService) ctx.getBean("oltPerfService");
    }

    @Test
    public void testRefreshPerfStatCycle() throws Exception {
        oltPerfService.refreshPerfStatCycle(entity.getEntityId());
    }

    @Test
    public void testRefreshPerfThreshold() throws Exception {
        // TODO 测试不通过 mib无数据
        oltPerfService.refreshPerfThreshold(entity.getEntityId());
    }

    @Test
    public void testRefreshPerfStatsGlobalSet() throws Exception {
        oltPerfService.refreshPerfStatsGlobalSet(entity.getEntityId());
    }

    @Test
    public void testRefreshPerf24FromFacade() throws Exception {
        OltService oltService = (OltService) ctx.getBean("oltService");
        List<OltSniAttribute> list = oltSlotService.getSlotSniList(45L);
        List<OltSimplePort> oltSimplePorts = new ArrayList<OltSimplePort>();
        for (OltSniAttribute attribute : list) {
            if (attribute.getSniOperationStatus() == 1) {
                OltSimplePort oltSimplePort = new OltSimplePort();
                oltSimplePort.setEntityId(entity.getEntityId());
                oltSimplePort.setPortId(attribute.getSniId());
                oltSimplePort.setPortIndex(attribute.getSniIndex());
                oltSimplePorts.add(oltSimplePort);
            }
        }
        for (OltSimplePort oltSimplePort : oltSimplePorts) {
            oltPerfService.refreshPerf24FromFacade(oltSimplePort);
        }
    }

    @Test
    public void testRefreshPerf15FromFacade() throws Exception {
        OltService oltService = (OltService) ctx.getBean("oltService");
        List<OltSniAttribute> list = oltSlotService.getSlotSniList(45L);
        List<OltSimplePort> oltSimplePorts = new ArrayList<OltSimplePort>();
        for (OltSniAttribute attribute : list) {
            if (attribute.getSniOperationStatus() == 1) {
                OltSimplePort oltSimplePort = new OltSimplePort();
                oltSimplePort.setEntityId(entity.getEntityId());
                oltSimplePort.setPortId(attribute.getSniId());
                oltSimplePort.setPortIndex(attribute.getSniIndex());
                oltSimplePorts.add(oltSimplePort);
            }
        }
        for (OltSimplePort oltSimplePort : oltSimplePorts) {
            oltPerfService.refreshPerf15FromFacade(oltSimplePort);
        }
    }
}