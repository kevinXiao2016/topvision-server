/***********************************************************************
 * $Id: CmcDhcpOption60.java,v1.0 2012-8-27 下午20:30:28 $
 * 
 * @author: leo
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
 * @author leo
 * @created @2012-8-27-下午20:30:28
 * 
 */
@Alias("cmcDhcpOption60")
public class CmcDhcpOption60 implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6920907595123276905L;
    private Long cmcId;
    private Long entityId;
    private Long option60Id;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;    
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.5.1.1", index = true)
    private Integer topCcmtsDhcpOption60DeviceType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.5.1.2", index = true)
    private Integer topCcmtsDhcpOption60Index;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.5.1.3", writable = true, type = "OctetString")
    private String topCcmtsDhcpOption60Str;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.5.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpOption60Status;

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

    public Long getOption60Id() {
        return option60Id;
    }

    public void setOption60Id(Long option60Id) {
        this.option60Id = option60Id;
    }

    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }

    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
    }

    public Integer getTopCcmtsDhcpOption60DeviceType() {
        return topCcmtsDhcpOption60DeviceType;
    }

    public void setTopCcmtsDhcpOption60DeviceType(Integer topCcmtsDhcpOption60DeviceType) {
        this.topCcmtsDhcpOption60DeviceType = topCcmtsDhcpOption60DeviceType;
    }

    public Integer getTopCcmtsDhcpOption60Index() {
        return topCcmtsDhcpOption60Index;
    }

    public void setTopCcmtsDhcpOption60Index(Integer topCcmtsDhcpOption60Index) {
        this.topCcmtsDhcpOption60Index = topCcmtsDhcpOption60Index;
    }

    public String getTopCcmtsDhcpOption60Str() {
        return topCcmtsDhcpOption60Str;
    }

    public void setTopCcmtsDhcpOption60Str(String topCcmtsDhcpOption60Str) {
        this.topCcmtsDhcpOption60Str = topCcmtsDhcpOption60Str;
    }

    public Integer getTopCcmtsDhcpOption60Status() {
        return topCcmtsDhcpOption60Status;
    }

    public void setTopCcmtsDhcpOption60Status(Integer topCcmtsDhcpOption60Status) {
        this.topCcmtsDhcpOption60Status = topCcmtsDhcpOption60Status;
    }

    public String convertBundleInterfaceToString(String bundleInterface) {
        String result = "";
        if (bundleInterface == null) {
            return result;
        }
        int i = 0;
        for (String s : bundleInterface.split("\\.")) {
            if (i != 0) {
                result += (char) Integer.parseInt(s);
            }
            i++;
        }
        return result;
    }

    public String convertStringToBundleInterface(String str) {
        String result = "";
        if (str.length() > 0) {
            result = result + str.length();
            for (int i = 0; i < str.length(); i++) {
                result = result + Symbol.POINT + (int) str.charAt(i);
            }
        }
        return result;
    }
    
    public CmcDhcpOption60 clone(){
        CmcDhcpOption60 op = new CmcDhcpOption60();
        op.setCmcId(cmcId);
        op.setEntityId(entityId);
        op.setOption60Id(option60Id);
        op.setTopCcmtsDhcpBundleInterface(topCcmtsDhcpBundleInterface);
        op.setTopCcmtsDhcpOption60DeviceType(topCcmtsDhcpOption60DeviceType);
        op.setTopCcmtsDhcpOption60Index(topCcmtsDhcpOption60Index);
        op.setTopCcmtsDhcpOption60Status(topCcmtsDhcpOption60Status);
        op.setTopCcmtsDhcpOption60Str(topCcmtsDhcpOption60Str);
        return op;        
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpOption60 [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsDhcpBundleInterface=");
        builder.append(topCcmtsDhcpBundleInterface);
        builder.append(", topCcmtsDhcpOption60DeviceType=");
        builder.append(topCcmtsDhcpOption60DeviceType);
        builder.append(", topCcmtsDhcpOption60Index=");
        builder.append(topCcmtsDhcpOption60Index);
        builder.append(", topCcmtsDhcpOption60Str=");
        builder.append(topCcmtsDhcpOption60Str);
        builder.append(", topCcmtsDhcpOption60Status=");
        builder.append(topCcmtsDhcpOption60Status);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
