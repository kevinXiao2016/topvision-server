/***********************************************************************
 * $Id: TelnetClientService.java,v1.0 2017年1月8日 上午10:13:29 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.TelnetRecord;

/**
 * @author vanzand
 * @created @2017年1月8日-上午10:13:29
 *
 */
public interface TelnetClientService {

    /**
     * 连接指定设备
     * 
     * @param sessionId
     * @param ip
     * @return
     */
    String connect(String sessionId, String ip) throws Exception;
    
    /**
     * 断开连接指定设备
     * 
     * @param sessionId
     * @param ip
     * @return
     */
    void close(String sessionId, String entityIp);

    /**
     * 像指定ip发送命令
     * 
     * @param ip
     * @param command
     * @return
     * @throws Exception
     */
    String sendCommand(String sessionId, String ip, String command) throws Exception;

    /**
     * 向设备发送username
     * 
     * @param ip
     * @param command
     * @return
     */
    String sendUsername(String sessionId, String ip, String username) throws Exception;

    /**
     * 向设备发送password
     * 
     * @param ip
     * @param command
     * @return
     */
    String sendPassword(String sessionId, String ip, String password) throws Exception;

    /**
     * 查询telnet记录
     * 
     * @param queryMap
     * @return
     */
    List<TelnetRecord> loadTelnetRecord(Map<String, Object> queryMap);

    /**
     * 获取符合条件的telnet记录的条数
     * 
     * @param queryMap
     * @return
     */
    Integer loadTelnetRecordCount(Map<String, Object> queryMap);

    
}
