/***********************************************************************
 * $Id: HsqlManager.java,v 1.1 Sep 26, 2009 11:47:20 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service.hsqldb;

import java.io.File;
import java.util.List;

import com.topvision.framework.domain.DataRecoveryResult;
import com.topvision.framework.domain.TableInfo;
import com.topvision.framework.version.service.DatabaseManager;

/**
 * @Create Date Sep 26, 2009 11:47:20 AM
 * 
 * @author kelers
 * 
 */
public class HsqlManager extends DatabaseManager {
    private static File list(File root) {
        if (root.isFile() || root.getName().startsWith("\\.")) {
            return null;
        }
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory() && file.getName().equals("hsqldb")) {
                boolean isFile = file.listFiles().length > 0;
                StringBuilder names = new StringBuilder();
                for (int i = 0; isFile && i < file.list().length; i++) {
                    names.append(file.list()[i]).append(",");
                }
                isFile = isFile && names.indexOf("ems.log,") != -1 && names.indexOf("ems.properties,") != -1;
                if (isFile) {
                    return file;
                }
            } else if (file.isDirectory()) {
                file = list(file);
                if (file != null) {
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.version.service.DatabaseManager#createDatabase()
     */
    @Override
    public void createDatabase() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.version.service.DatabaseManager#dropDatabase()
     */
    @Override
    public void dropDatabase() {
        File root = new File("../");
        File file = list(root);
        if (file != null) {
            for (File f : file.listFiles()) {
                if (f.getName().equals("ems.log") || f.getName().equals("ems.properties")
                        || f.getName().equals("ems.script")) {
                    f.deleteOnExit();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.version.service.DatabaseManager#exportDataBaseScript(java.lang.String)
     */
    @Override
    public String exportDataBaseScript(String tableNames) throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.version.service.DatabaseManager#processRecoveryResult(java.util.List, com.topvision.framework.domain.DataRecoveryResult)
     */
    @Override
    public List<String> processRecoveryResult(List<TableInfo> tableInfos, DataRecoveryResult dataRecoveryResult) {
        return null;
    }
}
