/***********************************************************************
 * $Id: CmcDocsisConfigService.java,v1.0 2013-4-26 下午8:28:02 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.service;

import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-4-26-下午8:28:02
 *
 */
public interface CmcDocsisConfigService extends Service {

    /**
     * 更新CCMTS　DOCSIS记录
     * 
     * @param cmcDocsis
     */
    public void updateCmcDocsis(CmcDocsisConfig cmcDocsis);

    /**
     * 获取指定CCMTS　DOCSIS记录
     * 
     * @param entityId
     * @param cmcId
     * @return
     */
    public CmcDocsisConfig getCmcDocsis(Long entityId, Long ifIndex);

    /**
     * 从设备获取CCMTS　Docsis数据
     * 
     * @param cmcDocsis
     */
    public void refreshCmcDocsisFromFacility(CmcDocsisConfig cmcDocsis);

}
