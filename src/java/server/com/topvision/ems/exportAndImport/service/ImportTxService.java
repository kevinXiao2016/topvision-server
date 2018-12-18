/***********************************************************************
 * $Id: ImportTxService.java,v1.0 2015-7-22 上午9:32:24 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.service;

import java.util.Map;

/**
 * @author fanzidong
 * @created @2015-7-22-上午9:32:24
 * 
 */
public interface ImportTxService {
    /**
     * 导入数据
     * 
     * @param sheetMap
     * @param dwrId
     * @param jconnectionId
     * @param uc
     */
    void txImportDatas(Map<String, String[][]> sheetMap, String jconnectionId);

    /**
     * 恢复权限数据
     * 
     * @param sheetMap
     * @param dwrId
     * @param jconnectionId
     * @param uc
     */
    void recoveryAuthority(Map<String, String[][]> sheetMap, String jconnectionId);
}
