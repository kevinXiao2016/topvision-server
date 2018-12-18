/***********************************************************************
 * $Id: PrimaryKeyHandler.java,v1.0 2016年7月25日 下午4:44:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.rollback;

/**
 * 删除/变更主键后,再插入数据可能导致rollback的时候主键重复，这种情况需要特殊处理 
 * @author Bravin
 * @created @2016年7月25日-下午4:44:18
 *
 */
public class PrimaryKeyHandler extends SpecialSQLHandler {
    public static final String PATTERN = "ALTER TABLE .* (ADD|DROP) PRIMARY KEY.*";

    public static void main(String[] args) {
        System.out.println("alter table Cmc8800BDSIpqamMappings DR primary key(mappingId);".toUpperCase().matches(
                PATTERN));
    }
}
