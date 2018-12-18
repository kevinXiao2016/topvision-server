/***********************************************************************
 * $Id: CmCpeNumInRegion.java,v1.0 2013-6-24 下午3:12:23 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-6-24-下午3:12:23
 *
 */
@Alias("cmCpeNumInArea")
public class CmCpeNumInArea implements AliasesSuperType {
    private static final long serialVersionUID = -4679761486171668655L;
    private Long areaId;
    private String areaName;
    private Long cmOnlineNum;
    private Long cpeOnLineNum;
    public Long getAreaId() {
        return areaId;
    }
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public Long getCmOnlineNum() {
        return cmOnlineNum;
    }
    public void setCmOnlineNum(Long cmOnlineNum) {
        this.cmOnlineNum = cmOnlineNum;
    }
    public Long getCpeOnLineNum() {
        return cpeOnLineNum;
    }
    public void setCpeOnLineNum(Long cpeOnLineNum) {
        this.cpeOnLineNum = cpeOnLineNum;
    }
    
}
