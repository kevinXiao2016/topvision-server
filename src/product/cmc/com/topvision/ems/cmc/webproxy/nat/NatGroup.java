/***********************************************************************
 * NatGroup.java,v1.0 17-5-22 上午10:45 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.nat;

import com.topvision.ems.cmc.webproxy.domain.CmWebPortMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sockslib.client.SocksProxy;
import sockslib.client.SocksSocket;
import sockslib.common.net.MonitorSocketWrapper;
import sockslib.common.net.NetworkMonitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author jay
 * @created 17-5-22 上午10:45
 */
public class NatGroup implements Runnable {
    private Logger logger = LoggerFactory.getLogger(NatGroup.class);
    private P2PChannel downChannel;
    private P2PChannel upChannel;
    private Socket clientSocket;
    private Socket serverSocket;
    private CmWebPortMap portMap;
    private SocksProxy proxy;

    public NatGroup(Socket clientSocket, CmWebPortMap portMap, SocksProxy proxy) {
        this.clientSocket = clientSocket;
        this.portMap = portMap;
        this.proxy = proxy;
        new Thread(this).start();
    }
    public void run() {
        try {
            if (proxy != null) {
                serverSocket = new SocksSocket(proxy, new InetSocketAddress(portMap.getIp(), portMap.getPort()));
                NetworkMonitor networkMonitor = new NetworkMonitor();
                serverSocket = MonitorSocketWrapper.wrap(serverSocket, networkMonitor);
            } else {
                serverSocket = new Socket(portMap.getIp(), portMap.getPort());
            }
            downChannel = new P2PChannel(clientSocket,serverSocket,portMap.getIp(), portMap.getPort(),portMap.getNatIp(),portMap.getForwoard());
            upChannel = new P2PChannel(serverSocket,clientSocket);
            downChannel.start();
            upChannel.start();
        }catch (Exception e) {
            logger.debug("",e);
        }
    }

    public void closeAll() {
        try {
            downChannel.channelClose();
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            upChannel.channelClose();
        } catch (Exception e) {
            logger.debug("", e);
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.debug("", e);
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.debug("",e);
        }
    }

    @Override
    public String toString() {
        return "NatGroup{" + "<br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;downChannel=" + downChannel + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;upChannel=" + upChannel + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;portMap=" + portMap + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;proxy=" + proxy != null ? "" + proxy.getInetAddress().getHostAddress() + ":" + proxy.getPort() : "null" + "<br>" +
                '}';
    }
}
