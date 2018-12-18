/***********************************************************************
 * $Id: DisableMode.java,v1.0 2016年7月2日 上午10:03:29 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.modestate;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;

/**
 * @author flack
 * @created @2016年7月2日-上午10:03:29
 *
 */
public class DisableMode extends AbstractMode {

    @Override
    public void switchToDisable() {
        //执行模式的切换 切换到DisableMode
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().modifyIgmpMode(entityId, IgmpConstants.IGMP_MODE_DISABLE);
    }

    @Override
    public void swicthThroughDisable(Integer resultMode) {
        //先切换到Disable
        this.switchToDisable();
        //再从Disable切换到其它模式
        switch (resultMode) {
        case IgmpConstants.IGMP_MODE_PROXY:
            this.switchToProxy();
            break;
        case IgmpConstants.IGMP_MODE_ROUTER:
            this.switchToRouter();
            break;
        case IgmpConstants.IGMP_MODE_DISABLE:
            break;
        case IgmpConstants.IGMP_MODE_SNOOPING:
            this.switchToSnooping();
            break;
        default:
            throw new RuntimeException("Has Wrong Igmp Mode value: " + resultMode);
        }
    }

    @Override
    public void switchToSnooping() {
        //disable切换到snooping时不允许配置VLAN相关业务,可控模板中不允许关联组播组(在删除VLAN业务时处理),也不允许CTC处理使能
        //1.删除VLAN业务
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().deleteAllVlanConfig(entityId);
        //2.将CTC设置为不使能
        super.modeContext.getOltIgmpConfigService().modifyCtcToDisable(entityId);
        //切换到Snooping模式
        super.modeContext.setMode(ModeContext.MODE_SNOOPING);
        super.modeContext.getMode().switchToSnooping();
    }

    @Override
    public void switchToProxy() {
        //disable切换到proxy模式，不允许配置全局上联口
        //1. 将全局上联口配置设置为INVALID
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().deleteSnpUplink(entityId);
        //切换到Proxy模式
        super.modeContext.setMode(ModeContext.MODE_PROXY);
        super.modeContext.getMode().switchToProxy();
    }

    @Override
    public void switchToRouter() {
        //disable切换到router模式，组播vlan中不允许配置组播vlan上联口，也不允许配置全局上联口
        //1. 将VLAN中端口全部设置为INVALID (确定是否都可以修改)
        Long entityId = super.modeContext.getEntityId();
        super.modeContext.getOltIgmpConfigService().deleteVlanUplink(entityId);
        //2. 将全局上联口配置设置为INVALID
        super.modeContext.getOltIgmpConfigService().deleteSnpUplink(entityId);
        // 切换到Router模式
        super.modeContext.setMode(ModeContext.MODE_ROUTER);
        super.modeContext.getMode().switchToRouter();
    }

}
