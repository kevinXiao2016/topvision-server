package com.topvision.ems.engine.executor.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.MibConstants;
import com.topvision.ems.facade.callback.TopologyCallback;
import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.ems.facade.network.TopologyFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

@Facade("topologyFacade")
public class TopologyFacadeImpl extends BaseEngine implements TopologyFacade {
    private class TopologySnmpWorker extends SnmpWorker<TopologyResult> {
        private static final long serialVersionUID = -1097092341751779876L;
        private TopologyParam param;
        private TopologyCallback listener;

        /**
         * @param snmpParam
         * @param result
         */
        public TopologySnmpWorker(SnmpParam snmpParam) {
            super(snmpParam);
        }

        /**
         * @param param
         * @param result
         */
        public TopologySnmpWorker(SnmpParam snmpParam, TopologyParam param) {
            super(snmpParam);
            this.param = param;
        }

        /**
         * @param param
         * @param result
         * @param l
         */
        public TopologySnmpWorker(SnmpParam snmpParam, TopologyParam param, TopologyCallback l) {
            super(snmpParam);
            this.param = param;
            this.listener = l;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.topvision.framework.snmp.SnmpWorker#exec()
         */
        @Override
        protected void exec() {
            if (logger.isDebugEnabled()) {
                logger.debug("Begin to topology:" + result.getIp() + "=" + result.getHostName());
            }
            if (snmpParam != null && topology()) {
                if (listener != null)
                    listener.onData(result);
                return;
            }
            for (int i = 0; param != null && param.getSnmpParams() != null && i < param.getSnmpParams().size(); i++) {
                snmpParam = param.getSnmpParams().get(i).clone();
                if (topology()) {
                    if (listener != null)
                        listener.onData(result);
                    return;
                }
            }
            try {
                InetAddress ia = InetAddress.getByName(result.getIp());
                result.setIp(ia.getHostAddress());
                String hostname = ia.getHostName();
                if (hostname == null || "".equals(hostname) || hostname.equals(result.getIp())) {
                } else {
                    result.setHostName(ia.getHostName());
                }
            } catch (UnknownHostException ex4) {
                // TODO Exception
            }
            result.setError("The device not snmp!");
            if (listener != null)
                listener.onData(result);
        }

        private boolean topology() {
            snmpParam.setIpAddress(result.getIp());
            snmpUtil.reset(snmpParam);
            if (!snmpUtil.verifySnmpVersion()) {
                return false;
            }
            snmpParam.setVersion(snmpUtil.getVersion());
            result.setSnmpParam(snmpParam);
            String[][] table = null;
            try {
                result.setSystem(snmpUtil.get(MibConstants.system));
                logger.debug(snmpUtil.array2String(result.getSystem()));
            } catch (Exception ex) {
                // TODO Exception
            }
            try {
                // must modify to MibConstants.ifTableBaseInfo
                table = snmpUtil.getTable(MibConstants.ifTableBaseInfo);
                result.setIfTable(table);
            } catch (Exception ex) {
                // TODO Exception
                logger.debug("Get ifTable's Value error", ex);
            }
            if (logger.isDebugEnabled() && table != null) {
                logger.debug(snmpUtil.table2String(table));
            }
            for (int i = 0; table != null && i < table.length; i++) {
                try {
                    if (table[i][5] == null) {
                        continue;
                    }
                    table[i][5] = table[i][5].trim();
                    if (table[i][5].length() == 17 && !table[i][5].equals("00:00:00:00:00:00")
                            && !table[i][5].equals("FF:FF:FF:FF:FF:FF")) {
                        result.addMac(table[i][5].trim().toUpperCase());
                    }
                } catch (Exception ex) {
                    logger.debug("Parser port's Value error" + ex);
                }
            }
            String[][] ifXTable = null;
            try {
                snmpUtil.loadMib(MibConstants.IF_MIB);
                // must modify to MibConstants.ifXTableBaseInfo
                ifXTable = snmpUtil.getTable(MibConstants.ifXTableBaseInfo);
            } catch (Exception ex) {
                logger.debug("Get ifTable's Value error", ex);
            }
            result.setIfXTable(ifXTable);
            try {
                // 获取物理地址与IP地址对应关系
                result.setAtTable(snmpUtil.getTable(MibConstants.atTable));
            } catch (Exception ex) {
                logger.debug("Get ifTable's Value error", ex);
            }
            try {
                // 获取IP地址
                result.setIpAddrTable(snmpUtil.getTable(MibConstants.ipAddrTable));
            } catch (Exception ex) {
                logger.debug("Get ifTable's Value error", ex);
            }
            try {
                // 获取Mac地址与IP对应关系
                table = snmpUtil.getTable(MibConstants.ipNetToMediaTable);
            } catch (Exception ex) {
                logger.debug("Get ipNetToMediaTable's Value error", ex);
            }
            Map<String, String> ipNetToMediaMap = new HashMap<String, String>();
            result.setIpNetToMediaMap(ipNetToMediaMap);
            for (int i = 0; table != null && i < table.length; i++) {
                try {
                    if (table[i][1].equals("00:00:00:00:00:00")
                            || table[i][1].toUpperCase().equals("FF:FF:FF:FF:FF:FF")) {
                        continue;
                    }
                    ipNetToMediaMap.put(table[i][2], table[i][1].toUpperCase());
                } catch (Exception ex) {
                    logger.debug("Get ipNetToMediaTable's Value error", ex);
                }
            }
            try {
                // 获取路由表
                result.setIpRouteTable(snmpUtil.getTable(MibConstants.ipRouteTable));
            } catch (Exception ex) {
                logger.debug("Get ipRouteTable's value error", ex);
            }
            try {
                snmpUtil.loadMib(MibConstants.BRIDGE_MIB);
                result.setDot1dBaseBridgeAddress(snmpUtil.getLeaf(MibConstants.dot1dBaseBridgeAddress).toUpperCase());
                logger.debug("dot1dBaseBridgeAddress:" + result.getDot1dBaseBridgeAddress());
            } catch (Exception ex) {
                logger.debug("Get dot1dBasePortTable's value error", ex);
            }
            try {
                table = snmpUtil.getTable(MibConstants.dot1dBasePortTable);
            } catch (Exception ex) {
                logger.debug("Get dot1dBasePortTable's value error", ex);
            }
            Map<String, String> portMap = new HashMap<String, String>();
            try {
                for (int i = 0; table != null && i < table.length; i++) {
                    portMap.put(table[i][0], table[i][1]);
                }
            } catch (Exception ex) {
                logger.debug("Get dot1dBasePortTable's Value error", ex);
            }
            Map<String, String> fdbTableMap = new HashMap<String, String>();
            result.setFdbTableMap(fdbTableMap);
            try {
                table = snmpUtil.getTable(MibConstants.dot1dTpFdbTable);
                for (int i = 0; i < table.length; i++) {
                    if (table[i][0] == null || table[i][0].trim().length() == 0 || table[i][1] == null) {
                        continue;
                    }
                    fdbTableMap.put(table[i][0].toUpperCase(),
                            portMap.get(table[i][1]) == null ? table[i][1] : portMap.get(table[i][1]));
                }
            } catch (Exception ex) {
                logger.debug("Get dot1dTpFdbTable's Value error", ex);
            }
            try {
                snmpUtil.loadMib(MibConstants.Q_BRIDGE_MIB);
                table = snmpUtil.getTable(MibConstants.dot1qTpFdbTable);
                for (int i = 0; i < table.length; i++) {
                    if (table[i][0] == null || table[i][0].trim().length() == 0 || table[i][1] == null) {
                        continue;
                    }
                    fdbTableMap.put(oid2mac(table[i][0]), portMap.get(table[i][1]) == null ? table[i][1]
                            : (String) portMap.get(table[i][1]));
                }
            } catch (Exception ex) {
                logger.debug("Get dot1qTpFdbTable's Value error", ex);
            }
            String[][] communities = null;
            try {
                snmpUtil.loadMib(MibConstants.ENTITY_MIB);
                communities = snmpUtil.getTable(MibConstants.entLogicalCommunity);
                logger.debug("communities.length=" + communities.length);
                logger.debug(snmpUtil.table2String(communities));
            } catch (Exception ex) {
                logger.debug("Get entLogicalCommunity's Value error", ex);
            }
            for (int i = 0; communities != null && i < communities.length; i++) {
                if (communities[i][0] == null) {
                    continue;
                }
                try {
                    table = snmpUtil.getTable(MibConstants.ipNetToMediaTable);
                    for (int j = 0; j < table.length; j++) {
                        if (table[j][0].equals("00:00:00:00:00:00")
                                || table[j][0].toUpperCase().equals("FF:FF:FF:FF:FF:FF")) {
                            continue;
                        }
                        ipNetToMediaMap.put(table[j][1], table[j][0].toUpperCase());
                    }
                } catch (Exception ex) {
                    logger.debug("Get ipNetToMediaTable's Value error", ex);
                }
                try {
                    table = snmpUtil.getTable(MibConstants.dot1dTpFdbTable);
                    for (int j = 0; j < table.length; j++) {
                        if (table[j][0] == null || table[j][0].trim().length() == 0 || table[j][1] == null) {
                            continue;
                        }
                        fdbTableMap.put(table[j][0].toUpperCase(), portMap.get(table[j][1]) == null ? table[j][1]
                                : (String) portMap.get(table[j][1]));
                    }
                } catch (Exception ex) {
                    logger.debug("Get dot1vTpFdbTable's Value error", ex);
                }
                try {
                    table = snmpUtil.getTable(MibConstants.dot1qTpFdbTable);
                    for (int j = 0; j < table.length; j++) {
                        if (table[j][0] == null || table[j][0].trim().length() == 0 || table[j][1] == null) {
                            continue;
                        }
                        fdbTableMap.put(oid2mac(table[j][0]), portMap.get(table[j][1]) == null ? table[j][1]
                                : (String) portMap.get(table[j][1]));
                    }
                } catch (Exception ex) {
                    logger.debug("Get dot1qTpFdbTable's Value error", ex);
                }
            }
            result.setDiscoveryTime(System.currentTimeMillis());
            if (logger.isDebugEnabled()) {
                logger.debug("Topology ended:" + result);
            }
            return true;
        }
    }

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public void addTopology(TopologyResult tr, SnmpParam snmpParam) throws SnmpException {
        snmpExecutorService.submit(new TopologySnmpWorker(snmpParam), tr);
    }

