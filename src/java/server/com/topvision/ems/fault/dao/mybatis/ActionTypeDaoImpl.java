package com.topvision.ems.fault.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.ActionTypeDao;
import com.topvision.ems.fault.domain.ActionType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("actionTypeDao")
public class ActionTypeDaoImpl extends MyBatisDaoSupport<ActionType> implements ActionTypeDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.ActionTypeDao#getActionTypeByName(java.lang .String)
     */
    @Override
    public ActionType getActionTypeByName(String name) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.ActionType";
    }
}
