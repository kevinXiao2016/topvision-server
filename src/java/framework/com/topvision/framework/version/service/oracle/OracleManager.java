/***********************************************************************
 * $Id: OracleManager.java,v 1.1 2010-1-7 下午01:15:04 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service.oracle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.version.service.DatabaseManager;

/**
 * @Create Date 2010-1-7 下午01:15:04
 * 
 * @author kelers
 * 
 */
@Service("oracleManager")
public class OracleManager extends DatabaseManager {
    private String dbaPassword;
    private String dbaUsername;

    /**
     * @return the dbaPassword
     */
    public String getDbaPassword() {
        return dbaPassword;
    }

    /**
     * @param dbaPassword
     *            the dbaPassword to set
     */
    public void setDbaPassword(String dbaPassword) {
        this.dbaPassword = dbaPassword;
    }

    /**
     * @return the dbaUsername
     */
    public String getDbaUsername() {
        return dbaUsername;
    }

    /**
     * @param dbaUsername
     *            the dbaUsername to set
     */
    public void setDbaUsername(String dbaUsername) {
        this.dbaUsername = dbaUsername;
    }

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
            conn = DriverManager.getConnection(url, dbaUsername, dbaPassword);
            st = conn.createStatement();
            // st.execute("CREATE DATABASE IF NOT EXISTS ems");
            st.execute("Create User ems identified by ems");
            st.execute("Grant Connect,Resource to ems");
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
        Statement st2 = null;
        Connection conn = null;
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, dbaUsername, dbaPassword);
            st = conn.createStatement();
            st2 = conn.createStatement();
            String sql = "select sid,serial# from v$session where upper(username)=upper('" + username + "')";
            String killSession;
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                killSession = "alter system kill session '" + rs.getString(1) + "," + rs.getString(2) + "'";
                st2.execute(killSession);
            }
            // st.execute("DROP DATABASE IF EXISTS ems");
            st.execute("DROP USER EMS CASCADE");
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
     * @see com.topvision.framework.version.service.DatabaseManager#exportDataBaseScript()
     */
    @Override
    public String exportDataBaseScript(String tableNames) throws Exception {
        Class.forName(driverClass);
        Connection conn = null;
        conn = DriverManager.getConnection(url, username, password);
        String fileName = System.getProperty("java.io.tmpdir") + System.currentTimeMillis();
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String tableInfoSql;
        String v_part_1;
        String v_part_2;
        String v_part_3;
        String v_sql;
        String v_sqlneed;
        String v_stringstartneed;
        String v_stringendneed;
        Statement statement;
        ResultSet resultSet;
        Statement s2;
        ResultSet resultSet2;
        for (String name : tableNames.split(" ")) {
            tableInfoSql = "select column_name, data_type from user_tab_columns where table_name='" + name + "'";
            v_part_1 = "insert into " + name + " (";
            v_part_2 = "";
            v_part_3 = "";
            v_sql = "";
            v_sqlneed = "||','||";
            v_stringstartneed = "''''||";
            v_stringendneed = "||''''";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(tableInfoSql);
            while (resultSet.next()) {
                v_part_2 = v_part_2 + resultSet.getString(1) + ",";
                if (resultSet.getString(2).indexOf("TIMESTAMP") != -1 || resultSet.getString(2).indexOf("DATE") != -1) {
                    v_part_3 = v_part_3 + "'to_date('||''''||to_char(" + resultSet.getString(1)
                            + ",'yyyy-mm-dd hh24:mi:ss')||''''||','||''''||'yyyy-mm-dd hh24:mi:ss'||''''||')'"
                            + v_sqlneed;
                } else if (resultSet.getString(2).indexOf("VARCHAR2") != -1
                        || resultSet.getString(2).indexOf("VARCHAR") != -1
                        || resultSet.getString(2).indexOf("CHAR") != -1) {
                    v_part_3 = v_part_3 + v_stringstartneed + "case when " + resultSet.getString(1)
                            + " IS NOT NULL THEN " + resultSet.getString(1) + " ELSE NULL END" + v_stringendneed
                            + v_sqlneed;
                } else if (resultSet.getString(2).indexOf("NUMBER") != -1) {
                    v_part_3 = v_part_3 + "case when " + resultSet.getString(1) + " IS NOT NULL THEN "
                            + resultSet.getString(1) + " ELSE NULL END" + v_sqlneed;
                } else if (resultSet.getString(2).indexOf("BLOB") != -1 || resultSet.getString(2).indexOf("CLOB") != -1) {
                    v_part_3 = v_part_3 + "'NULL'" + v_sqlneed;
                } else {
                    v_part_3 = v_part_3 + "case when " + resultSet.getString(1) + " IS NOT NULL THEN "
                            + resultSet.getString(1) + " ELSE NULL END" + v_sqlneed;
                }
            }
            v_part_2 = v_part_2.substring(0, v_part_2.length() - 1) + ") values (' ||";
            v_part_3 = v_part_3.substring(0, v_part_3.length() - 7);
            v_sql = "select '" + v_part_1 + v_part_2 + v_part_3 + "||')' from " + name;
            s2 = conn.createStatement();
            resultSet2 = s2.executeQuery(v_sql);
            while (resultSet2.next()) {
                String insertSql = resultSet2.getString(1);
                while (insertSql.indexOf(",,") != -1) {
                    insertSql = insertSql.replaceAll(",,", ",NULL,");
                }
                while (insertSql.indexOf(",)") != -1) {
                    insertSql = insertSql.replaceAll(",\\)", ",NULL\\)");
                }
                bufferedWriter.write(insertSql + ";");
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();
        return fileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.versio
     * n.service.DatabaseManager#processRecoveryResult(java.util.List,
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
