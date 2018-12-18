/***********************************************************************
 * $Id: ImportDao.java,v1.0 2015-7-15 下午4:05:51 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntityFolderRela;

/**
 * @author fanzidong
 * @created @2015-7-15-下午4:05:51
 * 
 */
public interface ImportDao {

    /**
     * 导入sheet数据
     * 
     * @param sheetName
     * @param list
     */
    void importSheetData(String sheetName, List<Object> list);

    /**
     * 恢复v_topo和t_entity
     */
    void reStoreTopoRela();

    /**
     * 导入entityFolderRela
     * 
     * @param relas
     */
    void importEntityFolderRela(List<EntityFolderRela> relas);

    /**
     * 导入设备别名
     * 
     * @param entityAlias
     */
    void importEntityAlias(List<Entity> entityAlias);

}
