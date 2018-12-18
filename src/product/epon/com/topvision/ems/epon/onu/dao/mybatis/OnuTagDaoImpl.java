package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuTagDao;
import com.topvision.ems.epon.onu.domain.OnuTag;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("onuTagDao")
public class OnuTagDaoImpl extends MyBatisDaoSupport<OnuTag> implements OnuTagDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.platform.domain.OnuTag";
    }

    @Override
    public List<OnuTag> selectOnuTags(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectOnuTags"), queryMap);
    }

    @Override
    public Integer selectOnuTagsCount() {
        return getSqlSession().selectOne(getNameSpace("selectOnuTagsCount"));
    }

}
