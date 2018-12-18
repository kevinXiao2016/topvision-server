package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.CompanyDao;
import com.topvision.platform.domain.Company;

/**
 * 
 * @author Victor
 * @created @2013-11-2-上午10:42:33
 *
 */
@Repository("companyDao")
public class CompanyDaoImpl extends MyBatisDaoSupport<Company> implements CompanyDao {
    @Override
    protected String getDomainName() {
        return Company.class.getName();
    }
}
