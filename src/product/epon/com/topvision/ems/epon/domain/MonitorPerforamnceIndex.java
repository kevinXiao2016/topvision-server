package com.topvision.ems.epon.domain;

import java.lang.reflect.Field;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 监视器性能指标轮询Domain
 * 
 * @author Bravin
 * 
 */
@Alias(value = "performance")
public class MonitorPerforamnceIndex implements AliasesSuperType {
    private static final long serialVersionUID = 5186502105097925178L;
    private Long entityId;
    private String entityIp;
    private String portIndex;
    private String Index;
    private Long value;

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
     * @return the portIndex
     */
    public String getPortIndex() {
        return portIndex;
    }

    /**
     * @param portIndex
     *            the portIndex to set
     */
    public void setPortIndex(Long portIndex) {
        this.portIndex = this.indexToMark(portIndex);
    }

    /**
     * long型的port轉成 slot/port/onu/uni的格式
     * 
     * @param idx
     * @return
     */
    public String indexToMark(Long idx) {
        long slot = (idx.longValue() & 0xFF00000000L) >> 32;
        long port = (idx.longValue() & 0x00FF000000L) >> 24;
        long onu = (idx.longValue() & 0x0000FF0000L) >> 16;
        long uni = (idx.longValue() & 0x000000FF00L) >> 8;
        return uni != 0 ? slot + "/" + port + ":" + onu + ":" + uni : onu != 0 ? slot + "/" + port + ":" + onu
                : port != 0 ? slot + "/" + port : "" + slot;
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return Index;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(String index) {
        Index = index;
    }

    /**
     * @return the value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Long value) {
        this.value = value;
    }

    /**
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @param entityIp
     *            the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /**
     * Deprecated
     * 
     * @param index2
     * @param o
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public Long getPerformance(String index2, MonitorPerforamnceIndex o) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        @SuppressWarnings("rawtypes")
        Class clazz = MonitorPerforamnceIndex.class;
        Field field = clazz.getDeclaredField("");
        field.get(o);
        return field.getLong(o);
    }

}
