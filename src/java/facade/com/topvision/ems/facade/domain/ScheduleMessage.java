/***********************************************************************
 * $ PerfCollectMessage.java,v1.0 2012-5-2 10:50:57 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created @2012-5-2-10:50:57
 */
public class ScheduleMessage<T extends OperClass> implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    public static int INIT = -1;
    public static int START = 0;
    public static int STOP = 1;
    public static int RESTART = 2;
    public static int INSERT = 3;
    public static int DELETE = 4;
    public static int UPDATE_START = 5;
    public static int UPDATE_SHUTDOWN = 6;
    public static int IGNORE = 65535;

    // 为采集而定义的一个DOMAIN 必须继承OperClass
    private T domain;
    // 消息规定的动作 只可能是取 START 0，STOP 1，RESTART 2
    private int action = INIT;
    // 消息规定的调度初始间隔时间
    private long initialDelay;
    // 消息规定的调度轮询间隔时间
    private long period;
    // 新建一个调度的时候生成的数据库唯一ID
    private long monitorId;
    // 监视器类型
    private String category;
    // 采集的设备ID
    private long identifyKey;
    // 采集使用的参数
    private SnmpParam snmpParam;
    // 采集使用的对象(oid or domian)
    private Integer scheduleType;
    // 是否随着服务启动线程(0表示否，1表示是)
    private Integer isStartUpWithServer;
    // 指标名称
    private String targetName;
    // 指标对应数据
    private Object data;
    // 修改标志
    private boolean updateFlag = false;

    private String domainString;
    private String snmpParamString;

    // Use for AdminAction
    private String createTime;
    private String lastCollectTime;
    private String entityName;
    private String entityIp;
    private String entityCreateTime;
    private Integer engineId;
    private String engineName;
    private String entityTypeName;

    public ScheduleMessage() {
    }

    public ScheduleMessage(String category, long identifyKey, long period) {
        this.category = category;
        this.identifyKey = identifyKey;
        this.period = period;
    }

    // Add by Victor@20160826增加实时队列，解决性能阻塞频谱的实时性问题
    // 目前只支持CC_CCSPECTRUM噪声频谱
    public boolean isRealtime() {
        return "CC_CCSPECTRUM".equals(category);
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public T getDomain() {
        // 对从数据库中查询出来的domainString反序列化使用延迟加载,避免每次查询完后在setDomainString方法中进行
        if (domain == null && domainString != null) {
            try {
                this.domain = (T) makeObjectFromString(domainString);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return domain;
    }

    public void setDomain(T domain) {
        this.domain = domain;
    }

    public String getDomainString() throws IOException {
        return makeObjectString(domain);
    }

    public void setDomainString(String domainString) throws IOException {
        // this.domain = (T) makeObjectFromString(domainString);
        this.domainString = domainString;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(long monitorId) {
        this.monitorId = monitorId;
    }

    public long getIdentifyKey() {
        return identifyKey;
    }

    public void setIdentifyKey(long identifyKey) {
        this.identifyKey = identifyKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SnmpParam getSnmpParam() {
        if (snmpParam == null && snmpParamString != null) {
            try {
                this.snmpParam = (SnmpParam) makeObjectFromString(snmpParamString);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public String getSnmpParamString() throws IOException {
        return makeObjectString(snmpParam);
    }

    public void setSnmpParamString(String snmpParamString) throws IOException {
        // this.snmpParam = (SnmpParam) makeObjectFromString(snmpParamString);
        this.snmpParamString = snmpParamString;
    }

    /**
     * @return the scheduleType
     */
    public Integer getScheduleType() {
        return scheduleType;
    }

    /**
     * @param scheduleType
     *            the scheduleType to set
     */
    public void setScheduleType(Integer scheduleType) {
        this.scheduleType = scheduleType;
    }

    private String makeObjectString(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        XMLEncoder xmlEncoder = null;
        BufferedReader reader = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            xmlEncoder = new XMLEncoder(byteArrayOutputStream);
            xmlEncoder.writeObject(obj);
            xmlEncoder.close();
            reader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
            String s;
            StringBuilder xml = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                xml.append(s);
                xml.append("\n");
            }
            return xml.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (xmlEncoder != null) {
                xmlEncoder.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private Object makeObjectFromString(String xml) throws IOException {
        Object re = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLDecoder xmlDecoder = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
            writer.write(xml);
            writer.close();
            xmlDecoder = new XMLDecoder(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            re = xmlDecoder.readObject();
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (xmlDecoder != null) {
                xmlDecoder.close();
            }
            byteArrayOutputStream.close();
        }
        return re;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + (int) (identifyKey ^ (identifyKey >>> 32));
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        try {
            ScheduleMessage schedule = (ScheduleMessage) obj;
            if (schedule.getCategory().equals(this.getCategory()) && schedule.getIdentifyKey() == this.getIdentifyKey()
                    && schedule.getPeriod() == this.getPeriod()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return the isStartUpWithServer
     */
    public Integer getIsStartUpWithServer() {
        return isStartUpWithServer;
    }

    /**
     * @param isStartUpWithServer
     *            the isStartUpWithServer to set
     */
    public void setIsStartUpWithServer(Integer isStartUpWithServer) {
        this.isStartUpWithServer = isStartUpWithServer;
    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName
     *            the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return the updateFlag
     */
    public boolean isUpdateFlag() {
        return updateFlag;
    }

    /**
     * @param updateFlag
     *            the updateFlag to set
     */
    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag = updateFlag;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the lastCollectTime
     */
    public String getLastCollectTime() {
        return lastCollectTime;
    }

    /**
     * @param lastCollectTime the lastCollectTime to set
     */
    public void setLastCollectTime(String lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ScheduleMessage [domain=");
        builder.append(domain);
        builder.append(", action=");
        builder.append(action);
        builder.append(", initialDelay=");
        builder.append(initialDelay);
        builder.append(", period=");
        builder.append(period);
        builder.append(", monitorId=");
        builder.append(monitorId);
        builder.append(", category=");
        builder.append(category);
        builder.append(", identifyKey=");
        builder.append(identifyKey);
        builder.append(", snmpParam=");
        builder.append(snmpParam);
        builder.append(", scheduleType=");
        builder.append(scheduleType);
        builder.append(", isStartUpWithServer=");
        builder.append(isStartUpWithServer);
        builder.append("]");
        return builder.toString();
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityCreateTime() {
        return entityCreateTime;
    }

    public void setEntityCreateTime(String entityCreateTime) {
        this.entityCreateTime = entityCreateTime;
    }

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }
}
