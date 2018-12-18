/***********************************************************************
 * $Id: CmDaoImpl.java,v1.0 2013-10-17 上午8:39:02 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.dao.mybatis;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cpe.domain.CmLocateInfo;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-17-上午8:39:02
 * 
 */
@Repository("cmDao")
public class CmDaoImpl extends MyBatisDaoSupport<Entity> implements CmDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.dao.CmDao#selectCmAttributeByCmcId(java.lang .Long)
     */
    @Override
    public List<CmAttribute> selectCmAttributeByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("selectCmAttributeByCmcId"), cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.dao.CmDao#selectCmUsStatusByCmcId(java.lang. Long)
     */
    @Override
    public List<DocsIf3CmtsCmUsStatus> selectCmUsStatusByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("selectCmUsStatusByCmcId"), cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.dao.CmDao#selectChannelCmNum(java.lang.Long, java.lang.Long)
     */
    @Override
    public CmNum selectChannelCmNum(Long entityId, Long channelIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("portIfIndex", channelIndex);
        return getSqlSession().selectOne(getNameSpace("selectChannelCmNum"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.dao.CmDao#selectChannelCmNumHis(java.lang.Long, java.lang.Long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<CmNum> selectChannelCmNumHis(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("portIfIndex", channelIndex);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectChannelCmNumHis"), map);
    }

    @Override
    public Long getEntityIdByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getEntityIdByCmcId"), cmcId);
    }

    @Override
    public List<CmAttribute> getCmListByOlt(Long cmId, Integer start, Integer limit) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectList(getNameSpace("getCmListByOlt"), authorityHashMap);
    }

    @Override
    public Long getCmListByOltCount(Long cmId) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("getCmListByOltCount"), authorityHashMap);
    }

    @Override
    public List<CmAttribute> getCmListByPon(Long cmId, Integer start, Integer limit) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectList(getNameSpace("getCmListByPon"), authorityHashMap);
    }

    @Override
    public Long getCmListByPonCount(Long cmId) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("getCmListByPonCount"), authorityHashMap);
    }

    @Override
    public List<CmAttribute> getCmListByCmc(Long cmId, Integer start, Integer limit) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectList(getNameSpace("getCmListByCmc"), authorityHashMap);
    }

    @Override
    public Long getCmListByCmcCount(Long cmId) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("getCmListByCmcCount"), authorityHashMap);
    }

    @Override
    public List<CmAttribute> getCmListByUpPortId(Long cmId, Integer start, Integer limit) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectList(getNameSpace("getCmListByUpPortId"), authorityHashMap);
    }

    @Override
    public Long getCmListByUpPortIdCount(Long cmId) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("getCmListByUpPortIdCount"), authorityHashMap);
    }

    @Override
    public List<CmAttribute> getCmListByDownPortId(Long cmId, Integer start, Integer limit) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectList(getNameSpace("getCmListByDownPortId"), authorityHashMap);
    }

    @Override
    public Long getCmListByDownPortIdCount(Long cmId) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("getCmListByDownPortIdCount"), authorityHashMap);
    }

    @Override
    public Long getCmNumByStatus(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        // add by fanzidong,在查询之前需要格式化MAC地址
        String mac = (String) map.get("cmMac");
        mac = MacUtils.convertToMaohaoFormat(mac);
        map.put("cmMac", mac);
        return getSqlSession().selectOne(getNameSpace("getCmNumByStatus"), map);
    }

    @Override
    public Long getCmtsCmNumByStatus(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        // add by fanzidong,在查询之前需要格式化MAC地址
        String mac = (String) map.get("cmMac");
        mac = MacUtils.convertToMaohaoFormat(mac);
        map.put("cmMac", mac);
        if (map.get("deviceType") == null) {
            return getSqlSession().selectOne(getNameSpace("getAllCmNumByStatus"), map);
        } else {
            return getSqlSession().selectOne(getNameSpace("getCmtsCmNumByStatus"), map);
        }
    }

    @Override
    public List<CmcQosServiceFlowInfo> getCmServiceFlowListInfo(String cmMac) {
        // add by fanzidong,在查询之前格式化MAC地址
        cmMac = MacUtils.convertToMaohaoFormat(cmMac);
        return getSqlSession().selectList(getNameSpace("getCmServiceFlowListInfo"), cmMac);
    }

    @Override
    public CmTopologyInfo getTopologyInfo(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getTopologyInfo"), cmId);
    }

    @Override
    public Long getDeviceTypeByCmId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getDeviceTypeByCmId"), cmId);
    }

    @Override
    public CmTopologyInfo get8800BTopologyInfo(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("get8800BTopologyInfo"), cmId);
    }

    @Override
    public CmTopologyInfo getCmtsTopologyInfo(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getCmtsTopologyInfo"), cmId);
    }

    @Override
    public Long getUpChannelId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getUpChannelId"), cmId);
    }

    @Override
    public List<Long> getUpChannelIdList(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getUpChannelIdList"), cmId);
    }

    @Override
    public Long getDownChannelId(Long cmId) {
        return (Long) getSqlSession().selectOne(getNameSpace("getDownChannelId"), cmId);
    }

    @Override
    public List<CmAttribute> getCmListByCmcId(Long cmcId, Integer start, Integer limit) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cmcId", cmcId);
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("getCmListByCmcId"), queryMap);
    }

    @Override
    public List<CpeAttribute> getCpeListByCmId(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getCpeListByCmId"), cmId);
    }

    @Override
    public Integer getCpeMaxIpNum(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("realCpeMaxCpe"), cmId);
    }

    @Override
    public Integer getRealIpNum(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getRealIpNum"), cmId);
    }

    @Override
    public Long getCmIndexByMac(String cmMac) {
        return getSqlSession().selectOne("getCmIndexByMac", cmMac);
    }

    @Override
    public Long getEntityIdByMac(String cmMac) {
        // 在查询之前，需要对MAC地址进行格式化
        cmMac = MacUtils.convertToMaohaoFormat(cmMac);
        return getSqlSession().selectOne(getNameSpace("getEntityIdByCmMac"), cmMac);
    }

    @Override
    public void updateCmAttribute(CmAttribute cmAttribute) {
        getSqlSession().update(getNameSpace("updateCmAttribute"), cmAttribute);
    }

    @Override
    public CmAttribute getCmAttributeByCmId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getCmAttributeByCmId"), cmId);
    }

    @Override
    public Long getCmcIdByCmId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getCmcIdByCmId"), cmId);
    }

    @Override
    public List<CmAttribute> selectCmListByCmMacs(String macs, Integer start, Integer limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("macs", macs);
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("selectCmListByCmMacs"), map);
    }

    @Override
    public List<CmCpe> queryCmCpeList(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getCmCpeList"), cmId);
    }

    @Override
    public List<CmStaticIp> queryCmStaticIpList(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getCmStaticIpList"), cmId);
    }

    @Override
    public List<DocsIf3CmtsCmUsStatus> queryDocsIf3CmtsCmUsStatusList(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getDocsIf3CmtsCmUsStatusList"), cmId);
    }

    @Override
    public List<CmAct> selectCmActionInfo(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectCmActionInfo"), map);
    }

    @Override
    public List<CpeAct> selectCpeActionInfoByCmMac(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectCpeActionInfoByCmMac"), map);
    }

    @Override
    public void deleteCmCpe(Long cmId, String cpeMac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmId", cmId);
        // modify by fanzidong,格式化MAC地址
        map.put("topCmCpeMacAddress", MacUtils.convertToMaohaoFormat(cpeMac));
        getSqlSession().delete(getNameSpace("deleteCmCpeInfo"), map);
    }

    @Override
    public void updateCmStatusValue(Long cmcId, String mac, int statusValue) {
        CmAttribute cm = new CmAttribute();
        cm.setCmcId(cmcId);
        cm.setStatusMacAddress(mac);
        cm.setStatusValue(statusValue);
        getSqlSession().update(getNameSpace("updateCmStatusValue"), cm);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cm.domain.Cm";
    }

    @Override
    public CmAttribute selectCmAttributeByCmId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmAttributeByCmId"), cmId);
    }

    @Override
    public void insertOrUpdateCmSignal(CmStatus cmStatus) {
        CmSignal cmSignal = getSqlSession().selectOne(getNameSpace("getCmSignalByCmId"), cmStatus);
        if (cmSignal != null) {
            getSqlSession().update(getNameSpace("updateCmSignal"), cmStatus);
        } else {
            getSqlSession().insert(getNameSpace("insertCmSignal"), cmStatus);
        }
    }

    @Override
    public List<Long> queryEntityIdByCmMac(String cmMac) {
        return getSqlSession().selectList(getNameSpace("queryEntityIdByCmMac"), cmMac);
    }

    @Override
    public List<CmLocateInfo> queryCmLocate(Long entityId, Long ccIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("ccIndex", ccIndex);
        return getSqlSession().selectList(getNameSpace("queryCmLocate"), map);
    }

    @Override
    public Long selectcmcIdByEntityIdAndCmmac(Long entityId, String cmmac) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("maclong", new MacUtils(cmmac).longValue());
        return getSqlSession().selectOne(getNameSpace("selectcmcIdByEntityIdAndCmmac"), map);
    }

    @Override
    public void updateUpChannelSnr(CmAttribute cmAttribute) {
        getSqlSession().update(getNameSpace("updateUpChannelSnr"), cmAttribute);
    }

    @Override
    public CmAttribute getPreviousStateById(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getPreviousStateById"), cmId);
    }

    @Override
    public void insertOrUpdateCmSignal(CmSignal cmSingal) {
        CmSignal oldCmSignal = getSqlSession().selectOne(getNameSpace("getCm2SignalByCmId"), cmSingal);
        cmSingal.setCollectTime(new Timestamp(System.currentTimeMillis()));
        if (oldCmSignal != null) {
            getSqlSession().update(getNameSpace("updateCm2Signal"), cmSingal);
        } else {
            getSqlSession().insert(getNameSpace("insertCm2Signal"), cmSingal);
        }
    }

    @Override
    public void insertOrUpdateCm3Signal(Cm3Signal cm3Signal) {
        Cm3Signal oldCmSignal = getSqlSession().selectOne(getNameSpace("getCm3SignalByCmId"), cm3Signal);
        cm3Signal.setCollectTime(new Timestamp(System.currentTimeMillis()));
        if (oldCmSignal != null) {
            getSqlSession().update(getNameSpace("updateCm3Signal"), cm3Signal);
        } else {
            getSqlSession().insert(getNameSpace("insertCm3Signal"), cm3Signal);
        }
    }

    @Override
    public CmSignal selectCmSignal(Long cmId) {
        CmSignal cmSingal = new CmSignal();
        cmSingal.setCmId(cmId);
        return getSqlSession().selectOne(getNameSpace("getCm2SignalByCmId"), cmSingal);
    }

    @Override
    public List<Cm3Signal> selectUpChannelCm3Signal(Long cmId, Long channelType) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("cmId", cmId);
        map.put("channelType", channelType);
        return getSqlSession().selectList(getNameSpace("selectCm3SignalByChannel"), map);
    }

    @Override
    public void deleteCmSignal(Long cmId) {
        getSqlSession().delete(getNameSpace("deleteCmSignal"), cmId);
    }

    @Override
    public void deleteCm3Signal(Long cmId) {
        getSqlSession().delete(getNameSpace("deleteCm3Signal"), cmId);
    }

    @Override
    public List<Cm3Signal> getUpChannelSignalByCmId(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getUpChannelSignalByCmId"), cmId);
    }

    @Override
    public List<Cm3Signal> getDownChannelSignalByCmId(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getDownChannelSignalByCmId"), cmId);
    }
    
    @Override
    public void updateStatusValue(Long cmId, Integer statusValue) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cmId", cmId);
        queryMap.put("statusValue", statusValue);
        getSqlSession().update(getNameSpace("updateStatusValue"), queryMap);
    }

    @Override
    public void updateCmConfigFile(Long cmId, String docsDevServerConfigFile) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmId", cmId);
        map.put("fileName", docsDevServerConfigFile);
        getSqlSession().update(getNameSpace("updateCmConfigFile"), map);
    }

    @Override
    public void deleteCmCleared(List<Long> cmId) {
        for(Long cmid:cmId){
            getSqlSession().delete(getNameSpace("deleteClearedCmInCmAttribute"), cmid);
            getSqlSession().delete(getNameSpace("deleteClearedCmInCmcCmRelation"), cmid);
        }        
    }

    @Override
    public void deleteCmClearedOne(Long cmId) {
        getSqlSession().delete(getNameSpace("deleteClearedCmInCmAttribute"), cmId);
        getSqlSession().delete(getNameSpace("deleteClearedCmInCmcCmRelation"), cmId);
    }

}