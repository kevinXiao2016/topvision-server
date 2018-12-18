/***********************************************************************
 * $Id: OltTrunkService.java,v1.0 2013-10-25 下午3:18:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.service;

import java.util.List;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午3:18:01
 *
 */
public interface OltTrunkService extends Service {
    /**
     * 取一台olt上所有Trunk组信息
     * 
     * @param entityId
     *            olt的id
     * @return List<OltSniTrunkConfig> trunk组配置信息列表
     */
    List<OltSniTrunkConfig> getTrunkConfigList(Long entityId);

    /**
     * 可用的sni口Index的列表
     * 
     * @param entityId
     *            olt的id
     * @param trunkIndex
     *            所属trunk组
     * @param sniIndex
     *            加入新trunk组的第一个sni口的index
     * @return sniIndex列表
     * @exception com.topvision.ems.epon.exception.SniTrunkConfigException
     *                没有可用端口时抛出
     */
    List<OltSniAttribute> availableSniList(Long entityId, Integer trunkIndex, Long sniIndex);

    /**
     * 添加Trunk组
     * 
     * @param entityId
     *            olt的id
     * @param oltSniTrunkConfig
     *            新建的没有trunkIndex的对象
     * @return OltSniTrunkConfig 填入了trunkIndex的对象
     */
    OltSniTrunkConfig addSniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig);

    /**
     * 添加Trunk组
     * 
     * @param entityId
     *            olt的id
     * @param trunkIndex
     *            需要删除的trunk组id
     * @return OltSniTrunkConfig 填入了trunkIndex的对象
     */
    void deleteSniTrunkConfig(Long entityId, Integer trunkIndex) throws Exception;

    /**
     * 修改Trunk组
     * 
     * @param entityId
     *            olt的id
     * @param oltSniTrunkConfig
     *            修改的trunk对象
     */
    void modifySniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig);

    /**
     * 刷新Trunk组
     * 
     * @param entityId
     *  
     */
    void refreshSniTrunkConfig(Long entityId);

    /**
     * 当发现完成的时候更新内存缓存的数据
     * 
     * @param entityId
     *            需要刷新的设备id
     */
    void updateMemoryDataAfterDiscovery(Long entityId);
    





}
