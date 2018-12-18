/***********************************************************************
 * $Id: RealtimeCmListService.java,v1.0 2014年5月11日 下午2:44:43 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cm.domain.RealtimeCm;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.framework.service.Service;

/**
 * @author YangYi
 * @created @2014年5月11日-下午2:44:43
 * 
 */
public interface RealtimeCmListService extends Service {
	/**
	 * 读取上行信道SNR
	 * 
	 * @param cmcId
	 * @param cmIndex
	 * @return
	 */
	Map<String, List<String>> loadUpChannelSnr(Long cmcId, Long cmIndex);

	/**
	 * 读取CM信号质量--下行信道SNR，上行发送电平，下行接收电平
	 * 
	 * @param cmType
	 * @param cmId
	 * @param cmIndex
	 * @param cmcId
	 * @return
	 */
	Map<String, List<String>> loadCmSignal(Integer cmType, Long cmIndex, Long cmcId);

	/**
	 * 读取CPE实时信息
	 * 
	 * @param cmcId
	 * @return
	 */
	List<CmCpe> getRealtimeCpeByCmcId(Long cmcId, Integer cpeStatus, Integer cpeType);

	/**
	 * 实时读取CM基本信息
	 * 
	 * @param cmcId
	 * @return
	 */
	List<RealtimeCm> getRealtimeCmAttributeByCmcId(Long cmcId, List<Long> cmIndexlist, String[] statusArray);

	/**
	 * 加载cm实时信息
	 * 
	 * @param1 cmcId
	 * @param2 List<Long>cmIndex
	 * @param3 cmStatus状态参数数组
	 * @return
	 */
	List<RealtimeCm> loadCmRealtimeInfo(Long cmcId, List<Long> cmIndexlist, String[] cmStatus);

}
