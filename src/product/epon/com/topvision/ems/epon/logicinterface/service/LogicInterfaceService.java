/***********************************************************************
 * $Id: LogicInterfaceService.java,v1.0 2016年10月14日 上午10:36:13 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.framework.service.Service;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:36:13
 *
 */
public interface LogicInterfaceService extends Service {

    void refreshLogicInterface(Long entityId);

    void refreshLogicInterfaceIpConfig(Long entityId);
    
    List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> map);

    int getOltLogicInterfaceByTypeCount(Map<String, Object> map);

    LogicInterface getOltLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId);

    void addLogicInterface(LogicInterface logicInterface);

    void updateLogicInterface(LogicInterface logicInterface);

    void deleteLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId);

    List<InterfaceIpV4Config> getInterfaceIpList(Map<String, Object> map);

    InterfaceIpV4Config getInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr);

    void addInterfaceIpV4Config(InterfaceIpV4Config ipV4Config);

    void updateInterfaceIpV4Config(InterfaceIpV4Config ipV4Config);

    void deleteInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr);
}
