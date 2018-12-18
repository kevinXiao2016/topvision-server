package com.topvision.ems.cmc.spectrum.performance.scheduler;

import com.topvision.ems.cmc.spectrum.exception.SpectrumException;
import com.topvision.ems.cmc.spectrum.facade.domain.*;
import com.topvision.ems.cmc.spectrum.performance.SpectrumUdpClient;
import com.topvision.ems.cmc.spectrum.performance.handle.SpectrumIIHandle;
import com.topvision.ems.cmc.spectrum.performance.handle.SpectrumIIIHandle;
import com.topvision.ems.cmc.spectrum.utils.SpectrumByteRead;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.snmp.SnmpExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bryan
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("spectrumIIIPerfScheduler")
public class SpectrumIIIPerfScheduler extends AbstractExecScheduler<SpectrumIII> {
    @Autowired
    @Qualifier("snmpExecutorService")
    protected SnmpExecutorService snmpExecutorService;
    
    private static Long collectTime = 0L;
    private static Long minInterval = 0L;
    private static Long maxInterval = 0L;
    private static Double avgInterval = 0d;
    private static Long minCost = 0L;
    private static Long maxCost = 0L;
    private static Double avgCost = 0d;
    private static Long collectCost=0L;
    private static Integer index = 0;
    private static Integer errorIndex = 0;
    private static List<Long> costList = new ArrayList<Long>();

    private Integer count = 0;
    private Integer sameCount = 0;
    private String preDataBuffer;
    
    @Override
    public void exec() {
        if (logger.isDebugEnabled() && !logger.isTraceEnabled()) {
            logger.debug("spectrumIIIPerfScheduler  entityId[" + operClass.getEntityId() + "] cmcId["
                    + operClass.getCmcId() + "] exec start.");
        }
        try {
            long dt = System.currentTimeMillis();
            long entityId = operClass.getEntityId();
            long cmcId = operClass.getCmcId();
            int cmcIndex = operClass.getCmcIndex().intValue();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            String mibs = snmpParam.getMibs() + ",TOPVISION-FFT-MOITOR-MIB";
            snmpParam.setMibs(mibs);
            SpectrumIIIResult spectrumIIIResult = new SpectrumIIIResult(operClass);
            spectrumIIIResult.setCmcId(cmcId);
            try {
            	Long startCollect = System.currentTimeMillis();
                SpectrumUdpClient client = new SpectrumUdpClient();
                String serverHost = snmpParam.getIpAddress();
                int serverPort = 3000;
                client.setSoTimeout(300);

                SpectrumIIIData request = new SpectrumIIIData(SpectrumIIIData.requestPacketLength);
                request.setVersion(SpectrumIIIData.UdpVersion);
                request.setCommunity(snmpParam.getCommunity());
                request.setPduType(SpectrumIIIData.GetRequest);
                short requestId = SpectrumIIIData.getNextRequestId();
                request.setRequestId(requestId);
                request.setErrorStatus(SpectrumIIIData.NoError);
                request.setCmcIndex(cmcIndex);

                client.send(serverHost, serverPort, request.getBytes());
                byte[] spectrumData = client.receive(serverHost, serverPort);
                SpectrumIIIData response = new SpectrumIIIData(spectrumData);
                if (requestId != response.getRequestId()) {
                    throw new SpectrumException("RequestId error request[" + requestId + "] response[" + response.getRequestId() + "]");
                }
                if (!response.getErrorStatus().equals(SpectrumIIIData.NoError)) {
                    throw new SpectrumException("Response error status [" + response.getErrorStatus() + "]");
                }
                if (logger.isTraceEnabled()) {
                    Long endCollect = System.currentTimeMillis();
                    collectCost = endCollect - startCollect;
                    costList.add(collectCost);
                    if(collectCost > 600L) {
                        errorIndex ++;
                    }
                    if(minCost == 0L || minCost > collectCost) {
                        minCost = collectCost;
                    }
                    if(maxCost == 0L || maxCost < collectCost) {
                        maxCost = collectCost;
                    }
                    avgCost = (avgCost * index + collectCost) / (index+1);
                }

                if (logger.isDebugEnabled() && !logger.isTraceEnabled()) {
                    logger.debug("Collect spectrum data");
                }
                Short dataLength = response.getDataLength();
                String dataBuffer = response.getDataBuffer(dataLength);
                spectrumIIIResult.setDataBuffer(dataBuffer);
            } catch (Exception e) {
            	logger.debug(e.getMessage());
                logger.trace("", e);
            }

            if (logger.isTraceEnabled()) {
                Long thisTime = System.currentTimeMillis();
                Long thisInterval = collectTime == 0L ? 0 : thisTime - collectTime;
                collectTime = thisTime;
                if(minInterval == 0L || minInterval > thisInterval) {
                    minInterval = thisInterval;
                }
                if(maxInterval == 0L || maxInterval < thisInterval) {
                    maxInterval = thisInterval;
                }
                avgInterval = (avgInterval * index + thisInterval) / (index+1);
                index++;
                logger.trace(String.format("Index: %s, thisInterval: %s, minInterval: %s, maxInterval: %s, avgInterval: %s, thisCost: %s, minCost: %s, maxCost: %s, avgCost: %s, errorPercent: %s",
                index, thisInterval, minInterval, maxInterval, avgInterval, collectCost, minCost, maxCost, avgCost, errorIndex*100/index + "%"));
                if(costList.size() % 1000 == 0){
                    logger.trace(costList.toString());
                }
            }


            spectrumIIIResult.setEntityId(entityId);
            spectrumIIIResult.setCmcId(cmcId);
            spectrumIIIResult.setDt(dt);
            if (logger.isDebugEnabled() && !logger.isTraceEnabled()) {
                logger.debug("spectrumIIResult write result to file.");
            }
            // 将结果记入磁盘文件 等待server处理
            // addLocalFileData(spectrumIIResult);
            // Modify by
            // Victor@20150613原来是通过Saver进行处理，现在Saver改为engine端程序，PerformanceData为传递到Server进行处理，原来Saver改Handle处理
            PerformanceData pData = new PerformanceData(entityId, SpectrumIIIHandle.TYPE_CODE, spectrumIIIResult);
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
        if (logger.isDebugEnabled() && !logger.isTraceEnabled()) {
            logger.debug("spectrumIIIPerfScheduler exec end.");
        }
    }

}
