/***********************************************************************
 * $Id: OltServiceImplTest.java,v1.0 2011-9-19 下午01:55:58 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.service.impl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.topvision.ems.EmsSpringBaseTest;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.epon.topology.facade.OltDiscoveryFacade;
import com.topvision.ems.epon.trunk.service.OltTrunkService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * @author jay
 * @created @2011-9-19-下午01:55:58
 */
public class OltServiceImplTest extends EmsSpringBaseTest {
    private OltService oltService;
    private OltTrunkService oltTrunkService;

    @Before
    public void setUp() {
        oltService = (OltService) ctx.getBean("oltService");
    }

    @Test
    public void trunkTest() throws Exception {
        // List<OltSniTrunkConfig> oltSniTrunkConfigList = oltDao.getTrunkConfigList(30000000000L);
        // System.out.println("oltSniTrunkConfigList.size() = " + oltSniTrunkConfigList.size());
        // //TODO CASE 可用端口查询
        List<OltSniAttribute> sniList = oltTrunkService.availableSniList(30000000000L, null, 1L);
        if (logger.isInfoEnabled()) {
            logger.info("sniList.size() = " + sniList.size());
        }
        for (OltSniAttribute oltSniAttribute : sniList) {
            if (logger.isInfoEnabled()) {
                logger.info("oltSniAttribute = " + oltSniAttribute);
            }
        }
        // TODO CASE 添加一个trunk
        // OltSniTrunkConfig oltSniTrunkConfig = new OltSniTrunkConfig();
        // oltSniTrunkConfig.setEntityId(30000000000L);
        // oltSniTrunkConfig.setSniTrunkGroupConfigName("trunk2");
        // oltSniTrunkConfig.setSniTrunkGroupConfigPolicy(3);
        // List<Long> al = new ArrayList<Long>();
        // al.add(4311744512L);
        // al.add(4328521728L);
        // al.add(4345298944L);
        // oltSniTrunkConfig.setSniTrunkGroupConfigGroup(al);
        // OltSniTrunkConfig oltSniTrunkConfigNew = oltService.addSniTrunkConfig(oltSniTrunkConfig);
        // System.out.println("oltSniTrunkConfigNew = " + oltSniTrunkConfigNew);

        // TODO CASE 修改turnk member
        // OltSniTrunkConfig oltSniTrunkConfig = new OltSniTrunkConfig();
        // oltSniTrunkConfig.setEntityId(30000000000L);
        // oltSniTrunkConfig.setSniTrunkGroupConfigIndex(101);
        // oltSniTrunkConfig.setSniTrunkGroupConfigName("trunk2");
        // oltSniTrunkConfig.setSniTrunkGroupConfigPolicy(3);
        // List<Long> al = new ArrayList<Long>();
        // al.add(4311744512L);
        // al.add(4328521728L);
        // oltSniTrunkConfig.setSniTrunkGroupConfigGroup(al);
        // oltService.modifySniTrunkConfig(oltSniTrunkConfig);
        // TODO CASE 修改turnk 名字 policy
        // TODO CASE 删除trunk
        // List<OltSniTrunkConfig> list = oltService.getTrunkConfigList(30000000000L);
        // if (logger.isInfoEnabled()) {
        // logger.info("list = " + list.size());
        // }
        // for (OltSniTrunkConfig oltSniTrunkConfig : list) {
        // if (logger.isInfoEnabled()) {
        // logger.info("oltSniTrunkConfig = " + oltSniTrunkConfig);
        // }
        // }

    }


    @Test
    public void testPonAttribute() throws Exception {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
        SnmpUtil snmpUtil = new SnmpUtil(snmpParam);
        List<OltPonAttribute> pons = snmpUtil.getTable(OltPonAttribute.class, true);
    }

    @Test
    public void testOnuAttribute() throws Exception {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
        SnmpUtil snmpUtil = new SnmpUtil(snmpParam);
        List<OltOnuAttribute> onus = snmpUtil.getTable(OltOnuAttribute.class, true);
    }

    /**
     * 获取OltDiscoveryFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltDiscoveryFacade
     */
    private OltDiscoveryFacade getOltDiscoveryFacade(String ip) {
        return facadeFactory.getFacade(ip, OltDiscoveryFacade.class);
    }
}