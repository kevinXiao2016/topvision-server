/***********************************************************************
 * $Id: CmcSyslogConfigDao.java,v1.0 2013-4-26 下午3:47:18 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.dao;

import java.util.List;

import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordType;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordTypeII;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfigForA;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author fanzidong
 * @created @2013-4-26-下午3:47:18
 *
 */
public interface CmcSyslogConfigDao extends BaseEntityDao<CmcSyslogConfig>{
    /**
     * 插入指定CCMTS的4种记录方式与最低事件等级的对应关系
     * @param cmcSyslogRecordType
     */
    void insertCmcAllRecordTypes(List<CmcSyslogRecordType> cmcSyslogRecordTypes);
    
    /**
     * 删除指定CCMTS的4种记录方式
     * @param entityId
     */
    void deleteCmcRecordType(Long entityId);
    
    /**
     * 获取指定CCMTS的4种记录方式
     * @param entityId
     * @return
     */
    List<CmcSyslogRecordType> getCmcRecordTypeMinLvl(Long entityId);
    
    /**
     * 更新指定CCMTS的指定记录方式的最低事件等级
     * @param cmcSyslogRecordType
     */
    void updateRcdTypeMinEvtLvl(CmcSyslogRecordType cmcSyslogRecordType);
    
    /**
     * 将指定设备的所有记录方式的最低事件等级设置为默认值
     * @param entityId
     */
    void undoAllMinEvtLvls(Long entityId);
    
    /**
     * 插入一个CCMTS的syslog配置信息
     * 
     * @param cmcSyslogConfig
     */
    void insertCmcSyslogConfig(CmcSyslogConfig cmcSyslogConfig);
    
    /**
     * 插入A型设备一个CCMTS的syslog配置信息
     * 
     * @param cmcSyslogConfig
     */
    void insertCmcSyslogConfigForA(CmcSyslogConfigForA cmcSyslogConfig);

    /**
     * 删除一个CCMTS的syslog配置信息
     * @param entityId
     */
    void deleteCmcSyslogConfig(Long entityId);
    
    /**
     * 获取指定CCMTS的syslog配置信息
     * @param entityId
     * @return
     */
    CmcSyslogConfig getCmcSyslogConfig(Long entityId);
    
    /**
     * 更新CCMTS的syslog相关配置(使能开关、最大记录数、检测间隔、阈值、处理方式等)
     * @param enable
     */
    void updateCmcSyslogConfigParams(CmcSyslogConfig cmcSyslogConfig);

    List<CmcSyslogRecordTypeII> getCmcRecordTypeMinLvlII(Long entityId);

    void updateRcdTypeMinEvtLvlII(CmcSyslogRecordTypeII cmcSyslogRecordTypeII);
}
