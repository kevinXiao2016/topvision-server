/***********************************************************************
 * $Id: DemoHsqlDaoImpl.java,v1.0 2015年3月12日 上午11:13:13 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.engine.dao.hsql;

import com.topvision.ems.cm.cmpoll.engine.dao.CmPollHsqlDao;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.engine.dao.HsqlAbstractDao;
import com.topvision.framework.annotation.Engine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jay
 * @created @2015年3月12日-上午11:13:13
 *
 */
@Engine("cmPollHsqlDao")
public class CmPollHsqlDaoImpl extends HsqlAbstractDao implements CmPollHsqlDao {
    private static String CREATERESULTTABLE = "CREATE TABLE  IF NOT EXISTS CMPOLLRESULT(" + "resultId BIGINT,"
            + "entityId BIGINT," + "cmcId BIGINT," + "cmId BIGINT," + "cmcIndex BIGINT," + "cmIndex BIGINT,"
            + "cmMac varchar(32)," + "cmIp varchar(32)," + "statusValue INT," + "checkStatus INT,"
            + "upChIndexs varchar(1024)," + "upChFreqs varchar(1024)," + "upChRxPowers varchar(1024),"
            + "upChTxPowers varchar(1024)," + "upChSnrs varchar(1024)," + "downChIndexs varchar(1024),"
            + "downChFreqs varchar(1024)," + "downChRxPowers varchar(1024)," + "downChSnrs varchar(1024),"
            + "collectTime BIGINT" + ");";

    private static String INSERTRESULTTABLE = "INSERT INTO CMPOLLRESULT(resultId,entityId,cmcId,cmId,cmcIndex,cmIndex,"
            + "cmMac,cmIp,statusValue,checkStatus,upChIndexs,upChFreqs,upChRxPowers,upChTxPowers,upChSnrs,"
            + "downChIndexs,downChFreqs,downChRxPowers,downChSnrs,collectTime)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    private static String SELECTRESULTTABLE = "select resultId,entityId,cmcId,cmId,cmcIndex,cmIndex,"
            + "cmMac,cmIp,statusValue,checkStatus,upChIndexs,upChFreqs,upChRxPowers,upChTxPowers,upChSnrs,"
            + "downChIndexs,downChFreqs,downChRxPowers,downChSnrs,collectTime from CMPOLLRESULT"
            + " where resultId >= ? and resultId <= ?;";

