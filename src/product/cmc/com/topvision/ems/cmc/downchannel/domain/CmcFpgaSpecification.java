/***********************************************************************
 * $Id: CC8800BHttpIpqamCheck.java,v1.0 2013-11-6 上午10:16:15 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 添加FPGA对所有功能及IPQAM功能的判断支持
 * @author bryan
 * @created @2013-11-6-上午10:16:15
 *
 */
@Alias("fpgaSpecification")
public class CmcFpgaSpecification implements Serializable, AliasesSuperType{
    /**
     * 
     */
    private static final long serialVersionUID = -2893881522946658891L;
    private Long fpgaId;// 其实一个cmcid只对应一个fpga,
    private Long cmcId;//
    private Integer ipqamChannelCount;
    private Integer subnetVlanCount;// int,
    private Integer aclUplinkEgress;// int,
    private Integer aclUplinkIngress;// int,
    private Integer aclCableEgress;// int,
    private Integer aclCableIngress;// int,
    private Integer aclUplinkEgressCopy2cpu;// int,
    private Integer aclUplinkIngressCopy2cpu;// int,
    private Integer aclCableEgressCopy2cpu;// int,
    private Integer aclCableIngressCopy2cpu;// int,
    private Integer vipSupportCount;// int,
    private Integer cosCmCount;// int,
    private Integer cosCountPerCm;// int,
    private Integer macTblHashLen;// int,
    private Integer macTblHashDeep;// int,
    private Integer srcVerifyHashLen;// int,
    private Integer srcVerifyHashDeep;// int
    
