/***********************************************************************
 * $Id: OnuCpeLocationAction.java,v1.0 2016-5-6 上午11:51:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.ems.epon.cpelocation.service.OnuCpeLocationService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2016-5-6-上午11:51:53
 *
 */
@Controller("onuCpeLocationAction")
public class OnuCpeLocationAction extends BaseAction {
    private static final long serialVersionUID = 3072596987133076253L;

    private String cpeMac;

    @Autowired
    private OnuCpeLocationService onuCpeLocationService;

    /**
     * 获取ONU CPE定位信息
     * @return
     */
    public String loadOnuCpeLoc() {
        OnuCpeLocation cpeLocation = onuCpeLocationService.getOnuCpeLocation(cpeMac);
        if (cpeLocation != null) {
            writeDataToAjax(cpeLocation);
        }
        return NONE;
    }

    public String getCpeMac() {
        return cpeMac;
    }

    public void setCpeMac(String cpeMac) {
        this.cpeMac = cpeMac;
    }

}
