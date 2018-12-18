/***********************************************************************
 * $Id: OnuPerfDaoImpl.java,v1.0 2015-4-24 上午10:15:21 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.performance.dao.OnuPerfDao;
import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.epon.performance.domain.PerfOnuQualityHistory;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2015-4-24-上午10:15:21
 *
 */
@Repository("onuPerfDao")
public class OnuPerfDaoImpl extends MyBatisDaoSupport<Object> implements OnuPerfDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo";
    }

    @Override
    public void insertOnuOnlineStatus(OnuOnlineResult onlineResult) {
        this.getSqlSession().insert(getNameSpace("insertOnuOnlineStatus"), onlineResult);
    }

    @Override
    public void insertOnuLinkQuality(OnuLinkQualityResult linkQuality) {
        this.getSqlSession().insert(getNameSpace("insertOnuLinkQuality"), linkQuality);
    }

    @Override
    public List<Long> queryUniIndexByOnuId(Long onuId) {
        return this.getSqlSession().selectList(getNameSpace("queryUniIndexByOnuId"), onuId);
    }

    @Override
    public List<Long> queryPonIndexByOnuId(Long onuId) {
        return this.getSqlSession().selectList(getNameSpace("queryPonIndexByOnuId"), onuId);
    }

    @Override
    public void batchInsertOnuFlowQuality(List<OnuFlowCollectInfo> flowList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OnuFlowCollectInfo flowPerf : flowList) {
                sqlSession.insert(getNameSpace("insertOnuFlowQuality"), flowPerf);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertOrUpdateLinkInfo(OnuLinkCollectInfo linkInfo) {
        getSqlSession().insert(getNameSpace("insertOrUpdateLinkInfo"), linkInfo);
    }

    @Override
    public void insertOrUpdateCatvInfo(OnuCatvInfo onuCatvInfo) {
        getSqlSession().insert(getNameSpace("insertOrUpdateCatvInfo"), onuCatvInfo);
    }

    @Override
    public Long getMonitorIdByIdentifyKeyAndCategory(Long identifyKey, String category) {
        Map<String, String> map = new HashMap<>();
        map.put("identifyKey", "" + identifyKey);
        map.put("category", category);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getMonitorIdByIdentifyKeyAndCategory", map);
    }

    @Override
    public PerfOnuQualityHistory queryMinPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo) {
        return getSqlSession().selectOne(getNameSpace("queryMinPonRevPower"), onuLinkCollectInfo);
    }

    @Override
    public PerfOnuQualityHistory queryMaxPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo) {
        return getSqlSession().selectOne(getNameSpace("queryMaxPonRevPower"), onuLinkCollectInfo);
    }

    @Override
    public PerfOnuQualityHistory queryMinCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo) {
        return getSqlSession().selectOne(getNameSpace("queryMinCATVRevPower"), onuLinkCollectInfo);
    }

    @Override
    public PerfOnuQualityHistory queryMaxCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo) {
        return getSqlSession().selectOne(getNameSpace("queryMaxCATVRevPower"), onuLinkCollectInfo);
    }

    @Override
    public void insertOrUpdateMinReceivedPower(PerfOnuQualityHistory perfOnuQualityHistory) {
        getSqlSession().insert(getNameSpace("insertOrUpdateMinReceivedPower"), perfOnuQualityHistory);
    }
}
