package com.topvision.ems.epon.onu.constants;

import java.io.Serializable;

/**
 * onu包中用常量
 * 
 * @author w1992wishes
 * @created @2017年6月12日-下午3:31:36
 *
 */
public class OnuConstants implements Serializable{
    
    private OnuConstants(){}
    
    private static final long serialVersionUID = -3351716964415122223L;

    // 自动采集配置在systempreferences表的module字段
    public final static String ON_OFF_RECORD_COLLECT_MOUDLE = "onOffRecordCollect";
    // 自动采集配置时间在systempreferences表的key
    public final static String ON_OFF_RECORD_COLLECT_TIME = "onu.onOffRecord.collectTime";
    
    // 自动采集上下线记录job name
    public final static String ONU_ONOFFRECORD_COLLECT_JOB_NAME = "OnuOnOffRecordCollectJob";
    // 自动采集上下线记录job group
    public final static String ONU_ONOFFRECORD_COLLECT_JOB_GROUP = "OnuOnOffRecordCollectJobGroup";
    // 自动采集上下线记录trigger name
    public final static String ONU_ONOFFRECORD_COLLECT_TRIGGER_NAME = "OnuOnOffRecordCollectTrigger";
    // 自动采集上下线记录trigger group
    public final static String ONU_ONOFFRECORD_COLLECT_TRIGGER_GROUP = "OnuOnOffRecordCollectTriggerGroup";
    // 默认采集时间
    public final static String DEFAULT_COLLECTED_TIME = "03:00";
    
    public final static String ONU_ONOFFRECORD_DOMAIN_NAME = "com.topvision.ems.epon.onu.dao.OnuOnOffRecord";
    
    // 每条上下线记录相应的字节长度
    public final static int BYTE_LENGTH_BY_RECORD = 12;
    // 一个12字节长度的记录包含上线记录，下线记录，下线原因三部分，每部分4字节
    public final static int BYTE_STEP = 4;
    // 16进制
    public final static int HEX_SYSTEM = 16;
    public final static int BYTE_INDEX = 36;
    public final static String NON_TIME = "00000000";
    
    public static final Integer ONU_SINGLE_TOPO = 1;
}
