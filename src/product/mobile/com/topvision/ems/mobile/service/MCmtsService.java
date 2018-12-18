package com.topvision.ems.mobile.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.QualityRangeResult;
import com.topvision.ems.cmc.spectrum.domain.UpChannelSpectrum;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsDownChannel;
import com.topvision.ems.mobile.domain.CmtsDownChannelWithPortId;
import com.topvision.ems.mobile.domain.CmtsInCmtsList;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.CmtsUpChannel;
import com.topvision.ems.mobile.domain.CmtsUpChannelWithPortId;
import com.topvision.framework.service.Service;

public interface MCmtsService extends Service {

    CmtsInfo getCmtsInfoById(Long cmtsId);

    List<CmtsDownChannel> getDownChannelsById(Long cmtsId);

    List<CmtsUpChannel> getUpChannelsById(Long cmtsId);

    QualityRangeResult getSignalQuality(Long cmtsId);
    
    List<UpChannelSpectrum> getCmtsSpectrum(Long cmtsId, String spectrumWidth);
    
    Long getErrorRateInterval(Long entityId);

    List<CmtsInCmtsList> getCmtsList(Map<String, Object> map);

    Long getCmtsListCount(Map<String, Object> map);

    List<CmtsUpChannelWithPortId> getUpChannelsInfoById(Long cmtsId);

    List<CmtsDownChannelWithPortId> getDownChannelsInfoById(Long cmtsId);

    void saveMapDataToDB(Map<String, Object> map);

    List<CmtsInCmtsList> getCmtsListWithRegion(Map<String, Object> map);

    Long getCmtsListCountWithRegion(Map<String, Object> map);

    void modifyCmcLocation(Map<String, Object> map);

    BaiduMapInfo getBaiduMapInfo(Long cmtsId);

    Double getCmcOptRecPower(Long cmtsId);

    void refreshUpChannel(Long cmtsId);

    void refreshDownChannel(Long cmtsId);


}
