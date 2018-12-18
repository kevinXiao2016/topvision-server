/***********************************************************************
 * $ EponCmListServiceImpl.java,v1.0 2014-2-25 18:17:57 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.cm.dao.EponCmListDao;
import com.topvision.ems.epon.cm.service.EponCmListService;
import com.topvision.ems.epon.domain.EponCmNumStatic;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.performance.domain.CmtsCmNum;
import com.topvision.ems.epon.performance.domain.PonCmNum;
import com.topvision.framework.service.BaseService;

/**
 * @author jay
 * @created @2014-2-25-18:17:57
 */
@Service(value = "eponCmListService")
public class EponCmListServiceImpl extends BaseService implements EponCmListService {
    @Resource(name = "eponCmListDao")
    private EponCmListDao eponCmListDao;

    @Override
    public JSONArray loadPonOfOlt(Long entityId) {
        JSONArray array = new JSONArray();
        List<OltPonAttribute> pons = eponCmListDao.loadPonOfOlt(entityId);
        for (OltPonAttribute pon : pons) {
            JSONObject json = new JSONObject();
            json.put("ponId", pon.getPonId());
            // 将ponIndex转成指定格式(eg:3/1)
            json.put("ponIndex", getPonIndexStr(pon.getPonIndex()));
            array.add(json);
        }
        return array;
    }

    @Override
    public JSONObject loadPonAttrByCmcId(Long cmcId) {
        OltPonAttribute ponAttribute = eponCmListDao.loadPonAttrByCmcId(cmcId);
        JSONObject json = new JSONObject();
        json.put("ponId", ponAttribute.getPonId());
        // 将ponIndex转成指定格式(eg:3/1)
        json.put("ponIndex", getPonIndexStr(ponAttribute.getPonIndex()));
        return json;
    }

    @Override
    public EponCmNumStatic getEponCmNumStatic(Long entityId) {
        return eponCmListDao.getEponCmNumStatic(entityId);
    }

    @Override
    public List<PonCmNum> getPonCmNumList(Long entityId) {
        return eponCmListDao.getPonCmNumList(entityId);
    }

    @Override
    public List<CmtsCmNum> getCmtsCmNumList(Long entityId) {
        return eponCmListDao.getCmtsCmNumList(entityId);
    }

    /**
     * 将PONINDEX转化为3/4格式
     *
     * @param ponIndex
     * @return
     */
    private static String getPonIndexStr(Long ponIndex) {
        String resultStr = "";
        String hexStr = Long.toHexString(ponIndex);
        // 补全为10位
        int hexStrLen = hexStr.length();
        for (int i = 0; i < 10 - hexStrLen; i++) {
            hexStr = "0" + hexStr;
        }
        // 转成3/4类似格式
        resultStr = Integer.valueOf(hexStr.substring(0, 2), 16) + "/" + Integer.valueOf(hexStr.substring(2, 4), 16);
        return resultStr;
    }
}
