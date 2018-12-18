package com.topvision.ems.cm.pnmp.engine.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsEngineService;
import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsPush;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created 15-3-27.
 */
@Engine
public class PnmpPollResultCalculation extends BaseEngine implements PnmpPollStatisticsPush, BeanFactoryAware {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private PnmpPollStatisticsEngineService pnmpPollStatisticsEngineService;
    private ThreadPoolExecutor threadPoolExecutor;
    private BeanFactory beanFactory;
    @Value("${PnmpPollResultCalculation.maxPoolsize:10}")
    private Integer maxPoolsize;

    @Override
    public void completeRoundStatistics(Long time, PnmpPollTask pnmpPollTask) {

    }

    @Override
    public void startRoundStatistics(Long time) {

    }

    @Override
    public void pushResult(long time, List<PnmpPollResult> pnmpPollResults, PnmpPollTask pnmpPollTask) {
        logger.trace("PnmpPollResultCalculation.pushResult---------------------------------------------------------");
        for (PnmpPollResult cmPollResult : pnmpPollResults) {
            try {
                pnmpPollStatisticsEngineService.addCalculationCount();
                PnmpPollResultCalculationThreadPool pnmpPollResultCalculationThreadPool = (PnmpPollResultCalculationThreadPool) beanFactory
                        .getBean("pnmpPollResultCalculationThreadPool");
                pnmpPollResultCalculationThreadPool.setPnmpPollResult(cmPollResult);
                pnmpPollResultCalculationThreadPool.setPnmpPollTask(pnmpPollTask);
                pnmpPollResultCalculationThreadPool.setPnmpPollStatisticsEngineService(pnmpPollStatisticsEngineService);
                threadPoolExecutor.execute(pnmpPollResultCalculationThreadPool);
            } catch (Exception e) {
                logger.debug("PnmpPollFacadeImpl.appendTesk.Exception", e);
            }
        }
    }

    @Override
    public String moduleName() {
        return "Demo push";
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void initialize() {
        super.initialize();
        pnmpPollStatisticsEngineService.registPnmpPollStatisticsPush(this);
        threadPoolExecutor = new ThreadPoolExecutor(maxPoolsize, maxPoolsize, maxPoolsize, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxPoolsize * 1000));
    }
}
