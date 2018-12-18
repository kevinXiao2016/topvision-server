/***********************************************************************
 * $Id: MOnuDaoImpl.java,v1.0 2016年7月18日 下午5:20:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.dao.mybatis;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.mobile.dao.MOnuDao;
import com.topvision.ems.mobile.domain.MobileOnu;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2016年7月18日-下午5:20:37
 *
 */
@Repository("mOnuDao")
public class MOnuDaoImpl extends MyBatisDaoSupport<Object> implements MOnuDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.mobile.domain.MobileOnu";
    }

    @Override
    public MobileOnu queryOnuBaseInfo(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("queryOnuBaseInfo"), onuId);
    }

    @Override
    public Long getOnuPonIndex(Long entityId, Long onuId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("onuId", onuId);
        params.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace("queryOnuPonIndex"), params);
    }

    @Override
    public OltPonOptical getOnuLinkPonOptical(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("getOnuLinkPonOpticalByOnuId"), onuId);
    }

}
