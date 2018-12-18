/***********************************************************************
 * $Id: MysqlTest.java,v1.0 2014-4-23 上午9:42:59 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.utils;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Administrator
 * @created @2014-4-23-上午9:42:59
 * 
 */
public class MysqlTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3003/ems";
        String user = "root";
        String password = "ems";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        if (!conn.isClosed())
            System.out.println("Succeeded connecting to the Database!");
        Statement statement = conn.createStatement();
        String setSql = "set character_set_client = 'utf8'";
        statement.execute(setSql);
        String sql = "create table `中国` (id int)";
        
        String insertSql = "insert into gbk4 select 1101,'武汉'";
        //statement.execute(sql);
        
        setSql = "select table_name from information_schema.tables where table_schema = 'ems'";
        /*statement.execute(setSql);
        insertSql = "insert into gbk4 select 1102,'武汉'";
        statement.execute(insertSql);*/
        
        ResultSet rs2 = statement.executeQuery(setSql);
        while (rs2.next()) {
            byte[] b = rs2.getBytes("table_name");
            for(byte tmp : b ){
                System.out.println(tmp & 0xFF);
            }
            String address = new String(b,"utf8");
            System.out.println(address);
        } 
    }
}
