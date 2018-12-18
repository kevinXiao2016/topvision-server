/***********************************************************************
 * $Id: MOltDaoImpl.java,v1.0 2016年7月16日 下午4:55:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.mobile.dao.MOltDao;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.MobileOlt;
import com.topvision.ems.mobile.domain.MobileOnu;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2016年7月16日-下午4:55:07
 *
 */
@Repository("mOltDao")
public class MOltDaoImpl extends MyBatisDaoSupport<Object> implements MOltDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.mobile.domain.MobileOlt";
    }

    @Override
    public List<MobileOlt> queryOltList(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryOltList"), paramMap);
    }

    @Override
    public Integer queryOltListCount(Map<String, Object> paramMap) {
        paramMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryOltListCount"), paramMap);
    }

    @Override
    public MobileOlt queryOltBaseInfo(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("queryOltBaseInfo"), entityId);
    }

    @Override
    public List<CmtsInfo> queryOltCmtsList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getNameSpace("queryOltCmtsList"), paramMap);
    }

    @Override
    public Integer queryOltCmtsCount(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getNameSpace("queryOltCmtsCount"), paramMap);
    }

    @Override
    public List<MobileOnu> queryOltOnuList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getNameSpace("queryOltOnuList"), paramMap);
    }

    @Override
    public Integer queryOltOnuCount(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getNameSpace("queryOltOnuCount"), paramMap);
    }


    @Override
    public List<Long> getPonOnuIndex(Long entityId, Long ponIndex) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("ponIndex", ponIndex);
        return getSqlSession().selectList(getNameSpace("getPonOnuIndex"), param);
    }

    @Override
    public List<MobileOnu> queryOltOnuListWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryOltOnuListWithRegion"), map);
    }

    @Override
    public Integer queryOltOnuCountWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryOltOnuCountWithRegion"), map);
    }
    
    @Override
    public Integer queryOltOnuOnlineCountWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryOltOnuOnlineCountWithRegion"), map);
    }

    @Override
    public List<CmtsInfo> queryOltCmtsListWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryOltCmtsListWithRegion"), map);
    }

    @Override
    public Integer queryOltCmtsCountWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryOltCmtsCountWithRegion"), map);
    }

    @Override
    public BaiduMapInfo getBaiduMapInfo(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getBaiduMapEntityInfo"),entityId);
    }

    @Override
    public void modifyEntityLocation(Map<String, Object> map) {
        getSqlSession().insert(getNameSpace("updateEntityLocation"), map);
    }

    @Override
    public void saveMapDataToDB(Map<String, Object> map) {
        getSqlSession().insert(getNameSpace("insertOrUpdataMapInfoEntity"), map);
    }

    @Override
    public Integer queryOltCmtsOnlineCountWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryOltCmtsOnlineCountWithRegion"), map);
    }

}
