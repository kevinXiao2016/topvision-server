package com.topvision.ems.network.domain;

import com.topvision.ems.facade.domain.PortEntity;
import com.topvision.ems.network.util.PortUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Port extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 297201649144065938L;
    private long portId;
    private String name;
    private String note;
    private long entityId;
    private long card;
    private Long ifIndex;
    private String ifDescr;
    private int ifType;
    private int ifMtu;
    private double ifSpeed;
    private String ifPhysAddress;
    private byte ifAdminStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 )
    private byte ifOperStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 ) ,
    // unknown ( 4 ) , dormant ( 5 ) , notPresent ( 6 )
    private String ifLastChange;
    private String ifName;
    private String ifAlias;
    private String x;
    private String y;
    private long lastChangeTime;
    private String lastChangeTimeStr;

    public Port() {
    }

    public Port(long entityId) {
        this.entityId = entityId;
    }

    public Port(long entityId, PortEntity portEntity) {
        this(entityId);
        this.ifIndex = portEntity.getIfIndex().longValue();
        this.ifDescr = portEntity.getIfDescr();
        this.ifType = portEntity.getIfType();
        this.ifMtu = portEntity.getIfMtu();
        this.ifSpeed = portEntity.getIfSpeed();
        if (portEntity.getIfAdminStatus() != null) {
            this.ifAdminStatus = Byte.parseByte(portEntity.getIfAdminStatus().toString());
        }
        this.ifPhysAddress = portEntity.getIfPhysAddress();
        if (portEntity.getIfOperStatus() != null) {
            this.ifOperStatus = Byte.parseByte(portEntity.getIfOperStatus().toString());
        }
        this.ifLastChange = portEntity.getIfLastChange();
        this.ifName = portEntity.getIfName();
        this.ifAlias = portEntity.getIfAlias();
    }

    public Port(long entityId, String[] ifInfo) {
        this(entityId);
        if (ifInfo.length == 9) {
            if (ifInfo[0] != null) {
                ifIndex = Long.parseLong(ifInfo[0]);
            }
            ifDescr = ifInfo[1];
            if (ifInfo[2] != null) {
                ifType = Integer.parseInt(ifInfo[2]);
            }
            if (ifInfo[3] != null) {
                ifMtu = Integer.parseInt(ifInfo[3]);
            }
            if (ifInfo[4] != null) {
                ifSpeed = Double.parseDouble(ifInfo[4]);
            }
            ifPhysAddress = ifInfo[5];
            if (ifInfo[6] != null) {
                ifAdminStatus = Byte.parseByte(ifInfo[6]);
            }
            if (ifInfo[7] != null) {
                ifOperStatus = Byte.parseByte(ifInfo[7]);
            }
            ifLastChange = ifInfo[8];
        }
    }

    public Port(long entityId, String[] ifInfo, String[] ifXInfo) {
        this(entityId, ifInfo);
        if (ifXInfo.length == 2) {
            ifName = ifXInfo[0];
            ifAlias = ifXInfo[1];
        }
    }

    /**
     * @return the portId
     */
    public long getPortId() {
        return portId;
    }

    /**
     * @param portId
     *            the portId to set
     */
    public void setPortId(long portId) {
        this.portId = portId;
    }

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the card
     */
    public long getCard() {
        return card;
    }

    /**
     * @param card
     *            the card to set
     */
    public void setCard(long card) {
        this.card = card;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the ifDescr
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * @param ifDescr
     *            the ifDescr to set
     */
    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    /**
     * @return the ifType
     */
    public int getIfType() {
        return ifType;
    }

    /**
     * @return the ifType name
     */
    public String getIfTypeString() {
        return PortUtil.getIfTypeString(ifType);
    }

    /**
     * @param ifType
     *            the ifType to set
     */
    public void setIfType(int ifType) {
        this.ifType = ifType;
    }

    /**
     * @return the ifMtu
     */
    public int getIfMtu() {
        return ifMtu;
    }

    /**
     * @return the ifMtu String
     */
    public String getIfMtuString() {
        return String.valueOf(ifMtu);
    }

    /**
     * @param ifMtu
     *            the ifMtu to set
     */
    public void setIfMtu(int ifMtu) {
        this.ifMtu = ifMtu;
    }

    /**
     * @return the ifSpeed
     */
    public double getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @return the ifSpeed
     */
    public String getIfSpeedString() {
        return PortUtil.getIfSpeedString(ifSpeed);
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(double ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifPhysAddress
     */
    public String getIfPhysAddress() {
        return ifPhysAddress;
    }

    /**
     * @param ifPhysAddress
     *            the ifPhysAddress to set
     */
    public void setIfPhysAddress(String ifPhysAddress) {
        this.ifPhysAddress = ifPhysAddress;
    }

    /**
     * @return the ifAdminStatus
     */
    public byte getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @return the ifAdminStatus
     */
    public String getIfAdminStatusString() {
        return PortUtil.getIfAdminStatusString(ifAdminStatus);
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(byte ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @return the ifOperStatus
     */
    public byte getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @return the ifOperStatus
     */
    public String getIfOperStatusString() {
        return PortUtil.getIfOperStatusString(ifOperStatus);
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(byte ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    /**
     * @return the ifLastChange
     */
    public String getIfLastChange() {
        return ifLastChange;
    }

    /**
     * @param ifLastChange
     *            the ifLastChange to set
     */
    public void setIfLastChange(String ifLastChange) {
        this.ifLastChange = ifLastChange;
    }

    /**
     * @return the ifName
     */
    public String getIfName() {
        return ifName;
    }

    /**
     * @param ifName
     *            the ifName to set
     */
    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    /**
     * @return the ifAlias
     */
    public String getIfAlias() {
        return ifAlias;
    }

    /**
     * @param ifAlias
     *            the ifAlias to set
     */
    public void setIfAlias(String ifAlias) {
        this.ifAlias = ifAlias;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getIfName());
        sb.append("[");
        sb.append(this.getIfIndex());
        sb.append("]");
        return sb.toString();
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getName() {
        return (name == null || "".equals(name)) ? ifName : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(long lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    public String getLastChangeTimeStr() {
        return lastChangeTimeStr;
    }

    public void setLastChangeTimeStr(String lastChangeTimeStr) {
        this.lastChangeTimeStr = lastChangeTimeStr;
    }

    public String getNote() {
        return note == null ? ifDescr : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
