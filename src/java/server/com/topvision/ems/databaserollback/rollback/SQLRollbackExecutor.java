/***********************************************************************
 * $Id: SQLRollbackExecutor.java,v1.0 2016年7月25日 下午3:32:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.rollback;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.databaserollback.dao.SQLRollbackDao;
import com.topvision.framework.common.ClassAware;

/**
 * @author Bravin
 * @created @2016年7月25日-下午3:32:58
 *
 */
public class SQLRollbackExecutor {
    protected final Logger logger = LoggerFactory.getLogger(SQLRollbackExecutor.class);
    private List<RollbackStatement> list;
    private SQLRollbackDao sqlRollbackDao;
    private Map<String, Class<?>> SPECIAL_HANDLER_PATTERN_MAPPER;

    public void exec() {
        loadSpecialList();
        for (RollbackStatement statement : list) {
            try {
                rollback(statement);
            } catch (Exception e) {
                logger.info("", e);
            }
        }
    }

    private void loadSpecialList() {
        SPECIAL_HANDLER_PATTERN_MAPPER = new HashMap<String, Class<?>>();
        Set<Class<?>> scanClass = new ClassAware().scanClass(SQLRollbackExecutor.class.getPackage().getName(),
                SpecialSQLHandler.class);
        for (Class<?> clazz : scanClass) {
            Field field;
            try {
                field = clazz.getDeclaredField("PATTERN");
                field.setAccessible(true);
                String pattern = (String) field.get(null);
                SPECIAL_HANDLER_PATTERN_MAPPER.put(pattern, clazz);
            } catch (NoSuchFieldException | SecurityException e) {
                logger.error("", e);
            } catch (IllegalArgumentException e) {
                logger.error("", e);
            } catch (IllegalAccessException e) {
                logger.error("", e);
            }
        }
    }

    /**
     * @param statement
     */
    private void rollback(RollbackStatement statement) {
        String sql = statement.getSql();
        Iterator<String> iterator = SPECIAL_HANDLER_PATTERN_MAPPER.keySet().iterator();
        while (iterator.hasNext()) {
            String pattern = iterator.next();
            if (sql.matches(pattern)) {
                rollback(statement, SPECIAL_HANDLER_PATTERN_MAPPER.get(pattern));
                return;
            }
        }
        sqlRollbackDao.exec(sql);
    }

    /**
     * @param statement
     * @param class1
     */
    private void rollback(RollbackStatement statement, Class<?> clazz) {
        try {
            SpecialSQLHandler handler = (SpecialSQLHandler) clazz.newInstance();
            handler.setSqlRollbackDao(sqlRollbackDao);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("", e);
        }
    }

    public List<RollbackStatement> getList() {
        return list;
    }

    public void setList(List<RollbackStatement> list) {
        this.list = list;
    }

    public SQLRollbackDao getSqlRollbackDao() {
        return sqlRollbackDao;
    }

    public void setSqlRollbackDao(SQLRollbackDao sqlRollbackDao) {
        this.sqlRollbackDao = sqlRollbackDao;
    }

}
