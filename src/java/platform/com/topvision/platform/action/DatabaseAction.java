/***********************************************************************
 * $Id: DatabaseAction.java,v 1.1 2009-9-28 上午09:45:47 kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.version.service.DatabaseManager;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.DatabaseInfo;
import com.topvision.platform.service.DataManageParser;
import com.topvision.platform.service.DatabaseService;
import com.topvision.platform.util.DataBaseConstants;

import net.sf.json.JSONObject;

@Controller("databaseAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DatabaseAction extends BaseAction {
    private static final long serialVersionUID = 7675538788028585221L;
    private static final String CORPORATION = "topvisionNM3000";
    public static final String ORACLE = "Oracle";
    public static final String MYSQL = "MySQL";
    private static final String ANNOTATION_START = "/*";
    private static final String ANNOTATION_END = "*/";
    private Logger logger = LoggerFactory.getLogger(DatabaseAction.class);
    // 历史告警清除周期
    private Integer days;
    @Resource(name = "mysqlManager")
    private DatabaseManager mysqlManager;
    @Resource(name = "oracleManager")
    private DatabaseManager oracleManager;
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseService databaseService;
    private String category;
    @Autowired
    private DataManageParser dataManageParser;
    private File importFile;

    private Integer historyKeepInterval;

    /**
     * 获取数据库连接信息
     * 
     * @return DatabaseInfo
     */
    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    /**
     * 获取数据库类型及版本信息
     * 
     * @return String ACTION指定字符串
     */
    public String showDatabaseParam() {
        databaseInfo = databaseService.getDatabaseInfo();
        historyKeepInterval = databaseService.getHistoryDataKeep();
        return SUCCESS;
    }

    /**
     * 获取数据登录用户密码
     * 
     * @return 登录用户密码
     */
    public String getPassword() {
        return mysqlManager.getPassword();
    }

    /**
     * 获取数据库连接地址
     * 
     * @return 数据库连接地址
     */
    public String getUrl() {
        return mysqlManager.getUrl();
    }

    /**
     * 获取数据库登录用户
     * 
     * @return 登录用户名
     */
    public String getUsername() {
        return mysqlManager.getUsername();
    }

    /**
     * 重新初始化数据库
     * 
     * @return String ACTION指定字符串
     */
    public String reInitDatabase() {
        databaseInfo = databaseService.getDatabaseInfo();
        // Modify by victor@2012.12.14获取的数据库名称在不同版本大小写可能不一样，改为判断时不区分大小写
        if (databaseInfo.getDatabaseProductName().equalsIgnoreCase("Oracle")) {
            if (oracleManager != null) {
                oracleManager.dropDatabase();
                oracleManager.createDatabase();
            }
        } else if (databaseInfo.getDatabaseProductName().equalsIgnoreCase("MySQL")) {
            if (mysqlManager != null) {
                mysqlManager.dropDatabase();
                mysqlManager.createDatabase();
                // 重新初始化会导致有些任务保存失败，所以还是需要重启server来重新初始化数据。
                // mysqlManager.initDatabase();
            }
        }
        return NONE;
    }

    /**
     * 全库数据导出
     * 
     * @return
     */
    public String databaseExportFull() {
        try {
            databaseInfo = databaseService.getDatabaseInfo();
            String fileName = null;
            String tableNames = null;
            if (category.equals("TOPOLOGY")) {
                tableNames = DataBaseConstants.TOPOLOGY_TABLE;
            } else if (category.equals("EVENT")) {
                tableNames = DataBaseConstants.EVENT_TABLE;
            } else if (category.equals("FULL")) {
                tableNames = DataBaseConstants.ALL_TABLE;
            }
            List<TableInfo> counts = databaseService.fetchTableCount(tableNames.split(" "));
            StringBuilder stringBuilder = new StringBuilder();
            for (TableInfo count : counts) {
                stringBuilder.append("&");
                stringBuilder.append(count.getTableName());
                stringBuilder.append(":");
                stringBuilder.append(count.getTableCount());
            }
            if (databaseInfo.getDatabaseProductName().equals(ORACLE)) {
                if (oracleManager != null) {
                    fileName = oracleManager.exportDataBaseScript(tableNames);
                }
            } else if (databaseInfo.getDatabaseProductName().equals(MYSQL)) {
                if (mysqlManager != null) {
                    fileName = mysqlManager.exportDataBaseScript(tableNames);
                }
            }
            // Modify by victor @2012.12.14自动获取系统版本号，com.topvision.ems.Version不在一个包，直接new会导致编译错误
            Class<?> clazz = Class.forName("com.topvision.ems.Version");
            com.topvision.framework.Version v = (com.topvision.framework.Version) clazz.newInstance();
            downLoadFile(fileName, stringBuilder.toString().substring(1), v.getBuildVersion(),
                    databaseInfo.getDatabaseProductName());
        } catch (Exception e) {
            logger.debug("", e);
        }
        return NONE;
    }

    /**
     * 全库数据导入
     * 
     * @return
     */
    public String databaseImportFull() {
        return null;
    }

    public void downLoadFile(String fileName, String tableCount, String version, String databaseType)
            throws UnsupportedEncodingException {
        File jdbcFile = new File(fileName);
        OutputStream out = null;
        FileInputStream fis = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        ServletActionContext.getResponse().setContentType("application/x-download");
        String readLine;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=NM3000");
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(jdbcFile), "UTF-8"));
            out = ServletActionContext.getResponse().getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            // 注释标识符
            writer.write(ANNOTATION_START);
            writer.newLine();
            // 公司标识
            writer.write(CORPORATION);
            writer.newLine();
            // 版本标识
            writer.write(version);
            writer.newLine();
            // 数据表行数信息
            writer.write(tableCount);
            writer.newLine();
            // 数据库类型信息
            writer.write(databaseType);
            writer.newLine();
            // 注释标识符
            writer.write(ANNOTATION_END);
            writer.newLine();
            while ((readLine = reader.readLine()) != null) {
                writer.write(readLine);
                writer.newLine();
            }
            writer.flush();
        } catch (Exception e) {
            logger.debug("downFile method is error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                logger.debug("downFile method is error:{}", e);
            }
        }
    }

    /**
     * 分类数据导入
     * 
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String databaseImportInCategory() throws IOException {
        String resultString = null;
        databaseInfo = databaseService.getDatabaseInfo();
        // String message;
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(importFile), "UTF-8"));
            List<String> sqlStrings = new ArrayList<String>();
            String sqlString;
            String annotationStart = bufferedReader.readLine();
            if (!annotationStart.equals(ANNOTATION_START)) {
                resultString = "wrongfile";
                writeDataToAjax(resultString);
                return NONE;
            }
            String corporation = bufferedReader.readLine();
            if (!corporation.equals(CORPORATION)) {
                resultString = "wrongfile";
                writeDataToAjax(resultString);
                return NONE;
            }
            String version = bufferedReader.readLine();
            // TODO 验证版本
            Class<?> clazz = Class.forName("com.topvision.ems.Version");
            com.topvision.framework.Version v = (com.topvision.framework.Version) clazz.newInstance();
            if (!v.getBuildVersion().equals(version)) {
                resultString = "wrongVersion";
                writeDataToAjax(resultString);
                return NONE;
            }
            String tableNumString = bufferedReader.readLine();
            String[] tableNum = tableNumString.split("&");
            List<TableInfo> counts = new ArrayList<TableInfo>();
            for (String table : tableNum) {
                TableInfo count = new TableInfo(table.split(":")[0], table.split(":")[1]);
                counts.add(count);
            }
            String databaseType = bufferedReader.readLine();
            if (!databaseType.equals(databaseInfo.getDatabaseProductName())) {
                resultString = "wrongDatabaseType";
                writeDataToAjax(resultString);
                return NONE;
            }
            String annotationEnd = bufferedReader.readLine();
            if (!annotationEnd.equals(ANNOTATION_END)) {
                resultString = "wrongfile";
                writeDataToAjax(resultString);
                return NONE;
            }
            while ((sqlString = bufferedReader.readLine()) != null) {
                if (databaseType.equals(MYSQL)) {
                    sqlStrings.add(sqlString);
                } else if (databaseType.equals(ORACLE)) {
                    // Oracle脚本需要去掉末位的分号
                    sqlStrings.add(sqlString.substring(0, sqlString.length() - 1));
                }
            }
            List<String> result = new ArrayList<String>();
            DataRecoveryResult dataRecoveryResult = new DataRecoveryResult();
            try {
                dataRecoveryResult = dataManageParser.processModule(sqlStrings, v.getBuildVersion(), version,
                        databaseType);
            } catch (Exception e) {
                logger.error("", e);
            }
            if (databaseType.equals(ORACLE)) {
                if (oracleManager != null) {
                    result = oracleManager.processRecoveryResult(counts, dataRecoveryResult);
                }
            } else if (databaseType.equals(MYSQL)) {
                if (mysqlManager != null) {
                    result = mysqlManager.processRecoveryResult(counts, dataRecoveryResult);
                }
            }
            if (dataRecoveryResult.getErrorInfo().equals("NOERROR")) {
                resultString = "NOERROR*" + result.toString();
            } else {
                resultString = dataRecoveryResult.getErrorInfo();
            }
        } catch (Exception e) {
            logger.debug("", e);
            resultString = "fileImportWrong";
        }
        writeDataToAjax(resultString);
        return NONE;
    }

    /**
     * 历史告警数据定期清除
     * 
     * @return
     */
    public String cycleHistoryDataClean() throws Exception {
        JSONObject resultMsg = new JSONObject();
        // 修改历史数据保存时长
        try {
            databaseService.updateHistoryDataKeepMonth(historyKeepInterval);
        } catch (Exception e) {
            logger.error("", e);
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public void setDatabaseInfo(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void setMysqlManager(DatabaseManager mysqlManager) {
        this.mysqlManager = mysqlManager;
    }

    public DatabaseManager getOracleManager() {
        return oracleManager;
    }

    public void setOracleManager(DatabaseManager oracleManager) {
        this.oracleManager = oracleManager;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DataManageParser getDataManageParser() {
        return dataManageParser;
    }

    public void setDataManageParser(DataManageParser dataManageParser) {
        this.dataManageParser = dataManageParser;
    }

    public File getImportFile() {
        return importFile;
    }

    public void setImportFile(File importFile) {
        this.importFile = importFile;
    }

    /**
     * @return the historyKeepInterval
     */
    public Integer getHistoryKeepInterval() {
        return historyKeepInterval;
    }

    /**
     * @param historyKeepInterval
     *            the historyKeepInterval to set
     */
    public void setHistoryKeepInterval(Integer historyKeepInterval) {
        this.historyKeepInterval = historyKeepInterval;
    }

}
