/***********************************************************************
 * $Id: OnuReplaceServiceImpl.java,v1.0 2016-4-18 上午10:55:03 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.OnuForceReplaceException;
import com.topvision.ems.epon.onu.dao.OnuReplaceDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuReplaceEntry;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onu.service.OnuReplaceService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Rod John
 * @created @2016-4-18-上午10:55:03
 *
 */
@Service("onuReplaceService")
public class OnuReplaceServiceImpl extends BaseService implements OnuReplaceService {
    private static final Integer REPLACE = 1;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private OnuReplaceDao onuReplaceDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    protected FacadeFactory facadeFactory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onu.service.OnuReplaceService#replaceOnuEntityByMac(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.Integer)
     */
    @Override
    public void replaceOnuEntityByMac(Long entityId, Long onuId, Long onuIndex, String onuMac, Integer forceReplace) {
        OnuReplaceEntry onuReplaceEntry = new OnuReplaceEntry();
        onuReplaceEntry.setOnuIndex(onuIndex);
        onuReplaceEntry.setTopOnuModifyMacAddress(onuMac);
        boolean deleteOnuResult = true;
        try {
            if (forceReplace == REPLACE) {
                OltOnuAttribute onuAttribute = onuReplaceDao.getOnuAttributeByMacAddress(entityId, onuMac);
                if (onuAttribute != null) {
                    // GET ONU AUTH TYPE
                    OltAuthentication auth = onuAuthService.getOltAuthenticationByIndex(entityId,
                            onuAttribute.getOnuIndex());
                    onuAuthService.deleteAuthenPreConfig(entityId, onuAttribute.getPonId(), onuAttribute.getOnuIndex(),
                            auth.getAuthType());
                }
            }
        } catch (Exception e) {
            deleteOnuResult = false;
            logger.error("Force Replace Onu error:", e);
        }
        try {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            OnuFacade onuFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuFacade.class);
            onuFacade.replaceOnuEntry(snmpParam, onuReplaceEntry);
            syncOnuInfoAfterReplace(entityId, onuIndex, onuId, onuMac, null, null);
        } catch (SnmpSetException e) {
            if (!deleteOnuResult) {
                OnuForceReplaceException onuForceReplaceException = new OnuForceReplaceException(e);
                throw onuForceReplaceException;
            } else {
                throw e;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onu.service.OnuReplaceService#replaceOnuEntityBySn(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public void replaceOnuEntityBySn(Long entityId, Long onuId, Long onuIndex, String sn, String pwd,
            Integer forceReplace) {
        OnuReplaceEntry onuReplaceEntry = new OnuReplaceEntry();
        onuReplaceEntry.setOnuIndex(onuIndex);
        onuReplaceEntry.setTopOnuModifyLogicSn(sn);
        // if pwd is not null, the auth mode is sn+pwd
        onuReplaceEntry.setTopOnuModifyPwd(pwd);
        boolean deleteOnuResult = true;
        try {
            if (forceReplace == REPLACE) {
                OltOnuAttribute onuAttribute = onuReplaceDao.getOnuAttributeBySn(entityId, sn);
                if (onuAttribute != null) {
                    OltAuthentication auth = onuAuthService.getOltAuthenticationByIndex(entityId,
                            onuAttribute.getOnuIndex());
                    onuAuthService.deleteAuthenPreConfig(entityId, onuAttribute.getPonId(), onuAttribute.getOnuIndex(),
                            auth.getAuthType());
                }
            }
        } catch (Exception e) {
            deleteOnuResult = false;
            logger.error("Force Replace Onu error:", e);
        }
        try {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            OnuFacade onuFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuFacade.class);
            onuFacade.replaceOnuEntry(snmpParam, onuReplaceEntry);
            syncOnuInfoAfterReplace(entityId, onuIndex, onuId, null, sn, pwd);
        } catch (SnmpSetException e) {
            if (!deleteOnuResult) {
                OnuForceReplaceException onuForceReplaceException = new OnuForceReplaceException(e);
                throw onuForceReplaceException;
            } else {
                throw e;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onu.service.OnuReplaceService#getOnuMacListByEntityId(java.lang.Long)
     */
    @Override
    public Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId) {
        return onuReplaceDao.getOnuMacListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onu.service.OnuReplaceService#getOnuSnListByEntityId(java.lang.Long)
     */
    @Override
    public Map<String, Map<String, Object>> getOnuSnListByEntityId(Long entityId) {
        return onuReplaceDao.getOnuSnListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.service.OnuReplaceService#getPonAuthType(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public Integer getPonAuthType(Long entityId, Long onuIndex) {
        Long ponIndex = EponIndex.getPonIndex(onuIndex);
        return onuReplaceDao.getPonAuthType(entityId, ponIndex);
    }

    /**
     * syncOnuInfoAfterReplace
     * 
     * @param onuId
     * @param onuIndex
     * @param entityId
     * @param macAddress
     * @param sn
     * @param pwd
     */
    private void syncOnuInfoAfterReplace(Long entityId, Long onuIndex, Long onuId, String macAddress, String sn,
            String pwd) {
        onuReplaceDao.syncOnuInfoAfterReplace(entityId, onuIndex, onuId, macAddress, sn, pwd);
    }

}
