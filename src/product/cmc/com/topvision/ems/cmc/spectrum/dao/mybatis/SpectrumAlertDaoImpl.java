/***********************************************************************
 * $Id: SpectrumAlertDao.java,v1.0 2015年3月11日 下午2:55:34 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.spectrum.dao.SpectrumAlertDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author YangYi
 * @created @2015年3月11日-下午2:55:34
 * 
 */
@Repository("spectrumAlertDao")
public class SpectrumAlertDaoImpl extends MyBatisDaoSupport<Entity> implements SpectrumAlertDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.spectrum.domain.SpectrumAlert";
    }

    /**
     * 判断频谱噪声告警是否已经存在
     */
    @Override
    public boolean isSpectrumAlertExist(String source) {
        Long alertId = getSqlSession().selectOne(getNameSpace("getAlertBysource"), source);
        return alertId != null;
    }

}
