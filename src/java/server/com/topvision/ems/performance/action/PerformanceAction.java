package com.topvision.ems.performance.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @modify by fanzidong, 将性能阈值从中分离出来，并删除许多不使用的变量及方法
 * @created @2013-9-25-下午8:25:00
 * 性能主action
 */
@Controller("performanceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PerformanceAction extends BaseAction {
    private static final long serialVersionUID = 8682214278818174969L;

    private final Logger logger = LoggerFactory.getLogger(PerformanceAction.class);
    private PerfThresholdService perfThresholdService;

    public PerfThresholdService getPerfThresholdService() {
        return perfThresholdService;
    }

    public void setPerfThresholdService(PerfThresholdService perfThresholdService) {
        this.perfThresholdService = perfThresholdService;
    }

    public Logger getLogger() {
        return logger;
    }

}