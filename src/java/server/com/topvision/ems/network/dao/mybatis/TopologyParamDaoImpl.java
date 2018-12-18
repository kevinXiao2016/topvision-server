package com.topvision.ems.network.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Attribute;
import com.topvision.ems.network.dao.TopologyParamDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("topologyParamDao")
public class TopologyParamDaoImpl extends MyBatisDaoSupport<Attribute> implements TopologyParamDao {
    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.TopologyParam";
    }
}
