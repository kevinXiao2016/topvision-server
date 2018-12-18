/***********************************************************************
 * $Id: AddColumnProcess.java,v1.0 2016年7月20日 下午3:21:59 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.databaserollback.process;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.version.service.RollbackableProcess;

import net.sf.jsqlparser.JSQLParserException;

/**
 * 
 *  支持连续添加多个column
 * @author Bravin
 * @created @2016年7月20日-下午3:21:59
 *
 */
public class AddColumnProcess extends RollbackableProcess {
    public static final String PATTERN = "ALTER TABLE .* ADD COLUMN.*";
    private String tableName;
    private List<String> columns;

    /* (non-Javadoc)
     * @see com.topvision.ems.databaserollback.process.SQLProcess#resolveSQL(java.lang.String)
     */
    @Override
    public void resolveSQL(String sql) {
        columns = new ArrayList<String>();
        String[] splitSegements = sql.split("ALTER TABLE|ADD COLUMN");
        tableName = splitSegements[1].trim();
        for (int i = 2; i < splitSegements.length; i++) {
            String columnSegment = splitSegements[i].trim();
            String[] split = columnSegment.split("\\(|\\s+");
            for (int j = 0; j < split.length; j++) {
                if (!"".equals(split[j])) {
                    columns.add(split[j]);
                    break;
                }
            }
        }
    }

    @Override
    public void restore() {
        for (String column : columns) {
            String sql = "ALTER TABLE " + tableName + " DROP COLUMN " + column;
            sqlBackupService.outputRestoreSql(sql);
        }
    }

    public static void main(String[] args) throws JSQLParserException {
        String sql = "alter table cmflap add column increaseInsNum int (11) default  0  after topCmFlapInsertionFailNum,add column increaseHitPercent decimal(10,2) default 0  after missNum,add column increasePowerAdjNum int (11) default 0  after powerAdjHigherNum;"
                .toUpperCase();
        new AddColumnProcess().resolveSQL(sql);
    }

}
