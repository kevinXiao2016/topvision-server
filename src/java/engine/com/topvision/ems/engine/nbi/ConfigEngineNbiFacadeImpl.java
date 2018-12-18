/***********************************************************************
 * $Id: ConfigEngineNbiFacadeImpl.java,v1.0 2016年3月14日 上午10:48:41 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.nbi;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.nbi.ConfigEngineNbiFacade;
import com.topvision.ems.facade.nbi.NbiAddress;
import com.topvision.framework.annotation.Facade;

/**
 * @author Bravin
 * @created @2016年3月14日-上午10:48:41
 *
 */
@Facade("configEngineNbiFacade")
public class ConfigEngineNbiFacadeImpl extends BaseEngine implements ConfigEngineNbiFacade {
    @Autowired
    private NbiSupportService nbiSupportService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.nbi.facade.ConfigEngineNbiFacade#setNbiConfig(com.topvision.ems.nbi.facade
     * .NbiAddressConfig)
     */
    @Override
    public void notifyEngineConnectNbi(NbiAddress nbiAddress) {
        logger.info("notifyEngineConnectNbi...");
        nbiSupportService.setNbiAddress(nbiAddress);
        nbiSupportService.setAllowDataRedirect(nbiAddress.isStartNbi());
    }

}
