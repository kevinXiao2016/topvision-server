package com.topvision.ems.fault.domain;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Alert extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -5952560186398148322L;
    public static final byte ALERT = 0;
    public static final byte CONFIRM = 1;
    public static final byte AUTO_CLEAR = 2;
    public static final byte MANUAL_CLEAR = 3;
    private Long alertId;
    private Integer typeId;
    private String name;
    private Byte levelId;
    private Long monitorId;
    private Long entityId;
    private Long parentId;
    private String host;
    private String ip;
    private EventSource sourceObject;
    private String source;
    private String message;
    private Timestamp firstTime = new Timestamp(System.currentTimeMillis());
    private Timestamp lastTime = new Timestamp(System.currentTimeMillis());
    private Integer happenTimes = 1;
    private String confirmUser;
    private Timestamp confirmTime;
    private String confirmMessage;
    private Byte status = ALERT;
    private String extend1;
    private String firstTimeStr;
    private String lastTimeStr;
    private String confirmTimeStr;
    private String typeName;
    private String levelName;
    private String entityName;
    private String sound = null;
    private Long orginalCode;
    private Object userObject;
    private byte[] userObjectString;
    private Long entityType;

    private String clearUser;
    private Timestamp clearTime;
    private String clearMessage;
    private String clearTimeStr;

    /**
     * 
     * @return alertId
     */
    public Long getAlertId() {
        return alertId;
    }

    /**
     * 
     * @return confirmMessage
     */
    public String getConfirmMessage() {
        return confirmMessage;
    }

    /**
     * 
     * @return confirmTime
     */
    public Timestamp getConfirmTime() {
        return confirmTime;
    }

    /**
     * 
     * @return confirmUser
     */
    public String getConfirmUser() {
        return confirmUser;
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
     * @return entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 
     * @return extend1
     */
    public String getExtend1() {
        return extend1;
    }

    /**
     * 
     * @return firstTime
     */
    public Timestamp getFirstTime() {
        return firstTime;
    }

    /**
     * 
     * @return firstTimeStr
     */
    public String getFirstTimeStr() {
        if (firstTimeStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            firstTimeStr = sdf.format(firstTime);
        }
        return firstTimeStr;
    }

    public String getLastTimeStr() {
        if (lastTimeStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            lastTimeStr = sdf.format(lastTime);
        }
        return lastTimeStr;
    }

    public void setLastTimeStr(String lastTimeStr) {
        this.lastTimeStr = lastTimeStr;
    }

    public String getConfirmTimeStr() {
        if (confirmTimeStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (confirmTime != null) {
                confirmTimeStr = sdf.format(confirmTime);
            }
        }
        return confirmTimeStr;
    }

    public void setConfirmTimeStr(String confirmTimeStr) {
        this.confirmTimeStr = confirmTimeStr;
    }

    /**
     * 
     * @return happenTimes
     */
    public Integer getHappenTimes() {
        return happenTimes;
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
     * @return lastTime
     */
    public Timestamp getLastTime() {
        return lastTime;
    }

    /**
     * 
     * @return levelId
     */
    public Byte getLevelId() {
        return levelId;
    }

    /**
     * 
     * @return levelName
     */
    public String getLevelName() {
        return levelName;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 
     * @return sound
     */
    public String getSound() {
        return sound;
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
     * @return status
     */
    public Byte getStatus() {
        return status;
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

    public void happenedAgain() {
        happenTimes++;
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
     * @param confirmMessage
     */
    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    /**
     * 
     * @param confirmTime
     */
    public void setConfirmTime(Timestamp confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 
     * @param confirmUser
     */
    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
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
     * @param entityName
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * 
     * @param extend1
     */
    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    /**
     * 
     * @param firstTime
     */
    public void setFirstTime(Timestamp firstTime) {
        this.firstTime = firstTime;
    }

    /**
     * 
     * @param firstTimeStr
     */
    public void setFirstTimeStr(String firstTimeStr) {
        this.firstTimeStr = firstTimeStr;
    }

    /**
     * 
     * @param happenTimes
     */
    public void setHappenTimes(Integer happenTimes) {
        this.happenTimes = happenTimes;
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
     * @param lastTime
     */
    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 
     * @param levelId
     */
    public void setLevelId(Byte levelId) {
        this.levelId = levelId;
    }

    /**
     * 
     * @param levelName
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
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
     * @param sound
     */
    public void setSound(String sound) {
        this.sound = sound;
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
     * @param status
     */
    public void setStatus(Byte status) {
        this.status = status;
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
     * @return the clearTime
     */
    public Timestamp getClearTime() {
        return clearTime;
    }

    /**
     * @param clearTime
     *            the clearTime to set
     */
    public void setClearTime(Timestamp clearTime) {
        this.clearTime = clearTime;
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

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public String getDtStr() {
        if (lastTime != null) {
            return DateUtils.format(this.lastTime);
        }
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Alert [alertId=");
        builder.append(alertId);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", levelId=");
        builder.append(levelId);
        builder.append(", monitorId=");
        builder.append(monitorId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", host=");
        builder.append(host);
        builder.append(", source=");
        builder.append(source);
        builder.append(", message=");
        builder.append(message);
        builder.append(", firstTime=");
        builder.append(firstTime);
        builder.append(", lastTime=");
        builder.append(lastTime);
        builder.append(", happenTimes=");
        builder.append(happenTimes);
        builder.append(", confirmUser=");
        builder.append(confirmUser);
        builder.append(", confirmTime=");
        builder.append(confirmTime);
        builder.append(", clearTime=");
        builder.append(clearTime);
        builder.append(", confirmMessage=");
        builder.append(confirmMessage);
        builder.append(", status=");
        builder.append(status);
        builder.append(", extend1=");
        builder.append(extend1);
        builder.append(", firstTimeStr=");
        builder.append(firstTimeStr);
        builder.append(", confirmTimeStr=");
        builder.append(confirmTimeStr);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", levelName=");
        builder.append(levelName);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", sound=");
        builder.append(sound);
        builder.append(", userObject=");
        builder.append(userObject);
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
     * 
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
     * Add by Rod @EMS-4379 采用XML方式-对象的序列化
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    /**
     * @return the clearUser
     */
    public String getClearUser() {
        return clearUser;
    }

    /**
     * @param clearUser
     *            the clearUser to set
     */
    public void setClearUser(String clearUser) {
        this.clearUser = clearUser;
    }

    /**
     * @return the clearMessage
     */
    public String getClearMessage() {
        return clearMessage;
    }

    /**
     * @param clearMessage
     *            the clearMessage to set
     */
    public void setClearMessage(String clearMessage) {
        this.clearMessage = clearMessage;
    }

    /**
     * @return the clearTimeStr
     */
    public String getClearTimeStr() {
        if (clearTimeStr == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (clearTime != null) {
                clearTimeStr = sdf.format(clearTime);
            }
        }
        return clearTimeStr;
    }

    /**
     * @param clearTimeStr
     *            the clearTimeStr to set
     */
    public void setClearTimeStr(String clearTimeStr) {
        this.clearTimeStr = clearTimeStr;
    }

    public EventSource getSourceObject() {
        return sourceObject;
    }

    public void setSourceObject(EventSource sourceObject) {
        this.sourceObject = sourceObject;
    }

}
