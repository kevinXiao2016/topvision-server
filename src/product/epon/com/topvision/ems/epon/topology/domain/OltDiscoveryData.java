/***********************************************************************
 * $Id: OltDiscoveryData.java,v1.0 2011-9-16 下午03:59:37 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onuauth.domain.OltAuthenMacInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthenSnInfo;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.facade.domain.DiscoveryData;

/**
 * @author Victor
 * @created @2011-9-16-下午03:59:37
 * 
 */
public class OltDiscoveryData extends DiscoveryData {
    private static final long serialVersionUID = -4388786173965905660L;

    private OltAttribute attribute;
    private List<OltSlotAttribute> slots;
    private List<OltPowerAttribute> powers;
    private List<OltFanAttribute> fans;
    private List<OltSniAttribute> snis;
    private List<OltPonAttribute> pons;
    private List<OltOnuAttribute> onus;
    private List<OltOnuPonAttribute> onuPons;
    private List<OltUniAttribute> unis;
    private List<OltSlotStatus> slotStatus;
    private List<OltPowerStatus> powerStatus;
    private List<OltFanStatus> fanStatus;
    private List<OltAuthenMacInfo> authenMacInfos;
    private List<OltAuthenSnInfo> authenSnInfos;
    private List<OltUniStormSuppressionEntry> uniStormSuppressionEntries;
    private List<OltOnuBlockAuthen> onuBlockAuthens;
    private List<OltUniPortRateLimit> uniPortRateLimits;
    private List<OltOnuCapability> onuCapabilities;
    private List<OltTopOnuCapability> topOnuCapabilities;
    private List<OltOnuRstp> onuRstps;
    private List<OltUniExtAttribute> uniExtAttributes;
    private List<OltTopOnuProductTable> topOnuProductTables;

    /**
     * Default TopoType = ALL TOPO
     * 
     */
    public OltDiscoveryData() {
        super();
    }

    public OltDiscoveryData(Integer topoType) {
        super(topoType);
    }

    public OltDiscoveryData(List<String> excludeOids) {
        super(excludeOids);
    }

