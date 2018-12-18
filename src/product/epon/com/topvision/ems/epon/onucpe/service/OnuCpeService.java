/***********************************************************************
 * $Id: OnuCpeService.java,v1.0 2016年7月5日 下午3:29:08 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.service;

import java.util.List;

import com.topvision.ems.epon.onucpe.domain.OnuCpeConfig;
import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年7月5日-下午3:29:08
 *
 */
public interface OnuCpeService extends Service {

    /**
     * 修改ONU CPE采集配置
     * @param config
     */
    void updateOnuCpeConfig(OnuCpeConfig config);

    /**
     * 开启ONU CPE采集
     * @param entityId
     * @param period
     */
    void startOnuCpeStatus(Long entityId, Long period);

    /**
     * 关闭ONU CATV性能采集
     * @param entityId
     * @param snmpParam
     */
    void stopOnuCpeStatus(Long entityId, SnmpParam snmpParam);

    /**
     * @param onuId
     * @return
     */
    List<OnuUniCpe> loadOnuUniCpeList(Long onuId);

    /**
     * @param onuId
     * @param limit 
     * @param start 
     * @return
     */
    List<OnuUniCpe> loadOltUniCpeList(Long entityId, int start, int limit);

    /**
     * @param onuId
     */
    void refreshOnuUniCpe(Long onuId);

    /**
     * @param oltId
     * @return
     */
    int loadOltUniCpeListCount(Long oltId);

    /**
     * 
     */
    void execDataClean();

}
