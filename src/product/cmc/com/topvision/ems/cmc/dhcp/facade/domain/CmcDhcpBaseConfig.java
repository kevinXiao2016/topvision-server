/***********************************************************************
 * $Id: CmcDhcpBaseConfig.java,v1.0 2012-2-13 下午05:00:56 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午05:00:56
 * 
 */
@Alias("cmcDhcpBaseConfig")
public class CmcDhcpBaseConfig implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -1158510720254668584L;
    private Long cmcId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.1.1.0", writable = true, type = "Integer32")
    private Integer cableSourceVerify;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.1.2.0", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpAlloc;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.1.3.0", writable = true, type = "OctetString")
    private String topCcmtsDhcpMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.1.4.0", writable = true, type = "OctetString")
    private String topCcmtsDhcpAllocOption60;
    private Integer cmMode;
    private Integer hostMode;
    private Integer mtaMode;
    private Integer stbMode;
    private int host;
    private int mta;
    private int stb;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopCcmtsDhcpAlloc() {
        return topCcmtsDhcpAlloc;
    }

    public void setTopCcmtsDhcpAlloc(Integer topCcmtsDhcpAlloc) {
        this.topCcmtsDhcpAlloc = topCcmtsDhcpAlloc;
    }

    public String getTopCcmtsDhcpAllocOption60() {
        return topCcmtsDhcpAllocOption60;
    }

    public void setTopCcmtsDhcpAllocOption60(String topCcmtsDhcpAllocOption60) {
        this.topCcmtsDhcpAllocOption60 = topCcmtsDhcpAllocOption60;
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public int getMta() {
        return mta;
    }

    public void setMta(int mta) {
        this.mta = mta;
    }

    public int getStb() {
        return stb;
    }

    public void setStb(int stb) {
        this.stb = stb;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpBaseConfig [cableSourceVerify=");
        builder.append(cableSourceVerify);
        builder.append(", topCcmtsDhcpAlloc=");
        builder.append(topCcmtsDhcpAlloc);
        builder.append(", topCcmtsDhcpMode=");
        builder.append(topCcmtsDhcpMode);
        builder.append(", topCcmtsDhcpAllocOption60=");
        builder.append(topCcmtsDhcpAllocOption60);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    /**
     * @return the topCcmtsDhcpMode
     */
    public String getTopCcmtsDhcpMode() {
        return topCcmtsDhcpMode;
    }

    /**
     * @param topCcmtsDhcpMode
     *            the topCcmtsDhcpMode to set
     */
    public void setTopCcmtsDhcpMode(String topCcmtsDhcpMode) {
        String[] temp = topCcmtsDhcpMode.split("\\:");
        int mode;
        try {
            mode = Integer.parseInt(temp[0], 16);
            this.cmMode = (mode & 0xC0) >> 6;
            this.hostMode = (mode & 0x30) >> 4;
            this.mtaMode = (mode & 0xC) >> 2;
            this.stbMode = mode & 0x3;
        } catch (NumberFormatException e) {
            mode = topCcmtsDhcpMode.charAt(0);
            this.cmMode = (mode & 0xC0) >> 6;
            this.hostMode = (mode & 0x30) >> 4;
            this.mtaMode = (mode & 0xC) >> 2;
            this.stbMode = mode & 0x3;
        }
        this.topCcmtsDhcpMode = topCcmtsDhcpMode;
    }

    /**
     * @return the cableSourceVerify
     */
    public Integer getCableSourceVerify() {
        return cableSourceVerify;
    }

    /**
     * @param cableSourceVerify
     *            the cableSourceVerify to set
     */
    public void setCableSourceVerify(Integer cableSourceVerify) {
        this.cableSourceVerify = cableSourceVerify;
    }

    /**
     * @return the cmMode
     */
    public Integer getCmMode() {
        return cmMode;
    }

    /**
     * @param cmMode
     *            the cmMode to set
     */
    public void setCmMode(Integer cmMode) {
        this.cmMode = cmMode;
    }

    /**
     * @return the hostMode
     */
    public Integer getHostMode() {
        return hostMode;
    }

    /**
     * @param hostMode
     *            the hostMode to set
     */
    public void setHostMode(Integer hostMode) {
        this.hostMode = hostMode;
    }

    /**
     * @return the mtaMode
     */
    public Integer getMtaMode() {
        return mtaMode;
    }

    /**
     * @param mtaMode
     *            the mtaMode to set
     */
    public void setMtaMode(Integer mtaMode) {
        this.mtaMode = mtaMode;
    }

    /**
     * @return the stbMode
     */
    public Integer getStbMode() {
        return stbMode;
    }

    /**
     * @param stbMode
     *            the stbMode to set
     */
    public void setStbMode(Integer stbMode) {
        this.stbMode = stbMode;
    }

}
