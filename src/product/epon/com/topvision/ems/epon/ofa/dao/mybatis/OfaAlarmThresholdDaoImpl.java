package com.topvision.ems.epon.ofa.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.ofa.dao.OfaAlarmThresholdDao;
import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author w1992wishes
 * @created @2017年10月13日-上午11:08:19
 *
 */
@Repository("ofaAlarmThresholdDao")
public class OfaAlarmThresholdDaoImpl extends MyBatisDaoSupport<OfaAlarmThreshold> implements OfaAlarmThresholdDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold";
    }

    @Override
    public void batchInsertOrUpdateOfaAlarm(List<OfaAlarmThreshold> ofaAlarmThresholds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OfaAlarmThreshold ofaAlarmThreshold : ofaAlarmThresholds) {
                sqlSession.insert(getNameSpace("insertOrUpdateOfaAlarm"), ofaAlarmThreshold);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("batchInsertOrUpdateOfaAlarm error", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertOrUpdateOfaAlarm(OfaAlarmThreshold ofaAlarmThreshold) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOfaAlarm"), ofaAlarmThreshold);
    }

    @Override
    public OfaAlarmThreshold getOfaAlarmThresholdById(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOfaAlarmThresholdById"), entityId);
    }

}
