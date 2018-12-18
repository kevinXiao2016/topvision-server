/***********************************************************************
 * $Id: OnuReplaceDaoImpl.java,v1.0 2016-4-18 上午11:31:02 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.OnuReplaceDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onuauth.domain.OltAuthenSnInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016-4-18-上午11:31:02
 *
 */
@Repository("onuReplaceDao")
public class OnuReplaceDaoImpl extends MyBatisDaoSupport<Entity> implements OnuReplaceDao {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OnuDao onuDao;

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.OnuReplaceEntry";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.OnuReplaceDao#getOnuMacListByEntityId(java.lang.Long)
     */
    @Override
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
        result.remove(null);
        return result;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.OnuReplaceDao#getOnuSnListByEntityId(java.lang.Long)
     */
    @Override
    public Map<String, Map<String, Object>> getOnuSnListByEntityId(Long entityId) {
        List<OltAuthenSnInfo> list = getSqlSession().selectList(getNameSpace() + "getOnuSnListByEntityId", entityId);
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (OltAuthenSnInfo snInfo : list) {
            Long onuId = onuDao.getOnuIdByIndex(entityId, snInfo.getOnuIndex());
            Entity onuEntity = entityDao.selectByPrimaryKey(onuId);
            StringBuilder tmp = new StringBuilder(onuEntity.getName()).append("(")
                    .append(EponIndex.getOnuStringByIndex(snInfo.getOnuIndex())).append(")");
            Map<String, Object> info = new HashMap<>();
            info.put("name", tmp.toString());
            info.put("typeId", onuEntity.getTypeId());
            result.put(snInfo.getTopOnuAuthLogicSn(), info);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.OnuReplaceDao#getOnuAttributeByMacAddress(java.lang.Long, java.lang.String)
     */
    @Override
    public OltOnuAttribute getOnuAttributeByMacAddress(Long entityId, String mac) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("mac", mac);
        return getSqlSession().selectOne(getNameSpace() + "getOnuAttributeByMacAddress", params);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.OnuReplaceDao#getPonAuthType(java.lang.Long, java.lang.Long)
     */
    @Override
    public Integer getPonAuthType(Long entityId, Long ponIndex) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("entityId", entityId);
        params.put("ponIndex", ponIndex);
        return getSqlSession().selectOne(getNameSpace() + "getPonAuthType", params);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.OnuReplaceDao#syncOnuInfoAfterReplace(java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void syncOnuInfoAfterReplace(Long entityId, Long onuIndex, Long onuId, String macAddress, String sn,
            String pwd) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("onuId", onuId);
        params.put("onuIndex", onuIndex);
        params.put("entityId", entityId);
        params.put("mac", macAddress);
        params.put("sn", sn);
        params.put("pwd", pwd);
        if (macAddress != null) {
            // Sync Mac Info
            updateOnuMacAfterReplace(entityId, onuId, macAddress, params);
            // Sync OltAuthentication
            getSqlSession().update(getNameSpace() + "updateOnuSnPwdAuth", params);
            updateOnuStatus(onuId);
        } else if (sn != null) {
            // Sync OltAuthentication
            getSqlSession().update(getNameSpace() + "updateOnuSnPwdAuth", params);
            Entity onuEntity = entityDao.selectByPrimaryKey(onuId);
            if (!onuEntity.getMac().equals("00:00:00:00:00:00")) {
                macAddress = "00:00:00:00:00:00";
                params.put("mac", null);
                // Sync Mac Info
                updateOnuMacAfterReplace(entityId, onuId, macAddress, params);
            }
            updateOnuStatus(onuId);
        }
    }

    private void updateOnuMacAfterReplace(Long entityId, Long onuId, String macAddress, Map<String, Object> params) {
        // Sync Entity
        entityDao.updateEntityMac(onuId, macAddress);
        // Sync OltOnuAttribute
        OltOnuAttribute onuAttribute = new OltOnuAttribute();
        onuAttribute.setOnuId(onuId);
        onuAttribute.setOnuMac(macAddress);
        getSqlSession().update(getNameSpace() + "updateOnuMacAttribute", onuAttribute);
        // Sync OltAuthentication
        getSqlSession().update(getNameSpace() + "updateOnuMacAuth", params);
    }

    private void updateOnuStatus(Long onuId) {
        //更新onu表的在线状态
        onuDao.updateOnuOperationStatus(onuId, EponConstants.ONU_STATUS_DOWN);
        //更新entitysnap表中onu的在线状态
        EntitySnap onuSnap = new EntitySnap();
        onuSnap.setEntityId(onuId);
        onuSnap.setState(false);
        entityDao.updateOnuEntitySnap(onuSnap);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.OnuReplaceDao#getOnuAttributeBySn(java.lang.Long, java.lang.String)
     */
    @Override
    public OltOnuAttribute getOnuAttributeBySn(Long entityId, String sn) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("sn", sn);
        return getSqlSession().selectOne(getNameSpace() + "getOnuAttributeBySn", params);
    }

}
