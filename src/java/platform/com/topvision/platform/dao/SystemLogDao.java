/**
 *
 */
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.SystemLog;

/**
 * @author niejun
 */
public interface SystemLogDao extends BaseEntityDao<SystemLog> {

    /**
     * 根据查询参数获得系统日志记录
     * 
     * @param start
     * @param limit
     * @return list
     */
    List<SystemLog> getUserLogByParams(Integer start, Integer limit);

    /**
     * 获得系统日志记录总数
     * 
     * @param entityIp
     * @param operName
     * @param status
     * @return list
     */
    List<SystemLog> getUserLogCounts();

}
