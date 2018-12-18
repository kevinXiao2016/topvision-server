/***********************************************************************
 * $Id: ProductionConfigBackupService.java,v1.0 2016年1月11日 下午4:11:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.congfigbackup.service;

/**
 * @author Bravin
 * @created @2016年1月11日-下午4:11:42
 *
 */
public interface ProductionConfigBackupService {

    /**
     * 执行下载命令,EPON和CC各自实现
     * @param connectInfo
     * @param entityId
     * @param ip 
     * @throws Exception 
     */
    void execDownloadAndBackup(Long entityType, Long entityId, String ip) throws Exception;

    void saveConfigFile(String ip, Long entityType, Long entityId);

    void applyConfigToDevice(Long entityId, Long entityType, String ip, String directory) throws Exception;

}
