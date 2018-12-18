package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.constants.OnuConstants;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.OnuOnOffRecordDao;
import com.topvision.ems.epon.onu.domain.OnuOnOffRecord;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author w1992wishes
 * @created @2017年6月14日-下午4:26:52
 *
 */
@Repository("onuOnOffRecordDao")
public class OnuOnOffRecordDaoImpl extends MyBatisDaoSupport<Object> implements OnuOnOffRecordDao{
    @Autowired
    private OnuDao onuDao;
    
    @Override
    protected String getDomainName() {
        return OnuConstants.ONU_ONOFFRECORD_DOMAIN_NAME;
    }
    
    @Override
    public List<OnuOnOffRecord> getOnuOnOffRecords(Map<String, Object> param) {
        return getSqlSession().selectList(getNameSpace("getOnuOnOffRecords"), param);
    }

    @Override
    public int getRecordCounts(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("getRecordCounts"), onuId);
    }

    @Override
    public void insertOrUpdateOnuOnOffRecord(OnuOnOffRecord onuOnOffRecord) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOnuOnOffRecord"), onuOnOffRecord);
    }

    @Override
    public void batchInsertOrUpdateOnuOnOffRecords(List<OnuOnOffRecord> onuOnOffRecords, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (OnuOnOffRecord onuOnOffRecord : onuOnOffRecords) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, onuOnOffRecord.getOnuIndex());
                onuOnOffRecord.setOnuId(onuId);
                onuOnOffRecord.setEntityId(entityId);
                session.insert(getNameSpace("insertOrUpdateOnuOnOffRecord"), onuOnOffRecord);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally{
            session.close();
        }
    }
    
    @Override
    public void deleteOnuOnOffRecords(){
        getSqlSession().delete(getNameSpace("deleteOnuOnOffRecord"));
    }

}
