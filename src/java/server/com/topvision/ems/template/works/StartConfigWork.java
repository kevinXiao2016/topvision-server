/***********************************************************************
 * $ StartConfigWork.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.works;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.template.domain.ApConfigParam;
import com.topvision.ems.template.domain.ApWorkParam;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */

public class StartConfigWork extends AbstractConfigWork {
    private static Logger logger = LoggerFactory.getLogger(StartConfigWork.class);

    public void execute(List<Long> entitys, List<ApWorkParam> workParamList, ApConfigParam apConfigParam,
            MessageService messageService) {
        logger.info("StartConfigWork.execute");
        workStart(messageService);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        try {
            logger.info("entitys = " + entitys);
            logger.info("workParamList = " + workParamList);
            logger.info("apConfigParam = " + apConfigParam);
            // 设置模式
            if (entitys.size() == 3) {
                success(messageService);
            } else {
                failure(messageService);
            }
            // 设置ssid
        } finally {
            workEnd(messageService);
        }
    }
}
