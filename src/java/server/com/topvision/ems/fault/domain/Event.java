package com.topvision.ems.fault.domain;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Event extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 4166351739886857513L;
    private Long alertId;
    private Long eventId;
    private Integer typeId;
    private String typeName;
    private String name;
    private Long monitorId;
    private Long entityId;
    private String host;
    private String ip;
    private String source;
    private String value;
    private String message;
    private Timestamp createTime;
    private Object userObject;
    private byte[] userObjectString;
    private Boolean clear;
    private Byte levelId;
    private Integer trapId;
    private Integer sequenceNumber;
    private String originMessage;
    private Long orginalCode;

    private Byte clearLevelId;

    private Long entityType;
    // 针对ONU和类A型设备需要使用Index的情况
    private Long deviceIndex;

    private EventSource sourceObject;
    private Long parentId;

    /**
     * 
     * @return alertId
     */
    public Long getAlertId() {
        return alertId;
    }

    /**
     * 
     * @return createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * 
     * @return entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * 
     * @return eventId
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * 
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @return monitorId
     */
    public Long getMonitorId() {
        return monitorId;
    }

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * 
     * @return typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 
     * @return typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 
     * @return userObject
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @return clear
     */
    public Boolean isClear() {
        return clear;
    }

    /**
     * 
     * @param alertId
     */
    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    /**
     * 
     * @param clear
     */
    public void setClear(Boolean clear) {
        this.clear = clear;
    }

    /**
     * @return the clearLevelId
     */
    public Byte getClearLevelId() {
        return clearLevelId;
    }

    /**
     * @param clearLevelId
     *            the clearLevelId to set
     */
    public void setClearLevelId(Byte clearLevelId) {
        this.clearLevelId = clearLevelId;
    }

    /**
     * 
     * @param createTime
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * @param entityId
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * 
     * @param eventId
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * 
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 
     * @param ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @param monitorId
     */
    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 
     * @param typeId
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 
     * @param typeName
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 
     * @param userObject
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the levelId
     */
    public Byte getLevelId() {
        return levelId;
    }

    /**
     * @param levelId
     *            the levelId to set
     */
    public void setLevelId(Byte levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the trapId
     */
    public Integer getTrapId() {
        return trapId;
    }

    /**
     * @param trapId
     *            the trapId to set
     */
    public void setTrapId(Integer trapId) {
        this.trapId = trapId;
    }

    /**
     * @return the sequenceNumber
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber
     *            the sequenceNumber to set
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the originMessage
     */
    public String getOriginMessage() {
        return originMessage;
    }

    /**
     * @param originMessage
     *            the originMessage to set
     */
    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }

    /**
     * @return the orginalCode
     */
    public Long getOrginalCode() {
        return orginalCode;
    }

    /**
     * @param orginalCode
     *            the orginalCode to set
     */
    public void setOrginalCode(Long orginalCode) {
        this.orginalCode = orginalCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Event [alertId=");
        builder.append(alertId);
        builder.append(", eventId=");
        builder.append(eventId);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", name=");
        builder.append(name);
        builder.append(", monitorId=");
        builder.append(monitorId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", host=");
        builder.append(host);
        builder.append(", source=");
        builder.append(source);
        builder.append(", value=");
        builder.append(value);
        builder.append(", message=");
        builder.append(message);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", userObject=");
        builder.append(userObject);
        builder.append(", clear=");
        builder.append(clear);
        builder.append(", levelId=");
        builder.append(levelId);
        builder.append(", trapId=");
        builder.append(trapId);
        builder.append(", sequenceNumber=");
        builder.append(sequenceNumber);
        builder.append(", originMessage=");
        builder.append(originMessage);
        builder.append(", orginalCode=");
        builder.append(orginalCode);
        builder.append("]");
        return builder.toString();
    }

    /*
     * Delete by Rod @EMS-4379
     * 
     * public byte[] serializeObject(Object object) throws IOException { ByteArrayOutputStream saos
     * = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(saos);
     * oos.writeObject(object); oos.flush(); return saos.toByteArray(); }
     * 
     * public Object deserializeObject(byte[] buf) throws IOException, ClassNotFoundException {
     * Object object = null; ByteArrayInputStream sais = new ByteArrayInputStream(buf);
     * ObjectInputStream ois = new ObjectInputStream(sais); object = ois.readObject(); return
     * object;
     * 
     * }
     */

    /**
     * @return the userObjectString
     * @throws IOException
     */
    public byte[] getUserObjectString() throws IOException {
        this.userObjectString = serializeObject(this.userObject);
        return userObjectString;
    }

    /**
     * @param userObjectString
     *            the userObjectString to set
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setUserObjectString(byte[] userObjectString) throws ClassNotFoundException, IOException {
        this.userObject = deserializeObject(userObjectString);
        this.userObjectString = userObjectString;
    }

    /**
     * Add by Rod @EMS-4379
     * 
     * @return the content of object
     */
    public Object deserializeObject(byte[] userObjectString) {
        ByteArrayInputStream bais = null;
        XMLDecoder de = null;
        Object obj = null;
        try {
            bais = new ByteArrayInputStream(userObjectString);
            de = new XMLDecoder(bais);
            obj = de.readObject();
            bais.close();
            de.close();
            return obj;
        } catch (Exception ex) {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException ex1) {
                }
            }
            if (de != null) {
                de.close();
            }
            if (userObjectString != null) {
                return new String(userObjectString);
            } else {
                return null;
            }
        }
    }

    /**
     * @param content
     *            the content to set
     */
    public byte[] serializeObject(Object obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder en = new XMLEncoder(baos);
        try {
            en.writeObject(obj);
            en.flush();
            baos.flush();
            en.close();
            baos.close();
            return baos.toByteArray();
        } catch (Exception ex) {
            if (en != null) {
                en.close();
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex1) {
                }
            }
            return obj.toString().getBytes();
        }
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 由于source是转换后的,但是有时需要用source反推index，所以提供这个方法
     * 
     * @return
     */
    public String getSourceRaw() {
        int $delimiter = source.indexOf(':');
        String rawSource = source.substring($delimiter + 1);
        return rawSource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    /**
     * 重写的equals方法,判断event对象是否相同 依据：entityId+source 只关注具体的设备告警,不关心具体的设备类型 很重要,不能删除
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (entityId == null) {
            if (other.entityId != null)
                return false;
        } else if (!entityId.equals(other.entityId))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }

    public EventSource getSourceObject() {
        return sourceObject;
    }

    public void setSourceObject(EventSource sourceObject) {
        this.sourceObject = sourceObject;
    }

}
