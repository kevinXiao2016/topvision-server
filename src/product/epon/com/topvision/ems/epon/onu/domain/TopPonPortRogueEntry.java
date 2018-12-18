/***********************************************************************
 * $Id: TopPonPortRogueEntry.java,v1.0 2017年6月17日 上午9:55:59 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:55:59
 *
 */
public class TopPonPortRogueEntry implements AliasesSuperType {

    private static final long serialVersionUID = -2668017393138490808L;

    private Long entityId;
    private Long ponId;
    private Long ponIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.2.1.1.1", index = true)
    private Integer rogueCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.2.1.1.2", index = true)
    private Integer roguePortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.2.1.1.3", writable = true, type = "Integer32")
    private Integer rogueSwitch;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.2.1.1.4", writable = true, type = "OctetString")
    private String rogueOnuList;
    private List<Integer> onuNoList;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.2.1.1.5", writable = true, type = "OctetString")
    private String rogueOnuCheck;
    private List<Integer> checkOnuNoList;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonIndex() {
        if (ponIndex == null) {
            ponIndex = EponIndex.getPonIndex(rogueCardIndex, roguePortIndex);
        }
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        rogueCardIndex = EponIndex.getSlotNo(ponIndex).intValue();
        roguePortIndex = EponIndex.getPonNo(ponIndex).intValue();
    }

    public Integer getRogueCardIndex() {
        return rogueCardIndex;
    }

    public void setRogueCardIndex(Integer rogueCardIndex) {
        this.rogueCardIndex = rogueCardIndex;
    }

    public Integer getRoguePortIndex() {
        return roguePortIndex;
    }

    public void setRoguePortIndex(Integer roguePortIndex) {
        this.roguePortIndex = roguePortIndex;
    }

    public Integer getRogueSwitch() {
        return rogueSwitch;
    }

    public void setRogueSwitch(Integer rogueSwitch) {
        this.rogueSwitch = rogueSwitch;
    }

    public String getRogueOnuList() {
        return rogueOnuList;
    }

    public void setRogueOnuList(String rogueOnuList) {
        this.rogueOnuList = rogueOnuList;
        if (rogueOnuList != null) {
            onuNoList = EponUtil.getRogueOnuFromMib(rogueOnuList);
        }
    }

    public String getRogueOnuCheck() {
        return rogueOnuCheck;
    }

    public void setRogueOnuCheck(String rogueOnuCheck) {
        this.rogueOnuCheck = rogueOnuCheck;
        if (rogueOnuCheck != null) {
            checkOnuNoList = EponUtil.getRogueOnuFromMib(rogueOnuCheck);
        }
    }

    public List<Integer> getOnuNoList() {
        return onuNoList;
    }

    public void setOnuNoList(List<Integer> onuNoList) {
        this.onuNoList = onuNoList;
        if (onuNoList != null) {
            rogueOnuList = EponUtil.getRogueBitMapFormList(onuNoList);
        }
    }

    public List<Integer> getCheckOnuNoList() {
        return checkOnuNoList;
    }

    public void setCheckOnuNoList(List<Integer> checkOnuNoList) {
        this.checkOnuNoList = checkOnuNoList;
        if (checkOnuNoList != null) {
            rogueOnuCheck = EponUtil.getRogueBitMapFormList(checkOnuNoList);
        }
    }
}
