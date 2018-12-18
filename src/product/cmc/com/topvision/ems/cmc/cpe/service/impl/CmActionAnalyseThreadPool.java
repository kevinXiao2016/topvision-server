package com.topvision.ems.cmc.cpe.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;

/**
 * @author jay
 * @created 15-8-3.
 */
@Service("cmActionAnalyseThreadPool")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmActionAnalyseThreadPool implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(CmActionAnalyseThreadPool.class);
    @Autowired
    private CpeAnalyseDao cpeAnalyseDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    private Long entityId;
    private CmAttribute cmAttribute;
    private Long realTimeLong;
    private Long endTimeLong;

    @Override
    public void run() {
        Long entityType = entityService.getEntity(entityId).getTypeId();
        CmAct cmAct = cpeAnalyseDao.getCmLastStatus(entityId, cmAttribute);
        if (cmAct != null && realTimeLong < cmAct.getRealtimeLong()) {
            realTimeLong = cmAct.getRealtimeLong() + 1000;
        }
        if (cmAct != null && endTimeLong < cmAct.getTimeLong()) {
            endTimeLong = cmAct.getTimeLong() + 1000;
        }
        if (cmAttribute.isCmOnline()) {
            if (cmAct == null || CmAct.OFFLINE.equals(cmAct.getAction())) {
                CmAct ca = new CmAct();
                ca.setAction(CmAct.ONLINE);
                ca.setEntityId(entityId);
                ca.setCmIndex(cmAttribute.getStatusIndex());
                ca.setCmmac(new MacUtils(cmAttribute.getStatusMacAddress()).longValue());
                try {
                    // modify by loyal 添加cmts支持
                    if (entityTypeService.isCmts(entityType)) {
                        ca.setCmip(new IpUtils(cmAttribute.getStatusIpAddress()).longValue());
                    } else {
                        ca.setCmip(new IpUtils(cmAttribute.getStatusInetAddress()).longValue());
                    }
                } catch (Exception e) {
                    try {
                        ca.setCmip(new IpUtils(cmAttribute.getStatusIpAddress()).longValue());
                    } catch (Exception e1) {
                        logger.debug("getStatusIpAddress", e1);
                        ca.setCmip(0L);
                    }
                    logger.debug("getStatusInetAddress", e);
                }
                ca.setRealtimeLong(realTimeLong);
                ca.setTimeLong(endTimeLong);
                cpeAnalyseDao.insertCmAct(ca);
            }
        } else {
            if (cmAct == null || CmAct.ONLINE.equals(cmAct.getAction())) {
                CmAct ca = new CmAct();
                ca.setAction(CmAct.OFFLINE);
                ca.setEntityId(entityId);
                ca.setCmIndex(cmAttribute.getStatusIndex());
                ca.setCmmac(new MacUtils(cmAttribute.getStatusMacAddress()).longValue());
                try {
                    if (entityTypeService.isCmts(entityType)) {
                        ca.setCmip(new IpUtils(cmAttribute.getStatusIpAddress()).longValue());
                    } else {
                        ca.setCmip(new IpUtils(cmAttribute.getStatusInetAddress()).longValue());
                    }
                } catch (Exception e) {
                    try {
                        ca.setCmip(new IpUtils(cmAttribute.getStatusIpAddress()).longValue());
                    } catch (Exception e1) {
                        logger.debug("getStatusIpAddress", e1);
                        ca.setCmip(0L);
                    }
                    logger.debug("getStatusInetAddress", e);
                }
                ca.setRealtimeLong(realTimeLong);
                ca.setTimeLong(endTimeLong);
                cpeAnalyseDao.insertCmAct(ca);
            }
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public CmAttribute getCmAttribute() {
        return cmAttribute;
    }

    public void setCmAttribute(CmAttribute cmAttribute) {
        this.cmAttribute = cmAttribute;
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
