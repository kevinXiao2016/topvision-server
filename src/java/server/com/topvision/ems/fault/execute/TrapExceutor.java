/***********************************************************************
 * $Id: TrapExceutor.java,v1.0 2017年1月4日 下午2:14:28 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.execute;

import com.topvision.ems.fault.message.TrapEvent;

/**
 * @author Bravin
 * @created @2017年1月4日-下午2:14:28
 *
 */
public interface TrapExceutor {
    void execute(TrapEvent evt);
}
