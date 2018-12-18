/***********************************************************************
 * $ CmPollStatisticsEngineServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.engine.service.impl;

import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.framework.annotation.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Engine
public class CmPollStatisticsEngineServiceImpl extends BaseEngine implements CmPollStatisticsEngineService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private List<CmPollStatisticsPush> cmPollStatisticsPushs = Collections.synchronizedList(new ArrayList<CmPollStatisticsPush>());

    @Override
    public void registCmPollStatisticsPush(CmPollStatisticsPush cmPollStatisticsPush) {
        cmPollStatisticsPushs.add(cmPollStatisticsPush);
    }

    @Override
    public void sendResult(Long time,List<CmPollResult> cmPollResults) {
        for (CmPollStatisticsPush cmPollStatisticsPush : cmPollStatisticsPushs) {
            try {
                cmPollStatisticsPush.pushResult(time,cmPollResults);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("sendResult error[" + cmPollStatisticsPush.moduleName()+"]", e);
                }
            }
        }
    }

    @Override
    public void completeRoundStatistics(Long time) {
        for (CmPollStatisticsPush cmPollStatisticsPush : cmPollStatisticsPushs) {
            try {
                cmPollStatisticsPush.completeRoundStatistics(time);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("completeRoundStatistics error[" + cmPollStatisticsPush.moduleName()+"]", e);
                }
            }
        }
    }

    @Override
    public void startRoundStatistics(Long time) {
        for (CmPollStatisticsPush cmPollStatisticsPush : cmPollStatisticsPushs) {
            try {
                cmPollStatisticsPush.startRoundStatistics(time);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("startRoundStatistics error[" + cmPollStatisticsPush.moduleName()+"]", e);
                }
            }
        }
    }


    @Override
    public void connected() {
        super.connected();
    }

    @Override
    public void disconnected() {
        super.disconnected();
    }
}