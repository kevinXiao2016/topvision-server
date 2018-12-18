package com.topvision.ems.cmc.optical.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2014-08-09-上午11:43:29
 * 
 */
@Alias("cmcOpReceiverType")
public class CmcOpReceiverType implements AliasesSuperType {

    private static final long serialVersionUID = -6985689487669320418L;

    /**
     * 数据库主键ID
     */
    private Long id;
    private Long cmcId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.1.1.1", index = true)
    private Integer dorDevTypeIndex;

    /**
     * NoDorDevice(0), TYPE-A(1), TYPE-B(2)
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.1.1.2")
    private Integer dorDevType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getDorDevTypeIndex() {
        return dorDevTypeIndex;
    }

    public void setDorDevTypeIndex(Integer dorDevTypeIndex) {
        this.dorDevTypeIndex = dorDevTypeIndex;
    }

    public Integer getDorDevType() {
        return dorDevType;
    }

    public void setDorDevType(Integer dorDevType) {
        this.dorDevType = dorDevType;
    }

}
