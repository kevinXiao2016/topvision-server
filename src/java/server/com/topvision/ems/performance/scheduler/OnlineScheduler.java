/***********************************************************************
 * $Id: OnlineScheduler.java,v1.0 2014-3-14 上午9:59:40 $
 *
 * @author: Rod John
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.scheduler;

import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.facade.domain.ConnectivityStrategy;
import com.topvision.ems.performance.domain.OnlinePerf;
import com.topvision.ems.performance.domain.OnlineResult;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.common.TimeCostRecord;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Rod John
 * @created @2014-3-14-上午9:59:40
 *
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("onlineScheduler")
public class OnlineScheduler extends AbstractExecScheduler<OnlinePerf> {
    @Autowired
    private PingExecutorService pingExecutorService;
    private List<ConnectivityStrategy> strategyList;
    private Boolean isCcmts;

    @Override
    public void exec() {
        try {
            long entityId = operClass.getEntityId();
            Timestamp collectTime = new Timestamp(System.currentTimeMillis());
            // Add by Rod 增加在Engine端回调记录性能任务执行时间
            try {
                if (operClass.getMonitorId() != null) {
                    getCallback().recordTaskCollectTime(operClass.getMonitorId(), collectTime);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            snmpParam = getCallback().getSnmpParamByEntity(entityId);
            // add by fanzidong@20170908 获取当前的连通性测试策略及基本参数
            strategyList = getCallback().getUsingConnectivityStrategy();
            isCcmts = getCallback().isCcmts(entityId);
            Integer delay = -1;
            String sysUpTime = "-1";
            String mac = null;
            String ipAddress = snmpParam.getIpAddress();
            delay = connect(ipAddress);

            try {
                // EMS-15300 中广有线蚌埠+欧阳益湘+设备和网管上的OLT在线时长不一致
                // PN8600-V1.10.0.5才支持该节点
                Boolean supporttopsysoltuptime = getCallback().isFunctionSupported(snmpParam.getEntityId(),
                        "topsysoltuptime");
                if (getCallback().isOlt(snmpParam.getEntityId()) && supporttopsysoltuptime) {
                    sysUpTime = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.2.9.0");
                } else {
                    sysUpTime = snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.1.3.0");
                }
                // 判断是不是数字字符串
                if (!StringUtils.isNumeric(sysUpTime)) {
                    sysUpTime = "-1";
                }
            } catch (SnmpNoResponseException e) {
                logger.debug(e.toString());
                sysUpTime = "snmpTimeout";
            } catch (Exception e) {
                logger.debug("", e);
            }

            if (delay > 0 && isCcmts) {
                try {
                    mac = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12.142671872");
                } catch (SnmpNoResponseException e) {
                    logger.debug(e.toString());
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
            OnlineResult onlineResult = new OnlineResult(operClass);
            onlineResult.setEntityId(entityId);
            onlineResult.setDelay(delay);
            onlineResult.setMac(mac);
            // add by fanzidong@20170908 如果未连通，记录尝试策略
            if (delay == -1) {
                List<String> strategys = getStrategyNames(strategyList);
                onlineResult.setStrategys(strategys);
            }
            onlineResult.setSysUpTime(sysUpTime);
            onlineResult.setCollectTime(collectTime);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(onlineResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    private List<String> getStrategyNames(List<ConnectivityStrategy> strategyList) {
        List<String> strategys = new ArrayList<String>();
        for (ConnectivityStrategy strategy : strategyList) {
            strategys.add(strategy.getName());
        }
        return strategys;
    }

    /**
     * 尝试连通该ip
     *
     * @param ipAddress
     * @return
     */
    private Integer connect(String ipAddress) {
        // 获取当前勾选并排好序的连通方式，逐一尝试
        int delay = -1;
        for (ConnectivityStrategy strategy : strategyList) {
            delay = tryConnect(ipAddress, strategy);
            if (delay > 0) {
                return delay;
            }
        }
        return delay;
    }

    /**
     * 尝试用指定策略连接ip
     *
     * @param strategy
     * @return
     */
    private int tryConnect(String ip, ConnectivityStrategy strategy) {
        int delay = -1;
        try {
            switch (strategy.getName()) {
            case ConnectivityStrategy.ICMP_CONNECT_STRATEGY:
                delay = tryIcmpConnect(ip, strategy);
                break;
            case ConnectivityStrategy.SNMP_CONNECT_STRATEGY:
                delay = trySnmpConnect(ip, strategy);
                break;
            case ConnectivityStrategy.TCP_CONNECT_STRATEGY:
                delay = tryTcpConnect(ip, strategy);
                break;
            }
        } catch (Exception e) {
            logger.error("tryConnect error, " + e);
        }
        return delay;
    }

    private int tryIcmpConnect(String ip, ConnectivityStrategy strategy) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        // 获取PING参数
        Properties props = strategy.getProperties();

        worker.setTimeout(Integer.parseInt(props.getProperty("Ping.timeout")));
        worker.setCount(Integer.parseInt(props.getProperty("Ping.count")));
        worker.setRetry(Integer.parseInt(props.getProperty("Ping.retry")));
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

    private int trySnmpConnect(String ip, ConnectivityStrategy strategy) {
        int delay = -1;

        // 获取参数
        Properties props = strategy.getProperties();
        String oid = props.getProperty("Snmp.connectivityOid");

        try {
            TimeCostRecord record = new TimeCostRecord();
            String str = snmpExecutorService.get(snmpParam, oid, record);
            if (!"noSuchInstance".equals(str) && !"noSuchObject".equals(str)) {
                delay = (int) (record.getCost());
            }
        } catch (SnmpNoResponseException e) {
            logger.debug(
                    "[OnlineScheduler] [trySnmpConnect error] happens. [Probably Because {}]. [Probably need to check device using this oid] [ip:{}, oid:{}].",
                    e.getMessage(), ip, oid);
            logger.debug(e.toString());
        } catch (Exception e) {
            logger.debug("", e);
        }
        return delay;
    }

    private int tryTcpConnect(String ip, ConnectivityStrategy strategy) {
        int delay = -1;

        // 获取参数
        Properties props = strategy.getProperties();
        Integer port = Integer.valueOf(props.getProperty("port"));
        Integer timeout = Integer.valueOf(props.getProperty("timeout"));
        long startTime = System.currentTimeMillis();
        Socket client = null;
        try {
            client = new Socket();
            SocketAddress addr = new InetSocketAddress(ip, port);
            client.connect(addr, timeout);
            delay = (int) (System.currentTimeMillis() - startTime);
        } catch (UnknownHostException e) {
            logger.warn(
                    "[tryTcpConnect] [{}] happens. [Probably Because this ip is not correct]. [Probably need to check it!]   [ip:{}] .",
                    e.getClass().getSimpleName(), ip);
        } catch (IOException e) {
            logger.warn(
                    "[tryTcpConnect] [{}] happens. [Probably Because this ip is not correct]. [Probably need to check it!]   [ip:{}] .",
                    e.getClass().getSimpleName(), ip);
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return delay;
    }

}
