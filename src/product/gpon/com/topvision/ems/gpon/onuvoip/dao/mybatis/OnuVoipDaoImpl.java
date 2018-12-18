/***********************************************************************
 * $Id: OnuVoipDaoImpl.java,v1.0 2017年5月4日 上午11:26:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onuvoip.dao.OnuVoipDao;
import com.topvision.ems.gpon.onuvoip.domain.TopOnuIfPotsInfo;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuPots;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:26:30
 *
 */
@Repository("onuVoipDao")
public class OnuVoipDaoImpl extends MyBatisDaoSupport<GponOnuAttribute> implements OnuVoipDao {
    @Autowired
    private OnuDao onuDao;

    @Override
    protected String getDomainName() {
        return "OnuVoip";
    }

    @Override
    public void batchInsertOrUpdateTopVoIPLineStatus(Long entityId, List<TopVoIPLineStatus> topVoIPLineStatusList) {
        SqlSession session = getBatchSession();
        try {
            for (TopVoIPLineStatus status : topVoIPLineStatusList) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, status.getOnuIndex());
                status.setOnuId(onuId);
                status.setEntityId(entityId);
                session.insert(getNameSpace("insertOrUpdateTopVoIPLineStatus"), status);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateTopSIPPstnUser(Long entityId, List<TopSIPPstnUser> topSIPPstnUserList) {
        SqlSession session = getBatchSession();
        try {
            for (TopSIPPstnUser user : topSIPPstnUserList) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, user.getOnuIndex());
                user.setOnuId(onuId);
                user.setEntityId(entityId);
                session.insert(getNameSpace("insertOrUpdateTopSIPPstnUser"), user);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public TopVoIPLineStatus getTopVoIPLineStatus(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("getTopVoIPLineStatus"), onuId);
    }

    @Override
    public void updateTopSIPPstnUser(TopSIPPstnUser topSIPPstnUser) {
        getSqlSession().update(getNameSpace("updateTopSIPPstnUser"), topSIPPstnUser);
    }

    @Override
    public void batchInsertOrUpdateTopOnuIfPotsInfo(Long entityId, List<TopOnuIfPotsInfo> potsInfoList) {
        SqlSession session = getBatchSession();
        try {
            for (TopOnuIfPotsInfo potsInfo : potsInfoList) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, potsInfo.getOnuIndex());
                potsInfo.setOnuId(onuId);
                potsInfo.setEntityId(entityId);
                session.insert(getNameSpace("insertOrUpdateTopOnuIfPotsInfo"), potsInfo);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertOrUpdateTopOnuIfPotsInfo(Long entityId, TopOnuIfPotsInfo topOnuIfPotsInfo) {
        Long onuId = onuDao.getOnuIdByIndex(entityId, topOnuIfPotsInfo.getOnuIndex());
        topOnuIfPotsInfo.setOnuId(onuId);
        topOnuIfPotsInfo.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("insertOrUpdateTopOnuIfPotsInfo"), topOnuIfPotsInfo);
    }

    @Override
    public List<TopGponOnuPots> getGponOnuPotsList(Long onuId) {
        return getSqlSession().selectList("getGponOnuPotsList", onuId);
    }

    @Override
    public TopSIPPstnUser getTopSIPPstnUser(HashMap<String, Object> map) {
        return getSqlSession().selectOne("getTopSIPPstnUser", map);
    }

    @Override
    public void updatePotsAdminStatus(Long onuId, Integer topSIPPstnUserPotsIdx, Integer potsAdminStatus) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("topOnuIfPotsPotsIdx", topSIPPstnUserPotsIdx);
        paramMap.put("topOnuIfPotsAdminState", potsAdminStatus);
        getSqlSession().update(getNameSpace() + "updatePotsAdminStatusByOnuIdAndPotsIdx", paramMap);

    }

}
