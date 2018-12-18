/***********************************************************************
 * $Id: CmUpgradeService.java,v1.0 2016年12月5日 下午2:09:00 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig;
import com.topvision.ems.cm.upgrade.domain.CmcAutoUpgradeProcess;
import com.topvision.ems.cm.upgrade.domain.CmcUpgradeInfo;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileInfoMgtTable;

/**
 * @author Rod John
 * @created @2016年12月5日-下午2:09:00
 *
 */
public interface CmUpgradeService {
    public static final Integer FILE_MAX = 20 * 1024 * 1024;
    public static final Integer CM_UPGRADING = 3;

    /**
     * Load Ccmts Entity
     * 
     * @return
     */
    List<CmcUpgradeInfo> loadCmcUpgradeInfo(Map<String, Object> param);

    /**
     * Load Ccmts File
     * 
     * @param entityId
     * @return
     */
    List<TopCcmtsSfFileInfoMgtTable> loadCcmtsFile(Long entityId);

    /**
     * Load Ccmts File Size
     * 
     * @param entityId
     * @return
     */
    Integer loadCcmtsFileSize(Long entityId);

    /**
     * Delete Ccmts File
     * 
     * @param entityId
     * @param fileName
     */
    void deleteCcmtsFile(Long entityId, String fileName);

    /**
     * UpLoad Ccmts File
     * 
     * @return
     */
    void uploadCcmtsFile(Long entityId, File file);

    /**
     * Load Upload Process
     *     
     * @param entityId
     */
    String loadUploadProcess(Long entityId);

    /**
    *  Upgrade Single CM
    * 
    * @param entityId
    * @param cmMac
    * @param statusIndex
    * @param fileName
    * 
    * @return
    */
    String upgradeSingleCm(Long entityId, String cmMac, Long statusIndex, String fileName);

    /**
     * 获得CM的版本统计信息
     * 
     * 如果entityId为空，代表获取全网的CM
     * 
     * 如果entityId不为空，代表获取某个CMC-I下的CM
     * 
     * @return
     */
    Map<String, Map<String, Integer>> getCmVersionInfo(Integer cmVersionInfoType, Long entityId);

    /**
     * 获得CM的ModulNum信息
     * 
     * @return
     */
    List<String> loadCmModuleNum();

    /**
     * 获得单个CM的软件信息
     * 
     * @return
     */
    TopCcmtsCmSwVersionTable getSingleCmSwVersion(Long cmId);

    /**
     * 加载自动升级配置
     * 
     * @return
     */
    List<CmUpgradeConfig> loadCmUpgradeConfig();

    /**
     * 添加自动升级配置
     * 
     * @param config
     */
    void addAutoUpgradeConfig(CmUpgradeConfig config);

    /**
     * 更新自动升级配置
     * 
     * @param config
     */
    void modifyAutoUpgradeConfig(CmUpgradeConfig config);

    /**
     * 删除自动升级配置
     * 
     * @param configId
     */
    void deleteAutoUpgradeConfig(Integer configId);

    /**
     * 下发自动升级配置
     * 
     * @param entityIds
     */
    void applyAutoUpgradeConfig(List<Long> entityIds);

    /**
     * 当前自动升级配置进度
     * 
     * @return
     */
    List<CmcAutoUpgradeProcess> loadAutoUpgradeProcess();

}
