/***********************************************************************
 * $Id: AlertSoundAction.java,v1.0 2015-1-21 上午10:09:20 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.domain.AlertSound;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertSoundService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

/**
 * @author vanzand
 * @created @2015-1-21-上午10:09:20
 * 
 */
@Controller("alertSoundAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlertSoundAction extends BaseAction {

    private static final long serialVersionUID = 1252931084307787190L;
    private static final String WAV_TYPE = "audio/wav";
    private static final String MP3_TYPE = "audio/mp3";

    @Autowired
    private AlertSoundService alertSoundService;
    // 声音数组
    private JSONArray sounds;
    // 告警等级数组
    private JSONArray alertLevels;

    private Integer levelId;
    private Integer soundId;
    private String soundName;
    private String description;

    // 上传的文件
    private File soundFile;
    // 上传文件的名称(struts2解析)
    private String soundFileFileName;
    // 上传文件的类型(struts2解析)
    private String soundFileContentType;

    /**
     * 打开告警声音页面
     * 
     * @return
     */
    public String showAlertSound() {
        // 获取所有告警声音
        List<AlertSound> soundList = alertSoundService.fetchAllSounds();
        ResourceManager resourceManager = getResourceManager();
        /*
         * for(AlertSound sound : soundList){
         * sound.setDescription(resourceManager.getNotNullString(sound.getDescription())); }
         */
        sounds = JSONArray.fromObject(soundList);
        // 获取所有告警等级的声音情况
        List<Level> alertLevelList = alertSoundService.fetchAllAlertLevels();
        // 告警名称需要做国际化处理
        for (Level level : alertLevelList) {
            level.setSoundDesc(resourceManager.getNotNullString(level.getSoundDesc()));
            level.setName(resourceManager.getNotNullString(level.getName()));
        }
        alertLevels = JSONArray.fromObject(alertLevelList);
        return SUCCESS;
    }

    /**
     * 获取所有告警声音
     * 
     * @return
     * @throws IOException
     */
    public String fetchAllSounds() throws IOException {
        List<AlertSound> soundList = alertSoundService.fetchAllSounds();
        // 告警名称需要做国际化处理
        /*
         * ResourceManager resourceManager = getResourceManager(); for(AlertSound sound :
         * soundList){
         * sound.setDescription(resourceManager.getNotNullString(sound.getDescription())); }
         */
        JSONArray.fromObject(soundList).write(response.getWriter());
        return NONE;
    }

    /**
     * 获取所有告警等级的声音情况
     * 
     * @return
     * @throws IOException
     */
    public String fetchAllAlertLevels() throws IOException {
        List<Level> alertLevelList = alertSoundService.fetchAllAlertLevels();
        // 告警名称需要做国际化处理
        ResourceManager resourceManager = getResourceManager();
        for (Level level : alertLevelList) {
            level.setSoundDesc(resourceManager.getNotNullString(level.getSoundDesc()));
            level.setName(resourceManager.getNotNullString(level.getName()));
        }
        JSONArray.fromObject(alertLevelList).write(response.getWriter());
        return NONE;
    }

    /**
     * 为选中告警等级加载可选声音文件
     * 
     * @return
     * @throws IOException
     */
    public String fetchSoundForAlertSelect() throws IOException {
        // 获取所有声音
        List<AlertSound> soundList = alertSoundService.fetchSoundForAlertSelect(levelId);
        JSONArray.fromObject(soundList).write(response.getWriter());
        return NONE;
    }

    public String editLevelSound() {
        // 保存告警等级与声音的对应关系
        alertSoundService.saveLevelSound(levelId, soundId);
        return NONE;
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
    }

    /**
     * 上传声音文件
     * 
     * @return
     * @throws IOException
     */
    public String upLoadAlertSound() throws IOException {
        Boolean result = false;
        JSONObject ret = new JSONObject();
        if(soundFile == null || !soundFile.exists()){
            ret.put("error", getResourceManager().getString("COMMON.fileNotFound"));
        }else{
            result = alertSoundService.upLoadAlertSound(soundFile, soundFileFileName, description);
        }
        ret.put("success", result);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(ret);
        /*ret.write(response.getWriter());*/
        return NONE;
    }

    /**
     * 编辑声音描述
     * 
     * @return
     */
    public String editSoundDescription() {
        alertSoundService.editSoundDescription(soundId, description);
        return NONE;
    }

    /**
     * 删除声音文件
     * 
     * @return
     * @throws IOException
     */
    public String deleteAlertSound() throws IOException {
        Map<String, Object> ret = alertSoundService.deleteAlertSound(soundId);
        JSONObject.fromObject(ret).write(response.getWriter());
        return NONE;
    }

    /**
     * 检查是否存在同名告警声音
     * 
     * @return
     * @throws IOException
     */
    public String checkDuplicateSoundName() throws IOException {
        Boolean isDuplicate = alertSoundService.checkDuplicateSoundName(soundName);
        JSONObject ret = new JSONObject();
        ret.put("isDuplicate", isDuplicate);
        ret.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取所有告警声音的名称
     * 
     * @return
     * @throws IOException
     */
    public String fetchAllSoundsName() throws IOException {
        List<String> names = alertSoundService.fetchAllSoundsName();
        JSONArray.fromObject(names).write(response.getWriter());
        return NONE;
    }

    /**
     * 获取各级别告警对应的声音名称
     * 
     * @return
     * @throws IOException
     */
    public String fetchAllAlertSoundsName() throws IOException {
        List<Level> levels = alertSoundService.fetchAllAlertLevels();
        JSONObject ret = new JSONObject();
        for (Level level : levels) {
            ret.put(level.getLevelId(), level.getSoundName());
        }
        ret.write(response.getWriter());
        return NONE;
    }

    public JSONArray getSounds() {
        return sounds;
    }

    public void setSounds(JSONArray sounds) {
        this.sounds = sounds;
    }

    public Integer getSoundId() {
        return soundId;
    }

    public void setSoundId(Integer soundId) {
        this.soundId = soundId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(File soundFile) {
        this.soundFile = soundFile;
    }

    public String getSoundFileFileName() {
        return soundFileFileName;
    }

    public void setSoundFileFileName(String soundFileFileName) {
        this.soundFileFileName = soundFileFileName;
    }

    public String getSoundFileContentType() {
        return soundFileContentType;
    }

    public void setSoundFileContentType(String soundFileContentType) {
        this.soundFileContentType = soundFileContentType;
    }

    public JSONArray getAlertLevels() {
        return alertLevels;
    }

    public void setAlertLevels(JSONArray alertLevels) {
        this.alertLevels = alertLevels;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

}
