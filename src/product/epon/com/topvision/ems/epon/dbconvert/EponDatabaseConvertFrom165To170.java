/***********************************************************************
 * $Id: EponDatabaseConvertFrom165To170.java,v1.0 2012-8-14 下午12:04:06 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dbconvert;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.platform.dbconvert.VersionConvert;
import com.topvision.platform.service.DataManageParser;

/**
 * @author RodJohn
 * @created @2012-8-14-下午12:04:06
 * 
 */
@Service("eponDatabaseConvertFrom165To170")
public class EponDatabaseConvertFrom165To170 extends VersionConvert {
    @Autowired
    private DataManageParser dataManageParser;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        setModule();
        // dataManageParser.registDataModule(this);
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
     * @see com.topvision.platform.util.VersionConvert#setStartVersion()
     */
    @Override
    public void setStartVersion() {
        this.startVersion = "1.6.5";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setEndVersion()
     */
    @Override
    public void setEndVersion() {
        this.endVersion = "1.7.0";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setPriority()
     */
    @Override
    public void setPriority() {
        this.priority = 10001;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setModule()
     */
    @Override
    public void setModule() {
        this.module = "EPON";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#processModule(java.util.List,
     * java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    @Override
    public List<String> processModule(List<String> sqlList, String nowVersion, String fileVersion) {
        if (true) {
            return modifyColumnName(sqlList, "oltAttribute", "oltDeviceUpTime", "deviceUpTime");
        }
        return sqlList;
    }

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

}
