/***********************************************************************
 * $Id: NbiDao.java,v1.0 2016-3-16 上午10:27:27 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.dao;

import java.util.List;

import com.topvision.ems.nbi.domain.NbiBaseConfig;
import com.topvision.ems.nbi.domain.NbiTarget;
import com.topvision.ems.nbi.domain.NbiTargetGroup;
import com.topvision.performance.nbi.api.NbiMultiPeriod;

/**
 * @author lizongtian
 * @created @2016-3-16-上午10:27:27
 *
 */
public interface NbiDao {

    NbiBaseConfig getNbiBaseConfig();

    NbiMultiPeriod getNbiMultiPeriod();

    List<NbiTargetGroup> getNbiTargetGroup(String module);

    List<NbiTarget> getNbiTargetListByGroup(Integer groupId);

    void updateNbiBaseConfig(NbiBaseConfig baseConfig);

    void updateNbiMultiPeriod(NbiMultiPeriod nbiMultiPeriod);

    void updateNbiTarget(List<NbiTarget> targets);

}
