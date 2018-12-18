/***********************************************************************
 * $Id: DataModule.java,v1.0 2012-8-3 上午10:21:37 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

/**
 * @author RodJohn
 * @created @2012-8-3-上午10:21:37
 * 
 */
public interface DataModule {

    /**
     * 处理导入的sql列表，获取本模块需要的表数据
     * 
     * @param sqlList
     * @param nowVersion
     * @param fileVersion
     * @return
     */
    List<String> processModule(List<String> sqlList);

}
