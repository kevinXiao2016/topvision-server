/***********************************************************************
 * $Id: AutoBatchDiscoveryFacadeImpl.java,v1.0 2014-5-11 下午3:48:51 $
 * 
 * @author: Rod John
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
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.callback.AutoDiscoveryCallBack;
import com.topvision.ems.facade.domain.AutoDiscoveryInfo;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.ems.facade.network.AutoBatchDiscoveryFacade;
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
 * @author Rod John
 * @created @2014-5-11-下午3:48:51
 * 
 */
@Facade("autoBatchDiscoveryFacade")
public class AutoBatchDiscoveryFacadeImpl extends BaseEngine implements AutoBatchDiscoveryFacade {
    @Autowired
    private PingExecutorService pingExecutorService = null;
    @Autowired
    private SnmpExecutorService snmpExecutorService = null;
    private ExecutorService executorService;
    private LinkedBlockingQueue<AutoDiscoveryInfo> snmpOptions;
    private LinkedBlockingQueue<AutoDiscoveryInfo> callBackOptions;
    private AtomicInteger snmpWorkSize;
    private AtomicInteger callBackWorkSize;

    @Override
    public void autoBatchDiscovery(List<String> ips, Long folderId, List<SnmpParam> snmpParams,
            List<EntityType> allTypes, List<String> types, String callBackURL, Integer pingTimeout, Integer pingCount,
            Integer pingRetry) {
        // Snmp采集线程计数器
        snmpWorkSize = new AtomicInteger(ips.size());
        // 回调线程计数器
        callBackWorkSize = new AtomicInteger(ips.size());
        // Snmp采集队列
        snmpOptions = new LinkedBlockingQueue<AutoDiscoveryInfo>();
        // 回调队列
        callBackOptions = new LinkedBlockingQueue<AutoDiscoveryInfo>();
        // Ping Thread start
        pingIps(ips, folderId, pingTimeout, pingCount, pingRetry);
        // SnmpInfo Thread Start
        discoverySysInfo(allTypes, types, snmpParams, callBackURL);
        // CallBack Thread Start
        callBackTopo(callBackURL);
    }

    private void pingIps(final List<String> ips, final Long topoFolderId, final int pingTimeout, final int pingCount,
            final int pingRetry) {
        Thread pingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                PingResult r = new PingResult();
                for (int ipCursor = 0; ipCursor < ips.size(); ipCursor++) {
                    final String ip = ips.get(ipCursor);
                    PingWorker worker = new PingWorker(ip, r) {

                        @Override
                        public void run() {
                            try {
                                int value = getPing().ping(ip, pingTimeout, pingCount, pingRetry);
                                if (value >= 0) {
                                    AutoDiscoveryInfo option = new AutoDiscoveryInfo();
                                    option.setIpAddress(ip);
                                    option.setTopoFolderId(topoFolderId.intValue());
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
            final List<SnmpParam> snmpParams, final String callBackURL) {
        final AutoDiscoveryCallBack callBack = getCallback(AutoDiscoveryCallBack.class);
        Thread snmpThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (snmpWorkSize.get() > 0) {
                    try {
                        AutoDiscoveryInfo option = snmpOptions.poll(1000, TimeUnit.MILLISECONDS);
                        if (option == null) {
                            continue;
                        }
                        snmpWorkSize.decrementAndGet();
                        SnmpParam snmpParam = new SnmpParam();
                        snmpParam.setIpAddress(option.getIpAddress());
                        SnmpWorker<AutoDiscoveryInfo> worker = new SnmpWorker<AutoDiscoveryInfo>(snmpParam) {
                            private static final long serialVersionUID = 8261988387454172666L;

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
                                        if (smartChooseEntityType(allEntityType, chooseSysObjId, sysInfo)) {
                                            result.setFlag(true);
                                            try {
                                                TopoHandlerProperty obj = callBack.searchProductTopoInfo(sysInfo[0]);
                                                if (obj != null) {
                                                    if (obj.getPorperty() == TopoHandlerProperty.TOPO_OID) {
                                                        result.setProductInfo(
                                                                (Object) snmpUtil.get((String) obj.getObject()));
                                                    } else if (obj.getPorperty() == TopoHandlerProperty.TOPO_STRINGS) {
                                                        result.setProductInfo(snmpUtil.get((String) obj.getObject()));
                                                    } else if (obj.getPorperty() == TopoHandlerProperty.TOPO_OBJECT) {
                                                        result.setProductInfo(snmpUtil.get(obj.getObject().getClass()));
                                                    } else if (obj.getPorperty() == TopoHandlerProperty.TOPO_TABLE) {
                                                        result.setProductInfo(
                                                                snmpUtil.getTable((Class) obj.getObject(), true));
                                                    } else if (obj
                                                            .getPorperty() == TopoHandlerProperty.TOPO_TABLELINE) {
                                                        result.setProductInfo(snmpUtil.getTableLine(obj.getObject()));
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

    private void callBackTopo(String callBackURL) {
        executorService = Executors.newFixedThreadPool(10);
        final AutoDiscoveryCallBack callBack = getCallback(AutoDiscoveryCallBack.class);
        Thread callBackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                List<java.util.concurrent.Future<String>> callBackList = new ArrayList<java.util.concurrent.Future<String>>();
                while (callBackWorkSize.get() > 0) {
                    try {
                        final AutoDiscoveryInfo option = callBackOptions.poll(1000, TimeUnit.MILLISECONDS);
                        if (option == null) {
                            continue;
                        }
                        callBackWorkSize.decrementAndGet();
                        if (option.getFlag()) {
                            FutureTask<String> callBackTask = new FutureTask<String>(new Callable<String>() {

                                @Override
                                public String call() throws Exception {
                                    callBack.discoveryExporter(option);
                                    return null;
                                }
                            });
                            callBackList.add(callBackTask);
                            executorService.submit(callBackTask);
                        }
                    } catch (InterruptedException e) {
                        logger.error("", e);
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
                callBack.discoveryComplete();
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
