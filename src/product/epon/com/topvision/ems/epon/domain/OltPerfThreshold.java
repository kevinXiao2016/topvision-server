/***********************************************************************
 * $ PerfThreshold.java,v1.0 2011-11-20 17:28:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.ems.epon.performance.domain.PerfThresholdPort;
import com.topvision.ems.epon.performance.domain.PerfThresholdTemperature;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-20-17:28:14
 */
public class OltPerfThreshold implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * sni
     */
    public static String SNI = "SNI";
    /**
     * pon
     */
    public static String PON = "PON";
    /**
     * onupon
     */
    public static String ONUPON = "ONUPON";
    /**
     * uni
     */
    public static String ONUUNI = "ONUUNI";
    /**
     * 温度
     */
    public static String TEMPERATURE = "TEMPERATURE";

    private Long entityId;
    private String thresholdType;
    /**
     * 阈值index
     */
    private Integer perfThresholdTypeIndex;
    /**
     * 阈值名称
     */
    private String perfName;
    /**
     * 作用对象类型
     */
    private Integer perfThresholdObject;
    /**
     * 阈值上限
     */
    private String perfThresholdUpper;
    /**
     * 阈值下限
     */
    private String perfThresholdLower;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }

    public String getPerfThresholdLower() {
        return perfThresholdLower;
    }

    public void setPerfThresholdLower(String perfThresholdLower) {
        this.perfThresholdLower = perfThresholdLower;
    }

    public Integer getPerfThresholdObject() {
        return perfThresholdObject;
    }

    public void setPerfThresholdObject(Integer perfThresholdObject) {
        this.perfThresholdObject = perfThresholdObject;
    }

    public Integer getPerfThresholdTypeIndex() {
        return perfThresholdTypeIndex;
    }

    public void setPerfThresholdTypeIndex(Integer perfThresholdTypeIndex) {
        this.perfThresholdTypeIndex = perfThresholdTypeIndex;
        // this.perfName = OltPerf.getPerfNameById(perfThresholdTypeIndex);
    }

    public String getPerfThresholdUpper() {
        return perfThresholdUpper;
    }

    public void setPerfThresholdUpper(String perfThresholdUpper) {
        this.perfThresholdUpper = perfThresholdUpper;
    }

    public String getPerfName() {
        return perfName;
    }

    public void setPerfName(String perfName) {
        this.perfName = perfName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPerfThreshold");
        sb.append("{entityId=").append(entityId);
        sb.append(", thresholdType='").append(thresholdType).append('\'');
        sb.append(", perfThresholdTypeIndex=").append(perfThresholdTypeIndex);
        sb.append(", perfThresholdObject=").append(perfThresholdObject);
        sb.append(", perfThresholdUpper=").append(perfThresholdUpper);
        sb.append(", perfThresholdLower=").append(perfThresholdLower);
        sb.append('}');
        return sb.toString();
    }

    public PerfThresholdPort createPerfThresholdPort() {
        PerfThresholdPort perfThresholdPort = new PerfThresholdPort();
        perfThresholdPort.setTopPerfThresholdTypeIndex(perfThresholdTypeIndex);
        perfThresholdPort.setTopPerfThresholdObject(perfThresholdObject);
        perfThresholdPort.setTopPerfThresholdUpper(perfThresholdUpper);
        perfThresholdPort.setTopPerfThresholdLower(perfThresholdLower);
        return perfThresholdPort;
    }

    public PerfThresholdTemperature createPerfThresholdTemperature() {
        PerfThresholdTemperature perfThresholdTemperature = new PerfThresholdTemperature();
        perfThresholdTemperature.setTopPerfTemperatureThresholdTypeIdx(perfThresholdTypeIndex);
        perfThresholdTemperature.setTopPerfTemperatureThresholdObject(perfThresholdObject);
        perfThresholdTemperature.setTopPerfTemperatureThresholdUpper(Long.parseLong(perfThresholdUpper));
        perfThresholdTemperature.setTopPerfTemperatureThresholdLower(Long.parseLong(perfThresholdLower));
        return perfThresholdTemperature;
    }

    public static OltPerfThreshold createOltPerfThreshold(PerfThresholdPort perfThresholdPort) {
        OltPerfThreshold oltPerfThreshold = new OltPerfThreshold();
        switch (perfThresholdPort.getTopPerfThresholdObject()) {
        case 1: {
            oltPerfThreshold.setThresholdType(OltPerfThreshold.SNI);
            break;
        }
        case 2: {
            oltPerfThreshold.setThresholdType(OltPerfThreshold.PON);
            break;
        }
        case 3: {
            oltPerfThreshold.setThresholdType(OltPerfThreshold.ONUPON);
            break;
        }
        case 4: {
            oltPerfThreshold.setThresholdType(OltPerfThreshold.ONUUNI);
            break;
        }
        }
        oltPerfThreshold.setPerfThresholdTypeIndex(perfThresholdPort.getTopPerfThresholdTypeIndex());
        oltPerfThreshold.setPerfThresholdObject(perfThresholdPort.getTopPerfThresholdObject());
        oltPerfThreshold.setPerfThresholdUpper(perfThresholdPort.getTopPerfThresholdUpper());
        oltPerfThreshold.setPerfThresholdLower(perfThresholdPort.getTopPerfThresholdLower());
        return oltPerfThreshold;
    }

    public static OltPerfThreshold createOltPerfThreshold(PerfThresholdTemperature perfThresholdTemperature) {
        OltPerfThreshold oltPerfThreshold = new OltPerfThreshold();
        oltPerfThreshold.setThresholdType(OltPerfThreshold.TEMPERATURE);
        oltPerfThreshold.setPerfThresholdTypeIndex(perfThresholdTemperature.getTopPerfTemperatureThresholdTypeIdx());
        oltPerfThreshold.setPerfThresholdObject(perfThresholdTemperature.getTopPerfTemperatureThresholdObject());
        oltPerfThreshold.setPerfThresholdUpper(perfThresholdTemperature.getTopPerfTemperatureThresholdUpper()
                .toString());
        oltPerfThreshold.setPerfThresholdLower(perfThresholdTemperature.getTopPerfTemperatureThresholdLower()
                .toString());
        return oltPerfThreshold;
    }
}