/***********************************************************************
 * $Id: CC8800ASystemTimeFacade.java,v1.0 2013-11-23 下午5:28:10 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.util.Map;

import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.domain.DeviceParam;

/**
 * 此类为在EPON下获取CC8800A的启动时间
 * 
 * @author Victor
 * @created @2013-11-23-下午5:28:10
 *
 */
@EngineFacade(serviceName = "CC8800ASystemTimeFacade", beanName = "cc8800ASystemTimeFacade")
public interface CC8800ASystemTimeFacade<T extends DeviceParam> {
    Map<String, Long> getUptime(T param);
}
