/***********************************************************************
 * $Id: ModeContext.java,v1.0 2016年7月2日 上午9:49:34 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.modestate;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService;

/**
 * @author flack
 * @created @2016年7月2日-上午9:49:34
 * 控制IGMP模式切换的上下文环境类
 */
public class ModeContext {
    public final static DisableMode MODE_DISABLE = new DisableMode();
    public final static SnoopingMode MODE_SNOOPING = new SnoopingMode();
    public final static ProxyMode MODE_PROXY = new ProxyMode();
    public final static RouterMode MODE_ROUTER = new RouterMode();

    private AbstractMode mode;

    private Long entityId;
    private OltIgmpConfigService oltIgmpConfigService;

    public AbstractMode getMode() {
        return mode;
    }

    public void setMode(AbstractMode mode) {
        this.mode = mode;
        //把当前的环境通知到实现类中
        this.mode.setModeContext(this);
    }

    public OltIgmpConfigService getOltIgmpConfigService() {
        return oltIgmpConfigService;
    }

    public void setOltIgmpConfigService(OltIgmpConfigService oltIgmpConfigService) {
        this.oltIgmpConfigService = oltIgmpConfigService;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * 设备当前模式
     * @param currentMode
     */
    public void createCurrentMode(Integer currentMode) {
        switch (currentMode) {
        case IgmpConstants.IGMP_MODE_PROXY:
            this.setMode(MODE_PROXY);
            break;
        case IgmpConstants.IGMP_MODE_ROUTER:
            this.setMode(MODE_ROUTER);
            break;
        case IgmpConstants.IGMP_MODE_DISABLE:
            this.setMode(MODE_DISABLE);
            break;
        case IgmpConstants.IGMP_MODE_SNOOPING:
            this.setMode(MODE_SNOOPING);
            break;
        default:
            throw new RuntimeException("Has Wrong Igmp Mode value: " + currentMode);
        }
    }

    /**
     * 切换到新的模式
     * @param newMode
     */
    public void changeNewMode(Integer newMode){
        switch (newMode) {
        case IgmpConstants.IGMP_MODE_PROXY:
            this.switchToProxy();
            break;
        case IgmpConstants.IGMP_MODE_ROUTER:
            this.switchToRouter();
            break;
        case IgmpConstants.IGMP_MODE_DISABLE:
            this.switchToDisable();
            break;
        case IgmpConstants.IGMP_MODE_SNOOPING:
            this.switchToSnooping();
            break;
        default:
            throw new RuntimeException("Has Wrong Igmp Mode value: " + newMode);
        }
    }

    /**
     * 切换到Disable模式
     */
    public void switchToDisable() {
        this.mode.switchToDisable();
    }

    /**
     * 切换到Snooping模式
     */
    public void switchToSnooping() {
        this.mode.switchToSnooping();
    }

    /**
     * 切换到Proxy模式
     */
    public void switchToProxy() {
        this.mode.switchToProxy();
    }

    /**
     * 切换到Router模式
     */
    public void switchToRouter() {
        this.mode.switchToRouter();
    }

}
