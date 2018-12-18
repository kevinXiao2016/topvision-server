package com.topvision.ems.cmc.spectrum.domain;

import java.util.List;

import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;

public class SpectrumVideoFrameData {
    private Long startFreq = null;
    private Long endFreq = null;
    private List<List<Number>> dataList;
    private List<CmcUpChannelBaseShowInfo> channelList;

    public List<List<Number>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<Number>> dataList) {
        this.dataList = dataList;
    }

    public List<CmcUpChannelBaseShowInfo> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<CmcUpChannelBaseShowInfo> channelList) {
        this.channelList = channelList;
    }

    public Long getStartFreq() {
        return startFreq;
    }

    public void setStartFreq(Long startFreq) {
        this.startFreq = startFreq;
    }

    public Long getEndFreq() {
        return endFreq;
    }

    public void setEndFreq(Long endFreq) {
        this.endFreq = endFreq;
    }
}
