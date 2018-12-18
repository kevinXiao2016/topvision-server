package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author liubing
 * @created @2012-05-10-上午12:31:14
 * 
 */
@Alias("docsDevEvControl")
public class DocsDevEvControl implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6600798334666353684L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.5.7.1.1", index = true)
    private Integer docsDevEvPriority; // The priority level that is controlled by this entry,
                                       // emergency(1)、alert(2)、critical(3)、error(4)、warning(5)、notice(6)、information(7)、debug(8)
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.5.7.1.2", writable = true)
    private String docsDevEvReporting; // Defines the action to be taken on occurrence of this event
                                       // class,
                                       // local(0)、traps(1)、syslog(2)、ignore3(3)、ignore4(4)、ignore5(5)、ignore6(6)、ignore7(7)、localVolatile(8)、stdInterface(9)

    private String docsDevEvReportingHex;
    private int localnonvol;
    private int traps;
    private int syslog;
    private int ignore3;
    private int ignore4;
    private int ignore5;
    private int ignore6;
    private int ignore7;
    private int localVolatile;
    private int stdInterface;

    public void calDocsDevEvReportingHex() {
        int tempNum = 0;
        StringBuffer docsDevEvReportingStringBuffer = new StringBuffer();
        if (localnonvol == 1) {
            tempNum = replace(tempNum, 4);
        }
        if (traps == 1) {
            tempNum = replace(tempNum, 3);
        }
        if (syslog == 1) {
            tempNum = replace(tempNum, 2);
        }
        if (ignore3 == 1) {
            tempNum = replace(tempNum, 1);
        }
        docsDevEvReportingStringBuffer.append("0x");
        docsDevEvReportingStringBuffer.append(Integer.toHexString(tempNum));
        tempNum = 0;
        if (ignore4 == 1) {
            tempNum = replace(tempNum, 4);
        }
        if (ignore5 == 1) {
            tempNum = replace(tempNum, 3);
        }
        if (ignore6 == 1) {
            tempNum = replace(tempNum, 2);
        }
        if (ignore7 == 1) {
            tempNum = replace(tempNum, 1);
        }
        docsDevEvReportingStringBuffer.append(Integer.toHexString(tempNum));
        docsDevEvReportingStringBuffer.append(Symbol.COLON);
        tempNum = 0;
        if (localVolatile == 1) {
            tempNum = replace(tempNum, 4);
        }
        if (stdInterface == 1) {
            tempNum = replace(tempNum, 3);
        }
        docsDevEvReportingStringBuffer.append(Integer.toHexString(tempNum));
        docsDevEvReportingStringBuffer.append("0:00:00");
        docsDevEvReportingHex = docsDevEvReportingStringBuffer.toString();
    }

    public static int replace(int source, int index) {
        int result = 1;
        while (--index != 0) {
            result = result << 1;
        }
        return source | result;
    }

    public void calDocsDevEvReportingType() {
        String result = "";
        for (String s : docsDevEvReporting.split(Symbol.COLON)) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result += String.format("%08d", str2);
        }
        if (result.charAt(0) == '1') {
            localnonvol = 1;
        }
        if (result.charAt(1) == '1') {
            traps = 1;
        }
        if (result.charAt(2) == '1') {
            syslog = 1;
        }
        if (result.charAt(8) == '1') {
            localVolatile = 1;
        }
        if (result.charAt(9) == '1') {
            stdInterface = 1;
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getDocsDevEvPriority() {
        return docsDevEvPriority;
    }

    public void setDocsDevEvPriority(Integer docsDevEvPriority) {
        this.docsDevEvPriority = docsDevEvPriority;
    }

    public String getDocsDevEvReporting() {
        return docsDevEvReporting;
    }

    public void setDocsDevEvReporting(String docsDevEvReporting) {
        this.docsDevEvReporting = docsDevEvReporting;
    }

    public int getLocalnonvol() {
        return localnonvol;
    }

    public void setLocalnonvol(int localnonvol) {
        this.localnonvol = localnonvol;
    }

    public int getTraps() {
        return traps;
    }

    public void setTraps(int traps) {
        this.traps = traps;
    }

    public int getSyslog() {
        return syslog;
    }

    public void setSyslog(int syslog) {
        this.syslog = syslog;
    }

    public int getLocalVolatile() {
        return localVolatile;
    }

    public void setLocalVolatile(int localVolatile) {
        this.localVolatile = localVolatile;
    }

    public int getIgnore3() {
        return ignore3;
    }

    public void setIgnore3(int ignore3) {
        this.ignore3 = ignore3;
    }

    public int getIgnore4() {
        return ignore4;
    }

    public void setIgnore4(int ignore4) {
        this.ignore4 = ignore4;
    }

    public int getIgnore5() {
        return ignore5;
    }

    public void setIgnore5(int ignore5) {
        this.ignore5 = ignore5;
    }

    public int getIgnore6() {
        return ignore6;
    }

    public void setIgnore6(int ignore6) {
        this.ignore6 = ignore6;
    }

    public int getIgnore7() {
        return ignore7;
    }

    public void setIgnore7(int ignore7) {
        this.ignore7 = ignore7;
    }

    public int getStdInterface() {
        return stdInterface;
    }

    public void setStdInterface(int stdInterface) {
        this.stdInterface = stdInterface;
    }

    public String getDocsDevEvReportingHex() {
        return docsDevEvReportingHex;
    }

    public void setDocsDevEvReportingHex(String docsDevEvReportingHex) {
        this.docsDevEvReportingHex = docsDevEvReportingHex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DocsDevEvControl [entityId=");
        builder.append(entityId);
        builder.append(", docsDevEvPriority=");
        builder.append(docsDevEvPriority);
        builder.append(", docsDevEvReporting=");
        builder.append(docsDevEvReporting);
        builder.append(", docsDevEvReportingHex=");
        builder.append(docsDevEvReportingHex);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
