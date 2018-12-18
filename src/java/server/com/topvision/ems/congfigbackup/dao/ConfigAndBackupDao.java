/***********************************************************************
 * $Id: ConfigAndBackupDao.java,v1.0 2015年10月30日 上午9:01:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.congfigbackup.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.congfigbackup.service.ConfigAndBackupRecord;
import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2015年10月30日-上午9:01:51
 *
 */
public interface ConfigAndBackupDao extends Dao {

    /**
     * 记录操作记录
     * @param record
     */
    void recordOperation(ConfigAndBackupRecord record);

    /**
     * 查询所有的配置记录
     * @param map 
     * @return
     */
    List<ConfigAndBackupRecord> selectOperationRecords(Map<String, Object> map);

}
