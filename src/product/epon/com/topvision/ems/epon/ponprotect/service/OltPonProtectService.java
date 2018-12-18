/***********************************************************************
 * $Id: OltPonProtectService.java,v1.0 2013-10-25 下午3:29:49 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.OltPonProtect;
import com.topvision.ems.epon.domain.PonProtectConfig;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午3:29:49
 *
 */
public interface OltPonProtectService extends Service {

    /**
     * 获得所有保护组列表
     * 
     * @param entityId
     * @return
     */
    List<OltPonProtect> getOltPonProtectsList(Long entityId);

    /**
     * 获得保护组对象
     * 
     * @param entityId
     * @param protectId
     * 
     * @return
     */
    OltPonProtect getOltPonProtectById(Long entityId, Integer protectId);

    /**
     * 新增保护组
     * 
     * @param oltPonProtect
     */
    void addPonProtect(OltPonProtect oltPonProtect);

    /**
     * 更新单个保护组对象
     * 
     * @param oltPonProtect
     * @return
     */
    void updateOltPonProtectById(OltPonProtect oltPonProtect);

    /**
     * 删除保护组对象
     * 
     * @param entityId
     * @param protectId
     * @return
     */
    void deleteOltPonProtectById(Long entityId, Integer protectId);

    /**
     * 添加保护组
     * 
     * @param config
     */
    void addPonProtect(OltPonProtectConfig config);

    /**
     * 往保护组中添加成员
     * 
     * @param config
     */
    void addPPGMembers(PonProtectConfig config);

    /**
     * 加载某设备的保护组
     * 
     * @param entityId
     * @return
     */
    List<OltPonProtectConfig> loadPPGList(Long entityId);

    /**
     * 加载某设备的保护组列表
     * 
     * @param entityId
     * @return
     */
    List<Integer> loadPPGArray(Long entityId);

    /**
     * 加载所有满足条件的PON口
     * 
     * @param map
     * @return
     */
    List<Long> loadAvialPorts(Map<String, String> map);

    /**
     * 删除组
     * 
     * @param config
     * @param entityId
     */
    void deletePPG(OltPonProtectConfig config, long entityId);

    /**
     * 组去使能 / 使能
     * 
     * @param config
     * @param entityId
     * @return
     */
    OltPonProtectConfig setPonProtectAdmin(OltPonProtectConfig config, Long entityId);

    /**
     * 保护组手动倒换
     * 
     * @param config
     * @param entityId
     * @return
     */
    OltPonProtectConfig manuSwitch(OltPonProtectConfig config, Long entityId);

}
