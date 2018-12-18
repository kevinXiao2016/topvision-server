package com.topvision.ems.cmc.spectrum.service;

import java.util.List;

public interface SpectrumAlertService {

    /**
     * 噪声处理逻辑
     * 
     * @param entityId
     * @param cmcId
     * @param list
     * @return
     */
    boolean process(Long entityId, Long cmcId, List<List<Number>> list);
    
    /**
     * 一台CC停止频谱采集时调用
     * 
     * @param cmcId
     */
    void removeCmc(Long cmcId);

    /**
     * 一台CC启动频谱采集时调用
     * 
     * @param cmcId
     */
    void addCmc(Long cmcId);

    /**
     * 获取累计超过阈值次数Y，默认为4
     * 
     * @return
     */
    Integer getOverThresholdTimes();

    /**
     * 获取累计不超过阈值次数Z，默认为4
     * 
     * @return
     */
    Integer getNotOverThresholdTimes();

    /**
     * 获取超过阈值N%个点的N，默认为20%
     * 
     * @return
     */
    Integer getOverThresholdPercent();

    /**
     * 获取未超过阈值M%个点的N，默认为10%
     * 
     * @return
     */
    Integer getNotOverThresholdPercent();

    /**
     * 修改频谱告警配置
     * 
     * @param overThresholdTimes
     * @param notOverThresholdTimes
     * @param overThresholdPercent
     * @param notOverThresholdPercent
     */
    void modifySpectrumAlertConfig(Integer overThresholdTimes, Integer notOverThresholdTimes,
            Integer overThresholdPercent, Integer notOverThresholdPercent);
    
    
}
