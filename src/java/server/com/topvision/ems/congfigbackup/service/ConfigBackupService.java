package com.topvision.ems.congfigbackup.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;

public interface ConfigBackupService {

    void downloadConfigFile(Long entityId, Long entityType, String ip) throws Exception;

    void saveConfigFile(Long entityId, Long entityType, String ip) throws Exception;

    List<Entity> getEntitList();

    /**
     * 将配置文件应用到设备
     * @param filePath
     * @throws Exception 
     */
    void applyConfigToDevice(String filePath, Long entityType, String ip) throws Exception;

    /**
     * 记录操作记录
     * @param record
     */
    void recordOperation(ConfigAndBackupRecord record);

    /**
     * 查询所有的配置记录
     * @param map 
     * @return
     */
    List<ConfigAndBackupRecord> selectOperationRecords(Map<String, Object> map);

}
