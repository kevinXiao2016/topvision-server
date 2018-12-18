/***********************************************************************
 * $Id: ImportDaoImpl.java,v1.0 2015-7-15 下午4:08:06 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.dao.mybatis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.exportAndImport.dao.ImportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntityFolderRela;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.SystemConstants;

/**
 * @author fanzidong
 * @created @2015-7-15-下午4:08:06
 * 
 */
@Repository("importDao")
public class ImportDaoImpl extends MyBatisDaoSupport<Object> implements ImportDao {

    @Override
    public void importSheetData(String sheetName, List<Object> list) {
        SqlSession session = getBatchSession();
        try {
            for (Object obj : list) {
                session.insert(getNameSpace("import" + sheetName), obj);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void importEntityAlias(List<Entity> entityAlias) {

        SqlSession session = getBatchSession();
        try {
            for (Entity e : entityAlias) {
                session.insert(getNameSpace("importEntityAlias"), e);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void reStoreTopoRela() {
        Connection connection = null;
        try {
            // connection = getSqlSession().getConnection();
            connection = getSqlSession().getConfiguration().getEnvironment().getDataSource().getConnection();
            connection.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setLogWriter(null);
            runner.setDelimiter("$$");
            StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
            fileName.append("META-INF/script/AutoCreateTopoFolderTableAndView.sql");
            runner.runScript(new InputStreamReader(new FileInputStream(fileName.toString()), "utf-8"));
            connection.commit();
        } catch (IOException exception) {
            logger.error("reStoreTopoRela error:", exception);
        } catch (SQLException exception) {
            logger.error("reStoreTopoRela error:", exception);
        } finally {
        }
    }

    @Override
    public void importEntityFolderRela(List<EntityFolderRela> relas) {
        SqlSession session = getBatchSession();
        try {
            for (EntityFolderRela rela : relas) {
                session.insert(getNameSpace("importEntityFolderRela"), rela);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.exportAndImport.dao.mybatis.ImportDaoImpl";
    }

}
