/***********************************************************************
 * $Id: EntityIdentifyImpl.java,v 1.1 May 16, 2008 6:42:56 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.dao.EntityTypeDao;
import com.topvision.ems.template.service.EntityIdentify;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.exception.dao.DaoException;
import com.topvision.framework.service.BaseService;

/**
 * @Create Date May 16, 2008 6:42:56 PM
 * 
 * @author kelers
 * 
 */
@Service("entityIdentify")
public class EntityIdentifyImpl extends BaseService implements EntityIdentify {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static final String SOID_HEAD = "1.3.6.1.4.1.";
    @Autowired
    private EntityTypeDao entityTypeDao;
    @Autowired
    private EntityTypeService entityTypeService;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.service.EntityIdentify#getCorp(java.lang.String)
     */
    @Override
    public long getCorp(String sysObjectID) {
        if (sysObjectID == null) {
            return -1;
        }
        String corp = sysObjectID;
        try {
            corp = sysObjectID.substring(sysObjectID.indexOf(SOID_HEAD) + SOID_HEAD.length());
            return Long.parseLong(corp.substring(0, corp.indexOf('.')));
        } catch (Exception ex) {
        }
        try {
            return Long.parseLong(corp);
        } catch (Exception ex) {
        }
        return -1;
    }

    /**
     * @return the entityTypeDao
     */
    public EntityTypeDao getEntityTypeDao() {
        return entityTypeDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.service.EntityIdentify#getOS(java.lang.String)
     */
    @Override
    public String getOS(String sysObjectID) {
        long corp = getCorp(sysObjectID);
        if (corp == 311) {
            return "Windows";
        } else if (corp == 42) {
            return "Solaris";
        } else if (corp == 4) {
            return "Unix";
        } else {
            return "";
        }
    }

    /**
     * 
     * @param sysServices
     * @return
     */
    private int getType(int sysServices) {
        return new Long(EntityType.UNKNOWN_TYPE).intValue();// unknown
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.service.EntityIdentify# identify(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public EntityType identify(String sysObjectID, String sysServices) {
        if (logger.isDebugEnabled()) {
            logger.debug("sysObjectID=" + sysObjectID + ",sysServices=" + sysServices);
        }
        EntityType et = null;
        long corp = getCorp(sysObjectID);
        int services;
        try {
            services = Integer.parseInt(sysServices);
        } catch (Exception ex) {
            services = -1;
        }
        if (sysObjectID == null || sysObjectID.indexOf(SOID_HEAD) == -1) {
            return identifyBySysServices(corp, services);
        }
        try {
            if (corp == -1) {
                return identifyBySysServices(corp, services);
            }
            List<EntityType> list = entityTypeDao.getTypesBySysObjectID(SOID_HEAD + corp);
            if (list == null || list.isEmpty()) {
                return identifyBySysServices(corp, services);
            }
            int index = Integer.MAX_VALUE;
            for (EntityType t : list) {
                if (sysObjectID.indexOf(t.getSysObjectID()) == -1) {
                    continue;
                }
                int c = Math.abs(sysObjectID.compareTo(t.getSysObjectID()));
                if (index > c) {
                    index = c;
                    et = t;
                }
            }
        } catch (DaoException ex) {
            logger.debug(ex.getMessage(), ex);
        }
        if (et == null) {
            return identifyBySysServices(corp, services);
        } else {
            return et;
        }
    }

    /**
     * 
     * @param sysServices
     * @return
     */
    private EntityType identifyBySysServices(long corp, int sysServices) {
        long type = getType(sysServices);
        if (type == EntityType.UNKNOWN_TYPE) {
            // windows
            if (corp == 311) {
                type = 10000000018L;
                // cisco,huawei,h3c
            } else if (corp == 9 || corp == 2011 || corp == 25506) {
                type = sysServices & 6;
            } else if (corp == 11 && sysServices == 72) {
                type = entityTypeService.getOltType();
            }
        }
        return entityTypeDao.selectByPrimaryKey(type);
    }

    /**
     * @param entityTypeDao
     *            the entityTypeDao to set
     */
    public void setEntityTypeDao(EntityTypeDao entityTypeDao) {
        this.entityTypeDao = entityTypeDao;
    }
}
