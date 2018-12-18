/**
 *
 */
package com.topvision.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.SystemLogDao;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.service.SystemLogService;

/**
 * @author niejun
 */
@Service("systemLogService")
public class SystemLogServiceImpl extends BaseService implements SystemLogService {
    @Autowired
    private SystemLogDao systemLogDao = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.common.service.SystemLogService#deleteSystemLog
     * (java.util.List)
     */
    @Override
    public void deleteSystemLog(List<Long> logIds) {
        systemLogDao.deleteByPrimaryKey(logIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.common.service.SystemLogService#deleteSystemLog (Long)
     */
    @Override
    public void deleteSystemLog(Long logId) {
        systemLogDao.deleteByPrimaryKey(logId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.common.service.SystemLogService#getSystemLog( Long)
     */
    @Override
    public SystemLog getSystemLog(Long logId) {
        return systemLogDao.selectByPrimaryKey(logId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SystemLogService#getSystemLog(java.util .Map,
     * com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<SystemLog> getSystemLog(Map<String, String> map, Page page) {
        return systemLogDao.selectByMap(map, page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SystemLogService#getUserLogByParams(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public List<SystemLog> getUserLogByParams(Integer start, Integer limit) {
        return systemLogDao.getUserLogByParams(start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SystemLogService#getUserLogCounts()
     */
    @Override
    public List<SystemLog> getUserLogCounts() {
        return systemLogDao.getUserLogCounts();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.common.service.SystemLogService#insertSystemLog
     * (com.topvision.ems.server.common.domain.SystemLog)
     */
    @Override
    public void insertSystemLog(SystemLog sysLog) {
        systemLogDao.insertEntity(sysLog);
    }

    public void setSystemLogDao(SystemLogDao systemLogDao) {
        this.systemLogDao = systemLogDao;
    }
}
