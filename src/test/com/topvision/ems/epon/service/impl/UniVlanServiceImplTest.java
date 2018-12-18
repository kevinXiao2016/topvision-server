package com.topvision.ems.epon.service.impl;

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.vlan.dao.UniVlanDao;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;

public class UniVlanServiceImplTest extends EmsSpringBaseTest {
    private UniVlanDao uniVlanDao;

    @Before
    public void setUp() {
        uniVlanDao = (UniVlanDao) ctx.getBean("uniVlanDao");
    }

    @Test
    public void testRefreshUniPortVlan() {
        PortVlanAttribute portVlanAttribute = uniVlanDao.getPortVlanAttribute(2L);
        int mode = portVlanAttribute.getVlanMode();
        assert mode == 1;
        if (1 == 1) {
            throw new RuntimeException("Assertion shoule be enable!");
        }
        logger.debug("\ntable.Size:" + portVlanAttribute.getVlanMode());
    }

}
