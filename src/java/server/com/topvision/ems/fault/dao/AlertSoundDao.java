/***********************************************************************
 * $Id: AlertSoundDao.java,v1.0 2015-1-21 上午10:24:18 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.dao;

import java.util.List;

import com.topvision.ems.fault.domain.AlertSound;
import com.topvision.ems.fault.domain.Level;

/**
 * @author vanzand
 * @created @2015-1-21-上午10:24:18
 * 
 */
public interface AlertSoundDao {
    /**
     * 获取所有声音
     * 
     * @return
     */
    List<AlertSound> fetchAllSounds();

    /**
     * 编辑告警声音描述
     * 
     * @param soundId
     * @param description
     */
    void editSoundDescription(Integer soundId, String description);

    /**
     * 加入告警声音
     * 
     * @param sound
     */
    void insertAlertSound(AlertSound sound);

    /**
     * 根据ID找到对应告警声音
     * 
     * @param soundId
     * @return
     */
    AlertSound getAlertSoundById(Integer soundId);

    /**
     * 删除告警声音记录
     * 
     * @param soundId
     */
    void deleteAlertSound(Integer soundId);

    /**
     * 获取所有告警等级对应的声音文件
     * 
     * @return
     */
    List<Level> fetchAllAlertLevels();

    /**
     * 获取指定告警等级对应的声音文件ID
     * 
     * @param levelId
     * @return
     */
    Integer getSoundIdByLevelId(Integer levelId);

    /**
     * 保存告警等级对应的告警声音
     * 
     * @param levelId
     * @param soundId
     */
    void saveLevelSound(Integer levelId, Integer soundId);
}
