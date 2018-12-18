package com.topvision.ems.fault.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.dao.ActionDao;
import com.topvision.ems.fault.dao.ActionTypeDao;
import com.topvision.ems.fault.dao.LevelDao;
import com.topvision.ems.fault.domain.ActionType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.ActionCenter;
import com.topvision.ems.fault.service.FaultService;
import com.topvision.ems.fault.service.LevelManager;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("faultService")
public class FaultServiceImpl extends BaseService implements FaultService {
    @Autowired
    private ActionTypeDao actionTypeDao;
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private LevelDao levelDao;
    private List<Level> levels;
    private JSONArray jsonLevels;
    @Autowired
    private ActionCenter actionCenter;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#deleteActions(java.util. List)
     */
    @Override
    public void deleteActions(List<Long> actionIds) {
        actionDao.deleteActions(actionIds);
        actionCenter.reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getActionById(java.lang. Long)
     */
    @Override
    public Action getActionById(Long actionId) {
        return actionDao.selectByPrimaryKey(actionId);
    }

    /**
     * @return the actionDao
     */
    public ActionDao getActionDao() {
        return actionDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getActions()
     */
    @Override
    public List<Action> getActions() {
        return actionDao.selectByMap(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getActionTypeByName(java .lang.String)
     */
    @Override
    public ActionType getActionTypeByName(String name) {
        return actionTypeDao.getActionTypeByName(name);
    }

    /**
     * @return the actionTypeDao
     */
    public ActionTypeDao getActionTypeDao() {
        return actionTypeDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getActionTypes()
     */
    @Override
    public List<ActionType> getActionTypes() {
        return actionTypeDao.selectByMap(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getAllAlertLevel()
     */
    @Override
    public List<Level> getAllAlertLevel() {
        return levels;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getLevel(java.lang.Byte)
     */
    @Override
    public Level getLevel(Byte level) {
        return LevelManager.getInstance().getLevel(level);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#getLevelName(java.lang.Byte)
     */
    @Override
    public String getLevelName(Byte level) {
        return LevelManager.getInstance().getLevelName(level);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        try {
            levels = levelDao.selectByMap(null);
            Level level = null;
            for (int i = 0; i < levels.size(); i++) {
                level = levels.get(i);
                LevelManager.getInstance().putLevel(level.getLevelId(), level);
            }
        } catch (DataAccessException ex) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("initialize.", ex.getMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#insertAction(com.topvision
     * .platform.domain.Action )
     */
    @Override
    public void insertAction(Action action) {
        actionDao.insertEntity(action);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#loadJSONAlertLevel()
     */
    @Override
    public JSONArray loadJSONAlertLevel() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        // if (jsonLevels == null) {
        jsonLevels = new JSONArray();
        JSONObject curAlert = new JSONObject();
        curAlert.put("text", resourceManager.getString("WorkBench.AllLevel"));
        curAlert.put("expanded", true);
        curAlert.put("id", "allLevel");
        curAlert.put("iconCls", "currentAlertIcon");
        curAlert.put("level", 0);
        JSONArray arr = new JSONArray();
        JSONObject json = null;
        Level level = null;
        List<Level> levels = getAllAlertLevel();
        int size = levels.size();
        for (int i = 0; i < size; i++) {
            level = levels.get(i);
            json = new JSONObject();
            json.put("id", "cl" + level.getLevelId());
            json.put("text", resourceManager.getString(level.getName()));
            json.put("leaf", true);
            json.put("iconCls", "level" + level.getLevelId() + "Icon");
            json.put("level", level.getLevelId());
            arr.add(json);
        }
        curAlert.put("children", arr);
        jsonLevels.add(curAlert);
        // }
        return jsonLevels;
    }

    /**
     * @param actionDao
     *            the actionDao to set
     */
    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }

    /**
     * @param actionTypeDao
     *            the actionTypeDao to set
     */
    public void setActionTypeDao(ActionTypeDao actionTypeDao) {
        this.actionTypeDao = actionTypeDao;
    }

    public void setLevelDao(LevelDao levelDao) {
        this.levelDao = levelDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#updateAction(com.topvision
     * .platform.domain.Action )
     */
    @Override
    public void updateAction(Action action) {
        actionDao.updateEntity(action);
        actionCenter.reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#updateActionStatus(java. util.List,
     * java.lang.Boolean)
     */
    @Override
    public void updateActionStatus(List<Long> actionIds, Boolean enable) {
        actionDao.updateActionStatus(actionIds, enable);
        actionCenter.reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.FaultService#updateActionType(com.topvision
     * .ems.fault.domain .ActionType)
     */
    @Override
    public void updateActionType(ActionType type) {
        actionTypeDao.updateEntity(type);
    }

    @Override
    public boolean existActionName(int actionType, String name, Long actionId) {
        int count = actionDao.existActionName(actionType, name, actionId);
        return count > 0;
    }

    /**
     * @return the actionCenter
     */
    public ActionCenter getActionCenter() {
        return actionCenter;
    }

    /**
     * @param actionCenter
     *            the actionCenter to set
     */
    public void setActionCenter(ActionCenter actionCenter) {
        this.actionCenter = actionCenter;
    }
}
