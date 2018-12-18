/***********************************************************************
 * $Id: ImportServiceImpl.java,v1.0 2015-7-9 下午1:47:27 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.exportAndImport.domain.EIConstant;
import com.topvision.ems.exportAndImport.service.ImportService;
import com.topvision.ems.exportAndImport.service.ImportTxService;
import com.topvision.framework.common.ExcelUtil;

/**
 * @author fanzidong
 * @created @2015-7-9-下午1:47:27
 * 
 */
@Service("importService")
public class ImportServiceImpl implements ImportService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ImportTxService importTxService;

    @Override
    public Boolean importEntireExcel(File localFile, final String jconnectionId) {
        // 将临时文件保存为待解析的excel文件
        File tmpFile = createTmpExcelFile(localFile);
        try {
            // 解析excel文件，获取各sheet，支持2003和2007
            // TODO 解析速度有点慢，后续进行优化
            final Map<String, String[][]> sheetMap = ExcelUtil.getExcelMapData(tmpFile);
            if (sheetMap == null) {
                return false;
            }
            // 导入各sheet数据,异步操作，不断向前端更新进度
            Thread importThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //导入基本数据
                        importTxService.txImportDatas(sheetMap, jconnectionId);
                        //恢复权限信息
                        importTxService.recoveryAuthority(sheetMap, jconnectionId);
                    } catch (Exception e) {
                        // 导入失败
                        logger.debug("Import excel error: ", e.getMessage());
                        e.printStackTrace();
                    }

                }
            });
            importThread.start();
            return true;
        } catch (Exception e1) {
            logger.error("ImportError::cannot convert excel data", e1);
            return false;
        }
    }

    /**
     * 将上传文件保存为excel，以待导入
     * 
     * @param localFile
     * @return
     */
    private File createTmpExcelFile(File localFile) {
        File destFile = null;
        InputStream fis = null;
        OutputStream fos = null;
        try {
            //确保文件夹存在
            File folder = new File(EIConstant.IMPORT_ROOT);
            if(!folder.exists()){
                folder.mkdirs();
            }
            destFile = new File(EIConstant.IMPORT_ROOT + "import.xlsx");
            destFile.createNewFile();
            fis = new BufferedInputStream(new FileInputStream(localFile));
            fos = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buffer = new byte[2048];
            int i;
            while ((i = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return destFile;
    }

}
