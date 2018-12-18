package com.topvision.ems.mobile.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.mobile.dao.MCmtsDao;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsDownChannel;
import com.topvision.ems.mobile.domain.CmtsDownChannelWithPortId;
import com.topvision.ems.mobile.domain.CmtsInCmtsList;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.CmtsUpChannel;
import com.topvision.ems.mobile.domain.CmtsUpChannelWithPortId;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository("mCmtsDao")
public class MCmtsDaoImpl extends MyBatisDaoSupport<CmtsInfo> implements MCmtsDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.mobile.domain.CmtsInfo";
    }

    @Override
    public CmtsInfo getCmtsInfoById(Long cmtsId) {
        return getSqlSession().selectOne(getNameSpace("getCmtsInfoById"), cmtsId);
    }

    @Override
    public List<CmtsDownChannel> getDownChannelsById(Long cmtsId) {
        return getSqlSession().selectList(getNameSpace("getDownChannelsById"), cmtsId);
    }

    @Override
    public List<CmtsUpChannel> getUpChannelsById(Long cmtsId) {
        return getSqlSession().selectList(getNameSpace("getUpChannelsById"), cmtsId);
    }

    @Override
    public Long getErrorRateInterval(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getErrorRateInterval"), entityId);
    }

    @Override
    public List<CmtsInCmtsList> getCmtsList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getCmtsList"), map);
    }

    @Override
    public Long getCmtsListCount(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getCmtsListCount"), map);
    }

    @Override
    public List<CmtsUpChannelWithPortId> getUpChannelsInfoById(Long cmtsId) {
        return getSqlSession().selectList(getNameSpace("getUpChannelsInfoById"), cmtsId);
    }

    @Override
    public List<CmtsDownChannelWithPortId> getDownChannelsInfoById(Long cmtsId) {
        return getSqlSession().selectList(getNameSpace("getDownChannelsInfoById"), cmtsId);
    }

    @Override
    public void saveMapDataToDB(Map<String, Object> map) {
        getSqlSession().insert(getNameSpace("insertOrUpdataMapInfoOfCc"), map);
    }

    @Override
    public List<CmtsInCmtsList> getCmtsListWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getCmtsListWithRegion"), map);
    }

    @Override
    public Long getCmtsListCountWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getCmtsListCountWithRegion"), map);
    }

    @Override
    public void modifyCmtsLocation(Map<String, Object> map) {
        getSqlSession().insert(getNameSpace("updateCmtsLocation"), map);
    }

    @Override
    public BaiduMapInfo getBaiduMapInfo(Long cmtsId) {
        return getSqlSession().selectOne(getNameSpace("getBaiduMapInfo"),cmtsId);
    }

    @Override
    public Double selectCmtsOptRecPower(Long cmtsId, Long portIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("cmcId", cmtsId);
        map.put("portIndex", portIndex);
        return getSqlSession().selectOne(getNameSpace("selectCmtsOptRecPower"), map);
    }
    
}
