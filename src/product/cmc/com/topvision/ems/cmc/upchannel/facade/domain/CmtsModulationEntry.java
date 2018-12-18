package com.topvision.ems.cmc.upchannel.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmtsModulationEntry")
public class CmtsModulationEntry implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2036164886141155339L;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.1", index = true)
    private Long docsIfCmtsModIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.2", index = true)
    private Long docsIfCmtsModIntervalUsageCode;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.4")
    private Long docsIfCmtsModType;

    @SuppressWarnings("unused")
    private String modTypeString;

    public String getModTypeString() {
        switch (docsIfCmtsModType.intValue()) {
        case 1:
            return "other";
        case 2:
            return "qpsk";
        case 3:
            return "qam16";
        case 4:
            return "qam8";
        case 5:
            return "qam32";
        case 6:
            return "qam64";
        case 7:
            return "qam128";
        default:
            return "other";
        }
    }

    public void setModTypeString(String modTypeString) {
        this.modTypeString = modTypeString;
    }

    public Long getDocsIfCmtsModIndex() {
        return docsIfCmtsModIndex;
    }

    public Long getDocsIfCmtsModIntervalUsageCode() {
        return docsIfCmtsModIntervalUsageCode;
    }

    public Long getDocsIfCmtsModType() {
        return docsIfCmtsModType;
    }

    public void setDocsIfCmtsModIndex(Long docsIfCmtsModIndex) {
        this.docsIfCmtsModIndex = docsIfCmtsModIndex;
    }

    public void setDocsIfCmtsModIntervalUsageCode(Long docsIfCmtsModIntervalUsageCode) {
        this.docsIfCmtsModIntervalUsageCode = docsIfCmtsModIntervalUsageCode;
    }

    public void setDocsIfCmtsModType(Long docsIfCmtsModType) {
        this.docsIfCmtsModType = docsIfCmtsModType;
    }

}
