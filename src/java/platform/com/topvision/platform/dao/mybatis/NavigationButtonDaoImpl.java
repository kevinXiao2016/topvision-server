package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.NavigationButtonDao;
import com.topvision.platform.domain.NavigationButton;

@Repository("navigationButtonDao")
public class NavigationButtonDaoImpl extends MyBatisDaoSupport<NavigationButton> implements NavigationButtonDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return NavigationButton.class.getName();
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.platform.dao.NavigationButtonDao#selectNaviByName(java.lang.String)
     */
    public NavigationButton selectNaviByName(String name) {
        return getSqlSession().selectOne(getNameSpace("selectNaviByName"), name);
    }
}
