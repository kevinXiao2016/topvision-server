/***********************************************************************
 * $Id: PingServiceImpl.java,v 1.1 Jul 31, 2008 10:23:19 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.PingFacade;
import com.topvision.ems.facade.domain.PingCtlEntry;
import com.topvision.ems.facade.domain.PingResultsEntry;
import com.topvision.exception.facade.DeviceNotExistException;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.exception.engine.SnmpNoSuchObjectException;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date Jul 31, 2008 10:23:19 PM
 * 
 * @author kelers
 * 
 */
@Facade("pingFacade")
public class PingFacadeImpl extends EmsFacade implements PingFacade {
    private static final String SNMP_PING_INDEX = "Topvision";
    @Autowired
    private PingExecutorService pingExecutorService = null;
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.PingService#ping(java.lang.String, int, int)
     */
    @Override
    public int ping(String ip, Integer timeout, Integer count) throws NetworkException {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        worker.setTimeout(timeout);
        worker.setCount(count);
        Future<PingResult> f = pingExecutorService.submit(worker, r);
        try {
            int result = f.get().getResult();
            if (result >= 0) {
                return result;
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
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.PingService# _ping(java.lang.String, int, int,
     *      com.topvision.ems.engine.ping.PingResult)
     */
    @Override
    public PingResult ping(String ip, Integer timeout, Integer count, PingResult result)
            throws DeviceNotExistException, Exception {
        PingWorker worker = new PingWorker(ip, result);
        worker.setTimeout(timeout);
        worker.setCount(count);
        Future<PingResult> f = pingExecutorService.submit(worker, result);
        try {
            result = f.get();
            if (result.getResult() >= 0) {
                return result;
            }
        } catch (InterruptedException e) {
            logger.debug(e.toString(), e);
            throw new PingException(e);
        } catch (ExecutionException e) {
            logger.debug(e.toString(), e);
            throw new PingException(e);
        }
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.PingService#scan(java.util.List, int, int)
     */
    @Override
    public Map<String, String> scan(List<String> ips, Integer timeout, Integer count) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        long bTime = System.currentTimeMillis();
        Map<String, String> reaches = new HashMap<String, String>();
        List<Future<PingResult>> fs = new ArrayList<Future<PingResult>>();
        for (String ip : ips) {
            PingResult r = new PingResult();
            PingWorker worker = new PingWorker(ip, r);
            worker.setTimeout(timeout);
            worker.setCount(count);
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            fs.add(f);
        }
        while (!fs.isEmpty()) {
            for (int i = 0; i < fs.size(); i++) {
                Future<PingResult> f = fs.get(i);
                try {
                    if (f.isDone()) {
                        if (f.get().getResult() >= 0) {
                            reaches.put(f.get().getIp(), "");
                        }
                        fs.remove(f);
                        i--;
                    }
                } catch (InterruptedException e) {
                    logger.debug(e.toString(), e);
                    throw new PingException(e);
                } catch (ExecutionException e) {
                    logger.debug(e.toString(), e);
                    throw new PingException(e);
                }
            }
            try {
                if (!fs.isEmpty()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ie) {
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("******ARP******");
        }
        Process p = null;
        BufferedReader reader = null;
        try {
            ProcessBuilder pb = new ProcessBuilder("arp", "-a");
            pb.redirectErrorStream(true);
            p = pb.start();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!Pattern.matches(".*(([0-9a-fA-F]{2}-){5}+[0-9a-fA-F]){1}+{2}.*", line)
                        || line.indexOf("invalid") != -1) {
                    continue;
                }
                String[] arp = line.trim().split("\\s++");
                if (ips.contains(arp[0])) {
                    reaches.put(arp[0], arp[1].toUpperCase().replace('-', ':'));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            FileUtils.closeQuitely(reader);
            p.destroy();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("ping " + reaches + " device(s), expends " + (System.currentTimeMillis() - bTime) + " Millis");
        }
        result.putAll(reaches);
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.PingService#scan(java.lang.String, int, int)
     */
    @Override
    public Map<String, String> scan(String target, Integer timeout, Integer count) throws Exception {
        List<String> ips = IpUtils.parseIp(target);
        return scan(ips, timeout, count);
    }

    /**
     * @param pingExecutorService
     */
    public void setPingExecutorService(PingExecutorService pingExecutorService) {
        this.pingExecutorService = pingExecutorService;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.PingFacade#snmpPing(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public PingResultsEntry snmpPing(SnmpParam snmpParam, Long entityId, String destIp, String srcIp, Integer timeout,
            Integer count) {
        // GET LAST Ctl
        PingCtlEntry pingCtlEntry = new PingCtlEntry();
        try {
            pingCtlEntry.setPingCtlOwnerIndex(SNMP_PING_INDEX);
            pingCtlEntry.setPingCtlTestName(destIp);
            pingCtlEntry = snmpExecutorService.getTableLineByMutiStringIndex(snmpParam, pingCtlEntry);
        } catch (Exception e) {
            if (e instanceof SnmpNoSuchObjectException) {
                throw e;
            } else {
                pingCtlEntry = null;
            }
        }
        // Send Ctl Config
        if (pingCtlEntry != null) {
            //TODO Check destIp V4/V6
            pingCtlEntry.setPingCtlTargetAddressType(PingCtlEntry.IPV4);
            pingCtlEntry.setPingCtlTargetAddress(destIp);
            pingCtlEntry.setPingCtlProbeCount(count);
            pingCtlEntry.setPingCtlTimeOut(timeout / 1000);
            pingCtlEntry.setPingCtlAdminStatus(1);
            snmpExecutorService.setData(snmpParam, pingCtlEntry);
        } else {
            pingCtlEntry = new PingCtlEntry();
            pingCtlEntry.setPingCtlOwnerIndex(SNMP_PING_INDEX);
            pingCtlEntry.setPingCtlTestName(destIp);
            //TODO Check destIp V4/V6
            pingCtlEntry.setPingCtlTargetAddressType(PingCtlEntry.IPV4);
            pingCtlEntry.setPingCtlTargetAddress(destIp);
            pingCtlEntry.setPingCtlProbeCount(count);
            pingCtlEntry.setPingCtlTimeOut(timeout / 1000);
            pingCtlEntry.setPingCtlAdminStatus(1);
            pingCtlEntry.setPingCtlRowStatus(RowStatus.CREATE_AND_GO);
            snmpExecutorService.setData(snmpParam, pingCtlEntry);
        }
        // Fetch Result
        PingResultsEntry disconnectResult = new PingResultsEntry();
        disconnectResult.setPingResultsAverageRtt(0);
        disconnectResult.setPingResultsMaxRtt(0);
        disconnectResult.setPingResultsMinRtt(0);
        disconnectResult.setPingResultsProbeResponses(0);
        try {
            int waitTimeThreshold = timeout * count + 3000;
            int waitTime = 0;
            while (true) {
                Thread.sleep(4000);
                if (waitTime > waitTimeThreshold) {
                    return disconnectResult;
                }
                try {
                    PingResultsEntry result = new PingResultsEntry();
                    result.setPingCtlOwnerIndex(SNMP_PING_INDEX);
                    result.setPingCtlTestName(destIp);
                    result = snmpExecutorService.getTableLineByMutiStringIndex(snmpParam, result);
                    if (!result.getPingResultsLastGoodProbe().equals("00:00:00:00:00:00:00")) {
                        return result;
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
                waitTime += 4000;
            }
        } catch (Exception e) {
            logger.error("", e);
            return disconnectResult;
        } finally {
            try {
                PingCtlEntry delEntry = new PingCtlEntry();
                delEntry.setPingCtlOwnerIndex(SNMP_PING_INDEX);
                delEntry.setPingCtlTestName(destIp);
                delEntry.setPingCtlRowStatus(RowStatus.DESTORY);
                snmpExecutorService.setData(snmpParam, delEntry);
            } catch (Exception e) {
            }
        }
        // Fetch Result
        /*try {
            PingResultsEntry result = new PingResultsEntry();
            result.setPingCtlOwnerIndex(SNMP_PING_INDEX);
            result.setPingCtlTestName(destIp);
            result = snmpExecutorService.getTableLineByMutiStringIndex(snmpParam, result);
            return result;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                PingCtlEntry delEntry = new PingCtlEntry();
                delEntry.setPingCtlOwnerIndex(SNMP_PING_INDEX);
                delEntry.setPingCtlTestName(destIp);
                delEntry.setPingCtlRowStatus(RowStatus.DESTORY);
                snmpExecutorService.setData(snmpParam, delEntry);
            } catch (Exception e) {
                //logger.debug("", e);
            }
        }*/
    }

}
