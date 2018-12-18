/***********************************************************************
 * $Id: AlertSoundServiceImpl.java,v1.0 2015-1-21 上午10:20:42 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.dao.AlertSoundDao;
import com.topvision.ems.fault.domain.AlertSound;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertSoundService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;

/**
 * @author vanzand
 * @created @2015-1-21-上午10:20:42
 * 
 */
@Service("alertSoundService")
public class AlertSoundServiceImpl implements AlertSoundService {

    public static final String SOUNDFOLDER = SystemConstants.ROOT_REAL_PATH + "/sounds/";

    @Autowired
    private AlertSoundDao alertSoundDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertSoundService#fetchAllSounds()
     */
    @Override
    public List<AlertSound> fetchAllSounds() {
        return alertSoundDao.fetchAllSounds();
    }

    @Override
    public List<String> fetchAllSoundsName() {
        List<AlertSound> sounds = fetchAllSounds();
        List<String> names = new ArrayList<String>();
        for (AlertSound sound : sounds) {
            names.add(sound.getName());
        }
        return names;
    }

    @Override
    public void editSoundDescription(Integer soundId, String description) {
        alertSoundDao.editSoundDescription(soundId, description);
    }

    @Override
    public Boolean upLoadAlertSound(File soundFile, String name, String description) {
        // 将文件放入sounds文件夹
        Boolean ret = false;
        File destFile = new File(SOUNDFOLDER + name);
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(soundFile));
            fos = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buffer = new byte[2048];
            int i;
            while ((i = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
            // 文件传输完成，进行数据库存储
            AlertSound sound = new AlertSound(name, description, 1);
            alertSoundDao.insertAlertSound(sound);
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources");
    }
    
    @Override
    public Map<String, Object> deleteAlertSound(Integer soundId) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        // 找到对应文件的名称
        AlertSound sound = alertSoundDao.getAlertSoundById(soundId);
        if (sound == null) {
            retMap.put("success", false);
            retMap.put("msg", getResourceManager().getString("COMMON.noCorrespondingFileFound"));
            return retMap;
        }
        // 删除sounds文件夹下文件
        File destFile = new File(SOUNDFOLDER + sound.getName());
        if (destFile.exists()) {
            Boolean deleted = destFile.delete();
            if (deleted == false) {
                retMap.put("success", false);
                retMap.put("msg", getResourceManager().getString("COMMON.fileDeleteFailed"));
                return retMap;
            }
        }
        // 删除数据库记录
        alertSoundDao.deleteAlertSound(soundId);
        retMap.put("success", true);
        return retMap;
    }

    @Override
    public List<Level> fetchAllAlertLevels() {
        return alertSoundDao.fetchAllAlertLevels();
    }

    @Override
    public List<AlertSound> fetchSoundForAlertSelect(Integer levelId) {
        // 获取所有声音文件
        List<AlertSound> sounds = fetchAllSounds();
        // 获取所选等级告警所选中的声音ID
        Integer soundId = alertSoundDao.getSoundIdByLevelId(levelId);
        // 将对应声音文件标记为已选择
        for (AlertSound sound : sounds) {
            if (sound.getId().equals(soundId)) {
                sound.setSelected(true);
            } else {
                sound.setSelected(false);
            }
        }
        return sounds;
    }

    @Override
    public void saveLevelSound(Integer levelId, Integer soundId) {
        alertSoundDao.saveLevelSound(levelId, soundId);
    }

    @Override
    public Boolean checkDuplicateSoundName(String name) {
        // 获取所有声音文件
        List<AlertSound> sounds = fetchAllSounds();
        for (AlertSound sound : sounds) {
            if (sound.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
