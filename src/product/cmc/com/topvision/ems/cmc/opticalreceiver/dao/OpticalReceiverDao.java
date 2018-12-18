/***********************************************************************
 * $Id: OpticalReceiverDao.java,v1.0 2016年9月20日 上午9:24:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.dao;

import java.util.List;

import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;

/**
 * @author haojie
 * @created @2016年9月20日-上午9:24:43
 *
 */
public interface OpticalReceiverDao {

    /**
     * 获取光机信息，不完整数据
     * 
     * @param cmcId
     * @return
     */
    OpticalReceiverData getOpticalReceiverData(Long cmcId);

    /**
     * 获取直流输出电压（DC12V/DC24V）
     * 
     * @param cmcId
     * @return
     */
    List<TopCcmtsDorDCPower> getDCPowers(Long cmcId);

    /**
     * 获取正向射频支路1-4衰减
     * 
     * @param cmcId
     * @return
     */
    List<TopCcmtsDorFwdAtt> getFwdAtts(Long cmcId);

    /**
     * 获取正向射频支路1-4均衡
     * 
     * @param cmcId
     * @return
     */
    List<TopCcmtsDorFwdEq> getFwdEqs(Long cmcId);

    /**
     * 获取反向射频支路1-4衰减
     * 
     * @param cmcId
     * @return
     */
    List<TopCcmtsDorRevAtt> getRevAtts(Long cmcId);

    /**
     * 获取RF1-4端口输出电平
     * 
     * @param cmcId
     * @return
     */
    List<TopCcmtsDorRFPort> getRfPorts(Long cmcId);

    /**
     * 获取反向光收1-4输入光功率
     * 
     * @param cmcId
     * @return
     */
    List<TopCcmtsDorRRXOptPow> getRrxOptPows(Long cmcId);

}
