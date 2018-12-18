/***********************************************************************
 * $Id: Cm3UsRemoteQuery.java,v1.0 2014-2-9 下午2:12:28 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * CM 3.0 上行信道 RemoteQuery方式查询 Domain
 * 
 * @author YangYi
 * @created @2014-2-9-下午2:12:28
 * 
 */
public class Cm3UsRemoteQuery implements Serializable, Comparable<Cm3UsRemoteQuery> {
    private static final long serialVersionUID = -6555344799539128417L;
    private final static DecimalFormat df = new DecimalFormat("0.0");
    private Long cmcId; // 所属Cmc的Id
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.1", index = true)
    private Long cmIndex; // CM的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.5.1.1", index = true)
    private Long cmUsIndex; // CM上行信道的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.5.1.2")
    private Long cmUsChanId; // 上行信道ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.5.1.3")
    private Long cmUsTxPower; // 3.0CM 上行发射电平

    private String cmUsTxPowerString;// 上行发射电平,转换后
    private String cmUsTxPowerStringFordBmV;// 上行发射电平,转换后

    @Override
    public int compareTo(Cm3UsRemoteQuery o) {
        if (o != null) {
            if (this.getCmUsChanId().longValue() > o.getCmUsChanId().longValue()) {
                return 1;
            } else if (this.getCmUsChanId().longValue() == o.getCmUsChanId().longValue()) {
                return 0;
            }
        }
        return -1;
    }

    public String getCmUsTxPowerString() {
        if (this.getCmUsTxPower() != null) {
            //cmUsTxPowerString = df.format((double) this.getCmUsTxPower() / 10);
            double powerValue = UnitConfigConstant.parsePowerValue((double) this.getCmUsTxPower() / 10);
            cmUsTxPowerString = df.format(powerValue);
        } else {
            cmUsTxPowerString = "";
        }
        return cmUsTxPowerString;
    }

    public void setCmUsTxPowerString(String cmUsTxPowerString) {
        this.cmUsTxPowerString = cmUsTxPowerString;
    }

    public String getCmUsTxPowerStringFordBmV() {
        if (this.getCmUsTxPower() != null) {
            cmUsTxPowerStringFordBmV = df.format(getCmUsTxPower() / 10);
        } else {
            cmUsTxPowerStringFordBmV = "";
        }
        return cmUsTxPowerStringFordBmV;
    }

    public void setCmUsTxPowerStringFordBmV(String cmUsTxPowerStringFordBmV) {
        this.cmUsTxPowerStringFordBmV = cmUsTxPowerStringFordBmV;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmUsIndex() {
        return cmUsIndex;
    }

    public void setCmUsIndex(Long cmUsIndex) {
        this.cmUsIndex = cmUsIndex;
    }

    public Long getCmUsChanId() {
        return cmUsChanId;
    }

    public void setCmUsChanId(Long cmUsChanId) {
        this.cmUsChanId = cmUsChanId;
    }

    public Long getCmUsTxPower() {
        return cmUsTxPower;
    }

    public void setCmUsTxPower(Long cmUsTxPower) {
        this.cmUsTxPower = cmUsTxPower;
    }

}
