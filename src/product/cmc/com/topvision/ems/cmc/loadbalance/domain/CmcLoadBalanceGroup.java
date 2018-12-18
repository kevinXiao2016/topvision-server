/***********************************************************************
 * $Id: CmcLoadBalanceGroup.java,v1.0 2013-4-26 上午10:51:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-4-26-上午10:51:30
 *
 */
@Alias("cmcLoadBalanceGroup")
public class CmcLoadBalanceGroup implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 4490578699135002613L;
    private static final long UP_CHANNEL_TYPE = 1L;
    private static final long DOWN_CHANNEL_TYPE = 0L;

    private Long grpId;
    private Long docsLoadBalGrpId;
    private Long devceGrpId;//根据索引解析出来id
    private Long cmcId;
    private String groupName;
    private List<CmcLoadBalChannel> upchannels;
    private List<CmcLoadBalChannel> downchannels;
    private List<CmcLoadBalChannel> channels;
    private List<CmcLoadBalRestrictCm> ranges;
    private String upchannelIds;
    private String downchannelIds;
    
    /**
     * @return the grpId
     */
    public Long getGrpId() {
        return grpId;
    }

    /**
     * @param grpId the grpId to set
     */
    public void setGrpId(Long grpId) {
        this.grpId = grpId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        if(groupName==null||"".equals(groupName)){
            if(devceGrpId==null){
                groupName = "";
            }else{
                groupName = ""+devceGrpId;
            }
            
        }
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    /**
     * @return the ranges
     */
    public List<CmcLoadBalRestrictCm> getRanges() {
        return ranges;
    }

    /**
     * @param ranges the ranges to set
     */
    public void setRanges(List<CmcLoadBalRestrictCm> ranges) {
        this.ranges = ranges;
    }

    /**
     * @return the upchannels
     */
    public List<CmcLoadBalChannel> getUpchannels() {
        return upchannels;
    }

    /**
     * @param upchannels the upchannels to set
     */
    public void setUpchannels(List<CmcLoadBalChannel> upchannels) {
        this.upchannels = upchannels;
    }

    /**
     * @return the downchannels
     */
    public List<CmcLoadBalChannel> getDownchannels() {
        return downchannels;
    }

    /**
     * @param downchannels the downchannels to set
     */
    public void setDownchannels(List<CmcLoadBalChannel> downchannels) {
        this.downchannels = downchannels;
    }

    /**
     * @return the channels
     */
    public List<CmcLoadBalChannel> getChannels() {
        return channels;
    }

    /**
     * @param channels the channels to set
     */
    public void setChannels(List<CmcLoadBalChannel> channels) {
        if (upchannels == null) {
            upchannels = new ArrayList<CmcLoadBalChannel>();
        }
        if (downchannels == null) {
            downchannels = new ArrayList<CmcLoadBalChannel>();
        }
        upchannelIds = "";
        downchannelIds = "";
        for (CmcLoadBalChannel channel : channels) {
            Long ifIndex = channel.getDocsLoadBalChannelIfIndex();
            long channelType = CmcIndexUtils.getChannelType(ifIndex);
            if (channelType == UP_CHANNEL_TYPE) {
                downchannels.add(channel);
                downchannelIds = downchannelIds + CmcIndexUtils.getChannelId(channel.getDocsLoadBalChannelIfIndex())
                        + ",";
            } else if (channelType == DOWN_CHANNEL_TYPE) {
                upchannels.add(channel);
                upchannelIds = upchannelIds + CmcIndexUtils.getChannelId(channel.getDocsLoadBalChannelIfIndex()) + ",";
            }
        }
        if(upchannelIds != null && upchannelIds.length() > 1){
        	upchannelIds = upchannelIds.trim().substring(0, upchannelIds.length() - 1);//去掉逗号
        }
        if(downchannelIds != null && downchannelIds.length() > 1){
        	downchannelIds = downchannelIds.trim().substring(0, downchannelIds.length() - 1);
        }
        this.channels = channels;
    }

	public String getUpchannelIds() {
		return upchannelIds;
	}

	public void setUpchannelIds(String upchannelIds) {
		this.upchannelIds = upchannelIds;
	}

	public String getDownchannelIds() {
		return downchannelIds;
	}

	public void setDownchannelIds(String downchannelIds) {
		this.downchannelIds = downchannelIds;
	}

	public Long getDocsLoadBalGrpId() {
		return docsLoadBalGrpId;
	}

	public void setDocsLoadBalGrpId(Long docsLoadBalGrpId) {
		this.docsLoadBalGrpId = docsLoadBalGrpId;
		this.devceGrpId = CmcIndexUtils.getGroupIdFromDocsLoadBalGrpId(docsLoadBalGrpId);
	}

	public Long getDevceGrpId() {
		return devceGrpId;
	}

	public void setDevceGrpId(Long devceGrpId) {
		this.devceGrpId = devceGrpId;
	}

}