    private Integer itemNum;//是否存在fpga，如果为1，则存在
    /**
     * @return the fpgaId
     */
    public Long getFpgaId() {
        return fpgaId;
    }
    /**
     * @param fpgaId the fpgaId to set
     */
    public void setFpgaId(Long fpgaId) {
        this.fpgaId = fpgaId;
    }
    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }
    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    
    /**
     * @return the ipqamChannelCount
     */
    public Integer getIpqamChannelCount() {
        return ipqamChannelCount;
    }
    /**
     * @param ipqamChannelCount the ipqamChannelCount to set
     */
    public void setIpqamChannelCount(Integer ipqamChannelCount) {
        this.ipqamChannelCount = ipqamChannelCount;
    }
    /**
     * @return the subnetVlanCount
     */
    public Integer getSubnetVlanCount() {
        return subnetVlanCount;
    }
    /**
     * @param subnetVlanCount the subnetVlanCount to set
     */
    public void setSubnetVlanCount(Integer subnetVlanCount) {
        this.subnetVlanCount = subnetVlanCount;
    }
    /**
     * @return the aclUplinkEgress
     */
    public Integer getAclUplinkEgress() {
        return aclUplinkEgress;
    }
    /**
     * @param aclUplinkEgress the aclUplinkEgress to set
     */
    public void setAclUplinkEgress(Integer aclUplinkEgress) {
        this.aclUplinkEgress = aclUplinkEgress;
    }
    /**
     * @return the aclUplinkIngress
     */
    public Integer getAclUplinkIngress() {
        return aclUplinkIngress;
    }
    /**
     * @param aclUplinkIngress the aclUplinkIngress to set
     */
    public void setAclUplinkIngress(Integer aclUplinkIngress) {
        this.aclUplinkIngress = aclUplinkIngress;
    }
    /**
     * @return the aclCableEgress
     */
    public Integer getAclCableEgress() {
        return aclCableEgress;
    }
    /**
     * @param aclCableEgress the aclCableEgress to set
     */
    public void setAclCableEgress(Integer aclCableEgress) {
        this.aclCableEgress = aclCableEgress;
    }
    /**
     * @return the aclCableIngress
     */
    public Integer getAclCableIngress() {
        return aclCableIngress;
    }
    /**
     * @param aclCableIngress the aclCableIngress to set
     */
    public void setAclCableIngress(Integer aclCableIngress) {
        this.aclCableIngress = aclCableIngress;
    }
    /**
     * @return the aclUplinkEgressCopy2cpu
     */
    public Integer getAclUplinkEgressCopy2cpu() {
        return aclUplinkEgressCopy2cpu;
    }
    /**
     * @param aclUplinkEgressCopy2cpu the aclUplinkEgressCopy2cpu to set
     */
    public void setAclUplinkEgressCopy2cpu(Integer aclUplinkEgressCopy2cpu) {
        this.aclUplinkEgressCopy2cpu = aclUplinkEgressCopy2cpu;
    }
    /**
     * @return the aclUplinkIngressCopy2cpu
     */
    public Integer getAclUplinkIngressCopy2cpu() {
        return aclUplinkIngressCopy2cpu;
    }
    /**
     * @param aclUplinkIngressCopy2cpu the aclUplinkIngressCopy2cpu to set
     */
    public void setAclUplinkIngressCopy2cpu(Integer aclUplinkIngressCopy2cpu) {
        this.aclUplinkIngressCopy2cpu = aclUplinkIngressCopy2cpu;
    }
    /**
     * @return the aclCableEgressCopy2cpu
     */
    public Integer getAclCableEgressCopy2cpu() {
        return aclCableEgressCopy2cpu;
    }
    /**
     * @param aclCableEgressCopy2cpu the aclCableEgressCopy2cpu to set
     */
    public void setAclCableEgressCopy2cpu(Integer aclCableEgressCopy2cpu) {
        this.aclCableEgressCopy2cpu = aclCableEgressCopy2cpu;
    }
    /**
     * @return the aclCableIngressCopy2cpu
     */
    public Integer getAclCableIngressCopy2cpu() {
        return aclCableIngressCopy2cpu;
    }
    /**
     * @param aclCableIngressCopy2cpu the aclCableIngressCopy2cpu to set
     */
    public void setAclCableIngressCopy2cpu(Integer aclCableIngressCopy2cpu) {
        this.aclCableIngressCopy2cpu = aclCableIngressCopy2cpu;
    }
    /**
     * @return the vipSupportCount
     */
    public Integer getVipSupportCount() {
        return vipSupportCount;
    }
    /**
     * @param vipSupportCount the vipSupportCount to set
     */
    public void setVipSupportCount(Integer vipSupportCount) {
        this.vipSupportCount = vipSupportCount;
    }
    /**
     * @return the cosCmCount
     */
    public Integer getCosCmCount() {
        return cosCmCount;
    }
    /**
     * @param cosCmCount the cosCmCount to set
     */
    public void setCosCmCount(Integer cosCmCount) {
        this.cosCmCount = cosCmCount;
    }
    /**
     * @return the cosCountPerCm
     */
    public Integer getCosCountPerCm() {
        return cosCountPerCm;
    }
    /**
     * @param cosCountPerCm the cosCountPerCm to set
     */
    public void setCosCountPerCm(Integer cosCountPerCm) {
        this.cosCountPerCm = cosCountPerCm;
    }
    /**
     * @return the macTblHashLen
     */
    public Integer getMacTblHashLen() {
        return macTblHashLen;
    }
    /**
     * @param macTblHashLen the macTblHashLen to set
     */
    public void setMacTblHashLen(Integer macTblHashLen) {
        this.macTblHashLen = macTblHashLen;
    }
    /**
     * @return the macTblHashDeep
     */
    public Integer getMacTblHashDeep() {
        return macTblHashDeep;
    }
    /**
     * @param macTblHashDeep the macTblHashDeep to set
     */
    public void setMacTblHashDeep(Integer macTblHashDeep) {
        this.macTblHashDeep = macTblHashDeep;
    }
    /**
     * @return the srcVerifyHashLen
     */
    public Integer getSrcVerifyHashLen() {
        return srcVerifyHashLen;
    }
    /**
     * @param srcVerifyHashLen the srcVerifyHashLen to set
     */
    public void setSrcVerifyHashLen(Integer srcVerifyHashLen) {
        this.srcVerifyHashLen = srcVerifyHashLen;
    }
    /**
     * @return the srcVerifyHashDeep
     */
    public Integer getSrcVerifyHashDeep() {
        return srcVerifyHashDeep;
    }
    /**
     * @param srcVerifyHashDeep the srcVerifyHashDeep to set
     */
    public void setSrcVerifyHashDeep(Integer srcVerifyHashDeep) {
        this.srcVerifyHashDeep = srcVerifyHashDeep;
    }
    /**
     * @return the itemNum
     */
    public Integer getItemNum() {
        return itemNum;
    }
    /**
     * @param itemNum the itemNum to set
     */
    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }
    
    
}
