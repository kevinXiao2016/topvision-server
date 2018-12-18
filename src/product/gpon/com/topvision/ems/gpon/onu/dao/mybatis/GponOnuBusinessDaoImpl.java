package com.topvision.ems.gpon.onu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.gpon.onu.dao.GponOnuBusinessDao;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onu.domain.GponOnuBusinessInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * 
 * @author CWQ
 * @created @2017年12月25日-下午2:30:17
 *
 */
@Repository("gponOnuBusinessDao")
public class GponOnuBusinessDaoImpl extends MyBatisDaoSupport<GponOnuAttribute> implements GponOnuBusinessDao {

    @Override
    public List<GponOnuBusinessInfo> selectGponOnuBusinessList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectGponOnuBusinessList"), queryMap);
    }

    @Override
    public Integer selectGponOnuBusinessCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectGponOnuBusinessCount"), queryMap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.gpon.onu.domain.GponOnuBusinessInfo";
    }

}
