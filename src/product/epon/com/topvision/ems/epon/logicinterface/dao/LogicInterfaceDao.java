/***********************************************************************
 * $Id: LogicInterfaceDao.java,v1.0 2016年10月14日 上午10:07:40 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:07:40
 *
 */
public interface LogicInterfaceDao {

    void updateLogicInterfaceList(Long entityId, List<LogicInterface> logicInterfaceList);

    void updateLogicInterfaceIpV4List(Long entityId, List<InterfaceIpV4Config> logicInterfaceIpList);

    List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> map);

    int getOltLogicInterfaceByTypeCount(Map<String, Object> map);

    LogicInterface getOltLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId);

    void insertLogicInterface(LogicInterface logicInterface);

    void updateLogicInterface(LogicInterface logicInterface);

    void deleteLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId);

    void deleteIpV4ConfigByInterface(Long entityId, Integer interfaceType, Integer interfaceId);

    List<InterfaceIpV4Config> getInterfaceIpList(Map<String, Object> map);

    InterfaceIpV4Config getInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr);

    void insertInterfaceIpV4Config(InterfaceIpV4Config ipV4Config);

    void updateInterfaceIpV4Config(InterfaceIpV4Config ipV4Config);

    void deletePriIpV4Config(Long entityId, Integer ipV4ConfigIndex);

    void deleteInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr);

}
