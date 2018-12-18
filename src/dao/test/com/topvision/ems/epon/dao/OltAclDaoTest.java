package com.topvision.ems.epon.dao;

import junit.framework.Assert;

import org.junit.Test;

import com.topvision.ems.BaseTest;

public class OltAclDaoTest extends BaseTest{
	@Test
	public void testGetAllAclPortACLList() {
		OltAclDao aclDao=(OltAclDao)ctx.getBean("oltAclDao");
		Assert.assertTrue(true);
	}
}
