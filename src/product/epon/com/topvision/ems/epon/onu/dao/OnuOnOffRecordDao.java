package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuOnOffRecord;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * 
 * @author w1992wishes
 * @created @2017年6月14日-下午4:01:21
 *
 */
public interface OnuOnOffRecordDao extends BaseEntityDao<Object>{
    /**
     * 获取单个ONU上下线记录
     * 
     * @param ounId
     * @return
     */
    List<OnuOnOffRecord> getOnuOnOffRecords(Map<String, Object> param);
    
    /**
     * 获取单个ONU上下线记录总数
     * 
     * @param onuId
     * @return
     */
    int getRecordCounts(Long onuId);
    
    /**
     * insert into or update onof record
     * 
     * @param onuOnOffRecord
     */
    void insertOrUpdateOnuOnOffRecord(OnuOnOffRecord onuOnOffRecord);
    
    /**
     * batch insert into or update onoff records
     * 
     * @param onuOnOffRecords
     */
    void batchInsertOrUpdateOnuOnOffRecords(List<OnuOnOffRecord> onuOnOffRecords, Long entityId);
    
    /**
     * 定时删除一段时间之前的数据
     */
    void deleteOnuOnOffRecords();
}
