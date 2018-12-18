/***********************************************************************
 * $Id: RogueOnuDaoImpl.java,v1.0 2017年6月17日 上午9:24:27 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.RogueOnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.TopOnuLaserEntry;
import com.topvision.ems.epon.onu.domain.TopPonPortRogueEntry;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:24:27
 *
 */
@Repository("rogueOnuDao")
public class RogueOnuDaoImpl extends MyBatisDaoSupport<Object> implements RogueOnuDao {

    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OltPonDao oltPonDao;

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.RogueOnu";
    }

    @Override
    public void updateOltRogueSwitch(Long entityId, Integer rogueSwitch) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("systemRogueCheck", rogueSwitch);
        getSqlSession().update(getNameSpace() + "updateOltRogueSwitch", paramMap);
    }

    @Override
    public void batchInsertOrUpdatePonRogueInfo(Long entityId, List<TopPonPortRogueEntry> ponPortRogueList) {
        SqlSession session = getBatchSession();
        try {
            for (TopPonPortRogueEntry topPonPortRogueEntry : ponPortRogueList) {
                topPonPortRogueEntry.setEntityId(entityId);
                Long ponId = oltPonDao.getPonIdByPonIndex(entityId, topPonPortRogueEntry.getPonIndex());
                topPonPortRogueEntry.setPonId(ponId);
                session.insert(getNameSpace("insertOrUpdatePonRogueInfo"), topPonPortRogueEntry);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuLaser(Long entityId, List<TopOnuLaserEntry> onuLaserList) {
        SqlSession session = getBatchSession();
        try {
            for (TopOnuLaserEntry topOnuLaserEntry : onuLaserList) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, topOnuLaserEntry.getOnuIndex());
                topOnuLaserEntry.setOnuId(onuId);
                session.insert(getNameSpace("insertOrUpdateOnuLaser"), topOnuLaserEntry);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateOnuLaserSwitch(Long onuId, Integer onuLaserSwitch) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("laserSwitch", onuLaserSwitch);
        getSqlSession().update(getNameSpace() + "updateOnuLaserSwitch", paramMap);
    }

    @Override
    public void updatePortRogueSwitch(Long ponId, Integer portRogueSwitch) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ponId", ponId);
        paramMap.put("rogueSwitch", portRogueSwitch);
        getSqlSession().update(getNameSpace() + "updatePortRogueSwitch", paramMap);
    }

    @Override
    public void updateOnuRogueStatus(Long onuId, Integer rogueOnu) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("rogueOnu", rogueOnu);
        getSqlSession().update(getNameSpace() + "updateOnuRogueStatus", paramMap);
    }

    @Override
    public void batchUpdateOnuRogueStatus(List<OltOnuAttribute> onuList) {
        SqlSession session = getBatchSession();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("rogueOnu", EponConstants.NO_ROGUE_ONU);
        try {
            for (OltOnuAttribute oltOnuAttribute : onuList) {
                paramMap.put("onuId", oltOnuAttribute.getOnuId());
                session.insert(getNameSpace("updateOnuRogueStatus"), paramMap);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<OltOnuAttribute> queryRogueOnuList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryRogueOnuList"), queryMap);
    }

    @Override
    public int queryRogueOnuCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("queryRogueOnuCount"), queryMap);
    }

    @Override
    public void changeOnuRogueStatus(Long entityId, Long onuIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("onuIndex", onuIndex);
        getSqlSession().update(getNameSpace() + "changeOnuRogueStatus", paramMap);
    }

    @Override
    public void insertOrupdateOnuLaserSwitch(TopOnuLaserEntry topOnuLaserEntry) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOnuLaser"), topOnuLaserEntry);
    }

}
