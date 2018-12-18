/***********************************************************************
 * $Id: SQLRestoreServiceImpl.java,v1.0 2016年7月20日 下午5:49:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.databaserollback.dao.SQLDecompileDao;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.version.service.SQLBackupService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.SystemVersion;

/**
 * @author Bravin
 * @created @2016年7月20日-下午5:49:26
 *
 */
@Service
public class SQLBackupServiceImpl extends BaseService implements SQLBackupService {
    @Value("${Rollback.module:Rollback}")
    private String ROLLBACK_MODULE;
    @Autowired
    private SQLDecompileDao sqlDecompileDao;
    private List<String> sqls = new ArrayList<String>();
    private String currentScript = "rollback-lasted.sql";

    private void _output(BufferedWriter sqlWriter, String sql) throws IOException {
        sqlWriter.write(sql);
        if (!sql.endsWith(";") && !sql.endsWith("&&") && !sql.startsWith("--")) {
            sqlWriter.write(";");
        }
        sqlWriter.write("\r\n");
        sqlWriter.flush();
    }

    @Override
    public void output() {
        /**如果不存在更新,则不备份*/
        if (sqls.isEmpty()) {
            return;
        }
        BufferedWriter lastedRollbackSQLWriter = null;
        BufferedWriter versionRollbackSQLWriter = null;
        String currentVersion = new SystemVersion().getBuildVersion();
        try {
            File fileNew = new File(String.format("%s/META-INF/script/%s", SystemConstants.ROOT_REAL_PATH,
                    currentScript));
            if (!fileNew.exists()) {
                fileNew.createNewFile();
            }
            lastedRollbackSQLWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNew), "UTF-8"));

            File fileVersion = new File(String.format("%s/META-INF/script/%s", SystemConstants.ROOT_REAL_PATH,
                    currentVersion + ".sql"));
            if (!fileVersion.exists()) {
                fileVersion.createNewFile();
            }
            versionRollbackSQLWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileVersion),
                    "UTF-8"));
        } catch (IOException e1) {
            logger.error("", e1);
        }
        String dayYear = DateUtils.MINUTE_FORMAT.format(new Date());
        String dataVersion = String
                .format("-- version %s,build %s,module %s", currentVersion, dayYear, ROLLBACK_MODULE);
        sqls.add(0, dataVersion);
        sqls.add("DELETE FROM versionrecords WHERE currentVersion='" + currentVersion + "'");
        for (String sql : sqls) {
            try {
                if (sql == null) {
                    continue;
                }
                _output(lastedRollbackSQLWriter, sql);
                _output(versionRollbackSQLWriter, sql);
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        try {
            lastedRollbackSQLWriter.close();
            versionRollbackSQLWriter.close();
            sqls.clear();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.service.SQLRestoreService#outputRestoreSql(java.lang.String)
     */
    @Override
    public void outputRestoreSql(String sql) {
        logger.debug(sql);
        sqls.add(0, sql);
        /*fileWriter.write(sql);
        if (!sql.endsWith(";")) {
            fileWriter.write(";");
        }
        fileWriter.write("\r\n");
        fileWriter.flush();*/
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.service.SQLRestoreService#execute2(java.lang.String)
     */
    @Override
    public String execute2(String sql) {
        return sqlDecompileDao.execute2(sql);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.service.SQLRestoreService#execute3(java.lang.String)
     */
    @Override
    public List<String> execute3(String sql) {
        return sqlDecompileDao.execute3(sql);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.databaserollback.service.SQLRestoreService#execute(java.lang.String, java.util.List)
     */
    @Override
    public List<String[]> execute(String sql, List<String> columns) throws SQLException {
        return sqlDecompileDao.execute(sql, columns);
    }

}
