/***********************************************************************
 * $Id: CmcVersionConvert.java,v1.0 2012-12-15 下午02:12:29 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dbconvert;

import java.util.ArrayList;
import java.util.List;

import com.topvision.platform.dbconvert.VersionConvert;
import com.topvision.platform.service.DataManageParser;
import com.topvision.platform.util.DataBaseConstants;

/**
 * @author RodJohn
 * @created @2012-12-15-下午02:12:29
 *
 */
public class CmcVersionConvert extends VersionConvert {
    private DataManageParser dataManageParser;
    //private int cos = 10;
    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#initialize()
     */
    @Override
    public void initialize() {
        setModule();
        dataManageParser.registDataModule(this);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#destroy()
     */
    @Override
    public void destroy() {
        dataManageParser.unRegistDataModule(this);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#setStartVersion()
     */
    @Override
    public void setStartVersion() {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#setEndVersion()
     */
    @Override
    public void setEndVersion() {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#setPriority()
     */
    @Override
    public void setPriority() {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#setModule()
     */
    @Override
    public void setModule() {
        this.module = "CMC";
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dbconvert.VersionConvert#processModule(java.util.List, java.lang.String, java.lang.String)
     */
    @Override
    public List<String> processModule(List<String> sqlList, String nowVersion, String fileVersion) {
        String[] tables = DataBaseConstants.CC_TABLE.split(" ");
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

    /**
     * @return the dataManageParser
     */
    public DataManageParser getDataManageParser() {
        return dataManageParser;
    }

    /**
     * @param dataManageParser the dataManageParser to set
     */
    public void setDataManageParser(DataManageParser dataManageParser) {
        this.dataManageParser = dataManageParser;
    }

    
}
