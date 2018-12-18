/***********************************************************************
 * $Id: DatabaseService.java,v 1.1 Sep 29, 2009 5:02:10 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.DatabaseInfo;

/**
 * @author kelers
 * @Create Date Sep 29, 2009 5:02:10 PM
 */
public interface DatabaseService extends Service {

    /**
     * 获取数据库信息
     * 
     * @return 数据库信息
     */
    DatabaseInfo getDatabaseInfo();

    /**
     * 重新初始化数据库
     * 
     * @param database
     */
    void reinit(String database);

    /* *//**
     * 历史告警数据定期清除
     * 
     *//*
    void cycleHistoryDataClean(Long time);*/
    
    /**
     * 更新历史数据保存时长
     * 
     * @param keepMonth
     */
    void updateHistoryDataKeepMonth(Integer keepMonth);
    
    /**
     * 获得历史数据保存时长
     * 
     * @return
     */
    Integer getHistoryDataKeep();

    /**
     * 获得表行数
     * 
     * @param tableName
     * @return
     */
    List<TableInfo> fetchTableCount(String[] tableName);
     
}
