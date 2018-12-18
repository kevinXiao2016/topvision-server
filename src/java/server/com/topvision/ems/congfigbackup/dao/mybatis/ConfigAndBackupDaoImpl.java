/***********************************************************************
 * $Id: ConfigAndBackupDaoImpl.java,v1.0 2015年10月30日 上午9:02:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.congfigbackup.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.congfigbackup.dao.ConfigAndBackupDao;
import com.topvision.ems.congfigbackup.service.ConfigAndBackupRecord;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2015年10月30日-上午9:02:29
 *
 */
@Repository
public class ConfigAndBackupDaoImpl extends MyBatisDaoSupport<ConfigAndBackupRecord> implements ConfigAndBackupDao {

    @Override
    protected String getDomainName() {
        return ConfigAndBackupRecord.class.getName();
    }

    @Override
    public void recordOperation(ConfigAndBackupRecord record) {
        getSqlSession().insert("recordOperation", record);
    }

    @Override
    public List<ConfigAndBackupRecord> selectOperationRecords(Map<String, Object> map) {
        return getSqlSession().selectList("selectOperationRecords", map);
    }

}
