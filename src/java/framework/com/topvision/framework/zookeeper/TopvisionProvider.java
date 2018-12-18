/***********************************************************************
 * $Id: TopvisionProvider.java,v1.0 2015年4月20日 上午9:03:58 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.zookeeper;

import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.auth.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Victor
 * @created @2015年4月20日-上午9:03:58
 *
 */
public class TopvisionProvider implements AuthenticationProvider {
    protected static final Logger logger = LoggerFactory.getLogger(TopvisionProvider.class);

    @Override
    public String getScheme() {
        logger.debug("TopvisionProvider.getScheme");
        return "Topvision";
    }

    @Override
    public Code handleAuthentication(ServerCnxn cnxn, byte[] authData) {
        logger.debug("TopvisionProvider.handleAuthentication");
        return null;
    }

    @Override
    public boolean matches(String id, String aclExpr) {
        logger.debug("TopvisionProvider.matches");
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        logger.debug("TopvisionProvider.isAuthenticated");
        return false;
    }

    @Override
    public boolean isValid(String id) {
        logger.debug("TopvisionProvider.isValid");
        return false;
    }
}
