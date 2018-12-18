package com.topvision.ems.cm.cmtsInfoSummary.facade;

import java.util.Map;

import com.topvision.framework.annotation.EngineFacade;


@EngineFacade(serviceName = "CmtsInfoSummaryFacade", beanName = "cmtsInfoSummaryFacade", category = "CmPoll")
public interface CmtsInfoSummaryFacade {
    /**
     * 获取engine端cmts统计
     * @param
     * @return Map<Long,Map<String,Double>>
     */
    Map<Long,Map<String,Double>>getCmtsInfoDist();

}
