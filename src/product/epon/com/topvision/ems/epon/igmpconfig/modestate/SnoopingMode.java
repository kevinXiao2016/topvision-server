/***********************************************************************
 * $Id: SnoopingMode.java,v1.0 2016年7月2日 上午10:28:39 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.modestate;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;

/**
 * @author flack
 * @created @2016年7月2日-上午10:28:39
 *
 */
public class SnoopingMode extends AbstractMode {

    @Override
    public void switchToDisable() {
        //snooping切换到disable模式，不允许存在静态加入的配置
        //1.删除所有静态加入配置
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().deleteAllSnpStaticFwd(entityId);
        //切换到Disable模式
        super.modeContext.setMode(ModeContext.MODE_DISABLE);
        super.modeContext.getMode().switchToDisable();
    }

    @Override
    public void swicthThroughDisable(Integer resultMode) {
        //snooping切换到disable模式，不允许存在静态加入的配置
        //1.删除所有静态加入配置
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().deleteAllSnpStaticFwd(entityId);
        //切换到Disable模式
        super.modeContext.setMode(ModeContext.MODE_DISABLE);
        super.modeContext.getMode().swicthThroughDisable(resultMode);
    }

    @Override
    public void switchToSnooping() {
        //在这里执行切换到Snooping模式操作
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().modifyIgmpMode(entityId, IgmpConstants.IGMP_MODE_SNOOPING);
    }

    @Override
    public void switchToProxy() {
        this.swicthThroughDisable(IgmpConstants.IGMP_MODE_PROXY);
    }

    @Override
    public void switchToRouter() {
        this.swicthThroughDisable(IgmpConstants.IGMP_MODE_ROUTER);
    }

}
