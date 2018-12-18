package com.topvision.ems.epon.onu.action;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.quartz.SchedulerException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.topvision.ems.epon.onu.constants.OnuConstants;
import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.domain.OnuOnOffRecord;
import com.topvision.ems.epon.onu.service.OnuOnOffRecordCollectService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * 网管支持Onu上下线记录查询
 * 
 * @author w1992wishes
 * @created @2017年6月8日-下午2:00:09
 *
 */
@Controller("onuOnOffRecordAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuOnOffRecordAction extends BaseAction {

    private static final long serialVersionUID = 2309610852258648936L;

    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private OnuOnOffRecordCollectService onuOnOffRecordCollectService;
    
    // 上下线记录自动采集的时间，格式为00:00
    private String autoCollectTime;
    private Long onuId;
    private Long onuIndex;
    private Long entityId;
    
    /**
     * 显示ONU上下线记录自动采集时间配置页面
     * 
     * @return
     */
    public String showOnOffRecordConfig() {
        Properties properties = systemPreferencesService.getModulePreferences(OnuConstants.ON_OFF_RECORD_COLLECT_MOUDLE);
        autoCollectTime = properties.getProperty(OnuConstants.ON_OFF_RECORD_COLLECT_TIME);
        return SUCCESS;
    }

    /**
     * 保存ONU上下线记录自动采集的时间
     * 
     * @return
     * @throws ParseException 
     * @throws SchedulerException 
     */
    public String saveOnOffRecordConfig() throws SchedulerException, ParseException {
        onuOnOffRecordCollectService.resetCollectTrigger(autoCollectTime);
        return NONE;
    }

    /**
     * 跳转到ONU 上下线记录 列表页面
     * 
     * @return
     */
    public String showOnOffRecords(){
        return SUCCESS;
    }
    
    /**
     * 加载上下线记录
     * 
     * @return
     */
    public String loadOnOffRecords(){
        Map<String, Object> paramMap = new HashMap<String, Object> ();
        paramMap.put("onuId", onuId);
        paramMap.put("limit", limit);
        paramMap.put("start", start);
        
        List<OnuOnOffRecord> onOffRecords = onuOnOffRecordCollectService.getOnuOnOffRecords(paramMap);
        int rowCount = onuOnOffRecordCollectService.getRecordCounts(onuId);
        
        JSONObject json = new JSONObject();
        json.put("rowCount", rowCount);
        json.put("data", onOffRecords);
        writeDataToAjax(json);
        
        return NONE;
    }
    
    /**
     * 实时刷新
     * 
     * @return
     */
    public String refreshOnOffRecords(){
        OnuEventLogEntry onuEventLogEntry = new OnuEventLogEntry();
        onuEventLogEntry.setEntityId(entityId);
        onuEventLogEntry.setOnuIndex(onuIndex);
        onuEventLogEntry.setOnuId(onuId);
        String message;
        try{
            onuOnOffRecordCollectService.refreshOnOffRecords(onuEventLogEntry);
            message = "success";
        }catch(Exception e){
            message = e.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        writeDataToAjax(message);
        return NONE;
    }
    
    public String getAutoCollectTime() {
        return autoCollectTime;
    }

    public void setAutoCollectTime(String autoCollectTime) {
        this.autoCollectTime = autoCollectTime;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
