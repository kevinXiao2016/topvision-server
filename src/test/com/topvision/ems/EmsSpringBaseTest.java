/***********************************************************************
 * $ BaseOltServcieTest.java,v1.0 2011-11-28 16:43:50 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems;

import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author jay
 * @created @2011-11-28-16:43:50
 */
public class EmsSpringBaseTest {
    protected static ApplicationContext ctx = null;
    protected static EntityService entityService;
    protected static SnmpExecutorService snmpExecutorService;
    protected static String ip = "172.10.10.47";
    protected static Entity entity;
    protected static SnmpParam snmpParam;
    protected static FacadeFactory facadeFactory;
    protected static SnmpUtil snmpUtil;
    protected static Logger logger = LoggerFactory.getLogger(EmsSpringBaseTest.class);

    @BeforeClass
    public static void setUpClass() {
        String[] configs = { "classpath:META-INF/spring/ac-test.xml",
                "classpath:META-INF/spring/applicationContext-facade.xml",
                "classpath*:com/**/businessContext-message.xml", "classpath*:com/**/businessContext-engine.xml",
                "classpath*:com/**/businessContext-epon.xml", "classpath*:com/**/bc-network-test.xml",
                "classpath*:com/**/bc-template-test.xml", "classpath*:com/**/dc-network-test.xml",
                "classpath*:com/**/dc-platform-test.xml", "classpath*:com/**/dc-template-test.xml",
                "classpath*:com/**/dc-monitor-test.xml", "classpath*:com/**/dc-epon-test.xml",
                "classpath*:com/**/ec-epon-test.xml" };
        ctx = new ClassPathXmlApplicationContext(configs);
        entityService = (EntityService) ctx.getBean("entityService");
        facadeFactory = (FacadeFactory) ctx.getBean("facadeFactory");
        snmpExecutorService = (SnmpExecutorService) ctx.getBean("snmpExecutorService");
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        init();
    }

    public static void init() {
        entity = entityService.getEntityByIp(ip);
        snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
        snmpUtil = new SnmpUtil();
        snmpUtil.reset(snmpParam);
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        EmsSpringBaseTest.ip = ip;
    }
}
