package com.topvision.ems.cmts.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmts.dao.CmtsChannelDao;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author YangYi
 * @created @2013-10-10-上午10:42:21
 * 
 */
@Repository("cmtsChannelDao")
public class CmtsChannelDaoImpl extends MyBatisDaoSupport<Entity> implements CmtsChannelDao {

    @Override
    public List<CmtsUpLinkPort> selectUpLinkPortList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("selectUpLinkPortList"), cmcId);
    }

    public List<ChannelPerfInfo> selectCmtsChannelPerfInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("selectCmtsChannelPerfInfoList"), cmcId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmts.domain.CmtsChannel";
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> selectUpChannelBaseShowInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("selectUpChannelBaseShowInfoList"), cmcId);
    }

}
