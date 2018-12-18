/***********************************************************************
 * $Id: DatabaseServiceImpl.java,v 1.1 Sep 29, 2009 5:02:33 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.version.service.DatabaseManager;
import com.topvision.platform.dao.DatabaseDao;
import com.topvision.platform.domain.DatabaseInfo;
import com.topvision.platform.service.DatabaseCleanService;
import com.topvision.platform.service.DatabaseService;

@Service("databaseService")
public class DatabaseServiceImpl extends BaseService implements DatabaseService {
    @Autowired
    private DatabaseDao databaseDao;
    // 数据库管理器
    private DatabaseManager mysqlManager;
    private DatabaseManager hsqlManager;
    @Autowired
    private DatabaseCleanService databaseCleanService;

    @Override
    public DatabaseInfo getDatabaseInfo() {
        return databaseDao.getDatabaseInfo();
    }

    @Override
    public void reinit(String database) {
        mysqlManager.createDatabase();
        hsqlManager.createDatabase();
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.service.DatabaseService#updateHistoryDataKeepMonth(java.lang.Integer)
     */
    @Override
    public void updateHistoryDataKeepMonth(final Integer keepMonth) {
        databaseDao.updateHistoryKeepMonth(keepMonth);
        databaseCleanService.txUpdateCleanTask(keepMonth);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
        Integer keepMonth = databaseDao.getHistoryKeepMonth();
        databaseCleanService.txCleanHistoryData(keepMonth);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    public void destroy() {
    }

    public void setDatabaseDao(DatabaseDao databaseDao) {
        this.databaseDao = databaseDao;
    }

    public void setHsqlManager(DatabaseManager hsqlManager) {
        this.hsqlManager = hsqlManager;
    }

    public void setMysqlManager(DatabaseManager mysqlManager) {
        this.mysqlManager = mysqlManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.DatabaseService#exportData()
     */
    /*
     * @Override public String exportDataInCategory(String category) throws Exception { RunCmd
     * runCmd = new RunCmd(); String[] tableNames = category.split(" "); //int[] countResult =
     * databaseDao.fetchTableCount(tableNames); StringBuilder stringBuilder = new StringBuilder();
     * for (int k = 0; k < tableNames.length; k++) { stringBuilder.append("&");
     * stringBuilder.append(tableNames[k]); stringBuilder.append(":");
     * //stringBuilder.append(countResult[k]); } String countString =
     * stringBuilder.toString().substring(1); String fileName = "MySqlBack" +
     * System.currentTimeMillis() + ".mbk"; String cmdCommand =
     * "cmd /c f:\\mp3\\com\\mysqldump -uroot -pems -hlocalhost -P3003 ems " + category +
     * " -c -e -t --trigger=false --default-character-set=utf8> d:\\temp.mkb";
     * runCmd.runCommand(cmdCommand); try { Thread.sleep(2000); } catch (InterruptedException e) {
     * logger.error("", e); } File cmdFile = new File("D:" + File.separator + "temp.mkb"); File
     * jdbcFile = new File("D:" + File.separator + fileName); FileReader fileReader = new
     * FileReader(cmdFile); FileWriter fileWriter = new FileWriter(jdbcFile); BufferedReader
     * bufferedReader = new BufferedReader(fileReader); BufferedWriter bufferedWriter = new
     * BufferedWriter(fileWriter); String readString; Version version = new Version(); // TODO //
     * bufferedWriter.write(version.getBuildVersion()); bufferedWriter.write("1.7.4");
     * bufferedWriter.newLine(); bufferedWriter.write(countString); bufferedWriter.newLine(); while
     * ((readString = bufferedReader.readLine()) != null) { if
     * (readString.startsWith("INSERT INTO")) {
     * 
     * int startIndex = readString.indexOf("`"); int endIndex = readString.substring(startIndex +
     * 1).indexOf("`"); String deleteString = new String(); deleteString = "delete from " +
     * readString.substring(startIndex, startIndex + endIndex + 2) + ";"; if
     * (!containString(deleteSql, deleteString)) { bufferedWriter.write(deleteString);
     * bufferedWriter.newLine(); deleteSql.add(deleteString); }
     * 
     * bufferedWriter.write(readString); bufferedWriter.newLine(); } } bufferedWriter.flush();
     * bufferedReader.close(); bufferedWriter.close(); fileReader.close(); //
     * bufferedReader.close(); fileWriter.close(); return jdbcFile.getAbsolutePath(); }
     */

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.DatabaseService#fetchTableCount(java.lang.String[])
     */
    @Override
    public List<TableInfo> fetchTableCount(String[] tableName) {
        return databaseDao.fetchTableCount(tableName);
    }


    /* (non-Javadoc)
     * @see com.topvision.platform.service.DatabaseService#getHistoryDataKeep()
     */
    @Override
    public Integer getHistoryDataKeep() {
        return databaseDao.getHistoryKeepMonth();
    }

}
