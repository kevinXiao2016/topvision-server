/***********************************************************************
 * $Id: CmcSyslogServerDao.java,v1.0 2013-4-23 下午4:53:52 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.dao;

import java.util.List;

import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogServerEntry;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Administrator
 * @created @2013-4-23-下午4:53:52
 *
 */
public interface CmcSyslogServerDao extends BaseEntityDao<CmcSyslogServerEntry> {
    
    /**
     * 查询指定CCMTS的所有syslog服务器
     * @param cmcId
     * @return
     */
    List<CmcSyslogServerEntry> getCmcSyslogServerListByEntityId(Long entityId);
    
    /**
     * 批量刷新syslog服务器
     * @param cmcSyslogServerEntrys
     */
    void batchRefreshCmcSyslogServerEntrys(List<CmcSyslogServerEntry> cmcSyslogServerEntrys, Long entityId);
    
    /**
     * 插入一条syslog服务器
     * @param cmcSyslogServerEntry
     */
    void insertCmcSyslogServerEntry(CmcSyslogServerEntry cmcSyslogServerEntry);
    
    /**
     * 删除一条syslog服务器
     * @param cmcSyslogServerEntry
     */
    void deleteCmcSyslogServerEntry(CmcSyslogServerEntry cmcSyslogServerEntry);
    
    /**
     * 更新一条Syslog服务器
     * @param cmcSyslogServerEntry
     */
    void updateCmcSyslogServerEntry(CmcSyslogServerEntry cmcSyslogServerEntry);
    
}
