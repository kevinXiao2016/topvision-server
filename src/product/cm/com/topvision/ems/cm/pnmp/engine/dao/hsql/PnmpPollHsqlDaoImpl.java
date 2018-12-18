/***********************************************************************
 * $Id: DemoHsqlDaoImpl.java,v1.0 2015年3月12日 上午11:13:13 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.dao.hsql;

import com.topvision.ems.cm.pnmp.engine.dao.PnmpPollHsqlDao;
import com.topvision.ems.cm.pnmp.facade.domain.*;
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
@Engine("pnmpPollHsqlDao")
public class PnmpPollHsqlDaoImpl extends HsqlAbstractDao implements PnmpPollHsqlDao {
    private static String CREATERESULTTABLE = "CREATE TABLE  IF NOT EXISTS PNMPPOLLRESULT(" + "resultId BIGINT,"
            + "entityId BIGINT," + "cmcId BIGINT," + "cmcIndex BIGINT," + "cmIndex BIGINT,"
            + "cmMac varchar(32)," + "cmIp varchar(32)," + "statusValue INT," + "checkStatus INT,"
            + "equalizationData varchar(300)," + "upChannelId INT," + "upChannelFreq INT," + "upChannelWidth INT,"
            + "upSnr INT," + "upTxPower INT," + "downSnr INT," + "downRxPower INT,"
            + "collectTime BIGINT" + ");";

    private static String INSERTRESULTTABLE = "INSERT INTO PNMPPOLLRESULT(resultId,entityId,cmcId,cmcIndex,cmIndex,"
            + "cmMac,cmIp,statusValue,checkStatus,equalizationData,upChannelId,upChannelFreq,upChannelWidth," +
            "upSnr,upTxPower,downSnr,downRxPower,collectTime)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    private static String SELECTRESULTTABLE = "select resultId,entityId,cmcId,cmcIndex,cmIndex,"
            + "cmMac,cmIp,statusValue,checkStatus,equalizationData,upChannelId,upChannelFreq,upChannelWidth," +
            "upSnr,upTxPower,downSnr,downRxPower,collectTime from PNMPPOLLRESULT"
            + " where resultId >= ? and resultId <= ?;";

    @Override
    public void saveLocalRecord(long time, PnmpPollResult pnmpPollResult, PnmpPollTask pnmpPollTask) {
        String tableName = makeTableName(time,pnmpPollTask);
        String sql = INSERTRESULTTABLE.replace("PNMPPOLLRESULT", tableName);
        logger.trace("saveLocalRecord----" + sql);
        PreparedStatement pstat = null;
        try {
            pstat = prepareStatement(sql);
            pstat.setLong(1, pnmpPollResult.getResultId());
            pstat.setLong(2, pnmpPollResult.getEntityId());
            pstat.setLong(3, pnmpPollResult.getCmcId());
            pstat.setLong(4, pnmpPollResult.getCmcIndex());
            pstat.setLong(5, pnmpPollResult.getCmIndex());
            pstat.setString(6, pnmpPollResult.getCmMac());
            pstat.setString(7, pnmpPollResult.getCmIp());
            pstat.setInt(8, pnmpPollResult.getStatusValue());
            pstat.setInt(9, pnmpPollResult.getCheckStatus());
            pstat.setString(10, pnmpPollResult.getEqualizationData());
            pstat.setInt(11, pnmpPollResult.getUpChannelId() == null ? -1 : pnmpPollResult.getUpChannelId());
            pstat.setLong(12, pnmpPollResult.getUpChannelFreq() == null ? -1L : pnmpPollResult.getUpChannelFreq());
            pstat.setLong(13, pnmpPollResult.getUpChannelWidth() == null ? -1L : pnmpPollResult.getUpChannelWidth());
            pstat.setInt(14, pnmpPollResult.getUpSnr() == null ? Integer.MIN_VALUE : pnmpPollResult.getUpSnr());
            pstat.setInt(15, pnmpPollResult.getUpTxPower() == null ? Integer.MIN_VALUE : pnmpPollResult.getUpTxPower());
            pstat.setInt(16, pnmpPollResult.getDownSnr() == null ? Integer.MIN_VALUE : pnmpPollResult.getDownSnr());
            pstat.setInt(17, pnmpPollResult.getDownRxPower() == null ? Integer.MIN_VALUE : pnmpPollResult.getDownRxPower());
            pstat.setLong(18, time);
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
    public List<PnmpPollResult> readLocalRecords(Long time, int start, int n, PnmpPollTask pnmpPollTask) {
        List<PnmpPollResult> re = new ArrayList<>();
        String tableName = makeTableName(time, pnmpPollTask);
        String sql = SELECTRESULTTABLE.replace("PNMPPOLLRESULT", tableName);
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            pstat = prepareStatement(sql);
            pstat.setInt(1, start);
            pstat.setInt(2, start + n);
            rs = pstat.executeQuery();
            while (rs.next()) {
                PnmpPollResult pnmpPollResult = new PnmpPollResult();
                pnmpPollResult.setResultId(rs.getLong("resultId"));
                pnmpPollResult.setEntityId(rs.getLong("entityId"));
                pnmpPollResult.setCmcId(rs.getLong("cmcId"));
                pnmpPollResult.setCmcIndex(rs.getLong("cmcIndex"));
                pnmpPollResult.setCmIndex(rs.getLong("cmIndex"));
                pnmpPollResult.setCmIndex(rs.getLong("cmIndex"));
                pnmpPollResult.setCmMac(rs.getString("cmMac"));
                pnmpPollResult.setCmIp(rs.getString("cmIp"));
                pnmpPollResult.setStatusValue(rs.getInt("statusValue"));
                pnmpPollResult.setCheckStatus(rs.getInt("checkStatus"));
                pnmpPollResult.setEqualizationData(rs.getString("equalizationData"));
                pnmpPollResult.setUpChannelId(rs.getInt("upChannelId"));
                pnmpPollResult.setUpChannelFreq(rs.getLong("upChannelFreq"));
                pnmpPollResult.setUpChannelWidth(rs.getLong("upChannelWidth"));
                pnmpPollResult.setUpSnr(rs.getInt("upSnr"));
                pnmpPollResult.setUpTxPower(rs.getInt("upTxPower"));
                pnmpPollResult.setDownSnr(rs.getInt("downSnr"));
                pnmpPollResult.setDownRxPower(rs.getInt("downRxPower"));
                pnmpPollResult.setCollectTime(rs.getLong("collectTime"));
                re.add(pnmpPollResult);
            }
        } catch (Exception e) {
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
    public void createRoundTable(Long time, PnmpPollTask pnmpPollTask) {
        String tableName = makeTableName(time, pnmpPollTask);
        String sql = CREATERESULTTABLE.replace("PNMPPOLLRESULT", tableName);
        logger.info("createRoundTable --- SQL----" + sql);
        try {
            Statement st = createStatement();
            st.execute(sql);
            st.close();
        } catch (SQLException e) {
            logger.error("", e);
        }

        String sql2 = "INSERT INTO PNMPPOLLTABLES VALUES(?,?)";
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
        String queryTable = "select TABLE_NAME from PNMPPOLLTABLES where CREATE_TIME < ?";
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
            st.addBatch("delete from PNMPPOLLTABLES where CREATE_TIME < " + t);
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

    private String makeTableName(Long time, PnmpPollTask pnmpPollTask) {
        if (pnmpPollTask instanceof PnmpPollLowSpeedTask
                || pnmpPollTask instanceof PnmpPollLowSpeedEndTask) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return "PnmpPollLowSpeedResult" + sdf.format(new Date(time));
        } else if (pnmpPollTask instanceof PnmpPollMiddleSpeedTask
                || pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return "PnmpPollMiddleSpeedResult" + sdf.format(new Date(time));
        } else if (pnmpPollTask instanceof PnmpPollHighSpeedTask
                || pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return "PnmpPollHighSpeedResult" + sdf.format(new Date(time));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "PnmpPollResult" + sdf.format(new Date(time));
    }

    @Override
    protected void initDatabase() {
        logger.info("Init PnmpPoll database.");
        try {
            String version = null;
            Statement st = createStatement();
            try {
                ResultSet rs = st.executeQuery("SELECT version FROM VERSIONS WHERE name = 'pnmppoll'");
                if (rs.next()) {
                    version = rs.getString(1);
                }
                rs.close();
            } catch (java.sql.SQLSyntaxErrorException e) {
                logger.debug("not initialized,initialization version management table", e);
            }
            if (version == null) {
                version = "1.0.0";
                st.execute("INSERT INTO VERSIONS VALUES('pnmppoll','" + version + "')");
                st.execute(
                        "CREATE TABLE IF NOT EXISTS PNMPPOLLTABLES(TABLE_NAME VARCHAR(64) NOT NULL PRIMARY KEY,CREATE_TIME BIGINT)");
            }
            st.close();
        } catch (SQLException e) {
            logger.error(getClass().getSimpleName(), e);
        }
    }

}
