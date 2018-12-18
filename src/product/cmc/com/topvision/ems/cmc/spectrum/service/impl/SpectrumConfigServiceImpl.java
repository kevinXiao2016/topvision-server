/***********************************************************************
 * $ SpectrumConfigServiceImpl.java,v1.0 2014-1-4 17:06:03 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.dao.SpectrumConfigDao;
import com.topvision.ems.cmc.spectrum.dao.SpectrumDiscoveryDao;
import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.exception.OltSwitchOffException;
import com.topvision.ems.cmc.spectrum.facade.SpectrumFacade;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.SpectrumMonitorService;
import com.topvision.ems.cmc.spectrum.service.SpectrumRecordingService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author jay
 * @created @2014-1-4-17:06:03
 */

@Service("spectrumConfigService")
public class SpectrumConfigServiceImpl extends CmcBaseCommonService implements SpectrumConfigService {
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcService cmcService;
    @Resource(name = "spectrumConfigDao")
    private SpectrumConfigDao spectrumConfigDao;
    @Resource(name = "systemPreferencesService")
    private SystemPreferencesService systemPreferencesService;
    @Resource(name = "spectrumMonitorService")
    private SpectrumMonitorService spectrumMonitorService;
    @Resource(name = "spectrumCallbackService1S")
    private SpectrumCallbackService1S spectrumCallbackService1S;
    @Resource(name = "spectrumDiscoveryDao")
    private SpectrumDiscoveryDao spectrumDiscoveryDao;
    @Resource(name = "spectrumRecordingService")
    private SpectrumRecordingService spectrumRecordingService;
    @Resource(name = "spectrumFacade")
    private SpectrumFacade spectrumFacade;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    private String seesionId = "";

    /**
     * 开启频谱历史采集
     */
    @Override
    public void startHisVideoSwitch(List<Long> cmcIds, final String dwrId, final String sessionId) {
        for (int i = 0; i < cmcIds.size(); i++) {
            SpectrumOltSwitch s = this.getSpectrumSwitchOlt(cmcIds.get(i)); // 开启传入的CC的历史频谱录像
            if (s.getCollectSwitch() != null && s.getCollectSwitch() == 0) {
                throw new OltSwitchOffException();
            }
        }
        int size = cmcIds.size();
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
        for (int i = 0; i < size; i++) {
            final Long cmcId = cmcIds.get(i);
            threadPool.execute(new Runnable() {
                public void run() {
                    boolean startSuccess = spectrumRecordingService.startHisVideo(cmcId); // 开启传入的CMTS的历史频谱录像
                    Message message = new Message("turnOnHisVideoSwitchResult");
                    CmtsSpectrumConfig cmtsSpectrumConfig = new CmtsSpectrumConfig();
                    if (startSuccess) {
                        cmtsSpectrumConfig.setHisVideoSwitch("5");
                    } else {
                        cmtsSpectrumConfig.setHisVideoSwitch("6");
                    }
                    cmtsSpectrumConfig.setCmcId(cmcId);
                    message.setData(cmtsSpectrumConfig);
                    message.setId(dwrId);
                    message.addSessionId(sessionId);
                    messagePusher.sendMessage(message);
                }
            });
        }
    }

    /**
     * 关闭频谱历史采集
     */
    @Override
    public void stopHisVideoSwitch(List<Long> cmcIds, final String dwrId, final String sessionId) {
        int size = cmcIds.size();
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
        for (int i = 0; i < size; i++) {
            final Long cmcId = cmcIds.get(i);
            threadPool.execute(new Runnable() {
                public void run() {
                    Boolean stopSuccess = spectrumRecordingService.stopHisVideo(cmcId);
                    Message message = new Message("turnOffHisVideoSwitchResult");
                    CmtsSpectrumConfig cmtsSpectrumConfig = new CmtsSpectrumConfig();
                    if (stopSuccess) {
                        cmtsSpectrumConfig.setHisVideoSwitch("6");
                    } else {
                        cmtsSpectrumConfig.setHisVideoSwitch("5");
                    }
                    cmtsSpectrumConfig.setCmcId(cmcId);
                    message.setData(cmtsSpectrumConfig);
                    message.setId(dwrId);
                    message.addSessionId(sessionId);
                    messagePusher.sendMessage(message);
                }
            });
        }
    }

