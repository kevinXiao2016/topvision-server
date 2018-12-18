package com.topvision.ems.cmc.spectrum.performance.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.spectrum.facade.domain.Spectrum;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumData;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumResult;
import com.topvision.ems.cmc.spectrum.performance.handle.SpectrumHandle;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.snmp.SnmpExecutorService;

/**
 * @author bryan
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("spectrumPerfScheduler")
public class SpectrumPerfScheduler extends AbstractExecScheduler<Spectrum> {
    @Autowired
    @Qualifier("snmpExecutorService")
    protected SnmpExecutorService snmpExecutorService;

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("spectrumPerfScheduler  entityId[" + operClass.getEntityId() + "] cmcId["
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
            SpectrumResult spectrumResult = new SpectrumResult(operClass);
            SpectrumData data = new SpectrumData();
            data.setTopCcmtsFftDataCmcMacIndex(new PhysAddress(cmcMac));
            try {
                List<SpectrumData> spectrumDatas = snmpExecutorService.getTableLines(snmpParam, data, 0,
                        Integer.MAX_VALUE);
                spectrumResult.setSpectrumData(spectrumDatas);
            } catch (Exception e) {
                logger.debug(e.toString());
            }
            spectrumResult.setEntityId(entityId);
            spectrumResult.setCmcId(cmcId);
            spectrumResult.setDt(dt);
            logger.debug("SpectrumResult write result to file. cmcId =" + operClass.getCmcId());
            // 将结果记入磁盘文件 等待server处理
            // modify by jay 修改为不走公共队列，保证频谱的实时性
            // addLocalFileData(spectrumResult);
            // Modify by
            // Victor@20150613原来是通过Saver进行处理，现在Saver改为engine端程序，PerformanceData为传递到Server进行处理，原来Saver改Handle处理
            PerformanceData pData = new PerformanceData(entityId, SpectrumHandle.TYPE_CODE, spectrumResult);
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
        logger.debug("SpectrumPerfScheduler exec end. cmcId =" + operClass.getCmcId());
    }
}
