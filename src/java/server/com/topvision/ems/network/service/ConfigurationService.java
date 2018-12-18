package com.topvision.ems.network.service;

import java.util.List;

import com.topvision.ems.facade.domain.Config;
import com.topvision.framework.service.Service;

public interface ConfigurationService extends Service {

    /**
     * 是否有配置没有上传
     * 
     * @param entityId
     * @return
     */
    boolean isUpLoadAccess(Long entityId);

    /**
     * 是否有配置没有同步
     * 
     * @param entityId
     * @return
     */
    boolean isDownLoadAccess(Long entityId);

    /**
     * 获取需要上传的设备配置
     * 
     * @param entityId
     * @return
     */
    List<Config> getUpLoadConfig(Long entityId);

    /**
     * 获取需要同步的设备配置
     * 
     * @param entityId
     * @return
     */
    List<Config> getDownLoadConfig(Long entityId);

    /**
     * 上传配置
     * 
     * @param entityId
     * @param configs
     * @return
     */
    void upLoadConfig(Long entityId, List<Config> configs);

    /**
     * 同步配置
     * 
     * @param entityId
     * @param configs
     */
    void downLoadConfig(Long entityId, List<Config> configs);
}