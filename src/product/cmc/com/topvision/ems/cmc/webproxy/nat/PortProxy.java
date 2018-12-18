/***********************************************************************
 * PortProxy.java,v1.0 17-5-22 上午10:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.nat;

import com.topvision.ems.cmc.webproxy.domain.CmWebPortMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sockslib.client.Socks5;
import sockslib.client.SocksProxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author jay
 * @created 17-5-22 上午10:47
 */
public class PortProxy extends Thread {
    private Logger logger = LoggerFactory.getLogger(PortProxy.class);
    private static Map<String,SocksProxy> proxyMap = Collections.synchronizedMap(new HashMap<String,SocksProxy>());
    private CmWebPortMap portMap;
    private ServerSocket proxySocket;
    private Boolean stop = false;
    private Boolean ready = false;

    public PortProxy(CmWebPortMap portMap) {
        this.portMap = portMap;
    }

    @Override
    public void run() {
        try {
            proxySocket = new ServerSocket(0);
            ready = true;
            portMap.setForwoard(proxySocket.getLocalPort());
            logger.debug("portMap:{} ", portMap);
            while (true) {
                Socket clientSocket = proxySocket.accept();
                if (!stop) {
                    SocksProxy proxy;
                    if (PortProxy.proxyMap.containsKey(portMap.getProxyKey())) {
                        proxy = PortProxy.proxyMap.get(portMap.getProxyKey());
                    } else {
                        proxy = new Socks5(new InetSocketAddress(portMap.getProxyIp(), portMap.getProxyPort()));
                        PortProxy.proxyMap.put(portMap.getProxyKey(), proxy);
                    }
                    new NatGroup(clientSocket,portMap,proxy);
                }
            }
        } catch (IOException e) {
            stopPortProxy();
            logger.error("",e);
        }
    }

    public void startServer() {
        start();
    }

    public void stopPortProxy() {
        stop = true;
        try {
            proxySocket.close();
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            interrupt();
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    public void checkStatus() {
        while (!ready) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {}
        }
        portMap.setStatus(!proxySocket.isClosed());
    }

    public CmWebPortMap getPortMap() {
        return portMap;
    }

    public void setPortMap(CmWebPortMap portMap) {
        this.portMap = portMap;
    }

    public List<Long> getHeartbeatIds() {
        return portMap.getHeartbeatIds();
    }

    public Integer heartbeatNum() {
        return portMap.heartbeatNum();
    }

    public void addHeartbeatId(Long heartbeatId) {
        portMap.addHeartbeatId(heartbeatId);
    }

    public void releaseHeartbeatId(Long heartbeatId) {
        portMap.releaseHeartbeatId(heartbeatId);
    }

    @Override
    public String toString() {
        return "PortProxy{" + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;portMap=" + portMap + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;proxySocket=" + proxySocket + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;stop=" + stop + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;ready=" + ready + "<br>" +
                '}';
    }
}
