package com.topvision.ems.cmts.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmts.dao.CmtsBaseDao;
import com.topvision.ems.cmts.domain.CmtsBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * 
 * @author YangYi
 * @created @2013-10-10-上午10:41:50
 * 
 */
@Repository("cmtsBaseDao")
public class CmtsBaseDaoImpl extends MyBatisDaoSupport<Entity> implements CmtsBaseDao {

    @Override
    public CmcAttribute getCmtsSystemBasicInfoByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getCmtsAttributeByCmcId"), cmcId);
    }

    @Override
    public EntitySnap selectCmtsSnapByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectCmtsSnapByEntityId"), entityId);
    }

    @Override
    public List<CmcAttribute> queryCmtsList(Map<String, Object> cmcQueryMap, int start, int limit) {
        cmcQueryMap.put("start", start);
        cmcQueryMap.put("limit", limit);
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryCmtsList"), cmcQueryMap);

    }

    @Override
    public Long queryCmtsNum(Map<String, Object> cmcQueryMap) {
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return (Long) getSqlSession().selectOne(getNameSpace("queryCmtsNum"), cmcQueryMap);
    }

    @Override
    public void updateCmtsBaseInfo(CmtsBaseInfo cmtsBaseInfo) {
        this.getSqlSession().update(getNameSpace("updateCmtsBaseInfo"), cmtsBaseInfo);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmts.domain.CmtsBase";
    }
}
