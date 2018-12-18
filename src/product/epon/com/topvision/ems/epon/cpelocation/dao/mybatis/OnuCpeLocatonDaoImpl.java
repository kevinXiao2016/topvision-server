/***********************************************************************
 * $Id: OnuCpeLocatonDaoImpl.java,v1.0 2016-5-6 上午9:45:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.cpelocation.dao.OnuCpeLocatonDao;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2016-5-6-上午9:45:09
 *
 */
@Repository("onuCpeLocatonDao")
public class OnuCpeLocatonDaoImpl extends MyBatisDaoSupport<Object> implements OnuCpeLocatonDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation";
    }

    @Override
    public void insertOrUpdateCpeLoc(OnuCpeLocation cpeLoc) {
        getSqlSession().insert(getNameSpace("insertOrUpdateCpeLoc"), cpeLoc);
    }

    @Override
    public OnuCpeLocation queryOnuCpeLoc(String cpeMac) {
        return getSqlSession().selectOne(getNameSpace("queryCpeLoc"), cpeMac);
    }

    @Override
    public OnuCpeLocation getOnuCpeRelaInfo(OnuCpeLocation cpeLoc) {
        return super.getSqlSession().selectOne(getNameSpace("queryOnuCpeRelaInfo"), cpeLoc);
    }

}
