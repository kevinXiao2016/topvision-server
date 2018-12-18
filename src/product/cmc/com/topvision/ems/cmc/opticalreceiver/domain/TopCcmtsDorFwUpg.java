/***********************************************************************
 * $Id: TopCcmtsDorFirmWare.java,v1.0 2016年9月13日 下午2:25:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月13日-下午2:25:41
 *
 */
public class TopCcmtsDorFwUpg implements AliasesSuperType {
    private static final long serialVersionUID = 3213248892009353375L;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.1", index = true)
    private Long fwUpgDevIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.2", writable = true, type = "Integer32")
    /**
     * 如果写入值为1，如果serveraddr和filename都指定合法，开始通过tftp下载文件，进行升级
                     其它值不进行处理
       1 - upgrade
       0 - noOperation
                     升级过程中返回 1表示正在升级，升级完成后置0恢复
     */
    private Integer fwUpgAction;// 恢复出厂设置 （设置1为恢复出厂设置）
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.3", writable = true, type = "ByteIpAddress")
    private String fwUpgServerAddr;// 设定光机升级tftp server地址，先支持ipv4
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.4", writable = true)
    private String fwUpgFileName;// 升级文件名
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.5", writable = true, type = "Integer32")
    private Integer fwUpgProto;// 1 - ftp, 2 - tftp, other value is invalid
    /**
     * 返回光机升级状态 未有升级操作时返回0 > 0 表示正常，对应当前升级阶段 < 0 表示失败，对应err code
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.8", type = "Integer32")
    private Integer fwUpgStatus;

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
     * @return the fwUpgDevIndex
     */
    public Long getFwUpgDevIndex() {
        return fwUpgDevIndex;
    }

    /**
     * @param fwUpgDevIndex
     *            the fwUpgDevIndex to set
     */
    public void setFwUpgDevIndex(Long fwUpgDevIndex) {
        this.fwUpgDevIndex = fwUpgDevIndex;
    }

    /**
     * @return the fwUpgAction
     */
    public Integer getFwUpgAction() {
        return fwUpgAction;
    }

    /**
     * @param fwUpgAction
     *            the fwUpgAction to set
     */
    public void setFwUpgAction(Integer fwUpgAction) {
        this.fwUpgAction = fwUpgAction;
    }

    /**
     * @return the fwUpgServerAddr
     */
    public String getFwUpgServerAddr() {
        return fwUpgServerAddr;
    }

    /**
     * @param fwUpgServerAddr
     *            the fwUpgServerAddr to set
     */
    public void setFwUpgServerAddr(String fwUpgServerAddr) {
        this.fwUpgServerAddr = fwUpgServerAddr;
    }

    /**
     * @return the fwUpgFileName
     */
    public String getFwUpgFileName() {
        return fwUpgFileName;
    }

    /**
     * @param fwUpgFileName
     *            the fwUpgFileName to set
     */
    public void setFwUpgFileName(String fwUpgFileName) {
        this.fwUpgFileName = fwUpgFileName;
    }

    /**
     * @return the fwUpgProto
     */
    public Integer getFwUpgProto() {
        return fwUpgProto;
    }

    /**
     * @param fwUpgProto
     *            the fwUpgProto to set
     */
    public void setFwUpgProto(Integer fwUpgProto) {
        this.fwUpgProto = fwUpgProto;
    }

    /**
     * @return the fwUpgStatus
     */
    public Integer getFwUpgStatus() {
        return fwUpgStatus;
    }

    /**
     * @param fwUpgStatus
     *            the fwUpgStatus to set
     */
    public void setFwUpgStatus(Integer fwUpgStatus) {
        this.fwUpgStatus = fwUpgStatus;
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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

}
