/***********************************************************************
 * $Id: CmcOpticalReceiverDao.java,v1.0 2013-12-2 下午2:33:55 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.engine;

import java.util.List;

import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverChannelNum;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverSwitchCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverType;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * 对光机信息进行增加、更新、查询
 * 
 * @author dosion
 * @created @2013-12-2-下午2:33:55
 * 
 */
public interface CmcOpticalReceiverDao {
    /**
     * 插入光机RF配置信息
     * 
     * @param cmcId
     * @param cmcOpReceiverRfCfg
     */
    void insertOpReceiverRfCfg(Long cmcId, CmcOpReceiverRfCfg cmcOpReceiverRfCfg);

    /**
     * 更新光机RF配置
     * 
     * @param cmcId
     * @param cmcOpReceiverRfCfg
     */
    void updateOpReceiverRfCfg(Long cmcId, CmcOpReceiverRfCfg cmcOpReceiverRfCfg);

    /**
     * 获取光机RF配置
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverRfCfg selectOpReceiverRfCfg(Long cmcId);

    /**
     * 插入光机开关配置
     * 
     * @param cmcId
     * @param cmcOpReceiverSwitchCfg
     */
    void insertOpReceiverSwitchCfg(Long cmcId, CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg);

    /**
     * 更新光机开关配置
     * 
     * @param cmcId
     * @param cmcOpReceiverSwitchCfg
     */
    void updateOpReceiverSwitchCfg(Long cmcId, CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg);

    /**
     * 获取光机开关配置
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverSwitchCfg selectOpReceiverSwitchCfg(Long cmcId);

    /**
     * 插入光机接收光功率信息
     * 
     * @param cmcId
     * @param cmcOpReceiverInputInfo
     */
    void insertOpReceiverInputInfo(Long cmcId, CmcOpReceiverInputInfo cmcOpReceiverInputInfo);

    /**
     * 更新光机接收光功率信息
     * 
     * @param cmcId
     * @param cmcOpReceiverInputInfo
     */
    void updateOpReceiverInputInfo(Long cmcId, CmcOpReceiverInputInfo cmcOpReceiverInputInfo);

    /**
     * 获取光机接收光功率信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcOpReceiverInputInfo> selectOpReceiverInputInfoList(Long cmcId);

    /**
     * 插入光机交流电源信息
     * 
     * @param cmcId
     * @param cmcOpReceiverAcPower
     */
    void insertOpReceiverAcPower(Long cmcId, CmcOpReceiverAcPower cmcOpReceiverAcPower);

    /**
     * 更新光机交流电源信息
     * 
     * @param cmcId
     * @param cmcOpReceiverAcPower
     */
    void updateOpReceiverAcPower(Long cmcId, CmcOpReceiverAcPower cmcOpReceiverAcPower);

    /**
     * 获取光机交流电源信息
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverAcPower selectOpReceiverAcPower(Long cmcId);

    /**
     * 插入光机直流电源信息
     * 
     * @param cmcId
     * @param cmcOpReceiverDcPower
     */
    void insertOpReceiverDcPower(Long cmcId, CmcOpReceiverDcPower cmcOpReceiverDcPower);

    /**
     * 更新光机直流电源信息
     * 
     * @param cmcId
     * @param cmcOpReceiverDcPower
     */
    void updateOpReceiverDcPower(Long cmcId, CmcOpReceiverDcPower cmcOpReceiverDcPower);

    /**
     * 获取光机交流电源信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcOpReceiverDcPower> selectOpReceiverDcPowerList(Long cmcId);

    /**
     * 获取直流电源信息
     * 
     * @param cmcId
     * @param powerIndex
     * @return
     */
    CmcOpReceiverDcPower selectOpReceiverDcPower(Long cmcId, Integer powerIndex);

    /**
     * 插入光机历史性能数据
     * 
     * @param cmcId
     * @param cmcOpReceiverInputInfo
     */
    void insertOpReceiverInputInfoHis(Long cmcId, CmcOpReceiverInputInfo cmcOpReceiverInputInfo);

    /**
     * 获取光机接收功率历史信息
     * 
     * @param cmcId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectOpReceiverInputInfoHis(Long cmcId, Long index, String startTime, String endTime);

    /**
     * 插入光机射频输出电平
     * 
     * @param cmcId
     * @param cmcOpReceiverRfPort
     */
    void insertOpReceiverRfPort(Long cmcId, CmcOpReceiverRfPort cmcOpReceiverRfPort);

    /**
     * 更新光机射频输出电平
     * 
     * @param cmcId
     * @param cmcOpReceiverRfPort
     */
    void updateOpReceiverRfPort(Long cmcId, CmcOpReceiverRfPort cmcOpReceiverRfPort);

    /**
     * 获取光机射频输出电平
     * 
     * @param cmcId
     * @return
     */
    List<CmcOpReceiverRfPort> selectOpReceiverRfPortList(Long cmcId);

    /**
     * 插入光机载波频道数目
     * 
     * @param cmcId
     * @param cmcOpReceiverChannelNum
     */
    void insertOpReceiverChannelNum(Long cmcId, CmcOpReceiverChannelNum cmcOpReceiverChannelNum);

    /**
     * 更新光机载波频道数目
     * 
     * @param cmcId
     * @param cmcOpReceiverChannelNum
     */
    void updateOpReceiverChannelNum(Long cmcId, CmcOpReceiverChannelNum cmcOpReceiverChannelNum);

    /**
     * 获取光机载波频道数目
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverChannelNum selectOpReceiverChannelNum(Long cmcId);

    /**
     * 获取光机类型信息
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverType selectOpReceiverType(Long cmcId);

    /**
     * 添加设备光机类型信息
     * 
     * @param cmcId
     * @param opType
     */
    void insertOpReceiverType(Long cmcId, CmcOpReceiverType opType);

    /**
     * 更新设备光机类型信息
     * 
     * @param cmcId
     * @param opType
     */
    void updateOpReceiverType(Long cmcId, CmcOpReceiverType opType);
}
