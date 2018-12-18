/***********************************************************************
 * $ com.topvision.ems.network.service.GetTopoFolderNum,v1.0 2012-3-27 11:41:30 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import com.topvision.ems.network.domain.TopoFolderNum;
import com.topvision.ems.network.domain.TopoFolderStat;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @created @2012-3-27-11:41:30
 */
public interface GetTopoFolderNum {
    /**
     * 获取本模块的设备统计结果
     * 
     * @return List<TopoFolderNum>
     * @param topoFolderStats
     */
    Map<Long, List<TopoFolderNum>> getTopoFolder(List<TopoFolderStat> topoFolderStats);

    List<String> getColNames();
}
