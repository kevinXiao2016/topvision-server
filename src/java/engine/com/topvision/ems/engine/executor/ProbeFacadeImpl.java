/***********************************************************************
 * $Id: ProbeServiceImpl.java,v 1.1 Jun 8, 2009 11:00:28 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.ProbeFacade;
import com.topvision.exception.facade.DeviceNotExistException;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;

/**
 * @Create Date Jun 8, 2009 11:00:28 PM
 * 
 * @author kelers
 * 
 */
@Facade("probeFacade")
public class ProbeFacadeImpl extends EmsFacade implements ProbeFacade {
    @Autowired
    private PingExecutorService pingExecutorService = null;
    private static byte[] bytes = "victor".getBytes();

    @Override
    public String getHostname(String ip) {
        try {
            String name = InetAddress.getByName(ip).getHostName();
            if (name != null && name.equals(ip)) {
                name = null;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("getHostname:" + ip + "=" + name);
            }
            return name;
        } catch (UnknownHostException e) {
            logger.debug(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int _ping(String ip, Integer tcpPort, Integer udpPort, Integer timeout) throws DeviceNotExistException, Exception {
        return _ping(ip, timeout, tcpPort, DEFAULT_TCP_TIMEOUT, udpPort, DEFAULT_UDP_TIMEOUT);
    }

    @Override
    public int _ping(String ip, Integer pingTimeout, Integer tcpPort, Integer tcpTimeout, Integer udpPort, Integer udpTimeout)
            throws DeviceNotExistException, Exception {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        worker.setTimeout(pingTimeout);
        worker.setCount(1);
        Future<PingResult> f = pingExecutorService.submit(worker, r);
        try {
            int value = f.get().getResult();
            if (value > 0) {
                return value;
            } else if (value == 0) {
                return 1;
            }
        } catch (InterruptedException e) {
            logger.debug(e.toString(), e);
            throw new PingException(e);
        } catch (ExecutionException e) {
            throw new PingException(e);
        }

        long t = System.currentTimeMillis();

        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, tcpPort), tcpTimeout);
            socket.close();
            return (int) (System.currentTimeMillis() - t);
        } catch (Exception ie) {
        }

        DatagramSocket dsock = null;
        try {
            t = System.currentTimeMillis();
            dsock = new DatagramSocket();
            dsock.setSoTimeout(udpTimeout);
            dsock.send(new DatagramPacket(bytes, bytes.length, new InetSocketAddress(ip, udpPort)));
            byte[] data = new byte[16];
            dsock.receive(new DatagramPacket(data, 16));
            dsock.close();
            return (int) (System.currentTimeMillis() - t);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (Exception e) {
        }
        return -1;
    }

    @Override
    public int ping(String ip, Integer timeout, Integer count, Integer retry) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        worker.setTimeout(timeout);
        worker.setCount(count);
        worker.setRetry(retry);
        Future<PingResult> f = pingExecutorService.submit(worker, r);
        try {
            int value = f.get().getResult();
            if (value > 0) {
                return value;
            } else if (value == 0) {
                return 1;
            }
        } catch (InterruptedException e) {
            logger.debug(e.toString(), e);
            throw new PingException(e);
        } catch (ExecutionException e) {
            throw new PingException(e);
        }
        return -1;
    }

    /**
     * @param pingExecutorService
     *            the pingExecutorService to set
     */
    public void setPingExecutorService(PingExecutorService pingExecutorService) {
        this.pingExecutorService = pingExecutorService;
    }
}
