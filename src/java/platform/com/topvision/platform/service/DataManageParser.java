/***********************************************************************
 * $Id: DataManageParser.java,v1.0 2012-8-3 上午10:09:52 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.platform.dao.DatabaseDao;
import com.topvision.platform.dbconvert.VersionConvert;

/**
 * @author RodJohn
 * @created @2012-8-3-上午10:09:52
 * 
 */
@Service("dataManageParser")
public class DataManageParser {
    private Map<String, List<VersionConvert>> manageMap = new LinkedHashMap<String, List<VersionConvert>>();
    @Autowired
    private DatabaseDao databaseDao;

    @PreDestroy
    public void destroy() {
        manageMap = null;
    }

    @PostConstruct
    public void initialize() {
        manageMap.put("PLAT", new ArrayList<VersionConvert>());
        manageMap.put("SERVER", new ArrayList<VersionConvert>());
        manageMap.put("TOPOLOGY", new ArrayList<VersionConvert>());
        manageMap.put("ALERT", new ArrayList<VersionConvert>());
        manageMap.put("EPON", new ArrayList<VersionConvert>());
        manageMap.put("CMC", new ArrayList<VersionConvert>());
    }

    /**
     * 注册新的数据模块处理单元
     * 
     * @param eventParser
     *            处理模型
     */
    public void registDataModule(VersionConvert versionConvert) {
        // System.out.print(versionConvert.getEndVersion());
        if (manageMap.containsKey(versionConvert.getModule())) {
            List<VersionConvert> vList = manageMap.get(versionConvert.getModule());
            vList.add(versionConvert);
            Collections.sort(vList);
        }
    }

    /**
     * 销毁数据模块处理单元
     * 
     * @param eventParser
     *            处理模型
     */
    public void unRegistDataModule(VersionConvert versionConvert) {

    }

    public DataRecoveryResult processModule(List<String> sqlList, String nowVersion, String fileVersion,
            String databaseType) throws Exception {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, List<VersionConvert>> entry : manageMap.entrySet()) {
            List<String> moduleSqlStrings = new ArrayList<String>();
            moduleSqlStrings = sqlList;
            if (entry.getValue().size() > 0) {
                for (VersionConvert convert : entry.getValue()) {
                    moduleSqlStrings = convert.processModule(moduleSqlStrings, nowVersion, fileVersion);
                }
                result.addAll(moduleSqlStrings);
            }
        }
        DataRecoveryResult recoveryResult = databaseDao.jdbcFullImport(result);
        return recoveryResult;
    }

    /**
     * @return the databaseDao
     */
    public DatabaseDao getDatabaseDao() {
        return databaseDao;
    }

    /**
     * @param databaseDao
     *            the databaseDao to set
     */
    public void setDatabaseDao(DatabaseDao databaseDao) {
        this.databaseDao = databaseDao;
    }

}
