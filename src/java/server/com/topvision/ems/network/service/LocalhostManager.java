package com.topvision.ems.network.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;
import com.topvision.platform.SystemConstants;

public class LocalhostManager {
    protected static final Logger logger = LoggerFactory.getLogger(LocalhostManager.class);
    private static LocalhostManager manager = new LocalhostManager();

    public static LocalhostManager getInstance() {
        return manager;
    }

    private InetAddress[] allAddrs = null;
    private List<String> macs = new ArrayList<String>();

    public InetAddress[] getAllAddrs() {
        return allAddrs;
    }

    public List<String> getMacs() {
        for (int i = 0; i < allAddrs.length; i++) {
            if (!"127.0.0.1".equals(allAddrs[i].getHostAddress())) {
            }
        }

        return macs;
    }

    public void initialize() {
        String rmiServer = SystemConstants.getInstance().getStringParam("java.rmi.server.hostname",
                EnvironmentConstants.getHostAddress());
        System.setProperty("java.rmi.server.hostname", rmiServer);
        logger.info("Use {} as rmi server hostname.", rmiServer);
    }
}
