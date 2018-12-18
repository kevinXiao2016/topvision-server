/***********************************************************************
 * $Id: OltBfsxAction.java,v1.0 2014年10月11日 上午10:51:40 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.service.OltBfsxService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Bravin
 * @created @2014年10月11日-上午10:51:40
 *
 */
@Controller("oltBfsxAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltBfsxAction extends BaseAction {
    private static final long serialVersionUID = -6124386878483277616L;
    @Autowired
    private OltBfsxService oltBfsxService;
    private Long entityId;

    /**
     * 刷新可见的OLT信息
     * @return
     */
    public String bfsxOlt() {
        oltBfsxService.bfsxOltInfo(entityId);
        return NONE;
    }

    public String bfsxOltVlan() {
        oltBfsxService.bfsxOltVlan(entityId);
        return NONE;
    }

    public String bfsxOltMirror() {
        oltBfsxService.bfsxOltMirror(entityId);
        return NONE;
    }

    public String bfsxOltTrunk() {
        oltBfsxService.bfsxOltTrunk(entityId);
        return NONE;
    }

    public String bfsxOltPonProtectGroup() {
        oltBfsxService.bfsxOltPonProtectGroup(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
