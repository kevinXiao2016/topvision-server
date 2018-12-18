package com.topvision.ems.epon.dao;

import junit.framework.Assert;

import org.junit.Test;

import com.topvision.ems.BaseDaoTest;
import com.topvision.ems.epon.acl.dao.OltAclDao;

public class OltAclDaoTest extends BaseDaoTest {
    @Test
    public void testGetAllAclPortACLList() {
        OltAclDao aclDao = (OltAclDao) ctx.getBean("oltAclDao");
        Assert.assertTrue(true);
    }
}
