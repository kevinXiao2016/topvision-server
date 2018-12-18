/***********************************************************************
 * $Id: OltTrunkDao.java,v1.0 2013-10-25 下午3:15:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.dao;

import java.util.List;

import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-下午3:15:09
 *
 */
public interface OltTrunkDao extends BaseEntityDao<Object> {

    /**
     * 批量插入Sni口Trunk属性
     * 
     * @param sniTrunkConfigs
     */
    void batchInsertOltSniTrunkConfig(final List<OltSniTrunkConfig> sniTrunkConfigs, Long entityId);

    /**
     * 获取Trunk组列表
     * 
     * @param entityId
     * @return
     */
    List<OltSniTrunkConfig> getTrunkConfigList(Long entityId);

    /**
     * 添加一个trunk组
     * 
     * @param newOltSniTrunkConfig
     *            新的trunk组
     */
    void insertSniTrunkConfig(OltSniTrunkConfig newOltSniTrunkConfig);

    /**
     * 删除一个trunk组
     * 
     * @param oltSniTrunkConfig
     *            trunk组对象
     */
    void deleteSniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig);

    /**
     * 修改trunk组
     * 
     * @param oltSniTrunkConfig
     *            修改的trunk组的对象
     */
    void updateSniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig);

}
