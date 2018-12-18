package com.topvision.ems.cm.pnmp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.cmpoll.facade.domain.Complex;
import com.topvision.ems.cm.pnmp.dao.PnmpCmDataDao;
import com.topvision.ems.cm.pnmp.facade.domain.CorrelationGroup;
import com.topvision.ems.cm.pnmp.facade.utils.PnmpUtils;
import com.topvision.ems.cm.pnmp.message.PnmpPollStateEvent;

@Service("pnmpCorrelationGroupThreadPool")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpCorrelationGroupThreadPool implements Runnable {
    private Logger logger = LoggerFactory.getLogger(PnmpCorrelationGroupThreadPool.class);
    private Long groupId = 1L;

    @Autowired
    private PnmpCmDataDao pnmpCmDataDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private String type;
    private Double correlationGroupMtrLevel;

    @Override
    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("PnmpCorrelationGroupThreadPool.run ");
        }
        SystemPreferences correlationGroupMtrLevelPreference = systemPreferencesService.getSystemPreference("PNMP.correlationGroupMtrLevel");
        if (correlationGroupMtrLevelPreference != null) {
            try {
                correlationGroupMtrLevel = Double.parseDouble(correlationGroupMtrLevelPreference.getValue());
            } catch (NumberFormatException e) {
                correlationGroupMtrLevel = 25D;
            }
        } else {
            correlationGroupMtrLevel = 25D;
        }
        List<Long> cmcIds;
        if (type.equalsIgnoreCase(PnmpPollStateEvent.MIDDLE)) {
            cmcIds = pnmpCmDataDao.selectMiddleIntervalCmcIds();
        } else {
            cmcIds = pnmpCmDataDao.selectHighIntervalCmcIds();
        }

        for (Long cmcId : cmcIds) {
            List<CorrelationGroup> correlationGroups;
            if (type.equalsIgnoreCase(PnmpPollStateEvent.MIDDLE)) {
                correlationGroups = pnmpCmDataDao.selectMiddleCorrelationGroups(cmcId);
            } else {
                correlationGroups = pnmpCmDataDao.selectHighCorrelationGroups(cmcId);
            }
            Map<Integer, List<CorrelationGroup>> channelMap = new HashMap<>();
            for (CorrelationGroup correlationGroup : correlationGroups) {
                Complex[] freqResponse = PnmpUtils.freqResponse(correlationGroup.getOrginalValue());
                correlationGroup.setFreqResponse(freqResponse);
                if (channelMap.containsKey(correlationGroup.getUpChannelId())) {
                    List<CorrelationGroup> list = channelMap.get(correlationGroup.getUpChannelId());
                    list.add(correlationGroup);
                } else {
                    List<CorrelationGroup> list = new ArrayList<>();
                    list.add(correlationGroup);
                    channelMap.put(correlationGroup.getUpChannelId(), list);
                }
            }
            pnmpCmDataDao.updateCorrelationGroupByCmcId(cmcId);
            for (List<CorrelationGroup> groups : channelMap.values()) {
                for (int i = 0; i < groups.size(); i++) {
                    CorrelationGroup src = groups.get(i);
                    try {
                        if (src.getGroupId() == null) {
                            src.setGroupId(groupId++);
                        }
                        for (int j = i + 1; j < groups.size(); j++) {
                            CorrelationGroup dest = groups.get(j);
                            try {
                                double mtr = PnmpUtils.correlationGroup(src.getFreqResponse(), dest.getFreqResponse());
                                logger.trace("PnmpCorrelationGroupThreadPool src[" + src + "] dest[" + dest + "] mtr[" + mtr
                                        + "]");
                                if (mtr > correlationGroupMtrLevel) {
                                    dest.setGroupId(src.getGroupId());
                                }
                            } catch (Exception e) {
                                logger.debug("",e);
                            }
                        }
                    } catch (Exception e) {
                        logger.debug("",e);
                    }
                }
                pnmpCmDataDao.updateCorrelationGroup(groups);
            }
        }
        logger.debug("PnmpCorrelationGroupThreadPool.run end");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}