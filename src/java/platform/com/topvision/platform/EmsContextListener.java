/***********************************************************************
 * $Id: EmsContextListener.java,v 1.1 2008-4-30 下午06:32:17 kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.common.FileUtils;
import com.topvision.platform.service.ThemeManager;
import com.topvision.platform.util.DllUtils;

/**
 * @author kelers
 * @Create Date 2008-4-30 下午06:32:17
 */
public class EmsContextListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(EmsContextListener.class);

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // 释放当前在线用户环境
        // UserContextManager.getInstance().destroy();
        // 释放动态链接库
        new DllUtils().freeLibrary();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public final void contextInitialized(ServletContextEvent event) {
        printEnv();

        ServletContext sc = event.getServletContext();
        loadSystemConstants(sc);
        loadThemeManager(sc);

        //UserContextManager.getInstance().loadModules();

        if (logger.isInfoEnabled()) {
            logger.info("Starting " + sc.getServletContextName() + " ...");
        }
    }

    /**
     * 加载系统基本路径信息,以及config.properties中的配置信息 ---- @Bravin
     * 
     * @param sc
     */
    private void loadSystemConstants(ServletContext sc) {
        SystemConstants constants = SystemConstants.getInstance();
        InputStream is = null;
        try {
            is = sc.getResourceAsStream("/WEB-INF/conf/config.properties");
            constants.initialize(is);
        } catch (IOException ex) {
            logger.error("Load WEB-INF/conf/config.properties unsuccessfully!", ex);
        } finally {
            FileUtils.closeQuitely(is);
        }
        SystemConstants.ROOT_REAL_PATH = sc.getRealPath("/");
        int len = SystemConstants.ROOT_REAL_PATH.length();
        char ch = SystemConstants.ROOT_REAL_PATH.charAt(len - 1);
        if (ch == '/' || ch == '\\') {
        } else {
            SystemConstants.ROOT_REAL_PATH = SystemConstants.ROOT_REAL_PATH + File.separatorChar;
        }

        SystemConstants.WEB_INF_REAL_PATH = sc.getRealPath("WEB-INF");
        len = SystemConstants.WEB_INF_REAL_PATH.length();
        ch = SystemConstants.WEB_INF_REAL_PATH.charAt(len - 1);
        if (ch == '/' || ch == '\\') {
        } else {
            SystemConstants.WEB_INF_REAL_PATH = SystemConstants.WEB_INF_REAL_PATH + File.separatorChar;
        }

        String dir = sc.getRealPath("WEB-INF");
        dir = dir.replaceAll("\\\\", "/");
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        EnvironmentConstants.putEnv(EnvironmentConstants.ENGINE_HOME, dir);
        EnvironmentConstants.putEnv(EnvironmentConstants.DRIVER_HOME, dir + "drivers/");
        if (SystemConstants.isDevelopment) {
            EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, dir + "../../../topvision-common/mibs/");
        } else {
            EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, dir + "mibs/");
        }
        EnvironmentConstants.putEnv(EnvironmentConstants.DLL_HOME, dir + "dll/");
        if (logger.isInfoEnabled()) {
            logger.info("EnvironmentConstants=" + EnvironmentConstants.showEnvs());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("System Constants: ");
            logger.debug(SystemConstants.getInstance().getSystemParam().toString());
        }
    }

    /**
     * 加载 界面主题 样式配置信息 ---- @Bravin
     * 
     * @param sc
     */
    private void loadThemeManager(ServletContext sc) {
        InputStream is = null;
        try {
            is = sc.getResourceAsStream("/WEB-INF/conf/theme.properties");
            ThemeManager.initialize(is);
        } catch (IOException ex) {
            logger.error("Load WEB-INF/conf/theme.properties unsuccessfully!", ex);
        } finally {
            FileUtils.closeQuitely(is);
        }
    }

    /**
     * 得到 java虚拟机当前的运行环境
     */
    private void printEnv() {
        if (logger.isDebugEnabled()) {
            StringBuilder env = new StringBuilder("System Properties:");
            Properties prop = System.getProperties();
            for (Object key : prop.keySet()) {
                env.append("\n").append(key).append("=").append(prop.get(key));
            }
            env.append("\nSystem environment:");
            Map<String, String> map = System.getenv();
            for (String key : map.keySet()) {
                env.append("\n").append(key).append("=").append(map.get(key));
            }
            logger.debug(env.toString());
        }
    }

}
