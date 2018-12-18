package com.topvision.ems.epon.onu.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.service.OnuOnOffRecordCollectService;
import com.topvision.framework.scheduler.AbstractJob;

/**
 * 
 * 定时采集ONU上下线记录 的 quartz job
 * 
 * @author w1992wishes
 * @created @2017年6月12日-下午2:03:45
 *
 */
public class OnuOnOffRecordCollectJob extends AbstractJob {

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        try {
            // 先获取需要的参数
            OnuOnOffRecordCollectService onuOnOffRecordCollectService = getService(OnuOnOffRecordCollectService.class);
            OltService oltService = getService(OltService.class);

            // 获取所有的有onu的olt
            List<Long> entityIdList = oltService.queryEntityIdOfOlt();
            // 单个olt下所有onu上下线记录一起更新
            for (Long entityId : entityIdList) {
                onuOnOffRecordCollectService.refreshOnOffRecordsAll(entityId);
            }
            // 定时清除一段时间前的数据记录
            onuOnOffRecordCollectService.deleteOnuOnOffRecords();
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
