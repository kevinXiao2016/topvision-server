package com.topvision.ems.cmc.perf.engine.mybatis;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.topvision.ems.cmc.perf.engine.CmFlapDao;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.framework.common.MacUtils;

public class CmFlapDaoImp extends EngineDaoSupport<CMFlapHis> implements CmFlapDao {
    @Override
    public void batchInsertCmFlapHis(List<CMFlapHis> allFlap, Long entityId) {
        if (allFlap == null || allFlap.size() == 0) {
            return;
        }

        SqlSession sqlSession = getBatchSession();
        try {
            for (CMFlapHis flap : allFlap) {
                flap.setCmMac(MacUtils.convertToMaohaoFormat(flap.getCmMac()));
                flap.setCollectTime(new Timestamp(System.currentTimeMillis()));
                // 通过cm的mac获取cmId
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", entityId);
                map.put("maclong", new MacUtils(flap.getCmMac()).longValue());
                Long cmId = (Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByMap", map);
                if (cmId != null) {
                    flap.setCmId(cmId);
                    sqlSession.insert(getNameSpace() + "insertCmFlapHis", flap);
                }

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
    protected String getDomainName() {
        return "com.topvision.ems.cmc.flap.domain.CmFlapHis";
    }
}
