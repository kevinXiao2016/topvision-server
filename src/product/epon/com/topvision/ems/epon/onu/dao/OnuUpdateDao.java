/***********************************************************************
 * $Id: OnuUpdateDao.java,v1.0 2013年10月31日 下午7:52:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年10月31日-下午7:52:38
 * 
 * 
 *
 */
public interface OnuUpdateDao extends BaseEntityDao<Object> {
    /**
     * 获得升级模板列表
     * 
     * @param entityId
     * @return
     */
    List<OltOnuAutoUpgProfile> getOnuAutoUpgProfile(Long entityId);

    // profile
    /**
     * 新增模板
     * 
     * @param profile
     * @return
     */
    void addOnuAutoUpgProfile(OltOnuAutoUpgProfile profile);

    /**
     * 修改模板
     * 
     * @param profile
     * @return
     */
    void modifyOnuAutoUpgProfile(OltOnuAutoUpgProfile profile);

    /**
     * 删除模板
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    void delOnuAutoUpgProfile(Long entityId, Integer profileId);

    // manu
    /**
     * 绑定模板
     * 
     * @param band
     * @return
     */
    void bandOnuAutoUpgProfile(OltOnuAutoUpgBand band);

    /**
     * 解除绑定
     * 
     * @param band
     * @return
     */
    void unbandOnuAutoUpgProfile(OltOnuAutoUpgBand band);

    /**
     * 重启
     * 
     * @param band
     * @return
     */
    void updateOnuAutoUpgBandStat(OltOnuAutoUpgProfile profile);

    // refresh
    /**
     * 刷新模板
     * 
     * @param entityId
     * @return
     */
    void refreshOnuAutoUpgProfile(List<OltOnuAutoUpgProfile> profileList, Long entityId);

    /**
     * 刷新升级记录
     * 
     * @param entityId
     * @return
     */
    void refreshOnuAutoUpgBand(List<OltOnuAutoUpgBand> bandList, Long entityId);

    /**
     * 插入ONU升级记录
     * 
     * @param oltOnuUpgrade
     */
    void insertOnuUpgrade(OltOnuUpgrade oltOnuUpgrade);

    /**
     * 获取ONU升级历史记录
     * 
     * @param entityId
     *            设备ID
     * @return List<OltOnuUpgrade>
     */
    List<OltOnuUpgrade> getOnuUpgradeHistory(Long entityId);

    /**
     * 获得绑定状态列表
     * 
     * @param entityId
     * @return
     */
    List<OltOnuAutoUpgBand> getOnuAutoUpgBand(Long entityId);

    List<Long> getOnuIndexListByHwVeList(Long entityId, String hwVersion);

}
