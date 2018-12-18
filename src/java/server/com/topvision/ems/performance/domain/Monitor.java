package com.topvision.ems.performance.domain;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class Monitor extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -1266386851110913747L;
    protected transient Logger logger = LoggerFactory.getLogger(getClass());
    public static final char START_STATUS = '1';
    public static final char STOP_STATUS = '0';
    private Long monitorId = 0L;
    private String name = "";
    private String note = "";
    private String category = "";
    private Boolean enabled = Boolean.TRUE;
    private Boolean available = Boolean.TRUE;
    private Boolean healthy = Boolean.TRUE;
    private Long entityId = -1L;
    private String ip = "";
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());
    private Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
    private byte[] content;
    private String reason = "";
    private Long intervalOfNormal = 300000L;
    private Long intervalAfterError = 300000L;
    // Modify by Rod
    // private Timestamp lastCollectTime = new Timestamp(System.currentTimeMillis());
    private Timestamp lastCollectTime;
    private Long dependMonitor = -1L;
    private String icon;
    private String jobClass;
    // Add by Rod 默认为0,数据库不保存，在启动系统时，所有的任务不延时开启，在新建设备的时候可以进行设置，实现延时开启的功能
    private Long intervalStart = 0L;

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @return the dependMonitor
     */
    public Long getDependMonitor() {
        return dependMonitor;
    }

    /**
     * @return the monitorId
     */
    public Long getMonitorId() {
        return monitorId;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the intervalAfterError
     */
    public Long getIntervalAfterError() {
        return intervalAfterError;
    }

    /**
     * @return the intervalOfNormal
     */
    public Long getIntervalOfNormal() {
        return intervalOfNormal;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the jobClass
     */
    public String getJobClass() {
        return jobClass;
    }

    /**
     * @return the lastCollectTime
     */
    public Timestamp getLastCollectTime() {
        return lastCollectTime;
    }

    /**
     * @return the modifyTime
     */
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the content of object
     */
    public Object getObjectContent() {
        ByteArrayInputStream bais = null;
        XMLDecoder de = null;
        Object obj = null;
        try {
            bais = new ByteArrayInputStream(content);
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
            if (content != null) {
                return new String(content);
            } else {
                return null;
            }
        }
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return the available
     */
    public Boolean isAvailable() {
        return available;
    }

    /**
     * @return the enabled
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the healthy
     */
    public Boolean isHealthy() {
        return healthy;
    }

    /**
     * @param available
     *            the available to set
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(Object obj) {
        if (obj == null) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder en = new XMLEncoder(baos);
        try {
            en.writeObject(obj);
            en.flush();
            baos.flush();
            en.close();
            baos.close();
            this.content = baos.toByteArray();
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
            this.content = obj.toString().getBytes();
        }
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @param dependMonitor
     *            the dependMonitor to set
     */
    public void setDependMonitor(Long dependMonitor) {
        this.dependMonitor = dependMonitor;
    }

    /**
     * @param monitorId
     *            the monitorId to set
     */
    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    /**
     * @param enabled
     *            the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param healthy
     *            the healthy to set
     */
    public void setHealthy(Boolean healthy) {
        this.healthy = healthy;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param intervalAfterError
     *            the intervalAfterError to set
     */
    public void setIntervalAfterError(Long intervalAfterError) {
        this.intervalAfterError = intervalAfterError;
    }

    /**
     * @param intervalOfNormal
     *            the intervalOfNormal to set
     */
    public void setIntervalOfNormal(Long intervalOfNormal) {
        this.intervalOfNormal = intervalOfNormal;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param jobClass
     *            the jobClass to set
     */
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    /**
     * @param lastCollectTime
     *            the lastCollectTime to set
     */
    public void setLastCollectTime(Timestamp lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
    }

    /**
     * @param modifyTime
     *            the modifyTime to set
     */
    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the intervalStart
     */
    public Long getIntervalStart() {
        return intervalStart;
    }

    /**
     * @param intervalStart
     *            the intervalStart to set
     */
    public void setIntervalStart(Long intervalStart) {
        this.intervalStart = intervalStart;
    }

}
