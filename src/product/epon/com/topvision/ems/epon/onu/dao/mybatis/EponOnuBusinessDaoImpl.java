package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.EponOnuBusinessDao;
import com.topvision.ems.epon.onu.domain.EponOnuBusinessInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository
public class EponOnuBusinessDaoImpl extends MyBatisDaoSupport<EponOnuBusinessInfo> implements EponOnuBusinessDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.EponOnuBusinessInfo";
    }

    @Override
    public List<EponOnuBusinessInfo> selectEponOnuBusinessList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectEponOnuBusinessList"), queryMap);
    }

    @Override
    public Integer selectEponOnuBusinessCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectEponOnuBusinessCount"), queryMap);
    }

}
