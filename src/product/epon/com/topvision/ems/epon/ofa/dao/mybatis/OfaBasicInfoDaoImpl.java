package com.topvision.ems.epon.ofa.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.ofa.dao.OfaBasicInfoDao;
import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("ofaBasicInfoDao")
public class OfaBasicInfoDaoImpl extends MyBatisDaoSupport<OfaBasicInfo> implements OfaBasicInfoDao {

    @Override
    public OfaBasicInfo getOfaBasicInfoById(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOfaBasicInfoById"), entityId);
    }

    @Override
    public void batchInsertOrUpdateOfaBasicInfo(List<OfaBasicInfo> ofaBasicInfos) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OfaBasicInfo ofaBasicInfo : ofaBasicInfos) {
                sqlSession.insert(getNameSpace("insertOrUpdateOfaBasicInfo"), ofaBasicInfo);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("batchInsertOrUpdateOfaBasicInfo error", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertOrUpdateOfaBasicInfo(OfaBasicInfo ofaBasicInfo) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOfaBasicInfo"), ofaBasicInfo);
    }

    @Override
    public void updateOfaBasicInfo(OfaBasicInfo ofaBasicInfo) {
        getSqlSession().update(getNameSpace("updateOfaBasicInfo"), ofaBasicInfo);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo";
    }

}
