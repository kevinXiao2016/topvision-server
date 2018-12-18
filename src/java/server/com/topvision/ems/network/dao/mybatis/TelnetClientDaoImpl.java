/***********************************************************************
 * $Id: TelnetClientDaoImpl.java,v1.0 2017年1月8日 下午3:57:06 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.TelnetClientDao;
import com.topvision.ems.network.domain.TelnetRecord;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author vanzand
 * @created @2017年1月8日-下午3:57:06
 *
 */
@Repository("telnetClientDao")
public class TelnetClientDaoImpl extends MyBatisDaoSupport<TelnetRecord> implements TelnetClientDao {

    @Override
    public String getDomainName() {
        return TelnetRecord.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.TelnetClientDao#insertRecord(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void insertRecord(String ip, String command, Long userId) {
        TelnetRecord record = new TelnetRecord();
        record.setIp(ip);
        record.setCommand(command);
        record.setUserId(userId);
        getSqlSession().insert(getNameSpace("insertRecord"), record);
    }

    @Override
    public List<TelnetRecord> selectTelnetRecordList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("selectTelnetRecordList"), queryMap);
    }

    @Override
    public Integer selectTelnetRecordCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("selectTelnetRecordCount"), queryMap);
    }

}
