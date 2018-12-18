/***********************************************************************
 * $ OltAclServiceImplTest.java,v1.0 2011-11-27 14:23:29 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.service.impl;

/**
 * @author jay
 * @created @2011-11-27-14:23:29
 *
 */

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.acl.service.OltAclService;

public class OltAclServiceImplTest extends EmsSpringBaseTest {
    private OltAclService oltAclService;

    @Before
    public void setUp() {
        oltAclService = (OltAclService) ctx.getBean("oltAclService");
    }

    @Test
    public void testRefreshAclListTable() throws Exception {
        oltAclService.refreshAclListTable(entity.getEntityId());
    }

    @Test
    public void testRefreshAclRuleList() throws Exception {
        oltAclService.refreshAclRuleList(entity.getEntityId());
    }

    @Test
    public void testRefreshAclPortACLList() throws Exception {
        oltAclService.refreshAclPortACLList(entity.getEntityId());
    }

}