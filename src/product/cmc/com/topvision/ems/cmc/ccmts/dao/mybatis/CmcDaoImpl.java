/***********************************************************************
 * $Id: CmcAclDaoImpl.java,v1.0 2013-5-3 下午01:40:57 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.dao.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmciAttribute;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.domain.User;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author lzs
 * @created @2013-5-3-下午01:40:57
 * 
 */
@Repository("cmcDao")
public class CmcDaoImpl extends MyBatisDaoSupport<CmcAttribute> implements CmcDao {
    private static final Pattern NAME_PATTERN = Pattern
            .compile("^CCMTS_\\d+\\/\\d+/\\d+$|^CMTS_\\d+\\/\\d+/\\d+$|^CC8800_\\d+\\/\\d+/\\d+$|^ONU_\\d+\\/\\d+:\\d+$");
    @Autowired
    private EntityDao entityDao;

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.ccmts.domain.Cmc";
    }

    @Override
    public Long getEntityIdByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "getEntityIdByCmcId", cmcId);
    }

    @Override
    public CmcAttribute getCmcAttributeByCmcId(Long cmcId) {
        HashMap<String, Long> params = new HashMap<String, Long>();
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        // 防止系统级的调用没有用户信息导致查询的cmcattribute为多条，默认给admin用户
        if (userId == null) {
            userId = User.ADMINISTRATOR_ID;
        }
        params.put("cmcId", cmcId);
        params.put("userId", userId);
        return getSqlSession().selectOne(getNameSpace() + "getCmcAttributeByCmcId", params);
    }

    @Override
    public Long getCmcIdByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getCmcIdByEntityId", entityId);
    }

    @Override
    public Integer getCmcTypeByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "getCmcTypeByCmcId", cmcId);
    }

    @Override
    public Long getCmcIndexByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "getCmcIndexByCmcId", cmcId);
    }

    @Override
    public List<CmcAttribute> getDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getDeviceListItem", map);
    }

    @Override
    public Long getCmcIdByOnuId(Long onuId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcIdByOnuId", onuId);
    }

    @Override
    public Map<Long, String> getCmcNotRelated() {
        // TODO 需要测试
        return getSqlSession().selectMap(getNameSpace() + "getCmcNotRelated", "cmcEntityId");
    }

    public List<Cmc> getCmcList(Long onuId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcByOnuId", onuId);
    }

    @Override
    public void relateCmcToOnu(Long onuId, Long cmcId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("onuId", onuId);
        params.put("cmcEntityId", cmcId);
        getSqlSession().update(getNameSpace() + "relateCmcToOnu", params);
    }

    @Override
    public Long getOnuIdByCmcId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getOnuIdByCmcId", cmcId);
    }

    @Override
    public boolean isCmcExistsInTopo(Long cmcId, Long folderId) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("cmcId", cmcId);
        params.put("folderId", folderId);
        Long count = (Long) getSqlSession().selectOne(getNameSpace() + "isCmcExistsInTopo", params);
        return count > 0;
    }

    @Override
    public List<Long> getCmcIdByFolder(Long folderId, Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("folderId", folderId);
        map.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getCmcIdByFolder", map);
    }

    @Override
    public void batchInsertCcSplitSystemInfo(CmcAttribute cmcAttribute) {
        Long cmcId = (Long) getSqlSession().selectOne(getNameSpace() + "getCmcAttribute", cmcAttribute.getCmcId());
        if (cmcId != null) {
            getSqlSession().insert(getNameSpace() + "updateCmcAttribute", cmcAttribute);
        } else {
            // getSqlSession().insert(getNameSpace() + "batchInsertCcSplitSystemInfo",
            // cmcAttribute);
        }
    }

    @Override
    public CmcEntityRelation getCmcEntityRelationByCmcId(Long cmcId) {
        return (CmcEntityRelation) getSqlSession().selectOne(getNameSpace() + "getCmcEntityRelationByCmcId", cmcId);
    }

    @Override
    public List<Long> getCmcIdsByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcIdsByEntityId", entityId);
    }

    @Override
    public void modifyOnuName(Long onuId, String onuName) {
        /**
         * entity表中的name的类型是varchar(64),设备命令行的名称长度允许255，所以做一下截位。 即使是64个字符，网管前台显示也不好看，应考虑其他限制。
         */
        if (onuName != null && onuName.length() >= 64) {
            onuName = onuName.substring(0, 64);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onuId", onuId);
        map.put("onuName", onuName);
        // Modify By Rod 修改Entity表的name
        map.put("entityId", onuId);
        map.put("name", onuName);
        getSqlSession().update(getNameSpace() + "modifyOnuName", map);
        // Modify By Rod 广州需求设备别名
        String cmcName = (String) getSqlSession().selectOne(getNameSpace() + "getCmcNameByCmcId", map);
        /*
         * if (cmcName == null || "".equals(cmcName) || isCmcNameDefault(cmcName) ||
         * cmcName.startsWith(CmcAttribute.DEFAULT_ONU_NAME_PRE)) { entityDao.renameEntity(onuId,
         * onuName); }
         */
        if (cmcName == null || "".equals(cmcName) || isCmcNameDefault(cmcName)) {
            entityDao.renameEntity(onuId, onuName);
        }
    }

    /**
     * 判断名称是否属于默认，如果是会用topCcmtsSysName进行替换
     * 
     * @param cmcName
     * @return
     */
    private boolean isCmcNameDefault(String cmcName) {
        /*
         * for (String defaultName : CmcAttribute.DEFAULT_CC_NAME_PRE) { if
         * (cmcName.startsWith(defaultName)) { return true; } }
         */
        Matcher matcher = NAME_PATTERN.matcher(cmcName);
        if (matcher != null) {
            return matcher.matches();
        }
        return false;
    }

    @Override
    public List<Long> getCmcIdListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcIdListByEntityIdExt", entityId);
    }

    @Override
    public String getCmcNameByMac(String cmcMacString) {
        return (String) getSqlSession().selectOne(getNameSpace() + "getCmcNameByMac", cmcMacString);
    }

    @Override
    public Long getCmcMacByEntityIdAndChannelIndex(Long entityId, Long channelIndex) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("entityId", entityId);
        params.put("channelIndex", channelIndex);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcMacByEntityIdAndChannelIndex", params);
    }

    @Override
    public boolean isCmcChannel(Long ifIndex) {
        List<Long> cmcIdList = new ArrayList<Long>();
        cmcIdList = getSqlSession().selectList(getNameSpace() + "getCmcIdListByChannelIfindex", ifIndex);
        if (cmcIdList != null && cmcIdList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateCcmtsBasicInfo(CmcSystemBasicInfo cmcSystemBasicInfo) {
        getSqlSession().update(getNameSpace() + "updateCcmtsBasicInfo", cmcSystemBasicInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.dao.CmcDao#getFpgaSpecificationById(java.lang.Long)
     */
    @Override
    public CmcFpgaSpecification getFpgaSpecificationById(Long cmcId) {

        return getSqlSession().selectOne(getNameSpace() + "getFPGASpecInfoByCmcId", cmcId);
    }

    @Override
    public void updateStatusChangeTime(Long cmcId, Timestamp time) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("statusChangeTime", time);
        getSqlSession().update(getNameSpace() + "updateStatusChangeTime", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.dao.CmcDao#getOnuIndexByCmcId(java.lang.Long)
     */
    @Override
    public Long getOnuIndexByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuIndexByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.dao.CmcDao#getCmcIdByTrapInfo(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Entity getCmcByTrapInfo(String ip, String address) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ip", ip);
        map.put("address", address);
        return getSqlSession().selectOne(getNameSpace() + "getCmcByTrapInfo", map);
    }

    @Override
    public SubDeviceCount querySubCountInfo(Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("querySubCountInfo"), map);
    }

    @Override
    public void updateCmcBaseInfo(CmcBfsxSnapInfo cmcSnapInfo) {
        this.getSqlSession().update(getNameSpace("updateCmcBaseInfo"), cmcSnapInfo);
    }

    @Override
    public List<CmcUpChannelBaseInfo> getCmcUpChannelBaseInfosForDiscovery(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcUpChannelBaseInfosForDiscovery", cmcId);
    }

    @Override
    public List<CmcDownChannelBaseInfo> getCmcDownChannelBaseInfosForDiscovery(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDownChannelBaseInfosForDiscovery", cmcId);
    }

    public void batchInsertCmcSystemIpInfo(final CmcSystemIpInfo cmcSystemIpInfo, final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteCmcSystemIpInfo", entityId);
        getSqlSession().insert(getNameSpace() + "insertCmcSystemIpInfo", cmcSystemIpInfo);
    }

    @Override
    public void updateCmcMac(Long cmcId, String topCcmtsSysMacAddr) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("mac", topCcmtsSysMacAddr);
        getSqlSession().update(getNameSpace() + "modifyCmcMac", map);
    }

    @Override
    public Long getCmcIdByEntityIdAndCmcIndex(Long entityId, Long cmcIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("cmcIndex", cmcIndex);
        return getSqlSession().selectOne(getNameSpace("getCmcIdByEntityIdAndCmcIndex"), map);
    }

    @Override
    public List<CmcEntityRelation> getCmcEntityRelationByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getCmcEntityRelationByEntityId"), entityId);
    }

    @Override
    public void updateCmcStatus(Long cmcId, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("status", status);
        getSqlSession().update(getNameSpace() + "updateCmcStatus", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.dao.CmcDao#getAllCmcForAutoClean()
     */
    @Override
    public List<CmcAttribute> getAllCmcForAutoClean() {
        return getSqlSession().selectList(getNameSpace() + "getAllCmcForAutoClean");
    }

    @Override
    public List<CmciAttribute> getCmciForAutoClean() {
        return getSqlSession().selectList(getNameSpace() + "getCmciForAutoClean");
    }

    @Override
    public Integer getCmcClearTime(Long deviceId) {
        return getSqlSession().selectOne(getNameSpace() + "selectCmcCMclearTime", deviceId);
    }

    @Override
    public void updateCmcClearTime(Integer time, Long cmcId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("time", time);
        getSqlSession().insert(getNameSpace("insertOrupdateCmcCmClearTime"), map);
    }

    @Override
    public List<CmcAttribute> getContainOptDorCmcList() {
        return getSqlSession().selectList(getNameSpace() + "getContainOptDorCmcList");
    }

}
