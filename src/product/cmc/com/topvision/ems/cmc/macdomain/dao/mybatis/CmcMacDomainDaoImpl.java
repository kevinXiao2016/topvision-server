/***********************************************************************
 * $Id: CmcMacDomainDaoImpl.java,v1.0 2012-2-13 下午04:20:23 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.macdomain.dao.CmcMacDomainDao;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author zhanglongyang
 * @created @2012-2-13-下午04:20:23
 * 
 */
@Repository("cmcMacDomainDao")
public class CmcMacDomainDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CmcMacDomainDao {
    @Override
    public MacDomainBaseInfo getMacDomainBaseInfo(Long cmcId) {
        return (MacDomainBaseInfo) getSqlSession().selectOne(getNameSpace() + "getMacDomainBaseInfo", cmcId);
    }

    @Override
    public MacDomainStatusInfo getMacDomainStatusInfo(Long cmcId) {
        return (MacDomainStatusInfo) getSqlSession().selectOne(getNameSpace() + "getMacDomainStatusInfo", cmcId);
    }

    public void updateMacDomainBaseInfo(MacDomainBaseInfo macDomainBaseInfo) {
        getSqlSession().update(getNameSpace() + "updateMacDomainBaseInfo", macDomainBaseInfo);
    }

    @Override
    public void insertOrUpdateMacDomainStatusInfo(MacDomainStatusInfo macDomainStatusInfo) {
        if (((Integer) getSqlSession().selectOne(getNameSpace() + "getMacDomainStatusInfo",
                macDomainStatusInfo.getCmcId())) != 0) {
            getSqlSession().update(getNameSpace() + "updateMacDomainStatusInfo", macDomainStatusInfo);
        } else {
            getSqlSession().insert(getNameSpace() + "insertMacDomainStatusInfo", macDomainStatusInfo);
        }
    }

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.macdomain.domain.CmcMacDomain";
    }
}
