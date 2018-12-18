/***********************************************************************
 * $Id: OnuCatvConfigDaoImpl.java,v1.0 2016-4-27 上午9:50:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuCatvDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016-4-27-上午9:50:40
 *
 */
@Repository("onuCatvConfigDao")
public class OnuCatvDaoImpl extends MyBatisDaoSupport<Object> implements OnuCatvDao {
    @Autowired
    private OnuDao onuDao;

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.OnuCatvConfig";
    }

    @Override
    public OnuCatvConfig getOnuCatvConfig(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("getOnuCatvConfig"), onuId);
    }

    @Override
    public OnuCatvInfo getOnuCatvInfo(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("getOnuCatvInfo"), onuId);
    }

    @Override
    public void batchInsertOrUpdateOnuCatvConfig(List<OnuCatvConfig> onuCatvConfigList, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (OnuCatvConfig onuCatvConfig : onuCatvConfigList) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, onuCatvConfig.getOnuIndex());
                onuCatvConfig.setOnuId(onuId);
                onuCatvConfig.setEntityId(entityId);
                session.insert(getNameSpace("insertOrUpdateOnuCatvConfig"), onuCatvConfig);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertOrUpdateOnuCatvConfig(OnuCatvConfig config) {
        config.setOnuId(onuDao.getOnuIdByIndex(config.getEntityId(), config.getOnuIndex()));
        getSqlSession().insert(getNameSpace("insertOrUpdateOnuCatvConfig"), config);
    }

}
