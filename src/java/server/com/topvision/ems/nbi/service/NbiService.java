/***********************************************************************
 * $Id: NbiService.java,v1.0 2016-3-16 上午10:20:51 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.service;

import java.util.List;

import com.topvision.ems.nbi.domain.NbiBaseConfig;
import com.topvision.ems.nbi.domain.NbiTarget;
import com.topvision.ems.nbi.domain.NbiTargetGroup;
import com.topvision.framework.service.Service;
import com.topvision.performance.nbi.api.NbiMultiPeriod;

/**
 * @author lizongtian
 * @created @2016-3-16-上午10:20:51
 *
 */
public interface NbiService extends Service {

    NbiBaseConfig getNbiBaseConfig();

    NbiMultiPeriod getNbiMultiPeriod();

    List<NbiTargetGroup> getNbiTargetGroup(Boolean oltModule, Boolean onuModule, Boolean cmcModule);

    void updateNbiBaseConfig(NbiBaseConfig baseConfig);

    void updateNbiMultiPeriod(NbiMultiPeriod nbiMultiPeriod);

    void updateNbiTarget(List<NbiTarget> targets);
}
