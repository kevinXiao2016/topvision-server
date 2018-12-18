/***********************************************************************
 * $Id: OltPonProtectDao.java,v1.0 2013-10-25 下午3:25:52 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.OltPonProtect;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-下午3:25:52
 *
 */
public interface OltPonProtectDao extends BaseEntityDao<Object> {
    /**
     * 获得所有保护组
     * 
     * @param entityId
     * @return
     */
    List<OltPonProtect> getOltPonProtectList(Long entityId);

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
     * @param ppgId
     * @param entityId
     */
    void deletePPG(Integer ppgId, long entityId);

    /**
     * 删除组成员
     * 
     * @param ppgId
     */
    void deletePPGMembers(Integer ppgId);

    /**
     * PON保护配置数据批量添加*
     * 
     * @param ponProtectConfigs
     * @param entityId
     */
    void batchInsertOltPonProtectConfigs(List<OltPonProtectConfig> ponProtectConfigs, long entityId);

    /**
     * 添加一个保护组，必须同时带端口
     * 
     * @param ponProtectConfig
     */
    void insertOltPonProtectConfig(OltPonProtectConfig ponProtectConfig);

    /**
     * 保护组使能/去使能
     * 
     * @param config
     */
    void setPonProtectAdmin(OltPonProtectConfig config);

    /**
     * 倒换后将倒换时间与次数计入DB
     * 
     * @param config
     */
    void updateSwitchInfo(OltPonProtectConfig config);

    /**
     * 获得单个保护组对象
     * 
     * @param entityId
     * @param protectId
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

}
