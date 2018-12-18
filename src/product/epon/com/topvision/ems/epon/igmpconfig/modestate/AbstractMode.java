/***********************************************************************
 * $Id: AbstractModeState.java,v1.0 2016年7月2日 上午9:49:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.modestate;

/**
 * @author flack
 * @created @2016年7月2日-上午9:49:08
 *
 */
public abstract class AbstractMode {
    protected ModeContext modeContext;

    public ModeContext getModeContext() {
        return modeContext;
    }

    public void setModeContext(ModeContext modeContext) {
        this.modeContext = modeContext;
    }

    /**
     * 切换到Disable模式
     */
    public abstract void switchToDisable();

    /**
     * 切换到Snooping模式
     */
    public abstract void switchToSnooping();

    /**
     * 切换到Proxy模式
     */
    public abstract void switchToProxy();

    /**
     * 切换到Router模式
     */
    public abstract void switchToRouter();

    /**
     * 经由Disable模式切换到其他模式<br/>
     * snooping、proxy、router模式之前不允许互相切换，必须先切换到disable；
     * @param resultMode
     */
    public abstract void swicthThroughDisable(Integer resultMode);


}
