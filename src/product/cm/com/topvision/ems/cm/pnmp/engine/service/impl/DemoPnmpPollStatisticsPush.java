package com.topvision.ems.cm.pnmp.engine.service.impl;

import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsEngineService;
import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsPush;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;
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
public class DemoPnmpPollStatisticsPush extends BaseEngine implements PnmpPollStatisticsPush {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private PnmpPollStatisticsEngineService pnmpPollStatisticsEngineService;

    @Override
    public void completeRoundStatistics(Long time, PnmpPollTask pnmpPollTask) {

    }

    @Override
    public void startRoundStatistics(Long time) {

    }

    @Override
    public void pushResult(long time, List<PnmpPollResult> pnmpPollResults, PnmpPollTask pnmpPollTask) {
        if (logger.isDebugEnabled()) {
            logger.debug(moduleName() + " time:" + sdf.format(new Date(time)));
            for (PnmpPollResult cmPollResult : pnmpPollResults) {
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
        pnmpPollStatisticsEngineService.registPnmpPollStatisticsPush(this);
    }
}
