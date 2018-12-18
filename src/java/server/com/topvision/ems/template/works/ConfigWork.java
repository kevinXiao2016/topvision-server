/***********************************************************************
 * $ ConfigWork.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.works;

import java.util.List;

import com.topvision.ems.template.domain.ApConfigParam;
import com.topvision.ems.template.domain.ApWorkParam;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public interface ConfigWork {
    public void execute(List<Long> entitys, List<ApWorkParam> workParamList, ApConfigParam apConfigParam,
            MessageService messageService);
}
