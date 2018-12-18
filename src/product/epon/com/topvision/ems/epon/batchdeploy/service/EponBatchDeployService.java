/***********************************************************************
 * $Id: EponBatchDeployService.java,v1.0 2013年12月2日 下午2:21:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年12月2日-下午2:21:26
 *
 */
public interface EponBatchDeployService extends Service {

    /**
     * EPON模板使用的批量配置接口,方便用于单设备批量
     * @param targetList
     * @param excludeList
     * @param entityId
     * @param bundle
     * @param executor
     * @return
     */
    public <V> ResultBundle<Target> batchDeploy(List<String> targetList, List<String> excludeList, Long entityId,
            V bundle, String executor);

    /**
     * EPON模板使用的批量配置接口,可用于多设备批量
     * @param multiTargetList
     * @param multiExcludeList
     * @param bundle
     * @param executor
     * @return
     */
    public <V> ResultBundle<Target> batchDeploy(Map<Long, List<String>> multiTargetList,
            Map<Long, List<String>> multiExcludeList, V bundle, String executor);

    /**
     * 带操作记录的批量配置功能
     * @param targetList
     * @param excludeList
     * @param entityId
     * @param bundle
     * @param executor
     * @param typeId
     * @param comment
     * @return
     */
    public <V> ResultBundle<Target> batchDeploy(List<String> targetList, List<String> excludeList, Long entityId,
            V bundle, String executor, Integer typeId, String comment);

    /**
     * 针对指定模板解绑定的方法
     * @param targetList
     * @param entityId
     * @param bundle
     * @param executor
     * @return
     */
    public <T extends BatchRecordSupport, V> ResultBundle<T> batchDeploy(List<T> targetList, Long entityId, V bundle,
            String executor, Integer typeId, String comment);

}
