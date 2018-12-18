package com.topvision.ems.cmc.spectrum.performance.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumII;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumIIData;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumIIResult;
import com.topvision.ems.cmc.spectrum.performance.handle.SpectrumIIHandle;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.snmp.SnmpExecutorService;

/**
 * @author bryan
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("spectrumIIPerfScheduler")
public class SpectrumIIPerfScheduler extends AbstractExecScheduler<SpectrumII> {
    @Autowired
    @Qualifier("snmpExecutorService")
    protected SnmpExecutorService snmpExecutorService;

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("spectrumIIPerfScheduler  entityId[" + operClass.getEntityId() + "] cmcId["
                    + operClass.getCmcId() + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            String cmcMac = operClass.getMacAddress();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            String mibs = snmpParam.getMibs() + ",TOPVISION-FFT-MONITOR-MIB";
            snmpParam.setMibs(mibs);
            SpectrumIIResult spectrumIIResult = new SpectrumIIResult(operClass);
            SpectrumIIData data = new SpectrumIIData();
            data.setMacIndex(new PhysAddress(cmcMac));
            try {
                SpectrumIIData spectrumIIData = snmpExecutorService.getTableLine(snmpParam, data);
                logger.debug("Collect spectrum data");
                logger.debug(spectrumIIData.getDataBuffer());
                spectrumIIResult.setSpectrumIIData(spectrumIIData);
            } catch (Exception e) {
                logger.debug("", e);
            }
            spectrumIIResult.setEntityId(entityId);
            spectrumIIResult.setCmcId(cmcId);
            spectrumIIResult.setDt(dt);
            logger.debug("spectrumIIResult write result to file.");
            // 将结果记入磁盘文件 等待server处理
            // addLocalFileData(spectrumIIResult);
            // Modify by
            // Victor@20150613原来是通过Saver进行处理，现在Saver改为engine端程序，PerformanceData为传递到Server进行处理，原来Saver改Handle处理
            PerformanceData pData = new PerformanceData(entityId, SpectrumIIHandle.TYPE_CODE, spectrumIIResult);
            // Add by Victor@20160823增加monitorId，为了推送无接收方时停止后台monitor
            pData.setMonitorId(operClass.getMonitorId());
            try {
                getCallback().sendRealtimePerfomaceResult(pData);
            } catch (Exception e) {
                logger.debug("", e);
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("spectrumIIPerfScheduler exec end.");
    }
}
