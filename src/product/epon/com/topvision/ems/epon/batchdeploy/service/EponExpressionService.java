/***********************************************************************
 * $Id: EponBatchDeployExpressionService.java,v1.0 2013年12月2日 下午2:33:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年12月2日-下午2:33:29
 *
 */
public interface EponExpressionService extends Service {

    /**
     * 考虑到多设备批量的时候可能各设备下发板卡的表达式会不一样，所以这里就没有采取 多设备同一表达式的策略，这样会增加解析时访问数据库的次数，但是
     * 扩展性比较好
     * @param multiTargets
     * @param multiExcludeList 
     * @return 
     */
    Map<Long, List<Target>> getBatchDeployTargets(Map<Long, List<String>> multiTargets,
            Map<Long, List<String>> multiExcludeList);

}
