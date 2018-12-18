/***********************************************************************
 * $ PnmpPollConfigService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import com.topvision.ems.cm.pnmp.domain.PnmpPollCollectParam;
import com.topvision.framework.service.Service;

import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface PnmpPollConfigService extends Service {


    /**
     * PNMP是否关闭
     *
     * @return
     */
    public boolean isPnmpPollClose();

    /**
     * 获取采集器配置信息
     * 
     * @return
     */
    public PnmpPollCollectParam getPnmpPollCollectParam();

}