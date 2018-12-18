package com.topvision.ems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.MacUtils;

/**
 * 前几次测试结果表明，加判断影响 后面几次测试表明，加判断，影响不大
 * 
 * @author Administrator
 * 
 */
public class TestLog {
    protected static Logger logger = LoggerFactory.getLogger(TestLog.class);

    public static void main(String[] args) {
        MacUtils macUtil = new MacUtils("24:76:7D:06:D0:84");
        System.out.println(macUtil.longValue());
        /*Long cp = 0l;
        Long date1 = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            logger.debug("1111");
            logger.info("ss");
        }
        Long date2 = System.currentTimeMillis();
        cp = date2 - date1;
        date1 = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("1111");
            }
            logger.info("ss");
        }
        date2 = System.currentTimeMillis();
        System.out.println("first " + cp + ", second" + (date2 - date1));*/
    }
}
