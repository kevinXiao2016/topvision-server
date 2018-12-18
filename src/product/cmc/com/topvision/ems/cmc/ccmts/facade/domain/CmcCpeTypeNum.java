/***********************************************************************
 * $ CmcCpeTypeNum.java,v1.0 14-5-12 上午11:19 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author jay
 * @created @14-5-12-上午11:19
 */
@Alias("cmcCpeTypeNum")
public class CmcCpeTypeNum implements Serializable {
    private static final long serialVersionUID = -584965612821026560L;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.2.1.1")
    private Long hostNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.2.1.2")
    private Long mtaNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.2.1.3")
    private Long stbNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.2.1.4")
    private Long extenDevNum;

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getHostNum() {
        return hostNum;
    }

    public void setHostNum(Long hostNum) {
        this.hostNum = hostNum;
    }

    public Long getMtaNum() {
        return mtaNum;
    }

    public void setMtaNum(Long mtaNum) {
        this.mtaNum = mtaNum;
    }

    public Long getStbNum() {
        return stbNum;
    }

    public void setStbNum(Long stbNum) {
        this.stbNum = stbNum;
    }

    public Long getExtenDevNum() {
        return extenDevNum;
    }

    public void setExtenDevNum(Long extenDevNum) {
        this.extenDevNum = extenDevNum;
    }
}
