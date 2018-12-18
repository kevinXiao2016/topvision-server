package com.topvision.ems.cmc.cpe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;

/**
 * @author jay
 * @created 15-8-3.
 */
@Service("cpeActionAnalyseThreadPool")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CpeActionAnalyseThreadPool implements Runnable {
    @Autowired
    private CpeAnalyseDao cpeAnalyseDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    private Long entityId;
    private TopCpeAttribute cpeAttribute;
    private Long realTimeLong;
    private Long endTimeLong;

    @Override
    public void run() {
        CpeAct cpeAct = cpeAnalyseDao.getCpeLastStatus(entityId, cpeAttribute);
        if (cpeAct != null && realTimeLong < cpeAct.getRealtimeLong()) {
            realTimeLong = cpeAct.getRealtimeLong() + 1000;
        }
        if (cpeAct != null && endTimeLong < cpeAct.getTimeLong()) {
            endTimeLong = cpeAct.getTimeLong() + 1000;
        }
        if (cpeAct == null || CpeAct.OFFLINE.equals(cpeAct.getAction())) {
            CpeAct ca = new CpeAct();
            ca.setAction(CpeAct.ONLINE);
            ca.setEntityId(entityId);
            ca.setCmIndex(cpeAttribute.getTopCmCpeCmStatusIndex());
            ca.setCmmac(cpeAttribute.getTopCmCpeToCmMacAddrLong());
            ca.setCpemac(cpeAttribute.getTopCmCpeMacAddressLong());
            try {
                ca.setCpeip(new IpUtils(cpeAttribute.getTopCmCpeIpAddressLong()).longValue());
            } catch (Exception e) {
                ca.setCpeip(0L);
            }
            ca.setRealtimeLong(realTimeLong);
            ca.setTimeLong(endTimeLong);
            cpeAnalyseDao.insertCpeAct(ca);
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public TopCpeAttribute getCpeAttribute() {
        return cpeAttribute;
    }

    public void setCpeAttribute(TopCpeAttribute cpeAttribute) {
        this.cpeAttribute = cpeAttribute;
    }

    public Long getRealTimeLong() {
        return realTimeLong;
    }

    public void setRealTimeLong(Long realTimeLong) {
        this.realTimeLong = realTimeLong;
    }

    public Long getEndTimeLong() {
        return endTimeLong;
    }

    public void setEndTimeLong(Long endTimeLong) {
        this.endTimeLong = endTimeLong;
    }
}
