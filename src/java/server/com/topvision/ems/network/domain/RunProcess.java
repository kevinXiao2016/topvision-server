package com.topvision.ems.network.domain;

import java.text.NumberFormat;

import com.topvision.framework.domain.BaseEntity;

public class RunProcess extends BaseEntity {
    private static final long serialVersionUID = 2519582684230911339L;
    private Integer pid;
    private String name;
    private String productId;
    private String path;
    private String parameters;
    private Byte type;
    private Byte status;
    private Long cpuUsage;
    private Long memUsage;

    /**
     * @return the cpuUsage
     */
    public Long getCpuUsage() {
        return cpuUsage;
    }

    /**
     * @return the cpuUsage
     */
    public String getCpuUsageString() {
        StringBuilder usage = new StringBuilder();
        if (cpuUsage < 1000) {
            usage.append(cpuUsage).append("Ms");
        } else if (cpuUsage < 60000) {
            usage.append(cpuUsage / 1000.0).append("S");
        } else if (cpuUsage < 3600000) {
            usage.append(cpuUsage / 60000).append("M").append((cpuUsage % 60000) / 1000).append("s");
        } else if (cpuUsage < 216000000) {
            usage.append(cpuUsage / 3600000).append("H").append((cpuUsage % 3600000) / 60000).append("M");
        } else {
            usage.append(cpuUsage / 5184000000L).append("D").append((cpuUsage % 5184000000L) / 3600000).append("H");
        }
        return usage.toString();
    }

    /**
     * @return the memUsage
     */
    public Long getMemUsage() {
        return memUsage;
    }

    /**
     * @return the memUsage
     */
    public String getMemUsageString() {
        NumberFormat format = NumberFormat.getNumberInstance();
        StringBuilder memUsageStr = new StringBuilder();
        if (memUsage < 1024) {
            memUsageStr.append(format.format(memUsage)).append("B");
        } else if (memUsage < 1048576) {
            memUsageStr.append(format.format(memUsage / 1024.0)).append("KB");
        } else if (memUsage < 1073741824) {
            memUsageStr.append(format.format(memUsage / 1048576.0)).append("MB");
        } else {
            memUsageStr.append(format.format(memUsage / 1073741824.0)).append("GB");
        }
        return memUsageStr.toString();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return the status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @return the type
     */
    public Byte getType() {
        return type;
    }

    /**
     * @param cpuUsage
     *            the cpuUsage to set
     */
    public void setCpuUsage(Long cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    /**
     * @param memUsage
     *            the memUsage to set
     */
    public void setMemUsage(Long memUsage) {
        this.memUsage = memUsage;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param parameters
     *            the parameters to set
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @param pid
     *            the pid to set
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Byte type) {
        this.type = type;
    }
}
