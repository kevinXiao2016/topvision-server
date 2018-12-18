package com.topvision.ems.cmc.ccmts.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.dao.IpSegmentDisplayDao;
import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository("ipsegmentDisplayDao")
public class IpsegmentDisplayDaoImpl extends MyBatisDaoSupport<CmcAttribute> implements IpSegmentDisplayDao {

    @Override
    public List<CmcAttribute> getDeviceListByIpSegment(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<CmcAttribute> list = getSqlSession().selectList(getNameSpace("queryCmcListByIpsegment"), params);
        return list;
    }

    @Override
    public Integer getDeviceListNum(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("queryDeviceNum"), params);
    }

    @Override
    public List<CmcAttribute> getDeviceListByFolder(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<CmcAttribute> list = getSqlSession().selectList(getNameSpace("queryCmcListByFolder"), params);
        return list;
    }

    @Override
    public Integer getDeviceListNumByFolder(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("queryDeviceNumByFolder"), params);
    }

    @Override
    public List<EntitySnap> queryDeviceByFolderList(Long folderId) {
        return this.getSqlSession().selectList(getNameSpace("queryDeviceByFolderId"), folderId);
    }

    @Override
    public List<EntitySnap> queryNoAreaDevice() {
        return this.getSqlSession().selectList(getNameSpace("queryNoAreaDevice"));
    }

    @Override
    protected String getDomainName() {
        return CmcAttribute.class.getName();
    }

    @Override
    public TopoFolderDisplayInfo queryAreaDeviceTotalInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("queryAreaDeviceTotalInfo"), map);
    }

    @Override
    public TopoFolderDisplayInfo queryTopoDisplayInfo(Long folderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("folderTable", "t_entity_" + folderId);
        return this.getSqlSession().selectOne(getNameSpace("queryTopoDisplayInfo"), map);
    }

    @Override
    public TopoFolderDisplayInfo queryNoAreaTotalInfo() {
        return this.getSqlSession().selectOne(getNameSpace("queryNoAreaTotalInfo"));
    }

}
