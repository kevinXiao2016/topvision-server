package com.topvision.ems.mibble.service;

import java.util.List;
import java.util.Map;

import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author Bravin
 *
 */
public interface MibbleBrowserService extends Service {

    /**
    * Get 操作
    * @param param
    * @param oid
    * @return
    */
    Map<String, String> get(SnmpParam param, String oid);

    /**
    * Get Next 操作
    * @param param
    * @param oid
    * @return
    */
    Map<String, String> getNext(SnmpParam param, String oid);

    /**
    * 保存用户选择的mib文件
    * @param mibs
    * @param userId
    */
    void saveSelectedMib(String[] mibs, long userId);

    /**
     * 停止getAll
     */
    void stopGetAll();

    /**
    * 加载用户选择的MIB
    * @param userId
    * @return
    */
    List<String> loadMibbles(long userId);
}
