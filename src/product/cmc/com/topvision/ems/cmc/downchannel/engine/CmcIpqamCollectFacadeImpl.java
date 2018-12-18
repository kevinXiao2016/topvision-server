package com.topvision.ems.cmc.downchannel.engine;

import java.util.List;

import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamISInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamMappings;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamOSInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamStatusInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDiscoveryIpqamDataB;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.downchannel.domain.CmcIpqamInfo;
import com.topvision.ems.cmc.downchannel.engine.collect.CmcHttpClient;
import com.topvision.ems.cmc.downchannel.engine.collect.CmcIpQamCollect;
import com.topvision.ems.cmc.downchannel.engine.collect.CmcIpQamConfigCollect;
import com.topvision.ems.cmc.downchannel.engine.collect.CmcIpQamMappingsCollect;
import com.topvision.ems.cmc.downchannel.engine.collect.CmcIpQamPStreamCollect;
import com.topvision.ems.cmc.downchannel.facade.CmcIpqamCollectFacade;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.http.HttpParam;

/**
 * 
 * @author bryan
 * @created @2013-10-12-上午10:53:40
 * 
 */
@Facade("cmcIpqamCollectFacade")
public class CmcIpqamCollectFacadeImpl extends EmsFacade implements CmcIpqamCollectFacade {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#refreshCmcbyHttp(com.topvision.ems.cmc
     * .facade.domain.CmcDiscoveryDataB, java.lang.String)
     */
    @Override
    public CmcDiscoveryIpqamDataB refreshCC8800BIpqambyHttp(HttpParam httpParam) {
        CmcDiscoveryIpqamDataB ipqamDataB = null;
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamCollect cc8800bIpQamCollect = new CmcIpQamCollect();
        CmcIpQamMappingsCollect cc8800bIpQamMappingsCollect = new CmcIpQamMappingsCollect();
        CmcIpQamPStreamCollect cc8800bIpQamPStreamCollect = new CmcIpQamPStreamCollect();
        CmcIpQamConfigCollect cmcIpQamConfigCollect = new CmcIpQamConfigCollect();
        ipqamDataB = new CmcDiscoveryIpqamDataB();
        // 1,CmcDSIpqamBaseInfo
        List<CmcDSIpqamBaseInfo> cc8800bHttpDownChannelIPQAMs = cc8800bIpQamCollect.getIpqamBaseInfoListResult(client);
        if (cc8800bHttpDownChannelIPQAMs != null && cc8800bHttpDownChannelIPQAMs.size() > 0) {
            ipqamDataB.setCmcDSIpqamBaseInfos(cc8800bHttpDownChannelIPQAMs);
        }
        // 2,CmcDSIpqamStatusInfo
        List<CmcDSIpqamStatusInfo> cc8800bHttpDSIpqamStatusInfos = cc8800bIpQamCollect
                .getIpqamStatusInfoListResult(client);
        if (cc8800bHttpDSIpqamStatusInfos != null && cc8800bHttpDSIpqamStatusInfos.size() > 0) {
            ipqamDataB.setCmcDSIpqamStatusInfos(cc8800bHttpDSIpqamStatusInfos);
        }
        // 3,CmcDSIpqamMappings
        List<CmcDSIpqamMappings> cc8800bHttpDSIpqamMappingsList = cc8800bIpQamMappingsCollect
                .getIpqamMappingsListResult(client);
        if (cc8800bHttpDSIpqamMappingsList != null && cc8800bHttpDSIpqamMappingsList.size() > 0) {
            ipqamDataB.setCmcDSIpqamMappings(cc8800bHttpDSIpqamMappingsList);
        }
        // 4,CmcDSIpqamISInfo
        List<CmcDSIpqamISInfo> cc8800bHttpIpQamISInfos = cc8800bIpQamPStreamCollect
                .getIpqamInputStreamInfoResult(client);
        if (cc8800bHttpIpQamISInfos != null && cc8800bHttpIpQamISInfos.size() > 0) {
            ipqamDataB.setCmcDSIpqamISInfos(cc8800bHttpIpQamISInfos);
        }
        // 5,CmcDSIpqamOSInfo
        List<CmcDSIpqamOSInfo> cc8800bHttpIpQamOSInfos = cc8800bIpQamPStreamCollect
                .getIpqamOutputStreamInfoResult(client);
        if (cc8800bHttpIpQamOSInfos != null && cc8800bHttpIpQamOSInfos.size() > 0) {
            ipqamDataB.setCmcDSIpqamOSInfos(cc8800bHttpIpQamOSInfos);
        }
        // 6,add ip info
        CmcIpqamInfo cmcIpqamInfo = cmcIpQamConfigCollect.getIpqamIPInfoResult(client);
        if (cmcIpqamInfo != null) {
            ipqamDataB.setCmcIpqamInfo(cmcIpqamInfo);
        }

        return ipqamDataB;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#getDSChannelIpqamBaseList()
     */
    @Override
    public List<CmcDSIpqamBaseInfo> getDSChannelIpqamBaseList(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamCollect cc8800bIpQamCollect = new CmcIpQamCollect();

        return cc8800bIpQamCollect.getIpqamBaseInfoListResult(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#setDSChannelIpqamBaseList(java.lang.String
     * , com.topvision.ems.cmc.domain.CmcDSIpqamBaseInfo)
     */
    @Override
    public String modifyDSChannelIpqamBaseList(HttpParam httpParam, CmcDSIpqamBaseInfo cc8800bHttpDownChannelIPQAM) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamCollect cc8800bIpQamCollect = new CmcIpQamCollect();
        String result = cc8800bIpQamCollect.setIpqamBaseInfoResult(client, cc8800bHttpDownChannelIPQAM);
        return result;// 期望值"SUCCESS"
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#setDSChannelIpqamBaseList(java.lang.String
     * , java.util.List, java.lang.Integer)
     */
    @Override
    public String modifyChannelsAdminStatus(HttpParam httpParam, Integer channelIds, Integer status) {

        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamCollect cc8800bIpQamCollect = new CmcIpQamCollect();
        String result = cc8800bIpQamCollect.setAdminStatusResult(client, channelIds, status);
        return result;// 期望值"SUCCESS"
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#fetchIpqamStatusInfoList(java.lang.String)
     */
    @Override
    public List<CmcDSIpqamStatusInfo> fetchIpqamStatusInfoList(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamCollect cc8800bIpQamCollect = new CmcIpQamCollect();

        return cc8800bIpQamCollect.getIpqamStatusInfoListResult(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#fetchIpqamMappingsInfoList(java.lang.String
     * )
     */
    @Override
    public List<CmcDSIpqamMappings> fetchIpqamMappingsInfoList(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamMappingsCollect cc8800bIpQamMappingsCollect = new CmcIpQamMappingsCollect();
        return cc8800bIpQamMappingsCollect.getIpqamMappingsListResult(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#modifyIpqamMappings(java.lang.String,
     * java.util.List)
     */
    @Override
    public String modifyIpqamMappings(HttpParam httpParam, List<CmcDSIpqamMappings> cc8800bHttpDSIpqamMappingsList,
            Integer action) {
        // 一个list中的对象ACTION状态只能是一种，另外List>1时是批量添加或删除。list==1时可以是增，删，改
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamMappingsCollect cc8800bIpQamMappingsCollect = new CmcIpQamMappingsCollect();
        String result = cc8800bIpQamMappingsCollect.setIpqamMappingsListResult(client, cc8800bHttpDSIpqamMappingsList,
                action);
        return result;// 期望值"SUCCESS"
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#fetchIpqamInputStreamInfoList(java.lang
     * .String)
     */
    @Override
    public List<CmcDSIpqamISInfo> fetchIpqamInputStreamInfoList(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamPStreamCollect collect = new CmcIpQamPStreamCollect();

        return collect.getIpqamInputStreamInfoResult(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcIpqamCollectFacade#fetchIpqamOutputStreamInfoList(java.lang
     * .String)
     */
    @Override
    public List<CmcDSIpqamOSInfo> fetchIpqamOutputStreamInfoList(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamPStreamCollect collect = new CmcIpQamPStreamCollect();

        return collect.getIpqamOutputStreamInfoResult(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.facade.CmcIpqamCollectFacade#getCmcIpQamIpInfo(com.topvision
     * .framework.http.HttpParam)
     */
    @Override
    public CmcIpqamInfo getCmcIpQamIpInfo(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamConfigCollect collect = new CmcIpQamConfigCollect();
        return collect.getIpqamIPInfoResult(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.facade.CmcIpqamCollectFacade#modifyCmcIpqamIPInfo(com.topvision
     * .framework.http.HttpParam, com.topvision.ems.cmc.downchannel.domain.CmcIpqamInfo)
     */
    @Override
    public String modifyCmcIpqamIPInfo(HttpParam httpParam, CmcIpqamInfo cmcIpqamInfo) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamConfigCollect collect = new CmcIpQamConfigCollect();

        return collect.modifyIpqamIPInfo(client, cmcIpqamInfo);
    }

    public CmcFpgaSpecification getCmcFpgaSpecification(HttpParam httpParam) {
        CmcHttpClient client = new CmcHttpClient(httpParam.getIpAddress());
        CmcIpQamCollect cc8800bIpQamCollect = new CmcIpQamCollect();
        CmcFpgaSpecification fpga = cc8800bIpQamCollect.checkIfIpqamSupported(client);
        return fpga;
    }

}
