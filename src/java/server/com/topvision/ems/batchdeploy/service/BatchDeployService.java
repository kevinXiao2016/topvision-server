/***********************************************************************
 * $Id: BatchDeployService.java,v1.0 2013年11月30日 下午2:57:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.framework.service.Service;

/**
 * 请一定要注意，如果 V bundle是一个引用类型，并且准备最终使用这个对象下下发的对象，那么在executor中一定要重新new一个，否则将出错
 * 如果bundle和最终下发的对象不是同一个对象，那么没有限制
 * 如果bundle是基本数据类型，也没有限制
 * @author Bravin
 * @created @2013年11月30日-下午2:57:58
 *
 */
public interface BatchDeployService extends Service {

    /**
     * T必须是可序列化的!
     * 由于executor实例可能仅存在于engineserver上,所以这时无论是从server上获取其executor的class还是获取其实例都是得不到的，
     * 所以这里采用的是传递executor的beanName，虽然这样不够灵活，但是是目前在这种途径下的唯一解决办法。
     * @param targetList
     * @param entityId
     * @param bundle
     * @param executor
     * @return
     */
    public <T extends BatchRecordSupport, V> ResultBundle<T> batchDeploy(List<T> targetList, Long entityId, V bundle,
            String executor);

    /**
     * 批量下发配置
     * @param multiEntityTargets
     * @param bundle
     * @param executor
     * @return
     */
    public <T extends BatchRecordSupport, V> ResultBundle<T> batchDeploy(Map<Long, List<T>> multiEntityTargets, V bundle,
            String executor);

    
}
