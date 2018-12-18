/***********************************************************************
 * $Id: OpticalReceiverDao.java,v1.0 2016年9月13日 下午3:16:18 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.dao;

import java.util.List;

import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorABSwitch;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorChannelNum;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDevParams;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorLinePower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxInput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxOutput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:16:18
 *
 */
public interface OpticalReceiverRefreshDao {

    /**
     * 更新光机类型字符串
     * 
     * @param topCcmtsSysDorType
     */
    void updateSysDorType(TopCcmtsSysDorType topCcmtsSysDorType);

    /**
     * 插入或者更新State of A-B switch
     * 
     * @param topCcmtsDorABSwitch
     */
    void insertOrUpdateABSwitch(TopCcmtsDorABSwitch topCcmtsDorABSwitch);

    /**
     * 插入或更新正向频道数量
     * 
     * @param topCcmtsDorChannelNum
     */
    void insertOrUpdateChannelNum(TopCcmtsDorChannelNum topCcmtsDorChannelNum);

    /**
     * 批量插入或更新DC Power
     * 
     * @param powerList
     */
    void batchInsertOrUpdateDCPower(List<TopCcmtsDorDCPower> powerList);

    /**
     * 插入或更新光机参数信息
     * 
     * @param topCcmtsDorDevParams
     */
    void insertOrUpdateDevParams(TopCcmtsDorDevParams topCcmtsDorDevParams);

    /**
     * 批量插入或更新正向射频支路1-4衰减
     * 
     * @param topCcmtsDorFwdAtts
     */
    void batchInsertOrUpdateFwdAtts(List<TopCcmtsDorFwdAtt> topCcmtsDorFwdAtts);

    /**
     * 批量插入或更新正向射频支路1-4均衡
     * 
     * @param topCcmtsDorFwdEqs
     */
    void batchInsertOrUpdateFwdEqs(List<TopCcmtsDorFwdEq> topCcmtsDorFwdEqs);

    /**
     * 插入或更新同轴供电输入电压 AC 60V电压
     * 
     * @param topCcmtsDorLinePower
     */
    void insertOrUpdateLinePower(TopCcmtsDorLinePower topCcmtsDorLinePower);

    /**
     * 批量插入或刷新反向射频支路1-4衰减表
     * 
     * @param topCcmtsDorRevAtts
     */
    void batchInsertOrUpdateRevAtts(List<TopCcmtsDorRevAtt> topCcmtsDorRevAtts);

    /**
     * 批量插入或刷新RF1-4端口输出电平
     * 
     * @param topCcmtsDorRFPorts
     */
    void batchInsertOrUpdateRFPorts(List<TopCcmtsDorRFPort> topCcmtsDorRFPorts);

    /**
     * 批量插入或刷新反向光收1-4输入光功率
     * 
     * @param topCcmtsDorRRXOptPows
     */
    void batchInsertOrUpdatesRRXOptPows(List<TopCcmtsDorRRXOptPow> topCcmtsDorRRXOptPows);

    /**
     * 插入或刷新正向光收A路光功率
     * @param topCcmtsOpRxInput
     */
    void insertOrUpdateOpRxInput(TopCcmtsOpRxInput topCcmtsOpRxInput);

    /**
     * 插入或刷新正向光发参数
     * @param topCcmtsOpRxOutput
     */
    void insertOrUpdateOpRxOutput(TopCcmtsOpRxOutput topCcmtsOpRxOutput);

}
