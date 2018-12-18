/***********************************************************************
 * $Id: AlertSoundDaoImpl.java,v1.0 2015-1-21 上午10:25:45 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.AlertSoundDao;
import com.topvision.ems.fault.domain.AlertSound;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author vanzand
 * @created @2015-1-21-上午10:25:45
 * 
 */
@Repository("alertSoundDao")
public class AlertSoundDaoImpl extends MyBatisDaoSupport<AlertSound> implements AlertSoundDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertSoundDao#fetchAllSounds()
     */
    @Override
    public List<AlertSound> fetchAllSounds() {
        return getSqlSession().selectList(getNameSpace("fetchAllSounds"));
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.AlertSound";
    }

    @Override
    public void editSoundDescription(Integer soundId, String description) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", soundId);
        map.put("description", description);
        getSqlSession().update(getNameSpace("editSoundDescription"), map);
    }

    @Override
    public void insertAlertSound(AlertSound sound) {
        getSqlSession().insert(getNameSpace("insertAlertSound"), sound);
    }

    @Override
    public AlertSound getAlertSoundById(Integer soundId) {
        return getSqlSession().selectOne(getNameSpace("getAlertSoundById"), soundId);
    }

    @Override
    public void deleteAlertSound(Integer soundId) {
        getSqlSession().delete(getNameSpace("deleteAlertSoundById"), soundId);
        // 重置已使用该声音的告警等级
        getSqlSession().update(getNameSpace("resetLevelSoundById"), soundId);
    }

    @Override
    public List<Level> fetchAllAlertLevels() {
        return getSqlSession().selectList(getNameSpace("fetchAllAlertLevels"));
    }

    @Override
    public Integer getSoundIdByLevelId(Integer levelId) {
        return getSqlSession().selectOne(getNameSpace("getSoundIdByLevelId"), levelId);
    }

    @Override
    public void saveLevelSound(Integer levelId, Integer soundId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("levelId", levelId);
        map.put("soundId", soundId);
        getSqlSession().update(getNameSpace("saveLevelSound"), map);
    }

}
