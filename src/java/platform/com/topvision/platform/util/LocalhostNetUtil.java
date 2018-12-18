/***********************************************************************
 * $Id: HostIpUtil.java,v1.0 2013-1-23 下午2:20:58 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fanzidong
 * @created @2013-1-23-下午2:20:58
 *
 */
public class LocalhostNetUtil {
    
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    
    private static Logger logger = LoggerFactory.getLogger(LocalhostNetUtil.class);
	
    /**
     * 获得机器上的所有IP地址
     * @return
     */
    public static List<String> getInetAddress(){
        List<String> ip = new ArrayList<String>();
        //网络接口类
        Enumeration<NetworkInterface> nis = null;
        try {
			nis = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			System.err.println("cannot get netWorkInterfaces!");
			ip.add("127.0.0.1");
			return ip;
		} 
        while (nis.hasMoreElements()) {
        	NetworkInterface ni = nis.nextElement();
        	//得到这个网络接口上的所有的InetAddress
            Enumeration<InetAddress> ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
            	InetAddress ia = ias.nextElement();
            	if (ia instanceof Inet4Address) {
            		if(!ia.getHostAddress().equalsIgnoreCase("127.0.0.1")){
            			ip.add(ia.getHostAddress());
            		}
                }
            }
        }
        return ip;
    }
    
    public static boolean checkTcpPortUsage(int port){
        return checkProtocolPortUsage(TCP, port);
    }
    
    public static boolean checkUdpPortUsage(int port){
        return checkProtocolPortUsage(UDP, port);
    }
    
    /**
     * 检查系统的TCP/UDP端口是否被占用
     * @param type
     * @param port
     * @return
     */
    private static boolean checkProtocolPortUsage(String type, int port){
        if (type.equals(TCP)) {
        	//尝试绑定此端口，如果出现异常，表明端口被占用
            ServerSocket sSocket = null;
            try {
                sSocket = new ServerSocket(port);
                logger.debug("bind TCP port: " + port + " successfully!");
            } catch (IOException e) {
            	logger.debug("TCP port: " + port + " has been used!");
                return true;
            } finally {
                if (sSocket != null) {
                    try {
                        sSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (type.equals(UDP)) {
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket(port);
                logger.debug("bind UDP port: " + port + " successfully!");
            } catch (SocketException e) {
            	logger.debug("UDP port: " + port + " has been used!");
                return true;
            } finally {
                if (ds != null) {
                    ds.close();
                }
            }
        }
        return false;
    }

    public Logger getLogger() {
        return logger;
    }
    
}
