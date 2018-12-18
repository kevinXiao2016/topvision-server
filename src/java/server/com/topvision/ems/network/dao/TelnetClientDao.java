/***********************************************************************
 * $Id: TelnetClientDao.java,v1.0 2017年1月8日 下午3:54:39 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.TelnetRecord;

/**
 * @author vanzand
 * @created @2017年1月8日-下午3:54:39
 *
 */
public interface TelnetClientDao {

    /**
     * 插入记录
     * 
     * @param ip
     * @param command
     * @param userId
     */
    void insertRecord(String ip, String command, Long userId);

    /**
     * 查询telnet记录
     * 
     * @param queryMap
     * @return
     */
    List<TelnetRecord> selectTelnetRecordList(Map<String, Object> queryMap);

    /**
     * 获取符合条件的telnet记录的条数
     * 
     * @param queryMap
     * @return
     */
    Integer selectTelnetRecordCount(Map<String, Object> queryMap);
}
