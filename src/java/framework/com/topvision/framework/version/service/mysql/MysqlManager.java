/***********************************************************************
 * $Id: MysqlManager.java,v 1.1 Sep 26, 2009 11:47:39 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service.mysql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.topvision.framework.common.RunCmd;
import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.version.service.DatabaseManager;

/**
 * @Create Date Sep 26, 2009 11:47:39 AM
 * 
 * @author kelers
 * 
 */
@Service("mysqlManager")
public class MysqlManager extends DatabaseManager {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.version.service.DatabaseManager#createDatabase()
     */
    @Override
    public void createDatabase() {
        Statement st = null;
        Connection conn = null;
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url.replace("ems", "mysql"), username, password);
            st = conn.createStatement();
            st.execute("CREATE DATABASE IF NOT EXISTS ems");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.version.service.DatabaseManager#dropDatabase()
     */
    @Override
    public void dropDatabase() {
        Statement st = null;
        Connection conn = null;
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url.replace("ems", "mysql"), username, password);
            st = conn.createStatement();
            st.execute("DROP DATABASE IF EXISTS ems");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private String getMysqlPath() {
        Statement st = null;
        Connection conn = null;
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url.replace("ems", "mysql"), username, password);
            st = conn.createStatement();
            String sql = "show variables like 'basedir'";
            ResultSet resultSet = st.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getString(2);
            }
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.framework.version.service.DatabaseManager#exportDataBaseScript(java.lang.String
     * )
     */
    @Override
    public String exportDataBaseScript(String tableNames) throws Exception {
        RunCmd runCmd = new RunCmd();
        getMysqlPath();
        String fileName = System.getProperty("java.io.tmpdir") + System.currentTimeMillis();
        String mySqlPath = getMysqlPath() + "bin" + File.separator + "mysqldump";
        // url like jdbc:mysql://localhost:3003/ems
        String mySqlAddress = url.split("//")[1].split(":")[0];
        String mySqlPort = url.split("//")[1].split(":")[1].split("/")[0];
        String mySqlDbName = url.split("//")[1].split("/")[1];
        StringBuilder mySqlBuilder = new StringBuilder();
        mySqlBuilder.append("cmd /c \"");
        mySqlBuilder.append(mySqlPath);
        mySqlBuilder.append("\" -u");
        mySqlBuilder.append(username);
        mySqlBuilder.append(" -p");
        mySqlBuilder.append(password);
        mySqlBuilder.append(" -h");
        mySqlBuilder.append(mySqlAddress);
        mySqlBuilder.append(" -P");
        mySqlBuilder.append(mySqlPort);
        mySqlBuilder.append(" ");
        mySqlBuilder.append(mySqlDbName);
        mySqlBuilder.append(" ");
        mySqlBuilder.append(tableNames);
        mySqlBuilder.append(" -c -t --extended-insert=false --trigger=false --default-character-set=utf8>");
        mySqlBuilder.append(fileName);
        // String cmdCommand =
        // "cmd /c \""+mySqlPath+"\" -u"+username+" -p"+password+" -h"+mySqlAddress+" -P"+mySqlPort+" "+mySqlDbName+" "
        // + tableNames
        // + " -c -t --extended-insert=false --trigger=false --default-character-set=utf8>" +
        // fileName;
        runCmd.runCommand(mySqlBuilder.toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        File cmdFile = new File(fileName);
        String jdbcFileName = System.getProperty("java.io.tmpdir") + System.currentTimeMillis();
        File jdbcFile = new File(jdbcFileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(cmdFile), "UTF-8"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jdbcFile),
                "UTF-8"));
        String readString;
        while ((readString = bufferedReader.readLine()) != null) {
            if (readString.startsWith("INSERT INTO")) {
                /*
                 * int startIndex = readString.indexOf("`"); int endIndex =
                 * readString.substring(startIndex + 1).indexOf("`"); String deleteString = new
                 * String(); deleteString = "delete from " + readString.substring(startIndex,
                 * startIndex + endIndex + 2) + ";"; if (!containString(deleteSql, deleteString)) {
                 * bufferedWriter.write(deleteString); bufferedWriter.newLine();
                 * deleteSql.add(deleteString); }
                 */
                bufferedWriter.write(readString.replaceAll("`", ""));
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();
        // bufferedReader.close();
        return jdbcFileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.framework.version.service.DatabaseManager#processRecoveryResult(java.util.List,
     * com.topvision.framework.domain.DataRecoveryResult)
     */
    @Override
    public List<String> processRecoveryResult(List<TableInfo> tableInfos, DataRecoveryResult dataRecoveryResult) {
        List<String> rList = new ArrayList<String>();
        List<Integer> tmp = new ArrayList<Integer>();
        List<Integer> result = new ArrayList<Integer>();
        for (int i : dataRecoveryResult.getRecoveryResult()) {
            result.add(i);
        }
        for (TableInfo count : tableInfos) {
            if (count.getTableCount().equals("0")) {
                rList.add(count.getTableName() + ":" + "0/0");
            } else {
                if (result.size() < Integer.parseInt(count.getTableCount())) {
                    rList.add(count.getTableName() + ":" + result.size() + "/" + count.getTableCount());
                    return rList;
                } else {
                    rList.add(count.getTableName() + ":" + count.getTableCount() + "/" + count.getTableCount());
                    tmp.addAll(result.subList(0, Integer.parseInt(count.getTableCount())));
                    result = result.subList(Integer.parseInt(count.getTableCount()), result.size());
                }
            }
        }
        return rList;
    }
}