    /**
     * @return the attribute
     */
    public OltAttribute getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(OltAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the slots
     */
    public List<OltSlotAttribute> getSlots() {
        return slots;
    }

    /**
     * @param slots
     *            the slots to set
     */
    public void setSlots(List<OltSlotAttribute> slots) {
        this.slots = slots;
    }

    /**
     * 添加slot
     * 
     * @param slot
     *            slot实例
     */
    public void addSlot(OltSlotAttribute slot) {
        if (slots == null) {
            slots = new ArrayList<OltSlotAttribute>();
        }
        slots.add(slot);
    }

    /**
     * @return the powers
     */
    public List<OltPowerAttribute> getPowers() {
        return powers;
    }

    /**
     * @param powers
     *            the powers to set
     */
    public void setPowers(List<OltPowerAttribute> powers) {
        this.powers = powers;
    }

    /**
     * 添加power
     * 
     * @param power
     *            power实例
     */
    public void addPower(OltPowerAttribute power) {
        if (powers == null) {
            powers = new ArrayList<OltPowerAttribute>();
        }
        powers.add(power);
    }

    /**
     * @return the fans
     */
    public List<OltFanAttribute> getFans() {
        return fans;
    }

    /**
     * @param fans
     *            the fans to set
     */
    public void setFans(List<OltFanAttribute> fans) {
        this.fans = fans;
    }

    /**
     * 添加fan
     * 
     * @param fan
     *            fan实例
     */
    public void addFan(OltFanAttribute fan) {
        if (fans == null) {
            fans = new ArrayList<OltFanAttribute>();
        }
        fans.add(fan);
    }

    /**
     * @return the snis
     */
    public List<OltSniAttribute> getSnis() {
        return snis;
    }

    /**
     * @param snis
     *            the snis to set
     */
    public void setSnis(List<OltSniAttribute> snis) {
        this.snis = snis;
    }

    /**
     * 添加sni
     * 
     * @param sni
     *            sni实例
     */
    public void addSni(OltSniAttribute sni) {
        if (snis == null) {
            snis = new ArrayList<OltSniAttribute>();
        }
        snis.add(sni);
    }

    /**
     * @return the pons
     */
    public List<OltPonAttribute> getPons() {
        return pons;
    }

    /**
     * @param pons
     *            the pons to set
     */
    public void setPons(List<OltPonAttribute> pons) {
        this.pons = pons;
    }

    /**
     * 添加pon
     * 
     * @param pon
     *            pon实例
     */
    public void addPon(OltPonAttribute pon) {
        if (pons == null) {
            pons = new ArrayList<OltPonAttribute>();
        }
        pons.add(pon);
    }

    /**
     * @return the onus
     */
    public List<OltOnuAttribute> getOnus() {
        return onus;
    }

    /**
     * @param onus
     *            the onus to set
     */
    public void setOnus(List<OltOnuAttribute> onus) {
        this.onus = onus;
    }

    /**
     * 添加onu
     * 
     * @param onu
     *            onu实例
     */
    public void addOnu(OltOnuAttribute onu) {
        if (onus == null) {
            onus = new ArrayList<OltOnuAttribute>();
        }
        onus.add(onu);
    }

    /**
     * @return the onuPons
     */
    public List<OltOnuPonAttribute> getOnuPons() {
        return onuPons;
    }

    /**
     * @param onuPons
     *            the onuPons to set
     */
    public void setOnuPons(List<OltOnuPonAttribute> onuPons) {
        this.onuPons = onuPons;
    }

    /**
     * 添加onuPon
     * 
     * @param onuPon
     *            onuPon实例
     */
    public void addOnuPon(OltOnuPonAttribute onuPon) {
        if (onuPons == null) {
            onuPons = new ArrayList<OltOnuPonAttribute>();
        }
        onuPons.add(onuPon);
    }

    /**
     * @return the unis
     */
    public List<OltUniAttribute> getUnis() {
        return unis;
    }

    /**
     * @param unis
     *            the unis to set
     */
    public void setUnis(List<OltUniAttribute> unis) {
        this.unis = unis;
    }

    /**
     * 添加uni
     * 
     * @param uni
     *            uni实例
     */
    public void addUni(OltUniAttribute uni) {
        if (unis == null) {
            unis = new ArrayList<OltUniAttribute>();
        }
        unis.add(uni);
    }

    /**
     * @return the slotStatus
     */
    public List<OltSlotStatus> getSlotStatus() {
        return slotStatus;
    }

    /**
     * @param slotStatus
     *            the slotStatus to set
     */
    public void setSlotStatus(List<OltSlotStatus> slotStatus) {
        this.slotStatus = slotStatus;
    }

    /**
     * 
     * 
     * @param slot
     */
    public void addSlotStatus(OltSlotStatus slot) {
        if (slotStatus == null) {
            slotStatus = new ArrayList<OltSlotStatus>();
        }
        slotStatus.add(slot);
    }

    /**
     * @return the powerStatus
     */
    public List<OltPowerStatus> getPowerStatus() {
        return powerStatus;
    }

    /**
     * @param powerStatus
     *            the powerStatus to set
     */
    public void setPowerStatus(List<OltPowerStatus> powerStatus) {
        this.powerStatus = powerStatus;
    }

    /**
     * 
     * 
     * @param power
     */
    public void addPowerStatus(OltPowerStatus power) {
        if (powerStatus == null) {
            powerStatus = new ArrayList<OltPowerStatus>();
        }
        powerStatus.add(power);
    }

    /**
     * @return the fanStatus
     */
    public List<OltFanStatus> getFanStatus() {
        return fanStatus;
    }

    /**
     * @param fanStatus
     *            the fanStatus to set
     */
    public void setFanStatus(List<OltFanStatus> fanStatus) {
        this.fanStatus = fanStatus;
    }

    /**
     * 
     * 
     * @param fan
     */
    public void addFanStatus(OltFanStatus fan) {
        if (fanStatus == null) {
            fanStatus = new ArrayList<OltFanStatus>();
        }
        fanStatus.add(fan);
    }

    /**
     * @return the authenMacInfos
     */
    public List<OltAuthenMacInfo> getAuthenMacInfos() {
        return authenMacInfos;
    }

    /**
     * @param authenMacInfos
     *            the authenMacInfos to set
     */
    public void setAuthenMacInfos(List<OltAuthenMacInfo> authenMacInfos) {
        this.authenMacInfos = authenMacInfos;
    }

    /**
     * 
     * 
     * @param mac
     */
    public void addMacInfo(OltAuthenMacInfo macInfo) {
        if (authenMacInfos == null) {
            authenMacInfos = new ArrayList<OltAuthenMacInfo>();
        }
        authenMacInfos.add(macInfo);
    }

    /**
     * @return the authenSnInfos
     */
    public List<OltAuthenSnInfo> getAuthenSnInfos() {
        return authenSnInfos;
    }

    /**
     * @param authenSnInfos
     *            the authenSnInfos to set
     */
    public void setAuthenSnInfos(List<OltAuthenSnInfo> authenSnInfos) {
        this.authenSnInfos = authenSnInfos;
    }

    /**
     * 
     * 
     * @param mac
     */
    public void addSnInfo(OltAuthenSnInfo snInfo) {
        if (authenSnInfos == null) {
            authenSnInfos = new ArrayList<OltAuthenSnInfo>();
        }
        authenSnInfos.add(snInfo);
    }

    /**
     * @return the onuBlockAuthens
     */
    public List<OltOnuBlockAuthen> getOnuBlockAuthens() {
        return onuBlockAuthens;
    }

    /**
     * @param onuBlockAuthens
     *            the onuBlockAuthens to set
     */
    public void setOnuBlockAuthens(List<OltOnuBlockAuthen> onuBlockAuthens) {
        this.onuBlockAuthens = onuBlockAuthens;
    }

    public void addOnuBlockAuthens(OltOnuBlockAuthen onuBlockAuthen) {
        if (onuBlockAuthens == null) {
            onuBlockAuthens = new ArrayList<OltOnuBlockAuthen>();
        }
        onuBlockAuthens.add(onuBlockAuthen);
    }

    /**
     * @return the uniPortRateLimits
     */
    public List<OltUniPortRateLimit> getUniPortRateLimits() {
        return uniPortRateLimits;
    }

    /**
     * @param uniPortRateLimits
     *            the uniPortRateLimits to set
     */
    public void setUniPortRateLimits(List<OltUniPortRateLimit> uniPortRateLimits) {
        this.uniPortRateLimits = uniPortRateLimits;
    }

    /**
     * 
     * 
     * @param uniPortRateLimit
     */
    public void addOltUniPortRateLimits(OltUniPortRateLimit oltUniPortRateLimit) {
        if (uniPortRateLimits == null) {
            uniPortRateLimits = new ArrayList<OltUniPortRateLimit>();
        }
        uniPortRateLimits.add(oltUniPortRateLimit);
    }

    /**
     * @return the uniStormSuppressionEntries
     */
    public List<OltUniStormSuppressionEntry> getUniStormSuppressionEntries() {
        return uniStormSuppressionEntries;
    }

    /**
     * @param uniStormSuppressionEntries
     *            the uniStormSuppressionEntries to set
     */
    public void setUniStormSuppressionEntries(List<OltUniStormSuppressionEntry> uniStormSuppressionEntries) {
        this.uniStormSuppressionEntries = uniStormSuppressionEntries;
    }

    /**
     * 
     * 
     * @param uniStormSuppressionEntry
     */
    public void addOltUniStormInfo(OltUniStormSuppressionEntry uniStormSuppressionEntry) {
        if (uniStormSuppressionEntries == null) {
            uniStormSuppressionEntries = new ArrayList<OltUniStormSuppressionEntry>();
        }
        uniStormSuppressionEntries.add(uniStormSuppressionEntry);
    }

    /**
     * @return the onuCapabilities
     */
    public List<OltOnuCapability> getOnuCapabilities() {
        return onuCapabilities;
    }

    /**
     * @param onuCapabilities
     *            the onuCapabilities to set
     */
    public void setOnuCapabilities(List<OltOnuCapability> onuCapabilities) {
        this.onuCapabilities = onuCapabilities;
    }

    /**
     * 
     * 
     * @param capability
     */
    public void addOnuCapability(OltOnuCapability capability) {
        if (onuCapabilities == null) {
            onuCapabilities = new ArrayList<OltOnuCapability>();
        }
        onuCapabilities.add(capability);
    }

    /**
     * @return the topOnuCapabilities
     */
    public List<OltTopOnuCapability> getTopOnuCapabilities() {
        return topOnuCapabilities;
    }

    /**
     * @param topOnuCapabilities
     *            the topOnuCapabilities to set
     */
    public void setTopOnuCapabilities(List<OltTopOnuCapability> topOnuCapabilities) {
        this.topOnuCapabilities = topOnuCapabilities;
    }

    /**
     * 
     * 
     * @param capability
     */
    public void addTopOnuCapability(OltTopOnuCapability capability) {
        if (topOnuCapabilities == null) {
            topOnuCapabilities = new ArrayList<OltTopOnuCapability>();
        }
        topOnuCapabilities.add(capability);
    }

    /**
     * @return the onuRstps
     */
    public List<OltOnuRstp> getOnuRstps() {
        return onuRstps;
    }

    /**
     * @param onuRstps
     *            the onuRstps to set
     */
    public void setOnuRstps(List<OltOnuRstp> onuRstps) {
        this.onuRstps = onuRstps;
    }

    /**
     * 
     * 
     * @param capability
     */
    public void addOnuRstps(OltOnuRstp capability) {
        if (onuRstps == null) {
            onuRstps = new ArrayList<OltOnuRstp>();
        }
        onuRstps.add(capability);
    }

    /**
     * @return the uniExtAttributes
     */
    public List<OltUniExtAttribute> getUniExtAttributes() {
        return uniExtAttributes;
    }

    /**
     * @param uniExtAttributes
     *            the uniExtAttributes to set
     */
    public void setUniExtAttributes(List<OltUniExtAttribute> uniExtAttributes) {
        this.uniExtAttributes = uniExtAttributes;
    }

    /**
     * 
     * 
     * @param uniExtAttribute
     */
    public void addOltUniExtAttribute(OltUniExtAttribute uniExtAttribute) {
        if (uniExtAttributes == null) {
            uniExtAttributes = new ArrayList<OltUniExtAttribute>();
        }
        uniExtAttributes.add(uniExtAttribute);
    }

    /**
     * @return the topOnuProductTables
     */
    public List<OltTopOnuProductTable> getTopOnuProductTables() {
        return topOnuProductTables;
    }

    /**
     * @param topOnuProductTables
     *            the topOnuProductTables to set
     */
    public void setTopOnuProductTables(List<OltTopOnuProductTable> topOnuProductTables) {
        this.topOnuProductTables = topOnuProductTables;
    }

    public void addTopOnuProductTables(OltTopOnuProductTable table) {
        if (topOnuProductTables == null) {
            topOnuProductTables = new ArrayList<OltTopOnuProductTable>();
        }
        topOnuProductTables.add(table);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltDiscoveryData [attribute=");
        builder.append(attribute);
        builder.append(", slots=");
        builder.append(slots);
        builder.append(", powers=");
        builder.append(powers);
        builder.append(", fans=");
        builder.append(fans);
        builder.append(", snis=");
        builder.append(snis);
        builder.append(", pons=");
        builder.append(pons);
        builder.append(", onus=");
        builder.append(onus);
        builder.append(", onuPons=");
        builder.append(onuPons);
        builder.append(", unis=");
        builder.append(unis);
        builder.append(", slotStatus=");
        builder.append(slotStatus);
        builder.append(", powerStatus=");
        builder.append(powerStatus);
        builder.append(", fanStatus=");
        builder.append(fanStatus);
        builder.append(", authenMacInfos=");
        builder.append(authenMacInfos);
        builder.append(", authenSnInfos=");
        builder.append(authenSnInfos);
        builder.append(", uniStormSuppressionEntries=");
        builder.append(uniStormSuppressionEntries);
        builder.append(", onuBlockAuthens=");
        builder.append(onuBlockAuthens);
        builder.append(", uniPortRateLimits=");
        builder.append(uniPortRateLimits);
        builder.append(", onuCapabilities=");
        builder.append(onuCapabilities);
        builder.append(", topOnuCapabilities=");
        builder.append(topOnuCapabilities);
        builder.append(", onuRstps=");
        builder.append(onuRstps);
        builder.append(", uniExtAttributes=");
        builder.append(uniExtAttributes);
        builder.append(", topOnuProductTables=");
        builder.append(topOnuProductTables);
        builder.append("]");
        return builder.toString();
    }

}
