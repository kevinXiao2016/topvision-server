/***********************************************************************
 * $Id: OnuAuthManageServiceImpl.java,v1.0 2015年4月20日 下午12:25:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onuauth.dao.OnuAuthManageDao;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.domain.OnuAuthInfo;
import com.topvision.ems.epon.onuauth.domain.PonOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.service.OnuAuthManageService;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author loyal
 * @created @2015年4月20日-下午12:25:43
 * 
 */
@Service("onuAuthManageService")
public class OnuAuthManageServiceImpl extends BaseService implements OnuAuthManageService {
    @Autowired
    private OnuAuthManageDao onuAuthManageDao;
    @Autowired
    private OnuAuthService onuAuthService;

    @Override
    public List<OnuAuthInfo> getOnuAuthList(Map<String, Object> queryMap) {
        return onuAuthManageDao.selectOnuAuthList(queryMap);
    }

    @Override
    public Long getOnuAuthListCount(Map<String, Object> queryMap) {
        return onuAuthManageDao.selectOnuAuthListCount(queryMap);
    }

    @Override
    public List<OltOnuAuthStatistics> getOltOnuAuthStatistics(Map<String, Object> queryMap) {
        return onuAuthManageDao.selectOltOnuAuthStatistics(queryMap);
    }

    @Override
    public Long getOltOnuAuthStatisticsCount(Map<String, Object> queryMap) {
        return onuAuthManageDao.selectOltOnuAuthStatisticsCount(queryMap);
    }

    @Override
    public List<PonOnuAuthStatistics> getPonOnuAuthStatistics(Long entityId) {
        return onuAuthManageDao.selectPonOnuAuthStatistics(entityId);
    }

    @Override
    public List<OnuAuthFail> getOnuAuthFailList(Map<String, Object> queryMap) {
        return onuAuthManageDao.selectOnuAuthFailList(queryMap);
    }

    @Override
    public Long getOnuAuthFailListCount(Map<String, Object> queryMap) {
        return onuAuthManageDao.selectOnuAuthFailListCount(queryMap);
    }

    @Override
    public List<Long> getAuthOnuId(Long entityId, Long ponIndex, Integer authAction) {
        List<Long> onuIndexs = onuAuthManageDao.getOnuAuthIndex(entityId, ponIndex, authAction);
        List<Long> onuIds = new ArrayList<Long>();
        if (onuIndexs != null) {
            for (Long onuIndex : onuIndexs) {
                onuIds.add(EponIndex.getOnuNo(onuIndex));
            }
        }
        return onuIds;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public JSONObject addOnuAuthList(JSONArray addAuthArray) {
        JSONObject resultJson = new JSONObject();
        String successResult = "";
        String failResult = "";
        JSONObject auth = null;
        Iterator it = addAuthArray.iterator();
        while (it.hasNext()) {
            auth = (JSONObject) it.next();
            String recordId = null;
            try {
                recordId = auth.getString("recordId");
                addOnuAuth(auth);
                successResult += recordId + "&";
            } catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("addOnuAuth error:{}", ex);
                }
                failResult += recordId + "&";
            }
        }
        resultJson.put("successResult", successResult);
        resultJson.put("failResult", failResult);

        return resultJson;
    }

    private void addOnuAuth(JSONObject auth) {
        OltAuthentication oltAuthentication = new OltAuthentication();
        oltAuthentication.setEntityId(auth.getLong("entityId"));
        oltAuthentication.setPonId(auth.getLong("ponId"));
        Integer slotId = EponIndex.getSlotNo(auth.getLong("ponIndex")).intValue();
        Integer ponId = EponIndex.getPonNo(auth.getLong("ponIndex")).intValue();
        Integer onuId = getAvailableOnuId(auth.getLong("entityId"), auth.getLong("ponIndex"), auth.getInt("authAction"));
        Long onuIndex = EponIndex.getOnuIndex(slotId, ponId, onuId);
        oltAuthentication.setOnuIndex(onuIndex);
        oltAuthentication.setPonIndex(auth.getLong("ponIndex"));
        oltAuthentication.setAuthAction(auth.getInt("authAction"));
        oltAuthentication.setAuthType(auth.getInt("authType"));
        oltAuthentication.setOnuPreType(auth.getInt("onuPreType"));
        oltAuthentication.setOnuAuthenMacAddress(auth.getString("mac"));
        oltAuthentication.setTopOnuAuthLogicSn(auth.getString("sn"));
        oltAuthentication.setTopOnuAuthPassword(auth.getString("password"));
        onuAuthService.addOnuAuthenPreConfig(oltAuthentication);
        if (oltAuthentication.getOnuAuthenMacAddress() != null
                && EponConstants.OLT_AUTHEN_MACACTION_ACCEPT.equals(oltAuthentication.getAuthAction())) {
            onuAuthService.deleteOnuAuthBlock(oltAuthentication.getPonId(), oltAuthentication.getOnuIndex());
        } else if (!("").equalsIgnoreCase(oltAuthentication.getTopOnuAuthLogicSn())) {
            onuAuthService.deleteOnuAuthBlock(oltAuthentication.getPonId(), oltAuthentication.getOnuIndex());
        }
        if (!(("").equalsIgnoreCase(oltAuthentication.getOnuPreTypeString()) || (EponConstants.OLT_AUTHEN_MAC
                .equals(oltAuthentication.getAuthType()) && EponConstants.OLT_AUTHEN_MACACTION_REJECT
                .equals(oltAuthentication.getAuthAction())))) {
            onuAuthService.modifyOnuPreType(oltAuthentication.getEntityId(), oltAuthentication.getOnuIndex(),
                    oltAuthentication.getOnuPreType().toString());
        }
    }

    private Integer getAvailableOnuId(Long entityId, Long ponIndex, Integer authAction) {
        List<Long> unAvailableOnuIds = getAuthOnuId(entityId, ponIndex, authAction);
        if (EponConstants.OLT_AUTHEN_MACACTION_ACCEPT.equals(authAction)) {
            for (Long i = 1L; i < 65; i++) {
                if (unAvailableOnuIds.contains(i)) {
                    continue;
                } else {
                    return i.intValue();
                }
            }
        } else {
            for (Long i = 129L; i < 193; i++) {
                if (unAvailableOnuIds.contains(i)) {
                    continue;
                } else {
                    return i.intValue();
                }
            }
        }
        return null;
    }
}