    /**
     * 开启CMTS频谱采集开关
     */
    @Override
    public void startSpectrumSwitchCmts(List<Long> cmcIds) {
        spectrumConfigDao.startSpectrumSwitchCmts(cmcIds);
    }

    /**
     * 关闭CMTS频谱采集开关
     */
    @Override
    public void stopSpectrumSwitchCmts(List<Long> cmcIds) {
        spectrumConfigDao.stopSpectrumSwitchCmts(cmcIds);
    }

    /**
     * 根据CMCID从数据库获取OLT频谱开关状态
     */
    @Override
    public Boolean getOltSwitchStatus(Long cmcId) {
        return spectrumConfigDao.getOltSwitchStatus(cmcId);
    }

    /**
     * 从数据库获取CMTS频谱开关状态
     */
    @Override
    public Boolean getCmcSwitchStatus(Long cmcId) {
        return spectrumConfigDao.getCmtsSwitchStatus(cmcId);
    }

    /**
     * 获取CMTS配置列表
     */
    @Override
    public List<CmtsSpectrumConfig> getCmtsSpectrumConfig(Map<String, Object> map) {
        map.put("type", entityTypeService.getCcmtsType());
        List<CmtsSpectrumConfig> configs = spectrumConfigDao.getCmtsSpectrumConfig(map);
        List<CmtsSpectrumConfig> resultList = new ArrayList<CmtsSpectrumConfig>();
        Boolean isSpectrumSupported = false;
        Boolean isSpectrumIISupported = false;
        for (CmtsSpectrumConfig config : configs) {
            if (entityTypeService.isCcmtsWithAgent(config.getTypeId())) {
                config.setOltCollectSwitch("-1");
            }
            // 判断该设备版本是否支持频谱
            isSpectrumSupported = deviceVersionService.isFunctionSupported(config.getCmcId(), "spectrum");
            isSpectrumIISupported = deviceVersionService.isFunctionSupported(config.getCmcId(), "spectrumII");
            if (isSpectrumSupported || isSpectrumIISupported) {
                resultList.add(config);
            }
        }
        return resultList;
    }

    /**
     * 获取CMTS配置列表条数
     */
    @Override
    public Long getCmtsSpectrumConfigCount(Map<String, Object> map) {
        if (map.containsKey("start")) {
            map.remove("start");
        }
        if (map.containsKey("limit")) {
            map.remove("limit");
        }
        List<CmtsSpectrumConfig> configs = getCmtsSpectrumConfig(map);
        return (long) configs.size();
    }

    /**
     * 获取OLT频谱采集开关列表
     */
    @Override
    public List<SpectrumOltSwitch> getOltSpectrumConfig(Map<String, Object> map) {
        return spectrumConfigDao.getOltSpectrumConfig(map);
    }

    /**
     * 获取OLT频谱采集开关列表数量
     */
    @Override
    public Long getOltSpectrumConfigCount(Map<String, Object> map) {
        return spectrumConfigDao.getOltSpectrumConfigCount(map);
    }

    /**
     * 开启OLT频谱开关
     */
    @Override
    public void startSpectrumSwitchOlt(Long[] entityIds, String dwrId) {
        for (int i = 0; i < entityIds.length; i++) {
            SnmpParam snmpParam = getSnmpParamByEntityId(entityIds[i]);
            try {
                spectrumFacade.startSpectrumSwitchOlt(snmpParam);
                Integer status = 1;
                SpectrumOltSwitch spectrumOltSwitch = new SpectrumOltSwitch();
                spectrumOltSwitch.setEntityId(entityIds[i]);
                spectrumOltSwitch.setCollectSwitch(status);
                spectrumDiscoveryDao.insertSpectrumOltSwitch(spectrumOltSwitch);
            } catch (Exception e) {
                logger.debug("stopSpectrumSwitchOlt failed", e);
            }
            this.pushSpectrumSwitchOltMessage(entityIds[i], dwrId);
        }
    }

