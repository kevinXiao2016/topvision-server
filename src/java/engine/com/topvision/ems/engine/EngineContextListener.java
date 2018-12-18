/***********************************************************************
 * $Id: EngineContextListener.java,v 1.1 2008-4-30 下午06:32:17 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Create Date 2008-4-30 下午06:32:17
 * 
 * @author kelers
 * 
 */
public class EngineContextListener implements ServletContextListener {
    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public final void contextInitialized(ServletContextEvent event) {
    }
}
