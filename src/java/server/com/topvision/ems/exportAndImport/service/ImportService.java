/***********************************************************************
 * $Id: ImportService.java,v1.0 2015-7-9 上午10:59:19 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.service;

import java.io.File;

/**
 * @author fanzidong
 * @created @2015-7-9-上午10:59:19
 * 
 */
public interface ImportService {

    /**
     * 导入完整的EXCEL文件
     * 
     * @param localFile
     * @param jconnectionId
     */
    Boolean importEntireExcel(File localFile, String jconnectionId);

}
