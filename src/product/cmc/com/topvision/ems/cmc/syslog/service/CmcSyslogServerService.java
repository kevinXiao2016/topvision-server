/***********************************************************************
 * $Id: SyslogServerService.java,v1.0 2013-4-23 下午2:54:08 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.service;

import java.util.List;

import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogServerEntry;
import com.topvision.framework.service.Service;

/**
 * @author Administrator
 * @created @2013-4-23-下午2:54:08
 *
 */
public interface CmcSyslogServerService extends Service {
    
    /**
     * 获取数据库中指定CCMTS的Syslog服务器信息
     * 
     * @param cmcId Long
     * @return SyslogServer
     */
     List<CmcSyslogServerEntry> getCmcSyslogServer(Long entityId);
     
     /**
      * 刷新数据库中指定CCMTS的Syslog服务器信息
      * @param cmcSyslogServerEntrys
      */
     void refreshDatabase(List<CmcSyslogServerEntry> cmcSyslogServerEntrys, Long entityId);
     
     /**
      * 获取指定CCMTS设备上的Syslog服务器信息
      * @param cmcId
      * @return
      */
     List<CmcSyslogServerEntry> getEntitySyslogServer(Long entityId);
     
     /**
      * 向设备添加一个Syslog服务器
      * @param cmcSyslogServerEntry
      */
     void insertCmcSyslogServer(CmcSyslogServerEntry cmcSyslogServerEntry);
     
     /**
      * 从设备上删除一个Syslog服务器
      * @param cmcSyslogServerEntry
      */
     void deleteCmcSyslogServer(CmcSyslogServerEntry cmcSyslogServerEntry);
     
     /**
      * 修改Syslog服务器
      * @param cmcSyslogServerEntry
      */
     void modifyCmcSyslogServer(CmcSyslogServerEntry cmcSyslogServerEntry);

}
