package com.topvision.ems.cm.cmtsInfoSummary.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmMainChannel implements AliasesSuperType {
    private static final long serialVersionUID = -178359843825790908L;
    
    private Long cmId;
    private Integer cmMainUpChannel;
    private Integer cmMainDownChannel;
    public Long getCmId() {
        return cmId;
    }
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }
    public Integer getCmMainUpChannel() {
        return cmMainUpChannel;
    }
    public void setCmMainUpChannel(Integer cmMainUpChannel) {
        this.cmMainUpChannel = cmMainUpChannel;
    }
    public Integer getCmMainDownChannel() {
        return cmMainDownChannel;
    }
    public void setCmMainDownChannel(Integer cmMainDownChannel) {
        this.cmMainDownChannel = cmMainDownChannel;
    }

}
