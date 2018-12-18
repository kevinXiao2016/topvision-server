/***********************************************************************
 * $Id: SQLDecompileServiceImpl.java,v1.0 2016年7月21日 上午9:43:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.common.ClassAware;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.version.service.RollbackableProcess;
import com.topvision.framework.version.service.SQLBackupService;
import com.topvision.framework.version.service.SQLDecompileService;

/**
 * @author Bravin
 * @created @2016年7月21日-上午9:43:00
 *
 */
@Service
public class SQLDecompileServiceImpl extends BaseService implements SQLDecompileService {
    private static final Map<String, Class<? extends RollbackableProcess>> PROCESS_PATTERN_MAPPER = new HashMap<String, Class<? extends RollbackableProcess>>();
    @Autowired
    private SQLBackupService sqlBackupService;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.service.SQLRollbackService#execSQLBackup(java.lang.String)
     */
    @Override
    public RollbackableProcess decompileSQL(String sql) {
        try {
            logger.debug("rollback : {}", sql);
            if (sql == null || sql.length() == 0) {
                return null;
            }
            int c = sql.indexOf("\r\n");
            String headRow = sql;
            if (c != -1) {
                headRow = sql.substring(0, c);
            }
            if (headRow.startsWith("--") || headRow.startsWith("/")) {
                decompileSQL(sql.substring(c));
                return null;
            }
            Iterator<String> patterns = PROCESS_PATTERN_MAPPER.keySet().iterator();
            sql = decorate(sql, 0);
            sql = sql.replaceAll("`", "");
            String upperSQL = sql.replace("\r\n", "");
            while (patterns.hasNext()) {
                String pattern = patterns.next();
                if (upperSQL.matches(pattern)) {
                    return exec(sql, pattern);
                }
            }
            logger.error("UNRECOGNIZE SQL :" + sql);
        } catch (Exception e) {
            logger.error("backup update sql faild!:{}", e);
        }
        return null;
    }

    /**
     * @param sql
     * @param pattern
     * @param sqlRunCallbackable 
     * @return 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private RollbackableProcess exec(String sql, String pattern) throws Exception {
        Class<? extends RollbackableProcess> clazz = PROCESS_PATTERN_MAPPER.get(pattern);
        RollbackableProcess proccess = null;
        proccess = clazz.newInstance();
        proccess.resolveSQL(sql);
        proccess.setSqlBackupService(sqlBackupService);
        proccess.backupBeforeUpdate();
        return proccess;
    }

    /**
     * @param sql
     * @return
     */
    private static String decorate(String sql, int position) {
        if (position == 0) {
            sql = sql.replaceAll("\\s+", " ").trim();
        }
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        int c = sql.indexOf("'", position);
        if (c == -1) {
            c = sql.indexOf("\"", position);
            if (c == -1) {
                if (position == 0) {
                    return sql.toUpperCase();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(sql.substring(0, position));
                sb.append(sql.substring(position).toUpperCase());
                return sb.toString();
            }
        }

        int s = sql.indexOf("'", c + 1);
        if (s == -1) {
            s = sql.indexOf("\"", c + 1);
            if (s == -1) {
                return sql;
            }
        }

        StringBuilder sb = new StringBuilder();
        if (position > 0) {
            sb.append(sql.substring(0, position));
        }
        sb.append(sql.substring(position, c).toUpperCase());
        sb.append(sql.substring(c));

        return decorate(sb.toString(), s + 1);
    }

    public void test() {
        logger.info(decorate(
                "  insert     into    OltOnuTypeInfo    values('PN8621',33,1,0,1,aaa\"DB.oltOnuTypeInfo.onu33\", bb'image/onuIcon.png')",
                0));
    }

    @Override
    public void initialize() {
        Set<Class<?>> clazzes = new ClassAware().scanClass("com.topvision.ems.databaserollback.process",
                RollbackableProcess.class);
        for (Class<?> clazz : clazzes) {
            try {
                if (clazz.equals(RollbackableProcess.class)) {
                    continue;
                }
                Field field = clazz.getDeclaredField("PATTERN");
                field.setAccessible(true);
                String pattern = (String) field.get(null);
                PROCESS_PATTERN_MAPPER.put(pattern, (Class<? extends RollbackableProcess>) clazz);
            } catch (NoSuchFieldException | SecurityException e) {
                logger.error("", e);
            } catch (IllegalArgumentException e) {
                logger.error("", e);
            } catch (IllegalAccessException e) {
                logger.error("", e);
            }
        }
        logger.info("register sqlrollback pattern completly!");
    }
}
