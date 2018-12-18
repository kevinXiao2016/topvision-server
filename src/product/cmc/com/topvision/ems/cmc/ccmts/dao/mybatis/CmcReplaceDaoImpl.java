/***********************************************************************
 * $Id: CmcReplaceDaoImpl.java,v1.0 2016-4-18 下午6:15:56 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao;
import com.topvision.ems.cmc.ccmts.domain.CmcReplaceInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016-4-18-下午6:15:56
 *
 */
@Repository("cmcReplaceDao")
public class CmcReplaceDaoImpl extends MyBatisDaoSupport<Entity> implements CmcReplaceDao {
    @Autowired
    private EntityDao entityDao;

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.ccmts.domain.CmcReplaceEntry";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao#getCmcMacListByEntityId(java.lang.Long)
     */
    public Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId) {
        List<Entity> list = getSqlSession().selectList(getNameSpace() + "getOnuMacListByEntityId", entityId);
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Entity entity : list) {
            StringBuilder tmp = new StringBuilder(entity.getName()).append("(")
                    .append(EponIndex.getOnuStringByIndex(Long.parseLong(entity.getLocation()))).append(")");
            Map<String, Object> info = new HashMap<>();
            info.put("name", tmp.toString());
            info.put("typeId", entity.getTypeId());
            result.put(entity.getMac(), info);
        }
        result.remove("00:00:00:00:00:00");
        return result;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao#getCmcAttributeByMacAddress(java.lang.Long, java.lang.String)
     */
    @Override
    public CmcAttribute getCmcAttributeByMacAddress(Long entityId, String mac) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("mac", mac);
        return getSqlSession().selectOne(getNameSpace() + "getCmcAttributeByMacAddress", params);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao#modifyEntityIpAndMac(java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public void modifyEntityIpAndMac(Long entityId, String ip, String mac) {
        // Update Entity Info
        entityDao.replaceEntity(entityId, ip, mac);
        // Update Cmc Info
        CmcAttribute cmcAttribute = new CmcAttribute();
        cmcAttribute.setCmcId(entityId);
        cmcAttribute.setTopCcmtsSysMacAddr(mac);
        getSqlSession().update(getNameSpace() + "updateCmcMac", cmcAttribute);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao#modifyEntityMac(java.lang.Long, java.lang.String)
     */
    @Override
    public void modifyEntityMac(Long entityId, String mac) {
        // Update Entity Info
        entityDao.updateEntityMac(entityId, mac);
        // Update Cmc Info
        CmcAttribute cmcAttribute = new CmcAttribute();
        cmcAttribute.setCmcId(entityId);
        cmcAttribute.setTopCcmtsSysMacAddr(mac);
        getSqlSession().update(getNameSpace() + "updateCmcMac", cmcAttribute);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao#loadCmcReplaceList(java.lang.Long)
     */
    @Override
    public List<CmcReplaceInfo> loadCmcReplaceList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "loadCmcReplaceList", cmcId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao#syncCmcInfoAfterReplace(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public void syncCmcInfoAfterReplace(Long cmcId, Long onuIndex, Long entityId, String macAddress) {
        // Sync Entity And Cmc Info
        this.modifyEntityMac(cmcId, macAddress);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("onuId", cmcId);
        params.put("onuIndex", onuIndex);
        params.put("entityId", entityId);
        params.put("mac", macAddress);
        params.put("macLong", new MacUtils(macAddress).longValue());
        params.put("status", EponConstants.ONU_STATUS_DOWN);
        // Sync Onu Info
        getSqlSession().update(getNameSpace() + "updateOnuMacAndStatus", params);
        // Sync EntitySnap
        EntitySnap onuSnap = new EntitySnap();
        onuSnap.setEntityId(cmcId);
        onuSnap.setState(false);
        entityDao.updateOnuEntitySnap(onuSnap);
        // Sync OltAuthentication
        getSqlSession().update("com.topvision.ems.epon.onu.domain.OnuReplaceEntry.updateOnuMacAuth", params);
    }

}
