/***********************************************************************
 * $Id: VersionManagerImpl.java,v 1.1 2007-10-16 下午06:25:52 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2006-2007 WantTo All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.exception.service.VersionServiceException;
import com.topvision.framework.version.dao.VersionDao;
import com.topvision.framework.version.domain.VersionRecord;

/**
 * 
 * @Create Date 2007-10-16 下午10:52:41
 * 
 * @author kelers
 * 
 */
public class VersionManager {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public static String LOG_PREFIX = "Database Version Control";
    public final static String versionTag = "-- version ";
    public static Pattern versionPattern = Pattern
            .compile("^--\\s*version\\s*([\\d\\.]+)\\s*,\\s*build\\s*([\\d-:\\s]+)\\s*,\\s*module\\s*(\\w+)");
    public static Pattern dayPattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");
    public static Pattern minutePattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}$");
    public static Pattern secondPattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$");

    private static Calendar compatibleCalendar = null;
    static {
        compatibleCalendar = Calendar.getInstance();
        compatibleCalendar.set(Calendar.YEAR, 2018);
        compatibleCalendar.set(Calendar.MONTH, 1);
        compatibleCalendar.set(Calendar.DATE, 1);
    }

    private List<Resource> dbFiles = null;
    private List<Resource> autorunFiles = null;
    private Resource[] db = null;

    private VersionDao versionDao = null;
    @Autowired
    private SQLDecompileService sqlRollbackService;
    @Autowired
    private SQLBackupService sqlBackupService;

    private List<VersionRecord> executeRecords = null;
    private List<VersionRecord> updateRecords = null;
    // 根据version进行分段，key为version，value为sql语句
    private Map<String, VersionRecord> recordMap;

    public void initialize() {
        executeRecords = new ArrayList<VersionRecord>();
        updateRecords = new ArrayList<VersionRecord>();
        recordMap = new HashMap<String, VersionRecord>();

        // 版本管理的前提是有versionrecords表
        versionDao.createVersionRecordTable();

        resetScripts();
        if (logger.isDebugEnabled()) {
            logger.debug("dbSqls:{}", dbFiles);
            logger.debug("autorunSqls:{}", autorunFiles);
        }

        try {
            smartUpdate(versionDao, dbFiles);
            sqlBackupService.output();
        } catch (Exception e) {
            logger.error(LOG_PREFIX, e);
        }
    }

    /**
     * 读取脚本文件重新组合
     */
    private void resetScripts() {
        dbFiles = new ArrayList<Resource>();
        autorunFiles = new ArrayList<Resource>();
        // 把核心Table初始化放到前面
        if (db == null || db.length == 0) {
            return;
        }
        List<Resource> tabs = new ArrayList<Resource>();
        List<Resource> datas = new ArrayList<Resource>();
        String filename = null;
        // 过滤重复的脚本文件
        Set<URL> tmp = new HashSet<URL>();
        for (Resource r : db) {
            try {
                if (tmp.contains(r.getURL())) {
                    continue;
                }
                tmp.add(r.getURL());
                filename = r.getFilename();
                if (logger.isDebugEnabled()) {
                    logger.debug(r.toString());
                }
                if (filename.indexOf("proc") != -1 || filename.indexOf("tri") != -1 || filename.indexOf("func") != -1) {
                    if (!tabs.contains(r)) {
                        tabs.add(r);
                    }
                } else if (filename.indexOf("table") != -1) {
                    if (!tabs.contains(r)) {
                        tabs.add(r);
                    }
                } else if (filename.indexOf("-data-") != -1) {
                    if (!datas.contains(r)) {
                        datas.add(r);
                    }
                } else if (filename.indexOf("autorun") != -1) {
                    if (!autorunFiles.contains(r)) {
                        autorunFiles.add(r);
                    }
                } else {
                    if (!dbFiles.contains(r)) {
                        dbFiles.add(r);
                    }
                }
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
        }
        tmp.clear();
        tmp = null;
        dbFiles.addAll(0, datas);
        dbFiles.addAll(0, tabs);
    }

    /**
     * 依据整理后的脚本文件，进行版本升级
     * 
     * @param dao
     * @param sqlResource
     */
    private void smartUpdate(VersionDao dao, List<Resource> sqlResource) {
        StringBuffer result = new StringBuffer();
        SqlExecuter exec = new SqlExecuter();
        exec.setVersionDao(dao);
        exec.setDelimiter(";");
        exec.addCommentPrefix("--");
        exec.addCommentPrefix("//");
        exec.addCommentPrefix("#");
        exec.addCommentPrefix("/*");
        exec.addCommentPrefix("*");
        exec.addCommentPrefix("*/");
        exec.addCommentPrefix("SET");
        exec.setOutputBuffer(result);

        String currentVersion = "0.0.0.0";
        try {
            com.topvision.framework.Version v = (com.topvision.framework.Version) Class
                    .forName("com.topvision.platform.SystemVersion").newInstance();
            currentVersion = v.getBuildVersion();
            exec.setCurrentVersion(currentVersion);
        } catch (Exception e) {
        }

        try {
            if (sqlResource != null && !sqlResource.isEmpty()) {
                getUpdateSQL(sqlResource);
            }
        } catch (Exception e) {
            logger.debug(new LogItem(LOG_PREFIX, sqlResource.toString()).toString(), e);
            throw new VersionServiceException(sqlResource.toString(), e);
        }

        // 获取已经执行过的版本记录
        executeRecords = dao.getRecords();

        // 将已经执行过的SQL从updateRecords中剔除
        for (Iterator<VersionRecord> itr = updateRecords.iterator(); itr.hasNext();) {
            VersionRecord vr = itr.next();
            if (executeRecords.contains(vr)) {
                itr.remove();
            }
        }

        if (executeRecords.isEmpty()) {
            logger.info(new LogItem(LOG_PREFIX, "Initialize database......").toString());
            update(dao, exec, result);
        } else if (updateRecords.isEmpty()) {
            logger.info(new LogItem(LOG_PREFIX, "No change.").toString());
        } else {
            logger.info(new LogItem(LOG_PREFIX, "Updating database.....").toString());
            exec.setSqlRollbackService(sqlRollbackService);
            update(dao, exec, result);
        }
        exec.setSqlRollbackService(null);

        // 执行autorun脚本的语句，每次启动都需要执行
        autorun(exec, autorunFiles);
        logger.info(new LogItem(LOG_PREFIX, "Database is ready.").toString());
    }

    /**
     * 从SQL脚本文件中，梳理版本块，封装成VersionRecord对象，存入updateRecords中
     * 
     * @param files
     */
    private void getUpdateSQL(List<Resource> files) {
        BufferedReader br = null;
        for (Resource dbScript : files) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Read Sql file:{}", dbScript);
                }
                if (dbScript == null) {
                    continue;
                }
                
                int sqlPriority = getSQLPriority(dbScript.getFilename());
                
                br = new BufferedReader(new InputStreamReader(dbScript.getInputStream(), "UTF-8"));
                String line = null;
                VersionRecord vr = null;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    // like --version 1.1.1,build 2006-04-12,module ems
                    // modify by fanzidong, 修改版本标识适配逻辑为正则表达式，修复时间精度无法到达时分秒的bug
                    Matcher matcher = versionPattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            if (logger.isDebugEnabled()) {
                                logger.debug(line);
                            }
                            String module = null;
                            String ver = null;
                            String build = null;

                            ver = matcher.group(1);
                            build = matcher.group(2);
                            module = matcher.group(3) + "-" + dbScript.getFilename();

                            // 由于历史原因，build时间格式可能有多种，需要进行统一格式化
                            String formatDate = formatVersionRecordTime(build);

                            VersionExpr expr = new VersionExpr(ver, formatDate);
                            expr.setModule(module);
                            if (logger.isDebugEnabled()) {
                                logger.debug(new LogItem(LOG_PREFIX, expr.getId()).toString());
                            }

                            vr = recordMap.get(expr.getId());
                            if (vr == null) {
                                vr = new VersionRecord();
                                vr.setModuleName(module);
                                vr.setVersionDate(new Timestamp(DateUtils.FULL_FORMAT.parse(formatDate).getTime()));
                                vr.setVersionNum(ver);
                                vr.setSqlPriority(sqlPriority);
                            } else {
                                // if (logger.isInfoEnabled()) {
                                logger.error(new LogItem(LOG_PREFIX, "Repeat record:" + expr.getId()).toString());
                                // }
                            }
                            recordMap.put(expr.getId(), vr);
                            if (!updateRecords.contains(vr)) {
                                updateRecords.add(vr);
                            }
                        } catch (Exception ex) {
                            logger.error(new LogItem(LOG_PREFIX, "Illegal version:" + line).toString(), ex);
                        }
                        continue;
                    } // if

                    if (vr != null) {
                        vr.append(line).append("\n");
                    }
                } // while
            } catch (FileNotFoundException ex) {
                throw new VersionServiceException("Script(" + dbScript + ")not found.\n", ex);
            } catch (Exception ex) {
                throw new VersionServiceException("unknown error", ex);
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException ex) {
                }
            }
        }
        
        // 排序
        Collections.sort(updateRecords);
    }

    private String formatVersionRecordTime(String build) {
        String formatStr = "";
        Matcher dayMatcher = dayPattern.matcher(build);
        Matcher minuteMatcher = minutePattern.matcher(build);
        Matcher secondMatcher = secondPattern.matcher(build);

        if (dayMatcher.matches()) {
            formatStr = build + " 00:00:00";
        } else if (minuteMatcher.matches()) {
            formatStr = build + ":00";
        } else if (secondMatcher.matches()) {
            formatStr = build;
        }

        // 考虑到实网中已经将部分带时分秒的转成00:00:00
        // 故在此处约定，2018-02-01之前的，即使带有时分秒，也转换成00:00:00
        try {
            Date buildTime = DateUtils.FULL_FORMAT.parse(formatStr);
            Calendar c = Calendar.getInstance();
            c.setTime(buildTime);
            if (c.before(compatibleCalendar)) {
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                formatStr = DateUtils.FULL_FORMAT.format(c.getTime());
            }
        } catch (ParseException e) {
            logger.error("", e);
        }

        return formatStr;
    }

    private Integer getSQLPriority(String fileName) {
        if(fileName.indexOf("table") != -1) {
            return VersionRecord.TABLE_PRIORITY;
        } else if(fileName.indexOf("tri") != -1) {
            return VersionRecord.TRI_PRIORITY;
        } else if(fileName.indexOf("func") != -1) {
            return VersionRecord.FUNC_PRIORITY;
        } else if(fileName.indexOf("proc") != -1) {
            return VersionRecord.PROC_PRIORITY;
        } else {
            return VersionRecord.DATA_PRIORITY;
        }
    }

    private void update(VersionDao dao, SqlExecuter exec, StringBuffer result) {
        try {
            for (VersionRecord vr : updateRecords) {
                logger.info(new LogItem(LOG_PREFIX, vr.getId()).toString());
                exec.addMissionSqls(vr);
            }
            exec.execMission();
        } catch (Exception ex) {
            logger.debug(new LogItem(LOG_PREFIX, "Update database error").toString(), ex);
            throw new VersionServiceException("Update database error", ex);
        }

        logger.info(new LogItem(LOG_PREFIX, result.insert(0, "\n").toString()).toString());
    }

    private void autorun(SqlExecuter exec, List<Resource> files) {
        if (files == null || files.size() == 0) {
            return;
        }
        StringBuffer sqls = new StringBuffer();
        BufferedReader br = null;

        for (Resource dbScript : files) {
            if (logger.isDebugEnabled()) {
                logger.debug("Read auto run sql file:" + dbScript);
            }
            try {
                if (dbScript == null) {
                    continue;
                }
                br = new BufferedReader(new InputStreamReader(dbScript.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    sqls.append(line).append("\n");
                } // while
            } catch (FileNotFoundException ex) {
                throw new VersionServiceException("Script(" + dbScript + ")not found.\n", ex);
            } catch (Exception ex) {
                throw new VersionServiceException("unknown error", ex);
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException ex) {
                }
            }
        }
        try {
            if (sqls != null && sqls.length() > 0) {
                exec.execMission(sqls.toString());
            }
        } catch (Exception ex) {
            logger.debug(new LogItem(LOG_PREFIX, "Auto run error").toString(), ex);
            throw new VersionServiceException("Auto run error", ex);
        }
    }

    /**
     * @return the db
     */
    public Resource[] getDb() {
        return db;
    }

    /**
     * @param db
     *            the db to set
     */
    public void setDb(Resource[] db) {
        this.db = db;
    }

    /**
     * @return the versionDao
     */
    public VersionDao getVersionDao() {
        return versionDao;
    }

    /**
     * @param versionDao
     *            the versionDao to set
     */
    public void setVersionDao(VersionDao versionDao) {
        this.versionDao = versionDao;
    }

}
