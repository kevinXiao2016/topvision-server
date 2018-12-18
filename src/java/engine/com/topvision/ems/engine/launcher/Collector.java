/***********************************************************************
 * $Id: Collector.java,v 1.1 Mar 12, 2009 7:21:58 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.launcher;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;

/**
 * @Create Date Mar 12, 2009 7:21:58 PM
 * 
 * @author kelers
 * 
 */
public class Collector {
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        new Collector().start(args);
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void start(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                logger.error(args[i]);
                if ("-port".equalsIgnoreCase(args[i])) {
                    EnvironmentConstants.putEnv(EnvironmentConstants.ENGINE_PORT, args[++i]);
                }
            }
        }
        long l = System.currentTimeMillis();
        File file = new File(".");
        if (!new File(file, "mibs").exists()) {
            EnvironmentConstants.putEnv(EnvironmentConstants.ENGINE_HOME,
                    new File("./webapp/WEB-INF/").getAbsolutePath());
            EnvironmentConstants.putEnv(EnvironmentConstants.DLL_HOME,
                    new File("./webapp/WEB-INF/dll").getAbsolutePath());
            EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME,
                    new File("./webapp/WEB-INF/mibs").getAbsolutePath());
            EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME,
                    new File("./webapp/WEB-INF/mibs").getAbsolutePath());
            EnvironmentConstants.putEnv("configFile", "classpath:META-INF/spring/applicationContext-dev.xml");
        } else {
            EnvironmentConstants.putEnv(EnvironmentConstants.ENGINE_HOME, file.getAbsolutePath());
            EnvironmentConstants.putEnv(EnvironmentConstants.DLL_HOME, file.getAbsolutePath() + "/dll");
            EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, file.getAbsolutePath() + "/mibs");
            EnvironmentConstants.putEnv("configFile", "classpath:META-INF/spring/applicationContext-engine.xml");
        }
        logger.info("ENGINE_HOME={}", EnvironmentConstants.getEnv(EnvironmentConstants.ENGINE_HOME));
        if (args.length != 0 && "stop".equals(args[0])) {
            CollectorContext.getInstance().stopService();
        } else {
            CollectorContext.getInstance().startService();
            logger.info("Collector startup in " + (System.currentTimeMillis() - l) + " ms");
        }
    }
}
