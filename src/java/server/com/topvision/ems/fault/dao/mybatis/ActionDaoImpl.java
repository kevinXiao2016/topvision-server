package com.topvision.ems.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.ActionDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.domain.Action;

@Repository("actionDao")
public class ActionDaoImpl extends MyBatisDaoSupport<Action> implements ActionDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.ActionDao#deleteActions(java.util.List)
     */
    @Override
    public void deleteActions(final List<Long> actionIds) {
        if (actionIds == null || actionIds.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (long id : actionIds) {
                sqlSession.delete(getNameSpace() + "deleteByPrimaryKey", id);
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
     * @see com.topvision.ems.fault.dao.ActionDao#getMapOfActions()
     */
    @Override
    public Map<Long, Action> getMapOfActions() {
        Map<Long, Action> actions = new HashMap<Long, Action>();
        List<Action> list = getSqlSession().selectList(getNameSpace() + "selectByMap", null);
        for (int i = 0; list != null && i < list.size(); i++) {
            Action a = list.get(i);
            actions.put(a.getActionId(), a);
        }
        return actions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.ActionDao#updateActionStatus(java.util.List,
     * java.lang.Boolean)
     */
    @Override
    public void updateActionStatus(final List<Long> actionIds, final Boolean enable) {
        if (actionIds == null || actionIds.isEmpty()) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (long id : actionIds) {
                Action action = new Action();
                action.setActionId(id);
                action.setEnabled(enable);
                sqlSession.update(getNameSpace() + "updateActionStatus", action);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int existActionName(int actionType, String name, Long actionId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actionType", String.valueOf(actionType));
        map.put("name", name);
        map.put("actionId", actionId);
        Integer count = getSqlSession().selectOne(getNameSpace() + "existActionName", map);
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.Action";
    }
}
