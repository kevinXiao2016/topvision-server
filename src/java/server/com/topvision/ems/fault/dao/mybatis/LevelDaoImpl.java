package com.topvision.ems.fault.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.LevelDao;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("levelDao")
public class LevelDaoImpl extends MyBatisDaoSupport<Level> implements LevelDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.Level";
    }

}
