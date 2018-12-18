package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.network.dao.EntityAttributeDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("entityAttributeDao")
public class EntityAttributeDaoImpl extends MyBatisDaoSupport<EntityAttribute> implements EntityAttributeDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityAttributeDao#deleteByGroup(java.lang .Long,
     * java.lang.String)
     */
    @Override
    public void deleteByGroup(Long entityId, String group) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("entityId", entityId.toString());
        param.put("group", group);
        getSqlSession().delete(this.getNameSpace() + "deleteByGroup", param);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.EntityAttribute";
    }
}
