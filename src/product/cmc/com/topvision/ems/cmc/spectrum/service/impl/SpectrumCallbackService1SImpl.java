package com.topvision.ems.cmc.spectrum.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.exception.CmtsSwitchOffException;
import com.topvision.ems.cmc.spectrum.exception.OltSwitchOffException;
import com.topvision.ems.cmc.spectrum.service.SpectrumAlertService;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallback;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.template.service.EntityTypeService;

@Service("spectrumCallbackService1S")
public class SpectrumCallbackService1SImpl extends CmcBaseCommonService
        implements SpectrumCallbackService1S, BeanFactoryAware {
    private BeanFactory beanFactory;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Resource(name = "spectrumConfigService")
    private SpectrumConfigService spectrumConfigService;
    @Resource(name = "spectrumAlertService")
    private SpectrumAlertService spectrumAlertService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    private HashMap<Long, SpectrumCallbackCollection> callbackMap = new HashMap<>();

    @Override
    public void start() {
    }

    @Override
    public Long delHisCallback(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        Long callbackId = callbackCollection.removeHisCallback();
        return callbackCollection.removeHisCallback();
    }

    @Override
    public Long addCallback(Long cmcId, SpectrumCallback callback)
            throws OltSwitchOffException, CmtsSwitchOffException {
        Boolean switchStatus = getSwitchStatus(cmcId);
        Long callbackId = -1L;
        if (switchStatus) {
            SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
            callbackCollection.addCallback(callback);
            callbackId = callback.getCallbackId();
        }
        logger.debug("Spectrum addCallback, cmcId = " + cmcId + " && callbackId = " + callbackId
                + " && callback type = " + callback.getClass());
        return callbackId;
    }

    @Override
    public SpectrumVideo delCallback(Long callbackId, Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        return callbackCollection.removeCallback(callbackId);
    }

    @Override
    public boolean isHisVideoCallbackExist(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        return callbackCollection.isHisVideoCallbackExist();
    }

    @Override
    public boolean pushResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list, Long dt) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        // Add by Victor@20160823 判断无接收方，则返回false，后台停止采集
        if (!callbackCollection.isAnyCallbackExist()) {
            logger.info("callbackCollection push data,but has not any Callback,stop collect");
            return false;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("callbackCollection push data  List size = {}", list.size());
        }
        callbackCollection.pushAllResult(entityId, cmcId, startFreq, endFreq, list, dt);
        spectrumAlertService.process(entityId, cmcId, list);
        return true;
    }

    @Override
    public void sendOverTimeMessage(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        callbackCollection.sendOverTimeMessage();
    }

    @Override
    public Long takeHisCallbackId(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        return callbackCollection.takeHisCallbackId();
    }

    @Override
    public Long takeRealtimeCallbackIdByWebCallbackId(Long callbackId, Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        return callbackCollection.takeRealtimeCallbackIdByWebCallbackId(callbackId);
    }

    @Override
    public boolean isSameCallbackExist(SpectrumCallback callback) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(callback.getCmcId());
        return callbackCollection.isSameCallbackExist(callback);
    }

    @Override
    public boolean isOnlyHisVideoCallbackExist(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        return callbackCollection.isOnlyHisVideoCallbackExist();
    }

    @Override
    public void sendHeartbeatTimeOut(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = getCallbackCollection(cmcId);
        callbackCollection.sendHeartbeatTimeOut();
    }

    private Boolean getSwitchStatus(Long cmcId) throws OltSwitchOffException, CmtsSwitchOffException {
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        boolean isSupportII = deviceVersionService.isFunctionSupported(cmcId, "spectrumII");
        if (!isSupportII && isWithoutAgentCmts(cmc)) {// 压缩频谱不需要OLT开关打开
            if (!spectrumConfigService.getOltSwitchStatus(cmcId)) {
                throw new OltSwitchOffException();
            }
        }
        if (!spectrumConfigService.getCmcSwitchStatus(cmcId)) {
            List<Long> cmcIds = new ArrayList<Long>();
            cmcIds.add(cmcId);
            spectrumConfigService.startSpectrumSwitchCmts(cmcIds);
        }
        return true;
    }

    private boolean isWithoutAgentCmts(CmcAttribute cmc) {
        // TODO 重构DeviceType后需要修改判断拆分型的方法
        return entityTypeService.isCcmtsWithoutAgent(cmc.getCmcDeviceStyle());
    }

    private SpectrumCallbackCollection getCallbackCollection(Long cmcId) {
        SpectrumCallbackCollection callbackCollection = callbackMap.get(cmcId);
        if (callbackCollection == null) {
            callbackCollection = (SpectrumCallbackCollection)beanFactory.getBean("spectrumCallbackCollection");
            callbackMap.put(cmcId, callbackCollection);
        }
        return callbackCollection;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
