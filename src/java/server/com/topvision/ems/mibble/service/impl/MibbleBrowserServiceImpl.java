package com.topvision.ems.mibble.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.mibble.dao.MibbleBrowserDao;
import com.topvision.ems.mibble.domain.MibbleMessage;
import com.topvision.ems.mibble.service.MibbleBrowserService;
import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;
import com.topvision.ems.socketserver.socket.SocketRequestExecutor;
import com.topvision.ems.socketserver.websocket.TopWebSocketHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONObject;

/**
 * Snmp Manager. for set/get/getnext/getall method
 * 
 * @author Bravin
 * 
 */
@Service("mibbleBrowserService")
public class MibbleBrowserServiceImpl extends BaseService
        implements MibbleBrowserService, SocketRequestExecutor, TopWebSocketHandler {
    public final static int REQ_TYPE_GETALL = 1;
    public final static int REQ_TYPE_STOP_GETALL = 2;
    private boolean stopCmd;
    @Autowired
    private SnmpExecutorService executor;
    @Autowired
    private MibbleBrowserDao mibbleBrowserDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    @Override
    public Map<String, String> get(SnmpParam param, String oid) {
        return executor.getVB(param, oid);
    }

    @Override
    public Map<String, String> getNext(SnmpParam param, String oid) {
        return executor.getNextVB(param, oid);
    }

    @Override
    public void saveSelectedMib(String[] mibs, long userId) {
        mibbleBrowserDao.saveSelectedMib(mibs, userId);
    }

    @Override
    public List<String> loadMibbles(long userId) {
        return mibbleBrowserDao.loadMibbles(userId);
    }

    // /////////// setter&getter //////////////////

    /**
     * @return the executor
     */
    public SnmpExecutorService getExecutor() {
        return executor;
    }

    /**
     * @param executor
     *            the executor to set
     */
    public void setExecutor(SnmpExecutorService executor) {
        this.executor = executor;
    }

    /**
     * @return the mibbleBrowserDao
     */
    public MibbleBrowserDao getMibbleBrowserDao() {
        return mibbleBrowserDao;
    }

    /**
     * @param mibbleBrowserDao
     *            the mibbleBrowserDao to set
     */
    public void setMibbleBrowserDao(MibbleBrowserDao mibbleBrowserDao) {
        this.mibbleBrowserDao = mibbleBrowserDao;
    }

    @Override
    public boolean execute(SocketRequest socketRequest, SocketResponse response) {
        onMessage(socketRequest);
        return MAINTAIN_SOCKET;
    }

    /**
     * 得到SnmpParam
     * 
     * @param host
     * @param port
     * @param community
     * @return
     */
    private SnmpParam getSnmpParam(String host, int port, String community) {
        Properties snmpProperty = systemPreferencesService.getModulePreferences("Snmp");
        Integer snmpPort = Integer.parseInt(snmpProperty.getProperty("Snmp.port"));
        snmpPort = snmpPort != null ? snmpPort : 161;
        Integer snmpCount = Integer.parseInt(snmpProperty.getProperty("Snmp.retries"));
        snmpCount = snmpCount != null ? snmpCount : 0;
        Integer snmpTimeout = Integer.parseInt(snmpProperty.getProperty("Snmp.timeout"));
        snmpTimeout = snmpTimeout != null ? snmpTimeout : 15000;
        SnmpParam param = new SnmpParam();
        param.setIpAddress(host);
        param.setPort(port);
        param.setPort(snmpPort);
        param.setTimeout(snmpTimeout);
        param.setRetry(Byte.parseByte(snmpCount.toString()));
        param.setCommunity(community);
        return param;
    }

    public boolean isStopCmd() {
        return stopCmd;
    }

    public void setStopCmd(boolean stopCmd) {
        this.stopCmd = stopCmd;
    }

    @Override
    public void stopGetAll() {
        setStopCmd(true);
    }

    @Override
    public void onConnected(SocketRequest socketRequest) {
        
    }

    @Override
    public void onClose(SocketRequest socketRequest) {
    }

    @Override
    public void onMessage(SocketRequest socketRequest) {
        int type = socketRequest.getInt("REQ_TYPE");
        SocketResponse response = socketRequest.getResponse();
        if (REQ_TYPE_GETALL == type) {
            stopCmd = false;
            String oid = socketRequest.getString("oid");
            String host = socketRequest.getString("host");
            int port = socketRequest.getInt("port");
            String community = socketRequest.getString("readCommunity");
            String thisOid = oid;
            SnmpParam snmpParam = getSnmpParam(host, port, community);
            do {
                Map<String, String> data = getNext(snmpParam, thisOid);
                thisOid = data.get("oid");
                try {
                    if (!thisOid.startsWith(oid)) {
                        break;
                    } else {
                        MibbleMessage message = new MibbleMessage();
                        message.setOid(thisOid);
                        message.setName(data.get("name"));
                        message.setValue(data.get("value"));
                        message.setRawOid(data.get("realOid"));
                        if (response.isClosed()) {
                            break;
                        }
                        response.write(JSONObject.fromObject(message).toString());
                    }
                } catch (IOException e) {
                    logger.error("", e);
                }
            } while (!isStopCmd());
            try {
                response.write("-GETALL-END");
            } catch (IOException e) {
                logger.error("", e);
            }
        } else if (REQ_TYPE_STOP_GETALL == type) {
            setStopCmd(true);
        }
    }

}
