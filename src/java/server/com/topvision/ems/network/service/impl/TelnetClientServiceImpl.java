/***********************************************************************
 * $Id: TelnetClientServiceImpl.java,v1.0 2017年1月8日 上午10:14:44 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.TelnetClientDao;
import com.topvision.ems.network.domain.TelnetRecord;
import com.topvision.ems.network.exception.TelnetClientException;
import com.topvision.ems.network.service.TelnetClientService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.telnet.TelnetVty;
import com.topvision.platform.domain.TftpClientInfo;
import com.topvision.platform.message.event.LogoffEvent;
import com.topvision.platform.message.event.LogoffListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author vanzand
 * @created @2017年1月8日-上午10:14:44
 *
 */
@Service("telnetClientService")
public class TelnetClientServiceImpl extends BaseService implements TelnetClientService, LogoffListener {

    private Map<String, Map<String, TelnetVty>> telnetMap = new HashMap<String, Map<String, TelnetVty>>();
    private Map<String, Long> timeMap = new HashMap<String, Long>();
    @Autowired
    private TelnetClientDao telnetClientDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private MessageService messageService;

    private static final int cacheTimeout = 900000;

    private static String connectTags = "#,>,:,[n],(yes/no)?";
    private static String usernameTags = "assword:, Please complete the input within 5 seconds.";
    private static String pwdResponseTags = "$,#,>,assword:,Please complete the input within 5 seconds.,%Login authentication failed. Too many failures., Bad passwords, too many failures!";

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(LogoffListener.class, this);

