package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.NetworkDao;
import com.topvision.ems.network.domain.EntityAvailableStat;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.PortPerfEx;
import com.topvision.ems.network.domain.StateStat;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository("networkDao")
public class NetworkDaoImpl extends MyBatisDaoSupport<Object> implements NetworkDao {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#getDeviceDelayTop(java.util.Map)
     */
    @Override
    // FIXME Network.getDeviceDelayTop没有参数
    public List<EntitySnap> getDeviceDelayTop(Long type) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("entityType", type);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getDeviceDelayTop"), map);
    }

    @Override
    public List<EntitySnap> getDeviceDelayList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getDeviceDelayList"), map);
    }

    @Override
    public Integer getDeviceDelayNum(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getDeviceDelayNum"), map);
    }

    @Override
    public List<EntitySnap> getDeviceDelayOut(Long type) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("entityType", type);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getDeviceDelayOut"), map);
    }

    @Override
    public List<EntitySnap> getDeviceDelayOutList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getDeviceDelayOutList"), map);
    }

    @Override
    public Integer getDeviceDelayOutNum(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getDeviceDelayOutNum"), map);
    }

    @Override
    public List<EntitySnap> getDeviceAttentionList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getDeviceAttentionList"), map);
    }

    @Override
    public Integer getDeviceAttentionCount(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getDeviceAttentionNum"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#getNetworkDeviceLoadingTop(java.lang.String)
     */
    @Override
    public List<EntitySnap> getNetworkDeviceLoadingTop(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        if (map.get("target").equals("cpu")) {
            return getSqlSession().selectList(getNameSpace("getDeviceCpuLoadingTop"), map);
        } else if (map.get("target").equals("mem")) {
            return getSqlSession().selectList(getNameSpace("getDeviceMemLoadingTop"), map);
        }
        return null;
    }

    @Override
    public List<EntitySnap> getNetworkDeviceLoadingList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        if (map.get("target").equals("cpu")) {
            return getSqlSession().selectList(getNameSpace("getDeviceCpuLoadingList"), map);
        } else if (map.get("target").equals("mem")) {
            return getSqlSession().selectList(getNameSpace("getDeviceMemLoadingList"), map);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.network.dao.NetworkDao#getNetworkDeviceCount(java.util.Map)
     */
    @Override
    public Integer getNetworkDeviceCount(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getNetWorkDeviceCount"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#getPortFlowTop(java.util.Map)
     */
    @Override
    // FIXME 该方法没有被调用，并且从network.xml中对应sql来看不需要传递参数
    public List<PortPerfEx> getPortFlowTop(Map<String, Long> map) {
        return getSqlSession().selectList(getNameSpace("getPortFlowTop"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#getPortRateTop(java.util.Map)
     */
    @Override
    // FIXME 该方法没有被调用，并且从network.xml中对应sql来看不需要传递参数
    public List<PortPerfEx> getPortRateTop(Map<String, Long> map) {
        return getSqlSession().selectList(getNameSpace("getPortRateTop"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#getServerLoadingTop(java.lang.String)
     */
    @Override
    public List<EntitySnap> getServerLoadingTop(String str) {
        if ("cpu".equals(str)) {
            return getSqlSession().selectList(getNameSpace() + "getCpuLoadingTop", str);
        } else if ("mem".equals(str)) {
            return getSqlSession().selectList(getNameSpace() + "getMemLoadingTop", str);
        } else if ("ping".equals(str)) {
            return getSqlSession().selectList(getNameSpace() + "getDelayingTop", str);
        }

        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#statEntityCountByState()
     */
    @Override
    public List<StateStat> statEntityCountByState() {
        return getSqlSession().selectList(getNameSpace() + "statEntityCountByState");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.NetworkDao#statServerAvailable()
     */
    @Override
    public List<EntityAvailableStat> statServerAvailable() {
        return getSqlSession().selectList(getNameSpace() + "statServerAvailable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.Network";
    }
}
