package com.topvision.ems.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.AlertTypeDao;
import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.domain.Action;

@Repository("alertTypeDao")
public class AlertTypeDaoImpl extends MyBatisDaoSupport<AlertType> implements AlertTypeDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertTypeDao#getActionsOfAlertType(java.lang .Integer)
     */
    @Override
    public List<Long> getActionsOfAlertType(Integer id) {
        return getSqlSession().selectList(getNameSpace("getActionsOfAlertType"), id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertTypeDao#getUserActionOfAlertType(java.lang.Integer)
     */
    @Override
    public List<Action> getUserActionOfAlertType(Integer typeId) {
        return getSqlSession().selectList(getNameSpace("getUserActionOfAlertType"), typeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertTypeDao#updateAlertTypeActionRela(java .lang.Integer,
     * java.util.List)
     */
    @Override
    public void updateAlertTypeActionRela(Integer id, List<Long> actionIds) {
        getSqlSession().delete(getNameSpace() + "deleteActionRelaByAlertTypeId", id);
        if (actionIds != null && actionIds.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("alertTypeId", id);
            for (Long actionId : actionIds) {
                map.put("actionId", actionId);
                getSqlSession().update(getNameSpace() + "insertAlertTypeActionRela", map);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertTypeDao#updateLevelByAlertType(com.topvision
     * .ems.fault.domain .AlertType)
     */
    @Override
    public void updateLevelByAlertType(AlertType alertType) {
        getSqlSession().update(getNameSpace() + "updateLevelByAlertType", alertType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertTypeDao#getAllAlertType()
     */
    @Override
    public List<AlertType> getAllAlertType() {
        return getSqlSession().selectList(getNameSpace("getAllAlertType"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertTypeDao#getCmcAlertTypes()
     */
    @Override
    public List<AlertType> getCmcAlertTypes() {
        return getSqlSession().selectList(getNameSpace("getCmcAllAlertType"));
    }

    public void insertAlertTypeAboutUsers(Integer typeId, Long userId, List<Integer> userActionChoose,
            List<Long> actionIds) {
        Map<String, Object> deleteMap = new HashMap<String, Object>();
        deleteMap.put("alertTypeId", typeId);
        deleteMap.put("userId", userId);
        getSqlSession().delete(getNameSpace() + "deleteAlertAboutUsersByAlertTypeId", deleteMap);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alertTypeId", typeId);
        map.put("userId", userId);
        for (Integer i : userActionChoose) {
            map.put("userAlertActionChoose", i);
            getSqlSession().insert(getNameSpace() + "insertAlertAboutUsers", map);
        }
        if (actionIds != null) {
            getSqlSession().delete(getNameSpace() + "deleteDumpTrapByAlertTypeId", map);
            map.put("userAlertActionChoose", 3);
            getSqlSession().insert(getNameSpace() + "insertAlertAboutUsers", map);
        }
    }

    @Override
    public List<String> getAlertAboutUsersOfuserName(Integer id) {
        return getSqlSession().selectList(getNameSpace("getAlertAboutUsersOfuserName"), id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.AlertType";
    }

    @Override
    public List<Integer> getUserAlertActions(Integer alertTypeId, Long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alertTypeId", alertTypeId);
        map.put("userId", userId);
        return getSqlSession().selectList(getNameSpace("getUserAlertActions"), map);
    }

    @Override
    public List<AlertAboutUsers> getOneUserActionChoose(Long userId) {
        return getSqlSession().selectList(getNameSpace("getOneUserActionChoose"), userId);
    }

    @Override
    public void updateUserActionCs(Long oneUserId, String ch) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", oneUserId);
        map.put("choose", ch);
        getSqlSession().update(getNameSpace() + "updateUserActionCs", map);
    }

    @Override
    public int selectUserAlertListNum() {
        return getSqlSession().selectOne(getNameSpace("getUserAlertListCount"));
    }

    @Override
    public List<AlertAboutUsers> selectSendingInfo(Integer alertTypeId) {
        return getSqlSession().selectList(getNameSpace("getSendingInfomationOfUsers"), alertTypeId);
    }

    @Override
    public void updateLocaleName(Integer typeId, String localeName) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("typeId", typeId);
        queryMap.put("localeName", localeName);
        getSqlSession().update(getNameSpace("updateLocaleName"), queryMap);
    }

    @Override
    public void insertAlertTypeTrapAboutUsers(Integer typeId, Long userId, List<Long> actionIds) {
        Map<String, Object> deleteMap = new HashMap<String, Object>();
        deleteMap.put("alertTypeId", typeId);
        deleteMap.put("userId", userId);
        getSqlSession().delete(getNameSpace() + "deleteAlertAboutUsersByAlertTypeId", deleteMap);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alertTypeId", typeId);
        map.put("userId", userId);
        for (Long i : actionIds) {
            map.put("userAlertActionChoose", 3);
            getSqlSession().insert(getNameSpace() + "insertAlertAboutUsers", map);
        }
    }

}
