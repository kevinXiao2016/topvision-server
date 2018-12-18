package com.topvision.ems.epon.onu.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.constants.OnuConstants;
import com.topvision.ems.epon.onu.dao.OnuOnOffRecordDao;
import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.domain.OnuOnOffRecord;
import com.topvision.ems.epon.onu.facade.OnuOnOffRecordFacade;
import com.topvision.ems.epon.onu.job.OnuOnOffRecordCollectJob;
import com.topvision.ems.epon.onu.service.OnuOnOffRecordCollectService;
import com.topvision.ems.epon.onu.util.OnuOnOffRecordUtil;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * 
 * @author w1992wishes
 * @created @2017年6月12日-下午2:33:06
 *
 */
@Service("onuOnOffRecordCollectService")
public class OnuOnOffRecordCollectServiceImpl extends BaseService implements OnuOnOffRecordCollectService,
        OnuSynchronizedListener {

    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private OnuOnOffRecordDao onuOnOffRecordDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltService oltService;
    @Autowired
    protected MessageService messageService;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void start() {
        try {
            newCollectJob();
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    };

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexlList = event.getOnuIndexList();
        if (OnuConstants.ONU_SINGLE_TOPO == onuIndexlList.size()) {
            OnuEventLogEntry onuEventLogEntry = new OnuEventLogEntry();
            onuEventLogEntry.setEntityId(entityId);
            onuEventLogEntry.setOnuIndex(onuIndexlList.get(0));
            try {
                refreshOnOffRecords(onuEventLogEntry);
            } catch (Exception e) {
                logger.error("refresh onu onoff records occur error!", e);
            }
        } else if (onuIndexlList.size() > OnuConstants.ONU_SINGLE_TOPO) {
            try {
                refreshOnOffRecordsAll(entityId);
            } catch (Exception e) {
                logger.error("refresh all onu onoff records occur error!", e);
            }
        }
    }

    @Override
    public void resetCollectTrigger(String autoCollectTime) throws SchedulerException, ParseException {

        Properties properties = systemPreferencesService
                .getModulePreferences(OnuConstants.ON_OFF_RECORD_COLLECT_MOUDLE);
        // 修改前的定时采集时间即旧值
        String oldAutoCollectTime = properties.getProperty(OnuConstants.ON_OFF_RECORD_COLLECT_TIME);

        // 第一次访问定时采集页面，数据库尚未有记录，设置默认时间
        if (oldAutoCollectTime == null) {
            oldAutoCollectTime = OnuConstants.DEFAULT_COLLECTED_TIME;
        }

        // 新旧值相同直接返回
        if (autoCollectTime.equalsIgnoreCase(oldAutoCollectTime)) {
            return;
        }

        // 构建新的preferences，更新采集时间
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences onOffRecordPreference = new SystemPreferences();
        onOffRecordPreference.setName(OnuConstants.ON_OFF_RECORD_COLLECT_TIME);
        onOffRecordPreference.setValue(autoCollectTime);
        onOffRecordPreference.setModule(OnuConstants.ON_OFF_RECORD_COLLECT_MOUDLE);
        preferences.add(onOffRecordPreference);

        // 新旧值不同更改任务触发器
        TriggerKey triggerKey = new TriggerKey(OnuConstants.ONU_ONOFFRECORD_COLLECT_TRIGGER_NAME,
                OnuConstants.ONU_ONOFFRECORD_COLLECT_TRIGGER_GROUP);
        CronTriggerImpl trigger = (CronTriggerImpl) schedulerService.getTrigger(triggerKey);
        // 如果job存在则修改定期执行时间，如果不存在则创建一个新的job
        if (trigger != null) {
            trigger.setCronExpression(bulidCronExpression(autoCollectTime));
            schedulerService.modifyJobTrigger(trigger);
        } else {
            newCollectJob();
        }

        // 新旧值不同并更新数据库
        systemPreferencesService.savePreferences(preferences);
    }

    /**
     * 触发job
     * 
     * @throws SchedulerException
     */
    public void newCollectJob() throws SchedulerException {
        Properties properties = systemPreferencesService
                .getModulePreferences(OnuConstants.ON_OFF_RECORD_COLLECT_MOUDLE);
        String autoCollectTime = properties.getProperty(OnuConstants.ON_OFF_RECORD_COLLECT_TIME);
        autoCollectTime = autoCollectTime == null ? OnuConstants.DEFAULT_COLLECTED_TIME : autoCollectTime;

        JobDetail job = newJob(OnuOnOffRecordCollectJob.class).withIdentity(
                OnuConstants.ONU_ONOFFRECORD_COLLECT_JOB_NAME, OnuConstants.ONU_ONOFFRECORD_COLLECT_JOB_GROUP).build();

        // 为任务绑定参数
        // job.getJobDataMap().put(EntityService.class.getSimpleName(), entityService);
        // job.getJobDataMap().put(FacadeFactory.class.getSimpleName(), facadeFactory);
        job.getJobDataMap().put(OnuOnOffRecordCollectService.class.getSimpleName(), this);
        job.getJobDataMap().put(OltService.class.getSimpleName(), oltService);

        Trigger trigger = newTrigger()
                .withIdentity(OnuConstants.ONU_ONOFFRECORD_COLLECT_TRIGGER_NAME,
                        OnuConstants.ONU_ONOFFRECORD_COLLECT_TRIGGER_GROUP).forJob(job)
                .withSchedule(cronSchedule(bulidCronExpression(autoCollectTime))).build();

        schedulerService.scheduleJob(job, trigger);
    }

    /**
     * 执行任务的时间表达式，这里是 每天 某时某分 执行
     * 
     * @param autoCollectTime
     */
    public String bulidCronExpression(String autoCollectTime) {
        String[] time = autoCollectTime.split(":");
        StringBuilder sb = new StringBuilder();
        sb.append("0 ").append(time[1]).append(" ").append(time[0]).append(" * * ?");
        return sb.toString();
    }

    @Override
    public List<OnuOnOffRecord> getOnuOnOffRecords(Map<String, Object> param) {
        return onuOnOffRecordDao.getOnuOnOffRecords(param);
    }

    @Override
    public int getRecordCounts(Long onuId) {
        return onuOnOffRecordDao.getRecordCounts(onuId);
    }

    @Override
    public void refreshOnOffRecords(OnuEventLogEntry onuEventLogEntry) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuEventLogEntry.getEntityId());
        refreshOnOffRecords(snmpParam, onuEventLogEntry);
    }

    @Override
    public void refreshOnOffRecords(SnmpParam snmpParam, OnuEventLogEntry onuEventLogEntry) {
        // 从设备获取上下线历史记录对应的字节
        OnuEventLogEntry logEntry = getOnuOnOffRecordFacade(snmpParam.getIpAddress()).getOnuOnOffRecords(snmpParam,
                onuEventLogEntry);
        // 解析并封装成上下线记录
        List<OnuOnOffRecord> onuOnOffRecords = OnuOnOffRecordUtil.parseOnuEventLogList(logEntry);
        // 插入或者更新数据库
        onuOnOffRecordDao.batchInsertOrUpdateOnuOnOffRecords(onuOnOffRecords, onuEventLogEntry.getEntityId());
    }

    @Override
    public void refreshOnOffRecordsAll(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OnuEventLogEntry> onuEventLogEntries = getOnuOnOffRecordFacade(snmpParam.getIpAddress())
                .getOnuOnOffRecordsAll(snmpParam);
        List<OnuOnOffRecord> onuOnOffRecords = new ArrayList<OnuOnOffRecord>();
        for (OnuEventLogEntry onuEventLogEntry : onuEventLogEntries) {
            onuOnOffRecords.addAll(OnuOnOffRecordUtil.parseOnuEventLogList(onuEventLogEntry));
        }
        onuOnOffRecordDao.batchInsertOrUpdateOnuOnOffRecords(onuOnOffRecords, entityId);
    }

    @Override
    public void deleteOnuOnOffRecords() {
        onuOnOffRecordDao.deleteOnuOnOffRecords();
    }

    private OnuOnOffRecordFacade getOnuOnOffRecordFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuOnOffRecordFacade.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

}
