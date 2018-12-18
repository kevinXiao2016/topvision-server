/***********************************************************************
 * $Id: RouterMode.java,v1.0 2016年7月2日 上午10:30:06 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.modestate;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;

/**
 * @author flack
 * @created @2016年7月2日-上午10:30:06
 *
 */
public class RouterMode extends AbstractMode {

    @Override
    public void switchToDisable() {
        //router模式切换到disable模式不用处理与业务配置
        //先将状态切换到disable
        super.modeContext.setMode(ModeContext.MODE_DISABLE);
        //然后委托disable状态的switchToDisable方法执行模式的切换
        super.modeContext.getMode().switchToDisable();
    }

    @Override
    public void swicthThroughDisable(Integer resultMode) {
        super.modeContext.setMode(ModeContext.MODE_DISABLE);
        super.modeContext.getMode().swicthThroughDisable(resultMode);
    }

    @Override
    public void switchToSnooping() {
        this.swicthThroughDisable(IgmpConstants.IGMP_MODE_SNOOPING);
    }

    @Override
    public void switchToProxy() {
        this.swicthThroughDisable(IgmpConstants.IGMP_MODE_PROXY);
    }

    @Override
    public void switchToRouter() {
        //在这里执行切换到Router模式操作
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().modifyIgmpMode(entityId, IgmpConstants.IGMP_MODE_ROUTER);
    }

}
