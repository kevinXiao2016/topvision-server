/***********************************************************************
 * $Id: AlertSoundService.java,v1.0 2015-1-21 上午10:19:42 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.AlertSound;
import com.topvision.ems.fault.domain.Level;

/**
 * @author vanzand
 * @created @2015-1-21-上午10:19:42
 * 
 */
public interface AlertSoundService {
    /**
     * 获取所有声音
     * 
     * @return
     */
    List<AlertSound> fetchAllSounds();

    /**
     * 获取所有声音文件的名称列表
     * 
     * @return
     */
    List<String> fetchAllSoundsName();

    /**
     * 编辑告警声音
     * 
     * @param soundId
     * @param description
     */
    void editSoundDescription(Integer soundId, String description);

    /**
     * 上传声音文件
     * 
     * @param soundFile
     * @param soundFileFileName
     * @return
     */
    Boolean upLoadAlertSound(File soundFile, String name, String description);

    /**
     * 删除声音文件
     * 
     * @param soundId
     * @return
     */
    Map<String, Object> deleteAlertSound(Integer soundId);

    /**
     * 获取所有告警等级对应的声音文件
     * 
     * @return
     */
    List<Level> fetchAllAlertLevels();

    /**
     * 为某等级告警加载可选声音列表
     * 
     * @param levelId
     * @return
     */
    List<AlertSound> fetchSoundForAlertSelect(Integer levelId);

    /**
     * 保存告警等级的声音文件
     * 
     * @param levelId
     * @param soundId
     */
    void saveLevelSound(Integer levelId, Integer soundId);

    /**
     * 告警声音重名校验。 返回值为true表示重名，false表示未重名
     * 
     * @param name
     * @return
     */
    Boolean checkDuplicateSoundName(String name);
}
