/***********************************************************************
 * $ SpectrumAlertServiceImpl.java,v1.0 2015-2-27 9:58:19 $
 *
 * @author: YangYi
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.dao.SpectrumAlertDao;
import com.topvision.ems.cmc.spectrum.service.SpectrumAlertService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author YangYi
 * @created @2015-2-27-9:58:19
 */
@Service("spectrumAlertService")
public class SpectrumAlertServiceImpl extends CmcBaseCommonService implements SpectrumAlertService, BeanFactoryAware {
    // private static final Integer SPECTRUM_EVENT = 200000;
    private static final Integer SPECTRUM_ALERT_TYPEID = 200001;
    private static final Integer SPECTRUM_EVENT_CLEARTYPEID = 200002;
    private static Properties properties;

    private int overThresholdTimes = 4;// 累计超过阈值次数Y，默认为4
    private int notOverThresholdTimes = 4;// 累计不超过阈值次数Z，默认为4
    private int overThresholdPercent = 20;// 超过阈值N%个点的N，默认为20%
    private int notOverThresholdPercent = 20;// 未超过阈值M%个点的M，默认为10%

    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "spectrumAlertDao")
    private SpectrumAlertDao spectrumAlertDao;
    @Resource(name = "systemPreferencesService")
    private SystemPreferencesService systemPreferencesService;
    private DecimalFormat df = new DecimalFormat("#.00");

    // 累计超过/未超过阈值计数器，此计数器在告警已存在时，记录累计未超过阈值次数，当告警不存在时，记录累计超过阈值次数。
    // 在发生/清除告警时，计数器清0，功能上翻转。
    private HashMap<Long, HashMap<Integer, AtomicInteger>> timesCounter = new HashMap<Long, HashMap<Integer, AtomicInteger>>();
    // 记录是否存在告警
    private HashMap<Long, HashMap<Integer, Boolean>> isAlertExist = new HashMap<Long, HashMap<Integer, Boolean>>();

    /**
     * 启动方法，初始化4个参数
     */
    @Override
    public void start() {
        overThresholdTimes = getOverThresholdTimes();// 获取累计超过阈值次数Y，默认为4
        notOverThresholdTimes = getNotOverThresholdTimes(); // 获取累计不超过阈值次数Z，默认为4
        overThresholdPercent = getOverThresholdPercent(); // 获取超过阈值N%个点的N,默认为20
        notOverThresholdPercent = getNotOverThresholdPercent(); // 获取未超过阈值M%个点的M,默认为10
    }

    /**
     * 添加一台CC到噪声告警服务，在该CC启动频谱perfmonitor时调用
     */
    @Override
    public void addCmc(Long cmcId) {
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        String cmcMac = cmc.getTopCcmtsSysMacAddr();// CC的MAC
        String source = cmcMac + this.getStr("spectrum.channel");
        HashMap<Integer, AtomicInteger> map1 = new HashMap<Integer, AtomicInteger>();
        HashMap<Integer, Boolean> map2 = new HashMap<Integer, Boolean>();
        for (int i = 1; i <= 4; i++) {
            map1.put(i, new AtomicInteger(0));
            map2.put(i, spectrumAlertDao.isSpectrumAlertExist(source + i));
        }
        timesCounter.put(cmcId, map1);
        isAlertExist.put(cmcId, map2);
    }

    /**
     * 从噪声告警服务移除一台CC，在该CC停止频谱pefrmonitor时调用
     */
    @Override
    public void removeCmc(Long cmcId) {
        HashMap<Integer, AtomicInteger> map1 = timesCounter.remove(cmcId);
        if (map1 != null) {
            map1.clear();
        }
        HashMap<Integer, Boolean> map2 = isAlertExist.remove(cmcId);
        if (map2 != null) {
            map2.clear();
        }
    }

    /**
     * 处理逻辑
     */
    @Override
    public boolean process(Long entityId, Long cmcId, List<List<Number>> list) {
        if (list == null) {
            return false;
        }
        List<CmcUpChannelBaseShowInfo> upChannels = cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId);// 获得上行信道信息
        for (int i = 0; i < upChannels.size(); i++) {
            CmcUpChannelBaseShowInfo upChannelInfo = upChannels.get(i);
            Integer status = upChannelInfo.getIfAdminStatus();
            if (status == null || status != 1) {
                continue;
            }
            Integer channelId = upChannelInfo.getChannelId();
            double noiseThreshold = getNoiseThreshold(upChannelInfo);// 当前信道噪声的阈值
            int overCount = calOverCount(upChannelInfo, list, noiseThreshold);// 本信道本次采集结果超过阈值的点的个数
            int needOverCount = getOverCount(upChannelInfo); // 大于多少个点超过阈值判定为此信道超过阈值
            int needNotOverCount = getNotOverCount(upChannelInfo);// 小于多少个点超过阈值判定为此信道超过阈值
            if (isAlertExist(cmcId, channelId)) {// 本信道已经产生告警
                if (overCount >= needOverCount) {// 本信道超过阈值
                    sendAlert(entityId, cmcId, upChannelInfo, noiseThreshold);// 发出告警
                } else if (overCount <= needNotOverCount) { // 本信道没有超过阈值
                    int nowNotOver = getTimesCounter(cmcId, channelId).incrementAndGet();// 算上本次，累计未超过阈值的次数
                    if (nowNotOver >= notOverThresholdTimes) {
                        clearAlert(entityId, cmcId, upChannelInfo, noiseThreshold); // 清除告警
                        setAlertExist(cmcId, channelId, false);
                        getTimesCounter(cmcId, channelId).set(0);// 重置计数器，此时计数器翻转用于记录累计超过阈值
                    }
                } else {
                    getTimesCounter(cmcId, channelId).set(0);// 重置累计超过阈值计数器
                }
            } else {// 本信道还未产生告警
                if (overCount >= needOverCount) {// 本信道超过阈值
                    int nowOver = getTimesCounter(cmcId, channelId).incrementAndGet();// 算上本次，累计超过阈值的次数
                    if (nowOver >= overThresholdTimes) {
                        sendAlert(entityId, cmcId, upChannelInfo, noiseThreshold);// 发出告警
                        setAlertExist(cmcId, channelId, true);
                        getTimesCounter(cmcId, channelId).set(0);// 重置计数器，此时计数器翻转用于记录累计未超过阈值
                    }
                } else {// 本信道没有超过阈值
                    getTimesCounter(cmcId, channelId).set(0);// 重置累计超过阈值计数器
                }
            }
        }
        return false;
    }

    /**
     * 发送告警
     * 
     * @param entityid
     * @param cmcId
     * @param upChannelInfo
     */
    private void sendAlert(Long entityid, Long cmcId, CmcUpChannelBaseShowInfo upChannelInfo, double noiseThreshold) {
        EventSender eventSender = EventSender.getInstance();
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        String host = cmc.getManageIp(); // CC的IP
        String cmcMac = cmc.getTopCcmtsSysMacAddr();// CC的MAC
        String source = cmcMac + this.getStr("spectrum.channel") + upChannelInfo.getChannelId(); // source需要包含信道编号
        Event event = eventSender.createEvent(SPECTRUM_ALERT_TYPEID, host, source);
        event.setLevelId(Level.MAJOR_LEVEL);
        String message = "CMTS" + "[" + cmcMac + "]" + this.getStr("spectrum.channel") + upChannelInfo.getChannelId()
                + this.getStr("spectrum.nosieTooHigh") + "(" + noiseThreshold + "dBmV)";
        event.setMessage(message);
        event.setEntityId(entityid);
        eventSender.send(event);
        logger.debug("Spectrum Alert Fired. cmcId = " + cmcId + "  channelId = " + upChannelInfo.getChannelId());
    }

    /**
     * 清除告警
     * 
     * @param entityId
     * @param cmcId
     * @param upChannelInfo
     */
    private void clearAlert(Long entityId, Long cmcId, CmcUpChannelBaseShowInfo upChannelInfo, double noiseThreshold) {
        EventSender eventSender = EventSender.getInstance();
        // 发送清除告警事件
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        String host = cmc.getManageIp(); // CC的IP
        String cmcMac = cmc.getTopCcmtsSysMacAddr();// CC的MAC
        String source = cmcMac + this.getStr("spectrum.channel") + upChannelInfo.getChannelId(); // source需要包含信道编号
        Event event = eventSender.createEvent(SPECTRUM_EVENT_CLEARTYPEID, host, source);
        event.setLevelId(Level.CLEAR_LEVEL);
        String message = "CMTS" + "[" + cmcMac + "]" + this.getStr("spectrum.channel") + upChannelInfo.getChannelId()
                + this.getStr("spectrum.nosieRecovered") + "(" + noiseThreshold + "dBmV)";
        event.setMessage(message);
        event.setEntityId(entityId);
        eventSender.send(event);
        logger.debug("Spectrum Alert Cleared. cmcId = " + cmcId + "  channelId = " + upChannelInfo.getChannelId());
    }

    /**
     * 获取噪声阈值
     * 
     * @param upChannelInfo
     * @return
     */
    public Double getNoiseThreshold(CmcUpChannelBaseShowInfo upChannelInfo) {
        if (properties == null) {
            try {
                InputStream is = new FileInputStream(new File(SystemConstants.WEB_INF_REAL_PATH
                        + "/conf/noiseThreshold.properties"));
                properties = new Properties();
                properties.load(is);
            } catch (IOException e) {
                logger.error("SpectrumSmoothUtil open file smoothPoints.properties error", e);
            }
        }
        String profile = upChannelInfo.getModulationProfileShortName();// 配置模板
        Long width = upChannelInfo.getChannelWidth();// 频宽
        String key = profile + "_" + width;
        String value = properties.getProperty(key);
        return value == null ? null : Double.valueOf(value);
    }

    /**
     * 计算超过了阈值的点数
     * 
     * @param upChannelInfo
     * @param list
     * @return
     */
    public int calOverCount(CmcUpChannelBaseShowInfo upChannelInfo, List<List<Number>> list,double noiseThreshold) {
        // long needOverThresholdCount = getOverCount(upChannelInfo);
        double channelFreq = (double) upChannelInfo.getChannelFrequency() / 1000000;
        double channelWidth = (double) upChannelInfo.getChannelWidth() / 1000000;
        double channelStart = channelFreq - channelWidth / 2;// 信道起始频点(MHz)
        double channelEnd = channelFreq + channelWidth / 2;// 信道终止频点(MHz)
        int count = 0;
        int start = (int) Math.round((channelStart - 0.3) / 0.02);
        int end = (int) Math.round((channelEnd - 0.3) / 0.02);
        for (int i = start; i <= end; i++) {
            double y = Double.valueOf(df.format(list.get(i).get(1))) - 60;
            if (y >= noiseThreshold) {
                count++;
            }
        }
//        logger.debug("Spectrum calOverNoiseThresholdCount: upchannelId =  " + upChannelInfo.getChannelId()
//                + "  profile = " + upChannelInfo.getModulationProfileShortName() + "  width = "
//                + upChannelInfo.getChannelWidth() + "  noiseThreshold = " + noiseThreshold
//                + " needOverThresholdCount = " + needOverThresholdCount + "  channelFreq = " + channelFreq
//                + " channelWidth = " + channelWidth + " channelStart = " + channelStart + " channelEnd = " + channelEnd
//                + " count =  " + count);
        return count;
    }

    @Override
    /**
     *  累计超过阈值次数Y，默认为4
     */
    public Integer getOverThresholdTimes() {
        return Integer.parseInt(getSystemPreferences("overThresholdTimes", "4"));
    }

    @Override
    /**
     *  累计不超过阈值次数Z，默认为4
     */
    public Integer getNotOverThresholdTimes() {
        return Integer.parseInt(getSystemPreferences("notOverThresholdTimes", "4"));
    }

    @Override
    /**
     *  超过阈值N%个点的N，默认为20%
     */
    public Integer getOverThresholdPercent() {
        return Integer.parseInt(getSystemPreferences("overThresholdPercent", "20"));
    }

    @Override
    /**
     *  未超过阈值M%个点的N，默认为10%
     */
    public Integer getNotOverThresholdPercent() {
        return Integer.parseInt(getSystemPreferences("notOverThresholdPercent", "10"));
    }

    /**
     * 修改频谱噪声告警配置
     */
    @Override
    public void modifySpectrumAlertConfig(Integer overThresholdTimes, Integer notOverThresholdTimes,
            Integer overThresholdPercent, Integer notOverThresholdPercent) {
        this.overThresholdTimes = overThresholdTimes;
        this.notOverThresholdTimes = notOverThresholdTimes;
        this.overThresholdPercent = overThresholdPercent;
        this.notOverThresholdPercent = notOverThresholdPercent;
        this.saveSystemPreference("overThresholdTimes", overThresholdTimes);
        this.saveSystemPreference("notOverThresholdTimes", notOverThresholdTimes);
        this.saveSystemPreference("overThresholdPercent", overThresholdPercent);
        this.saveSystemPreference("notOverThresholdPercent", notOverThresholdPercent);
    }

    /**
     * 读取累计超过/未超过阈值计数器
     * 
     * @param cmcId
     * @param channelId
     * @return
     */
    private AtomicInteger getTimesCounter(Long cmcId, Integer channelId) {
        HashMap<Integer, AtomicInteger> map = timesCounter.get(cmcId);
        return map != null ? map.get(channelId) : new AtomicInteger(0);
    }

    /**
     * 判断某CC的某信道是否已经存在告警
     * 
     * @param cmcId
     * @param channelId
     * @return
     */
    private boolean isAlertExist(Long cmcId, Integer channelId) {
        HashMap<Integer, Boolean> map = isAlertExist.get(cmcId);
        return map != null ? map.get(channelId) : false;
    }

    /**
     * 设置某CC的某信道是否已经存在告警
     * 
     * @param cmcId
     * @param channelId
     * @param isExist
     */
    private void setAlertExist(Long cmcId, Integer channelId, boolean isExist) {
        HashMap<Integer, Boolean> map = isAlertExist.get(cmcId);
        if (map != null) {
            map.put(channelId, isExist);
        }
    }

    /**
     * 读取配置参数
     * 
     * @param name
     * @param defaultString
     * @return
     */
    private String getSystemPreferences(String name, String defaultString) {
        List<SystemPreferences> list = systemPreferencesService.getSystemPreferences("spectrum");
        for (SystemPreferences systemPreferences : list) {
            if (systemPreferences.getName().equalsIgnoreCase(name)) {
                return systemPreferences.getValue();
            }
        }
        return defaultString;
    }

    /**
     * 存储配置参数
     * 
     * @param name
     * @param value
     */
    private void saveSystemPreference(String name, Object value) {
        SystemPreferences preferences = new SystemPreferences();
        preferences.setModule("spectrum");
        preferences.setName(name);
        preferences.setValue(String.valueOf(value));
        systemPreferencesService.savePreferences(preferences);
    }

    /**
     * 获取超过阈值N%个点的N，默认为20% * 频宽 / 20KHz
     * 
     * @param upChannelInfo
     * @return
     */
    private int getOverCount(CmcUpChannelBaseShowInfo upChannelInfo) {
        Long w = upChannelInfo.getChannelWidth();
        return w == null ? 9999 : (int) (overThresholdPercent * w / 20000 / 100);
    }

    /**
     * 获取未超过阈值M%个点的M，默认为10% * 频宽 / 20KHz
     * 
     * @param upChannelInfo
     * @return
     */
    private int getNotOverCount(CmcUpChannelBaseShowInfo upChannelInfo) {
        Long w = upChannelInfo.getChannelWidth();
        return w == null ? 9999 : (int) (notOverThresholdPercent * w / 20000 / 100);
    }

    /**
     * 从国际化文件获取字符串
     * 
     * @param key
     * @param strings
     * @return
     */
    private String getStr(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.cmc.spectrum.resources").getString(key,
                    strings);
        } catch (Exception e) {
            logger.debug("", e);
            return key;
        }
    }

    @Override
    public void setBeanFactory(BeanFactory arg0) throws BeansException {
    }

}
