/***********************************************************************
 * $Id: BatchDiscoveryFacadeImpl.java,v1.0 2012-11-16 上午11:05:43 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.callback.BatchDiscoveryCallBack;
import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.DwrInfo;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.ems.facade.network.BatchDiscoveryFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author RodJohn
 * @created @2012-11-16-上午11:05:43
 * 
 */
@Facade("batchDiscoveryFacade")
public class BatchDiscoveryFacadeImpl extends BaseEngine implements BatchDiscoveryFacade {
    @Autowired
    private PingExecutorService pingExecutorService = null;
    @Autowired
    private SnmpExecutorService snmpExecutorService = null;
    private ExecutorService executorService;
    private LinkedBlockingQueue<BatchDiscoveryInfo> snmpOptions;
    private LinkedBlockingQueue<BatchDiscoveryInfo> callBackOptions;
    private AtomicInteger snmpWorkSize;
    private AtomicInteger callBackWorkSize;
    private AtomicBoolean workShutdown;
    // add by fanzidong, 增加当前拓扑网段ip序列，避免上次回调本次执行
    private List<String> ips = null;

    @Override
    public void batchDiscovery(List<String> ips, List<String> entityNames, List<String> topoNames,
            List<SnmpParam> snmpParams, List<EntityType> allTypes, List<String> types, final DwrInfo dwrInfo,
            Integer pingCount, Integer pingTimeout, Integer pingRetry) {
        this.ips = ips;
        // Snmp采集线程计数器
        snmpWorkSize = new AtomicInteger(ips.size());
        // 回调线程计数器
        callBackWorkSize = new AtomicInteger(ips.size());
        // 拓扑任务标识符
        workShutdown = new AtomicBoolean(false);
        // Snmp采集队列
        snmpOptions = new LinkedBlockingQueue<BatchDiscoveryInfo>();
        // 回调队列
        callBackOptions = new LinkedBlockingQueue<BatchDiscoveryInfo>();
        // Ping Thread start
        pingIps(ips, entityNames, topoNames, pingTimeout, pingCount, pingRetry);
        // SnmpInfo Thread Start
        discoverySysInfo(allTypes, types, snmpParams);
        // CallBack Thread Start
        callBackTopo(dwrInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.network.BatchDiscoveryFacade#shutDownDiscovery()
     */
    @Override
    public void shutDownDiscovery() {
        workShutdown.set(true);
    }

    private void pingIps(final List<String> ips, final List<String> entityNames, final List<String> topoNames,
            final int pingTimeout, final int pingCount, final int pingRetry) {
        Thread pingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                PingResult r = new PingResult();
                for (int ipCursor = 0; ipCursor < ips.size(); ipCursor++) {
                    final String name = entityNames.get(ipCursor);
                    final String ip = ips.get(ipCursor);
                    final String topoName = topoNames.get(ipCursor);
                    PingWorker worker = new PingWorker(ip, r) {

                        @Override
                        public void run() {
                            try {
                                int value = getPing().ping(ip, pingTimeout, pingCount, pingRetry);
                                if (value >= 0) {
                                    BatchDiscoveryInfo option = new BatchDiscoveryInfo();
                                    option.setIpAddress(ip);
                                    option.setEntityName(name);
                                    option.setTopoName(topoName);
                                    snmpOptions.add(option);
                                } else {
                                    snmpWorkSize.decrementAndGet();
                                    callBackWorkSize.decrementAndGet();
                                }
                            } catch (Exception ex) {
                                logger.error(ex.getMessage(), ex);
                                snmpWorkSize.decrementAndGet();
                                callBackWorkSize.decrementAndGet();
                            }
                        }
                    };
                    pingExecutorService.submit(worker, r);
                }
            }
        });
        pingThread.start();
    }

    private void discoverySysInfo(final List<EntityType> allEntityType, final List<String> chooseSysObjId,
            final List<SnmpParam> snmpParams) {
        final BatchDiscoveryCallBack callBack = getCallback(BatchDiscoveryCallBack.class);
        Thread snmpThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (snmpWorkSize.get() > 0) {
                    if (!workShutdown.get()) {
                        try {
                            BatchDiscoveryInfo option = snmpOptions.poll(1000, TimeUnit.MILLISECONDS);
                            if (option == null) {
                                continue;
                            }
                            // add by fanzidong， 如果ip不是本次网段内，跳过
                            if (!ips.contains(option.getIpAddress())) {
                                logger.debug("IP is not in this network segment, skip:" + option.getIpAddress());
                                continue;
                            }
                            snmpWorkSize.decrementAndGet();
                            SnmpParam snmpParam = new SnmpParam();
                            snmpParam.setIpAddress(option.getIpAddress());
                            SnmpWorker<BatchDiscoveryInfo> worker = new SnmpWorker<BatchDiscoveryInfo>(snmpParam) {
                                private static final long serialVersionUID = 378439495788751288L;

                                @SuppressWarnings({ "unchecked", "rawtypes" })
                                @Override
                                protected void exec() throws Exception {
                                    for (SnmpParam tmpParam : snmpParams) {
                                        tmpParam.setIpAddress(result.getIpAddress());
                                        snmpUtil.reset(tmpParam);
                                        String[] sysInfo = new String[2];
                                        String[] oids = new String[] { "1.3.6.1.2.1.1.2.0", "1.3.6.1.2.1.1.5.0" };
                                        try {
                                            snmpUtil.setTimeout(1000);
                                            snmpUtil.setRetries(0);
                                            sysInfo = snmpUtil.get(oids);
                                        } catch (SnmpException snmpException) {
                                            continue;
                                        }
                                        if (sysInfo != null) {
                                            result.setSysInfo(sysInfo);
                                            // if (types.contains(result.getSysInfo()[0])) {
                                            if (smartChooseEntityType(allEntityType, chooseSysObjId, sysInfo)) {
                                                result.setFlag(true);
                                                try {
                                                    TopoHandlerProperty obj = callBack
                                                            .searchProductTopoInfo(sysInfo[0]);
                                                    if (obj != null) {
                                                        if (obj.getPorperty() == TopoHandlerProperty.TOPO_OID) {
                                                            result.setProductInfo(
                                                                    (Object) snmpUtil.get((String) obj.getObject()));
                                                        } else if (obj
                                                                .getPorperty() == TopoHandlerProperty.TOPO_STRINGS) {
                                                            result.setProductInfo(
                                                                    snmpUtil.get((String) obj.getObject()));
                                                        } else if (obj
                                                                .getPorperty() == TopoHandlerProperty.TOPO_OBJECT) {
                                                            result.setProductInfo(
                                                                    snmpUtil.get(obj.getObject().getClass()));
                                                        } else if (obj
                                                                .getPorperty() == TopoHandlerProperty.TOPO_TABLE) {
                                                            result.setProductInfo(
                                                                    snmpUtil.getTable((Class) obj.getObject(), true));
                                                        } else if (obj
                                                                .getPorperty() == TopoHandlerProperty.TOPO_TABLELINE) {
                                                            result.setProductInfo(
                                                                    snmpUtil.getTableLine(obj.getObject()));
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    logger.error("", e);
                                                }
                                                // 只有符合条件的时候设置相应的SNMP参数
                                                result.setSnmpParam(tmpParam);
                                                break;
                                            }
                                        }
                                    }
                                    callBackOptions.add(result);
                                }
                            };
                            snmpExecutorService.execute(worker, option);
                        } catch (SnmpNoResponseException snre) {
                            logger.info("", snre);
                        } catch (InterruptedException e) {
                            logger.info("", e);
                        }
                    } else {
                        break;
                    }
                }
                logger.info("BatchTopo SnmpWorker Finished");
            }
        });
        snmpThread.start();
    }

    private boolean smartChooseEntityType(List<EntityType> allEntityType, List<String> chooseSysObjId,
            String[] sysInfo) {
        if (chooseSysObjId.contains(sysInfo[0])) {
            return true;
        }
        Map<String, EntityType> eMap = new HashMap<>();
        for (EntityType tmp : allEntityType) {
            eMap.put(tmp.getSysObjectID(), tmp);
        }
        if (eMap.containsKey(sysInfo[0]) && !chooseSysObjId.contains(sysInfo[0])) {
            return false;
        }
        if (!eMap.containsKey(sysInfo[0])) {
            String preSysObjecId = getEntityTypeBySysObjIdPre(eMap.keySet(), sysInfo[0]);
            if (chooseSysObjId.contains(preSysObjecId)) {
                sysInfo[0] = preSysObjecId;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private String getEntityTypeBySysObjIdPre(Set<String> allSysObjId, String sysObjId) {
        int max = -1;
        String result = null;
        for (String tmp : allSysObjId) {
            if (tmp.length() > 0 && sysObjId.contains(tmp)) {
                if (tmp.length() > max) {
                    result = tmp;
                    max = tmp.length();
                }
            }
        }
        return result;
    }
    
    private void callBackTopo(final DwrInfo dwrInfo) {
        executorService = Executors.newFixedThreadPool(10);
        final BatchDiscoveryCallBack callBack = getCallback(BatchDiscoveryCallBack.class);
        Thread callBackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                List<java.util.concurrent.Future<String>> callBackList = new ArrayList<java.util.concurrent.Future<String>>();
                while (callBackWorkSize.get() > 0) {
                    if (!workShutdown.get()) {
                        try {
                            final BatchDiscoveryInfo option = callBackOptions.poll(1000, TimeUnit.MILLISECONDS);
                            if (option == null) {
                                continue;
                            }
                            // add by fanzidong， 如果ip不是本次网段内，跳过
                            if (!ips.contains(option.getIpAddress())) {
                                logger.debug("IP is not in this network segment, skip:" + option.getIpAddress());
                                continue;
                            }
                            callBackWorkSize.decrementAndGet();
                            if (option.getFlag()) {
                                option.setDwrInfo(dwrInfo);
                                FutureTask<String> callBackTask = new FutureTask<String>(new Callable<String>() {

                                    @Override
                                    public String call() throws Exception {
                                        String result = callBack.discoveryExporter(option);
                                        return result;
                                    }
                                });
                                callBackList.add(callBackTask);
                                executorService.submit(callBackTask);
                            }
                        } catch (InterruptedException e) {
                            logger.error("", e);
                        }
                    } else {
                        break;
                    }
                }
                // 修改防止循环无法退出
                for (int i = 0; i < callBackList.size(); i++) {
                    try {
                        callBackList.get(i).get();
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                }
                callBack.discoveryComplete(dwrInfo);
                logger.info("BatchTopo CallBack Finished");
                executorService.shutdown();
            }
        });
        callBackThread.start();
    }

    /**
     * @return the pingExecutorService
     */
    public PingExecutorService getPingExecutorService() {
        return pingExecutorService;
    }

    /**
     * @param pingExecutorService
     *            the pingExecutorService to set
     */
    public void setPingExecutorService(PingExecutorService pingExecutorService) {
        this.pingExecutorService = pingExecutorService;
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }
}
