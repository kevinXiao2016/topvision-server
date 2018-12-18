/***********************************************************************
 * $Id: SnmpTrapManager.java,v 1.1 Jul 25, 2008 10:40:38 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.framework.annotation.Engine;
import com.topvision.framework.exception.engine.TrapConfigException;

/**
 * @Create Date Jul 25, 2008 10:40:38 AM
 * 
 * @author kelers
 * 
 */
@Engine("snmpTrapManager")
public class SnmpTrapManager implements CommandResponder {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private MultiThreadedMessageDispatcher dispatcher;
    private Set<TrapCallback> listeners = null;
    private Map<Integer, DefaultUdpTransportMapping> trapServers;
    private ThreadPoolExecutor executorService;
    private Snmp snmp;
    // Modify by Victor@20170601修改原来Trap处理单线程为线程池处理
    @Value("${Trap.WorkerPool.Size:2}")
    private Integer workerPoolSize;
    @Value("${Trap.ThreadPool.corePoolSize:8}")
    private Integer corePoolSize;
    @Value("${Trap.ThreadPool.maximumPoolSize:256}")
    private Integer maximumPoolSize;
    @Value("${Trap.ThreadPool.keepAliveTime:30}")
    private Integer keepAliveTime;
    @Value("${Trap.ThreadPool.queueSize:1024}")
    private Integer queueSize;
    private Map<String, ConcurrentTrapQueue> deviceTraps;
    private Object mutex = new Object();

    static {
        org.snmp4j.log.LogFactory.setLogFactory(new Log4jLogFactory());
    }

    /**
     * @param listener
     *            the listener to add
     */
    public void addListener(TrapCallback listener) {
        listeners.add(listener);
    }

    public void addListenPort(UdpAddress address) {
        if (trapServers.containsKey(address.getPort())) {
            return;
        }
        try {
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping(address);
            if (snmp == null) {
                snmp = new Snmp(dispatcher, transport);
                snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
                snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
                snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
                snmp.listen();
                snmp.addCommandResponder(this);
                if (logger.isInfoEnabled()) {
                    logger.info("Snmp trap listener[{}] started...", address);
                }
            } else {
                snmp.addTransportMapping(transport);
                snmp.listen();
            }
            trapServers.put(address.getPort(), transport);
        } catch (UnknownHostException uhe) {
            logger.debug(uhe.getMessage(), uhe);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * destroy
     */
    @PreDestroy
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("SnmpTrapManager destroy...");
        }
        try {
            executorService.shutdownNow();
            while (!trapServers.isEmpty()) {
                removeListenPort(trapServers.keySet().iterator().next());
            }
            snmp.close();
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * initialize
     */
    @PostConstruct
    public void initialize() {
        listeners = new HashSet<TrapCallback>();
        deviceTraps = new HashMap<>();
        trapServers = new ConcurrentHashMap<Integer, DefaultUdpTransportMapping>();
        dispatcher = new MultiThreadedMessageDispatcher(ThreadPool.create("Trap", workerPoolSize),
                new MessageDispatcherImpl());

        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize), new SnmpThreadFactory("TrapThreadGroup"));
        // Trap队列处理线程
        new Thread() {
            public void run() {
                setName("DoTrapFromQueueThread");
                while (true) {
                    try {
                        for (ConcurrentTrapQueue queue : deviceTraps.values()) {
                            if (queue.isEmpty() || queue.isLocked()) {
                                continue;
                            }
                            executorService.submit(queue);
                        }
                        synchronized (mutex) {
                            mutex.wait(1000L);
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    } catch (IllegalMonitorStateException e) {
                        continue;
                    } catch (InterruptedException e) {
                        continue;
                    } catch (Exception e) {
                        logger.error("Do trap queue error:{}", e.getMessage());
                        logger.debug(e.getMessage(), e);
                    }
                }
            }
        }.start();
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.snmp4j.CommandResponder# processPdu(org.snmp4j.CommandResponderEvent)
     */
    @Override
    public void processPdu(CommandResponderEvent event) {
        // 处理Inform
        if (event.getPDU().getType() == PDU.INFORM) {
            try {
                event.setProcessed(true);
                PDU command = event.getPDU();
                command.setErrorIndex(0);
                command.setErrorStatus(0);
                command.setType(PDU.RESPONSE);
                StatusInformation statusInformation = new StatusInformation();
                StateReference stateReference = event.getStateReference();
                event.getMessageDispatcher().returnResponsePdu(event.getMessageProcessingModel(),
                        event.getSecurityModel(), event.getSecurityName(), event.getSecurityLevel(), command,
                        event.getMaxSizeResponsePDU(), stateReference, statusInformation);
            } catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("INFORM RESPONSE WRONG", ex);
                }
            }
        }
        int count = executorService.getActiveCount() + executorService.getQueue().size();
        if (logger.isDebugEnabled()) {
            logger.debug("Thread pool status:Count:{},ActiveCount/PoolSize:{}/{},Queue Size:{}", count,
                    executorService.getActiveCount(), executorService.getPoolSize(), executorService.getQueue().size());
            logger.debug("Receive Trap From {} at Time {}", event.getPeerAddress().toString(), new Date().toString());
            logger.debug("-----------*Trap Start*-----------------");
            logger.debug(event.getPDU().toString());
            logger.debug("-----------*Trap End*-----------------");
        }
        Trap trap = TrapUtil.parseTrap(event);
        ConcurrentTrapQueue queue = deviceTraps.get(trap.getAddress());
        if (queue == null) {
            queue = new ConcurrentTrapQueue(listeners, trap.getAddress());
            deviceTraps.put(trap.getAddress(), queue);
        }
        queue.offer(trap);
        try {
            synchronized (mutex) {
                mutex.notifyAll();
            }
        } catch (Exception e) {
        }
    }

