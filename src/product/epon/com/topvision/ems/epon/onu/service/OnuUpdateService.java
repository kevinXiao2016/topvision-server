/***********************************************************************
 * $Id: OnuUpdateService.java,v1.0 2013-10-29 上午9:07:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgRecord;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-29-上午9:07:01
 *
 */
public interface OnuUpdateService extends Service {

    /**
     * 升级ONU
     * 
     * @param oltOnuUpgrade
     *            ONU升级参数
     */
    void upgradeOnu(OltOnuUpgrade oltOnuUpgrade);

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

    /**
     * 获得升级模板列表
     * 
     * @param entityId
     * @return
     */
    List<OltOnuAutoUpgProfile> getOnuAutoUpgProfile(Long entityId);

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
    void restartOnuAutoUpg(OltOnuAutoUpgBand band);

    /**
     * 取消
     * 
     * @param record
     * @return
     */
    void cancelOnuAutoUpg(OltOnuAutoUpgRecord record);

    /**
     * 刷新模板
     * 
     * @param entityId
     * @return
     */
    void refreshOnuAutoUpg(Long entityId);

    /**
     * 刷新升级记录
     * 
     * @param entityId
     * @return
     */
    List<OltOnuAutoUpgRecord> refreshOnuAutoUpging(Long entityId);

    List<Long> getOnuIndexListByHwVeList(Long entityId, String hwVersion);
}