        // 启动线程维护telnetbase的map
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long now = System.currentTimeMillis();
                        // 遍历timeMap对象
                        Iterator<Entry<String, Long>> iterator = timeMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Entry<String, Long> sessionObj = iterator.next();
                            String sessionId = sessionObj.getKey();
                            Long sessionTime = sessionObj.getValue();
                            if (now - sessionTime > cacheTimeout) {
                                timeMap.remove(sessionId);
                                telnetMap.remove(sessionId);
                            }
                        }
                        Thread.sleep(cacheTimeout);
                    } catch (Exception e) {
                        logger.debug("cacu cm error: " + e.getMessage());
                    }
                }

            }
        });
        thread.start();
    }

    @Override
    public List<TelnetRecord> loadTelnetRecord(Map<String, Object> queryMap) {
        return telnetClientDao.selectTelnetRecordList(queryMap);
    }

    @Override
    public Integer loadTelnetRecordCount(Map<String, Object> queryMap) {
        return telnetClientDao.selectTelnetRecordCount(queryMap);
    }

    @Override
    public String connect(String sessionId, String ip) throws Exception {
        insertRecord(ip, "telnet " + ip);

        // modify by fanzidong,重构底层技术，使用TelnetVty

        TelnetVty telnetVty = getTelnetBase(sessionId, ip);
        String connectStr = "";

        try {
            telnetVty.setPrompt(connectTags, "");
            telnetVty.connect(ip);
            connectStr = telnetVty.receiveUntil();
            if (connectStr.indexOf("\r") != -1) {
                connectStr = connectStr.substring(connectStr.indexOf("\r") + 1);
            }
        } catch (Exception e) {
            logger.debug(String.format("connect %s error, e:", ip) + e.getMessage());
            close(sessionId, ip);
            throw new TelnetClientException(e.getMessage());
        }

        return connectStr;
    }

    @Override
    public void close(String sessionId, String ip) {
        insertRecord(ip, "exit");
        TelnetVty telnetVty = getTelnetBase(sessionId, ip);

        try {
            telnetVty.disconnect();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            removeTelnetBase(sessionId, ip);
        }
    }

    @Override
    public String sendUsername(String sessionId, String ip, String username) throws Exception {
        insertRecord(ip, username);
        TelnetVty telnetVty = getTelnetBase(sessionId, ip);

        String info = "";
        try {
            telnetVty.setPrompt(usernameTags, "");
            info = telnetVty.sendLine(username);
            info = formatSingleLine(username, info);
        } catch (Exception e) {
            logger.debug(String.format("sendUsername %s to %s error, e:", username, ip) + e.getMessage());
            close(sessionId, ip);
            throw new TelnetClientException(e.getMessage());
        }
        return info;
    }

    @Override
    public String sendPassword(String sessionId, String ip, String password) throws Exception {
        insertRecord(ip, "******");
        TelnetVty telnet = getTelnetBase(sessionId, ip);

        String info = "";
        try {
            telnet.setPrompt(pwdResponseTags, "");
            info = telnet.sendLine(password);
            info = formatSingleLine(password, info);
        } catch (Exception e) {
            logger.debug(String.format("sendPassword %s to %s error, e:", password, ip) + e.getMessage());
            close(sessionId, ip);
            throw new TelnetClientException(e.getMessage());
        }

        return info;
    }

    @Override
    public String sendCommand(String sessionId, String ip, String command) throws Exception {
        insertRecord(ip, command);
        TelnetVty telnet = getTelnetBase(sessionId, ip);
        // 获取全局超时时间配置
        Properties properties = systemPreferencesService.getModulePreferences(TftpClientInfo.SYSTEM_MODULE_NAME);
        Integer timeout = Integer.valueOf(properties.getProperty(TftpClientInfo.TIMEOUT));

        String info = null;
        try {
            telnet.setTimeout(timeout);
            telnet.setPrompt(connectTags, "");
            // 下发命令
            if (command.endsWith("?")) {
                // info = telnet.sendCmd(command, timeout, TelnetBase.allTags);
                // commandcommand.replaceAll("\\b", "\b");
                command.replaceAll("\\b", "\b");
                info = telnet.sendLine(command);
            } else {
                info = telnet.sendLine(command);
            }
            info = formatMultiLine(command, info);

        } catch (Exception e) {
            logger.debug(String.format("send command %s error, e:", command) + e.getMessage());
            close(sessionId, ip);
            throw new TelnetClientException(e.getMessage());
        }

        return info;
    }

    @Override
    public void logoff(LogoffEvent evt) {
        if (telnetMap.containsKey(evt.getSessionId())) {
            telnetMap.remove(evt.getSessionId());
        }
    }

    private TelnetVty getTelnetBase(String sessionId, String ip) {
        Map<String, TelnetVty> sessionTelnets;
        TelnetVty telnet = null;
        if (telnetMap.containsKey(sessionId)) {
            sessionTelnets = telnetMap.get(sessionId);
            if (sessionTelnets.containsKey(ip)) {
                telnet = sessionTelnets.get(ip);
            } else {
                // 确保一个客户端同时只有一个ip telnet客户端
                sessionTelnets.clear();
                telnet = new TelnetVty();
                sessionTelnets.put(ip, telnet);
            }
        } else {
            sessionTelnets = new HashMap<String, TelnetVty>();
            telnetMap.put(sessionId, sessionTelnets);
            telnet = new TelnetVty();
            sessionTelnets.put(ip, telnet);
        }
        timeMap.put(sessionId, System.currentTimeMillis());
        return telnet;
    }

    private void removeTelnetBase(String sessionId, String ip) {
        if (telnetMap.containsKey(sessionId)) {
            Map<String, TelnetVty> sessionTelnets = telnetMap.get(sessionId);
            if (sessionTelnets.containsKey(ip)) {
                sessionTelnets.remove(ip);
            }
        }
    }

    private String formatSingleLine(String command, String recv) {
        // 要去掉可能一起返回command以及\r，即截掉command\r 前的不要
        if (!"".equals(command) && recv.indexOf(command) != -1) {
            Integer infoIndex = recv.indexOf(command);
            recv = recv.substring(infoIndex + command.length() + 1);
        }
        while (recv.indexOf("\r") != -1 && recv.indexOf("\r") != recv.length() - 1) {
            recv = recv.substring(recv.indexOf("\r") + 1);
        }
        return recv;
    }

    private String formatMultiLine(String command, String recv) {
        if (!"".equals(command) && recv.indexOf(command) != -1) {
            Integer infoIndex = recv.indexOf(command);
            if ((infoIndex + command.length() + 1) < recv.length()) {
                recv = recv.substring(infoIndex + command.length() + 1);
            }
        }
        // 要去掉最后一行prompt前可能有的退格符
        String lastRow;
        if (recv.lastIndexOf("\r") != -1) {
            lastRow = recv.substring(recv.lastIndexOf("\r") + 1);
        } else {
            lastRow = recv;
        }
        lastRow = formatPromptLine(lastRow);

        if (recv.lastIndexOf("\r") != -1) {
            recv = recv.substring(0, recv.lastIndexOf("\r") + 1) + lastRow;
        } else {
            recv = lastRow;
        }

        // add by fanzidong，有时候设备返回两次重复的prompt，需要去掉最后一次
        if (recv.lastIndexOf("\r") != -1) {
            String[] arrays = recv.split("\r");
            String secondLast = arrays[arrays.length - 2].trim();
            String last = arrays[arrays.length - 1].trim();
            if (arrays.length >= 2 && secondLast.equals(last)) {
                recv = recv.substring(0, recv.lastIndexOf("\r") + 1);
            }
        }

        return recv;
    }

    private String formatPromptLine(String promptLine) {
        StringBuilder builder = new StringBuilder();
        String[] lastRowArray = promptLine.split("");
        for (int i = 0; i < lastRowArray.length; i++) {
            if (!"\b".equals(lastRowArray[i])) {
                builder.append(lastRowArray[i]);
            } else {
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        return builder.toString().trim();
    }

    private void insertRecord(String ip, String command) {
        // 判断是否是空命令
        if (command == null || "".equals(command)) {
            return;
        }
        // 判断是否开启了记录功能
        Properties properties = systemPreferencesService.getModulePreferences(TftpClientInfo.SYSTEM_MODULE_NAME);
        String mode = properties.getProperty(TftpClientInfo.RECORD_STATE);
        if (mode != null && mode.equals(String.valueOf(TftpClientInfo.RECORD_ON))) {
            telnetClientDao.insertRecord(ip, command, CurrentRequest.getCurrentUser().getUserId());
        }
    }

}
