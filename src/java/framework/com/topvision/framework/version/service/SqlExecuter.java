/***********************************************************************
 * $Id: SQLExec.java,v 1.1 2007-10-16 PM 10:51:31 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2006-2007 WantTo All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.RunCmd;
import com.topvision.framework.version.dao.VersionDao;
import com.topvision.framework.version.domain.VersionRecord;

/**
 * 
 * @Create Date 2007-10-16 PM 10:51:31
 * 
 * @author kelers
 * 
 */
public class SqlExecuter {
    private SQLDecompileService sqlRollbackService;

    public SQLDecompileService getSqlRollbackService() {
        return sqlRollbackService;
    }

    public void setSqlRollbackService(SQLDecompileService sqlRollbackService) {
        this.sqlRollbackService = sqlRollbackService;
    }

    /**
     * SQL Transaction Executor
     * 
     * @author kelers
     * 
     */
    public class Transaction {
        private VersionRecord versionRecord;
        private String scripts = null;
        private String sqls = null;
        private String module;

        private void execSQL(VersionDao dao, String sql, RollbackableProcess rollbackableProcess) throws Exception {
            sql = sql.trim();
            // Check and ignore empty statements
            if ("".equals(sql)) {
                return;
            }
            if (sql.endsWith(";") || sql.endsWith("/")) {
                sql = sql.substring(0, sql.length() - 1);
            } else if (sql.endsWith("$$")) {
                sql = sql.substring(0, sql.length() - 2);
            }
            try {
                totalSql++;
                dao.addBatch(sql);
                if (rollbackableProcess != null) {
                    rollbackableProcess.restore();
                }
                goodSql++;
            } catch (Exception e) {
                output.append("<Warning>>>>>>>>>Run database script error>>>>>>>>>").append("\n");
                output.append("<Warning>" + sql).append("\n");
                output.append("<Warning>Exception: " + e.toString()).append("\n");
                logger.debug(e.getMessage(), e);
                output.append("<Warning><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<").append("\n");
            }

        }

