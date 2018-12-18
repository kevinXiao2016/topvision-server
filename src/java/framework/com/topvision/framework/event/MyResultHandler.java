/***********************************************************************
 * $Id: MyResultHandler.java,v1.0 2013-9-22 下午5:28:16 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.event;

import org.apache.ibatis.session.ResultHandler;

/**
 * @author Victor
 * @created @2013-9-22-下午5:28:16
 * 
 */
public interface MyResultHandler extends ResultHandler {
    /**
     * 处理数据记录后调用此方法.
     */
    void complete();

    /**
     * 处理数据记录前调用此方法做准备工作.
     */
    void prepare();
}
