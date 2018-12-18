package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-3-27-15:30:39
 * @modifyTime 2012-9-4 11:30:26
 * @reson 此对象与OltTopOnuProductTable对象重复，按照对象与mib表的对应规则，此对象命名不统一
 * @author lizongtian
 */
@Deprecated
public class OltOnuPreType implements Serializable {
    private static final long serialVersionUID = -5771103977882468851L;
    // private Long entityId;
    // private Long onuIndex;
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.1", index = true)
    // private Integer topOnuProductCardIndex;
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.2", index = true)
    // private Integer topOnuProductPonIndex;
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.3", index = true)
    // private Integer topOnuProductOnuIndex;
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.4", writable = true, type =
    // "OctetString")
    // private Integer topOnuProductTypeNum;
    // private String topOnuProductType;
    // private Integer onuType ;
    // public Long getEntityId() {
    // return entityId;
    // }
    //
    // public void setEntityId(Long entityId) {
    // this.entityId = entityId;
    // }
    //
    // public Long getOnuIndex() {
    // if(onuIndex == null){
    // return EponIndex.getOnuIndex(topOnuProductCardIndex, topOnuProductPonIndex,
    // topOnuProductOnuIndex);
    // }else{
    // return onuIndex;
    // }
    // }
    //
    // public void setOnuIndex(Long onuIndex) {
    // this.onuIndex = onuIndex;
    // this.topOnuProductCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
    // this.topOnuProductPonIndex = EponIndex.getPonNo(onuIndex).intValue();
    // this.topOnuProductOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    // }
    //
    // public Integer getTopOnuProductCardIndex() {
    // return topOnuProductCardIndex;
    // }
    //
    // public void setTopOnuProductCardIndex(Integer topOnuProductCardIndex) {
    // this.topOnuProductCardIndex = topOnuProductCardIndex;
    // }
    //
    // public Integer getTopOnuProductPonIndex() {
    // return topOnuProductPonIndex;
    // }
    //
    // public void setTopOnuProductPonIndex(Integer topOnuProductPonIndex) {
    // this.topOnuProductPonIndex = topOnuProductPonIndex;
    // }
    //
    // public Integer getTopOnuProductOnuIndex() {
    // return topOnuProductOnuIndex;
    // }
    //
    // public void setTopOnuProductOnuIndex(Integer topOnuProductOnuIndex) {
    // this.topOnuProductOnuIndex = topOnuProductOnuIndex;
    // }
    //
    // public Integer getTopOnuProductTypeNum() {
    // return topOnuProductTypeNum;
    // }
    //
    // public void setTopOnuProductTypeNum(Integer topOnuProductTypeNum) {
    // this.topOnuProductTypeNum = topOnuProductTypeNum;
    // this.onuType = topOnuProductTypeNum;
    // }
    //
    // public Integer getOnuType() {
    // return onuType;
    // }
    //
    // public void setOnuType(Integer onuType) {
    // this.onuType = onuType;
    // }
    //
    // public String getTopOnuProductType() {
    // return topOnuProductType;
    // }
    //
    // public void setTopOnuProductType(String topOnuProductType) {
    // this.topOnuProductType = topOnuProductType;
    // }
    //
    // @Override
    // public String toString() {
    // final StringBuilder sb = new StringBuilder();
    // sb.append("OltOnuPreType");
    // sb.append("{entityId=").append(entityId);
    // sb.append("{onuIndex=").append(onuIndex);
    // sb.append("{topOnuProductCardIndex=").append(topOnuProductCardIndex);
    // sb.append("{topOnuProductPonIndex=").append(topOnuProductPonIndex);
    // sb.append("{topOnuProductOnuIndex=").append(topOnuProductOnuIndex);
    // sb.append("{topOnuProductType=").append(topOnuProductType);
    // sb.append('}');
    // return sb.toString();
    // }
}
