/***********************************************************************
 * $Id: CmcDownChannelStaticInfo.java,v1.0 2011-10-26 下午02:44:28 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:44:28
 * 
 */
@Alias("cmcDownChannelStaticInfo")
public class CmcDownChannelStaticInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6044583137888083478L;
    private Long cmcId;
    private Long cmcPortId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.10.1.1")
    private Integer ctrId; // MAC域下行通道Id
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.10.1.2")
    private Long ctrTotalBytes; // 有效负载字节数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.10.1.3")
    private Long ctrUsedBytes; // DOCSIS数据字节数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.10.1.4")
    private Long ctrExtTotalBytes; // 64有效负载字节数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.10.1.5")
    private Long ctrExtUsedBytes; // 64DOCSIS数字字节数

    private String docsIfCmtsDownChnlCtrTotalBytesForunit;
    private String docsIfCmtsDownChnlCtrUsedBytesForunit;
    private String docsIfCmtsDownChnlCtrExtTotalBytesForunit;
    private String docsIfCmtsDownChnlCtrExtUsedBytesForunit;

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the docsIfCmtsDownChnlCtrId
     */
    public Integer getCtrId() {
        return ctrId;
    }

    /**
     * @param ctrId
     *            the docsIfCmtsDownChnlCtrId to set
     */
    public void setCtrId(Integer ctrId) {
        this.ctrId = ctrId;
    }

    /**
     * @return the docsIfCmtsDownChnlCtrTotalBytes
     */
    public Long getCtrTotalBytes() {
        return ctrTotalBytes;
    }

    /**
     * @param ctrTotalBytes
     *            the docsIfCmtsDownChnlCtrTotalBytes to set
     */
    public void setCtrTotalBytes(Long ctrTotalBytes) {
        this.ctrTotalBytes = ctrTotalBytes;
        if (this.ctrTotalBytes != null) {
            this.setDocsIfCmtsDownChnlCtrTotalBytesForunit(this.ctrTotalBytes.toString() + " Bytes");

        }
    }

    /**
     * @return the docsIfCmtsDownChnlCtrUsedBytes
     */
    public Long getCtrUsedBytes() {
        return ctrUsedBytes;
    }

    /**
     * @param ctrUsedBytes
     *            the docsIfCmtsDownChnlCtrUsedBytes to set
     */
    public void setCtrUsedBytes(Long ctrUsedBytes) {
        this.ctrUsedBytes = ctrUsedBytes;
        if (this.ctrUsedBytes != null) {
            this.setDocsIfCmtsDownChnlCtrUsedBytesForunit(this.ctrUsedBytes.toString() + " Bytes");

        }
    }

    /**
     * @return the docsIfCmtsDownChnlCtrExtTotalBytes
     */
    public Long getCtrExtTotalBytes() {
        return ctrExtTotalBytes;
    }

    /**
     * @param ctrExtTotalBytes
     *            the docsIfCmtsDownChnlCtrExtTotalBytes to set
     */
    public void setCtrExtTotalBytes(Long ctrExtTotalBytes) {
        this.ctrExtTotalBytes = ctrExtTotalBytes;
        if (this.ctrExtTotalBytes != null) {
            this.setDocsIfCmtsDownChnlCtrExtTotalBytesForunit(this.ctrExtTotalBytes.toString() + " Bytes");

        }
    }

    /**
     * @return the docsIfCmtsDownChnlCtrExtUsedBytes
     */
    public Long getCtrExtUsedBytes() {
        return ctrExtUsedBytes;
    }

    /**
     * @param ctrExtUsedBytes
     *            the docsIfCmtsDownChnlCtrExtUsedBytes to set
     */
    public void setCtrExtUsedBytes(Long ctrExtUsedBytes) {
        this.ctrExtUsedBytes = ctrExtUsedBytes;
        if (this.ctrExtUsedBytes != null) {
            this.setDocsIfCmtsDownChnlCtrExtUsedBytesForunit(this.ctrExtUsedBytes.toString() + " Bytes");

        }
    }

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    /**
     * @return the docsIfCmtsDownChnlCtrTotalBytesForunit
     */
    public String getDocsIfCmtsDownChnlCtrTotalBytesForunit() {
        return docsIfCmtsDownChnlCtrTotalBytesForunit;
    }

    /**
     * @param docsIfCmtsDownChnlCtrTotalBytesForunit
     *            the docsIfCmtsDownChnlCtrTotalBytesForunit to set
     */
    public void setDocsIfCmtsDownChnlCtrTotalBytesForunit(String docsIfCmtsDownChnlCtrTotalBytesForunit) {
        this.docsIfCmtsDownChnlCtrTotalBytesForunit = docsIfCmtsDownChnlCtrTotalBytesForunit;
    }

    /**
     * @return the docsIfCmtsDownChnlCtrUsedBytesForunit
     */
    public String getDocsIfCmtsDownChnlCtrUsedBytesForunit() {
        float usedBytes = 0f;
        if (this.ctrUsedBytes > 1000000) {
            usedBytes = this.ctrUsedBytes;
            usedBytes = usedBytes / 1000000;
            DecimalFormat df = new DecimalFormat("0.000");
            docsIfCmtsDownChnlCtrUsedBytesForunit = df.format(usedBytes) + " M";
        }
        return docsIfCmtsDownChnlCtrUsedBytesForunit;
    }

    /**
     * @param docsIfCmtsDownChnlCtrUsedBytesForunit
     *            the docsIfCmtsDownChnlCtrUsedBytesForunit to set
     */
    public void setDocsIfCmtsDownChnlCtrUsedBytesForunit(String docsIfCmtsDownChnlCtrUsedBytesForunit) {
        this.docsIfCmtsDownChnlCtrUsedBytesForunit = docsIfCmtsDownChnlCtrUsedBytesForunit;
    }

    /**
     * @return the docsIfCmtsDownChnlCtrExtTotalBytesForunit
     */
    public String getDocsIfCmtsDownChnlCtrExtTotalBytesForunit() {
        return docsIfCmtsDownChnlCtrExtTotalBytesForunit;
    }

    /**
     * @param docsIfCmtsDownChnlCtrExtTotalBytesForunit
     *            the docsIfCmtsDownChnlCtrExtTotalBytesForunit to set
     */
    public void setDocsIfCmtsDownChnlCtrExtTotalBytesForunit(String docsIfCmtsDownChnlCtrExtTotalBytesForunit) {
        this.docsIfCmtsDownChnlCtrExtTotalBytesForunit = docsIfCmtsDownChnlCtrExtTotalBytesForunit;
    }

    /**
     * @return the docsIfCmtsDownChnlCtrExtUsedBytesForunit
     */
    public String getDocsIfCmtsDownChnlCtrExtUsedBytesForunit() {
        float extUsedBytes = 0f;
        if (this.ctrExtUsedBytes != null && this.ctrExtUsedBytes > 1000000) {
            extUsedBytes = this.ctrExtUsedBytes;
            extUsedBytes = extUsedBytes / 1000000;
            DecimalFormat df = new DecimalFormat("0.000");
            docsIfCmtsDownChnlCtrExtUsedBytesForunit = df.format(extUsedBytes) + " M";
        }
        return docsIfCmtsDownChnlCtrExtUsedBytesForunit;
    }

    /**
     * @param docsIfCmtsDownChnlCtrExtUsedBytesForunit
     *            the docsIfCmtsDownChnlCtrExtUsedBytesForunit to set
     */
    public void setDocsIfCmtsDownChnlCtrExtUsedBytesForunit(String docsIfCmtsDownChnlCtrExtUsedBytesForunit) {
        this.docsIfCmtsDownChnlCtrExtUsedBytesForunit = docsIfCmtsDownChnlCtrExtUsedBytesForunit;
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDownChannelStaticInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", ctrId=");
        builder.append(ctrId);
        builder.append(", ctrTotalBytes=");
        builder.append(ctrTotalBytes);
        builder.append(", ctrUsedBytes=");
        builder.append(ctrUsedBytes);
        builder.append(", ctrExtTotalBytes=");
        builder.append(ctrExtTotalBytes);
        builder.append(", ctrExtUsedBytes=");
        builder.append(ctrExtUsedBytes);
        builder.append(", docsIfCmtsDownChnlCtrTotalBytesForunit=");
        builder.append(docsIfCmtsDownChnlCtrTotalBytesForunit);
        builder.append(", docsIfCmtsDownChnlCtrUsedBytesForunit=");
        builder.append(docsIfCmtsDownChnlCtrUsedBytesForunit);
        builder.append(", docsIfCmtsDownChnlCtrExtTotalBytesForunit=");
        builder.append(docsIfCmtsDownChnlCtrExtTotalBytesForunit);
        builder.append(", docsIfCmtsDownChnlCtrExtUsedBytesForunit=");
        builder.append(docsIfCmtsDownChnlCtrExtUsedBytesForunit);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
