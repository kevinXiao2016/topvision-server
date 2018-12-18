/***********************************************************************
 * $Id: AutoClearService.java,v1.0 2016年11月14日 上午10:24:16 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.autoclear.service;

import java.util.List;

import com.topvision.ems.autoclear.domain.AutoClearCmciRecord;
import com.topvision.ems.autoclear.domain.AutoClearRecord;

/**
 * @author Rod John
 * @created @2016年11月14日-上午10:24:16
 *
 */
public interface AutoClearService {

    /**
     * 分页查询
     * 
     * @param start
     * @param limit
     * @return
     */
    public List<AutoClearRecord> loadAutoClearRecord(Integer start, Integer limit);
    
    /**
     * 分页查询cmci型设备删除的离线记录
     * 
     * @param start
     * @param limit
     * @return
     */
    public List<AutoClearCmciRecord> loadAutoClearCmciRecord(Integer start, Integer limit);

    /**
     * 
     * 
     * @return
     */
    public Integer loadAutoClearRecordCount();
    
    /**
     * 查询cmci型设备删除的离线记录总数
     * 
     * @return
     */
    public Integer loadAutoClearCmciRecordCount();

}
