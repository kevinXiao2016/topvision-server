/***********************************************************************
 * $Id: CmSignalService.java,v1.0 2016年6月28日 上午11:43:46 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.UserContext;

/**
 * @author YangYi
 * @created @2016年6月28日-上午11:43:46
 *
 */
public interface CmSignalService extends Service {
    /**
     * 查询CM实时信号并存入数据库
     * 
     * @param cmId
     */
    boolean refreshSignalWithSave(Long cmId);

    /**
     * 获取CM实时信号质量并推送至前台
     * 
     * @param cmIds
     * @param jConnectedId
     * @param uc
     */
    void getSignalAndPush(String cmIds, final String jConnectedId, String operationId, final UserContext uc);

    /**
     * 获取CM的信号质量
     * 
     * @param cmId
     * @return
     */
    CmAttribute getCmSignal(Long cmId);
}
