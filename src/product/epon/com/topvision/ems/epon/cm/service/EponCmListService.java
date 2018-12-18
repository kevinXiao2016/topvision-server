/***********************************************************************
 * $ com.topvision.ems.epon.cm.service.EponCmListService,v1.0 2014-2-25 18:13:17 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cm.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.topvision.ems.epon.domain.EponCmNumStatic;
import com.topvision.ems.epon.performance.domain.CmtsCmNum;
import com.topvision.ems.epon.performance.domain.PonCmNum;
import com.topvision.framework.service.Service;

/**
 * @author jay
 * @created @2014-2-25-18:13:17
 */
public interface EponCmListService extends Service {
    /**
     * 加载指定OLT下的所有PON口
     *
     * @param entityId
     * @return
     */
    JSONArray loadPonOfOlt(Long entityId);

    /**
     * 根据8800A的cmcId去获取其所在的PON口
     *
     * @param cmcId
     * @return
     */
    JSONObject loadPonAttrByCmcId(Long cmcId);

    /**
     * 刷新epon下的关联CM信息
     *
     * @param entityId
     * @return
     */
    EponCmNumStatic getEponCmNumStatic(Long entityId);

    /**
     * 获取olt下所有Pon口下CM个数
     * @param entityId
     * @return
     */
    List<PonCmNum> getPonCmNumList(Long entityId);

    /**
     * 获取olt下所有Cmts下CM个数
     * @param entityId
     * @return
     */
    List<CmtsCmNum> getCmtsCmNumList(Long entityId);
}
