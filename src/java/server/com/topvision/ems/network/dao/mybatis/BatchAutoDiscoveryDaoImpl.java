/***********************************************************************
 * $Id: BatchAutoDiscoveryDaoImpl.java,v1.0 2014-5-11 上午11:35:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.BatchAutoDiscoveryDao;
import com.topvision.ems.network.domain.AutoDiscoveryPeriodConfig;
import com.topvision.ems.network.domain.BatchAutoDiscoveryEntityType;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.BatchAutoDiscoveryPeriod;
import com.topvision.ems.network.domain.BatchAutoDiscoverySnmpConfig;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.IpSegmentInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2014-5-11-上午11:35:38
 * 
 */
@Repository("batchAutoDiscoveryDao")
public class BatchAutoDiscoveryDaoImpl extends MyBatisDaoSupport<Entity> implements BatchAutoDiscoveryDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.BatchAutoDiscoveryIps";
    }

    @Override
    public List<BatchAutoDiscoveryIps> queryAllDiscoveryIps(Map<String,Object>queryMap) {
        return this.getSqlSession().selectList(getNameSpace("queryAllDiscoveryIps"),queryMap);
    }

    @Override
    public IpSegmentInfo queryDeviceInfoByIps(List<String> ipList) {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("ipList", ipList);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("queryDeviceInfoByIps"), map);
    }

    @Override
    public List<EntitySnap> queryDeviceByIpSegment(List<String> ipList) {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("list", ipList);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectList(getNameSpace("queryDeviceByIpSegment"), map);
    }

    @Override
    public List<AutoDiscoveryPeriodConfig> getAutoDiscoveryPeriodConfigs() {
        return getSqlSession().selectList(getNameSpace("getAutoDiscoveryPeriodConfigs"));
    }

    @Override
    public List<BatchAutoDiscoveryIps> getAutoDiscoveryIps() {
        return getSqlSession().selectList(getNameSpace("getAutoDiscoveryIps"));
    }

    @Override
    public List<BatchAutoDiscoverySnmpConfig> getAutoDiscoverySnmpConfigs() {
        return getSqlSession().selectList(getNameSpace("getAutoDiscoverySnmpConfigs"));
    }

    @Override
    public List<String> getAutoDiscoverySysObjectId() {
        return getSqlSession().selectList(getNameSpace("getAutoDiscoverySysObjectId"));
    }

    @Override
    public List<BatchAutoDiscoveryEntityType> queryAllTypeIds() {
        return getSqlSession().selectList(getNameSpace("queryAllTypeIds"));
    }

    @Override
    public List<BatchAutoDiscoverySnmpConfig> querySnmpConfigs() {
        return getSqlSession().selectList(getNameSpace("querySnmpConfigs"));
    }

    @Override
    public BatchAutoDiscoveryPeriod queryPeriod() {
        return getSqlSession().selectOne(getNameSpace("queryPeriod"));
    }

    @Override
    public void addNetSegment(BatchAutoDiscoveryIps ips) {
        getSqlSession().insert(getNameSpace("insertNetSegmet"), ips);
    }

    @Override
    public void modifyNetSegment(BatchAutoDiscoveryIps ips) {
        getSqlSession().update(getNameSpace("updateNetSegment"), ips);
    }

    @Override
    public void batchDeleteNetSegment(List<Long> ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        getSqlSession().delete(getNameSpace("batchDeleteNetSegment"), map);
    }

    @Override
    public void addSnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig) {
        getSqlSession().insert(getNameSpace("insertSnmpConfig"), snmpConfig);
    }

    @Override
    public void modifySnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig) {
        getSqlSession().update(getNameSpace("updateSnmpConfig"), snmpConfig);
    }

    @Override
    public void deleteSnmpConfig(Long id) {
        getSqlSession().delete(getNameSpace("deleteSnmpConfig"), id);
    }

    @Override
    public void updateLastAutoDiscoveryTime(BatchAutoDiscoveryIps batchAutoDiscoveryIps) {
        getSqlSession().update(getNameSpace("updateLastAutoDiscoveryTime"), batchAutoDiscoveryIps);
    }

    @Override
    public void modifyTypeIds(List<Long> typeIds) {
        //先删后加
        getSqlSession().delete(getNameSpace("deleteTypeIds"));
        for (Long typeId : typeIds) {
            getSqlSession().insert(getNameSpace("insertTypeId"), typeId);
        }
    }

    @Override
    public void modifyPeriod(BatchAutoDiscoveryPeriod period) {
        getSqlSession().update(getNameSpace("updatePeriod"), period);
    }

    @Override
    public List<BatchAutoDiscoveryIps> getIpsByIds(List<Long> ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        return getSqlSession().selectList(getNameSpace("getIpsByIds"), map);
    }

    @Override
    public BatchAutoDiscoveryIps queryNetSegmentById(Long id) {
        return getSqlSession().selectOne(getNameSpace("queryNetSegmentById"), id);
    }

    @Override
    public BatchAutoDiscoverySnmpConfig querySnmpConfigById(Long id) {
        return getSqlSession().selectOne(getNameSpace("querySnmpConfigById"), id);
    }

    @Override
    public void updateAutoDiscovery(BatchAutoDiscoveryIps batchAutoDiscoveryIps) {
        getSqlSession().update(getNameSpace("updateAutoDiscovery"), batchAutoDiscoveryIps);
    }
}