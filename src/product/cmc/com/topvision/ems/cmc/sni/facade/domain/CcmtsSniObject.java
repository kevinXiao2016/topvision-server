package com.topvision.ems.cmc.sni.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("ccmtsSniObject")
public class CcmtsSniObject implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1L;
    private Long cmcId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.1.0", writable = true, type = "Integer32")
    private Integer topCcmtsSniEthInt;// 上联口 主备设置，设备暂不支持该节点
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.2.0", writable = true, type = "Integer32")
    private Integer topCcmtsSniMainInt;// 主上联口链路介质选择：copper(1)、fiber(2)、autoCopperPreferred(3)、autoFiberPreferred(4)，暂不支持3、4
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.3.0", writable = true, type = "Integer32")
    private Integer topCcmtsSniBackupInt;// 备上联口链路介质选择：copper(1)、fiber(2)、autoCopperPreferred(3)、autoFiberPreferred(4)暂不支持3、4

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
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the topCcmtsSniEthInt
     */
    public Integer getTopCcmtsSniEthInt() {
        return topCcmtsSniEthInt;
    }

    /**
     * @param topCcmtsSniEthInt
     *            the topCcmtsSniEthInt to set
     */
    public void setTopCcmtsSniEthInt(Integer topCcmtsSniEthInt) {
        this.topCcmtsSniEthInt = topCcmtsSniEthInt;
    }

    /**
     * @return the topCcmtsSniMainInt
     */
    public Integer getTopCcmtsSniMainInt() {
        return topCcmtsSniMainInt;
    }

    /**
     * @param topCcmtsSniMainInt
     *            the topCcmtsSniMainInt to set
     */
    public void setTopCcmtsSniMainInt(Integer topCcmtsSniMainInt) {
        this.topCcmtsSniMainInt = topCcmtsSniMainInt;
    }

    /**
     * @return the topCcmtsSniBackupInt
     */
    public Integer getTopCcmtsSniBackupInt() {
        return topCcmtsSniBackupInt;
    }

    /**
     * @param topCcmtsSniBackupInt
     *            the topCcmtsSniBackupInt to set
     */
    public void setTopCcmtsSniBackupInt(Integer topCcmtsSniBackupInt) {
        this.topCcmtsSniBackupInt = topCcmtsSniBackupInt;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSniObject [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsSniEthInt=");
        builder.append(topCcmtsSniEthInt);
        builder.append(", topCcmtsSniMainInt=");
        builder.append(topCcmtsSniMainInt);
        builder.append(", topCcmtsSniBackupInt=");
        builder.append(topCcmtsSniBackupInt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