    @Override
    public void addTopology(TopologyResult tr, TopologyParam param) throws SnmpException {
        TopologyCallback l = getCallback(TopologyCallback.class);
        snmpExecutorService.submit(new TopologySnmpWorker(null, param, l), tr);

    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    // TODO 添加注释
    private String oid2mac(String oid) {
        if (oid == null) {
            return "";
        }
        String[] s = oid.split("\\.");
        if (s.length != 6) {
            return oid.toUpperCase();
        }
        StringBuffer mac = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            String tmp = "00" + Integer.toHexString(Integer.parseInt(s[i]));
            mac.append(tmp.substring(tmp.length() - 2));
            mac.append(":");
        }
        return mac.substring(0, 17).toUpperCase();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.network.TopologyFacade#scan(java.util.List, Integer, Integer)
     */
    @Override
    public Map<String, String> scan(List<String> ips, Integer timeout, Integer retries) {
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.network.TopologyFacade#scan(java.lang.String, Integer, Integer)
     */
    @Override
    public Map<String, String> scan(String targets, Integer timeout, Integer retries) {
        return null;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.network.TopologyFacade#topology(com.topvision.ems.facade.domain.TopologyResult,
     *      com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public TopologyResult topology(TopologyResult tr, SnmpParam snmpParam) throws SnmpException, InterruptedException,
            ExecutionException {
        return snmpExecutorService.execute(new TopologySnmpWorker(snmpParam), tr);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.network.TopologyFacade#topology(com.topvision.ems.facade.domain.TopologyResult,
     *      com.topvision.ems.facade.domain.TopologyParam)
     */
    @Override
    public TopologyResult topology(TopologyResult tr, TopologyParam param) throws SnmpException, InterruptedException,
            ExecutionException {
        return snmpExecutorService.execute(new TopologySnmpWorker(null, param), tr);
    }
}
