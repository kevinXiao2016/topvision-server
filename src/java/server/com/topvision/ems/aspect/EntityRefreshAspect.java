/***********************************************************************
 * $Id: EntityRefreshAspect.java,v1.0 2012-5-2 下午1:36:04 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.exception.EntityRefreshException;

/**
 * 设备刷新动作的切片，不允许多个刷新动作同时发生，如果出现这种情况，抛给调用者 EntityRefreshException
 * 
 * @author dengl
 * @created @2012-5-2-下午1:36:04
 * 
 */
public class EntityRefreshAspect {
    private static final Logger logger = LoggerFactory.getLogger(EntityRefreshAspect.class);

    private EntityDao entityDao;

    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    public void init() {
        try {
            // 程序启动时候，重置设备的刷新状态
            entityDao.resetRefreshStatus();
        } catch (DataAccessException e) {
            // 有可能数据库未初始化好
            logger.warn(e.getMessage());
        }
    }

    /**
     * 代理设备刷新方法，如果处于刷新状态，抛出 EntityRefreshException 异常
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object doRefresh(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Start entity refresh aspect.");

        Object[] args = joinPoint.getArgs();
        Long entityId = (Long) args[0];
        Integer status = entityDao.getRefreshStatus(entityId);

        if (status != null && status == 1) {// 处于刷新状态
            logger.debug("Throw Entity Refresh Exception.");

            throw new EntityRefreshException("Entity[" + entityId + "] is refreshing.");
        } else {// 处于非刷新状态
            entityDao.updateRefreshStatus(entityId, 1);
        }

        try {
            return joinPoint.proceed();
        } finally {
            // 刷新动作完成，重置刷新状态
            entityDao.updateRefreshStatus(entityId, 0);

            logger.debug("End entity refresh aspect.");
        }
    }
}
