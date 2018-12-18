package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuLinkDao;
import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * 
 * @author CWQ
 * @created @2017年12月21日-下午4:18:05
 *
 */
@Repository
public class OnuLinkDaoImpl extends MyBatisDaoSupport<UniPort> implements OnuLinkDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.OnuLinkInfo";
    }

    @Override
    public List<OnuLinkInfo> selectOnuLinkList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList("selectOnuLinkList", queryMap);
    }

    @Override
    public Integer selectOnuLinkListCount(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne("selectOnuLinkListCount", queryMap);
    }

    @Override
    public List<OnuLinkThreshold> selectOnuLinkThreshold() {
        return getSqlSession().selectList("selectOnuLinkThreshold");
    }

    @Override
    public OnuLinkInfo selectOnuLinkInfo(Long onuId) {
        return getSqlSession().selectOne("selectOnuLinkInfo", onuId);
    }

}
