/**
 * 
 */
package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author dosion
 * 
 */
public class CmcSysControl implements Serializable {
    private static final long serialVersionUID = 2089668078882891009L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.2.1.1", writable = true, type = "Integer32")
    private Integer topCcmtsSysResetNow;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.2.1.2", writable = true, type = "BITS")
    private String topCcmtsSysTrapControl;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.2.1.3", writable = true, type = "Integer32")
    private Integer topCcmtsSysNoMacBind;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getTopCcmtsSysResetNow() {
        return topCcmtsSysResetNow;
    }

    public void setTopCcmtsSysResetNow(Integer topCcmtsSysResetNow) {
        this.topCcmtsSysResetNow = topCcmtsSysResetNow;
    }

    public String getTopCcmtsSysTrapControl() {
        return topCcmtsSysTrapControl;
    }

    public void setTopCcmtsSysTrapControl(String topCcmtsSysTrapControl) {
        this.topCcmtsSysTrapControl = topCcmtsSysTrapControl;
    }

    /**
     * @return the topCcmtsSysNoMacBind
     */
    public Integer getTopCcmtsSysNoMacBind() {
        return topCcmtsSysNoMacBind;
    }

    /**
     * @param topCcmtsSysNoMacBind
     *            the topCcmtsSysNoMacBind to set
     */
    public void setTopCcmtsSysNoMacBind(Integer topCcmtsSysNoMacBind) {
        this.topCcmtsSysNoMacBind = topCcmtsSysNoMacBind;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSysControl [cmcId=");
        builder.append(cmcId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", topCcmtsSysResetNow=");
        builder.append(topCcmtsSysResetNow);
        builder.append(", topCcmtsSysTrapControl=");
        builder.append(topCcmtsSysTrapControl);
        builder.append(", topCcmtsSysNoMacBind=");
        builder.append(topCcmtsSysNoMacBind);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