    @Override
    public void saveLocalRecord(long time, CmPollResult cmPollResult) {
        String tableName = makeTableName(time);
        String sql = INSERTRESULTTABLE.replace("CMPOLLRESULT", tableName);
        logger.trace("saveLocalRecord----" + sql);
        PreparedStatement pstat = null;
        try {
            pstat = prepareStatement(sql);
            pstat.setLong(1, cmPollResult.getResultId());
            pstat.setLong(2, cmPollResult.getEntityId());
            pstat.setLong(3, cmPollResult.getCmcId());
            pstat.setLong(4, cmPollResult.getCmId());
            pstat.setLong(5, cmPollResult.getCmcIndex());
            pstat.setLong(6, cmPollResult.getCmIndex());
            pstat.setString(7, cmPollResult.getCmMac());
            pstat.setString(8, cmPollResult.getCmIp());
            pstat.setInt(9, cmPollResult.getStatusValue());
            pstat.setInt(10, cmPollResult.getCheckStatus());
            pstat.setString(11, cmPollResult.getUpChIds());
            pstat.setString(12, cmPollResult.getUpChFreqs());
            pstat.setString(13, cmPollResult.getUpChRxPowers());
            pstat.setString(14, cmPollResult.getUpChTxPowers());
            pstat.setString(15, cmPollResult.getUpChSnrs());
            pstat.setString(16, cmPollResult.getDownChIds());
            pstat.setString(17, cmPollResult.getDownChFreqs());
            pstat.setString(18, cmPollResult.getDownChRxPowers());
            pstat.setString(19, cmPollResult.getDownChSnrs());
            pstat.setLong(20, cmPollResult.getCollectTime());
            pstat.execute();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        } finally {
            if (pstat != null) {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
        }
    }

    @Override
    public List<CmPollResult> readLocalRecords(Long time, int start, int n) {
        List<CmPollResult> re = new ArrayList<>();
        String tableName = makeTableName(time);
        String sql = SELECTRESULTTABLE.replace("CMPOLLRESULT", tableName);
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            pstat = prepareStatement(sql);
            pstat.setInt(1, start);
            pstat.setInt(2, start + n);
            rs = pstat.executeQuery();
            while (rs.next()) {
                CmPollResult cmPollResult = new CmPollResult();
                cmPollResult.setResultId(rs.getLong("resultId"));
                cmPollResult.setEntityId(rs.getLong("entityId"));
                cmPollResult.setCmcId(rs.getLong("cmcId"));
                cmPollResult.setCmId(rs.getLong("cmId"));
                cmPollResult.setCmcIndex(rs.getLong("cmcIndex"));
                cmPollResult.setCmIndex(rs.getLong("cmIndex"));
                cmPollResult.setCmIndex(rs.getLong("cmIndex"));
                cmPollResult.setCmMac(rs.getString("cmMac"));
                cmPollResult.setCmIp(rs.getString("cmIp"));
                cmPollResult.setStatusValue(rs.getInt("statusValue"));
                cmPollResult.setCheckStatus(rs.getInt("checkStatus"));
                cmPollResult.setUpChIds(rs.getString("upChIndexs"));
                cmPollResult.setUpChFreqs(rs.getString("upChFreqs"));
                cmPollResult.setUpChRxPowers(rs.getString("upChRxPowers"));
                cmPollResult.setUpChTxPowers(rs.getString("upChTxPowers"));
                cmPollResult.setUpChSnrs(rs.getString("upChSnrs"));
                cmPollResult.setDownChIds(rs.getString("downChIndexs"));
                cmPollResult.setDownChRxPowers(rs.getString("downChRxPowers"));
                cmPollResult.setDownChFreqs(rs.getString("downChFreqs"));
                cmPollResult.setDownChSnrs(rs.getString("downChSnrs"));
                cmPollResult.setCollectTime(rs.getLong("collectTime"));
                re.add(cmPollResult);
            }
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        } finally {
            if (rs != null) {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
            if (pstat != null) {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
        }
        return re;
    }

    @Override
    public void createRoundTable(Long time) {
        String tableName = makeTableName(time);
        String sql = CREATERESULTTABLE.replace("CMPOLLRESULT", tableName);
        logger.info("createRoundTable --- SQL----" + sql);
        try {
            Statement st = createStatement();
            st.execute(sql);
            st.close();
        } catch (SQLException e) {
            logger.error("", e);
        }

        String sql2 = "INSERT INTO CMPOLLTABLES VALUES(?,?)";
        PreparedStatement pstat = null;
        try {
            pstat = prepareStatement(sql2);
            pstat.setString(1, tableName);
            pstat.setLong(2, System.currentTimeMillis());
            pstat.execute();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        } finally {
            if (pstat != null) {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
        }
        // Add by Victor@20160908增加删除24小时之前的记录
        long t = System.currentTimeMillis() - 24 * 60 * 60000;
        String queryTable = "select TABLE_NAME from CMPOLLTABLES where CREATE_TIME < ?";
        try {
            Statement st = createStatement();
            pstat = prepareStatement(queryTable);
            pstat.setLong(1, t);
            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                st.addBatch("drop table " + rs.getString(1));
            }
            rs.close();
            pstat.close();
            st.addBatch("delete from CMPOLLTABLES where CREATE_TIME < " + t);
            st.executeBatch();
            st.close();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        } finally {
            if (pstat != null) {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
        }
    }

    private String makeTableName(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return "CmPollResult" + sdf.format(new Date(time));
    }

    @Override
    protected void initDatabase() {
        logger.info("Init CmPoll database.");
        try {
            String version = null;
            Statement st = createStatement();
            try {
                ResultSet rs = st.executeQuery("SELECT version FROM VERSIONS WHERE name = 'cmpoll'");
                if (rs.next()) {
                    version = rs.getString(1);
                }
                rs.close();
            } catch (java.sql.SQLSyntaxErrorException e) {
                logger.debug("not initialized,initialization version management table", e);
            }
            if (version == null) {
                version = "1.0.0";
                st.execute("INSERT INTO VERSIONS VALUES('cmpoll','" + version + "')");
                st.execute(
                        "CREATE TABLE IF NOT EXISTS CMPOLLTABLES(TABLE_NAME VARCHAR(32) NOT NULL PRIMARY KEY,CREATE_TIME BIGINT)");
            }
            st.close();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        }
    }

}
