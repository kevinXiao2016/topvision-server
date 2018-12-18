/***********************************************************************
 * $Id: PlatVersionConvert.java,v1.0 2012-8-6 上午10:38:04 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dbconvert;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.platform.service.DataManageParser;
import com.topvision.platform.util.DataBaseConstants;

/**
 * @author RodJohn
 * @created @2012-8-6-上午10:38:04
 * 
 */
@Service("platVersionConvert")
public class PlatVersionConvert extends VersionConvert {
    @Autowired
    private DataManageParser dataManageParser;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.DataModule#processModule(java.util.List,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<String> processModule(List<String> sqlList, String nowVersion, String fileVersion) {
        String[] tables = DataBaseConstants.PLATFORM_TABLE.split(" ");
        Object[] result = new Object[tables.length];
        for (int i = 0; i < tables.length; i++) {
            List<String> insertList = new ArrayList<String>();
            result[i] = insertList;
            for (String sql : sqlList) {
                if (getTableNameFromInsertSql(sql).equals(tables[i])) {
                    insertList.add(sql);
                }
            }
        }
        return makeList(result);
    }

    public List<String> makeList(Object[] result) {
        List<String> rList = new ArrayList<String>();
        for (Object object : result) {
            @SuppressWarnings("unchecked")
            List<String> strings = (List<String>) object;
            if (strings.size() > 0) {
                rList.addAll(strings);
            }
        }
        return rList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        setModule();
        dataManageParser.registDataModule(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        dataManageParser.unRegistDataModule(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#convertFrom165to170(java.util.List)
     * 
     * @Override public void convertFrom165to170(List<String> sql) { }
     * 
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#convertFrom170to174(java.util.List)
     * 
     * @Override public void convertFrom170to174(List<String> sql) { for (int i = 0; i < sql.size();
     * i++) { if (sql.get(i).startsWith("INSERT INTO `entity`")) { sql.set(i,
     * sql.get(i).replaceAll("offManagementTime", "offManagementTime")); } } }
     */
    /**
     * @return the dataManageParser
     */
    public DataManageParser getDataManageParser() {
        return dataManageParser;
    }

    /**
     * @param dataManageParser
     *            the dataManageParser to set
     */
    public void setDataManageParser(DataManageParser dataManageParser) {
        this.dataManageParser = dataManageParser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setStartVersion()
     */
    @Override
    public void setStartVersion() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setEndVersion()
     */
    @Override
    public void setEndVersion() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setPriority()
     */
    @Override
    public void setPriority() {
        this.priority = 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setModule()
     */
    @Override
    public void setModule() {
        this.module = "PLAT";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#convertFrom174to175(java.util.List)
     */
    /*
     * @Override public void convertFrom174to175(List<String> sql) { for (int i = 0; i < sql.size();
     * i++) { if (sql.get(i).startsWith("INSERT INTO `entity`")) { sql.set(i,
     * sql.get(i).replaceAll("offManagement", "offManagementTimes")); } } }
     */

}
