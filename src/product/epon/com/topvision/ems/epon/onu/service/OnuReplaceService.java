/***********************************************************************
 * $Id: OnuReplaceService.java,v1.0 2016-4-18 上午10:16:09 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.Map;

import com.topvision.framework.service.Service;

/**
 * @author Rod John
 * @created @2016-4-18-上午10:16:09
 *
 */
public interface OnuReplaceService extends Service {

    /**
     * replaceOnuEntity (MAC)
     * 
     * @param entityId
     * @param onuId
     * @param onuIndex
     * @param onuMac
     * @param forceReplace
     */
    void replaceOnuEntityByMac(Long entityId, Long onuId, Long onuIndex, String onuMac, Integer forceReplace);

    /**
     * replaceOnuEntity (SN/SN+PWD)
     * 
     * if sn=null  auth:sn
     * if sn!=null auth:sn+pwd
     * 
     * @param entityId
     * @param onuId
     * @param onuIndex
     * @param onuMac
     * @param forceReplace
     */
    void replaceOnuEntityBySn(Long entityId, Long onuId, Long onuIndex, String sn, String pwd, Integer forceReplace);

    /**
     * getOnuMacListByEntityId
     * 
     * @param entityId
     * @return
     */
    Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId);
    
    
    /**
     * getOnuSnListByEntityId
     * 
     * @param entityId
     * @return
     */
    Map<String, Map<String, Object>> getOnuSnListByEntityId(Long entityId);

    /**
     * getPonAuthType
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    Integer getPonAuthType(Long entityId, Long onuIndex);

}
