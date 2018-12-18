/***********************************************************************
 * $Id: CmcUpChannelCounterInfo.java,v1.0 2011-10-26 下午02:25:50 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:25:50
 * 
 */
@Alias("cmcUpChannelCounterInfo")
public class CmcUpChannelCounterInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2263364325984935621L;
    private Long cmcId;
    private Long cmcPortId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.1")
    private Integer ctrId; // 上行channelId
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.2")
    private Long ctrTotalMslots; // 所有minislots数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.3")
    private Long ctrUcastGrantedMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.4")
    private Long ctrTotalCntnMslots; // 所有竞争minislots数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.5")
    private Long ctrUsedCntnMslots; // 已使用竞争minislots数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.6")
    private Long ctrExtTotalMslots; // 64位所有minislots数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.7")
    private Long ctrExtUcastGrantedMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.8")
    private Long ctrExtTotalCntnMslots; // 64位所有竞争minislots数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.9")
    private Long ctrExtUsedCntnMslots; // 64位已使用竞争minislots数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.10")
    private Long ctrCollCntnMslots; // 竞争minislots碰撞计数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.11")
    private Long ctrTotalCntnReqMslots; // 所有contention request
                                        // mini-slots
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.12")
    private Long ctrUsedCntnReqMslots; // 已利用contention request
                                       // mini-slots
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.13")
    private Long ctrCollCntnReqMslots; // 碰撞contention request
                                       // mini-slots
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.14")
    private Long ctrTotalCntnReqDataMslots; // request data
                                            // mini-slots总数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.15")
    private Long ctrUsedCntnReqDataMslots; // request data
                                           // mini-slots的利用率
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.16")
    private Long ctrCollCntnReqDataMslots; // IUC2碰撞的minislots计数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.17")
    private Long ctrTotalCntnInitMaintMslots; //
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.18")
    private Long ctrUsedCntnInitMaintMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.19")
    private Long ctrCollCntnInitMaintMslots; // IU3碰撞minislots计数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.20")
    private Long ctrExtCollCntnMslots; // 64位已使用竞争minislots
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.21")
    private Long ctrExtTotalCntnReqMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.22")
    private Long ctrExtUsedCntnReqMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.23")
    private Long ctrExtCollCntnReqMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.24")
    private Long ctrExtTotalCntnReqDataMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.25")
    private Long ctrExtUsedCntnReqDataMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.26")
    private Long ctrExtCollCntnReqDataMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.27")
    private Long ctrExtTotalCntnInitMaintMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.28")
    private Long ctrExtUsedCntnInitMaintMslots;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.11.1.29")
    private Long ctrExtCollCntnInitMaintMslots;
    // For unit
    private String docsIfCmtsUpChnlCtrTotalMslotsForunit; // 所有minislots数
    private String docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit;
    private String docsIfCmtsUpChnlCtrTotalCntnMslotsForunit; // 所有竞争minislots数
    private String docsIfCmtsUpChnlCtrUsedCntnMslotsForunit; // 已使用竞争minislots数
    private String docsIfCmtsUpChnlCtrExtTotalMslotsForunit; // 64位所有minislots数
    private String docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit; // 64位所有竞争minislots数
    private String docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit; // 64位已使用竞争minislots数
    private String docsIfCmtsUpChnlCtrCollCntnMslotsForunit; // 竞争minislots碰撞计数
    private String docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit; // 所有contention
                                                                 // request
                                                                 // mini-slots
    private String docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit; // 已利用contention
                                                                // request
                                                                // mini-slots
    private String docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit; // 碰撞contention
                                                                // request
                                                                // mini-slots
    private String docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit; // request
                                                                     // data
                                                                     // mini-slots总数
    private String docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit; // request
                                                                    // data
                                                                    // mini-slots的利用率
    private String docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit; // IUC2碰撞的minislots计数
    private String docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit; //
    private String docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit;
    private String docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit; // IU3碰撞minislots计数
    private String docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit; // 64位已使用竞争minislots
    private String docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit;
    private String docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit;

    /**
     * 增加单位，for所有的的String
     */
    private String transToStringUnit(Long l) {
        String str = l.toString();
        return str + "mini-slots";
    }

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
     * @return the docsIfCmtsUpChnlCtrId
     */
    public Integer getCtrId() {
        return ctrId;
    }

    /**
     * @param ctrId
     *            the docsIfCmtsUpChnlCtrId to set
     */
    public void setCtrId(Integer ctrId) {
        this.ctrId = ctrId;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalMslots
     */
    public Long getCtrTotalMslots() {
        return ctrTotalMslots;
    }

    /**
     * @param ctrTotalMslots
     *            the docsIfCmtsUpChnlCtrTotalMslots to set
     */
    public void setCtrTotalMslots(Long ctrTotalMslots) {
        this.ctrTotalMslots = ctrTotalMslots;
        if (this.ctrTotalMslots != null) {
            this.docsIfCmtsUpChnlCtrTotalMslotsForunit = transToStringUnit(ctrTotalMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUcastGrantedMslots
     */
    public Long getCtrUcastGrantedMslots() {
        return ctrUcastGrantedMslots;
    }

    /**
     * @param ctrUcastGrantedMslots
     *            the docsIfCmtsUpChnlCtrUcastGrantedMslots to set
     */
    public void setCtrUcastGrantedMslots(Long ctrUcastGrantedMslots) {
        this.ctrUcastGrantedMslots = ctrUcastGrantedMslots;
        if (this.ctrUcastGrantedMslots != null) {
            this.docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit = transToStringUnit(ctrUcastGrantedMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnMslots
     */
    public Long getCtrTotalCntnMslots() {
        return ctrTotalCntnMslots;
    }

    /**
     * @param ctrTotalCntnMslots
     *            the docsIfCmtsUpChnlCtrTotalCntnMslots to set
     */
    public void setCtrTotalCntnMslots(Long ctrTotalCntnMslots) {
        this.ctrTotalCntnMslots = ctrTotalCntnMslots;
        if (this.ctrTotalCntnMslots != null) {
            this.docsIfCmtsUpChnlCtrTotalCntnMslotsForunit = transToStringUnit(ctrTotalCntnMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUcastGrantedMslots
     */
    public Long getCtrExtUcastGrantedMslots() {
        return ctrExtUcastGrantedMslots;
    }

    /**
     * @param ctrExtUcastGrantedMslots
     *            the docsIfCmtsUpChnlCtrExtUcastGrantedMslots to set
     */
    public void setCtrExtUcastGrantedMslots(Long ctrExtUcastGrantedMslots) {
        this.ctrExtUcastGrantedMslots = ctrExtUcastGrantedMslots;
        if (this.ctrExtUcastGrantedMslots != null) {
            this.docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit = transToStringUnit(ctrExtUcastGrantedMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnMslots
     */
    public Long getCtrUsedCntnMslots() {
        return ctrUsedCntnMslots;
    }

    /**
     * @param ctrUsedCntnMslots
     *            the docsIfCmtsUpChnlCtrUsedCntnMslots to set
     */
    public void setCtrUsedCntnMslots(Long ctrUsedCntnMslots) {
        this.ctrUsedCntnMslots = ctrUsedCntnMslots;
        if (this.ctrUsedCntnMslots != null) {
            this.docsIfCmtsUpChnlCtrUsedCntnMslotsForunit = transToStringUnit(ctrUsedCntnMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalMslots
     */
    public Long getCtrExtTotalMslots() {
        return ctrExtTotalMslots;
    }

    /**
     * @param ctrExtTotalMslots
     *            the docsIfCmtsUpChnlCtrExtTotalMslots to set
     */
    public void setCtrExtTotalMslots(Long ctrExtTotalMslots) {
        this.ctrExtTotalMslots = ctrExtTotalMslots;
        if (this.ctrExtTotalMslots != null) {
            this.docsIfCmtsUpChnlCtrExtTotalMslotsForunit = transToStringUnit(ctrExtTotalMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnMslots
     */
    public Long getCtrExtTotalCntnMslots() {
        return ctrExtTotalCntnMslots;
    }

    /**
     * @param ctrExtTotalCntnMslots
     *            the docsIfCmtsUpChnlCtrExtTotalCntnMslots to set
     */
    public void setCtrExtTotalCntnMslots(Long ctrExtTotalCntnMslots) {
        this.ctrExtTotalCntnMslots = ctrExtTotalCntnMslots;
        if (this.ctrExtTotalCntnMslots != null) {
            this.docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit = transToStringUnit(ctrExtTotalCntnMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnMslots
     */
    public Long getCtrExtUsedCntnMslots() {
        return ctrExtUsedCntnMslots;
    }

    /**
     * @param ctrExtUsedCntnMslots
     *            the docsIfCmtsUpChnlCtrExtUsedCntnMslots to set
     */
    public void setCtrExtUsedCntnMslots(Long ctrExtUsedCntnMslots) {
        this.ctrExtUsedCntnMslots = ctrExtUsedCntnMslots;
        if (this.ctrExtUsedCntnMslots != null) {
            this.docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit = transToStringUnit(ctrExtUsedCntnMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnMslots
     */
    public Long getCtrCollCntnMslots() {
        return ctrCollCntnMslots;
    }

    /**
     * @param ctrCollCntnMslots
     *            the docsIfCmtsUpChnlCtrCollCntnMslots to set
     */
    public void setCtrCollCntnMslots(Long ctrCollCntnMslots) {
        this.ctrCollCntnMslots = ctrCollCntnMslots;
        if (this.ctrCollCntnMslots != null) {
            this.docsIfCmtsUpChnlCtrCollCntnMslotsForunit = transToStringUnit(ctrCollCntnMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnReqMslots
     */
    public Long getCtrTotalCntnReqMslots() {
        return ctrTotalCntnReqMslots;
    }

    /**
     * @param ctrTotalCntnReqMslots
     *            the docsIfCmtsUpChnlCtrTotalCntnReqMslots to set
     */
    public void setCtrTotalCntnReqMslots(Long ctrTotalCntnReqMslots) {
        this.ctrTotalCntnReqMslots = ctrTotalCntnReqMslots;
        if (this.ctrTotalCntnReqMslots != null) {
            this.docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit = transToStringUnit(ctrTotalCntnReqMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnReqMslots
     */
    public Long getCtrUsedCntnReqMslots() {
        return ctrUsedCntnReqMslots;
    }

    /**
     * @param ctrUsedCntnReqMslots
     *            the docsIfCmtsUpChnlCtrUsedCntnReqMslots to set
     */
    public void setCtrUsedCntnReqMslots(Long ctrUsedCntnReqMslots) {
        this.ctrUsedCntnReqMslots = ctrUsedCntnReqMslots;
        if (this.ctrUsedCntnReqMslots != null) {
            this.docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit = transToStringUnit(ctrUsedCntnReqMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnReqMslots
     */
    public Long getCtrCollCntnReqMslots() {
        return ctrCollCntnReqMslots;
    }

    /**
     * @param ctrCollCntnReqMslots
     *            the docsIfCmtsUpChnlCtrCollCntnReqMslots to set
     */
    public void setCtrCollCntnReqMslots(Long ctrCollCntnReqMslots) {
        this.ctrCollCntnReqMslots = ctrCollCntnReqMslots;
        if (this.ctrCollCntnReqMslots != null) {
            this.docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit = transToStringUnit(ctrCollCntnReqMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnReqDataMslots
     */
    public Long getCtrTotalCntnReqDataMslots() {
        return ctrTotalCntnReqDataMslots;
    }

    /**
     * @param ctrTotalCntnReqDataMslots
     *            the docsIfCmtsUpChnlCtrTotalCntnReqDataMslots to set
     */
    public void setCtrTotalCntnReqDataMslots(Long ctrTotalCntnReqDataMslots) {
        this.ctrTotalCntnReqDataMslots = ctrTotalCntnReqDataMslots;
        if (this.ctrTotalCntnReqDataMslots != null) {
            this.docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit = transToStringUnit(ctrTotalCntnReqDataMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnReqDataMslots
     */
    public Long getCtrUsedCntnReqDataMslots() {
        return ctrUsedCntnReqDataMslots;
    }

    /**
     * @param ctrUsedCntnReqDataMslots
     *            the docsIfCmtsUpChnlCtrUsedCntnReqDataMslots to set
     */
    public void setCtrUsedCntnReqDataMslots(Long ctrUsedCntnReqDataMslots) {
        this.ctrUsedCntnReqDataMslots = ctrUsedCntnReqDataMslots;
        if (this.ctrUsedCntnReqDataMslots != null) {
            this.docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit = transToStringUnit(ctrUsedCntnReqDataMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnReqDataMslots
     */
    public Long getCtrCollCntnReqDataMslots() {
        return ctrCollCntnReqDataMslots;
    }

    /**
     * @param ctrCollCntnReqDataMslots
     *            the docsIfCmtsUpChnlCtrCollCntnReqDataMslots to set
     */
    public void setCtrCollCntnReqDataMslots(Long ctrCollCntnReqDataMslots) {
        this.ctrCollCntnReqDataMslots = ctrCollCntnReqDataMslots;
        if (this.ctrCollCntnReqDataMslots != null) {
            this.docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit = transToStringUnit(ctrCollCntnReqDataMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnInitMaintMslots
     */
    public Long getCtrTotalCntnInitMaintMslots() {
        return ctrTotalCntnInitMaintMslots;
    }

    /**
     * @param ctrTotalCntnInitMaintMslots
     *            the docsIfCmtsUpChnlCtrTotalCntnInitMaintMslots to set
     */
    public void setCtrTotalCntnInitMaintMslots(Long ctrTotalCntnInitMaintMslots) {
        this.ctrTotalCntnInitMaintMslots = ctrTotalCntnInitMaintMslots;
        if (this.ctrTotalCntnInitMaintMslots != null) {
            this.docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit = transToStringUnit(ctrTotalCntnInitMaintMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnInitMaintMslots
     */
    public Long getCtrUsedCntnInitMaintMslots() {
        return ctrUsedCntnInitMaintMslots;
    }

    /**
     * @param ctrUsedCntnInitMaintMslots
     *            the docsIfCmtsUpChnlCtrUsedCntnInitMaintMslots to set
     */
    public void setCtrUsedCntnInitMaintMslots(Long ctrUsedCntnInitMaintMslots) {
        this.ctrUsedCntnInitMaintMslots = ctrUsedCntnInitMaintMslots;
        if (this.ctrUsedCntnInitMaintMslots != null) {
            this.docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit = transToStringUnit(ctrUsedCntnInitMaintMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnInitMaintMslots
     */
    public Long getCtrCollCntnInitMaintMslots() {
        return ctrCollCntnInitMaintMslots;
    }

    /**
     * @param ctrCollCntnInitMaintMslots
     *            the docsIfCmtsUpChnlCtrCollCntnInitMaintMslots to set
     */
    public void setCtrCollCntnInitMaintMslots(Long ctrCollCntnInitMaintMslots) {
        this.ctrCollCntnInitMaintMslots = ctrCollCntnInitMaintMslots;
        if (this.ctrCollCntnInitMaintMslots != null) {
            this.docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit = transToStringUnit(ctrCollCntnInitMaintMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnMslots
     */
    public Long getCtrExtCollCntnMslots() {
        return ctrExtCollCntnMslots;
    }

    /**
     * @param ctrExtCollCntnMslots
     *            the docsIfCmtsUpChnlCtrExtCollCntnMslots to set
     */
    public void setCtrExtCollCntnMslots(Long ctrExtCollCntnMslots) {
        this.ctrExtCollCntnMslots = ctrExtCollCntnMslots;
        if (this.ctrExtCollCntnMslots != null) {
            this.docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit = transToStringUnit(ctrExtCollCntnMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnReqMslots
     */
    public Long getCtrExtTotalCntnReqMslots() {
        return ctrExtTotalCntnReqMslots;
    }

    /**
     * @param ctrExtTotalCntnReqMslots
     *            the docsIfCmtsUpChnlCtrExtTotalCntnReqMslots to set
     */
    public void setCtrExtTotalCntnReqMslots(Long ctrExtTotalCntnReqMslots) {
        this.ctrExtTotalCntnReqMslots = ctrExtTotalCntnReqMslots;
        if (this.ctrExtTotalCntnReqMslots != null) {
            this.docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit = transToStringUnit(ctrExtTotalCntnReqMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnReqMslots
     */
    public Long getCtrExtUsedCntnReqMslots() {
        return ctrExtUsedCntnReqMslots;
    }

    /**
     * @param ctrExtUsedCntnReqMslots
     *            the docsIfCmtsUpChnlCtrExtUsedCntnReqMslots to set
     */
    public void setCtrExtUsedCntnReqMslots(Long ctrExtUsedCntnReqMslots) {
        this.ctrExtUsedCntnReqMslots = ctrExtUsedCntnReqMslots;
        if (this.ctrExtUsedCntnReqMslots != null) {
            this.docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit = transToStringUnit(ctrExtUsedCntnReqMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnReqMslots
     */
    public Long getCtrExtCollCntnReqMslots() {
        return ctrExtCollCntnReqMslots;
    }

    /**
     * @param ctrExtCollCntnReqMslots
     *            the docsIfCmtsUpChnlCtrExtCollCntnReqMslots to set
     */
    public void setCtrExtCollCntnReqMslots(Long ctrExtCollCntnReqMslots) {
        this.ctrExtCollCntnReqMslots = ctrExtCollCntnReqMslots;
        if (this.ctrExtCollCntnReqMslots != null) {
            this.docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit = transToStringUnit(ctrExtCollCntnReqMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslots
     */
    public Long getCtrExtTotalCntnReqDataMslots() {
        return ctrExtTotalCntnReqDataMslots;
    }

    /**
     * @param ctrExtTotalCntnReqDataMslots
     *            the docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslots to set
     */
    public void setCtrExtTotalCntnReqDataMslots(Long ctrExtTotalCntnReqDataMslots) {
        this.ctrExtTotalCntnReqDataMslots = ctrExtTotalCntnReqDataMslots;
        if (this.ctrExtTotalCntnReqDataMslots != null) {
            this.docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit = transToStringUnit(ctrExtTotalCntnReqDataMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslots
     */
    public Long getCtrExtUsedCntnReqDataMslots() {
        return ctrExtUsedCntnReqDataMslots;
    }

    /**
     * @param ctrExtUsedCntnReqDataMslots
     *            the docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslots to set
     */
    public void setCtrExtUsedCntnReqDataMslots(Long ctrExtUsedCntnReqDataMslots) {
        this.ctrExtUsedCntnReqDataMslots = ctrExtUsedCntnReqDataMslots;
        if (this.ctrExtUsedCntnReqDataMslots != null) {
            this.docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit = transToStringUnit(ctrExtUsedCntnReqDataMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnReqDataMslots
     */
    public Long getCtrExtCollCntnReqDataMslots() {
        return ctrExtCollCntnReqDataMslots;
    }

    /**
     * @param ctrExtCollCntnReqDataMslots
     *            the docsIfCmtsUpChnlCtrExtCollCntnReqDataMslots to set
     */
    public void setCtrExtCollCntnReqDataMslots(Long ctrExtCollCntnReqDataMslots) {
        this.ctrExtCollCntnReqDataMslots = ctrExtCollCntnReqDataMslots;
        if (this.ctrExtCollCntnReqDataMslots != null) {
            this.docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit = transToStringUnit(ctrExtCollCntnReqDataMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslots
     */
    public Long getCtrExtTotalCntnInitMaintMslots() {
        return ctrExtTotalCntnInitMaintMslots;
    }

    /**
     * @param ctrExtTotalCntnInitMaintMslots
     *            the docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslots to set
     */
    public void setCtrExtTotalCntnInitMaintMslots(Long ctrExtTotalCntnInitMaintMslots) {
        this.ctrExtTotalCntnInitMaintMslots = ctrExtTotalCntnInitMaintMslots;
        if (this.ctrExtTotalCntnInitMaintMslots != null) {
            this.docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit = transToStringUnit(ctrExtTotalCntnInitMaintMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslots
     */
    public Long getCtrExtUsedCntnInitMaintMslots() {
        return ctrExtUsedCntnInitMaintMslots;
    }

    /**
     * @param ctrExtUsedCntnInitMaintMslots
     *            the docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslots to set
     */
    public void setCtrExtUsedCntnInitMaintMslots(Long ctrExtUsedCntnInitMaintMslots) {
        this.ctrExtUsedCntnInitMaintMslots = ctrExtUsedCntnInitMaintMslots;
        if (this.ctrExtUsedCntnInitMaintMslots != null) {
            this.docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit = transToStringUnit(ctrExtUsedCntnInitMaintMslots);
        }
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslots
     */
    public Long getCtrExtCollCntnInitMaintMslots() {
        return ctrExtCollCntnInitMaintMslots;
    }

    /**
     * @param ctrExtCollCntnInitMaintMslots
     *            the docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslots to set
     */
    public void setCtrExtCollCntnInitMaintMslots(Long ctrExtCollCntnInitMaintMslots) {
        this.ctrExtCollCntnInitMaintMslots = ctrExtCollCntnInitMaintMslots;
        if (this.ctrExtCollCntnInitMaintMslots != null) {
            this.docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit = transToStringUnit(ctrExtCollCntnInitMaintMslots);
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
     * @return the docsIfCmtsUpChnlCtrTotalMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrTotalMslotsForunit() {
        return docsIfCmtsUpChnlCtrTotalMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrTotalMslotsForunit
     *            the docsIfCmtsUpChnlCtrTotalMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrTotalMslotsForunit(String docsIfCmtsUpChnlCtrTotalMslotsForunit) {
        this.docsIfCmtsUpChnlCtrTotalMslotsForunit = docsIfCmtsUpChnlCtrTotalMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrUcastGrantedMslotsForunit() {
        return docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit
     *            the docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrUcastGrantedMslotsForunit(String docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit) {
        this.docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit = docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrTotalCntnMslotsForunit() {
        return docsIfCmtsUpChnlCtrTotalCntnMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrTotalCntnMslotsForunit
     *            the docsIfCmtsUpChnlCtrTotalCntnMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrTotalCntnMslotsForunit(String docsIfCmtsUpChnlCtrTotalCntnMslotsForunit) {
        this.docsIfCmtsUpChnlCtrTotalCntnMslotsForunit = docsIfCmtsUpChnlCtrTotalCntnMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrUsedCntnMslotsForunit() {
        return docsIfCmtsUpChnlCtrUsedCntnMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrUsedCntnMslotsForunit
     *            the docsIfCmtsUpChnlCtrUsedCntnMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrUsedCntnMslotsForunit(String docsIfCmtsUpChnlCtrUsedCntnMslotsForunit) {
        this.docsIfCmtsUpChnlCtrUsedCntnMslotsForunit = docsIfCmtsUpChnlCtrUsedCntnMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtTotalMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtTotalMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtTotalMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtTotalMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtTotalMslotsForunit(String docsIfCmtsUpChnlCtrExtTotalMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtTotalMslotsForunit = docsIfCmtsUpChnlCtrExtTotalMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit(
            String docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit = docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit(String docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit = docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit(String docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit = docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrCollCntnMslotsForunit() {
        return docsIfCmtsUpChnlCtrCollCntnMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrCollCntnMslotsForunit
     *            the docsIfCmtsUpChnlCtrCollCntnMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrCollCntnMslotsForunit(String docsIfCmtsUpChnlCtrCollCntnMslotsForunit) {
        this.docsIfCmtsUpChnlCtrCollCntnMslotsForunit = docsIfCmtsUpChnlCtrCollCntnMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit() {
        return docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit
     *            the docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit(String docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit) {
        this.docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit = docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit() {
        return docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit
     *            the docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit(String docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit) {
        this.docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit = docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrCollCntnReqMslotsForunit() {
        return docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit
     *            the docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrCollCntnReqMslotsForunit(String docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit) {
        this.docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit = docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit() {
        return docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit
     *            the docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit(
            String docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit) {
        this.docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit = docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit() {
        return docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit
     *            the docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit(
            String docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit) {
        this.docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit = docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit() {
        return docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit
     *            the docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit(
            String docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit) {
        this.docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit = docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit() {
        return docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit
     *            the docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit(
            String docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit) {
        this.docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit = docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit() {
        return docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit
     *            the docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit(
            String docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit) {
        this.docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit = docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit() {
        return docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit
     *            the docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit(
            String docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit) {
        this.docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit = docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtCollCntnMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtCollCntnMslotsForunit(String docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit = docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit(
            String docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit = docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit(String docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit = docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit(String docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit = docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit(
            String docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit = docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit(
            String docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit = docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit(
            String docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit = docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit(
            String docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit = docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit(
            String docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit = docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit;
    }

    /**
     * @return the docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit
     */
    public String getDocsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit() {
        return docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit;
    }

    /**
     * @param docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit
     *            the docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit to set
     */
    public void setDocsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit(
            String docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit) {
        this.docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit = docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit;
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
        builder.append("CmcUpChannelCounterInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", ctrId=");
        builder.append(ctrId);
        builder.append(", ctrTotalMslots=");
        builder.append(ctrTotalMslots);
        builder.append(", ctrUcastGrantedMslots=");
        builder.append(ctrUcastGrantedMslots);
        builder.append(", ctrTotalCntnMslots=");
        builder.append(ctrTotalCntnMslots);
        builder.append(", ctrUsedCntnMslots=");
        builder.append(ctrUsedCntnMslots);
        builder.append(", ctrExtTotalMslots=");
        builder.append(ctrExtTotalMslots);
        builder.append(", ctrExtUcastGrantedMslots=");
        builder.append(ctrExtUcastGrantedMslots);
        builder.append(", ctrExtTotalCntnMslots=");
        builder.append(ctrExtTotalCntnMslots);
        builder.append(", ctrExtUsedCntnMslots=");
        builder.append(ctrExtUsedCntnMslots);
        builder.append(", ctrCollCntnMslots=");
        builder.append(ctrCollCntnMslots);
        builder.append(", ctrTotalCntnReqMslots=");
        builder.append(ctrTotalCntnReqMslots);
        builder.append(", ctrUsedCntnReqMslots=");
        builder.append(ctrUsedCntnReqMslots);
        builder.append(", ctrCollCntnReqMslots=");
        builder.append(ctrCollCntnReqMslots);
        builder.append(", ctrTotalCntnReqDataMslots=");
        builder.append(ctrTotalCntnReqDataMslots);
        builder.append(", ctrUsedCntnReqDataMslots=");
        builder.append(ctrUsedCntnReqDataMslots);
        builder.append(", ctrCollCntnReqDataMslots=");
        builder.append(ctrCollCntnReqDataMslots);
        builder.append(", ctrTotalCntnInitMaintMslots=");
        builder.append(ctrTotalCntnInitMaintMslots);
        builder.append(", ctrUsedCntnInitMaintMslots=");
        builder.append(ctrUsedCntnInitMaintMslots);
        builder.append(", ctrCollCntnInitMaintMslots=");
        builder.append(ctrCollCntnInitMaintMslots);
        builder.append(", ctrExtCollCntnMslots=");
        builder.append(ctrExtCollCntnMslots);
        builder.append(", ctrExtTotalCntnReqMslots=");
        builder.append(ctrExtTotalCntnReqMslots);
        builder.append(", ctrExtUsedCntnReqMslots=");
        builder.append(ctrExtUsedCntnReqMslots);
        builder.append(", ctrExtCollCntnReqMslots=");
        builder.append(ctrExtCollCntnReqMslots);
        builder.append(", ctrExtTotalCntnReqDataMslots=");
        builder.append(ctrExtTotalCntnReqDataMslots);
        builder.append(", ctrExtUsedCntnReqDataMslots=");
        builder.append(ctrExtUsedCntnReqDataMslots);
        builder.append(", ctrExtCollCntnReqDataMslots=");
        builder.append(ctrExtCollCntnReqDataMslots);
        builder.append(", ctrExtTotalCntnInitMaintMslots=");
        builder.append(ctrExtTotalCntnInitMaintMslots);
        builder.append(", ctrExtUsedCntnInitMaintMslots=");
        builder.append(ctrExtUsedCntnInitMaintMslots);
        builder.append(", ctrExtCollCntnInitMaintMslots=");
        builder.append(ctrExtCollCntnInitMaintMslots);
        builder.append(", docsIfCmtsUpChnlCtrTotalMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrTotalMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrUcastGrantedMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrTotalCntnMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrTotalCntnMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrUsedCntnMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrUsedCntnMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtTotalMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtTotalMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtUcastGrantedMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtTotalCntnMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtUsedCntnMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrCollCntnMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrCollCntnMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrTotalCntnReqMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrUsedCntnReqMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrCollCntnReqMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrTotalCntnReqDataMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrUsedCntnReqDataMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrCollCntnReqDataMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrTotalCntnInitMaintMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrUsedCntnInitMaintMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrCollCntnInitMaintMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtCollCntnMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtTotalCntnReqMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtUsedCntnReqMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtCollCntnReqMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtTotalCntnReqDataMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtUsedCntnReqDataMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtCollCntnReqDataMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtTotalCntnInitMaintMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtUsedCntnInitMaintMslotsForunit);
        builder.append(", docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit=");
        builder.append(docsIfCmtsUpChnlCtrExtCollCntnInitMaintMslotsForunit);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
