/***********************************************************************
 * $Id: CpuAndMemCallBack.java,v1.0 2015-6-24 下午4:59:25 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.callback;

import java.util.Map;

import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.framework.annotation.Callback;

/**
 * @author flack
 * @created @2015-6-24-下午4:59:25
 *
 */
@Callback(beanName = "cpuAndMemService", serviceName = "cpuAndMemService")
public interface CpuAndMemCallBack {

    /**
     * 根据模块名称对配置文件进行解析并返回包含配置文件中所有类型及子类型对应的数据
     * @param moduleName 模块名称(用以区分不同的模块包,以定位配置文件)
     * @param deviceTypeId 设备typeId
     * @return
     */
    public Map<Long, CpuAndMemData> getCpuAndMemConfig(String moduleName);

    /**
     * 根据模块名称以及设备typeId进行对应配置文件的解析并返回指定设备类型对应的数据
     * @param moduleName
     * @param deviceTypeId
     * @return 
     */
    public CpuAndMemData getCpuAndMemData(String moduleName, Long deviceTypeId);

    /**
     * 查询指定的配置文件是否支持对应的设备类型
     * @param moduleName
     * @param deviceTypeId
     * @return
     */
    public Boolean isTypeContained(String moduleName, Long deviceTypeId);

}
