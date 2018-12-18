package com.topvision.ems.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.dao.AlertDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertStat;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.platform.util.CurrentRequest;

@Repository("alertDao")
public class AlertDaoImpl extends MyBatisDaoSupport<Alert> implements AlertDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#insertEntity(java.lang.Object)
     */
    @Override
    public void insertEntity(Alert alert) {
        getSqlSession().insert(getNameSpace() + "insertEntity", alert);
        alert.setAlertId(alert.getAlertId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#confirmAlert(com.topvision.ems.fault .domain.Alert)
     */
    @Override
    public void confirmAlert(Alert alert) {
        getSqlSession().update(getNameSpace() + "confirmAlert", alert);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#confirmAlert(java.util.List)
     */
    @Override
    public void confirmAlert(final List<Alert> alerts) {
        if (alerts == null || alerts.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (Alert alert : alerts) {
                sqlSession.update(getNameSpace() + "confirmAlert", alert);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#clearAlert(com.topvision.ems.fault.domain.Alert)
     */
    @Override
    public void clearAlert(Alert alert) {
        getSqlSession().update(getNameSpace() + "clearAlert", alert);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#clearAlert(java.util.List)
     */
    @Override
    public void clearAlert(List<Alert> alerts) {
        if (alerts == null || alerts.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (Alert alert : alerts) {
                sqlSession.update(getNameSpace() + "clearAlert", alert);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getMaxAlertInChildFolder(java.lang .String)
     */
    @Override
    public List<TopoFolderEx> getMaxAlertInChildFolder(String path) {
        return getSqlSession().selectList(getNameSpace("getMaxAlertInChildFolder"), path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getMaxAlerts()
     */
    @Override
    public Map<Long, Alert> getMaxAlerts() {
        List<Alert> list = getSqlSession().selectList(getNameSpace("getMaxLevelAlerts"));
        if (list == null || list.isEmpty()) {
            return null;
        }
        Map<Long, Alert> alerts = new HashMap<Long, Alert>(list.size());
        for (Alert alert : list) {
            alerts.put(alert.getAlertId(), alert);
        }
        return alerts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getMaxLevelAlertByEntityId(java. lang.Long)
     */
    @Override
    public Alert getMaxLevelAlertByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getMaxLevelByEntityId"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getRecentAlert(java.lang.Integer)
     */
    @Override
    public List<Alert> getRecentAlert(Integer limit) {
        return getSqlSession().selectList(getNameSpace("handleRecentAlert"), String.valueOf(limit));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getRecentAlert(java.util.Map)
     */
    @Override
    public List<Alert> getRecentAlert(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("getRecentAlert"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#handleRecentAlert(java.lang.Integer,
     * com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void handleRecentAlert(Integer limit, MyResultHandler handler) {
        super.selectWithRowHandler(getNameSpace("handleRecentAlert"), String.valueOf(limit), handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#loadFloatingAlert(java.lang.Long)
     */
    @Override
    public List<Alert> loadFloatingAlert(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadFloatingAlert"), entityId);
    }

    @Override
    public List<Alert> loadEntityAndSubAlert(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadEntityAndSubAlert"), entityId);
    }

    @Override
    public List<Long> getConcernAlertTypes() {
        return getSqlSession().selectList(getNameSpace("getConcernAlertTypes"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#restoreEntityCurrentAlertState(com
     * .topvision.framework .event.MyResultHandler)
     */
    @Override
    public void restoreEntityCurrentAlertState(MyResultHandler handler) {
        selectWithRowHandler(getNameSpace() + "restoreEntityCurrentAlertState", handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#statAlertByEntity(java.util.Map)
     */
    @Override
    public List<AlertStat> statAlertByEntity(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("statAlertByEntity"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#topAlertByEntity(java.util.Map)
     */
    @Override
    public List<AlertStat> topAlertByEntity(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("topAlertByEntity"), map);
    }

    @Override
    public List<AlertStat> statAlertListByEntity(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getStatAlertListByEntity"), map);
    }

    @Override
    public Integer statAlertCountByEntity(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getStatAlertCountByEntity"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#statAlertByLevel(java.util.Map)
     */
    @Override
    public List<LevelStat> statAlertByLevel(Map<String, String> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("statAlertByLevel"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#statAlertByServer(java.util.Map)
     */
    @Override
    public List<AlertStat> statAlertByServer(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("statAlertByServer"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getRecentAlertByEntityIdAndLimit (int,
     * java.lang.Long)
     */
    @Override
    public List<Alert> getRecentAlertByEntityIdAndLimit(int limit, Long entityId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", "" + limit);
        map.put("entityId", "" + entityId);
        return getSqlSession().selectList(getNameSpace("getRecentAlertByEntityIdAndLimit"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getEntityAvailability(java.lang. Long,
     * java.lang.String, java.lang.Long)
     */
    @Override
    public Alert getEntityAvailability(Long entityId, String alertType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", String.valueOf(entityId));
        map.put("typeId", String.valueOf(alertType));
        List<Alert> alerts = getSqlSession().selectList(getNameSpace("getCurrentAlert"), map);
        if (alerts != null && alerts.size() > 0) {
            return alerts.get(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getEntityCreateTime(java.lang.Long)
     */
    @Override
    public Entity getEntityCreateTime(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getEntityCreateTime"), entityId);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getAlertByAlertId(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public Alert getAlertByAlertId(Long alertId) {
        return getSqlSession().selectOne(getNameSpace("getAlertByAlertId"), alertId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#getAllAlertType()
     */
    @Override
    public List<AlertType> getAllAlertType(String type) {
        return getSqlSession().selectList(getNameSpace("getAllAlertType"), type);
    }

    @Override
    public String getEntityNameByIp(String host) {
        return getSqlSession().selectOne(getNameSpace("getEntityNameByIp"), host);
    }

    @Override
    public Long queryEntityIdByMac(String cmcMac) {
        // add by fanzidong,需要在查询前格式化MAC地址
        cmcMac = MacUtils.convertToMaohaoFormat(cmcMac);
        return getSqlSession().selectOne(getNameSpace("getEntityIdByMac"), cmcMac);
    }

    @Override
    public String queryMacByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getMacByEntityId"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.Alert";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertDao#selectCurrentAlert(java.util.Map)
     */
    @Override
    public List<Alert> selectCurrentAlert(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("selectCurrentAlert"), map);
    }

    @Override
    public List<Alert> getAlert(String mac, Long cmcPowerOffAlerttype) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("mac", mac);
        queryMap.put("typeId", cmcPowerOffAlerttype);
        return getSqlSession().selectList(getNameSpace("getCmcPowerOffAlert"), queryMap);
    }

    @Override
    public List<Alert> getSpecialCmcAlertList(Long cmcId, List<Integer> alertTypeList) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("entityId", cmcId);
        queryMap.put("typeList", alertTypeList);
        return getSqlSession().selectList(getNameSpace("getSpecialCmcAlertList"), queryMap);
    }

    @Override
    public List<AlertAboutUsers> getAlertInfoOfUsersAndChoose(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getAlertInfoOfUsersAndChoose"),map);
    }
    
    public String selectAlertNameById(Long userId){
    	return getSqlSession().selectOne(getNameSpace("selectUserNameByUserId"),userId);
    }
}
