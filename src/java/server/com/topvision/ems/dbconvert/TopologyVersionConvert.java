/***********************************************************************
 * $Id: PlatVersionConvert.java,v1.0 2012-8-6 上午10:38:04 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.dbconvert;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.platform.dbconvert.VersionConvert;
import com.topvision.platform.service.DataManageParser;
import com.topvision.platform.util.DataBaseConstants;

/**
 * @author RodJohn
 * @created @2012-8-6-上午10:38:04
 * 
 */
public class TopologyVersionConvert extends VersionConvert {
    @Autowired
    private DataManageParser dataManageParser;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.DataModule#processModule(java.util.List,
     * java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    @Override
    public List<String> processModule(List<String> sqlList, String nowVersion, String fileVersion) {
        if (true) {
            String[] tables = DataBaseConstants.TOPOLOGY_TABLE.split(" ");
            Object[] result = new Object[tables.length];
            for (int i = 0; i < tables.length; i++) {
                List<String> insertList = new ArrayList<String>();
                result[i] = insertList;
                for (String sql : sqlList) {
                    if (getTableNameFromInsertSql(sql).equals(tables[i])) {
                        insertList.add(sql);
                    }
                }

                /*
                 * if(sql.toLowerCase().startsWith("INSERT INTO `versioncontrol`")){ result[0] =
                 * sql; }else if(sql.startsWith("INSERT INTO `users`")){ result[1] = sql; }else
                 * if(sql.startsWith("INSERT INTO `datadictionary`")){ result[2] = sql; }else
                 * if(sql.startsWith("INSERT INTO `department`")){ result[3] = sql; }else
                 * if(sql.startsWith("INSERT INTO `favouritefolder`")){ result[4] = sql; }else
                 * if(sql.startsWith("INSERT INTO `navigationbutton`")){ result[5] = sql; }else
                 * if(sql.startsWith("INSERT INTO `functionitem`")){ result[6] = sql; }else
                 * if(sql.startsWith("INSERT INTO `menuitem`")){ result[7] = sql; }else
                 * if(sql.startsWith("INSERT INTO `toolbarbutton`")){ result[8] = sql; }else
                 * if(sql.startsWith("INSERT INTO `buttonmenu`")){ result[9] = sql; }else
                 * if(sql.startsWith("INSERT INTO `googlelocation`")){ result[10] = sql; }else
                 * if(sql.startsWith("INSERT INTO `place`")){ result[11] = sql; }else
                 * if(sql.startsWith("INSERT INTO `portletcategory`")){ result[12] = sql; }else
                 * if(sql.startsWith("INSERT INTO `portletitem`")){ result[13] = sql; }else
                 * if(sql.startsWith("INSERT INTO `portletview`")){ result[14] = sql; }else
                 * if(sql.startsWith("INSERT INTO `portletviewRela`")){ result[15] = sql; }else
                 * if(sql.startsWith("INSERT INTO `report`")){ result[16] = sql; }else
                 * if(sql.startsWith("INSERT INTO `reporttemplate`")){ result[17] = sql; }else
                 * if(sql.startsWith("INSERT INTO `reporttask`")){ result[18] = sql; }else
                 * if(sql.startsWith("INSERT INTO `repository`")){ result[19] = sql; }else
                 * if(sql.startsWith("INSERT INTO `role`")){ result[20] = sql; }else
                 * if(sql.startsWith("INSERT INTO `rolenavirela`")){ result[21] = sql; }else
                 * if(sql.startsWith("INSERT INTO `rolefunctionrela`")){ result[22] = sql; }else
                 * if(sql.startsWith("INSERT INTO `rolemenurela`")){ result[23] = sql; }else
                 * if(sql.startsWith("INSERT INTO `rolebuttonrela`")){ result[24] = sql; }else
                 * if(sql.startsWith("INSERT INTO `staff`")){ result[25] = sql; }else
                 * if(sql.startsWith("INSERT INTO `systemlog`")){ result[26] = sql; }else
                 * if(sql.startsWith("INSERT INTO `systempreferences`")){ result[27] = sql; }else
                 * if(sql.startsWith("INSERT INTO `userdepartmentrela`")){ result[28] = sql; }else
                 * if(sql.startsWith("INSERT INTO `usergroup`")){ result[29] = sql; }else
                 * if(sql.startsWith("INSERT INTO `usergrouprela`")){ result[30] = sql; }else
                 * if(sql.startsWith("INSERT INTO `userplacerela`")){ result[31] = sql; }else
                 * if(sql.startsWith("INSERT INTO `userportletrela`")){ result[32] = sql; }else
                 * if(sql.startsWith("INSERT INTO `userpreferences`")){ result[33] = sql; }else
                 * if(sql.startsWith("INSERT INTO `userrolerela`")){ result[34] = sql; }else
                 * if(sql.startsWith("INSERT INTO `imagedirectory`")){ result[35] = sql; }else
                 * if(sql.startsWith("INSERT INTO `imagefile`")){ result[36] = sql; }else
                 * if(sql.startsWith("INSERT INTO `entity`")){ result[37] = sql; }else
                 * if(sql.startsWith("INSERT INTO `entityaddress`")){ result[38] = sql; }else
                 * if(sql.startsWith("INSERT INTO `topofolder`")){ result[39] = sql; }else
                 * if(sql.startsWith("INSERT INTO `entityfolderrela`")){ result[40] = sql; }else
                 * if(sql.startsWith("INSERT INTO `entitysnap`")){ result[41] = sql; }else
                 * if(sql.startsWith("INSERT INTO `snmpparam`")){ result[42] = sql; }else
                 * if(sql.startsWith("INSERT INTO `entitytype`")){ result[43] = sql; }
                 */

            }
            return makeList(result);
        }
        return null;
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#initialize()
     */
    @PostConstruct
    @Override
    public void initialize() {
        setModule();
        // dataManageParser.registDataModule(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#destroy()
     */
    @PreDestroy
    @Override
    public void destroy() {
        dataManageParser.unRegistDataModule(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#convertFrom165to170(java.util.List)
     * 
     * @Override public void convertFrom165to170(List<String> sql) { }
     * 
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#convertFrom170to174(java.util.List)
     * 
     * @Override public void convertFrom170to174(List<String> sql) { for (int i = 0; i < sql.size();
     * i++) { if (sql.get(i).startsWith("INSERT INTO `entity`")) { sql.set(i,
     * sql.get(i).replaceAll("offManagementTime", "offManagementTime")); } } }
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setStartVersion()
     */
    @Override
    public void setStartVersion() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setEndVersion()
     */
    @Override
    public void setEndVersion() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setPriority()
     */
    @Override
    public void setPriority() {
        this.priority = 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setModule()
     */
    @Override
    public void setModule() {
        this.module = "TOPOLOGY";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#convertFrom174to175(java.util.List)
     */
    /*
     * @Override public void convertFrom174to175(List<String> sql) { for (int i = 0; i < sql.size();
     * i++) { if (sql.get(i).startsWith("INSERT INTO `entity`")) { sql.set(i,
     * sql.get(i).replaceAll("offManagement", "offManagementTimes")); } } }
     */

}
