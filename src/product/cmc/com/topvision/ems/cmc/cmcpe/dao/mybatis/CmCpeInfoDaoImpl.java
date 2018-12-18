package com.topvision.ems.cmc.cmcpe.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cmcpe.dao.CmCpeInfoDao;
import com.topvision.ems.cmc.cmcpe.domain.CmCpeInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("cmCpeInfoDao")
public class CmCpeInfoDaoImpl extends MyBatisDaoSupport<CmCpeInfo> implements CmCpeInfoDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cmcpe.domain.CmCpeInfo";
    }

    @Override
    public List<CmCpeInfo> selectCmCpeList(Map<String, Object> queryMap) {
        return this.getSqlSession().selectList(getNameSpace("selectCmCpeList"), queryMap);
    }

    @Override
    public Integer selectCmCpeListNum(Map<String, Object> queryMap) {
        return this.getSqlSession().selectOne(getNameSpace("selectCmCpeListNum"), queryMap);
    }

}
