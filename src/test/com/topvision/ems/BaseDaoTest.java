package com.topvision.ems;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseDaoTest {
    protected static ApplicationContext ctx = null;
    protected static Logger logger = LoggerFactory.getLogger(BaseDaoTest.class);

    @BeforeClass
    public static void setUpClass() {
        String[] configs = { "classpath:com/conf/applicationContext.xml", "classpath*:com/**/daoContext-*.xml", };
        ctx = new ClassPathXmlApplicationContext(configs);
    }
}
