/***********************************************************************
 * $Id: OnuUpdateServiceImpl.java,v1.0 2013-10-29 上午9:09:24 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.NoIndexException;
import com.topvision.ems.epon.onu.dao.OnuUpdateDao;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgRecord;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.ems.epon.onu.domain.OltOnuUpgradeEx;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.OnuUpdateService;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2013-10-29-上午9:09:24
 *
 */
@Service("onuUpdateService")
public class OnuUpdateServiceImpl extends BaseService implements OnuUpdateService {

    @Autowired
    private OnuUpdateDao onuUpdateDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private DiscoveryService<OltDiscoveryData> discoveryService;
    // 标识等待ONU升级状态的线程
    private final Object onuMutex = new Object();

    @Override
    public void upgradeOnu(OltOnuUpgrade oltOnuUpgrade) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltOnuUpgrade.getEntityId());
        OltOnuUpgrade newUpgrade = new OltOnuUpgrade();
        // 获取升级ID
        Integer index = getTransIdForDevice(oltOnuUpgrade.getEntityId());
        // 设置升级ID
        oltOnuUpgrade.setTopOnuUpgradeTransactionIndex(index);
        // ONU升级（在MIB中topOnuUpgradeTable表中创建一条记录）
        try {
            if (oltOnuUpgrade.getTopOnuUpgradeOnuType() == 255) {
                oltOnuUpgrade.setTopOnuUpgradeOnuType(0);
            }
            newUpgrade = getOnuFacade(snmpParam.getIpAddress()).addOnuUpgrade(snmpParam, oltOnuUpgrade);
        } catch (Exception e) {
            logger.error("", e);
        }

        // 定时获取升级状态
        int count = 0;
        String upgradeStatus = "0" + EponConstants.ONU_UPGRADE_STATUS_INPROGRESS;
        /* 只要存在02则表示有ONU还在上传中,如果没有02则表示没有ONU还在上传中 * */
        while (upgradeStatus.contains("0" + EponConstants.ONU_UPGRADE_STATUS_INPROGRESS)) {
            try {
                synchronized (onuMutex) {
                    onuMutex.wait(5000);
                }
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            // if (trap != null) {
            // upgradeStatus =
            // getOltControlFacade(snmpParam.getIpAddress()).getOnuUpgradeStatus(snmpParam,
            // newUpgrade.getTopOnuUpgradeTransactionIndex());
            // break;
            // }
            try {
                upgradeStatus = getOnuFacade(snmpParam.getIpAddress()).getOnuUpgradeStatus(snmpParam,
                        newUpgrade.getTopOnuUpgradeTransactionIndex());
            } catch (SnmpNoResponseException e) {
                // 存在循环过程中出现设备无法及时响应的问题 导致抛异常 这里做的是容错
                logger.debug("", e);
            } catch (Exception e) {
                logger.debug("", e);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("count=" + count);
                logger.debug("upgradeStatus=" + upgradeStatus);
            }
            count++;
            if (count > 720) {
                break;
            }
        }
        // 获取设备的升级结果
        try {
            upgradeStatus = getOnuFacade(snmpParam.getIpAddress()).getOnuUpgradeStatus(snmpParam,
                    newUpgrade.getTopOnuUpgradeTransactionIndex());
        } catch (SnmpNoResponseException e) {
            // 存在循环过程中出现设备无法及时响应的问题 导致抛异常 这里做的是容错
            logger.debug("upgrade fail:{}", e);
        } catch (Exception e) {
            logger.debug("upgrade fail:{}", e);
        }
        newUpgrade.setTopOnuUpgradeStatus(upgradeStatus);
        // 升级成功后，将该次升级记录插入数据库。
        onuUpdateDao.insertOnuUpgrade(newUpgrade);
        // 插入数据库后查询升级记录，判断是否升级成功
        List<OltOnuUpgrade> upList = onuUpdateDao.getOnuUpgradeHistory(oltOnuUpgrade.getEntityId());
        if (upList != null && upList.size() > 0) {
            // 获得最后一次的升级记录
            OltOnuUpgrade upgrade = upList.get(upList.size() - 1);
            // 判断是否升级成功，失败则抛出异常给action
            if (upgrade.getUpgradeOnuList().toString().startsWith("[<font color='red'>")) {
                throw new RuntimeException("upgradeFaild");
            }
        }
    }

    // 从设备上获得升级的记录列表，选择未使用的ID
    private Integer getTransIdForDevice(Long entityId) {
        Integer index = checkTransIdFromDevice(entityId);
        // 当网管实用的100个ID全部用完,重新将已经升级成功和失败的记录删除这样ID可以重复使用
        if (index == null) {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // 先去设备上获取所有的升级记录
            List<OltOnuUpgradeEx> oltOnuUpgradeList = getOnuFacade(snmpParam.getIpAddress()).getOnuUpgradeRecords(
                    snmpParam);
            for (OltOnuUpgradeEx onuUpgrade : oltOnuUpgradeList) {
                if (!onuUpgrade.getTopOnuUpgradeStatus().contains("02")
                        && onuUpgrade.getTopOnuUpgradeTransactionIndex() > 1000) {
                    // 删除不包含inprogress的记录（即升级成功或失败的记录）
                    getOnuFacade(snmpParam.getIpAddress()).deleteOnuUpgrade(snmpParam, onuUpgrade);
                }
            }
            index = checkTransIdFromDevice(entityId);
            if (index == null) {
                throw new NoIndexException("no index");
            }
        }
        return index;
    }

    private Integer checkTransIdFromDevice(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 先去设备上获取所有的升级记录
        List<OltOnuUpgradeEx> oltOnuUpgradeList = getOnuFacade(snmpParam.getIpAddress())
                .getOnuUpgradeRecords(snmpParam);
        List<Integer> indexs = new ArrayList<Integer>();
        Integer index = null;
        for (OltOnuUpgradeEx onuUpgrade : oltOnuUpgradeList) {
            indexs.add(onuUpgrade.getTopOnuUpgradeTransactionIndex());
        }
        // 网管升级的Id从1001~1100
        for (int i = 1001; i <= 1100; i++) {
            if (!indexs.contains(i)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public List<OltOnuUpgrade> getOnuUpgradeHistory(Long entityId) {
        return onuUpdateDao.getOnuUpgradeHistory(entityId);
    }

    @Override
    public List<OltOnuAutoUpgBand> getOnuAutoUpgBand(Long entityId) {
        return onuUpdateDao.getOnuAutoUpgBand(entityId);
    }

    @Override
    public List<OltOnuAutoUpgProfile> getOnuAutoUpgProfile(Long entityId) {
        return onuUpdateDao.getOnuAutoUpgProfile(entityId);
    }

    @Override
    public void addOnuAutoUpgProfile(OltOnuAutoUpgProfile profile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profile.getEntityId());
        getOnuFacade(snmpParam.getIpAddress()).addOnuAutoUpgProfile(snmpParam, profile);
        onuUpdateDao.addOnuAutoUpgProfile(profile);
    }

    @Override
    public void modifyOnuAutoUpgProfile(OltOnuAutoUpgProfile profile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profile.getEntityId());
        getOnuFacade(snmpParam.getIpAddress()).modifyOnuAutoUpgProfile(snmpParam, profile);
        onuUpdateDao.modifyOnuAutoUpgProfile(profile);
    }

    @Override
    public void delOnuAutoUpgProfile(Long entityId, Integer profileId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOnuFacade(snmpParam.getIpAddress()).delOnuAutoUpgProfile(snmpParam, profileId);
        onuUpdateDao.delOnuAutoUpgProfile(entityId, profileId);
    }

    @Override
    public void bandOnuAutoUpgProfile(OltOnuAutoUpgBand band) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(band.getEntityId());
        getOnuFacade(snmpParam.getIpAddress()).bandOnuAutoUpgProfile(snmpParam, band);
        onuUpdateDao.bandOnuAutoUpgProfile(band);
        // 绑定后，都去将对应的profile的绑定状态修改为绑定
        OltOnuAutoUpgProfile profile = new OltOnuAutoUpgProfile();
        profile.setEntityId(band.getEntityId());
        profile.setProfileId(band.getProfileId());
        profile.setProBandStat(EponConstants.EPON_ONU_AUTOUPG_BANDSTAT_BAND);
        onuUpdateDao.updateOnuAutoUpgBandStat(profile);
    }

    @Override
    public void unbandOnuAutoUpgProfile(OltOnuAutoUpgBand band) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(band.getEntityId());
        getOnuFacade(snmpParam.getIpAddress()).unbandOnuAutoUpgProfile(snmpParam, band);
        onuUpdateDao.unbandOnuAutoUpgProfile(band);
        // 查看是否还有其他端口绑定到其对应的profile上，若无，则设置该profile状态为NOBAND，否则设置为BAND
        List<OltOnuAutoUpgBand> oldBand = onuUpdateDao.getOnuAutoUpgBand(band.getEntityId());
        boolean flag = true;
        if (oldBand.size() > 0) {
            for (OltOnuAutoUpgBand ob : oldBand) {
                if (ob.getProfileId().equals(band.getProfileId())) {
                    flag = false;
                    break;
                }
            }
        }
        OltOnuAutoUpgProfile profile = new OltOnuAutoUpgProfile();
        profile.setEntityId(band.getEntityId());
        profile.setProfileId(band.getProfileId());
        if (flag) {
            profile.setProBandStat(EponConstants.EPON_ONU_AUTOUPG_BANDSTAT_NOBAND);
        } else {
            profile.setProBandStat(EponConstants.EPON_ONU_AUTOUPG_BANDSTAT_BAND);
        }
        onuUpdateDao.updateOnuAutoUpgBandStat(profile);
    }

    @Override
    public void restartOnuAutoUpg(OltOnuAutoUpgBand band) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(band.getEntityId());
        getOnuFacade(snmpParam.getIpAddress()).restartOnuAutoUpg(snmpParam, band);
    }

    @Override
    public void cancelOnuAutoUpg(OltOnuAutoUpgRecord record) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(record.getEntityId());
        getOnuFacade(snmpParam.getIpAddress()).cancelOnuAutoUpg(snmpParam, record);
    }

    @Override
    public void refreshOnuAutoUpg(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltOnuAutoUpgProfile> profile = getOnuFacade(snmpParam.getIpAddress()).getOnuAutoUpgProfile(snmpParam);
        onuUpdateDao.refreshOnuAutoUpgProfile(profile, entityId);
        List<OltOnuAutoUpgBand> band = getOnuFacade(snmpParam.getIpAddress()).getOnuAutoUpgBand(snmpParam);
        onuUpdateDao.refreshOnuAutoUpgBand(band, entityId);
        // modify by haojie 和lizongtian沟通，此处换成刷新整个olt，现在已经没有刷新全部onu的流程
        // onuService.refreshOnuInfo(entityId);
        // discoveryService.refresh(entityId);

    }

    @Override
    public List<OltOnuAutoUpgRecord> refreshOnuAutoUpging(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getOnuFacade(snmpParam.getIpAddress()).getOnuAutoUpgRecord(snmpParam);
    }

    @Override
    public List<Long> getOnuIndexListByHwVeList(Long entityId, String hwVersion) {
        return onuUpdateDao.getOnuIndexListByHwVeList(entityId, hwVersion);
    }

    /**
     * 获取OltControlFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltControlFacade
     */
    private OnuFacade getOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuFacade.class);
    }

}
