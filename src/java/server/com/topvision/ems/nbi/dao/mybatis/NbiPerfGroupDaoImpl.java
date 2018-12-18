/***********************************************************************
 * $Id: NbiPerfGroupDaoImpl.java,v1.0 2016年3月21日 下午2:28:28 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.nbi.dao.NbiPerfGroupDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.performance.nbi.api.NbiFtpConfig;
import com.topvision.performance.nbi.api.PerfGroupRow;

/**
 * @author Bravin
 * @created @2016年3月21日-下午2:28:28
 *
 */
@Repository("nbiPerfGroupDao")
public class NbiPerfGroupDaoImpl extends MyBatisDaoSupport<PerfGroupRow> implements NbiPerfGroupDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.nbi.domain.Nbi";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.nbi.dao.NbiPerfGroupDao#getPerfGroupRow()
     */
    @Override
    public List<PerfGroupRow> getPerfGroupRow() {
        return getSqlSession().selectList("selectPerfGroupRow");
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.nbi.dao.NbiPerfGroupDao#selectNbiFtpConfigParam()
     */
    @Override
    public NbiFtpConfig selectNbiFtpConfigParam() {
        return getSqlSession().selectOne("selectNbiFtpConfigParam");
    }

}
