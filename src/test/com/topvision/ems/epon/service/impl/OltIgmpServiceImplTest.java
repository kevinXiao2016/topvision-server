/***********************************************************************
 * $ OltIgmpServiceImplTest.java,v1.0 2011-11-27 14:15:55 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.service.impl;

/**
 * @author jay
 * @created @2011-11-27-14:15:55
 *
 */

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.igmp.service.OltIgmpService;

public class OltIgmpServiceImplTest extends EmsSpringBaseTest {
    private OltIgmpService oltIgmpService;

    @Before
    public void setUp() {
        oltIgmpService = (OltIgmpService) ctx.getBean("oltIgmpService");
    }

    @Test
    public void testInsertEntityStates() throws Exception {

    }

    @Test
    public void testUpdateEntityStates() throws Exception {
    }

    @Test
    public void testAddIgmpMvlan() throws Exception {
    }

    @Test
    public void testAddIgmpProxy() throws Exception {
    }

    @Test
    public void refreshIgmpControlledMcCdrTable() {

    }

    @Test
    public void refreshIgmpControlledMulticastPackageTable() {
        oltIgmpService.refreshIgmpControlledMulticastPackageTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpControlledMulticastUserAuthorityTable() {
        oltIgmpService.refreshIgmpControlledMulticastUserAuthorityTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpEntityTable() {
        oltIgmpService.refreshIgmpEntityTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpForwardingTable() {
        oltIgmpService.refreshIgmpForwardingTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpMcOnuTable() {
        oltIgmpService.refreshIgmpMcOnuTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpMcOnuVlanTransTable() {
        oltIgmpService.refreshIgmpMcOnuVlanTransTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpMcParamMgmtObjects() {
        oltIgmpService.refreshIgmpMcParamMgmtObjects(entity.getEntityId());
    }

    @Test
    public void refreshIgmpMcSniConfigMgmtObjects() {
        oltIgmpService.refreshIgmpMcSniConfigMgmtObjects(entity.getEntityId());
    }

    @Test
    public void refreshIgmpMcUniConfigTable() {
        oltIgmpService.refreshIgmpMcUniConfigTable(entity.getEntityId());
    }

    @Test
    public void refreshIgmpProxyParaTable() {
        oltIgmpService.refreshIgmpProxyParaTable(entity.getEntityId());
    }

}