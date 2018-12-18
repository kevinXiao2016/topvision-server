/**
 * 
 */
package com.topvision.ems.cmc.downchannel.facade;

import java.util.List;

import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamISInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamMappings;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamOSInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamStatusInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDiscoveryIpqamDataB;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.downchannel.domain.CmcIpqamInfo;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.http.HttpParam;

/**
 * Http方式采集端对外接口，包括设备数据刷新及信息采集
 * @author bryan
 *
 */
@EngineFacade(serviceName = "CmcIpqamCollectFacade", beanName = "cmcIpqamCollectFacade")
public interface CmcIpqamCollectFacade extends Facade {
    /**
     * 根据传递的Data数据及ip地址刷新CC8800B的IPQAM数据
     * @param httpParam
     * @return
     */
    CmcDiscoveryIpqamDataB refreshCC8800BIpqambyHttp(HttpParam httpParam);
    
	/**
	 * 获取B设备IPQAM的baseInfo
	 * @param httpParam
	 */
	List<CmcDSIpqamBaseInfo> getDSChannelIpqamBaseList(HttpParam httpParam);
	/**
	 * 更新IPQAM设备基本信息
	 * @param httpParam
	 * @param cc8800bHttpDownChannelIPQAM
	 * @return
	 */
	String modifyDSChannelIpqamBaseList(HttpParam httpParam,CmcDSIpqamBaseInfo cc8800bHttpDownChannelIPQAM);
	
	/**
     * 更新IPQAM设备信道状态信息
     * @param httpParam
     * @param channelIds
     * @param status
     * @return
     */
    String modifyChannelsAdminStatus(HttpParam httpParam,Integer channelIds, Integer status);
	/**
	 * 信道的输出列表的状态列表（统计列表）
	 * @param httpParam
	 * @return
	 */
	List<CmcDSIpqamStatusInfo> fetchIpqamStatusInfoList(HttpParam httpParam);
	/**
     * 获取信道的所有节目映射信息
     * @param httpParam
     * @return
     */
    List<CmcDSIpqamMappings> fetchIpqamMappingsInfoList(HttpParam httpParam);
    
	/**
	 * 增加行状态ipqamAction，1表示删除，2表示修改，3表示增加
	 * @param httpParam
	 * @param cc8800bHttpDSIpqamMappingsList
	 * @param action
	 * @return
	 */
	String modifyIpqamMappings(HttpParam httpParam, List<CmcDSIpqamMappings> cc8800bHttpDSIpqamMappingsList,Integer action);
	/**
	 * 采集IPQAM输入流节目信息
	 * @param httpParam
	 * @return
	 */
	List< CmcDSIpqamISInfo> fetchIpqamInputStreamInfoList(HttpParam httpParam);  
	/**
	 * 采集IPQAM输出流节目信息
	 * @param httpParam
	 * @return
	 */
	List< CmcDSIpqamOSInfo> fetchIpqamOutputStreamInfoList(HttpParam httpParam);
	
	/**
	 * 采集IPQAM的IP类信息
	 * @param httpParam
	 */
	CmcIpqamInfo getCmcIpQamIpInfo(HttpParam httpParam);
	/**
	 * 修改IPQAM的IP地址信息
	 * @param httpParam
	 * @param cmcIpqamInfo
	 */
	String modifyCmcIpqamIPInfo(HttpParam httpParam,CmcIpqamInfo cmcIpqamInfo);
	
	/**
	 * 获取FPGA信息
	 * @param httpParam
	 * @return
	 */
	public CmcFpgaSpecification getCmcFpgaSpecification(HttpParam httpParam);

}