        private boolean isNotScripts(String line) {
            for (String prefix : commentPrefixs) {
                if (line.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 执行SQL语句块的核心方法
         * 
         * @throws Exception
         */
        private void runTransaction() throws Exception {
            // database jdbc
            if (sqls != null && sqls.length() >= 0) {
                // output.append("Excute the sql command:").append("\n");
                StringBuilder sql = new StringBuilder();
                String line = "";
                String old_delimiter = delimiter;
                BufferedReader in = new BufferedReader(new StringReader(sqls));
                try {
                    versionDao.beginBatch();
                    while ((line = in.readLine()) != null) {
                        if (isNotScripts(line)) {
                            continue;
                        }
                        line = line.trim();
                        sql.append(" ").append(line);
                        if (line.toUpperCase().startsWith("CREATE OR REPLACE")) {
                            delimiter = "/";
                        }
                        if (line.toUpperCase().startsWith("CREATE PROCEDURE")
                                || line.toUpperCase().startsWith("CREATE TRIGGER")) {
                            delimiter = "$$";
                        }
                        if (line.toUpperCase().startsWith("CREATE FUNCTION")) {
                            delimiter = "$$";
                        }
                        if (line.toUpperCase().startsWith("CREATE EVENT")) {
                            delimiter = "$$";
                        }
                        if (sql.toString().endsWith(delimiter)) {
                            // 在实际实行之前先进行处理,如果是Rollback模块,则不执行回退
                            RollbackableProcess rollbackableProcess = null;
                            if (!"Rollback".equalsIgnoreCase(module) && sqlRollbackService != null) {
                                rollbackableProcess = sqlRollbackService.decompileSQL(sql.toString());
                            }
                            execSQL(versionDao, sql.toString(), rollbackableProcess);
                            sql = new StringBuilder();
                            delimiter = old_delimiter;
                        }
                    }
                    // end of reader , catch any statements not followed by ;
                    if (!sql.equals("")) {
                        execSQL(versionDao, sql.toString(), null);
                    }
                    
                    // add by fanzidong，应该把对应语句块的versionRecord记录插入，放在此处。
                    if(versionRecord != null) {
                        versionDao.saveRecordInBatch(getVersionRecord());
                    }
                    
                    versionDao.executeBatch();
                } catch (Exception e) {
                    logger.error("error", sqls);
                    throw e;
                }
            }
            // command line
            if (scripts != null && scripts.length() >= 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Excute the script file\n" + scripts);
                }
                String line = null;
                File file = new File("scripts.sql");
                File bat = new File("init.bat");
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Temp file:" + file.getAbsolutePath());
                    }
                    FileWriter writer = new FileWriter(file);
                    BufferedReader in = new BufferedReader(new StringReader(scripts));
                    while ((line = in.readLine()) != null) {
                        if (isNotScripts(line)) {
                            continue;
                        }
                        writer.write(line);
                        writer.write('\n');
                    }
                    writer.flush();
                    writer.close();
                    StringBuilder command = new StringBuilder();
                    command.append("\"").append(versionDao.getVariables().get("basedir")).append("\"");
                    command.append(param).append(" -f < \"");
                    command.append(file.getAbsolutePath()).append("\"");
                    if (logger.isDebugEnabled()) {
                        logger.debug("Command:" + command);
                    }
                    writer = new FileWriter(bat);
                    writer.write(command.toString());
                    writer.write("\n");
                    writer.flush();
                    writer.close();
                    RunCmd cmd = new RunCmd();
                    cmd.runCommand(bat.getPath());
                    logger.debug(cmd.getStdout());
                    output.append("Initialize the procedure and trigger success!!!\n");
                } catch (Exception ex) {
                    output.append("Initialize the procedure and trigger error:").append(ex);
                    logger.debug(ex.getMessage(), ex);
                } finally {
                    try {
                        if (!logger.isDebugEnabled()) {
                            file.delete();
                            bat.delete();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        /**
         * @param scripts
         *            the scripts to set
         */
        public void setScripts(String scripts) {
            this.scripts = scripts;
        }

        /**
         * @param sqls
         *            the sqls to set
         */
        public void setSqls(String sqls) {
            this.sqls = sqls;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public VersionRecord getVersionRecord() {
            return versionRecord;
        }

        public void setVersionRecord(VersionRecord versionRecord) {
            this.versionRecord = versionRecord;
        }

    }

    private Logger logger = LoggerFactory.getLogger(getClass());
    private int goodSql = 0, totalSql = 0;
    private String delimiter = ";";
    private String param = null;
    private VersionDao versionDao = null;
    private StringBuffer output = new StringBuffer();
    private List<String> commentPrefixs = new ArrayList<String>();
    private String currentVersion = null;

    private List<Transaction> transactions = new ArrayList<Transaction>();

    public void addCommentPrefix(String prefix) {
        this.commentPrefixs.add(prefix);
    }

    public void addMissionScripts(String scripts) {
        Transaction t = new Transaction();
        t.setScripts(scripts);
        transactions.add(t);
    }

    public void addMissionSqls(VersionRecord vr) {
        vr.setCurrentVersion(currentVersion);
        Transaction t = new Transaction();
        t.setVersionRecord(vr);
        t.setSqls(vr.getContent());
        t.setModule(vr.getModuleName());
        transactions.add(t);
    }

    public void execMission() throws Exception {
        if (transactions.size() == 0) {
            return;
        }
        this.totalSql = 0;
        this.goodSql = 0;
        // Process all transactions

        // modify by fanzidong, 一个Transaction事务的执行失败，不应当影响其他Transaction的执行
        for (Transaction tran : transactions) {
            try {
                tran.runTransaction();
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
                // throw new Exception("Excute the sql error", e);
            }
        }

        output.append("Excute the sql(").append(totalSql).append("), result is ").append(goodSql).append("/")
                .append(totalSql).append("\n");
        if (totalSql != goodSql)
            output.append("Excute the sql has error(").append(totalSql - goodSql).append(")").append("\n");
    }

    public void execMission(String sqls) throws Exception {
        this.totalSql = 0;
        this.goodSql = 0;
        try {
            Transaction t = new Transaction();
            t.setSqls(sqls);
            t.runTransaction();
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
            throw new Exception("Excute the sql error", e);
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
            throw new Exception("Excute the sql error", e);
        }

        output.append("Excute the sql(").append(totalSql).append("), result is ").append(goodSql).append("/")
                .append(totalSql).append("\n");
        if (totalSql != goodSql)
            output.append("Excute the sql has error(").append(totalSql - goodSql).append(")").append("\n");
    }

    public StringBuffer getOutputBuffer() {
        return output;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setOutputBuffer(StringBuffer output) {
        this.output = output;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * @param versionDao
     *            the versionDao to set
     */
    public void setVersionDao(VersionDao versionDao) {
        this.versionDao = versionDao;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
}