    /**
     * 关闭OLT频谱开关
     */
    @Override
    public void stopSpectrumSwitchOlt(Long[] entityIds, String dwrId) {
        for (int i = 0; i < entityIds.length; i++) {
            SnmpParam snmpParam = getSnmpParamByEntityId(entityIds[i]);
            try {
                spectrumFacade.stopSpectrumSwitchOlt(snmpParam);
                Integer status = 0;
                spectrumConfigDao.updateSpectrumSwitchOlt(entityIds[i], status);
            } catch (Exception e) {
                logger.debug("stopSpectrumSwitchOlt failed", e);
            }
            this.pushSpectrumSwitchOltMessage(entityIds[i], dwrId);
        }
    }

    /**
     * 批量刷新OLT频谱开关
     */
    @Override
    public void refreshSpectrumSwitchOlt(Long[] entityIds, String dwrId) {
        for (int i = 0; i < entityIds.length; i++) {
            this.refreshOltSwitchByEntityId(entityIds[i]);
            this.pushSpectrumSwitchOltMessage(entityIds[i], dwrId);
        }
    }

    /**
     * 从设备获取OLT开关开启状态
     */
    @Override
    public Boolean getOltSwtichFromDevice(Long cmcId) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        return this.refreshOltSwitchByEntityId(entityId);
    }

    /**
     * 向页面发送OLT开关状态
     * 
     * @param entityId
     * @param dwrId
     */
    private void pushSpectrumSwitchOltMessage(Long entityId, String dwrId) {
        SpectrumOltSwitch spectrumOltSwitch = this.getSpectrumSwitchOlt(entityId);
        // seesionId,在推送时使用
        seesionId = ServletActionContext.getRequest().getSession().getId();
        if (dwrId != null) {
            Message message = new Message("pushSpectrumSwitchOltMessage");
            message.addSessionId(this.seesionId);
            message.setId(dwrId);
            message.setData(spectrumOltSwitch);
            messagePusher.sendMessage(message);
        }
    }

    /**
     * 获取OLT频谱采集开关状态
     * 
     * @param entityId
     * @return
     */
    private SpectrumOltSwitch getSpectrumSwitchOlt(Long entityId) {
        Integer collectSwitch = spectrumConfigDao.getSpectrumSwitchOlt(entityId);
        SpectrumOltSwitch spectrumOltSwitch = new SpectrumOltSwitch();
        spectrumOltSwitch.setEntityId(entityId);
        spectrumOltSwitch.setCollectSwitch(collectSwitch);
        return spectrumOltSwitch;
    }

    /**
     * 根据entityId获取OLT开关状态
     * 
     * @param entityId
     * @return
     */
    private boolean refreshOltSwitchByEntityId(Long entityId) {
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        boolean b = spectrumFacade.getOltSwitchStatus(snmpParam);
        Integer status = b ? 1 : 0;
        spectrumConfigDao.updateSpectrumSwitchOlt(entityId, status);
        return b;
    }

    /**
     * 保存配置
     */
    @Override
    public void saveSystemPreference(String name, Object value) {
        SystemPreferences preferences = new SystemPreferences();
        preferences.setModule("spectrum");
        preferences.setName(name);
        preferences.setValue(String.valueOf(value));
        systemPreferencesService.savePreferences(preferences);
    }

    @Override
    public Integer getTimeInterval() {
        String tString = getSystemPreferences("timeInterval", "5000");
        return Integer.parseInt(tString);
    }

    @Override
    public Long getTimeLimit() {
        String tString = getSystemPreferences("timeLimit", "3600000");
        return Long.parseLong(tString);
    }

    @Override
    public Long getHisCollectCycle() {
        String tString = getSystemPreferences("hisCollectCycle", "3600000");
        return Long.parseLong(tString);
    }

    @Override
    public Long getHisCollectDuration() {
        String tString = getSystemPreferences("hisCollectDuration", "300000");
        return Long.parseLong(tString);
    }

    @Override
    public Integer getHisTimeInterval() {
        String tString = getSystemPreferences("hisTimeInterval", "300000");
        return Integer.parseInt(tString);
    }

    private String getSystemPreferences(String name, String defaultString) {
        List<SystemPreferences> list = systemPreferencesService.getSystemPreferences("spectrum");
        for (SystemPreferences systemPreferences : list) {
            if (systemPreferences.getName().equalsIgnoreCase(name)) {
                return systemPreferences.getValue();
            }
        }
        return defaultString;
    }

}
