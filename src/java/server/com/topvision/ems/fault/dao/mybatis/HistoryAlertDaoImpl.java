package com.topvision.ems.fault.dao.mybatis;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.HistoryAlertDao;
import com.topvision.ems.fault.domain.AlertTypeEx;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;

@Repository("historyAlertDao")
public class HistoryAlertDaoImpl extends MyBatisDaoSupport<HistoryAlert> implements HistoryAlertDao {
    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#handleHistoryAlert(com.topvision
     * .framework.event .MyResultHandler, java.util.Map)
     */
    @Override
    public void handleHistoryAlert(MyResultHandler handler, Map<String, String> map) {
        selectWithRowHandler(getNameSpace() + "handleHistoryAlert", map, handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#queryHistoryAlert(com.topvision
     * .framework.domain .Page, java.util.Map)
     */
    @Override
    public PageData<HistoryAlert> queryHistoryAlert(Page p, Map<String, String> map) {
        return super.selectByMap(getNameSpace() + "queryHistoryAlertCount", getNameSpace() + "queryHistoryAlert", map,
                p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#selectByMap(java. util.Map,
     * com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<HistoryAlert> selectByMap(Map<String, String> map, Page page) {
        PageData<HistoryAlert> data = super.selectByMap(map, page);
        /*
         * try { List<HistoryAlert> alerts = data.getData(); Statement rs =
         * getDataSource().getConnection().createStatement(); for (HistoryAlert alert : alerts) {
         * ResultSet r = rs.executeQuery("SELECT userObject FROM HistoryAlert WHERE alertId = " +
         * alert.getAlertId()); if (r.next()) { Blob b = r.getBlob(1); if (b != null) {
         * ObjectInputStream in = new ObjectInputStream(b.getBinaryStream());
         * alert.setUserObject(in.readObject()); in.close(); } } } rs.close(); } catch (Exception e)
         * { getLogger().error(e.getMessage(), e); }
         */
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#statHistoryAlertByCategory()
     */
    @Override
    public List<AlertTypeEx> statHistoryAlertByCategory() {
        return getSqlSession().selectList(getNameSpace("statHistoryAlertByCategory"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#statHistoryAlertByLevel(java .util.Map)
     */
    @Override
    public List<LevelStat> statHistoryAlertByLevel(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("statHistoryAlertByLevel"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#getEntityAvailability(java .lang.Long,
     * java.lang.String, java.lang.Long)
     */
    @Override
    public List<HistoryAlert> getEntityAvailability(Long entityId, String alertType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", String.valueOf(entityId));
        map.put("typeId", String.valueOf(alertType));
        return getSqlSession().selectList(getNameSpace("getHistoryAlertList"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#getAvailability(java.lang .Long,
     * java.sql.Timestamp, java.sql.Timestamp)
     */
    @Override
    public Long getAvailability(Long alertId, Timestamp startTime, Timestamp endTime) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("alertId", String.valueOf(alertId));
        map.put("startTime", String.valueOf(startTime));
        map.put("endTime", String.valueOf(endTime));
        /*
         * return Long.parseLong(getSqlMapClientTemplate().queryForObject(getNameSpace() +
         * "getAvailability", map) .toString());
         */
        return Long.parseLong(getSqlSession().selectOne(getNameSpace("getAvailability"), map).toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.HistoryAlertDao#getHistoryAlertByAlertId(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public HistoryAlert getHistoryAlertByAlertId(Long alertId) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("alertId", alertId);
        return getSqlSession().selectOne(getNameSpace("getHistoryAlertByAlertId"), paramsMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.HistoryAlert";
    }

}