    /**
     * @param listener
     *            the listener to remove
     */
    public void removeListener(TrapCallback listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void removeListenPort(UdpAddress address) {
        if (trapServers.containsKey(address.getPort())) {
            DefaultUdpTransportMapping transport = trapServers.remove(address.getPort());
            try {
                transport.close();
                dispatcher.removeTransportMapping(transport);
                if (logger.isInfoEnabled()) {
                    logger.info("Snmp trap listener[" + address + "] removed");
                }
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    public void removeListenPort(int port) {
        if (trapServers.containsKey(port)) {
            DefaultUdpTransportMapping transport = trapServers.remove(port);
            try {
                transport.close();
                dispatcher.removeTransportMapping(transport);
                if (logger.isInfoEnabled()) {
                    logger.info("Snmp trap listener[" + port + "] removed");
                }
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * @param param
     */
    // Modify by Rod 原先的逻辑存在很大的问题，重新修改, 参与SNMP源码中的close方法，
    // 原先的方法会删除messageDispatcher中所有的UdpTransportMapping
    // 导致所有的端口全部解绑定
    public void reset(TrapServerParam param) {
        List<Integer> ports = param.getListenPorts();
        Iterator<Integer> it = trapServers.keySet().iterator();
        while (it.hasNext()) {
            Integer port = it.next();
            if (!ports.contains(port)) {
                UdpAddress address = (UdpAddress) GenericAddress.parse("udp:" + param.getListenAddress() + "/" + port);
                removeListenPort(address);
            }
        }
        // 删除所有的端口绑定
        // trapServers.clear();
        // 删除messageDispatcher中UdpTransportMapping where Socket is null
        /*
         * Iterator<UdpTransportMapping> mIterator = dispatcher.getTransportMappings().iterator();
         * while (mIterator.hasNext()) { UdpTransportMapping mapping = mIterator.next(); if
         * (!mapping.isListening()) { dispatcher.removeTransportMapping(mapping); } }
         */
        for (Integer port : ports) {
            UdpAddress address = (UdpAddress) GenericAddress.parse("udp:" + param.getListenAddress() + "/" + port);
            addListenPort(address);
        }
    }

    /**
     * 发送Trap的方法
     * 
     * @param address
     * @param port
     * @param trap
     *            key 必须是OID字符串。eg：.1.3.6.1.2.1.1.2
     */
    public void sendTrap(String address, int port, Map<String, String> content) {
        if (trapServers == null || trapServers.isEmpty()) {
            throw new TrapConfigException("No trap server.");
        }
        try {
            CommunityTarget target = new CommunityTarget();
            target.setVersion(SnmpConstants.version2c);
            target.setCommunity(new OctetString("public"));
            target.setAddress(GenericAddress.parse("udp:" + address + "/" + port));
            PDU pdu = DefaultPDUFactory.createPDU(SnmpConstants.version2c);
            for (String oid : content.keySet()) {
                pdu.add(new VariableBinding(new OID(oid), new OctetString(content.get(oid))));
            }
            pdu.setType(PDU.TRAP);
            // trapServers.values().iterator().next().send(pdu, target);
            snmp.send(pdu, target);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * 发送一个trap
     * 
     * @param trap
     */
    public void sendTrap(Trap trap) {
        /*
         * if (trapServers == null || trapServers.isEmpty()) { throw new TrapConfigException(
         * "No trap server."); }
         */
        try {
            CommunityTarget target = new CommunityTarget();
            target.setVersion(SnmpConstants.version2c);
            target.setCommunity(new OctetString(trap.getSecurityName()));
            target.setAddress(GenericAddress.parse("udp:" + trap.getAddress() + "/" + trap.getPort()));
            PDU pdu = DefaultPDUFactory.createPDU(SnmpConstants.version2c);
            for (String oid : trap.getVariableBindings().keySet()) {
                Object value = trap.getVariableBindings().get(oid);
                if (value instanceof OctetString) {
                    pdu.add(new VariableBinding(new OID(oid), (OctetString) value));
                } else if (value instanceof Integer32) {
                    pdu.add(new VariableBinding(new OID(oid), (Integer32) value));
                } else if (value instanceof IpAddress) {
                    pdu.add(new VariableBinding(new OID(oid), (IpAddress) value));
                } else if (value instanceof TimeTicks) {
                    pdu.add(new VariableBinding(new OID(oid), (TimeTicks) value));
                }
            }
            pdu.setType(PDU.TRAP);
            // trapServers.values().iterator().next().send(pdu, target);
            snmp.send(pdu, target);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * @return the executorService
     */
    public ThreadPoolExecutor getExecutorService() {
        return executorService;
    }

    /**
     * @return the deviceTraps
     */
    public Map<String, ConcurrentTrapQueue> getDeviceTraps() {
        return new HashMap<>(deviceTraps);
    }

    /**
     * @return the deviceTraps
     */
    public int getTrapCounts() {
        int counts = 0;
        for (ConcurrentTrapQueue traps : deviceTraps.values()) {
            counts += traps.size();
        }
        return counts;
    }
}
