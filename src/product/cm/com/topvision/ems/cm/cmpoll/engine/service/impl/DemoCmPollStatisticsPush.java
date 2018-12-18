package com.topvision.ems.cm.cmpoll.engine.service.impl;

import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.framework.annotation.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author jay
 * @created 15-3-27.
 */
@Engine
public class DemoCmPollStatisticsPush extends BaseEngine implements CmPollStatisticsPush {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;

    @Override
    public void completeRoundStatistics(Long time) {

    }

    @Override
    public void startRoundStatistics(Long time) {

    }

    @Override
    public void pushResult(long time, List<CmPollResult> cmPollResults) {
        if (logger.isDebugEnabled()) {
            logger.debug(moduleName() + " time:" + sdf.format(new Date(time)));
            for (CmPollResult cmPollResult : cmPollResults) {
                logger.debug(moduleName() + " cmPollResult:" + cmPollResult);
            }
        }
    }

    @Override
    public String moduleName() {
        return "Demo push";
    }

    @Override
    public void initialize() {
        super.initialize();
        cmPollStatisticsEngineService.registCmPollStatisticsPush(this);
    }
}
