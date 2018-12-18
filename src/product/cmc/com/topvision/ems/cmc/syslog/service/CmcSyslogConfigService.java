/***********************************************************************
 * $Id: CmcSyslogConfigService.java,v1.0 2013-4-26 下午8:11:42 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.service;

import java.util.List;

import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordType;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordTypeII;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.framework.service.Service;

/**
 * @author fanzidong
 * @created @2013-4-26-下午8:11:42
 *
 */
public interface CmcSyslogConfigService extends Service {

    /**
     * 判断是否支持syslogII
     * @param entityId
     * @return
     */
    Boolean isSupportSyslogII(Long entityId);

    /**
     * 获取指定CCMTS的数据库中的记录方式信息
     * 
     * @param entityId
     * @return
     */
    List<CmcSyslogRecordType> getCmcEventLevel(Long entityId);

    /**
     * 获取数据库指定CCMTS的配置项
     * 
     * @param entityId
     * @return
     */
    CmcSyslogConfig getCmcSyslogConfig(Long entityId);

    /**
     * 更新指定CCMTS的指定记录方式的最低事件等级
     * 
     * @param entityId
     * @param recordType
     * @param minLevel
     */
    void updateCmcRecordEventLevel(Long entityId, String recordType, int minLevel);

    /**
     * 将指定设备的所有记录方式的最低事件等级恢复默认
     * 
     * @param entityId
     */
    void undoAllEventLevels(Long entityId);

    /**
     * 更新指定CCMTS的配置项
     * 
     * @param cmcSyslogConfig
     */
    void updateCmcSyslogConfig(CmcSyslogConfig cmcSyslogConfig);

    /**
     * 从设备获取数据
     * 
     * @param cmcSyslogConfig
     */
    CmcSyslogConfig getSyslogConfigFromEntity(Long entityId);

    /**
     * 获取一个设备的日志等级
     * 
     * @param entityId
     * @return
     */
    List<CmcSyslogRecordTypeII> getCmcRecordTypeMinLvlII(Long entityId);

    /**
     * 修改日志等级
     * 
     * @param entityId
     * @param evPriority
     * @param evReporting
     */
    void updateCmcRecordEventLevelII(CmcSyslogRecordTypeII cmcSyslogRecordTypeII);
}
