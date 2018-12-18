/***********************************************************************
 * $Id: NbiPerfGroupService.java,v1.0 2016年3月21日 下午2:26:05 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.service;

import java.util.List;

import com.topvision.performance.nbi.api.NbiFtpConfig;
import com.topvision.performance.nbi.api.PerfGroupRow;

/**
 * @author Bravin
 * @created @2016年3月21日-下午2:26:05
 *
 */
public interface NbiCallBackService {

    List<PerfGroupRow> getPerfGroupRow();

    /**
     * 获取NBI的FTP配置参数
     * @return
     */
    NbiFtpConfig loadNbiFtpConfigParam();
}
