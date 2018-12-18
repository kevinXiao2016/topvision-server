/***********************************************************************
 * $Id: ExportService.java,v1.0 2015-6-29 上午9:00:38 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import com.topvision.ems.facade.domain.Entity;

import jxl.write.WriteException;

/**
 * @author fanzidong
 * @created @2015-6-29-上午9:00:38
 * 
 */
public interface ExportService {

    /**
     * 根据传入的数据、列信息和文件名，生成excel文件
     * 
     * @param data
     * @param columns
     * @param fileName
     * @return
     * @throws WriteException
     * @throws Exception
     */
    String generateExcelFile(com.alibaba.fastjson.JSONArray data, com.alibaba.fastjson.JSONArray columns,
            String fileName) throws WriteException, Exception;

    /**
     * 完整的导出，包括角色、用户、地域、权限及设备等信息
     * 
     * @return
     */
    String entireExport() throws Exception;

    /**
     * 下载文件
     * 
     * @param fileName
     */
    void downloadFile(String fileName);

    /**
     * 获取待导出设备列表
     * 
     * @param map
     * @return
     */
    List<Entity> getExportEntity(Map<String, Object> map);

    /**
     * 获得符合条件的导出设备数目
     * 
     * @param map
     * @return
     */
    Integer getExportEntityNum(Map<String, Object> map);

    /**
     * 将对象导出成excel文件
     * @param workbook
     * @param sheetName
     * @param datas
     * @throws WriteException
     */
    String exportSheetContent(String fileName, String sheetName, List<?> datas) throws WriteException, FileNotFoundException, IOException;

    /**
     * 将对象导出成excel文件
     * @param workbook
     * @param sheetName
     * @param datas
     * @throws WriteException
     */
    String exportSheetContent(String fileName, String sheetName, List<?> datas,boolean isNewFile) throws WriteException, FileNotFoundException,IOException;
}
